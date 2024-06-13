package com.tiborszabo.quizapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tiborszabo.quizapp.R;

/**
 * This activity is used to show the correctness of the answer and the explanation of the question.
 * @author Tibor Peter Szabo.
 */
public class QuestionEvaluationPage extends Activity {

    /**
     * Called by the UI manager, display if the question was coorect or wrong ot the user.
     * Displays the explanation of the question to the user.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set UI.
        setContentView(R.layout.questionevaluationpage);

        Bundle extras = getIntent().getExtras();
        //Show feedback to user.
        TextView feedback = findViewById(R.id.Feedback);
        if (extras.getBoolean("Correct")){
            feedback.setText(R.string.Correct);
        }else{
            feedback.setText(R.string.Wrong);
        }
        //Show explanation to the user.
        TextView explanation = findViewById(R.id.Explanation);
        explanation.setText(extras.getString("Explanation"));
    }

    /**
     * Called when the user presses the confirm button.
     * @param v View, the calling view object.
     */
    public void onContinue(View v){
        finish();
    }

}
