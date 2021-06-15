package com.ntsua.thelife;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class JobCongNghe extends AppCompatActivity {

    ListView lvCaSi;
    FoodAdapter adapter;
    ArrayList<Food> arrCaSi;
    TextView txtName, txtJob, txtMoney;
    JSONObject jsonCaSi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_cong_nghe);
        txtName = findViewById(R.id.textviewName);
        txtJob = findViewById(R.id.textviewJob);
        txtMoney = findViewById(R.id.textviewMoney);
        loadGame();
        try {
            readDegree();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        lvCaSi = (ListView) findViewById((R.id.listviewCongNghe));
        arrCaSi = new ArrayList<>();
        arrCaSi.add(new Food("Cài WIN dạo", "Thu nhập 5 triệu/ 1 năm", R.drawable.caiwin, 5));
        arrCaSi.add(new Food("Lập trình viên", "Thu nhập 20 triệu/ 1 năm", R.drawable.laptrinhvien, 20));
        arrCaSi.add(new Food("Chuyên gia công nghệ", "Thu nhập 50 triệu/ 1 năm", R.drawable.chuyengiacongnghe, 50));
        arrCaSi.add(new Food("Chủ tịch tập đoàn công nghệ thông tin", "Thu nhập 300 triệu/ 1 năm", R.drawable.chutichtapdoan, 300));
        adapter = new FoodAdapter(this, R.layout.food_line, arrCaSi);
        lvCaSi.setAdapter(adapter);
        lvCaSi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(Degree.this, MainActivity.saveGame.getMoney() + " - " + arrDegree.get(position).getFoodName(), Toast.LENGTH_SHORT).show();
                switch (position)
                {
                    case 0:
                        try {
                            //Toast.makeText(Degree.this, "here", Toast.LENGTH_SHORT).show();
                            JSONArray arrDamCuoi = jsonCaSi.getJSONArray("Công nghệ");
                            JSONArray arrQuestion = chooseQuestion(arrDamCuoi);
                            dialogEvent(arrQuestion, 0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        MainActivity.createNotification(R.drawable.jobsearch, "Bạn chưa có kinh nghiệm cho vị trí này", JobCongNghe.this);
                        break;
                    case 2:
                        MainActivity.createNotification(R.drawable.jobsearch, "Bạn chưa có kinh nghiệm cho vị trí này", JobCongNghe.this);
                        break;
                    case 3:
                        MainActivity.createNotification(R.drawable.jobsearch, "Bạn chưa có kinh nghiệm cho vị trí này", JobCongNghe.this);
                        break;
                }
            }
        });
    }
    void createDialogOFM() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_job_accept);
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
    void dialogResult(boolean result)
    {
        if (result) {
            MainActivity.createNotification(R.drawable.holding_hands, "Chúc mừng bạn đã xuất sắc vượt qua bài phỏng vấn. Bắt đầu từ mai bạn có thể bắt đầu công việc của mình", JobCongNghe.this);
        }
        else MainActivity.createNotification(R.drawable.cancel, "Trả lời sai bét, bạn không được nhận công việc", JobCongNghe.this);
    }
    void dialogEvent(JSONArray arrQuestion, int index) throws JSONException
    {
        if (index == 3)
        {
            dialogResult(true);
            return;
        }

        JSONObject object = arrQuestion.getJSONObject(index);
        //Tao dialog va them cac button lua chon vao dialog
        Dialog dialog = createDialog(object.getString("question"));
        int correctAnswer = object.getInt("correct");
        LinearLayout dialogCustom = dialog.findViewById(R.id.dialog_event);
        JSONArray arrSelect = object.getJSONArray("answers");
        for (int i = 0; i < arrSelect.length(); i++) {
            Button btn = addButton(dialogCustom, arrSelect.getString(i));
            int finalI = i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalI == correctAnswer) {
                        try {
                            dialogEvent(arrQuestion, index + 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else dialogResult(false);
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }
    Dialog createDialog(String event){
        //Dinh dang dialog
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_event);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogEventAnimation;
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    return true;
                }
                return false;
            }
        });

        //Anh xa cac phan tu trong dialog
        TextView txtTitle = dialog.findViewById(R.id.textviewDialogEventTitle);
        TextView txtContent = dialog.findViewById(R.id.textviewDialogEventContent);

        //Gan gia tri vao view
        txtContent.setText(event);
        txtTitle.setText("Câu hỏi");

        return dialog;
    }

    JSONArray chooseQuestion(JSONArray arrEnglish) throws JSONException {
        JSONArray arrQuestion = new JSONArray();
        Random random = new Random();
        int first = random.nextInt(arrEnglish.length());

        arrQuestion.put(arrEnglish.getJSONObject(first));

        int second = first;
        while (first == second)
        {
            second = random.nextInt(arrEnglish.length());
        }
        arrQuestion.put(arrEnglish.getJSONObject(second));
        int third = second;
        while (second == third || third == first)
        {
            third = random.nextInt(arrEnglish.length());
        }
        arrQuestion.put(arrEnglish.getJSONObject(third));

        return arrQuestion;
    }

    @SuppressLint("ResourceAsColor")
    Button addButton(LinearLayout dialogCustom, String text)
    {
        Button btn = new Button(dialogCustom.getContext());
        btn.setText(text);
        btn.setTextSize(15);
        btn.setBackgroundResource(R.drawable.custom_dialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(800, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = 10;
        params.topMargin = 10;
        params.gravity = Gravity.CENTER_HORIZONTAL;
        btn.setLayoutParams(params);
        btn.setTextColor( Color.argb(255,16,54,103));
        dialogCustom.addView(btn);

        return btn;
    }
    void readDegree() throws JSONException {
        String jsonEvent = null;
        try {
            InputStream inputStream = getAssets().open("job_cacloaicongviec.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonEvent = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        jsonCaSi = new JSONObject(jsonEvent);
    }
    public void gotoMainMenu(View view)
    {
        //startActivity(new Intent(JobCaSi.this, ChuyenViec.class));
        overridePendingTransition(R.anim.enter, R.anim.exit);
        onBackPressed();
    }
    private void loadGame() {
        txtName.setText(MainActivity.saveGame.getName());
        txtMoney.setText(MainActivity.saveGame.getMoney() + "VND");
        txtJob.setText(MainActivity.saveGame.getJob());
    }
}