package control;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Client;
import model.ClientImpl;
import model.database.DaoFactory;
import model.database.DaoFactoryImpl;
import java.util.List;

public class AdminControll {

    private static DaoFactory daoFactory = new DaoFactoryImpl();

    public static void allUsers(Client client){
        GridPane pane = new GridPane();
        Stage stage = new Stage();
        stage.setTitle("Все пользователи");

        List<ClientImpl> clients = null;
        try {
            clients = daoFactory.client().getAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int logLength = 0;
        int i = 0;
        for (ClientImpl client1 : clients) {
            if (client1.getLogin().length()>logLength){
                logLength = client1.getLogin().length();
            }
            Label user = new Label("     "+client1.getLogin());
            user.setStyle("-fx-font-size: 11pt");
            Button userInfo = new Button("Информация");
            userInfo.setStyle("-fx-font-size: 11pt");
            Button chPriv = new Button("Изменить привелегии");
            chPriv.setStyle("-fx-font-size: 11pt");
            Button delUser = new Button("Удалить");
            delUser.setStyle("-fx-font-size: 11pt");
            pane.add(user, 0,i);
            pane.add(userInfo, 1,i);
            pane.add(chPriv,2,i);
            pane.add(delUser, 3,i);
            i++;
            userInfo.setOnAction(event -> {
                userInf(client1);
            });
            chPriv.setOnAction(event -> {
                changePriv(client, client1);
            });
            delUser.setOnAction(event -> {
                if (deliteUser(client, client1)) {
                    pane.getChildren().removeAll(user, userInfo, chPriv, delUser);
                }
            });
        }

        pane.setVgap(20);
        pane.setHgap(20);
        pane.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(pane);

        VBox box = new VBox();
        box.getChildren().add(scrollPane);

        if (i == 0) {
            Scene scene = new Scene(box, 280, 70);
            stage.setScene(scene);
            stage.show();
        }else {
            Scene scene = new Scene(box, 280+logLength*35, i*55);
            stage.setScene(scene);
            stage.show();
        }

        stage.setResizable(false);
    }

    private static void userInf(Client client) {
        GridPane pane = new GridPane();
        Stage stage = new Stage();
        stage.setTitle(client.getLogin());

        Label login = new Label("Логин: ");
        login.setStyle("-fx-font-size: 11pt");
        Label userLog = new Label(client.getLogin());
        userLog.setStyle("-fx-font-size: 11pt");
        Label privelege = new Label("Привелегия: ");
        privelege.setStyle("-fx-font-size: 11pt");
        Label userPriv = null;
        try {
            userPriv = new Label(daoFactory.client().getByID(client.getId()).getPrivilege());
        } catch (Exception e) {
            e.printStackTrace();
        }
        userPriv.setStyle("-fx-font-size: 11pt");
        pane.add(login,0,0);
        pane.add(userLog,1,0);
        pane.add(privelege,0,1);
        pane.add(userPriv,1,1);

        pane.setVgap(20);
        pane.setHgap(20);
        pane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(pane, 100+client.getLogin().length()*15, 150);
        stage.setScene(scene);
        stage.show();

        stage.setResizable(false);
    }

    private static void changePriv(Client rootClient, Client client){
        GridPane pane = new GridPane();
        Stage stage = new Stage();
        stage.setTitle(client.getLogin());

        Label userPriv = null;
        try {
            userPriv = new Label(daoFactory.client().getByID(client.getId()).getPrivilege());
        } catch (Exception e) {
            e.printStackTrace();
        }
        userPriv.setStyle("-fx-font-size: 15pt");
        Button client1 = new Button("Client");
        client1.setStyle("-fx-font-size: 11pt");
        Button moder = new Button("Moder");
        moder.setStyle("-fx-font-size: 11pt");
        Button admin = new Button("Admin");
        admin.setStyle("-fx-font-size: 11pt");
        pane.add(userPriv,0,0);
        pane.add(client1,0,1);
        pane.add(moder,1,1);
        pane.add(admin,2,1);

        client1.setOnAction(event -> {
            if (!rootClient.getLogin().equals(client.getLogin())) {
                try {
                    daoFactory.client().setPriv(client.getId(), "Client");
                    Label newPriv = new Label(daoFactory.client().getByID(client.getId()).getPrivilege());
                    newPriv.setStyle("-fx-font-size: 15pt");
                    newPriv.setAlignment(Pos.CENTER);
                    pane.getChildren().set(0,newPriv);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert((Alert.AlertType.WARNING));
                alert.setTitle("Не-а");
                alert.setContentText("Это не смешно");
                alert.showAndWait();
            }
        });

        moder.setOnAction(event -> {
            if (!rootClient.getLogin().equals(client.getLogin())) {
                try {
                    daoFactory.client().setPriv(client.getId(), "Moder");
                    Label newPriv = new Label(daoFactory.client().getByID(client.getId()).getPrivilege());
                    newPriv.setStyle("-fx-font-size: 15pt");
                    newPriv.setAlignment(Pos.CENTER);
                    pane.getChildren().set(0,newPriv);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert((Alert.AlertType.WARNING));
                alert.setTitle("Пффф");
                alert.setContentText("Ага, еще что скажешь?");
                alert.showAndWait();
            }
        });

        admin.setOnAction(event -> {
            if (!rootClient.getLogin().equals(client.getLogin())) {
                try {
                    daoFactory.client().setPriv(client.getId(), "Admin");
                    Label newPriv = new Label(daoFactory.client().getByID(client.getId()).getPrivilege());
                    newPriv.setStyle("-fx-font-size: 15pt");
                    newPriv.setAlignment(Pos.CENTER);
                    pane.getChildren().set(0,newPriv);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert((Alert.AlertType.WARNING));
                alert.setTitle("ОХ...");
                alert.setContentText("ХВАТИТ");
                alert.showAndWait();
            }
        });

        pane.setVgap(20);
        pane.setHgap(20);
        pane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(pane, 100+client.getLogin().length()*30, 100);
        stage.setScene(scene);
        stage.show();

        stage.setResizable(false);
    }

    private static boolean deliteUser(Client rootClient, Client client) {
        boolean deleted = false;
        if (!rootClient.getLogin().equals(client.getLogin())) {
            try {
                daoFactory.client().delete(client.getId());
                deleted = true;
                Alert alert = new Alert((Alert.AlertType.INFORMATION));
                alert.setTitle("ИНФО");
                alert.setContentText("УДАЛЕН");
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Alert alert = new Alert((Alert.AlertType.WARNING));
            alert.setTitle("Бля");
            alert.setContentText("Ты дурак?");
            alert.showAndWait();
        }
        return deleted;
    }

}
