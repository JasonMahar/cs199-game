package com.example.model;

import com.example.util.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import java.io.*;

public class PlayerDataDeserializer extends JsonDeserializer<PlayerData> {

    @Override
    public PlayerData deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        PrintUtils.cyan("Deserializing: ");
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        ObjectNode root = mapper.readTree(p);
        return mapper.readValue(root.toString(), TemplePlayerData.class);
    }
}
