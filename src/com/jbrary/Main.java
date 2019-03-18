package com.jbrary;

import com.jbrary.model.ORM;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Scene welcome;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("welcome.fxml"));
        primaryStage.setTitle("JBrary");
        welcome = new Scene(root, 600, 400);
        primaryStage.setScene(welcome);
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        ORM.getInstance().open();
        super.init();
    }

    @Override
    public void stop() throws Exception {
        ORM.getInstance().close();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}