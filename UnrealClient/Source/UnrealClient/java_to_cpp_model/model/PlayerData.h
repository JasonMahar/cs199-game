#pragma once

#include <string>
#include <vector>
#include <stdexcept>
#include <utility>
#include <memory>

namespace com::example::model
{

	//
	// Source code recreated from a .class file by IntelliJ IDEA
	// (powered by FernFlower decompiler)
	//

	using namespace com::fasterxml::jackson::annotation;


	class PlayerData
	{

	public:
		static constexpr int MAX_PROJECTILES = 1;

		virtual bool isGameOwner() = 0;

//JAVA TO C++ CONVERTER TODO TASK: Most Java annotations will not have direct C++ equivalents:
//ORIGINAL LINE: @JsonIgnore UUID getPrivateID();
		virtual std::shared_ptr<UUID> getPrivateID() = 0;


		virtual int getPublicID() = 0;

		virtual void setPublicID(int var1) = 0;

//JAVA TO C++ CONVERTER TODO TASK: Most Java annotations will not have direct C++ equivalents:
//ORIGINAL LINE: @JsonIgnore void setPrivateID(UUID var1);
		virtual void setPrivateID(std::shared_ptr<UUID> var1) = 0;

		virtual std::wstring getName() = 0;

		virtual void setName(const std::wstring &var1) = 0;

		virtual State getState() = 0;

		virtual void setState(State var1) = 0;

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
//JAVA TO C++ CONVERTER TODO TASK: Enum methods are not converted by Java to C++ Converter:
//			private State()
	//		{
	//		}
		};

		class StateHelper
		{
		private:
			static std::vector<std::pair<State, std::wstring>> pairs()
			{
				return
				{
					{State::PLAYER_CREATED, L"PLAYER_CREATED"},
					{State::MAIN_MENU, L"MAIN_MENU"},
					{State::IN_LOBBY, L"IN_LOBBY"},
					{State::JOINING_GAME, L"JOINING_GAME"},
					{State::ENTERING_GAME, L"ENTERING_GAME"},
					{State::IN_GAME, L"IN_GAME"},
					{State::LEAVING_GAME, L"LEAVING_GAME"}
				};
			}

		public:
			static std::vector<State> values()
			{
				std::vector<State> temp;
				for (auto pair : pairs())
				{
					temp.push_back(pair.first);
				}
				return temp;
			}

			static std::wstring enumName(State value)
			{
				for (auto pair : pairs())
				{
					if (pair.first == value)
						return pair.second;
				}

				throw std::runtime_error("Enum not found.");
			}

			static int ordinal(State value)
			{
				std::vector<std::pair<State, std::wstring>> temp = pairs();
				for (std::size_t i = 0; i < temp.size(); i++)
				{
					if (temp[i].first == value)
						return i;
				}

				throw std::runtime_error("Enum not found.");
			}

			static State enumFromString(std::wstring value)
			{
				for (auto pair : pairs())
				{
					if (pair.second == value)
						return pair.first;
				}

				throw std::runtime_error("Enum not found.");
			}
		};

	};

}
