package com.example.jogo1;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.Optional;

public class PopupManager {

    public static void showInformationPopup(String title, String content) {
        showInformationPopup(title, content, null);
    }

    public static void showWarningPopup(String title, String content) {
        showWarningPopup(title, content, null);
    }

    public static Optional<String> showChoicePopup(String title, String header, List<String> choices) {
        return showChoicePopup(title, header, choices, null);
    }

    public static Optional<ButtonType> showConfirmationPopup(String title, String content) {
        return showConfirmationPopup(title, content, null);
    }

    public static Optional<String> showTextInputPopup(String title, String header, String defaultValue) {
        return showTextInputPopup(title, header, defaultValue, null);
    }

    public static void showInformationPopup(String title, String content, String iconPath) {
        Platform.runLater(() -> {
            Alert alert = createStyledAlert(Alert.AlertType.INFORMATION, title, content, iconPath);
            alert.showAndWait();
        });
    }

    public static void showWarningPopup(String title, String content, String iconPath) {
        Platform.runLater(() -> {
            Alert alert = createStyledAlert(Alert.AlertType.WARNING, title, content, iconPath);
            alert.showAndWait();
        });
    }

    public static Optional<String> showChoicePopup(String title, String header, List<String> choices, String iconPath) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        styleDialog(dialog.getDialogPane(), title, header, iconPath);
        return dialog.showAndWait();
    }


    public static Optional<ButtonType> showConfirmationPopup(String title, String content, String iconPath) {
        Alert alert = createStyledAlert(Alert.AlertType.CONFIRMATION, title, content, iconPath);
        return alert.showAndWait();
    }

    public static Optional<String> showTextInputPopup(String title, String header, String defaultValue, String iconPath) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        styleDialog(dialog.getDialogPane(), title, header, iconPath);
        return dialog.showAndWait();
    }

    private static Alert createStyledAlert(Alert.AlertType type, String title, String content, String iconPath) {
        Alert alert = new Alert(type);
        styleDialog(alert.getDialogPane(), title, content, iconPath);
        return alert;
    }

    private static void styleDialog(DialogPane dialogPane, String title, String content, String iconPath) {

        dialogPane.setHeaderText(title);
        dialogPane.setContentText(content);
        dialogPane.setMinSize(400, 250);

        String css = """
            .dialog-pane {
                -fx-background-color: #6699FF;
                -fx-background-radius: 20;
                -fx-border-radius: 20;
                -fx-border-width: 2;
                -fx-border-color: #3366CC;
                -fx-padding: 20;
            }
            .content {
                -fx-font-size: 14px;
                -fx-text-fill: white;
                -fx-font-weight: bold;
            }
            .header-panel {
                -fx-font-size: 18px;
                -fx-text-fill: white;
                -fx-font-weight: bold;
            }
            .button {
                -fx-background-color: #FFFFFF;
                -fx-text-fill: #3366CC;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                -fx-border-radius: 10;
            }
        """;

        dialogPane.getStylesheets().add("data:text/css," + css);
    }
}