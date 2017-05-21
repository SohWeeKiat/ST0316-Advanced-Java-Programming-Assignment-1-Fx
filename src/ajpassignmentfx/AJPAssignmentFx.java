/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajpassignmentfx;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import ui.SplashScreenController;

/**
 *
 * @author Wee Kiat
 */
public class AJPAssignmentFx extends Application {
    
    private void CreateMainForm() throws IOException
    {
        Stage stage = new Stage();
        
        Parent scene = FXMLLoader.load(getClass().getResource("/ui/MainForm.fxml"));
        Scene MainForm = new Scene(scene);
        
        stage.setTitle("Bus Service Browser");
        stage.setScene(MainForm);
        stage.show();
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.UNDECORATED);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/SplashScreen.fxml"));
        Parent root = (Parent)loader.load();
        SplashScreenController controller = (SplashScreenController)loader.getController();
       
        Scene SplashScreenScene = new Scene(root);
        stage.setScene(SplashScreenScene);
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent event){
               event.consume();
            }
        });
        stage.setOnShown(new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent event){
               if (!BusService.get().Initialize()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Initialization Error");
                    alert.setHeaderText("Initialization Error");
                    alert.setContentText("Failed to initialize bus & bus stop data");

                    alert.showAndWait();
                    Platform.exit();
                }
                controller.SetProgressBarToFull();
                new DelayedEvent(1000,() -> {
                    stage.close();
                   try {
                       CreateMainForm();
                   } catch (IOException ex) {
                       Logger.getLogger(AJPAssignmentFx.class.getName()).log(Level.SEVERE, null, ex);
                   }
                });
            }
        });
        javafx.geometry.Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - 473) / 2);
        stage.setY((primScreenBounds.getHeight() - 227) / 2);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
