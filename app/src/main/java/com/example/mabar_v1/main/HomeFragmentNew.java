package com.example.mabar_v1.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.asura.library.posters.Poster;
import com.asura.library.posters.RemoteImage;
import com.asura.library.views.PosterSlider;
import com.example.mabar_v1.R;
import com.example.mabar_v1.login.LoginActivity;
import com.example.mabar_v1.login.model.ResponseLoginModel;
import com.example.mabar_v1.main.adapter.ListGameAdapter;
import com.example.mabar_v1.main.adapter.ListTournamentAdapter;
import com.example.mabar_v1.main.model.ListGameModel;
import com.example.mabar_v1.profile.DetailProfileAccountActivity;
import com.example.mabar_v1.retrofit.RetrofitConfig;
import com.example.mabar_v1.retrofit.model.DataItem;
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

    private RecyclerView rlGame,rlTournament;
    private PosterSlider posterSlider;
    private ShimmerFrameLayout shimmerLoad;
    private ScrollView svHome;

    private GlobalMethod gm;
    private SessionUser sess;
    private String page = "0";
    private ListGameAdapter listGameAdapter;

    private ArrayList<ListGameModel> listGameModels = new ArrayList<>();
    private JSONArray listFilterGame = new JSONArray();
    List<GetListTournamentResponseModel.Data> listTournament = new ArrayList<>();
    List<DataItem> listGames = new ArrayList<>();

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

        rlGame = root.findViewById(R.id.recycler_game_list);
        rlTournament = root.findViewById(R.id.recycler_new_tournaments);
        posterSlider = root.findViewById(R.id.poster_slider);
        shimmerLoad = root.findViewById(R.id.shimmer_load);
        svHome = root.findViewById(R.id.svHome);

        posterSlider.setDefaultIndicator(3);
        posterSlider.setMustAnimateIndicators(true);

        getListGame(sess.getString("id_user"),"","0");
        getListTournament(sess.getString("id_user"),"","0",listFilterGame);

        // Inflate the layout for this fragment
        return root;
    }

    private void getListTournament(String userId,String search,String page,JSONArray filterGame){
        /*ProgressDialog progress = new ProgressDialog(getContext());
        progress.setMessage("Loading...");
        progress.show();*/
        setLoad(true);

        try {
            Call<GetListTournamentResponseModel> req = RetrofitConfig.getApiServices("").getListTournament(userId, search, page,filterGame);
            req.enqueue(new Callback<GetListTournamentResponseModel>() {
                @Override
                public void onResponse(Call<GetListTournamentResponseModel> call, Response<GetListTournamentResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            listTournament = response.body().getData();

                            List<Poster> posters=new ArrayList<>();
                            //add poster using remote url
                            posters.add(new RemoteImage("https://eventkampus.com/data/event/1/1319/poster-i-fest-2018-supernova-tournament-mobile-legends.jpeg"));
                            posters.add(new RemoteImage("https://1.bp.blogspot.com/-nz7E2so-ud8/X2lbq-9nb4I/AAAAAAAAFTU/5UrnMMLEhOoaiSY5MyXhoB8neZX9m9HwwCLcBGAsYHQ/s957/Untitled-1.jpg"));
                            posters.add(new RemoteImage("https://www.itcshoppingfestival.com/wp-content/uploads/2019/03/banner-web.jpeg"));
                            posters.add(new RemoteImage("https://1.bp.blogspot.com/-tpxDJceDtTg/YS8FCUQncfI/AAAAAAAAClo/QRiA3xb6j00osV6jROkrUFFD7fW4Sry4wCLcBGAsYHQ/s1697/Poster%2BTurnamen%2BMobile%2BLegends%2BVersi%2B1%2Blow.jpg"));
                            posters.add(new RemoteImage("https://mhs.unikama.ac.id/hmps-si/wp-content/nfs/sites/38/2019/10/Artboard-1.png"));
                            posterSlider.setPosters(posters);

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

                            List<Poster> posters=new ArrayList<>();
                            //add poster using remote url
                            posters.add(new RemoteImage("https://eventkampus.com/data/event/1/1319/poster-i-fest-2018-supernova-tournament-mobile-legends.jpeg"));
                            posters.add(new RemoteImage("https://1.bp.blogspot.com/-nz7E2so-ud8/X2lbq-9nb4I/AAAAAAAAFTU/5UrnMMLEhOoaiSY5MyXhoB8neZX9m9HwwCLcBGAsYHQ/s957/Untitled-1.jpg"));
                            posters.add(new RemoteImage("https://www.itcshoppingfestival.com/wp-content/uploads/2019/03/banner-web.jpeg"));
                            posters.add(new RemoteImage("https://1.bp.blogspot.com/-tpxDJceDtTg/YS8FCUQncfI/AAAAAAAAClo/QRiA3xb6j00osV6jROkrUFFD7fW4Sry4wCLcBGAsYHQ/s1697/Poster%2BTurnamen%2BMobile%2BLegends%2BVersi%2B1%2Blow.jpg"));
                            posters.add(new RemoteImage("https://mhs.unikama.ac.id/hmps-si/wp-content/nfs/sites/38/2019/10/Artboard-1.png"));
                            posterSlider.setPosters(posters);

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
}