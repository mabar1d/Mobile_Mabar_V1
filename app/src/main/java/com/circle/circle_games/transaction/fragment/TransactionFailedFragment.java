package com.circle.circle_games.transaction.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.circle.circle_games.R;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.transaction.adapter.ListTransactionSuccessAdapter;
import com.circle.circle_games.transaction.model.GetListTransactionResponseModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionFailedFragment extends Fragment {

    private SessionUser sess;
    private GlobalMethod globalMethod;
    public Context context;

    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.shimmer_load)
    ShimmerFrameLayout shimmerLoad;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.search_bar_transaction)
    EditText sbTransaction;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.rv_transaction)
    RecyclerView rvTransaction;

    List<GetListTransactionResponseModel.Data> listTransaction = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sess = new SessionUser(getActivity());
        globalMethod = new GlobalMethod();
        context = getContext();

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transaction_failed, container, false);
        ButterKnife.bind(this, rootView);

        getListTransaction("");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListTransaction(sbTransaction.getText().toString());
            }
        });

        sbTransaction.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    getListTransaction(sbTransaction.getText().toString());
                    return true;
                }
                return false;
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                getListTransaction(sbTransaction.getText().toString());
            }
        });

        return rootView;
    }

    private void getListTransaction(String search){

        globalMethod.setShimmerLinearLayout(true,shimmerLoad,llContent);

        try {
            Call<GetListTransactionResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getListTransactions(sess.getString("id_user"), "202", search ,"0");
            req.enqueue(new Callback<GetListTransactionResponseModel>() {
                @Override
                public void onResponse(Call<GetListTransactionResponseModel> call, Response<GetListTransactionResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            listTransaction.clear();
                            listTransaction = response.body().getData();

                            rvTransaction.setAdapter(new ListTransactionSuccessAdapter(getActivity(), "failed", listTransaction, new ListTransactionSuccessAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(GetListTransactionResponseModel.Data item, int position) {

                                }
                            }));
                            rvTransaction.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                            sess.clearSess();
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }else {
                            String notif = response.body().getDesc();
                            Toast.makeText(getContext(), notif, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String desc = "Failed get list transaction";
                        Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();

                    }
                    globalMethod.setShimmerLinearLayout(false,shimmerLoad,llContent);
                }

                @Override
                public void onFailure(Call<GetListTransactionResponseModel> call, Throwable t) {
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