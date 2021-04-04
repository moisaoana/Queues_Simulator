package sample;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.ColorConvertOp;
import java.util.List;

public class UserInterface extends Stage {
    GridPane gridPane;
    Text time;
    private int maxNrTasks;
    ScrollPane scrollPane;
    UserInterface(){
        gridPane = new GridPane();
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        time=new Text("0");
        gridPane.add(time,2,0);
        this.setWidth(1000);
        this.setHeight(500);
        this.setTitle("Queues Simulator");
        gridPane.setStyle("-fx-background-color: burlywood");
         scrollPane=new ScrollPane();
        scrollPane.setPrefSize(1000, 500);
        scrollPane.setFitToWidth(true);  //Adapt the content to the width of ScrollPane
        scrollPane.setFitToHeight(true);
        scrollPane.setContent(gridPane);
        Scene scene = new Scene(scrollPane);
        this.setScene(scene);
        this.show();

    }
    public  void displayQueues(int n){
        for(int i=0;i<n;i++){
            Text label=new Text("Q"+i);
            styleText(label,25);
            Text separator=new Text("|");
            styleText(separator,40);
            gridPane.add(label,0,i+1);
            gridPane.add(separator,1,i+1);
        }
    }
    public void changeTime(String n){
        styleText(time,30);
        time.setText("Time: "+n);
        scrollPane.setContent(gridPane);
    }
    public static void styleText(Text text, int font) {
        text.setFont(Font.font("Arial", FontWeight.BOLD, font));
        text.setFill(Color.BROWN);
    }
    public void updateQ(List<Server> servers,int maxNrTasksPerQueue){
        int pos;
        for(int i=0;i<servers.size();i++){
            pos=2;
            for( Task task: servers.get(i).getTasks()){
                int finalPos = pos;
                int finalI = i;
                Text text=new Text("Client: ("+task.getID()+", "+task.getArrivalTime()+", "+task.getProcessingTime()+")   ");
                text.setFont(Font.font("Arial", FontWeight.BOLD, 15));
                text.setFill(Color.WHITE);
                HBox hBox=new HBox(text);
                hBox.setStyle("-fx-border-color: #ffffff;");
                hBox.setBackground(new Background(new BackgroundFill(Color.MAROON,null,null)));
                Platform.runLater(()->gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == finalI+1 && GridPane.getColumnIndex(node)==finalPos));
                Platform.runLater(()->gridPane.add(hBox, finalPos, finalI +1));
                pos++;
            }
            for(int j=servers.get(i).getTasks().size();j<maxNrTasksPerQueue;j++){
                int finalPos = pos;
                int finalI = i;
                Platform.runLater(()->gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == finalI+1 && GridPane.getColumnIndex(node)==finalPos));
                pos++;
            }
        }

    }
    public void updateListOfTasks(List<Task>generatedTasks, int headOfList,List<Server> servers,int maxNrTasksPerQueue){
        int pos=2;
        for(int j=headOfList+1;j<generatedTasks.size();j++) {
            int finalPos = pos;
            int finalI = servers.size()+1;
            Text text=new Text("Client: ("+generatedTasks.get(j).getID()+", "+generatedTasks.get(j).getArrivalTime()+", "+generatedTasks.get(j).getProcessingTime()+")   ");
            text.setFont(Font.font("Arial", FontWeight.BOLD, 15));
            text.setFill(Color.WHITE);
            HBox hBox=new HBox(text);
            hBox.setStyle("-fx-border-color: #ffffff;");
            hBox.setBackground(new Background(new BackgroundFill(Color.CHOCOLATE,null,null)));
            Platform.runLater(()->gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == finalI+1 && GridPane.getColumnIndex(node)==finalPos));
            Platform.runLater(()->gridPane.add(hBox, finalPos, finalI +1));
            pos++;

        }
        for(int k=pos-2;k<maxNrTasksPerQueue;k++){
            int finalPos = k+2;
            int finalI = servers.size()+1;
            Platform.runLater(()->gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == finalI+1 && GridPane.getColumnIndex(node)==finalPos));
           // pos++;
        }
    }
}
