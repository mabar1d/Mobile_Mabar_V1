package com.example.mabar_v1.host;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mabar_v1.R;
import com.example.mabar_v1.login.LoginActivity;
import com.example.mabar_v1.main.DetailTournamentActivity;
import com.example.mabar_v1.profile.CreateTournamentActivity;
import com.example.mabar_v1.retrofit.ApiService;
import com.example.mabar_v1.retrofit.RetrofitConfig;
import com.example.mabar_v1.retrofit.model.ResponseCreateTournamentResponseModel;
import com.example.mabar_v1.retrofit.model.ResponseGetInfoTournamentModel;
import com.example.mabar_v1.retrofit.model.SuccessResponseDefaultModel;
import com.example.mabar_v1.utility.CurrencyEditTextWatcher;
import com.example.mabar_v1.utility.GlobalMethod;
import com.example.mabar_v1.utility.SessionUser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostManageTournamentActivity extends AppCompatActivity {

    @BindView(R.id.image_tournament)
    ImageView ivTournament;
    @BindView(R.id.btn_edit_image)
    Button btnEditImage;
    @BindView(R.id.et_tour_name)
    EditText etTourName;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.radio_group_participants)
    RadioGroup rgParticipants;
    @BindView(R.id.radio_eight)
    RadioButton rbEight;
    @BindView(R.id.radio_sixteen)
    RadioButton rbSixteen;
    @BindView(R.id.et_register_date_start)
    EditText etRegisterDateStart;
    @BindView(R.id.et_register_date_end)
    EditText etRegisterDateEnd;
    @BindView(R.id.et_start_date)
    EditText etStartDate;
    @BindView(R.id.et_end_date)
    EditText etEndDate;
    @BindView(R.id.et_prize)
    EditText etPrize;
    @BindView(R.id.et_reg_fee)
    EditText etRegFee;
    @BindView(R.id.sp_game)
    Spinner spGame;
    @BindView(R.id.radio_group_type)
    RadioGroup rgType;
    @BindView(R.id.radio_tree)
    RadioButton rbTree;
    @BindView(R.id.radio_stage)
    RadioButton rbGroupStage;
    TextView etType;
    @BindView(R.id.btn_update_tournament)
    Button btnUpdateTournament;

    private String tourName = "";
    private String tourDescription = "";
    private Integer numberParticipants;

    private String prize = "";
    private String regFee = "";
    private Integer game;
    private Integer type;
    private boolean flagSendData = false;
    private String idTour = "";

    private String regDateStartNonFormat = "";
    private String regDateEndNonFormat = "";
    private String dateStartNonFormat = "";
    private String dateEndNonFormat = "";

    Calendar myCalendar = Calendar.getInstance();
    private SimpleDateFormat sdf;
    private GlobalMethod gm;
    private SessionUser sess;
    private static int RESULT_LOAD_IMAGE = 1;
    private String picturePath = "";
    private String idTournament = "";
    private RadioButton radioButton;
    ArrayList<String> listSpinnerGame = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_manage_tournament);
        ButterKnife.bind(this);

        Bundle b = getIntent().getExtras();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if(b != null) {
            idTournament = b.getString("id_tournament");
//            judulGame = b.getString("judul_game");
//            usage = b.getString("usage");
        }

        sess = new SessionUser(this);
        gm = new GlobalMethod();
        sdf = new SimpleDateFormat("dd-MM-yyyy");

        etPrize.addTextChangedListener(new CurrencyEditTextWatcher(etPrize));
        etRegFee.addTextChangedListener(new CurrencyEditTextWatcher(etRegFee));

        listSpinnerGame.add("Mobile Legend");
        listSpinnerGame.add("Free Fire");
        listSpinnerGame.add("PUBG");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item,listSpinnerGame);
        spGame.setAdapter(adapter);

        btnUpdateTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gm.showDialogConfirmation(HostManageTournamentActivity.this, "Update Tournament?", "Are you sure?", "Update", "Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int i = rgParticipants.getCheckedRadioButtonId();
                        radioButton = (RadioButton) findViewById(i);
                        numberParticipants = Integer.parseInt(radioButton.getText().toString());

                        String typeId ;
                        int j = rgType.getCheckedRadioButtonId();
                        radioButton = (RadioButton) findViewById(j);

                        typeId = radioButton.getText().toString();
                        if (typeId.equalsIgnoreCase("Tree")){
                            type = 1;
                        }else {
                            type = 2;
                        }

                        tourName = etTourName.getText().toString();
                        tourDescription = etDescription.getText().toString();
                        prize = String.valueOf(CurrencyEditTextWatcher.parseCurrencyValue(etPrize.getText().toString()));
                        regFee = String.valueOf(CurrencyEditTextWatcher.parseCurrencyValue(etRegFee.getText().toString()));

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
                            Toast.makeText(HostManageTournamentActivity.this, "Please select a picture before create Tournament", Toast.LENGTH_SHORT).show();
                        }else {
                            if (flagSendData == true){
                                uploadImage(idTour);
                            }else {
                                updateTournament(idTour,tourName,tourDescription,numberParticipants,regDateStartNonFormat,regDateEndNonFormat,
                                        dateStartNonFormat,dateEndNonFormat,regFee,prize,game,type);
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

        btnEditImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(HostManageTournamentActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(HostManageTournamentActivity.this, "Please Check Application Permissions", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            }
        });

        //Open DatePicker
        setDateOpenRegStart();
        setDateOpenRegEnd();
        setDateOpenStart();
        setDateOpenEnd();

        getInfoTournament(sess.getString("id_user"),idTournament);


    }

    private void setDateOpenRegStart(){
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                etRegisterDateStart.setText(gm.setDateIndonesia(2,sdf.format(myCalendar.getTime())));
                regDateStartNonFormat = sdf.format(myCalendar.getTime());
            }
        };
        etRegisterDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(HostManageTournamentActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setDateOpenRegEnd(){
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                etRegisterDateEnd.setText(gm.setDateIndonesia(2,sdf.format(myCalendar.getTime())));
                regDateEndNonFormat = sdf.format(myCalendar.getTime());
            }
        };

        etRegisterDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(HostManageTournamentActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setDateOpenStart(){
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                etStartDate.setText(gm.setDateIndonesia(2,sdf.format(myCalendar.getTime())));
                dateStartNonFormat = sdf.format(myCalendar.getTime());
            }
        };

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(HostManageTournamentActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setDateOpenEnd(){
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                etEndDate.setText(gm.setDateIndonesia(2,sdf.format(myCalendar.getTime())));
                dateEndNonFormat = sdf.format(myCalendar.getTime());
            }
        };

        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(HostManageTournamentActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateTournament(String tourId,String tourName,String description,Integer numParticipants,
                                  String regStartDate,String regEndDate,String startDate,String endDate,String regFee,
                                  String prize,Integer game,Integer type){
        ProgressDialog progress = new ProgressDialog(HostManageTournamentActivity.this);
        progress.setMessage("Update Tournament");
        progress.show();
        try {
            Call<SuccessResponseDefaultModel> req = RetrofitConfig.getApiServices(sess.getString("token")).updateTournament(sess.getString("id_user"),tourId,
                    tourName,description,numParticipants,regStartDate,regEndDate,startDate,endDate,regFee,prize,game,type);
            req.enqueue(new Callback<SuccessResponseDefaultModel>() {
                @Override
                public void onResponse(Call<SuccessResponseDefaultModel> call, Response<SuccessResponseDefaultModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            flagSendData = true;
                            String desc = response.body().getDesc();
                            Toast.makeText(HostManageTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                            uploadImage(idTour);

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(HostManageTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(HostManageTournamentActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(HostManageTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(HostManageTournamentActivity.this, "Failed Update Tournament", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(HostManageTournamentActivity.this, msg, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void uploadImage(String idTournament) {
        ProgressDialog progress = new ProgressDialog(HostManageTournamentActivity.this);
        progress.setMessage("Uploading Image");
        progress.show();

        File file = new File(picturePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part parts = MultipartBody.Part.createFormData("image_file", file.getName(), requestBody);

        RequestBody idUser = RequestBody.create(MediaType.parse("text/plain"), sess.getString("id_user"));
        RequestBody idTour = RequestBody.create(MediaType.parse("text/plain"), idTournament);

        try {
            Call<SuccessResponseDefaultModel> req = RetrofitConfig.getApiUpload(sess.getString("token")).create(ApiService.class).uploadImageTournament(idUser,idTour,parts);
            req.enqueue(new Callback<SuccessResponseDefaultModel>() {
                @Override
                public void onResponse(Call<SuccessResponseDefaultModel> call, Response<SuccessResponseDefaultModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            String notif = response.body().getDesc();
                            Toast.makeText(HostManageTournamentActivity.this, notif, Toast.LENGTH_SHORT).show();
                            finish();

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(HostManageTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(HostManageTournamentActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(HostManageTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(HostManageTournamentActivity.this, "Failed Upload Image", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(HostManageTournamentActivity.this, msg, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getInfoTournament(String userId,String idTournament){
        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.show();
        try {
            Call<ResponseGetInfoTournamentModel> req = RetrofitConfig.getApiServices("").getInfoTournament(userId, idTournament);
            req.enqueue(new Callback<ResponseGetInfoTournamentModel>() {
                @Override
                public void onResponse(Call<ResponseGetInfoTournamentModel> call, Response<ResponseGetInfoTournamentModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            Glide.with(HostManageTournamentActivity.this)
                                    .load(response.body().getData().getImage())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    //.skipMemoryCache(true)
                                    .into(ivTournament);
                            etTourName.setText(response.body().getData().getName());

                            etPrize.setText("Rp. "+response.body().getData().getPrize());
                            etDescription.setText(response.body().getData().getDetail());
                            etRegisterDateStart.setText(response.body().getData().getRegisterDateStart());
                            etRegisterDateEnd.setText(response.body().getData().getRegisterDateEnd());
                            etStartDate.setText(response.body().getData().getStartDate());
                            etEndDate.setText(response.body().getData().getEndDate());
                            etRegFee.setText(response.body().getData().getRegister_fee());
                            //e.setText(response.body().getData().getDetail());
                            //type belom
                            String fee = response.body().getData().getRegister_fee();

                            if (fee.equalsIgnoreCase("0")){
                                fee = "FREE";
                            }else {
                                fee = "Rp. "+fee;
                            }
                           // btnRegister.setText("Register "+ "("+fee+")");


                        }else {
                            String notif = response.body().getDesc();
                            Toast.makeText(HostManageTournamentActivity.this, notif, Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.body().getCode().equals("05")){
                        String desc = response.body().getDesc();
                        Toast.makeText(HostManageTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                        sess.clearSess();
                        Intent i = new Intent(HostManageTournamentActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }  else {
                        String desc = response.body().getDesc();
                        Toast.makeText(HostManageTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseGetInfoTournamentModel> call, Throwable t) {
                    Toast.makeText(HostManageTournamentActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
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

            ivTournament.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


    }

}