package com.example.mabar_v1.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.asura.library.posters.Poster;
import com.asura.library.posters.RemoteImage;
import com.asura.library.views.PosterSlider;
import com.example.mabar_v1.MainActivity;
import com.example.mabar_v1.R;
import com.example.mabar_v1.login.LoginActivity;
import com.example.mabar_v1.login.model.ResponseLoginModel;
import com.example.mabar_v1.main.adapter.ListGameAdapter;
import com.example.mabar_v1.main.adapter.ListMenuHomeAdapter;
import com.example.mabar_v1.main.adapter.ListNewsHomeAdapter;
import com.example.mabar_v1.main.adapter.ListTournamentAdapter;
import com.example.mabar_v1.main.model.ListGameModel;
import com.example.mabar_v1.profile.DetailProfileAccountActivity;
import com.example.mabar_v1.retrofit.RetrofitConfig;
import com.example.mabar_v1.retrofit.model.DataItem;
import com.example.mabar_v1.retrofit.model.GetCarouselResponseModel;
import com.example.mabar_v1.retrofit.model.GetListMenuResponseModel;
import com.example.mabar_v1.retrofit.model.GetListNewsResponseModel;
import com.example.mabar_v1.retrofit.model.GetListTournamentResponseModel;
import com.example.mabar_v1.retrofit.model.ResponseListGame;
import com.example.mabar_v1.splash.SplashScreen1;
import com.example.mabar_v1.utility.GlobalMethod;
import com.example.mabar_v1.utility.SessionUser;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragmentNew extends Fragment {

    private RecyclerView rlGame,rlTournament,rlMenu, rlNews;
    private PosterSlider posterSlider;
    private ShimmerFrameLayout shimmerLoad;
    private ScrollView svHome;

    @BindView(R.id.search_bar)
    EditText etSearchBar;
    @BindView(R.id.btn_search)
    ImageView btnSearch;

    private GlobalMethod gm;
    private SessionUser sess;
    private String page = "0";
    private ListGameAdapter listGameAdapter;
    private ListMenuHomeAdapter listMenuAdapter;

    private ArrayList<ListGameModel> listGameModels = new ArrayList<>();
    private JSONArray listFilterGame = new JSONArray();
    List<GetListTournamentResponseModel.Data> listTournament = new ArrayList<>();
    List<GetListNewsResponseModel.Data> listNews = new ArrayList<>();
    List<DataItem> listGames = new ArrayList<>();
    List<GetListMenuResponseModel.Data> listMenu = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gm = new GlobalMethod();
        sess = new SessionUser(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home_new, container, false);
        ButterKnife.bind(this,root);

        rlGame = root.findViewById(R.id.recycler_game_list);
        rlMenu = root.findViewById(R.id.recycler_menu);
        rlNews = root.findViewById(R.id.recycler_news);
        rlTournament = root.findViewById(R.id.recycler_new_tournaments);
        posterSlider = root.findViewById(R.id.poster_slider);
        shimmerLoad = root.findViewById(R.id.shimmer_load);
        svHome = root.findViewById(R.id.svHome);

        posterSlider.setDefaultIndicator(3);
        posterSlider.setMustAnimateIndicators(true);

        etSearchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    intentWithBundle();
                    return true;
                }
                return false;
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentWithBundle();

            }
        });

        getHighlightsImage(sess.getString("id_user"));
        getListMenu(sess.getString("id_user"));
        getListGame(sess.getString("id_user"),"","0");
        getListTournament(sess.getString("id_user"),"","0",listFilterGame);
        getListNews(sess.getString("id_user"),"","0");

        // Inflate the layout for this fragment
        return root;
    }

    private void getListNews(String userId,String search,String page){

        setLoad(true);

        try {
            Call<GetListNewsResponseModel> req = RetrofitConfig.getApiServices("").getListNews(userId, search, page);
            req.enqueue(new Callback<GetListNewsResponseModel>() {
                @Override
                public void onResponse(Call<GetListNewsResponseModel> call, Response<GetListNewsResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            listNews = response.body().getData();

                            rlNews.setAdapter(new ListNewsHomeAdapter(getActivity(),listNews));
                            rlNews.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                            sess.clearSess();
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Failed Request List News", Toast.LENGTH_SHORT).show();

                    }
                    setLoad(false);
                }

                @Override
                public void onFailure(Call<GetListNewsResponseModel> call, Throwable t) {
                    Toast.makeText(getContext(), "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    setLoad(false);

                }

            });
        }catch (Exception e){
            e.printStackTrace();
            setLoad(false);
        }
    }

    private void getListTournament(String userId,String search,String page,JSONArray filterGame){

        setLoad(true);

        try {
            Call<GetListTournamentResponseModel> req = RetrofitConfig.getApiServices("").getListTournament(userId, search, page,filterGame);
            req.enqueue(new Callback<GetListTournamentResponseModel>() {
                @Override
                public void onResponse(Call<GetListTournamentResponseModel> call, Response<GetListTournamentResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            listTournament = response.body().getData();

                            rlTournament.setAdapter(new ListTournamentAdapter(getActivity(),listTournament));
                            rlTournament.setLayoutManager(new GridLayoutManager(getActivity(),1,GridLayoutManager.HORIZONTAL,false));
                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                            sess.clearSess();
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Failed Request List Tournament", Toast.LENGTH_SHORT).show();

                    }
                    setLoad(false);
                }

                @Override
                public void onFailure(Call<GetListTournamentResponseModel> call, Throwable t) {
                    Toast.makeText(getContext(), "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    setLoad(false);

                }

            });
        }catch (Exception e){
            e.printStackTrace();
            setLoad(false);
        }
    }

    private void getListGame(String userId,String search,String page){

        /*ProgressDialog progress = new ProgressDialog(getContext());
        progress.setMessage("Loading...");
        progress.show();*/
        setLoad(true);
        try {
            Call<ResponseListGame> req = RetrofitConfig.getApiServices("").getListGame(userId, search, page);
            req.enqueue(new Callback<ResponseListGame>() {
                @Override
                public void onResponse(Call<ResponseListGame> call, Response<ResponseListGame> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            listGames.clear();
                            listGames = response.body().getData();

                            /*List<Poster> posters=new ArrayList<>();
                            //add poster using remote url
                            posters.add(new RemoteImage("https://eventkampus.com/data/event/1/1319/poster-i-fest-2018-supernova-tournament-mobile-legends.jpeg"));
                            posters.add(new RemoteImage("https://1.bp.blogspot.com/-nz7E2so-ud8/X2lbq-9nb4I/AAAAAAAAFTU/5UrnMMLEhOoaiSY5MyXhoB8neZX9m9HwwCLcBGAsYHQ/s957/Untitled-1.jpg"));
                            posters.add(new RemoteImage("https://www.itcshoppingfestival.com/wp-content/uploads/2019/03/banner-web.jpeg"));
                            posters.add(new RemoteImage("https://1.bp.blogspot.com/-tpxDJceDtTg/YS8FCUQncfI/AAAAAAAAClo/QRiA3xb6j00osV6jROkrUFFD7fW4Sry4wCLcBGAsYHQ/s1697/Poster%2BTurnamen%2BMobile%2BLegends%2BVersi%2B1%2Blow.jpg"));
                            posters.add(new RemoteImage("https://mhs.unikama.ac.id/hmps-si/wp-content/nfs/sites/38/2019/10/Artboard-1.png"));
                            posterSlider.setPosters(posters);*/

                            listGameAdapter = new ListGameAdapter(getContext(),listGames);
                            rlGame.setLayoutManager(new GridLayoutManager(getContext(),1,GridLayoutManager.HORIZONTAL,false));
                            rlGame.setAdapter(listGameAdapter);
                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                            sess.clearSess();
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Failed Request List Games", Toast.LENGTH_SHORT).show();

                    }
                    setLoad(false);
                }

                @Override
                public void onFailure(Call<ResponseListGame> call, Throwable t) {
                    Toast.makeText(getActivity(), "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    setLoad(false);

                }


            });
        }catch (Exception e){
            e.printStackTrace();
            setLoad(false);
        }


    }

    private void getListMenu(String userId){

        setLoad(true);
        try {
            Call<GetListMenuResponseModel> req = RetrofitConfig.getApiServices("").getListMenuHome(userId);
            req.enqueue(new Callback<GetListMenuResponseModel>() {
                @Override
                public void onResponse(Call<GetListMenuResponseModel> call, Response<GetListMenuResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            listMenu.clear();
                            listMenu = response.body().getData();

                            listMenuAdapter = new ListMenuHomeAdapter(getContext(),listMenu);
                            rlMenu.setLayoutManager(new GridLayoutManager(getContext(),1,GridLayoutManager.HORIZONTAL,false));
                            rlMenu.setAdapter(listMenuAdapter);
                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                            sess.clearSess();
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Failed Request List Games", Toast.LENGTH_SHORT).show();

                    }
                    setLoad(false);
                }

                @Override
                public void onFailure(Call<GetListMenuResponseModel> call, Throwable t) {
                    Toast.makeText(getActivity(), "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    setLoad(false);

                }


            });
        }catch (Exception e){
            e.printStackTrace();
            setLoad(false);
        }


    }

    private void getHighlightsImage(String userId){

        setLoad(true);
        try {
            Call<GetCarouselResponseModel> req = RetrofitConfig.getApiServices("").getCarouselTournament(userId);
            req.enqueue(new Callback<GetCarouselResponseModel>() {
                @Override
                public void onResponse(Call<GetCarouselResponseModel> call, Response<GetCarouselResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            List<Poster> posters=new ArrayList<>();
                            posters.clear();

                            for(int i = 0; i < response.body().getData().size() ; i++){
                                posters.add(new RemoteImage(response.body().getData().get(i).getImage()));
                            }
                            posterSlider.setPosters(posters);

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                            sess.clearSess();
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Failed Request List Games", Toast.LENGTH_SHORT).show();

                    }
                    setLoad(false);
                }

                @Override
                public void onFailure(Call<GetCarouselResponseModel> call, Throwable t) {
                    Toast.makeText(getActivity(), "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    setLoad(false);

                }


            });
        }catch (Exception e){
            e.printStackTrace();
            setLoad(false);
        }


    }

    private void setLoad(Boolean loading){
        if (loading){
            shimmerLoad.setVisibility(View.VISIBLE);
            svHome.setVisibility(View.GONE);

            shimmerLoad.startShimmer();
        }else {

            shimmerLoad.setVisibility(View.GONE);
            svHome.setVisibility(View.VISIBLE);

            shimmerLoad.stopShimmer();

        }


    }

    private void intentWithBundle(){
        Intent i = new Intent(getActivity(),GeneralSearchTournamentActivity.class);
        Bundle bun = new Bundle();
        bun.putString("judul_game",etSearchBar.getText().toString());
        i.putExtras(bun);
        startActivity(i);
    }
}