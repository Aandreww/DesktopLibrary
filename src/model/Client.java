package model;

public interface Client {

    String getId();

    String getLogin();

    String getPass();

    String getPrivilege();

    void setId(String id);

    void setLogin(String login);

    void setPass(String pass);

    void setPrivilege(String privilege);

}
