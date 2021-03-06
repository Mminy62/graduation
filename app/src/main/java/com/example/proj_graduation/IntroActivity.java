package com.example.proj_graduation;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Button intro_btn = (Button) findViewById(R.id.intro_btn);
        TextView bof_text = (TextView)findViewById(R.id.textView);
        int color = ContextCompat.getColor(this, R.color.colorDefault);

        SpannableStringBuilder spannable2 = new SpannableStringBuilder("Filming Locations \nwith AR");
        spannable2.setSpan(new ForegroundColorSpan(color),0, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // AR
        spannable2.setSpan(new StyleSpan(Typeface.BOLD),24, 26, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        bof_text.setText(spannable2, TextView.BufferType.EDITABLE);

        intro_btn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
        });

    }
}
