#pragma once

#include "PlayerData.h"
#include <string>
#include <unordered_map>
#include <vector>
#include <stdexcept>
#include <any>
#include <utility>
#include <optional>
#include <memory>

namespace com::example::model
{

	using namespace com::fasterxml::jackson::annotation;




	class GameInstance : public std::enable_shared_from_this<GameInstance>
	{

	private:
		int gameID = 0;

		GameState gameState = static_cast<GameState>(0);
	public:
		std::wstring gameName;
	private:
//JAVA TO C++ CONVERTER TODO TASK: Most Java annotations will not have direct C++ equivalents:
//ORIGINAL LINE: @JsonIgnore private PlayerData gameOwner;
		std::shared_ptr<PlayerData> gameOwner;

		std::unordered_map<int, std::shared_ptr<PlayerData>> players;

	public:
		GameInstance();

		GameInstance(GameState gameState, std::optional<int> &ID, std::unordered_map<int, std::shared_ptr<PlayerData>> &players);

		GameInstance(GameState gameState, std::optional<int> &ID, std::unordered_map<int, std::shared_ptr<PlayerData>> &players, std::shared_ptr<PlayerData> gameOwner);

		virtual std::wstring getGameName();

		virtual std::shared_ptr<GameInstance> setGameName(const std::wstring &gameName);

		virtual std::shared_ptr<PlayerData> getGameOwner();

	private:
		void setGameOwner(std::shared_ptr<PlayerData> gameOwner);

	public:
		virtual bool addPlayer(std::shared_ptr<PlayerData> player);

		virtual bool updatePlayer(std::shared_ptr<PlayerData> player);

		virtual bool removePlayer(int playerPublicID);

//JAVA TO C++ CONVERTER TODO TASK: Most Java annotations will not have direct C++ equivalents:
//ORIGINAL LINE: @JsonIgnore public boolean isEmpty()
		virtual bool isEmpty();
//JAVA TO C++ CONVERTER TODO TASK: Most Java annotations will not have direct C++ equivalents:
//ORIGINAL LINE: @JsonIgnore public boolean isFull()
		virtual bool isFull();

		virtual bool join();

		virtual bool start();

		virtual bool enter();

		virtual bool run();

		virtual bool close();


		virtual int getGameID();

		virtual void setGameID(int iD);

		virtual GameState getGameState();

		virtual void setGameState(GameState newState);

		virtual std::shared_ptr<PlayerData> getPlayer(int playerID);

		virtual std::unordered_map<int, std::shared_ptr<PlayerData>> getPlayers();

		virtual std::wstring toString();

		virtual bool equals(std::any o);

		virtual int hashCode();

	private:
		void setCustomGameName(const std::wstring &gameName);

	public:
		enum class GameState
		{
			GAME_LOBBY,
			GAME_STARTING,
			ENTERING_GAME,
			IN_GAME,
			CLOSING,

			UNKNOWN

//JAVA TO C++ CONVERTER TODO TASK: Enum methods are not converted by Java to C++ Converter:
//			private GameState()
	//		{
	//		}
		};

		class GameStateHelper
		{
		private:
			static std::vector<std::pair<GameState, std::wstring>> pairs()
			{
				return
				{
					{GameState::GAME_LOBBY, L"GAME_LOBBY"},
					{GameState::GAME_STARTING, L"GAME_STARTING"},
					{GameState::ENTERING_GAME, L"ENTERING_GAME"},
					{GameState::IN_GAME, L"IN_GAME"},
					{GameState::CLOSING, L"CLOSING"},
					{GameState::UNKNOWN, L"UNKNOWN"}
				};
			}

		public:
			static std::vector<GameState> values()
			{
				std::vector<GameState> temp;
				for (auto pair : pairs())
				{
					temp.push_back(pair.first);
				}
				return temp;
			}

			static std::wstring enumName(GameState value)
			{
				for (auto pair : pairs())
				{
					if (pair.first == value)
						return pair.second;
				}

				throw std::runtime_error("Enum not found.");
			}

			static int ordinal(GameState value)
			{
				std::vector<std::pair<GameState, std::wstring>> temp = pairs();
				for (std::size_t i = 0; i < temp.size(); i++)
				{
					if (temp[i].first == value)
						return i;
				}

				throw std::runtime_error("Enum not found.");
			}

			static GameState enumFromString(std::wstring value)
			{
				for (auto pair : pairs())
				{
					if (pair.second == value)
						return pair.first;
				}

				throw std::runtime_error("Enum not found.");
			}
		};

	};


}
