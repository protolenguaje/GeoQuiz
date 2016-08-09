package com.bignerdranch.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import static  android.view.View.VISIBLE;
import static  android.view.View.INVISIBLE;


public class CheatActivity extends AppCompatActivity {

    private static final String TAG = CheatActivity.class.getSimpleName();

    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";

    private static final String KEY_ANSWER_SHOWN = "answer_shown";
    private static final String KEY_ANSWER_IS_TRUE = "answer_is_true";

    private boolean mAnswerIsTrue;
    private boolean mAnswerShown;

    private TextView mAnswerTextView;
    private Button mShowAnswer;

    private TextView mApiLevelDeviceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        mShowAnswer = (Button) findViewById(R.id.show_answer_button);
        mShowAnswer.setOnClickListener(view -> {
            setAnswerTextView(mAnswerIsTrue);
            setAnswerShownResult(true);
            buttonAnimation();
        });

        mApiLevelDeviceTextView = (TextView) findViewById(R.id.api_level_text_view);
        mApiLevelDeviceTextView.setText("API Level " + Build.VERSION.SDK_INT);

        if (savedInstanceState != null) {

            mAnswerIsTrue = savedInstanceState.getBoolean(KEY_ANSWER_IS_TRUE, false);

            if (savedInstanceState.getBoolean(KEY_ANSWER_SHOWN, false)) {
                setAnswerShownResult(true);
                setAnswerTextView(mAnswerIsTrue);
            }
        }
    }


    private void setAnswerTextView(boolean answerIsTrue) {

        if (answerIsTrue) {
            mAnswerTextView.setText(R.string.true_button);
        } else {
            mAnswerTextView.setText(R.string.false_button);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_ANSWER_SHOWN, mAnswerShown);
        savedInstanceState.putBoolean(KEY_ANSWER_IS_TRUE, mAnswerIsTrue);
    }

    private void setAnswerShownResult(boolean isAnswerShown) {

        mAnswerShown = isAnswerShown;
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, mAnswerShown);
        setResult(RESULT_OK, data);
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {

        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    public static boolean wasAnswerShown(Intent result) {

        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }


    private void buttonAnimation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            int cx = mShowAnswer.getWidth() / 2;
            int cy = mShowAnswer.getHeight() / 2;
            float radius = mShowAnswer.getWidth();
            Animator anim = ViewAnimationUtils
                    .createCircularReveal(mShowAnswer, cx, cy, radius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mAnswerTextView.setVisibility(VISIBLE);
                    mShowAnswer.setVisibility(INVISIBLE);
                }
            });
        }
    }
}
