#include "PrintUtils.h"

namespace com::example::util
{

	void PrintUtils::title(std::any title)
	{
		println(L"");
		println(L"-" + title);
	}

const std::wstring PrintUtils::RESET = L"\033[0m";
}
