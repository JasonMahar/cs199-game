#pragma once

#include <string>

using namespace std;

class GameDesignVars final
{
public:

	// Menus constants:

	static const bool START_WITH_SOUND_ENABLED = false;

	static const float MULTIPLAYER_SCREEN_TIME_BETWEEN_UPDATES = 10.0F; 	// seconds per update
	static const float MULTIPLAYER_SCREEN_UPDATE_RATE = 1.0f / LOBBY_SCREEN_TIME_BETWEEN_UPDATES; 	// updates per second
	static const float LOBBY_SCREEN_TIME_BETWEEN_UPDATES = 3.0F; 	// seconds per update
	static const float LOBBY_SCREEN_UPDATE_RATE = 1.0f / LOBBY_SCREEN_TIME_BETWEEN_UPDATES; 	// updates per second



	// Game Instance constants:

	static const int BAD_GAME_ID = 0;
	static const string DEFAULT_GAME_NAME = "Default Game Name";

	static const int MAX_PLAYERS_PER_GAME = 4;



	// Player constants:
	static const int DEFAULT_PLAYER_ID = 0;
	static const string DEFAULT_PLAYER_NAME = "Default Player Name";



	// Player Character constants:
	public static final int 	STARTING_LIVES = 3;
	public static final int 	MAX_LIVES = 3;
	public static final float 	MAX_PLAYER_SPEED = 2.0f;		// in virtual meters/second
	public static final float 	MAX_HEALTH = 1000.0f;		// 



/*private:
	GameDesignVars();
*/

};

