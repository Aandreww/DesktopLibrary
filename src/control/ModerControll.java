package control;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;
import model.database.DaoFactory;
import model.database.DaoFactoryImpl;

import java.util.List;

public class ModerControll {

    private static DaoFactory daoFactory = new DaoFactoryImpl();
    private static List<AuthorImpl> authors;
    private static List<BookImpl> books;
    private static int i;

    public static void allAuthors(){
        GridPane pane = new GridPane();
        Stage stage = new Stage();
        stage.setTitle("Все авторы");
        try {
            authors = daoFactory.author().getAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int authorLength = 0;
        i = 0;
        for(AuthorImpl author : authors){
            if (author.getName().length()>authorLength){
                authorLength = author.getName().length();
            }
            Label author1 = new Label("     "+author.getName());
            author1.setStyle("-fx-font-size: 11pt");
            Button authInfo = new Button("Информация");
            authInfo.setStyle("-fx-font-size: 11pt");
            Button getBook = new Button("Книги");
            getBook.setStyle("-fx-font-size: 11pt");
            Button delAuth = new Button("Удалить");
            delAuth.setStyle("-fx-font-size: 11pt");
            pane.add(author1, 0,i);
            pane.add(authInfo, 1,i);
            pane.add(getBook, 2,i);
            pane.add(delAuth, 3,i);
            authInfo.setOnAction(event -> {
                authInf(author);
            });
            getBook.setOnAction(event -> {
                getBooks(author);
            });
            delAuth.setOnAction(event -> {
                deleteAuth(author);
                pane.getChildren().removeAll(author1, authInfo, getBook, delAuth);
            });
            i++;
        }

        Button addAuth = new Button("Добавить автора");
        addAuth.setStyle("-fx-font-size: 11pt");
        if (i>0) {
            pane.add(addAuth, 3, i);
        }else {
            pane.add(addAuth, 0, i);
        }

        addAuth.setOnAction(event -> {
            addAuthor();
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
            Scene scene = new Scene(box, 180, 70);
            stage.setScene(scene);
            stage.show();
        }else {
            Scene scene = new Scene(box, 180 + authorLength * 20, 60+i * 65);
            stage.setScene(scene);
            stage.show();
        }

        stage.setResizable(false);
    }

    private static void authInf(Author author){
        GridPane pane = new GridPane();
        Stage stage = new Stage();
        stage.setTitle(author.getName());

        Label name = new Label("Имя: ");
        name.setStyle("-fx-font-size: 11pt");
        Label authName = new Label(author.getName());
        authName.setStyle("-fx-font-size: 11pt");
        pane.add(name,0,0);
        pane.add(authName,1,0);

        pane.setVgap(20);
        pane.setHgap(20);
        pane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(pane, 150 + author.getName().length()*15, 90);
        stage.setScene(scene);
        stage.show();

        stage.setResizable(false);
    }

    private static void getBooks(Author author){
        GridPane pane = new GridPane();
        try {
            books = daoFactory.book().getByAuthor(author.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Книги " + author.getName() + "(a)");
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
            Button delBook = new Button("Удалить");
            delBook.setStyle("-fx-font-size: 11pt");
            pane.add(book, 0,i);
            pane.add(bookInfo, 1,i);
            pane.add(delBook, 2,i);
            i++;
            bookInfo.setOnAction(event -> {
                bookInf(book1);
            });
            delBook.setOnAction(event -> {
                deleteBook(book1);
                pane.getChildren().removeAll(book, bookInfo, delBook);
            });
        }

        Button addBook = new Button("Добавить книгу");
        addBook.setStyle("-fx-font-size: 11pt");
        if (i>0) {
            pane.add(addBook, 3, i);
        }else {
            pane.add(addBook, 0, i);
        }

        addBook.setOnAction(event -> {
            addBookToAuthor(author);
            stage.close();
        });

        pane.setVgap(20);
        pane.setHgap(20);
        pane.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(pane);

        VBox box = new VBox();
        box.getChildren().add(scrollPane);

        if (i==0) {
            Scene scene = new Scene(box, 180, 70);
            stage.setScene(scene);
            stage.show();
        }else {
            Scene scene = new Scene(box, 390 + bookLength * 15, 110+i * 60);
            stage.setScene(scene);
            stage.show();
        }

        stage.setResizable(false);
    }

    private static void deleteAuth(Author author){
        try {
            daoFactory.author().delete(author.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteBook(Book book){
        try {
            daoFactory.book().delete(book.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void bookInf(Book book){
        GridPane pane = new GridPane();
        Stage stage = new Stage();
        stage.setTitle(book.getTitle());

        int bookLength = book.getTitle().length();

        Label title = new Label("Название: ");
        title.setStyle("-fx-font-size: 11pt");
        Label booksTitle = new Label(book.getTitle());
        booksTitle.setStyle("-fx-font-size: 11pt");
        Label author = new Label("Автор: ");
        author.setStyle("-fx-font-size: 11pt");
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

        Scene scene = new Scene(pane, 260+bookLength*15, 190);
        stage.setScene(scene);
        stage.show();

        stage.setResizable(false);
    }

    private static void addAuthor(){
        GridPane pane = new GridPane();
        Stage stage = new Stage();
        stage.setTitle("Новый автор");

        Label author1 = new Label("     Имя автора: ");
        author1.setStyle("-fx-font-size: 11pt");

        TextField autorName = new TextField();
        autorName.setStyle("-fx-font-size: 11pt");

        Button addAuth = new Button("Добавить");
        addAuth.setStyle("-fx-font-size: 11pt");
        pane.add(author1, 0,0);
        pane.add(autorName, 1,0);
        pane.add(addAuth, 1,1);

        addAuth.setOnAction(event -> {
            try {
                if (autorName.getText()!=null&&!autorName.getText().equals("")) {
                    daoFactory.author().create(autorName.getText());
                    stage.close();
                    allAuthors();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        pane.setVgap(20);
        pane.setHgap(20);
        pane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(pane, 325, 120);
        stage.setScene(scene);
        stage.show();

        stage.setResizable(false);
    }

    private static void addBookToAuthor(Author author){
        GridPane pane = new GridPane();
        Stage stage = new Stage();
        stage.setTitle("Новая книга");

        Label title = new Label("     Название книги: ");
        title.setStyle("-fx-font-size: 11pt");

        TextField bookTitle = new TextField();
        bookTitle.setStyle("-fx-font-size: 11pt");

        Label year = new Label("     Год книги: ");
        year.setStyle("-fx-font-size: 11pt");

        TextField bookYear = new TextField();
        bookYear.setStyle("-fx-font-size: 11pt");

        Label genre = new Label("     Жанр книги: ");
        genre.setStyle("-fx-font-size: 11pt");

        TextField bookGenre = new TextField();
        bookGenre.setStyle("-fx-font-size: 11pt");

        Button addBook = new Button("Добавить");
        addBook.setStyle("-fx-font-size: 11pt");

        pane.add(title, 0,0);
        pane.add(bookTitle, 1,0);
        pane.add(year, 0,1);
        pane.add(bookYear, 1,1);
        pane.add(genre, 0,2);
        pane.add(bookGenre, 1,2);
        pane.add(addBook, 1,3);

        addBook.setOnAction(event -> {
            try {
                if (!bookTitle.getText().equals("")&&!bookYear.getText().equals("")&&!bookGenre.getText().equals("")) {
                    daoFactory.book().create(bookTitle.getText(),bookYear.getText(),bookGenre.getText(),author.getName());
                    stage.close();
                    getBooks(author);
                }else {
                    Alert alert = new Alert((Alert.AlertType.WARNING));
                    alert.setTitle("Ошибка");
                    alert.setContentText("Все поля должны быть заполнены");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        pane.setVgap(20);
        pane.setHgap(20);
        pane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(pane, 375, 300);
        stage.setScene(scene);
        stage.show();

        stage.setResizable(false);
    }
}
