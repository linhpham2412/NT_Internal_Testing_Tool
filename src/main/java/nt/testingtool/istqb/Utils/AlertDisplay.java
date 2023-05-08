package nt.testingtool.istqb.Utils;

import javafx.scene.control.Alert;

public class AlertDisplay {

    public static void displayMissingInformationAlert(String alertHeader, String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Missing Information");
        alert.setHeaderText(alertHeader);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }

    public static void displayTimingAlert(String alertHeader, String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Timing Alert");
        alert.setHeaderText(alertHeader);
        alert.setContentText(alertMessage);
        alert.show();
//        alert.showAndWait().ifPresent(rs -> {
//            if (rs == ButtonType.OK) {
//                changeStageAndScene(actionEvent, setupSummaryPage(toolFont), "Summary Page");
//            }
//        });
    }
}
