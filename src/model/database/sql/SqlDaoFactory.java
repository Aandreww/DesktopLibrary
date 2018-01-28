package model.database.sql;

import model.database.AuthorDao;
import model.database.BookDao;
import model.database.ClientDao;
import model.database.DaoFactory;

import java.sql.Connection;

public interface SqlDaoFactory{

    Connection getConnecting();

    void closeConnecting(Connection connection);

    String getConnectException();

    ClientDao client(Connection connection);

    AuthorDao author(Connection connection);

    BookDao book(Connection connection);

}
