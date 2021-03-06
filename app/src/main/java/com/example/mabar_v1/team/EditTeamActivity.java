package com.example.mabar_v1.team;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mabar_v1.R;
import com.example.mabar_v1.login.LoginActivity;
import com.example.mabar_v1.main.adapter.ListPersonAdapter;
import com.example.mabar_v1.main.adapter.ListPersonAddedAdapter;
import com.example.mabar_v1.retrofit.ApiService;
import com.example.mabar_v1.retrofit.RetrofitConfig;
import com.example.mabar_v1.retrofit.model.CreateTeamResponseModel;
import com.example.mabar_v1.retrofit.model.GetTeamInfoResponseModel;
import com.example.mabar_v1.retrofit.model.ListPersonnelResponseModel;
import com.example.mabar_v1.retrofit.model.SuccessResponseDefaultModel;
import com.example.mabar_v1.utility.GlobalMethod;
import com.example.mabar_v1.utility.SessionUser;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTeamActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;

    private String valueMember = "";

    @BindView(R.id.civ_team)
    CircularImageView civTeam;
    @BindView(R.id.team_name)
    EditText etTeamName;
    @BindView(R.id.team_info)
    EditText etTeamInfo;
    @BindView(R.id.sp_game)
    Spinner spGame;
    @BindView(R.id.et_personnel)
    TextView etPersonnel;
    @BindView(R.id.btn_update_team)
    Button btnUpdateTeam;
    @BindView(R.id.btn_edit_image)
    Button btnEditImage;

    BottomSheetDialog bsDialog;
    View bottomSheet;

    private EditText sbPerson;
    private TextView tvAddedPerson;
    private ImageView btnSearch;
    RecyclerView rvPersonell;
    RecyclerView rvPersonellAdded;
    Button btnAddPerson;


    private String idTeam = "";
    private boolean flagSendData = false;

    private String picturePath = "";

    private String teamName = "";
    private String teamInfo = "";
    private String tourDescription = "";
    private Integer game;
    private JSONArray personnel = new JSONArray();
    private JSONArray personnelId = new JSONArray();
    private boolean memberEdited = false;

    private SessionUser sess;
    private GlobalMethod gm;
    private ListPersonAdapter listPersonAdapter;
    private ListPersonAddedAdapter listPersonAddedAdapter;
    List<ListPersonnelResponseModel.Data> listPerson = new ArrayList<>();
    List<ListPersonnelResponseModel.Data> listPersonAdded = new ArrayList<>();
    List<GetTeamInfoResponseModel.Data.Personnel> listPersonnelExisting = new ArrayList<>();
    List<String> listPersonnelText = new ArrayList<>();
    ArrayList<String> listSpinnerGame = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_team);

        ButterKnife.bind(this);
        sess = new SessionUser(this);
        gm = new GlobalMethod();

        Bundle b = getIntent().getExtras();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if(b != null) {
            idTeam = b.getString("id_team");
        }

        listSpinnerGame.add("Mobile Legend");
        listSpinnerGame.add("Free Fire");
        listSpinnerGame.add("PUBG");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,listSpinnerGame);
        spGame.setAdapter(adapter);

        btnEditImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(EditTeamActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(EditTeamActivity.this, "Please Check Application Permissions", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }

            }
        });

        //BOttomsheet
        bsDialog = new BottomSheetDialog(EditTeamActivity.this);
        bottomSheet = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.dialog_search_person,
                        (LinearLayout)findViewById(R.id.cs_dialog)
                );

        sbPerson = bottomSheet.findViewById(R.id.search_bar_person);
        rvPersonell = bottomSheet.findViewById(R.id.recycler_personell);
        rvPersonellAdded = bottomSheet.findViewById(R.id.recycler_personell_added);
        btnAddPerson = bottomSheet.findViewById(R.id.btn_add_person);
        btnSearch = bottomSheet.findViewById(R.id.btn_search);
        //tvAddedPerson = bottomSheet.findViewById(R.id.added_person);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListPerson(sbPerson.getText().toString(),"0");
            }
        });
        sbPerson.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    getListPerson(sbPerson.getText().toString(),"0");
                    return true;
                }
                return false;
            }
        });

        etPersonnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListPerson("","0");

            }
        });

        btnAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bsDialog.dismiss();
            }
        });

        btnUpdateTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gm.showDialogConfirmation(EditTeamActivity.this, "Update Team?", "Are you sure?", "Update", "Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        teamName = etTeamName.getText().toString();
                        teamInfo = etTeamInfo.getText().toString();

                        //Mapping Game
                        String getGame = spGame.getSelectedItem().toString();
                        if (getGame.equalsIgnoreCase("Mobile Legends")){
                            game = 6;
                        }else if (getGame.equalsIgnoreCase("Free Fire")){
                            game = 7;
                        }else if (getGame.equalsIgnoreCase("PUBG")){
                            game = 8;
                        }else {
                            game = 0;
                        }
                        //game = Integer.parseInt(etGame.getText().toString());
                        if (picturePath.equals("")){
                            updateTeam(idTeam,teamName,teamInfo,game.toString(),personnelId);
                        }else {
                            if (flagSendData){
                                uploadImage(idTeam);
                            }else {
                                updateTeam(idTeam,teamName,teamInfo,game.toString(),personnelId);
                            }
                        }
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gm.dismissDialogConfirmation();
                    }
                });


            }
        });

        getDataTeam(idTeam);

    }



    private void updateTeam(String teamId,String teamName,String teamInfo,String game,JSONArray personnels){
        ProgressDialog progress = new ProgressDialog(EditTeamActivity.this);
        progress.setMessage("Update Team");
        progress.show();
        try {
            Call<SuccessResponseDefaultModel> req = RetrofitConfig.getApiServices(sess.getString("token")).updateTeam(sess.getString("id_user"),
                    teamId,teamName,teamInfo,game,personnels);
            req.enqueue(new Callback<SuccessResponseDefaultModel>() {
                @Override
                public void onResponse(Call<SuccessResponseDefaultModel> call, Response<SuccessResponseDefaultModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            String desc = response.body().getDesc();
                            Toast.makeText(EditTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                            flagSendData = true;
                            if (!picturePath.equals("")) {
                                uploadImage(idTeam);
                            }else {
                                gm.dismissDialogConfirmation();
                                finish();
                            }
                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(EditTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(EditTeamActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(EditTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(EditTeamActivity.this, "Failed Update Team", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(EditTeamActivity.this, msg, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void uploadImage(String idTeam) {
        ProgressDialog progress = new ProgressDialog(EditTeamActivity.this);
        progress.setMessage("Uploading Image");
        progress.show();

        File file = new File(picturePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part parts = MultipartBody.Part.createFormData("image_file", file.getName(), requestBody);

        RequestBody idUser = RequestBody.create(MediaType.parse("text/plain"), sess.getString("id_user"));
        RequestBody idTeam1 = RequestBody.create(MediaType.parse("text/plain"), idTeam);

        try {
            Call<SuccessResponseDefaultModel> req = RetrofitConfig.getApiUpload(sess.getString("token")).create(ApiService.class).uploadImageTeam(idUser,idTeam1,parts);
            req.enqueue(new Callback<SuccessResponseDefaultModel>() {
                @Override
                public void onResponse(Call<SuccessResponseDefaultModel> call, Response<SuccessResponseDefaultModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            String notif = response.body().getDesc();
                            Toast.makeText(EditTeamActivity.this, notif, Toast.LENGTH_SHORT).show();
                            gm.dismissDialogConfirmation();
                            finish();

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(EditTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(EditTeamActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(EditTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(EditTeamActivity.this, "Failed Upload Image", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(EditTeamActivity.this, msg, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getListPerson(String search,String page){

        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.show();
        try {
            Call<ListPersonnelResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getListPersonnel(search, page);
            req.enqueue(new Callback<ListPersonnelResponseModel>() {
                @Override
                public void onResponse(Call<ListPersonnelResponseModel> call, Response<ListPersonnelResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
/*
                bottomSheet.buttonSubmit.setOnClickListener { dialog.dismiss() }
*/

                            listPerson.clear();
                            listPerson = response.body().getData();
                            if (memberEdited == false){
                                listPersonAdded.clear();
                                personnel = new JSONArray();
                                personnelId = new JSONArray();
                                for (int i = 0; i < listPersonnelExisting.size(); i++) {
                                    String idPersonEx = "";
                                    idPersonEx = listPersonnelExisting.get(i).getUserId();
                                    for (int j = 0; j < listPerson.size(); j++) {
                                        Integer idPerson;
                                        idPerson = listPerson.get(j).getUserId();
                                        if (idPersonEx.equals(idPerson.toString())){

                                            listPersonAdded.add(listPerson.get(j));
                                            personnel.put(listPerson.get(j).getUsername());
                                            personnelId.put(idPersonEx);
                                            valueMember = personnel.toString();

                                            if (valueMember.contains("[")) {
                                                valueMember=valueMember.replace("[", "");
                                            }
                                            if (valueMember.contains("]")){
                                                valueMember=valueMember.replace("]", "");
                                            }

                                            etPersonnel.setText(valueMember);
                                        }

                                    }

                                }
                            }
                            memberEdited = true;


                            listPersonAdapter = new ListPersonAdapter(EditTeamActivity.this, listPerson, new ListPersonAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(ListPersonnelResponseModel.Data item, int position) {
                                    //item.setOnClick(true);

                                    listPerson.set(position, item);
                                    for (int i = 0; i < listPerson.size(); i++) {
                                        ListPersonnelResponseModel.Data itemLain = listPerson.get(i);
                                        itemLain.setOnClick(false);
                                        listPerson.set(i, itemLain);
                                    }


                                    //tvAddedPerson.setText(item.getFirstname());
                                    listPerson.set(position, item);

                                    if (!listPersonAdded.contains(item)){

                                        listPersonAdded.add(item);
                                        personnel.put(item.getUsername());
                                        personnelId.put(item.getUserId());
                                        //tvAddedPerson.setText(personnel.toString());

                                        valueMember = personnel.toString();
                                        if (valueMember.contains("[")) {
                                            valueMember=valueMember.replace("[", "");
                                        }
                                        if (valueMember.contains("]")){
                                            valueMember=valueMember.replace("]", "");
                                        }
                                    /*if (valueMember.contains("")){
                                        valueMember=valueMember.replace("", "");
                                    }*/

                                        etPersonnel.setText(valueMember);

                                        item.setOnClick(true);
                                        listPersonAddedAdapter.notifyDataSetChanged();

                                    }

                                    //listPerson.remove(position);
                                    listPersonAdapter.notifyDataSetChanged();
                                    //notifyAll();
                                }
                            });

                            rvPersonell.setLayoutManager(new GridLayoutManager(EditTeamActivity.this,2));
                            rvPersonell.setAdapter(listPersonAdapter);
                            listPersonAdapter.notifyDataSetChanged();

                            //Setting Adapter Added Person
                            listPersonAddedAdapter = new ListPersonAddedAdapter(EditTeamActivity.this, listPersonAdded, new ListPersonAddedAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(ListPersonnelResponseModel.Data item, int position) {
                                    listPersonAdded.remove(position);
                                    personnel.remove(position);
                                    personnelId.remove(position);

                                    valueMember = personnel.toString();
                                    if (valueMember.contains("[")) {
                                        valueMember=valueMember.replace("[", "");
                                    }
                                    if (valueMember.contains("]")){
                                        valueMember=valueMember.replace("]", "");
                                    }
                                    /*if (valueMember.contains("")){
                                        valueMember=valueMember.replace("", "");
                                    }*/

                                    etPersonnel.setText(valueMember);
                                    listPersonAddedAdapter.notifyDataSetChanged();
                                }
                            });

                            rvPersonellAdded.setLayoutManager(new GridLayoutManager(EditTeamActivity.this,2,GridLayoutManager.HORIZONTAL,false));
                            rvPersonellAdded.setAdapter(listPersonAddedAdapter);
                            listPersonAddedAdapter.notifyDataSetChanged();

                            bsDialog.setContentView(bottomSheet);
                            bsDialog.show();

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(EditTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(EditTeamActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(EditTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(EditTeamActivity.this, "Failed Request List Teams", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<ListPersonnelResponseModel> call, Throwable t) {
                    Toast.makeText(EditTeamActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    progress.dismiss();

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void getDataTeam(String teamId){
        ProgressDialog progress = new ProgressDialog(EditTeamActivity.this);
        progress.setMessage("Getting Info...");
        progress.show();
        try {
            Call<GetTeamInfoResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getInfoTeam(sess.getString("id_user"),teamId);
            req.enqueue(new Callback<GetTeamInfoResponseModel>() {
                @Override
                public void onResponse(Call<GetTeamInfoResponseModel> call, Response<GetTeamInfoResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            Glide.with(EditTeamActivity.this)
                                    .load(response.body().getData().getImage())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .into(civTeam);

                            etTeamName.setText(response.body().getData().getName());
                            etTeamInfo.setText(response.body().getData().getInfo());

                            if (response.body().getData().getGame_id().equals("6")){
                                spGame.setSelection(0);
                            }else if (response.body().getData().getGame_id().equals("7")){
                                spGame.setSelection(1);
                            }else if (response.body().getData().getGame_id().equals("8")){
                                spGame.setSelection(2);
                            }else {
                                spGame.setSelection(0);
                            }

                            listPersonnelExisting = response.body().getData().getPersonnel();
                            for (int i = 0; i < listPersonnelExisting.size(); i++) {
                                personnel.put(listPersonnelExisting.get(i).getUsername());
                                personnelId.put(listPersonnelExisting.get(i).getUserId());
                            }
                            valueMember = personnel.toString();
                            if (valueMember.contains("[")) {
                                valueMember=valueMember.replace("[", "");
                            }
                            if (valueMember.contains("]")){
                                valueMember=valueMember.replace("]", "");
                            }
                            etPersonnel.setText(valueMember);


                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(EditTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(EditTeamActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(EditTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(EditTeamActivity.this, "Failed Request Team Info", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<GetTeamInfoResponseModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(EditTeamActivity.this, msg, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            civTeam.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


    }

}