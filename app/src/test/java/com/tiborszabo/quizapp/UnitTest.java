package com.tiborszabo.quizapp;

import org.junit.Test;

import static org.junit.Assert.*;
import com.tiborszabo.quizapp.objects.Question;
import com.tiborszabo.quizapp.objects.SettingData;
import com.tiborszabo.quizapp.objects.SettingsList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * These tests will be executed on the machine host without the need for an Android device.
 * @author Tibor Peter Szabo
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

            //Test toString() function.
            StringBuilder expected = new StringBuilder();
            expected.append(name+";");
            expected.append(questionType.name()+";");
            expected.append(question+";");
            expected.append(explanation+";");

            for (String answer : lhs.keySet()){
                boolean value = lhs.get(answer);
                expected.append(answer+";"+value+";");
            }

            assertEquals(expected.substring(0,expected.length()-1),testingQuestion.toString());
        }
    }

    /**
     * Tests the SettingData object.
     */
    @Test
    public void test_setting_data(){
        for (int i = 0; i < 50; i++) {
            //create data
            String name = ""+i;
            String value = ""+Math.random();

            //create SettingData object.
            SettingData testSettingData = new SettingData(name,value);

            //Test name and value query.
            assertEquals(name,testSettingData.getName());
            assertEquals(value,testSettingData.getData());

            //create new name and value.
            name += ""+i;
            value += ""+Math.random();

            //change the name and value attribute in the object.
            testSettingData.setName(name);
            testSettingData.setData(value);

            //assert newly added data.
            assertEquals(name,testSettingData.getName());
            assertEquals(value,testSettingData.getData());

            //Check toString function.
            assertEquals(name+";"+value,testSettingData.toString());
        }
    }

    /**
     * Tests the SettingList object.
     */
    @Test
    public void test_settings_list(){
        for (int i = 0; i < 50; i++) {
            //Create the SettingList and the expected data holder.
            SettingsList testSettingsList = new SettingsList();
            List<SettingData> dataList = new ArrayList<>();

            //Fill both the SettingList and the expected data with testing data.
            for (int j = 0; j < 10; j++) {
                String name = j+"";
                String value = Math.random()+"";
                SettingData testingData = new SettingData(name,value);

                dataList.add(testingData);
                testSettingsList.add(testingData);
            }

            for (SettingData expectedData : dataList){

                //Check if data is equal and the object is equal based on the name.
                assertEquals(expectedData.getData(),testSettingsList.getValueByName(expectedData.getName()));
                assertEquals(expectedData,testSettingsList.getObjectByName(expectedData.getName()));
            }

            List<String> stringList = new ArrayList<>();
            for (SettingData expectedData : dataList){
                stringList.add(expectedData.toString());
            }

            assertEquals(stringList,testSettingsList.toStringList());
        }
    }
}