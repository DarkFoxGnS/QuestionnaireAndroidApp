package com.tiborszabo.quizapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tiborszabo.quizapp.R;
import com.tiborszabo.quizapp.managers.FileManager;
import com.tiborszabo.quizapp.objects.Question;
import com.tiborszabo.quizapp.objects.SettingData;
import com.tiborszabo.quizapp.objects.SettingsList;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the main menu, it is displayed on the Launch of the program.
 * @author Tibor Peter Szabo
 */
public class MainMenu extends Activity {

    /**
     * A special subclass of ArrayList holding all the SettingData objects.
     */
    public static SettingsList settingsList = new SettingsList();

    /**
     * An ArrayList containing all questions.
     */
    public static List<Question> questionsList = new ArrayList<>();

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadSettings();
        loadQuestions();

        //If there are no questions present, set demo data.
        if (questionsList.size() == 0){
            FileManager.dryWrite(getApplicationContext(), "questions.dat",
                "Division;MULTICHOICE;Which of the following is dividable by 2;Only 2 and 4 are dividable by 2;2;true;1;false;3;false;4;true\r\n" +
                    "Hello World;TEXT;What is the first sentence a programmer should print in a new programming language;Amongst programmer it is a tradition, that Hello World should be the first printed text.;Hello World;true\r\n" +
                    "42;SINGLECHOICE;According to The Hitchhiker's Guide to the Galaxy, what is the meaning of life.; According to The Hitchhiker's Guide to the Galaxy 42 is the meaning of life;42;true;100;false;50;false\r\n" +
                    "AlphabetFirst;SINGLECHOICE;Which is the first letter of the alphabet?;The first letter of the alphabet is the A;B;false;C;false;C;false;A;true\r\n" +
                    "Typing;TEXT;Type the following sentence: \"This is a brown dog\";There is no explanation;This is a brown dog;true\r\n" +
                    "Binary;SINGLECHOICE;In decimal number system, what does the following binary code mean 0011.;0011 means 3 in decimal.;0;false;1;false;2;false;3;true\r\n" +
                    "Button;SINGLECHOICE;Select the button;;Button;true\r\n" +
                    "NoButton;SINGLECHOICE; Don't select the button;;Button;false\r\n" +
                    "Water;MULTICHOICE;What is in water?;Water is made up of 2 Hydrogen and 1 Oxygen;Oxygen;true;Argon;false;Nitrogen;false;Hydrogen;true\r\n" +
                    "Smiley;SINGLECHOICE;How is the commonly known smiley drawn?;:);(:;false;<:);false;~_~;false;:);true\r\n" +
                    "Fox;SINGLECHOICE;What is the Latin name for the red fox?;The latin name of the red fox is Vulpes Vulpes;Vulpes Vulpes;true;Canis lupus familiaris;false;Felis catus;false\r\n" +
                    "Cat;SINGLECHOICE;What is the Latin name for the cat?;The latin name of the cat is Felis catus;Vulpes Vulpes;false;Canis lupus familiaris;false;Felis catus;true\r\n" +
                    "Dog;SINGLECHOICE;What is the Latin name for the dog?;The latin name of the dog is Canis lupus familiaris;Vulpes Vulpes;false;Canis lupus familiaris;true;Felis catus;false\r\n" +
                    "Easter Egg;SINGLECHOICE;Which game coined the term Easter Egg?;Adventure was the first game that used the term Easter Egg.;Adventure(1980);true;Moonlander(1973);false;Gradius(1985);false\r\n" +
                    "Creator;SINGLECHOICE;Who is the creator of this software?;Yes, it is me Tibor Peter Szabo.;Pan Peter;false;Tinker Bell;false;Captain Hook;false;Tibor Peter Szabo;true\r\n" +
                    "Draw;TEXT;Draw a the most iconic smiley.;:);:);true\r\n" +
                    "AlphabetLast;TEXT;Which is the last character of the alphabet?;It is the Z;Z;true\r\n" +
                    "Galileo;SINGLECHOICE;Complete the name Galileo _______.;Galileo Galilei;Galilei;true;DaVinci;false;Ronald;false;Mike;false\r\n" +
                    "Greece Letters;MULTICHOICE;Which of the following is part of the Greece alphabet?;Alpha,Beta,Delta;Alpha;true;Echo;false;Beta;true;Delta;true");
            loadQuestions();
        }

        setContentView(R.layout.main);

    }

    /**
     * Called when the user presses the Start button.
     * @param v, the view of the calling object.
     */
    public void onStart(View v) {
        Intent intent = new Intent(getApplicationContext(), Questionnaire.class);
        startActivity(intent);
    }

    /**
     * Called when the user presses the Setting button.
     * @param v, the view of the calling object.
     */
    public void onSettings(View v) {
        Intent intent = new Intent(getApplicationContext(), Settings.class);
        startActivity(intent);
    }

    /**
     * Called when the user presses the Questions button.
     * @param v, the view of the calling object.
     */
    public void onQuestions(View v) {
        Intent intent = new Intent(getApplicationContext(), QuestionSettings.class);
        startActivity(intent);
    }

    /**
     * Loads the settings from persistent storage if possible, else sets the settingsList to the default values.
     */
    private void loadSettings() {
        List<String> list = FileManager.getData(getApplicationContext(),"settings.dat");
        settingsList = loadDefaultSettings();


        for (String item : list) {
            String[] dataArray = item.split(";");

            for (int i = 0; i < dataArray.length; i++) {
                dataArray[i] = dataArray[i].replace("%59",";");
            }
            settingsList.setValueByName(dataArray[0],dataArray[1]);
        }

    }

    /**
     *Creates a list of settings, that hold default settings data.
     * @return list, containing the default values for settings.
     */
    private SettingsList loadDefaultSettings() {
        SettingsList list= new SettingsList();
        list.add(new SettingData("username","User"));
        list.add(new SettingData("questioncount","5"));
        list.add(new SettingData("questionseconds","30"));
        list.add(new SettingData("repeatmissedquestions","true"));

        return list;
    }

    /**
     * Loads questions from the local directory.
     */
    private void loadQuestions(){
        List<String> questions = FileManager.getData(getApplicationContext(),"questions.dat");
        for(int j = 0; j < questions.size();j++){
            String[] questionsArray = questions.get(j).split(";");

            Question question = new Question();

            for (int i = 0; i < questionsArray.length;i++) {
                questionsArray[i] = questionsArray[i].replace("%59", ";");
                switch (i) {
                    case 0:
                        question.setName(questionsArray[i]);
                        break;
                    case 1:
                        question.setQuestionType(Question.QuestionType.valueOf(questionsArray[i]));
                        break;
                    case 2:
                        question.setQuestion(questionsArray[i]);
                        break;
                    case 3:
                        question.setExplanation(questionsArray[i]);
                        break;
                    default:
                        question.putAnswer(questionsArray[i],Boolean.parseBoolean(questionsArray[i+1]));
                        i++;
                }
            }
            MainMenu.questionsList.add(question);
        }
    }
}