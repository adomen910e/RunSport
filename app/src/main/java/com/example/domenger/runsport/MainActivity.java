package com.example.domenger.runsport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.text_begin);
        tv.setGravity(Gravity.CENTER);

        Spannable wordtoSpan = new SpannableString("Bienvenue sur RunSport");
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.WHITE), 14, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new BackgroundColorSpan(Color.BLACK), 14, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new StyleSpan(Typeface.ITALIC), 14, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new RelativeSizeSpan(0.7f), 15, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 17, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new BackgroundColorSpan(0xffff8800), 17, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new StyleSpan(Typeface.BOLD), 17, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(wordtoSpan);

    }

    public void openConnection(View view) {
        Intent intent = new Intent(MainActivity.this, ConnexionProfile.class);
        startActivity(intent);
    }

    public void openCreation(View view) {
        Intent intent = new Intent(MainActivity.this, CreationProfile.class);
        startActivity(intent);
    }

}
