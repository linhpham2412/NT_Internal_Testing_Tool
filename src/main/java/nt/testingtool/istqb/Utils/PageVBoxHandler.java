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

import static nt.testingtool.istqb.Utils.EncryptDecryptBased64.decryptedBase64TextWithSecretKey;
import static nt.testingtool.istqb.Utils.EncryptDecryptBased64.encryptTextBase64WithSecretKey;
import static nt.testingtool.istqb.Utils.ImageCaptureHandler.*;
import static nt.testingtool.istqb.Utils.ProjectConfiguration.*;
import static nt.testingtool.istqb.Utils.QuestionHandler.*;
import static nt.testingtool.istqb.Utils.TestingToolUtils.*;
import static org.apache.commons.io.FileUtils.cleanDirectory;

public class PageVBoxHandler {
    static Stage mainStage;
    static Pagination pagination;
    public static ComboBox selectTestingTypeComboBox;
    public static TextField testNameTextField;
    public static Button btn_StartTest;
    public static String testUserName = "";

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

    public static VBox setupHomePage() throws IOException, net.lingala.zip4j.exception.ZipException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        utilQuestionHandler = initQuestionHandler();
        isTestingEnd = false;
        File projectContentFile = new File(getCurrentPath() + File.separator + "ProjectContentEncrypted.txt");
        String[] readValue = readAndDeryptPasswordAndFileNameDataFromText(projectContentFile);
        setQuestionFileName(readValue[1]);
        setZipFilePassword(readValue[0]);
        //Read zip data file
        utilQuestionHandler.readQuestionZipFile(getQuestionFileName(), getZipFilePassword());
        utilQuestionHandler.readAndSaveAllISTQBTypeInData(getZipFilePassword());
        //Set up layout
        Pane blankPaneHeader = new Pane();
        double headerHeight = screenHeight / 8;
        blankPaneHeader.setPrefHeight(headerHeight);
        blankPaneHeader.setBackground(grayBackGround);
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
            changeStageAndScene(event, setupCreditPage(), "Credit Page");

//            try {
//                //Debug
////                testUserName = testNameTextField.getText();
//                //End debug
//                openNewStageAndScene(setupCertificatePage(), "Certificate Page");
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
        blankPaneFooter.setBackground(grayBackGround);
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
        infoVbox.setBackground(grayBackGround);
        VBox.setMargin(istqbDetailText, new Insets(5, 0, 0, 5));

        istqbInformationHBox.getChildren().add(infoVbox);
        //End information pane

        selectTestingTypeComboBox = new ComboBox();
        utilQuestionHandler.getListOfISTQBTypeReadFromData().stream()
                .map(e -> selectTestingTypeComboBox.getItems().add(e)).collect(Collectors.toList());
        selectTestingTypeComboBox.setStyle("-fx-font-size: 16");
        selectTestingTypeComboBox.setOnAction(event -> {
            setQuestionGroupName((String) selectTestingTypeComboBox.getValue());
            updateISTQBTypeInformation(getQuestionGroupName());
            istqbDetailText.setText(getQuestionGroupShortName() + "\nNumber of Question: "
                    + getNumberOfQuestionsPerQuestionBank() + "\n" +
                    "Passing Score (at least 65%): " + getPassingScore() + "\n" +
                    "Testing Time: " + getTestingMinutes() + " minutes\n" +
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
        if (!testUserName.equals("")) testNameTextField.setText(testUserName);
        btn_StartTest = new Button("Start Test");
        btn_StartTest.setFont(toolFont);
        btn_StartTest.setOnAction(event -> {
            if (selectTestingTypeComboBox.getValue() != null) {
                utilQuestionHandler.isFirstLoad = true;
                testUserName = testNameTextField.getText();
                if (testUserName.equals("")) {
                    AlertDisplay.displayMissingInformationAlert("Missing User Name"
                            , "Please input your name before you start the test");
                } else {
                    try {
                        //Debug
//                        setTestingMinutes(1);
                        //End debug
                        changeStageAndScene(event, setupLayoutPageExam(), "Examination Page of: "
                                + getQuestionGroupName());
                    } catch (IOException | ZipException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                AlertDisplay.displayMissingInformationAlert("Missing ISTQB Type"
                        , "Please select ISTQB Type to start the test");
            }
        });
        Button quitAppHomeButton = new Button("Quit");
        quitAppHomeButton.setFont(toolFont);
        quitAppHomeButton.setOnAction(event -> {
            try {
                cleanDirectory(utilQuestionHandler.imagesFolder);
                utilQuestionHandler.imagesFolder.delete();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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

    private static VBox setupCertificatePage() throws Exception {
        //Build code
//        Image certificateBackGround = new Image("nt/testingtool/istqb/imageAsset/NTInternalCertificate.png");
        //Debug code
        Image certificateBackGround = new Image("C:\\Users\\linhpham\\IdeaProjects\\demo\\src\\main\\resources\\nt\\testingtool\\istqb\\imageAsset\\NTInternalCertificate.png");
        Label testUserNameCertificate = new Label(testUserName);
        testUserNameCertificate.setStyle("-fx-font-size: 48; -fx-font-weight: bold;-fx-text-alignment: center;");
        testUserNameCertificate.setTextFill(darkBlueColor);
        testUserNameCertificate.setFont(new Font("Poppins Extrabold", 36));
        Label testTypeCertificate = new Label(getQuestionGroupShortName());
        testTypeCertificate.setStyle("-fx-font-size: 36; -fx-font-weight: bold;-fx-text-alignment: center;");
        testTypeCertificate.setTextFill(redNTColor);
        testTypeCertificate.setFont(new Font("Lato Bold", 36));
        if (testTypeCertificate.getText().length() > 44) {
            testTypeCertificate.setPrefWidth(screenWidth / 1.8);
        }
        testTypeCertificate.setWrapText(true);
        Label testDateCertificate = new Label("Test Date: " + getTodayDate());
        testDateCertificate.setStyle("-fx-font-size: 18; -fx-font-weight: bold;-fx-text-alignment: center;");
        testDateCertificate.setTextFill(darkBlueColor);
        testDateCertificate.setFont(new Font("Lato Bold", 18));
        String encryptedText = encryptTextBase64WithSecretKey("This is a certificate to " + testUserName + " | " +
                        "Passed " + getQuestionGroupShortName() + " | Pass Score " + calculateTestingResult()
                        + "/" + getNumberOfQuestionsPerQuestionBank() + " | Test Date: " + getTodayDate()
                , getEncryptDecryptKey());
        VBox certificateVbox = new VBox();
        certificateVbox.setAlignment(Pos.TOP_CENTER);
        certificateVbox.setPrefSize(screenWidth / 1.2, screenHeight);
        certificateVbox.getChildren().add(testTypeCertificate);
        certificateVbox.getChildren().add(testUserNameCertificate);
        certificateVbox.getChildren().add(testDateCertificate);
        VBox.setMargin(testTypeCertificate, new Insets(screenHeight / 3, 0, 0, 0));
        VBox.setMargin(testDateCertificate, new Insets(screenHeight / 6, 0, 0, 0));
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
            String imageNamePrefix = testUserName.replace(" ", "_") + "_"
                    + getQuestionGroupShortName().replace(" ", "_") + "_"
                    + getTodayDateTime();
            captureAndSaveImageInContainer(certificateVbox, imageNamePrefix + "_Processing");
            convertPNGImageToJPG(imageNamePrefix + "_Processing");
            try {
                File jpgImageFile = new File(getCurrentPath() + File.separator + imageNamePrefix + "_Processing.jpg");
                File pngImageFile = new File(getCurrentPath() + File.separator + imageNamePrefix + "_Processing.png");
                File destinationImage = new File(getCurrentPath() + File.separator + imageNamePrefix + ".jpg");
                updateImageJPGTitleMetadataFields(jpgImageFile, destinationImage, encryptedText);
                pngImageFile.delete();
                jpgImageFile.delete();
            } catch (IOException | ImageWriteException | ImageReadException e) {
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

    private static VBox setupCreditPage() {
        HBox creditHeader = new HBox();
        Label thanksLabel = new Label("Thank you to the effort of all members in the team!");
        thanksLabel.setStyle("-fx-font-size: 48; -fx-font-weight: bold;-fx-text-alignment: center;");
        thanksLabel.setTextFill(redNTColor);
        thanksLabel.setFont(new Font("Lato Bold", 48));
        Button creditPageReturnHome = new Button("Home");
        creditPageReturnHome.setFont(toolFont);
        creditPageReturnHome.setOnAction(event -> {
            initSelectedAnwser();
            utilQuestionHandler.isFirstLoad = true;
            try {
                changeStageAndScene(event, setupHomePage(), "Home Page");
            } catch (IOException | ZipException | InvalidAlgorithmParameterException | NoSuchPaddingException |
                     IllegalBlockSizeException | NoSuchAlgorithmException | InvalidKeySpecException |
                     BadPaddingException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        });

        creditHeader.setAlignment(Pos.CENTER_RIGHT);
        creditHeader.setPrefWidth(screenWidth);
        creditHeader.setPrefHeight(screenHeight / 8);
        creditHeader.setBackground(grayBackGround);
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
        creatorName.setTextFill(darkBlueColor);
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
        dataCollectorTitle.setTextFill(darkBlueColor);
        dataCollectorTitle.setFont(new Font("Poppins Extrabold", 36));
        creditDataCollector.getChildren().add(dataCollectorTitle);

        VBox creditPageVbox = new VBox();
        creditPageVbox.setAlignment(Pos.CENTER_RIGHT);
        creditPageVbox.getChildren().add(creditHeader);
        creditPageVbox.getChildren().add(creditCreator);
        creditPageVbox.getChildren().add(creditDataCollector);
        return creditPageVbox;
    }

    public static VBox setupLayoutPageExam() throws IOException, ZipException {
        //Read and assign all questions data to questionHandler
        utilQuestionHandler.readQuestionZipFile(getQuestionFileName(), getZipFilePassword());
        utilQuestionHandler.mapDataInQuestionFileToDataModelByGroupName();
        initCorrectAnswer();
        initSelectedAnwser();
        utilQuestionHandler.randomChooseQuestionsInBankThenShuffleAndSaveToTestingQuestions();

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
            if (seconds[0] < 0) {
                getTimerTimeLine().stop();
                isTestingEnd = true;
                disableAllAnswersInHBoxContainer();
                assignAnswersDataFromClassToCheckBoxOrRadioButton(getCurrentPageIndex());
                pagination.setCurrentPageIndex(getNumberOfQuestionsPerQuestionBank());
                AlertDisplay.displayTimingAlert("Time's up!", "All questions are unable to change!");
            }
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
        pagination = new Pagination(getNumberOfQuestionsPerQuestionBank());
        pagination.setMaxPageIndicatorCount(getNumberOfQuestionsPerQuestionBank());
        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        pagination.setPageFactory(TestingToolUtils::getQuestionPages);
        pagination.setMaxPageIndicatorCount(maxQuestionPerPagination);
        pagination.setPrefWidth(getObjectWidthInScrollPane() + 30);
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

    public static VBox setupSummaryPage() {
        Pane blankPaneHeader = new Pane();
        blankPaneHeader.setPrefHeight(screenHeight / 4);
        blankPaneHeader.setBackground(grayBackGround);
        Pane blankPaneFooter = new Pane();
        blankPaneFooter.setPrefHeight(screenHeight / 2);
        blankPaneFooter.setBackground(grayBackGround);
        Label resultTitle = new Label("Your result is:");
        resultTitle.setStyle("-fx-font-size: 48;");
        int correctAnswer = calculateTestingResult();
//        String passFailString = determinePassOrFail(correctAnswer);
        String passFailString = "Passed";
        Label resultPassFail = new Label(passFailString);
        if (passFailString.equals("Passed")) {
            resultPassFail.setStyle("-fx-text-fill: #5E8C5D; -fx-font-size: 72; -fx-font-weight: bold;");
        } else {
            resultPassFail.setStyle("-fx-text-fill: #F0292A; -fx-font-size: 72; -fx-font-weight: bold;");
        }
        Label resultDashLine = new Label("------------------------------------------------------------------------------------------------------");
        resultDashLine.setFont(toolFont);
        Label resultActualScore = new Label("Correct " + correctAnswer + "/" + getNumberOfQuestionsPerQuestionBank());
        resultActualScore.setStyle("-fx-font-size: 48;");

        //add command button
        Button returnToHomeButton = new Button("Return Home");
        returnToHomeButton.setFont(toolFont);
        returnToHomeButton.setOnAction(event -> {
            initSelectedAnwser();
            utilQuestionHandler.isFirstLoad = true;
            try {
                changeStageAndScene(event, setupHomePage(), "Home Page");
            } catch (IOException | ZipException | InvalidAlgorithmParameterException | NoSuchPaddingException |
                     IllegalBlockSizeException | NoSuchAlgorithmException | InvalidKeySpecException |
                     BadPaddingException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        });
        Button startNewTestButton = new Button("Start New Test");
        startNewTestButton.setOnAction(event -> {
            initSelectedAnwser();
            try {
                isTestingEnd = false;
                changeStageAndScene(event, setupLayoutPageExam()
                        , "Examination Page of: " + selectTestingTypeComboBox.getValue());
            } catch (IOException | ZipException e) {
                throw new RuntimeException(e);
            }
        });
        startNewTestButton.setFont(toolFont);
        Button quitAppButton = new Button("Quit");
        quitAppButton.setFont(toolFont);
        quitAppButton.setOnAction(event -> {
            try {
                cleanDirectory(utilQuestionHandler.imagesFolder);
                utilQuestionHandler.imagesFolder.delete();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ((Stage) (quitAppButton.getScene().getWindow())).close();
        });
        HBox summaryCommandContainer = new HBox();
        summaryCommandContainer.setPrefWidth(screenWidth);
        summaryCommandContainer.setAlignment(Pos.CENTER);
        summaryCommandContainer.getChildren().add(returnToHomeButton);
        summaryCommandContainer.getChildren().add(startNewTestButton);
        summaryCommandContainer.getChildren().add(quitAppButton);
        HBox.setMargin(returnToHomeButton, new Insets(10, 10, 10, 10));
        HBox.setMargin(startNewTestButton, new Insets(10, 10, 10, 10));
        HBox.setMargin(quitAppButton, new Insets(10, 10, 10, 30));

        //Set up get certificate button
        Button getCertificateButton = new Button("Get Your CERTIFICATION Here!");
        getCertificateButton.setFont(toolFont);
        getCertificateButton.setVisible(false);
        if (passFailString.equals("Passed")) getCertificateButton.setVisible(true);
        getCertificateButton.setOnAction(event -> {
            try {
                openNewStageAndScene(setupCertificatePage(), "Certificate Page");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        HBox summaryCertificateCommandContainer = new HBox();
        summaryCertificateCommandContainer.setPrefWidth(screenWidth);
        summaryCertificateCommandContainer.setAlignment(Pos.CENTER);
        summaryCertificateCommandContainer.getChildren().add(getCertificateButton);

        //Set up Result VBox
        VBox resultVBox = new VBox();
        resultVBox.setPrefWidth(screenWidth);
        resultVBox.setAlignment(Pos.CENTER);
        resultVBox.getChildren().add(blankPaneHeader);
        resultVBox.getChildren().add(resultTitle);
        resultVBox.getChildren().add(resultPassFail);
        resultVBox.getChildren().add(resultDashLine);
        resultVBox.getChildren().add(resultActualScore);
        resultVBox.getChildren().add(summaryCommandContainer);
        resultVBox.getChildren().add(summaryCertificateCommandContainer);
        resultVBox.getChildren().add(blankPaneFooter);

        return resultVBox;
    }

    public static VBox setupManagePage() throws IOException {
        //Set up layout
        HBox layoutHBox = new HBox();
        VBox imagePaneContainer = new VBox();
        imagePaneContainer.setAlignment(Pos.CENTER);
        imagePaneContainer.setPrefWidth(screenWidth / 2);
        imagePaneContainer.setPrefHeight(screenHeight);
        imagePaneContainer.setBackground(grayBackGround);
        TextArea textToProcessArea = new TextArea();
        textToProcessArea.setWrapText(true);
        textToProcessArea.setPrefSize(screenWidth / 2, screenHeight / 2.5);
        textToProcessArea.setFont(toolFont);
        TextArea textConvertedArea = new TextArea();
        textConvertedArea.setWrapText(true);
        textConvertedArea.setPrefSize(screenWidth / 2, screenHeight / 2.5);
        textConvertedArea.setFont(toolFont);
        Button imageSelector = new Button("Open Image");
        imageSelector.setFont(toolFont);
        imageSelector.setOnAction(event -> {
            try {
                fileChooser.setInitialDirectory(new File(getCurrentPath()));
            } catch (IOException ignored) {
            }
            loadedImageFile = fileChooser.showOpenDialog(null);
            ImageView imageToCheck = new ImageView(loadedImageFile.toURI().toString());
            imageToCheck.setFitWidth(screenWidth / 2);
            imageToCheck.setFitHeight(screenHeight / 1.5);
            if (imagePaneContainer.getChildren().size() > 1) {
                imagePaneContainer.getChildren().remove(imagePaneContainer.getChildren().size() - 1);
            }
            imagePaneContainer.getChildren().add(imageToCheck);

            try {
                textToProcessArea.setText(readTitleInMetadataOfJpgImage(loadedImageFile));
                textConvertedArea.setText(decryptedBase64TextWithSecretKey(textToProcessArea.getText(), getEncryptDecryptKey()));
            } catch (IOException | ImageReadException | InvalidAlgorithmParameterException | NoSuchPaddingException |
                     IllegalBlockSizeException | NoSuchAlgorithmException | InvalidKeySpecException |
                     BadPaddingException |
                     InvalidKeyException ignored) {
            }
        });
        imagePaneContainer.getChildren().add(imageSelector);
        VBox.setMargin(imageSelector, new Insets(10, 10, 10, 10));

        VBox textPaneContainer = new VBox();
        Label textToProcess = new Label("Text To Process:");
        textToProcess.setFont(toolFont);
        HBox textConvertCommandContainer = new HBox();
        Button encryptButton = new Button("Encrypt");
        encryptButton.setFont(toolFont);
        Button decryptButton = new Button("Decrypt");
        decryptButton.setFont(toolFont);
        Button clearButton = new Button("Clear");
        clearButton.setFont(toolFont);
        Button generatePasswordButton = new Button("Generate Password File");
        generatePasswordButton.setFont(toolFont);
        textConvertCommandContainer.setAlignment(Pos.CENTER);
        textConvertCommandContainer.getChildren().add(generatePasswordButton);
        textConvertCommandContainer.getChildren().add(encryptButton);
        textConvertCommandContainer.getChildren().add(decryptButton);
        textConvertCommandContainer.getChildren().add(clearButton);
        HBox.setMargin(encryptButton, new Insets(5, 5, 5, 5));
        HBox.setMargin(decryptButton, new Insets(5, 5, 5, 5));
        HBox.setMargin(clearButton, new Insets(5, 5, 5, 5));
        HBox.setMargin(generatePasswordButton, new Insets(5, 5, 5, 5));
        Label textConverted = new Label("Result Text:");
        textConverted.setFont(toolFont);
        textPaneContainer.getChildren().add(textToProcess);
        textPaneContainer.getChildren().add(textToProcessArea);
        textPaneContainer.getChildren().add(textConvertCommandContainer);
        textPaneContainer.getChildren().add(textConverted);
        textPaneContainer.getChildren().add(textConvertedArea);
        VBox.setMargin(textToProcess, new Insets(5, 5, 5, 5));
        VBox.setMargin(textConverted, new Insets(5, 5, 5, 5));
        VBox.setMargin(textConvertedArea, new Insets(5, 5, 5, 5));
        VBox.setMargin(textToProcessArea, new Insets(5, 5, 5, 5));
        encryptButton.setOnAction(event -> {
            try {
                textConvertedArea.setText(encryptTextBase64WithSecretKey(textToProcessArea.getText(), getEncryptDecryptKey()));
            } catch (Exception ignored) {
            }
        });
        decryptButton.setOnAction(event -> {
            try {
                textConvertedArea.setText(decryptedBase64TextWithSecretKey(textToProcessArea.getText(), getEncryptDecryptKey()));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidAlgorithmParameterException |
                     NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException |
                     InvalidKeyException ignored) {
            }
        });
        clearButton.setOnAction(event -> {
            textToProcessArea.setText("");
            textConvertedArea.setText("");
        });
        generatePasswordButton.setOnAction(event -> {
            String projectContent = "Project content=| " + textToProcessArea.getText() + "|";
            try {
                textConvertedArea.setText(encryptTextBase64WithSecretKey(projectContent, getEncryptDecryptKey()));
                File passwordTextFile = new File(getCurrentPath() + File.separator + "ProjectContentEncrypted.txt");
                writeDataToTextFile(passwordTextFile, textConvertedArea.getText());
            } catch (Exception ignored) {
            }
        });

        layoutHBox.getChildren().add(imagePaneContainer);
        layoutHBox.getChildren().add(textPaneContainer);

        //Set up Result VBox
        VBox resultVBox = new VBox();
        resultVBox.setPrefWidth(screenWidth);
        resultVBox.setAlignment(Pos.CENTER);
        resultVBox.getChildren().add(layoutHBox);

        return resultVBox;
    }
}
