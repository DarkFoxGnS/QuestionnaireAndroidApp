package com.tiborszabo.quizapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tiborszabo.quizapp.R;
import com.tiborszabo.quizapp.managers.FileManager;
import com.tiborszabo.quizapp.managers.QuestionnaireManager;
import com.tiborszabo.quizapp.objects.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This Activity manages the question display, and the answer gathering.
 * @author Tibor Peter Szabo.
 */
public class Questionnaire extends Activity {

    private boolean wasPaused = false;

    private QuestionnaireManager questionnaireManager;
    private int elapsedTime = 0;

    private Timer timer;

    /**
     * This is called by the Android system, when the activity is being created.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create questionnaireManager and set the questionList.
        questionnaireManager = new QuestionnaireManager();

        //Select random questions to display.
        //Create a copy and shuffle it.
        List<Question> questionCopy = new ArrayList<>(MainMenu.questionsList);
        Collections.shuffle(questionCopy);

        //Select the first questions, that is limited by the user selected amount. Maximum of 15.
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < questionCopy.size(); i++) {
            questions.add(questionCopy.get(i));
            if (i > Integer.parseInt(MainMenu.settingsList.getValueByName("questioncount"))){
                break;
            }
        }

        questionnaireManager.setQuestions(questions);

        //set UI.
        setContentView(R.layout.questionnaire);
        displayQuestion();
    }

    /**
     * Displays the question and the answer views on the screen.
     * Requires the questionManager to be initialized.
     */
    private void displayQuestion(){
        //Display the question to the user by calling questionnaireManager.show().
        ScrollView descriptionView = findViewById(R.id.descriptionView);
        LinearLayout answerView = findViewById(R.id.answerView);
        Questionnaire context = this;
        questionnaireManager.show(this,descriptionView,answerView);

        //If required create a timer, based on the user setting.
        if (Integer.parseInt(MainMenu.settingsList.getValueByName("questionseconds")) != 0){
            this.elapsedTime = 0;
            timer = new Timer();
            timer.schedule(new TimerTask(){
                @Override
                public void run() {
                    //update UI to represent the remaining time, and if the time is up confirm the question.
                    int maxTime = Integer.parseInt(MainMenu.settingsList.getValueByName("questionseconds"));
                    ProgressBar timerBar = findViewById(R.id.timerBar);
                    timerBar.setProgress(100-(int)((float) elapsedTime /maxTime*100));
                    if (elapsedTime == maxTime){
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(() -> {
                            Toast.makeText(context,"Times up!",Toast.LENGTH_SHORT).show();
                            onConfirm(null);
                        });
                    }
                    elapsedTime += 1;
                }
            },1000,1000);
        }
    }

    /**
     * Called by the UI manager, when the user submits their answer.
     * @param view the calling View object.
     */
    public void onConfirm(View view){
        //Clear the timer.
        if (timer != null){
            timer.cancel();
            timer.purge();
        }
        //Evaluate the question, create an intent to show the evaluation page, pass in the correctness and the explanation of the question.
        boolean correct = questionnaireManager.evaluate();
        String explanation = questionnaireManager.getQuestion().getExplanation();

        Intent i = new Intent(getApplicationContext(),QuestionEvaluationPage.class);
        i.putExtra("Correct",correct);
        i.putExtra("Explanation",explanation);
        startActivity(i);

    }

    /**
     * Called by the UI manager, when the user leaves this Activity.
     */
    @Override
    protected void onPause() {
        //To ensure that the Activity was left, since the onResume is called on start as well.
        super.onPause();
        this.wasPaused = true;
    }

    /**
     * Called by the UI manager, when the user returns to this Activity.
     */
    @Override
    protected void onResume() {
        super.onResume();
        //Leave if it was not paused.
        if(!this.wasPaused){
            return;
        }

        //Loads the next question, and if there are no more question left show the summary page.
        boolean hasMore = questionnaireManager.next();

        if(!hasMore){
            Intent i = new Intent(getApplicationContext(),SummaryPage.class);
            i.putExtra("All",questionnaireManager.getAll());
            i.putExtra("Correct",questionnaireManager.getCorrect());
            startActivity(i);
            finish();
        }else {
            displayQuestion();
        }
    }

    /**
     * Called by the UI manager, when the user leaves this Activity.
     */
    @Override
    protected void onStop() {
        //Clear timer, since it can run without this activity.
        if (timer != null){
            timer.cancel();
            timer.purge();
        }
        super.onStop();
    }
}
