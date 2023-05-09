package nt.testingtool.istqb.pageController;

import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.lingala.zip4j.exception.ZipException;
import nt.testingtool.istqb.Utils.PageVBoxHandler;
import nt.testingtool.istqb.Utils.QuestionHandler;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;

import static nt.testingtool.istqb.Utils.PageVBoxHandler.*;
import static nt.testingtool.istqb.Utils.ProjectConfiguration.*;

public class HomePage_Controller implements Initializable {

    public Pane examPagePane;
    public VBox examPageVBox;
    public QuestionHandler questionHandler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        questionHandler = new QuestionHandler();
        try {
            //Access Home Page
            examPageVBox = setupHomePage();
            //Access Manage Page
//            examPageVBox = setupManagePage();
        }
        catch (IOException | ZipException | InvalidAlgorithmParameterException | NoSuchPaddingException |
                 IllegalBlockSizeException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException |
                 InvalidKeyException e) {
            throw new RuntimeException(e);
        }
//        catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        examPagePane.getChildren().add(examPageVBox);
    }
}
