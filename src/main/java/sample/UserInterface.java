package sample;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
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

    UserInterface(){
        gridPane = new GridPane();
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        time=new Text("0");
        gridPane.add(time,2,0);
        this.setWidth(600);
        this.setHeight(500);
        this.setTitle("Queues Simulator");
        Scene scene = new Scene(gridPane);
        gridPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #efb8d5, #e1d4ef);");
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
    }
    public static void styleText(Text text, int font) {
        text.setFont(Font.font("Arial", FontWeight.BOLD, font));
        text.setFill(Color.MIDNIGHTBLUE);
    }
    public void updateQ(List<Server> servers,int maxNrTasksPerQueue){
        int pos;
        for(int i=0;i<servers.size();i++){
            pos=2;
            for( Task task: servers.get(i).getTasks()){
                int finalPos = pos;
                int finalI = i;
                Text text=new Text("("+task.getID()+", "+task.getArrivalTime()+", "+task.getProcessingTime()+")");
                styleText(text,15);
                Platform.runLater(()->gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == finalI+1 && GridPane.getColumnIndex(node)==finalPos));
                Platform.runLater(()->gridPane.add(text, finalPos, finalI +1));
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
}
