// Copyright Epic Games, Inc. All Rights Reserved.

#pragma once

#include "CoreMinimal.h"
#include "GameFramework/GameModeBase.h"
#include "Http.h"
#include "UnrealClientGameMode.generated.h"


UCLASS(minimalapi, Blueprintable)
class AUnrealClientGameMode : public AGameModeBase
{
	GENERATED_BODY()

public:
	AUnrealClientGameMode();
	virtual void StartPlay() override;

};



