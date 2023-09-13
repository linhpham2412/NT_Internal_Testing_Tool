package nt.testingtool.istqb.Utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static nt.testingtool.istqb.Utils.PageVBoxHandler.*;
import static nt.testingtool.istqb.Utils.ProjectConfiguration.*;
import static nt.testingtool.istqb.Utils.QuestionHandler.*;

public class TestingToolUtils {

    static QuestionHandler utilQuestionHandler;
    static int currentPageIndex = 0;
    static boolean isTestingEnd = false;
    static File loadedImageFile = null;
    static String[] generatedTableContent = new String[0];

    public static int getCurrentPageIndex() {
        return currentPageIndex;
    }

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
        initQuestionElements(pageIndex);
        //map question data to local variables
        mapValueFromTestingQuestionToLocalVariablesByPageIndex(pageIndex);
        //Add more question components inside vbox
        Label questionNumber = new Label("");
        if (isQuestionDesign) {
            questionNumber.setText("Question " + getCurrentQuestionPreviewIndex() + ":");
        } else {
            questionNumber.setText("Question " + (pageIndex + 1) + ":");
        }
//        questionNumber = new Label("Question " + (pageIndex + 1) + ":");
        assignQuestionDataFromClassToTitleLabelOrImage();
        String kindOfChoice = (isQuestionMultipleChoices()) ? "[Multi Choice]" : "[Single choice]";
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
        QuestionDataModel testingQuestion = new QuestionDataModel();
        if (isQuestionDesign && !isQuestionTempCheck) {
            testingQuestion = utilQuestionHandler.getquestionDataModels()[PageVBoxHandler.questionIndex];
        } else if (isQuestionTempCheck) {
            testingQuestion = getTemporaryChangeQuestion();
        } else {
            testingQuestion = testingQuestions[questionIndex];
        }
        questionStringTitle[0] = testingQuestion.getQuestionTitle1();
        questionStringTitle[1] = testingQuestion.getQuestionTitle2();
        questionStringTitle[2] = testingQuestion.getQuestionTitle3();
        questionStringTitle[3] = testingQuestion.getQuestionTitle4();
        questionStringTitle[4] = testingQuestion.getQuestionTitle5();
        questionStringTitle[5] = testingQuestion.getQuestionTitle6();
        questionStringTitle[6] = testingQuestion.getQuestionTitle7();
        questionStringTitle[7] = testingQuestion.getQuestionTitle8();
        questionStringTitle[8] = testingQuestion.getQuestionTitle9();
        questionStringTitle[9] = testingQuestion.getQuestionTitle10();
        isQuestionMultipleChoices = testingQuestion.isMultipleChoice;
        questionStringAnswer[0] = testingQuestion.getQuestionAnswer1();
        questionStringAnswer[1] = testingQuestion.getQuestionAnswer2();
        questionStringAnswer[2] = testingQuestion.getQuestionAnswer3();
        questionStringAnswer[3] = testingQuestion.getQuestionAnswer4();
        questionStringAnswer[4] = testingQuestion.getQuestionAnswer5();
        questionStringAnswer[5] = testingQuestion.getQuestionAnswer6();
        questionStringAnswer[6] = testingQuestion.getQuestionAnswer7();
        questionStringAnswer[7] = testingQuestion.getQuestionAnswer8();
        questionStringAnswer[8] = testingQuestion.getQuestionAnswer9();
        questionStringAnswer[9] = testingQuestion.getQuestionAnswer10();
        questionBooleanIsAnswerCorrect[0] = testingQuestion.isQuestionAnswer1Correct;
        questionBooleanIsAnswerCorrect[1] = testingQuestion.isQuestionAnswer2Correct;
        questionBooleanIsAnswerCorrect[2] = testingQuestion.isQuestionAnswer3Correct;
        questionBooleanIsAnswerCorrect[3] = testingQuestion.isQuestionAnswer4Correct;
        questionBooleanIsAnswerCorrect[4] = testingQuestion.isQuestionAnswer5Correct;
        questionBooleanIsAnswerCorrect[5] = testingQuestion.isQuestionAnswer6Correct;
        questionBooleanIsAnswerCorrect[6] = testingQuestion.isQuestionAnswer7Correct;
        questionBooleanIsAnswerCorrect[7] = testingQuestion.isQuestionAnswer8Correct;
        questionBooleanIsAnswerCorrect[8] = testingQuestion.isQuestionAnswer9Correct;
        questionBooleanIsAnswerCorrect[9] = testingQuestion.isQuestionAnswer10Correct;
    }

    private static void assignQuestionDataFromClassToTitleLabelOrImage() {
        String fileHeaderSymbol = "file:///";
        for (int i = 0; i < 10; i++) {
            if (Objects.equals(questionStringTitle[i], "") || questionStringTitle[i] == null) {
                break;
            } else if (questionStringTitle[i].contains("Images\\")) {
                questionStringTitle[i] = questionStringTitle[i]
                        .replace("Images\\", fileHeaderSymbol + imageFolderAbsolutePath);
                questionImage[i] = new ImageView();
                questionImage[i].setImage(new Image(questionStringTitle[i]));
                checkImageSizeAndResizeIfLongerThanScreenSize(getObjectWidthInScrollPane(), screenHeight / 2
                        , questionImage[i]);
                questionObjects[i] = questionImage[i];
            } else if (questionStringTitle[i].contains("[TableHeader]")) {
                String[] tableRowData = questionStringTitle[i].split("(\\[TableRow\\])");
                questionGridTable[i] = new GridPane();
                questionGridTable[i].setGridLinesVisible(true);
                renderQuestionGridTable(questionGridTable[i], tableRowData);
                questionObjects[i] = questionGridTable[i];
            } else if (questionStringTitle[i].contains("<InsertingImage>")) {
                questionStringTitle[i] = questionStringTitle[i].replace("<InsertingImage>", fileHeaderSymbol);
                questionStringTitle[i] = questionStringTitle[i].substring(0, questionStringTitle[i].indexOf("=")).trim();
                questionImage[i] = new ImageView();
                questionImage[i].setImage(new Image(questionStringTitle[i]));
                checkImageSizeAndResizeIfLongerThanScreenSize(getObjectWidthInScrollPane(), screenHeight / 2
                        , questionImage[i]);
                questionObjects[i] = questionImage[i];
            }else if (questionStringTitle[i].contains("<CodeBlock>")){
                String codeSB = questionStringTitle[i].replace("<CodeBlock>","\t")+"\n\n";
                codeSB = codeSB.replace("\r\n","\t\r\n\t");
                Label codeContentLabel = new Label(codeSB);
                codeContentLabel.setStyle(cssWhiteColorFontValue);
                codeContentLabel.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));
                questionObjects[i] = codeContentLabel;
            }else {
                questionTitle[i] = new Label(questionStringTitle[i]);
                questionTitle[i].setPrefWidth(getObjectWidthInScrollPane());
                questionTitle[i].setWrapText(true);
                questionObjects[i] = questionTitle[i];
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

    public static void scaleDownImgWithPercentage(ImageView imageToScale, int percentage) {
        double imgHeight = imageToScale.getImage().getHeight();
        double imgWidth = imageToScale.getImage().getWidth();
        double percentValue = (double) percentage / 100;
        imgHeight = imgHeight * percentValue;
        imgWidth = imgWidth * percentValue;
        imageToScale.setFitHeight(imgHeight);
        imageToScale.setFitWidth(imgWidth);
    }

    public static GridPane renderGridPaneWithDataGridTable(GridPane dataGridPane) {
        int maxRow = dataGridPane.getRowCount();
        int maxCol = dataGridPane.getColumnCount() - 1;
        String[] tableRowData = new String[maxRow];
        generatedTableContent = new String[maxRow];
        String headerText = "[TableHeader]";
        String rowText = "[TableRow]";
        int startColData = 1;
        int endColData = 0;
        for (int row = 0; row < maxRow; row++) {
            StringBuilder rowDataSB = new StringBuilder();
            endColData = startColData + maxCol;
            List<Node> rawRowData = dataGridPane.getChildren().subList(startColData, endColData);
            rawRowData.forEach(node -> {
                try {
                    TextField workingTF = (TextField) node;
                    rowDataSB.append("#").append(workingTF.getText());
                } catch (ClassCastException ignored) {
                }
            });
            String rowResult = String.valueOf(rowDataSB).substring(1);
            if (row == 0) {
                tableRowData[row] = headerText + rowResult;
                generatedTableContent[row] = headerText + rowResult;
            } else {
                tableRowData[row] = rowResult;
                generatedTableContent[row] = rowText + rowResult;
            }
            startColData += maxCol + 1;
        }
        GridPane previewGridPage = new GridPane();
        renderQuestionGridTable(previewGridPage, tableRowData);
        return previewGridPage;
    }

    public static String getGeneratedTableContent() {
        previewTable.fire();
        StringBuilder generateTableSB = new StringBuilder();
        for (int i = 0; i < generatedTableContent.length; i++) {
            generateTableSB.append(generatedTableContent[i]);
        }
        return String.valueOf(generateTableSB);
    }


    public static void renderQuestionGridTable(GridPane gridPane, String[] tableRowData) {
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
            if (questionObjects[i] == null) {
                break;
            } else {
                questionContainer.getChildren().add((Node) questionObjects[i]);
                VBox.setMargin((Node) questionObjects[i], new Insets(2, 2, 2, 5));
            }
        }
        return questionContainer;
    }

    public static void assignAnswersDataFromClassToCheckBoxOrRadioButton(int pageIndex) {
        int answerIndex = 0;
        for (int i = 0; i < 5; i++) {
            if ((Objects.equals(questionStringAnswer[answerIndex], "")) || questionStringAnswer[answerIndex] == null) {
                break;
            } else if (isQuestionMultipleChoices) {
                answerHBoxContainers[i] = new HBox();
                createAnswerCheckBoxElementWithIndexAndAddToHBoxContainer(pageIndex, answerIndex, answerHBoxContainers[i]);
                answerIndex += 1;
                createAnswerCheckBoxElementWithIndexAndAddToHBoxContainer(pageIndex, answerIndex, answerHBoxContainers[i]);
                answerHBoxContainers[i].setAlignment(Pos.CENTER);
                answerIndex += 1;
                coloringBackGroundForHBoxBasedOnIndexOddAndEven(i);
            } else {
                answerHBoxContainers[i] = new HBox();
                createAnswerRadioButtonElementWithIndexAndAddToHBoxContainer(pageIndex, answerIndex, answerHBoxContainers[i]);
                answerIndex += 1;
                createAnswerRadioButtonElementWithIndexAndAddToHBoxContainer(pageIndex, answerIndex, answerHBoxContainers[i]);
                answerHBoxContainers[i].setAlignment(Pos.CENTER);
                answerIndex += 1;
                coloringBackGroundForHBoxBasedOnIndexOddAndEven(i);
            }
        }
    }

    private static void coloringBackGroundForHBoxBasedOnIndexOddAndEven(int hboxIndex) {
        if (hboxIndex % 2 == 0) {
            answerHBoxContainers[hboxIndex].setBackground(
                    new Background(new BackgroundFill(Paint.valueOf("#CACACA"), CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            answerHBoxContainers[hboxIndex].setBackground(
                    new Background(new BackgroundFill(Paint.valueOf("#BFBFBF"), CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    private static HBox createAnswerCheckBoxElementWithIndexAndAddToHBoxContainer(int pageIndex, int elementIndex, HBox container) {
        answerCheckBoxes[elementIndex] = new CheckBox(questionStringAnswer[elementIndex]);
        answerCheckBoxes[elementIndex].setPrefWidth(getCheckBoxWidthInScrollPane());
        answerCheckBoxes[elementIndex].setWrapText(true);
        if (Objects.equals(questionStringAnswer[elementIndex], "")) {
            answerCheckBoxes[elementIndex].setVisible(false);
        }
        if (answerCheckBoxes[elementIndex].isVisible()) {
            if (!isQuestionDesign) {
                if (getSelectedAnswer()[pageIndex][elementIndex] == 1) {
                    answerCheckBoxes[elementIndex].setSelected(true);
                }
            }
            //update green background if it is correct answer in review
            if (isReviewAnswers) {
                if (isQuestionDesign) {
                    if (questionBooleanIsAnswerCorrect[elementIndex]) {
                        answerCheckBoxes[elementIndex].setStyle(cssGreenColorBGValue);
                    }
                } else {
                    if (getCorrectAnswer()[pageIndex][elementIndex] == 1) {
                        answerCheckBoxes[elementIndex].setStyle(cssGreenColorBGValue);
                    }
                }
            }
            answerCheckBoxes[elementIndex].setOnAction(event -> {
                if (answerCheckBoxes[elementIndex].isSelected()) {
                    setSelectedAnswer(pageIndex, elementIndex, 1);
                } else {
                    setSelectedAnswer(pageIndex, elementIndex, 0);
                }
            });
        }
        container.getChildren().add(answerCheckBoxes[elementIndex]);
        HBox.setMargin(answerCheckBoxes[elementIndex], new Insets(10, 10, 10, 10));
        return container;
    }

    private static HBox createAnswerRadioButtonElementWithIndexAndAddToHBoxContainer(int pageIndex, int elementIndex, HBox container) {
        answerRadioButtons[elementIndex] = new RadioButton(questionStringAnswer[elementIndex]);
        answerRadioButtons[elementIndex].setPrefWidth(getCheckBoxWidthInScrollPane());
        answerRadioButtons[elementIndex].setWrapText(true);
        answerRadioGroup[pageIndex].getToggles().add(answerRadioButtons[elementIndex]);
        if (Objects.equals(questionStringAnswer[elementIndex], "")) {
            answerRadioButtons[elementIndex].setVisible(false);
        }
        if (answerRadioButtons[elementIndex].isVisible()) {
            if (!isQuestionDesign) {
                if (getSelectedAnswer()[pageIndex][elementIndex] == 1) {
                    answerRadioButtons[elementIndex].setSelected(true);
                }
            }
            //update green background if it is correct answer in review
            if (isReviewAnswers) {
                if (isQuestionDesign) {
                    if (questionBooleanIsAnswerCorrect[elementIndex]) {
                        answerRadioButtons[elementIndex].setStyle(cssGreenColorBGValue);
                    }
                } else {
                    if (getCorrectAnswer()[pageIndex][elementIndex] == 1) {
                        answerRadioButtons[elementIndex].setStyle(cssGreenColorBGValue);
                    }
                }
            }
            answerRadioButtons[elementIndex].setOnAction(event -> {
                setSelectedAnswerElementArray(pageIndex,
                        Arrays.stream(selectedAnswer[pageIndex]).map(e -> e = 0).toArray());
                if (answerRadioButtons[elementIndex].isSelected()) {
                    setSelectedAnswer(pageIndex, elementIndex, 1);
                }
            });
        }
        container.getChildren().add(answerRadioButtons[elementIndex]);
        HBox.setMargin(answerRadioButtons[elementIndex], new Insets(10, 10, 10, 10));
        return container;
    }

    private static VBox addAnswerHBoxObjectsToVBoxContainer(VBox questionContainer) {
        for (int i = 0; i < 5; i++) {
            if (answerHBoxContainers[i] == null) {
                break;
            } else {
                answerHBoxContainers[i].setMaxWidth(getObjectWidthInScrollPane());
                answerHBoxContainers[i].setPrefWidth(getObjectWidthInScrollPane());
                questionContainer.getChildren().add(answerHBoxContainers[i]);
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

    public static String[] readAndDecryptPasswordAndFileNameDataFromText(File fileToRead) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
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
        readValue[0] = splitedValue[1].substring(splitedValue[1].indexOf(":") + 1).trim();
        readValue[1] = splitedValue[2].substring(splitedValue[2].indexOf(":") + 1).trim();
        return readValue;
    }
}
