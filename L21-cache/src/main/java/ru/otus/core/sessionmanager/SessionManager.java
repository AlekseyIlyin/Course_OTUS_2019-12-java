package ru.otus.core.sessionmanager;

import org.hibernate.Session;
import ru.otus.hibernate.sessionmanager.DatabaseSessionHibernate;

public interface SessionManager extends AutoCloseable {
    Session beginSession();
    void commitSession();
    void rollbackSession();
    void close();

    DatabaseSession getCurrentSession();
}
