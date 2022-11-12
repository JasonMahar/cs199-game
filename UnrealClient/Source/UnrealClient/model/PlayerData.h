#pragma once

#include <string>
#include <vector>
#include <stdexcept>
#include "GameDesignVars.h"

using namespace std;


// abstract class
class PlayerData
{

	
public:		// all PlayerData methods are pure virtual 

	virtual int getPublicID() = 0;
	virtual void setPublicID(int publicID) = 0;

	virtual UUID getPrivateID() = 0;
	virtual void setPrivateID(UUID privateID) = 0;
	
	virtual string getName() = 0;
	virtual void setName(const string &name) = 0;

	virtual State getState() = 0;
	virtual void setState(State state) = 0;

public:
	enum class State
	{
		PLAYER_CREATED,
		MAIN_MENU,
		IN_LOBBY,
		JOINING_GAME,
		ENTERING_GAME,
		IN_GAME,
		LEAVING_GAME
	};

	class StateHelper
	{
	private:
		static vector<pair<State, string>> pairs()
		{
			return
			{
				{State::PLAYER_CREATED, "PLAYER_CREATED"},
				{State::MAIN_MENU, "MAIN_MENU"},
				{State::IN_LOBBY, "IN_LOBBY"},
				{State::JOINING_GAME, "JOINING_GAME"},
				{State::ENTERING_GAME, "ENTERING_GAME"},
				{State::IN_GAME, "IN_GAME"},
				{State::LEAVING_GAME, "LEAVING_GAME"}
			};
		}

	public:
		static vector<State> values()
		{
			vector<State> temp;
			for (auto pair : pairs())
			{
				temp.push_back(pair.first);
			}
			return temp;
		}

		static string enumName(State value)
		{
			for (auto pair : pairs())
			{
				if (pair.first == value)
					return pair.second;
			}

			throw runtime_error("Enum not found.");
		}

		static int ordinal(State value)
		{
			vector<pair<State, string>> temp = pairs();
			for (size_t i = 0; i < temp.size(); i++)
			{
				if (temp[i].first == value)
					return i;
			}

			throw runtime_error("Enum not found.");
		}

		static State enumFromString(string value)
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
