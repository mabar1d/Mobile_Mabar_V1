package com.example.mabar_v1.utility;

import android.content.Context;
import android.graphics.Color;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public void dialogLoading(Context context,String title, String isi){
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(title);
        pDialog.setContentText(isi);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void dismissDialogLoading(){
        pDialog.dismissWithAnimation();
    }

    public String setDateIndonesia(Integer type, String date){
        DateFormat df;
        Date dt;
        Calendar calendar = Calendar.getInstance();
        try{
            if(type == 0) {
                df = new SimpleDateFormat("yyyy-MM-dd");
            }else if(type == 1){
                df = new SimpleDateFormat("ddMMyyyy");
            }else if(type == 2){
                df = new SimpleDateFormat("dd-MM-yyyy");
            }else if(type == 3) {
                df = new SimpleDateFormat("yyyyMMdd");
            }else {
                df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
            dt = df.parse(date);
        }catch (Exception e){
            dt = calendar.getTime();
        }
        int seq = 1;
        String month;
        String result;

        try{
            calendar.setTime(dt);

            seq += calendar.get(Calendar.MONTH);
            if(seq == 1){
                month = "Januari";
            }else if(seq == 2){
                month = "Februari";
            }else if(seq == 3){
                month = "Maret";
            }else if(seq == 4){
                month = "April";
            }else if(seq == 5){
                month = "Mei";
            }else if(seq == 6){
                month = "Juni";
            }else if(seq == 7){
                month = "Juli";
            }else if(seq == 8){
                month = "Agustus";
            }else if(seq == 9){
                month = "September";
            }else if(seq == 10){
                month = "Oktober";
            }else if(seq == 11){
                month = "November";
            }else if(seq == 12){
                month = "Desember";
            }else{
                month = "";
            }

            result = Integer.toString(calendar.get(Calendar.DATE)) + " " + month + " " + Integer.toString(calendar.get(Calendar.YEAR));
        }catch (Exception ex){
            result = "Ada kesalahan input";
        }

        return result;
    }

}
