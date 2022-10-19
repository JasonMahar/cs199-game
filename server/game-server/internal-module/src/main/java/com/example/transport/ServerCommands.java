package com.example.transport;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//



import com.example.model.*;

import java.util.*;

public enum ServerCommands {
    CREATE_GAME {
        public String toString() {
            return "Create Game";
        }

        public GameInstance send(String params, PlayerData player, Integer gameID) {
            if (player == null && params != null && !params.isBlank()) {
                player = ServerCommands.getGameSession().createNewPlayer(params);
            }

            GameInstance game = ServerCommands.getGameSession().createNewGame(player);
            if (game == null) {
                ServerCommands.resultMessage = "ERROR: CREATE_GAME.send() could not create GameInstance";
            } else {
                ServerCommands.resultMessage = "SUCCESS";
            }

            return game;
        }
    },
    GET_GAME_INFO {
        public String toString() {
            return "Get Game Info";
        }

        public GameInstance send(String params, PlayerData player, Integer gameID) {
            GameInstance game = ServerCommands.getGameSession().getGameData(gameID);
            if (game == null) {
                ServerCommands.resultMessage = "ERROR: GET_GAME_INFO.send() could not create GameInstance";
            } else {
                ServerCommands.resultMessage = "SUCCESS";
            }

            return game;
        }
    },
    CREATE_PLAYER {
        public String toString() {
            return "Create Player";
        }

        public GameInstance send(String params, PlayerData player, Integer gameID) {
            if (params != null && !params.isEmpty()) {
                player = ServerCommands.getGameSession().createNewPlayer(params);
                if (player == null) {
                    ServerCommands.resultMessage = "ERROR: CREATE_PLAYER.send() Could not create new player.";
                    return null;
                } else {
                    HashMap<Integer, PlayerData> players = new HashMap();
                    players.put(player.getPublicID(), player);
                    GameInstance dummyGame = new GameInstance(GameInstance.GameState.UNKNOWN, GameDesignVars.BAD_GAME_ID, players);
                    ServerCommands.resultMessage = "SUCCESS";
                    return dummyGame;
                }
            } else {
                ServerCommands.resultMessage = "ERROR: CREATE_PLAYER.send() Must enter player name.";
                return null;
            }
        }
    },
    JOIN_GAME {
        public String toString() {
            return "Join Game";
        }

        public GameInstance send(String params, PlayerData player, Integer gameID) {
            if (gameID == GameDesignVars.BAD_GAME_ID) {
                System.out.println("\tERROR: JOIN_GAME send() Bad gameID: " + gameID);
                ServerCommands.resultMessage = "ERROR: JOIN_GAME send() Bad gameID: " + gameID;
                return null;
            } else if (player == null) {
                System.out.println("\tERROR: JOIN_GAME send() PlayerData is null.");
                ServerCommands.resultMessage = "ERROR: JOIN_GAME.send() PlayerData is null.";
                return null;
            } else {
                GameInstance game = ServerCommands.getGameSession().joinGame(gameID, player);
                if (game == null) {
                    System.out.println("\tERROR: JOIN_GAME send()  could not create GameInstance.");
                    ServerCommands.resultMessage = "ERROR: JOIN_GAME send() could not create GameInstance";
                } else {
                    ServerCommands.resultMessage = "SUCCESS";
                }

                return game;
            }
        }
    },
    START_GAME {
        public String toString() {
            return "Start Game";
        }

        public GameInstance send(String params, PlayerData player, Integer gameID) {
            GameInstance game = ServerCommands.getGameSession().startGame(gameID);
            if (game == null) {
                ServerCommands.resultMessage = "ERROR: Unable to start game.";
            } else {
                ServerCommands.resultMessage = "SUCCESS";
            }

            return game;
        }
    },
    LEAVE_GAME {
        public String toString() {
            return "Leave Game";
        }

        public GameInstance send(String params, PlayerData player, Integer gameID) {
            ServerCommands.getGameSession().quitGame(player);
            ServerCommands.resultMessage = "SUCCESS";
            return null;
        }
    },
    GET_ALL_PLAYERS_DATA {
        public String toString() {
            return "List All Players";
        }

        public GameInstance send(String params, PlayerData player, Integer gameID) {
            HashMap<Integer, PlayerData> players = ServerCommands.getGameSession().getAllPlayersData();
            if (players == null) {
                ServerCommands.resultMessage = "ERROR: GET_ALL_PLAYERS_DATA.send() PlayerData player is null.";
                return null;
            } else {
                GameInstance dummyGame = new GameInstance(GameInstance.GameState.UNKNOWN, GameDesignVars.BAD_GAME_ID, players);
                ServerCommands.resultMessage = "SUCCESS";
                return dummyGame;
            }
        }
    },
    GET_PLAYER_DATA {
        public String toString() {
            return "Get Player Info";
        }

        public GameInstance send(String params, PlayerData player, Integer gameID) {
            player = ServerCommands.getGameSession().getPlayerData(player.getPublicID());
            if (player == null) {
                return null;
            } else {
                HashMap<Integer, PlayerData> players = new HashMap();
                players.put(player.getPublicID(), player);
                GameInstance dummyGame = new GameInstance(GameInstance.GameState.UNKNOWN, GameDesignVars.BAD_GAME_ID, players);
                ServerCommands.resultMessage = "SUCCESS";
                return dummyGame;
            }
        }
    },
    UPDATE_PLAYER_DATA {
        public String toString() {
            return "Update Player";
        }

        public GameInstance send(String params, PlayerData player, Integer gameID) {
            ServerCommands.resultMessage = "SUCCESS";
            return ServerCommands.getGameSession().updatePlayerData(gameID, player);
        }
    };

    public static final String SUCCESS_MESSAGE = "SUCCESS";
    private static GameSessionInterface gameSession = null;
    private static String resultMessage = "";

    private ServerCommands() {
    }

    public abstract String toString();

    public abstract GameInstance send(String var1, PlayerData var2, Integer var3);

    public static ServerCommands getServerCommandsFromString(String commandString) {
        if (commandString != null && !commandString.isEmpty()) {
            ServerCommands[] var4;
            int var3 = (var4 = values()).length;

            for(int var2 = 0; var2 < var3; ++var2) {
                ServerCommands command = var4[var2];
                if (command.toString().equals(commandString)) {
                    resultMessage = "SUCCESS";
                    return command;
                }
            }

            resultMessage = "ERROR: ServerCommands.getServerCommandsFromString() No ServerCommands found for commandString:" + commandString;
            return null;
        } else {
            resultMessage = "ERROR: ServerCommands.getServerCommandsFromString() Must enter player a command string.";
            return null;
        }
    }

    public static String getLastJSONReceived() {
        return GameSession.getLastJSONReceived();
    }

    public static String getLastResultMessage() {
        return resultMessage;
    }

    private static GameSessionInterface getGameSession() {
        if (gameSession == null) {
            gameSession = GameSession.getGameSession();
        }

        return gameSession;
    }
}
