package com.example.mabar_v1.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.mabar_v1.login.LoginActivity;

import java.util.Map;

public class SessionUser {
    private Context cont;
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;

    public SessionUser(Context _cont)
    {
        this.cont = _cont;
        this.sp = cont.getSharedPreferences("circleSession", Context.MODE_PRIVATE);
        this.edit = sp.edit();
    }

    public boolean has(String key) {
        return sp.contains(key);
    }

    public Boolean init_session() {
        Boolean valid = false;
        if(!cek_session()) {
            valid = true;
//            DialogSignUp dialog = new DialogSignUp(cont);
//            dialog.showDialog();
            Intent in = new Intent(cont, LoginActivity.class);
            cont.startActivity(in);
            ((Activity) cont).finish();
//            this.setBoolean("isSignIn",true);
//            this.commitSess();
        } else {
            this.edit = this.sp.edit();
            valid = true;
        }
        return valid;
    }

    public SharedPreferences getSp() {return this.sp; }
    public SharedPreferences.Editor getEdit() {return this.edit; }
    public String getString(String key) {return this.sp.getString(key,"");}
    public Long getLong(String key) {return this.sp.getLong(key,0);}

    public Float getFloat(String key) {return this.sp.getFloat(key,0);}
    public Boolean getBoolean(String key) {return this.sp.getBoolean(key,false);}
    public Map allSes() {return this.sp.getAll();}
    public void setString(String key, String val) {this.edit.putString(key, val);}
    public void setLong(String key, Long val) {this.edit.putLong(key, val);}

    public void setFloat(String key, Float val) {this.edit.putFloat(key, val);}
    public void setBoolean(String key, Boolean val) {this.edit.putBoolean(key, val);}
    public void commitSess() {this.edit.commit();}
    public void delSess(String key) {this.edit.remove(key);this.edit.commit();}
    public void clearSess() {this.edit.clear();this.edit.commit();}
    public Boolean cek_session() {
        return this.sp.getBoolean("isSignIn",false);
//        return true;
    }
}
