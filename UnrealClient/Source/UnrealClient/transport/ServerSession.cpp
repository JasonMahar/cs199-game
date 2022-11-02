// Fill out your copyright notice in the Description page of Project Settings.


#include "transport/ServerSession.h"

ServerSession::ServerSession()
{
}

ServerSession::~ServerSession()
{
}



/// <summary>
/// ////////////////
/// </summary>New Code for handling REST/HTTP calls and 
/// JSON serialization/deserialization
/// 

FString AServerSession::SomePrintFunction()
{
	FString msg = FString("It Works!");

	UE_LOG(LogTemp, Warning, TEXT("---------------------------------"));
	UE_LOG(LogTemp, Warning, TEXT("It Works!")); // (msg.GetCharArray())); // );
	UE_LOG(LogTemp, Warning, TEXT("---------------------------------"));

	GEngine->AddOnScreenDebugMessage(-1, 1000, FColor::Blue, msg);
	return msg;
}

void AServerSession::testHTTPRequest()
{

	FHttpRequestRef request = FHttpModule::Get().CreateRequest();

	TSharedRef<FJsonObject> requestObj = MakeShared<FJsonObject>();
	requestObj->SetStringField("Title", "Title 2");

	FString requestBody;
	TSharedRef<TJsonWriter<>> writer = TJsonWriterFactory<>::Create(&requestBody);
	FJsonSerializer::Serialize(requestObj, writer);

	request->OnProcessRequestComplete().BindUObject(this, &AServerSession::OnResponseReceived);

	//request->SetURL("https://jsonplaceholder.typicode.com/posts/1");
	//request->SetVerb("GET");
	request->SetURL("https://jsonplaceholder.typicode.com/posts");
	request->SetVerb("POST");

	request->SetHeader("Content-Type", "application/json");
	request->SetContentAsString(requestBody);
	request->ProcessRequest();
}

FString  AServerSession::createPlayerRequest(FString name)
{

	UE_LOG(LogTemp, Warning, TEXT("---------------------------------"));
	UE_LOG(LogTemp, Warning, TEXT(" createPlayerRequest"));
	UE_LOG(LogTemp, Warning, TEXT("---------------------------------"));

	if (name.IsEmpty())
	{
		// create random name
		// 
		srand(time(NULL));
		name = "Player " + rand() % 1000; // may need to include <cstdlib>
		//name = "Player ?";
	}
	FHttpRequestRef request = FHttpModule::Get().CreateRequest();

	TSharedRef<FJsonObject> requestObj = MakeShared<FJsonObject>();
	requestObj->SetStringField("name", name);

	FString requestBody;
	TSharedRef<TJsonWriter<>> writer = TJsonWriterFactory<>::Create(&requestBody);
	FJsonSerializer::Serialize(requestObj, writer);

	request->OnProcessRequestComplete().BindUObject(this, &AServerSession::OnResponseReceived);

	//request->SetURL("https://jsonplaceholder.typicode.com/posts/1");
	//request->SetVerb("GET");
	request->SetURL(SERVER_BASE_URI_STRING + PLAYER_URI_STRING + "?name=BAD_NAME"); // + name);
	request->SetVerb("POST");

	request->SetHeader("Content-Type", "application/json");
	request->SetContentAsString(requestBody);
	request->ProcessRequest();

	return(FString("Create Player sent"));
}


void AServerSession::OnResponseReceived(FHttpRequestPtr request, FHttpResponsePtr response, bool bConnectedSuccessfully)
{
	TSharedPtr<FJsonObject> responseObj;
	TSharedRef<TJsonReader<>> reader = TJsonReaderFactory<>::Create(response->GetContentAsString());
	FJsonSerializer::Deserialize(reader, responseObj);

	UE_LOG(LogTemp, Display, TEXT("Response %s"), *response->GetContentAsString());
	//UE_LOG(LogTemp, Display, TEXT("Title: %s"), *responseObj->GetStringField("title"));
	//*responseObj->HasField;
}

