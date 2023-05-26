package com.unc.domainenc;

import com.unc.domainenc.api.DomaineInfoEntity;
import com.unc.domainenc.api.Request;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Desktop;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


public class DomaineNcInfoController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(DomaineNcInfoController.class);
    private final String nom;
    private final Request request;
    private String ridet;
    @FXML
    private VBox infoVbox;

    public DomaineNcInfoController(String nom, Request request) {
        this.nom = nom;
        this.request = request;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info(String.format("Recuperation des information sur " + nom + ".nc."));
        DomaineInfoEntity domaineInfoEntity = request.getDomaineInfo(nom);
        this.ridet = cleanRidet(domaineInfoEntity.getBeneficiaire());
        addInfoLien("Ouvrir sur Domaine.nc", FontAwesomeIcon.GLOBE);
        addInfoLienRidet("Bénéficiaire :\n" + ridet, FontAwesomeIcon.CREDIT_CARD);
        addInfo("Gestionnaire :\n" + domaineInfoEntity.getGestionnaire(), FontAwesomeIcon.USER);
        addInfo("Date de création :\n" + setDate(domaineInfoEntity.getDateCreation()), FontAwesomeIcon.CHECK_CIRCLE_ALT);
        addInfo("Date d'expiration :\n" + setDate(domaineInfoEntity.getDateExpiration()), FontAwesomeIcon.CALENDAR_TIMES_ALT);
        addInfo("Temps avant expiration :\n" + setExpiration(domaineInfoEntity.getNbDaysBeforeExpires()), FontAwesomeIcon.HOURGLASS_END);
        addInfo("Serveur DNS :\n" + String.join(", ", domaineInfoEntity.getDns()), FontAwesomeIcon.SERVER);
    }

    public void addInfo(String contenu, FontAwesomeIcon iconType) {
        FontAwesomeIconView icon = new FontAwesomeIconView(iconType);
        icon.setSize("28px");
        icon.getStyleClass().add("color");
        Label info = new Label(contenu);
        info.getStyleClass().add("color");
        HBox container = new HBox(16);
        container.setPadding(new Insets(8));
        container.setPrefSize(300, 60);
        container.getChildren().addAll(icon, info);
        infoVbox.getChildren().add(container);
    }

    public void addInfoLien(String contenu, FontAwesomeIcon iconType) {
        FontAwesomeIconView icon = new FontAwesomeIconView(iconType);
        icon.setSize("28px");
        icon.getStyleClass().add("color");
        Label info = new Label(contenu);
        info.getStyleClass().add("color");
        info.setOnMouseClicked(event -> {
            try {
                openLink(event);
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        });
        HBox container = new HBox(16);
        container.setPadding(new Insets(8));
        container.setPrefSize(300, 60);
        container.getChildren().addAll(icon, info);
        infoVbox.getChildren().add(container);
    }

    public void addInfoLienRidet(String contenu, FontAwesomeIcon iconType) {
        FontAwesomeIconView icon = new FontAwesomeIconView(iconType);
        icon.setSize("28px");
        icon.getStyleClass().add("color");
        Label info = new Label(contenu);
        info.getStyleClass().add("color");
        if (this.ridet.startsWith("Ridet :")) {
            info.setOnMouseClicked(event -> {
                try {
                    openRidetEntreprise(event);
                } catch (URISyntaxException | IOException e) {
                    e.printStackTrace();
                }
            });
        }
        HBox container = new HBox(16);
        container.setPadding(new Insets(8));
        container.setPrefSize(300, 60);
        container.getChildren().addAll(icon, info);
        infoVbox.getChildren().add(container);
    }

    public String setDate(String stringDate) {
        try {
            SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dmyFormat = new SimpleDateFormat("dd MMMM yyyy");
            Date date = ymdFormat.parse(stringDate);
            return dmyFormat.format(date);
        } catch (ParseException dateException) {
            System.out.println("erreur parsing date");
        }
        return "";
    }

    public String setExpiration(int joursRestant) {
        String res;
        if (joursRestant > 365) {
            res = joursRestant / 365 + " années restantes";
        } else if (joursRestant > 31) {
            res = joursRestant / 31 + " mois restants";
        } else {
            res = joursRestant + " jours restants";
        }
        return res;
    }

    public String cleanRidet(String beneficiaire) {
        if (beneficiaire.startsWith("Ridet : ")) {
            int index = beneficiaire.indexOf("-", "Ridet : ".length());
            if (index != -1) {
                beneficiaire = beneficiaire.substring(0, index);
            }
        }
        return beneficiaire;
    }

    public void openLink(MouseEvent event) throws URISyntaxException,IOException{
        Desktop.getDesktop().browse(new URI("https://www.domaine.nc/whos?domain="+this.nom+"&ext=.nc"));
    }

    public void openRidetEntreprise(MouseEvent event) throws URISyntaxException,IOException{
        String[] num = this.ridet.split(" ");
        Desktop.getDesktop().browse(new URI("https://data.gouv.nc/explore/dataset/entreprises-actives-au-ridet/table/?disjunctive.libelle_formjur&disjunctive.code_ape&disjunctive.libelle_naf&disjunctive.section_naf&disjunctive.libelle_section_naf&disjunctive.libelle_commune&disjunctive.hors_nc&disjunctive.province&q="+num[2]));
    }

}
