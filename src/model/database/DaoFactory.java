package model.database;

public interface DaoFactory {

    ClientDao client() throws Exception;

    BookDao book() throws Exception;

    AuthorDao author() throws Exception;

    void end() throws Exception;

}
