/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import ajpassignmentfx.Bus;
import ajpassignmentfx.BusStop;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javax.swing.table.DefaultTableModel;

/**
 * FXML Controller class
 *
 * @author wee
 */
public class BusStopInfoFormController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private BusStop CurrentBusStop;
    @FXML
    private Label lBusStopNumber;
    @FXML
    private Label lBusStopDesc;
    @FXML
    private Label lRoadDesc;
    @FXML
    private TableView<Bus> TableBuses;
    @FXML
    private TableView<BusStopTableData> TableRoute;
  
    private final ObservableList<Bus> BusList = FXCollections.observableArrayList();
    private final ObservableList<BusStopTableData> BusRouteList = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableBuses.setItems(BusList);
        TableRoute.setItems(BusRouteList);
        
        TableBuses.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ShowBusRoute(newSelection);
            }
        });
        // TODO
    }    
    
    public void SetBusStop(BusStop bs)
    {
        CurrentBusStop = bs;
        
        lBusStopNumber.setText("Bus Stop Number:\n" + bs.getBusStopCode());
        lBusStopDesc.setText("Bus Stop Description:\n" + bs.getBusStopDesc());
        lRoadDesc.setText("Road Description:\n" + bs.getRoadDesc());
        
        BusList.addAll(bs.GetBuses());
    }
    
    private void ShowBusRoute(Bus b)
    {
        ArrayList<BusStop> bus_stops = b.GetRoute(CurrentBusStop);
        
        BusRouteList.clear();
        int Index = 1;
        for(BusStop bs : bus_stops){
            String Seq = bs != CurrentBusStop ? Integer.toString(Index++) : ("=>" + Index++);
            BusRouteList.add(new BusStopTableData(Seq,bs));
        }
    }
}
