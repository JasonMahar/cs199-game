#pragma once

#include "PlayerData.h"
#include <string>
#include <memory>

namespace com::example::model
{

	using namespace com::fasterxml::jackson::annotation;


	class TemplePlayerData : public std::enable_shared_from_this<TemplePlayerData>, public PlayerData
	{

	protected:
		int publicID = 0;
	private:
//JAVA TO C++ CONVERTER TODO TASK: Most Java annotations will not have direct C++ equivalents:
//ORIGINAL LINE: @JsonIgnore private UUID privateID;
		std::shared_ptr<UUID> privateID;

	protected:
		std::wstring name;
		PlayerData::State state = static_cast<PlayerData::State>(0);

//JAVA TO C++ CONVERTER TODO TASK: Most Java annotations will not have direct C++ equivalents:
//ORIGINAL LINE: @JsonIgnore protected String gameName;
		std::wstring gameName;

	private:
//JAVA TO C++ CONVERTER NOTE: Field name conflicts with a method name of the current type:
		bool isGameOwner_Conflict = false;

	public:
		TemplePlayerData();
		TemplePlayerData(const std::wstring &name, int publicId);

		TemplePlayerData(const std::wstring &name, int publicId, bool isGameOwner);

		bool isGameOwner() override;

		virtual void setIsGameOwner(bool isGameOwner);
		int getPublicID() override;

		void setPublicID(int publicID) override;

		std::shared_ptr<UUID> getPrivateID() override;

		void setPrivateID(std::shared_ptr<UUID> privateID) override;

		std::wstring getName() override;

		void setName(const std::wstring &name) override;

		virtual std::wstring getGameName();

		virtual void setGameName(const std::wstring &gameName);

		virtual PlayerData::State getState();

		virtual void setState(PlayerData::State state);
		virtual std::wstring toString();

	};

}
