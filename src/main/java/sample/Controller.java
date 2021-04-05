package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    private int clients,queues,simulationT,minArrivalT,maxArrivalT,minProcT,maxProcT;
    private String strategy;
    @FXML
    private Label titleLabel;
    @FXML
    private Label clientsLabel;
    @FXML
    private Label queuesLabel;
    @FXML
    private Label simulationLabel;
    @FXML
    private Label arrivalLabel;
    @FXML
    private Label processingLabel;
    @FXML
    private TextField clientsTextField;
    @FXML
    private TextField queuesTextField;
    @FXML
    private TextField simulationTextField;
    @FXML
    private TextField arrivalMinTextField;
    @FXML
    private TextField arrivalMaxTextField;
    @FXML
    private TextField processingMinTextField;
    @FXML
    private TextField processingMaxTextField;
    @FXML
    private Label minLabel;
    @FXML
    private Label maxLabel;
    @FXML
    private Button simulateButton;
    @FXML
    private Label errorLabel;

    @FXML
    private Label emptyFieldsLabel;

    @FXML
    private Button clearButton;
    @FXML
    private Label strategyLabel;

    @FXML
    private TextField strategyTextField;

    @FXML
    private Label errorStrategyLabel;
    @FXML
    private Label detailsLabel;
    @FXML
    void clickClear(ActionEvent event) {
        errorLabel.setVisible(false);
        emptyFieldsLabel.setVisible(false);
        errorStrategyLabel.setVisible(false);
        clientsTextField.clear();
        queuesTextField.clear();
        simulationTextField.clear();
        arrivalMinTextField.clear();
        arrivalMaxTextField.clear();
        processingMinTextField.clear();
        processingMaxTextField.clear();
        strategyTextField.clear();
    }
    @FXML
    void clickSimulate(ActionEvent event) {
        errorLabel.setVisible(false);
        emptyFieldsLabel.setVisible(false);
        errorStrategyLabel.setVisible(false);
        if(clientsTextField.getText().isEmpty() || queuesTextField.getText().isEmpty() || simulationTextField.getText().isEmpty() || arrivalMinTextField.getText().isEmpty() || arrivalMaxTextField.getText().isEmpty() || processingMinTextField.getText().isEmpty() || processingMaxTextField.getText().isEmpty()|| strategyTextField.getText().isEmpty()){
            emptyFieldsLabel.setVisible(true);
        }else {
            clients = Integer.parseInt(clientsTextField.getText());
            queues = Integer.parseInt(queuesTextField.getText());
            simulationT = Integer.parseInt(simulationTextField.getText());
            minArrivalT = Integer.parseInt(arrivalMinTextField.getText());
            maxArrivalT = Integer.parseInt(arrivalMaxTextField.getText());
            minProcT = Integer.parseInt(processingMinTextField.getText());
            maxProcT = Integer.parseInt(processingMaxTextField.getText());
            strategy=strategyTextField.getText();
            if (minArrivalT >= maxArrivalT) {
                errorLabel.setVisible(true);
            }else {
                if (minProcT >= maxProcT) {
                    errorLabel.setVisible(true);
                }else{
                    if(minProcT<0 || minArrivalT<0 || simulationT<0 || maxArrivalT>simulationT || maxProcT>simulationT){
                        errorLabel.setVisible(true);
                    }else{
                        SelectionPolicy selectionPolicy=SelectionPolicy.SHORTEST_QUEUE;
                        if(!strategy.equals("SHORTEST_TIME") && !strategy.equals("SHORTEST_QUEUE")){
                            errorStrategyLabel.setVisible(true);
                        }else{
                            if(strategy.equals("SHORTEST_TIME")){
                                selectionPolicy=SelectionPolicy.SHORTEST_TIME;
                            }
                            SimulationManager simulationManager=new SimulationManager(queues,clients,simulationT,minArrivalT,maxArrivalT,minProcT,maxProcT,selectionPolicy);
                            Thread thread=new Thread(simulationManager);
                            thread.start();
                        }
                    }

                }
            }
        }
    }
}
