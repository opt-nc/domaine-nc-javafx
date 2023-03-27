module com.unc.domainenc {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.unc.domainenc to javafx.fxml;
    exports com.unc.domainenc;
}