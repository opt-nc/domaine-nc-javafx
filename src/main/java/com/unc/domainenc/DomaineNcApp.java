package com.unc.domainenc;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;
import com.unc.domainenc.api.Request;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class DomaineNcApp extends Application {

    public static final String os = System.getProperty("os.name").toLowerCase();
    private static final Logger logger = LoggerFactory.getLogger(DomaineNcApp.class);
    public static Image appIcon = new Image(Objects.requireNonNull(DomaineNcApp.class.getResourceAsStream("img/Icon.png")));
    public static String appStyle = Objects.requireNonNull(DomaineNcApp.class.getResource("css/StyleDomaineNc.css")).toExternalForm();
    private final String apiKEYname = "X_RAPIDAPI_KEY";
    private static HostServices webServices;

    public static void main(String[] args) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(loggerContext);
        try {
            configurator.doConfigure(DomaineNcApp.class.getResourceAsStream("logback.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
        logger.info("Lancement de l'application.");
        launch();
    }

    public static void afficheError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText("Erreur:\n" + message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(DomaineNcApp.appStyle);
        dialogPane.getStyleClass().add("color");
        alert.showAndWait();
    }

    public static void browse(String url) {
        if (webServices != null) {
            webServices.showDocument(url);
            logger.info("Ouverture de " + url);
        } else {
            logger.warn("Impossible d'acceder à " + url);
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Recherche de la clef d'API dans l'environement du systeme.");
        String apiKEY = System.getenv(apiKEYname);
        if (apiKEY == null) {
            logger.warn("Clef d'API non trouvee dans l'environement du systeme.");
            try {
                logger.info("Recherche de la clef d'API dans le fichier .env.");
                Dotenv dotenv = Dotenv.load();
                apiKEY = dotenv.get(apiKEYname);
                if (apiKEY == null) {
                    logger.warn("Clef d'API non trouvee dans le fichier .env.");
                    DomaineNcApp.afficheError(".env Incorect\n" + apiKEYname + " non trouvee.\n\n" +
                            "Le .env dois contenir: " + apiKEYname + "=Votre_clef_d'API");
                    System.exit(1);
                }
                logger.info("Clef d'API trouvee dans le fichier .env.");
            } catch (DotenvException e) {
                logger.warn("Fichier .env introuvable.");
                String errorMsg = "Impossible de trouver " + apiKEYname + ".\n\n" +
                        "Creer un fichier .env qui dois contenir: " + apiKEYname + "=Votre_clef_d'API\n" +
                        "ou\n" +
                        "Ajouter votre clef d'API dans l'environement du systeme.\n\n";
                if (os.contains("windows")) {
                    DomaineNcApp.afficheError(errorMsg + "Windows:\nsetx " + apiKEYname + " \"Votre_clef_d'API\"");
                } else if (os.contains("linux") || os.contains("mac")) {
                    DomaineNcApp.afficheError(errorMsg + "Linux/Mac:\nexport " + apiKEYname + "=\"Votre_clef_d'API\"");
                } else {
                    DomaineNcApp.afficheError(errorMsg);
                }
                System.exit(1);
            }
        } else {
            logger.info("Clef d'API trouvee dans l'environement du systeme.");
        }
        Request.setApiKey(apiKEY);
        FXMLLoader fxmlLoader = new FXMLLoader(DomaineNcApp.class.getResource("InterfaceDomaineNc.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 420, 440);
        scene.getStylesheets().add(DomaineNcApp.appStyle);
        stage.setTitle("DOMAINE.nc");
        stage.getIcons().add(DomaineNcApp.appIcon);
        stage.setScene(scene);
        stage.show();
        webServices = getHostServices();
    }
}
