package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



import java.io.File;
import java.net.URL;
import java.util.Random;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL url=new File("src/main/java/sample/View.fxml").toURI().toURL();
        Parent root=FXMLLoader.load(url);
        primaryStage.setTitle("Queues Simulator");
        primaryStage.setScene(new Scene(root, 300, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);

    }
}