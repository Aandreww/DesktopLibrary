import model.AuthorImpl;
import model.BookImpl;
import model.ClientImpl;
import model.database.DaoFactory;
import model.database.DaoFactoryImpl;
import java.util.List;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws Exception {

        DaoFactory daoFactory = new DaoFactoryImpl();
        Scanner sc = new Scanner(System.in);
        String log, pass;
        int select;
        char assepting;
        boolean run = true;
        ClientImpl client;
        BookImpl book;
        AuthorImpl author;
        List<BookImpl> books;
        List<ClientImpl> clients;
        List<AuthorImpl> authors;

        while(run) {
            System.out.print("Введите ваш логин: ");
            log = sc.next();

            System.out.print("Введите ваш пароль: ");
            pass = sc.next();

            client = daoFactory.client().getByName(log);

            if (client != null) {
                if (client.getPass().equals(pass)) {
                    //тело программы. работа с клиентом
                    System.out.println("Здравствуй, " + client.getLogin());
                    switch (client.getPrivilege()){
                        case "Client":
                            while(run) {
                                System.out.println("Что бы вы хотели сделать?\nПосмотреть список ваших книг? Введите '1'.\nПосмотреть список всех книг? Введите '2'.\nПосмотреть информацию о книге? Введите '3'.\nВзять новую книгу? Введите '4'.\nВернуть книгу? Введите '5'.\nВыйти? Введите '6'.");
                                select = sc.nextInt();
                                String bookName;
                                switch (select) {
                                    case 1:
                                        books = daoFactory.book().getByClientID(client.getId());
                                        for (BookImpl book1 : books) {
                                            System.out.println(book1.getTitle());
                                        }
                                        break;
                                    case 2:
                                        books = daoFactory.book().getAll();
                                        for (BookImpl book1 : books) {
                                            System.out.println(book1.getTitle());
                                        }
                                        break;
                                    case 3:
                                        System.out.println("Введите название книги: ");
                                        sc.nextLine();
                                        bookName = sc.nextLine();
                                        book = daoFactory.book().getByName(bookName);
                                        if (book!=null) {
                                            System.out.println(book.getTitle() + " " + book.getYear() + " " + book.getGenre());
                                        }else {
                                            System.out.println("Книги с таким названием нет");
                                        }
                                        break;
                                    case 4:
                                        System.out.println("Введите название книги: ");
                                        sc.nextLine();
                                        bookName = sc.nextLine();
                                        book = daoFactory.book().getByName(bookName);
                                        if(book!=null) {
                                            daoFactory.client().getBook(client.getId(), book.getId());
                                            System.out.println("Книга добавлена в ваш список");
                                        }else {
                                            System.out.println("Книги с таким названием нет");
                                        }
                                        break;
                                    case 5:
                                        System.out.println("Введите название книги: ");
                                        sc.nextLine();
                                        bookName = sc.nextLine();
                                        book = daoFactory.book().getByName(bookName);
                                        if(book!=null) {
                                            daoFactory.client().returnBook(client.getId(), book.getId());
                                            System.out.println("Книга возвращена");
                                        }else {
                                            System.out.println("Книги с таким названием нет");
                                        }
                                        break;
                                    case 6:
                                        run=false;
                                        break;
                                    default:
                                        System.out.println("Неверный ввод. Попробуйте снова");
                                        break;
                                }
                            }
                            break;
                        case "Admin":
                            while(run) {
                                System.out.println("Что бы вы хотели сделать?\nПолучить список всех пользователей? Введите '1'.\nУдалить пользователя? Введите '2'.\nДать привелегии пользователю? Введите '3'.\nВыйти? Введите '4'.");
                                select = sc.nextInt();
                                String clientName;
                                String clientPriv;
                                switch (select) {
                                    case 1:
                                        clients = daoFactory.client().getAll();
                                        for (ClientImpl client3 : clients) {
                                            System.out.println(client3.getLogin() + " - " + client3.getPrivilege());
                                        }
                                        break;
                                    case 2:
                                        System.out.println("Введите логин пользователя: ");
                                        sc.nextLine();
                                        clientName = sc.nextLine();
                                        ClientImpl client1 = daoFactory.client().getByName(clientName);
                                        if (client1!=null) {
                                            if (!client1.getLogin().equals(client.getLogin())) {
                                                daoFactory.client().delete(client1.getId());
                                                System.out.println("Пользователь "+clientName +" удален");
                                            }else {
                                                System.out.println("Это не выход");
                                            }
                                        }else {
                                            System.out.println("Нет клиента с таким логином");
                                        }
                                        break;
                                    case 3:
                                        System.out.println("Введите логин пользователя: ");
                                        sc.nextLine();
                                        clientName = sc.nextLine();
                                        ClientImpl client2 = daoFactory.client().getByName(clientName);
                                        if (client2!=null) {
                                            if (!client2.getLogin().equals(client.getLogin())) {
                                                System.out.println("Какие привилегии будут у пользователя?(Client, Moder, Admin)");
                                                clientPriv = sc.nextLine();
                                                if(clientPriv.equals("Client") || clientPriv.equals("Moder") || clientPriv.equals("Admin")) {
                                                    daoFactory.client().setPriv(client2.getId(), clientPriv);
                                                    System.out.println("Пользователь "+ clientName + " теперь " + clientPriv);
                                                }else {
                                                    System.out.println("Таких привилегий нет");
                                                }
                                            }else {
                                                System.out.println("У вас и так все хорошо");
                                            }
                                        }else {
                                            System.out.println("Нет клиента с таким логином");
                                        }
                                        break;
                                    case 4:
                                        run=false;
                                        break;
                                    default:
                                        System.out.println("Неверный ввод. Попробуйте снова");
                                        break;
                                }
                            }
                            break;
                        case "Moder":
                            while(run) {
                                System.out.println("Что бы вы хотели сделать?\nПолучить список всех книг? Введите '1'.\nПолучить список всех авторов? Введите '2'.\nУзнать автора книги?  Введите '3'.\nДобавить автора?  Введите '4'.\nДобавить автору книгу?  Введите '5'.\nУдалить книгу?  Введите '6'.\nУдалить автора?  Введите '7'.\nВыйти? Введите '8'.");
                                String bookName, bookYear, authorName, bookGenre;
                                select = sc.nextInt();
                                switch (select) {
                                    case 1:
                                        books = daoFactory.book().getAll();
                                        for (BookImpl book1 : books) {
                                            System.out.println(book1.getTitle());
                                        }
                                        break;
                                    case 2:
                                        authors = daoFactory.author().getAll();
                                        for (AuthorImpl author1 : authors) {
                                            System.out.println(author1.getName());
                                        }
                                        break;
                                    case 3:
                                        System.out.println("Введите название книги: ");
                                        sc.nextLine();
                                        bookName = sc.nextLine();
                                        book = daoFactory.book().getByName(bookName);
                                        if(book!=null) {
                                            System.out.println(daoFactory.author().getByBook(book.getTitle()).getName());
                                        }else {
                                            System.out.println("Книги с таким названием нет");
                                        }
                                        break;
                                    case 4:
                                        System.out.println("Введите имя автора: ");
                                        sc.nextLine();
                                        authorName = sc.nextLine();
                                        if(daoFactory.author().getByName(authorName)==null) {
                                            daoFactory.author().create(authorName);
                                            System.out.println("Добавлен автор: " + authorName);
                                        }else {
                                            System.out.println("Такой автор уже есть");
                                        }
                                        break;
                                    case 5:
                                        System.out.println("Введите имя автора: ");
                                        sc.nextLine();
                                        authorName = sc.nextLine();
                                        author = daoFactory.author().getByName(authorName);
                                        if(author!=null){
                                            System.out.println("Введите название книги: ");
                                            bookName = sc.nextLine();
                                            System.out.println("Введите год книги: ");
                                            bookYear = sc.nextLine();
                                            System.out.println("Введите жанр книги: ");
                                            bookGenre = sc.nextLine();
                                            if (daoFactory.book().getByName(bookName)==null) {
                                                daoFactory.book().create(bookName, bookYear, bookGenre, author.getName());

                                                System.out.println("Книга " + bookName + " теперь написана автором " + authorName);
                                            }else {
                                                System.out.println("Книга с таким названием уже есть");
                                            }
                                        }else {
                                            System.out.println("Нет автора с таким именем");
                                        }
                                        break;
                                    case 6:
                                        System.out.println("Введите название книги: ");
                                        sc.nextLine();
                                        bookName = sc.nextLine();
                                        book = daoFactory.book().getByName(bookName);
                                        if(book!=null) {
                                            daoFactory.book().delete(book.getTitle());
                                            System.out.println("Книга " + book.getTitle() + " удалена");
                                        }else {
                                            System.out.println("Книги с таким названием нет");
                                        }
                                        break;
                                    case 7:
                                        System.out.println("Введите имя автора: ");
                                        sc.nextLine();
                                        authorName = sc.nextLine();
                                        author = daoFactory.author().getByName(authorName);
                                        if(author!=null){
                                            daoFactory.author().delete(author.getName());
                                            System.out.println("Автор " + author.getName() + " удален");
                                        }else {
                                            System.out.println("Нет автора с таким именем");
                                        }
                                        break;
                                    case 8:
                                        run=false;
                                        break;
                                    default:
                                        System.out.println("Неверный ввод. Попробуйте снова");
                                        break;
                                }
                            }
                            break;
                    }
                } else {
                    System.out.println("Неверный пароль");
                }
            } else {
                System.out.println("Такого пользователя нет");
            }
            System.out.print("Попробуете войти?(1) Зарегестрироваться?(2) Выйти?(3) : ");
            select = sc.nextInt();
            String clientName, clientPass, clientPass1;
            switch (select){
                case 1:
                    run=true;
                    break;
                case 2:
                    System.out.println("Введите логин: ");
                    sc.nextLine();
                    clientName = sc.nextLine();
                    client = daoFactory.client().getByName(clientName);
                    if(client==null){
                        System.out.println("Введите пароль: ");
                        clientPass = sc.nextLine();
                        System.out.println("Повторите пароль: ");
                        clientPass1 = sc.nextLine();
                        if (clientPass.equals(clientPass1)) {
                            daoFactory.client().create(clientName, clientPass);

                            System.out.println("Вы зарегестрированны. Пожалуйста, войдите в систему для работы");
                        }else {
                            System.out.println("Пароль введенн неверный");
                        }
                    }else {
                        System.out.println("Пользователь с таким именем уже существует");
                    }
                    run = true;
                    break;
                case 3:
                    run=false;
                    break;
                default:
                    System.out.println("Неверный ввод. Попробуйте снова");
                    break;
            }
        }
    }
}