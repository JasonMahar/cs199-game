
#include "GameInstance.h"
#include "PlayerData.h"

using namespace std;


GameInstance::GameInstance()
{
}

GameInstance::GameInstance(GameState gameState, int ID, 
			unordered_map<int, PlayerData> &players)
{
	this->gameState = gameState;
	this->gameID = ID.value();

	// create new players map or set players map
	// 
	//if (players.empty())
	if (players == null)
	{
		throw new Exception("players map cannot be null");
		//this->players = unordered_map<int, PlayerData>();
	}
	else
	{
		this->players = players;
		this->gameOwner = (vector<>(players.values()))[0];
	}
}


// NOTE: this version calls other constructor to set most attributes
GameInstance::GameInstance(GameState gameState, int ID, 
			unordered_map<int, PlayerData> &players, PlayerData gameOwner)
					: GameInstance(gameState, ID, players)
{

	//if (gameOwner == nullptr)
	if (gameOwner == NULL)
	{
		throw runtime_error("gameOwner passed in is NULL");
	}

	// NOTE: this should proably be just the playerID
	this->gameOwner = gameOwner;

	// NOTE: commenting out. don't think we want to pass gameName this way
	/*
	// set game name
	 if ((static_pointer_cast<TemplePlayerData>(gameOwner))->gameName != "")
	{
		this->gameName = (gameOwner)->gameName;
	}
	else
	{
		this->gameName = "TempleRun-" + ID.value();
	}*/


}

string GameInstance::getGameName()
{
	return gameName;
}

GameInstance GameInstance::setGameName(const string gameName)
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

	//if (player == nullptr)
	if (player == NULL)
	{
		throw runtime_error("Null Input Exception");
	}

	return this->players.emplace(player->getPublicID(), player) == nullptr;
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

	return this->players.erase(playerPublicID) != nullptr;
}


//ORIGINAL LINE: @JsonIgnore public boolean isEmpty()
bool GameInstance::isEmpty()
{
	return this->players.empty();
}

//ORIGINAL LINE: @JsonIgnore public boolean isFull()
bool GameInstance::isFull()
{
	return this->players.size() >= GameDesignVars::MAX_PLAYERS_PER_GAME;
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
	return this->players[playerID];
}

unordered_map<int, PlayerData> & GameInstance::getPlayers()
{
	return players;
}

string GameInstance::toString()
{
	return  "gameID=" + gameID + 
			", gameName=" + gameName + 
			", gameOwner=" + gameOwner->getName() + 
			", gameState=" + gameState;

	/*
	return "\n{ "
		+ "'_commnent' : 'GAME',"
		+ "'ID' : " + gameID + ", "
		+ "'gameState' : " + gameState + ", "
		+ getAllPlayers()
		+ " } ";
	*/

}

bool GameInstance::equals(any o)
{
	if (this == o)
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

	return true;
}


public int GameInstance::hashCode() {
	int result = gameState != null ? gameState.hashCode() : 0;
	result = 31 * result + gameID;
	result = 31 * result + (players != null ? players.hashCode() : 0);
	return result;
}



void GameInstance::setCustomGameName(const string gameName)
{
	this.gameName = gameName;
}


