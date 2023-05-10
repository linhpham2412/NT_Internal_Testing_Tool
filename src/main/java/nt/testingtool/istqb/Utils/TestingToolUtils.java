package nt.testingtool.istqb.Utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import nt.testingtool.istqb.datamodel.QuestionDataModel;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static nt.testingtool.istqb.Utils.PageVBoxHandler.*;
import static nt.testingtool.istqb.Utils.ProjectConfiguration.*;
import static nt.testingtool.istqb.Utils.QuestionHandler.*;

public class TestingToolUtils {

    static QuestionHandler utilQuestionHandler;

    public static int getCurrentPageIndex() {
        return currentPageIndex;
    }

    static int currentPageIndex = 0;
    static boolean isTestingEnd = false;
    static File loadedImageFile = null;

    public static int calculateTestingResult() {
        int calculatedCorrectAnswers = 0;
        for (int i = 0; i < getCorrectAnswer().length; i++) {
            if (Arrays.equals(getCorrectAnswer()[i], getSelectedAnswer()[i])) {
                calculatedCorrectAnswers += 1;
            }
        }
        return calculatedCorrectAnswers;
    }

    public static String determinePassOrFail(int actualResult) {
        return (actualResult < getPassingScore()) ? "Failed" : "Passed";
    }

    static String calculateTimeLeft(int[] seconds) {
        int minsLeft = seconds[0] / 60;
        String minsText = "";
        if (minsLeft < 10) {
            minsText = "0" + minsLeft;
        } else {
            minsText = String.valueOf(minsLeft);
        }
        int secsLeft = seconds[0] - minsLeft * 60;
        String secsText = "";
        if (secsLeft < 10) {
            secsText = "0" + secsLeft;
        } else {
            secsText = String.valueOf(secsLeft);
        }
        return minsText + ":" + secsText;
    }

    public static ScrollPane getQuestionPages(int pageIndex) {
        //setup and clean up data
        currentPageIndex = pageIndex;
        utilQuestionHandler.initQuestionElements(pageIndex);
        //map question data to local variables
        mapValueFromTestingQuestionToLocalVariablesByPageIndex(pageIndex);
        //Add more question components inside vbox
        Label questionNumber = new Label("Question " + (pageIndex + 1) + ":");
        assignQuestionDataFromClassToTitleLabelOrImage();
        String kindOfChoice = (utilQuestionHandler.isQuestionMultipleChoices()) ? "[Multi Choice]" : "[Single choice]";
        Label answerLabel = new Label("Answer: " + kindOfChoice);
        assignAnswersDataFromClassToCheckBoxOrRadioButton(pageIndex);
        if (isTestingEnd) disableAllAnswersInHBoxContainer();
        VBox questionContainer = new VBox();
        questionContainer.getChildren().add(questionNumber);
        addTitleObjectsToVBoxContainer(questionContainer);
        questionContainer.getChildren().add(answerLabel);
        addAnswerHBoxObjectsToVBoxContainer(questionContainer);
        addNavigationButtonsToVBoxContainer(questionContainer, pageIndex);
        VBox.setMargin(answerLabel, new Insets(5, 5, 5, 5));
        VBox.setMargin(questionNumber, new Insets(5, 5, 5, 5));
        //End of question components
        VBox alignmentQuestionVBox = new VBox();
        alignmentQuestionVBox.getChildren().add(questionContainer);
        VBox.setMargin(questionContainer, new Insets(5, 5, 5, 5));
        AnchorPane anchorPaneQuestion = new AnchorPane();
        anchorPaneQuestion.getChildren().add(alignmentQuestionVBox);
        ScrollPane scrollPaneQuestion = new ScrollPane();
        scrollPaneQuestion.setFitToWidth(true);
        scrollPaneQuestion.setPrefViewportHeight(3000);
        scrollPaneQuestion.setPrefViewportWidth(3000);
        scrollPaneQuestion.setPrefHeight(screenHeight / 2);
        scrollPaneQuestion.setPrefWidth(getObjectWidthInScrollPane());
        scrollPaneQuestion.setContent(anchorPaneQuestion);
        return scrollPaneQuestion;
    }

    public static void addNavigationButtonsToVBoxContainer(VBox questionContainer, int pageIndex) {
        Button nextPageButton = new Button("Next");
        Button previousPageButton = new Button("Previous");
        Button endTestButton = new Button("End Test");
        Button summaryPageButton = new Button("Summary Page");
//        summaryPageButton.setVisible(isTestingEnd);
        HBox navigationContainer = new HBox();
        navigationContainer.getChildren().add(summaryPageButton);
        navigationContainer.getChildren().add(previousPageButton);
        navigationContainer.getChildren().add(nextPageButton);
        navigationContainer.getChildren().add(endTestButton);
        navigationContainer.setAlignment(Pos.CENTER);
        HBox.setMargin(summaryPageButton, new Insets(10, 5, 5, 5));
        HBox.setMargin(previousPageButton, new Insets(10, 5, 5, 5));
        HBox.setMargin(nextPageButton, new Insets(10, 5, 5, 5));
        HBox.setMargin(endTestButton, new Insets(10, 10, 5, 10));
        if (pageIndex == 0) {
            summaryPageButton.setVisible(false);
            nextPageButton.setDisable(false);
            previousPageButton.setDisable(true);
            endTestButton.setVisible(false);
        } else if (pageIndex == getNumberOfQuestionsPerQuestionBank() - 1) {
            summaryPageButton.setVisible(true);
            nextPageButton.setDisable(true);
            previousPageButton.setDisable(false);
            endTestButton.setVisible(true);
        } else {
            summaryPageButton.setVisible(false);
            nextPageButton.setDisable(false);
            previousPageButton.setDisable(false);
            endTestButton.setVisible(false);
        }
        if (isTestingEnd) endTestButton.setDisable(true);
        nextPageButton.setOnAction(event -> pagination.setCurrentPageIndex(pageIndex + 1));
        previousPageButton.setOnAction(event -> pagination.setCurrentPageIndex(pageIndex - 1));
        endTestButton.setOnAction(event -> {
            try {
                String remainingAnswers = checkUnAnsweredQuestions();
                AlertDisplay.displayMissingInformationAlert("Unfinished Answers Remain! \n" +
                        "Click on Summary Page button to skip this check! \n" +
                        "Please recheck these questions below: ", remainingAnswers);
            } catch (Exception e) {
                changeStageAndScene(event, setupSummaryPage(), "Summary Page");
            }
        });
        summaryPageButton.setOnAction(event -> {
            isTestingEnd = false;
            changeStageAndScene(event, setupSummaryPage(), "Summary Page");
        });
        questionContainer.getChildren().add(navigationContainer);
    }


    private static void mapValueFromTestingQuestionToLocalVariablesByPageIndex(int questionIndex) {
        QuestionDataModel testingQuestion = utilQuestionHandler.testingQuestions[questionIndex];
        utilQuestionHandler.questionStringTitle[0] = testingQuestion.getQuestionTitle1();
        utilQuestionHandler.questionStringTitle[1] = testingQuestion.getQuestionTitle2();
        utilQuestionHandler.questionStringTitle[2] = testingQuestion.getQuestionTitle3();
        utilQuestionHandler.questionStringTitle[3] = testingQuestion.getQuestionTitle4();
        utilQuestionHandler.questionStringTitle[4] = testingQuestion.getQuestionTitle5();
        utilQuestionHandler.questionStringTitle[5] = testingQuestion.getQuestionTitle6();
        utilQuestionHandler.questionStringTitle[6] = testingQuestion.getQuestionTitle7();
        utilQuestionHandler.questionStringTitle[7] = testingQuestion.getQuestionTitle8();
        utilQuestionHandler.questionStringTitle[8] = testingQuestion.getQuestionTitle9();
        utilQuestionHandler.questionStringTitle[9] = testingQuestion.getQuestionTitle10();
        utilQuestionHandler.isQuestionMultipleChoices = testingQuestion.isMultipleChoice;
        utilQuestionHandler.questionStringAnswer[0] = testingQuestion.getQuestionAnswer1();
        utilQuestionHandler.questionStringAnswer[1] = testingQuestion.getQuestionAnswer2();
        utilQuestionHandler.questionStringAnswer[2] = testingQuestion.getQuestionAnswer3();
        utilQuestionHandler.questionStringAnswer[3] = testingQuestion.getQuestionAnswer4();
        utilQuestionHandler.questionStringAnswer[4] = testingQuestion.getQuestionAnswer5();
        utilQuestionHandler.questionStringAnswer[5] = testingQuestion.getQuestionAnswer6();
        utilQuestionHandler.questionStringAnswer[6] = testingQuestion.getQuestionAnswer7();
        utilQuestionHandler.questionStringAnswer[7] = testingQuestion.getQuestionAnswer8();
        utilQuestionHandler.questionStringAnswer[8] = testingQuestion.getQuestionAnswer9();
        utilQuestionHandler.questionStringAnswer[9] = testingQuestion.getQuestionAnswer10();
        utilQuestionHandler.questionBooleanIsAnswerCorrect[0] = testingQuestion.isQuestionAnswer1Correct;
        utilQuestionHandler.questionBooleanIsAnswerCorrect[1] = testingQuestion.isQuestionAnswer2Correct;
        utilQuestionHandler.questionBooleanIsAnswerCorrect[2] = testingQuestion.isQuestionAnswer3Correct;
        utilQuestionHandler.questionBooleanIsAnswerCorrect[3] = testingQuestion.isQuestionAnswer4Correct;
        utilQuestionHandler.questionBooleanIsAnswerCorrect[4] = testingQuestion.isQuestionAnswer5Correct;
        utilQuestionHandler.questionBooleanIsAnswerCorrect[5] = testingQuestion.isQuestionAnswer6Correct;
        utilQuestionHandler.questionBooleanIsAnswerCorrect[6] = testingQuestion.isQuestionAnswer7Correct;
        utilQuestionHandler.questionBooleanIsAnswerCorrect[7] = testingQuestion.isQuestionAnswer8Correct;
        utilQuestionHandler.questionBooleanIsAnswerCorrect[8] = testingQuestion.isQuestionAnswer9Correct;
        utilQuestionHandler.questionBooleanIsAnswerCorrect[9] = testingQuestion.isQuestionAnswer10Correct;
    }

    private static void assignQuestionDataFromClassToTitleLabelOrImage() {
        for (int i = 0; i < 10; i++) {
            if (Objects.equals(utilQuestionHandler.questionStringTitle[i], "")) {
                break;
            } else if (utilQuestionHandler.questionStringTitle[i].contains("Images\\")) {
                utilQuestionHandler.questionStringTitle[i] = utilQuestionHandler.questionStringTitle[i]
                        .replace("Images\\", "file:///" + utilQuestionHandler.imageFolderAbsolutePath);
                utilQuestionHandler.questionImage[i] = new ImageView();
                utilQuestionHandler.questionImage[i].setImage(new Image(utilQuestionHandler.questionStringTitle[i]));
                checkImageSizeAndResizeIfLongerThanScreenSize(getObjectWidthInScrollPane(), screenHeight / 2
                        , utilQuestionHandler.questionImage[i]);
                utilQuestionHandler.questionObjects[i] = utilQuestionHandler.questionImage[i];
            } else if (utilQuestionHandler.questionStringTitle[i].contains("[TableHeader]")) {
                String[] tableRowData = utilQuestionHandler.questionStringTitle[i].split("(\\[TableRow\\])");
                utilQuestionHandler.questionGridTable[i] = new GridPane();
                utilQuestionHandler.questionGridTable[i].setGridLinesVisible(true);
                renderQuestionGridTable(utilQuestionHandler.questionGridTable[i], tableRowData);
                utilQuestionHandler.questionObjects[i] = utilQuestionHandler.questionGridTable[i];
            } else {
                utilQuestionHandler.questionTitle[i] = new Label(utilQuestionHandler.questionStringTitle[i]);
                utilQuestionHandler.questionTitle[i].setPrefWidth(getObjectWidthInScrollPane());
                utilQuestionHandler.questionTitle[i].setWrapText(true);
                utilQuestionHandler.questionObjects[i] = utilQuestionHandler.questionTitle[i];
            }
        }
    }

    private static void checkImageSizeAndResizeIfLongerThanScreenSize(Double maxWidth, Double maxHeight, ImageView imageViewToCheck) {
        double imageWidth = imageViewToCheck.getImage().getWidth();
        double imageHeight = imageViewToCheck.getImage().getHeight();
        imageViewToCheck.setFitWidth(imageWidth);
        imageViewToCheck.setFitHeight(imageHeight);
        if (imageWidth > maxWidth) {
            imageViewToCheck = resizeBasedOnWidth(imageViewToCheck, maxWidth);
        }
        double imageHeightAfterFirstResize = imageViewToCheck.getFitHeight();
        if (imageHeightAfterFirstResize > maxHeight) {
            imageViewToCheck = resizeBasedOnHeight(imageViewToCheck, maxHeight);
        }
    }

    private static ImageView resizeBasedOnWidth(ImageView imageToCheck, double maxWidth) {
        double imageWidth = imageToCheck.getImage().getWidth();
        double imageHeight = imageToCheck.getImage().getHeight();
        double imageHeightShouldReduce = ((imageWidth - maxWidth) / imageWidth) * imageHeight;
        imageToCheck.setFitWidth(maxWidth);
        imageToCheck.setFitHeight(imageHeight - imageHeightShouldReduce);
        return imageToCheck;
    }

    private static ImageView resizeBasedOnHeight(ImageView imageToCheck, double maxHeight) {
        double imageWidth = imageToCheck.getFitWidth();
        double imageHeight = imageToCheck.getFitHeight();
        double imageWidthShouldReduce = ((imageHeight - maxHeight) / imageHeight) * imageWidth;
        imageToCheck.setFitHeight(maxHeight);
        imageToCheck.setFitWidth(imageWidth - imageWidthShouldReduce);
        return imageToCheck;
    }

    public static void scaleDownImgWithPercentage(ImageView imageToScale, int percentage){
        double imgHeight = imageToScale.getImage().getHeight();
        double imgWidth = imageToScale.getImage().getWidth();
        double percentValue = (double) percentage /100;
        imgHeight = imgHeight*percentValue;
        imgWidth = imgWidth*percentValue;
        imageToScale.setFitHeight(imgHeight);
        imageToScale.setFitWidth(imgWidth);
    }

    private static void renderQuestionGridTable(GridPane gridPane, String[] tableRowData) {
        String[] rowDataStringList;
        for (int rowIndex = 0; rowIndex < tableRowData.length; rowIndex++) {
            tableRowData[rowIndex] = tableRowData[rowIndex].replace("[TableHeader]", "");
            rowDataStringList = tableRowData[rowIndex].split("#");
            renderQuestionGridRow(gridPane, rowDataStringList, rowIndex);
        }
    }

    private static void renderQuestionGridRow(GridPane gridPane, String[] rowDataStringList, int rowIndex) {
        Label[] colLabels = new Label[rowDataStringList.length];
        for (int colIndex = 0; colIndex < rowDataStringList.length; colIndex++) {
            colLabels[colIndex] = new Label(rowDataStringList[colIndex]);
            gridPane.add(colLabels[colIndex], colIndex, rowIndex);
            GridPane.setMargin(colLabels[colIndex], new Insets(5, 5, 5, 5));
        }
    }

    private static VBox addTitleObjectsToVBoxContainer(VBox questionContainer) {
        for (int i = 0; i < 10; i++) {
            if (utilQuestionHandler.questionObjects[i] == null) {
                break;
            } else {
                questionContainer.getChildren().add((Node) utilQuestionHandler.questionObjects[i]);
                VBox.setMargin((Node) utilQuestionHandler.questionObjects[i], new Insets(2, 2, 2, 5));
            }
        }
        return questionContainer;
    }

    public static void assignAnswersDataFromClassToCheckBoxOrRadioButton(int pageIndex) {
        int answerIndex = 0;
        for (int i = 0; i < 5; i++) {
            if (Objects.equals(utilQuestionHandler.questionStringAnswer[answerIndex], "")) {
                break;
            } else if (utilQuestionHandler.isQuestionMultipleChoices) {
                utilQuestionHandler.answerHBoxContainers[i] = new HBox();
                createAnswerCheckBoxElementWithIndexAndAddToHBoxContainer(pageIndex, answerIndex, utilQuestionHandler.answerHBoxContainers[i]);
                answerIndex += 1;
                createAnswerCheckBoxElementWithIndexAndAddToHBoxContainer(pageIndex, answerIndex, utilQuestionHandler.answerHBoxContainers[i]);
                utilQuestionHandler.answerHBoxContainers[i].setAlignment(Pos.CENTER);
                answerIndex += 1;
                coloringBackGroundForHBoxBasedOnIndexOddAndEven(i);
            } else {
                utilQuestionHandler.answerHBoxContainers[i] = new HBox();
                createAnswerRadioButtonElementWithIndexAndAddToHBoxContainer(pageIndex, answerIndex, utilQuestionHandler.answerHBoxContainers[i]);
                answerIndex += 1;
                createAnswerRadioButtonElementWithIndexAndAddToHBoxContainer(pageIndex, answerIndex, utilQuestionHandler.answerHBoxContainers[i]);
                utilQuestionHandler.answerHBoxContainers[i].setAlignment(Pos.CENTER);
                answerIndex += 1;
                coloringBackGroundForHBoxBasedOnIndexOddAndEven(i);
            }
        }
    }

    private static void coloringBackGroundForHBoxBasedOnIndexOddAndEven(int hboxIndex) {
        if (hboxIndex % 2 == 0) {
            utilQuestionHandler.answerHBoxContainers[hboxIndex].setBackground(
                    new Background(new BackgroundFill(Paint.valueOf("#CACACA"), CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            utilQuestionHandler.answerHBoxContainers[hboxIndex].setBackground(
                    new Background(new BackgroundFill(Paint.valueOf("#BFBFBF"), CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    private static HBox createAnswerCheckBoxElementWithIndexAndAddToHBoxContainer(int pageIndex, int elementIndex, HBox container) {
        utilQuestionHandler.answerCheckBoxes[elementIndex] = new CheckBox(utilQuestionHandler.questionStringAnswer[elementIndex]);
        utilQuestionHandler.answerCheckBoxes[elementIndex].setPrefWidth(getCheckBoxWidthInScrollPane());
        utilQuestionHandler.answerCheckBoxes[elementIndex].setWrapText(true);
        if (Objects.equals(utilQuestionHandler.questionStringAnswer[elementIndex], "")) {
            utilQuestionHandler.answerCheckBoxes[elementIndex].setVisible(false);
        }
        if (utilQuestionHandler.answerCheckBoxes[elementIndex].isVisible()) {
            if (getSelectedAnswer()[pageIndex][elementIndex] == 1) {
                utilQuestionHandler.answerCheckBoxes[elementIndex].setSelected(true);
            }
            utilQuestionHandler.answerCheckBoxes[elementIndex].setOnAction(event -> {
                if (utilQuestionHandler.answerCheckBoxes[elementIndex].isSelected()) {
                    setSelectedAnswer(pageIndex, elementIndex, 1);
                } else {
                    setSelectedAnswer(pageIndex, elementIndex, 0);
                }
            });
        }
        container.getChildren().add(utilQuestionHandler.answerCheckBoxes[elementIndex]);
        HBox.setMargin(utilQuestionHandler.answerCheckBoxes[elementIndex], new Insets(10, 10, 10, 10));
        return container;
    }

    private static HBox createAnswerRadioButtonElementWithIndexAndAddToHBoxContainer(int pageIndex, int elementIndex, HBox container) {
        utilQuestionHandler.answerRadioButtons[elementIndex] = new RadioButton(utilQuestionHandler.questionStringAnswer[elementIndex]);
        utilQuestionHandler.answerRadioButtons[elementIndex].setPrefWidth(getCheckBoxWidthInScrollPane());
        utilQuestionHandler.answerRadioButtons[elementIndex].setWrapText(true);
        utilQuestionHandler.answerRadioGroup[pageIndex].getToggles().add(utilQuestionHandler.answerRadioButtons[elementIndex]);
        if (Objects.equals(utilQuestionHandler.questionStringAnswer[elementIndex], "")) {
            utilQuestionHandler.answerRadioButtons[elementIndex].setVisible(false);
        }
        if (utilQuestionHandler.answerRadioButtons[elementIndex].isVisible()) {
            if (getSelectedAnswer()[pageIndex][elementIndex] == 1) {
                utilQuestionHandler.answerRadioButtons[elementIndex].setSelected(true);
            }
            utilQuestionHandler.answerRadioButtons[elementIndex].setOnAction(event -> {
                setSelectedAnswerElementArray(pageIndex,
                        Arrays.stream(utilQuestionHandler.selectedAnswer[pageIndex]).map(e -> e = 0).toArray());
                if (utilQuestionHandler.answerRadioButtons[elementIndex].isSelected()) {
                    setSelectedAnswer(pageIndex, elementIndex, 1);
                }
            });
        }
        container.getChildren().add(utilQuestionHandler.answerRadioButtons[elementIndex]);
        HBox.setMargin(utilQuestionHandler.answerRadioButtons[elementIndex], new Insets(10, 10, 10, 10));
        return container;
    }

    private static VBox addAnswerHBoxObjectsToVBoxContainer(VBox questionContainer) {
        for (int i = 0; i < 5; i++) {
            if (utilQuestionHandler.answerHBoxContainers[i] == null) {
                break;
            } else {
                utilQuestionHandler.answerHBoxContainers[i].setMaxWidth(getObjectWidthInScrollPane());
                utilQuestionHandler.answerHBoxContainers[i].setPrefWidth(getObjectWidthInScrollPane());
                questionContainer.getChildren().add(utilQuestionHandler.answerHBoxContainers[i]);
            }
        }
        return questionContainer;
    }

    public static String getTodayDate() {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Format the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return formatter.format(now);
    }

    public static String getTodayDateTime() {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Format the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM_dd_yyyy_HH_mm_ss");
        return formatter.format(now);
    }

    public static void disableAllAnswersInHBoxContainer() {
        try {
            Arrays.stream(answerHBoxContainers).forEach(hBox -> hBox.setDisable(true));
        } catch (Exception ignored) {
        }
    }

    public static void writeDataToTextFile(File textFile, String valueToWrite) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(textFile));
        writer.write(valueToWrite);
        writer.close();
    }

    public static String[] readAndDeryptPasswordAndFileNameDataFromText(File fileToRead) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        String valueToRead = "";
        try {
            Scanner scanner = new Scanner(fileToRead);
            while (scanner.hasNextLine()) {
                valueToRead = scanner.nextLine();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        valueToRead = EncryptDecryptBased64.decryptedBase64TextWithSecretKey(valueToRead, getEncryptDecryptKey());
        String[] splitedValue = valueToRead.split("(?:\\|)");
        String[] readValue = new String[2];
        readValue[0] = splitedValue[1].substring(splitedValue[1].indexOf(":") + 1, splitedValue[1].length()).trim();
        readValue[1] = splitedValue[2].substring(splitedValue[2].indexOf(":") + 1, splitedValue[2].length()).trim();
        return readValue;
    }
}
