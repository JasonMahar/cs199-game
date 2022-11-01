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
/// ////////////////
/// </summary>New Code for handling REST/HTTP calls and 
/// JSON serialization/deserialization
/// 

void AUnrealClientGameMode::SomePrintFunction()
{
	GEngine->AddOnScreenDebugMessage(-1, 1000, FColor::Blue, FString::Printf(TEXT("It Works!")));

}
void AUnrealClientGameMode::StartPlay()
{
	Super::StartPlay();

	//std::cout << "!!!!!!!!!! http test - in console";
	UE_LOG(LogTemp, Warning, TEXT("---------------------------------"));
	UE_LOG(LogTemp, Warning, TEXT("!!!!! http test - in console !!!!"));
	UE_LOG(LogTemp, Warning, TEXT("---------------------------------"));

	FHttpRequestRef request = FHttpModule::Get().CreateRequest();

	TSharedRef<FJsonObject> requestObj = MakeShared<FJsonObject>();
	requestObj->SetStringField("title", "New Title!");

	FString requestBody;
	TSharedRef<TJsonWriter<>> writer = TJsonWriterFactory<>::Create(&requestBody);
	FJsonSerializer::Serialize(requestObj, writer);

	request->OnProcessRequestComplete().BindUObject(this, &AUnrealClientGameMode::OnResponseReceived);

	//request->SetURL("https://jsonplaceholder.typicode.com/posts/1");
	//request->SetVerb("GET");
	request->SetURL("https://jsonplaceholder.typicode.com/posts");
	request->SetVerb("POST");

	request->SetHeader("Content-Type", "application/json");
	request->SetContentAsString(requestBody);
	request->ProcessRequest();
}


void AUnrealClientGameMode::OnResponseReceived(FHttpRequestPtr request, FHttpResponsePtr response, bool bConnectedSuccessfully)
{
	TSharedPtr<FJsonObject> responseObj;
	TSharedRef<TJsonReader<>> reader = TJsonReaderFactory<>::Create(response->GetContentAsString());
	FJsonSerializer::Deserialize(reader, responseObj);

	UE_LOG(LogTemp, Display, TEXT("Response %s"), *response->GetContentAsString());
	UE_LOG(LogTemp, Display, TEXT("Title: %s"), *responseObj->GetStringField("title"));
	//*responseObj->HasField;
}

