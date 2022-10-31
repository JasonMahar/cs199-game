package com.example.game.server.controller;

import com.example.game.server.exceptions.*;
import com.example.model.*;
import com.example.util.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class MainController {
    private static final Logger logger;
    private static GamesController gamesController;
    private static PlayersController playersController;


    static {
        
        logger = LoggerFactory.getLogger(MainController.class);
        gamesController = new GamesController();
        playersController = new PlayersController();
    }


    //===================================================================================================
    //                          POST Mapping
    //===================================================================================================

    /* createGame
     *
     * creates and returns a GameInstance
     * receives the Player's data, which is automatically added to the GameInstance
     */
    @PostMapping("/game")
    @ResponseStatus(HttpStatus.CREATED)
    public GameInstance createGame(@RequestBody TemplePlayerData newPlayer
    ) throws Exception {
        GameInstance game;

        try {
            int id = GamesController.createGame();
            game = gamesController.getGame(id);

            game.setGameName(newPlayer.getGameName() == null ?
                    "TempleGame-" + id :
                    newPlayer.getGameName());

            PlayerData player = PlayersController.getPlayer(newPlayer.getPublicID());
            game.setGameOwner(player);

            game.addPlayer(player);
            
        } catch (PlayerNotFoundException e) {
            throw new PlayerNotFoundException("Player name is null {}", e);
        }
        return game;
    }


    /* joinGame
     *
     * add a Player's data in RequestBody to the GameInstance with gameID.
     *
     * if successful return the current GameInstance
     */
    @PostMapping("/game/{gameID}")
    @ResponseStatus(HttpStatus.OK)
    public GameInstance joinGame(
            @PathVariable Integer gameID,
            @RequestBody TemplePlayerData newPlayer) throws Exception {

        if (gameID == 0) {
            throw new GameNotFoundException("Game ID is 0.");
        }

        if (newPlayer == null || newPlayer.getPublicID() == 0) {
            throw new PlayerNotFoundException("New Player Public ID is 0.");
        }

        int newPlayerId = newPlayer.getPublicID();

        GameInstance game;
        try {
            game = gamesController.getGame(gameID);
            TemplePlayerData player = (TemplePlayerData) PlayersController.getPlayer(newPlayerId);
            player.setState(PlayerData.State.IN_LOBBY);


            PrintUtils.red("adding player to game: " + game.addPlayer(player));

        } catch (GameNotFoundException e) {
            throw new GameNotFoundException("Game ID Not Found" + gameID);
        }
        
        return game;
    }


    /* createPlayer
     *
     * creates and returns a PlayerData object
     * with:
     * 		PrivateID set for a player's own use with the server
     * 		PublicID set for opponents to refer to the player
     */
    @PostMapping("/player")
    @ResponseStatus(HttpStatus.OK)
    public PlayerData createPlayer(
            @RequestParam(value = "name") String name) throws PlayerNotFoundException {

        if (name == null || name.isBlank()) {
            throw new PlayerNotFoundException("Cannot create player with null Player Data.");
        }

        Integer ID = PlayersController.createPlayer();
        PlayerData newPlayer = PlayersController.getPlayer(ID);
        newPlayer.setPublicID(ID);
        newPlayer.setName(name);
        return newPlayer;
    }


    //===================================================================================================
    //                          Delete Mapping
    //===================================================================================================

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
            boolean removed = game.removePlayer(playerID);

            if (!game.removePlayer(playerID)) {
                throw new Exception("Could not remove player with player id: " + playerID);
            }
            if (game.isEmpty()) {
                gamesController.removeGame(gameID);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return gamesController.getAllGames();
    }


    //===================================================================================================
    //                          PUT Mapping
    //===================================================================================================

    //      owner of the room clicks Start Game button or Game Ends, etc
    // 		(for this first iteration, everyone in the lobby has Start Game privileges,
    //		since we're skipping the CreateGame step and there's only one GameID to JoinGame)
    //
    @PutMapping("/game/{id}")
    public GameInstance updateGameState(@PathVariable Integer gameID, @RequestBody GameInstance.GameState state) throws Exception {
        if (gameID == null || gamesController.getGame(gameID) == null) {
            throw new GameNotFoundException("Game ID or Game Instance using ID is null");
        }

        if (state == null) {
            throw new Exception("Stub for Invalid GameState Exception");
        }
        logger.info("updateGameState(): gameID = {} gameState = {}", gameID, state);

//        GameInstance.GameState gameState = GameInstance.GameState.ENTERING_GAME;

        GameInstance.GameState gameState = state;

        GameInstance game = gamesController.getGame(gameID);

        // different game states will change Player's state as appropriate
        PlayerData.State playersState = null;

        switch (state) {

            case GAME_STARTING -> {
                game.setGameState(gameState);
            }

            //NOTE:  for this first iteration, we're skipping the starting countdown
            //		and loading phase and going directly to IN_GAME
            case ENTERING_GAME -> {
                game.setGameState(GameInstance.GameState.IN_GAME);
            }

            case IN_GAME -> {
                // fall through to GameState.ENTERING_GAME
                playersState = PlayerData.State.RUNNING;

            }

            case GAME_LOBBY -> {
                playersState = PlayerData.State.IN_LOBBY;
            }

            case CLOSING -> {
                playersState = PlayerData.State.MAIN_MENU;
                gamesController.removeGame(gameID);
            }

            default -> {
                throw new Exception("\tUnknown State Exception: ERROR: Unknown state: " + gameState + ". returning null");
            }
        }

        for (PlayerData player : game.getPlayers().values()) {
            player.setState(playersState);
        }

        return game;
    }


    @PutMapping("/startgame")
    public GameInstance startGame() {

        Integer gameID = gamesController.getAllGames().stream().toList().get(0).getID();

        GameInstance game = gamesController.getGame(gameID);

        game.setGameState(GameInstance.GameState.ENTERING_GAME);

        // temporary state set to running just to make sure state is changing to expected value
            for (PlayerData player : game.getPlayers().values()) {
                player.setState(PlayerData.State.RUNNING);
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
    public GameInstance updatePlayer(@PathVariable Integer gameID, @RequestBody TemplePlayerData playerUpdates) throws Exception {

        if (gameID == null) {
            throw new GameNotFoundException("Game ID is null");
        }

        if (playerUpdates == null || playerUpdates.getPublicID() == 0) {
            throw new PlayerNotFoundException("Player ID is null or Player ID in invalid");
        }
        GameInstance game;

        try {
            game = gamesController.getGame(gameID);

            PlayerData playerData = PlayersController.getPlayer(playerUpdates.getPublicID());

            playerData.setName(playerUpdates.getName());

            PlayersController.updatePlayer(playerData);

            if (game.isEmpty()) {
                gamesController.removeGame(gameID);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return game;
    }


    //===================================================================================================
    //                          GET MAPPING
    //===================================================================================================
    // return all games
    @GetMapping("/game")
    public Collection<GameInstance> getAllGames() {
        if(gamesController == null) {
            gamesController = new GamesController();
        }

        Collection<GameInstance> allGames = gamesController.getAllGames();
        if(allGames == null || allGames.size() == 0) {
            throw new GameNotFoundException("No Game Instances Found");
        }

        return gamesController.getAllGames();
    }


    @GetMapping("/game/{id}")
    public GameInstance getGame(@PathVariable String id) {
        if(gamesController == null) {
            gamesController = new GamesController();
        }

        if(id == null || id.isBlank()) {
            throw new GameNotFoundException("Game ID not found: id = " + id);
        }

        int gameID = Integer.parseInt(id);

        if (gameID == 0) {
            throw new GameNotFoundException("Bad game id = " + gameID);
        }
        return gamesController.getGame(gameID);
    }


    /*
     * gets all players
     */
    @GetMapping("/player")
    public Collection<PlayerData> getAllplayers() {
        PrintUtils.green(String.format("GetMapping: /player gets all players. String id = %s", PlayersController.getAllPlayers()));

        return PlayersController.getAllPlayers();
    }


    /* getPlayer
     *
     * gets player identified by id passed in
     */
    @GetMapping("/player/{id}")
    public PlayerData getPlayer(@RequestParam(value = "id", defaultValue = "0") String id) throws PlayerNotFoundException {

        if (id == null || id.isBlank() || id.hashCode() < 1) {
            throw new PlayerNotFoundException("bad playerID = " + id);
        }
        int playerID = Integer.parseInt(id);

        if (playerID == 0) {
            throw new PlayerNotFoundException("bad playerID = " + id);
        }
        return PlayersController.getPlayer(playerID);
    }

}
