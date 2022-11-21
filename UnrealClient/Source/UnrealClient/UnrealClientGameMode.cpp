// Copyright Epic Games, Inc. All Rights Reserved.

#include "UnrealClientGameMode.h"
#include "UnrealClientCharacter.h"
#include "Json.h"
#include "UObject/ConstructorHelpers.h"


AUnrealClientGameMode::AUnrealClientGameMode()
{
	// set default pawn class to our Blueprinted character
	static ConstructorHelpers::FClassFinder<APawn> PlayerPawnBPClass(TEXT("/Game/ThirdPerson/Blueprints/BP_ThirdPersonCharacter"));
	if (PlayerPawnBPClass.Class != NULL)
	{
		DefaultPawnClass = PlayerPawnBPClass.Class;
	}
}

/// <summary>
/// New Code for handling REST/HTTP calls and 
/// JSON serialization/deserialization
/// </summary>
/// 
/// 
void AUnrealClientGameMode::StartPlay()
{
	Super::StartPlay();

	//std::cout << "!!!!!!!!!! http test - in console";
	UE_LOG(LogTemp, Warning, TEXT("---------------------------------"));
	UE_LOG(LogTemp, Warning, TEXT("AUnrealClientGameMode::StartPlay()"));
	UE_LOG(LogTemp, Warning, TEXT("---------------------------------"));

}