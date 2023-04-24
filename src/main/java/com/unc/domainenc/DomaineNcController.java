package com.unc.domainenc;


import com.unc.domainenc.api.DomaineEntity;
import com.unc.domainenc.api.Request;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

public class DomaineNcController {

    @FXML
    private VBox vBoxDomaines;

    @FXML
    private TextField searchBar;

    @FXML
    private Button searchButton;

    private List<DomaineEntity> listDomaines;

    public void initialize() {
        Request request = new Request();
        listDomaines = request.getDomaine();
        searchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onClick();
            }
        });
    }

    @FXML
    protected void onClick() {
        searchButton.setDisable(true);
        vBoxDomaines.getChildren().clear();
        Thread searchThread = new Thread(() -> {
            List<DomaineEntity> sortList = searchList(searchBar.getText());
            for (DomaineEntity domaine : sortList) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() -> {
                    TextField textField = new TextField(String.format("Name: %s , Extensions: %s", domaine.getName(), domaine.getExtension()));
                    textField.setEditable(false);
                    vBoxDomaines.getChildren().add(textField);
                });
            }
            searchButton.setDisable(false);
        });
        searchThread.setDaemon(true);
        searchThread.start();
    }

    protected List<DomaineEntity> searchList(String searchWords) {
        List<DomaineEntity> res = new ArrayList<>();
        for (DomaineEntity domaine : listDomaines) {
            boolean check = true;
            for (int i = 0; i < searchWords.length(); i++) {
                if (domaine.getName().charAt(i) != searchWords.charAt(i)) {
                    check = false;
                    break;
                }
            }
            if (check) {
                res.add(domaine);
            }
        }
        return res;
    }
}