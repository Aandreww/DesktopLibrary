package model.database;

import model.database.sql.SqlDaoFactoryImpl;
import model.database.xml.XmlDaoFactory;
import model.database.xml.XmlDaoFactoryImpl;

import java.sql.Connection;

public class DaoFactoryImpl implements DaoFactory{

    SqlDaoFactoryImpl sql = new SqlDaoFactoryImpl();
    XmlDaoFactory xml = new XmlDaoFactoryImpl();
    Connection connection = null;

    @Override
    public ClientDao client() throws Exception {
        connection = sql.getConnecting();
        if(connection!=null) {
            return sql.client(connection);
        }else{
            //должно быть подключение к xml файлу и считывание клиента с него
            return xml.client();
        }
    }

    @Override
    public BookDao book() throws Exception {
        connection = sql.getConnecting();
        if(connection!=null) {
            return sql.book(connection);
        }else{
            //должно быть подключение к xml файлу и считывание книги с него
            return xml.book();
        }
    }

    @Override
    public AuthorDao author() throws Exception {
        connection = sql.getConnecting();
        if(connection!=null) {
            return sql.author(connection);
        }else{
            //должно быть подключение к xml файлу и считывание автора с него
            return xml.author();
        }
    }

    @Override
    public void end() throws Exception {
        if(connection!=null) {
            sql.closeConnecting(connection);
        }
    }
}
