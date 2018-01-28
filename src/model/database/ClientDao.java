package model.database;

import model.ClientImpl;

import java.util.List;

public interface ClientDao{

    ClientImpl getByID(String id);

    ClientImpl getByName(String login);

    void getBook(String clientID, String bookID);

    void returnBook(String clientID, String bookID);

    void create(String login, String pass);

    void setPriv(String id, String priv);

    void delete(String id);

    List<ClientImpl> getAll();

}
