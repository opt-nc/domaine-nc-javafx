module com.unc.domainenc {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires java.desktop;
    requires io.github.cdimascio.dotenv.java;
    requires spring.web;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires de.jensd.fx.glyphs.fontawesome;

    opens com.unc.domainenc to javafx.fxml;
    exports com.unc.domainenc;
    exports com.unc.domainenc.api;
}