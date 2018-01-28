package model.database.xml.jaxb;

import model.ClientImpl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="Clients")
@XmlAccessorType(XmlAccessType.FIELD)
public class Clietns {

    @XmlElement(name="Client")
    private List<ClientImpl> clients = null;

    public List<ClientImpl> getClients() {
        return clients;
    }

    public void setClients(List<ClientImpl> clients) {
        this.clients = clients;
    }
}
