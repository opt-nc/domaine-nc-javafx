package com.unc.domainenc;

import com.unc.domainenc.api.Request;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public class DomaineNcApp extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        String apiKEY = System.getenv("X-RAPIDAPI-KEY");
        if (apiKEY == null) {
            try {
                Dotenv dotenv = Dotenv.load();
                apiKEY = dotenv.get("X-RAPIDAPI-KEY");
                if (apiKEY == null) {
                    JOptionPane.showMessageDialog(null, "Erreur:\n.env Incorect\nX-RAPIDAPI-KEY non trouver.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            } catch (DotenvException e) {
                JOptionPane.showMessageDialog(null, "Erreur:\nImpossible de trouver X-RAPIDAPI-KEY.", "Erreur", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
        Request.setApiKey(apiKEY);
        FXMLLoader fxmlLoader = new FXMLLoader(DomaineNcApp.class.getResource("InterfaceDomaineNc.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 420, 440);
        scene.getStylesheets().add(Objects.requireNonNull(DomaineNcApp.class.getResource("css/StyleDomaineNc.css")).toExternalForm());
        stage.setTitle("DOMAINE.nc");
        stage.getIcons().add(new Image(Objects.requireNonNull(DomaineNcApp.class.getResourceAsStream("img/Icon.png"))));
        stage.setScene(scene);
        stage.show();
    }
}