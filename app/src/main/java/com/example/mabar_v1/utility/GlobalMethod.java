package com.example.mabar_v1.utility;

import android.content.Context;
import android.graphics.Color;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GlobalMethod {

    SessionUser sessionUser;
    SweetAlertDialog pDialog;
    /*public String getUriImage(String path){
        //String url = "https://image.tmdb.org/t/p/w500"+path;
        //return url;
    }*/

    public String getKeyApi(){
        String keyApi = "2ca7c12f40da3aeb0502d71884939cc2";
        return keyApi;
    }

    public void deleteLocalSession(Context context){
        sessionUser = new SessionUser(context);
        sessionUser.clearSess();
    }

    private void dialogLoading(Context context,String title, String isi){
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(title);
        pDialog.setContentText(isi);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void dismissDialogLoading(){
        pDialog.dismissWithAnimation();
    }

}
