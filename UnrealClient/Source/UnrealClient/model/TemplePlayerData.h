#pragma once

#include "PlayerData.h"
#include <string>


using namespace std;

class TemplePlayerData :  public PlayerData
{

protected:
	int publicID = 0;

private:
	UUID privateID;

protected:
	string name;
	PlayerData::State state = static_cast<PlayerData::State>(0);

	string gameName;

private:
	bool ownsGame = false;

public:
	TemplePlayerData();
	TemplePlayerData(const string &name, int publicId);

	TemplePlayerData(const string &name, int publicId, bool isGameOwner);


	bool isGameOwner();
	void setIsGameOwner(bool isGameOwner);

	int getPublicID() override;
	void setPublicID(int publicID) override;

	UUID getPrivateID() override;
	void setPrivateID(UUID privateID) override;

	string getName() override;
	void setName(const string &name) override;

	string getGameName();
	void setGameName(const string &gameName);

	PlayerData::State getState();
	void setState(PlayerData::State state);


	string toString();

};

