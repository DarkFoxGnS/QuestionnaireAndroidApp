package com.tiborszabo.quizapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tiborszabo.quizapp.R;
import com.tiborszabo.quizapp.managers.FileManager;
import com.tiborszabo.quizapp.objects.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity display all the questions to the user and lets them access those questions or to add a new one.
 * @author Tibor Peter Szabo.
 */
public class QuestionSettings extends Activity {

    /**
     * Called by the UI manager,called first when the user starts the activity.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set UI.
        setContentView(R.layout.questionsettings);

        //Define the add new question functionality.
        Button createNew = findViewById(R.id.AddNewQuestionButton);
        createNew.setOnClickListener(v -> {
            Intent i = new Intent(this,QuestionAttribute.class);
            startActivityForResult(i,0);
        });
    }

    /**
     * Called by the UI manager, when the user starts the activity.
     */
    @Override
    protected void onResume() {
        super.onResume();
        //clear views and reload all the buttons, since if a question was modified, but not updated on this screen, the screen and the memory can become asynced.
        LinearLayout questionList = findViewById(R.id.QuestionList);
        questionList.removeAllViews();
        for (int i = 0; i < MainMenu.questionsList.size(); i++) {
            //Create buttons for every question.
            Question question = MainMenu.questionsList.get(i);

            Button questionButton = new Button(new ContextThemeWrapper(this, R.style.PrimaryButton));
            if (question.getName().length() > 10) {
                questionButton.setText(question.getName().substring(0, 10) + "...");
            } else {
                questionButton.setText(question.getName());
            }
            //i is re-assigned as a local variable to be used in the lambda.
            //Every button is assigned an ID that is used as the intent resultId.
            int finalI = i;
            questionButton.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), QuestionAttribute.class);
                intent.putExtra("Question", question);
                startActivityForResult(intent, finalI);
            });

            questionList.addView(questionButton);
        }
    }

    /**
     * Called by the UI manager when the user returns from the previous activity.
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If the user backs into this activity, the data will be null.
        if (data == null){
            return;
        }
        //If the user deletes the question then the rest of the method can be left out.
        Bundle returnData = data.getExtras();
        if (returnData.getBoolean("delete")){
            MainMenu.questionsList.remove(requestCode);
            saveQuestions();
            return;
        }
        // If there were no question passed in originally, isNew will return true, meaning that it was created.
        if(returnData.getBoolean("isNew")){
            MainMenu.questionsList.add((Question) returnData.getSerializable("question"));
            saveQuestions();
            return;
        }
        //If none of the previous happens, then a question was passed in, so we set the new question as the old question based on the call id of the Intent.
        MainMenu.questionsList.set(requestCode,(Question) returnData.getSerializable("question"));
        saveQuestions();
    }

    /**
     * Saves the questions into the local directory.
     */
    private void saveQuestions(){
        //Converts the question to String, and write it to the local directory.
        List<String> saveData = new ArrayList<>();
        for (Question question : MainMenu.questionsList) {
            saveData.add(question.toString());
        }
        FileManager.setData(getApplicationContext(),"questions.dat",saveData);
    }
}
