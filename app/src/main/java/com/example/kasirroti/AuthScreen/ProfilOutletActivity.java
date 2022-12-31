package com.example.kasirroti.AuthScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.Adapter.AdapterUlasan;
import com.example.kasirroti.BuildConfig;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.Model.ListUlasan;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfilOutletActivity extends AppCompatActivity {

    private String nama, no, alamat, nama_rek, id_outlet, logo, no_rek;
    private TextView txt_profil_nama, txt_profil_alamat, txt_profil_notelp, txt_profil_norek, txt_profil_namarek, txt_rata_rata, txt_total_ulasan, txt_semua_ulasan, txt_kosong, txt_noversi;
    private ImageView img_logo_profil;
    private RatingBar ratingBar;
    private LinearLayout linear_ulasan;
    private SwipeRefreshLayout swipe_profil;

    private RecyclerView recyclerUlasan;
    private ArrayList<ListUlasan> listUlasan;

    private ShimmerFrameLayout shimmer_ulasan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_outlet);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profil);
        toolbar.setTitle("Profil Outlet");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent                   = getIntent();
        nama                            = intent.getStringExtra("nama_outlet");
        no                              = intent.getStringExtra("no_telp");
        alamat                          = intent.getStringExtra("alamat");
        id_outlet                       = intent.getStringExtra("id_outlet");
        logo                            = intent.getStringExtra("logo");
        nama_rek                        = intent.getStringExtra("nama_rek");
        no_rek                          = intent.getStringExtra("no_rek");

        txt_profil_nama                 = findViewById(R.id.txt_profil_nama);
        txt_profil_alamat               = findViewById(R.id.txt_profil_alamat);
        txt_profil_notelp               = findViewById(R.id.txt_profil_notelp);
        txt_profil_norek                = findViewById(R.id.txt_profil_norek);
        img_logo_profil                 = findViewById(R.id.img_logo_profil);
        txt_profil_namarek              = findViewById(R.id.txt_profil_namarek);
        txt_rata_rata                   = findViewById(R.id.txt_rata_rata);
        txt_total_ulasan                = findViewById(R.id.txt_total_ulasan);
        txt_semua_ulasan                = findViewById(R.id.txt_semua_ulasan);
        txt_kosong                      = findViewById(R.id.txt_kosong);
        txt_noversi                     = findViewById(R.id.txt_noversi);

        ratingBar                       = findViewById(R.id.ratingBar);

        shimmer_ulasan                  = findViewById(R.id.shimmer_ulasan);

        swipe_profil                    = findViewById(R.id.swipe_profil);

        linear_ulasan                   = findViewById(R.id.linear_ulasan);

        listUlasan                      = new ArrayList<>();
        recyclerUlasan                  = findViewById(R.id.recyclerUlasan);
        recyclerUlasan.setLayoutManager(new LinearLayoutManager(this));
        recyclerUlasan.setItemAnimator(new DefaultItemAnimator());
        recyclerUlasan.setHasFixedSize(true);

        txt_profil_nama.setText(nama);
        txt_profil_alamat.setText(alamat);
        txt_profil_notelp.setText(no);
        txt_profil_norek.setText(no_rek);
        txt_profil_namarek.setText(nama_rek);
        txt_noversi.setText("Versi " + BuildConfig.VERSION_NAME);

        txt_semua_ulasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        swipe_profil.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showUlasan();
                swipe_profil.setRefreshing(false);
            }
        });

        showUlasan();
    }

    public void showUlasan(){
        listUlasan.clear();
        recyclerUlasan.setAdapter(null);
        shimmer_ulasan.setVisibility(View.VISIBLE);
        linear_ulasan.setVisibility(View.GONE);
        AndroidNetworking.post(Server.URL2 + "rating_outlet")
                .addBodyParameter("id_outlet", HomeActivity.id_outlet)
                .setPriority(Priority.MEDIUM)
                .setTag("ulasan")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("200")){
                                shimmer_ulasan.setVisibility(View.GONE);
                                if (response.getString("jml_ulasan").equals("0")){
                                    txt_kosong.setVisibility(View.VISIBLE);
                                }
                                else{
                                    JSONArray jsonArray     = response.getJSONArray("ulasan");

                                    txt_total_ulasan.setText(response.getString("jml_ulasan") + " ulasan");
                                    txt_rata_rata.setText(response.getString("bintang"));
                                    if (response.getString("bintang").equals("0.0")){
                                        ratingBar.setRating(0);
                                    }else {
                                        float bintang = Float.parseFloat(response.getString("bintang").substring(0, 1));
                                        ratingBar.setRating(bintang);
                                    }

                                    for (int i = 0; i < jsonArray.length(); i++){
                                        JSONObject object   = jsonArray.getJSONObject(i);
                                        String ulasan;
                                        if (object.getString("saran").equals("null")){
                                            ulasan          = "";
                                        }
                                        else {
                                            ulasan          = object.getString("saran");
                                        }
                                        ListUlasan data     = new ListUlasan(
                                                object.getString("nama_user"),
                                                object.getString("tgl_ulasan"),
                                                object.getString("bintang"),
                                                ulasan
                                        );

                                        listUlasan.add(data);
                                    }

                                    AdapterUlasan adapter   = new AdapterUlasan(ProfilOutletActivity.this, listUlasan);
                                    adapter.notifyDataSetChanged();
                                    recyclerUlasan.setAdapter(adapter);
                                    linear_ulasan.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            shimmer_ulasan.setVisibility(View.GONE);
                            Log.d("Ulasan", e.getMessage());
                            FancyToast.makeText(ProfilOutletActivity.this, "Gagal Memuat Ulasan, kesalahan request!",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Ulasan", anError.getMessage());
                        shimmer_ulasan.setVisibility(View.GONE);
                        FancyToast.makeText(ProfilOutletActivity.this, "Gagal Memuat Ulasan, periksa koneksi internet!",
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}