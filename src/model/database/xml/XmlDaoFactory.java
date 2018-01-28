package model.database.xml;

import model.database.AuthorDao;
import model.database.BookDao;
import model.database.ClientDao;
import model.database.DaoFactory;

public interface XmlDaoFactory{

    ClientDao client() throws Exception;

    BookDao book() throws Exception;

    AuthorDao author() throws Exception;
}
