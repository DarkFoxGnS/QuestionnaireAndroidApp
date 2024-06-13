package com.tiborszabo.quizapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tiborszabo.quizapp.R;

/**
 * Display the summary result to the user.
 * @author Tibor Peter Szabo.
 */
public class SummaryPage extends Activity {

    /**
     * Called by the UI manager, display the passed in requirements.
     *      Correct - Integer
     *      All - Integer
     *
     * And displays them to the user.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set UI.
        setContentView(R.layout.summary);

        Bundle extras = getIntent().getExtras();

        //Show score to the user.
        TextView score = findViewById(R.id.Score);
        score.setText(extras.getInt("Correct")+"/"+extras.getInt("All"));
    }

    /**
     * Called when the user presses the Confirm button.
     * @param v View, the calling view.
     */
    public void SummaryOnContinue(View v){
        finish();
    }
}
