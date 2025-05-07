module ro.mpp2024.temalab4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.context;
    requires org.apache.logging.log4j;
    requires java.sql;


    opens ro.mpp2024.temalab4 to javafx.fxml;
    exports ro.mpp2024.temalab4;
}