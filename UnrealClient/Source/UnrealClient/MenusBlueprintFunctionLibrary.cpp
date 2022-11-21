// Fill out your copyright notice in the Description page of Project Settings.


#include "MenusBlueprintFunctionLibrary.h"

#include "transport/ServerSession.h"


// STATIC ATTRIBUTES AND METHODS
//
//ServerSession UMenusBlueprintFunctionLibrary::theSession = ServerSession();



UMenusBlueprintFunctionLibrary::UMenusBlueprintFunctionLibrary()
{
	theSession = ServerSession();
}

UMenusBlueprintFunctionLibrary::~UMenusBlueprintFunctionLibrary()
{
}

FString UMenusBlueprintFunctionLibrary::SomePrintFunction()
{
	return theSession.SomePrintFunction();
}

//FString UMenusBlueprintFunctionLibrary::createPlayerRequest(FString name)
//{
//	return theSession.createPlayerRequest(name);
//}



