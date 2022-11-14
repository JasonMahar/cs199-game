#pragma once

#include "PlayerData.h"
#include <string>
#include <unordered_map>
#include <vector>
#include <stdexcept>

#include "GameDesignVars.h"


using namespace std;

class GameInstance 
{

private:
	int gameID = GameDesignVars::BAD_GAME_ID;

	GameState gameState = GameState::GAME_LOBBY;

public:
	string gameName = GameDesignVars::DEFAULT_GAME_NAME;

private:
//ORIGINAL LINE: @JsonIgnore private PlayerData gameOwner;
	PlayerData gameOwner = NULL;

	unordered_map<int, PlayerData> players;

public:
	GameInstance();

	GameInstance(GameState gameState, int ID, 
			unordered_map<int, PlayerData> &players);
	GameInstance(GameState gameState, int ID, 
			unordered_map<int, PlayerData> &players, PlayerData gameOwner);

	string getGameName();
	GameInstance setGameName(const string gameName);
	

	// NOTE: this should proably be just the playerID
	PlayerData getGameOwner();

private:
	// NOTE: this should proably be just the playerID
	void setGameOwner(PlayerData gameOwner);

public:
	bool addPlayer(PlayerData player);
	bool updatePlayer(PlayerData player);
	bool removePlayer(int playerPublicID);

//ORIGINAL LINE: @JsonIgnore public boolean isEmpty()
	bool isEmpty();
//ORIGINAL LINE: @JsonIgnore public boolean isFull()
	bool isFull();

	bool join();
	bool start();
	bool enter();
	bool run();
	bool close();

	int getGameID();
	void setGameID(int iD);

	GameState getGameState();
	void setGameState(GameState newState);

	PlayerData getPlayer(int playerID);
	unordered_map<int, PlayerData> getPlayers();

	string toString();

	bool equals(any o);

	int hashCode();

private:
	void setCustomGameName(const string gameName);

public:
	enum class GameState
	{
		GAME_LOBBY,
		GAME_STARTING,
		ENTERING_GAME,
		IN_GAME,
		CLOSING,

		UNKNOWN
	};

	class GameStateHelper
	{
	private:
		static vector<pair<GameState, string>> pairs()
		{
			return
			{
				{GameState::GAME_LOBBY, "GAME_LOBBY"},
				{GameState::GAME_STARTING, "GAME_STARTING"},
				{GameState::ENTERING_GAME, "ENTERING_GAME"},
				{GameState::IN_GAME, "IN_GAME"},
				{GameState::CLOSING, "CLOSING"},
				{GameState::UNKNOWN, "UNKNOWN"}
			};
		}

	public:
		static vector<GameState> values()
		{
			vector<GameState> temp;
			for (auto pair : pairs())
			{
				temp.push_back(pair.first);
			}
			return temp;
		}

		static string enumName(GameState value)
		{
			for (auto pair : pairs())
			{
				if (pair.first == value)
					return pair.second;
			}

			throw runtime_error("Enum not found.");
		}

		static int ordinal(GameState value)
		{
			vector<pair<GameState, string>> temp = pairs();
			for (size_t i = 0; i < temp.size(); i++)
			{
				if (temp[i].first == value)
					return i;
			}

			throw runtime_error("Enum not found.");
		}

		static GameState enumFromString(string value)
		{
			for (auto pair : pairs())
			{
				if (pair.second == value)
					return pair.first;
			}

			throw runtime_error("Enum not found.");
		}
	};

};


