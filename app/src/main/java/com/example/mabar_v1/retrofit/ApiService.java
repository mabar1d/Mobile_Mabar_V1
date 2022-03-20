package com.example.mabar_v1.retrofit;

import com.example.mabar_v1.login.model.ResponseLoginModel;
import com.example.mabar_v1.retrofit.model.GetListTournamentResponseModel;
import com.example.mabar_v1.retrofit.model.ListPersonnelResponseModel;
import com.example.mabar_v1.retrofit.model.PersonnelResponseModel;
import com.example.mabar_v1.retrofit.model.ResponseGetInfoTournamentModel;
import com.example.mabar_v1.retrofit.model.ResponseListGame;
import com.example.mabar_v1.retrofit.model.SuccessResponseDefaultModel;
import com.example.mabar_v1.signup.model.ResponseRegisterModel;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    @POST("register")
    Call<ResponseRegisterModel> register(
            @Query("username") String username,
            @Query("email") String email,
            @Query("password") String password,
            @Query("password_confirmation") String password_confirmation
    );

    @POST("login")
    Call<ResponseLoginModel> login(
            @Query("username") String username,
            @Query("email") String email,
            @Query("password") String password
    );

    /*@POST("refresh")
    Call<> refreshToken(
            @Query("username") String username,
            @Query("email") String email,
            @Query("password") String password
    );*/

    /*Personel API <====>*/

    //detail player
    @POST("getPersonnel")
    Call<PersonnelResponseModel> getPersonnel(
            @Query("user_id") String user_id
    );

    //Search Personnel
    @POST("getListPersonnel")
    Call<ListPersonnelResponseModel> getListPersonnel(
            @Query("search") String search,
            @Query("page") String page
    );

    //Update Personnel
    @POST("updateInfoPersonnel")
    Call<SuccessResponseDefaultModel> updateInfoPersonnel(
            @Query("user_id") String user_id,
            @Query("firstname") String firstname,
            @Query("lastname") String lastname,
            @Query("gender_id") String gender_id,
            @Query("birthdate") String birthdate,
            @Query("address") String address,
            @Query("sub_district_id") String sub_district_id,
            @Query("district_id") String district_id,
            @Query("province_id") String province_id,
            @Query("zipcode") String zipcode,
            @Query("phone") String phone
    );

    //Update Team Personnel
    @POST("updateTeamPersonnel")
    Call<SuccessResponseDefaultModel> updateTeamPersonnel(
            @Query("user_id") String user_id,
            @Query("team_id") String team_id
    );

    //Req Team Lead
    @POST("personnelReqTeamLead")
    Call<SuccessResponseDefaultModel> personnelReqTeamLead(
            @Query("user_id") String user_id
    );

    //Req Host
    @POST("personnelReqHost")
    Call<SuccessResponseDefaultModel> personnelReqHost(
            @Query("user_id") String user_id
    );

    //Person Request Join Team
    @POST("personnelReqJoinTeam")
    Call<SuccessResponseDefaultModel> personnelReqJoinTeam(
            @Query("user_id") String user_id,
            @Query("team_id") String team_id
    );

    //Person leave Team
    @POST("personnelLeaveTeam")
    Call<SuccessResponseDefaultModel> personnelLeaveTeam(
            @Query("user_id") String user_id
    );


    /*Team API <====>*/

    //Create Team
    @POST("createTeam")
    Call<SuccessResponseDefaultModel> createTeam(
            @Query("user_id") String user_id,
            @Query("name") String name,
            @Query("info") String info,
            @Query("personnel") String[] personnel
    );

    //Update Team
    @POST("updateTeam")
    Call<SuccessResponseDefaultModel> updateTeam(
            @Query("user_id") String user_id,
            @Query("team_id") String team_id,
            @Query("name") String name,
            @Query("info") String info,
            @Query("personnel") String[] personnel
    );

    //answer Req Team from person
    @POST("answerReqJoinTeam")
    Call<SuccessResponseDefaultModel> answerReqJoinTeam(
            @Query("user_id") String user_id,
            @Query("user_id_requested") String user_id_requested,
            @Query("answer") String answer
    );

    //Delete Team
    @POST("deleteTeam")
    Call<SuccessResponseDefaultModel> deleteTeam(
            @Query("user_id") String user_id,
            @Query("team_id") String team_id
    );

    //search list Team
    @POST("getListTeam")
    Call<SuccessResponseDefaultModel> getListTeam(
            @Query("user_id") String user_id,
            @Query("search") String search,
            @Query("page") String page
    );

    //get Team Info
    @POST("getInfoTeam")
    Call<SuccessResponseDefaultModel> getInfoTeam(
            @Query("user_id") String user_id,
            @Query("team_id") String team_id
    );


    /*Tournament API <====>*/

    //Create Tournament
    @POST("createTournament")
    Call<SuccessResponseDefaultModel> createTournament(
            @Query("host_id") String host_id,
            @Query("name") String name,
            @Query("detail") String detail,
            @Query("number_of_participants") String number_of_participants,
            @Query("register_date_start") String register_date_start,
            @Query("register_date_end") String register_date_end,
            @Query("start_date") String start_date,
            @Query("end_date") String end_date,
            @Query("prize") String prize
    );

    //Update Tournament
    @POST("updateTournament")
    Call<SuccessResponseDefaultModel> updateTournament(
            @Query("host_id") String host_id,
            @Query("tournament_id") String tournament_id,
            @Query("name") String name,
            @Query("detail") String detail,
            @Query("number_of_participants") String number_of_participants,
            @Query("register_date_start") String register_date_start,
            @Query("register_date_end") String register_date_end,
            @Query("start_date") String start_date,
            @Query("end_date") String end_date,
            @Query("prize") String prize
    );

    //get List Tournament
    @POST("getListTournament")
    Call<GetListTournamentResponseModel> getListTournament(
            @Query("user_id") String user_id,
            @Query("search") String search,
            @Query("page") String page,
            @Query("filter_game") JSONArray filter_game
    );

    //Register Tournament
    @POST("registerTournament")
    Call<SuccessResponseDefaultModel> registerTournament(
            @Query("user_id") String user_id,
            @Query("tournament_id") String search
    );

    //get Info Tournament
    @POST("getInfoTournament")
    Call<ResponseGetInfoTournamentModel> getInfoTournament(
            @Query("user_id") String user_id,
            @Query("tournament_id") String tournament_id
    );

    //Game API

    //get list game
    @POST("getListMasterGame")
    Call<ResponseListGame> getListGame(
            @Query("user_id") String user_id,
            @Query("search") String search,
            @Query("page") String page
    );

    //Upload Image
    @Multipart
    @POST("uploadImagePersonnel")
    Call<SuccessResponseDefaultModel> uploadImagePerson(
            @Part("user_id") RequestBody user_id,
            @Part MultipartBody.Part image_file
    );

}
