package com.anastasia.controller;

import com.anastasia.dao.BookDao;
import com.anastasia.data.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created on 24.10.2019.
 * @author Kodoma.
 */
public class DialogController {

    private BookDao bookDao;

    private TableView<Book> bookTable;

    @FXML
    private TextField textField;

    @FXML
    private TextField authorField;

    @FXML
    public void saveButtonAction(ActionEvent event) {
        final String name = textField.getText();
        final String author = authorField.getText();

        String description = "";

        if (name == null || name.isEmpty()) {
            System.out.println("Name is empty");
            description += "Название не заполонено\n";
        }
        if (author == null || author.isEmpty()) {
            System.out.println("Author is empty");
            description += "Автор не заполнен";
        }
        if (!description.isEmpty()) {
            showErrorMessage("Предупреждение", description);
            return;
        }
        try {
            final Book book = bookDao.saveBook(new Book(0, name, author));
            bookTable.getItems().add(book);

        } catch (Exception e) {
            showErrorMessage("Ошибка", e.getMessage());
        }
        bookTable.getSelectionModel().select(bookTable.getItems().size() - 1);
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
    public void cancelButtonAction(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    void setBookTable(TableView<Book> bookTable) {
        this.bookTable = bookTable;
    }
}
