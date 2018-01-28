package model.database.xml.jaxb;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="Clients_books")
@XmlSeeAlso({Client_book.class})
@XmlAccessorType(XmlAccessType.FIELD)
public class Clients_books implements Serializable{

    public Clients_books(){
        this.client_books = new ArrayList<>();
    }

    @XmlElement(name="Client_book")
    private List<Client_book> client_books = null;

    public List<Client_book> getClient_books() {
        return client_books;
    }

    public void setClient_books(List<Client_book> client_books) {
        this.client_books = client_books;
    }

}
