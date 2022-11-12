#include "GameDesignVars.h"

namespace com::example::model
{

const std::optional<int> GameDesignVars::GAME_LOBBY_ID = 1;
const std::optional<int> GameDesignVars::BAD_GAME_ID = 0;
const std::optional<int> GameDesignVars::MAX_GAME_FPS = 30;
const std::optional<int> GameDesignVars::MAX_PLAYERS_PER_GAME = 4;
const std::wstring GameDesignVars::DEFAULT_PLAYER_NAME = L"";
const std::wstring GameDesignVars::DEFAULT_GAME_NAME = L"Temple Run Game";

	GameDesignVars::GameDesignVars()
	{
	}
}
