#pragma once

#include "PlayerData.h"
#include <string>


using namespace std;

class TemplePlayerData :  public PlayerData
{

protected:
	int publicID = GameDesignVars::DEFAULT_PLAYER_ID;

private:
	UUID privateID;

protected:
	string name = GameDesignVars::DEFAULT_PLAYER_NAME;

	PlayerData::State state = PlayerData::State::PLAYER_CREATED;

	string gameName = GameDesignVars::DEFAULT_GAME_NAME;

private:
	bool ownsGame = false;

public:
	TemplePlayerData();
	TemplePlayerData(string name, int publicId);

	TemplePlayerData(string name, int publicId, bool isGameOwner);


	bool isGameOwner();
	void setIsGameOwner(bool isGameOwner);

	int getPublicID() override;
	void setPublicID(int publicID) override;

	UUID getPrivateID() override;
	void setPrivateID(UUID privateID) override;

	string getName() override;
	void setName(string name) override;

	string getGameName();
	void setGameName(string gameName);

	PlayerData::State getState();
	void setState(PlayerData::State state);


	string toString();

};

