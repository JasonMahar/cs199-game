package com.example.game.server.controller;

import com.example.game.server.exceptions.*;
import com.example.model.*;
import com.example.util.*;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.example.game.server.model.MapManager.setPlayersStartingLocations;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class MainController {

    private static final Logger logger;

    private static GamesController gamesController;
    private static PlayersController playersController;


    static {

        logger = LoggerFactory.getLogger(MainController.class);
        logger.trace("static initialization block: {}",
                PrintUtils.green(String.format("class/logger = %s", MainController.class.toString())));
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
        if (newPlayer == null || newPlayer.getName() == null) {
            throw new InvalidPlayerFieldException("Cannot create game with null Player Data.");
        } else {
        }


        GameInstance game = gamesController.getAllGames() == null || gamesController.getAllGames().size() == 0 ?
                gamesController.getGame(GamesController.createGame()) :
                gamesController.getAllGames().stream().toList().get(0);

        game.addPlayer(newPlayer);
        PlayersController.addPlayer(newPlayer);
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
            @RequestBody TemplePlayerData newPlayer) throws Exception {

        if (newPlayer == null || newPlayer.getPublicID() == 0) {
            throw new InvalidPlayerFieldException("Cannot Join Game with null Player Data.");
        } else {
            playersController = new PlayersController();
        }

        if (gameID == null) {
            throw new InvalidGameIdException("Game Instance Not Found");
        } else {
            gamesController = new GamesController();
            GameInstance game = gamesController.getGame(gameID);
        }


        newPlayer.setState(PlayerData.State.IN_LOBBY);
        PlayersController.addPlayer(newPlayer);
        GameInstance game = gamesController.getGame(gameID);

        if (game == null) {
            throw new InvalidGameIdException("Game ID Not Found" + gameID);
        }

        if (!game.addPlayer(newPlayer)) {

            throw new InvalidPlayerFieldException("Cant add player to game" + gameID);
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
            @RequestParam(value = "name", defaultValue = "") String name) throws InvalidPlayerFieldException {

        if (name == null || name.isBlank()) {
            throw new InvalidPlayerFieldException("Cannot create player with null Player Data.");
        }

        Integer ID = PlayersController.createPlayer();
        PrintUtils.cyan("iD: " + ID);
        PlayerData newPlayer = PlayersController.getPlayer(ID);
        newPlayer.setPublicID(ID);
        newPlayer.setName(name);

        PrintUtils.cyan("newPlayer: " + newPlayer.getPublicID() + " state:" + newPlayer.getState());
        PrintUtils.cyan("name: " + newPlayer.getName());
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

        if (game.isEmpty()) {
            gamesController.removeGame(gameID);

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
            throw new InvalidGameIdException("Game ID or Game Instance using ID is null");
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

                // need to set all players spawn points
                setPlayersStartingLocations(game.getPlayers().values());
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

        Integer gameID = GameDesignVars.GAME_LOBBY_ID;

        GameInstance game = gamesController.getGame(gameID);
        game.setGameState(gameState);


        PlayerData.State playersState = PlayerData.State.RUNNING;

        // need to set all players spawn points
        setPlayersStartingLocations(game.getPlayers().values());

        if (playersState != null) {

            for (PlayerData player : game.getPlayers().values()) {
                player.setState(playersState);
            }
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
        if (gameID == null || gamesController.getGame(gameID) == null) {
            throw new InvalidGameIdException("Game ID is null or Game ID is invalid");
        }
        if (player == null || PlayersController.getPlayer(player.getPublicID()) == null) {
            throw new InvalidPlayerFieldException("Player ID is null or Player ID in invalid");
        }
        GameInstance game = gamesController.getGame(gameID);
        game.updatePlayer(player);

        PlayersController.updatePlayer(player);

        if (game.isEmpty()) {
            gamesController.removeGame(gameID);
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
            throw new InvalidGameIdException("No Game Instances Found");
        }

        return gamesController.getAllGames();
    }


    @GetMapping("/game/{id}")
    public GameInstance getGame(@PathVariable String id) {
        if(gamesController == null) {
            gamesController = new GamesController();
        }

        if(id == null || id.isBlank()) {
            throw new InvalidGameIdException("Game ID not found: id = " + id);
        }

        int gameID = Integer.parseInt(id);

        GameInstance gameInstance = gamesController.getGame(gameID);
        return gameInstance;
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
    public Collection<PlayerData> getPlayer(@RequestParam(value = "id", defaultValue = "0") String id) throws InvalidPlayerFieldException {

        if (id == null || id.isBlank() || id.hashCode() < 1) {
            throw new InvalidPlayerFieldException("bad playerID = " + id);
        }

        int playerID = Integer.parseInt(id);

        if (playerID == 0) {
            throw new InvalidPlayerFieldException("bad playerID = " + id);
        }

        Collection<PlayerData> playerList = new ArrayList<PlayerData>();
        PlayerData player = PlayersController.getPlayer(playerID);
        playerList.add(player);
        return playerList;
    }

}
