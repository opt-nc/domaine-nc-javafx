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
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DomaineNcController {

    private static final Logger logger = LoggerFactory.getLogger(DomaineNcController.class);
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
        logger.info("Recuperation des noms de domaines.");
        try {
            listDomaines = request.getDomaine();
        } catch (Exception e) {
            logger.error("Clef d'API invalide.");
            DomaineNcApp.afficheError("Votre clef d'API est invalide");
            System.exit(1);
        }
        searchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onClick();
            }
        });
    }

    @FXML
    protected void onClick() {
        String contenu = searchBar.getText();
        logger.info("Recherche " + contenu + ".");
        List<DomaineEntity> sortList = searchList(contenu);
        searchButton.setDisable(true);
        searchBar.setDisable(true);
        vBoxDomaines.getChildren().clear();
        Thread searchThread = new Thread(() -> {
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
                    FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.GLOBE);
                    icon.setSize("40px");
                    label.getStyleClass().add("color");
                    icon.getStyleClass().add("color");
                    HBox container = new HBox(20);
                    container.setPadding(new Insets(4));
                    altern[0] = !altern[0];
                    if (altern[0]) {
                        container.getStyleClass().add("pos1");
                    } else {
                        container.getStyleClass().add("pos2");
                    }
                    container.setAlignment(Pos.CENTER_LEFT);
                    container.setOnMouseClicked(this::onDomainesClick);
                    container.setOnMouseEntered(e -> container.setCursor(Cursor.HAND));
                    container.setOnMouseExited(e -> container.setCursor(Cursor.DEFAULT));
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

    public void onDomainesClick(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (event.getClickCount() == 2) {
                HBox container = (HBox) event.getSource();
                Label label = (Label) container.getChildren().get(1);
                String[] listContenu = label.getText().split(" ");
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(DomaineNcApp.class.getResource("InterfaceInfoDomaineNc.fxml"));
                    fxmlLoader.setController(new DomaineNcInfoController(listContenu[0], request));
                    Scene scene = new Scene(fxmlLoader.load(), 420, 440);
                    scene.getStylesheets().add(DomaineNcApp.appStyle);
                    stage.setTitle(listContenu[0] + listContenu[2]);
                    stage.getIcons().add(DomaineNcApp.appIcon);
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
            if (domaine.getName().startsWith(searchWords)) {
                res.add(domaine);
            }
        }
        return res;
    }
}