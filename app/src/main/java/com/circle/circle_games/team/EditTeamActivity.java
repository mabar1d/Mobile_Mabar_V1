package com.circle.circle_games.team;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

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
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.main.adapter.ListGameAdapter;
import com.circle.circle_games.main.adapter.ListPersonAdapter;
import com.circle.circle_games.main.adapter.ListPersonAddedAdapter;
import com.circle.circle_games.retrofit.ApiService;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.DataItem;
import com.circle.circle_games.retrofit.model.GetTeamInfoResponseModel;
import com.circle.circle_games.retrofit.model.ListPersonnelNotMemberResponseModel;
import com.circle.circle_games.retrofit.model.ListPersonnelResponseModel;
import com.circle.circle_games.retrofit.model.SuccessResponseDefaultModel;
import com.circle.circle_games.room.viewmodel.MasterViewModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;
import com.circle.circle_games.R;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.main.adapter.ListPersonAdapter;
import com.circle.circle_games.main.adapter.ListPersonAddedAdapter;
import com.circle.circle_games.retrofit.ApiService;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.GetTeamInfoResponseModel;
import com.circle.circle_games.retrofit.model.ListPersonnelResponseModel;
import com.circle.circle_games.retrofit.model.SuccessResponseDefaultModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;
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
    @BindView(R.id.recycler_personell_added_clone)
    RecyclerView rvAddedPersonellClone;
    @BindView(R.id.btn_add_personell)
    ImageView btnAddPersonell;
    @BindView(R.id.btn_update_team)
    Button btnUpdateTeam;
    @BindView(R.id.btn_edit_image)
    Button btnEditImage;
    @BindView(R.id.btn_back)
    ImageView btnBack;

    BottomSheetDialog bsDialog;
    View bottomSheet;


    private EditText sbPerson;
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
    private Integer idUser;


    private SessionUser sess;
    private GlobalMethod gm;
    private ListPersonAdapter listPersonAdapter;
    private ListPersonAddedAdapter listPersonAddedAdapter;
    List<ListPersonnelNotMemberResponseModel.Data> listPerson = new ArrayList<>();
    List<ListPersonnelNotMemberResponseModel.Data> listPersonAdded = new ArrayList<>();
    List<GetTeamInfoResponseModel.Data.Personnel> listPersonnelExisting = new ArrayList<>();
    ArrayList<String> listSpinnerGame = new ArrayList<>();

    private MasterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_team);

        ButterKnife.bind(this);
        sess = new SessionUser(this);
        gm = new GlobalMethod();
        viewModel = ViewModelProviders.of(this).get(MasterViewModel.class);

        Bundle b = getIntent().getExtras();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if(b != null) {
            idTeam = b.getString("id_team");
        }

        idUser = Integer.parseInt(sess.getString("id_user"));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
                getListNotMemberPerson(sbPerson.getText().toString(),"0");
            }
        });
        sbPerson.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    getListNotMemberPerson(sbPerson.getText().toString(),"0");
                    return true;
                }
                return false;
            }
        });

        getAllGameTitle();

        btnAddPersonell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListNotMemberPerson("","0");
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
                        game = getGameId(getGame);

                        JSONArray personnelId = new JSONArray();

                        for (int i = 0;i<listPersonAdded.size();i++){
                            personnelId.put(listPersonAdded.get(i).getUserId());
                        }

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

        //Setting Adapter Added Person
        listPersonAddedAdapter = new ListPersonAddedAdapter(EditTeamActivity.this,idUser, listPersonAdded, new ListPersonAddedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ListPersonnelNotMemberResponseModel.Data item, int position) {
                if (idUser == item.getUserId()){
                    Toast.makeText(EditTeamActivity.this, "Cannot delete yourself..", Toast.LENGTH_SHORT).show();
                }else {
                    gm.showDialogConfirmation(EditTeamActivity.this, "Remove From Team?", "Remove "+ item.getUsername() + " from your team?", "Yes", "Cancel", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listPersonAdded.remove(position);
                            listPersonAddedAdapter.notifyDataSetChanged();
                            gm.dismissDialogConfirmation();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            gm.dismissDialogConfirmation();
                        }
                    });
                }


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
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
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
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
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

    private void getListNotMemberPerson(String search,String page){

        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.show();
        try {
            Call<ListPersonnelNotMemberResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getListPersonnelNotMember(sess.getString("id_user"), search, page);
            req.enqueue(new Callback<ListPersonnelNotMemberResponseModel>() {
                @Override
                public void onResponse(Call<ListPersonnelNotMemberResponseModel> call, Response<ListPersonnelNotMemberResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            listPerson.clear();
                            listPerson = response.body().getData();


                            listPersonAdapter = new ListPersonAdapter(EditTeamActivity.this, listPerson, new ListPersonAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(ListPersonnelNotMemberResponseModel.Data item, int position) {
                                    //item.setOnClick(true);

                                    listPerson.set(position, item);
                                    for (int i = 0; i < listPerson.size(); i++) {
                                        ListPersonnelNotMemberResponseModel.Data itemLain = listPerson.get(i);
                                        itemLain.setOnClick(false);
                                        listPerson.set(i, itemLain);
                                    }


                                    //tvAddedPerson.setText(item.getFirstname());
                                    listPerson.set(position, item);

                                    if (!listPersonAdded.contains(item)){

                                        listPersonAdded.add(item);
                                        //tvAddedPerson.setText(personnel.toString());

                                        item.setOnClick(true);
                                        listPersonAddedAdapter.notifyDataSetChanged();

                                    }

                                    listPersonAdapter.notifyDataSetChanged();
                                }
                            });

                            rvPersonell.setLayoutManager(new GridLayoutManager(EditTeamActivity.this,2));
                            rvPersonell.setAdapter(listPersonAdapter);
                            listPersonAdapter.notifyDataSetChanged();

                            rvPersonellAdded.setLayoutManager(new GridLayoutManager(EditTeamActivity.this,2,GridLayoutManager.HORIZONTAL,false));
                            rvPersonellAdded.setAdapter(listPersonAddedAdapter);

                            rvAddedPersonellClone.setLayoutManager(new LinearLayoutManager(EditTeamActivity.this));
                            rvAddedPersonellClone.setAdapter(listPersonAddedAdapter);

                            listPersonAddedAdapter.notifyDataSetChanged();

                            bsDialog.setContentView(bottomSheet);
                            bsDialog.show();

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(EditTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(EditTeamActivity.this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
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
                public void onFailure(Call<ListPersonnelNotMemberResponseModel> call, Throwable t) {
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

                            CircularProgressDrawable cp = new CircularProgressDrawable(EditTeamActivity.this);
                            cp.setStrokeWidth(5f);
                            //cp.setBackgroundColor(R.color.material_grey_300);
                            cp.setColorSchemeColors(R.color.primary_color_black, R.color.material_grey_800, R.color.material_grey_900);
                            cp.setCenterRadius(30f);
                            cp.start();

                            if (!response.body().getData().getImage().equals("")){
                                Glide.with(EditTeamActivity.this)
                                        .load(response.body().getData().getImage())
                                        .placeholder(cp)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(civTeam);
                            }


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

                                ListPersonnelNotMemberResponseModel.Data item = new ListPersonnelNotMemberResponseModel.Data(
                                        listPersonnelExisting.get(i).getUsername(),
                                        Integer.parseInt(listPersonnelExisting.get(i).getUserId()),
                                        true
                                );
                                listPersonAdded.add(item);
                                listPersonAddedAdapter.notifyDataSetChanged();

                            }

                            rvAddedPersonellClone.setLayoutManager(new LinearLayoutManager(EditTeamActivity.this));
                            rvAddedPersonellClone.setAdapter(listPersonAddedAdapter);



                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(EditTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(EditTeamActivity.this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
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

    private void getAllGameTitle(){
        viewModel.getAllGame().observe(this, new Observer<List<DataItem>>() {
            @Override
            public void onChanged(List<DataItem> models) {
                for (int i = 0;i<models.size();i++){
                    listSpinnerGame.add(models.get(i).getTitle());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditTeamActivity.this, R.layout.spinner_item,listSpinnerGame);
                spGame.setAdapter(adapter);
            }
        });
    }

    private int getGameId(String title){
        final int[] id = {0};
        viewModel.getAllGame().observe(this, new Observer<List<DataItem>>() {
            @Override
            public void onChanged(List<DataItem> models) {
                for (int i = 0;i<models.size();i++){
                    if (models.get(i).getTitle().equalsIgnoreCase(title)){
                        id[0] = models.get(i).getId();
                    }
                }
            }
        });

        return id[0];
    }

}