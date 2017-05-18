/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import ajpassignmentfx.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author wee
 */
public class BusInfoFormController implements Initializable {

    @FXML
    private ImageView ImageBusCompany;
    @FXML
    private Label lBusNumber;
    @FXML
    private TabPane TabCtrl;
    @FXML
    private TableView<BusStopTableData> TableRoute1;
    @FXML
    private TableView<BusStopTableData> TableRoute2;

    private Bus CurrentBus;
    private final ObservableList<BusStopTableData> RouteList1 = FXCollections.observableArrayList();
    private final ObservableList<BusStopTableData> RouteList2 = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableRoute1.setItems(RouteList1);
        TableRoute2.setItems(RouteList2);
        // TODO
    }    
    
    public void SetBus(Bus b)
    {
        CurrentBus = b;
        
        lBusNumber.setText("Bus Number:\n" + b.getBusCode());
        
        int i = 0;
        HashMap<Integer,ArrayList<BusStop>> routes = b.GetRoutes();
        ObservableList<BusStopTableData> RouteList = RouteList1;
        for(ArrayList<BusStop> route : routes.values()){
            int Seq = 1;
            if (i > 0){
                RouteList = RouteList2;
            }
            for(BusStop bs : route){
                RouteList.add(new BusStopTableData(Integer.toString(Seq++),bs));
            }
            i++;
        }
        if (i <= 1)
            TabCtrl.getTabs().remove(1);
        
        Image image = new Image(CurrentBus.IsSBSBus() ? "/ui/images/SBS.png" : "/ui/images/SMRT.png");
        ImageBusCompany.setImage(image);
    }
}
