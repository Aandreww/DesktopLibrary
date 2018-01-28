package model.database.xml;

import model.database.AuthorDao;
import model.database.BookDao;
import model.database.ClientDao;

public class XmlDaoFactoryImpl implements XmlDaoFactory {
    @Override
    public ClientDao client() throws Exception {
        return new XmlClientDao();
    }

    @Override
    public BookDao book() throws Exception {
        return new XmlBookDao();
    }

    @Override
    public AuthorDao author() throws Exception {
        return new XmlAuthorDao();
    }

}
