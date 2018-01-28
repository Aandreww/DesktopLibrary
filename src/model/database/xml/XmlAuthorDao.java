package model.database.xml;

import model.AuthorImpl;
import model.BookImpl;
import model.database.AuthorDao;
import model.database.DaoFactory;
import model.database.DaoFactoryImpl;
import model.database.xml.jaxb.Authors;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class XmlAuthorDao implements AuthorDao {

    private DaoFactory daoFactory = new DaoFactoryImpl();

    private AuthorImpl author = new AuthorImpl();

    private XPath xPath = XPathFactory.newInstance().newXPath();
    private NodeList node = null;

    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder builder = null;

    @Override
    public AuthorImpl getByID(String id) {
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document authorXml = null;
        try {
            authorXml = builder.parse("src/model/database/xml/files/author.xml");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            node = (NodeList)xPath.compile("/Authors/Author[@id='"+ id +"']").evaluate(authorXml, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        author.setId(id);
        author.setName(node.item(0).getChildNodes().item(1).getTextContent());
        return author;
    }

    @Override
    public AuthorImpl getByName(String name) {
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document authorXml = null;
        try {
            authorXml = builder.parse("src/model/database/xml/files/author.xml");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            node = (NodeList)xPath.compile("/Authors/Author[name='"+ name +"']").evaluate(authorXml, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        author.setId(node.item(0).getAttributes().getNamedItem("id").getTextContent());
        author.setName(node.item(0).getChildNodes().item(1).getTextContent());
        return author;
    }

    @Override
    public AuthorImpl getByBook(String title) {
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document authorXml = null;
        Document author_bookXml = null;
        try {
            authorXml = builder.parse("src/model/database/xml/files/author.xml");
            author_bookXml = builder.parse("src/model/database/xml/files/author_book.xml");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String bookID = null;

        try {
            bookID = daoFactory.book().getByName(title).getId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            node = (NodeList)xPath.compile("/Authors_books/Author_book[book_id='"+ bookID +"']").evaluate(author_bookXml, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        String id = node.item(0).getChildNodes().item(1).getTextContent();

        try {
            node = (NodeList)xPath.compile("/Authors/Author[@id='"+ id +"']").evaluate(authorXml, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        author.setId(id);
        author.setName(node.item(0).getChildNodes().item(1).getTextContent());
        return author;
    }

    @Override
    public void create(String name) {

        AuthorImpl author = new AuthorImpl();
        author.setId(UUID.randomUUID().toString());
        author.setName(name);

        Authors authors;

        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(Authors.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            authors = (Authors)unmarshaller.unmarshal(new File("src/model/database/xml/files/author.xml"));

            List<AuthorImpl> list = new ArrayList<>();
            for (AuthorImpl author1:authors.getAuthors()){
                list.add(author1);
            }
            list.add(author);

            authors.setAuthors(list);

            jaxbContext = JAXBContext.newInstance(Authors.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(authors, new File("src/model/database/xml/files/author.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(AuthorImpl author) {

        Authors authors;

        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(Authors.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            authors = (Authors)unmarshaller.unmarshal(new File("src/model/database/xml/files/author.xml"));

            List<AuthorImpl> list = new ArrayList<>();
            for (AuthorImpl author1:authors.getAuthors()){
                list.add(author1);
            }
            list.add(author);

            authors.setAuthors(list);

            jaxbContext = JAXBContext.newInstance(Authors.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(authors, new File("src/model/database/xml/files/author.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void delete(String name) {
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document authorXml = null;
        try {
            authorXml = builder.parse("src/model/database/xml/files/author.xml");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            List<BookImpl> books = daoFactory.book().getByAuthor(name);
            for (BookImpl book: books){
                daoFactory.book().delete(book.getTitle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Node node1 = (Node)xPath.compile("/Authors/Author[name='" + name + "']").evaluate(authorXml, XPathConstants.NODE);
            if (node1!=null) {
                node1.getParentNode().removeChild(node1);

                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer t = tf.newTransformer();
                authorXml.normalize();
                t.transform(new DOMSource(authorXml), new StreamResult(new File("src/model/database/xml/files/author.xml")));
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<AuthorImpl> getAll() {
        JAXBContext context;
        Authors authors = new Authors();
        try {
            context = JAXBContext.newInstance(Authors.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            authors = (Authors)unmarshaller.unmarshal(new File("src/model/database/xml/files/author.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return authors.getAuthors();
    }
}
