package com.unc.domainenc;

import com.unc.domainenc.api.DomaineInfoEntity;
import com.unc.domainenc.api.Request;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;



public class DomaineNcInfoController {
    @FXML
    private VBox infoVbox;

    public void initialize() {
        String titre = "1012";
        setInfo(titre);
    }


    public void setInfo(String titre){
        Request request = new Request();
        DomaineInfoEntity domaineInfoEntity = request.getDomaineInfo(titre);

        TextField infoBeneficiaire = new TextField(domaineInfoEntity.getBeneficiaire());
        infoBeneficiaire.setEditable(false);
        infoVbox.getChildren().add(infoBeneficiaire);

        TextField infoGestionnaire = new TextField(domaineInfoEntity.getGestionnaire());
        infoGestionnaire.setEditable(false);
        infoVbox.getChildren().add(infoGestionnaire);

        String dateCreation = domaineInfoEntity.getDateCreation();
        String dateExpiration = domaineInfoEntity.getDateExpiration();
        setDate(dateCreation);
        setDate(dateExpiration);

        int joursRestant = domaineInfoEntity.getNbDaysBeforeExpires();
        setExpiration(32);

        List<String> listDns = domaineInfoEntity.getDns();
        setListDns(listDns);

    }

    public void setDate(String stringDate){
        try {
            stringDate = stringDate.replace("-", " ");
            SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy MM dd");
            SimpleDateFormat dmyFormat = new SimpleDateFormat("dd MMMM yyyy");
            Date date = ymdFormat.parse(stringDate);
            TextField infoDate = new TextField(dmyFormat.format(date));
            infoDate.setEditable(false);
            infoVbox.getChildren().add(infoDate);
        } catch (ParseException dateException) {
            System.out.println("erreur parsing date");
        }
    }

    public void setExpiration(int joursRestant){
        int res;
        if (joursRestant>365) {
            res = joursRestant/365;
        }else if(joursRestant <= 365 && joursRestant > 31){
            res = joursRestant/31;
        }
        else{
            res = joursRestant;
        }
        TextField infoExpiration= new TextField(Integer.toString(res));
        infoExpiration.setEditable(false);
        infoVbox.getChildren().add(infoExpiration);  

    }

    public void setListDns(List<String> listDns){
        String string = "";
        TextField infoDns= new TextField();
        for (String dns : listDns) {
            string = string+","+dns;
        }
        infoDns.setText(string);
        infoDns.setEditable(false);
        infoVbox.getChildren().add(infoDns);
    }

}
