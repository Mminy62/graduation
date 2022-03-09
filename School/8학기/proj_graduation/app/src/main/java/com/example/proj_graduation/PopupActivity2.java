package com.example.proj_graduation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class PopupActivity2 extends AppCompatActivity {

    com.example.proj_graduation.GameSystem gameSystem;

    Button finishBtn;
    TextView textView;
    TextView finalScore;
    int currentScore;
    String scoreString;




    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup2);


        finalScore = (TextView)findViewById(R.id.finalScore);

        Intent intent = getIntent();
        String scoreString = intent.getExtras().getString("Score");

        int length = scoreString.length();
        SpannableStringBuilder spannable = new SpannableStringBuilder(scoreString);
        spannable.setSpan(new AbsoluteSizeSpan(60),0, length-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new AbsoluteSizeSpan(45),length-1, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        finalScore.setText(spannable, TextView.BufferType.EDITABLE);

        finishBtn = (Button)findViewById(R.id.finishbtn);

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.example.proj_graduation.MainActivity.ma.finish();
                finish();
                //popup, info activity 까지 삭제하면 stack 거꾸로여서 안되는것같음.

            }
        });

    }
}
