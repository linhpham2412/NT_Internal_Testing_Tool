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
    private final int checkingBankIndex = 0;
    private final int previousTestingBankIndex = 0;
    private final int testingBankIndex = 0;
    public File imagesFolder;
    public boolean isFirstLoad = true;
    List<String> listOfISTQBTypeReadFromData = new ArrayList<>();
    private ZipFile zipFile;
    private QuestionBankDataModel questionBankDataModels;
    private QuestionDataModel[] questionDataModels;
    private String zipFileName;
    private String readData;
    private String zipPassword = "";

    public static boolean isQuestionMultipleChoices() {
        return isQuestionMultipleChoices;
    }

    public void setQuestionMultipleChoices(boolean questionMultipleChoices) {
        isQuestionMultipleChoices = questionMultipleChoices;
    }

    public static void initQuestionElements(int pageIndex) {
        questionTitle = new Label[getMaxNumberOfAnswerElementsInQuestionBank()];
        questionImage = new ImageView[getMaxNumberOfAnswerElementsInQuestionBank()];
        questionGridTable = new GridPane[getMaxNumberOfAnswerElementsInQuestionBank()];
        questionObjects = new Object[getMaxNumberOfAnswerElementsInQuestionBank()];
        answerCheckBoxes = new CheckBox[getMaxNumberOfAnswerElementsInQuestionBank()];
        answerRadioButtons = new RadioButton[getMaxNumberOfAnswerElementsInQuestionBank()];
        answerRadioGroup[pageIndex] = new ToggleGroup();
        answerHBoxContainers = new HBox[getMaxNumberOfAnswerElementsInQuestionBank() / 2];
        questionStringTitle = new String[getMaxNumberOfAnswerElementsInQuestionBank()];
        questionStringAnswer = new String[getMaxNumberOfAnswerElementsInQuestionBank()];
        questionBooleanIsAnswerCorrect = new boolean[getMaxNumberOfAnswerElementsInQuestionBank()];
    }

    public static int[][] getSelectedAnswer() {
        return selectedAnswer;
    }

    public static void setSelectedAnswer(int pageIndex, int elementIndex, int value){
        selectedAnswer[pageIndex][elementIndex] = value;
    }
    public static void setSelectedAnswerElementArray(int pageIndex, int[] elementArray){
        selectedAnswer[pageIndex] = elementArray;
    }
    public static void setCorrectAnswerAnswer(int pageIndex, int elementIndex, int value){
        correctAnswer[pageIndex][elementIndex] = value;
    }

    public static int[][] getCorrectAnswer() {
        return correctAnswer;
    }

    public String[] getQuestionStringTitle() {
        return questionStringTitle;
    }

    public void setQuestionStringTitle(String[] questionStringTitle) {
        QuestionHandler.questionStringTitle = questionStringTitle;
    }

    public String[] getQuestionStringAnswer() {
        return questionStringAnswer;
    }

    public void setQuestionStringAnswer(String[] questionStringAnswer) {
        QuestionHandler.questionStringAnswer = questionStringAnswer;
    }

    public boolean[] getQuestionBooleanIsAnswerCorrect() {
        return questionBooleanIsAnswerCorrect;
    }

    public void setQuestionBooleanIsAnswerCorrect(boolean[] questionBooleanIsAnswerCorrect) {
        QuestionHandler.questionBooleanIsAnswerCorrect = questionBooleanIsAnswerCorrect;
    }

    public Label[] getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(Label[] title) {
        questionTitle = title;
    }

    public ImageView[] getQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(ImageView[] questionImage) {
        QuestionHandler.questionImage = questionImage;
    }

    public GridPane[] getQuestionGridTable() {
        return questionGridTable;
    }

    public void setQuestionGridTable(GridPane[] questionGridTable) {
        QuestionHandler.questionGridTable = questionGridTable;
    }

    public Object[] getQuestionObjects() {
        return questionObjects;
    }

    public void setQuestionObjects(Object[] questionObjects) {
        QuestionHandler.questionObjects = questionObjects;
    }

    public CheckBox[] getAnswerCheckBoxes() {
        return answerCheckBoxes;
    }

    public void setAnswerCheckBoxes(CheckBox[] answerCheckBoxes) {
        QuestionHandler.answerCheckBoxes = answerCheckBoxes;
    }

    public RadioButton[] getAnswerRadioButtons() {
        return answerRadioButtons;
    }

    public void setAnswerRadioButtons(RadioButton[] answerRadioButtons) {
        QuestionHandler.answerRadioButtons = answerRadioButtons;
    }

    public ToggleGroup[] getAnswerRadioGroup() {
        return answerRadioGroup;
    }

    public void setAnswerRadioGroup(ToggleGroup[] answerRadioGroup) {
        QuestionHandler.answerRadioGroup = answerRadioGroup;
    }

    public HBox[] getAnswerHBoxContainers() {
        return answerHBoxContainers;
    }

    public void setAnswerHBoxContainers(HBox[] answerHBoxContainers) {
        QuestionHandler.answerHBoxContainers = answerHBoxContainers;
    }

    public static void initSelectedAnwser() {
        selectedAnswer = new int[getNumberOfQuestionsPerQuestionBank()][getMaxNumberOfAnswerElementsInQuestionBank()];
    }

    public static void initCorrectAnswer() {
        correctAnswer = new int[getNumberOfQuestionsPerQuestionBank()][getMaxNumberOfAnswerElementsInQuestionBank()];
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

    private File switchDataZipFileName(String zipFileName) throws IOException, ZipException {
        File zipFileToDelete;
//        executionPath = System.getProperty("user.dir");
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
//                numberOfQuestionBanksInGroup=0;
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
            String[] splitedStringsBasedOnNewLine = columnData.split("\\\\n");
            columnData = "";
            for (String line : splitedStringsBasedOnNewLine) {
                columnData += line + System.lineSeparator();
            }
        }
        return columnData;
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
}