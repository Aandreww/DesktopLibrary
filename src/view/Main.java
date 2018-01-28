package view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Client;
import model.database.DaoFactory;
import model.database.DaoFactoryImpl;

import static control.AdminControll.allUsers;
import static control.ClientControll.allBooks;
import static control.ClientControll.clientBooks;
import static control.ModerControll.allAuthors;

public class Main extends Application {

    private boolean registred = false;
    DaoFactory daoFactory = new DaoFactoryImpl();
    Client client;

    public static void main(String[] args) {
        launch(args);
    }

    public void logIn(Stage primaryStage) {

        Label log = new Label("Введите логин: ");
        Label passw = new Label("Введите пароль: ");
        log.setStyle("-fx-font-size: 11pt");
        passw.setStyle("-fx-font-size: 11pt");

        TextField login = new TextField();
        TextField pass = new TextField();
        Button ok = new Button("Продолжить");
        ok.setStyle("-fx-font-size: 11pt");
        Button reg = new Button("Регистрация");
        reg.setStyle("-fx-font-size: 11pt");

        Stage stage = new Stage();
        stage.setTitle("Вход");
        reg.setOnAction(event -> {
            reg(primaryStage,login.getText());
            stage.close();
        });

        ok.setOnAction(event -> {
            try {
                client = daoFactory.client().getByName(login.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(client!=null) {
                if(client.getPass().equals(pass.getText())) {

                    registred = true;

                    stage.close();
                    try {
                        start(primaryStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Alert alert = new Alert((Alert.AlertType.ERROR));
                    alert.setTitle("Ошибка входа");
                    alert.setContentText("Невеный пароль. Повторите поптыку");
                    alert.showAndWait();
                }
            }else{
                Alert alert = new Alert((Alert.AlertType.INFORMATION));
                alert.setTitle("Ошибка входа");
                alert.setContentText("Такого пользователя не существует. Может вам стоит зарегестрироваться?");
                alert.showAndWait();
            }
        });


        GridPane pane = new GridPane();
        pane.add(log, 0, 0);
        pane.add(login, 1, 0);
        pane.add(passw, 0, 1);
        pane.add(pass, 1, 1);
        pane.add(ok, 0, 2);
        pane.add(reg, 1, 2);
        pane.setVgap(20);
        pane.setHgap(20);
        pane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(pane, 320, 170);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void reg(Stage primaryStage, String login1) {
        Label log = new Label("Введите логин: ");
        Label passw = new Label("Введите пароль: ");
        Label passw1 = new Label("Повторите пароль: ");
        log.setStyle("-fx-font-size: 11pt");
        passw.setStyle("-fx-font-size: 11pt");
        passw1.setStyle("-fx-font-size: 11pt");

        TextField login = new TextField();
        login.setText(login1);
        TextField pass = new TextField();
        TextField pass1 = new TextField();
        Button reg = new Button("Зарегестрироваться");
        reg.setStyle("-fx-font-size: 11pt");

        Stage stage = new Stage();
        stage.setTitle("Регистрация");

        reg.setOnAction(event -> {
            try {
                client = daoFactory.client().getByName(login.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(client==null) {
                if(pass.getText().equals(pass1.getText())) {
                    try {
                        daoFactory.client().create(login.getText(), pass.getText());
                        client = daoFactory.client().getByName(login.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    registred = true;

                    stage.close();
                    try {
                        start(primaryStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Alert alert = new Alert((Alert.AlertType.WARNING));
                    alert.setTitle("Ошибка входа");
                    alert.setContentText("Невено повторен пароль. Повторите поптыку");
                    alert.showAndWait();
                    pass.setText("");
                    pass1.setText("");
                }
            }else{
                Alert alert = new Alert((Alert.AlertType.WARNING));
                alert.setTitle("Ошибка входа");
                alert.setContentText("Такой пользователь уже существует. Попробуйте другой логин");
                alert.showAndWait();
                login.setText("");
            }
        });


        GridPane pane = new GridPane();
        pane.add(log, 0, 0);
        pane.add(login, 1, 0);
        pane.add(passw, 0, 1);
        pane.add(pass, 1, 1);
        pane.add(passw1, 0, 2);
        pane.add(pass1, 1, 2);
        pane.add(reg, 0, 3);
        pane.setVgap(20);
        pane.setHgap(20);
        pane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(pane, 360, 250);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void start(Stage primaryStage){
        GridPane root = new GridPane();
        primaryStage.setResizable(false);
        Label label;
        if(client!=null) {
            switch (client.getPrivilege()) {
                case "Client":
                    label = new Label("Привет, " + client.getLogin());
                    label.setStyle("-fx-font-size: 15pt");
                    label.setAlignment(Pos.TOP_RIGHT);

                    Button myBooks = new Button("Мои книги");
                    myBooks.setStyle("-fx-font-size: 12pt");
                    Button allBooks = new Button("Все книги");
                    allBooks.setStyle("-fx-font-size: 12pt");
                    root.add(label, 0, 0);
                    root.add(myBooks, 0, 1);
                    root.add(allBooks, 1, 1);
                    root.setVgap(20);
                    root.setHgap(20);
                    root.setAlignment(Pos.CENTER);

                    myBooks.setOnAction(event -> {
                        clientBooks(client);
                    });
                    allBooks.setOnAction(event -> {
                        allBooks(client, null);
                    });

                    break;
                case "Moder":
                    label = new Label("Привет, " + client.getLogin());
                    label.setStyle("-fx-font-size: 15pt");
                    label.setAlignment(Pos.TOP_RIGHT);

                    Button allAuthor = new Button("Все авторы");
                    allAuthor.setStyle("-fx-font-size: 12pt");
                    root.add(label, 0, 0);
                    root.add(allAuthor, 0, 1);
                    root.setVgap(20);
                    root.setHgap(20);
                    root.setAlignment(Pos.CENTER);

                    allAuthor.setOnAction(event -> {
                        allAuthors();
                    });
                    break;
                case "Admin":
                    label = new Label("Привет, " + client.getLogin());
                    label.setStyle("-fx-font-size: 15pt");
                    label.setAlignment(Pos.TOP_RIGHT);

                    Button allClients = new Button("Список пользователей");
                    allClients.setStyle("-fx-font-size: 12pt");
                    root.add(label, 0, 0);
                    root.add(allClients, 0, 1);
                    root.setVgap(20);
                    root.setHgap(20);
                    root.setAlignment(Pos.CENTER);

                    allClients.setOnAction(event -> {
                        allUsers(client);
                    });
                    break;
            }
        }

        Scene scene = new Scene(root, 300, 160);
        primaryStage.setTitle("Библиотека");
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(event -> {
            try {
                daoFactory.end();
            } catch (Exception e) {
                e.printStackTrace();
            }
            primaryStage.close();
            logIn(primaryStage);
        });

        if (registred) {
            primaryStage.show();
        }
        if(!registred) {
            logIn(primaryStage);
        }


    }

}
