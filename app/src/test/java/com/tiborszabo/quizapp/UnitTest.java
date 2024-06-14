package com.tiborszabo.quizapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.ContextWrapper;
import android.widget.ScrollView;

import com.tiborszabo.quizapp.managers.QuestionnaireManager;
import com.tiborszabo.quizapp.objects.Question;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * These tests will be executed on the machine host without the need for an Android device.
 */
public class UnitTest {
    /**
     * Tests the creation and methods of the Question object.
     */
    @Test
    public void test_questions() {
        //Run the test multiple times.
        for (int i = 0; i < 50; i++) {
            //Create random data
            String name = Math.random()*100+1+"";
            String explanation = Math.random()*100+1+"";
            String question = Math.random()*100+1+"";
            Question.QuestionType questionType = (Math.random()>0.5)? Question.QuestionType.MULTICHOICE: Question.QuestionType.SINGLECHOICE;

            //Create question and fill random data.
            Question testingQuestion = new Question();
            testingQuestion.setName(name);
            testingQuestion.setQuestion(question);
            testingQuestion.setExplanation(explanation);
            testingQuestion.setQuestionType(questionType);

            //Create random data for the answers.
            LinkedHashMap<String,Boolean> lhs = new LinkedHashMap<>();
            for (int j = 0; j < (int)(Math.random()*4+1);j++){

                String answer = ""+j;
                boolean value = Math.random() > 0.5;
                lhs.put(answer,value);
            }
            //put random data into object.
            testingQuestion.setAnswers(lhs);
            
            
            //Testing
            assertEquals (name,testingQuestion.getName());
            assertEquals (question,testingQuestion.getQuestion());
            assertEquals (explanation,testingQuestion.getExplanation());
            assertEquals (questionType,testingQuestion.getQuestionType());
            assertEquals(lhs.keySet(),testingQuestion.getAnswerText());

            for (int j = 0; j < testingQuestion.getAnswerText().size(); j++) {

                String answer = testingQuestion.getAnswerText().toArray()[j].toString();
                String expected = lhs.keySet().toArray()[j].toString();
                assertEquals(expected,answer);
            }
        }
    }

    /**
     * Tests the SettingData object.
     */
    @Test
    public void test_SettingData(){

    }
}