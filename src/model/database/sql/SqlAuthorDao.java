package model.database.sql;

import model.Author;
import model.AuthorImpl;
import model.BookImpl;
import model.database.AuthorDao;
import model.database.DaoFactory;
import model.database.DaoFactoryImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class SqlAuthorDao implements AuthorDao {

    private String url = "jdbc:oracle:thin:@localhost:1521:XE";
    private String name = "Aandreww";
    private String password = "qwerty";
    private static String driver = "oracle.jdbc.driver.OracleDriver";

    private DaoFactory daoFactory = new DaoFactoryImpl();

    private static Connection connection = null;

    SqlAuthorDao(Connection connection){
       this.connection = connection;
        /* try {
            Locale.setDefault(Locale.ENGLISH);
            Class.forName(driver);
            connection = DriverManager.getConnection(url, name, password);
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    @Override
    public AuthorImpl getByID(String id) {

        String sql = "SELECT * FROM Author WHERE ID = ?";
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
                AuthorImpl author = new AuthorImpl();

                author.setId(result.getString("ID"));
                author.setName(result.getString("Name"));

                return author;
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
    public AuthorImpl getByName(String name) {

        String sql = "SELECT * FROM Author WHERE Name = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.setString(1, name);

            ResultSet result = statement.executeQuery();

            if(result.next()){
                AuthorImpl author = new AuthorImpl();

                author.setId(result.getString("ID"));
                author.setName(result.getString("Name"));

                return author;
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
    public AuthorImpl getByBook(String name) {
        String sql = "SELECT Author.id FROM Author, Author_book, Book WHERE Author.id = Author_book.Author_id and Book.id=Author_book.Book_id and book.title=?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.setString(1, name);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return getByID(result.getString("ID"));
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
    public void create(String name) {

        String sql = "INSERT INTO Author VALUES (?,?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.setString(1, UUID.randomUUID().toString());
            statement.setString(2,name);

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
    public void update(AuthorImpl author) {

    }

    @Override
    public void delete(String name) {
        String sql = "DELETE FROM Author WHERE ID = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            List<BookImpl> books = daoFactory.book().getByAuthor(daoFactory.author().getByName(name).getId());

            for(BookImpl book : books){
                daoFactory.book().delete(book.getTitle());
            }

            statement.setString(1,daoFactory.author().getByName(name).getId());
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
    public List<AuthorImpl> getAll() {
        String sql = "SELECT * FROM Author";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            ResultSet result = statement.executeQuery();

            List<AuthorImpl> authors = new ArrayList<>();
            while (result.next()) {
                AuthorImpl author = new AuthorImpl();

                author.setId(result.getString("ID"));
                author.setName(result.getString("Name"));

                authors.add(author);
            }
            return authors;
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
