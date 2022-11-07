#include "TemplePlayerData.h"

namespace com::example::model
{
	using namespace com::fasterxml::jackson::annotation;

	TemplePlayerData::TemplePlayerData()
	{
	}

	TemplePlayerData::TemplePlayerData(const std::wstring &name, int publicId) : TemplePlayerData(name, publicId, false)
	{
	}

	TemplePlayerData::TemplePlayerData(const std::wstring &name, int publicId, bool isGameOwner)
	{
		this->name = name;
		this->publicID = publicId;
		this->isGameOwner_Conflict = isGameOwner;
	}

	bool TemplePlayerData::isGameOwner()
	{
		return isGameOwner_Conflict;
	}

	void TemplePlayerData::setIsGameOwner(bool isGameOwner)
	{
		this->isGameOwner_Conflict = isGameOwner;

	}

	int TemplePlayerData::getPublicID()
	{
		return this->publicID;
	}

	void TemplePlayerData::setPublicID(int publicID)
	{
		this->publicID = publicID;
	}

	std::shared_ptr<UUID> TemplePlayerData::getPrivateID()
	{
		return this->privateID;
	}

	void TemplePlayerData::setPrivateID(std::shared_ptr<UUID> privateID)
	{
		this->privateID = privateID;
	}

	std::wstring TemplePlayerData::getName()
	{
		return this->name;
	}

	void TemplePlayerData::setName(const std::wstring &name)
	{
		this->name = name;
	}

	std::wstring TemplePlayerData::getGameName()
	{
		return this->gameName;
	}

	void TemplePlayerData::setGameName(const std::wstring &gameName)
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

	std::wstring TemplePlayerData::toString()
	{

		return L"{" + L" publicID=" + std::to_wstring(this->publicID) + L", name=" + this->name + L", state=" + this->state + L"}";
	}
}
