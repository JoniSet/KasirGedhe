package com.example.kasirroti.RedeemScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.Adapter.AdapterKategoriPoin;
import com.example.kasirroti.Adapter.AdapterProdukPoin;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.Model.ListKategori;
import com.example.kasirroti.Model.ListProduk;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PoinActivity extends AppCompatActivity {
    RecyclerView list_kategori, list_produk;
    TextView txt_kategori;
    ProgressBar loading;

    ArrayList<ListKategori> listKategori    = new ArrayList<>();
    ArrayList<ListProduk> listProduk        = new ArrayList<>();

    int pos     = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poin);

        Log.d("token", HomeActivity.token);

        initView();
    }

    private void initView() {
        list_kategori   = findViewById(R.id.list_kategori);
        list_produk     = findViewById(R.id.list_produk);
        loading         = findViewById(R.id.loading);
        txt_kategori    = findViewById(R.id.txt_kategori);

        setKategori();
    }

    private void setKategori() {
        AndroidNetworking.get(Server.URL + "kategori-redeem")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res_kode     = response.getString("response");
                            String message      = response.getString("message");

                            if (res_kode.equals("200")) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                listKategori.add(new ListKategori(
                                        "",
                                        "Semua",
                                        "true"
                                ));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    ListKategori data   = new ListKategori(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("nama_kategori"),
                                        "false"
                                    );

                                    listKategori.add(data);
                                }

                                AdapterKategoriPoin adapterKategoriPoin     = new AdapterKategoriPoin(PoinActivity.this, listKategori);
                                list_kategori.setAdapter(adapterKategoriPoin);
                                list_kategori.setLayoutManager(new LinearLayoutManager(PoinActivity.this, LinearLayoutManager.HORIZONTAL, false));
                                list_kategori.setItemAnimator(new DefaultItemAnimator());
                                list_kategori.setHasFixedSize(true);
                                adapterKategoriPoin.setOnItemClickListener(new AdapterKategoriPoin.recyclerViewClickListener() {
                                    @Override
                                    public void onClick(View v, int position, TextView textView) {
                                        pos         = position;
                                        loading.setVisibility(View.VISIBLE);

                                        for (int i = 0; i < listKategori.size(); i++){
                                            listKategori.get(i).setDi_klik("false");
                                        }
                                        listKategori.get(position).setDi_klik("true");
                                        adapterKategoriPoin.notifyItemChanged(position);
                                        adapterKategoriPoin.notifyDataSetChanged();

                                        txt_kategori.setText(listKategori.get(position).getNama_kategori());
                                        int index;
                                        if (listKategori.get(position).getId_kategori().equals("")){
                                            index = 0;
                                        }
                                        else {
                                            index = Integer.parseInt(listKategori.get(position).getId_kategori());
                                        }

                                        if (position + 1 == index){
                                            textView.setBackgroundResource(R.drawable.bg_btn_login);
                                            textView.setTextColor(Color.WHITE);
                                        }
                                        else
                                        {
                                            textView.setBackgroundResource(R.drawable.bg_layout);
                                            textView.setTextColor(Color.DKGRAY);
                                        }

                                        setProduk(listKategori.get(position).getId_kategori());
                                    }
                                });

                                setProduk("");
                            }
                            else {
                                FancyToast.makeText(PoinActivity.this,  message,
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                loading.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(PoinActivity.this,  e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            loading.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Hasil",anError.toString());
                        if (anError.getErrorCode() == 401){
                            FancyToast.makeText(PoinActivity.this,  "Sesi anda telah berakhir, silahkan login kembali!",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            loading.setVisibility(View.GONE);
                            HomeActivity.sm.logout();
                            HomeActivity.sessionManager.storeLogin(
                                    HomeActivity.id_outlet,
                                    HomeActivity.nama,
                                    HomeActivity.notelp,
                                    HomeActivity.alamat,
                                    HomeActivity.logo,
                                    HomeActivity.nama_rek,
                                    HomeActivity.no_rek, "0");
                            finish();
                        }
                        else{
                            FancyToast.makeText(PoinActivity.this,  anError.toString(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            loading.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void setProduk(String id_kategori) {
        listProduk.clear();
        list_produk.setAdapter(null);
        AndroidNetworking.post(Server.URL + "produk-redeem")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addBodyParameter("id_kategori", id_kategori)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res_kode     = response.getString("response");
                            String message      = response.getString("message");

                            if (res_kode.equals("200")) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject   = jsonArray.getJSONObject(i);
                                    ListProduk data         = new ListProduk(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("id_kategori"),
                                            jsonObject.getString("nama_produk"),
                                            jsonObject.getString("poin"),
                                            jsonObject.getString("img"),
                                            ""
                                    );

                                    listProduk.add(data);
                                }

                                AdapterProdukPoin adapterProdukPoin     = new AdapterProdukPoin(PoinActivity.this, listProduk);
                                list_produk.setAdapter(adapterProdukPoin);
                                list_produk.setLayoutManager(new LinearLayoutManager(PoinActivity.this));
                                list_produk.setItemAnimator(new DefaultItemAnimator());
                                list_produk.setHasFixedSize(true);

                                adapterProdukPoin.adapterPoinClickListener(new AdapterProdukPoin.AdapterProdukPoinOnClick() {
                                    @Override
                                    public void onClick(View v, int position) {
                                        Intent intent       = new Intent(PoinActivity.this, RedeemActivity.class);
                                        intent.putExtra("id", listProduk.get(position).getId());
                                        intent.putExtra("id_kategori", listProduk.get(position).getId_kategori());
                                        intent.putExtra("nama_produk", listProduk.get(position).getNama_produk());
                                        intent.putExtra("poin", listProduk.get(position).getHarga());
                                        intent.putExtra("img", listProduk.get(position).getImg());
                                        startActivity(intent);
                                    }
                                });

                                loading.setVisibility(View.GONE);
                            }
                            else{
                                FancyToast.makeText(PoinActivity.this,  message,
                                        FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                                loading.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(PoinActivity.this,  e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            loading.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Hasil", anError.toString());
                        if (anError.getErrorCode() == 401){
                            FancyToast.makeText(PoinActivity.this,  "Sesi anda telah berakhir, silahkan login kembali!",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            loading.setVisibility(View.GONE);
                            HomeActivity.sm.logout();
                            HomeActivity.sessionManager.storeLogin(
                                    HomeActivity.id_outlet,
                                    HomeActivity.nama,
                                    HomeActivity.notelp,
                                    HomeActivity.alamat,
                                    HomeActivity.logo,
                                    HomeActivity.nama_rek,
                                    HomeActivity.no_rek, "0");
                            finish();
                        }
                        else{
                            FancyToast.makeText(PoinActivity.this,  anError.toString(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            loading.setVisibility(View.GONE);
                        }
                    }
                });
    }


}