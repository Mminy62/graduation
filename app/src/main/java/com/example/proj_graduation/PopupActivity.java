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


public class PopupActivity extends AppCompatActivity {

    Button finishBtn;
    TextView textView;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup2);


        finishBtn = (Button)findViewById(R.id.finishbtn);

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.proj_graduation.MainActivity.class);
                intent.putExtra("capturing", true);
                startActivityForResult(intent, 1);
                finish();
                //popup, info activity 까지 삭제하면 stack 거꾸로여서 안되는것같음.

            }
        });

    }
}
