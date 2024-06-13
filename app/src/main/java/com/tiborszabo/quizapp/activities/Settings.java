package com.tiborszabo.quizapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tiborszabo.quizapp.managers.FileManager;
import com.tiborszabo.quizapp.R;

/**
 * This activity handles the Settings menu.
 * @author Tibor Peter Szabo.
 */
public class Settings extends Activity {

    /**
     * This is called by the Android system, whenever the activity is being created.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Set the UI.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }

    /**
     * This is called by the Android system, whenever the activity is being started.
     */
    @Override
    protected void onStart() {
        super.onStart();
        //Username
        //Sets the username to the username setting.
        EditText usernameInput = findViewById(R.id.UsernameEditText);
        usernameInput.setText(MainMenu.settingsList.getValueByName("username"));

        //Question count
        //Set the question count to the question count setting.
        SeekBar questionCountSeekBar = findViewById(R.id.QuestionCountSeekBar);
        questionCountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView questionCountFeedback = findViewById(R.id.QuestionCountFeedback);
                questionCountFeedback.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        TextView questionCountFeedback = findViewById(R.id.QuestionCountFeedback);
        questionCountSeekBar.setProgress(Integer.parseInt(MainMenu.settingsList.getValueByName("questioncount")));
        questionCountFeedback.setText(MainMenu.settingsList.getValueByName("questioncount"));

        //Seconds
        //Sets the timer to the setting questionsecond. If set to 0 disable it.
        SeekBar secondsSeekBar = findViewById(R.id.SecondsSeekBar);
        secondsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress/5*5;
                seekBar.setProgress(progress);
                TextView secondsFeedback = findViewById(R.id.SecondsFeedback);
                if (progress == 0){
                    secondsFeedback.setText("Disabled");
                }else{
                    secondsFeedback.setText(""+progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        TextView secondsFeedback = findViewById(R.id.SecondsFeedback);
        secondsSeekBar.setProgress(Integer.parseInt(MainMenu.settingsList.getValueByName("questionseconds")));
        if (Integer.parseInt(MainMenu.settingsList.getValueByName("questionseconds")) == 0){
            secondsFeedback.setText("Disabled");
        }else{
            secondsFeedback.setText(MainMenu.settingsList.getValueByName("questionseconds"));
        }

        //Repeat
        //Sets the repetition of the last question to the value of the loaded in value from the settingList.
        Switch repeatInput = findViewById(R.id.RepeatInput);

        if (Boolean.parseBoolean(MainMenu.settingsList.getValueByName("repeatmissedquestions"))){
            repeatInput.setChecked(true);
            repeatInput.setText(R.string.True);
        }else{
            repeatInput.setChecked(false);
            repeatInput.setText(R.string.False);
        }

        repeatInput.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    buttonView.setText(R.string.True);
                }else{
                    buttonView.setText(R.string.False);
                }
            }
        });

    }


    /**
     * Called by the UI manager, whenever the user saves their settings.
     * @param view the calling View object.
     */
    public void onSave(View view){
        //Saves the data to the settingList and to the local directory.
        Toast.makeText(this,"Settings saved",Toast.LENGTH_SHORT).show();
        EditText usernameInput = findViewById(R.id.UsernameEditText);
        TextView questionCountFeedback = findViewById(R.id.QuestionCountFeedback);
        SeekBar secondsSeekBar = findViewById(R.id.SecondsSeekBar);
        Switch repeatInput = findViewById(R.id.RepeatInput);


        MainMenu.settingsList.setValueByName("username",""+usernameInput.getText());
        MainMenu.settingsList.setValueByName("questioncount",""+questionCountFeedback.getText());
        MainMenu.settingsList.setValueByName("questionseconds",""+secondsSeekBar.getProgress());
        MainMenu.settingsList.setValueByName("repeatmissedquestions",""+repeatInput.isChecked());

        FileManager.setData(getApplicationContext(),"settings.dat", MainMenu.settingsList.toStringList());
        finish();
    }



}
