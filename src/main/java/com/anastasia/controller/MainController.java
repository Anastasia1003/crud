package com.anastasia.controller;

import com.anastasia.dao.BookDao;
import com.anastasia.dao.SQLBookDao;
import com.anastasia.data.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;

public class MainController {

    private BookDao bookDao;

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, Long> idColumn;

    @FXML
    private TableColumn<Book, String> nameColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TextField searchNameFiled;

    private FilteredData bookData = new FilteredData();

    @FXML
    private void initialize() {
        // Инициализируем объект доступа к данным
        try {
            bookDao = new SQLBookDao();

        } catch (SQLException | IOException e) {
            System.err.print("XMLBookDao initialize error");
        }
        // устанавливаем тип и значение, которое должно хранится в колонке
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        // Определяем данные, которые будут отображаться во время редактирования ячеек
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        authorColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        bookTable.setEditable(true);

        // заполняем таблицу данными
        bookTable.setItems(bookData.getBooks());

        // подготавливаем данные для таблицы
        try {
            bookData.getBooks().addAll(bookDao.getAllBooks());

        } catch (SQLException e) {
            showErrorMessage("Ошибка", e.getMessage());
        }
    }

    @FXML
    public void addButtonAction(ActionEvent event) {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/dialog.fxml"));
            final Parent root = loader.load();
            final Stage dialogStage = new Stage();

            final DialogController dialogController = loader.getController();

            dialogController.setBookDao(bookDao);
            dialogController.setBookTable(bookTable);

            dialogStage.setTitle("Добавление новой записи");
            dialogStage.setScene(new Scene(root, 500, 300));
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showErrorMessage(String title, String description) {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/error.fxml"));
            final Parent root = loader.load();
            final Stage errorStage = new Stage();

            final ErrorController errorController = loader.getController();

            errorController.setTitle(title);
            errorController.setDescription(description);

            errorStage.setScene(new Scene(root, 500, 300));
            errorStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void removeButtonAction(ActionEvent event) {
        final Book book = bookTable.getSelectionModel().getSelectedItem();

        if (book != null) {
            bookTable.getItems().remove(book);

            try {
                bookDao.removeBook(book);

            } catch (SQLException e) {
                showErrorMessage("Ошибка", e.getMessage());
            }
        }
    }

    @FXML
    public void removeAllButtonAction(ActionEvent event) {
        bookTable.getItems().clear();

        try {
            bookDao.removeAllBooks();

        } catch (SQLException e) {
            showErrorMessage("Ошибка", e.getMessage());
        }
    }

    public void onExit() {
        System.out.println("exit");
    }

    @FXML
    public void nameColumnEditCommit(TableColumn.CellEditEvent<Book, String> event) {
        final String newName = event.getNewValue();

        if (newName == null || newName.isEmpty()) {
            // Хак для обновления значения, перерисовывает колонку
            nameColumn.setVisible(false);
            nameColumn.setVisible(true);

            System.out.println("Name is empty");
            return;
        }
        final Book book = getBook(event);
        final String oldName = book.getName();

        if (newName.equals(oldName)) {
            System.out.println("It's the same name");
            return;
        }
        book.setName(event.getNewValue());

        try {
            bookDao.updateBook(book);

        } catch (SQLException e) {
            showErrorMessage("Ошибка", e.getMessage());
        }
    }

    @FXML
    public void authorColumnEditCommit(TableColumn.CellEditEvent<Book, String> event) {
        final String newAuthor = event.getNewValue();

        if (newAuthor == null || newAuthor.isEmpty()) {
            // Хак для обновления значения, перерисовывает колонку
            authorColumn.setVisible(false);
            authorColumn.setVisible(true);

            System.out.println("Author is empty");
            return;
        }
        final Book book = getBook(event);
        final String oldAuthor = book.getAuthor();

        if (newAuthor.equals(oldAuthor)) {
            System.out.println("It's the same author");
            return;
        }
        book.setAuthor(event.getNewValue());

        try {
            bookDao.updateBook(book);

        } catch (SQLException e) {
            showErrorMessage("Ошибка", e.getMessage());
        }
    }

    @FXML
    public void searchNameFiledPressed(KeyEvent event) {
        if (KeyCode.ENTER.equals(event.getCode())) {
            final String name = searchNameFiled.getText();

            if (name.isEmpty()) {
                bookTable.setItems(bookData.getBooks());
                return;
            }
            try {
                final List<Book> books = bookDao.searchBooksByName(name);
                bookTable.setItems(FXCollections.observableArrayList(books));

            } catch (SQLException e) {
                showErrorMessage("Ошибка", e.getMessage());
            }
        }
    }

    private <T> Book getBook(TableColumn.CellEditEvent<Book, T> event) {
        final int row = event.getTablePosition().getRow();
        return event.getTableView().getItems().get(row);
    }

    private static class FilteredData {

        private ObservableList<Book> books = FXCollections.observableArrayList();

        public ObservableList<Book> getBooks() {
            return books;
        }

        public ObservableList<Book> getFilteredBooks(Predicate<Book> predicate) {
            final ObservableList<Book> result = FXCollections.observableArrayList();

            for (Book book : books) {
                if (predicate.test(book)) {
                    result.add(book);
                }
            }
            return result;
        }

        public ObservableList<Book> mergeFilteredBooks(List<Book> booksFromDB) {
            final ObservableList<Book> result = FXCollections.observableArrayList();

            for (Book book1 : booksFromDB) {
                for (Book book2 : books) {
                    if (book1.getName().equals(book2.getName())) {
                        result.add(book1);
                    }
                }
            }
            return result;
        }

        public static class BookNamePredicate implements Predicate<Book> {

            private Book book;

            public BookNamePredicate(Book book) {
                this.book = book;
            }

            @Override
            public boolean test(Book book) {
                return this.book.getName().equals(book.getName());
            }
        }
    }
}
