package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="Book")
@XmlType(propOrder = {"title","year","genre"})
public class BookImpl implements Book{

    private String id;
    private String title;
    private Integer year;
    private String genre;

    public BookImpl(){}

    public BookImpl(String id, String title, Integer year, String genre){
        this.id = id;
        this.title = title;
        this.year = year;
        this.genre = genre;
    }

    @Override
    @XmlAttribute
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    @XmlElement
    public Integer getYear() {
        return year;
    }

    @Override
    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    @XmlElement
    public String getGenre() {
        return genre;
    }

    @Override
    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    @XmlElement
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }
}
