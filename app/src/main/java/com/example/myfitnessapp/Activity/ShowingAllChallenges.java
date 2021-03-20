package com.example.myfitnessapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfitnessapp.R;

public class ShowingAllChallenges extends AppCompatActivity {

    private CardView cardViewRegularPushUps1,cardViewRegularPushUps2,cardViewRegularPushUps3,cardViewPullUps1,cardViewPullUps2,cardViewPullUps3,cardViewBenchPress1,cardViewBenchPress2,cardViewBenchPress3,cardViewLifts1,cardViewLifts2,cardViewLifts3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_all_challenges);

        //------------------------------REFERENCING XML VIEWS-------------------------------------//

        cardViewRegularPushUps1 =findViewById(R.id.cvRPU1);
        cardViewRegularPushUps2 = findViewById(R.id.cvRPU2);
        cardViewRegularPushUps3 = findViewById(R.id.cvRPU3);
        cardViewPullUps1 = findViewById(R.id.cvPU1);
        cardViewPullUps2 = findViewById(R.id.cvPU2);
        cardViewPullUps3 = findViewById(R.id.cvPU3);
        cardViewBenchPress1 = findViewById(R.id.cvBP1);
        cardViewBenchPress2 = findViewById(R.id.cvBP2);
        cardViewBenchPress3 = findViewById(R.id.cvBP3);
        cardViewLifts1 = findViewById(R.id.cvL1);
        cardViewLifts2 = findViewById(R.id.cvL2);
        cardViewLifts3 = findViewById(R.id.cvL3);

        //------------------------------ONCLICK LISTENERS-------------------------------------//

        cardViewRegularPushUps1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowingAllChallenges.this,SelectChallenge.class);
                intent.putExtra("image","pushUps");
                intent.putExtra("challengeDuration","30");
                intent.putExtra("theRankingQuery","30s Push Ups");
                startActivity(intent);
            }
        });
        cardViewRegularPushUps2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowingAllChallenges.this,SelectChallenge.class);
                intent.putExtra("image","pushUps");
                intent.putExtra("challengeDuration","20");
                intent.putExtra("theRankingQuery","20s Push Ups");
                startActivity(intent);
            }
        });
        cardViewRegularPushUps3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowingAllChallenges.this,SelectChallenge.class);
                intent.putExtra("image","pushUps");
                intent.putExtra("challengeDuration","10");
                intent.putExtra("theRankingQuery","10s Push Ups");
                startActivity(intent);
            }
        });
        cardViewPullUps1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowingAllChallenges.this,SelectChallenge.class);
                intent.putExtra("image","pullUps");
                intent.putExtra("challengeDuration","30");
                startActivity(intent);
            }
        });
        cardViewPullUps2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowingAllChallenges.this,SelectChallenge.class);
                intent.putExtra("image","pullUps");
                intent.putExtra("challengeDuration","20");
                startActivity(intent);
            }
        });
        cardViewPullUps3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowingAllChallenges.this,SelectChallenge.class);
                intent.putExtra("image","pullUps");
                intent.putExtra("challengeDuration","10");
                startActivity(intent);
            }
        });
        cardViewBenchPress1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowingAllChallenges.this,SelectChallenge.class);
                intent.putExtra("image","benchPress");
                intent.putExtra("challengeDuration","30");
                startActivity(intent);
            }
        });
        cardViewBenchPress2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowingAllChallenges.this,SelectChallenge.class);
                intent.putExtra("image","benchPress");
                intent.putExtra("challengeDuration","20");
                startActivity(intent);
            }
        });
        cardViewBenchPress3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowingAllChallenges.this,SelectChallenge.class);
                intent.putExtra("image","benchPress");
                intent.putExtra("challengeDuration","10");
                startActivity(intent);
            }
        });
        cardViewLifts1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowingAllChallenges.this,SelectChallenge.class);
                intent.putExtra("image","lifts");
                intent.putExtra("challengeDuration","30");
                startActivity(intent);
            }
        });
        cardViewLifts2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowingAllChallenges.this,SelectChallenge.class);
                intent.putExtra("image","lifts");
                intent.putExtra("challengeDuration","20");
                startActivity(intent);
            }
        });
        cardViewLifts3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowingAllChallenges.this,SelectChallenge.class);
                intent.putExtra("image","lifts");
                intent.putExtra("challengeDuration","10");
                startActivity(intent);
            }
        });

    }
}