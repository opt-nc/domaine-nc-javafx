module com.unc.domainenc {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.github.cdimascio.dotenv.java;
    requires spring.web;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    opens com.unc.domainenc to javafx.fxml;
    exports com.unc.domainenc;
    exports com.unc.domainenc.api;
}