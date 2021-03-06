package com.ntsua.thelife;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMain extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentMain() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMain.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMain newInstance(String param1, String param2) {
        FragmentMain fragment = new FragmentMain();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    View view;
    Button btnActivity, btnRelationship, btnWork, btnAssets;
    ImageView imgAvatar;
    ImageButton ibtnAddAge;
    ProgressBar prbHappy, prbHealth, prbSmart, prbAppearance, prbLoadData;
    ScrollView scrollView;
    TextView txtContent, txtHappy, txtHealth, txtSmart, txtAppearance, txtMoney, txtName, txtJob, txtScrollviewContent;
    ArrayList<QuanHe> arrRelationship;
    JSONArray arrJsonAge, arrAge;
    JSONObject jsonResult, jsonAllJob, jsonJob;
    String contentHtml, tienAn;
    int money, TempAge, namTu;
    boolean University = false;
    int REQUEST_CODE_INIT = 123;
    CircleImageView imgUserAvatar;
    TextView txtUserName, txtPlayerName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    void DeathofRelation()
    {
        Random random = new Random();
        NameOfRelationship dad = NameOfRelationship.Dad;
        NameOfRelationship mom = NameOfRelationship.Mom;
        int flag, image = R.drawable.death;

        for(int i = 0; i< arrRelationship.size();i++)
        {
            QuanHe Relatives = arrRelationship.get(i);
            if(Relatives.getTuoi() >= 75 && Relatives.getTuoi() <=90 && Relatives.getHinhAnh() != image)
            {
                flag = random.nextInt(4);
                if(Relatives.getTuoi() == 90) flag = 1;

                if(flag == 1) {
                    Relatives.setHinhAnh(image);
                    if(Relatives.getQuanHe() == dad || Relatives.getQuanHe() == mom) {
                        MainActivity.createNotification(Relatives.getHinhAnh(), Relatives.getQuanHe() + " c???a b???n ???? m???t", view.getContext());
                    } else MainActivity.createNotification(Relatives.getHinhAnh(),Relatives.getQuanHe() + " c???a b???n" + Relatives.getHoten() + " ???? m???t",view.getContext());

                    prbHappy.setProgress(prbHappy.getProgress() - 30);
                    this.txtHappy.setText(prbHappy.getProgress() + "%");
                }
            }
        }
        MainActivity.saveGame.saveRelationship(arrRelationship);
    }


    void dialogJob(JSONArray arrJob) throws JSONException {
        Dialog dialog = createDialog("L??m vi???c", "L??m, l??m n???a, l??m m??i");
        LinearLayout dialogCustom = dialog.findViewById(R.id.dialog_event);

        for (int i = 0; i < arrJob.length(); i++) {
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
                        dialogEventResult(object.getString("content"), false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        Button btn = addButton(dialogCustom, "B??? qua");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    void doWork() throws JSONException {
        JSONArray arrJob = jsonJob.getJSONArray("work");
        dialogJob(arrJob);
//        if (jsonJob == null)
//            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
    }

    void addAge() throws JSONException {
        //Log.d("Salary", "setSalary: New age");
        //them tuoi
        int age = MainActivity.saveGame.getAge() + 1;
        //So nam ra tu
        int year = TempAge + namTu - age;

        if(age > 0)
        {
            btnWork.setEnabled(true);
            btnWork.setBackgroundResource(R.drawable.custom_button_menu);
        }

        if(age > 5 && namTu == 0)
        {
            btnAssets.setEnabled(true);
            btnActivity.setEnabled(true);

            btnAssets.setBackgroundResource(R.drawable.custom_button_menu);
            btnActivity.setBackgroundResource(R.drawable.custom_button_menu);

        }

        if(namTu!=0) {
            if (age >= TempAge + namTu) {
                namTu = 0;
                MainActivity.saveGame.saveNamTu(namTu);
                MainActivity.saveGame.saveJob("Th???t nghi???p");
                changeWork();

                btnActivity.setEnabled(true);
                btnRelationship.setEnabled(true);
                btnAssets.setEnabled(true);

                btnActivity.setBackgroundResource(R.drawable.custom_button_menu);
                btnRelationship.setBackgroundResource(R.drawable.custom_button_menu);
                btnAssets.setBackgroundResource(R.drawable.custom_button_menu);

                scrollView.setBackgroundColor(Color.WHITE);
                txtScrollviewContent.setVisibility(View.VISIBLE);
                txtJob.setText("Th???t nghi???p");

                MainActivity.createNotification(R.drawable.police,
                        "B???n ???? ???????c th???, hi v???ng b???n ???? nh???n ra t???i l???i c???a m??nh",
                        view.getContext());

            } else {
                MainActivity.createNotification(R.drawable.police,
                        "B???n c??n "+ year +" n??m t?? tr?????c khi ???????c th??? t??? do" ,
                        view.getContext());
            }
            initNewAge();
            addAgeHTML(age);
        }
        else {
            if (age == 6)
            {
                MainActivity.saveGame.saveJob("H???c sinh");
                txtJob.setText("H???c sinh");
                changeWork();
            }
            if(age == 18)
            {
                addAgeHTML(18);
                dialogUniversity();
                return;
            }

            if(age == 22 && University)
            {
                addAgeHTML(22);
                MainActivity.createNotification(R.drawable.graduation,"b???n ???? k???t th??c 4 n??m ?????i h???c",view.getContext());
                MainActivity.saveGame.saveJob("Th???t nghi???p");
                txtJob.setText("Th???t nghi???p");
                University = false;
                MainActivity.saveGame.saveUniversity(false);
                changeWork();
                return;
            }
            //lay su kien tuoi
            if(age < arrJsonAge.length()) {
                arrAge = arrJsonAge.getJSONArray(age);
            } else arrAge = null;
            Random random = new Random();
            //Toast.makeText(this, arrAge.length() + " - " + age, Toast.LENGTH_SHORT).show();
            if (arrAge == null||arrAge.length() == 0 ) {
                MainActivity.createNotification(MainActivity.saveGame.getAvatar(), "N??m nay kh??ng c?? s??? ki???n g?? ?????c bi???t", view.getContext());
                initNewAge();
                addAgeHTML(age);
                contentHtml = MainActivity.saveGame.getDetailActivity();
                contentHtml += "N??m nay kh??ng c?? s??? ki???n g?? ?????c bi???t<br>";
                txtContent.setText(android.text.Html.fromHtml(contentHtml));
                MainActivity.saveGame.saveDetailActivity(contentHtml);
                return;
            }
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
                addAgeHTML(age);
                dialogEventResult(title, true);
            }
        }
    }

    void dialogEvent(JSONObject[] object, boolean[] isSelection, String title, String[] event) throws JSONException {
        jsonResult = object[0];
        if (!isSelection[0]) {
            JSONArray arr = jsonResult.getJSONArray("event");
            jsonResult = arr.getJSONObject(new Random().nextInt(arr.length()));
            int age = MainActivity.saveGame.getAge() + 1;
            addAgeHTML(age);
            //Tao dialog hien thi ket qua cua event
            dialogEventResult(title, true);
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

    void dialogEventResult(String title, boolean isAgeEvent) throws JSONException {
        //Toast.makeText(this, "hey", Toast.LENGTH_SHORT).show();
        Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_event_result);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogResultAnimation;


        //Lay gia tri gan vao dialog
        LinearLayout dialogResult = dialog.findViewById(R.id.dialog_event_result);
        Button btnOke = dialog.findViewById(R.id.buttonDialogEventOke);
        TextView txtTitle = dialog.findViewById(R.id.textviewDialogEventTitle);
        TextView txtContent = dialog.findViewById(R.id.textviewDialogEventContent);
        TextView txtHappy = dialog.findViewById(R.id.textviewResultHappy);
        TextView txtHealth = dialog.findViewById(R.id.textviewResultHealth);
        TextView txtSmart = dialog.findViewById(R.id.textviewResultSmart);
        TextView txtAppearance = dialog.findViewById(R.id.textviewResultAppearance);
        TextView txtAssets = dialog.findViewById(R.id.textviewResultMoney);

        txtTitle.setText(title);
        txtContent.setText(jsonResult.getString("event"));
        contentHtml += txtContent.getText().toString() + "<br>";

        int value = 0;
        value = jsonResult.getInt("happy");
        if (value == 0) {
            dialogResult.removeView(dialog.findViewById(R.id.linearHappy));
        } else {
            toString(value, txtHappy);
            prbHappy.setProgress(prbHappy.getProgress() + value);
            this.txtHappy.setText(prbHappy.getProgress() + "%");
        }
        value = jsonResult.getInt("health");
        if (value == 0) {
            dialogResult.removeView(dialog.findViewById(R.id.linearHealth));
        } else {
            toString(value, txtHealth);
            prbHealth.setProgress(prbHealth.getProgress() + value);
            this.txtHealth.setText(prbHealth.getProgress() + "%");
        }
        value = jsonResult.getInt("smart");
        if (value == 0) {
            dialogResult.removeView(dialog.findViewById(R.id.linearSmart));
        } else {
            toString(value, txtSmart);
            prbSmart.setProgress(prbSmart.getProgress() + value);
            this.txtSmart.setText(prbSmart.getProgress() + "%");
        }
        value = jsonResult.getInt("appearance");
        if (value == 0) {
            dialogResult.removeView(dialog.findViewById(R.id.linearAppearance));
        } else {
            toString(value, txtAppearance);
            prbAppearance.setProgress(prbAppearance.getProgress() + value);
            this.txtAppearance.setText(prbAppearance.getProgress() + "%");
        }
        value = jsonResult.getInt("assets");
        if (value == 0) {
            dialogResult.removeView(dialog.findViewById(R.id.linearMoney));
        } else {
            toString(value, txtAssets);
            money += value;
            this.txtMoney.setText(money + "K VND");
            MainActivity.saveGame.saveMoney(money);
        }
        MainActivity.saveGame.savePlayerInfo(prbHappy.getProgress(), prbHealth.getProgress(), prbSmart.getProgress(), prbAppearance.getProgress());
        MainActivity.saveGame.saveDetailActivity(contentHtml);
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
                    if (MainActivity.saveGame.getHealth() <= 0) {
                        MainActivity.checkMySelf(view.getContext(), "\"Qua ?????i v?? s???c kh???e y???u k??o d??i, kh??ng ch???u n???i nh???ng bi???n c??? trong cu???c s???ng\"");
                    }
                    else if (isAgeEvent)
                    {
                        initNewAge();
                        DeathofRelation();
                    }

                    else jobEvent();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        changeProgressBackground(prbAppearance);
        changeProgressBackground(prbHappy);
        changeProgressBackground(prbHealth);
        changeProgressBackground(prbSmart);

        dialog.show();

        //Kiem tra co bi benh hay khong
        String sick = jsonResult.getString("sick");
        if (!sick.equals("")) //Co benh
        {
            ArrayList<Sick> arrSick = MainActivity.saveGame.getSick();
            int index = -1;
            for (int i = 0; i < arrSick.size(); i++) {
                if (arrSick.get(i).getSickName().equals(sick)) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                //Toast.makeText(this, "cant find sick name", Toast.LENGTH_SHORT).show();
                return;
            }
            arrSick.get(index).setSick(true);
            MainActivity.saveGame.saveSick(arrSick);
        }

    }


    void dialogJobEvent(String title) throws JSONException {
        //Toast.makeText(this, "ssdfsgsdgsdgd", Toast.LENGTH_LONG).show();
        if (!jsonResult.getBoolean("selection")) {
            JSONArray arr = jsonResult.getJSONArray("event");
            jsonResult = arr.getJSONObject(new Random().nextInt(arr.length()));
            //MainActivity.saveGame.saveSalary(jsonResult.getInt("salary"));
            //Tao dialog hien thi ket qua cua event
            dialogEventResult(title, false);
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
    void dialogJobUpgradeEvent(String title) throws JSONException {
        //Tao dialog asset
        Dialog dialog = createJobUpgradeDialog();
        //Toast.makeText(this, "Asset", Toast.LENGTH_SHORT).show();
        //Anh xa
        TextView txtContent = dialog.findViewById(R.id.textviewJobUpgradeContent);
        Button btnAccept = dialog.findViewById(R.id.buttonJobAccept);
        //Gan gia tri
        txtContent.setText(jsonResult.getString("event"));
        JSONArray arrSelect = jsonResult.getJSONArray("select");
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    try {
                        dialog.dismiss();
                        JSONArray arr = arrSelect.getJSONArray(0);
                        jsonResult = arr.getJSONObject(new Random().nextInt(arr.length()));
                        dialogEventResult(title, false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

        });
        dialog.show();
    }
    void dialogJobEventWithAsset(String title) throws JSONException {
        //Tao dialog asset
        Dialog dialog = createAssetDialog();

        //Toast.makeText(this, "Asset", Toast.LENGTH_SHORT).show();
        //Anh xa
        TextView txtContent = dialog.findViewById(R.id.textviewAssetContent);
        ImageView imgAsset = dialog.findViewById(R.id.imageviewAsset);
        TextView txtAssetName = dialog.findViewById(R.id.textviewAsset);
        Button btnAccept = dialog.findViewById(R.id.buttonAssetAccept);
        Button btnCancel = dialog.findViewById(R.id.buttonAssetCancel);

        int imageID = getResources().getIdentifier(jsonResult.getString("asset"), "drawable", view.getContext().getPackageName());
        //Gan gia tri
        txtContent.setText(jsonResult.getString("event"));
        imgAsset.setImageResource(imageID);
        txtAssetName.setText(jsonResult.getString("name"));

        JSONArray arrSelect = jsonResult.getJSONArray("select");

        //Kiem tra xem co so huu do vat nay hay chua
        boolean isOwn = false;
        ArrayList<Food> arrProduct = MainActivity.saveGame.getAsset();
        if (arrProduct != null) {
            for (int i = 0; i < arrProduct.size(); i++) {
                if (imageID == arrProduct.get(i).getImage()) //Trung ID hinh la so huu
                {
                    isOwn = true;
                    break;
                }
            }
        }
        if (!isOwn)
            btnAccept.setBackgroundResource(R.drawable.list_item_unable);
        else btnAccept.setBackgroundResource(R.drawable.custom_button_menu);

        boolean finalIsOwn = isOwn;
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!finalIsOwn)
                    MainActivity.createNotification(R.drawable.cancel, "B???n ???? s??? h???u m??n ????? n??y ????u m?? ?????ng ??!!!", view.getContext());
                else {
                    try {
                        dialog.dismiss();
                        JSONArray arr = arrSelect.getJSONArray(0);
                        jsonResult = arr.getJSONObject(new Random().nextInt(arr.length()));
                        dialogEventResult(title, false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray arr = null;
                try {
                    //Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    arr = arrSelect.getJSONArray(1);
                    jsonResult = arr.getJSONObject(new Random().nextInt(arr.length()));
                    dialog.dismiss();
                    dialogEventResult(title, false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }

    void jobEvent() throws JSONException {
        int currentSkill = MainActivity.saveGame.getSkill();
        int addSkill = jsonResult.getInt("skill");
        MainActivity.saveGame.saveSkill(currentSkill + addSkill);
        JSONArray arrJob = jsonJob.getJSONArray("event");
        for (int i = 0; i < arrJob.length(); i++) {
            //Toast.makeText(MainActivity.this, "error1", Toast.LENGTH_SHORT).show();
            jsonResult = arrJob.getJSONObject(i);
            int require = jsonResult.getInt("require");
            if (currentSkill < require && currentSkill + addSkill >= require) {
                //Toast.makeText(getActivity(), "Congviec0", Toast.LENGTH_SHORT).show();
                if (!jsonResult.getString("asset").equals(""))
                    dialogJobEventWithAsset("C??ng Vi???c");
                else if(!jsonResult.getString("newjob").equals(""))
                {
                    dialogJobUpgradeEvent("C??ng Vi???c");
                    MainActivity.saveGame.saveJob(jsonResult.getString("newjob"));
                    changeWork();
                    //jsonResult.getJSONArray("select").getJSONObject(0).
                    MainActivity.saveGame.saveSalary(jsonResult.getJSONArray("select").getJSONArray(0).getJSONObject(0).getInt("salary"));
                }
                else if (jsonResult.getBoolean("selection")) {
                    //Toast.makeText(getActivity(), "Congviec", Toast.LENGTH_SHORT).show();
                    dialogJobEvent("C??ng vi???c");
                }
                else{
                    //Toast.makeText(getActivity(), "Congviec2", Toast.LENGTH_SHORT).show();
                    dialogEventResult("C??ng vi???c", false);
                }
                break;
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    Button addButton(LinearLayout dialogCustom, String text) {
        Button btn = new Button(dialogCustom.getContext());
        btn.setText(text);
        btn.setTextSize(15);
        btn.setBackgroundResource(R.drawable.custom_button_menu);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(800, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = 10;
        params.topMargin = 10;
        params.gravity = Gravity.CENTER_HORIZONTAL;
        btn.setLayoutParams(params);
        btn.setTextColor(Color.argb(255, 16, 54, 103));
        dialogCustom.addView(btn);
        return btn;
    }
    Dialog createJobUpgradeDialog() {
        //Dinh dang dialog
        Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_job_upgrade_event);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogEventAnimation;
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
        return dialog;
    }
    Dialog createAssetDialog() {
        //Dinh dang dialog
        Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_job_asset);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogEventAnimation;
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
        return dialog;
    }

    Dialog createDialog(String title, String event) {
        //Dinh dang dialog
        Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_event);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogEventAnimation;
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
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


    void readEvent() throws JSONException {
        String jsonEvent = null;
        try {
            InputStream inputStream = view.getContext().getAssets().open("age_event.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonEvent = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        arrJsonAge = new JSONArray(jsonEvent);
    }

    void readFriend() throws JSONException {
        String jsonEvent = null;
        try {
            InputStream inputStream = view.getContext().getAssets().open("friend.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonEvent = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        MainActivity.arrFriend = new ArrayList<>();
        JSONArray jsonFriend = new JSONArray(jsonEvent);

        for (int i = 0; i < jsonFriend.length(); i++) {
            JSONObject object = jsonFriend.getJSONObject(i);
            String name = object.getString("name");
            int age = MainActivity.saveGame.getAge() + (new Random().nextInt(5) - 2);
            String avatar = object.getString("avatar");

            boolean isBoy = false;
            if (object.getString("gender").equals("boy")) {
                isBoy = true;
            }

            MainActivity.arrFriend.add(new QuanHe(name, age, new Random().nextInt(30) + 30,
                    NameOfRelationship.Friend, getResources().getIdentifier(avatar, "drawable", view.getContext().getPackageName()), isBoy));
        }
        setPeopleAvatar(MainActivity.arrFriend);
    }

    void readJob() throws JSONException {

        String jsonEvent = null;
        try {
            InputStream inputStream = view.getContext().getAssets().open("job.json");
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



    void loadGame() {
        contentHtml = MainActivity.saveGame.getDetailActivity();
        txtContent.setText(android.text.Html.fromHtml(contentHtml));
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });


        imgAvatar.setImageResource(MainActivity.saveGame.getAvatar());
        prbAppearance.setProgress(MainActivity.saveGame.getAppearance());
        prbHappy.setProgress(MainActivity.saveGame.getHappy());
        prbHealth.setProgress(MainActivity.saveGame.getHealth());
        prbSmart.setProgress(MainActivity.saveGame.getSmart());

        txtAppearance.setText(prbAppearance.getProgress() + "%");
        txtHappy.setText(prbHappy.getProgress() + "%");
        txtSmart.setText(prbSmart.getProgress() + "%");
        txtHealth.setText(prbHealth.getProgress() + "%");

        money = MainActivity.saveGame.getMoney();
        txtMoney.setText(money + "K VND");
        txtName.setText(MainActivity.saveGame.getName());
        txtJob.setText(MainActivity.saveGame.getJob());
        arrRelationship = MainActivity.saveGame.getRelationship();

        changeProgressBackground(prbAppearance);
        changeProgressBackground(prbHappy);
        changeProgressBackground(prbHealth);
        changeProgressBackground(prbSmart);

        setUserInfo();
    }

    private void setUserInfo() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txtUserName = getActivity().findViewById(R.id.textViewUserName);
                txtPlayerName = getActivity().findViewById(R.id.textViewPlayerName);
                imgUserAvatar = getActivity().findViewById(R.id.imageViewUserAvatar);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                txtUserName.setText(user.getDisplayName());
                txtPlayerName.setText(MainActivity.saveGame.getName());
                Picasso.get().load(user.getPhotoUrl()).into(imgUserAvatar);
            }
        }, 2000);
    }

    void changeWork() throws JSONException {
        switch (MainActivity.saveGame.getJob()) {
            case "H???c sinh":
                jsonJob = jsonAllJob.getJSONObject("student");
                break;
            case "T?? nh??n":
                jsonJob = jsonAllJob.getJSONObject("T?? nh??n");
                break;
            case "Ca s?? ????m c?????i":
                jsonJob = jsonAllJob.getJSONObject("Ca s?? ????m c?????i");
                break;
            case "Ca s?? ph??ng tr??":
                jsonJob = jsonAllJob.getJSONObject("Ca s?? ph??ng tr??");
                break;
            case "Ca s?? th???n t?????ng":
                jsonJob = jsonAllJob.getJSONObject("Ca s?? th???n t?????ng");
                break;
            case "DIVA":
                jsonJob = jsonAllJob.getJSONObject("DIVA");
                break;
            case "L???p tr??nh vi??n":
                jsonJob = jsonAllJob.getJSONObject("coder");
                break;
            case "Chuy??n gia c??ng ngh???":
                jsonJob = jsonAllJob.getJSONObject("Chuy??n gia c??ng ngh???");
                break;
            case "Ch??? t???ch t???p ??o??n c??ng ngh??? th??ng tin":
                jsonJob = jsonAllJob.getJSONObject("Ch??? t???ch t???p ??o??n c??ng ngh??? th??ng tin");
                break;
            case "C??i WIN d???o":
                jsonJob = jsonAllJob.getJSONObject("C??i WIN d???o");
                break;
            case "Ph??? b???p":
                jsonJob = jsonAllJob.getJSONObject("Ph??? b???p");
                break;
            case "?????u b???p":
                jsonJob = jsonAllJob.getJSONObject("?????u b???p");
                break;
            case "Chuy??n gia ???m th???c":
                jsonJob = jsonAllJob.getJSONObject("Chuy??n gia ???m th???c");
                break;
            case "VUA ?????U B???P":
                jsonJob = jsonAllJob.getJSONObject("VUA ?????U B???P");
                break;
            case "Ph??ng vi??n":
                jsonJob = jsonAllJob.getJSONObject("Ph??ng vi??n");
                break;
            case "Tr?????ng chuy??n m???c":
                jsonJob = jsonAllJob.getJSONObject("Tr?????ng chuy??n m???c");
                break;
            case "Th?? k?? t??a so???n":
                jsonJob = jsonAllJob.getJSONObject("Th?? k?? t??a so???n");
                break;
            case "T???ng bi??n t???p":
                jsonJob = jsonAllJob.getJSONObject("T???ng bi??n t???p");
                break;
            case "C???u th??? d??? b???":
                jsonJob = jsonAllJob.getJSONObject("C???u th??? d??? b???");
                break;
            case "Ch??n s??t tri???n v???ng":
                jsonJob = jsonAllJob.getJSONObject("Ch??n s??t tri???n v???ng");
                break;
            case "Ng??i sao b??ng ????":
                jsonJob = jsonAllJob.getJSONObject("Ng??i sao b??ng ????");
                break;
            case "Huy???n tho???i b??ng ????":
                jsonJob = jsonAllJob.getJSONObject("Huy???n tho???i b??ng ????");
                break;
            case "B???i b??n":
                jsonJob = jsonAllJob.getJSONObject("B???i b??n");
                break;
            case "Thu ng??n":
                jsonJob = jsonAllJob.getJSONObject("Thu ng??n");
                break;
            case "Qu???n l?? nh?? h??ng":
                jsonJob = jsonAllJob.getJSONObject("Qu???n l?? nh?? h??ng");
                break;
            case "Ch??? nh?? h??ng":
                jsonJob = jsonAllJob.getJSONObject("Ch??? nh?? h??ng");
                break;
            case "Di???n vi??n ????ng th???":
                jsonJob = jsonAllJob.getJSONObject("Di???n vi??n ????ng th???");
                break;
            case "Di???n vi??n ch??nh":
                jsonJob = jsonAllJob.getJSONObject("Di???n vi??n ch??nh");
                break;
            case "Ng??i sao ??i???n ???nh":
                jsonJob = jsonAllJob.getJSONObject("Ng??i sao ??i???n ???nh");
                break;
            case "Th???c t???p sinh":
                jsonJob = jsonAllJob.getJSONObject("Th???c t???p sinh");
                break;
            case "Gi??o vi??n":
                jsonJob = jsonAllJob.getJSONObject("Gi??o vi??n");
                break;
            case "Tr?????ng b??? m??n":
                jsonJob = jsonAllJob.getJSONObject("Tr?????ng b??? m??n");
                break;
            case "Hi???u tr?????ng":
                jsonJob = jsonAllJob.getJSONObject("Hi???u tr?????ng");
                break;
            case "B??n h??ng rong":
                jsonJob = jsonAllJob.getJSONObject("B??n h??ng rong");
                break;
            case "Ch??? shop online":
                jsonJob = jsonAllJob.getJSONObject("Ch??? shop online");
                break;
            case "Qu???n l?? si??u th??? mini":
                jsonJob = jsonAllJob.getJSONObject("Qu???n l?? si??u th??? mini");
                break;
            case "Binh nh???t":
                jsonJob = jsonAllJob.getJSONObject("Binh nh???t");
                break;
            case "Trung s??":
                jsonJob = jsonAllJob.getJSONObject("Trung s??");
                break;
            case "Th?????ng ??y":
                jsonJob = jsonAllJob.getJSONObject("Th?????ng ??y");
                break;
            case "?????i t??":
                jsonJob = jsonAllJob.getJSONObject("?????i t??");
                break;
            case "Nh??n vi??n sale":
                jsonJob = jsonAllJob.getJSONObject("Nh??n vi??n sale");
                break;
            case "Tr?????ng ph??ng marketing":
                jsonJob = jsonAllJob.getJSONObject("Tr?????ng ph??ng marketing");
                break;
            case "Gi??m ?????c kinh doanh":
                jsonJob = jsonAllJob.getJSONObject("Gi??m ?????c kinh doanh");
                break;
            case "Ch???y Grab":
                jsonJob = jsonAllJob.getJSONObject("Ch???y Grab");
                break;
            case "T??i x??? Taxi":
                jsonJob = jsonAllJob.getJSONObject("T??i x??? Taxi");
                break;
            case "Qu???n l?? ?????i xe":
                jsonJob = jsonAllJob.getJSONObject("Qu???n l?? ?????i xe");
                break;
            case "Ch??? c??ng ty Taxi":
                jsonJob = jsonAllJob.getJSONObject("Ch??? c??ng ty Taxi");
                break;
            case "B??c s?? th???c t???p":
                jsonJob = jsonAllJob.getJSONObject("B??c s?? th???c t???p");
                break;
            case "B??c s?? ch??nh":
                jsonJob = jsonAllJob.getJSONObject("B??c s?? ch??nh");
                break;
            case "B??c s?? tr?????ng khoa":
                jsonJob = jsonAllJob.getJSONObject("B??c s?? tr?????ng khoa");
                break;
            case "Vi???n tr?????ng":
                jsonJob = jsonAllJob.getJSONObject("Vi???n tr?????ng");
                break;
            case "Sinh vi??n":
                jsonJob = jsonAllJob.getJSONObject("Sinh vi??n");
                break;
            case "Th???t nghi???p":
                jsonJob = jsonAllJob.getJSONObject("Th???t nghi???p");
                break;
            case "Tr??? tr??u":
                jsonJob = jsonAllJob.getJSONObject("Tr??? tr??u");
                break;
        }
    }

    // init tam thoi
    void init(String name, boolean isBoy) throws JSONException {
        String json = null;
        try {
            InputStream inputStream = view.getContext().getAssets().open("parent.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject object = new JSONObject(json);
        JSONArray arrFather = object.getJSONArray("father");
        JSONArray arrMother = object.getJSONArray("mother");
        JSONArray arrJob = object.getJSONArray("job");
        Random random = new Random();

        //Tao quan he Bo Me
        String fatherName = arrFather.getString(random.nextInt(arrFather.length()));
        int fatherAge = (random.nextInt(11) + 20);
        QuanHe father = new QuanHe(fatherName, fatherAge, 100, NameOfRelationship.Dad, R.drawable.boy_5, true); //Thay hinh sau
        String motherName = arrMother.getString(random.nextInt(arrMother.length()));
        int motherAge = (random.nextInt(11) + 20);
        QuanHe mother = new QuanHe(motherName, motherAge, 100, NameOfRelationship.Mom, R.drawable.girl_5, false); //Thay hinh sau

        ArrayList<QuanHe> arrRelationship = new ArrayList<>();
        arrRelationship.add(father);
        arrRelationship.add(mother);
        //Them cho vui
//        arrRelationship.add(new QuanHe("Tr???n Thanh V??", 19, 50, NameOfRelationship.Friend,R.drawable.boy, true));
//        arrRelationship.add(new QuanHe("Nguy???n Thi???n Sua", 19, 50, NameOfRelationship.Friend, R.drawable.boy, true));
//        arrRelationship.add(new QuanHe("Nguy???n Hi???u Ngh??a", 19, 50, NameOfRelationship.Friend, R.drawable.boy, true));
//        arrRelationship.add(new QuanHe("Mai Long Th??nh", 19, 80, NameOfRelationship.Friend, R.drawable.boy, true));
//        arrRelationship.add(new QuanHe("V?? Th??nh Ph??t", 19, 20, NameOfRelationship.Friend, R.drawable.boy, true));
//        arrRelationship.add(new QuanHe("Ho??ng Nh???t Ti???n", 19, 50, NameOfRelationship.Friend, R.drawable.boy, true));
        MainActivity.saveGame.saveRelationship(arrRelationship);
        this.arrRelationship = arrRelationship;
        MainActivity.saveGame.saveNewFriendInYear(0);
        MainActivity.saveGame.saveNumberOfFriends(0);
        MainActivity.saveGame.saveNumberOfGirlFriend(0);

        String gender = "N???";
        if (isBoy)
            gender = "Nam";
        //Tao ngay sinh
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        contentHtml = "<h5> <font color=\"blue\">Tu???i 0</font></h5>" +
                "T??i t??n " + name + " - " + gender + "<br>" +
                "Sinh ng??y " + format.format(date) + "<br>" +
                "B??? t??i l?? " + fatherName + " - " +
                arrJob.getString(random.nextInt(arrJob.length())) +
                " (" + fatherAge + " tu???i )" + "<br>" +
                "M??? t??i l?? " + motherName + " - " +
                arrJob.getString(random.nextInt(arrJob.length())) +
                " (" + motherAge + " tu???i )" + "<br>";
        //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

        MainActivity.saveGame.saveGender(isBoy);
        int avatarID = getResources().getIdentifier("boy_0", "drawable", view.getContext().getPackageName());
        if (!isBoy)
            avatarID = getResources().getIdentifier("girl_0", "drawable", view.getContext().getPackageName());
        imgAvatar.setImageResource(avatarID);
        MainActivity.saveGame.saveAvatar(avatarID);
        MainActivity.saveGame.saveAge(0);
        MainActivity.saveGame.saveBirthDay(format.format(date));
        MainActivity.saveGame.saveMoney(0);
        MainActivity.saveGame.saveDetailActivity(contentHtml);
        MainActivity.saveGame.saveName(name);
        MainActivity.saveGame.saveJob("Tr??? tr??u");
        MainActivity.saveGame.saveSkill(0);
        MainActivity.saveGame.saveExercise(0);
        MainActivity.saveGame.saveJogging(0);
        MainActivity.saveGame.saveJogging(0);
        MainActivity.saveGame.saveCrime(0);
        MainActivity.saveGame.saveAsset(null);
        MainActivity.saveGame.saveTienAn(null);
        MainActivity.saveGame.saveUniversity(false);
        MainActivity.saveGame.saveSalary(0);
        btnAssets.setEnabled(false);
        btnActivity.setEnabled(false);
        btnWork.setEnabled(false);

        btnAssets.setBackgroundResource(R.drawable.list_item_unable);
        btnActivity.setBackgroundResource(R.drawable.list_item_unable);
        btnWork.setBackgroundResource(R.drawable.list_item_unable);

        txtJob.setText(MainActivity.saveGame.getJob());
        txtMoney.setText("0K VND");
        txtContent.setText(android.text.Html.fromHtml(contentHtml));
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
        txtName.setText(name);

        prbHealth.setProgress(100);
        prbHappy.setProgress(80);
        prbSmart.setProgress(30);
        prbAppearance.setProgress(50);

        changeProgressBackground(prbAppearance);
        changeProgressBackground(prbHappy);
        changeProgressBackground(prbHealth);
        changeProgressBackground(prbSmart);

        txtAppearance.setText(prbAppearance.getProgress() + "%");
        txtHappy.setText(prbHappy.getProgress() + "%");
        txtSmart.setText(prbSmart.getProgress() + "%");
        txtHealth.setText(prbHealth.getProgress() + "%");
        MainActivity.saveGame.savePlayerInfo(prbHappy.getProgress(), prbHealth.getProgress(), prbSmart.getProgress(), prbAppearance.getProgress());

        ArrayList<Sick> arrSick = new ArrayList<>();
        arrSick.add(new Sick(false, "??au r??ng", "Nha s??", "nhasi", 10));
        arrSick.add(new Sick(false, "??au m???t", "B??c s?? m???t", "mat", 10));
        arrSick.add(new Sick(false, "tai m??i h???ng", "B??c s?? tai m??i h???ng", "taimuihong", 100));
        arrSick.add(new Sick(false, "m???n ng???a", "B??c s?? da li???u", "dalieu", 10));
        arrSick.add(new Sick(false, "c???m", "B??c s?? c???m s???t", "camsot", 20));
        arrSick.add(new Sick(false, "tr??", "B??c s?? tr??", "tri", 30));
        arrSick.add(new Sick(false, "s???t xu???t huy???t", "B??c s?? c???m s???t", "sotxuathuyet", 100));

        MainActivity.saveGame.saveSick(arrSick);
    }

    void initNewAge() {
        //Toast.makeText(this, "new age", Toast.LENGTH_SHORT).show();
        int age = MainActivity.saveGame.getAge() + 1;
        MainActivity.saveGame.saveBoiSN(0);
        MainActivity.saveGame.saveTuVi(0);
        MainActivity.saveGame.saveBoiTinh(0);
        MainActivity.saveGame.saveAge(age);
        MainActivity.saveGame.saveExercise(0);
        MainActivity.saveGame.saveJogging(0);
        MainActivity.saveGame.saveJogging(0);
        MainActivity.saveGame.saveLibrary(0);
        MainActivity.saveGame.saveNewFriendInYear(0);
        MainActivity.saveGame.saveNhaHang(0);
        MainActivity.saveGame.saveNuocEp(0);
        MainActivity.saveGame.saveTraSua(0);
        MainActivity.saveGame.saveCaPhe(0);
        MainActivity.saveGame.saveRuou(0);
        MainActivity.saveGame.saveBia(0);
        MainActivity.saveGame.saveHamBurGer(0);
        MainActivity.saveGame.saveBanhMi(0);
        MainActivity.saveGame.saveMy(0);
        MainActivity.saveGame.saveTraiCay(0);
        MainActivity.saveGame.savePizza(0);
        MainActivity.saveGame.saveLau(0);
        MainActivity.saveGame.saveCom(0);
        MainActivity.saveGame.saveHaiSan(0);
        MainActivity.saveGame.saveGa(0);
        MainActivity.saveGame.saveRauCu(0);
        MainActivity.saveGame.saveKeo(0);
        MainActivity.saveGame.saveFastFood(0);
        MainActivity.saveGame.saveCrime(0);
        MainActivity.saveGame.saveDaNang(0);
        MainActivity.saveGame.savePhuQuoc(0);
        MainActivity.saveGame.saveVungTau(0);
        MainActivity.saveGame.saveThaiLand(0);
        MainActivity.saveGame.saveKorea(0);
        MainActivity.saveGame.saveAmerica(0);
        MainActivity.saveGame.saveHongKong(0);
        for (int i = 0; i < arrRelationship.size(); i++) {
            QuanHe friend = arrRelationship.get(i);
            friend.setInteraction(0);
            friend.setTuoi(friend.getTuoi() + 1); // Them tuoi
            if (friend.getQuanHe() == NameOfRelationship.Friend ||
                    friend.getQuanHe() == NameOfRelationship.GirlFriend ||
                    friend.getQuanHe() == NameOfRelationship.BoyFriend) {
                arrRelationship.get(i).setDoThanMat(friend.getDoThanMat() - 20); //Giam moi quan he
            }
        }
        setPeopleAvatar(arrRelationship);
        MainActivity.saveGame.saveRelationship(arrRelationship);

        for (int i = 0; i < MainActivity.arrFriend.size(); i++) {
            MainActivity.arrFriend.get(i).setTuoi(MainActivity.arrFriend.get(i).getTuoi() + 1);
        }
        setPeopleAvatar(MainActivity.arrFriend);

        setAvatar();
        dialogLostFriend(0);

        //xet suc khoe
        int health = MainActivity.saveGame.getHealth();
        if (prbHappy.getProgress() < 30) //Kiem tra xem co van de ve tam li hay khong (happy<30)
        {
            health -= (30 - prbHappy.getProgress());
        }
        health -= (int) (MainActivity.saveGame.getAge() / 6); //Tru suc khoe theo do tuoi, tuoi cang cao tru cang nhieu
        String reason = "";
        if (health <= 0)
            reason = "\"Qua ?????i v?? tu???i gi?? s???c y???u\"";
        else {
            ArrayList<Sick> arrSick = MainActivity.saveGame.getSick(); //Kiem tra co benh ma khong chua hay khong
            for (int i = 0; i < arrSick.size(); i++) {
                if (arrSick.get(i).isSick()) {
                    health -= arrSick.get(i).getHealth();
                }
            }
            if (health <= 0)
                reason = "\"Qua ?????i v?? s???c kh???e y???u k??o d??i, b???nh t???t kh??ng ???????c ch???a tr??? k???p th???i\"";
        }
        //Luu suc khoe lai
        MainActivity.saveGame.savePlayerInfo(MainActivity.saveGame.getHappy(),
                health,
                MainActivity.saveGame.getSmart(),
                MainActivity.saveGame.getAppearance());
        prbHealth.setProgress(health);
        txtHealth.setText(prbHealth.getProgress() + "%");
        changeProgressBackground(prbHealth);
        MainActivity.checkMySelf(view.getContext(), reason);
    }

    boolean isVehicle()
    {
        ArrayList<Food> arrAsset = MainActivity.saveGame.getAsset();
        if (arrAsset == null)
            return false;
        for (int i=0; i<arrAsset.size(); i++)
        {
            if (arrAsset.get(i).getImage() == R.drawable.supercar ||
                    arrAsset.get(i).getImage() == R.drawable.car ||
                    arrAsset.get(i).getImage() == R.drawable.vespa ||
                    arrAsset.get(i).getImage() == R.drawable.motorcycle ||
                    arrAsset.get(i).getImage() == R.drawable.helicopter ||
                    arrAsset.get(i).getImage() == R.drawable.airplane ||
                    arrAsset.get(i).getImage() == R.drawable.boat)
                return true;
        }
        return false;
    }

    boolean isHouse()
    {
        ArrayList<Food> arrAsset = MainActivity.saveGame.getAsset();
        if (arrAsset == null)
            return false;
        for (int i=0; i<arrAsset.size(); i++)
        {
            if (arrAsset.get(i).getImage() == R.drawable.chungcu ||
                    arrAsset.get(i).getImage() == R.drawable.simplehouse ||
                    arrAsset.get(i).getImage() == R.drawable.masion )
                return true;
        }
        return false;
    }

    void setPeopleAvatar(ArrayList<QuanHe> array) {
        for (int i = 0; i < array.size(); i++) {
            QuanHe quanHe = array.get(i);
            int id = quanHe.getHinhAnh();
            if(id == R.drawable.death)
            {
                continue;
            }
            if (quanHe.isBoy()) //Boy
            {
                if (quanHe.getTuoi() >= 60) id = R.drawable.boy_8;
                else if (quanHe.getTuoi() >= 40) {
                    id = R.drawable.boy_7;
                } else if (quanHe.getTuoi() >= 30) {
                    id = R.drawable.boy_6;
                } else if (quanHe.getTuoi() >= 20) {
                    id = R.drawable.boy_5;
                } else if (quanHe.getTuoi() >= 15) {
                    id = R.drawable.boy_4;
                } else if (quanHe.getTuoi() >= 10) {
                    id = R.drawable.boy_3;
                } else if (quanHe.getTuoi() >= 5) {
                    id = R.drawable.boy_2;
                } else if (quanHe.getTuoi() >= 2) {
                    id = R.drawable.boy;
                }
            } else {
                if (quanHe.getTuoi() >= 60) id = R.drawable.girl_8;
                else if (quanHe.getTuoi() >= 40) {
                    id = R.drawable.girl_7;
                } else if (quanHe.getTuoi() >= 30) {
                    id = R.drawable.girl_6;
                } else if (quanHe.getTuoi() >= 20) {
                    id = R.drawable.girl_5;
                } else if (quanHe.getTuoi() >= 15) {
                    id = R.drawable.girl_4;
                } else if (quanHe.getTuoi() >= 10) {
                    id = R.drawable.girl_3;
                } else if (quanHe.getTuoi() >= 5) {
                    id = R.drawable.girl_2;
                } else if (quanHe.getTuoi() >= 2) {
                    id = R.drawable.girl;
                }
            }
            quanHe.setHinhAnh(id);
        }
    }

    void setAvatar() {
        int age = MainActivity.saveGame.getAge();
        String avatarName = "";

        if (MainActivity.saveGame.getGender()) //Boy
        {
            if (age == 2) avatarName = "boy";
            else if (age == 5) {
                avatarName = "boy_2";
            } else if (age == 10) {
                avatarName = "boy_3";
            } else if (age == 15) {
                avatarName = "boy_4";
            } else if (age == 20) {
                avatarName = "boy_5";
            } else if (age == 30) {
                avatarName = "boy_6";
            } else if (age == 40) {
                avatarName = "boy_7";
            } else if (age >= 60) {
                avatarName = "boy_8";
            }
        } else {
            if (age == 2) avatarName = "girl";
            else if (age == 5) {
                avatarName = "girl_2";
            } else if (age == 10) {
                avatarName = "girl_3";
            } else if (age == 15) {
                avatarName = "girl_4";
            } else if (age == 20) {
                avatarName = "girl_5";
            } else if (age == 30) {
                avatarName = "girl_6";
            } else if (age == 40) {
                avatarName = "girl_7";
            } else if (age >= 60) {
                avatarName = "girl_8";
            }
        }

        if (avatarName.equals(""))
            return;
        int id = getResources().getIdentifier(avatarName, "drawable", view.getContext().getPackageName());
        MainActivity.saveGame.saveAvatar(id);
        imgAvatar.setImageResource(id);
    }

    void dialogLostFriend(int index) {
        if (index == arrRelationship.size()) {
            MainActivity.saveGame.saveRelationship(arrRelationship);
            return;
        }
        QuanHe friend = arrRelationship.get(index);
        if (friend.getDoThanMat() <= 0) {
            Dialog dialog = MainActivity.createNotification(R.drawable.cancel, "M??i quan h??? gi???a b???n v?? " + friend.getHoten() + " ???? ????? v???.", view.getContext());
            Button btnOke = dialog.findViewById(R.id.buttonNotificationtOke);
            arrRelationship.remove(index);
            MainActivity.saveGame.saveNumberOfFriends(MainActivity.saveGame.getNumberOfFriends() - 1);
            btnOke.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dialogLostFriend(index);
                }
            });
        } else {
            dialogLostFriend(index + 1);
        }
    }

    void addAgeHTML(int age) {
        contentHtml = MainActivity.saveGame.getDetailActivity();
        contentHtml += "<h5> <font color=\"blue\">Tu???i " + age + "</font></h5>";
        if (age == 18)
        {
            contentHtml += "B??? m??? quy???t ?????nh t??? nay cho b???n ra s???ng t??? l???p v???i s??? ti???n 20 tri???u <br />";
            MainActivity.saveGame.saveDetailActivity(contentHtml);
            MainActivity.createNotification(R.drawable.asset,
                    "B??? m??? quy???t ?????nh t??? nay cho b???n ra s???ng t??? l???p v???i s??? ti???n 20 tri???u",
                    getActivity());
            MainActivity.saveGame.saveMoney(MainActivity.saveGame.getMoney() + 20000);
            txtMoney.setText(MainActivity.saveGame.getMoney() + "K VND");
        } else if (age > 18)
        {
            int money = 0;
            if (!isHouse())
            {
                contentHtml += "Tr??? ti???n thu?? nh?? 3 tri???u <br />";
                money += 3000;
            }
            if (!isVehicle())
            {
                contentHtml += "Tr??? ti???n ??i xe nh?? 2 tri???u <br />";
                money += 2000;
            }
            String salary = String.format( "%,d", MainActivity.saveGame.getSalary()*1000);
            contentHtml += "Ti???n l????ng c???a b???n m???i n??m: " + salary +" VND<br />";
            MainActivity.saveGame.saveMoney(MainActivity.saveGame.getMoney() - money + MainActivity.saveGame.getSalary());
            txtMoney.setText(MainActivity.saveGame.getMoney() + "K VND");
            //Toast.makeText(getActivity(), "" + MainActivity.saveGame.getSalary(), Toast.LENGTH_SHORT).show();
        }
        txtContent.setText(android.text.Html.fromHtml(contentHtml));
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        MainActivity.saveGame.saveDetailActivity(contentHtml);
    }

    void toString(int value, TextView txtResult) {
        if (value > 0)
            txtResult.setText("+ " + value);
        else if (value < 0) {
            txtResult.setText("- " + (-1 * value));
            txtResult.setTextColor(Color.RED);
        }
    }

    void changeProgressBackground(ProgressBar pb) {
        int progress = pb.getProgress();
        if (progress >= 80)
            pb.setProgressDrawable(getResources().getDrawable(R.drawable.custom_progressbar));
        else if (progress < 80 && progress > 30)
            pb.setProgressDrawable(getResources().getDrawable(R.drawable.custom_progressbar_medium));
        else pb.setProgressDrawable(getResources().getDrawable(R.drawable.custom_progressbar_low));
        //Toast.makeText(this, "a", Toast.LENGTH_SHORT).show();
    }

    void dialogUniversity() {
        Dialog dialog = new Dialog(view.getContext());
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_university);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogResultAnimation;
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });

        //Anh xa
        ListView lvUni = dialog.findViewById(R.id.listViewUni);
        ArrayList<University> arrUniversity = new ArrayList<>();

        arrUniversity.add(new University(R.drawable.angiang, "?????i h???c An Giang", 1, 40));
        arrUniversity.add(new University(R.drawable.uit, "?????i h???c C??ng ngh??? Th??ng tin", 5, 60));
        arrUniversity.add(new University(R.drawable.fpt, "?????i h???c FPT", 1, 40));
        arrUniversity.add(new University(R.drawable.bachkhoa, "?????i h???c B??ch Khoa", 5, 60));
        arrUniversity.add(new University(R.drawable.hoasen, "?????i h???c Hoa Sen", 3, 50));
        arrUniversity.add(new University(R.drawable.yduoc, "?????i h???c Y D?????c", 4, 55));
        arrUniversity.add(new University(R.drawable.cantho, "?????i h???c C???n Th??", 2, 45));
        arrUniversity.add(new University(R.drawable.rmit, "?????i h???c RMIT", 4, 55));
        arrUniversity.add(new University(R.drawable.ngoaithuong, "?????i h???c Ngo???i th????ng", 4, 55));
        arrUniversity.add(new University(R.drawable.kinhte, "?????i h???c Kinh t???", 4, 55));
        arrUniversity.add(new University(R.drawable.supham, "?????i h???c S?? ph???m", 2, 45));
        arrUniversity.add(new University(R.drawable.suphamkithuat, "?????i h???c S?? ph???m K?? thu???t", 4, 55));
        arrUniversity.add(new University(R.drawable.tonducthang, "?????i h???c T??n ?????c Th???ng", 4, 55));
        arrUniversity.add(new University(R.drawable.cancel, "Kh??ng h???c", 0, 0));
        UniversityAdapter adapter = new UniversityAdapter(view.getContext(), R.layout.university_line, arrUniversity);
        lvUni.setAdapter(adapter);

        lvUni.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (arrUniversity.get(position).getName().equals("Kh??ng h???c")) {
                    contentHtml += "B???n quy???t ?????nh kh??ng h???c ?????i h???c<br/>";
                    MainActivity.saveGame.saveJob("Th???t nghi???p");
                } else if (arrUniversity.get(position).getRequire() <= MainActivity.saveGame.getSmart()) {
                    contentHtml += "B???n ch???n v??o h???c t???i tr?????ng " + arrUniversity.get(position).getName() + "<br/>";
                    MainActivity.saveGame.saveJob("Sinh vi??n");
                    University = true;
                    MainActivity.saveGame.saveUniversity(University);
                    try {
                        changeWork();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else return;
                MainActivity.saveGame.saveDetailActivity(contentHtml);
                txtContent.setText(android.text.Html.fromHtml(contentHtml));
                txtJob.setText(MainActivity.saveGame.getJob());
                dialog.dismiss();
                initNewAge();
            }
        });

        dialog.show();
    }

    private void AnhXa() {
        btnRelationship = (Button) view.findViewById(R.id.buttonRelationship);
        btnActivity = (Button) view.findViewById(R.id.buttonActivity);

        imgAvatar = view.findViewById(R.id.imageAvatar);
        txtContent = view.findViewById(R.id.textViewDetail);
        txtAppearance = view.findViewById(R.id.txtAppearance);
        txtHappy = view.findViewById(R.id.txtHappy);
        txtSmart = view.findViewById(R.id.txtSmart);
        txtHealth = view.findViewById(R.id.txtHealth);
        txtMoney = view.findViewById(R.id.textviewMoney);
        txtName = view.findViewById(R.id.textviewName);
        txtJob = view.findViewById(R.id.textviewJob);

        prbAppearance = view.findViewById(R.id.progressbarAppearance);
        prbHappy = view.findViewById(R.id.progressbarHappy);
        prbHealth = view.findViewById(R.id.progressbarHealth);
        prbSmart = view.findViewById(R.id.progressbarSmart);
        prbLoadData = view.findViewById(R.id.progressBarLoadData);

        ibtnAddAge = view.findViewById(R.id.imagebuttonAddAge);
        btnAssets = view.findViewById(R.id.buttonAssets);
        btnWork = view.findViewById(R.id.buttonInfant);
        scrollView = view.findViewById(R.id.scrollViewText);
        txtScrollviewContent = view.findViewById(R.id.textViewDetail);


        if (MainActivity.saveGame == null) {
            MainActivity.saveGame = new SaveGame();
            //Toast.makeText(this, "Save game null", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main, container, false);
        AnhXa();
        MainActivity.saveGame.setOnLoaded(new LoadDone() {
            @Override
            public void onLoaded() {
                prbLoadData.setVisibility(ProgressBar.INVISIBLE);
                //Toast.makeText(MainActivity.this, "Loaded " + time, Toast.LENGTH_SHORT).show();
                namTu = MainActivity.saveGame.getNamTu();
                tienAn = MainActivity.saveGame.getTienAn();
                University = MainActivity.saveGame.getUniversity();

                if(MainActivity.saveGame.getAge() <= 6)
                {
                    btnAssets.setEnabled(false);
                    btnActivity.setEnabled(false);

                    btnAssets.setBackgroundResource(R.drawable.list_item_unable);
                    btnActivity.setBackgroundResource(R.drawable.list_item_unable);
                }
                if(namTu!=0)
                {
                    MainActivity.saveGame.saveJob("T?? nh??n");
                    TempAge = MainActivity.saveGame.getAge();

                    btnAssets.setEnabled(false);
                    btnActivity.setEnabled(false);
                    btnRelationship.setEnabled(false);

                    btnAssets.setBackgroundResource(R.drawable.list_item_unable);
                    btnActivity.setBackgroundResource(R.drawable.list_item_unable);
                    btnRelationship.setBackgroundResource(R.drawable.list_item_unable);

                    scrollView.setBackgroundResource(R.drawable.background_prison);
                    txtScrollviewContent.setVisibility(View.INVISIBLE);
                    
                    MainActivity.createNotification(R.drawable.police,
                            "B???n b??? b???t v?? t???i " + tienAn +", b???n s??? kh??ng th??? th???c hi???n ???????c m???t s??? ho???t ?????ng th?????ng ng??y c???a m??nh",
                            view.getContext());
                }

                try {
                    if (MainActivity.saveGame.getDetailActivity().equals("")) {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.startInit();
                    }
                    else {
                        loadGame();
                        readEvent();
                        readJob();
                        readFriend();
                        changeWork();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //Toast.makeText(this, "Load Data", Toast.LENGTH_SHORT).show();
        MainActivity.saveGame.loadData();

        ibtnAddAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(view.getContext(), "new age", Toast.LENGTH_SHORT).show();
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
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (requestCode == REQUEST_CODE_INIT && resultCode == RESULT_OK && data != null) {
            String name = data.getStringExtra("name");
            boolean isBoy = data.getBooleanExtra("gender", false);

            //Toast.makeText(getActivity(), "INIT", Toast.LENGTH_SHORT).show();
            try {
                init(name, isBoy);
                readJob();
                changeWork();
                //Toast.makeText(this, "Changed", Toast.LENGTH_SHORT).show();
                readEvent();
                //Toast.makeText(this, "Event", Toast.LENGTH_SHORT).show();
                readFriend();
                //Toast.makeText(this, "Friend", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}