package com.example.myfitnessapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myfitnessapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class DecideWhatToDo extends AppCompatActivity {

    Button buttonTryAChallenge,buttonJustForFun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decide_what_to_do);

        //--------------------------------REFERENCING XML VIEWS-----------------------------------//

        buttonJustForFun = findViewById(R.id.btnJustForFun);
        buttonTryAChallenge = findViewById(R.id.btnTryAChallenge);

        //-----------------------------ONCLICK LISTENERS FOR VIEWS--------------------------------//

        buttonTryAChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DecideWhatToDo.this,ShowingAllChallenges.class));
            }
        });

        buttonJustForFun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent intent =new Intent(DecideWhatToDo.this,LoginActivity.class);
                startActivity(intent);

                // startActivity(new Intent(DecideWhatToDo.this,UploadAttempt.class));
            }
        });
    }
}
