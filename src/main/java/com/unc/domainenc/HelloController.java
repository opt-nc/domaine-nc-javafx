package com.unc.domainenc;

import com.unc.domainenc.api.DomaineEntity;
import com.unc.domainenc.api.Request;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML
    private VBox vBoxDomaines;

    @FXML
    private TextField searchBar;

    private List<DomaineEntity> listDomaines;

    public void initialize() {
        Request request = new Request();
        listDomaines = request.getDomaine();
    }

    @FXML
    protected void onClick() {
        vBoxDomaines.getChildren().clear();
        List<DomaineEntity> sortList = searchList(searchBar.getText());
        for (DomaineEntity domaine : sortList) {
            TextField textField = new TextField(String.format("Name: %s , Extensions: %s", domaine.getName(), domaine.getExtension()));
            textField.setEditable(false);
            vBoxDomaines.getChildren().add(textField);
        }
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