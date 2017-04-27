package com.example.brandonvowell.fanbase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.graphics.Color;
import android.view.Gravity;

import java.util.ArrayList;

public class TailgateHistoryActivity extends AppCompatActivity {

    private ArrayList<Tailgate> tailgateList;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailgate_history);
        mContext = getApplicationContext();
        tailgateList = (ArrayList<Tailgate>) getIntent().getSerializableExtra("TAILGATE_LIST");
        setCards(tailgateList);
    }

    private void setCards(ArrayList<Tailgate> tList) {
        LinearLayout mScrollLinear = (LinearLayout) findViewById(R.id.historyLinear);
        for(Tailgate t : tList) {
        CardView card = new CardView(mContext);
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        );
        card.setLayoutParams(params);
        card.setRadius(5);
        card.setContentPadding(15, 15, 15, 15);
        card.setCardBackgroundColor(Color.LTGRAY);
        card.setCardElevation(3);
        card.setUseCompatPadding(true);
        TextView tViewOne = new TextView(mContext);
        tViewOne.setLayoutParams(params);
        String value = t.getTailgateName();
        tViewOne.setText(value);
        tViewOne.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        tViewOne.setTextColor(Color.rgb(252,96,55));
        tViewOne.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        tViewOne.setPadding(20, 20, 20, 20);
        card.addView(tViewOne);
        mScrollLinear.addView(card);
    }
}

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}