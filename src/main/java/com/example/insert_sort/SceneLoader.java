package com.example.insert_sort;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneLoader {
    public static void load(String fxmlFile, boolean isModality,boolean isResizable,String title) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SceneLoader.class.getResource(fxmlFile));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.setResizable(isResizable);
        if (isModality) {
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } else {
            stage.show();
        }
    }
}
