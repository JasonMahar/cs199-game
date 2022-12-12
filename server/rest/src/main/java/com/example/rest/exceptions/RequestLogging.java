package com.example.rest.exceptions;

import com.example.model.*;
import org.slf4j.*;
// Log requests
public class RequestLogging {
    private static final Logger logger = LoggerFactory.getLogger("tracer");
    public static void logNoActivePlayers(Logger logger, StringBuilder sb) {
        StringBuilder s = new StringBuilder("no active players.");
        s.append(sb.toString());
        if (logger.isInfoEnabled()) {
            logger.info(sb.toString());
        }
        if (logger.isDebugEnabled()) {
            logger.debug(sb.toString());
        }

        if (logger.isTraceEnabled()) {
            logger.trace(sb.toString());
        }
    }

    public static void logNoActiveGames(Logger logger, StringBuilder sb) {
        StringBuilder s = new StringBuilder("no active games.");
        s.append(sb.toString());
        if (logger.isInfoEnabled()) {
            logger.info(sb.toString());
        }
        if (logger.isDebugEnabled()) {
            logger.debug(sb.toString());
        }

        if (logger.isTraceEnabled()) {
            logger.trace(sb.toString());
        }
    }

    public static void logInvalidPlayer(Logger logger, InvalidPlayerDataException ex) {
        if (logger.isInfoEnabled()) {
            logger.error("Invalid Player " + ex);
        }
        if (logger.isDebugEnabled()) {
            logger.error("Invalid Player " + ex);
        }

        if (logger.isTraceEnabled()) {
            logger.error("Invalid Player " + ex);
        }
    }

    public static void logInvalidGame(Logger logger, InvalidGameInstanceException ex) {
        if (logger.isInfoEnabled()) {
            logger.error("Invalid Game " + ex);
        }
        if (logger.isDebugEnabled()) {
            logger.error("Invalid Game " + ex);
        }

        if (logger.isTraceEnabled()) {
            logger.error("Invalid Game " + ex);
        }
    }

    public static void logInvalidRequest(Logger logger, Exception e) {
        if (logger.isInfoEnabled()) {
            logger.error("logInvalidRequest " + e);
        }
        if (logger.isDebugEnabled()) {
            logger.error("logInvalidRequest " + e);
        }

        if (logger.isTraceEnabled()) {
            logger.error("logInvalidRequest " + e);
        }
    }
}
