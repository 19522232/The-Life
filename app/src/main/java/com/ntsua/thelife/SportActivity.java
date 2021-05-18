package com.ntsua.thelife;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Spliterator;

public class SportActivity extends AppCompatActivity {

    ListView lvSport;
    FoodAdapter adapter;
    ArrayList<Food> arrSport;
    JSONObject Sportjs;
    Random random;
    String jsonEvent;
    ActivitiesEvent Activity;
    TextView txtName, txtJob, txtMoney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);
        txtName = findViewById(R.id.textviewName);
        txtJob = findViewById(R.id.textviewJob);
        txtMoney = findViewById(R.id.textviewMoney);
        random = new Random();
        lvSport = (ListView) findViewById((R.id.listviewSports));
        loadGame();
        arrSport = new ArrayList<Food>();
        arrSport.add(new Food("Chạy bộ", "FREE", R.drawable.jogging, 0));
        arrSport.add(new Food("Bài tập thể dục", "FREE", R.drawable.triangle, 0));
        arrSport.add(new Food("Bóng đá", "150 nghìn", R.drawable.football, 150));
        arrSport.add(new Food("Bóng bàn", "200 nghìn", R.drawable.ping_pong, 200));
        arrSport.add(new Food("Bóng rỗ", "100 nghìn", R.drawable.basketball, 100));
        arrSport.add(new Food("Bóng chày", "220 nghìn", R.drawable.baseball, 220));
        arrSport.add(new Food("Quần vợt", "1 triệu", R.drawable.tennis_racket, 1000));
        arrSport.add(new Food("Cầu lông", "150 nghìn", R.drawable.badminton, 150));
        arrSport.add(new Food("Chạy xe đạp", "150 nghìn", R.drawable.racing, 150));
        arrSport.add(new Food("Leo núi", "300 nghìn", R.drawable.hiking, 300));
        arrSport.add(new Food("Cử tạ", "400 nghìn", R.drawable.fitness, 400));
        arrSport.add(new Food("Võ Vovinam", "200 nghìn", R.drawable.vovinam, 200));
        arrSport.add(new Food("Chèo thuyền", "450 nghìn", R.drawable.rowing, 450));
        arrSport.add(new Food("Lướt sóng", "600 nghìn", R.drawable.surfing, 600));
        arrSport.add(new Food("Lặn", "1 triệu", R.drawable.snorkle, 1000));
        arrSport.add(new Food("Yoga", "500 nghìn", R.drawable.yoga, 500));
        adapter = new FoodAdapter(this, R.layout.food_line, arrSport);
        lvSport.setAdapter(adapter);
        jsonEvent = readEvent();
        Activity = new ActivitiesEvent(jsonEvent,MainActivity.saveGame,SportActivity.this);
        lvSport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (arrSport.get(position).getFoodName() == "Chạy bộ" )
                {
                    if (MainActivity.saveGame.getJogging() < 3) {
                        try {
                            Activity.CreateDialog("jogging", "Thể thao");
                            MainActivity.saveGame.saveJogging(MainActivity.saveGame.getJogging() + 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        MainActivity.createNotification(R.drawable.jogging, "Hôm nay bạn đã chạy bộ quá nhiều rồi", SportActivity.this);
                    }
                }
                if (arrSport.get(position).getFoodName() == "Bài tập thể dục")
                {
                    if (MainActivity.saveGame.getExercise() < 3) {
                        try {
                            Activity.CreateDialog("exercise", "Thể thao");
                            MainActivity.saveGame.saveExercise(MainActivity.saveGame.getExercise() + 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        MainActivity.createNotification(R.drawable.triangle, "Hôm nay bạn đã tập thể dục quá nhiều rồi", SportActivity.this);
                    }
                }
                if (arrSport.get(position).getFoodName() == "Bóng đá" &&
                        MainActivity.saveGame.getMoney() >= arrSport.get(2).getPrice())
                {
                    try {
                        Activity.CreateDialog("football","Thể thao");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if(position == 2) {
                    createDialogOFM();
                }

                if (arrSport.get(position).getFoodName() == "Bóng bàn"
                        && MainActivity.saveGame.getMoney() >= arrSport.get(3).getPrice())
                {
                    try {
                        Activity.CreateDialog("pingpong","Thể thao");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(position == 3) {
                    createDialogOFM();
                }

                if (arrSport.get(position).getFoodName() == "Bóng rỗ"
                        && MainActivity.saveGame.getMoney() >= arrSport.get(4).getPrice())
                {
                    try {
                        Activity.CreateDialog("basketball","Thể thao");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(position == 4){
                    createDialogOFM();
                }

                if (arrSport.get(position).getFoodName() == "Bóng chày"
                        && MainActivity.saveGame.getMoney() >= arrSport.get(5).getPrice())
                {
                    try {
                        Activity.CreateDialog("baseball","Thể thao");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(position ==5 ){
                    createDialogOFM();
                }

                if (arrSport.get(position).getFoodName() == "Quần vợt"
                        && MainActivity.saveGame.getMoney() >= arrSport.get(6).getPrice())
                {
                    try {
                        Activity.CreateDialog("tennis","Thể thao");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(position==6){
                    createDialogOFM();
                }

                if (arrSport.get(position).getFoodName() == "Cầu lông"
                        && MainActivity.saveGame.getMoney() >= arrSport.get(7).getPrice()) {
                    try {
                        Activity.CreateDialog("badminton","Thể thao");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(position == 7){
                    createDialogOFM();
                }

                if (arrSport.get(position).getFoodName() == "Chạy xe đạp"
                        && MainActivity.saveGame.getMoney() >= arrSport.get(8).getPrice()) {
                    try {
                        Activity.CreateDialog("cycling","Thể thao");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(position ==8){
                    createDialogOFM();
                }

                if (arrSport.get(position).getFoodName() == "Leo núi"
                        && MainActivity.saveGame.getMoney() >= arrSport.get(9).getPrice()) {
                    try {
                        Activity.CreateDialog("climb","Thể thao");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(position ==9){
                    createDialogOFM();
                }

                if (arrSport.get(position).getFoodName() == "Cử tạ"
                        && MainActivity.saveGame.getMoney() >= arrSport.get(10).getPrice()) {
                    try {
                        Activity.CreateDialog("GYM","Thể thao");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(position ==10){
                    createDialogOFM();
                }

                if (arrSport.get(position).getFoodName() == "Võ Vovinam"
                        && MainActivity.saveGame.getMoney() >= arrSport.get(11).getPrice()) {
                    try {
                        Activity.CreateDialog("martial","Thể thao");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(position == 11)
                {
                    createDialogOFM();
                }

                if (arrSport.get(position).getFoodName() == "Chèo thuyền"
                        && MainActivity.saveGame.getMoney() >= arrSport.get(12).getPrice()) {
                    try {
                        Activity.CreateDialog("rowing","Thể thao");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(position ==12)
                {
                    createDialogOFM();
                }

                if (arrSport.get(position).getFoodName() == "Lướt sóng"
                        && MainActivity.saveGame.getMoney() >= arrSport.get(13).getPrice()) {
                    try {
                        Activity.CreateDialog("surf","Thể thao");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(position ==13)
                {
                    createDialogOFM();
                }

                if (arrSport.get(position).getFoodName() == "Lặn"
                        && MainActivity.saveGame.getMoney() >= arrSport.get(14).getPrice()) {
                    try {
                        Activity.CreateDialog("dive","Thể thao");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(position ==14)
                {
                    createDialogOFM();
                }

                if (arrSport.get(position).getFoodName() == "Yoga"
                        && MainActivity.saveGame.getMoney() >= arrSport.get(15).getPrice()) {
                    try {
                        Activity.CreateDialog("yoga","Thể thao");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(position ==15)
                {
                    createDialogOFM();
                }
            }
        });
    }
    void createDialogOFM() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_out_of_money);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LinearLayout dialogResult = dialog.findViewById(R.id.dialog_event_result);
        Button btnOke = dialog.findViewById(R.id.buttonDialogEventOke);
        btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void loadGame() {
        txtName.setText(MainActivity.saveGame.getName());
        txtMoney.setText(MainActivity.saveGame.getMoney() + "VND");
        txtJob.setText(MainActivity.saveGame.getJob());
    }

    String readEvent()
    {
        jsonEvent = null;
        try {
            InputStream inputStream = getAssets().open("sport_event.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonEvent = new String(buffer, "UTF-8");
            Sportjs = new JSONObject(jsonEvent);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return jsonEvent;
    }

    public void gotoMainMenu(View view)
    {
        //startActivity(new Intent(SportActivity.this, HoatDong.class));
        finish();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGame();
    }
}