package com.unc.domainenc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class DomaineNcApp extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DomaineNcApp.class.getResource("InterfaceDomaineNc.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 420, 440);
        scene.getStylesheets().add(Objects.requireNonNull(DomaineNcApp.class.getResource("css/StyleDomaineNc.css")).toExternalForm());
        stage.setTitle("DOMAINE.nc");
        stage.getIcons().add(new Image(Objects.requireNonNull(DomaineNcApp.class.getResourceAsStream("img/Icon.png"))));
        stage.setScene(scene);
        stage.show();
    }
}