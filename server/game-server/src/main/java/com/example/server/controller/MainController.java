package com.example.server.controller;


import com.example.model.*;
import com.example.server.model.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
public class MainController {

    private static final Integer STUB_gameID = 1;
    private static final Integer STUB_playerID = 1001;
    private static final Integer STUB_playerID2 = 1002;

    private static final Integer STUB_playerID3 = 1003;

    private static GamesController gamesController = new GamesController();

    private static PlayersController playersController = new PlayersController();
    /////////////////////////////////////////////
    //
    // Game calls


    static {
        GamesController.createGame(GameDesignVars.GAME_LOBBY_ID);
    }

    @GetMapping
    public String defaultGreeting() {
        return "Test greeting in MainController";
    }


    @GetMapping("/test/{test}")
    public String getGreetings(@PathVariable("test") String gameId) {
        return "get mapping /game/{gameId} " + gameId;
    }

    /* createGame
     *
     * creates and returns a GameInstance
     * receives the Player's data, which is automatically added to the GameInstance
     */
    @PostMapping("/game")
    public GameInstance createGame(@RequestBody TemplePlayerData newPlayer) {

        Integer ID = GameDesignVars.BAD_GAME_ID;
// HACK:
        // this will simply instantiate a GameInstance with GAME_LOBBY_ID
        //		replacing the previous instance if there was one
        if( GameDesignVars.ONLY_ONE_GAME ) {
            ID = GameDesignVars.GAME_LOBBY_ID;
            GamesController.createGame(ID);
        }
        else {

            ID = GamesController.createGame();
        }
        GameInstance newGame = gamesController.getGame(ID);
        newGame.addPlayer(newPlayer);
        PlayersController.addPlayer(newPlayer);

        return newGame;
    }


    /* joinGame
     *
     * add a Player's data in RequestBody to the GameInstance with gameID.
     *
     * if successful return the current GameInstance
     */
    @PostMapping("/game/{gameID}")
    public GameInstance joinGame(@PathVariable Integer gameID, @RequestBody TemplePlayerData newPlayer) {


        if( newPlayer == null) {
            // TODO: Create an Error response
            //		see examples: https://www.amitph.com/spring-rest-api-custom-error-messages/
            return null;
        }
        System.out.println("\twith gameID: " + gameID + ", playerID: " + newPlayer.getPublicID());

        newPlayer.setState(PlayerData.State.IN_LOBBY);
        PlayersController.addPlayer(newPlayer);

        GameInstance game = gamesController.getGame(gameID);
        if( game == null) {
            // TODO: Create an Error response
            //		see examples: https://www.amitph.com/spring-rest-api-custom-error-messages/
            return null;
        }

        if( !game.addPlayer(newPlayer) ) {

            // TODO: Create an Error response
            //		see examples: https://www.amitph.com/spring-rest-api-custom-error-messages/


            // NOTE: this usually means that the player was already in the game.
            //		But because we probably won't implement leaveGame() for the
            //		first iteration, we'll go ahead and return the game
//			return null;
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
        System.out.println("ServerApplication.leaveGame() called with gameID: " +
                gameID + ", playerID: " + playerID);

        GameInstance game = gamesController.getGame(gameID);

        game.removePlayer(playerID);
        if( game.isEmpty()) {
            gamesController.removeGame(gameID);
        }

        return gamesController.getAllGames();
    }


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


    // TODO: this is really for debugging purposes and would be a security concern in a final product
    // return all games
    @GetMapping("/game")
    public Collection<GameInstance> getAllGames() {
        System.out.println("ServerApplication.getAllGames() called");

        return gamesController.getAllGames();
    }


    //
    @GetMapping("/game/{id}")
    public GameInstance getGame(@PathVariable String id) {

        int gameID = 0;
        try {
            gameID = Integer.parseInt(id);
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }

        return gamesController.getGame(gameID);
    }


    // owner of the room clicks Start Game button or Game Ends, etc
    // 		(for this first iteration, everyone in the lobby has Start Game privileges,
    //		since we're skipping the CreateGame step and there's only one GameID to JoinGame)
    //
//	@PutMapping("/game/{id}")
//	public GameInstance updateGameState(@PathVariable Integer gameID, @RequestBody GameState state) {
//		System.out.println("ServerApplication.updateGameState() called with gameID: " +
//				gameID + ", state: " + state);

    // HACK: for some reason passing in state is messing up the ID, so hardcoding the state
    @PutMapping("/game/{id}")
    public GameInstance updateGameState(@PathVariable Integer gameID) {
//		public GameInstance updateGameState(@PathVariable String ID) {
//		public GameInstance updateGameState(@PathVariable String ID, @RequestParam(value = "state", defaultValue = "IN_GAME") String state) {
        System.out.println("ServerApplication.updateGameState() called with gameID: " + gameID + ",");

//		Integer gameID = 1;
//		try {
//			gameID = Integer.valueOf(ID);
//		}
//        catch (NumberFormatException ex){
//            ex.printStackTrace();
//        }

        // HACK: for some reason passing in state is messing up the ID, so hardcoding the state
//		GameState gameState = GameState.valueOf(state);
        GameInstance.GameState gameState = GameInstance.GameState.ENTERING_GAME;
        System.out.println("\tstate: " + gameState);

        GameInstance game = gamesController.getGame(gameID);
        game.setGameState(gameState);


        // different game states will change Player's state as appropriate
        PlayerData.State playersState = null;


        switch(gameState) {

            case GAME_STARTING -> {}

                //NOTE:  for this first iteration, we're skipping the starting countdown
                //		and loading phase and going directly to IN_GAME
            case ENTERING_GAME -> {
                game.setGameState(GameInstance.GameState.IN_GAME);
            }

            case IN_GAME -> {
                // fall through to GameState.ENTERING_GAME
                playersState =  PlayerData.State.RUNNING;

                // need to set all players spawn points
                MapManager.setPlayersStartingLocations( game.getAllPlayers() );
            }

            case GAME_LOBBY -> {
                playersState = PlayerData.State.IN_LOBBY;
            }

            case CLOSING -> {
                playersState = PlayerData.State.MAIN_MENU;
                gamesController.removeGame(gameID);
            }

            default -> {
                System.out.println("\tERROR: Unknown state: " + gameState  + ". returning null");
            }

        }


        if( playersState != null) {

            for(PlayerData player : game.getAllPlayers()) {
                player.setState(playersState);
            }
        }

        return game;
    }


    // SUPER HACK: I can't get the gameID to show up for updateGameState so I'm just create a
    // whole new version with no parameters
    @PutMapping("/startgame/")
    public GameInstance startGame() {
        System.out.println("ServerApplication.startGame() called");

        // HACK: for some reason passing in state is messing up the ID, so hardcoding the state
//			GameState gameState = GameState.valueOf(state);
        GameInstance.GameState gameState = GameInstance.GameState.ENTERING_GAME;
        System.out.println("\tstate: " + gameState);

// HACK: can't get gameID to pass in so hardcoding it:
        Integer gameID = GameDesignVars.GAME_LOBBY_ID;

        GameInstance game = gamesController.getGame(gameID);
        game.setGameState(gameState);

//		switch(gameState) {
//			case IN_GAME:
        PlayerData.State playersState =  PlayerData.State.RUNNING;

        // need to set all players spawn points
        MapManager.setPlayersStartingLocations( game.getAllPlayers() );

        if( playersState != null) {

            for(PlayerData player : game.getAllPlayers()) {
                player.setState(playersState);
            }
        }

        return game;
    }


    /////////////////////////////////////////////
    //
    // Player calls


    /* createPlayer
     *
     * creates and returns a PlayerData object
     * with:
     * 		PrivateID set for a player's own use with the server
     * 		PublicID set for opponents to refer to the player
     */
    @PostMapping("/player")
    public PlayerData createPlayer(@RequestParam(value = "name", defaultValue = "") String name) {
//		System.out.println("ServerApplication.createPlayer() called with name = " + name);
        System.out.println("ServerApplication.createPlayer() called");

        Integer ID = PlayersController.createPlayer();
        PlayerData newPlayer = PlayersController.getPlayer(ID);
        newPlayer.setName(name);

        return newPlayer;
    }

    /*
     * gets all players
     * This really only exists for admin/debugging purposes
     * normally the URI looks like "/player/{id}" in which getPlayer is called
     */
    @GetMapping("/player")
//	public Collection<PlayerData> player(@RequestParam(value = "id", defaultValue = "0") String id) {
    public Collection<PlayerData> getAllplayers() {

        System.out.println("ServerApplication.getAllplayers() called. ");
//
//		Integer playerID = 0;
//		try {
//			playerID = Integer.valueOf(id);
//		}
//        catch (NumberFormatException ex){
//            ex.printStackTrace();
//        }
//
//		if( playerID == null || playerID == 0 ) {
//			System.out.println("no playerID = " + playerID + ". getting all players.");

        // TODO:
        // 	should normally return an error code for invalid param or data not found

        // STUB!
//			playerID = STUB_playerID;

        return PlayersController.getAllPlayers();
//		}
//		else {
//			System.out.println("getPlayer playerID = " + playerID);
//
//			Collection<PlayerData> playerList = new ArrayList<PlayerData>();
//			PlayerData player = PlayersController.getPlayer(playerID);
//			playerList.add(player);
//			return playerList;
//
//		}

//PlayerData ps = PlayersController.getPlayer(playerID);
//
//System.out.println("ServerApplication.player() returning PlayerData: " + ps);
//		return ps;

    }


    /* getPlayer
     *
     * gets player identified by id passed in
     */
    @GetMapping("/player/{id}")
    public PlayerData getPlayer(@PathVariable String id) {

        System.out.println("ServerApplication.getPlayer() called with id: " + id);

        Integer playerID = 0;
        try {
            playerID = Integer.valueOf(id);
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }

        if( playerID == null || playerID == 0 ) {
            System.out.println("bad playerID = " + playerID + ". returning 1st player.");

            // TODO:
            // 	should normally return an error code for invalid param or data not found

//			// STUB!
//			playerID = STUB_playerID;
        }

        return PlayersController.getPlayer(playerID);
    }



    /* updatePlayer
     *
     * updates a player's data for the game with gameID.
     *
     * if successful return GameInstance - i.e. latest updated data
     */
    @PutMapping("/player/{gameID}")
    public GameInstance updatePlayer(@PathVariable Integer gameID, @RequestBody TemplePlayerData player) {
//		System.out.println("ServerApplication.updatePlayer() called with gameID: " +
//				gameID + ", playerID: " + player.getPublicID());

        GameInstance game = gamesController.getGame(gameID);
        game.updatePlayer(player);

        PlayersController.updatePlayer(player);

        if( game.isEmpty()) {
            gamesController.removeGame(gameID);
        }

        return game;
    }

    // STUB Methods for Testing!
    //

    private static void STUB_createSampleGame() {


        // STUBS!
        // TODO: Creating just a single game instance for early testing purposes
        GamesController.createGame();
        //GameInstance game = gamesController.getGame(STUB_gameID);
        System.out.println("ServerApplication.STUB_createSampleGame() created demo game. id = " + GameDesignVars.GAME_LOBBY_ID);

//		STUB_playerID = PlayersController.createPlayer();
        PlayerData player1 = new TemplePlayerData("Player1");
        player1.setPublicID(STUB_playerID);
        PlayersController.addPlayer(player1);
        //System.out.println("ServerApplication.main() created PlayerData: " + player1);

//		STUB_playerID2 = PlayersController.createPlayer();
        PlayerData player2 = new TemplePlayerData("Player2");
        player2.setPublicID(STUB_playerID2);
        PlayersController.addPlayer(player2);

        gamesController.addPlayer(STUB_gameID, player1);
        gamesController.addPlayer(STUB_gameID, player2);
    }


    // easy way to test server is running
    //
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }
}
