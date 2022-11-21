// Fill out your copyright notice in the Description page of Project Settings.


#include "ServerSession.h"

#include "Json.h"
//#include "UObject/ConstructorHelpers.h"



//ServerSession::FString SERVER_BASE_URI = FString("http://localhost:8080/");
const FString ServerSession::SERVER_BASE_URI = FString("http://ec2-54-183-199-115.us-west-1.compute.amazonaws.com:8080/rest-0.0.1-SNAPSHOT/");
const FString ServerSession::SERVER_GAME_URI = SERVER_BASE_URI + "game/";
const FString ServerSession::SERVER_PLAYER_URI = SERVER_BASE_URI + "player/";



ServerSession::ServerSession()
{

	UE_LOG(LogTemp, Warning, TEXT("---------------------------------"));
	UE_LOG(LogTemp, Warning, TEXT("ServerSession Created!"));
	UE_LOG(LogTemp, Warning, TEXT("---------------------------------"));

}

ServerSession::~ServerSession()
{

	UE_LOG(LogTemp, Warning, TEXT("---------------------------------"));
	UE_LOG(LogTemp, Warning, TEXT("ServerSession Destroyed!"));
	UE_LOG(LogTemp, Warning, TEXT("---------------------------------"));


}



/// <summary>
/// ////////////////
/// </summary>New Code for handling REST/HTTP calls and 
/// JSON serialization/deserialization
/// 

FString ServerSession::SomePrintFunction()
{
	FString msg = FString("ServerSession::SomePrintFunction()");

	UE_LOG(LogTemp, Warning, TEXT("---------------------------------"));
	UE_LOG(LogTemp, Warning, TEXT("ServerSession::SomePrintFunction()")); // (msg.GetCharArray())); // );
	UE_LOG(LogTemp, Warning, TEXT("---------------------------------"));

	GEngine->AddOnScreenDebugMessage(-1, 1000, FColor::Yellow, msg);
	return msg;
}


//FString  ServerSession::createPlayerRequest(FString name)
//{
//
//	UE_LOG(LogTemp, Warning, TEXT("---------------------------------"));
//	UE_LOG(LogTemp, Warning, TEXT(" createPlayerRequest"));
//	UE_LOG(LogTemp, Warning, TEXT("---------------------------------"));
//
//	if (name.IsEmpty())
//	{
//		// create random name
//		// 
//		srand(time(NULL));
//		name = "Player " + rand() % 1000; // may need to include <cstdlib>
//		//name = "Player ?";
//	}
//	FHttpRequestRef request = FHttpModule::Get().CreateRequest();
//
//	TSharedRef<FJsonObject> requestObj = MakeShared<FJsonObject>();
//	requestObj->SetStringField("name", name);
//
//	FString requestBody;
//	TSharedRef<TJsonWriter<>> writer = TJsonWriterFactory<>::Create(&requestBody);
//	FJsonSerializer::Serialize(requestObj, writer);
//
//	request->OnProcessRequestComplete().BindUObject(this, &ServerSession::OnResponseReceived);
//
//	request->SetURL(ServerSession::SERVER_PLAYER_URI + "?name=" + name); // "UNKNOWN_NAME1");
//	request->SetVerb("POST");
//	request->SetHeader("Content-Type", "application/json");
//	request->SetContentAsString(requestBody);
//
//	request->ProcessRequest();
//
//	return(FString("Create Player sent"));
//}


//FString  ServerSession::getAllGamesRequest()
//{
//
//	UE_LOG(LogTemp, Warning, TEXT("---------------------------------"));
//	UE_LOG(LogTemp, Warning, TEXT(" getAllGamesRequest"));
//	UE_LOG(LogTemp, Warning, TEXT("---------------------------------"));
//
//	FHttpRequestRef request = FHttpModule::Get().CreateRequest();
//	TSharedRef<FJsonObject> requestObj = MakeShared<FJsonObject>();
//
//	/*FString requestBody;
//	TSharedRef<TJsonWriter<>> writer = TJsonWriterFactory<>::Create(&requestBody);
//	FJsonSerializer::Serialize(requestObj, writer);*/
//
//
//	request->OnProcessRequestComplete().BindUObject(this, &ServerSession::OnResponseReceived);
//
//	//request->SetHeader("Content-Type", "application/json");
//	request->SetVerb("GET");
//	request->SetURL(ServerSession::SERVER_GAME_URI);
//	request->ProcessRequest();
//
//	return(FString("getAllGamesRequest sent"));
//}


void ServerSession::OnResponseReceived(FHttpRequestPtr request, FHttpResponsePtr response, bool bConnectedSuccessfully)
{
	TSharedPtr<FJsonObject> responseObj;
	TSharedRef<TJsonReader<>> reader = TJsonReaderFactory<>::Create(response->GetContentAsString());
	FJsonSerializer::Deserialize(reader, responseObj);

	UE_LOG(LogTemp, Display, TEXT("Response %s"), *response->GetContentAsString());
	//UE_LOG(LogTemp, Display, TEXT("Title: %s"), *responseObj->GetStringField("title"));
	//*responseObj->HasField;
}



