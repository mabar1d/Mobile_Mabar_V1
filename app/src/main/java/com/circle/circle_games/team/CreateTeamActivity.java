package com.circle.circle_games.team;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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

import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.main.adapter.ListPersonAdapter;
import com.circle.circle_games.main.adapter.ListPersonAddedAdapter;
import com.circle.circle_games.profile.DetailProfileAccountActivity;
import com.circle.circle_games.retrofit.ApiService;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.CreateTeamResponseModel;
import com.circle.circle_games.retrofit.model.DataItem;
import com.circle.circle_games.retrofit.model.ListPersonnelNotMemberResponseModel;
import com.circle.circle_games.retrofit.model.ListPersonnelNotMemberResponseModel;
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
import com.circle.circle_games.retrofit.model.CreateTeamResponseModel;
import com.circle.circle_games.retrofit.model.ListPersonnelNotMemberResponseModel;
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

public class CreateTeamActivity extends AppCompatActivity {

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
    @BindView(R.id.recycler_personell_added_clone)
    RecyclerView rvAddedPersonellClone;
    @BindView(R.id.btn_add_personell)
    ImageView btnAddPersonell;
    @BindView(R.id.btn_crate_team)
    Button btnCreateTeam;
    @BindView(R.id.btn_add_image)
    Button btnAddImage;
    @BindView(R.id.btn_back)
    ImageView btnBack;

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
    private Integer game;
    private Integer idUser;
    private JSONArray personnel = new JSONArray();
    private JSONArray personnelId = new JSONArray();

    private String[] person1;

    private SessionUser sess;
    private GlobalMethod gm;
    private ListPersonAdapter listPersonAdapter;
    private ListPersonAddedAdapter listPersonAddedAdapter;
    List<ListPersonnelNotMemberResponseModel.Data> listPerson = new ArrayList<>();
    List<ListPersonnelNotMemberResponseModel.Data> listPersonAdded = new ArrayList<>();
    ArrayList<String> listSpinnerGame = new ArrayList<>();
    private MasterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        ButterKnife.bind(this);
        sess = new SessionUser(this);
        gm = new GlobalMethod();
        viewModel = ViewModelProviders.of(this).get(MasterViewModel.class);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        idUser = Integer.parseInt(sess.getString("id_user"));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAddImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }else {
                    if (ContextCompat.checkSelfPermission(CreateTeamActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(CreateTeamActivity.this, "Please Check Application Permissions", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                    }

                }

            }
        });

        getAllGameTitle();

        //BOttomsheet
        bsDialog = new BottomSheetDialog(CreateTeamActivity.this);
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

        btnAddPersonell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListPerson("","0");
            }
        });

        btnAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bsDialog.dismiss();
            }
        });

        btnCreateTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gm.showDialogConfirmation(CreateTeamActivity.this, "Create Team?", "Are you sure?", "Create", "Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        teamName = etTeamName.getText().toString();
                        teamInfo = etTeamInfo.getText().toString();
                        //Mapping Game
                        String getGame = spGame.getSelectedItem().toString();
                        game = getGameId(getGame);

                        if (picturePath.equals("")){
                            Toast.makeText(CreateTeamActivity.this, "Please select a picture before create Team", Toast.LENGTH_SHORT).show();
                        }else {
                            if (flagSendData){
                                uploadImage(idTeam);
                            }else {
                                createTeam(teamName,teamInfo,game.toString(),personnelId);
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

    }



    private void createTeam(String teamName,String teamInfo,String game,JSONArray personnels){
        gm.showLoadingDialog(CreateTeamActivity.this);
        try {
            Call<CreateTeamResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).createTeam(sess.getString("id_user"),
                    teamName,teamInfo,game,personnels);
            req.enqueue(new Callback<CreateTeamResponseModel>() {
                @Override
                public void onResponse(Call<CreateTeamResponseModel> call, Response<CreateTeamResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            String desc = response.body().getDesc();
                            Toast.makeText(CreateTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                            idTeam = String.valueOf(response.body().getData().getTeamId());
                            flagSendData = true;
                            uploadImage(idTeam);

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(CreateTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                            gm.dismissLoadingDialog();
                            sess.clearSess();
                            Intent i = new Intent(CreateTeamActivity.this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(CreateTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(CreateTeamActivity.this, "Failed Create Team", Toast.LENGTH_SHORT).show();
                        gm.dismissLoadingDialog();
                    }
                    gm.dismissLoadingDialog();
                }

                @Override
                public void onFailure(Call<CreateTeamResponseModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(CreateTeamActivity.this, msg, Toast.LENGTH_SHORT).show();
                    gm.dismissLoadingDialog();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void uploadImage(String idTeam) {
        gm.showLoadingDialog(CreateTeamActivity.this);

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
                            Toast.makeText(CreateTeamActivity.this, notif, Toast.LENGTH_SHORT).show();
                            finish();

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(CreateTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                            gm.dismissLoadingDialog();
                            sess.clearSess();
                            Intent i = new Intent(CreateTeamActivity.this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(CreateTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(CreateTeamActivity.this, "Failed Upload Image", Toast.LENGTH_SHORT).show();
                        gm.dismissLoadingDialog();
                    }
                    gm.dismissLoadingDialog();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(CreateTeamActivity.this, msg, Toast.LENGTH_SHORT).show();
                    gm.dismissLoadingDialog();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getListPerson(String search,String page){

        gm.showLoadingDialog(CreateTeamActivity.this);
        try {
            Call<ListPersonnelNotMemberResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getListPersonnelNotMember(sess.getString("id_user"), search, page);
            req.enqueue(new Callback<ListPersonnelNotMemberResponseModel>() {
                @Override
                public void onResponse(Call<ListPersonnelNotMemberResponseModel> call, Response<ListPersonnelNotMemberResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            listPerson.clear();
                            listPerson = response.body().getData();

                            listPersonAdapter = new ListPersonAdapter(CreateTeamActivity.this, listPerson, new ListPersonAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(ListPersonnelNotMemberResponseModel.Data item, int position) {
                                    //item.setOnClick(true);

                                    listPerson.set(position, item);
                                    for (int i = 0; i < listPerson.size(); i++) {
                                        ListPersonnelNotMemberResponseModel.Data itemLain = listPerson.get(i);
                                        itemLain.setOnClick(false);
                                        listPerson.set(i, itemLain);
                                    }

                                    listPerson.set(position, item);

                                    if (!listPersonAdded.contains(item)){

                                        listPersonAdded.add(item);
                                        personnel.put(item.getUsername());
                                        personnelId.put(item.getUserId());

                                        valueMember = personnel.toString();
                                        if (valueMember.contains("[")) {
                                            valueMember=valueMember.replace("[", "");
                                        }
                                        if (valueMember.contains("]")){
                                            valueMember=valueMember.replace("]", "");
                                        }

                                        etPersonnel.setText(valueMember);

                                        item.setOnClick(true);
                                        listPersonAddedAdapter.notifyDataSetChanged();

                                    }

                                    listPersonAdapter.notifyDataSetChanged();
                                }
                            });

                            rvPersonell.setLayoutManager(new GridLayoutManager(CreateTeamActivity.this,2));
                            rvPersonell.setAdapter(listPersonAdapter);
                            listPersonAdapter.notifyDataSetChanged();

                            //Setting Adapter Added Person
                            listPersonAddedAdapter = new ListPersonAddedAdapter(CreateTeamActivity.this,idUser, listPersonAdded, new ListPersonAddedAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(ListPersonnelNotMemberResponseModel.Data item, int position) {
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

                            rvPersonellAdded.setLayoutManager(new GridLayoutManager(CreateTeamActivity.this,2,GridLayoutManager.HORIZONTAL,false));
                            rvPersonellAdded.setAdapter(listPersonAddedAdapter);

                            rvAddedPersonellClone.setLayoutManager(new LinearLayoutManager(CreateTeamActivity.this));
                            rvAddedPersonellClone.setAdapter(listPersonAddedAdapter);

                            listPersonAddedAdapter.notifyDataSetChanged();

                            bsDialog.setContentView(bottomSheet);
                            bsDialog.show();

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(CreateTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                            gm.dismissLoadingDialog();
                            sess.clearSess();
                            Intent i = new Intent(CreateTeamActivity.this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(CreateTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(CreateTeamActivity.this, "Failed Request List Teams", Toast.LENGTH_SHORT).show();
                        gm.dismissLoadingDialog();
                    }
                    gm.dismissLoadingDialog();
                }

                @Override
                public void onFailure(Call<ListPersonnelNotMemberResponseModel> call, Throwable t) {
                    Toast.makeText(CreateTeamActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    gm.dismissLoadingDialog();

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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateTeamActivity.this, R.layout.spinner_item,listSpinnerGame);
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