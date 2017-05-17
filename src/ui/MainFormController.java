/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import ajpassignmentfx.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Wee Kiat
 */
public class MainFormController implements Initializable {

    @FXML
    private TextField tBSearch;
    @FXML
    private TableView<BusStop> TableBusStopResult;
    @FXML
    private TableView<Bus> TableBusResult;
    @FXML
    private TableView<Bus> TableBuses;
    @FXML
    private TableView<BusStop> TableBusStops;

    private final ObservableList<BusStop> BusStopSearchResult = FXCollections.observableArrayList();
    private final ObservableList<Bus> BusSearchResult = FXCollections.observableArrayList();
    private final ObservableList<BusStop> AllBusStops = FXCollections.observableArrayList();
    private final ObservableList<Bus> AllBuses = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableBusStopResult.setItems(BusStopSearchResult);
        TableBusResult.setItems(BusSearchResult);

        TableBusStops.setItems(AllBusStops);
        TableBuses.setItems(AllBuses);

        tBSearch.textProperty().addListener((observable) -> {
            RefreshSearchResult();
        });

        TableBusStopResult.setRowFactory(tv -> {
            TableRow<BusStop> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    BusStop rowData = row.getItem();
                    System.out.println(rowData);
                    ShowResultBusStop(rowData);
                }
            });
            return row;
        });
        // TODO
        RefreshSearchResult();
        ShowAllBusAndBusStops();
    }

    private void RefreshSearchResult() {
        String SearchText = tBSearch.getText();
        BusStopSearchResult.clear();
        BusSearchResult.clear();
        BusStopSearchResult.addAll(BusService.get().SearchBusStop(SearchText));
        BusSearchResult.addAll(BusService.get().SearchBus(SearchText));
    }

    private void ShowAllBusAndBusStops() {
        AllBusStops.addAll(BusService.get().GetAllBusStop());
        AllBuses.addAll(BusService.get().GetAllBuses());
    }
    
    private void ShowResultBusStop(BusStop bs)
    {
        Stage stage = new Stage();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/BusStopInfoForm.fxml"));
        Parent root = null;
        try {
            root = (Parent)loader.load();
        } catch (IOException ex) {
            Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        BusStopInfoFormController controller = (BusStopInfoFormController)loader.getController();
        controller.SetBusStop(bs);
        
        Scene BusStopInfoScene = new Scene(root);
        stage.setTitle("Bus Stop Info");
        stage.setScene(BusStopInfoScene);
        stage.initOwner(tBSearch.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.showAndWait();
    }
}
