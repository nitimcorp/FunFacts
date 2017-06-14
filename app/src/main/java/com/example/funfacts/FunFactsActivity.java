package com.example.funfacts;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Shared preferences are used to keep the data upon destroying the activity. This is basically
 * used to store default values in an app.
 */

public class FunFactsActivity extends AppCompatActivity {
    public static final String TAG = FunFactsActivity.class.getSimpleName();
    // This should be unique to your app.
    private static final String PREFS_FILE = "com.example.funfactsapp.preferences";
    private static final String KEY_COLOR = "KEY_COLOR";
    private static final String KEY_EDIT_TEXT = "KEY_EDIT_TEXT";
    private static final String KEY_FACT = "KEY_FACT";
    private FactBook mFactBook = new FactBook();
    private ColorWheel mColorWheel = new ColorWheel();
    // Declare view variables
    private TextView mFactTextView;
    private Button mNextButton;
    private ConstraintLayout mConstraintLayout;
    private String mFact = mFactBook.mFacts[0];
    private int mColor = Color.parseColor(mColorWheel.mColors[8]);

    // Field to store data after the activity dies.
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    /**
     * This method is used to save the current instance so when the instance is destroyed for
     * many reasons a copy of this can be used to reconstruct it back and make it look like there
     * was no change. For ex - portrait to landscape orientation change.
     * @param outState: param to save the instance.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_FACT, mFact);
        outState.putInt(KEY_COLOR, mColor);
    }

    /**
     * This method is used to get the stored instance and recreate the view as it was before.
     * @param savedInstanceState: instance that was saved.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mFact = savedInstanceState.getString(KEY_FACT);
        mColor = savedInstanceState.getInt(KEY_COLOR);
        mFactTextView.setText(mFact);
        mNextButton.setTextColor(mColor);
        mConstraintLayout.setBackgroundColor(mColor);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun_facts);

        // Assign the views from the layout file to the corresponding variables
        mFactTextView = (TextView) findViewById(R.id.factTextView);
        mNextButton = (Button) findViewById(R.id.nextButton);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        mSharedPreferences = getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        String savedFact = mSharedPreferences.getString(KEY_EDIT_TEXT, "");
        //mFactTextView.setText(savedFact);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // The button was clicked,
                // Update the screen with the dynamic fact
                mFact = mFactBook.getFact();
                mColor = mColorWheel.getColor();
                mFactTextView.setText(mFact);
                mNextButton.setTextColor(mColor);
                mConstraintLayout.setBackgroundColor(mColor);
            }
        };
        mNextButton.setOnClickListener(listener);

        //Toast.makeText(FunFactsActivity.this, "Yes, activity created", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Logging from the OnCreate method");
    }

    /**
     * Used to save the text using the Editor form SharedPreferences.
     */
    @Override
    protected void onPause() {
        super.onPause();
        mEditor.putString(KEY_EDIT_TEXT, mFactTextView.getText().toString());
        mEditor.apply();
    }
}
