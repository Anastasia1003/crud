package com.anastasia.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created on 26.10.2019.
 * @author Kodoma.
 */
public class ErrorController {

    @FXML
    private Text titleText;

    @FXML
    private Text descriptionText;

    @FXML
    public void okButtonAction(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

    public void setTitle(String title) {
        //final double height = errorStage.getHeight();
        //final double width = errorStage.getWidth();

        titleText.setText(title);

        //AnchorPane.setLeftAnchor(errorTitle, 10D);
    }

    public void setDescription(String description) {
        descriptionText.setText(description);
    }
}
