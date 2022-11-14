#include "TemplePlayerData.h"

using namespace std;

TemplePlayerData::TemplePlayerData()
{
}

TemplePlayerData::TemplePlayerData(string name, int publicId) 
		: TemplePlayerData(name, publicId, false)
{
}

TemplePlayerData::TemplePlayerData(string name, int publicId, bool isGameOwner)
{
	this->name = name;
	this->publicID = publicId;
	this->ownsGame = isGameOwner;
}

bool TemplePlayerData::isGameOwner()
{
	return ownsGame;
}

void TemplePlayerData::setIsGameOwner(bool isGameOwner)
{
	this->ownsGame = isGameOwner;

}

int TemplePlayerData::getPublicID()
{
	return this->publicID;
}

void TemplePlayerData::setPublicID(int publicID)
{
	this->publicID = publicID;
}

UUID TemplePlayerData::getPrivateID()
{
	return this->privateID;
}

void TemplePlayerData::setPrivateID(UUID privateID)
{
	this->privateID = privateID;
}

string TemplePlayerData::getName()
{
	return this->name;
}

void TemplePlayerData::setName(string name)
{
	this->name = name;
}

string TemplePlayerData::getGameName()
{
	return this->gameName;
}

void TemplePlayerData::setGameName(string gameName)
{
	this->gameName = gameName;
}

PlayerData::State TemplePlayerData::getState()
{
	return this->state;
}

void TemplePlayerData::setState(PlayerData::State state)
{
	this->state = state;
}

string TemplePlayerData::toString()
{

	//return L"{" + L" publicID=" + std::to_wstring(this->publicID) + L", name=" + this->name + L", state=" + this->state + L"}";
	return "{" + " publicID=" + this->publicID + ", name=" + this->name + ", state=" + this->state + "}";
}


