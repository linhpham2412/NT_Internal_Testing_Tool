package nt.testingtool.istqb.Utils;

import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.util.Arrays;

public class ProjectConfiguration {
    public static Font toolFont = new Font(24);
    public static double screenWidth = 1500;
    public static double screenHeight = 850;
    private static String questionFileName = "ISTQB_QuestionsBank";
    private static String zipFilePassword = "123";
    private static final String encryptDecryptSalt = "13260779";
    private static final String encryptDecryptKey = "q>CO((oQKm9Pl^aR7UV.a3wx$6kX-D";
    private static String testUserName;
    //set up testing time
    private static int testingMinutes = 60;
    //Question section
    private static int numberOfQuestionsPerQuestionBank = 40;
    public static int maxQuestionPerPagination = 40;

    public static int getPassingScore() {
        return passingScore;
    }

    public static void setPassingScore(int passingScore) {
        ProjectConfiguration.passingScore = passingScore;
    }

    private static int passingScore = 26;
    private static String questionGroupName = "";
//    private final String currentPath = null;
    public static FileChooser fileChooser = new FileChooser();

    public static double getObjectWidthInScrollPane() {
        return objectWidthInScrollPane;
    }

    public static double getCheckBoxWidthInScrollPane() {
        return checkBoxWidthInScrollPane;
    }

    //set up component size
    private static final double objectWidthInScrollPane = screenWidth * 0.54;
    private static final double checkBoxWidthInScrollPane = screenWidth * 0.25;

    public static Timeline getTimerTimeLine() {
        return timerTimeLine;
    }

    public static void setTimerTimeLine(Timeline timer) {
        timerTimeLine = timer;
    }

    private static Timeline timerTimeLine;
    private static int maxNumberOfAnswerElementsInQuestionBank = 10;

    public static Background grayBackGround = new Background(new BackgroundFill(Paint.valueOf("#CACACA"), CornerRadii.EMPTY, Insets.EMPTY));
    public static Color redNTColor = Color.valueOf("#FA6070");
    public static Color darkBlueColor = Color.valueOf("#284977");

    public static String getCurrentPath() throws IOException {
        return new java.io.File(".").getCanonicalPath();
    }

    public static int getNumberOfQuestionsPerQuestionBank() {
        return numberOfQuestionsPerQuestionBank;
    }

    public static void setNumberOfQuestionsPerQuestionBank(int numberOfQuestions) {
        numberOfQuestionsPerQuestionBank = numberOfQuestions;
    }

    public static String getQuestionGroupName() {
        return questionGroupName;
    }

    public static String getQuestionGroupShortName(){
        String[] istqbTestingTypeShortName = questionGroupName.split(" ");
        Object[] istqbShortName = Arrays.stream(istqbTestingTypeShortName,2
                ,istqbTestingTypeShortName.length).toArray();
        return Arrays.toString(istqbShortName)
                .replace("[","")
                .replace("]","")
                .replace(",","")
                .trim();
    }

    public static void setQuestionGroupName(String questionGroupName) {
        ProjectConfiguration.questionGroupName = questionGroupName;
    }

    public static String getQuestionFileName() {
        return questionFileName;
    }

    public static void setQuestionFileName(String questionFileName) {
        ProjectConfiguration.questionFileName = questionFileName;
    }

    public static String getZipFilePassword() {
        return zipFilePassword;
    }

    public static void setZipFilePassword(String filePassword) {
        ProjectConfiguration.zipFilePassword = filePassword;
    }


    public static int getMaxNumberOfAnswerElementsInQuestionBank() {
        return maxNumberOfAnswerElementsInQuestionBank;
    }

    public static void setMaxNumberOfAnswerElementsInQuestionBank(int numberOfAnswerElementsInQuestionBank) {
        maxNumberOfAnswerElementsInQuestionBank = numberOfAnswerElementsInQuestionBank;
    }

    public static int getTestingMinutes() {
        return testingMinutes;
    }

    public static void setTestingMinutes(int testingMinutes) {
        ProjectConfiguration.testingMinutes = testingMinutes;
    }

    public static void updateISTQBTypeInformation(String istqbTypeFullName){
        switch (istqbTypeFullName){
            case "Foundation CTFL Certified Tester Foundation Level":
            case "Foundation CTFL-AT Certified Tester Foundation Level Agile Tester":
            case "Specialist CT-MBT Certified Tester Model-Based Tester":
            case "Specialist CT-AuT Certified Tester Automotive Software Tester":
            case "Specialist CT-UT Certified Tester Usability Testing":
            case "Specialist CT-AcT Certified Tester Acceptance Testing":
            case "Specialist CT-GT Certified Tester Gambling Industry Tester":
            case "Specialist CT-MAT Certified Tester Mobile Application Testing":
            case "Specialist CT-AI Certified Tester AI Testing":
            case "Specialist CT-GaMe Certified Tester Game Testing":
                setNumberOfQuestionsPerQuestionBank(40);
                setPassingScore(26);
                setTestingMinutes(60);
                break;
            case "Advanced CTAL-TM Certified Tester Advanced Level Test Manager":
                setNumberOfQuestionsPerQuestionBank(65);
                setPassingScore(42);
                setTestingMinutes(180);
                break;
            case "Advanced CTAL-TA Certified Tester Advanced Level Test Analyst":
                setNumberOfQuestionsPerQuestionBank(40);
                setPassingScore(26);
                setTestingMinutes(120);
                break;
            case "Advanced CTAL-TTA Certified Tester Advanced Level Technical Test Analyst":
            case "Advanced CT-SEC Certified Tester Security Tester":
                setNumberOfQuestionsPerQuestionBank(45);
                setPassingScore(29);
                setTestingMinutes(120);
                break;
            case "Advanced CTAL-ATLaS Certified Tester Advanced Level Agile Test Leadership at Scale":
                setNumberOfQuestionsPerQuestionBank(15);
                setPassingScore(10);
                setTestingMinutes(45);
                break;
            case "Advanced CTAL-ATT Certified Tester Advanced Level Agile Technical Tester":
            case "Specialist CT-PT Certified Tester Performance Testing":
            case "Specialist CT-TAE Certified Tester Test Automation Engineer":
                setNumberOfQuestionsPerQuestionBank(40);
                setPassingScore(26);
                setTestingMinutes(90);
                break;
        }
    }

    public static String getEncryptDecryptSalt() {
        return encryptDecryptSalt;
    }

    public static String getEncryptDecryptKey() {
        return encryptDecryptKey;
    }
}
