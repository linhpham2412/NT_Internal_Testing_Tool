package nt.testingtool.istqb.Utils;

import javafx.animation.Timeline;
import javafx.scene.text.Font;

import java.io.IOException;

public class ProjectConfiguration {
    public static Font toolFont = new Font(24);
    public static double screenWidth = 1500;
    public static double screenHeight = 850;
    private static String questionFileName = "ISTQB_QuestionsBank";
    private static String zipFilePassword = "123";
    private static String testUserName;
    //set up testing time
    private static int testingMinutes = 60;
    //Question section
    private static int numberOfQuestionsPerQuestionBank = 40;
    //    private static int numberOfQuestionBanksInGroup = 0;
    private static String questionGroupName = "";
    private final String currentPath = null;
    //set up component size
    private final double objectWidthInScrollPane = screenWidth * 0.54;
    private final double checkBoxWidthInScrollPane = screenWidth * 0.25;
    private Timeline timerTimeLine;

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

    public static void setQuestionGroupName(String questionGroupName) {
        ProjectConfiguration.questionGroupName = questionGroupName;
    }

    public static String getQuestionFileName() {
        return questionFileName;
    }

    public static void setQuestionFileName(String questionFileName) {
        ProjectConfiguration.questionFileName = questionFileName;
    }

//    public static int getNumberOfQuestionBanksInGroup() {
//        return numberOfQuestionBanksInGroup;
//    }
//
//    public static void setNumberOfQuestionBanksInGroup(int numberOfQuestionBanksInGroup) {
//        ProjectConfiguration.numberOfQuestionBanksInGroup = numberOfQuestionBanksInGroup;
//    }

    public static String getZipFilePassword() {
        return zipFilePassword;
    }

    public static void setZipFilePassword(String filePassword) {
        ProjectConfiguration.zipFilePassword = filePassword;
    }

    public int getTestingMinutes() {
        return testingMinutes;
    }

    public static void setTestingMinutes(int testingMinutes) {
        ProjectConfiguration.testingMinutes = testingMinutes;
    }
}
