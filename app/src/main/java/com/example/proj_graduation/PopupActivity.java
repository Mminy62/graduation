package com.example.proj_graduation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PopupActivity extends AppCompatActivity {

    Button startBtn;
    public static com.example.proj_graduation.PopupActivity pa;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.howtoplay_ui);

      /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
               WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
      //  getWindow().setBackgroundDrawable(new ColorDrawable(0xCC000000));//배경 투명하게


        startBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.proj_graduation.MainActivity.class);
                startActivityForResult(intent, 1);
            }
        });

    }
}
