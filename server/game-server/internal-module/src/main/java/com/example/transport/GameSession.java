package com.example.transport;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import com.example.model.*;
import org.json.*;

import java.io.*;
import java.math.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;

public class GameSession implements GameSessionInterface {
    private static String lastJSONReceived = "";
    private static final String SERVER_BASE_URI = "http://localhost:8080/";
    private static final String SERVER_GAME_URI = "http://localhost:8080/game/";
    private static final String SERVER_PLAYER_URI = "http://localhost:8080/player/";

    static String getLastJSONReceived() {
        return lastJSONReceived;
    }

    private static void setLastJSONReceived(String lastJSONReceived) {
        GameSession.lastJSONReceived = lastJSONReceived;
    }

    public static GameSessionInterface getGameSession() {
        return new GameSession();
    }

    private GameSession() {
    }

    public GameInstance createNewGame(PlayerData userPlayer) {
        System.out.println("GameSession.createNewGame() called.");
        if (userPlayer == null) {
            return null;
        } else {
            try {
                JSONObject json = new JSONObject(userPlayer);
                System.out.println("sending userPlayer json: " + json.toString());
                JSONObject returnedJson = postJsonToUrl(json, "http://localhost:8080/game/");
                if (returnedJson != null) {
                    System.out.println("received game json: " + returnedJson.toString());
                    setLastJSONReceived(returnedJson.toString());
                    return createGameInstanceFromJSON(returnedJson);
                }

                System.out.println("ERROR: received game json is null ");
            } catch (IOException var5) {
                System.out.println("GameSession.createNewGame() caught IOException = " + var5);
                System.out.println("Check if server is running and that SERVER_BASE_URI is set correct.");
                var5.printStackTrace();
            } catch (JSONException var6) {
                System.out.println("GameSession.createNewGame() caught JSONException = " + var6);
                var6.printStackTrace();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }

            return null;
        }
    }

    public Collection<GameInstance> getAllGames() {
        System.out.println("GameSession.getAllGames() called.");

        try {
            JSONArray returnedJson = readJsonArrayFromUrl("http://localhost:8080/game/");
            if (returnedJson == null) {
                System.out.println("ERROR: received game json is null ");
            } else {
                System.out.println("received game json: " + returnedJson.toString());
                setLastJSONReceived(returnedJson.toString());
            }
        } catch (IOException var3) {
            System.out.println("GameSession.getAllGames() caught IOException = " + var3);
            System.out.println("Check if server is running and that SERVER_BASE_URI is set correct.");
            var3.printStackTrace();
        } catch (JSONException var4) {
            System.out.println("GameSession.getAllGames() caught JSONException = " + var4);
            var4.printStackTrace();
        }

        return null;
    }

    public GameInstance getGameData(Integer gameID) {
        System.out.println("GameSession.getGameData() called with gameID: " + gameID);
        JSONObject returnedJson = null;

        try {
            returnedJson = readJsonFromUrl("http://localhost:8080/game/" + gameID);
            if (returnedJson == null) {
                System.out.println("ERROR: received game json is null ");
            } else {
                System.out.println("received game json: " + returnedJson.toString());
                setLastJSONReceived(returnedJson.toString());
            }
        } catch (IOException var4) {
            System.out.println("GameSession.getGameData() caught IOException = " + var4);
            System.out.println("Check if server is running and that SERVER_BASE_URI is set correct.");
            var4.printStackTrace();
        } catch (JSONException var5) {
            System.out.println("GameSession.getGameData() caught JSONException = " + var5);
            var5.printStackTrace();
        }

        return createGameInstanceFromJSON(returnedJson);
    }

    public GameInstance startGame(Integer gameID) {
        return this.updateGameState(gameID, GameInstance.GameState.IN_GAME);
    }

    public GameInstance updateGameState(Integer gameID, GameInstance.GameState state) {
        System.out.println("GameSession.updateGameState() called with gameID: " + gameID);

        try {
            JSONObject json = new JSONObject(state);
            System.out.println("sending updateGameState json: " + json.toString());
            System.out.println("\t to URI: http://localhost:8080/game/" + gameID);
            JSONObject returnedJson = putJsonToUrl(json, "http://localhost:8080/game/" + gameID);
            if (returnedJson != null) {
                System.out.println("received game json: " + returnedJson);
                setLastJSONReceived(returnedJson.toString());
                return createGameInstanceFromJSON(returnedJson);
            }

            System.out.println("ERROR: received game json = null ");
        } catch (IOException var6) {
            System.out.println("GameSession.updateGameState() caught IOException = " + var6);
            System.out.println("Check if server is running and that SERVER_BASE_URI is set correct.");
            var6.printStackTrace();
        } catch (JSONException var7) {
            System.out.println("GameSession.updateGameState() caught JSONException = " + var7);
            var7.printStackTrace();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public GameInstance joinGame(Integer gameID, PlayerData userPlayer) {
        System.out.println("GameSession.joinGame() called.");
        if (userPlayer == null) {
            return null;
        } else {
            try {
                JSONObject json = new JSONObject(userPlayer);
                System.out.println("sending userPlayer json: " + json.toString());
                JSONObject returnedJson = postJsonToUrl(json, "http://localhost:8080/game/" + gameID);
                if (returnedJson != null) {
                    System.out.println("received game json: " + returnedJson);
                    setLastJSONReceived(returnedJson.toString());
                    return createGameInstanceFromJSON(returnedJson);
                }

                System.out.println("ERROR: received game json = null ");
            } catch (IOException var6) {
                System.out.println("GameSession.joinGame() caught IOException = " + var6);
                System.out.println("Check if server is running and that SERVER_BASE_URI is set correct.");
                var6.printStackTrace();
            } catch (JSONException var7) {
                System.out.println("GameSession.joinGame() caught JSONException = " + var7);
                var7.printStackTrace();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }

            return null;
        }
    }

    public boolean quitGame(PlayerData userPlayer) {
        return false;
    }

    public GameInstance updatePlayerData(Integer gameID, PlayerData userPlayer) {
        if (userPlayer == null) {
            return null;
        } else {
            try {
                JSONObject json = new JSONObject(userPlayer);
                JSONObject returnedJson = putJsonToUrl(json, "http://localhost:8080/player/" + gameID);
                if (returnedJson != null) {
                    setLastJSONReceived(returnedJson.toString());
                    return createGameInstanceFromJSON(returnedJson);
                }

                System.out.println("ERROR: received game json = null ");
            } catch (IOException var6) {
                System.out.println("GameSession.updatePlayerData() caught IOException = " + var6);
                System.out.println("Check if server is running and that SERVER_BASE_URI is set correct.");
                var6.printStackTrace();
            } catch (JSONException var7) {
                System.out.println("GameSession.updatePlayerData() caught JSONException = " + var7);
                var7.printStackTrace();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }

            return null;
        }
    }

    public HashMap<Integer, PlayerData> getAllPlayersData() {
        System.out.println("GameSession.getAllPlayersData() called.");
        HashMap<Integer, PlayerData> playersMap = new HashMap<>();

        try {
            JSONArray jsonAry = readJsonArrayFromUrl("http://localhost:8080/player/");
            System.out.println(jsonAry.toString());
            setLastJSONReceived(jsonAry.toString());

            for(int i = 0; i < jsonAry.length(); ++i) {
                JSONObject json = jsonAry.getJSONObject(i);
                TemplePlayerData newPlayer = createTemplePlayerDataFromJSON(json);
                playersMap.put(newPlayer.getPublicID(), newPlayer);
            }
        } catch (IOException | JSONException var6) {
            System.out.println("GameSession.getPlayerData() caught Exception = " + var6);
            var6.printStackTrace();
        }

        return playersMap;
    }

    public TemplePlayerData getPlayerData(int ID) {
        System.out.println("GameSession.getPlayerData() called.");

        try {
            if (ID != 0) {
                JSONObject returnedJson = readJsonFromUrl("http://localhost:8080/player/" + ID);
                System.out.println(returnedJson.toString());
                setLastJSONReceived(returnedJson.toString());
                return createTemplePlayerDataFromJSON(returnedJson);
            }
        } catch (IOException | JSONException var3) {
            System.out.println("GameSession.getPlayerData() caught JSONException = " + var3);
            var3.printStackTrace();
        }

        return null;
    }

    public PlayerData createNewPlayer(String playerName) {
        if (playerName != null && !playerName.isBlank()) {
            try {
                JSONObject jsonSend = new JSONObject();
                jsonSend.put("name", playerName);
                System.out.println("sending json: " + jsonSend.toString());
                JSONObject returnedJson = postJsonToUrl(jsonSend, "http://localhost:8080/player/?name=" + playerName);
                System.out.println("received PlayerData json: " + returnedJson);
                PlayerData newPlayer = createTemplePlayerDataFromJSON(returnedJson);
                setLastJSONReceived(returnedJson.toString());
                return newPlayer;
            } catch (IOException | JSONException var5) {
                System.out.println("GameSession.getPlayerData() caught JSONException = " + var5);
                var5.printStackTrace();
                return null;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    private static GameInstance createGameInstanceFromJSON(JSONObject json) {
        GameInstance.GameState state = GameInstance.GameState.valueOf((String)json.get("gameState"));
        Integer id = json.getInt("id");
        JSONObject playersJSON = (JSONObject)json.get("players");
        Map<String, Object> playersMap = playersJSON.toMap();
        HashMap<Integer, PlayerData> playersHashMap = new HashMap<>();

        for (Map.Entry<String, Object> stringObjectEntry : playersMap.entrySet()) {
            Map.Entry<String, Object> entry = (Map.Entry) stringObjectEntry;
            Integer key = Integer.parseInt((String) entry.getKey());
            HashMap<String, Object> value = (HashMap) entry.getValue();
            TemplePlayerData nextPlayer = createTemplePlayerDataFromJSON(value);
            playersHashMap.put(key, nextPlayer);
        }

        return new GameInstance(state, id, playersHashMap);
    }

    private static TemplePlayerData createTemplePlayerDataFromJSON(HashMap<String, Object> map) {
        float x = ((BigDecimal)map.get("x")).floatValue();
        float y = ((BigDecimal)map.get("y")).floatValue();
        float speed = ((BigDecimal)map.get("speed")).floatValue();
        return new TemplePlayerData((Integer)map.get("publicID"), (String)map.get("name"), PlayerData.State.valueOf(map.get("state").toString()), x, y,
                PlayerData.Facing.valueOf(map.get("facing").toString()), speed);
    }

    private static TemplePlayerData createTemplePlayerDataFromJSON(JSONObject json) throws JSONException {
        return new TemplePlayerData(json.getInt("publicID"), json.getString("name"),
                PlayerData.State.valueOf(json.getString("state")), json.getFloat("x"), json.getFloat("y"),
                PlayerData.Facing.valueOf(json.getString("facing")), json.getFloat("speed"));
    }

    private static Projectile createCS195ProjectileFromJSON(JSONObject json) throws JSONException {
        Projectile projectile = new TempleProjectile(json.getInt("ID"),
                json.getFloat("xPosition"), json.getFloat("yPosition"), (PlayerData.Facing)json.get("facing"), json.getFloat("speed"));
        return projectile;
    }

    private static String readAll(InputStream is) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        StringBuilder sb = new StringBuilder();

        int cp;
        while((cp = rd.read()) != -1) {
            sb.append((char)cp);
        }

        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = (new URL(url)).openStream();

        JSONObject var5;
        try {
            String jsonText = readAll(is);
            JSONObject json = new JSONObject(jsonText);
            var5 = json;
        } finally {
            is.close();
        }

        return var5;
    }

    public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
        InputStream is = (new URL(url)).openStream();

        JSONArray var5;
        try {
            String jsonText = readAll(is);
            JSONArray json = new JSONArray(jsonText);
            var5 = json;
        } finally {
            is.close();
        }

        return var5;
    }

    public static JSONObject postJsonToUrl(JSONObject json, String urlString) throws Throwable {
        URL url = new URL(urlString);
        HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
        httpcon.setRequestMethod("POST");
        httpcon.setRequestProperty("Content-Type", "application/json; utf-8");
        httpcon.setRequestProperty("Accept", "application/json");
        httpcon.setDoOutput(true);
        Throwable var4 = null;
        String jsonText = null;

        try {
            OutputStream os = httpcon.getOutputStream();

            try {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            } finally {
                if (os != null) {
                    os.close();
                }

            }
        } catch (Throwable var19) {
            if (var4 == null) {
                var4 = var19;
            } else if (var4 != var19) {
                var4.addSuppressed(var19);
            }

            throw var4;
        }

        InputStream is = httpcon.getInputStream();

        try {
            jsonText = readAll(is);
            if (!jsonText.isEmpty()) {
                JSONObject returnedJson = new JSONObject(jsonText);
                JSONObject var8 = returnedJson;
                return var8;
            }
        } finally {
            is.close();
        }

        return null;
    }

    public static JSONObject putJsonToUrl(JSONObject json, String urlString) throws Throwable {
        if (json == null || urlString == null || urlString.isEmpty()) {
            System.out.println("ERROR: GameSession.putJsonToUrl(): json or url is null ");
        }

        URL url = new URL(urlString);
        HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
        httpcon.setRequestMethod("PUT");
        httpcon.setRequestProperty("Content-Type", "application/json; utf-8");
        httpcon.setRequestProperty("Accept", "application/json");
        httpcon.setDoOutput(true);
        Throwable var4 = null;
        String jsonText = null;

        try {
            OutputStream os = httpcon.getOutputStream();

            try {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            } finally {
                if (os != null) {
                    os.close();
                }

            }
        } catch (Throwable var19) {
            if (var4 == null) {
                var4 = var19;
            } else if (var4 != var19) {
                var4.addSuppressed(var19);
            }

            throw var4;
        }

        InputStream is = httpcon.getInputStream();

        JSONObject var8;
        try {
            jsonText = readAll(is);
            if (jsonText.isEmpty()) {
                return null;
            }

            JSONObject returnedJson = new JSONObject(jsonText);
            var8 = returnedJson;
        } finally {
            is.close();
        }

        return var8;
    }
}
