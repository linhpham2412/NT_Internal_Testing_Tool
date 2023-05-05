package nt.testingtool.istqb.Utils;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
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
import static nt.testingtool.istqb.Utils.TestingToolUtils.*;

public class PageVBoxHandler {
    static Stage mainStage;
    static Pagination pagination;
    public static ComboBox selectTestingTypeComboBox;
    public static TextField testNameTextField;
    public static Button btn_StartTest;
    public static String testUserName;

    public static void changeStageAndScene(ActionEvent event, VBox layoutVBoxContainer, String sceneTitle) {
        mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Pane layout = new Pane(layoutVBoxContainer);
        Scene scene = new Scene(layout, screenWidth, screenHeight);
        mainStage.setResizable(false);
        mainStage.setTitle(sceneTitle);
        mainStage.setScene(scene);
    }

    public static void openNewStageAndScene(VBox layoutVBoxContainer, String sceneTitle) {
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
        //Build
//        ImageView infoImage = new ImageView(new Image("nt/testingtool/istqb/imageAsset/infomationIcon.png"));
//        nashTechLogo.setImage(new Image("nt/istqbtt/nt_istqbtt/NashTechLogo.png"));
        //Debug
        ImageView infoImage = new ImageView(new Image("C:\\Users\\linhpham\\IdeaProjects\\demo\\src\\main\\resources\\nt\\testingtool\\istqb\\imageAsset\\infomationIcon.png"));
        nashTechLogo.setImage(new Image("C:\\Users\\linhpham\\IdeaProjects\\demo\\src\\main\\resources\\nt\\testingtool\\istqb\\imageAsset\\NashTechLogo.png"));
        infoImage.setFitHeight(headerHeight / 2);
        infoImage.setFitWidth(headerHeight / 2);
        informationButton.setGraphic(infoImage);
        informationButton.setOnAction(event -> {
            changeStageAndScene(event, setupCreditPage(toolFont,questionHandler), "Credit Page");

//            try {
//                openNewStageAndScene(setupCertificatePage(toolFont), "Certificate Page");
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }

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

                try {
                    changeStageAndScene(event, setupLayoutPageExam(toolFont,questionHandler), "Examination Page of: " + selectTestingTypeComboBox.getValue());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ZipException e) {
                    throw new RuntimeException(e);
                }

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
        //Build code
//        Image certificateBackGround = new Image("nt/testingtool/istqb/imageAsset/NTInternalCertificate.png");
        //Debug code
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
        String encryptedText = encryptTextBase64WithSecretKey("This is a certificate to ANH TRAN THI HUYNH | " +
                "Passed ISTQB - Certified Tester Foundation Level | Pass Score 30/40 | Test Date: April 25, 2023", "123");
        VBox certificateVbox = new VBox();
        certificateVbox.setAlignment(Pos.TOP_CENTER);
        certificateVbox.setPrefSize(screenWidth / 1.2, screenHeight);
        certificateVbox.getChildren().add(testTypeCertificate);
        certificateVbox.getChildren().add(testUserNameCertificate);
        VBox.setMargin(testTypeCertificate, new Insets(screenHeight / 3, 0, 0, 0));
        if (testTypeCertificate.getText().length() < 44) {
            VBox.setMargin(testUserNameCertificate, new Insets(screenHeight / 6.5, 0, 0, 0));
        } else {
            VBox.setMargin(testUserNameCertificate, new Insets(screenHeight / 11, 0, 0, 0));
        }
        certificateVbox.setBackground(new Background(new BackgroundImage(certificateBackGround, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO,
                BackgroundSize.AUTO, false, false, true, false))));

        VBox commandCertificateButtonContainer = new VBox();
        commandCertificateButtonContainer.setAlignment(Pos.TOP_CENTER);
        Button saveCertificateAsImage = new Button("Save as Image");
        saveCertificateAsImage.setFont(toolFont);
        Button closeCertificatePage = new Button("Close");
        closeCertificatePage.setFont(toolFont);
        commandCertificateButtonContainer.getChildren().add(saveCertificateAsImage);
        commandCertificateButtonContainer.getChildren().add(closeCertificatePage);
        VBox.setMargin(saveCertificateAsImage, new Insets(30, 5, 5, 25));
        VBox.setMargin(closeCertificatePage, new Insets(5, 5, 5, 25));
        saveCertificateAsImage.setOnAction(event -> {
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

    private static VBox setupCreditPage(Font toolFont, QuestionHandler questionHandler) {
        HBox creditHeader = new HBox();
        Label thanksLabel = new Label("Thank you to the effort of all members in the team!");
        thanksLabel.setStyle("-fx-font-size: 48; -fx-font-weight: bold;-fx-text-alignment: center;");
        thanksLabel.setTextFill(Color.valueOf("#FA6070"));
        thanksLabel.setFont(new Font("Lato Bold", 48));
        Button creditPageReturnHome = new Button("Home");
        creditPageReturnHome.setOnAction(event -> {
            questionHandler.initSelectedAnwser();
            questionHandler.isFirstLoad = true;
            try {
                changeStageAndScene(event, setupHomePage(toolFont,questionHandler), "Home Page");
            } catch (IOException | net.lingala.zip4j.exception.ZipException e) {
                throw new RuntimeException(e);
            }
        });

        creditHeader.setAlignment(Pos.CENTER_RIGHT);
        creditHeader.setPrefWidth(screenWidth);
        creditHeader.setPrefHeight(screenHeight / 8);
        creditHeader.setBackground(new Background(new BackgroundFill(Paint.valueOf("#CACACA"), CornerRadii.EMPTY, Insets.EMPTY)));
        creditHeader.getChildren().add(thanksLabel);
        creditHeader.getChildren().add(creditPageReturnHome);
        HBox.setMargin(creditPageReturnHome, new Insets(0, 20, 0, 100));

        HBox creditCreator = new HBox();
        creditCreator.setAlignment(Pos.CENTER);
        creditCreator.setPrefWidth(screenWidth);
        creditCreator.setPrefHeight(screenHeight / 8);
        Label creatorName = new Label("Creator: LINH PHAM\n" +
                "linh.pham@nashtechglobal.com");
        creatorName.setStyle("-fx-font-size: 36; -fx-font-weight: bold;-fx-text-alignment: center;");
        creatorName.setTextFill(Color.valueOf("#284977"));
        creatorName.setFont(new Font("Poppins Extrabold", 36));
        creditCreator.getChildren().add(creatorName);

        HBox creditDataCollector = new HBox();
        creditDataCollector.setAlignment(Pos.CENTER);
        creditDataCollector.setPrefWidth(screenWidth);
        Label dataCollectorTitle = new Label("\nData Collector:\n" +
                "ANH TRAN THI HUYNH\nAnh.TranThiHuynh@nashtechglobal.com\n\n" +
                "ANH NGUYEN TA TUYET\nAnh.NguyenTaTuyet@nashtechglobal.com\n\n" +
                "TRAM NGUYEN PHUONG NGUYET\nTram.NguyenPhuongNguyet@nashtechglobal.com");
        dataCollectorTitle.setStyle("-fx-font-size: 36; -fx-font-weight: bold;-fx-text-alignment: center;");
        dataCollectorTitle.setTextFill(Color.valueOf("#284977"));
        dataCollectorTitle.setFont(new Font("Poppins Extrabold", 36));
        creditDataCollector.getChildren().add(dataCollectorTitle);

        VBox creditPageVbox = new VBox();
        creditPageVbox.setAlignment(Pos.CENTER_RIGHT);
        creditPageVbox.getChildren().add(creditHeader);
        creditPageVbox.getChildren().add(creditCreator);
        creditPageVbox.getChildren().add(creditDataCollector);
        return creditPageVbox;
    }

    public static VBox setupLayoutPageExam(Font toolFont, QuestionHandler questionHandler) throws IOException, ZipException {
        //Read and assign all questions data to questionHandler
        questionHandler.readQuestionZipFile(getQuestionFileName(), getZipFilePassword());
        questionHandler.mapDataInQuestionFileToDataModelByGroupName();
        questionHandler.initCorrectAnswer();
        questionHandler.initSelectedAnwser();
        questionHandler.randomChooseQuestionsInBankThenShuffleAndSaveToTestingQuestions();

        //Set up Timer header
        Label timerValue = new Label();
        timerValue.setFont(toolFont);
        ProgressBar timerProgressBar = new ProgressBar();
        timerProgressBar.setPrefWidth(screenWidth * 0.87);
        timerProgressBar.setRotate(180);
        timerProgressBar.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        final int[] seconds = {getTestingMinutes() * 60};
        int totalSeconds = seconds[0];
        setTimerTimeLine(new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            timerValue.setText("Time left " + calculateTimeLeft(seconds));
            seconds[0]--;
            timerProgressBar.setProgress(1 - (double) seconds[0] / totalSeconds);
            if (seconds[0] < 0) getTimerTimeLine().stop();
        })));
        getTimerTimeLine().setCycleCount(Animation.INDEFINITE);
        getTimerTimeLine().play();
        //End of timer
        HBox timerArea = new HBox();
        HBox.setMargin(timerValue, new Insets(5, 5, 5, 5));
        HBox.setMargin(timerProgressBar, new Insets(15, 15, 15, 15));
        timerArea.getChildren().add(timerValue);
        timerArea.getChildren().add(timerProgressBar);

        //Set up Pagination question pages
        TestingToolUtils.getQuestionHandler(questionHandler);
        pagination = new Pagination(getNumberOfQuestionsPerQuestionBank());
        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        pagination.setPageFactory(TestingToolUtils::getQuestionPages);
        pagination.setMaxPageIndicatorCount(40);
        pagination.setScaleX(1.7);
        pagination.setScaleY(1.7);
        HBox questionPane = new HBox(pagination);
        questionPane.setAlignment(Pos.CENTER);
        HBox.setMargin(pagination, new Insets(160, 0, 0, 0));

        //Set up Exam VBox
        VBox examVBox = new VBox();
        examVBox.getChildren().add(timerArea);
        examVBox.getChildren().add(questionPane);

        return examVBox;
    }
}
