package com.tiborszabo.quizapp.managers;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tiborszabo.quizapp.R;
import com.tiborszabo.quizapp.activities.MainMenu;
import com.tiborszabo.quizapp.objects.Question;

import org.w3c.dom.Text;

import java.io.Console;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * QuestionnaireManager can be used to hold, evaluate and display the contents of the Question objects into the application.
 * @author Tibor Peter Szabo.
 */
public class QuestionnaireManager {

    private List<Question> questions;
    private List<Question> completedQuestions;
    private List<Question> failedQuestions;
    private int questionIndex;
    private List<View> views;
    private boolean isQuestionsRepeated = false;

    /**
     * Initializes the QuestionManager object.
     */
    public QuestionnaireManager(){
        this.completedQuestions = new ArrayList<>();
        this.failedQuestions = new ArrayList<>();
        this.questions = new ArrayList<>();
        this.views = new ArrayList<>();
    }

    /**
     * Sets question of the object to question.
     * @param questions List of Questions, which will be set into the object.
     */
    public void setQuestions(List<Question> questions){
        this.questions = questions;
    }

    /**
     *Evaluates the currently active question.
     * @return if the question was answered correctly.
     */
    public Boolean evaluate() {
        Question currentQuestion = this.questions.get(this.questionIndex);
        boolean correct = true;
        //The use of switch allows for easier implementation of new code if required.
        //Compare the given answers to the expected answers.
        switch (currentQuestion.getQuestionType()){
            case SINGLECHOICE:
            case MULTICHOICE:
                for (View view:this.views) {
                    Button b = (Button) view;
                    if(b.isSelected() != currentQuestion.getAnswerValue(b.getText())){
                        correct = false;
                    }
                }
                break;
            case TEXT:
                EditText answer = (EditText) this.views.get(0);
                correct = answer.getText().toString().toLowerCase().equals(this.questions.get(this.questionIndex).getAnswerText().toArray()[0].toString().toLowerCase());
                break;
        }
        //If no difference was found, save it to the completed questions.
        if (correct){
            this.completedQuestions.add(currentQuestion);
        }else{
            this.failedQuestions.add(currentQuestion);
        }

        return correct;
    }

    /**
     * Advances the questionIndex, and returns if there are any question left.
     * @return Boolean, if any question left.
     */
    public boolean next(){
        this.questionIndex++;
        //If there are no more questions and the question repetition is turned on, then the failed questions are reloaded.
        if(!(this.questionIndex < this.questions.size()) &&
                Boolean.parseBoolean(MainMenu.settingsList.getValueByName("repeatmissedquestions")) &&
                !this.failedQuestions.isEmpty() &&
                !this.isQuestionsRepeated){

            this.isQuestionsRepeated = true;
            this.questionIndex = 0;
            this.questions.clear();
            this.questions = this.failedQuestions;
            this.failedQuestions = new ArrayList<>();

        }

        return this.questionIndex < this.questions.size();
    }

    /**
     *Displays the content of the Question object onto the inputted holding View objects.
     * @param context type of android.content.Context, it is used as a context for the newly created elements.
     * @param descriptionView holds the a ScrollView layout, that will hold the question.
     * @param answerView holds a LinearLayout, that will hold the answering View objects.
     */
    public void show(android.content.Context context, ScrollView descriptionView, LinearLayout answerView) {
        descriptionView.removeAllViews();
        answerView.removeAllViews();
        this.views.clear();
        //Display the question and their answers based on the questionType of the question.
        switch (this.questions.get(this.questionIndex).getQuestionType()){
            case TEXT:
                showText(context,descriptionView,answerView);
                break;
            case MULTICHOICE:
                showMultichoice(context, descriptionView, answerView);
                break;
            case SINGLECHOICE:
                showSinglechoice(context, descriptionView, answerView);
                break;
        }

    }

    /**
     * Shows a Text input form.
     * @param context type of android.content.Context, it is used as a context for the newly created elements.
     * @param descriptionView holds the a ScrollView layout, that will hold the question.
     * @param answerView holds a LinearLayout, that will hold the answering View objects.
     *
     */
    private void showText(android.content.Context context, ScrollView descriptionView, LinearLayout answerView){
        //Set the question.
        TextView description = new TextView(context);
        description.setText(this.questions.get(this.questionIndex).getQuestion());
        descriptionView.addView(description);

        //Add an answer holder.
        EditText answer = new EditText(context);
        this.views.add(answer);
        answerView.addView(answer);
    }

    /**
     * Shows a Single choice input form.
     * @param context type of android.content.Context, it is used as a context for the newly created elements.
     * @param descriptionView holds the a ScrollView layout, that will hold the question.
     * @param answerView holds a LinearLayout, that will hold the answering View objects.
     *
     */
    private void showSinglechoice(android.content.Context context, ScrollView descriptionView, LinearLayout answerView){
        //Dispaly question.
        TextView description = new TextView(context);
        description.setText(this.questions.get(this.questionIndex).getQuestion());
        descriptionView.addView(description);

        //Display answers and make them exclusively toggleable.
        Set<String> answers = this.questions.get(this.questionIndex).getAnswerText();
        for (String answer : answers){
            Button b = new Button(context);
            b.setId(View.generateViewId());
            b.setText(answer);

            Resources.Theme theme = context.getTheme();
            TypedValue buttonInactiveColor = new TypedValue();
            theme.resolveAttribute(R.attr.buttonInactive,buttonInactiveColor,true);
            b.getBackground().setTint(buttonInactiveColor.data);


            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v;
                    TypedValue buttonActiveColor = new TypedValue();
                    TypedValue buttonInactiveColor = new TypedValue();
                    Resources.Theme theme = context.getTheme();
                    theme.resolveAttribute(R.attr.buttonActive,buttonActiveColor,true);
                    theme.resolveAttribute(R.attr.buttonInactive,buttonInactiveColor,true);

                    for (View view: views) {
                        Button button = (Button) view;
                        button.setSelected(false);
                        button.getBackground().setTint(buttonInactiveColor.data);

                    }

                    b.setSelected(!b.isSelected());
                    if (b.isSelected()){
                        b.getBackground().setTint(buttonActiveColor.data);
                    }else{
                        b.getBackground().setTint(buttonInactiveColor.data);
                    }
                }
            });
            this.views.add(b);
            answerView.addView(b);
        }
    }

    /**
     * Shows a Multi choice form.
     * @param context type of android.content.Context, it is used as a context for the newly created elements.
     * @param descriptionView holds the a ScrollView layout, that will hold the question.
     * @param answerView holds a LinearLayout, that will hold the answering View objects.
     *
     */
    private void showMultichoice(android.content.Context context, ScrollView descriptionView, LinearLayout answerView){
        //Display question.
        TextView description = new TextView(context);
        description.setText(this.questions.get(this.questionIndex).getQuestion());
        descriptionView.addView(description);

        //Display the possible answers, and make them toggleable.
        Set<String> answers = this.questions.get(this.questionIndex).getAnswerText();
        for (String answer : answers){
            Button b = new Button(new ContextThemeWrapper(context,R.style.SecondaryButton));
            b.setId(View.generateViewId());
            b.setText(answer);

            TypedValue buttonInactiveColor = new TypedValue();
            Resources.Theme theme = context.getTheme();
            theme.resolveAttribute(R.attr.buttonInactive,buttonInactiveColor,true);
            b.getBackground().setTint(buttonInactiveColor.data);

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v;
                    b.setSelected(!b.isSelected());

                    TypedValue buttonActiveColor = new TypedValue();
                    TypedValue buttonInactiveColor = new TypedValue();
                    Resources.Theme theme = context.getTheme();
                    theme.resolveAttribute(R.attr.buttonActive,buttonActiveColor,true);
                    theme.resolveAttribute(R.attr.buttonInactive,buttonInactiveColor,true);

                    if (b.isSelected()){
                        b.getBackground().setTint(buttonActiveColor.data);
                    }else{
                        b.getBackground().setTint(buttonInactiveColor.data);
                    }
                }
            });
            this.views.add(b);
            answerView.addView(b);
        }
    }


    /**
     * Gets the current question.
     * @return Question, the current question.
     */
    public Question getQuestion() {
        return this.questions.get(this.questionIndex);
    }

    /**
     * Returns the number of all failed or completed questions.
     * @return Integer, number of completed or failed question.
     */
    public int getAll() {
        return this.completedQuestions.size()+this.failedQuestions.size();
    }

    /**
     * Returns the number of correct question.
     * @return Integer, the number of correctly answered question.
     */
    public int getCorrect() {
        return this.completedQuestions.size();
    }
}
