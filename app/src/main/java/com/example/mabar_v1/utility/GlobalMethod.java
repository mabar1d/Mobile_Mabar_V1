package com.example.mabar_v1.utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.mabar_v1.R;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class GlobalMethod {
    SessionUser sessionUser;
    SweetAlertDialog pDialog;
    Dialog confirmationDialog;
    NumberFormat currencyFormatter;
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

    public String getRoleById(Integer role){
        String textRole = "";
        if (role == 1){
            textRole = "Member";
        }else if (role == 2){
            textRole = "Team Leader";
        }else if (role == 3){
            textRole = "Host";
        }else {
            textRole = "--";
        }
        return textRole;
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

    public static MultipartBody.Part prepareFilePart(
            String fileName,
            File file
    ){
        String filePath = file.getAbsolutePath();
        RequestBody requestFile = (GlobalMethod.isImageFile(filePath))?RequestBody.create(file, MediaType.parse("image/jpg")):
                GlobalMethod.isPdfFile(filePath)?RequestBody.create(file,MediaType.parse("application/pdf")):
                        RequestBody.create(file,MediaType.parse("multipart/form-data"));

        return MultipartBody.Part.createFormData("file", fileName, requestFile);
    }

    public static Boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public static Boolean isPdfFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("application");
    }

    public void showDialogConfirmation(
            Context context,
            String title,
            String subTitle,
            String valueBtnYes,
            String valueBtnNo,
            View.OnClickListener onClickListenerYes,
            View.OnClickListener onClickListenerNo) {

        confirmationDialog = new Dialog(context);
        View dv  = View.inflate(context,R.layout.dialog_confirmation,null);
        confirmationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirmationDialog.setContentView(dv);

        TextView tvTitle = (TextView) confirmationDialog.findViewById(R.id.tv_title);
        TextView tvsubTitle = (TextView) confirmationDialog.findViewById(R.id.tv_subtitle);
        Button btnYes = (Button) confirmationDialog.findViewById(R.id.btn_yes);
        Button btnNo = (Button) confirmationDialog.findViewById(R.id.btn_no);

        tvTitle.setText(title);
        tvsubTitle.setText(subTitle);
        btnYes.setText(valueBtnYes);
        btnNo.setText(valueBtnNo);
        btnYes.setOnClickListener(onClickListenerYes);
        btnNo.setOnClickListener(onClickListenerNo);

        confirmationDialog.show();

    }
    public void dismissDialogConfirmation() {
        confirmationDialog.dismiss();

    }

    public String formattedRupiah(String value){
        currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        currencyFormatter.setMaximumFractionDigits(0);
        currencyFormatter.setRoundingMode(RoundingMode.FLOOR);

        BigDecimal bigValue = new BigDecimal(value);
        String formattedValue = currencyFormatter.format(bigValue);
        return formattedValue;
    }

}
