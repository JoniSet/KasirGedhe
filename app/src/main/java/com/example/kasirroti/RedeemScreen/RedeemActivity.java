package com.example.kasirroti.RedeemScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.Adapter.AdapterListCustomer;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.Model.DataCustomer;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.example.kasirroti.TransaksiScreen.PembayaranActivity;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RedeemActivity extends AppCompatActivity {
    private RecyclerView recycler_data_pelanggan;
    private ImageView img_back, img_produk;
    private TextView txt_nama_produk, txt_poin, txt_poin_cst, txt_poin_produk, txt_poin_sisa, txt_nama_produk_detail, txt_nama_cust;
    private EditText edt_cari_wa;
    private Button btn_cari_customer;
    private ProgressBar progress;
    private LinearLayout linear_detail;

    ArrayList<DataCustomer> customers   = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);

        initView();
    }

    private void initView() {
        img_back            = findViewById(R.id.img_back);
        img_produk          = findViewById(R.id.img_produk);
        txt_nama_produk     = findViewById(R.id.txt_nama_produk);
        txt_poin            = findViewById(R.id.txt_poin);
        edt_cari_wa         = findViewById(R.id.edt_cari_wa);
        btn_cari_customer   = findViewById(R.id.btn_cari_customer);
        progress            = findViewById(R.id.progress);
        txt_poin_sisa       = findViewById(R.id.txt_poin_sisa);
        txt_poin_produk     = findViewById(R.id.txt_poin_produk);
        txt_poin_cst        = findViewById(R.id.txt_poin_cst);
        linear_detail       = findViewById(R.id.linear_detail);
        txt_nama_produk_detail       = findViewById(R.id.txt_nama_produk_detail);
        txt_nama_cust       = findViewById(R.id.txt_nama_cust);

        txt_nama_produk.setText(getIntent().getStringExtra("nama_produk"));
        txt_poin.setText(getIntent().getStringExtra("poin"));

        recycler_data_pelanggan = findViewById(R.id.recycler_data_pelanggan);
        recycler_data_pelanggan.setLayoutManager(new LinearLayoutManager(RedeemActivity.this));
        recycler_data_pelanggan.setItemAnimator(new DefaultItemAnimator());
        recycler_data_pelanggan.setHasFixedSize(true);

        setButton();
    }

    private void setPelanggan(String nohp) {
        progress.setVisibility(View.VISIBLE);
        recycler_data_pelanggan.setAdapter(null);
        customers.clear();
        linear_detail.setVisibility(View.GONE);
        recycler_data_pelanggan.setVisibility(View.GONE);
        AndroidNetworking.post(Server.URL + "search_customer")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addBodyParameter("nohp", nohp)
                .setPriority(Priority.MEDIUM)
                .setTag("search_customer")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progress.setVisibility(View.GONE);
                        try {
                            String res_kode     = response.getString("response");
                            String message      = response.getString("message");
                            Log.d("Hasil", response.toString());

                            if (res_kode.equals("200")){
                                recycler_data_pelanggan.setVisibility(View.VISIBLE);
                                JSONArray data      = response.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++){
                                    JSONObject obj  = data.getJSONObject(i);
                                    DataCustomer dataCust = new DataCustomer(
                                            obj.getString("id"),
                                            obj.getString("name"),
                                            obj.getString("nohp"),
                                            "",
                                            obj.getString("poin")
                                    );
                                    customers.add(dataCust);
                                }
                                AdapterListCustomer adapter     = new AdapterListCustomer(RedeemActivity.this, customers);
                                recycler_data_pelanggan.setLayoutManager(new LinearLayoutManager(RedeemActivity.this));
                                recycler_data_pelanggan.setItemAnimator(new DefaultItemAnimator());
                                recycler_data_pelanggan.setAdapter(adapter);
                                adapter.setOnItemClickListener(new AdapterListCustomer.recyclerViewClickListener() {
                                    @Override
                                    public void onClick(View v, int position) {
                                        linear_detail.setVisibility(View.VISIBLE);
                                        recycler_data_pelanggan.setVisibility(View.GONE);
                                        txt_poin_cst.setText(customers.get(position).getPoin());
                                        txt_nama_cust.setText("Poin " + customers.get(position).getNama() + " :");
//                                        id_customer = customers.get(position).getId();
                                        txt_nama_produk_detail.setText(getIntent().getStringExtra("nama_produk") + " :");
                                        txt_poin_produk.setText(getIntent().getStringExtra("poin"));

                                        int sisa_poin   = Integer.parseInt(txt_poin_cst.getText().toString()) -
                                                Integer.parseInt(txt_poin_produk.getText().toString());
                                        if (sisa_poin < 0){
                                            txt_poin_sisa.setText("Poin tidak cukup");
                                        }
                                        else {
                                            txt_poin_sisa.setText(String.valueOf(sisa_poin));
                                        }
                                    }
                                });
                            }
                            else if (res_kode.equals("201")){
                                FancyToast.makeText(RedeemActivity.this, message, FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                                progress.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(RedeemActivity.this, e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            progress.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Hasil", anError.getMessage());
                        if (anError.getErrorCode() == 401){
                            FancyToast.makeText(RedeemActivity.this,  "Sesi anda telah berakhir, silahkan login kembali!",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            progress.setVisibility(View.GONE);
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
                            FancyToast.makeText(RedeemActivity.this, anError.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            progress.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void setButton() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_cari_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_cari_wa.getText().toString().isEmpty()){
                    FancyToast.makeText(RedeemActivity.this, "Mohon masukkan nomor wa customer!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
                else {
                    setPelanggan(edt_cari_wa.getText().toString());
                }
            }
        });
    }
}