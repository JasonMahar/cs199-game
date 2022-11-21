// Fill out your copyright notice in the Description page of Project Settings.

#pragma once

#include "CoreMinimal.h"
#include "Http.h"
#include "Kismet/BlueprintFunctionLibrary.h"

#include "transport/ServerSession.h"

#include "MenusBlueprintFunctionLibrary.generated.h"

/**
 * 
 */
UCLASS(Blueprintable)
class UNREALCLIENT_API UMenusBlueprintFunctionLibrary : public UBlueprintFunctionLibrary
{
	GENERATED_BODY()


public:
	UMenusBlueprintFunctionLibrary();
	~UMenusBlueprintFunctionLibrary();


	public:
	
		UFUNCTION(BlueprintCallable)
			FString SomePrintFunction();

		//UFUNCTION(BlueprintCallable)
		//	FString createPlayerRequest(FString name);

	private:

		//static ServerSession theSession;
		ServerSession theSession;

	//	void OnResponseReceived(FHttpRequestPtr request, FHttpResponsePtr response, bool bConnectedSuccessfully);
	//
	//	void testHTTPRequest();
};
