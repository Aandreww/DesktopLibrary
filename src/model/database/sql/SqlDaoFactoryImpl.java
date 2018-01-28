package model.database.sql;

import model.database.AuthorDao;
import model.database.BookDao;
import model.database.ClientDao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

public class SqlDaoFactoryImpl implements SqlDaoFactory{

    private String url = "jdbc:oracle:thin:@localhost:1521:XE";
    private String name = "ndreww";
    private String password = "qwerty";

    @Override
    public Connection getConnecting(){
        try {
            Locale.setDefault(Locale.ENGLISH);
            Connection connection = DriverManager.getConnection(url, name, password);
            return connection;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public void closeConnecting(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getConnectException() {
        try {
            Locale.setDefault(Locale.ENGLISH);
            DriverManager.getConnection(url, name, password);
            return "Connected by second try";
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @Override
    public ClientDao client(Connection connection) {
        return new SqlClientDao(connection);
    }

    @Override
    public AuthorDao author(Connection connection) {
        return new SqlAuthorDao(connection);
    }

    @Override
    public BookDao book(Connection connection){
        return new SqlBookDao(connection);
    }
}
