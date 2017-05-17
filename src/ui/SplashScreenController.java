/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;

/**
 * FXML Controller class
 *
 * @author Wee Kiat
 */
public class SplashScreenController implements Initializable {

    @FXML
    private ProgressBar progress_bar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void SetProgressBarToFull()
    {
        progress_bar.setProgress(100);
    }
}
