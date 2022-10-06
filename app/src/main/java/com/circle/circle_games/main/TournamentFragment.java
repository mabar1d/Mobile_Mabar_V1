package com.circle.circle_games.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle.circle_games.utility.SessionUser;
import com.circle.circle_games.R;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.main.adapter.ListTournamentAdapter;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.GetListTournamentResponseModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TournamentFragment extends Fragment {

    private RecyclerView rlMyTournament;
    private EditText searchBarTournament;
    private ShimmerFrameLayout shimmerLoad;
    private LinearLayout llContent;
    private ImageView btnSearch;
    private SessionUser sess;
    private GlobalMethod globalMethod;
    private JSONArray fltGame = new JSONArray();
    List<GetListTournamentResponseModel.Data> listTournament = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sess = new SessionUser(getActivity());
        globalMethod = new GlobalMethod();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tournament, container, false);

        rlMyTournament = root.findViewById(R.id.recycler_my_tournaments);
        searchBarTournament = root.findViewById(R.id.search_bar_tournament);
        btnSearch = root.findViewById(R.id.btn_search);
        shimmerLoad = root.findViewById(R.id.shimmer_load);
        llContent = root.findViewById(R.id.llcontent);

        getListTournament(sess.getString("id_user"),"","0",fltGame);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListTournament(sess.getString("id_user"),searchBarTournament.getText().toString(),"0",fltGame);
            }
        });
        searchBarTournament.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    getListTournament(sess.getString("id_user"),searchBarTournament.getText().toString(),"0",fltGame);
                    return true;
                }
                return false;
            }
        });

        return root;
    }

    private void getListTournament(String userId, String search, String page, JSONArray filterGame ){
        filterGame = new JSONArray();
        filterGame.put("6");
        filterGame.put("7");
        /*ProgressDialog progress = new ProgressDialog(getContext());
        progress.setMessage("Loading...");
        progress.show();*/
        globalMethod.setShimmerLinearLayout(true,shimmerLoad,llContent);

        try {
            Call<GetListTournamentResponseModel> req = RetrofitConfig.getApiServices("").getListTournament(userId, search, page,filterGame);
            req.enqueue(new Callback<GetListTournamentResponseModel>() {
                @Override
                public void onResponse(Call<GetListTournamentResponseModel> call, Response<GetListTournamentResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            listTournament = response.body().getData();

                            rlMyTournament.setAdapter(new ListTournamentAdapter(getActivity(),listTournament));
                            rlMyTournament.setLayoutManager(new GridLayoutManager(getActivity(),2));
                        }else {
                            String notif = response.body().getDesc();
                            Toast.makeText(getContext(), notif, Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.body().getCode().equals("05")){
                        String desc = response.body().getDesc();
                        Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                        sess.clearSess();
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    }  else {
                        String desc = response.body().getDesc();
                        Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();

                    }
                    globalMethod.setShimmerLinearLayout(false,shimmerLoad,llContent);
                }

                @Override
                public void onFailure(Call<GetListTournamentResponseModel> call, Throwable t) {
                    Toast.makeText(getActivity(), "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    globalMethod.setShimmerLinearLayout(false,shimmerLoad,llContent);

                }


            });
        }catch (Exception e){
            e.printStackTrace();
            globalMethod.setShimmerLinearLayout(false,shimmerLoad,llContent);
        }
    }
}