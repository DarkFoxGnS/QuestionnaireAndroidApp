package com.tiborszabo.quizapp.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * This object represents the questions.
 * @author Tibor Peter Szabo.
 */
public class Question implements Serializable {


    /**
     * This enum contains tho possible question types that a Question can be.
     */
    public enum QuestionType{
        /**
         * Question uses a Text input.
         */
        TEXT,
        /**
         * Question accepts a single corrent answer amongst the possible answers.
         */
        SINGLECHOICE,
        /**
         * There are multiple correct possible answers in this question.
         */
        MULTICHOICE
    }

    /**
     * The name of the object.
     */
    private String name;
    /**
     * The Question type of the object
     */
    private QuestionType questionType;
    /**
     * The question of the question.
     */
    private String question;

    /**
     * The answers and their validity of the question.
     */
    private LinkedHashMap<String,Boolean> answers;
    /**
     * The explanation of the object.
     */
    private String explanation;

    /**
     * Initializes the Question object.
     */
    public Question(){
        this.questionType = QuestionType.TEXT;
        this.name = "";
        this.explanation = "";
        this.question = "";
        this.answers = new LinkedHashMap<>();
    }

    /**
     * Initializes the Question object with parameters.
     * @param questionType QuestionType, type of the question.
     * @param question String, the question of the Question.
     * @param answers LinkedHashMap of Strings and booleans, the answer(s) of the question. It's  multiple options for SINGLECHOICE and MULTICHOICE questions, and a single option for a TEXT type question.
     * @param name String, the name of the question.
     * @param explanation String, the explanation of the question.
     */
    public Question(String name,QuestionType questionType, String question, String explanation,LinkedHashMap<String,Boolean> answers){
        this.name = name;
        this.questionType = questionType;
        this.question = question;
        this.answers = answers;
        this.explanation = explanation;
    }


    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.name.replace(";","%59")+";");
        s.append(this.getQuestionType()+";");
        s.append(this.question.replace(";","%59")+";");
        s.append(this.explanation.replace(";","%59")+";");

         Set<String> a = getAnswerText();

        for(String answer : a){
            boolean value = getAnswerValue(answer);

            s.append(answer.replace(";","%59")+";"+value+";");
        }
            s.deleteCharAt(s.length()-1);
        return s.toString();
    }

    /**
     * Sets the name of the object to name.
     * @param name String, the new name of the object.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets the question.
     * @param question, the String that will be set as the question.
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Sets the explanation of the object.
     * @param explanation String, the explanation of the question.
     */
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    /**
     * Sets the answers.
     * @param answers, the LinkedHashMap of answers to set as the answers.
     */
    public void setAnswers(LinkedHashMap<String, Boolean> answers) {
        this.answers = answers;
    }

    /**
     * Sets the question type of the question.
     * @param questionType, the QuestionType to be set.
     */
    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    /**
     * Appends the answers LinkedHashMap with the pair of answer, correct.
     * @param answer, the text of the answers.
     * @param correct, the validity of the answer. For a TEXT type question, it should always be true.
     */
    public void putAnswer(String answer, Boolean correct){
        this.answers.put(answer,correct);
    }

    /**
     * Gets the name of the object.
     * @return String, the name of the object.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the answers LinkedHashMap.
     * @return the answers LinkedHashMap.
     */
    public LinkedHashMap<String, Boolean> getAnswers() {
        return answers;
    }

    /**
     * Gets the possible answers.
     * @return a set of answer strings.
     */
    public Set<String> getAnswerText(){
        return this.answers.keySet();
    }

    /**
     * Gets the validity of the answer.
     * @param key, the text of the answer.
     * @return boolean, representing the validity of the answer.
     */
    public Boolean getAnswerValue(Object key){
        return this.answers.get(key);
    }

    /**
     * Gets the question String of the Question object.
     * @return String, the question string of the Question.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Gets the question type.
     * @return QuestionType, the QuestionType of this Question object.
     */
    public QuestionType getQuestionType() {
        return questionType;
    }

    /**
     * Gets the explanation of the object.
     * @return String, the explanation.
     */
    public String getExplanation() {
        return explanation;
    }
}
