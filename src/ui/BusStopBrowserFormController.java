/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import ajpassignmentfx.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author wee
 */
public class BusStopBrowserFormController implements Initializable {

    @FXML
    private TextField tBSearch;
    @FXML
    private TableView<BusStop> TableBusStopResult;
    private final ObservableList<BusStop> BusStopSearchResult = FXCollections.observableArrayList();
    private BusStop Selection;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        TableBusStopResult.setItems(BusStopSearchResult);
        tBSearch.textProperty().addListener((observable) -> {
            RefreshSearchResult();
        });
        Selection = null;
        RefreshSearchResult();
        
        TableBusStopResult.setRowFactory(tv -> {
            TableRow<BusStop> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    BusStop rowData = row.getItem();
                    Selection = rowData;
                    Stage stage = (Stage)TableBusStopResult.getScene().getWindow();
                    stage.close();
                    //TableBusStopResult
                }
            });
            return row;
        });
    }    
    
    private void RefreshSearchResult() {
        String SearchText = tBSearch.getText();
        BusStopSearchResult.clear();
        BusStopSearchResult.addAll(BusService.get().SearchBusStop(SearchText));
    }
    
    public BusStop GetSelection()
    {
        return Selection;
    }
}
