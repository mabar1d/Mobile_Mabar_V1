package com.circle.circle_games.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle.circle_games.R;
import com.circle.circle_games.main.GeneralSearchTournamentActivity;
import com.circle.circle_games.retrofit.model.ChatMessageModel;
import com.circle.circle_games.service.FCMMessageSender;
import com.circle.circle_games.utility.SessionUser;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatRoomActivity extends AppCompatActivity {
    private TextView tvTitle;
    /*@BindView(R.id.textView)
    TextView tvChat;*/
    @BindView(R.id.rvChat)
    RecyclerView rvChat;
    @BindView(R.id.messageInput)
    EditText msgInput;
    @BindView(R.id.sendButton)
    Button btnSendMsg;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    private String user_name ,room_name;
    private DatabaseReference root;
    private ChatAdapter chatAdapter;
    private SessionUser sess;
    List<ChatMessageModel> chatMessages = new ArrayList<>(); // Isi ini dengan data dari Firebase
    LinearLayoutManager layoutManager;

    private String temp_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        ButterKnife.bind(this);
        sess = new SessionUser(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        tvTitle = (TextView)findViewById(R.id.tv_title);
        user_name = sess.getString("username");
        room_name = getIntent().getExtras().get("room_name").toString();
        layoutManager = new LinearLayoutManager(this);

        FirebaseApp.initializeApp(this);

        tvTitle.setText(room_name);

        root = FirebaseDatabase.getInstance().getReference().child(room_name);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = msgInput.getText().toString();
                if (!message.isEmpty()) {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM HH:mm");
                    String dateTime = dateFormat.format(calendar.getTime());

                    // Kirim pesan ke obrolan (lakukan sesuai kebutuhan Anda)
                    Map<String,Object> map = new HashMap<String, Object>();
                    temp_key = root.push().getKey();
                    root.updateChildren(map);

                    DatabaseReference message_root = root.child(temp_key);
                    Map<String,Object> map2 = new HashMap<String, Object>();
                    map2.put("name",user_name);
                    map2.put("msg",msgInput.getText().toString());
                    map2.put("dateTime",dateTime);

                    message_root.updateChildren(map2);

                    sendNotif(room_name,user_name,msgInput.getText().toString());

                    // Mengosongkan EditText setelah mengirim pesan
                    msgInput.setText("");
                }

            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                append_chat_conversatin(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_conversatin(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private String chat_msg, chat_user_name,dateTime;
    private Boolean sentByMe = false;

    private void append_chat_conversatin(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext())
        {
            dateTime = (String) ((DataSnapshot)i.next()).getValue();
            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();
            if (chat_user_name.equalsIgnoreCase(sess.getString("username"))){
                sentByMe = true;
            }else {
                sentByMe = false;
            }

            chatMessages.add(new ChatMessageModel(dateTime,chat_msg,chat_user_name,sentByMe));

        }
        chatAdapter = new ChatAdapter(chatMessages,ChatRoomActivity.this, sess.getString("username"));
        rvChat.setAdapter(chatAdapter);
        //layoutManager.setStackFromEnd(true);
        rvChat.setLayoutManager(layoutManager);

        // RecyclerView sudah di posisi bawah, gulirkan ke pesan terbaru
        int newPosition = chatMessages.size() - 1; // Indeks pesan terbaru
        chatAdapter.notifyItemInserted(newPosition); // Perbarui adapter jika diperlukan
        //rvChat.smoothScrollToPosition(newPosition);
        rvChat.scrollToPosition(newPosition);

    }

    private void sendNotif(String roomName,String title, String message){

        String tokenPerangkatTujuan = "chitMVVoRAKIVMR4zolNNH:APA91bFup6cvTwxjlSf6tS5xez2QQJPzAhLesWZjb84BnyR24cnrq5viCFHRzZJfA8n6-HLIE7UKd1cFWi-5bLUmPo_bovIdnZ2RK6BMZLgVKgcZxZ9f5QvoUoTdzUf0JJpABCOrIcDz";

        Map<String, String> messageData = new HashMap<>();
        messageData.put("tipe", "message");
        messageData.put("room_name", roomName);
        messageData.put("name", title);
        messageData.put("msg", message);

        FCMMessageSender.sendMessage(tokenPerangkatTujuan, messageData,ChatRoomActivity.this);
    }

    /*public void sendNotif(String name, String message) {
        try {
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=AAAAhTq3pao:APA91bFlQBDVnB3r9FGGPhsWH3648q1SuBJhspzcz_KSfRTULex35bkT7YbY5eKtKjdKx5rfFCiZVCIPOPdI1y4q7GXOHZNimn-Cw0zo0LZKIw6hLRrC6WwYgrQxsmpgW4ErheKFJlYF"); // Gantilah dengan kunci server FCM Anda

            JSONObject data = new JSONObject();
            data.put("name", name);
            data.put("msg", message);

            OutputStream os = conn.getOutputStream();
            os.write(data.toString().getBytes("UTF-8"));
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                Toast.makeText(ChatRoomActivity.this, "Berhasil send", Toast.LENGTH_SHORT).show();
                // Pesan FCM berhasil dikirim
                // Proses tanggapan jika diperlukan
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
