package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="Client")
@XmlType(propOrder = {"login","pass","privilege"})
public class ClientImpl implements Client{

    private String id;
    private String login;
    private String pass;
    private String privilege;

    public ClientImpl(){ }

    public ClientImpl(String id, String login, String pass, String privilege){

        this.id = id;
        this.login = login;
        this.pass = pass;
        this.privilege = privilege;
    }

    @Override
    @XmlAttribute
    public String getId() {
        return id;
    }

    @Override
    @XmlElement
    public String getLogin() {
        return login;
    }

    @Override
    @XmlElement
    public String getPass() {
        return pass;
    }

    @Override
    @XmlElement
    public String getPrivilege() {
        return privilege;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }
}
