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

	GameInstance::GameInstance(GameState gameState, std::optional<int> &ID, std::unordered_map<int, std::shared_ptr<PlayerData>> &players)
	{
		this->gameState = gameState;
		this->gameID = ID.value();

		if (players.empty())
		{
			this->players = std::unordered_map<int, PlayerData>();
		}
		else
		{
			this->players = players;
			this->gameOwner = (std::vector<>(players.values()))[0];
		}
	}

	GameInstance::GameInstance(GameState gameState, std::optional<int> &ID, std::unordered_map<int, std::shared_ptr<PlayerData>> &players, std::shared_ptr<PlayerData> gameOwner)
	{
		this->gameState = gameState;
		this->gameID = ID.value();

		if (gameOwner == nullptr)
		{
			throw std::runtime_error("players map cannot be null");
		}
		// concrete class in GameInstance?
		this->gameOwner = std::static_pointer_cast<TemplePlayerData>(gameOwner);

		// set game name
		if ((std::static_pointer_cast<TemplePlayerData>(gameOwner))->gameName != L"")
		{
			this->gameName = (std::static_pointer_cast<TemplePlayerData>(gameOwner))->gameName;
		}
		else
		{
			this->gameName = L"TempleRun-" + ID.value();
		}

		// create new players map or set players map
		if (players.empty())
		{
			this->players = std::unordered_map<int, PlayerData>();
		}
		else
		{
			this->players = players;
		}
		this->players.emplace(gameOwner->getPublicID(), gameOwner);

	}

	std::wstring GameInstance::getGameName()
	{
		return gameName;
	}

	std::shared_ptr<GameInstance> GameInstance::setGameName(const std::wstring &gameName)
	{
		this->gameName = gameName;
		return shared_from_this();
	}

	std::shared_ptr<PlayerData> GameInstance::getGameOwner()
	{
		return gameOwner;
	}

	void GameInstance::setGameOwner(std::shared_ptr<PlayerData> gameOwner)
	{
		this->gameOwner = gameOwner;
	}

	bool GameInstance::addPlayer(std::shared_ptr<PlayerData> player)
	{
		if (player == nullptr)
		{
			throw std::runtime_error("Null Input Exception");
		}
		if (std::dynamic_pointer_cast<TemplePlayerData>(player) != nullptr)
		{
			std::shared_ptr<TemplePlayerData> other = std::static_pointer_cast<TemplePlayerData>(player);

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

	bool GameInstance::updatePlayer(std::shared_ptr<PlayerData> player)
	{
		if (player == nullptr)
		{
			throw std::runtime_error("Null Input Exception");
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

	std::shared_ptr<PlayerData> GameInstance::getPlayer(int playerID)
	{
		return std::static_pointer_cast<TemplePlayerData>(this->players[playerID]);
	}

	std::unordered_map<int, std::shared_ptr<PlayerData>> GameInstance::getPlayers()
	{
		return players;
	}

	std::wstring GameInstance::toString()
	{
		return L"gameID=" + std::to_wstring(gameID) + L", gameName=" + gameName + L", gameOwner=" + gameOwner->getName() + L", gameState=" + gameState;

	}

	bool GameInstance::equals(std::any o)
	{
		if (shared_from_this() == o)
		{
			return true;
		}
		if (!(o.type() == typeid(std::shared_ptr<GameInstance>)))
		{
			return false;
		}
		std::shared_ptr<GameInstance> game = std::any_cast<std::shared_ptr<GameInstance>>(o);
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
