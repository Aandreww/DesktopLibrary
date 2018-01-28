package model.database.xml.jaxb;

import model.AuthorImpl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="Authors")
@XmlAccessorType(XmlAccessType.FIELD)
public class Authors {

    @XmlElement(name="Author")
    private List<AuthorImpl> authors = null;

    public List<AuthorImpl> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorImpl> authors) {
        this.authors = authors;
    }
}
