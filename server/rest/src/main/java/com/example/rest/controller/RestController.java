package com.example.rest.controller;

import com.example.model.*;
import com.example.rest.exceptions.*;
import com.example.util.*;
import com.fasterxml.jackson.databind.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.*;

import java.util.*;

import static com.example.model.PlayerData.State.*;

@org.springframework.web.bind.annotation.RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RestController {
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

        TemplePlayerData newPlayer;
        try {
            Integer ID = playersController.createPlayer(name);
            newPlayer = (TemplePlayerData) playersController.getPlayer(ID);

        } catch (InvalidPlayerDataException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player Not Found", ex);
        }
        newPlayer.setState(PLAYER_CREATED);
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
            if(!publicIDNode.isInt()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player gameID must be int");
            }
            
            int publicID = publicIDNode.asInt();
            TemplePlayerData player = (TemplePlayerData) playersController.getPlayer(publicID);

            int gameID = gamesController.createGame(player);

     
            game = gamesController.getGame(gameID);
            game.setGameState(GameInstance.GameState.GAME_LOBBY);

            String gameName = player.getGameName();
            if(gameName != null) {
                game.setGameName(gameName);
            }
            else {
                game.setGameName(GameDesignVars.DEFAULT_GAME_NAME + " " + game.getGameID());
            }

            game.addPlayer(player);
            player.setState(IN_LOBBY);

        } catch (InvalidPlayerDataException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player Not Found", ex);
        } catch (InvalidGameInstanceException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game Not Found", ex);
        }
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

        if (gameID == null) {
            throw new Exception("InvalidGameIdException");
        }

        GameInstance game = null;

        try {

            JsonNode publicIDNode = playerID.get("publicID");
            PrintUtils.green("JsonNode publicIDNode = playerID.get(\"publicID\");");
            if (publicIDNode == null) {
                throw new Exception("MustCreateGameUsingPlayerPublicIDException");
            }

            int publicID;
            if (publicIDNode.isInt()) {
                publicID = playerID.get("publicID").asInt();
                PrintUtils.green("publicID = playerID.get(\"publicID\").asInt()");
            } else {
                publicID = Integer.parseInt(String.valueOf(playerID.get("publicID")));
                PrintUtils.green("Integer.parseInt(String.valueOf(playerID.get(\"publicID\"))");
            }

            game = gamesController.getGame(gameID);
            if (!game.isFull()) {
                TemplePlayerData joiningPlayer = (TemplePlayerData) playersController.getPlayer(publicID);
                game.addPlayer(joiningPlayer);
                joiningPlayer.setState(IN_LOBBY);
            }

        } catch (InvalidPlayerDataException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player Not Found", ex);
        } catch (InvalidGameInstanceException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game Not Found", ex);
        }
        PrintUtils.green("games getPlayers : " + game.getPlayers().size());
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
            if (game.getPlayers().size() == 1) {
                game.getPlayer(playerID); // remove last player from game instance

                playersController.removePlayer(playerID); // remove player from players controller
                gamesController.removePlayer(gameID, playerID);
                gamesController.removeGame(gameID);

                return null;
            }
            else {
                gamesController.removePlayer(gameID, playerID);
                playersController.removePlayer(playerID);
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

        try {
            game = gamesController.getGame(gameID);

            PlayerData playerData = playersController.getPlayer(player.getPublicID());

            playerData.setName(player.getName());

            playersController.updatePlayer(playerData);

        } catch (InvalidGameInstanceException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game Not Found", ex);
        } catch (InvalidPlayerDataException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player Not Found", ex);
        }

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
    public GameInstance getGame(@PathVariable String id) throws Exception {

        GameInstance game;
        try {
            game = gamesController.getGame(Integer.parseInt(id));

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
     * gets player identified by id passed in
     */
    @GetMapping("/player/{id}")
    public Collection<PlayerData> getPlayer(@RequestParam(value = "id", defaultValue = "0") String id) throws Exception {

        if (id == null || id.equals("")) {
            throw new Exception("NullPlayerIdException");
        }
        Collection<PlayerData> playerList;
        
        try{
            int playerID = Integer.parseInt(id);
            playerList = new ArrayList<PlayerData>();

            playerList.add(
                    playersController.getPlayer(playerID)
            );

        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

        return playerList;
    }


    @GetMapping("/health")
    public String heartBeat() {
        GameInstance game = new ArrayList<>(gamesController.getAllGames()).get(0);
        return String.format(
                "Game gameID: %s%n" +
                        "Game Name: %s%n" +
                        "Game State: %s%n" +
                        "Number of Players: %s%n" +
                        "Public Player Ids: %s",
                game.getGameID(),
                game.gameName,
                game.getGameState(),
                playersController.getAllPlayers().size(),
                game.getPlayers().keySet());
    }

}
                                                                                                                                                                        