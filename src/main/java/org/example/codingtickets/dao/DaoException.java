package org.example.codingtickets.dao;

// Hériter de RuntimeException permet d'utiliser l'exception sans forcer le try/catch partout
public class DaoException extends RuntimeException {

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}