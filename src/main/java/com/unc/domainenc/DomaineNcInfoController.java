package com.unc.domainenc;

import com.unc.domainenc.api.DomaineInfoEntity;
import com.unc.domainenc.api.Request;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


public class DomaineNcInfoController implements Initializable {
    private final String nom;
    @FXML
    private VBox infoVbox;

    public DomaineNcInfoController(String nom) {
        this.nom = nom;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Request request = new Request();
        DomaineInfoEntity domaineInfoEntity = request.getDomaineInfo(nom);
        addInfo("Bénéficiaire :\n"+domaineInfoEntity.getBeneficiaire());
        addInfo("Gestionnaire :\n"+domaineInfoEntity.getGestionnaire());
        addInfo("Date de création :\n"+setDate(domaineInfoEntity.getDateCreation()));
        addInfo("Date d'expiration :\n"+setDate(domaineInfoEntity.getDateExpiration()));
        addInfo("Temps avant expiration :\n"+setExpiration(domaineInfoEntity.getNbDaysBeforeExpires()));
        addInfo("Serveur DNS :\n"+String.join(", ", domaineInfoEntity.getDns()));
    }

    public void addInfo(String contenu) {
        Label info = new Label(contenu);
        info.setPrefSize(300,50);
        infoVbox.getChildren().add(info);
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
}
