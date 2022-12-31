package com.example.kasirroti.TransaksiScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.Adapter.AdapterProdukDiStruk;
import com.example.kasirroti.Model.ListInputPesan;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.example.kasirroti.Helper.Tanggal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StrukActivity extends AppCompatActivity {

    private String token, nama_outlet, alamat_outlet, notelp_outlet, nama_kasir, bayar, uang, nama, metode, no_antri, kode_transaksi;
    private TextView txt_alamat_outlet, txt_notelp_outlet, txt_nama_struk, txt_nama_outlet, txt_tanggal, txt_waktu
            , txt_no_antri, txt_metode_struk, txt_total_b, txt_total_c, txt_kembalian_struk, txt_order_id, txt_judul;

    private Tanggal tanggal;

    private LinearLayout L_17;
    private ProgressBar loading_struk;

    RecyclerView listStruk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_struk);

        Intent intent           = getIntent();
        nama                    = intent.getStringExtra("nama");
        uang                    = intent.getStringExtra("uang");
        bayar                   = intent.getStringExtra("bayar");
        metode                  = intent.getStringExtra("metode");
        nama_kasir              = intent.getStringExtra("kasir");
        notelp_outlet           = intent.getStringExtra("notelp_outlet");
        alamat_outlet           = intent.getStringExtra("alamat_outlet");
        nama_outlet             = intent.getStringExtra("outlet");
        no_antri                = intent.getStringExtra("no_antri");
        kode_transaksi          = intent.getStringExtra("kode_transaksi");
        token                   = intent.getStringExtra("token");

        tanggal                 = new Tanggal();

        txt_alamat_outlet       = findViewById(R.id.txt_alamat_outlet);
        txt_notelp_outlet       = findViewById(R.id.txt_notelp_outlet);
        txt_nama_struk          = findViewById(R.id.txt_nama_struk);
        txt_nama_outlet         = findViewById(R.id.txt_nama_outlet);
        txt_tanggal             = findViewById(R.id.txt_tanggal);
        txt_waktu               = findViewById(R.id.txt_waktu);
        txt_no_antri            = findViewById(R.id.txt_no_antri);
        txt_metode_struk        = findViewById(R.id.txt_metode_struk);
        txt_total_b             = findViewById(R.id.txt_total_b);
        txt_total_c             = findViewById(R.id.txt_total_c);
        txt_kembalian_struk     = findViewById(R.id.txt_kembalian_struk);
        loading_struk           = findViewById(R.id.loading_struk);
        L_17                    = findViewById(R.id.L_17);
        txt_order_id            = findViewById(R.id.txt_order_id);
        txt_judul               = findViewById(R.id.txt_judul);

        listStruk               = findViewById(R.id.list_produk_struk);
        listStruk.setHasFixedSize(true);
        listStruk.setLayoutManager(new LinearLayoutManager(this));
        listStruk.setItemAnimator(new DefaultItemAnimator());


        txt_alamat_outlet.setText(alamat_outlet);
        txt_notelp_outlet.setText(notelp_outlet);
        txt_nama_struk.setText(nama_kasir);
        txt_nama_outlet.setText(nama_outlet);
        txt_judul.setText(nama_outlet);
        txt_tanggal.setText(tanggal.getTanggal1());
        txt_waktu.setText(tanggal.getTime());
        txt_no_antri.setText(no_antri);
        txt_metode_struk.setText("Pembayaran (" + metode + ")");
        txt_total_b.setText("Rp. " + bayar );
        txt_total_c.setText("Rp. " + uang );
        txt_kembalian_struk.setText("Rp. " + String.valueOf(Integer.parseInt(uang) - Integer.parseInt(bayar)));

        set_transaksi();

    }


    public void set_transaksi(){
        AndroidNetworking.post(Server.URL + "nota_transaksi")
                .addHeaders("Authorization", "Bearer " + token)
                .addBodyParameter("id_transaksi", kode_transaksi)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res_kode     = response.getString("response");
                            String message      = response.getString("response");
                            String object       = response.getString("data");

                            if (res_kode.equals("200"))
                            {
                                JSONObject data = new JSONObject(object);

                                txt_order_id.setText(data.getString("order_id"));

                                set_nota_produk();
                            }
                            else
                            {
                                Toast.makeText(StrukActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(StrukActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        L_17.setVisibility(View.VISIBLE);
                        loading_struk.setVisibility(View.GONE);
                        Toast.makeText(StrukActivity.this, anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void set_nota_produk(){
        AndroidNetworking.post(Server.URL + "nota_produk")
                .addHeaders("Authorization", "Bearer " + token)
                .addBodyParameter("id_transaksi", kode_transaksi)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        L_17.setVisibility(View.VISIBLE);
                        loading_struk.setVisibility(View.GONE);

                        try {
                            if (response.getString("response").equals("200"))
                            {
                                JSONArray array             = response.getJSONArray("data");

                                ArrayList<ListInputPesan> listItem  = new ArrayList<>();

                                for (int i = 0; i < array.length(); i++)
                                {
                                    JSONObject object       = array.getJSONObject(i);

                                    ListInputPesan list     = new ListInputPesan(
                                            object.getString("nama_produk"),
                                            object.getString("jumlah"),
                                            object.getString("harga_satuan")
                                    );

                                    listItem.add(list);

                                    AdapterProdukDiStruk adapter    = new AdapterProdukDiStruk(StrukActivity.this, listItem);
                                    adapter.notifyDataSetChanged();

                                    listStruk.setAdapter(adapter);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        L_17.setVisibility(View.VISIBLE);
                        loading_struk.setVisibility(View.GONE);
                        Toast.makeText(StrukActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
