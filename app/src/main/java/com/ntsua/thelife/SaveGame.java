package com.ntsua.thelife;

import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SaveGame {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    Gson gson;

    public SaveGame(SharedPreferences preferences) {
        this.preferences = preferences;
        editor = this.preferences.edit();
        gson = new Gson();
    }

    public  void saveEnglish(boolean english)
    {
        editor.putBoolean("english", english);
        editor.commit();
    }

    public void saveDriving(boolean driving)
    {
        editor.putBoolean("driving", driving);
        editor.commit();
    }

    public void saveNumberOfFriends(int number)
    {
        editor.putInt("allfiend", number);
        editor.commit();
    }

    public void saveJogging(int time) {
        editor.putInt("jogging", time);
        editor.commit();
    }

    public void saveNewFriendInYear(int friend)
    {
        editor.putInt("friend", friend);
        editor.commit();
    }

    public void saveExercise(int time) {
        editor.putInt("exercise", time);
        editor.commit();
    }

    public void saveLibrary(int time) {
        editor.putInt("library", time);
        editor.commit();
    }

    public void saveSalary(int salary) {
        editor.putInt("salary", salary);
        editor.commit();
    }

    public void saveSkill(int skill) {
        editor.putInt("skill", skill);
        editor.commit();
    }

    public void saveName(String name) {
        editor.putString("name", name);
        editor.commit();
    }

    public void saveMoney(int money) {
        editor.putInt("money", money);
        editor.commit();
    }

    public void saveAge(int age) {
        editor.putInt("age", age);
        editor.commit();
    }

    public void savePlayerInfo(int happy, int health, int smart, int appearance) {
        editor.putInt("happy", happy);
        editor.putInt("health", health);
        editor.putInt("smart", smart);
        editor.putInt("appearance", appearance);
        editor.commit();
    }

    public void saveDetailActivity(String s) {
        editor.putString("detail", s);
        editor.commit();
    }

    public void saveRelationship(ArrayList<QuanHe> arrRelationship) {
        String json = gson.toJson(arrRelationship);
        editor.putString("relationship", json);
        editor.commit();
    }

    public void saveJob(String job) {
        editor.putString("job", job);
        editor.commit();
    }

    public boolean getEnglish()
    {
        return preferences.getBoolean("english", false);
    }

    public boolean getDriving()
    {
        return preferences.getBoolean("driving", false);
    }

    public int getNumberOfFriends()
    {
        return preferences.getInt("allfriend", 0);
    }

    public int getNewFriendInYear()
    {
        return preferences.getInt("friend", 0);
    }

    public int getJogging()
    {
        return preferences.getInt("jogging", 0);
    }

    public int getTuVi()
    {
        return preferences.getInt("Bói tử vi", 0);
    }

    public int getBoiSN()
    {
        return preferences.getInt("Bói công danh sự nghiệp", 0);
    }

    public int getBoiTinh()
    {
        return preferences.getInt("Bói tình duyên", 0);
    }

    public int getExercise()
    {
        return preferences.getInt("exercise", 0);
    }

    public int getLibrary()
    {
        return preferences.getInt("library", 0);
    }

    public int getSalary()
    {
        return preferences.getInt("salary", 0);
    }

    public String getName(){
        return preferences.getString("name", "NoName");
    }

    public int getHappy()
    {
        return  preferences.getInt("happy", 0);
    }

    public int getHealth()
    {
        return  preferences.getInt("health", 0);
    }

    public int getSmart()
    {
        return  preferences.getInt("smart", 0);
    }

    public int getAppearance()
    {
        return  preferences.getInt("appearance",0);
    }

    public String getJob()
    {
        return preferences.getString("job", "unemployment");
    }

    public int getMoney()
    {
        return preferences.getInt("money", 5000);
    }

    public int getAge(){ return preferences.getInt("age", 0);}

    public String getDetailActivity() {
        return preferences.getString("detail", "");
    }

    public int getSkill()
    {
        return preferences.getInt("skill", 0);
    }

    public ArrayList<QuanHe> getRelationship(){
        String json = preferences.getString("relationship", "");
        Type type = new TypeToken<ArrayList<QuanHe>>() {}.getType();
        ArrayList<QuanHe> arrRelationship = gson.fromJson(json, type);
        return arrRelationship;
    }
}
