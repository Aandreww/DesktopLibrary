package model.database.xml;

import model.ClientImpl;
import model.database.ClientDao;
import model.database.xml.jaxb.Client_book;
import model.database.xml.jaxb.Clients_books;
import model.database.xml.jaxb.Clietns;
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

public class XmlClientDao implements ClientDao {

    private ClientImpl client = new ClientImpl();

    private XPath xPath = XPathFactory.newInstance().newXPath();
    private NodeList node = null;

    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder builder = null;

    @Override
    public ClientImpl getByID(String id) {
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document clientXml = null;
        try {
            clientXml = builder.parse("src/model/database/xml/files/client.xml");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            node = (NodeList)xPath.compile("/Clients/Client[@id='"+ id +"']").evaluate(clientXml, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        client.setId(id);
        client.setLogin(node.item(0).getChildNodes().item(1).getTextContent());
        client.setPass(node.item(0).getChildNodes().item(3).getTextContent());
        client.setPrivilege(node.item(0).getChildNodes().item(5).getTextContent());
        return client;
    }

    @Override
    public ClientImpl getByName(String login) {
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document clientXml = null;
        try {
            clientXml = builder.parse("src/model/database/xml/files/client.xml");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            node = (NodeList)xPath.compile("/Clients/Client[login='"+ login +"']").evaluate(clientXml, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        client.setId(node.item(0).getAttributes().getNamedItem("id").getTextContent());
        client.setLogin(node.item(0).getChildNodes().item(1).getTextContent());
        client.setPass(node.item(0).getChildNodes().item(3).getTextContent());
        client.setPrivilege(node.item(0).getChildNodes().item(5).getTextContent());
        return client;
    }

    @Override
    public void getBook(String clientID, String bookID) {
        Client_book client_book = new Client_book();
        client_book.setId(UUID.randomUUID().toString());
        client_book.setClient_id(clientID);
        client_book.setBook_id(bookID);

        Clients_books clients_books;

        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(Clients_books.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            clients_books = (Clients_books)unmarshaller.unmarshal(new File("src/model/database/xml/files/client_book.xml"));

            List<Client_book> list = new ArrayList<>();
            for (Client_book clients_books1:clients_books.getClient_books()){
                list.add(clients_books1);
            }
            list.add(client_book);

            clients_books.setClient_books(list);

            jaxbContext = JAXBContext.newInstance(Clients_books.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(clients_books, new File("src/model/database/xml/files/client_book.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void returnBook(String clientID, String bookID) {
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document clientBookXml = null;
        try {
            clientBookXml = builder.parse("src/model/database/xml/files/client_book.xml");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Node node1 = (Node)xPath.compile("/Clients_books/Client_book[client_id='" + clientID + "'][book_id='" + bookID +"']").evaluate(clientBookXml, XPathConstants.NODE);
            if (node1!=null) {
                node1.getParentNode().removeChild(node1);

                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer t = tf.newTransformer();
                clientBookXml.normalize();
                t.transform(new DOMSource(clientBookXml), new StreamResult(new File("src/model/database/xml/files/client_book.xml")));
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
    public void create(String login, String pass) {
        String privilege = "Client";

        ClientImpl client = new ClientImpl();
        client.setId(UUID.randomUUID().toString());
        client.setLogin(login);
        client.setPass(pass);
        client.setPrivilege(privilege);

        Clietns clietns;

        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(Clietns.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            clietns = (Clietns)unmarshaller.unmarshal(new File("src/model/database/xml/files/client.xml"));

            List<ClientImpl> list = new ArrayList<>();
            for (ClientImpl client1:clietns.getClients()){
                list.add(client1);
            }
            list.add(client);

            clietns.setClients(list);

            jaxbContext = JAXBContext.newInstance(Clietns.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(clietns, new File("src/model/database/xml/files/client.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setPriv(String id, String priv) {
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document clientXml = null;
        try {
            clientXml = builder.parse("src/model/database/xml/files/client.xml");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            node = (NodeList)xPath.compile("/Clients/Client[@id='" + id + "']").evaluate(clientXml, XPathConstants.NODESET);

            node.item(0).getChildNodes().item(5).setTextContent(priv);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            clientXml.normalize();
            t.transform(new DOMSource(clientXml), new StreamResult(new File("src/model/database/xml/files/client.xml")));
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document clientXml = null;
        try {
            clientXml = builder.parse("src/model/database/xml/files/client.xml");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Node node1 = (Node)xPath.compile("/Clients/Client[@id='" + id + "']").evaluate(clientXml, XPathConstants.NODE);
            if (node1!=null) {
                node1.getParentNode().removeChild(node1);

                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer t = tf.newTransformer();
                clientXml.normalize();
                t.transform(new DOMSource(clientXml), new StreamResult(new File("src/model/database/xml/files/client.xml")));
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
    public List<ClientImpl> getAll() {
        JAXBContext context;
        Clietns clietns = new Clietns();
        try {
            context = JAXBContext.newInstance(Clietns.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            clietns = (Clietns)unmarshaller.unmarshal(new File("src/model/database/xml/files/client.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return clietns.getClients();
    }
}
