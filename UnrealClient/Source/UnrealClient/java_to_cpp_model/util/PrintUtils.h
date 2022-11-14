#pragma once

#include <string>
#include <iostream>
#include <any>
#include <memory>

namespace com::example::util
{

//JAVA TO C++ CONVERTER TODO TASK: Most Java annotations will not have direct C++ equivalents:
//ORIGINAL LINE: @SuppressWarnings("ALL") public class PrintUtils
	class PrintUtils : public std::enable_shared_from_this<PrintUtils>
	{

	public:
		static void title(std::any title);
		static const std::wstring RESET;

		template<typename T>
		static T green(T title)
		{
			const std::wstring GREEN = L"\u001B[32m";
			println(L"");
			println(GREEN + title + RESET);
			println(L"-" + title);
			return title;
		}

		template<typename T>
		static T red(T title)
		{
			const std::wstring RED = L"\u001B[31m";
			println(L"");
			println(RED + title + RESET);
			println(L"-" + title);
			return title;
		}

		template<typename T>
		static T cyan(T title)
		{
			const std::wstring CYAN = L"\u001B[36m";
			println(L"");
			println(CYAN + title + RESET);
			println(L"-" + title);
			return title;
		}

		template<typename T>
		static T println(T t)
		{
			std::wcout << t << std::endl;
			return t;
		}

		template<typename T>
		static T println(const std::wstring &description, T t)
		{
			std::wcout << L"description: " << description << L" ";
			std::wcout << t;
			return t;
		}
	};
}
