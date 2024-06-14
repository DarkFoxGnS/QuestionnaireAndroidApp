package com.tiborszabo;

import android.content.Context;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.test.platform.app.InstrumentationRegistry;

import com.tiborszabo.quizapp.managers.QuestionnaireManager;
import com.tiborszabo.quizapp.objects.Question;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Tests that require an android device to be executed.
 * @author Tibor Peter Szabo
 */
@RunWith(JUnit4.class)
public class InstrumentedTest {

    /**
     *Tests the QuestionnaireManager with Text questions.
     */
    @Test
    public void test_question_manager_text(){
        //Try to prepare the looper to interact with the mock UI elements.
        try{
            Looper.prepare();
        }catch (Exception e){

        }
        for (int i = 0; i < 50; i++) {
            //Create mockContext
            Context mockContext = InstrumentationRegistry.getInstrumentation().getContext();

            //Create questions and testing Views.
            List<Question> testingData = new ArrayList<>();
            ScrollView descriptionView = new ScrollView(mockContext);
            LinearLayout answerView = new LinearLayout(mockContext);

            //Create random answer.
            for (int j = 0; j < (int)(Math.random()*5+1); j++) {

                Question question = new Question();
                question.setQuestionType(Question.QuestionType.TEXT);
                question.putAnswer(""+(int)(Math.random()*1000),true);
                testingData.add(question);
            }

            //Create QuestionnaireManager, and set questions..
            QuestionnaireManager testQuestionnaireManager = new QuestionnaireManager();
            testQuestionnaireManager.setQuestions(testingData);



            //Test questions
            for (Question question: testingData) {

                //Let the QuestionnaireManager create the EditText.
                testQuestionnaireManager.show(mockContext,descriptionView,answerView);

                //update the EditText.
                EditText et = (EditText) answerView.getChildAt(0);
                et.setText((String)question.getAnswerText().toArray()[0]);

                //Evaluate and progress the manager.
                Assert.assertTrue(testQuestionnaireManager.evaluate());
                testQuestionnaireManager.next();
            }

        }
    }

    /**
     * Test QuestionnaireManager with SingleChoice questions.
     */
    @Test
    public void test_question_manager_singlechoice(){
        //Try to prepare the looper to interact with the mock UI elements.
        try{
            Looper.prepare();
        }catch (Exception e){

        }
        for (int i = 0; i < 50; i++) {
            //Create mockContext
            Context mockContext = InstrumentationRegistry.getInstrumentation().getContext();

            //Create questions and testing Views.
            List<Question> testingData = new ArrayList<>();
            ScrollView descriptionView = new ScrollView(mockContext);
            LinearLayout answerView = new LinearLayout(mockContext);

            //Create random answers.
            for (int j = 0; j < (int)(Math.random()*5+1); j++) {

                Question question = new Question();
                question.setQuestionType(Question.QuestionType.SINGLECHOICE);
                LinkedHashMap<String,Boolean> lhs = new LinkedHashMap<>();
                int limit = (int)(Math.random()*4+1);
                int correct = (int)(Math.random()*limit);
                for (int k = 0; k < limit; k++) {
                    lhs.put(""+k,k == correct);
                }
                question.setAnswers(lhs);
                testingData.add(question);
            }

            //Create QuestionnaireManager, and set questions..
            QuestionnaireManager testQuestionnaireManager = new QuestionnaireManager();
            testQuestionnaireManager.setQuestions(testingData);



            //Test questions
            for (Question question: testingData) {

                //Let the QuestionnaireManager create the buttons.
                testQuestionnaireManager.show(mockContext,descriptionView,answerView);

                //update the buttons.
                boolean [] answers = new boolean[question.getAnswerText().size()];

                for (int j = 0; j < answers.length; j++) {
                    answers[j] = question.getAnswerValue(question.getAnswerText().toArray()[j]);
                }

                for (int j = 0; j < answerView.getChildCount(); j++) {
                    Button b = (Button) answerView.getChildAt(j);
                    b.setSelected(answers[j]);
                }

                //Evaluate and progress the manager.
                Assert.assertTrue(testQuestionnaireManager.evaluate());
                testQuestionnaireManager.next();
            }

        }
    }

    /**
     * Test QuestionnaireManager MultiChoice questions.
     */
    @Test
    public void test_question_manager_multichoice(){
        //Try to prepare the looper to interact with the mock UI elements.
        try{
            Looper.prepare();
        }catch (Exception e){

        }
        for (int i = 0; i < 50; i++) {
            //Create mockContext
            Context mockContext = InstrumentationRegistry.getInstrumentation().getContext();

            //Create questions and testing Views.
            List<Question> testingData = new ArrayList<>();
            ScrollView descriptionView = new ScrollView(mockContext);
            LinearLayout answerView = new LinearLayout(mockContext);

            //Create random answers.
            for (int j = 0; j < (int)(Math.random()*5+1); j++) {

                Question question = new Question();
                question.setQuestionType(Question.QuestionType.MULTICHOICE);
                LinkedHashMap<String,Boolean> lhs = new LinkedHashMap<>();

                for (int k = 0; k < (int)(Math.random()*4+1); k++) {
                    lhs.put(""+k,Math.random()>0.5);
                }
                question.setAnswers(lhs);
                testingData.add(question);
            }

            //Create QuestionnaireManager, and set questions..
            QuestionnaireManager testQuestionnaireManager = new QuestionnaireManager();
            testQuestionnaireManager.setQuestions(testingData);



            //Test questions
            for (Question question: testingData) {

                //Let the QuestionnaireManager create the buttons.
                testQuestionnaireManager.show(mockContext,descriptionView,answerView);

                //update the buttons.
                boolean [] answers = new boolean[question.getAnswerText().size()];

                for (int j = 0; j < answers.length; j++) {
                    answers[j] = question.getAnswerValue(question.getAnswerText().toArray()[j]);
                }


                for (int j = 0; j < answerView.getChildCount(); j++) {
                    Button b = (Button) answerView.getChildAt(j);
                    b.setSelected(answers[j]);
                }

                //Evaluate and progress the manager.
                Assert.assertTrue(testQuestionnaireManager.evaluate());
                testQuestionnaireManager.next();
            }

        }
    }

}
