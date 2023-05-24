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
        // TODO Positionner la clé "X_RAPIDAPI_KEY" statiue en variable final et passer par la vaiable au lieu de la code en dur partout
        String apiKEY = System.getenv("X_RAPIDAPI_KEY");
        // TODO AJouter de la log pour dire ce qui est fait comme recherche, très utile... "quand ça ne marche pas" et qu'il faut gérer un enduser
        if (apiKEY == null) {
            try {
                Dotenv dotenv = Dotenv.load();
                apiKEY = dotenv.get("X_RAPIDAPI_KEY");
                if (apiKEY == null) {
                    // TODO styler la fenêtre avec le thème de l'app
                    // cf https://github.com/adriens/domaine-nc-javafx/issues/20#issuecomment-1561907166
                    JOptionPane.showMessageDialog(null, "Erreur:\n.env Incorect\nX_RAPIDAPI_KEY non trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    // TODO : Ajouter de la log console pour dire ce qu'il faut faire pour que ça marche
                    // cf https://github.com/adriens/domaine-nc-javafx/issues/20#issuecomment-1561907166
                    System.exit(1);
                }
            } catch (DotenvException e) {
                JOptionPane.showMessageDialog(null, "Erreur:\nImpossible de trouver X_RAPIDAPI_KEY.", "Erreur", JOptionPane.ERROR_MESSAGE);
                // TODO : Ajouter de la log console pour dire ce qu'il faut faire pour que ça marche
                // cf https://github.com/adriens/domaine-nc-javafx/issues/20#issuecomment-1561907166
                System.exit(1);
            }
        }
        Request.setApiKey(apiKEY);
        FXMLLoader fxmlLoader = new FXMLLoader(DomaineNcApp.class.getResource("InterfaceDomaineNc.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 420, 440);
        scene.getStylesheets().add(Objects.requireNonNull(DomaineNcApp.class.getResource("css/StyleDomaineNc.css")).toExternalForm());
        
        // Passer par une varibale globale (ie. ne pas coder en dur ce genre contenu en local dans une méthode... sinon c pas facile à maintenir)
        stage.setTitle("DOMAINE.nc");
        stage.getIcons().add(new Image(Objects.requireNonNull(DomaineNcApp.class.getResourceAsStream("img/Icon.png"))));
        stage.setScene(scene);
        stage.show();
    }
}
