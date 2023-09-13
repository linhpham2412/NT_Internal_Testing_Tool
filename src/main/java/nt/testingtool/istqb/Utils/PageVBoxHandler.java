package nt.testingtool.istqb.Utils;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.lingala.zip4j.exception.ZipException;
import nt.testingtool.istqb.datamodel.QuestionDataModel;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.stream.Collectors;

import static nt.testingtool.istqb.Utils.EncryptDecryptBased64.decryptedBase64TextWithSecretKey;
import static nt.testingtool.istqb.Utils.EncryptDecryptBased64.encryptTextBase64WithSecretKey;
import static nt.testingtool.istqb.Utils.ImageCaptureHandler.*;
import static nt.testingtool.istqb.Utils.ProjectConfiguration.*;
import static nt.testingtool.istqb.Utils.QuestionDesigner.*;
import static nt.testingtool.istqb.Utils.QuestionHandler.*;
import static nt.testingtool.istqb.Utils.TestingToolUtils.*;
import static org.apache.commons.io.FileUtils.cleanDirectory;

public class PageVBoxHandler {
    public static ComboBox selectTestingTypeComboBox;
    public static TextField testNameTextField;
    public static Button btn_StartTest;
    public static String testUserName = "";
    static Stage mainStage;
    static Pagination pagination;
    static int questionIndex = 0;
    static int maxQuestionIndex = 0;
    static boolean isQuestionDesign = false;
    static boolean isQuestionTempCheck = false;
    static Stage newStage = new Stage();
    static Stage popUpStage = new Stage();
    static Stage popUpTablePreviewStage = new Stage();
    static Button previewQuestionButton = new Button();
    static File questionBank = new File("");
    static File imagesFolder = new File("");
    static GridPane tableGridPane = new GridPane();
    static Button addRow = new Button("Add Row");
    static Button addCol = new Button("Add Col");
    static ScrollPane tableScrollPane = new ScrollPane();
    static ScrollPane tablePreviewScrollPane = new ScrollPane();
    static Button previewTable = new Button("Preview Table");
    static TextField tableRowTF = new TextField();
    static TextField tableColTF = new TextField();


    public static void changeStageAndScene(ActionEvent event, VBox layoutVBoxContainer, String sceneTitle) {
        mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Pane layout = new Pane(layoutVBoxContainer);
        Scene scene = new Scene(layout, screenWidth, screenHeight);
        mainStage.setResizable(false);
        mainStage.setTitle(sceneTitle);
        mainStage.getIcons().add(applicationIconLocation);
        mainStage.setScene(scene);
    }

    public static void openNewStageAndScene(VBox layoutVBoxContainer, String sceneTitle) {
        Pane layout = new Pane(layoutVBoxContainer);
        Scene scene = new Scene(layout, screenWidth, screenHeight);
        newStage.setResizable(false);
        newStage.setTitle(sceneTitle);
        newStage.setScene(scene);
        newStage.getIcons().add(applicationIconLocation);
        newStage.show();
    }

    public static void openNewStageAndSceneWithDefinedScreenSize(Stage openStage, VBox layoutVBoxContainer, String sceneTitle, double screenWidth, double screenHeight) {
        Pane layout = new Pane(layoutVBoxContainer);
        Scene scene = new Scene(layout, screenWidth, screenHeight);
        Stage workingStage = openStage;
        workingStage.setResizable(false);
        workingStage.setTitle(sceneTitle);
        workingStage.setScene(scene);
        workingStage.getIcons().add(applicationIconLocation);
        workingStage.show();
    }

    public static VBox setupHomePage() throws IOException, net.lingala.zip4j.exception.ZipException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        utilQuestionHandler = initQuestionHandler();
        isTestingEnd = false;
        File projectContentFile = new File(getCurrentPath() + File.separator + "ProjectContentEncrypted.txt");
        String[] readValue = readAndDecryptPasswordAndFileNameDataFromText(projectContentFile);
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
        Image imgInfoIcon = new Image(PageVBoxHandler.class.getResource(
                "/nt/testingtool/istqb/imageAsset/infomationIcon.png").toString());
        ImageView infoImage = new ImageView(imgInfoIcon);
        Image imgNTLogo = new Image(PageVBoxHandler.class.getResource(
                "/nt/testingtool/istqb/imageAsset/NashTechLogo.png").toString());
        nashTechLogo.setImage(imgNTLogo);
        infoImage.setFitHeight(headerHeight / 2);
        infoImage.setFitWidth(headerHeight / 2);
        informationButton.setGraphic(infoImage);
        informationButton.setOnAction(event -> {
            changeStageAndScene(event, setupCreditPage(), "Credit Page");
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
        selectTestingTypeComboBox.setStyle(fontStyle16);
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
                        isReviewAnswers = false;
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
        Image certificateBackGround = new Image(PageVBoxHandler.class.getResource(
                "/nt/testingtool/istqb/imageAsset/NTInternalCertificate.png").toString());
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
                "Linh.Pham@nashtechglobal.com");
        creatorName.setStyle("-fx-font-size: 34; -fx-font-weight: bold;-fx-text-alignment: center;");
        creatorName.setTextFill(darkBlueColor);
        creatorName.setFont(new Font("Poppins Extrabold", 34));
        ImageView creatorImgView = new ImageView();
        Image imgCreator = new Image(PageVBoxHandler.class.getResource(
                "/nt/testingtool/istqb/imageAsset/LinhPham.jpeg").toString());
        addImageIntoColorRoundBorder(creatorImgView, imgCreator, Color.BLUE);
        scaleDownImgWithPercentage(creatorImgView, 20);
        creatorImgView.setRotationAxis(new Point3D(0, 0, 1));
        creatorImgView.setRotate(10);
        creditCreator.getChildren().add(creatorImgView);
        creditCreator.getChildren().add(creatorName);

        HBox creditData1 = new HBox();
        creditData1.setAlignment(Pos.CENTER);
        creditData1.setPrefWidth(screenWidth);
        Label data1Name = new Label("Data Collectors:\nANH TRAN THI HUYNH\n" +
                "Anh.TranThiHuynh@nashtechglobal.com");
        data1Name.setStyle("-fx-font-size: 30; -fx-font-weight: bold;-fx-text-alignment: center;");
        data1Name.setTextFill(darkBlueColor);
        data1Name.setFont(new Font("Poppins Extrabold", 30));
        ImageView data1ImgView = new ImageView();
        Image imgData1 = new Image(PageVBoxHandler.class.getResource(
                "/nt/testingtool/istqb/imageAsset/AnhTranThiHuynh.png").toString());
        addImageIntoColorRoundBorder(data1ImgView, imgData1, Color.BLUEVIOLET);
        scaleDownImgWithPercentage(data1ImgView, 6);
        data1ImgView.setRotationAxis(new Point3D(0, 0, 1));
        data1ImgView.setRotate(-10);
        creditData1.getChildren().add(data1Name);
        creditData1.getChildren().add(data1ImgView);

        HBox creditData2 = new HBox();
        creditData2.setAlignment(Pos.CENTER);
        creditData2.setPrefWidth(screenWidth);
        Label data2Name = new Label("ANH NGUYEN TA TUYET\n" +
                "Anh.NguyenTaTuyet@nashtechglobal.com");
        data2Name.setStyle("-fx-font-size: 30; -fx-font-weight: bold;-fx-text-alignment: center;");
        data2Name.setTextFill(darkBlueColor);
        data2Name.setFont(new Font("Poppins Extrabold", 30));
        ImageView data2ImgView = new ImageView();
        Image imgData2 = new Image(PageVBoxHandler.class.getResource(
                "/nt/testingtool/istqb/imageAsset/AnhNguyenTaTuyet.png").toString());
        addImageIntoColorRoundBorder(data2ImgView, imgData2, Color.BLUEVIOLET);
        scaleDownImgWithPercentage(data2ImgView, 14);
        data2ImgView.setRotationAxis(new Point3D(0, 0, 1));
        data2ImgView.setRotate(10);
        creditData2.getChildren().add(data2ImgView);
        creditData2.getChildren().add(data2Name);

        HBox creditData3 = new HBox();
        creditData3.setAlignment(Pos.CENTER);
        creditData3.setPrefWidth(screenWidth);
        Label data3Name = new Label("TRAM NGUYEN PHUONG NGUYET\n" +
                "Tram.NguyenPhuongNguyet@nashtechglobal.com");
        data3Name.setStyle("-fx-font-size: 30; -fx-font-weight: bold;-fx-text-alignment: center;");
        data3Name.setTextFill(darkBlueColor);
        data3Name.setFont(new Font("Poppins Extrabold", 30));
        ImageView data3ImgView = new ImageView();
        Image imgData3 = new Image(PageVBoxHandler.class.getResource(
                "/nt/testingtool/istqb/imageAsset/TramNguyenPhuongNguyet.png").toString());
        addImageIntoColorRoundBorder(data3ImgView, imgData3, Color.BLUEVIOLET);
        scaleDownImgWithPercentage(data3ImgView, 13);
        data3ImgView.setRotationAxis(new Point3D(0, 0, 1));
        data3ImgView.setRotate(-10);
        creditData3.getChildren().add(data3Name);
        creditData3.getChildren().add(data3ImgView);

        HBox technicalSupportCredit = new HBox();
        technicalSupportCredit.setAlignment(Pos.CENTER);
        technicalSupportCredit.setPrefWidth(screenWidth);
        technicalSupportCredit.setPrefHeight(screenHeight / 8);
        Label techSupportName = new Label("Technical Support:\nBANG VAN CONG\n" +
                "Bang.VanCong@nashtechglobal.com");
        techSupportName.setStyle("-fx-font-size: 30; -fx-font-weight: bold;-fx-text-alignment: center;");
        techSupportName.setTextFill(darkBlueColor);
        techSupportName.setFont(new Font("Poppins Extrabold", 30));
        ImageView techSupportImgView = new ImageView();
        Image imgSupporter = new Image(PageVBoxHandler.class.getResource(
                "/nt/testingtool/istqb/imageAsset/BangVanCong.png").toString());
        addImageIntoColorRoundBorder(techSupportImgView, imgSupporter, Color.BLUE);
        scaleDownImgWithPercentage(techSupportImgView, 15);
        techSupportImgView.setRotationAxis(new Point3D(0, 0, 1));
        techSupportImgView.setRotate(10);
        technicalSupportCredit.getChildren().add(techSupportImgView);
        technicalSupportCredit.getChildren().add(techSupportName);

        VBox creditPageVbox = new VBox();
        creditPageVbox.setAlignment(Pos.CENTER_RIGHT);
        creditPageVbox.getChildren().add(creditHeader);
        creditPageVbox.getChildren().add(creditCreator);
        creditPageVbox.getChildren().add(creditData1);
        creditPageVbox.getChildren().add(creditData2);
        creditPageVbox.getChildren().add(creditData3);
        creditPageVbox.getChildren().add(technicalSupportCredit);
        return creditPageVbox;
    }

    public static VBox setupLayoutPageExam() throws IOException, ZipException {
        if (!isReviewAnswers) {
            //Read and assign all questions data to questionHandler
            utilQuestionHandler.readQuestionZipFile(getQuestionFileName(), getZipFilePassword());
            utilQuestionHandler.mapDataInQuestionFileToDataModelByGroupName();
            initCorrectAnswer();
            initSelectedAnwser();
            utilQuestionHandler.randomChooseQuestionsInBankThenShuffleAndSaveToTestingQuestions();
        }

        //Set up Timer header
        Label timerValue = new Label();
        Button previewQuestion = new Button("_____Preview___");
        previewQuestion.setFont(toolFont);
        timerValue.setFont(toolFont);
        ProgressBar timerProgressBar = new ProgressBar();
        timerProgressBar.setPrefWidth(screenWidth * 0.87);
        timerProgressBar.setRotate(180);
        timerProgressBar.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        final int[] seconds = {getTestingMinutes() * 60};
        int totalSeconds = seconds[0];
        if (!isReviewAnswers) {
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
        } else {
            timerValue.setText("Time left 00:00");
            if (isQuestionDesign) {
                timerValue.setVisible(false);
                previewQuestion.setVisible(true);
            } else {
                timerValue.setVisible(true);
                previewQuestion.setVisible(false);
            }
            timerProgressBar.setProgress(1);
            isTestingEnd = true;
        }
        //End of timer
        HBox timerArea = new HBox();
        HBox.setMargin(timerValue, new Insets(5, 5, 5, 5));
        HBox.setMargin(timerProgressBar, new Insets(15, 15, 15, 15));
        if (isQuestionDesign) {
            timerArea.getChildren().add(previewQuestion);
        } else {
            timerArea.getChildren().add(timerValue);
        }
        timerArea.getChildren().add(timerProgressBar);

        previewQuestion.setOnAction(event -> {
            try {
                isReviewAnswers = true;
                isQuestionDesign = true;
                changeStageAndScene(event, setupLayoutPageExam(), "Exam Page Preview of: " + QuestionDesigner.getSelectedGroupNameValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        previewQuestionButton = previewQuestion;

        //Set up Pagination question pages
        if (isQuestionDesign) {
            pagination = new Pagination(1);
            setCurrentQuestionPreviewIndex(questionIndex);
            pagination.setMaxPageIndicatorCount(1);
        } else {
            pagination = new Pagination(getNumberOfQuestionsPerQuestionBank());
            pagination.setMaxPageIndicatorCount(getNumberOfQuestionsPerQuestionBank());
        }
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
        String passFailString = determinePassOrFail(correctAnswer);
        //Debug
//        String passFailString = "Passed";
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
                isReviewAnswers = false;
                changeStageAndScene(event, setupLayoutPageExam()
                        , "Examination Page of: " + selectTestingTypeComboBox.getValue());
            } catch (IOException | ZipException e) {
                throw new RuntimeException(e);
            }
        });
        startNewTestButton.setFont(toolFont);
        Button reviewAnswerButton = new Button("Review Answers");
        reviewAnswerButton.setOnAction(event -> {
            try {
                isTestingEnd = false;
                isQuestionDesign = false;
                isReviewAnswers = true;
                changeStageAndScene(event, setupLayoutPageExam()
                        , "Answers Review Page of: " + selectTestingTypeComboBox.getValue());
            } catch (IOException | ZipException e) {
                throw new RuntimeException(e);
            }
        });
        reviewAnswerButton.setStyle(cssGreenColorBGValue);
        reviewAnswerButton.setFont(toolFont);
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
        summaryCommandContainer.getChildren().add(reviewAnswerButton);
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

    public static VBox setupQuestionDesignerPage() {
        utilQuestionHandler = initQuestionHandler();
        QuestionDesigner questionDesigner = new QuestionDesigner();
        VBox resultVBox = questionDesigner.generateElements();

        questionDesigner.saveChangesQuestion.setOnAction(event -> {
            QuestionDataModel[] writtingQuestions = utilQuestionHandler.getquestionDataModels();
            String separator = "|";
            StringBuilder lineBuilder = new StringBuilder();
            for (int i = 0; i < maxQuestionIndex; i++) {
                if (i == 0) {
                    lineBuilder.append(separator).append(utilQuestionHandler.getFullListOfISTQBTypeReadFromData().get(i))
                            .append(separator).append(writtingQuestions[i].questionTitle1)
                            .append(separator).append(writtingQuestions[i].questionTitle2)
                            .append(separator).append(writtingQuestions[i].questionTitle3)
                            .append(separator).append(writtingQuestions[i].questionTitle4)
                            .append(separator).append(writtingQuestions[i].questionTitle5)
                            .append(separator).append(writtingQuestions[i].questionTitle6)
                            .append(separator).append(writtingQuestions[i].questionTitle7)
                            .append(separator).append(writtingQuestions[i].questionTitle8)
                            .append(separator).append(writtingQuestions[i].questionTitle9)
                            .append(separator).append(writtingQuestions[i].questionTitle10)
                            .append(separator).append(isMultiHeaderText)
                            .append(separator).append(isAnswer1CorrectHeaderText)
                            .append(separator).append(writtingQuestions[i].questionAnswer1)
                            .append(separator).append(isAnswer2CorrectHeaderText)
                            .append(separator).append(writtingQuestions[i].questionAnswer2)
                            .append(separator).append(isAnswer3CorrectHeaderText)
                            .append(separator).append(writtingQuestions[i].questionAnswer3)
                            .append(separator).append(isAnswer4CorrectHeaderText)
                            .append(separator).append(writtingQuestions[i].questionAnswer4)
                            .append(separator).append(isAnswer5CorrectHeaderText)
                            .append(separator).append(writtingQuestions[i].questionAnswer5)
                            .append(separator).append(isAnswer6CorrectHeaderText)
                            .append(separator).append(writtingQuestions[i].questionAnswer6)
                            .append(separator).append(isAnswer7CorrectHeaderText)
                            .append(separator).append(writtingQuestions[i].questionAnswer7)
                            .append(separator).append(isAnswer8CorrectHeaderText)
                            .append(separator).append(writtingQuestions[i].questionAnswer8)
                            .append(separator).append(isAnswer9CorrectHeaderText)
                            .append(separator).append(writtingQuestions[i].questionAnswer9)
                            .append(separator).append(isAnswer10CorrectHeaderText)
                            .append(separator).append(writtingQuestions[i].questionAnswer10).append(separator).append(System.lineSeparator());
                } else {
                    lineBuilder.append(separator).append(utilQuestionHandler.getFullListOfISTQBTypeReadFromData().get(i))
                            .append(separator).append(getAllCharsInString(writtingQuestions[i].questionTitle1))
                            .append(separator).append(getAllCharsInString(writtingQuestions[i].questionTitle2))
                            .append(separator).append(getAllCharsInString(writtingQuestions[i].questionTitle3))
                            .append(separator).append(getAllCharsInString(writtingQuestions[i].questionTitle4))
                            .append(separator).append(getAllCharsInString(writtingQuestions[i].questionTitle5))
                            .append(separator).append(getAllCharsInString(writtingQuestions[i].questionTitle6))
                            .append(separator).append(getAllCharsInString(writtingQuestions[i].questionTitle7))
                            .append(separator).append(getAllCharsInString(writtingQuestions[i].questionTitle8))
                            .append(separator).append(getAllCharsInString(writtingQuestions[i].questionTitle9))
                            .append(separator).append(getAllCharsInString(writtingQuestions[i].questionTitle10))
                            .append(separator).append(utilQuestionHandler.convertBooleanToSaveFormat(writtingQuestions[i].isMultipleChoice))
                            .append(separator).append(utilQuestionHandler.convertBooleanToSaveFormat(writtingQuestions[i].isQuestionAnswer1Correct))
                            .append(separator).append(getAllCharsInString(writtingQuestions[i].questionAnswer1))
                            .append(separator).append(utilQuestionHandler.convertBooleanToSaveFormat(writtingQuestions[i].isQuestionAnswer2Correct))
                            .append(separator).append(getAllCharsInString(writtingQuestions[i].questionAnswer2))
                            .append(separator).append(utilQuestionHandler.convertBooleanToSaveFormat(writtingQuestions[i].isQuestionAnswer3Correct))
                            .append(separator).append(getAllCharsInString(writtingQuestions[i].questionAnswer3))
                            .append(separator).append(utilQuestionHandler.convertBooleanToSaveFormat(writtingQuestions[i].isQuestionAnswer4Correct))
                            .append(separator).append(getAllCharsInString(writtingQuestions[i].questionAnswer4))
                            .append(separator).append(utilQuestionHandler.convertBooleanToSaveFormat(writtingQuestions[i].isQuestionAnswer5Correct))
                            .append(separator).append(getAllCharsInString(writtingQuestions[i].questionAnswer5))
                            .append(separator).append(utilQuestionHandler.convertBooleanToSaveFormat(writtingQuestions[i].isQuestionAnswer6Correct))
                            .append(separator).append(getAllCharsInString(writtingQuestions[i].questionAnswer6))
                            .append(separator).append(utilQuestionHandler.convertBooleanToSaveFormat(writtingQuestions[i].isQuestionAnswer7Correct))
                            .append(separator).append(getAllCharsInString(writtingQuestions[i].questionAnswer7))
                            .append(separator).append(utilQuestionHandler.convertBooleanToSaveFormat(writtingQuestions[i].isQuestionAnswer8Correct))
                            .append(separator).append(getAllCharsInString(writtingQuestions[i].questionAnswer8))
                            .append(separator).append(utilQuestionHandler.convertBooleanToSaveFormat(writtingQuestions[i].isQuestionAnswer9Correct))
                            .append(separator).append(getAllCharsInString(writtingQuestions[i].questionAnswer9))
                            .append(separator).append(utilQuestionHandler.convertBooleanToSaveFormat(writtingQuestions[i].isQuestionAnswer10Correct))
                            .append(separator).append(getAllCharsInString(writtingQuestions[i].questionAnswer10)).append(separator).append(System.lineSeparator());
                }
            }
            try {
                String textToWrite = convertEnterCharacterToSave(String.valueOf(lineBuilder));
                writeDataToTextFile(questionBank, textToWrite);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        questionDesigner.openFileButton.setOnAction(event -> {
            try {
                fileChooser.setInitialDirectory(new File(getCurrentPath()));
                imagesFolder = new File(getCurrentPath() + "\\Images");
                imageFolderAbsolutePath = imagesFolder.getAbsolutePath();
            } catch (IOException ignored) {
            }
            questionBank = new File("");
            questionBank = fileChooser.showOpenDialog(null);
            questionIndex = 1;
            questionDesigner.getQuestionBankFileName().setText(questionBank.getName());
            utilQuestionHandler.readAndMapQuestionDataFromFileToDataObject(questionBank);
            maxQuestionIndex = utilQuestionHandler.getquestionDataModels().length;
            questionDesigner.displayQuestionDataInQuestionModelByIndex(utilQuestionHandler, questionIndex);
            utilQuestionHandler.getListOfISTQBTypeReadFromData().stream()
                    .map(e -> questionDesigner.checkBoxGroup.getItems().add(e)).collect(Collectors.toList());
            questionDesigner.checkBoxGroup.setStyle(fontStyle16);
            try {
                isReviewAnswers = true;
                isQuestionDesign = true;
                isQuestionTempCheck = false;
                openNewStageAndScene(setupLayoutPageExam(), "Exam Page Preview of: " + QuestionDesigner.getSelectedGroupNameValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            newStage.setOnCloseRequest(event1 -> {
                newStage = null;
            });
        });

        questionDesigner.applyTempChange.setOnAction(event -> {
            if (getTemporaryChangeQuestion() != null) {
                try {
                    utilQuestionHandler.getquestionDataModels()[questionIndex] = checkIfTitlesHasNewImageThenSaveToImagesFolder(getTemporaryChangeQuestion());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                QuestionHandler.fullListOfISTQBTypeReadFromData.set(questionIndex, selectedGroupName.getText());
                int newListCount = QuestionHandler.fullListOfISTQBTypeReadFromData.size();
                maxQuestionIndex = (newListCount == maxQuestionIndex) ? maxQuestionIndex : newListCount;
                questionDesigner.displayQuestionDataInQuestionModelByIndex(utilQuestionHandler, questionIndex);
                updatePreviewPage();
            }
        });

        questionDesigner.checkTempChange.setOnAction(event -> {
            QuestionDataModel tempChanges = new QuestionDataModel();
            tempChanges.questionTitle1 = convertEnterCharacterToReview(questionDesigner.textArea1.getText());
            tempChanges.questionTitle2 = convertEnterCharacterToReview(questionDesigner.textArea2.getText());
            tempChanges.questionTitle3 = convertEnterCharacterToReview(questionDesigner.textArea3.getText());
            tempChanges.questionTitle4 = convertEnterCharacterToReview(questionDesigner.textArea4.getText());
            tempChanges.questionTitle5 = convertEnterCharacterToReview(questionDesigner.textArea5.getText());
            tempChanges.questionTitle6 = convertEnterCharacterToReview(questionDesigner.textArea6.getText());
            tempChanges.questionTitle7 = convertEnterCharacterToReview(questionDesigner.textArea7.getText());
            tempChanges.questionTitle8 = convertEnterCharacterToReview(questionDesigner.textArea8.getText());
            tempChanges.questionTitle9 = convertEnterCharacterToReview(questionDesigner.textArea9.getText());
            tempChanges.questionTitle10 = convertEnterCharacterToReview(questionDesigner.textArea10.getText());
            tempChanges.isMultipleChoice = questionDesigner.isMultiAnswersCheckbox.isSelected();
            tempChanges.questionAnswer1 = convertEnterCharacterToReview(questionDesigner.textAreaAnswer1.getText());
            tempChanges.isQuestionAnswer1Correct = questionDesigner.isAnswer1Correct.isSelected();
            tempChanges.questionAnswer2 = convertEnterCharacterToReview(questionDesigner.textAreaAnswer2.getText());
            tempChanges.isQuestionAnswer2Correct = questionDesigner.isAnswer2Correct.isSelected();
            tempChanges.questionAnswer3 = convertEnterCharacterToReview(questionDesigner.textAreaAnswer3.getText());
            tempChanges.isQuestionAnswer3Correct = questionDesigner.isAnswer3Correct.isSelected();
            tempChanges.questionAnswer4 = convertEnterCharacterToReview(questionDesigner.textAreaAnswer4.getText());
            tempChanges.isQuestionAnswer4Correct = questionDesigner.isAnswer4Correct.isSelected();
            tempChanges.questionAnswer5 = convertEnterCharacterToReview(questionDesigner.textAreaAnswer5.getText());
            tempChanges.isQuestionAnswer5Correct = questionDesigner.isAnswer5Correct.isSelected();
            tempChanges.questionAnswer6 = convertEnterCharacterToReview(questionDesigner.textAreaAnswer6.getText());
            tempChanges.isQuestionAnswer6Correct = questionDesigner.isAnswer6Correct.isSelected();
            tempChanges.questionAnswer7 = convertEnterCharacterToReview(questionDesigner.textAreaAnswer7.getText());
            tempChanges.isQuestionAnswer7Correct = questionDesigner.isAnswer7Correct.isSelected();
            tempChanges.questionAnswer8 = convertEnterCharacterToReview(questionDesigner.textAreaAnswer8.getText());
            tempChanges.isQuestionAnswer8Correct = questionDesigner.isAnswer8Correct.isSelected();
            tempChanges.questionAnswer9 = convertEnterCharacterToReview(questionDesigner.textAreaAnswer9.getText());
            tempChanges.isQuestionAnswer9Correct = questionDesigner.isAnswer9Correct.isSelected();
            tempChanges.questionAnswer10 = convertEnterCharacterToReview(questionDesigner.textAreaAnswer10.getText());
            tempChanges.isQuestionAnswer10Correct = questionDesigner.isAnswer10Correct.isSelected();
            setTemporaryChangeQuestion(tempChanges);
            isReviewAnswers = true;
            isQuestionDesign = true;
            isQuestionTempCheck = true;
            if (newStage == null) {
                try {
                    newStage = new Stage();
                    openNewStageAndScene(setupLayoutPageExam(), "Exam Page Preview of: " + QuestionDesigner.getSelectedGroupNameValue());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                newStage.setOnCloseRequest(event1 -> {
                    newStage = null;
                });
            } else {
                updatePreviewPage();
            }
        });

        questionDesigner.questionIndexTextField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                questionIndex = Integer.parseInt(questionDesigner.questionIndexTextField.getText());
                questionDesigner.displayQuestionDataInQuestionModelByIndex(utilQuestionHandler, questionIndex);
                isQuestionTempCheck = false;
                updatePreviewPage();
            }
        });

        questionDesigner.nextQuestion.setOnAction((event -> {
            if (questionIndex < maxQuestionIndex - 1) {
                questionIndex++;
                questionDesigner.displayQuestionDataInQuestionModelByIndex(utilQuestionHandler, questionIndex);
                isQuestionTempCheck = false;
                updatePreviewPage();
            }
        }));

        questionDesigner.prevQuestion.setOnAction((event -> {
            if (questionIndex > 1) {
                questionIndex--;
                questionDesigner.displayQuestionDataInQuestionModelByIndex(utilQuestionHandler, questionIndex);
                isQuestionTempCheck = false;
                updatePreviewPage();
            }
        }));

        questionDesigner.firstQuestion.setOnAction((event -> {
            if (questionIndex >= 1) {
                questionIndex = 1;
                questionDesigner.displayQuestionDataInQuestionModelByIndex(utilQuestionHandler, questionIndex);
                isQuestionTempCheck = false;
                updatePreviewPage();
            }
        }));

        questionDesigner.lastQuestion.setOnAction((event -> {
            if (questionIndex < maxQuestionIndex - 1) {
                questionIndex = maxQuestionIndex - 1;
                questionDesigner.displayQuestionDataInQuestionModelByIndex(utilQuestionHandler, questionIndex);
                isQuestionTempCheck = false;
                updatePreviewPage();
            }
        }));

        questionDesigner.addQuestion.setOnAction(event -> {
            questionIndex = maxQuestionIndex;
            utilQuestionHandler.addNewQuestionToModel();
            questionDesigner.displayQuestionDataInQuestionModelByIndex(utilQuestionHandler, questionIndex);
            isQuestionTempCheck = false;
            updatePreviewPage();
        });

        questionDesigner.deleteQuestion.setOnAction(event -> {
            utilQuestionHandler.deleteCurrentQuestionFromModel();
            maxQuestionIndex -= 1;
            if (questionIndex >= maxQuestionIndex) {
                questionIndex = maxQuestionIndex - 1;
            }
            questionDesigner.displayQuestionDataInQuestionModelByIndex(utilQuestionHandler, questionIndex);
            isQuestionTempCheck = false;
            updatePreviewPage();
        });

        return resultVBox;
    }

    public static VBox setupTableDesignerForTextArea(TextArea workingTextArea) {
        Label tableRowNo = new Label("Row");
        Label tableColNo = new Label("Column");
        Button createTableBT = new Button("Create Table");
        HBox commandBox = new HBox();
        commandBox.setAlignment(Pos.CENTER);
        commandBox.setPrefWidth(screenWidth / 1.5);
        commandBox.getChildren().add(tableRowNo);
        commandBox.getChildren().add(tableRowTF);
        commandBox.getChildren().add(tableColNo);
        commandBox.getChildren().add(tableColTF);
        commandBox.getChildren().add(createTableBT);

        String headerText = "[TableHeader]";
        String rowText = "[TableRow]";
        tableGridPane = new GridPane();
        tableGridPane.autosize();
        String textAreaContent = workingTextArea.getText();
        if (textAreaContent.startsWith(headerText)) {
            String[] tableRowData = textAreaContent.split("(\\[TableRow\\])");
            displayTableGridPaneWithData(tableRowTF, tableColTF, tableRowData);
        }

        createTableBT.setOnAction(event -> {
            tableGridPane = new GridPane();
            displayTableGridPane(tableRowTF, tableColTF);
            tableScrollPane.setContent(tableGridPane);
        });

        Button insertTableBT = new Button("Insert Table and Close");
        previewTable.setStyle(cssGreenColorBGValue);
        HBox bottomCommandBox = new HBox();
        bottomCommandBox.setAlignment(Pos.CENTER);
        bottomCommandBox.setBackground(grayBackGround);
        bottomCommandBox.getChildren().add(insertTableBT);
        bottomCommandBox.getChildren().add(previewTable);

        insertTableBT.setOnAction(event -> {
            workingTextArea.setText(getGeneratedTableContent());
            popUpStage.close();
            popUpTablePreviewStage.close();
        });

        popUpStage.setOnCloseRequest(event -> {
            popUpTablePreviewStage.close();
        });

        previewTable.setOnAction(event -> {
            openNewStageAndSceneWithDefinedScreenSize(popUpTablePreviewStage, setupTablePreviewVBox(), "Table Preview", screenWidth / 1.5, screenHeight / 1.5);
        });
        tableScrollPane = new ScrollPane();
        tableScrollPane.setStyle(fontStyle16);
        tableScrollPane.setBackground(grayBackGround);
        tableScrollPane.setMaxWidth(screenWidth / 1.5);
        tableScrollPane.setPrefHeight(screenHeight / 1.5);
        tableScrollPane.setContent(tableGridPane);

        VBox layoutTableDesigner = new VBox();
        layoutTableDesigner.setBackground(grayBackGround);
        layoutTableDesigner.setPrefHeight(screenHeight / 1.5);
        layoutTableDesigner.getChildren().add(commandBox);
        layoutTableDesigner.getChildren().add(tableScrollPane);
        layoutTableDesigner.getChildren().add(bottomCommandBox);

        VBox resultVBox = new VBox();
        resultVBox.getChildren().add(layoutTableDesigner);

        return resultVBox;
    }

    private static void displayTableGridPane(TextField tableRowTF, TextField tableColTF) {
        if (!tableRowTF.getText().isEmpty() && !tableColTF.getText().isEmpty()) {
            int rowNo = Integer.parseInt(tableRowTF.getText());
            int colNo = Integer.parseInt(tableColTF.getText()) + 1;
            generateTextFieldsInTableDesign(rowNo, colNo, null);
        }
    }

    private static void generateTextFieldsInTableDesign(int rowNo, int colNo, String[][] data) {
        for (int row = 0; row < rowNo; row++) {
            TextField cellTF = null;
            for (int col = 0; col < colNo; col++) {
                if (col == 0) {
                    if ((row == 0)) {
                        tableGridPane.add(new Label("[Header]"), col, row);
                    } else {
                        tableGridPane.add(new Label("[TableRow]"), col, row);
                    }
                } else {
                    if (data == null) {
                        cellTF = new TextField("row" + row + "col" + col);
                    } else {
                        try {
                            cellTF = new TextField(data[row][col]);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            cellTF = new TextField("");
                        }
                    }
                    cellTF.setPrefWidth(100);
                    tableGridPane.add(cellTF, col, row);
                }
            }
        }
    }

    public static void displayTableGridPaneWithData(TextField tableRowTF, TextField tableColTF, String[] tableRowData) {
        int maxRow = tableRowData.length;
        int maxCol = 0;
        String[][] tableRowSplitedData = new String[maxRow][];
        for (int row = 0; row < maxRow; row++) {
            if (row == 0) {
                tableRowSplitedData[row] = tableRowData[row].replace("[TableHeader]", "#").split("#");
            } else {
                tableRowData[row] = "#" + tableRowData[row];
                tableRowSplitedData[row] = tableRowData[row].split("#");
            }
            if (maxCol < tableRowSplitedData[row].length) {
                maxCol = tableRowSplitedData[row].length;
            }
        }
        tableRowTF.setText(String.valueOf(maxRow));
        tableColTF.setText(String.valueOf(maxCol - 1));
        generateTextFieldsInTableDesign(maxRow, maxCol, tableRowSplitedData);
    }

    public static VBox setupTablePreviewVBox() {
        GridPane tableContent = renderGridPaneWithDataGridTable(tableGridPane);
        tableContent.setGridLinesVisible(true);

        tablePreviewScrollPane.setStyle(fontStyle16);
        tablePreviewScrollPane.setBackground(grayBackGround);
        tablePreviewScrollPane.setMaxWidth(screenWidth / 1.5);
        tablePreviewScrollPane.setPrefHeight(screenHeight / 1.5);
        tablePreviewScrollPane.setContent(tableContent);

        VBox previewTable = new VBox();
        previewTable.setBackground(grayBackGround);
        previewTable.setPrefHeight(screenHeight / 1.5);
        previewTable.getChildren().add(tablePreviewScrollPane);

        VBox resultVBox = new VBox();
        resultVBox.getChildren().add(previewTable);

        return resultVBox;
    }

    private static QuestionDataModel checkIfTitlesHasNewImageThenSaveToImagesFolder(QuestionDataModel temporaryChangeQuestion) throws IOException {
        temporaryChangeQuestion.questionTitle1 = (temporaryChangeQuestion.questionTitle1.matches("<InsertingImage>.+")) ? updateNewImageNameAndSaveToImageFolderInTitle(temporaryChangeQuestion.questionTitle1) : temporaryChangeQuestion.questionTitle1;
        temporaryChangeQuestion.questionTitle2 = (temporaryChangeQuestion.questionTitle2.matches("<InsertingImage>.+")) ? updateNewImageNameAndSaveToImageFolderInTitle(temporaryChangeQuestion.questionTitle2) : temporaryChangeQuestion.questionTitle2;
        temporaryChangeQuestion.questionTitle3 = (temporaryChangeQuestion.questionTitle3.matches("<InsertingImage>.+")) ? updateNewImageNameAndSaveToImageFolderInTitle(temporaryChangeQuestion.questionTitle3) : temporaryChangeQuestion.questionTitle3;
        temporaryChangeQuestion.questionTitle4 = (temporaryChangeQuestion.questionTitle4.matches("<InsertingImage>.+")) ? updateNewImageNameAndSaveToImageFolderInTitle(temporaryChangeQuestion.questionTitle4) : temporaryChangeQuestion.questionTitle4;
        temporaryChangeQuestion.questionTitle5 = (temporaryChangeQuestion.questionTitle5.matches("<InsertingImage>.+")) ? updateNewImageNameAndSaveToImageFolderInTitle(temporaryChangeQuestion.questionTitle5) : temporaryChangeQuestion.questionTitle5;
        temporaryChangeQuestion.questionTitle6 = (temporaryChangeQuestion.questionTitle6.matches("<InsertingImage>.+")) ? updateNewImageNameAndSaveToImageFolderInTitle(temporaryChangeQuestion.questionTitle6) : temporaryChangeQuestion.questionTitle6;
        temporaryChangeQuestion.questionTitle7 = (temporaryChangeQuestion.questionTitle7.matches("<InsertingImage>.+")) ? updateNewImageNameAndSaveToImageFolderInTitle(temporaryChangeQuestion.questionTitle7) : temporaryChangeQuestion.questionTitle7;
        temporaryChangeQuestion.questionTitle8 = (temporaryChangeQuestion.questionTitle8.matches("<InsertingImage>.+")) ? updateNewImageNameAndSaveToImageFolderInTitle(temporaryChangeQuestion.questionTitle8) : temporaryChangeQuestion.questionTitle8;
        temporaryChangeQuestion.questionTitle9 = (temporaryChangeQuestion.questionTitle9.matches("<InsertingImage>.+")) ? updateNewImageNameAndSaveToImageFolderInTitle(temporaryChangeQuestion.questionTitle9) : temporaryChangeQuestion.questionTitle9;
        temporaryChangeQuestion.questionTitle10 = (temporaryChangeQuestion.questionTitle10.matches("<InsertingImage>.+")) ? updateNewImageNameAndSaveToImageFolderInTitle(temporaryChangeQuestion.questionTitle10) : temporaryChangeQuestion.questionTitle10;
        return temporaryChangeQuestion;
    }

    private static void updatePreviewPage() {
        previewQuestionButton.fire();
    }
}
