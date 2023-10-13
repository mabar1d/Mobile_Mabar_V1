package com.circle.circle_games.chat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.circle.circle_games.R;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChatMainActivity extends AppCompatActivity {

    private Button add_room;
    private ImageView btnBack;
    private EditText room_name;
    private ListView listView;

    private String name;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList();
    private SessionUser sess;
    GlobalMethod gm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        sess = new SessionUser(this);
        gm = new GlobalMethod();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        add_room = (Button)findViewById(R.id.btnAdd_room);
        btnBack = (ImageView)findViewById(R.id.btn_back);
        room_name = (EditText)findViewById(R.id.etNeme_room);
        listView = (ListView)findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_rooms);
        listView.setAdapter(arrayAdapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String roomName = room_name.getText().toString();
                if (!roomName.isEmpty()) {
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put(roomName,"");
                    root.updateChildren(map);

                    // Mengosongkan EditText setelah mengirim pesan
                    room_name.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(room_name.getWindowToken(), 0);
                }


            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while ( i.hasNext())
                {
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                list_of_rooms.clear();
                list_of_rooms.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent I = new Intent(getApplicationContext(),ChatRoomActivity.class);
                I.putExtra("room_name",((TextView)view).getText().toString());
                I.putExtra("user_name",sess.getString("username"));
                startActivity(I);
            }
        });
    }
}