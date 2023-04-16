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
    private VBox vBox;

    @FXML
    private TextField searchBar=new TextField("opt");
    

    private List<DomaineEntity> listDomaine;

    public void initialize() {
        Request request = new Request();
        listDomaine = request.getDomaine();
    }

    private void setSearch(TextField text){
        searchBar.setText(text.getText());
    }

    @FXML
    protected void click() {
        vBox.getChildren().clear();
        setSearch(searchBar);
        List<DomaineEntity> res=searchList(searchBar.getText());
        for (DomaineEntity domaine : res) {
            TextField textField = new TextField(String.format("Name: %s , Extensions: %s", domaine.getName(), domaine.getExtension()));
            textField.setEditable(false);
            vBox.getChildren().add(textField);
        }
    }

    protected List<DomaineEntity> searchList(String searchWords) {

        List<DomaineEntity> res=new ArrayList<>();
        for(DomaineEntity domaine : listDomaine){
            if(domaine.getName().contains(searchWords)) {
                res.add(domaine);
            }
        }
        return res;

    }
}