package com.example.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Find_City extends AppCompatActivity {


    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_city);
        editText = findViewById(R.id.newcity_find_id);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String newcity = editText.getText().toString();
                Intent intent = new Intent(Find_City.this, MainActivity.class);
                intent.putExtra("City", newcity);
                startActivity(intent);
                 return false;
            }
        });




    }

    @Override
    public void onBackPressed() {

        finish();

    }


}