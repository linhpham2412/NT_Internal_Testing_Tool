package nt.testingtool.istqb.Utils;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import nt.testingtool.istqb.datamodel.QuestionBankDataModel;
import nt.testingtool.istqb.datamodel.QuestionDataModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

import static nt.testingtool.istqb.Utils.ProjectConfiguration.*;

public class QuestionHandler {
    public static boolean isQuestionMultipleChoices;
    public static QuestionDataModel[] testingQuestions;
    public static String imageFolderAbsolutePath;
    public static String[] questionStringAnswer;
    public static boolean[] questionBooleanIsAnswerCorrect;
    public static String isMultiHeaderText = "";
    public static String isAnswer1CorrectHeaderText = "";
    public static String isAnswer2CorrectHeaderText = "";
    public static String isAnswer3CorrectHeaderText = "";
    public static String isAnswer4CorrectHeaderText = "";
    public static String isAnswer5CorrectHeaderText = "";
    public static String isAnswer6CorrectHeaderText = "";
    public static String isAnswer7CorrectHeaderText = "";
    public static String isAnswer8CorrectHeaderText = "";
    public static String isAnswer9CorrectHeaderText = "";
    public static String isAnswer10CorrectHeaderText = "";

    public static QuestionDataModel getTemporaryChangeQuestion() {
        return temporaryChangeQuestion;
    }

    public static void setTemporaryChangeQuestion(QuestionDataModel temporaryChangeQuestion) {
        QuestionHandler.temporaryChangeQuestion = temporaryChangeQuestion;
    }

    public static QuestionDataModel temporaryChangeQuestion;

    public static int getCurrentQuestionPreviewIndex() {
        return currentQuestionPreviewIndex;
    }

    public static void setCurrentQuestionPreviewIndex(int currentQuestionPreviewIndex) {
        QuestionHandler.currentQuestionPreviewIndex = currentQuestionPreviewIndex;
    }

    public static int currentQuestionPreviewIndex = 0;
    public static int[][] selectedAnswer;
    public static int[][] correctAnswer;
    //Question Field items
    static Label[] questionTitle;
    static javafx.scene.image.ImageView[] questionImage;
    static GridPane[] questionGridTable;
    static Object[] questionObjects;
    static CheckBox[] answerCheckBoxes;
    static RadioButton[] answerRadioButtons;
    static ToggleGroup[] answerRadioGroup = new ToggleGroup[getNumberOfQuestionsPerQuestionBank()];
    static HBox[] answerHBoxContainers;
    //Question data item
    static String[] questionStringTitle;
    public File imagesFolder;
    public boolean isFirstLoad = true;
    List<String> listOfISTQBTypeReadFromData = new ArrayList<>();
    public static List<String> fullListOfISTQBTypeReadFromData = new ArrayList<>();
    private ZipFile zipFile;
    private QuestionBankDataModel questionBankDataModels;
    private QuestionDataModel[] questionDataModels;
    private String zipFileName;
    private String readData;
    private String zipPassword = "";

    public static QuestionHandler initQuestionHandler() {
        return new QuestionHandler();
    }

    public static boolean isQuestionMultipleChoices() {
        return isQuestionMultipleChoices;
    }

    public static void initQuestionElements(int pageIndex) {
        questionTitle = new Label[getMaxNumberOfAnswerElementsInQuestionBank()];
        questionImage = new ImageView[getMaxNumberOfAnswerElementsInQuestionBank()];
        questionGridTable = new GridPane[getMaxNumberOfAnswerElementsInQuestionBank()];
        questionObjects = new Object[getMaxNumberOfAnswerElementsInQuestionBank()];
        answerCheckBoxes = new CheckBox[getMaxNumberOfAnswerElementsInQuestionBank()];
        answerRadioButtons = new RadioButton[getMaxNumberOfAnswerElementsInQuestionBank()];
        answerRadioGroup = new ToggleGroup[getNumberOfQuestionsPerQuestionBank()];
        answerRadioGroup[pageIndex] = new ToggleGroup();
        answerHBoxContainers = new HBox[getMaxNumberOfAnswerElementsInQuestionBank() / 2];
        questionStringTitle = new String[getMaxNumberOfAnswerElementsInQuestionBank()];
        questionStringAnswer = new String[getMaxNumberOfAnswerElementsInQuestionBank()];
        questionBooleanIsAnswerCorrect = new boolean[getMaxNumberOfAnswerElementsInQuestionBank()];
    }

    public static int[][] getSelectedAnswer() {
        return selectedAnswer;
    }

    public static void setSelectedAnswer(int pageIndex, int elementIndex, int value) {
        selectedAnswer[pageIndex][elementIndex] = value;
    }

    public static void setSelectedAnswerElementArray(int pageIndex, int[] elementArray) {
        selectedAnswer[pageIndex] = elementArray;
    }

    public static int[][] getCorrectAnswer() {
        return correctAnswer;
    }

    public static void initSelectedAnwser() {
        selectedAnswer = new int[getNumberOfQuestionsPerQuestionBank()][getMaxNumberOfAnswerElementsInQuestionBank()];
    }

    public static void initCorrectAnswer() {
        correctAnswer = new int[getNumberOfQuestionsPerQuestionBank()][getMaxNumberOfAnswerElementsInQuestionBank()];
    }

    public QuestionDataModel[] getquestionDataModels() {
        return questionDataModels;
    }

    public static String checkUnAnsweredQuestions() {
        StringBuilder remainingUnAnswers = new StringBuilder();
        for (int i = 0; i < getNumberOfQuestionsPerQuestionBank(); i++) {
            if (Arrays.stream(selectedAnswer[i]).sum() == 0) {
                remainingUnAnswers.append(i + 1).append(", ");
            }
        }
        return remainingUnAnswers.deleteCharAt(remainingUnAnswers.length() - 2).toString().trim();
    }

    public void readQuestionZipFile(String fileName, String zipPassword) throws ZipException, IOException {
        if (isFirstLoad) {
            zipFileName = fileName;
            File zipFileToDelete = switchDataZipFileName(zipFileName);
            zipFile = new ZipFile(zipFileToDelete.toPath().toFile());
            this.zipPassword = zipPassword;
            // Unzip a password-protected ZIP file
            removePasswordToQuestionZipFile(zipFile, zipPassword);
            zipFile.extractAll(getCurrentPath());
            imagesFolder = new File(getCurrentPath() + "\\Images");
            imageFolderAbsolutePath = imagesFolder.getAbsolutePath();
            //Delete zip file
            zipFileToDelete.delete();
        }
    }

    public void removePasswordToQuestionZipFile(ZipFile zipFile, String password) throws ZipException {
        if (zipFile.isEncrypted()) {
            zipFile.setPassword(password);
        }
    }

    public void readAndSaveAllISTQBTypeInData(String zipPassword) throws IOException {
        this.zipPassword = zipPassword;
        String dataTextFileName = getCurrentPath() + "\\" + zipFileName + ".txt";
        if (isFirstLoad) {
            try (FileReader fr = new FileReader(dataTextFileName);
                 BufferedReader reader = new BufferedReader(fr)) {
                readData = reader.readLine();
                do {
                    try {
                        String[] readList = Arrays.stream(readData.split("\\|"))
                                .map(e -> e.trim()).collect(Collectors.toList()).toArray(new String[0]);
                        if (!readList[1].equals("Group")) {
                            listOfISTQBTypeReadFromData.add(readList[1]);
                        }
                    } catch (Exception e) {
                    }
                }
                while ((readData = reader.readLine()) != null);
                listOfISTQBTypeReadFromData = listOfISTQBTypeReadFromData.stream().distinct().collect(Collectors.toList());
                //Remove txt file after read
                reader.close();
                fr.close();
                //Remove txt file after rezip
                File questionTextFile = new File(dataTextFileName);
                questionTextFile.delete();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<String> getFullListOfISTQBTypeReadFromData() {
        return fullListOfISTQBTypeReadFromData;
    }

    public void readAndMapQuestionDataFromFileToDataObject(File readFile) {
        File dataTextFileName = readFile;
        questionDataModels = new QuestionDataModel[1];
        int numberOfQuestionBanksInGroup = 0;
        try (FileReader fr = new FileReader(dataTextFileName);
             BufferedReader reader = new BufferedReader(fr)) {
            readData = reader.readLine();
            do {
                try {
                    String[] readList = Arrays.stream(readData.split("\\|"))
                            .map(e -> e.trim()).collect(Collectors.toList()).toArray(new String[0]);
                    if (readList[1].equals("Group")) {
                        isMultiHeaderText = readList[12];
                        isAnswer1CorrectHeaderText = readList[13];
                        isAnswer2CorrectHeaderText = readList[15];
                        isAnswer3CorrectHeaderText = readList[17];
                        isAnswer4CorrectHeaderText = readList[19];
                        isAnswer5CorrectHeaderText = readList[21];
                        isAnswer6CorrectHeaderText = readList[23];
                        isAnswer7CorrectHeaderText = readList[25];
                        isAnswer8CorrectHeaderText = readList[27];
                        isAnswer9CorrectHeaderText = readList[29];
                        isAnswer10CorrectHeaderText = readList[31];
                    }
                    listOfISTQBTypeReadFromData.add(readList[1]);
                    questionDataModels[numberOfQuestionBanksInGroup] = readAndAddQuestionsToQuestionBank(readList);
                    numberOfQuestionBanksInGroup++;
                    questionDataModels = Arrays.copyOf(questionDataModels, numberOfQuestionBanksInGroup + 1);
                } catch (Exception e) {
                }
            }
            while ((readData = reader.readLine()) != null);
            fullListOfISTQBTypeReadFromData = listOfISTQBTypeReadFromData;
            listOfISTQBTypeReadFromData = listOfISTQBTypeReadFromData.stream().distinct().collect(Collectors.toList());
            questionDataModels = Arrays.copyOf(questionDataModels, questionDataModels.length - 1);
            questionBankDataModels = new QuestionBankDataModel();
            questionBankDataModels.setQuestionDataModels(questionDataModels);
            //close reader after read
            reader.close();
            fr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addNewQuestionToModel() {
        int lastQuestionIndex = questionDataModels.length;
        questionDataModels = Arrays.copyOf(questionDataModels, lastQuestionIndex + 1);
        fullListOfISTQBTypeReadFromData.add("");
        questionDataModels[lastQuestionIndex] = new QuestionDataModel();
    }

    public void deleteCurrentQuestionFromModel() {
        int currentQuestionIndex = getCurrentQuestionPreviewIndex();
        fullListOfISTQBTypeReadFromData.remove(currentQuestionIndex);
        QuestionDataModel[] tempQuestionList = new QuestionDataModel[questionDataModels.length - 1];
        for (int oldIndex = 0, newIndex = 0; oldIndex < questionDataModels.length; oldIndex++) {
            if (oldIndex != currentQuestionIndex) {
                tempQuestionList[newIndex++] = questionDataModels[oldIndex];
            }
        }
        questionDataModels = Arrays.copyOf(questionDataModels, questionDataModels.length - 1);
        questionDataModels = tempQuestionList;
    }

    private File switchDataZipFileName(String zipFileName) throws IOException, ZipException {
        File zipFileToDelete;
        Path sourceFile = Paths.get(getCurrentPath() + "//QuestionData//" + zipFileName + ".zip");
        Path cloneFile = Paths.get(getCurrentPath() + zipFileName + "_Process.zip");
        Files.copy(sourceFile, cloneFile, StandardCopyOption.REPLACE_EXISTING);
        zipFileToDelete = new File(cloneFile.toUri());
        return zipFileToDelete;
    }

    public List<String> getListOfISTQBTypeReadFromData() {
        return listOfISTQBTypeReadFromData;
    }

    public void mapDataInQuestionFileToDataModelByGroupName() throws IOException {
        if (isFirstLoad) {
            String dataTextFileName = getCurrentPath() + "\\" + zipFileName + ".txt";
            questionDataModels = new QuestionDataModel[1];
            int numberOfQuestionBanksInGroup = 0;
            try (FileReader fr = new FileReader(dataTextFileName);
                 BufferedReader reader = new BufferedReader(fr)) {
                readData = reader.readLine();
                do {
                    try {
                        String[] readList = Arrays.stream(readData.split("\\|"))
                                .map(e -> e.trim()).collect(Collectors.toList()).toArray(new String[0]);
                        if (readList[1].equals(getQuestionGroupName())) {
                            questionDataModels[numberOfQuestionBanksInGroup] = readAndAddQuestionsToQuestionBank(readList);
                            numberOfQuestionBanksInGroup++;
                            questionDataModels = Arrays.copyOf(questionDataModels, numberOfQuestionBanksInGroup + 1);
                        }
                    } catch (Exception e) {
                    }
                }
                while ((readData = reader.readLine()) != null);
                questionDataModels = Arrays.copyOf(questionDataModels, questionDataModels.length - 1);
                questionBankDataModels = new QuestionBankDataModel();
                questionBankDataModels.setQuestionDataModels(questionDataModels);
                //Remove txt file after read
                reader.close();
                fr.close();
                //Remove txt file after rezip
                File questionTextFile = new File(dataTextFileName);
                questionTextFile.delete();
                isFirstLoad = false;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private QuestionDataModel readAndAddQuestionsToQuestionBank(String[] readList) {
        QuestionDataModel questionDataModel = new QuestionDataModel();
        int colIndex = 1;
        for (int i = 2; i < readList.length; i++) {
            mapColumnDataToQuestionDataModelBasedOnColumnIndex(questionDataModel, readList[i], colIndex);
            colIndex++;
        }
        return questionDataModel;
    }

    private QuestionDataModel mapColumnDataToQuestionDataModelBasedOnColumnIndex(QuestionDataModel questionDataModel, String columnData, int colIndex) {
        columnData = columnData.trim();
        //Check if there is any \n in columnData then replace with System lineseparator
        columnData = checkNewLineSymbolInDataToReplaceWithLineSeparator(columnData);
        switch (colIndex) {
            case 1: {
                questionDataModel.setQuestionTitle1(columnData);
                break;
            }
            case 2: {
                questionDataModel.setQuestionTitle2(columnData);
                break;
            }
            case 3: {
                questionDataModel.setQuestionTitle3(columnData);
                break;
            }
            case 4: {
                questionDataModel.setQuestionTitle4(columnData);
                break;
            }
            case 5: {
                questionDataModel.setQuestionTitle5(columnData);
                break;
            }
            case 6: {
                questionDataModel.setQuestionTitle6(columnData);
                break;
            }
            case 7: {
                questionDataModel.setQuestionTitle7(columnData);
                break;
            }
            case 8: {
                questionDataModel.setQuestionTitle8(columnData);
                break;
            }
            case 9: {
                questionDataModel.setQuestionTitle9(columnData);
                break;
            }
            case 10: {
                questionDataModel.setQuestionTitle10(columnData);
                break;
            }
            case 11: {
                questionDataModel.setMultipleChoice(convertDataToBoolean(columnData));
                break;
            }
            case 13: {
                questionDataModel.setQuestionAnswer1(columnData);
                break;
            }
            case 12: {
                questionDataModel.setQuestionAnswer1Correct(convertDataToBoolean(columnData));
                break;
            }
            case 15: {
                questionDataModel.setQuestionAnswer2(columnData);
                break;
            }
            case 14: {
                questionDataModel.setQuestionAnswer2Correct(convertDataToBoolean(columnData));
                break;
            }
            case 17: {
                questionDataModel.setQuestionAnswer3(columnData);
                break;
            }
            case 16: {
                questionDataModel.setQuestionAnswer3Correct(convertDataToBoolean(columnData));
                break;
            }
            case 19: {
                questionDataModel.setQuestionAnswer4(columnData);
                break;
            }
            case 18: {
                questionDataModel.setQuestionAnswer4Correct(convertDataToBoolean(columnData));
                break;
            }
            case 21: {
                questionDataModel.setQuestionAnswer5(columnData);
                break;
            }
            case 20: {
                questionDataModel.setQuestionAnswer5Correct(convertDataToBoolean(columnData));
                break;
            }
            case 23: {
                questionDataModel.setQuestionAnswer6(columnData);
                break;
            }
            case 22: {
                questionDataModel.setQuestionAnswer6Correct(convertDataToBoolean(columnData));
                break;
            }
            case 25: {
                questionDataModel.setQuestionAnswer7(columnData);
                break;
            }
            case 24: {
                questionDataModel.setQuestionAnswer7Correct(convertDataToBoolean(columnData));
                break;
            }
            case 27: {
                questionDataModel.setQuestionAnswer8(columnData);
                break;
            }
            case 26: {
                questionDataModel.setQuestionAnswer8Correct(convertDataToBoolean(columnData));
                break;
            }
            case 29: {
                questionDataModel.setQuestionAnswer9(columnData);
                break;
            }
            case 28: {
                questionDataModel.setQuestionAnswer9Correct(convertDataToBoolean(columnData));
                break;
            }
            case 31: {
                questionDataModel.setQuestionAnswer10(columnData);
                break;
            }
            case 30: {
                questionDataModel.setQuestionAnswer10Correct(convertDataToBoolean(columnData));
                break;
            }
        }
        return questionDataModel;
    }

    private String checkNewLineSymbolInDataToReplaceWithLineSeparator(String columnData) {
        if (columnData.contains("\\n")) {
            columnData = convertAllEnterSymbolInTextToSpecialCharacter(columnData);
            columnData = QuestionDesigner.convertEnterCharacterToReview(columnData);
        }
        return columnData;
    }

    private String convertAllEnterSymbolInTextToSpecialCharacter(String textToConvert) {
        return textToConvert.replaceAll("\\\\n", "¶");
    }

    private boolean convertDataToBoolean(String dataToConvert) {
        return dataToConvert.equals("Y");
    }

    public void randomChooseQuestionsInBankThenShuffleAndSaveToTestingQuestions() {
        List<QuestionDataModel> shuffleTestingQuestions =
                new LinkedList<>
                        (Arrays.asList(questionBankDataModels.getQuestionDataModels()));
        Collections.shuffle(shuffleTestingQuestions, new Random());
        testingQuestions = new QuestionDataModel[1];
        testingQuestions = shuffleTestingQuestions.subList(0, getNumberOfQuestionsPerQuestionBank())
                .toArray(new QuestionDataModel[0]);
        for (int i = 0; i < correctAnswer.length; i++) {
            correctAnswer[i][0] = convertBooleanToOneOrZero(testingQuestions[i].isQuestionAnswer1Correct);
            correctAnswer[i][1] = convertBooleanToOneOrZero(testingQuestions[i].isQuestionAnswer2Correct);
            correctAnswer[i][2] = convertBooleanToOneOrZero(testingQuestions[i].isQuestionAnswer3Correct);
            correctAnswer[i][3] = convertBooleanToOneOrZero(testingQuestions[i].isQuestionAnswer4Correct);
            correctAnswer[i][4] = convertBooleanToOneOrZero(testingQuestions[i].isQuestionAnswer5Correct);
            correctAnswer[i][5] = convertBooleanToOneOrZero(testingQuestions[i].isQuestionAnswer6Correct);
            correctAnswer[i][6] = convertBooleanToOneOrZero(testingQuestions[i].isQuestionAnswer7Correct);
            correctAnswer[i][7] = convertBooleanToOneOrZero(testingQuestions[i].isQuestionAnswer8Correct);
            correctAnswer[i][8] = convertBooleanToOneOrZero(testingQuestions[i].isQuestionAnswer9Correct);
            correctAnswer[i][9] = convertBooleanToOneOrZero(testingQuestions[i].isQuestionAnswer10Correct);
        }
    }

    private int convertBooleanToOneOrZero(boolean booleanToConvert) {
        return (booleanToConvert) ? 1 : 0;
    }

    public String convertBooleanToSaveFormat(boolean booleanToConvert) {
        return (booleanToConvert) ? "Y" : "";
    }
}
