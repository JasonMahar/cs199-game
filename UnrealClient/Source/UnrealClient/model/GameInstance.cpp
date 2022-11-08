//====================================================================================================
//The Free Edition of Java to C++ Converter limits conversion output to 100 lines per file.

//To purchase the Premium Edition, visit our website:
//https://www.tangiblesoftwaresolutions.com/order/order-java-to-cplus.html
//====================================================================================================

#include "GameInstance.h"
#include "TemplePlayerData.h"

namespace com::example::model
{
	using namespace com::fasterxml::jackson::annotation;

	GameInstance::GameInstance()
	{
	}

	GameInstance::GameInstance(GameState gameState, int &ID, unordered_map<int, PlayerData> &players)
	{
		this->gameState = gameState;
		this->gameID = ID.value();

		if (players.empty())
		{
			this->players = unordered_map<int, PlayerData>();
		}
		else
		{
			this->players = players;
			this->gameOwner = (vector<>(players.values()))[0];
		}
	}

	GameInstance::GameInstance(GameState gameState, int &ID, unordered_map<int, PlayerData> &players, PlayerData gameOwner)
	{
		this->gameState = gameState;
		this->gameID = ID.value();

		if (gameOwner == nullptr)
		{
			throw runtime_error("players map cannot be null");
		}
		// concrete class in GameInstance?
		this->gameOwner = static_pointer_cast<TemplePlayerData>(gameOwner);

		// set game name
		if ((static_pointer_cast<TemplePlayerData>(gameOwner))->gameName != L"")
		{
			this->gameName = (static_pointer_cast<TemplePlayerData>(gameOwner))->gameName;
		}
		else
		{
			this->gameName = L"TempleRun-" + ID.value();
		}

		// create new players map or set players map
		if (players.empty())
		{
			this->players = unordered_map<int, PlayerData>();
		}
		else
		{
			this->players = players;
		}
		this->players.emplace(gameOwner->getPublicID(), gameOwner);

	}

	string GameInstance::getGameName()
	{
		return gameName;
	}

	GameInstance GameInstance::setGameName(const string &gameName)
	{
		this->gameName = gameName;
		return shared_from_this();
	}

	PlayerData GameInstance::getGameOwner()
	{
		return gameOwner;
	}

	void GameInstance::setGameOwner(PlayerData gameOwner)
	{
		this->gameOwner = gameOwner;
	}

	bool GameInstance::addPlayer(PlayerData player)
	{
		if (player == nullptr)
		{
			throw runtime_error("Null Input Exception");
		}
		if (dynamic_pointer_cast<TemplePlayerData>(player) != nullptr)
		{
			TemplePlayerData other = static_pointer_cast<TemplePlayerData>(player);

			if (players.empty())
			{
				setGameOwner(player);
			}
			if (players.find(other->getPublicID()) != players.end())
			{
				return false;
			}

			return this->players.emplace(other->getPublicID(), other) == nullptr;
		}
		return false;
	}

	bool GameInstance::updatePlayer(PlayerData player)
	{
		if (player == nullptr)
		{
			throw runtime_error("Null Input Exception");
		}
		if (players.find(player->getPublicID()) == players.end())
		{
			return false;
		}

		return this->players.replace(player->getPublicID(), player) != nullptr;
	}

	bool GameInstance::removePlayer(int playerPublicID)
	{
		if (this->players.find(playerPublicID) == this->players.end())
		{
			return false;
		}
		else
		{
			return this->players.erase(playerPublicID) != nullptr;
		}
	}

//JAVA TO C++ CONVERTER TODO TASK: Most Java annotations will not have direct C++ equivalents:
//ORIGINAL LINE: @JsonIgnore public boolean isEmpty()
	bool GameInstance::isEmpty()
	{
		return this->players.empty();
	}

//JAVA TO C++ CONVERTER TODO TASK: Most Java annotations will not have direct C++ equivalents:
//ORIGINAL LINE: @JsonIgnore public boolean isFull()
	bool GameInstance::isFull()
	{
		return this->players.size() >= 4;
	}

	bool GameInstance::join()
	{
		this->setGameState(GameState::GAME_LOBBY);
		return true;
	}

	bool GameInstance::start()
	{
		this->setGameState(GameState::GAME_STARTING);
		return true;
	}

	bool GameInstance::enter()
	{
		this->setGameState(GameState::ENTERING_GAME);
		return true;
	}

	bool GameInstance::run()
	{
		this->setGameState(GameState::IN_GAME);
		return true;
	}

	bool GameInstance::close()
	{
		this->setGameState(GameState::CLOSING);
		return true;
	}

	int GameInstance::getGameID()
	{
		return this->gameID;
	}

	void GameInstance::setGameID(int iD)
	{
		this->gameID = iD;
	}

	GameInstance::GameState GameInstance::getGameState()
	{
		return this->gameState;
	}

	void GameInstance::setGameState(GameState newState)
	{
		this->gameState = newState;
	}

	PlayerData GameInstance::getPlayer(int playerID)
	{
		return static_pointer_cast<TemplePlayerData>(this->players[playerID]);
	}

	unordered_map<int, PlayerData> GameInstance::getPlayers()
	{
		return players;
	}

	string GameInstance::toString()
	{
		return L"gameID=" + to_wstring(gameID) + L", gameName=" + gameName + L", gameOwner=" + gameOwner->getName() + L", gameState=" + gameState;

	}

	bool GameInstance::equals(any o)
	{
		if (shared_from_this() == o)
		{
			return true;
		}
		if (!(o.type() == typeid(GameInstance)))
		{
			return false;
		}
		GameInstance game = any_cast<GameInstance>(o);
		if (gameID != game->gameID)
		{
			return false;
		}
		if (gameState != game->gameState)
		{
			return false;
		}

//====================================================================================================
//End of the allowed output for the Free Edition of Java to C++ Converter.

//To purchase the Premium Edition, visit our website:
//https://www.tangiblesoftwaresolutions.com/order/order-java-to-cplus.html
//====================================================================================================
