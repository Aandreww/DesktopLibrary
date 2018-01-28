package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="Author")
@XmlType(propOrder = {"name"})
public class AuthorImpl implements Author{

    private String id;
    private String name;

    public AuthorImpl(){}

    public AuthorImpl(String id, String name){
        this.id = id;
        this. name = name;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    @XmlAttribute
    public String getId() {
        return id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    @XmlElement
    public String getName() {
        return name;
    }
}