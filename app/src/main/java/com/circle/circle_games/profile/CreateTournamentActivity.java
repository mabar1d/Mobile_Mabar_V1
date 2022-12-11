package com.circle.circle_games.profile;

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

import com.circle.circle_games.retrofit.ApiService;
import com.circle.circle_games.retrofit.model.ResponseCreateTournamentResponseModel;
import com.circle.circle_games.utility.CurrencyEditTextWatcher;
import com.circle.circle_games.utility.SessionUser;
import com.circle.circle_games.R;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.retrofit.ApiService;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.ResponseCreateTournamentResponseModel;
import com.circle.circle_games.retrofit.model.SuccessResponseDefaultModel;
import com.circle.circle_games.utility.CurrencyEditTextWatcher;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.text.NumberFormat;
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

public class CreateTournamentActivity extends AppCompatActivity {

    @BindView(R.id.image_tournament)
    ImageView ivTournament;
    @BindView(R.id.btn_add_image)
    Button btnAddImage;
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
    @BindView(R.id.btn_create_tournament)
    Button btnCreateTournament;
    @BindView(R.id.btn_back)
    ImageView btnBack;

    BottomSheetDialog bsDialog;

    private String tourName = "";
    private String tourDescription = "";
    private Integer numberParticipants;
    private String regDateStart = "";
    private String regDateEnd = "";
    private String startDate = "";
    private String endDate = "";
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
    private RadioButton radioButton;
    private String current = "";
    NumberFormat currencyFormatter;
    ArrayList<String> listSpinnerGame = new ArrayList<>();

    private long minRegEndDate = 0;
    private long minStartDate = 0;
    private long minEndDate = 0;


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

        etPrize.addTextChangedListener(new CurrencyEditTextWatcher(etPrize));
        etRegFee.addTextChangedListener(new CurrencyEditTextWatcher(etRegFee));

        listSpinnerGame.add("Mobile Legends");
        listSpinnerGame.add("Free Fire");
        listSpinnerGame.add("PUBG");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,listSpinnerGame);
        spGame.setAdapter(adapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCreateTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gm.showDialogConfirmation(CreateTournamentActivity.this, "Create Tournament?", "Are you sure?", "Create", "Cancel", new View.OnClickListener() {
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
                            Toast.makeText(CreateTournamentActivity.this, "Please select a picture before create Tournament", Toast.LENGTH_SHORT).show();
                        }else {
                            if (flagSendData == true){
                                uploadImage(idTour);
                            }else {
                                createTournament(tourName,tourDescription,numberParticipants,regDateStartNonFormat,regDateEndNonFormat,
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

        btnAddImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CreateTournamentActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(CreateTournamentActivity.this, "Please Check Application Permissions", Toast.LENGTH_SHORT).show();
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
                minRegEndDate = myCalendar.getTimeInMillis();
                etRegisterDateStart.setText(gm.setDateIndonesia(2,sdf.format(myCalendar.getTime())));
                regDateStartNonFormat = sdf.format(myCalendar.getTime());
            }
        };
        etRegisterDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dp = new DatePickerDialog(CreateTournamentActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dp.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dp.show();

                etRegisterDateEnd.getText().clear();
                etStartDate.getText().clear();
                etEndDate.getText().clear();

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
                minStartDate = myCalendar.getTimeInMillis();
                etRegisterDateEnd.setText(gm.setDateIndonesia(2,sdf.format(myCalendar.getTime())));
                regDateEndNonFormat = sdf.format(myCalendar.getTime());
            }
        };

        etRegisterDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dp = new DatePickerDialog(CreateTournamentActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dp.getDatePicker().setMinDate(minRegEndDate);
                dp.show();
                etStartDate.getText().clear();
                etEndDate.getText().clear();
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
                minEndDate = myCalendar.getTimeInMillis();
                etStartDate.setText(gm.setDateIndonesia(2,sdf.format(myCalendar.getTime())));
                dateStartNonFormat = sdf.format(myCalendar.getTime());
            }
        };

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dp = new DatePickerDialog(CreateTournamentActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dp.getDatePicker().setMinDate(minStartDate);
                dp.show();
                etEndDate.getText().clear();
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
                DatePickerDialog dp = new DatePickerDialog(CreateTournamentActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dp.getDatePicker().setMinDate(minEndDate);
                dp.show();
            }
        });
    }

    private void createTournament(String tourName,String description,Integer numParticipants,
                                  String regStartDate,String regEndDate,String startDate,String endDate,String regFee,
                                  String prize,Integer game,Integer type){
        ProgressDialog progress = new ProgressDialog(CreateTournamentActivity.this);
        progress.setMessage("Create Tournament");
        progress.show();
        try {
            Call<ResponseCreateTournamentResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).createTournament(sess.getString("id_user"),
                    tourName,description,numParticipants,regStartDate,regEndDate,startDate,endDate,regFee,prize,game,type);
            req.enqueue(new Callback<ResponseCreateTournamentResponseModel>() {
                @Override
                public void onResponse(Call<ResponseCreateTournamentResponseModel> call, Response<ResponseCreateTournamentResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            flagSendData = true;
                            String desc = response.body().getDesc();
                            Toast.makeText(CreateTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                            idTour = String.valueOf(response.body().getData().getTournamentId());
                            uploadImage(idTour);

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(CreateTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(CreateTournamentActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(CreateTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(CreateTournamentActivity.this, "Failed Create Tournament", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseCreateTournamentResponseModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(CreateTournamentActivity.this, msg, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void uploadImage(String idTournament) {
        ProgressDialog progress = new ProgressDialog(CreateTournamentActivity.this);
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
                            Toast.makeText(CreateTournamentActivity.this, notif, Toast.LENGTH_SHORT).show();
                            finish();

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(CreateTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(CreateTournamentActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(CreateTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(CreateTournamentActivity.this, "Failed Upload Image", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(CreateTournamentActivity.this, msg, Toast.LENGTH_SHORT).show();
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