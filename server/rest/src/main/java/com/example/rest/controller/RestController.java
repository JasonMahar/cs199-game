package com.example.rest.controller;

import com.example.model.GameDesignVars;
import com.example.model.GameInstance;
import com.example.model.PlayerData;
import com.example.model.TemplePlayerData;
import com.example.rest.exceptions.InvalidGameInstanceException;
import com.example.rest.exceptions.InvalidPlayerDataException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.example.model.PlayerData.State.IN_LOBBY;
import static com.example.model.PlayerData.State.PLAYER_CREATED;

@org.springframework.web.bind.annotation.RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RestController {

    private static final Logger logger = LoggerFactory.getLogger(RestController.class);
    private static final GamesController gamesController;
    private static final PlayersController playersController;

    static {
        playersController = PlayersController.getInstance();
        gamesController = GamesController.getInstance();
    }

    /* createPlayer
     *
     * accepts a name
     * returns a new TemplePlayerData object
     */
    @PostMapping(path = "/player", produces = "application/json; charset=UTF-8")
    public TemplePlayerData createPlayer(@RequestParam(value = "name", defaultValue = "") String name) throws Exception {

        if(name == null || name.equals("")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player Not Found");
        }

        TemplePlayerData newPlayer = null;
        try {
            Integer ID = playersController.createPlayer(name);
            newPlayer = (TemplePlayerData) playersController.getPlayer(ID);

        } catch (InvalidPlayerDataException ex) {
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
    @PostMapping("/game/player/{playerID}")
    public GameInstance createGame(@PathVariable(value = "playerID") Integer playerID) throws Exception {

        logger.info("input: " + playerID);
        GameInstance game;
        try {

            int playerPublicID = playerID;

            String gameName = GameDesignVars.DEFAULT_GAME_NAME + " " + playerPublicID;

            TemplePlayerData player = (TemplePlayerData) playersController.getPlayer(playerPublicID);

            int gameID = gamesController.createGame(player);

            game = gamesController.getGame(gameID);

            game.setGameName(gameName);

            game.setGameState(GameInstance.GameState.GAME_LOBBY);

            game.addPlayer(player);

            player.setState(IN_LOBBY);

        } catch (InvalidPlayerDataException ex) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player Not Found", ex);

        } catch (InvalidGameInstanceException ex) {

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
    @PostMapping("/game/{gameID}/player/{playerID}")
    public GameInstance joinGame(
            @PathVariable Integer gameID,
            @PathVariable Integer playerID) throws Exception {

        GameInstance game = null;

        try {

            int publicID = playerID;

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
                game = null;
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
        } catch (InvalidPlayerDataException e) {
            throw new RuntimeException(e);
        }

        return gamesController.getAllGames();
    }

    /* updateGameState
     *
     * remove a player with playerID from a GameInstance with gameID.
     * if the GameInstance doesn't have any players remaining, destroy the GameInstance
     *
     * returns
     */
    @PutMapping("/game/{gameID}")
    public GameInstance updateGameState(@PathVariable Integer gameID, @RequestBody GameInstance.GameState state) throws Exception {

        GameInstance.GameState gameState;
        PlayerData.State playerState;

        try {
            switch (state) {
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
        }catch(InvalidPlayerDataException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player not found", ex);
        }catch(InvalidGameInstanceException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game not found", ex);
        }


        GameInstance game = gamesController.getGame(gameID);
        game.setGameState(gameState);

        // different game states will change Player's state as appropriate
        for (PlayerData player : game.getPlayers().values()) {
            player.setState(playerState);
        }

        return game;
    }

    /* updatePlayer
     *
     * accepts
     *  @PathVariable Integer gameID
     *  @RequestBody TemplePlayerData updatedPlayer that contains updated TemplePlayerData fields
     * replaces a player with playerID from a GameInstance with gameID.
     *
     * returns all players in specified game
     */
    @PutMapping("/game/{gameID}/player/{playerID}")
    public Collection<PlayerData> updatePlayer(@PathVariable("gameID") Integer gameID,
                                               @RequestBody TemplePlayerData updatedPlayer) {
        GameInstance game;
        try {
            int playerID = updatedPlayer.getPublicID();

            // get player by playerID
            TemplePlayerData player = (TemplePlayerData) playersController.getPlayer(playerID);

            // check if player is in specified gameID
            game =  gamesController.getGame(gameID);
            game.getPlayer(playerID);

            // update player fields
            if(updatedPlayer.getName() != null) {
                player.setName(updatedPlayer.getName());
            }
            if(updatedPlayer.getState() != null) {
                player.setState(updatedPlayer.getState());
            }
            if(updatedPlayer.getGameName() != null) {
                player.setGameName(updatedPlayer.getGameName());
            }

        } catch (InvalidGameInstanceException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game not found", ex);
        } catch (InvalidPlayerDataException e) {
            throw new RuntimeException(e);
        }

        return game.getPlayers().values();
    }


    /* startGame
     *
     * accepts @PathVariable{gameID} Accepts a game id.
     * updates GameInstance and TemplePlayerData state's to STARTING_GAME
     * returns the GameInstance
     */
    @PutMapping("/startGame/{gameID}")
    public GameInstance startGame(@PathVariable Integer gameID) throws Exception {

        GameInstance game = null;
        try {
            game = gamesController.getGame(gameID);
            if(game.getPlayers().size() >= 2) {

                game.setGameState(GameInstance.GameState.ENTERING_GAME);

                for (PlayerData player : game.getPlayers().values()) {
                    player.setState(PlayerData.State.ENTERING_GAME);
                }

            } else {
                System.out.println("Less than minimum required number of Players to start a game");
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Less than minimum required number of Players to start a game");
            }
        } catch (InvalidGameInstanceException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game Not Found", ex);
        }

        return game;
    }

    //===================================================================================================
    //                          GET MAPPING
    //===================================================================================================

    /* /game
     *
     * returns all games contained in gamesController
     */
    @GetMapping("/game")
    public Collection<GameInstance> getAllGames() throws Exception {

        return gamesController.getAllGames();
    }

    @GetMapping("/gameIDs")
    public Collection<Integer> getAllGameIds()  {

        return gamesController.getAllGames().stream().map(GameInstance::getGameID).collect(Collectors.toList());
    }

    /* /game/{id}
     *
     * accepts @PathVariable Integer id
     * returns the specified GameInstance
     */
    @GetMapping("/game/{gameID}")
    public GameInstance getGame(@PathVariable Integer gameID) throws Exception {

        GameInstance game;
        try {
            game = gamesController.getGame(gameID);

        } catch(InvalidGameInstanceException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game Not Found", ex);
        }

        return game;
    }


    /* getAllPlayers
     *
     * returns all players contained in playersController
     */
    @GetMapping("/player")
    public Collection<PlayerData> getAllPlayers() {
        return playersController.getAllPlayers();
    }

    @GetMapping("/game/{gameID}/players")
    public Collection<Integer> getAllPlayersForGame(@PathVariable Integer gameID) throws InvalidGameInstanceException {

        return gamesController.getGame(gameID).getPlayers().values().stream().map(PlayerData::getPublicID).collect(Collectors.toList());
    }


    /* getPlayer
     *
     * accepts
     * @PathVariable Integer gameID
     * @PathVariable Integer playerID
     * returns specified player in specified game
     */
    @GetMapping("/game/{gameID}/player/{playerID}")
    public PlayerData getPlayer(@PathVariable Integer gameID,
                                @PathVariable(value = "playerID") Integer playerID) throws Exception {

        GameInstance game;
        TemplePlayerData player;
        try{
            game = gamesController.getGame(gameID);
            TemplePlayerData p = (TemplePlayerData) playersController.getPlayer(playerID);
            player = (TemplePlayerData) game.getPlayer(playerID);

        } catch(InvalidGameInstanceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game Instance Not Found");
        }
        return player;
    }

    /* getPlayer
     *
     * accepts @PathVariable Integer playerID
     * returns specified player
     */
    @GetMapping("/player/{playerID}")
    public PlayerData getPlayer(@PathVariable(value = "playerID") Integer playerID) throws Exception {

        return playersController.getPlayer(playerID);
    }

    @GetMapping("/health")
    public String heartBeat() {
        StringBuilder sb = new StringBuilder();

        GameInstance game = new ArrayList<>(gamesController.getAllGames()).get(0);
        sb.append("\nGame gameID: ").append(game.getGameID())
                .append("\nGame Name: ").append(game.gameName)
                .append("\nGame State: ").append(game.getGameState())
                .append("\nNumber of Players: ").append(game.getPlayers().size())
                .append("\nPublic Player Ids: ").append(game.getPlayers().keySet());

        logger.info(sb.toString());
        return sb.toString();
    }

}
