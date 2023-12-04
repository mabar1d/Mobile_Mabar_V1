package com.circle.circle_games.service;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FCMMessageSender {
    public static void sendMessage(String recipientToken, Map<String, String> data, Context context) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                try {
                    String recipientToken = params[0];
                    URL url = new URL("https://fcm.googleapis.com/fcm/send");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Authorization", "key=AAAAhTq3pao:APA91bFlQBDVnB3r9FGGPhsWH3648q1SuBJhspzcz_KSfRTULex35bkT7YbY5eKtKjdKx5rfFCiZVCIPOPdI1y4q7GXOHZNimn-Cw0zo0LZKIw6hLRrC6WwYgrQxsmpgW4ErheKFJlYF"); // Gantilah dengan kunci server FCM Anda

                    conn.setDoOutput(true);
                    conn.connect();

                    Map<String, Object> message = new HashMap<>();
                    message.put("to", recipientToken);
                    message.put("data", data);

                    OutputStream os = conn.getOutputStream();
                    os.write(new Gson().toJson(message).getBytes("UTF-8"));
                    os.close();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {

                        // Pemberitahuan terkirim dengan sukses
                    } else {
                        // Gagal mengirim pemberitahuan
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(recipientToken);
    }
}

