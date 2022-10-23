package com.example.server.controller;


import com.example.model.*;
import com.example.server.exceptions.*;
import com.example.server.model.*;
import com.example.util.*;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class MainController {

    private static final Logger logger;

    private static GamesController gamesController;
    private static PlayersController playersController;

    // Create Game with initialized GamesController, PlayersController
    static {
        logger = LoggerFactory.getLogger(MainController.class);
        PrintUtils.green(String.format("STATIC BLOCK IN class/logger = %s", MainController.class.toString()));
//        gamesController = new GamesController();
//        playersController = new PlayersController();
//        GamesController.createGame(GameDesignVars.GAME_LOBBY_ID);
//
//        logger.info("Created new GameInstance(GameInstance.GameState.GAME_LOBBY, key , null) = {}",
//                PrintUtils.green(String.format("GameDesignVars.GAME_LOBBY_ID = %s%n" +
//                                "game state = %s%n",
//                        GameDesignVars.GAME_LOBBY_ID,
//                        gamesController.getGame(GameDesignVars.GAME_LOBBY_ID).getGameState()
//                )));
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
    public GameInstance createGame(
            @RequestBody // Annotation indicating a method parameter should be bound to the body of the web request.
            TemplePlayerData newPlayer
    ) throws Exception {

        if (newPlayer == null || newPlayer.getName() == null) {
            throw new PlayerNotFoundException("Player information in Request Body cannot be null");
        } else {
            if(playersController == null) {
                playersController = new PlayersController();
            }
            logger.info("Logging @PostMapping: /game, createGame {}%n{}",
                    MainController.class.toString(),
                    PrintUtils.green(String.format(" @PostMapping /game @RequestBody = " +
                            "newPlayer.getPublicId = %s%n" +
                            "newPlayer.getName() = %s%n" +
                            "newPlayer.getState = %s%n" +
                            "newPlayer.getFacing = %s%n",
                    newPlayer.getPublicID(),
                    newPlayer.getName(),
                    newPlayer.getState(),
                    newPlayer.getFacing()
            )));
        }

        if(gamesController == null){
            gamesController = new GamesController();
            PrintUtils.green("GamesController initialized with default values (none?) ");
        }

        int sizeOfGamesMap = gamesController.getAllGames().size();
        if(sizeOfGamesMap == 0){
            PrintUtils.green("Games map is empty: size = 0");
        }
        else {
            PrintUtils.green(String.format("Games map is not empty: size = %s", sizeOfGamesMap));
        }


        GameInstance game = gamesController.getAllGames() == null || gamesController.getAllGames().size() == 0 ?
                gamesController.getGame(GamesController.createNewGame()) :
                gamesController.getAllGames().stream().toList().get(0);


        PrintUtils.green(String.format("" +
                        "Game ID = %s, Game State = %s, Players in Game = %s",
                game.getID(),
                game.getGameState(),
                game.getAllPlayers()
        ));

        int ID = game.getID();

        game.addPlayer(newPlayer);
        PlayersController.addPlayer(newPlayer);

        PrintUtils.green(String.format("GameInstance = %s , Added Player in GameInstance = %s, Added Player in PlayersController = %s",
                game.getID(),
                game.getAllPlayers().stream().toList().contains(newPlayer),
                PlayersController.getPlayer(newPlayer.getPublicID())
                ));

        return game;
    }


    /* joinGame
     *
     * add a Player's data in RequestBody to the GameInstance with gameID.
     *
     * if successful return the current GameInstance
     */
    @PostMapping("/game/{gameID}")
    public GameInstance joinGame(
            @PathVariable Integer gameID,
            @RequestBody TemplePlayerData newPlayer) throws PlayerNotFoundException {

        if (newPlayer == null || newPlayer.getPublicID() == 0) {
            throw new PlayerNotFoundException("Player information in Request Body cannot be null");
        } else {
            playersController = new PlayersController();
            logger.info("Logging @PostMapping: /game/{gameID}, joinGame(@PathVariable Integer gameID = {}, @RequestBody TemplePlayerData newPlayer = {}%n{}",
                    gameID,
                    newPlayer,
                    PrintUtils.green(String.format(" @PostMapping /game @RequestBody = " +
                            "newPlayer.getPublicId = %s%n" +
                            "newPlayer.getName() = %s%n" +
                            "newPlayer.getState = %s%n" +
                            "newPlayer.getFacing = %s%n",
                    newPlayer.getPublicID(),
                    newPlayer.getName(),
                    newPlayer.getState(),
                    newPlayer.getFacing()
            )));
        }

        if (gameID == null) {
            throw new GameInstanceNotFoundException("Game Instance Not Found");
        } else {
            gamesController = new GamesController();
            GameInstance game = gamesController.getGame(gameID);
            PrintUtils.green(String.format("@PostMapping /game/{gameID} @RequestBody = " +
                            "game.getID() = %s%n" +
                            "game.getGameState() = %s%n" +
                            "game.getAllPlayers().size() = %s%n",
                    game.getID(),
                    game.getGameState(),
                    game.getAllPlayers().size()
            ));
        }


        newPlayer.setState(PlayerData.State.IN_LOBBY);
        PlayersController.addPlayer(newPlayer);
        GameInstance game = gamesController.getGame(gameID);

        if (game == null) {
            PrintUtils.green(String.format("game == null, gameID = %s", gameID));
            throw new GameInstanceNotFoundException("Game ID Not Found" + gameID);
        }

        if (!game.addPlayer(newPlayer)) {
            PrintUtils.green(String.format("!game.addPlayer(newPlayer). newPlayer = %s", gameID));
            throw new PlayerNotFoundException("Cant add player to game" + gameID);
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
    public PlayerData createPlayer(
            @RequestParam(value = "name", defaultValue = "") String name) throws PlayerNotFoundException {

        if (name == null || name.isBlank()) {
            throw new PlayerNotFoundException("Player information in Request Body cannot be null");
        } else {
            if(playersController == null) {
                playersController = new PlayersController();
            }
            PrintUtils.green(String.format("@PostMapping: /player, @RequestParam value: name = %s",
                    name
            ));
        }

        Integer ID = PlayersController.createPlayer();

        PlayerData newPlayer = PlayersController.getPlayer(ID);

        PrintUtils.green(String.format("PlayersController.getPlayer(ID): newPlayer = %s", newPlayer.getPublicID()));
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

        GameInstance game = gamesController.getGame(gameID);
        boolean removed = game.removePlayer(playerID);

        PrintUtils.green(String.format("player id = %s, removed = %s", playerID, removed));

        if (game.isEmpty()) {
            gamesController.removeGame(gameID);
            PrintUtils.green("game.isEmpty() removing game instance");
        }

        return gamesController.getAllGames();
    }


    //===================================================================================================
    //                          PUT Mapping
    //===================================================================================================

/*
	// if no gameID is supplied then return all games
	@GetMapping("/game")

		public Collection<GameInstance> game(@RequestParam(value = "id", defaultValue = "0") String id) {
			System.out.println("ServerApplication.game() called with id: " + id);

		Integer gameID = 0;
		try {
			gameID = Integer.valueOf(id);
		}
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }

		// TODO: probably should not allow clients to see list of games
		//		but it's handy for testing purposes.
		if( gameID == null || gameID == 0 ) {
			// TODO:
			// 	should normally return an error code for invalid param or data not found


			return gamesController.getAllGames();
		}

//
//GameInstance ps =  gamesController.getGame(gameID);
//System.out.println("ServerApplication.game() returning GameInstance: " + ps);
//return ps;

		Collection<GameInstance> c = new ArrayList<GameInstance>();
		GameInstance g = gamesController.getGame(gameID);
		c.add(g);
		return c;
	}
*/

    // owner of the room clicks Start Game button or Game Ends, etc
    // 		(for this first iteration, everyone in the lobby has Start Game privileges,
    //		since we're skipping the CreateGame step and there's only one GameID to JoinGame)
    //
    @PutMapping("/game/{id}")
    public GameInstance updateGameState(@PathVariable Integer gameID, @RequestBody GameInstance.GameState state) throws Exception {
        if (gameID == null || gamesController.getGame(gameID) == null) {
            throw new GameInstanceNotFoundException("Game ID or Game Instance using ID is null");
        }

        if (state == null) {
            throw new Exception("Stub for Invalid GameState Exception");
        }
        logger.info("updateGameState(): gameID = {} gameState = {}", gameID, state);

//        GameInstance.GameState gameState = GameInstance.GameState.ENTERING_GAME;

        GameInstance.GameState gameState = state;

        PrintUtils.green(String.format("\tstate: %s" + gameState));

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

                // need to set all players spawn points
                MapManager.setPlayersStartingLocations(game.getAllPlayers());
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

        PrintUtils.green(String.format("PlayerState = %s ", playersState));

        for (PlayerData player : game.getAllPlayers()) {
            player.setState(playersState);
        }

        return game;
    }


//    @PutMapping("/game/{id}")
//    public GameInstance updateGameState(@PathVariable Integer gameID) {
////		public GameInstance updateGameState(@PathVariable String ID) {
////		public GameInstance updateGameState(@PathVariable String ID, @RequestParam(value = "state", defaultValue = "IN_GAME") String state) {
//        logger.info("ServerApplication.updateGameState() called with gameID: {}",PrintUtils.green(gameID));
//
//        // HACK: for some reason passing in state is messing up the ID, so hardcoding the state
//
//        GameInstance.GameState gameState = GameInstance.GameState.ENTERING_GAME;
//        logger.info("gameState: {}", PrintUtils.green(gameState));
//
//
//        GameInstance game = gamesController.getGame(gameID);
//        game.setGameState(gameState);
//
//
//        // different game states will change Player's state as appropriate
//        PlayerData.State playersState = null;
//
//
//        switch (gameState) {
//
//            case GAME_STARTING -> {
//            }
//
//            //NOTE:  for this first iteration, we're skipping the starting countdown
//            //		and loading phase and going directly to IN_GAME
//            case ENTERING_GAME -> {
//                game.setGameState(GameInstance.GameState.IN_GAME);
//            }
//
//            case IN_GAME -> {
//                // fall through to GameState.ENTERING_GAME
//                playersState = PlayerData.State.RUNNING;
//
//                // need to set all players spawn points
//                MapManager.setPlayersStartingLocations(game.getAllPlayers());
//            }
//
//            case GAME_LOBBY -> {
//                playersState = PlayerData.State.IN_LOBBY;
//            }
//
//            case CLOSING -> {
//                playersState = PlayerData.State.MAIN_MENU;
//                gamesController.removeGame(gameID);
//            }
//
//            default -> {
//                System.out.println("\tERROR: Unknown state: " + gameState + ". returning null");
//            }
//
//        }
//
//
//        if (playersState != null) {
//
//            for (PlayerData player : game.getAllPlayers()) {
//                player.setState(playersState);
//            }
//        }
//
//        return game;
//    }


    @PutMapping("/startgame/")
    public GameInstance startGame() {

        GameInstance.GameState gameState = GameInstance.GameState.ENTERING_GAME;
        PrintUtils.green(String.format("PutMapping: /startgame GameState =  %s", gameState));


        Integer gameID = GameDesignVars.GAME_LOBBY_ID;

        GameInstance game = gamesController.getGame(gameID);
        game.setGameState(gameState);


        PlayerData.State playersState = PlayerData.State.RUNNING;

        // need to set all players spawn points
        MapManager.setPlayersStartingLocations(game.getAllPlayers());

        if (playersState != null) {

            for (PlayerData player : game.getAllPlayers()) {
                player.setState(playersState);
            }
        }
        PrintUtils.green(String.format("PutMapping: /startgame: game.getAllPlayers() = %s", game.getAllPlayers()));
        return game;
    }

    /* updatePlayer
     *
     * updates a player's data for the game with gameID.
     *
     * if successful return GameInstance - i.e. latest updated data
     */
    @PutMapping("/player/{gameID}")
    public GameInstance updatePlayer(@PathVariable Integer gameID, @RequestBody TemplePlayerData player) throws PlayerNotFoundException {
        if (gameID == null || gamesController.getGame(gameID) == null) {
            throw new GameInstanceNotFoundException("Game ID is null or Game ID is invalid");
        }
        if (player == null || PlayersController.getPlayer(player.getPublicID()) == null) {
            throw new PlayerNotFoundException("Player ID is null or Player ID in invalid");
        }
        GameInstance game = gamesController.getGame(gameID);
        game.updatePlayer(player);

        PlayersController.updatePlayer(player);

        if (game.isEmpty()) {
            gamesController.removeGame(gameID);
        }
        PrintUtils.green(String.format("PutMapping: /player/{gameID}: RequestBody = %s", player));
        return game;
    }


    //===================================================================================================
    //                          GET MAPPING
    //===================================================================================================
    // return all games
    @GetMapping("/game")
    public Collection<GameInstance> getAllGames() {
        Collection<GameInstance> allGames = gamesController.getAllGames();
        if(allGames == null || allGames.size() == 0) {
            throw new GameInstanceNotFoundException("No Game Instances Found");
        }

        PrintUtils.green(String.format("GetMapping: /game: getAllGames() = %s", allGames));
        return gamesController.getAllGames();
    }


    @GetMapping("/game/{id}")
    public GameInstance getGame(@PathVariable String id) {

        if(id == null || id.isBlank()) {
            PrintUtils.green(String.format("GetMapping: /game/{id} getGame(@PathVariable String id = %s)", id));
            throw new GameInstanceNotFoundException("Game ID not found: id = " + id);
        }

        int gameID = Integer.parseInt(id);

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
    public Collection<PlayerData> player(@RequestParam(value = "id", defaultValue = "0") String id) throws PlayerNotFoundException {

        if (id == null || id.isBlank() || id.hashCode() < 1) {
            throw new PlayerNotFoundException("bad playerID = " + id);
        }

        int playerID = Integer.parseInt(id);

        if (playerID == 0) {
            throw new PlayerNotFoundException("bad playerID = " + id);
        }

        Collection<PlayerData> playerList = new ArrayList<PlayerData>();
        PlayerData player = PlayersController.getPlayer(playerID);
        playerList.add(player);
        return playerList;
    }

}
