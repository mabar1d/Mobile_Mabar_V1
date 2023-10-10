package com.circle.circle_games.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle.circle_games.main.adapter.ListNewsHomeAdapter;
import com.circle.circle_games.retrofit.model.GetListNewsResponseModel;
import com.circle.circle_games.R;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.main.adapter.ListGameAdapter;
import com.circle.circle_games.main.adapter.ListMenuHomeAdapter;
import com.circle.circle_games.main.adapter.ListNewsHomeAdapter;
import com.circle.circle_games.main.adapter.ListTournamentAdapter;
import com.circle.circle_games.main.model.ListGameModel;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.DataItem;
import com.circle.circle_games.retrofit.model.GetCarouselResponseModel;
import com.circle.circle_games.retrofit.model.GetListMenuResponseModel;
import com.circle.circle_games.retrofit.model.GetListNewsResponseModel;
import com.circle.circle_games.retrofit.model.GetListTournamentResponseModel;
import com.circle.circle_games.retrofit.model.ResponseListGame;
import com.circle.circle_games.room.viewmodel.MasterViewModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragmentNew extends Fragment {

    private RecyclerView rlGame,rlTournament,rlMenu, rlNews;
    private ImageSlider posterSlider;
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

    private MasterViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gm = new GlobalMethod();
        sess = new SessionUser(getActivity());
        viewModel = ViewModelProviders.of(this).get(MasterViewModel.class);

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
        posterSlider = root.findViewById(R.id.image_slider);
        shimmerLoad = root.findViewById(R.id.shimmer_load);
        svHome = root.findViewById(R.id.svHome);


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
       // getListGame(sess.getString("id_user"),"","0");
        getListTournament(sess.getString("id_user"),"","0",listFilterGame);
        getListNews(sess.getString("id_user"),"","0");
        getListGameFromDB();

        // Inflate the layout for this fragment
        return root;
    }

    private void getListGameFromDB(){
        // below line is use to get all the game from view modal.
        viewModel.getAllGame().observe(requireActivity(), new Observer<List<DataItem>>() {
            @Override
            public void onChanged(List<DataItem> models) {
                // when the data is changed in our models we are
                // adding that list to our adapter class.
                //listGameAdapter.submitList(models);
                listGameAdapter = new ListGameAdapter(getContext(),models);
                rlGame.setLayoutManager(new GridLayoutManager(getContext(),1,GridLayoutManager.HORIZONTAL,false));
                rlGame.setAdapter(listGameAdapter);
            }
        });

    }

    private void getListNews(String userId,String search,String page){

        setLoad(true);

        try {
            Call<GetListNewsResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getListNews(userId, search, page);
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
            Call<GetListTournamentResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getListTournament(userId, search, page,filterGame);
            req.enqueue(new Callback<GetListTournamentResponseModel>() {
                @Override
                public void onResponse(Call<GetListTournamentResponseModel> call, Response<GetListTournamentResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            listTournament = response.body().getData();

                            rlTournament.setAdapter(new ListTournamentAdapter(getActivity(),listTournament,"home"));
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

        setLoad(true);
        try {
            Call<ResponseListGame> req = RetrofitConfig.getApiServices(sess.getString("token")).getListGame(userId, search, page);
            req.enqueue(new Callback<ResponseListGame>() {
                @Override
                public void onResponse(Call<ResponseListGame> call, Response<ResponseListGame> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            viewModel.deleteAllGame();
                            listGames.clear();
                            listGames = response.body().getData();

                            for(int i = 0; i < response.body().getData().size(); i++){
                                DataItem di = new DataItem(
                                        response.body().getData().get(i).getImage(),
                                        response.body().getData().get(i).getUpdatedAt(),
                                        response.body().getData().get(i).getCreatedAt(),
                                        response.body().getData().get(i).getId(),
                                        response.body().getData().get(i).getTitle()
                                );
                                viewModel.insert(di);
                            }
                            /*listGameAdapter = new ListGameAdapter(getContext(),listGames);
                            rlGame.setLayoutManager(new GridLayoutManager(getContext(),1,GridLayoutManager.HORIZONTAL,false));
                            rlGame.setAdapter(listGameAdapter);*/


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
            Call<GetListMenuResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getListMenuHome(userId);
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
                        Toast.makeText(getActivity(), "Failed Request List Menu", Toast.LENGTH_SHORT).show();

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
            Call<GetCarouselResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getCarouselTournament(userId);
            req.enqueue(new Callback<GetCarouselResponseModel>() {
                @Override
                public void onResponse(Call<GetCarouselResponseModel> call, Response<GetCarouselResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            ArrayList<SlideModel> imageList = new ArrayList<SlideModel>();

                            for (int i = 0; i < response.body().getData().size() ; i++) {
                                if (response.body().getData().get(i).getImage() != null){
                                    imageList.add(new SlideModel(response.body().getData().get(i).getImage(),
                                            response.body().getData().get(i).getTitleTournament(), ScaleTypes.CENTER_CROP));
                                }else {
                                    imageList.add(new SlideModel(R.drawable.img_not_found,
                                            response.body().getData().get(i).getTitleTournament(), ScaleTypes.CENTER_CROP));
                                }

                            }
                            posterSlider.setImageList(imageList);
                            //Item Click Listener Slider
                            posterSlider.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void onItemSelected(int i) {
                                    Intent intent = new Intent(getActivity(), DetailTournamentActivity.class);
                                    Bundle bun = new Bundle();
                                    bun.putString("id_tournament", response.body().getData().get(i).getId());
                                    bun.putString("judul_game", response.body().getData().get(i).getTitleTournament());
                                    intent.putExtras(bun);
                                    startActivity(intent);
                                }
                            });


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

    @Override
    public void onResume() {
        super.onResume();
        getListTournament(sess.getString("id_user"),"","0",listFilterGame);
    }
}