package model.database.xml;

import model.BookImpl;
import model.database.BookDao;
import model.database.DaoFactory;
import model.database.DaoFactoryImpl;
import model.database.xml.jaxb.Author_book;
import model.database.xml.jaxb.Authors_books;
import model.database.xml.jaxb.Books;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.print.Doc;
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

public class XmlBookDao implements BookDao {

    private DaoFactory daoFactory = new DaoFactoryImpl();

    private BookImpl book = new BookImpl();

    private XPath xPath = XPathFactory.newInstance().newXPath();
    private NodeList node = null;

    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder builder = null;

    @Override
    public BookImpl getByID(String id) {
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document bookXml = null;
        try {
            bookXml = builder.parse("src/model/database/xml/files/book.xml");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            node = (NodeList)xPath.compile("/Books/Book[@id='"+ id +"']").evaluate(bookXml, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        book.setId(id);
        book.setTitle(node.item(0).getChildNodes().item(1).getTextContent());
        book.setYear(Integer.parseInt(node.item(0).getChildNodes().item(3).getTextContent()));
        book.setGenre(node.item(0).getChildNodes().item(5).getTextContent());
        return book;
    }

    @Override
    public List<BookImpl> getByAuthor(String id) {
        List<BookImpl> books = new ArrayList<>();
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document bookXml = null;
        try {
            bookXml = builder.parse("src/model/database/xml/files/author_book.xml");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            node = (NodeList)xPath.compile("/Authors_books/Author_book[author_id='"+ id +"']").evaluate(bookXml, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        for (int i = 0; i<node.getLength();i++) {
            try {
                books.add(daoFactory.book().getByID(node.item(i).getChildNodes().item(3).getTextContent()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return books;
    }

    @Override
    public BookImpl getByName(String title) {
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document bookXml = null;
        try {
            bookXml = builder.parse("src/model/database/xml/files/book.xml");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            node = (NodeList)xPath.compile("/Books/Book[title='"+ title +"']").evaluate(bookXml, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        book.setId(node.item(0).getAttributes().getNamedItem("id").getTextContent());
        book.setTitle(node.item(0).getChildNodes().item(1).getTextContent());
        book.setYear(Integer.parseInt(node.item(0).getChildNodes().item(3).getTextContent()));
        book.setGenre(node.item(0).getChildNodes().item(5).getTextContent());
        return book;
    }

    @Override
    public void create(String title, String year, String genre, String author) {
        BookImpl book = new BookImpl();
        book.setId(UUID.randomUUID().toString());
        book.setTitle(title);
        book.setYear(Integer.parseInt(year));
        book.setGenre(genre);

        Books books;
        Authors_books authors_books;

        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(Books.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            books = (Books) unmarshaller.unmarshal(new File("src/model/database/xml/files/book.xml"));

            List<BookImpl> list = new ArrayList<>();
            for (BookImpl book1:books.getBooks()){
                list.add(book1);
            }
            list.add(book);

            books.setBooks(list);

            jaxbContext = JAXBContext.newInstance(Books.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(books, new File("src/model/database/xml/files/book.xml"));


            Author_book author_book = new Author_book();
            author_book.setId(UUID.randomUUID().toString());
            author_book.setBook_id(book.getId());
            author_book.setAuthor_id(daoFactory.author().getByName(author).getId());

            jaxbContext = JAXBContext.newInstance(Authors_books.class);

            unmarshaller = jaxbContext.createUnmarshaller();
            authors_books = (Authors_books)unmarshaller.unmarshal(new File("src/model/database/xml/files/author_book.xml"));

            List<Author_book> list1 = new ArrayList<>();
            for (Author_book author_book1:authors_books.getAuthors_books()){
                list1.add(author_book1);
            }
            list1.add(author_book);

            authors_books.setAuthors_books(list1);

            jaxbContext = JAXBContext.newInstance(Authors_books.class);
            jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(authors_books, new File("src/model/database/xml/files/author_book.xml"));

        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(BookImpl book) {

    }

    @Override
    public void delete(String title) {
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document bookXml = null;
        Document author_book = null;
        try {
            bookXml = builder.parse("src/model/database/xml/files/book.xml");
            author_book = builder.parse("src/model/database/xml/files/author_book.xml");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Node node2 = (Node)xPath.compile("/Authors_books/Author_book[book_id='" + daoFactory.book().getByName(title).getId() + "']").evaluate(author_book, XPathConstants.NODE);
            if (node2!=null) {
                node2.getParentNode().removeChild(node2);

                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer t = tf.newTransformer();
                author_book.normalize();
                t.transform(new DOMSource(author_book), new StreamResult(new File("src/model/database/xml/files/author_book.xml")));
            }
            Node node1 = (Node)xPath.compile("/Books/Book[title='" + title + "']").evaluate(bookXml, XPathConstants.NODE);
            if (node1!=null) {
                node1.getParentNode().removeChild(node1);

                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer t = tf.newTransformer();
                bookXml.normalize();
                t.transform(new DOMSource(bookXml), new StreamResult(new File("src/model/database/xml/files/book.xml")));
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<BookImpl> getByClientID(String id) {
        List<BookImpl> books = new ArrayList<>();
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document bookXml = null;
        try {
            bookXml = builder.parse("src/model/database/xml/files/client_book.xml");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            node = (NodeList)xPath.compile("/Clients_books/Client_book[client_id='"+ id +"']").evaluate(bookXml, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        for (int i = 0; i<node.getLength();i++) {
            try {
                books.add(daoFactory.book().getByID(node.item(i).getChildNodes().item(3).getTextContent()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return books;
    }

    @Override
    public List<BookImpl> getAll() {
        JAXBContext context;
        Books books = new Books();
        try {
            context = JAXBContext.newInstance(Books.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            books = (Books) unmarshaller.unmarshal(new File("src/model/database/xml/files/book.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return books.getBooks();
    }

    @Override
    public List<BookImpl> getByPartOfName(String name) {
        JAXBContext context;
        List<BookImpl> book1 = new ArrayList<>();
        Books books = new Books();
        try {
            context = JAXBContext.newInstance(Books.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            books = (Books) unmarshaller.unmarshal(new File("src/model/database/xml/files/book.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        for (BookImpl book:books.getBooks()){
            if(book.getTitle().contains(name)){
                book1.add(book);
            }
        }
        return book1;
    }
}
