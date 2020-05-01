package ru.otus.l10.core.sessionmanager;

import org.hibernate.Session;
import ru.otus.l10.hibernate.sessionmanager.DatabaseSessionHibernate;

public interface SessionManager extends AutoCloseable {
    Session beginSession();
    void commitSession();
    void rollbackSession();
    void close();

    DatabaseSession getCurrentSession();
}
