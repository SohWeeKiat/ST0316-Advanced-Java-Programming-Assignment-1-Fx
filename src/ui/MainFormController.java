/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import ajpassignmentfx.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.Alert;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
    @FXML
    private TextField tBStartLoc;
    @FXML
    private TextField tBEndLoc;
    @FXML
    private TableView<BusStopPathCollection> TableRoutes;
    @FXML
    private TableView<BusStopTableData> TableRouteData;
    
    private final ObservableList<BusStop> BusStopSearchResult = FXCollections.observableArrayList();
    private final ObservableList<Bus> BusSearchResult = FXCollections.observableArrayList();
    private final ObservableList<BusStop> AllBusStops = FXCollections.observableArrayList();
    private final ObservableList<Bus> AllBuses = FXCollections.observableArrayList();
    
    private final ObservableList<BusStopPathCollection> Routes = FXCollections.observableArrayList();
    private final ObservableList<BusStopTableData> RouteData = FXCollections.observableArrayList();

    private BusStop StartLocation;
    private BusStop EndLocation;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableBusStopResult.setItems(BusStopSearchResult);
        TableBusResult.setItems(BusSearchResult);

        TableBusStops.setItems(AllBusStops);
        TableBuses.setItems(AllBuses);

        TableRoutes.setItems(Routes);
        TableRouteData.setItems(RouteData);
        
        tBSearch.textProperty().addListener((observable) -> {
            RefreshSearchResult();
        });

        TableBusStopResult.setRowFactory(tv -> {
            TableRow<BusStop> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    BusStop rowData = row.getItem();
                    ShowBusStopInfo(rowData);
                }
            });
            return row;
        });
        
        TableBusResult.setRowFactory(tv -> {
            TableRow<Bus> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Bus rowData = row.getItem();
                    ShowBusInfo(rowData);
                }
            });
            return row;
        });
        
        TableBusStops.setRowFactory(tv -> {
            TableRow<BusStop> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    BusStop rowData = row.getItem();
                    ShowBusStopInfo(rowData);
                }
            });
            return row;
        });
        
        TableBuses.setRowFactory(tv -> {
            TableRow<Bus> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Bus rowData = row.getItem();
                    ShowBusInfo(rowData);
                }
            });
            return row;
        });

        TableRoutes.setRowFactory(tv -> {
            TableRow<BusStopPathCollection> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()){
                    BusStopPathCollection rowData = row.getItem();
                    if (event.getClickCount() == 1){
                        ShowRouteInfo(rowData);
                    }else if (event.getClickCount() == 2){
                        ShowRouteOnMap(rowData);
                    }
                }
            });
            return row;
        });
        StartLocation = null;
        EndLocation = null;
        // TODO
        RefreshSearchResult();
        ShowAllBusAndBusStops();
    }

    private void ShowErrorMessage(String Title,String Header,String Desc)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(Title);
        alert.setHeaderText(Header);
        alert.setContentText(Desc);
        alert.showAndWait();
    }
    
    @FXML
    private void OnStartBrowseClicked(MouseEvent event) {
        boolean Success = false;
        do{
            StartLocation = BrowseForBusStop();
            if (StartLocation != null){
                 if (EndLocation != null && EndLocation == StartLocation){
                    ShowErrorMessage("Error","Error","Start location cannot be the same as destination");
                }else{
                     tBStartLoc.setText(StartLocation.toString());
                     Success = true;
                     SearchRoute();
                }
            }else{
                Success = true;
            }
        }while(!Success);
    }
    
    @FXML
    private void OnEndBrowseClicked(MouseEvent event) {
        boolean Success = false;
        do{
            EndLocation = BrowseForBusStop();
            if (EndLocation != null){
                 if (StartLocation != null && StartLocation == EndLocation){
                    ShowErrorMessage("Error","Error","Destination cannot be the same as start location");
                }else{
                     tBEndLoc.setText(EndLocation.toString());
                     Success = true;
                     SearchRoute();
                }
            }else{
                Success = true;
            }
        }while(!Success);
    }
    
    @FXML
    private void OnSwapClicked(MouseEvent event) {
        BusStop TempBS = StartLocation;
        StartLocation = EndLocation;
        EndLocation = TempBS;
        tBStartLoc.setText(StartLocation != null ? StartLocation.toString() : "");
        tBEndLoc.setText(EndLocation != null ? EndLocation.toString() : "");
        SearchRoute();
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
    
    private void ShowBusStopInfo(BusStop bs)
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
    
    private void ShowBusInfo(Bus b)
    {
        Stage stage = new Stage();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/BusInfoForm.fxml"));
        Parent root = null;
        try {
            root = (Parent)loader.load();
        } catch (IOException ex) {
            Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        BusInfoFormController controller = (BusInfoFormController)loader.getController();
        controller.SetBus(b);
        
        Scene BusInfoScene = new Scene(root);
        stage.setTitle("Bus Info");
        stage.setScene(BusInfoScene);
        stage.initOwner(tBSearch.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.showAndWait();
    }

    private BusStop BrowseForBusStop()
    {
        Stage stage = new Stage();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/BusStopBrowserForm.fxml"));
        Parent root = null;
        try {
            root = (Parent)loader.load();
        } catch (IOException ex) {
            Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        BusStopBrowserFormController controller = (BusStopBrowserFormController)loader.getController();
        
        Scene BusInfoScene = new Scene(root);
        stage.setTitle("Bus Info");
        stage.setScene(BusInfoScene);
        stage.initOwner(tBSearch.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.showAndWait();
        
        return controller.GetSelection();
    }
    
    private void SearchRoute()
    {
        if (StartLocation == null || EndLocation == null)
            return;
        Routes.clear();
        RouteData.clear();
        ArrayList<BusStopPathCollection> routes = BusService.get().GeneratePath(StartLocation, EndLocation);
        for(int i = 0;i < routes.size();i++){
            routes.get(i).SetIndex(i + 1);
        }
        Routes.addAll(routes);
        
        if (Routes.size() > 0){
            TableRoutes.getSelectionModel().selectFirst();
            ShowRouteInfo(Routes.get(0));
        }else
            ShowErrorMessage("Error", "Error", "Fail to find possible routes");
    }
    
    private void ShowRouteInfo(BusStopPathCollection c)
    {
        RouteData.clear();
        int index = 1;
        BusStopPath LastPath = null;
        for(BusStopPath p : c.GetPath()){
            LastPath = p;
            RouteData.add(new BusStopTableData(Integer.toString(index++),p.GetSrc(),p.GetBus()));
        }
        RouteData.add(new BusStopTableData(Integer.toString(index++),LastPath.GetDest(),LastPath.GetBus()));
    }
    
    private void ShowRouteOnMap(BusStopPathCollection c)
    {
        Stage stage = new Stage();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/GoogleMapForm.fxml"));
        Parent root = null;
        try {
            root = (Parent)loader.load();
        } catch (IOException ex) {
            Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        GoogleMapFormController controller = (GoogleMapFormController)loader.getController();
        controller.SetRoute(c);
        Scene MapScene = new Scene(root);
        stage.setTitle("Google Map Route");
        stage.setScene(MapScene);
        stage.initOwner(tBSearch.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.showAndWait();
    }
}
