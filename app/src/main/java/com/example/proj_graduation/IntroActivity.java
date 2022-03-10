package com.example.proj_graduation;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Button intro_btn = (Button) findViewById(R.id.intro_btn);
        TextView bof_text = (TextView)findViewById(R.id.textView);
        int color = getResources().getColor(R.color.colorDefault);

        SpannableStringBuilder spannable2 = new SpannableStringBuilder("AR로 즐기는\n드라마 촬영장소");
        spannable2.setSpan(new StyleSpan(Typeface.BOLD),0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // AR
        spannable2.setSpan(new StyleSpan(Typeface.NORMAL),3,7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable2.setSpan(new StyleSpan(Typeface.BOLD),8, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable2.setSpan(new ForegroundColorSpan(color),8, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        bof_text.setText(spannable2, TextView.BufferType.EDITABLE);

        intro_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }

        });

    }
}
