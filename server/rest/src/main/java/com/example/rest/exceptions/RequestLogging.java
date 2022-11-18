package com.example.rest.exceptions;

import com.example.model.*;
import org.slf4j.*;

public class RequestLogging {

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
        if (logger.isWarnEnabled()) {
            logger.warn("logger is warn enabled stub");
        }
        if (logger.isErrorEnabled()) {
            logger.error("logger is error enabled stub");
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
        if (logger.isWarnEnabled()) {
            logger.warn("logger is warn enabled stub");
        }
        if (logger.isErrorEnabled()) {
            logger.error("logger is error enabled stub");
        }
    }

    public static void logInvalidPlayer(Logger logger, InvalidPlayerDataException ex) {
        if (logger.isInfoEnabled()) {
            logger.info("Invalid Player " + ex);
        }
        if (logger.isDebugEnabled()) {
            logger.info("Invalid Player " + ex);
        }

        if (logger.isTraceEnabled()) {
            logger.info("Invalid Player " + ex);
        }
    }

    public static void logInvalidGame(Logger logger, InvalidGameInstanceException ex) {
        if (logger.isInfoEnabled()) {
            logger.info("Invalid Game " + ex);
        }
        if (logger.isDebugEnabled()) {
            logger.info("Invalid Game " + ex);
        }

        if (logger.isTraceEnabled()) {
            logger.info("Invalid Game " + ex);
        }
    }

    public static void logInvalidRequest(Logger logger, Exception e) {
        if (logger.isInfoEnabled()) {
            logger.info("logInvalidRequest " + e);
        }
        if (logger.isDebugEnabled()) {
            logger.info("logInvalidRequest " + e);
        }

        if (logger.isTraceEnabled()) {
            logger.info("logInvalidRequest " + e);
        }
    }
}
