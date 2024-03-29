module nt.testingtool.istqb {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires zip4j;
    requires java.desktop;
    requires javafx.swing;
    requires metadata.extractor;
    requires org.apache.commons.imaging;
    requires org.apache.commons.io;

    opens nt.testingtool.istqb to javafx.fxml, javafx.graphics;
    exports nt.testingtool.istqb;
    exports nt.testingtool.istqb.Utils;
    opens nt.testingtool.istqb.Utils to javafx.fxml, javafx.graphics;
    exports nt.testingtool.istqb.pageController;
    opens nt.testingtool.istqb.pageController to javafx.fxml, javafx.graphics;
}