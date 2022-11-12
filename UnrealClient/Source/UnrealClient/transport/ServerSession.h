// 

#pragma once

#include "CoreMinimal.h"
#include "Http.h"


/**
 * 
 */
UCLASS(minimalapi, Blueprintable)
class UNREALCLIENT_API ServerSession
{
	GENERATED_BODY()

public:
	ServerSession();
	~ServerSession();


private:
	static const FString SERVER_BASE_URI_STRING;
	static const FString PLAYER_URI_STRING;

public:

	UFUNCTION(BlueprintCallable)
		static FString SomePrintFunction();

	UFUNCTION(BlueprintCallable)
		FString createPlayerRequest(FString name);

private:
	void OnResponseReceived(FHttpRequestPtr request, FHttpResponsePtr response, bool bConnectedSuccessfully);

	void testHTTPRequest();

};
