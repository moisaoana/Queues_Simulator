package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    private int clients,queues,simulationT,minArrivalT,maxArrivalT,minProcT,maxProcT;
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
    void clickClear(ActionEvent event) {
        errorLabel.setVisible(false);
        emptyFieldsLabel.setVisible(false);
        clientsTextField.clear();
        queuesTextField.clear();
        simulationTextField.clear();
        arrivalMinTextField.clear();
        arrivalMaxTextField.clear();
        processingMinTextField.clear();
        processingMaxTextField.clear();
    }
    @FXML
    void clickSimulate(ActionEvent event) {
        errorLabel.setVisible(false);
        emptyFieldsLabel.setVisible(false);
        if(clientsTextField.getText().isEmpty() || queuesTextField.getText().isEmpty() || simulationTextField.getText().isEmpty() || arrivalMinTextField.getText().isEmpty() || arrivalMaxTextField.getText().isEmpty() || processingMinTextField.getText().isEmpty() || processingMaxTextField.getText().isEmpty()){
            emptyFieldsLabel.setVisible(true);
        }else {
            clients = Integer.parseInt(clientsTextField.getText());
            queues = Integer.parseInt(queuesTextField.getText());
            simulationT = Integer.parseInt(simulationTextField.getText());
            minArrivalT = Integer.parseInt(arrivalMinTextField.getText());
            maxArrivalT = Integer.parseInt(arrivalMaxTextField.getText());
            minProcT = Integer.parseInt(processingMinTextField.getText());
            maxProcT = Integer.parseInt(processingMaxTextField.getText());
            if (minArrivalT >= maxArrivalT) {
                errorLabel.setVisible(true);
            }
            if (minProcT >= maxProcT) {
                errorLabel.setVisible(true);
            }
            SimulationManager simulationManager=new SimulationManager(queues,clients,simulationT,minArrivalT,maxArrivalT,minProcT,maxProcT);
            Thread thread=new Thread(simulationManager);
            thread.start();
        }

        //new UserInterface();
    }
}
