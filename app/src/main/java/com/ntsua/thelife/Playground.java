package com.ntsua.thelife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Playground extends AppCompatActivity {

    ListView lvPlayground;
    FoodAdapter adapter;
    ArrayList<Food> arrPlayground;
    TextView txtName, txtJob, txtMoney;
    ImageView imgAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground);

        txtName = findViewById(R.id.textviewName);
        txtJob = findViewById(R.id.textviewJob);
        txtMoney = findViewById(R.id.textviewMoney);
        imgAvatar = findViewById(R.id.imageAvatar);
        loadGame();

        lvPlayground = (ListView) findViewById((R.id.listviewPlayground));
        arrPlayground = new ArrayList<>();

        arrPlayground.add(new Food("2048", "", R.drawable.game2048, 0));
        arrPlayground.add(new Food("Ai là triệu phú", "", R.drawable.ailatrieuphu, 0));
        adapter = new FoodAdapter(this, R.layout.food_line, arrPlayground);
        lvPlayground.setAdapter(adapter);

        lvPlayground.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent myintent = new Intent(Playground.this, game2048_batdau.class);
                    startActivityForResult(myintent, 0);
                }
                overridePendingTransition(R.anim.enter, R.anim.exit);
                if (position == 1) {
                    Intent myintent = new Intent(Playground.this, AiLaTrieuPhu.class);
                    startActivityForResult(myintent, 0);
                }
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    private void loadGame() {
        txtName.setText(MainActivity.saveGame.getName());
        txtMoney.setText(MainActivity.saveGame.getMoney() + "K VND");
        txtJob.setText(MainActivity.saveGame.getJob());
        imgAvatar.setImageResource(MainActivity.saveGame.getAvatar());
    }

    public void gotoMainMenu(View view)
    {
        //startActivity(new Intent(Playground.this, HoatDong.class));
        overridePendingTransition(R.anim.enter, R.anim.exit);
        onBackPressed();
    }
}