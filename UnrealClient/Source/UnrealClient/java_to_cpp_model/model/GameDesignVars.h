#pragma once

#include <string>
#include <optional>
#include <memory>

namespace com::example::model
{

	class GameDesignVars final : public std::enable_shared_from_this<GameDesignVars>
	{
	public:
		static constexpr bool USE_STUB_IN_PLACE_OF_SERVER = false;
		static constexpr bool ONLY_ONE_GAME = true;
		static const std::optional<int> GAME_LOBBY_ID;
		static const std::optional<int> BAD_GAME_ID;
		static constexpr bool START_WITH_SOUND_ENABLED = false;
		static const std::optional<int> MAX_GAME_FPS;
		static const std::optional<int> MAX_PLAYERS_PER_GAME;
		static constexpr float LOBBY_SCREEN_TIME_BETWEEN_UPDATES = 3.0F;
		static constexpr float LOBBY_SCREEN_UPDATE_RATE = 0.33333334F;
		static constexpr float GAMEPLAY_MAX_SERVER_UPDATE_RATE = 5.0F;
		static constexpr float GAMEPLAY_TIME_BETWEEN_UPDATES = 0.2F;

		static constexpr int DEFAULT_PLAYER_ID = 0;
		static const std::wstring DEFAULT_PLAYER_NAME;
		static const std::wstring DEFAULT_GAME_NAME;


	private:
		GameDesignVars();
	};

}
