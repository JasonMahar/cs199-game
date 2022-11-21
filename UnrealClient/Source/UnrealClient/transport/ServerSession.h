// 

#pragma once

#include "CoreMinimal.h"
#include "GameFramework/GameModeBase.h"
#include "Http.h"


/**
 * 
 */
UCLASS(minimalapi, Blueprintable)
class UNREALCLIENT_API ServerSession
class ServerSession
{
	//GENERATED_BODY()

public:
	ServerSession();
	~ServerSession();


private:
	static const FString SERVER_BASE_URI;
	static const FString SERVER_PLAYER_URI;
	static const FString SERVER_GAME_URI;
	

public:

	UFUNCTION(BlueprintCallable)
		FString SomePrintFunction();


	//UFUNCTION(BlueprintCallable)
		//FString createPlayerRequest(FString name);

	//UFUNCTION(BlueprintCallable)
	//	FString getAllGamesRequest();

private:
	void OnResponseReceived(FHttpRequestPtr request, FHttpResponsePtr response, bool bConnectedSuccessfully);

	/*
	void getAllGamesRequest();
	*/
};
