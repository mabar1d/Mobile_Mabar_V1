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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mabar_v1.R;
import com.example.mabar_v1.login.LoginActivity;
import com.example.mabar_v1.retrofit.ApiService;
import com.example.mabar_v1.retrofit.RetrofitConfig;
import com.example.mabar_v1.retrofit.model.PersonnelResponseModel;
import com.example.mabar_v1.retrofit.model.SuccessResponseDefaultModel;
import com.example.mabar_v1.utility.GlobalMethod;
import com.example.mabar_v1.utility.SessionUser;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailProfileAccountActivity extends AppCompatActivity {

    @BindView(R.id.id_akun)
    TextView tvIdAkun;
    @BindView(R.id.nama_akun)
    TextView tvNamaAkun;
    @BindView(R.id.civ_profile)
    CircularImageView ivProfile;
    @BindView(R.id.first_name)
    EditText etFirstName;
    @BindView(R.id.last_name)
    EditText etLastName;
    @BindView(R.id.radio_group_gender)
    RadioGroup rgGender;
    @BindView(R.id.radio_male)
    RadioButton rbMale;
    @BindView(R.id.radio_female)
    RadioButton rbFemale;
    @BindView(R.id.birthdate)
    EditText etBirthdate;
    @BindView(R.id.address)
    EditText etAddress;
    @BindView(R.id.zip_code)
    EditText etZipCode;
    @BindView(R.id.phone)
    EditText etPhone;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.btn_edit_image)
    Button btnEditImage;
    private RadioButton radioButton;
    @BindView(R.id.iv_is_verif)
    ImageView ivIsVerified;

    Calendar myCalendar = Calendar.getInstance();
    private SimpleDateFormat sdf;
    private GlobalMethod gm;
    private Compressor compressedImageFile;


    private SessionUser sess;
    private String firstName = "";
    private String lastName = "";
    private String gender = "";
    private String birthDate = "";
    private String address = "";
    private String zipCode = "";
    private String phone = "";
    private String brithdateNonFormat = "";

    private static int RESULT_LOAD_IMAGE = 1;
    private String picturePath = "";
    private File fileUpload;
    private MultipartBody.Part bodyImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile_account);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        ButterKnife.bind(this);
        sess = new SessionUser(this);
        gm = new GlobalMethod();
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        getDataPerson();

        tvNamaAkun.setText(sess.getString("username"));
        tvIdAkun.setText("Id: "+sess.getString("id_user"));


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String genderId = "";
                int i = rgGender.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(i);

                gender = radioButton.getText().toString();
                if (gender.equalsIgnoreCase("male")){
                    genderId = "1";
                }else {
                    genderId = "2";
                }
                firstName = etFirstName.getText().toString();
                lastName = etLastName.getText().toString();
                address = etAddress.getText().toString();
                zipCode = etZipCode.getText().toString();
                phone = etPhone.getText().toString();

                if (zipCode.length()<5){
                    Toast.makeText(DetailProfileAccountActivity.this, "Zip Code Must be 5 digits", Toast.LENGTH_SHORT).show();
                }else {
                    if (phone.length()<10){
                        Toast.makeText(DetailProfileAccountActivity.this, "Phone Number Must be at Least 10 digits", Toast.LENGTH_SHORT).show();
                    }else {
                        updateDataPerson(firstName,lastName,genderId,brithdateNonFormat,address,"","","",zipCode,phone);
                    }
                }



            }
        });

        final DatePickerDialog.OnDateSetListener dateOfBirth = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                etBirthdate.setText(gm.setDateIndonesia(2,sdf.format(myCalendar.getTime())));
                brithdateNonFormat = sdf.format(myCalendar.getTime());
            }
        };
        etBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(DetailProfileAccountActivity.this, dateOfBirth,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnEditImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(DetailProfileAccountActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(DetailProfileAccountActivity.this, "Please Check Application Permissions", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            }
        });


    }


    private void getDataPerson(){
        ProgressDialog progress = new ProgressDialog(DetailProfileAccountActivity.this);
        progress.setMessage("Getting Profile Info...");
        progress.show();
        try {
            Call<PersonnelResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getPersonnel(sess.getString("id_user"));
            req.enqueue(new Callback<PersonnelResponseModel>() {
                @Override
                public void onResponse(Call<PersonnelResponseModel> call, Response<PersonnelResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            if (response.body().getData().getImage() != null){
                                Glide.with(DetailProfileAccountActivity.this)
                                        .load(response.body().getData().getImage())
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        //.skipMemoryCache(true)
                                        .into(ivProfile);
                            }

                            if (response.body().getData().isVerified() == 1){
                                ivIsVerified.setVisibility(View.VISIBLE);
                            }else {
                                ivIsVerified.setVisibility(View.GONE);
                            }

                            if (response.body().getData().getFirstname() != null){
                                etFirstName.setText(response.body().getData().getFirstname());
                            }
                            if (response.body().getData().getLastname() != null){
                                etLastName.setText(response.body().getData().getLastname());
                            }
                            if (response.body().getData().getGenderId() != null){
                                if (response.body().getData().getGenderId().equalsIgnoreCase("1")){
                                    rbMale.setChecked(true);
                                    rbFemale.setChecked(false);

                                }else {
                                    rbMale.setChecked(false);
                                    rbFemale.setChecked(true);
                                }
                            }
                            if (response.body().getData().getBirthdate() != null){
                                etBirthdate.setText(gm.setDateIndonesia(2,response.body().getData().getBirthdate()));
                            }
                            if (response.body().getData().getAddress() != null){
                                etAddress.setText(response.body().getData().getAddress());
                            }
                            if (response.body().getData().getZipcode() != null){
                                etZipCode.setText(response.body().getData().getZipcode());
                            }
                            if (response.body().getData().getPhone() != null){
                                etPhone.setText(response.body().getData().getPhone());
                            }
                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(DetailProfileAccountActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(DetailProfileAccountActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(DetailProfileAccountActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(DetailProfileAccountActivity.this, "Failed Request Profil Info", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<PersonnelResponseModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(DetailProfileAccountActivity.this, msg, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateDataPerson(String fname,String lName,String gender_id, String birthDate,String address,String subDistrictId,String districtId,String provinceId,String zipCode,String phone){
        ProgressDialog progress = new ProgressDialog(DetailProfileAccountActivity.this);
        progress.setMessage("Updating Profile Info...");
        progress.show();
        try {
            Call<SuccessResponseDefaultModel> req = RetrofitConfig.getApiServices(sess.getString("token")).updateInfoPersonnel(sess.getString("id_user"),fname,lName,gender_id,birthDate,address,subDistrictId,districtId,provinceId,zipCode,phone);
            req.enqueue(new Callback<SuccessResponseDefaultModel>() {
                @Override
                public void onResponse(Call<SuccessResponseDefaultModel> call, Response<SuccessResponseDefaultModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            String desc = response.body().getDesc();
                            Toast.makeText(DetailProfileAccountActivity.this, desc, Toast.LENGTH_SHORT).show();
                            if (!picturePath.equals("")){
                                uploadImage();
                            }

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(DetailProfileAccountActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(DetailProfileAccountActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(DetailProfileAccountActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(DetailProfileAccountActivity.this, "Failed Update Profil Info", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(DetailProfileAccountActivity.this, msg, Toast.LENGTH_SHORT).show();
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

            //compressedImageFile = new Compressor().compress(DetailProfileAccountActivity.this,picturePath);

            ivProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            //fileUpload = new File(picturePath);
            //filePath = fileUpload.getAbsoluteFile();
            //create RequestBody instance from file
            //RequestBody requestFile = RequestBody.create(MediaType.parse("image"), fileUpload);

            // MultipartBody.Part is used to send also the actual file name
            //bodyImage = MultipartBody.Part.createFormData("uploaded_file", fileUpload.getName(), requestFile);
            //body.toString();

        }


    }

   private void uploadImage() {
       ProgressDialog progress = new ProgressDialog(DetailProfileAccountActivity.this);
       progress.setMessage("Uploading Photo");
       progress.show();

       File file = new File(picturePath);
       RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
       MultipartBody.Part parts = MultipartBody.Part.createFormData("image_file", file.getName(), requestBody);

       RequestBody someData = RequestBody.create(MediaType.parse("text/plain"), sess.getString("id_user"));

       try {
           Call<SuccessResponseDefaultModel> req = RetrofitConfig.getApiUpload(sess.getString("token")).create(ApiService.class).uploadImagePerson(someData,parts);
           req.enqueue(new Callback<SuccessResponseDefaultModel>() {
               @Override
               public void onResponse(Call<SuccessResponseDefaultModel> call, Response<SuccessResponseDefaultModel> response) {
                   if (response.isSuccessful()) {
                       if (response.body().getCode().equals("00")){
                           String desc = response.body().getDesc();
                           Toast.makeText(DetailProfileAccountActivity.this, desc, Toast.LENGTH_SHORT).show();

                       }else if (response.body().getCode().equals("05")){
                           String desc = response.body().getDesc();
                           Toast.makeText(DetailProfileAccountActivity.this, desc, Toast.LENGTH_SHORT).show();
                           progress.dismiss();
                           sess.clearSess();
                           Intent i = new Intent(DetailProfileAccountActivity.this, LoginActivity.class);
                           startActivity(i);
                           finish();
                       }else {
                           String desc = response.body().getDesc();
                           Toast.makeText(DetailProfileAccountActivity.this, desc, Toast.LENGTH_SHORT).show();
                       }

                   } else {
                       Toast.makeText(DetailProfileAccountActivity.this, "Failed Upload Image", Toast.LENGTH_SHORT).show();
                       progress.dismiss();
                   }
                  progress.dismiss();
               }

               @Override
               public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                   String msg = t.getMessage();
                   Toast.makeText(DetailProfileAccountActivity.this, msg, Toast.LENGTH_SHORT).show();
                   progress.dismiss();
               }


           });
       }catch (Exception e){
           e.printStackTrace();
       }
   }

}