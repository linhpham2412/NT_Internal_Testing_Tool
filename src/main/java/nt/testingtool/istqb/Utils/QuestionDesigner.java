package nt.testingtool.istqb.Utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import nt.testingtool.istqb.datamodel.QuestionDataModel;

import java.util.List;

import static nt.testingtool.istqb.Utils.ProjectConfiguration.*;

public class QuestionDesigner {
    public Label getQuestionBankFileName() {
        return questionBankFileName;
    }

    Label questionBankFileName = new Label("file name");
    TextField questionIndexTextField = new TextField();

    public Button getOpenFileButton() {
        return openFileButton;
    }

    Button openFileButton = new Button("Open File");

    public Button getPreviewQuestion() {
        return previewQuestion;
    }

    Button previewQuestion = new Button("Preview");

    public Button getPrevQuestion() {
        return prevQuestion;
    }

    public Button getNextQuestion() {
        return nextQuestion;
    }

    public Button getFirstQuestion() {
        return firstQuestion;
    }

    public Button getLastQuestion() {
        return lastQuestion;
    }

    public Button getAddQuestion() {
        return addQuestion;
    }

    Button prevQuestion = new Button("<");
    Button nextQuestion = new Button(">");
    Button firstQuestion = new Button("|<");
    Button lastQuestion = new Button(">|");
    Button addQuestion = new Button("+");
    TextField selectedGroupName = new TextField();
    TextArea textArea1 = new TextArea();
    TextArea textArea2 = new TextArea();
    TextArea textArea3 = new TextArea();
    TextArea textArea4 = new TextArea();
    TextArea textArea5 = new TextArea();
    TextArea textArea6 = new TextArea();
    TextArea textArea7 = new TextArea();
    TextArea textArea8 = new TextArea();
    TextArea textArea9 = new TextArea();
    TextArea textArea10 = new TextArea();
    CheckBox isMultiAnswersCheckbox = new CheckBox("Is Multiple Answers");
    CheckBox isAnswer1Correct = new CheckBox("Is Correct");
    TextArea textAreaAnswer1 = new TextArea();
    CheckBox isAnswer2Correct = new CheckBox("Is Correct");
    TextArea textAreaAnswer2 = new TextArea();
    CheckBox isAnswer3Correct = new CheckBox("Is Correct");
    TextArea textAreaAnswer3 = new TextArea();
    CheckBox isAnswer4Correct = new CheckBox("Is Correct");
    TextArea textAreaAnswer4 = new TextArea();
    CheckBox isAnswer5Correct = new CheckBox("Is Correct");
    TextArea textAreaAnswer5 = new TextArea();
    CheckBox isAnswer6Correct = new CheckBox("Is Correct");
    TextArea textAreaAnswer6 = new TextArea();
    CheckBox isAnswer7Correct = new CheckBox("Is Correct");
    TextArea textAreaAnswer7 = new TextArea();
    CheckBox isAnswer8Correct = new CheckBox("Is Correct");
    TextArea textAreaAnswer8 = new TextArea();
    CheckBox isAnswer9Correct = new CheckBox("Is Correct");
    TextArea textAreaAnswer9 = new TextArea();
    CheckBox isAnswer10Correct = new CheckBox("Is Correct");
    TextArea textAreaAnswer10 = new TextArea();

    public VBox generateElements() {
        Pane questionDesignerPaneHeader = new Pane();
        questionDesignerPaneHeader.setPrefHeight(screenHeight / 16);
        questionDesignerPaneHeader.setBackground(grayBackGround);

        HBox qdHeaderPane = new HBox();
        HBox qdLeftHeader = new HBox();
        qdLeftHeader.setAlignment(Pos.CENTER_LEFT);
        qdLeftHeader.setMinWidth(screenWidth / 1.1);
        qdLeftHeader.setMaxWidth(screenWidth / 1.1);
        HBox qdRightHeader = new HBox();
        qdRightHeader.setAlignment(Pos.CENTER_RIGHT);
        qdHeaderPane.getChildren().add(qdLeftHeader);
        qdHeaderPane.getChildren().add(qdRightHeader);

        Label selectFile = new Label("Question File: ");
        selectFile.setFont(toolFont);
        HBox.setMargin(selectFile, new Insets(10, 10, 10, 10));
//        Button openFile = new Button("Open File");
        openFileButton.setFont(toolFont);
        questionBankFileName = new Label("file name");
        questionBankFileName.setFont(toolFont);
        HBox.setMargin(questionBankFileName, new Insets(10, 10, 10, 10));
        qdLeftHeader.getChildren().add(selectFile);
        qdLeftHeader.getChildren().add(openFileButton);
        qdLeftHeader.getChildren().add(questionBankFileName);
//        Button previewQuestion = new Button("Preview");
        previewQuestion.setFont(toolFont);
        previewQuestion.setStyle(cssGreenColorBGValue);
        qdRightHeader.getChildren().add(previewQuestion);
        //Command pane
        HBox commandPane = new HBox();
        commandPane.setPrefHeight(screenHeight / 16);
        commandPane.setTranslateY(screenHeight / 16);

        HBox leftCommandPane = new HBox();
        leftCommandPane.setPrefWidth(screenWidth / 1.5);
        Label commandText = new Label("Command buttons will be placed here");
        commandText.setFont(toolFont);
        HBox.setMargin(commandText, new Insets(10, 10, 10, 10));
        leftCommandPane.getChildren().add(commandText);

        HBox rightCommandPane = new HBox();
        rightCommandPane.setPrefWidth(screenWidth / 3);
//        Button prevQuestion = new Button("<");
//        Button nextQuestion = new Button(">");
//        Button firstQuestion = new Button("|<");
//        Button lastQuestion = new Button(">|");
//        Button addQuestion = new Button("+");
//        TextField questionIndex = new TextField();
        questionIndexTextField.setFont(toolFont);
        questionIndexTextField.setPrefWidth(screenWidth / 8);
        questionIndexTextField.setAlignment(Pos.CENTER);
        prevQuestion.setFont(toolFont);
        nextQuestion.setFont(toolFont);
        firstQuestion.setFont(toolFont);
        lastQuestion.setFont(toolFont);
        addQuestion.setFont(toolFont);
        HBox.setMargin(addQuestion, new Insets(0, 0, 0, 10));
        rightCommandPane.getChildren().add(firstQuestion);
        rightCommandPane.getChildren().add(prevQuestion);
        rightCommandPane.getChildren().add(questionIndexTextField);
        rightCommandPane.getChildren().add(nextQuestion);
        rightCommandPane.getChildren().add(lastQuestion);
        rightCommandPane.getChildren().add(addQuestion);

        commandPane.getChildren().add(leftCommandPane);
        commandPane.getChildren().add(rightCommandPane);


        questionDesignerPaneHeader.getChildren().add(qdHeaderPane);
        questionDesignerPaneHeader.getChildren().add(commandPane);

        //DesignPane
        HBox designPane = new HBox();
        designPane.setPrefHeight(screenHeight / 1.1);
        designPane.setTranslateY(screenHeight / 14);
        designPane.setBackground(grayBackGround);

        ScrollPane designerScrollPane = new ScrollPane();
        designerScrollPane.setPrefWidth(screenWidth);
        HBox.setMargin(designerScrollPane, new Insets(10, 10, 10, 10));

        designPane.getChildren().add(designerScrollPane);

        //Scroll pane value
        VBox scrollPaneContent = new VBox();

        HBox titleGroupHBox = new HBox();
        Label titleGroupLabel = new Label("Group:");
        titleGroupLabel.setFont(toolFont);
//        TextField selectedGroupName = new TextField();
        selectedGroupName.setFont(toolFont);
        selectedGroupName.setPrefSize(screenWidth / 2, 50);
        ComboBox checkBoxGroup = new ComboBox();
        checkBoxGroup.setPrefSize(screenWidth / 2.4, 50);
        titleGroupHBox.getChildren().add(titleGroupLabel);
        titleGroupHBox.getChildren().add(selectedGroupName);
        titleGroupHBox.getChildren().add(checkBoxGroup);

        HBox questionBox = new HBox();
        Label questionLabel = new Label("Question area:");
        questionLabel.setFont(toolFont);
        questionBox.getChildren().add(questionLabel);

        HBox titleHBox1 = new HBox();
        Label titleLabel1 = new Label("Title 01:");
        titleLabel1.setFont(toolFont);
//        TextArea textArea1 = new TextArea();
        textArea1.setFont(toolFont);
        textArea1.setWrapText(true);
        textArea1.setPrefSize(screenWidth / 1.1, screenHeight / 4);
        titleHBox1.getChildren().add(titleLabel1);
        titleHBox1.getChildren().add(textArea1);

        HBox titleHBox2 = new HBox();
        Label titleLabel2 = new Label("Title 02:");
        titleLabel2.setFont(toolFont);
//        TextArea textArea2 = new TextArea();
        textArea2.setFont(toolFont);
        textArea2.setWrapText(true);
        textArea2.setPrefSize(screenWidth / 1.1, screenHeight / 4);
        titleHBox2.getChildren().add(titleLabel2);
        titleHBox2.getChildren().add(textArea2);

        HBox titleHBox3 = new HBox();
        Label titleLabel3 = new Label("Title 03:");
        titleLabel3.setFont(toolFont);
//        TextArea textArea3 = new TextArea();
        textArea3.setFont(toolFont);
        textArea3.setWrapText(true);
        textArea3.setPrefSize(screenWidth / 1.1, screenHeight / 4);
        titleHBox3.getChildren().add(titleLabel3);
        titleHBox3.getChildren().add(textArea3);

        HBox titleHBox4 = new HBox();
        Label titleLabel4 = new Label("Title 04:");
        titleLabel4.setFont(toolFont);
//        TextArea textArea4 = new TextArea();
        textArea4.setFont(toolFont);
        textArea4.setWrapText(true);
        textArea4.setPrefSize(screenWidth / 1.1, screenHeight / 4);
        titleHBox4.getChildren().add(titleLabel4);
        titleHBox4.getChildren().add(textArea4);

        HBox titleHBox5 = new HBox();
        Label titleLabel5 = new Label("Title 05:");
        titleLabel5.setFont(toolFont);
//        TextArea textArea5 = new TextArea();
        textArea5.setFont(toolFont);
        textArea5.setWrapText(true);
        textArea5.setPrefSize(screenWidth / 1.1, screenHeight / 4);
        titleHBox5.getChildren().add(titleLabel5);
        titleHBox5.getChildren().add(textArea5);

        HBox titleHBox6 = new HBox();
        Label titleLabel6 = new Label("Title 06:");
        titleLabel6.setFont(toolFont);
//        TextArea textArea6 = new TextArea();
        textArea6.setFont(toolFont);
        textArea6.setWrapText(true);
        textArea6.setPrefSize(screenWidth / 1.1, screenHeight / 4);
        titleHBox6.getChildren().add(titleLabel6);
        titleHBox6.getChildren().add(textArea6);

        HBox titleHBox7 = new HBox();
        Label titleLabel7 = new Label("Title 07:");
        titleLabel7.setFont(toolFont);
//        TextArea textArea7 = new TextArea();
        textArea7.setFont(toolFont);
        textArea7.setWrapText(true);
        textArea7.setPrefSize(screenWidth / 1.1, screenHeight / 4);
        titleHBox7.getChildren().add(titleLabel7);
        titleHBox7.getChildren().add(textArea7);

        HBox titleHBox8 = new HBox();
        Label titleLabel8 = new Label("Title 08:");
        titleLabel8.setFont(toolFont);
//        TextArea textArea8 = new TextArea();
        textArea8.setFont(toolFont);
        textArea8.setWrapText(true);
        textArea8.setPrefSize(screenWidth / 1.1, screenHeight / 4);
        titleHBox8.getChildren().add(titleLabel8);
        titleHBox8.getChildren().add(textArea8);

        HBox titleHBox9 = new HBox();
        Label titleLabel9 = new Label("Title 09:");
        titleLabel9.setFont(toolFont);
//        TextArea textArea9 = new TextArea();
        textArea9.setFont(toolFont);
        textArea9.setWrapText(true);
        textArea9.setPrefSize(screenWidth / 1.1, screenHeight / 4);
        titleHBox9.getChildren().add(titleLabel9);
        titleHBox9.getChildren().add(textArea9);

        HBox titleHBox10 = new HBox();
        Label titleLabel10 = new Label("Title 10:");
        titleLabel10.setFont(toolFont);
//        TextArea textArea10 = new TextArea();
        textArea10.setFont(toolFont);
        textArea10.setWrapText(true);
        textArea10.setPrefSize(screenWidth / 1.1, screenHeight / 4);
        titleHBox10.getChildren().add(titleLabel10);
        titleHBox10.getChildren().add(textArea10);

        HBox answerBox = new HBox();
        Label answerLabel = new Label("Answer area:");
        answerLabel.setFont(toolFont);
//        CheckBox isMultiAnswersCheckbox = new CheckBox("Is Multiple Answers");
        isMultiAnswersCheckbox.setFont(toolFont);

        answerBox.getChildren().add(answerLabel);
        answerBox.getChildren().add(isMultiAnswersCheckbox);

        HBox answerHBox1 = new HBox();
        Label answerLabel1 = new Label("Answer 01:");
        answerLabel1.setFont(toolFont);
//        CheckBox isAnswer1Correct = new CheckBox("Is Correct");
        isAnswer1Correct.setFont(toolFont);
//        TextArea textAreaAnswer1 = new TextArea();
        textAreaAnswer1.setFont(toolFont);
        textAreaAnswer1.setWrapText(true);
        textAreaAnswer1.setPrefSize(screenWidth / 1.5, screenHeight / 4);
        answerHBox1.getChildren().add(answerLabel1);
        answerHBox1.getChildren().add(isAnswer1Correct);
        answerHBox1.getChildren().add(textAreaAnswer1);

        HBox answerHBox2 = new HBox();
        Label answerLabel2 = new Label("Answer 02:");
        answerLabel2.setFont(toolFont);
//        CheckBox isAnswer2Correct = new CheckBox("Is Correct");
        isAnswer2Correct.setFont(toolFont);
//        TextArea textAreaAnswer2 = new TextArea();
        textAreaAnswer2.setFont(toolFont);
        textAreaAnswer2.setWrapText(true);
        textAreaAnswer2.setPrefSize(screenWidth / 1.5, screenHeight / 4);
        answerHBox2.getChildren().add(answerLabel2);
        answerHBox2.getChildren().add(isAnswer2Correct);
        answerHBox2.getChildren().add(textAreaAnswer2);

        HBox answerHBox3 = new HBox();
        Label answerLabel3 = new Label("Answer 03:");
        answerLabel3.setFont(toolFont);
//        CheckBox isAnswer3Correct = new CheckBox("Is Correct");
        isAnswer3Correct.setFont(toolFont);
//        TextArea textAreaAnswer3 = new TextArea();
        textAreaAnswer3.setFont(toolFont);
        textAreaAnswer3.setWrapText(true);
        textAreaAnswer3.setPrefSize(screenWidth / 1.5, screenHeight / 4);
        answerHBox3.getChildren().add(answerLabel3);
        answerHBox3.getChildren().add(isAnswer3Correct);
        answerHBox3.getChildren().add(textAreaAnswer3);

        HBox answerHBox4 = new HBox();
        Label answerLabel4 = new Label("Answer 04:");
        answerLabel4.setFont(toolFont);
//        CheckBox isAnswer4Correct = new CheckBox("Is Correct");
        isAnswer4Correct.setFont(toolFont);
//        TextArea textAreaAnswer4 = new TextArea();
        textAreaAnswer4.setFont(toolFont);
        textAreaAnswer4.setWrapText(true);
        textAreaAnswer4.setPrefSize(screenWidth / 1.5, screenHeight / 4);
        answerHBox4.getChildren().add(answerLabel4);
        answerHBox4.getChildren().add(isAnswer4Correct);
        answerHBox4.getChildren().add(textAreaAnswer4);

        HBox answerHBox5 = new HBox();
        Label answerLabel5 = new Label("Answer 05:");
        answerLabel5.setFont(toolFont);
//        CheckBox isAnswer5Correct = new CheckBox("Is Correct");
        isAnswer5Correct.setFont(toolFont);
//        TextArea textAreaAnswer5 = new TextArea();
        textAreaAnswer5.setFont(toolFont);
        textAreaAnswer5.setWrapText(true);
        textAreaAnswer5.setPrefSize(screenWidth / 1.5, screenHeight / 4);
        answerHBox5.getChildren().add(answerLabel5);
        answerHBox5.getChildren().add(isAnswer5Correct);
        answerHBox5.getChildren().add(textAreaAnswer5);

        HBox answerHBox6 = new HBox();
        Label answerLabel6 = new Label("Answer 06:");
        answerLabel6.setFont(toolFont);
//        CheckBox isAnswer6Correct = new CheckBox("Is Correct");
        isAnswer6Correct.setFont(toolFont);
//        TextArea textAreaAnswer6 = new TextArea();
        textAreaAnswer6.setFont(toolFont);
        textAreaAnswer6.setWrapText(true);
        textAreaAnswer6.setPrefSize(screenWidth / 1.5, screenHeight / 4);
        answerHBox6.getChildren().add(answerLabel6);
        answerHBox6.getChildren().add(isAnswer6Correct);
        answerHBox6.getChildren().add(textAreaAnswer6);

        HBox answerHBox7 = new HBox();
        Label answerLabel7 = new Label("Answer 07:");
        answerLabel7.setFont(toolFont);
//        CheckBox isAnswer7Correct = new CheckBox("Is Correct");
        isAnswer7Correct.setFont(toolFont);
//        TextArea textAreaAnswer7 = new TextArea();
        textAreaAnswer7.setFont(toolFont);
        textAreaAnswer7.setWrapText(true);
        textAreaAnswer7.setPrefSize(screenWidth / 1.5, screenHeight / 4);
        answerHBox7.getChildren().add(answerLabel7);
        answerHBox7.getChildren().add(isAnswer7Correct);
        answerHBox7.getChildren().add(textAreaAnswer7);

        HBox answerHBox8 = new HBox();
        Label answerLabel8 = new Label("Answer 08:");
        answerLabel8.setFont(toolFont);
//        CheckBox isAnswer8Correct = new CheckBox("Is Correct");
        isAnswer8Correct.setFont(toolFont);
//        TextArea textAreaAnswer8 = new TextArea();
        textAreaAnswer8.setFont(toolFont);
        textAreaAnswer8.setWrapText(true);
        textAreaAnswer8.setPrefSize(screenWidth / 1.5, screenHeight / 4);
        answerHBox8.getChildren().add(answerLabel8);
        answerHBox8.getChildren().add(isAnswer8Correct);
        answerHBox8.getChildren().add(textAreaAnswer8);

        HBox answerHBox9 = new HBox();
        Label answerLabel9 = new Label("Answer 09:");
        answerLabel9.setFont(toolFont);
//        CheckBox isAnswer9Correct = new CheckBox("Is Correct");
        isAnswer9Correct.setFont(toolFont);
//        TextArea textAreaAnswer9 = new TextArea();
        textAreaAnswer9.setFont(toolFont);
        textAreaAnswer9.setWrapText(true);
        textAreaAnswer9.setPrefSize(screenWidth / 1.5, screenHeight / 4);
        answerHBox9.getChildren().add(answerLabel9);
        answerHBox9.getChildren().add(isAnswer9Correct);
        answerHBox9.getChildren().add(textAreaAnswer9);

        HBox answerHBox10 = new HBox();
        Label answerLabel10 = new Label("Answer 10:");
        answerLabel10.setFont(toolFont);
//        CheckBox isAnswer10Correct = new CheckBox("Is Correct");
        isAnswer10Correct.setFont(toolFont);
//        TextArea textAreaAnswer10 = new TextArea();
        textAreaAnswer10.setFont(toolFont);
        textAreaAnswer10.setWrapText(true);
        textAreaAnswer10.setPrefSize(screenWidth / 1.5, screenHeight / 4);
        answerHBox10.getChildren().add(answerLabel10);
        answerHBox10.getChildren().add(isAnswer10Correct);
        answerHBox10.getChildren().add(textAreaAnswer10);

        VBox.setMargin(titleGroupHBox, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(titleGroupHBox);
        VBox.setMargin(questionBox, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(questionBox);
        VBox.setMargin(titleHBox1, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(titleHBox1);
        VBox.setMargin(titleHBox2, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(titleHBox2);
        VBox.setMargin(titleHBox3, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(titleHBox3);
        VBox.setMargin(titleHBox4, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(titleHBox4);
        VBox.setMargin(titleHBox5, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(titleHBox5);
        VBox.setMargin(titleHBox6, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(titleHBox6);
        VBox.setMargin(titleHBox7, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(titleHBox7);
        VBox.setMargin(titleHBox8, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(titleHBox8);
        VBox.setMargin(titleHBox9, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(titleHBox9);
        VBox.setMargin(titleHBox10, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(titleHBox10);
        VBox.setMargin(answerBox, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(answerBox);
        VBox.setMargin(answerHBox1, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(answerHBox1);
        VBox.setMargin(answerHBox2, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(answerHBox2);
        VBox.setMargin(answerHBox3, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(answerHBox3);
        VBox.setMargin(answerHBox4, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(answerHBox4);
        VBox.setMargin(answerHBox5, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(answerHBox5);
        VBox.setMargin(answerHBox6, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(answerHBox6);
        VBox.setMargin(answerHBox7, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(answerHBox7);
        VBox.setMargin(answerHBox8, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(answerHBox8);
        VBox.setMargin(answerHBox9, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(answerHBox9);
        VBox.setMargin(answerHBox10, new Insets(10, 10, 10, 10));
        scrollPaneContent.getChildren().add(answerHBox10);

        designerScrollPane.setContent(scrollPaneContent);

        //Set up Result VBox
        VBox resultVBox = new VBox();
        resultVBox.setPrefWidth(screenWidth);
        resultVBox.setAlignment(Pos.CENTER);
        resultVBox.getChildren().add(questionDesignerPaneHeader);
        resultVBox.getChildren().add(designPane);

        return resultVBox;
    }

    public void displayQuestionDataInQuestionModelByIndex(QuestionHandler questionHandler, int questionIndex) {
        QuestionDataModel[] questionDataModels = questionHandler.getquestionDataModels();
        List<String> listOfGroupName = questionHandler.fullListOfISTQBTypeReadFromData;
        questionIndexTextField.setText(String.valueOf(questionIndex));
        selectedGroupName.setText(listOfGroupName.get(questionIndex));
        textArea1.setText(getAllCharsInString(questionDataModels[questionIndex].questionTitle1));
        textArea2.setText(getAllCharsInString(questionDataModels[questionIndex].questionTitle2));
        textArea3.setText(getAllCharsInString(questionDataModels[questionIndex].questionTitle3));
        textArea4.setText(getAllCharsInString(questionDataModels[questionIndex].questionTitle4));
        textArea5.setText(getAllCharsInString(questionDataModels[questionIndex].questionTitle5));
        textArea6.setText(getAllCharsInString(questionDataModels[questionIndex].questionTitle6));
        textArea7.setText(getAllCharsInString(questionDataModels[questionIndex].questionTitle7));
        textArea8.setText(getAllCharsInString(questionDataModels[questionIndex].questionTitle8));
        textArea9.setText(getAllCharsInString(questionDataModels[questionIndex].questionTitle9));
        textArea10.setText(getAllCharsInString(questionDataModels[questionIndex].questionTitle10));
        isMultiAnswersCheckbox.setSelected(questionDataModels[questionIndex].isMultipleChoice);
        textAreaAnswer1.setText(getAllCharsInString(questionDataModels[questionIndex].questionAnswer1));
        textAreaAnswer2.setText(getAllCharsInString(questionDataModels[questionIndex].questionAnswer2));
        textAreaAnswer3.setText(getAllCharsInString(questionDataModels[questionIndex].questionAnswer3));
        textAreaAnswer4.setText(getAllCharsInString(questionDataModels[questionIndex].questionAnswer4));
        textAreaAnswer5.setText(getAllCharsInString(questionDataModels[questionIndex].questionAnswer5));
        textAreaAnswer6.setText(getAllCharsInString(questionDataModels[questionIndex].questionAnswer6));
        textAreaAnswer7.setText(getAllCharsInString(questionDataModels[questionIndex].questionAnswer7));
        textAreaAnswer8.setText(getAllCharsInString(questionDataModels[questionIndex].questionAnswer8));
        textAreaAnswer9.setText(getAllCharsInString(questionDataModels[questionIndex].questionAnswer9));
        textAreaAnswer10.setText(getAllCharsInString(questionDataModels[questionIndex].questionAnswer10));
        isAnswer1Correct.setSelected(questionDataModels[questionIndex].isQuestionAnswer1Correct);
        isAnswer2Correct.setSelected(questionDataModels[questionIndex].isQuestionAnswer2Correct);
        isAnswer3Correct.setSelected(questionDataModels[questionIndex].isQuestionAnswer3Correct);
        isAnswer4Correct.setSelected(questionDataModels[questionIndex].isQuestionAnswer4Correct);
        isAnswer5Correct.setSelected(questionDataModels[questionIndex].isQuestionAnswer5Correct);
        isAnswer6Correct.setSelected(questionDataModels[questionIndex].isQuestionAnswer6Correct);
        isAnswer7Correct.setSelected(questionDataModels[questionIndex].isQuestionAnswer7Correct);
        isAnswer8Correct.setSelected(questionDataModels[questionIndex].isQuestionAnswer8Correct);
        isAnswer9Correct.setSelected(questionDataModels[questionIndex].isQuestionAnswer9Correct);
        isAnswer10Correct.setSelected(questionDataModels[questionIndex].isQuestionAnswer10Correct);
    }

    private String getAllCharsInString(String stringToGet) {
        char[] charList = stringToGet.toCharArray();
        StringBuilder resultString = new StringBuilder("");
        for (int i = 0; i < charList.length; i++) {
            if (charList[i] == 13) {
//                resultString.append("\\r");
            } else if (charList[i] == 10) {
                resultString.append("\\n");
            } else {
                resultString.append(charList[i]);
            }
        }
        return String.valueOf(resultString);
    }
}