package model.database;

import model.Book;
import model.BookImpl;

import java.util.List;

public interface BookDao {

    BookImpl getByID(String id);

    List<BookImpl> getByAuthor(String id);

    BookImpl getByName(String title);

    void create(String title,String year,String genre, String author);

    void update(BookImpl book);

    void delete(String name);

    List<BookImpl> getByClientID(String id);

    List<BookImpl> getAll();

    List<BookImpl> getByPartOfName(String name);

}
