package com.tiborszabo.quizapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.tiborszabo.quizapp.R;
import com.tiborszabo.quizapp.objects.Question;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This Activity is used to display and modify the attributes of a question.
 *@author Tibor Peter Szabo
 */
public class QuestionAttribute extends Activity {

    private boolean isNewQuestion = false;
    private Question question;
    private List<View> answersList = new ArrayList<>();

    private int spinnerLocation = 0;

    /**
     * Called by the UI manager when the user start the activity.
     * If an object was passed in with the intend, set it as question.
     * If no object was passed in, create a new object and set isNewQuestion to true.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.questionattribute);
        Bundle extras = getIntent().getExtras();
        //Load question if possible.
        if (extras != null) {
            question = (Question) extras.getSerializable("Question");
        }else{
            question = new Question();
            isNewQuestion = true;
        }
        //Set name, question,explanation,type and answers to the correct UI elment.
            EditText name = findViewById(R.id.QuestionNameEditText);
            EditText questionText = findViewById(R.id.QuestionQuestionEditText);
            EditText explanation = findViewById(R.id.QuestionExplanantionEditText);
            Spinner type = findViewById(R.id.QuestionAttributeType);

            name.setText(question.getName());
            questionText.setText(question.getQuestion());
            explanation.setText(question.getExplanation());

            //Load and set up spinner.
            List<String> types = new ArrayList<>();
            spinnerLocation = 0;
            int i = 0;
            for (Question.QuestionType qt: Question.QuestionType.values()) {
                types.add(qt.name());
                if (qt.equals(question.getQuestionType())){
                    spinnerLocation = i;
                }
                i++;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,types);
            type.setAdapter(adapter);
            type.setSelection(spinnerLocation);
            type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spinnerLocation != position){
                        spinnerLocation = position;
                        resetAnswers(Question.QuestionType.valueOf(types.get(position)));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            if (this.question.getAnswerText().size() == 0){
                resetAnswers(question.getQuestionType());
            }else{
                fillAnswers();
            }

    }

    /**
     * Clears the answerList and prepares it for new information to be added.
     * @param newQuestionType QuestionType, the new type of the question.
     */
    private void resetAnswers(Question.QuestionType newQuestionType){
        LinearLayout answers = findViewById(R.id.QuestionAttribute_AnswerHolder);
        //clear answerList array and answers UI element.
        answers.removeAllViews();
        answersList.clear();
        //A switch give more flexibility for possible additions.
        switch (newQuestionType){
            case TEXT:
                EditText textAnswer = new EditText(QuestionAttribute.this);
                textAnswer.setText("Answer");
                answersList.add(textAnswer);
                answers.addView(textAnswer);
                break;
            case MULTICHOICE:
            case SINGLECHOICE:
                Button addAnswers = new Button(new ContextThemeWrapper(QuestionAttribute.this,R.style.PrimaryButton));
                addAnswers.setText("Add more answer");
                addAnswers.setOnClickListener(v -> {
                    if(answersList.size() == 8){
                        return;
                    }
                    EditText answer = new EditText(QuestionAttribute.this);
                    answer.setText("Answer");
                    Switch valid = new Switch(QuestionAttribute.this);
                    valid.setText("Is correct?");

                    answersList.add(answer);
                    answersList.add(valid);

                    answers.addView(answer);
                    answers.addView(valid);

                });
                answers.addView(addAnswers);
                break;
        }
    }

    /**
     * Fills the answerList with data from an already existing question object.
     */
    private void fillAnswers(){
        LinearLayout answers = findViewById(R.id.QuestionAttribute_AnswerHolder);

        //A switch give more flexibility for possible additions.
        switch (question.getQuestionType()){
            case TEXT:
                //Get the first element of the answers of the question, since the text type question can't have more answers.
                EditText textAnswer = new EditText(QuestionAttribute.this);
                textAnswer.setText((String)question.getAnswerText().toArray()[0]);
                answersList.add(textAnswer);
                answers.addView(textAnswer);
                break;
            case MULTICHOICE:
            case SINGLECHOICE:
                //Add a button to generate more answers, 8 is the maximum.
                Button addAnswers = new Button(new ContextThemeWrapper(QuestionAttribute.this,R.style.PrimaryButton));
                addAnswers.setText("Add more answer");
                addAnswers.setOnClickListener(v -> {
                    if(answersList.size() == 8){
                        Toast.makeText(this, "You've reached the maximum amount of answers.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    EditText answer = new EditText(QuestionAttribute.this);
                    answer.setText("Answer");
                    Switch valid = new Switch(QuestionAttribute.this);
                    valid.setText("Is correct?");
                    //add EditText and Switch to the array.
                    answersList.add(answer);
                    answersList.add(valid);

                    //add EditText and Switch to the UI.
                    answers.addView(answer);
                    answers.addView(valid);

                });

                //Load existing answers.
                answers.addView(addAnswers);
                for (String answerText : question.getAnswerText()) {
                    boolean value = question.getAnswerValue(answerText);

                    EditText answer = new EditText(QuestionAttribute.this);
                    answer.setText(answerText);
                    Switch valid = new Switch(QuestionAttribute.this);
                    valid.setText("Is correct?");
                    valid.setChecked(value);

                    answersList.add(answer);
                    answersList.add(valid);

                    answers.addView(answer);
                    answers.addView(valid);
                }
                break;
        }
    }

    /**
     * Called when the user presses the delete function.
     * Asks the user if they would like ot delete the question, sends an intent to the previous activity to delete this object from the list.
     * @param v View, the calling view.
     */
    public void onQuestionAttributeDelete(View v){
        //If the question is new, the user can freely leave.
        if (this.isNewQuestion){
            finish();
            return;
        }
        //Prompt the user if they would wish to proceed with the removal of the object.
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.delete);
        alert.setMessage("You are about to delete this question, are you sure?");
        alert.setPositiveButton("Yes",(dialog, which) -> {
            Intent returningIntent = new Intent();
            returningIntent.putExtra("delete",true);
            setResult(Activity.RESULT_OK, returningIntent);
            finish();
        });
        alert.show();
    }

    /**
     * Called when the user presses the save item.
     * Sends an intent back to the previous activity to save this object.
     * @param v View, the calling view.
     */
    public void onQuestionAttributeSave(View v){
        //Get UI elements.
        EditText name = findViewById(R.id.QuestionNameEditText);
        EditText questionText = findViewById(R.id.QuestionQuestionEditText);
        EditText explanation = findViewById(R.id.QuestionExplanantionEditText);
        Spinner type = findViewById(R.id.QuestionAttributeType);
        LinearLayout answers = findViewById(R.id.QuestionAttribute_AnswerHolder);

        //Set data from UI to the question object.
        this.question.setName(name.getText().toString());
        this.question.setQuestion(questionText.getText().toString());
        this.question.setExplanation(explanation.getText().toString());
        this.question.setQuestionType(Question.QuestionType.valueOf((String) type.getAdapter().getItem(type.getSelectedItemPosition())));

        //Fill answers and assign to the question object.
        LinkedHashMap<String,Boolean> answersLinkedHashMap = new LinkedHashMap<>();
        if (this.question.getQuestionType().equals(Question.QuestionType.TEXT)){
            EditText et = (EditText) this.answersList.get(0);
            answersLinkedHashMap.put(et.getText().toString(),true);
        }else{
            for (int i = 0; i < this.answersList.size(); i++) {
                EditText answer = (EditText) this.answersList.get(i);
                i++;
                Switch value = (Switch) this.answersList.get(i);
                answersLinkedHashMap.put(answer.getText().toString(),value.isChecked());
            }
        }
        this.question.setAnswers(answersLinkedHashMap);

        //Create a return intent to send the object and other parameters to the previous activity.
        Intent returningIntent = new Intent();
        returningIntent.putExtra("isNew",isNewQuestion);
        returningIntent.putExtra("delete",false);
        returningIntent.putExtra("question",this.question);
        setResult(Activity.RESULT_OK, returningIntent);
        finish();
    }
}
