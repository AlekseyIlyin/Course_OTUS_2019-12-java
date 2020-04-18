package ru.otus.l10.jdbc.dao;

public class SqlQueryBuilderNotSupported extends RuntimeException {
    public SqlQueryBuilderNotSupported(String message) {
        super(message);
    }
}
