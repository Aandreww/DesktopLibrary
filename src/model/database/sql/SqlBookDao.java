package model.database.sql;

import model.Book;
import model.BookImpl;
import model.ClientImpl;
import model.database.BookDao;
import model.database.DaoFactory;
import model.database.DaoFactoryImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class SqlBookDao implements BookDao {

    private String url = "jdbc:oracle:thin:@localhost:1521:XE";
    private String name = "Aandreww";
    private String password = "qwerty";
    private static String driver = "oracle.jdbc.driver.OracleDriver";

    private DaoFactory daoFactory = new DaoFactoryImpl();

    private static Connection connection = null;

    SqlBookDao(Connection connection){
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
    public BookImpl getByID(String id) {
        String sql = "SELECT * FROM Book WHERE ID = ?";
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
                BookImpl book = new BookImpl();

                book.setId(result.getString("ID"));
                book.setTitle(result.getString("Title"));
                book.setYear(Integer.parseInt(result.getString("Year")));
                book.setGenre(result.getString("Genre"));

                return book;
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
    public List<BookImpl> getByAuthor(String id) {
        String sql = "SELECT book.id, book.title, book.year, book.genre FROM Book,Author_book WHERE Book.ID=Author_book.Book_ID AND Author_book.Author_ID=?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.setString(1,id);

            ResultSet result = statement.executeQuery();

            List<BookImpl> books = new ArrayList<>();
            while (result.next()) {
                BookImpl book = new BookImpl();

                book.setId(result.getString("ID"));
                book.setTitle(result.getString("Title"));
                book.setYear(Integer.parseInt(result.getString("Year")));
                book.setGenre(result.getString("Genre"));

                books.add(book);
            }

            return books;
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
    public BookImpl getByName(String title) {
        String sql = "SELECT * FROM Book WHERE Title = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.setString(1, title);

            ResultSet result = statement.executeQuery();

            if(result.next()){
                BookImpl book = new BookImpl();

                book.setId(result.getString("ID"));
                book.setTitle(result.getString("Title"));
                book.setYear(Integer.parseInt(result.getString("Year")));
                book.setGenre(result.getString("Genre"));

                return book;
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
    public void create(String title, String year, String genre, String author) {
        String sql = "INSERT INTO Book VALUES (?,?,?,?)";
        String sql1 = "INSERT INTO Author_book VALUES (?,?,?)";
        String bookID = UUID.randomUUID().toString();
        PreparedStatement statement = null;
        PreparedStatement statement1 = null;
        try {
            statement = connection.prepareStatement(sql);
            statement1 = connection.prepareStatement(sql1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {

            statement.setString(1, bookID);
            statement.setString(2,title);
            statement.setInt(3,Integer.parseInt(year));
            statement.setString(4,genre);

            statement.executeUpdate();

            statement1.setString(1, UUID.randomUUID().toString());
            statement1.setString(2,daoFactory.author().getByName(author).getId());
            statement1.setString(3,daoFactory.book().getByName(title).getId());

            statement1.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                statement.close();
                statement1.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void update(BookImpl book) {}

    @Override
    public void delete(String name) {
        String sql = "DELETE FROM Book WHERE ID = ?";
        String sql1 = "DELETE FROM Author_book WHERE Book_ID = ?";
        String sql2 = "DELETE FROM Client_book WHERE Book_ID = ?";
        PreparedStatement statement = null;
        PreparedStatement statement1 = null;
        PreparedStatement statement2 = null;
        try {
            statement = connection.prepareStatement(sql);
            statement1 = connection.prepareStatement(sql1);
            statement2 = connection.prepareStatement(sql2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement1.setString(1,daoFactory.book().getByName(name).getId());

            statement1.executeUpdate();

            statement2.setString(1,daoFactory.book().getByName(name).getId());

            statement2.executeUpdate();

            statement.setString(1,daoFactory.book().getByName(name).getId());

            statement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                statement.close();
                statement1.close();
                statement2.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<BookImpl> getByClientID(String id) {
        String sql = "SELECT book.id, book.title, book.year, book.genre FROM Book,Client_book WHERE Book.ID=Client_book.Book_ID AND Client_book.Client_ID=?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.setString(1,id);

            ResultSet result = statement.executeQuery();

            List<BookImpl> books = new ArrayList<>();
            while (result.next()) {
                BookImpl book = new BookImpl();

                book.setId(result.getString("ID"));
                book.setTitle(result.getString("Title"));
                book.setYear(Integer.parseInt(result.getString("Year")));
                book.setGenre(result.getString("Genre"));

                books.add(book);
            }

            return books;
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
    public List<BookImpl> getAll() {
        String sql = "SELECT * FROM Book";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            ResultSet result = statement.executeQuery();

            List<BookImpl> books = new ArrayList<>();
            while (result.next()) {
                BookImpl book = new BookImpl();

                book.setId(result.getString("ID"));
                book.setTitle(result.getString("Title"));
                book.setYear(Integer.parseInt(result.getString("Year")));
                book.setGenre(result.getString("Genre"));

                books.add(book);
            }
            return books;
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
    public List<BookImpl> getByPartOfName(String name) {
        String sql = "SELECT book.id, book.title, book.year, book.genre FROM Book WHERE Book.title LIKE '%"+ name +"%'";
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {

            ResultSet result = statement.executeQuery(sql);

            List<BookImpl> books = new ArrayList<>();
            while (result.next()) {
                BookImpl book = new BookImpl();

                book.setId(result.getString("ID"));
                book.setTitle(result.getString("Title"));
                book.setYear(Integer.parseInt(result.getString("Year")));
                book.setGenre(result.getString("Genre"));

                books.add(book);
            }

            return books;
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
