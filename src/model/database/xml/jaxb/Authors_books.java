package model.database.xml.jaxb;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="Authors_books")
@XmlSeeAlso({Author_book.class})
@XmlAccessorType(XmlAccessType.FIELD)
public class Authors_books {

    @XmlElement(name="Author_book")
    private List<Author_book> authors_books= null;

    public Authors_books(){
        this.authors_books = new ArrayList<>();
    }

    public List<Author_book> getAuthors_books() {
        return authors_books;
    }

    public void setAuthors_books(List<Author_book> authors_books) {
        this.authors_books = authors_books;
    }
}
