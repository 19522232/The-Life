package com.ntsua.thelife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Asset extends AppCompatActivity {

    ListView lvAsset;
    ArrayList<product> arrAsset;
    productAdapter adapter;
    TextView txtMoney, txtName, txtJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);

        txtMoney = findViewById(R.id.textviewMoney);
        txtJob = findViewById(R.id.textviewJob);
        txtName = findViewById(R.id.textviewName);
        loadGame();

        TextView tvThongbao = findViewById(R.id.tvthongbao);
        lvAsset = findViewById(R.id.listviewAsset);

        arrAsset = new ArrayList<>();
        arrAsset = MainActivity.saveGame.getAsset();
        if(arrAsset.size()!=0)
        {
            tvThongbao.setText("Danh sách tài sản bạn có: ");
            adapter = new productAdapter(this,R.layout.food_line, arrAsset);
            lvAsset.setAdapter(adapter);
        }
  }

    private void loadGame() {
        txtName.setText(MainActivity.saveGame.getName());
        txtMoney.setText(MainActivity.saveGame.getMoney() + "VND");
        txtJob.setText(MainActivity.saveGame.getJob());
    }

    public void gotoMainMenu(View view)
    {
        startActivity(new Intent(Asset.this, MainActivity.class));
    }
}