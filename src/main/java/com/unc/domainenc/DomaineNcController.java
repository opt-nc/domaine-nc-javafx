package com.unc.domainenc;

import com.unc.domainenc.api.DomaineEntity;
import com.unc.domainenc.api.Request;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DomaineNcController {

    @FXML
    private VBox vBoxDomaines;

    @FXML
    private TextField searchBar;

    @FXML
    private Button searchButton;

    private Request request;

    private List<DomaineEntity> listDomaines;

    public void initialize() {
        request = new Request();
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
        searchBar.setDisable(true);
        vBoxDomaines.getChildren().clear();
        Thread searchThread = new Thread(() -> {
            List<DomaineEntity> sortList = searchList(searchBar.getText());
            final boolean[] altern = {true};
            for (DomaineEntity domaine : sortList) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() -> {
                    Label label = new Label(String.format("%s \nExtensions: %s", domaine.getName(), domaine.getExtension()));
                    label.setMinSize(260, 50);
                    label.setOnMouseClicked(this::onLabelClick);
                    FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.GLOBE);
                    icon.setSize("40px");
                    HBox container = new HBox(20);
                    container.setPadding(new Insets(4));
                    altern[0] = !altern[0];
                    if (altern[0]) {
                        container.getStyleClass().add("pos1");
                        label.getStyleClass().add("color");
                        icon.getStyleClass().add("color");
                    } else {
                        container.getStyleClass().add("pos2");
                        label.getStyleClass().add("colorInv");
                        icon.getStyleClass().add("colorInv");
                    }
                    container.setAlignment(Pos.CENTER_LEFT);
                    container.getChildren().addAll(icon, label);
                    vBoxDomaines.getChildren().add(container);
                });
            }
            searchButton.setDisable(false);
            searchBar.setDisable(false);
        });
        searchThread.setDaemon(true);
        searchThread.start();
    }

    public void onLabelClick(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (event.getClickCount() == 2) {
                Label label = (Label) event.getSource();
                String[] listContenu = label.getText().split(" ");
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(DomaineNcApp.class.getResource("InterfaceInfoDomaineNc.fxml"));
                    fxmlLoader.setController(new DomaineNcInfoController(listContenu[0], request));
                    Scene scene = new Scene(fxmlLoader.load(), 420, 440);
                    scene.getStylesheets().add(Objects.requireNonNull(DomaineNcApp.class.getResource("css/StyleDomaineNc.css")).toExternalForm());
                    stage.setTitle(listContenu[0] + listContenu[2]);
                    stage.getIcons().add(new Image(Objects.requireNonNull(DomaineNcApp.class.getResourceAsStream("img/Icon.png"))));
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
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