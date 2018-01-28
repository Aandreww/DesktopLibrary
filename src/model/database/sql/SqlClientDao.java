package model.database.sql;

import model.BookImpl;
import model.ClientImpl;
import model.database.ClientDao;
import model.database.DaoFactory;
import model.database.DaoFactoryImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class SqlClientDao implements ClientDao {

    private String url = "jdbc:oracle:thin:@localhost:1521:XE";
    private String name = "Aandreww";
    private String password = "qwerty";
    private static String driver = "oracle.jdbc.driver.OracleDriver";

    DaoFactory daoFactory = new DaoFactoryImpl();

    private static Connection connection = null;

    SqlClientDao(Connection connection){
        this.connection = connection;
        /*try {
            Locale.setDefault(Locale.ENGLISH);
            Class.forName(driver);
            connection = DriverManager.getConnection(url, name, password);
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    @Override
    public ClientImpl getByID(String id){

        String sql = "SELECT * FROM Client WHERE ID = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.setString(1, id);

            ResultSet result = statement.executeQuery();

            if(result.next()){
                ClientImpl client = new ClientImpl();

                client.setId(result.getString("ID"));
                client.setLogin(result.getString("Login"));
                client.setPass(result.getString("Pass"));
                client.setPrivilege(result.getString("Privilege"));

                return client;
            }else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public ClientImpl getByName(String login){
        String sql = "SELECT * FROM Client WHERE Login = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.setString(1, login);

            ResultSet result = statement.executeQuery();

            if(result.next()){
                ClientImpl client = new ClientImpl();

                client.setId(result.getString("ID"));
                client.setLogin(result.getString("Login"));
                client.setPass(result.getString("Pass"));
                client.setPrivilege(result.getString("Privilege"));

                return client;
            }else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void getBook(String clientID, String bookID) {
        String sql = "INSERT INTO Client_book VALUES (?,?,?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.setString(1,UUID.randomUUID().toString());
            statement.setString(2,clientID);
            statement.setString(3,bookID);

            statement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void returnBook(String clientID, String bookID) {
        String sql = "DELETE FROM Client_book WHERE Client_ID = ? AND Book_ID = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.setString(1,clientID);
            statement.setString(2,bookID);

            statement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void create(String login, String pass) {
        String sql = "INSERT INTO Client VALUES (?,?,?,?)";
        String privilege = "Client";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.setString(1, UUID.randomUUID().toString());
            statement.setString(2, login);
            statement.setString(3, pass);
            statement.setString(4, privilege);

            statement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setPriv(String id, String priv) {
        String sql = "UPDATE Client SET Privilege = ? WHERE ID = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.setString(1,priv);
            statement.setString(2,id);

            statement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM Client WHERE ID = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            List<BookImpl> books = daoFactory.book().getByClientID(id);

            for(BookImpl book : books){
                daoFactory.book().delete(book.getTitle());
            }

            statement.setString(1,id);

            statement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<ClientImpl> getAll(){
        String sql = "SELECT * FROM Client";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            ResultSet result = statement.executeQuery();

            List<ClientImpl> clients = new ArrayList<>();
            while (result.next()) {
                ClientImpl client = new ClientImpl();

                client.setId(result.getString("ID"));
                client.setLogin(result.getString("Login"));
                client.setPass(result.getString("Pass"));
                client.setPrivilege(result.getString("Privilege"));

                clients.add(client);
            }

            return clients;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
