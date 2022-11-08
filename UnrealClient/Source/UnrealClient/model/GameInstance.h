#pragma once

#include "PlayerData.h"
#include <string>
#include <unordered_map>
#include <vector>
#include <stdexcept>

using namespace std;

class GameInstance 
{

private:
	int gameID = 0;

	GameState gameState = static_cast<GameState>(0);
public:
	string gameName;
private:
//JAVA TO C++ CONVERTER TODO TASK: Most Java annotations will not have direct C++ equivalents:
//ORIGINAL LINE: @JsonIgnore private PlayerData gameOwner;
	PlayerData gameOwner;

	unordered_map<int, PlayerData> players;

public:
	GameInstance();

	GameInstance(GameState gameState, int &ID, unordered_map<int, PlayerData> &players);

	GameInstance(GameState gameState, int &ID, unordered_map<int, PlayerData> &players, PlayerData gameOwner);

	virtual string getGameName();

	virtual GameInstance setGameName(const string &gameName);

	virtual PlayerData getGameOwner();

private:
	void setGameOwner(PlayerData gameOwner);

public:
	virtual bool addPlayer(PlayerData player);

	virtual bool updatePlayer(PlayerData player);

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

	virtual PlayerData getPlayer(int playerID);

	virtual unordered_map<int, PlayerData> getPlayers();

	virtual string toString();

	virtual bool equals(any o);

	virtual int hashCode();

private:
	void setCustomGameName(const string &gameName);

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
		static vector<pair<GameState, string>> pairs()
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


