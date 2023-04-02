package com.circle.circle_games.retrofit;

import com.circle.circle_games.login.model.ResponseLoginModel;
import com.circle.circle_games.retrofit.model.CreateTeamResponseModel;
import com.circle.circle_games.retrofit.model.GetCarouselResponseModel;
import com.circle.circle_games.retrofit.model.GetInfoNewsCategoryResponseModel;
import com.circle.circle_games.retrofit.model.GetInfoNewsResponseModel;
import com.circle.circle_games.retrofit.model.GetLinkTreeWebviewResponseModel;
import com.circle.circle_games.retrofit.model.GetListCategoryNewsResponseModel;
import com.circle.circle_games.retrofit.model.GetListMenuResponseModel;
import com.circle.circle_games.retrofit.model.GetListNewsResponseModel;
import com.circle.circle_games.retrofit.model.GetListRequestJoinTeamResponseModel;
import com.circle.circle_games.retrofit.model.GetListTeamResponseModel;
import com.circle.circle_games.retrofit.model.GetListTeamTournamentResponseModel;
import com.circle.circle_games.retrofit.model.GetListTournamentResponseModel;
import com.circle.circle_games.retrofit.model.GetTeamInfoResponseModel;
import com.circle.circle_games.retrofit.model.ListPersonnelResponseModel;
import com.circle.circle_games.retrofit.model.PersonnelResponseModel;
import com.circle.circle_games.retrofit.model.ResponseCreateTournamentResponseModel;
import com.circle.circle_games.retrofit.model.ResponseGetInfoTournamentModel;
import com.circle.circle_games.retrofit.model.ResponseListGame;
import com.circle.circle_games.retrofit.model.SuccessResponseDefaultModel;
import com.circle.circle_games.retrofit.model.*;

import com.circle.circle_games.signup.model.ResponseRegisterModel;
import com.circle.circle_games.transaction.model.GetListTransactionResponseModel;

import org.json.JSONArray;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    @POST("checkVersionApk")
    Call<CheckVersionResponseModel> checkVersion(
            @Query("user_id") String user_id
    );


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
            @Query("password") String password,
            @Query("token_firebase") String token_firebase
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

    //Search not Member
    @POST("getPersonnelNotMember")
    Call<ListPersonnelNotMemberResponseModel> getListPersonnelNotMember(
            @Query("user_id") String user_id,
            @Query("search") String search,
            @Query("page") String page
    );

    //Update Personnel
    @POST("updateInfoPersonnel")
    Call<SuccessResponseDefaultModel> updateInfoPersonnel(
            @Query("user_id") String user_id,
            @Query("firstname") String firstname,
            @Query("lastname") String lastname,
            @Query("ign") String ign,
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
            @Query("type") Integer type,
            @Query("terms_condition") String terms_condition
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
            @Query("type") Integer type,
            @Query("terms_condition") String terms_condition
    );

    //get List Tournament
    @POST("getListTournament")
    Call<GetListTournamentResponseModel> getListTournament(
            @Query("user_id") String user_id,
            @Query("search") String search,
            @Query("page") String page,
            @Query("filter_game") JSONArray filter_game
    );

    //get List Host Tournament
    @POST("getListMyTournament")
    Call<GetListTournamentResponseModel> getListHostTournament(
            @Query("user_id") String user_id,
            @Query("search") String search,
            @Query("page") String page,
            @Query("filter_game") JSONArray filter_game,
            @Query("type") String type
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

    //get list game
    @POST("getListApkMenu")
    Call<GetListMenuResponseModel> getListMenuHome(
            @Query("user_id") String user_id
    );

    @POST("getTournamentTreeWeb")
    Call<GetLinkTreeWebviewResponseModel> getLinkTournamentTreeWeb(
            @Query("user_id") String user_id,
            @Query("tournament_id") String tournament_id
    );

    @POST("getCarouselTournament")
    Call<GetCarouselResponseModel> getCarouselTournament(
            @Query("user_id") String user_id
    );

    @POST("getListNews")
    Call<GetListNewsResponseModel> getListNews(
            @Query("user_id") String user_id,
            @Query("search") String search,
            @Query("page") String page
    );

    @POST("getInfoNews")
    Call<GetInfoNewsResponseModel> getInfoNews(
            @Query("user_id") String user_id,
            @Query("news_id") String news_id
    );

    @POST("getListNewsCategory")
    Call<GetListCategoryNewsResponseModel> getListNewsCategory(
            @Query("user_id") String user_id,
            @Query("search") String search,
            @Query("page") String page
    );

    @POST("getInfoNewsCategory")
    Call<GetInfoNewsCategoryResponseModel> getInfoNewsCategory(
            @Query("user_id") String user_id,
            @Query("news_category_id") String news_category_id
    );

    //Videos

    @POST("getListVideo")
    Call<GetListVideosResponseModel> getListVideo(
            @Query("user_id") String user_id,
            @Query("search") String search,
            @Query("page") String page
    );

    @POST("getInfoVideo")
    Call<GetInfoVideosResponseModel> getInfoVideo(
            @Query("user_id") String user_id,
            @Query("video_id") String video_id,
            @Query("slug") String slug
    );

    //General Info
    @POST("getInfoGeneral")
    Call<GetTermsConditionResponseModel> getInfoGeneral(
            @Query("user_id") String user_id,
            @Query("type") String type
    );

    //Transaction
    @POST("/api/payment/getListTransactions")
    Call<GetListTransactionResponseModel> getListTransactions(
            @Query("user_id") String user_id,
            @Query("payment_code") String payment_code,
            @Query("search") String search,
            @Query("page") String page
    );

}
