package nt.testingtool.istqb.Utils;

import com.drew.imaging.ImageProcessingException;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.stream.Collectors;

import static nt.testingtool.istqb.Utils.EncryptDecryptBased64.*;
import static nt.testingtool.istqb.Utils.ImageCaptureHandler.*;
import static nt.testingtool.istqb.Utils.ProjectConfiguration.*;

public class PageVBoxHandler {
    static Stage mainStage;

    public static ComboBox selectTestingTypeComboBox;
    public static TextField testNameTextField;
    public static Button btn_StartTest;
    public static String testUserName;
    private static void changeStageAndScene(ActionEvent event, VBox layoutVBoxContainer, String sceneTitle) {
        mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Pane layout = new Pane(layoutVBoxContainer);
        Scene scene = new Scene(layout, screenWidth, screenHeight);
        mainStage.setResizable(false);
        mainStage.setTitle(sceneTitle);
        mainStage.setScene(scene);
    }

    private static void openNewStageAndScene(VBox layoutVBoxContainer, String sceneTitle) {
        Stage newStage = new Stage();
        Pane layout = new Pane(layoutVBoxContainer);
        Scene scene = new Scene(layout, screenWidth, screenHeight);
        newStage.setResizable(false);
        newStage.setTitle(sceneTitle);
        newStage.setScene(scene);
        newStage.show();
    }
    public static VBox setupHomePage(Font toolFont, QuestionHandler questionHandler) throws IOException, net.lingala.zip4j.exception.ZipException {
        //Read zip data file
        questionHandler.readQuestionZipFile(getQuestionFileName(), getZipFilePassword());
        questionHandler.readAndSaveAllISTQBTypeInData(getZipFilePassword());
        //Set up layout
        Pane blankPaneHeader = new Pane();
        double headerHeight = screenHeight / 8;
        blankPaneHeader.setPrefHeight(headerHeight);
        blankPaneHeader.setBackground(new Background(new BackgroundFill(Paint.valueOf("#CACACA"), CornerRadii.EMPTY, Insets.EMPTY)));
        ImageView nashTechLogo = new ImageView();
        nashTechLogo.setFitWidth(headerHeight);
        nashTechLogo.setFitHeight(headerHeight);
        HBox logoBox = new HBox();
        HBox infoBox = new HBox();
        Button informationButton = new Button("Credit");
//        ImageView infoImage = new ImageView(new Image("nt/istqbtt/nt_istqbtt/infomationIcon.png"));
//        infoImage.setFitHeight(headerHeight / 2);
//        infoImage.setFitWidth(headerHeight / 2);
//        informationButton.setGraphic(infoImage);
        informationButton.setOnAction(event -> {
//            changeStageAndScene(event, setupCreditPage(toolFont), "Credit Page");

            try {
                openNewStageAndScene(setupCertificatePage(toolFont), "Certificate Page");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
        logoBox.setPrefSize(screenWidth / 2, headerHeight);
        infoBox.setPrefSize(screenWidth / 2, headerHeight);
        logoBox.setAlignment(Pos.TOP_LEFT);
        infoBox.setAlignment(Pos.CENTER_RIGHT);
        logoBox.getChildren().add(nashTechLogo);
        infoBox.getChildren().add(informationButton);
        infoBox.setTranslateX(screenWidth / 2);
        HBox.setMargin(informationButton, new Insets(20, 20, 20, 20));
        blankPaneHeader.getChildren().add(logoBox);
        blankPaneHeader.getChildren().add(infoBox);
//        nashTechLogo.setImage(new Image("nt/istqbtt/nt_istqbtt/NashTechLogo.png"));
        Pane blankPaneFooter = new Pane();
        blankPaneFooter.setPrefHeight(screenHeight / 8);
        blankPaneFooter.setBackground(new Background(new BackgroundFill(Paint.valueOf("#CACACA"), CornerRadii.EMPTY, Insets.EMPTY)));
        Label welcomeTitle = new Label("Welcome To Internal ISTQB Knowledge Testing Tool");
        welcomeTitle.setPrefWidth(screenWidth / 3);
        welcomeTitle.setWrapText(true);
        welcomeTitle.setAlignment(Pos.CENTER);
        welcomeTitle.setStyle("-fx-font-size: 48; -fx-font-weight: bold;-fx-text-alignment: center;");
        HBox welcomeContainer = new HBox();
        HBox welcomeBorder = new HBox();
        Pane blankLeftPane = new Pane();
        blankLeftPane.setPrefWidth(screenWidth / 8);
        Pane blankRightPane = new Pane();
        blankRightPane.setPrefWidth(screenWidth / 8);
        welcomeBorder.setAlignment(Pos.CENTER);
        welcomeBorder.setStyle("-fx-border-color: red; -fx-border-width: 10px; -fx-border-style: solid;");
        welcomeBorder.getChildren().add(welcomeTitle);
        HBox.setMargin(welcomeTitle, new Insets(20, 20, 20, 20));
        welcomeContainer.getChildren().add(blankLeftPane);
        welcomeContainer.getChildren().add(welcomeBorder);
        welcomeContainer.getChildren().add(blankRightPane);
        welcomeContainer.setAlignment(Pos.CENTER);
        HBox.setMargin(welcomeBorder, new Insets(20, 20, 0, 20));
        Label creatorTitle = new Label("By Linh Pham");
        creatorTitle.setFont(toolFont);
        HBox selectTestingTypeHBox = new HBox();
        Label selectYourTestingTypeLabel = new Label("Select ISTQB:");
        selectYourTestingTypeLabel.setFont(toolFont);

        //Set up ISTQB Type information pane
        HBox istqbInformationHBox = new HBox();
        istqbInformationHBox.setPrefSize(screenWidth, screenHeight / 3.5);
        VBox infoVbox = new VBox();
        infoVbox.setPrefWidth(screenWidth / 2);
        infoVbox.setStyle("-fx-font-size: 16; -fx-border-width: 5px; -fx-border-style: solid;-fx-border-color: #3282F6;");
        HBox.setMargin(infoVbox, new Insets(20, 20, 20, 20));
        istqbInformationHBox.setAlignment(Pos.CENTER);
        Label istqbDetailText = new Label("Detail of ISTQB here");
        istqbDetailText.setFont(toolFont);
        istqbDetailText.setStyle("-fx-text-alignment: center;");
        istqbDetailText.setPrefWidth(screenWidth / 2);
        istqbDetailText.setWrapText(true);


        infoVbox.setAlignment(Pos.TOP_LEFT);
        infoVbox.getChildren().add(istqbDetailText);
        infoVbox.setBackground(new Background(new BackgroundFill(Paint.valueOf("#CACACA"), CornerRadii.EMPTY, Insets.EMPTY)));
        VBox.setMargin(istqbDetailText, new Insets(5, 0, 0, 5));

        istqbInformationHBox.getChildren().add(infoVbox);
        //End information pane

        selectTestingTypeComboBox = new ComboBox();
        questionHandler.getListOfISTQBTypeReadFromData().stream()
                .map(e -> selectTestingTypeComboBox.getItems().add(e)).collect(Collectors.toList());
        selectTestingTypeComboBox.setStyle("-fx-font-size: 16");
        selectTestingTypeComboBox.setOnAction(event -> {
            ProjectConfiguration.setQuestionGroupName((String) selectTestingTypeComboBox.getValue());
            istqbDetailText.setText(selectTestingTypeComboBox.getValue().toString() + "\nNumber of Question: 40\nPassing Score (at least 65%): 26\nTesting Time: 60 minutes\n" +
                    "Note that the score calculate based on number of correct questions!");
        });
        selectTestingTypeHBox.setAlignment(Pos.CENTER);
        selectTestingTypeHBox.getChildren().add(selectYourTestingTypeLabel);
        selectTestingTypeHBox.getChildren().add(selectTestingTypeComboBox);
        HBox.setMargin(selectYourTestingTypeLabel, new Insets(10, 10, 10, 10));
        HBox.setMargin(selectTestingTypeComboBox, new Insets(10, 10, 10, 10));


        //set up command button
        Label testNameLabel = new Label("Your Name: ");
        testNameLabel.setFont(toolFont);
        testNameTextField = new TextField();
        testNameTextField.setFont(toolFont);
        btn_StartTest = new Button("Start Test");
        btn_StartTest.setFont(toolFont);
        btn_StartTest.setOnAction(event -> {
            if (selectTestingTypeComboBox.getValue() != null) {
                questionHandler.isFirstLoad = true;
                testUserName = testNameTextField.getText();

//                try {
//                    changeStageAndScene(event, setupLayoutPageExam(toolFont), "Examination Page of: " + selectTestingTypeComboBox.getValue());
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                } catch (ZipException e) {
//                    throw new RuntimeException(e);
//                }

            }
        });
        Button quitAppHomeButton = new Button("Quit");
        quitAppHomeButton.setFont(toolFont);
        quitAppHomeButton.setOnAction(event -> {
//            try {
//                cleanDirectory(questionHandler.imagesFolder);
//                questionHandler.imagesFolder.delete();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
            ((Stage) (quitAppHomeButton.getScene().getWindow())).close();
        });
        HBox commandContainer = new HBox();
        commandContainer.setAlignment(Pos.CENTER);
        commandContainer.getChildren().add(testNameLabel);
        commandContainer.getChildren().add(testNameTextField);
        commandContainer.getChildren().add(btn_StartTest);
        commandContainer.getChildren().add(quitAppHomeButton);
        HBox.setMargin(btn_StartTest, new Insets(10, 10, 10, 10));
        HBox.setMargin(quitAppHomeButton, new Insets(10, 10, 10, 10));

        //Set up Result VBox
        VBox resultVBox = new VBox();
        resultVBox.setPrefWidth(screenWidth);
        resultVBox.setAlignment(Pos.CENTER);
        resultVBox.getChildren().add(blankPaneHeader);
        resultVBox.getChildren().add(welcomeContainer);
        resultVBox.getChildren().add(creatorTitle);
        resultVBox.getChildren().add(selectTestingTypeHBox);
        resultVBox.getChildren().add(commandContainer);
        resultVBox.getChildren().add(istqbInformationHBox);
        resultVBox.getChildren().add(blankPaneFooter);

        return resultVBox;
    }

    private static VBox setupCertificatePage(Font toolFont) throws Exception {
//        Image certificateBackGround = new Image("nt/testingtool/istqb/imageAsset/NTInternalCertificate.png");
        Image certificateBackGround = new Image("C:\\Users\\linhpham\\IdeaProjects\\demo\\src\\main\\resources\\nt\\testingtool\\istqb\\imageAsset\\NTInternalCertificate.png");
        Label testUserNameCertificate = new Label("ANH TRAN THI HUYNH");
        testUserNameCertificate.setStyle("-fx-font-size: 48; -fx-font-weight: bold;-fx-text-alignment: center;");
        testUserNameCertificate.setTextFill(Color.valueOf("#284977"));
        testUserNameCertificate.setFont(new Font("Poppins Extrabold", 36));
        Label testTypeCertificate = new Label("Advanced CTAL-TTA Certified Tester Advanced Level Technical Test Analyst v3.0");
        testTypeCertificate.setStyle("-fx-font-size: 36; -fx-font-weight: bold;-fx-text-alignment: center;");
        testTypeCertificate.setTextFill(Color.valueOf("#FA6070"));
        testTypeCertificate.setFont(new Font("Lato Bold", 36));
        testTypeCertificate.setPrefWidth(screenWidth / 1.8);
        testTypeCertificate.setWrapText(true);
//        Label verificationTextLabel = new Label("Verification Text:");
//        verificationTextLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
//        verificationTextLabel.setTextFill(Color.valueOf("#284977"));
        String encryptedText = encryptTextBase64WithSecretKey("This is a certificate to ANH TRAN THI HUYNH | " +
                "Passed ISTQB - Certified Tester Foundation Level | Pass Score 30/40 | Test Date: April 25, 2023", "123");
        System.out.println(encryptedText);

//        System.out.println(System.getProperty("java.classpath"));
//        String[] blocks = encryptedText.split("(?<=\\G.{5})");
//        String encryptedLabel = "";
//        for (int i=0;i<blocks.length;i++){
//            encryptedLabel += blocks[i] + " ";
//        }
//        Image qrImage = SwingFXUtils.toFXImage(generateQRCodeImage(encryptedText), null);
//        ImageView qrcode = new ImageView(qrImage);
//        qrcode.setFitHeight(150);
//        qrcode.setFitWidth(150);
//        Label encryptedCertificateInfo = new Label(encryptedText);
//        encryptedCertificateInfo.setPrefWidth(screenWidth / 2);
//        encryptedCertificateInfo.setWrapText(true);
//        encryptedCertificateInfo.setStyle("-fx-font-weight: bold;");
////        encryptedCertificateInfo.setTextFill(Color.valueOf("#284977"));
//        encryptedCertificateInfo.setFont(new Font("Poppins Extrabold", 20));
//        System.out.println(encryptedCertificateInfo.getText());
//        System.out.println("Decrypt: " + decryptedBase64TextWithSecretKey(encryptedCertificateInfo.getText(), "123"));

        VBox certificateVbox = new VBox();
        certificateVbox.setAlignment(Pos.TOP_CENTER);
        certificateVbox.setPrefSize(screenWidth / 1.2, screenHeight);
        certificateVbox.getChildren().add(testTypeCertificate);
        certificateVbox.getChildren().add(testUserNameCertificate);
//        certificateVbox.getChildren().add(verificationTextLabel);
//        certificateVbox.getChildren().add(encryptedCertificateInfo);
//        certificateVbox.getChildren().add(qrcode);
        VBox.setMargin(testTypeCertificate, new Insets(screenHeight / 3, 0, 0, 0));
        System.out.println(testTypeCertificate.getText().length());
        if (testTypeCertificate.getText().length() < 44) {
            VBox.setMargin(testUserNameCertificate, new Insets(screenHeight / 6.5, 0, 0, 0));
        } else {
            VBox.setMargin(testUserNameCertificate, new Insets(screenHeight / 11, 0, 0, 0));
        }
//        VBox.setMargin(verificationTextLabel, new Insets(screenHeight / 6.5, 0, 0, 0));
//        VBox.setMargin(qrcode, new Insets(screenHeight / 8, 0, 0, 0));
        certificateVbox.setBackground(new Background(new BackgroundImage(certificateBackGround, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO,
                BackgroundSize.AUTO, false, false, true, false))));

        VBox commandCertificateButtonContainer = new VBox();
        commandCertificateButtonContainer.setAlignment(Pos.TOP_CENTER);
//        commandCertificateButtonContainer.setPrefSize(screenWidth/1.1, screenHeight);
        Button saveCertificateAsImage = new Button("Save as Image");
        saveCertificateAsImage.setFont(toolFont);
        Button closeCertificatePage = new Button("Close");
        closeCertificatePage.setFont(toolFont);
        commandCertificateButtonContainer.getChildren().add(saveCertificateAsImage);
        commandCertificateButtonContainer.getChildren().add(closeCertificatePage);
        VBox.setMargin(saveCertificateAsImage, new Insets(30, 5, 5, 25));
        VBox.setMargin(closeCertificatePage, new Insets(5, 5, 5, 25));
        saveCertificateAsImage.setOnAction(event -> {
            //                currentPath = new java.io.File(".").getCanonicalPath();
            captureAndSaveImageInContainer(certificateVbox, "abc");
            convertPNGImageToJPG("abc");
            try {
                File jpgImageFile = new File(getCurrentPath()+File.separator+"abc.jpg");
                File destinationImage = new File(getCurrentPath()+File.separator+"abcWithTitle.jpg");
                updateWindowsFields(jpgImageFile,destinationImage,encryptedText);
                String imageTitle = readTitleInMetadataOfJpgImage(destinationImage);
                System.out.println(EncryptDecryptBased64.decryptedBase64TextWithSecretKey(imageTitle,"123"));
            } catch (IOException | ImageWriteException | ImageReadException | InvalidAlgorithmParameterException |
                     NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                     InvalidKeySpecException | BadPaddingException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        });
        closeCertificatePage.setOnAction(event -> {
            ((Stage) (closeCertificatePage.getScene().getWindow())).close();
        });

        HBox certificateContainer = new HBox();
        certificateContainer.getChildren().add(certificateVbox);
        certificateContainer.getChildren().add(commandCertificateButtonContainer);

        VBox certificateWrapper = new VBox();
        certificateWrapper.getChildren().add(certificateContainer);
        return certificateWrapper;
    }
}
