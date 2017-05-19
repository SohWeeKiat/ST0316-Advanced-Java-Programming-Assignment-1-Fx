/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import ajpassignmentfx.*;
import ajpassignmentfx.BusService;
import ajpassignmentfx.BusStop;
import ajpassignmentfx.BusStopPathCollection;
import ajpassignmentfx.DelayedEvent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * FXML Controller class
 *
 * @author wee
 */
public class GoogleMapFormController implements Initializable {

    @FXML
    private WebView webView;
    private WebEngine webEngine;
    
    private BusStopPathCollection route;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        webEngine = webView.getEngine();
        final URL urlGoogleMaps = getClass().getResource("/ui/google_map.html");
        webEngine.load(urlGoogleMaps.toExternalForm());
        
        /*new DelayedEvent(2000,()->{
            List<BusStop> list = BusService.get().GetAllBusStop();
            
            
        });*/
        webEngine.getLoadWorker().stateProperty().addListener(
            new ChangeListener<State>() {
            @Override 
            public void changed(ObservableValue ov, State oldState, State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    double lat = route.GetPath().getFirst().GetSrc().GetY();
                    double lon = route.GetPath().getFirst().GetSrc().GetX();
                    String JavaScriptCode = 
                            "var map = new google.maps.Map(document.getElementById('mapcanvas'), Options);" +
                            "var latLng = new google.maps.LatLng(" + lat + "," +  lon + ");" +
                            "map.setCenter(latLng);";
                    int index = 1;
                    BusStopPath LastPath = null;
                    for(BusStopPath p : route.GetPath()){
                        LastPath = p;
                        JavaScriptCode += AddMarker(index,LastPath.GetSrc());
                        index++;
                    }
                    JavaScriptCode += AddMarker(index,LastPath.GetDest());
                    System.out.println(JavaScriptCode);
                    webEngine.executeScript(JavaScriptCode);
                }
            }
        });
    }

    private String AddMarker(int index, BusStop b)
    {
        String MarkerVariable = "marker" + index;
        return "var " + MarkerVariable + " = new google.maps.Marker({"+
                "position: new google.maps.LatLng(" + b.GetY() + "," + b.GetX() + "),"+
                "map: map," + 
                "draggable: false," + 
                "title: \"" + b.toString() + "\"," + 
                "autoPan: true," + 
                "label:'" + index + "'" +
                "});";
    }
    
    public void SetRoute(BusStopPathCollection c){
        route = c;
    }
}
