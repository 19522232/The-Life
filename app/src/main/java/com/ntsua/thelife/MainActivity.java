package com.ntsua.thelife;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.UiAutomation;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnActivity, btnRelationship, btnWork, btnAssets;;
    ImageButton ibtnAddAge;
    ProgressBar prbHappy, prbHealth, prbSmart, prbAppearance;
    ScrollView scrollView;
    TextView txtContent, txtHappy, txtHealth, txtSmart, txtAppearance, txtMoney, txtName, txtJob;
    SharedPreferences preferences;
    static public SaveGame saveGame;
    JSONArray arrJsonAge;
    JSONObject jsonResult, jsonAllJob, jsonJob;
    String contentHtml;
    int money;
    long currentTime = 0;
    Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();
        //Test
        try {
            if (saveGame.getDetailActivity().equals(""))
                init("Name", "vn");
            else loadGame();
            readEvent();
            readJob();
            changeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //(R.drawable.jogging, "Hôm nay bạn đã chạy bộ quá nhiều rồi", MainActivity.this);
        //SpannableString s = new SpannableString(txtContent.getText().toString());
        //s.setSpan(new RelativeSizeSpan(2f), 0, 3, 0);
        //txtContent.setText(saveGame.getDetailActivity());
        //saveGame.saveDetailActivity(txtContent.getText().toString());


        ibtnAddAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addAge();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        btnWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    doWork();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    void dialogJob(JSONArray arrJob) throws JSONException {
        Dialog dialog = createDialog("Làm việc", "Làm, làm nữa, làm mãi");
        LinearLayout dialogCustom = dialog.findViewById(R.id.dialog_event);


        for (int i=0; i<arrJob.length(); i++)
        {
            JSONObject object = arrJob.getJSONObject(i);
            Button btn = addButton(dialogCustom, object.getString("content"));
            int finalI = i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JSONArray array = object.getJSONArray("event");
                        jsonResult = array.getJSONObject(new Random().nextInt(array.length()));
                        dialog.dismiss();
                        //saveGame.saveSkill(saveGame.getSkill() + jsonResult.getInt("skill"));
                        //Toast.makeText(MainActivity.this, "" + saveGame.getSkill(), Toast.LENGTH_SHORT).show();
                        dialogEventResult(object.getString("content"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        dialog.show();
    }

    void doWork() throws JSONException {
        JSONArray arrJob = jsonJob.getJSONArray("work");
        dialogJob(arrJob);
    }

    void addAge() throws JSONException {
        //them tuoi
        int age = saveGame.getAge() + 1;

        //lay su kien tuoi
        JSONArray arrAge = arrJsonAge.getJSONArray(age);
        Random random = new Random();
        //Toast.makeText(this, arrAge.length() + " - " + age, Toast.LENGTH_SHORT).show();
        final JSONObject[] object = {arrAge.getJSONObject(random.nextInt(arrAge.length()))};

        final String[] event = {object[0].getString("event")};
        String title = object[0].getString("title");

        //Kiem tra su kien co su lua chon hay khong
        final boolean[] isSelection = {object[0].getBoolean("selection")};

        //Tao dialog hien thi su kien
        if (isSelection[0])
            dialogEvent(object, isSelection, title, event);
        else {
            jsonResult = object[0];
            initNewAge();
            dialogEventResult(title);
        }
    }

    void dialogEvent(JSONObject[] object, boolean[] isSelection, String title, String[] event) throws JSONException
    {
        jsonResult = object[0];
        if (!isSelection[0]) {
            JSONArray arr = jsonResult.getJSONArray("event");
            jsonResult = arr.getJSONObject(new Random().nextInt(arr.length()));
            //Luu tuoi
            initNewAge();
            //saveGame.saveAge(age);
            //Tao dialog hien thi ket qua cua event
            dialogEventResult(title);
            return;
        }


        //Them thong tin da choi vao textview
        //contentHtml += event[0] + "<br>";

        //Tao dialog va them cac button lua chon vao dialog
        Dialog dialog = createDialog(title, event[0]);
        LinearLayout dialogCustom = dialog.findViewById(R.id.dialog_event);
        JSONArray arrSelect = object[0].getJSONArray("select");
        for (int i = 0; i < arrSelect.length(); i++) {
            JSONObject objectSelect = arrSelect.getJSONObject(i);
            Button btn = addButton(dialogCustom, objectSelect.getString("content"));
            int finalI = i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        object[0] = arrSelect.getJSONObject(finalI);
                        isSelection[0] = object[0].getBoolean("selection");
                        event[0] = object[0].getString("event");
                        dialog.dismiss();
                        dialogEvent(object, isSelection, title, event);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        dialog.show();
    }

    void dialogEventResult(String title) throws JSONException {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_event_result);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogResultAnimation;


        //Lay gia tri gan vao dialog
        LinearLayout dialogResult = dialog.findViewById(R.id.dialog_event_result);
        Button btnOke = dialog.findViewById(R.id.buttonDialogEventOke);
        TextView txtTitle = dialog.findViewById(R.id.textviewDialogEventTitle);
        TextView txtContent= dialog.findViewById(R.id.textviewDialogEventContent);
        TextView txtHappy = dialog.findViewById(R.id.textviewResultHappy);
        TextView txtHealth = dialog.findViewById(R.id.textviewResultHealth);
        TextView txtSmart= dialog.findViewById(R.id.textviewResultSmart);
        TextView txtAppearance = dialog.findViewById(R.id.textviewResultAppearance);
        TextView txtAssets = dialog.findViewById(R.id.textviewResultMoney);

        txtTitle.setText(title);
        txtContent.setText(jsonResult.getString("event"));
        contentHtml += txtContent.getText().toString() + "<br>";



        int value = 0;
        value = jsonResult.getInt("happy");
        if (value == 0)
        {
            dialogResult.removeView(dialog.findViewById(R.id.linearHappy));
        } else {
            txtHappy.setText(value + "");
            prbHappy.setProgress(prbHappy.getProgress() + value);
            this.txtHappy.setText(prbHappy.getProgress() + "%");
        }
        value = jsonResult.getInt("health");
        if (value == 0)
        {
            dialogResult.removeView(dialog.findViewById(R.id.linearHealth));
        } else {
            txtHealth.setText(value + "");
            prbHealth.setProgress(prbHealth.getProgress() + value);
            this.txtHealth.setText(prbHealth.getProgress() + "%");
        }
        value = jsonResult.getInt("smart");
        if (value == 0)
        {
            dialogResult.removeView(dialog.findViewById(R.id.linearSmart));
        } else {
            txtSmart.setText(value + "");
            prbSmart.setProgress(prbSmart.getProgress() + value);
            this.txtSmart.setText(prbSmart.getProgress() + "%");
        }
        value = jsonResult.getInt("appearance");
        if (value == 0)
        {
            dialogResult.removeView(dialog.findViewById(R.id.linearAppearance));
        } else {
            txtAppearance.setText(value + "");
            prbAppearance.setProgress(prbAppearance.getProgress() + value);
            this.txtAppearance.setText(prbAppearance.getProgress() + "%");
        }
        value = jsonResult.getInt("assets");
        if (value == 0)
        {
            dialogResult.removeView(dialog.findViewById(R.id.linearMoney));
        } else {
            txtAssets.setText(value + "");
            money += value;
            this.txtMoney.setText(money + " VND");
            saveGame.saveMoney(money);
        }
        saveGame.savePlayerInfo(prbHappy.getProgress(), prbHealth.getProgress(), prbSmart.getProgress(), prbAppearance.getProgress());
        saveGame.saveDetailActivity(contentHtml);
        this.txtContent.setText(android.text.Html.fromHtml(contentHtml));
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                try {
                    jobEvent();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }

    void dialogJobEvent(String title) throws JSONException {
        //Toast.makeText(this, "ssdfsgsdgsdgd", Toast.LENGTH_LONG).show();
        if (!jsonResult.getBoolean("selection")) {
            JSONArray arr = jsonResult.getJSONArray("event");
            jsonResult = arr.getJSONObject(new Random().nextInt(arr.length()));
            saveGame.saveSalary(jsonResult.getInt("salary"));
            //Tao dialog hien thi ket qua cua event
            dialogEventResult(title);
            return;
        }

        //Tao dialog va them cac button lua chon vao dialog
        Dialog dialog = createDialog(title, jsonResult.getString("event"));
        LinearLayout dialogCustom = dialog.findViewById(R.id.dialog_event);
        JSONArray arrSelect = jsonResult.getJSONArray("select");
        for (int i = 0; i < arrSelect.length(); i++) {
            JSONObject objectSelect = arrSelect.getJSONObject(i);
            Button btn = addButton(dialogCustom, objectSelect.getString("content"));
            int finalI = i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        jsonResult = arrSelect.getJSONObject(finalI);
                        dialog.dismiss();
                        dialogJobEvent(title);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        dialog.show();
    }

    void jobEvent() throws JSONException {
        int currentSkill = saveGame.getSkill();
        int addSkill = jsonResult.getInt("skill");
        saveGame.saveSkill(currentSkill + addSkill);

        JSONArray arrJob = jsonJob.getJSONArray("event");
        for (int i=0; i<arrJob.length(); i++)
        {
            //Toast.makeText(MainActivity.this, "error1", Toast.LENGTH_SHORT).show();
            jsonResult =  arrJob.getJSONObject(i);
            int require = jsonResult.getInt("require");
            if (currentSkill < require && currentSkill + addSkill >= require)
            {
                if (jsonResult.getBoolean("selection")) {
                    dialogJobEvent("Công việc");
                } else {
                    saveGame.saveSalary(jsonResult.getInt("salary"));
                    dialogEventResult("Công việc");
                    //Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    Button addButton(LinearLayout dialogCustom, String text)
    {
        Button btn = new Button(dialogCustom.getContext());
        btn.setText(text);
        btn.setTextSize(14);
        btn.setBackgroundResource(R.drawable.custom_button_menu);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(550, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = 10;
        params.topMargin = 10;
        params.gravity = Gravity.CENTER_HORIZONTAL;
        btn.setLayoutParams(params);
        btn.setTextColor(Color.WHITE);
        dialogCustom.addView(btn);

        return btn;
    }

    Dialog createDialog(String title, String event){
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
        txtTitle.setText(title);

        return dialog;
    }

    public static void createNotification(int image, String content, Context context)
    {
        //Tao dialog
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_notification);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogResultAnimation;

        //Anh xa
        TextView txtTitle   = dialog.findViewById(R.id.textviewNotificationTitle);
        TextView txtContent = dialog.findViewById(R.id.textviewNotificationContent);
        ImageView imageView = dialog.findViewById(R.id.imageviewNotification);
        Button btnOke       = dialog.findViewById(R.id.buttonNotificationtOke);

        //Gan gia tri
        txtTitle.setText("Thông báo");
        txtContent.setText(content);
        imageView.setImageResource(image);

        btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    void readEvent() throws JSONException {
        String jsonEvent = null;
        try {
            InputStream inputStream = getAssets().open("age_event.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonEvent = new String(buffer, "UTF-8");
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject object = new JSONObject(jsonEvent);
        arrJsonAge = object.getJSONArray("age");
    }

    void readJob() throws JSONException {
        String jsonEvent = null;
        try {
            InputStream inputStream = getAssets().open("job.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonEvent = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        jsonAllJob = new JSONObject(jsonEvent);
    }

    private void AnhXa() {
        btnRelationship = (Button) findViewById(R.id.buttonRelationship);
        btnActivity = (Button) findViewById(R.id.buttonActivity);

        txtContent = findViewById(R.id.textViewDetail);
        txtAppearance = findViewById(R.id.txtAppearance);
        txtHappy = findViewById(R.id.txtHappy);
        txtSmart = findViewById(R.id.txtSmart);
        txtHealth = findViewById(R.id.txtHealth);
        txtMoney = findViewById(R.id.textviewMoney);
        txtName = findViewById(R.id.textviewName);
        txtJob = findViewById(R.id.textviewJob);

        prbAppearance = findViewById(R.id.progressbarAppearance);
        prbHappy = findViewById(R.id.progressbarHappy);
        prbHealth = findViewById(R.id.progressbarHealth);
        prbSmart = findViewById(R.id.progressbarSmart);

        ibtnAddAge = findViewById(R.id.imagebuttonAddAge);
        btnAssets = findViewById(R.id.buttonAssets);
        btnWork = findViewById(R.id.buttonInfant);
        scrollView = findViewById(R.id.scrollViewText);


        preferences = getSharedPreferences("data", MODE_PRIVATE);
        saveGame = new SaveGame(preferences);
    }

    public void gotoActivity(View view)
    {
        startActivity(new Intent(MainActivity.this, HoatDong.class));
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    public void gotoRelationship(View view)
    {
        startActivity(new Intent(MainActivity.this, RelationShip.class));
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    void loadGame()
    {
        contentHtml = saveGame.getDetailActivity();
        txtContent.setText(android.text.Html.fromHtml(contentHtml));
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });


        prbAppearance.setProgress(saveGame.getAppearance());
        prbHappy.setProgress(saveGame.getHappy());
        prbHealth.setProgress(saveGame.getHealth());
        prbSmart.setProgress(saveGame.getSmart());

        txtAppearance.setText(prbAppearance.getProgress() + "%");
        txtHappy.setText(prbHappy.getProgress() + "%");
        txtSmart.setText(prbSmart.getProgress() + "%");
        txtHealth.setText(prbHealth.getProgress() + "%");

        money = saveGame.getMoney();
        txtMoney.setText(money + "VND");
        txtName.setText(saveGame.getName());
        txtJob.setText(saveGame.getJob());

    }

    void changeWork() throws JSONException {
        switch (saveGame.getJob())
        {
            case "Lập trình viên":
                jsonJob = jsonAllJob.getJSONObject("coder");
                break;
            case "Trẻ trâu":
                jsonJob = jsonAllJob.getJSONObject("student");
                break;
        }
    }

    // init tam thoi
    void init(String name, String country) throws JSONException {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("parent.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject object = new JSONObject(json);
        JSONObject vn = object.getJSONObject("vi");
        JSONArray arrFather = vn.getJSONArray("father");
        JSONArray arrMother = vn.getJSONArray("mother");
        JSONArray arrJob = vn.getJSONArray("job");
        Random random = new Random();

        //Tao quan he Bo Me
        String fatherName = arrFather.getString(random.nextInt(arrFather.length()));
        int fatherAge = (random.nextInt(11) + 20);
        QuanHe father = new QuanHe(fatherName, fatherAge, 100, "Bố", R.drawable.boy); //Thay hinh sau
        String motherName = arrMother.getString(random.nextInt(arrMother.length()));
        int motherAge = (random.nextInt(11) + 20);
        QuanHe mother = new QuanHe(motherName, motherAge, 100, "Mẹ", R.drawable.girl); //Thay hinh sau

        ArrayList<QuanHe> arrRelationship = new ArrayList<>();
        arrRelationship.add(father);
        arrRelationship.add(mother);
        //Them cho vui
        arrRelationship.add(new QuanHe("Trần Thanh Vũ", 19, 50, "Bạn bè",R.drawable.boy));
        arrRelationship.add(new QuanHe("Nguyễn Thiện Sua", 19, 2, "Bạn bè",R.drawable.boy));
        arrRelationship.add(new QuanHe("Nguyễn Hiếu Nghĩa", 19, 50, "Bạn bè",R.drawable.boy));
        arrRelationship.add(new QuanHe("Mai Long Thành", 19, 80, "Bạn bè",R.drawable.boy));
        arrRelationship.add(new QuanHe("Võ Thành Phát", 19, 2, "Bạn bè",R.drawable.boy));
        arrRelationship.add(new QuanHe("Hoàng Nhật Tiến", 19, 0, "Bạn bè",R.drawable.boy));

        saveGame.saveRelationship(arrRelationship);

        //Tao ngay sinh
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        contentHtml = "<h5> <font color=\"blue\">Tuổi 0</font></h5>" +
                "Tôi tên " + name + "<br>" +
                "Sinh ngày " + format.format(date) + "<br>" +
                "Bố tôi là " + fatherName + " - " +
                arrJob.getString(random.nextInt(arrJob.length())) +
                " (" + fatherAge + " tuổi )" + "<br>" +
                "Mẹ tôi là " + motherName + " - " +
                arrJob.getString(random.nextInt(arrJob.length())) +
                " (" + motherAge + " tuổi )" + "<br>";
        //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        saveGame.saveAge(0);
        saveGame.saveMoney(5000);
        saveGame.saveDetailActivity(contentHtml);
        saveGame.saveName(name);
        saveGame.saveJob("Trẻ trâu");
        saveGame.saveSkill(0);
        saveGame.saveExercise(0);
        saveGame.saveJogging(0);
        saveGame.saveJogging(0);
        txtJob.setText(saveGame.getJob());
        txtMoney.setText("0 VND");
        txtContent.setText(android.text.Html.fromHtml(contentHtml));
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
        txtName.setText(name);

        prbHealth.setProgress(100);
        prbHappy.setProgress(100);
        prbSmart.setProgress(33);
        prbAppearance.setProgress(50);

        txtAppearance.setText(prbAppearance.getProgress() + "%");
        txtHappy.setText(prbHappy.getProgress() + "%");
        txtSmart.setText(prbSmart.getProgress() + "%");
        txtHealth.setText(prbHealth.getProgress() + "%");
        saveGame.savePlayerInfo(prbHappy.getProgress(), prbHealth.getProgress(), prbSmart.getProgress(), prbAppearance.getProgress());
    }

    void initNewAge()
    {
        int age = saveGame.getAge() + 1;
        saveGame.saveExercise(0);
        saveGame.saveJogging(0);
        saveGame.saveJogging(0);
        addAgeHTML(age);
    }

    void addAgeHTML(int age)
    {
        contentHtml += "<h5> <font color=\"blue\">Tuổi " + age + "</font></h5>";
        txtContent.setText(android.text.Html.fromHtml(contentHtml));
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
        saveGame.saveDetailActivity(contentHtml);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (currentTime + 2000 > System.currentTimeMillis())
        {
            backToast.cancel();
            this.finishAffinity();

        }
        else {
            backToast = Toast.makeText(this, "Ấn lần nữa để thoát", Toast.LENGTH_SHORT);
            backToast.show();
        }
        currentTime = System.currentTimeMillis();
    }
}