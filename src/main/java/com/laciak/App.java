package com.laciak;

import com.laciak.Model.Job;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"));
        stage.setScene(scene);
        stage.setTitle("Job Searching Process Application");
        stage.show();
        this.stage=stage;
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        App.stage.setTitle("Job Searching Process Application");
        App.stage.setHeight(760);
        App.stage.setWidth(1300);
    }
    static void setRoot(String fxml,Job job) throws IOException {
        FXMLLoader loader= new FXMLLoader(App.class.getResource(fxml+".fxml"));
        scene.setRoot(loader.load());
        App.stage.setTitle("Edit Application Data");
        App.stage.setWidth(600);
        App.stage.setHeight(600);
        SecondaryController secondaryController=loader.getController();
        secondaryController.setId(job);

    }
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }


    public static void main(String[] args) {
        launch();
    }

}