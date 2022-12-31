package com.example.kasirroti.TransaksiScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.Adapter.AdapterPesanTest;
import com.example.kasirroti.DataSqlite.BahanOutletSqlite;
import com.example.kasirroti.DataSqlite.BahanProdukSqlite;
import com.example.kasirroti.Helper.SingOut;
import com.example.kasirroti.Helper.SqliteHelper;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.Model.ListGridPesanan;
import com.example.kasirroti.Model.ListStokRoti;
import com.example.kasirroti.Model.ListStokRotiP;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.example.kasirroti.StokScreen.ManajemenStokActivity;
import com.example.kasirroti.Helper.Tanggal;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CekOutActivity extends AppCompatActivity {
    TextView T_total_bayar;
    int bayar;

    public static Activity cekOut;

    public static RecyclerView recyclerView;
    private Button button;
    private Dialog dialog2;

    public static ArrayList<ListGridPesanan> item;
    public static ListGridPesanan deleted_item;

    private String listString       = "";

    SqliteHelper sqliteHelper;

    ArrayList<BahanOutletSqlite> bhn_outlet     = new ArrayList<>();
    ArrayList<Object> jml_bahan     = new ArrayList<>();
    ArrayList<String> list;
    ArrayList<Integer> hitung        = new ArrayList<>();
    AdapterPesanTest adapter;
    JSONArray abc;

    public static String id_bahan, dipakai, tidak_cukup;
    boolean stok                    = true;

    HashMap<String, String> map;
    int count;
    int sum, sum2, k;
    int total_bayar = 0;

    Tanggal tanggal                 = new Tanggal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek_out);

        Toolbar toolbar     = findViewById(R.id.toolbar_cekout);
        toolbar.setTitle("Check Out Pesanan");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sqliteHelper        = new SqliteHelper(this);

        dialog2                 = new Dialog(CekOutActivity.this);
        dialog2.setContentView(R.layout.loading);
        dialog2.show();
        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog2.setCancelable(false);

        TextView txt_title          = dialog2.findViewById(R.id.txt_title);
        txt_title.setText("Cek Stok Bahan");

        T_total_bayar           = findViewById(R.id.T_total_bayar);

        Bundle bundle           = getIntent().getExtras();
        item                    = (ArrayList<ListGridPesanan>) bundle.getSerializable("pesan");

        for (int i = 0; i < item.size(); i++)
        {
//            if (item.get(i).getJumlah().equals("0")) {
//                item.remove(i);
//            }
            sum                 += Integer.parseInt(item.get(i).getTotal());
        }

        total_bayar             = sum;
        T_total_bayar.setText(tanggal.formatRupiah(Double.parseDouble(String.valueOf(sum))));

        cekOut                  = this;
        recyclerView            = findViewById(R.id.list_pesanan);
        adapter                 = new AdapterPesanTest(this, item);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new AdapterPesanTest.recyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                final Dialog dialog                           = new Dialog(CekOutActivity.this);
                dialog.setContentView(R.layout.dialog_edit_pesanan);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();
                hitung.clear();

                ImageView img_c             = dialog.findViewById(R.id.img_c);
                ImageView img_produk_plus   = dialog.findViewById(R.id.img_produk_plus);
                ImageView img_produk_min    = dialog.findViewById(R.id.img_produk_min);
                TextView txt_nama_produk    = dialog.findViewById(R.id.txt_nama_produk);
                EditText txt_jumlah_produk  = dialog.findViewById(R.id.txt_jumlah_produk);

                Button btn_simpan1          = dialog.findViewById(R.id.btn_simpan1);

                txt_nama_produk.setText(item.get(position).getNama_produk());
                txt_jumlah_produk.setText(item.get(position).getJumlah());

                Picasso.get()
                        .load(item.get(position).getImg())
                        .into(img_c);

                int jumlah_produk_awal      = Integer.parseInt(txt_jumlah_produk.getText().toString());

                img_produk_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String jumlah   = txt_jumlah_produk.getText().toString();
                        int jlh         = 0;
                        if (jumlah.isEmpty()){
                            jlh         = 0;
                        }else{
                            jlh         = Integer.parseInt(jumlah);
                        }
                        int jumlah_final= jlh + 1;

                        txt_jumlah_produk.setText(String.valueOf(jumlah_final));

                        img_produk_min.setVisibility(View.VISIBLE);
                        img_produk_plus.setVisibility(View.VISIBLE);

                    }
                });

                img_produk_min.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String jumlah   = txt_jumlah_produk.getText().toString();
                        int jlh         = 0;
                        if (jumlah.isEmpty()){
                            jlh         = 0;
                        }else{
                            jlh         = Integer.parseInt(jumlah);
                        }
                        int jumlah_final= jlh - 1;

                        if (jumlah_final == 0)
                        {
                            img_produk_min.setVisibility(View.GONE);
                            txt_jumlah_produk.setText(String.valueOf(jumlah_final));
                        }
                        else {
                            txt_jumlah_produk.setText(String.valueOf(jumlah_final));

                            img_produk_min.setVisibility(View.VISIBLE);
                            img_produk_plus.setVisibility(View.VISIBLE);
                        }
                    }
                });

                btn_simpan1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        adapter.notifyDataSetChanged();

                        if (item.get(position).getJumlah().equals(txt_jumlah_produk.getText().toString())){
                            for (int i = 0; i < list.size(); i++)
                            {
                                if (!sqliteHelper.readAllBahanOutletSqlite().isEmpty()) {
                                    cek_stokSqlite(list.get(i), String.valueOf(map.get(list.get(i))));
                                }
                                else {
                                    cek_stok(list.get(i), String.valueOf(map.get(list.get(i))));
                                }
                            }
                        }
                        else if (txt_jumlah_produk.getText().toString().equals("0")){
                            HomeActivity.txt_total_pesanan.setText(
                                    String.valueOf(
                                            Integer.parseInt(HomeActivity.txt_total_pesanan.getText().toString())
                                                    -
                                                    Integer.parseInt(item.get(position).getJumlah())
                                    )
                            );
                            String asd              = String.valueOf(total_bayar - jumlah_produk_awal * Integer.parseInt(item.get(position).getHarga_satuan()));
                            Gson gson               = new Gson();
                            String oke              = gson.toJson(
                                    item.get(position).getBahan(),
                                    new TypeToken<ArrayList<ListGridPesanan>>() {}.getType());
                            item.remove(position);

                            T_total_bayar.setText(tanggal.formatRupiah(Double.parseDouble(asd)));
                            total_bayar             = Integer.parseInt(asd);

                            adapter.notifyDataSetChanged();
                            adapter.notifyItemRemoved(position);

                            String nama_bahan ="", dipakai = "";

                            try {
                                JSONArray jsonArray     = new JSONArray(oke);
                                for (int i = 0; i < 1; i++){
                                    JSONObject object   = jsonArray.getJSONObject(0);
                                    JSONArray array     = object.getJSONArray("listStokRoti");
                                    for (int a = 0; a < array.length(); a++){
                                        JSONObject ob   = array.getJSONObject(a);
                                        nama_bahan   = ob.getString("id_bahan");
                                        dipakai      = ob.getString("dipakai");

                                        kurangi_bahan(nama_bahan, String.valueOf(jumlah_produk_awal), txt_jumlah_produk.getText().toString());
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                            dialog2.dismiss();

                            list      = new ArrayList<String>(map.keySet());

                            for (int i = 0; i < list.size(); i++){
                                if(String.valueOf(item.size()).equals("0")){
                                    startActivity(new Intent(CekOutActivity.this, HomeActivity.class));
                                    finish();
                                }
                            }


                            map.clear();
                            jml_bahan.clear();
                            hitung.clear();
                            get_total_bahan();
                        }
                        else{
                            int jumlah_produk_akhir     = Integer.parseInt(txt_jumlah_produk.getText().toString());
                            int b       = jumlah_produk_awal;
                            int jml     = 0;
                            if (jumlah_produk_akhir < jumlah_produk_awal){
                                jml     = jumlah_produk_awal - jumlah_produk_akhir;
                            }
                            else {
                                jml     = jumlah_produk_akhir - jumlah_produk_awal;
                            }

                            item.get(position).setJumlah(txt_jumlah_produk.getText().toString());

                            int rubah_harga = jml * Integer.parseInt(item.get(position).getHarga_satuan());
                            String asd;
                            if (jumlah_produk_akhir < jumlah_produk_awal){
                                asd     = String.valueOf(total_bayar - rubah_harga);
                                T_total_bayar.setText(tanggal.formatRupiah(Double.parseDouble(asd)));
                                total_bayar             = Integer.parseInt(asd);

                                HomeActivity.txt_total_pesanan.setText(
                                        String.valueOf(
                                                Integer.parseInt(HomeActivity.txt_total_pesanan.getText().toString())
                                                        - jml
                                        )
                                );

                                Gson gson               = new Gson();
                                String oke              = gson.toJson(
                                        item.get(position).getBahan(),
                                        new TypeToken<ArrayList<ListGridPesanan>>() {}.getType());

                                String nama_bahan ="", dipakai = "";

                                try {
                                    JSONArray jsonArray     = new JSONArray(oke);
                                    JSONObject object       = jsonArray.getJSONObject(0);
                                    JSONArray array         = object.getJSONArray("listStokRoti");
                                    for (int a = 0; a < array.length(); a++)
                                    {
                                        JSONObject ob   = array.getJSONObject(a);

                                        nama_bahan   = ob.getString("id_bahan");
                                        dipakai      = ob.getString("dipakai");

                                        for (int i = 0; i < jml; i++) {
                                            kurangi_bahan(nama_bahan, dipakai, txt_jumlah_produk.getText().toString());
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                item.get(position).setTotal(String.valueOf(
                                        Integer.parseInt(txt_jumlah_produk.getText().toString())
                                                * Integer.parseInt(item.get(position).getHarga_satuan())
                                ));
                                for (int k = 0; k < jml; k++) {
                                    item.get(position).getBahan().remove(0);
                                }

                                map.clear();
                                jml_bahan.clear();
                                hitung.clear();
                                get_total_bahan();
                            }
                            else {
                                asd     = String.valueOf(total_bayar + rubah_harga);
                                T_total_bayar.setText(tanggal.formatRupiah(Double.parseDouble(asd)));
                                total_bayar             = Integer.parseInt(asd);
                                item.get(position).setTotal(
                                        String.valueOf(
                                                Integer.parseInt(txt_jumlah_produk.getText().toString()) *
                                                        Integer.parseInt(item.get(position).getHarga_satuan())
                                        )
                                );

                                HomeActivity.txt_total_pesanan.setText(
                                        String.valueOf(
                                                Integer.parseInt(HomeActivity.txt_total_pesanan.getText().toString())
                                                        + jml
                                        )
                                );

                                ArrayList<BahanProdukSqlite> bahanProdukSqlite  = new ArrayList<>();
                                bahanProdukSqlite                               = sqliteHelper.readAllBahanProdukSqlite();
                                for (int i = 0; i < jml; i++){
                                    if (bahanProdukSqlite.size() != 0){
                                        tambah_banyak_produk_sqlite(item.get(position).getId_produk(), position, item.get(position).getBahan(), txt_jumlah_produk.getText().toString());
                                    }
                                    else {
                                        tambah_banyak_produk(item.get(position).getId_produk(), position, item.get(position).getBahan(), txt_jumlah_produk.getText().toString());
                                    }
                                }
                            }

                            int jk = 0;
                            for (int i = 0; i < item.size(); i++)
                            {
                                jk                += Integer.parseInt(item.get(i).getTotal());
                            }
                        }
                    }
                });
            }
        });
        adapter.notifyDataSetChanged();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        button              = findViewById(R.id.button);
        button.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < hitung.size(); i++){
                    k  += hitung.get(i);
                }
                if (k == 0)
                {
                    Gson gson = new Gson();
                    String jsonList = gson.toJson(item);
                    Log.d("list", map.toString());

                    Intent a        = new Intent(CekOutActivity.this, PembayaranActivity.class);
                    Bundle bundle   = new Bundle();
                    bundle.putSerializable("pesan", item);
                    a.putExtras(bundle);
                    a.putExtra("total", String.valueOf(total_bayar));
                    a.putExtra("map", map);
                    startActivity(a);
                }
                else
                {
                    Gson gson = new Gson();
                    String jsonList = gson.toJson(item);
//                    Log.d("list", jsonList);
                    FancyToast.makeText(CekOutActivity.this, tidak_cukup, FancyToast.LENGTH_SHORT,
                            FancyToast.WARNING, false
                    ).show();
                }
            }
        });

        ItemTouchHelper itemTouchHelper     = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        get_total_bahan();
    }

    public void tambah_banyak_produk(String id_produk, int position, ArrayList<ListStokRotiP> listBahan, String jumlah){
        Dialog dialog               = new Dialog(CekOutActivity.this);
        dialog.setContentView(R.layout.loading);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        ArrayList<ListStokRoti> listStokRoti        = new ArrayList<>();

        AndroidNetworking.post(Server.URL + "komposisi_produk")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addHeaders("Accept", "Bearer " + HomeActivity.token)
                .addBodyParameter("id_produk", id_produk)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("200")){
                                dialog.dismiss();

                                JSONArray array         = response.getJSONArray("data");

                                for (int i = 0; i < array.length(); i++){
                                    JSONObject object   = array.getJSONObject(i);

                                    String id_bahan     = object.getString("id_bahan");
                                    String dipakai      = object.getString("terpakai");
                                    String nama         = object.getString("nama_bahan");


                                    ListStokRoti data1  = new ListStokRoti(
                                            id_bahan,
                                            nama,
                                            dipakai
                                    );
                                    listStokRoti.add(data1);

                                    tambahi_bahan(id_bahan, dipakai, jumlah, dialog);
//                                    if (!sqliteHelper.readAllBahanOutletSqlite().isEmpty()) {
//                                        cek_stokSqlite(id_bahan, jumlah);
//                                    }
//                                    else {
//                                        cek_stok(id_bahan, jumlah);
//                                    }
                                }

                                ListStokRotiP stokRotiParent        = new ListStokRotiP(
                                        id_produk,
                                        listStokRoti
                                );

                                listBahan.add(stokRotiParent);

                                item.get(position).setBahan(listBahan);

                                map.clear();
                                jml_bahan.clear();
                                hitung.clear();
                                get_total_bahan();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut singOut     = new SingOut();
                            if (anError.getErrorCode() == 401){
                                singOut.Logout(CekOutActivity.this);
                            }
                            else {
                                dialog.dismiss();
                            }
                        }
                        else {
                            dialog.dismiss();
                        }
                    }
                });
    }

    public void tambah_banyak_produk_sqlite(String id_produk, int position, ArrayList<ListStokRotiP> listBahan, String jumlah){
        Dialog dialog               = new Dialog(CekOutActivity.this);
        dialog.setContentView(R.layout.loading);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        ArrayList<ListStokRoti> listStokRoti        = new ArrayList<>();
        ArrayList<BahanProdukSqlite> listBahanProduk= new ArrayList<>();
        listBahanProduk                             = sqliteHelper.readBahanProdukSqlite(id_produk);
        for (int i = 0; i < listBahanProduk.size(); i++){
            String id_bhn       = listBahanProduk.get(i).getId_bahan();
            String nm_bhn       = listBahanProduk.get(i).getNama_bahan();
            String dpk          = listBahanProduk.get(i).getTerpakai();

            ListStokRoti data1  = new ListStokRoti(
                    id_bhn,
                    nm_bhn,
                    dpk
            );
            listStokRoti.add(data1);

            tambahi_bahan(listBahanProduk.get(i).getId_bahan(), listBahanProduk.get(i).getTerpakai(), jumlah, dialog);
        }
        ListStokRotiP stokRotiParent        = new ListStokRotiP(
                id_produk,
                listStokRoti
        );

        listBahan.add(stokRotiParent);

        item.get(position).setBahan(listBahan);

        map.clear();
        jml_bahan.clear();
        hitung.clear();
        get_total_bahan();
    }

    public void get_total_bahan(){
        map                     = new HashMap<>();
        Gson gson               = new Gson();
        listString              = gson.toJson(
                item,
                new TypeToken<ArrayList<ListGridPesanan>>() {}.getType());

        try {
            JSONArray jsonArray     = new JSONArray(listString);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject   = jsonArray.getJSONObject(i);
                JSONArray array         = jsonObject.getJSONArray("bahan");

                for (int a = 0; a < array.length(); a++){
                    JSONObject object       = array.getJSONObject(a);
                    JSONArray array_bahan   = object.getJSONArray("listStokRoti");

                    for (int e = 0; e < array_bahan.length(); e++){
                        jml_bahan.add(array_bahan.get(e));
                    }

                }
            }

            abc             = new JSONArray(jml_bahan);

            for (int jml = 0; jml < abc.length(); jml++)
            {
                JSONObject jsonObject  = abc.getJSONObject(jml);

                id_bahan                = jsonObject.getString("id_bahan");
                dipakai                 = jsonObject.getString("dipakai");

                if (dipakai.equals("0")){
                    map.put(id_bahan, "0");
                }
                else if(map.containsKey(id_bahan)){
                    count               = Integer.parseInt(map.get(id_bahan)) + 1;
                    map.put(id_bahan, String.valueOf(count));
                }
                else {
                    map.put(id_bahan, dipakai);
                }
            }
            Log.d("DataPes", map.toString());

            list      = new ArrayList<String>(map.keySet());

            for (int i = 0; i < list.size(); i++)
            {
                if (!sqliteHelper.readAllBahanOutletSqlite().isEmpty()) {
                    cek_stokSqlite(list.get(i), String.valueOf(map.get(list.get(i))));
                }
                else {
                    cek_stok(list.get(i), String.valueOf(map.get(list.get(i))));
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void kurangi_bahan(String id_bahan, String tambah, String jumlah){
        if(map.containsKey(id_bahan)){
            count               = Integer.parseInt(map.get(id_bahan)) - Integer.parseInt(tambah);
            map.put(id_bahan, String.valueOf(count));

        }else {
            map.put(id_bahan, dipakai);

        }
//        if (!sqliteHelper.readAllBahanOutletSqlite().isEmpty()) {
//            cek_stokSqlite(id_bahan, jumlah);
//        }
//        else {
//            cek_stok(id_bahan, jumlah);
//        }

        Log.d("DataPesan", String.valueOf(map));
    }

    public void tambahi_bahan(String id_bahan, String tambah, String jumlah, Dialog dialog){
        if(map.containsKey(id_bahan)){
            count               = Integer.parseInt(map.get(id_bahan)) + Integer.parseInt(tambah);
            map.put(id_bahan, String.valueOf(count));
//            cek_stokSqlite(id_bahan, jumlah);
        }else {
            map.put(id_bahan, dipakai);
//            cek_stokSqlite(id_bahan, jumlah);
        }
        dialog.dismiss();
        Log.d("DataPesan", String.valueOf(map));
    }

    public void cek_stok(String id_bahan, String jumlah){
//        hitung.clear();
        dialog2.show();
        AndroidNetworking.post(Server.URL + "stok_bahan")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addBodyParameter("id_outlet", HomeActivity.id_outlet)
                .addBodyParameter("id_bahan", id_bahan)
                .setPriority(Priority.MEDIUM)
                .setTag("cek")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("200")){
                                if (Integer.parseInt(jumlah) > Integer.parseInt(response.getString("stok")))
                                {
                                    hitung.add(1);
                                    tidak_cukup = "Stok " + response.getString("nama_bahan") +" tidak cukup";
                                    FancyToast.makeText(CekOutActivity.this, tidak_cukup, FancyToast.LENGTH_SHORT,
                                            FancyToast.WARNING, false
                                    ).show();
                                    dialog2.dismiss();
                                    button.setVisibility(View.GONE);
                                }
                                else {
                                    button.setVisibility(View.VISIBLE);
                                    dialog2.dismiss();
                                    Log.d("list", map.toString());
                                    hitung.add(0);
                                }
                            }
                            else{
                                dialog2.dismiss();
                                FancyToast.makeText(CekOutActivity.this,
                                        response.getString("message"),
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.INFO,
                                        false)
                                        .show();
                                AndroidNetworking.cancel("cek");
                                startActivity(new Intent(CekOutActivity.this, HomeActivity.class));
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            AndroidNetworking.cancel("cek");
                            FancyToast.makeText(CekOutActivity.this, tidak_cukup, FancyToast.LENGTH_SHORT,
                                    FancyToast.ERROR, false
                            ).show();
                            dialog2.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        String errorCode           = anError.getMessage();
                        Log.d("Coba",errorCode);
                        startActivity(new Intent(CekOutActivity.this, HomeActivity.class));
                        finish();
                        FancyToast.makeText(CekOutActivity.this, "Jaringan bermasalah", FancyToast.LENGTH_SHORT,
                                FancyToast.ERROR, false
                        ).show();

                        dialog2.dismiss();
                    }
                });
    }

    public void cek_stokSqlite(String id_bahan, String jumlah){
//        hitung.clear();
        Log.d("CekData", id_bahan);
        dialog2.show();
        bhn_outlet          = sqliteHelper.readBahanOutletSqlite(id_bahan);

        Gson gson               = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(bhn_outlet).getAsJsonArray();

        Log.d("bahan_kosong", myCustomArray.toString());

        if (bhn_outlet.isEmpty()){
            FancyToast.makeText(CekOutActivity.this, "Terdapat bahan yang belum terdaftar\nMohon isi stok bahan anda!", FancyToast.LENGTH_LONG,
                    FancyToast.ERROR, false
            ).show();
            Intent intent   = new Intent(CekOutActivity.this, ManajemenStokActivity.class);
            intent.putExtra("tipe", "kosong");
            startActivity(intent);
            finish();
        }
        else{
            for (int i = 0; i < bhn_outlet.size(); i++){
                if (Integer.parseInt(jumlah) > Integer.parseInt(bhn_outlet.get(i).getStok()))
                {
                    hitung.add(1);
                    button.setVisibility(View.GONE);
                    dialog2.dismiss();
                    tidak_cukup = "Stok " + bhn_outlet.get(i).getNama_bahan() +" tidak cukup";
                    FancyToast.makeText(CekOutActivity.this, tidak_cukup, FancyToast.LENGTH_SHORT,
                            FancyToast.WARNING, false
                    ).show();
                }
                else {
                    button.setVisibility(View.VISIBLE);
                    dialog2.dismiss();
                    Log.d("list", map.toString());
                    hitung.add(0);
                }
            }
        }

    }

    ItemTouchHelper.SimpleCallback simpleCallback   = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            list.clear();
            sum2                = 0;

            int position        = viewHolder.getAdapterPosition();
            String item_terhapus= item.get(position).getNama_produk();
            String total_terhapus= item.get(position).getTotal();
            String jml_terhapus = item.get(position).getJumlah();
            deleted_item        = item.get(position);
            item.remove(position);
            adapter.notifyItemRemoved(position);

            HomeActivity.txt_total_pesanan.setText(
                    String.valueOf(
                            Integer.parseInt(HomeActivity.txt_total_pesanan.getText().toString())
                                    - Integer.parseInt(jml_terhapus)
                    )
            );

            for (int i = 0; i < item.size(); i++)
            {
                sum2                += Integer.parseInt(item.get(i).getTotal());
            }
            T_total_bayar.setText(tanggal.formatRupiah(Double.parseDouble(String.valueOf(sum2))));
            total_bayar             = sum2;
            Log.d("total11", String.valueOf(sum2));

            Snackbar.make(recyclerView, item_terhapus, Snackbar.LENGTH_LONG)
                    .setAction("Batal", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            item.add(position, deleted_item);
                            adapter.notifyItemInserted(position);
                            T_total_bayar.setText(tanggal.formatRupiah(Double.parseDouble(String.valueOf(sum2 + Integer.parseInt(total_terhapus)))));
                            total_bayar = sum2 + Integer.parseInt(total_terhapus);
                            jml_bahan.clear();
                            get_total_bahan();

                            HomeActivity.txt_total_pesanan.setText(
                                    String.valueOf(
                                            Integer.parseInt(HomeActivity.txt_total_pesanan.getText().toString())
                                                    + Integer.parseInt(jml_terhapus)
                                    )
                            );
                        }
                    }).show();

            map.clear();
            jml_bahan.clear();
            get_total_bahan();

            if (item.size() == 0){
                finish();
                item.clear();
                HomeActivity.act.recreate();
                hitung.clear();
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(CekOutActivity.this, R.color.putih))
                    .addActionIcon(R.drawable.delete)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        for (int i = 0; i < item.size(); i++){
            HomeActivity.listGridPesanan.add(item.get(i));
        }
        item.clear();
        hitung.clear();
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        for (int i = 0; i < item.size(); i++){
            HomeActivity.listGridPesanan.add(item.get(i));
        }
        Log.d("item", item.toString());
        item.clear();
        hitung.clear();
        finish();
    }
}