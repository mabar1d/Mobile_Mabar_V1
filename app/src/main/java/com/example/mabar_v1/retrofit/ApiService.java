package com.example.mabar_v1.retrofit;

import com.example.mabar_v1.login.model.ResponseLoginModel;
import com.example.mabar_v1.retrofit.model.CreateTeamResponseModel;
import com.example.mabar_v1.retrofit.model.GetListRequestJoinTeamResponseModel;
import com.example.mabar_v1.retrofit.model.GetListTeamResponseModel;
import com.example.mabar_v1.retrofit.model.GetListTeamTournamentResponseModel;
import com.example.mabar_v1.retrofit.model.GetListTournamentResponseModel;
import com.example.mabar_v1.retrofit.model.GetTeamInfoResponseModel;
import com.example.mabar_v1.retrofit.model.ListPersonnelResponseModel;
import com.example.mabar_v1.retrofit.model.PersonnelResponseModel;
import com.example.mabar_v1.retrofit.model.ResponseCreateTournamentResponseModel;
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

    //Req Member
    @POST("personnelReqMember")
    Call<SuccessResponseDefaultModel> personnelReqMember(
            @Query("user_id") String user_id
    );

    //Person Request Join Team
    @POST("personnelReqJoinTeam")
    Call<SuccessResponseDefaultModel> personnelReqJoinTeam(
            @Query("user_id") String user_id,
            @Query("team_id") String team_id
    );

    //Team Tournament List(Team Leader)
    @POST("getListTournamentTeam")
    Call<GetListTeamTournamentResponseModel> getListTeamTournamentforLeader(
            @Query("user_id") String user_id,
            @Query("team_id") String team_id
    );

    //get List Tournament (Member)
    @POST("getListMyTeamTournament")
    Call<GetListTournamentResponseModel> getListTeamTournamentforMember(
            @Query("user_id") String user_id,
            @Query("team_id") String team_id,
            @Query("search") String search,
            @Query("page") String page
    );

    //Person leave Team
    @POST("personnelLeaveTeam")
    Call<SuccessResponseDefaultModel> personnelLeaveTeam(
            @Query("user_id") String user_id
    );


    /*Team API <====>*/

    //Create Team
    @POST("createTeam")
    Call<CreateTeamResponseModel> createTeam(
            @Query("user_id") String user_id,
            @Query("name") String name,
            @Query("info") String info,
            @Query("game_id") String game_id,
            @Query("personnel") JSONArray personnel
    );

    //Update Team
    @POST("updateTeam")
    Call<SuccessResponseDefaultModel> updateTeam(
            @Query("user_id") String user_id,
            @Query("team_id") String team_id,
            @Query("name") String name,
            @Query("info") String info,
            @Query("game_id") String game_id,
            @Query("personnel") JSONArray personnel
    );

    //answer Req Team from person
    @POST("answerReqJoinTeam")
    Call<SuccessResponseDefaultModel> answerReqJoinTeam(
            @Query("user_id") String user_id,
            @Query("user_id_requested") int user_id_requested,
            @Query("answer") String answer
    );

    @POST("getListReqJoinTeam")
    Call<GetListRequestJoinTeamResponseModel> getListReqJoinTeam(
            @Query("user_id") String user_id,
            @Query("team_id") String user_id_requested
    );

    //Delete Team
    @POST("deleteTeam")
    Call<SuccessResponseDefaultModel> deleteTeam(
            @Query("user_id") String user_id,
            @Query("team_id") String team_id
    );

    //search list Team
    @POST("getListTeam")
    Call<GetListTeamResponseModel> getListTeam(
            @Query("user_id") String user_id,
            @Query("search") String search,
            @Query("page") String page
    );

    //get Team Info
    @POST("getInfoTeam")
    Call<GetTeamInfoResponseModel> getInfoTeam(
            @Query("user_id") String user_id,
            @Query("team_id") String team_id
    );


    /*Tournament API <====>*/

    //Create Tournament
    @POST("createTournament")
    Call<ResponseCreateTournamentResponseModel> createTournament(
            @Query("host_id") String host_id,
            @Query("name") String name,
            @Query("detail") String detail,
            @Query("number_of_participants") Integer number_of_participants,
            @Query("register_date_start") String register_date_start,
            @Query("register_date_end") String register_date_end,
            @Query("start_date") String start_date,
            @Query("end_date") String end_date,
            @Query("register_fee") String register_fee,
            @Query("prize") String prize,
            @Query("game_id") Integer game_id,
            @Query("type") Integer type
    );

    //Update Tournament
    @POST("updateTournament")
    Call<SuccessResponseDefaultModel> updateTournament(
            @Query("host_id") String host_id,
            @Query("tournament_id") String tournament_id,
            @Query("name") String name,
            @Query("detail") String detail,
            @Query("number_of_participants") Integer number_of_participants,
            @Query("register_date_start") String register_date_start,
            @Query("register_date_end") String register_date_end,
            @Query("start_date") String start_date,
            @Query("end_date") String end_date,
            @Query("register_fee") String register_fee,
            @Query("prize") String prize,
            @Query("game_id") Integer game_id,
            @Query("type") Integer type
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
    @Multipart
    @POST("uploadImageTournament")
    Call<SuccessResponseDefaultModel> uploadImageTournament(
            @Part("user_id") RequestBody user_id,
            @Part("tournament_id") RequestBody tournament_id,
            @Part MultipartBody.Part image_file
    );

    @Multipart
    @POST("uploadImageTeam")
    Call<SuccessResponseDefaultModel> uploadImageTeam(
            @Part("user_id") RequestBody user_id,
            @Part("team_id") RequestBody team_id,
            @Part MultipartBody.Part image_file
    );

}
