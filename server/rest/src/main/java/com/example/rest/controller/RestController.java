package com.example.rest.controller;

import com.example.model.*;
import com.example.rest.exceptions.*;
import com.example.util.*;
import com.fasterxml.jackson.databind.*;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static com.example.model.PlayerData.State.*;

@org.springframework.web.bind.annotation.RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RestController {

    Logger logger = LoggerFactory.getLogger(RestController.class);
    private static final GamesController gamesController;
    private static final PlayersController playersController;

    static {
        playersController = PlayersController.getInstance();
        gamesController = GamesController.getInstance();
    }


    /* createPlayer
     *
     * accepts a name
     * returns a Player containing created ids for:
     * PrivateID set for a player's own use with the server
     * PublicID set for opponents to refer to the player
     */
    @PostMapping(path = "/player", produces = "application/json; charset=UTF-8")
    public TemplePlayerData createPlayer(
            @RequestParam(value = "name", defaultValue = "") String name) throws Exception {
        if(name == null || name.equals("")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player Not Found");
        }

        TemplePlayerData newPlayer = null;
        try {
            Integer ID = playersController.createPlayer(name);
            newPlayer = (TemplePlayerData) playersController.getPlayer(ID);

        } catch (InvalidPlayerDataException ex) {
            RequestLogging.logInvalidPlayer(logger, ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player Not Found", ex);
        }
        newPlayer.setState(PLAYER_CREATED);
        logger.info("Created player: " + newPlayer);
        return newPlayer;
    }


    /* createGame
     *
     * creates and returns a GameInstance
     * accepts a Player's gameID. If the gameID is in the playersMap,
     * Get the player, create a new game, and set the player as game owner.
     */
    @PostMapping("/game")
    public GameInstance createGame(@RequestBody JsonNode playerID
    ) throws Exception {

        GameInstance game;
        try {
            JsonNode publicIDNode = playerID.get("publicID");

            if (!publicIDNode.isInt()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player ID must be a number");
            }
            int playerPublicID = publicIDNode.asInt();

            JsonNode gameNameNode = playerID.get("gameName");

            String gameName;

            if (gameNameNode != null) {
                gameName = gameNameNode.asText();
            } else {
                gameName = GameDesignVars.DEFAULT_GAME_NAME + " " + playerPublicID;
            }

            TemplePlayerData player = (TemplePlayerData) playersController.getPlayer(playerPublicID);

            int gameID = gamesController.createGame(player);

            game = gamesController.getGame(gameID);

            game.setGameName(gameName);

            game.setGameState(GameInstance.GameState.GAME_LOBBY);

            game.addPlayer(player);

            player.setState(IN_LOBBY);

        } catch (InvalidPlayerDataException ex) {
            RequestLogging.logInvalidPlayer(logger, ex);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player Not Found", ex);
        } catch (InvalidGameInstanceException ex) {
            RequestLogging.logInvalidGame(logger, ex);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game Not Found", ex);
        }

        logger.info("Created game: " + game);
        return game;
    }


    /* joinGame
     *
     * Adds a player to an existing game
     * Accepts a game id that exists in the games' controller.
     * Accepts a player containing a public id, private id and name.
     * returns the modified GameInstance
     */
    @PostMapping("/game/{gameID}")
    public GameInstance joinGame(
            @PathVariable Integer gameID,
            @RequestBody JsonNode playerID) throws Exception {

        GameInstance game = null;

        try {

            JsonNode publicIDNode = playerID.get("publicID");

            int publicID;
            if (publicIDNode.isInt()) {
                publicID = playerID.get("publicID").asInt();

            } else {
                publicID = Integer.parseInt(String.valueOf(playerID.get("publicID")));
            }

            game = gamesController.getGame(gameID);

            if (!game.isFull()) {
                TemplePlayerData joiningPlayer = (TemplePlayerData) playersController.getPlayer(publicID);
                game.addPlayer(joiningPlayer);
                joiningPlayer.setState(IN_LOBBY);
                logger.info("Added playerID: {} to gameID: {}", joiningPlayer.getPublicID(), game.getGameID());
            }

        } catch (InvalidPlayerDataException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player Not Found", ex);
        } catch (InvalidGameInstanceException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game Not Found", ex);
        } catch (Exception e) {
            RequestLogging.logInvalidRequest(logger, e);
        }

        return game;
    }


    /* leaveGame
     *
     * remove a player with playerID from a GameInstance with gameID.
     * if the GameInstance doesn't have any players remaining, destroy the GameInstance
     *
     * if successful return all games (since leaving a game returns to the Multiplayer Menu that lists games)
     */
    @DeleteMapping("/game/{gameID}/{playerID}")
    public Collection<GameInstance> leaveGame(@PathVariable Integer gameID, @PathVariable Integer playerID) {

        try {
            GameInstance game = gamesController.getGame(gameID);

            if (game.getPlayers().size() <= 1) {
                playersController.removePlayer(playerID);
                gamesController.removeGame(gameID);
            }

            else {
                TemplePlayerData player = (TemplePlayerData) game.getPlayer(playerID);
                playersController.removePlayer(playerID);
                gamesController.removePlayer(gameID, playerID);
                if (player.isGameOwner()) {

                    ((TemplePlayerData) new ArrayList<>(game
                            .getPlayers()
                            .values())
                            .get(0))
                            .setIsGameOwner(true);
                }
            }

        } catch (InvalidGameInstanceException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game not found", ex);
        }

        return gamesController.getAllGames();
    }


    // owner of the room clicks Start Game button or Game Ends, etc
    // 		(for this first iteration, everyone in the lobby has Start Game privileges,
    //		since we're skipping the CreateGame step and there's only one GameID to JoinGame)
    //
    @PutMapping("/game/{gameID}")
    public GameInstance updateGameState(@PathVariable Integer gameID, @RequestBody GameInstance.GameState state) throws Exception {
        if (gameID == 0) {
            throw new Exception("InvalidGameIdException");
        }
        if (state == null) {
            throw new Exception("Invalid");
        }

        GameInstance.GameState gameState;
        PlayerData.State playerState;

        switch(state) {
            case GAME_LOBBY:
                gameState = GameInstance.GameState.GAME_LOBBY;
                playerState = PlayerData.State.IN_LOBBY;
                break;
            case GAME_STARTING:
                gameState = GameInstance.GameState.GAME_STARTING;
                playerState = PlayerData.State.IN_LOBBY;
                break;
            case ENTERING_GAME:
                gameState = GameInstance.GameState.ENTERING_GAME;
                playerState = PlayerData.State.ENTERING_GAME;
                break;
            case IN_GAME:
                gameState = GameInstance.GameState.IN_GAME;
                playerState = PlayerData.State.IN_GAME;
                break;
            case CLOSING:
                gameState = GameInstance.GameState.CLOSING;
                playerState = PlayerData.State.LEAVING_GAME;
                break;
            default:
                throw new Exception("InvalidStateException");
        }


        GameInstance game = gamesController.getGame(gameID);
        game.setGameState(gameState);

        // different game states will change Player's state as appropriate
        for (PlayerData player : game.getPlayers().values()) {
            player.setState(playerState);
        }

        return game;
    }

//    @RequestMapping(value = "/game/{gameID}/player/{playerID}", method=RequestMethod.PUT)
//    public ResponseEntity<?> getSomething(@PathVariable("gameID") String gameID, @PathVariable("playerID") String playerID) {
//
//    }

    @PutMapping("/game/{gameID}/player/{playerID}")
    public ResponseEntity<?> updateGameName(@PathVariable("gameID") Integer gameID, @PathVariable("playerID") Integer playerID, @RequestBody String gameName) {
        if(gameID == null || gameID == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game Not Found");
        }
        TemplePlayerData player = (TemplePlayerData) playersController.getPlayer(playerID);
        if(!player.isGameOwner()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN, HttpStatus.valueOf("Only Game owner can change game name"));
        }
        GameInstance game = null;
        try {
            game = gamesController.getGame(gameID);
            game.setGameName(gameName);
        } catch (InvalidGameInstanceException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED, HttpStatus.valueOf("New game name: " + game.getGameName()));
    }


    // Accepts a game id, sets player and game state to entering game.
    // returns a GameInstance
    @PutMapping("/startGame/{gameID}")
    public GameInstance startGame(@PathVariable Integer gameID) throws Exception {

        GameInstance game;
        try {
            game = gamesController.getGame(gameID);

            game.setGameState(GameInstance.GameState.ENTERING_GAME);

            if (game.getPlayers().size() <= 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Minimum players to start a game is 2");
            }

            for (PlayerData player : game.getPlayers().values()) {
                player.setState(PlayerData.State.ENTERING_GAME);
            }
        } catch (InvalidGameInstanceException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game Not Found", ex);
        }

        return game;
    }


    /* updatePlayer
     *
     * updates a player's data for the game with gameID.
     *
     * if successful return GameInstance - i.e. latest updated data
     */
    @PutMapping("/player/{gameID}")
    public GameInstance updatePlayer(@PathVariable Integer gameID, @RequestBody TemplePlayerData player) throws Exception {

        GameInstance game;
        PlayerData playerData;
        try {
            game = gamesController.getGame(gameID);

            playerData = playersController.getPlayer(player.getPublicID());
            // update a player's name and state
            if(player.getName() != null) {
                playerData.setName(player.getName());
            }
            if(player.getState() != null) {
                playerData.setState(player.getState());
            }
            // update public/private keys?
            playersController.updatePlayer(playerData);

        } catch (InvalidGameInstanceException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game Not Found", ex);
        } catch (InvalidPlayerDataException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player Not Found", ex);
        }
        playerData.setName(player.getName());
        return game;
    }


    //===================================================================================================
    //                          GET MAPPING
    //===================================================================================================

    @GetMapping("/game")
    public Collection<GameInstance> getAllGames() throws Exception {
        if (gamesController.getAllGames() == null) {
            throw new Exception("Empty collection of games");
        }
        return gamesController.getAllGames();
    }


    @GetMapping("/game/{id}")
    public GameInstance getGame(@PathVariable Integer id) throws Exception {

        GameInstance game;
        try {
            game = gamesController.getGame(id);

        } catch(InvalidGameInstanceException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game Not Found", ex);
        }

        return game;
    }


    /*
     * gets all players
     */
    @GetMapping("/player")
    public Collection<PlayerData> getAllPlayers() {
        return playersController.getAllPlayers();
    }

    /* getPlayer
     *
     * gets player in specified game
     */
    @GetMapping("/game/{gameID}/player/{playerID}")
    public PlayerData getPlayerInGame(@PathVariable Integer gameID,
                                      @RequestParam(value = "playerID") Integer playerID) throws Exception {

        GameInstance game;
        TemplePlayerData player;
        try{
            game = gamesController.getGame(gameID);

            player = (TemplePlayerData) game.getPlayer(playerID);

        } catch(InvalidGameInstanceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game Instance Not Found");
        }
        logger.info("getting {}", player);
        return player;
    }


    /* getPlayer
     *
     * gets player identified by id passed in
     */
    @GetMapping("/player/{id}")
    public PlayerData getPlayer(@RequestParam(value = "id", defaultValue = "0") Integer id) throws Exception {

        if (id == null || id == 0) {
            throw new Exception("NullPlayerIdException");
        }

        return playersController.getPlayer(id);
    }


    @GetMapping("/testlogging")
    public String testLogging() {
        if (logger.isWarnEnabled()) {
            logger.warn("A WARN Message");
        }
        if (logger.isErrorEnabled()) {
            logger.error("An ERROR Message");
        }
        if (logger.isInfoEnabled()) {
            logger.info("An INFO Message");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("A DEBUG Message");
        }

        if (logger.isTraceEnabled()) {
            logger.trace("A TRACE Message");
        }
        return "test logger";
    }
    @GetMapping("/health")
    public String heartBeat() {
        StringBuilder sb = new StringBuilder();

        if (gamesController.getAllGames().size() == 0) {
            RequestLogging.logNoActiveGames(logger, sb);
        }

        else {
            GameInstance game = new ArrayList<>(gamesController.getAllGames()).get(0);
            sb.append("\nGame gameID: ").append(game.getGameID())
                    .append("\nGame Name: ").append(game.gameName)
                    .append("\nGame State: ").append(game.getGameState())
                    .append("\nNumber of Players: ").append(game.getPlayers().size())
                    .append("\nPublic Player Ids: ").append(game.getPlayers().keySet());
        }
        logger.info(sb.toString());
        return sb.toString();
    }

}
                                                                                                                                                                        