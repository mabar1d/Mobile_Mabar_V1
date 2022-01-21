package com.example.mabar_v1.main;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.asura.library.posters.Poster;
import com.asura.library.posters.RemoteImage;
import com.example.mabar_v1.R;
import com.example.mabar_v1.main.adapter.ListTournamentAdapter;
import com.example.mabar_v1.retrofit.RetrofitConfig;
import com.example.mabar_v1.retrofit.model.GetListTournamentResponseModel;
import com.example.mabar_v1.utility.SessionUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TournamentFragment extends Fragment {

    private RecyclerView rlMyTournament;
    private EditText searchBarTournament;
    private ImageView btnSearch;
    private SessionUser sess;

    List<GetListTournamentResponseModel.Data> listTournament = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sess = new SessionUser(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tournament, container, false);

        rlMyTournament = root.findViewById(R.id.recycler_my_tournaments);
        searchBarTournament = root.findViewById(R.id.search_bar_tournament);
        btnSearch = root.findViewById(R.id.btn_search);

        getListTournament(sess.getString("id_user"),"","0");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListTournament(sess.getString("id_user"),searchBarTournament.getText().toString(),"0");
            }
        });
        searchBarTournament.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    getListTournament(sess.getString("id_user"),searchBarTournament.getText().toString(),"0");
                    return true;
                }
                return false;
            }
        });

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

                            rlMyTournament.setAdapter(new ListTournamentAdapter(getActivity(),listTournament));
                            rlMyTournament.setLayoutManager(new GridLayoutManager(getActivity(),2));
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