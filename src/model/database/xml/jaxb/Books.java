package model.database.xml.jaxb;

import model.BookImpl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="Books")
@XmlAccessorType(XmlAccessType.FIELD)
public class Books {

    @XmlElement(name="Book")
    private List<BookImpl> books = null;

    public List<BookImpl> getBooks() {
        return books;
    }

    public void setBooks(List<BookImpl> books) {
        this.books = books;
    }
}
