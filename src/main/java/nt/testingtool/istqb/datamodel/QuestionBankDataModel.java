package nt.testingtool.istqb.datamodel;

public class QuestionBankDataModel {
    public QuestionDataModel[] getQuestionDataModels() {
        return questionDataModels;
    }

    public void setQuestionDataModels(QuestionDataModel[] questionDataModels) {
        this.questionDataModels = questionDataModels;
    }

    public QuestionDataModel[] questionDataModels;
}
