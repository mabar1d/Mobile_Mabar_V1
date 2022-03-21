package com.example.mabar_v1.profile;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mabar_v1.R;
import com.example.mabar_v1.login.LoginActivity;
import com.example.mabar_v1.retrofit.ApiService;
import com.example.mabar_v1.retrofit.RetrofitConfig;
import com.example.mabar_v1.retrofit.model.SuccessResponseDefaultModel;
import com.example.mabar_v1.utility.GlobalMethod;
import com.example.mabar_v1.utility.SessionUser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostSettingNewActivity extends AppCompatActivity {

    @BindView(R.id.image_tournament)
    ImageView ivTournament;
    @BindView(R.id.btn_add_image)
    Button btnAddImage;
    @BindView(R.id.et_tour_name)
    TextView etTourName;
    @BindView(R.id.et_description)
    TextView etDescription;
    @BindView(R.id.radio_group_participants)
    RadioGroup rgParticipants;
    @BindView(R.id.radio_eight)
    RadioButton rbEight;
    @BindView(R.id.radio_sixteen)
    RadioButton rbSixteen;
    @BindView(R.id.et_register_date_start)
    TextView etRegisterDateStart;
    @BindView(R.id.et_register_date_end)
    TextView etRegisterDateEnd;
    @BindView(R.id.et_start_date)
    TextView etStartDate;
    @BindView(R.id.et_end_date)
    TextView etEndDate;
    @BindView(R.id.et_prize)
    TextView etPrize;
    @BindView(R.id.et_game)
    TextView etGame;
    @BindView(R.id.radio_group_type)
    RadioGroup rgType;
    @BindView(R.id.radio_tree)
    RadioButton rbTree;
    @BindView(R.id.radio_stage)
    RadioButton rbGroupStage;
    TextView etType;
    @BindView(R.id.btn_create_tournament)
    Button btnCreateTournament;

    private String tourName = "";
    private String tourDescription = "";
    private Integer numberParticipants;
    private String regDateStart = "";
    private String regDateEnd = "";
    private String startDate = "";
    private String endDate = "";
    private String prize = "";
    private Integer game;
    private Integer type;

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
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_setting);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        sess = new SessionUser(this);
        gm = new GlobalMethod();
        sdf = new SimpleDateFormat("dd-MM-yyyy");

        btnCreateTournament.setOnClickListener(new View.OnClickListener() {
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
                prize = etPrize.getText().toString();
                game = Integer.parseInt(etGame.getText().toString());

                createTournament(tourName,tourDescription,numberParticipants,regDateStartNonFormat,regDateEndNonFormat,
                        dateStartNonFormat,dateEndNonFormat,prize,game,type);


            }
        });

        btnAddImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(HostSettingNewActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(HostSettingNewActivity.this, "Please Check Application Permissions", Toast.LENGTH_SHORT).show();
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
                new DatePickerDialog(HostSettingNewActivity.this, date,
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
                new DatePickerDialog(HostSettingNewActivity.this, date,
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
                new DatePickerDialog(HostSettingNewActivity.this, date,
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
                new DatePickerDialog(HostSettingNewActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void createTournament(String tourName,String description,Integer numParticipants,
                                  String regStartDate,String regEndDate,String startDate,String endDate,
                                  String prize,Integer game,Integer type){
        ProgressDialog progress = new ProgressDialog(HostSettingNewActivity.this);
        progress.setMessage("Create Tournament");
        progress.show();
        try {
            Call<SuccessResponseDefaultModel> req = RetrofitConfig.getApiServices(sess.getString("token")).createTournament(sess.getString("id_user"),
                    tourName,description,numParticipants,regStartDate,regEndDate,startDate,endDate,prize,game,type);
            req.enqueue(new Callback<SuccessResponseDefaultModel>() {
                @Override
                public void onResponse(Call<SuccessResponseDefaultModel> call, Response<SuccessResponseDefaultModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            String desc = response.body().getDesc();
                            Toast.makeText(HostSettingNewActivity.this, desc, Toast.LENGTH_SHORT).show();
                            if (!picturePath.equals("")){
                                //uploadImage();
                            }

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(HostSettingNewActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(HostSettingNewActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(HostSettingNewActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(HostSettingNewActivity.this, "Failed Create Tournament", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(HostSettingNewActivity.this, msg, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void uploadImage() {
        ProgressDialog progress = new ProgressDialog(HostSettingNewActivity.this);
        progress.setMessage("Uploading Image");
        progress.show();

        File file = new File(picturePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part parts = MultipartBody.Part.createFormData("image_file", file.getName(), requestBody);

        RequestBody idUser = RequestBody.create(MediaType.parse("text/plain"), sess.getString("id_user"));
        RequestBody idTour = RequestBody.create(MediaType.parse("text/plain"), "belom ditambah siniiii");

        try {
            Call<SuccessResponseDefaultModel> req = RetrofitConfig.getApiUpload(sess.getString("token")).create(ApiService.class).uploadImageTournament(idUser,idTour,parts);
            req.enqueue(new Callback<SuccessResponseDefaultModel>() {
                @Override
                public void onResponse(Call<SuccessResponseDefaultModel> call, Response<SuccessResponseDefaultModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            String desc = response.body().getDesc();
                            Toast.makeText(HostSettingNewActivity.this, desc, Toast.LENGTH_SHORT).show();

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(HostSettingNewActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(HostSettingNewActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(HostSettingNewActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(HostSettingNewActivity.this, "Failed Upload Image", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(HostSettingNewActivity.this, msg, Toast.LENGTH_SHORT).show();
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