package com.example.mabar_v1.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.asura.library.views.PosterSlider;
import com.example.mabar_v1.R;
import com.example.mabar_v1.login.LoginActivity;
import com.example.mabar_v1.login.model.ResponseLoginModel;
import com.example.mabar_v1.main.adapter.ListTournamentAdapter;
import com.example.mabar_v1.retrofit.RetrofitConfig;
import com.example.mabar_v1.retrofit.model.GetListTournamentResponseModel;
import com.example.mabar_v1.splash.SplashScreen1;
import com.example.mabar_v1.utility.GlobalMethod;
import com.example.mabar_v1.utility.SessionUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragmentNew extends Fragment {

    private RecyclerView rlGame,rlTournament;
    private PosterSlider posterSlider;

    private GlobalMethod gm;
    private SessionUser sess;
    private String page = "0";

    List<GetListTournamentResponseModel.Data> listTournament = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gm = new GlobalMethod();
        sess = new SessionUser(getActivity());


        getListTournament(sess.getString("id_user"),"","0");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home_new, container, false);

        rlGame = root.findViewById(R.id.recycler_game_list);
        rlTournament = root.findViewById(R.id.recycler_new_tournaments);
        posterSlider = root.findViewById(R.id.poster_slider);

        // Inflate the layout for this fragment
        return root;
    }

    private void getListTournament(String userId,String search,String page){
        ProgressDialog progress = new ProgressDialog(getContext());
        progress.setMessage("Loading...");
        progress.show();
        try {
            Call<GetListTournamentResponseModel> req = RetrofitConfig.getApiServices("").getListTournament(userId, search, page);
            req.enqueue(new Callback<GetListTournamentResponseModel>() {
                @Override
                public void onResponse(Call<GetListTournamentResponseModel> call, Response<GetListTournamentResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            listTournament = response.body().getData();

                            rlTournament.setAdapter(new ListTournamentAdapter(getActivity(),listTournament));
                            rlTournament.setLayoutManager(new GridLayoutManager(getActivity(),1,GridLayoutManager.HORIZONTAL,false));
                        }else {
                            String notif = response.body().getDesc();
                            Toast.makeText(getContext(), notif, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Gagal Mengambil List Tournament", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<GetListTournamentResponseModel> call, Throwable t) {
                    Toast.makeText(getContext(), "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    progress.dismiss();

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}