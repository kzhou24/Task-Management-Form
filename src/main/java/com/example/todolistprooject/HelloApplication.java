package com.example.todolistprooject;

import com.example.todolistprooject.dataModel.TodoData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("mainBorder.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 500);
        stage.setTitle("Task TodoList");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() throws Exception {

        try {

            TodoData.getInstance().load();
        }catch (IOException e){

            System.out.println(e.getMessage());
        }
    }

    @Override
    public void stop() throws Exception {

        try {
            TodoData.getInstance().save();
        }
        catch (IOException e){

            System.out.println(e.getMessage());
        }

    }

}