package com.example.myprayerschedule;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myprayerschedule.model.Items;
import com.example.myprayerschedule.model.Jadwal;
import com.example.myprayerschedule.rest.ApiClient;
import com.example.myprayerschedule.rest.ApiInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tvSubuh)
    TextView tvSubuh;
    @BindView(R.id.tvZuhur)
    TextView tvZuhur;
    @BindView(R.id.tvAshar)
    TextView tvAshar;
    @BindView(R.id.tvMagrhib)
    TextView tvMaghrib;
    @BindView(R.id.tvIsya)
    TextView tvIsya;
    @BindView(R.id.tvLokasi)
    TextView tvLokasi;
    @BindView(R.id.tvTanggal)
    TextView tvTanggal;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.blue),
                getResources().getColor(R.color.purple),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.orange));
        refreshLayout.setOnRefreshListener(() -> {
            resetData();
            actionLoadData();
            refreshLayout.setRefreshing(false);
        });
        actionLoadData();
    }

    void actionLoadData() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Items> call = apiService.getJadwalSholat("cirebon");
        call.enqueue(new Callback<Items>() {
            @Override
            public void onResponse(Call<Items> call, Response<Items> response) {
                List<Jadwal> jadwal = response.body().getItems();
                String lokasi = new StringBuilder()
                        .append("Cirebon, ")
                        .append(response.body().getState())
                        .append(", ")
                        .append(response.body().getCountry())
                        .toString();
                try {
                    loadData(jadwal, lokasi);
                } catch (ParseException e) {
                    Log.d(this.getClass().getSimpleName(),"ParseException: "+e);
                }
            }

            @Override
            public void onFailure(Call<Items> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Tidak dapat memuat data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void loadData(List<Jadwal> jadwal, String lokasi) throws ParseException {
        for (Jadwal data : jadwal) {
            tvSubuh.setText(data.getSubuh());
            tvZuhur.setText(data.getZuhur());
            tvAshar.setText(data.getAshar());
            tvMaghrib.setText(data.getMaghrib());
            tvIsya.setText(data.getIsya());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date mDate = formatter.parse(data.getTanggal());
            formatter = new SimpleDateFormat("dd MMM yyyy");
            String strDate = formatter.format(mDate);
            tvTanggal.setText(strDate);
            tvLokasi.setText(lokasi);
        }
    }
    private void resetData(){
        tvSubuh.setText("");
        tvZuhur.setText("");
        tvAshar.setText("");
        tvMaghrib.setText("");
        tvIsya.setText("");
        tvTanggal.setText("");
        tvLokasi.setText("");
    }
}
