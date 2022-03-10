package com.example.proj_graduation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    public static com.example.proj_graduation.InfoActivity ia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Button start_btn = (Button) findViewById(R.id.start_btn);
        ImageView imageView = (ImageView)findViewById(R.id.illust_image);

        start_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), com.example.proj_graduation.PopupActivity.class);
                startActivityForResult(intent,1);

            }

        });


    }
}

