package control;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Book;
import model.BookImpl;
import model.Client;
import model.database.DaoFactory;
import model.database.DaoFactoryImpl;

import java.util.List;

public class ClientControll {


    private static DaoFactory daoFactory = new DaoFactoryImpl();
    private static List<BookImpl> books;

    public static void clientBooks(Client client){
        GridPane pane = new GridPane();
        Stage stage = new Stage();
        stage.setTitle("Мои Книги");

        try {
            books = daoFactory.book().getByClientID(client.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        int i = 0;
        int bookLength = 0;
        for(BookImpl book1 : books){
            if (book1.getTitle().length()>bookLength){
                bookLength = book1.getTitle().length();
            }
            Label book = new Label("     "+book1.getTitle());
            book.setStyle("-fx-font-size: 11pt");
            Button bookInfo = new Button("Информация");
            bookInfo.setStyle("-fx-font-size: 11pt");
            Button retBook = new Button("Вернуть");
            retBook.setStyle("-fx-font-size: 11pt");
            pane.add(book, 0,i);
            pane.add(bookInfo, 1,i);
            pane.add(retBook, 2,i);
            bookInfo.setOnAction(event -> {
                bookInf(book1);
            });
            retBook.setOnAction(event -> {
                returnBook(client, book1);
                pane.getChildren().removeAll(book,bookInfo,retBook);
            });
            i++;
        }

        pane.setVgap(20);
        pane.setHgap(20);
        pane.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(pane);

        VBox box = new VBox();
        box.getChildren().add(scrollPane);

        stage.setResizable(false);

        if (i == 0) {
            Scene scene = new Scene(box, 100, 70);
            stage.setScene(scene);
            stage.show();
        }else {
            Scene scene = new Scene(box, 100+bookLength*20, i*60);
            stage.setScene(scene);
            stage.show();
        }

        stage.setResizable(false);
    }

    public static void allBooks(Client client, List<BookImpl> books1){
        GridPane pane = new GridPane();
        Stage stage = new Stage();
        stage.setTitle("Библиотека");
        if(books1==null) {
            try {
                books = daoFactory.book().getAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            books = books1;
        }
        int bookLength = 0;
        int i = 0;
        for(BookImpl book1 : books){
            if (book1.getTitle().length()>bookLength){
                bookLength = book1.getTitle().length();
            }
            Label book = new Label("     "+book1.getTitle());
            book.setStyle("-fx-font-size: 11pt");
            Button bookInfo = new Button("Информация");
            bookInfo.setStyle("-fx-font-size: 11pt");
            Button gettBook = new Button("Взять");
            gettBook.setStyle("-fx-font-size: 11pt");
            pane.add(book, 0,i);
            pane.add(bookInfo, 1,i);
            pane.add(gettBook, 2,i);
            i++;
            bookInfo.setOnAction(event -> {
                bookInf(book1);
            });
            gettBook.setOnAction(event -> {
                getBook(client,book1);
            });
        }

        Button search = new Button("Найти");
        search.setStyle("-fx-font-size: 11pt");
        Label empty = new Label("     Пусто");
        empty.setStyle("-fx-font-size: 11pt");
        if (i>0) {
            pane.add(search, 2, i);
        }else {
            pane.add(empty, 0, i);
        }

        search.setOnAction(event -> {
            searchBook(client);
            stage.close();
        });


        pane.setVgap(20);
        pane.setHgap(20);
        pane.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(pane);

        VBox box = new VBox();
        box.getChildren().add(scrollPane);

        if (i == 0) {
            Scene scene = new Scene(box, 100, 70);
            stage.setScene(scene);
            stage.show();
        }else {
            Scene scene = new Scene(box, 100+bookLength*20, 60+i*60);
            stage.setScene(scene);
            stage.show();
        }
        stage.setResizable(false);
    }

    private static void bookInf(Book book){
        GridPane pane = new GridPane();
        Stage stage = new Stage();
        stage.setTitle(book.getTitle());

        Label title = new Label("Название: ");
        title.setStyle("-fx-font-size: 11pt");
        Label booksTitle = new Label(book.getTitle());
        booksTitle.setStyle("-fx-font-size: 11pt");
        Label author = new Label("Автор: ");
        author.setStyle("-fx-font-size: 11pt");
        int bookLength = book.getTitle().length();
        Label booksAuthor = null;
        try {
            booksAuthor = new Label(daoFactory.author().getByBook(book.getTitle()).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        booksAuthor.setStyle("-fx-font-size: 11pt");
        Label genre = new Label("Жанр: ");
        genre.setStyle("-fx-font-size: 11pt");
        Label booksGenre = new Label(book.getGenre());
        booksGenre.setStyle("-fx-font-size: 11pt");
        Label year = new Label("Год: ");
        year.setStyle("-fx-font-size: 11pt");
        Label booksYear = new Label(book.getYear().toString());
        booksYear.setStyle("-fx-font-size: 11pt");
        pane.add(title,0,0);
        pane.add(booksTitle,1,0);
        pane.add(author,0,1);
        pane.add(booksAuthor,1,1);
        pane.add(year,0,2);
        pane.add(booksYear,1,2);
        pane.add(genre,0,3);
        pane.add(booksGenre,1,3);

        pane.setVgap(20);
        pane.setHgap(20);
        pane.setAlignment(Pos.CENTER);


        Scene scene = new Scene(pane, 260+bookLength*20, 190);
        stage.setScene(scene);
        stage.show();

        stage.setResizable(false);
    }

    private static void returnBook(Client client, Book book) {
        try {
            daoFactory.client().returnBook(client.getId(), book.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Alert alert = new Alert((Alert.AlertType.INFORMATION));
        alert.setTitle("ИНФО");
        alert.setContentText("Книга возвращена");
        alert.showAndWait();
    }

    private static void getBook(Client client, Book book) {
        boolean have = false;
        try {
            books = daoFactory.book().getByClientID(client.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (BookImpl book1: books){
            if (book1.getTitle().equals(book.getTitle())){
                have = true;
            }
        }
        if (!have) {
            try {
                daoFactory.client().getBook(client.getId(), book.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Alert alert = new Alert((Alert.AlertType.INFORMATION));
            alert.setTitle("ИНФО");
            alert.setContentText("Книга добавлена");
            alert.showAndWait();
        }else {
            Alert alert = new Alert((Alert.AlertType.WARNING));
            alert.setTitle("ОШИБКА");
            alert.setContentText("У вас уже есть эта книга");
            alert.showAndWait();
        }
    }

    private static void searchBook(Client client){
        GridPane pane = new GridPane();
        Stage stage = new Stage();
        stage.setTitle("Новый автор");

        Label title = new Label("     Часть названия: ");
        title.setStyle("-fx-font-size: 11pt");

        TextField bookTitle = new TextField();
        bookTitle.setStyle("-fx-font-size: 11pt");

        Button search = new Button("Найти");
        search.setStyle("-fx-font-size: 11pt");
        pane.add(title, 0,0);
        pane.add(bookTitle, 1,0);
        pane.add(search, 1,1);

        search.setOnAction(event -> {
            try {
                if (!bookTitle.getText().equals("")) {
                    books = daoFactory.book().getByPartOfName(bookTitle.getText());
                    stage.close();
                    allBooks(client, books);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        pane.setVgap(20);
        pane.setHgap(20);
        pane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(pane, 360, 130);
        stage.setScene(scene);
        stage.show();

        stage.setResizable(false);
    }


}
