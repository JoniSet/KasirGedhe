package com.example.kasirroti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.Adapter.AdapterKategori;
import com.example.kasirroti.Adapter.AdapterProduk;
import com.example.kasirroti.AuthScreen.FotoTutupActivity;
import com.example.kasirroti.AuthScreen.GantiPasswordActivity;
import com.example.kasirroti.AuthScreen.ProfilOutletActivity;
import com.example.kasirroti.AuthScreen.SecondLoginActivity;
import com.example.kasirroti.DataSqlite.BahanProdukSqlite;
import com.example.kasirroti.DataSqlite.KategoriSqlite;
import com.example.kasirroti.DataSqlite.ProdukSqlite;
import com.example.kasirroti.Helper.SingOut;
import com.example.kasirroti.Helper.SqliteHelper;
import com.example.kasirroti.Helper.Tanggal;
import com.example.kasirroti.LaporanScreen.LaporanActivity;
import com.example.kasirroti.Model.ListGridPesanan;
import com.example.kasirroti.Model.ListKategori;
import com.example.kasirroti.Model.ListProduk;
import com.example.kasirroti.Model.ListStokRoti;
import com.example.kasirroti.Model.ListStokRotiP;
import com.example.kasirroti.RedeemScreen.PoinActivity;
import com.example.kasirroti.Server.Server;
import com.example.kasirroti.SessionManager.SessionManager;
import com.example.kasirroti.SessionManager.SessionManagerUser;
import com.example.kasirroti.StokScreen.ManajemenStokActivity;
import com.example.kasirroti.TransaksiScreen.CekOutActivity;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

public class HomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    public static String token, id_outlet, nama, alamat, notelp, logo, nama_rek, no_rek, last_klik;

    public static SessionManagerUser sm;
    public static SessionManager sessionManager;

    private RecyclerView recyclerView, recyclerView_list_produk;

    private TextView txt_kategori, txt_outlet, txt_posisi, txt_nama;
    public static TextView txt_total_pesanan, txt_lanjut;
    private ProgressBar loading1;
    public static ConstraintLayout R_lanjut;
    public static FloatingActionButton fab_hapus;
    private ImageView tutorial;

    ArrayList<ListKategori> listKategori;
    public static ArrayList<ListProduk> listProduk;
    public static ArrayList<ListGridPesanan> listGridPesanan;
    ArrayList<ListStokRotiP> listBahan      = new ArrayList<>();
    //    ArrayList<ArrayList<ListStokRotiP>> cek_total_pesanan     = new ArrayList<ArrayList<ListStokRotiP>>();
    ListKategori data;
    ListStokRotiP stokRotiParent;
    public  static AdapterProduk adapter;

    AdapterKategori adapterKategori;

    int index   = 0;
    int pos     = 0;

    public static HashMap<String, String> mappp = new HashMap<>();
    public static String status      = "0";
    public static HomeActivity act;

    public static String tampil_nama;

    CountDownLatch latch            = new CountDownLatch(1);
    FancyToast toast;

    //SQLite
    public static SqliteHelper sqliteHelper;
    private ArrayList<KategoriSqlite> kategoriSqlite;
    private ArrayList<ProdukSqlite> produkSqlite;
    private ArrayList<BahanProdukSqlite> bahanProdukSqlite;

    FloatingActionButton img_sort;
    String sort         = "default";

    private Tanggal tanggal     = new Tanggal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sales Transaction");
        setSupportActionBar(toolbar);

        act                         = this;

        AndroidNetworking.initialize(this);

        sessionManager              = new SessionManager(this);
        HashMap<String, String> map1= sessionManager.getDetailLogin();
        id_outlet                   = map1.get(sessionManager.KEY_ID);
        nama                        = map1.get(sessionManager.NAMA);
        alamat                      = map1.get(sessionManager.ALAMAT);
        notelp                      = map1.get(sessionManager.TELP);
        logo                        = map1.get(sessionManager.LOGO);
        nama_rek                    = map1.get(sessionManager.NAMA_REK);
        no_rek                      = map1.get(sessionManager.NO_REK);
        last_klik                   = map1.get(sessionManager.KLIK_KAEGORI);

        sm                          = new SessionManagerUser(this);
        HashMap<String, String> map = sm.getDetailLogin();
        token                       = map.get(sm.KEY_TOKEN);

        toast                       = new FancyToast(this);

        //sqlite
        sqliteHelper                = new SqliteHelper(HomeActivity.this);
        kategoriSqlite              = new ArrayList<>();
        produkSqlite                = new ArrayList<>();
        kategoriSqlite              = sqliteHelper.readKategoriSqlite();

        //get token

        if (token == null)
        {
            token                   = "kosong";
        }


        if (token.equals("kosong"))
        {
            startActivity(new Intent(HomeActivity.this, SecondLoginActivity.class));
            finish();
        }
        else
        {
            cekVersi();
            token                   = map.get(sm.KEY_TOKEN);

            String[] dua_kata   = map.get(sm.KEY_NAME).trim().split(" ");
            if (dua_kata.length >= 2){
                tampil_nama  = dua_kata[0] + " " + dua_kata[1];
            }
            else{
                tampil_nama  = map.get(sm.KEY_NAME);
            }
        }

        loading1                    = findViewById(R.id.loading1);
        txt_kategori                = findViewById(R.id.txt_kategori);
        txt_total_pesanan           = findViewById(R.id.txt_total_pesanan);
        R_lanjut                    = findViewById(R.id.R_lanjut);
        fab_hapus                   = findViewById(R.id.fab_hapus);
        tutorial                    = findViewById(R.id.tutorial);
        img_sort                    = findViewById(R.id.img_sort);

        //Setting navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view                   = navigationView.getHeaderView(0);
        txt_nama                    = view.findViewById(R.id.txt_nama);
        txt_outlet                  = view.findViewById(R.id.txt_outlet);
        txt_posisi                  = view.findViewById(R.id.txt_posisi);
//        TextView txt_posisi         = view.findViewById(R.id.txt_posisi);


        txt_nama.setText(tampil_nama);
        txt_outlet.setText(nama);
        txt_posisi.setText(" - " + map.get(sm.KEY_JABATAN));

        txt_outlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent       = new Intent(HomeActivity.this, ProfilOutletActivity.class);
                intent.putExtra("id_outlet", id_outlet);
                intent.putExtra("nama_outlet", nama);
                intent.putExtra("alamat", alamat);
                intent.putExtra("no_telp", notelp);
                intent.putExtra("logo", logo);
                intent.putExtra("nama_rek", nama_rek);
                intent.putExtra("no_rek", no_rek);
                startActivity(intent);
            }
        });


        //initializing
        txt_kategori                = findViewById(R.id.txt_kategori);
        loading1                    = findViewById(R.id.loading1);
        txt_lanjut                  = findViewById(R.id.txt_lanjut);

        recyclerView                = findViewById(R.id.list_kategori);
        recyclerView_list_produk    = findViewById(R.id.list_produk);
        listKategori                = new ArrayList<>();
        listProduk                  = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new AdapterKategori(this, listKategori));

        recyclerView_list_produk.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_list_produk.setItemAnimator(new DefaultItemAnimator());
        recyclerView_list_produk.setHasFixedSize(true);
        recyclerView_list_produk.setAdapter(new AdapterProduk(this, listProduk));
        recyclerView_list_produk.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d("-----","end");

                }
            }
        });

        listGridPesanan             = new ArrayList<>();

        R_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listGridPesanan.size() == 0)
                {
                    FancyToast.makeText(HomeActivity.this,  "Anda Belum Memiliki Produk!",
                            FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }
                else {
                    if (status.equals("0"))
                    {
                        toast.cancel();
                        fab_hapus.setVisibility(View.VISIBLE);
                        for (int i = 0; i < listGridPesanan.size(); i++){
                            if (listGridPesanan.get(i).getJumlah().equals("0")){
                                listGridPesanan.remove(i);
                            }
                        }
                        if (!sqliteHelper.readAllBahanProdukSqlite().isEmpty()) {
                            get_bahanSqlite();
                        }
                        else {
                            get_bahan();
                        }
                        status                  = "1";
                    }
                    else {
                        Gson gson               = new GsonBuilder().create();
                        JsonArray myCustomArray = gson.toJsonTree(listGridPesanan).getAsJsonArray();

                        for (int i = 0; i < listGridPesanan.size(); i++){
                            if (listGridPesanan.get(0).getBahan().isEmpty()){
                                toast.makeText(HomeActivity.this,
                                        "Sedang Menyelesaikan Proses!",
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.INFO,
                                        false)
                                        .show();
                            }
                            else {
                                Log.d("list", String.valueOf(myCustomArray) + "\n" + token);
                                Intent intent           = new Intent(HomeActivity.this, CekOutActivity.class);
                                Bundle bundle           = new Bundle();
                                bundle.putSerializable("pesan", listGridPesanan);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                R_lanjut.setBackgroundResource(R.color.colorAccent);
                                listGridPesanan.clear();
                            }
                        }

                    }
                }
            }
        });

        fab_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_hapus.setVisibility(View.GONE);
                loading1.setVisibility(View.VISIBLE);
                recyclerView_list_produk.setVisibility(View.GONE);

                listBahan.clear();
                listGridPesanan.clear();
//                setProduk(listKategori.get(index).getId_kategori());
                setData();

                mappp.clear();
                txt_total_pesanan.setText("0");
                txt_kategori.setText("Reguler");

                status          = "0";
                txt_lanjut.setText("Lanjut");
                R_lanjut.setBackgroundResource(R.color.colorAccent);
            }
        });

        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tutorial.setEnabled(false);
                recyclerView_list_produk.scrollToPosition(0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        show_tutorial();
                        tutorial.setEnabled(true);
                    }
                }, 200);
            }
        });

        img_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listProduk.size() != 0){
                    if (kategoriSqlite.isEmpty()) {
                        loading1.setVisibility(View.VISIBLE);
                        if (sort.equals("default")) {
                            sort    = "a";
                            setProduk(last_klik, sort);
                        }
                        else if (sort.equals("a")) {
                            sort    = "z";
                            setProduk(last_klik, sort);
                        }
                        else if (sort.equals("z")) {
                            sort    = "default";
                            setProduk(last_klik, sort);
                        }
                    }
                    else {
                        if (sort.equals("default")) {
                            sort    = "a";
                            setProdukSqlite(last_klik, sort);
                        }
                        else if (sort.equals("a")) {
                            sort    = "z";
                            setProdukSqlite(last_klik, sort);
                        }
                        else if (sort.equals("z")) {
                            sort    = "default";
                            setProdukSqlite(last_klik, sort);
                        }
                    }
                }
            }
        });
        setData();
        mappp.clear();
        Log.d("token", token + "\n" + id_outlet);

    }

    public void setData(){
        if (!kategoriSqlite.isEmpty()) {
            setKategoriSqlite();
        }
        else {
            setKategori();
        }
    }

    private void setKategori() {
        listKategori.clear();
        recyclerView.setAdapter(null);
        AndroidNetworking.post(Server.URL + "kategori")
                .addHeaders("Authorization", "Bearer " + token)
                .addHeaders("Accept", "application/json")
                .addBodyParameter("id_outlet", id_outlet)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        recyclerView.setVisibility(View.VISIBLE);
                        try {
                            String message = response.getString("message");
                            String responseCode = response.getString("response");

                            if (responseCode.equals("200")) {
                                JSONArray jsonArray         = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject   = jsonArray.getJSONObject(i);

                                    data                    = new ListKategori(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("nama_kategori"),
                                            "false"
                                    );
                                    listKategori.add(data);
                                    listKategori.get(0).setDi_klik("true");

                                    adapterKategori     = new AdapterKategori(HomeActivity.this, listKategori);
                                    adapterKategori.setOnItemClickListener(new AdapterKategori.recyclerViewClickListener() {
                                        @Override
                                        public void onClick(View v, int position, TextView textView) {
                                            pos         = position;
                                            loading1.setVisibility(View.VISIBLE);
                                            sessionManager.updatePoin(listKategori.get(position).getId_kategori());
                                            HashMap<String, String> map = sessionManager.getDetailLogin();
                                            last_klik                   = map.get(sessionManager.KLIK_KAEGORI);

                                            for (int i = 0; i < listKategori.size(); i++){
                                                listKategori.get(i).setDi_klik("false");
                                            }
                                            listKategori.get(position).setDi_klik("true");
                                            adapterKategori.notifyItemChanged(position);
                                            adapterKategori.notifyDataSetChanged();
;
                                            txt_kategori.setText(listKategori.get(position).getNama_kategori());
                                            if (position + 1 == Integer.parseInt(listKategori.get(position).getId_kategori())){
                                                textView.setBackgroundResource(R.drawable.bg_btn_login);
                                                textView.setTextColor(Color.WHITE);
                                            }
                                            else
                                            {
                                                textView.setBackgroundResource(R.drawable.bg_layout);
                                                textView.setTextColor(Color.DKGRAY);
                                            }

                                            setProduk(listKategori.get(position).getId_kategori(), "default");
                                        }
                                    });

                                    adapterKategori.notifyDataSetChanged();
                                    recyclerView.setAdapter(adapterKategori);

                                }

                                setProduk(listKategori.get(index).getId_kategori(), "default");

                            } else {
                                FancyToast.makeText(HomeActivity.this,  message,
                                        FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            recyclerView.setVisibility(View.VISIBLE);
                            loading1.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Kategori", anError.getMessage());
//                        Toast.makeText(HomeActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        if (anError.getErrorCode() == 401){
                            sm.logout();
                            sessionManager.storeLogin(id_outlet, nama, notelp, alamat, logo, nama_rek, no_rek, "0");
                            finish();
                        }
                        else{
                            recyclerView.setVisibility(View.VISIBLE);
                            loading1.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public void setProduk(String position, String sort)
    {
        listProduk.clear();
        recyclerView_list_produk.setAdapter(null);
        AndroidNetworking.post(Server.URL + "produk")
                .addHeaders("Authorization", "Bearer " + token)
                .addHeaders("Accept", "application/json")
                .addBodyParameter("id_kategori", position)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        loading1.setVisibility(View.GONE);

                        try {
                            if (response.getString("response").equals("200"))
                            {
                                JSONArray array             = response.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++)
                                {
                                    JSONObject object       = array.getJSONObject(i);
                                    ListProduk data         = new ListProduk(
                                            object.getString("id"),
                                            object.getString("id_kategori"),
                                            object.getString("nama_produk"),
                                            object.getString("harga"),
                                            object.getString("img"),
                                            "0"
                                    );

                                    listProduk.add(data);

                                    adapter   = new AdapterProduk(HomeActivity.this, listProduk);
                                    adapter.notifyDataSetChanged();
                                    adapter.setHasStableIds(true);
                                    recyclerView_list_produk.setAdapter(adapter);
                                    recyclerView_list_produk.setVisibility(View.VISIBLE);
                                }
                                if (sort.equals("a")) {
                                    Collections.sort(listProduk, new Comparator<ListProduk>() {
                                        @Override
                                        public int compare(ListProduk lhs, ListProduk rhs) {
                                            adapter.notifyDataSetChanged();
                                            return lhs.getNama_produk().compareTo(rhs.getNama_produk());
                                        }
                                    });
                                }
                                else if (sort.equals("z")) {
                                    Collections.sort(listProduk, new Comparator<ListProduk>() {
                                        @Override
                                        public int compare(ListProduk lhs, ListProduk rhs) {
                                            adapter.notifyDataSetChanged();
                                            return rhs.getNama_produk().compareTo(lhs.getNama_produk());
                                        }
                                    });
                                }
                                else if (sort.equals("default")) {
                                    Collections.sort(listProduk, new Comparator<ListProduk>() {
                                        @Override
                                        public int compare(ListProduk lhs, ListProduk rhs) {
                                            adapter.notifyDataSetChanged();
                                            return lhs.getId().compareTo(rhs.getId());
                                        }
                                    });
                                }
                                cek_sinkron_pertama();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(HomeActivity.this,  "Terjadi Kesalahan Request!",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            sm.logout();
                            sessionManager.storeLogin(id_outlet, nama, notelp, alamat, logo, nama_rek, no_rek, "0");
                            finish();
                        }
                        else {
                            FancyToast.makeText(HomeActivity.this, "Terjadi Kesalahn Request!",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }
                });
    }

    //sqlite
    private void setKategoriSqlite() {
        listKategori.clear();
        for (int i = 0; i < kategoriSqlite.size(); i++){
            data                    = new ListKategori(
                    kategoriSqlite.get(i).getId(),
                    kategoriSqlite.get(i).getNama_kategori(),
                    "false"
            );
            listKategori.add(data);
            listKategori.get(0).setDi_klik("true");
        }

        adapterKategori     = new AdapterKategori(HomeActivity.this, listKategori);
        adapterKategori.setOnItemClickListener(new AdapterKategori.recyclerViewClickListener() {
            @Override
            public void onClick(View v, int position, TextView textView) {
                pos         = position;

                loading1.setVisibility(View.VISIBLE);
                txt_kategori.setText(listKategori.get(position).getNama_kategori());

                sessionManager.updatePoin(listKategori.get(position).getId_kategori());
                HashMap<String, String> map = sessionManager.getDetailLogin();
                last_klik                   = map.get(sessionManager.KLIK_KAEGORI);

                for (int i = 0; i < listKategori.size(); i++){
                    listKategori.get(i).setDi_klik("false");
                }
                listKategori.get(position).setDi_klik("true");
                adapterKategori.notifyItemChanged(position);
                adapterKategori.notifyDataSetChanged();

                if (position + 1 == Integer.parseInt(listKategori.get(position).getId_kategori())){
                    textView.setBackgroundResource(R.drawable.bg_btn_login);
                    textView.setTextColor(Color.WHITE);
                }
                else
                {
                    textView.setBackgroundResource(R.drawable.bg_layout);
                    textView.setTextColor(Color.DKGRAY);
                }

                setProdukSqlite(listKategori.get(position).getId_kategori(), "default");

                Gson gson               = new GsonBuilder().create();
                JsonArray myCustomArray = gson.toJsonTree(listKategori).getAsJsonArray();

                Log.d("Salah", myCustomArray.toString());
            }
        });

        adapterKategori.notifyDataSetChanged();
        recyclerView.setAdapter(adapterKategori);

        setProdukSqlite(listKategori.get(index).getId_kategori(), "default");
    }

    public void setProdukSqlite(String position, String sort)
    {
        loading1.setVisibility(View.GONE);
        listProduk.clear();
        recyclerView_list_produk.setAdapter(null);
        produkSqlite                = sqliteHelper.readProdukSqlite(position);

        for (int i = 0; i < produkSqlite.size(); i++)
        {
            ListProduk data         = new ListProduk(
                    produkSqlite.get(i).getId(),
                    produkSqlite.get(i).getId_kategori(),
                    produkSqlite.get(i).getNama_produk(),
                    produkSqlite.get(i).getHarga(),
                    produkSqlite.get(i).getImg(),
                    "0"
            );

            listProduk.add(data);

            adapter   = new AdapterProduk(HomeActivity.this, listProduk);
            adapter.notifyDataSetChanged();
            adapter.setHasStableIds(true);
            recyclerView_list_produk.setAdapter(adapter);
            recyclerView_list_produk.setVisibility(View.VISIBLE);

            if (sort.equals("a")) {
                Collections.sort(listProduk, new Comparator<ListProduk>() {
                    @Override
                    public int compare(ListProduk lhs, ListProduk rhs) {
                        adapter.notifyDataSetChanged();
                        return lhs.getNama_produk().compareTo(rhs.getNama_produk());
                    }
                });
            }
            else if (sort.equals("z")) {
                Collections.sort(listProduk, new Comparator<ListProduk>() {
                    @Override
                    public int compare(ListProduk lhs, ListProduk rhs) {
                        adapter.notifyDataSetChanged();
                        return rhs.getNama_produk().compareTo(lhs.getNama_produk());
                    }
                });
            }
            else if (sort.equals("default")) {
                Collections.sort(listProduk, new Comparator<ListProduk>() {
                    @Override
                    public int compare(ListProduk lhs, ListProduk rhs) {
                        adapter.notifyDataSetChanged();
                        return lhs.getId().compareTo(rhs.getId());
                    }
                });
            }
        }
        cekWaktuSinkron();
    }

    public void cekWaktuSinkron(){
        int waktu_sekarang      = Integer.parseInt(tanggal.getTime().replace(":", ""));
        int waktu_sinkron       = Integer.parseInt(sqliteHelper.readAllInfoSqlite("1").substring(12).replace(":", ""));
        Log.d("Cek", String.valueOf(waktu_sekarang - waktu_sinkron));

        if (waktu_sekarang - waktu_sinkron > 10000){
            cek_sinkron();
        }
    }

    public void get_bahanSqlite(){
        Dialog dialog               = new Dialog(HomeActivity.this);
        dialog.show();
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        TextView txt_title          = dialog.findViewById(R.id.txt_title);
        txt_title.setText("Mengecek Bahan");

        for (int l = 0; l < listGridPesanan.size(); l++) {
            listBahan.clear();
            String id_produkk = listGridPesanan.get(l).getId_produk();
            ArrayList<ListStokRoti> listStokRoti = new ArrayList<>();

            bahanProdukSqlite = sqliteHelper.readBahanProdukSqlite(id_produkk);
            for (int i = 0; i < bahanProdukSqlite.size(); i++) {
                ListStokRoti data1 = new ListStokRoti(
                        bahanProdukSqlite.get(i).getId_bahan(),
                        bahanProdukSqlite.get(i).getNama_bahan(),
                        bahanProdukSqlite.get(i).getTerpakai()
                );

                listStokRoti.add(data1);
            }

            stokRotiParent    = new ListStokRotiP(
                    id_produkk,
                    listStokRoti
            );

            listBahan.add(stokRotiParent);
            for (int i = 0; i < listBahan.size(); i++){
                set_pesanan(dialog, listBahan.get(i).getId_produk(), i);
            }
        }

    }


    //============================================setting pesanan===================================

    public void get_bahan(){
        Dialog dialog               = new Dialog(HomeActivity.this);
        dialog.show();
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        TextView txt_title          = dialog.findViewById(R.id.txt_title);
        txt_title.setText("Mengecek Bahan");

        for (int l = 0; l < listGridPesanan.size(); l++)
        {
            String id_produkk              = listGridPesanan.get(l).getId_produk();
            listBahan.clear();
            ArrayList<ListStokRoti> listStokRoti        = new ArrayList<>();

            AndroidNetworking.post(Server.URL + "komposisi_produk")
                    .addHeaders("Authorization", "Bearer " + token)
                    .addHeaders("Accept", "application/json")
                    .addBodyParameter("id_produk", listGridPesanan.get(l).getId_produk())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("response").equals("200")){
                                    JSONArray array         = response.getJSONArray("data");

                                    for (int i = 0; i < array.length(); i++){
                                        JSONObject object   = array.getJSONObject(i);

                                        String id_bahan     = object.getString("id_bahan");
                                        String dipakai      = object.getString("terpakai");
                                        String nama         = object.getString("nama_bahan");

                                        ListStokRoti data1   = new ListStokRoti(
                                                id_bahan,
                                                nama,
                                                dipakai
                                        );

                                        listStokRoti.add(data1);

                                    }

                                    stokRotiParent    = new ListStokRotiP(
                                            id_produkk,
                                            listStokRoti
                                    );

                                    listBahan.add(stokRotiParent);
                                    if (listBahan.size() == listGridPesanan.size()){
                                        for (int i = 0; i < listBahan.size(); i++){
                                            set_pesanan(dialog, listBahan.get(i).getId_produk(), i);
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                loading1.setVisibility(View.GONE);
                                e.printStackTrace();                       }
                        }

                        @Override
                        public void onError(ANError anError) {
                            if (anError.getErrorCode() == 401){
                                sm.logout();
                                sessionManager.storeLogin(id_outlet, nama, notelp, alamat, logo, nama_rek, no_rek, "0");
                                finish();
                            }
                            else {
                                loading1.setVisibility(View.GONE);
                            }
                        }
                    });
        }

    }

    public void set_pesanan(Dialog dialog, String id_produk, int index){
        ArrayList<ListStokRotiP> listStokRotiP  = new ArrayList<>();
        for (int a = 0; a < listGridPesanan.size(); a++)
        {
            if (id_produk.equals(listGridPesanan.get(a).getId_produk()))
            {
                ListStokRotiP data                      = new ListStokRotiP(
                        listBahan.get(index).getId_produk(),
                        listBahan.get(index).getListStokRoti()
                );

                int jml_pesanan     = Integer.parseInt(listGridPesanan.get(a).getJumlah());
                for (int e = 0; e < jml_pesanan; e++)
                {
                    listStokRotiP.add(data);

                    listGridPesanan.get(a).setBahan(listStokRotiP);
                    listGridPesanan.get(a).setTotal(
                            String.valueOf(
                                    Integer.parseInt(listGridPesanan.get(a).getJumlah()) *
                                            Integer.parseInt(listGridPesanan.get(a).getHarga_satuan())
                            )
                    );
                    dialog.dismiss();
                    txt_lanjut.setText("Chek Out");
                    R_lanjut.setBackgroundResource(R.color.hijau);
                }
            }

        }
    }

    //========================================menu==================================================

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (id == R.id.nav_stock) {
            Intent intent       = new Intent(HomeActivity.this, ManajemenStokActivity.class);
            intent.putExtra("tipe", "home");
            startActivity(intent);
        } else if (id == R.id.nav_transaksi) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_report) {
            startActivity(new Intent(HomeActivity.this, LaporanActivity.class));
        } else if (id == R.id.nav_poin) {
            startActivity(new Intent(HomeActivity.this, PoinActivity.class));
        } else if (id == R.id.nav_setting) {
            setting_printer();
        } else if (id == R.id.nav_ubah_password) {
            startActivity(new Intent(HomeActivity.this, GantiPasswordActivity.class));
        }else if (id == R.id.nav_sync_data) {
            startActivity(new Intent(HomeActivity.this, SinkronDataActivity.class));
        } else if (id == R.id.nav_logout) {
            Logout();
        }else if (id == R.id.nav_tutup_toko) {
            showDialog();
        }else if (id == R.id.nav_foto) {
            reqPermission();
        }else if (id == R.id.nav_sop) {
            startActivity(new Intent(HomeActivity.this, SOPActivity.class));
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setting_printer() {
        startActivity(new Intent(HomeActivity.this, SettingPrinterActivity.class));
    }

    private void Logout() {
        AndroidNetworking.get(Server.URL + "logout")
                .addHeaders("Authorization", "Bearer " + token)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String message = response.getString("message");
                            String responseCode = response.getString("response");

                            if (responseCode.equals("200")) {
                                sm.logout();
                                sessionManager.storeLogin(id_outlet, nama, notelp, alamat, logo, nama_rek, no_rek, "0");
                                finish();

                            } else {
                                Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Kategori", anError.getMessage());
                    }
                });
    }

    private void tutup_toko(){
        Dialog dialog       = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.loading);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        TextView txt_title  = dialog.findViewById(R.id.txt_title);
        txt_title.setText("Menutup Toko");

        AndroidNetworking.post(Server.URL + "tutup")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addHeaders("Accept", "application/json")
                .addBodyParameter("id_outlet", HomeActivity.id_outlet)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res_cde      = response.getString("response");
                            if (res_cde.equals("200")){
                                Logout();
                                dialog.dismiss();
                                FancyToast.makeText(
                                        HomeActivity.this,
                                        "Toko Berhasil Ditutup",
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.SUCCESS,
                                        false
                                ).show();
                            }
                            else{
                                FancyToast.makeText(
                                        HomeActivity.this,
                                        "Gagal Menutup Toko",
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR,
                                        false
                                ).show();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(
                                    HomeActivity.this,
                                    "Kesalahan Request!",
                                    FancyToast.LENGTH_SHORT,
                                    FancyToast.ERROR,
                                    false
                            ).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(HomeActivity.this);
                        }
                        else {
                            FancyToast.makeText(
                                    HomeActivity.this,
                                    "Jaringan Bermasalah!",
                                    FancyToast.LENGTH_SHORT,
                                    FancyToast.ERROR,
                                    false
                            ).show();
                            dialog.dismiss();
                        }
                    }
                });
    }

    private void showDialog(){
        Dialog dialog       = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_tutup);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        Button btn_batal_tutup  = dialog.findViewById(R.id.btn_batal_tutup);
        Button btn_lanjut_tutup = dialog.findViewById(R.id.btn_lanjut_tutup);

        btn_batal_tutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_lanjut_tutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutup_toko();
            }
        });
    }

    private void show_tutorial(){
        TapTargetView.showFor(this,                 // `this` is an Activity
                TapTarget.forView(img_sort, "Menu Sortir", "Sortir produk berdasarkan abjad")
                        // All options below are optional
                        .outerCircleColor(android.R.color.holo_orange_dark)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(R.color.putih)   // Specify a color for the target circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.putih)      // Specify the color of the title text
                        .descriptionTextSize(18)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.putih)  // Specify the color of the description text
                        .textColor(R.color.putih)            // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                        .dimColor(R.color.hitam)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(false)                   // Specify a custom drawable to draw as the target
                        .targetRadius(40),
                new TapTargetView.Listener(){
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                        new GuideView.Builder(HomeActivity.this)
                                .setTitle("Daftar Kategori Produk")
                                .setContentText("Anda bisa beralih ke kategori lain yang telah tersedia")
                                .setTargetView(recyclerView)
                                .setContentTextSize(16)//optional
                                .setTitleTextSize(20)//optional
                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                .setGuideListener(new GuideListener() {
                                    @Override
                                    public void onDismiss(View view) {

                                        new GuideView.Builder(HomeActivity.this)
                                                .setTitle("Daftar Produk + harga berdasarkan kategori")
                                                .setContentText("Untuk melakukan Pembelian produk, pilih dan tekan produk untuk menambah jumlah dengan kelipatan 1 atau tekan dan tahan untuk mengisi jumlah lebih dari 1")
                                                .setTargetView(recyclerView_list_produk.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.R_8))
                                                .setContentTextSize(16)//optional
                                                .setTitleTextSize(20)//optional
                                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                                .setGuideListener(new GuideListener() {
                                                    @Override
                                                    public void onDismiss(View view) {

                                                        new GuideView.Builder(HomeActivity.this)
                                                                .setTitle("Total jumlah pembelian Produk dan Tombol lanjut pembelian")
                                                                .setContentText("")
                                                                .setTargetView(R_lanjut)
                                                                .setContentTextSize(12)//optional
                                                                .setTitleTextSize(20)//optional
                                                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                                                .setGuideListener(new GuideListener() {
                                                                    @Override
                                                                    public void onDismiss(View view) {

                                                                        new GuideView.Builder(HomeActivity.this)
                                                                                .setTitle("Tombol untuk menghapus semua daftar pembelian")
                                                                                .setContentText("")
                                                                                .setTargetView(fab_hapus)
                                                                                .setContentTextSize(12)//optional
                                                                                .setTitleTextSize(20)//optional
                                                                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                                                                .setGuideListener(new GuideListener() {
                                                                                    @Override
                                                                                    public void onDismiss(View view) {

                                                                                        fab_hapus.setVisibility(View.GONE);

                                                                                    }
                                                                                })
                                                                                .build()
                                                                                .show();

                                                                    }
                                                                })
                                                                .build()
                                                                .show();

                                                    }
                                                })
                                                .build()
                                                .show();

                                    }

                                })
                                .build()
                                .show();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
            super.onBackPressed();
        }
    }

    private void reqPermission() {

        if (hasCameraPermissions() || hasLocationPermissions() ) {
            Intent intent   = new Intent(HomeActivity.this, FotoTutupActivity.class);
            startActivity(intent);
        }
        else{
            requestPermission();
        }
    }

    private boolean hasCameraPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasLocationPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                10);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loading1.setVisibility(View.VISIBLE);
        recyclerView_list_produk.setVisibility(View.GONE);
//        setData();
//        txt_kategori.setText("Reguler");
        setDataRestart();
    }

    public void setDataRestart(){
        if (!kategoriSqlite.isEmpty()) {
            setKategoriSqlite2();
        }
        else {
            setKategori2();
        }
    }

    private void setKategori2() {
        listKategori.clear();
        recyclerView.setAdapter(null);
        AndroidNetworking.post(Server.URL + "kategori")
                .addHeaders("Authorization", "Bearer " + token)
                .addHeaders("Accept", "application/json")
                .addBodyParameter("id_outlet", id_outlet)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        recyclerView.setVisibility(View.VISIBLE);
                        try {
                            String message = response.getString("message");
                            String responseCode = response.getString("response");

                            if (responseCode.equals("200")) {
                                JSONArray jsonArray         = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject   = jsonArray.getJSONObject(i);

                                    data                    = new ListKategori(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("nama_kategori"),
                                            "false"
                                    );
                                    listKategori.add(data);

                                    adapterKategori     = new AdapterKategori(HomeActivity.this, listKategori);
                                    adapterKategori.setOnItemClickListener(new AdapterKategori.recyclerViewClickListener() {
                                        @Override
                                        public void onClick(View v, int position, TextView textView) {
                                            pos = position;
                                            loading1.setVisibility(View.VISIBLE);
                                            txt_kategori.setText(listKategori.get(position).getNama_kategori());

                                            sessionManager.updatePoin(listKategori.get(position).getId_kategori());
                                            HashMap<String, String> map = sessionManager.getDetailLogin();
                                            last_klik                   = map.get(sessionManager.KLIK_KAEGORI);

                                            for (int i = 0; i < listKategori.size(); i++){
                                                listKategori.get(i).setDi_klik("false");
                                            }
                                            listKategori.get(position).setDi_klik("true");
                                            adapterKategori.notifyItemChanged(position);
                                            adapterKategori.notifyDataSetChanged();

                                            if (position + 1 == Integer.parseInt(listKategori.get(position).getId_kategori())){
                                                textView.setBackgroundResource(R.drawable.bg_btn_login);
                                                textView.setTextColor(Color.WHITE);
                                            }
                                            else
                                            {
                                                textView.setBackgroundResource(R.drawable.bg_layout);
                                                textView.setTextColor(Color.DKGRAY);
                                            }

                                            setProduk(listKategori.get(position).getId_kategori(), "default");
                                        }
                                    });

                                    adapterKategori.notifyDataSetChanged();
                                    recyclerView.setAdapter(adapterKategori);
                                    recyclerView.scrollToPosition(pos);

                                }

                                listKategori.get(pos).setDi_klik("true");
                                if (Integer.parseInt(last_klik) - 1 != 0) {
                                    listKategori.get(0).setDi_klik("false");
                                }
                                else {
                                    listKategori.get(0).setDi_klik("true");
                                }

                                setProduk(last_klik, "default");

                            } else {
                                FancyToast.makeText(HomeActivity.this,  message,
                                        FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            recyclerView.setVisibility(View.VISIBLE);
                            loading1.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Kategori", anError.getMessage());
//                        Toast.makeText(HomeActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        if (anError.getErrorCode() == 401){
                            sm.logout();
                            sessionManager.storeLogin(id_outlet, nama, notelp, alamat, logo, nama_rek, no_rek, "0");
                            finish();
                        }
                        else{
                            recyclerView.setVisibility(View.VISIBLE);
                            loading1.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void setKategoriSqlite2() {
        listKategori.clear();
        for (int i = 0; i < kategoriSqlite.size(); i++){
            data                    = new ListKategori(
                    kategoriSqlite.get(i).getId(),
                    kategoriSqlite.get(i).getNama_kategori(),
                    "false"
            );
            listKategori.add(data);
        }

        listKategori.get(pos).setDi_klik("true");
        if (Integer.parseInt(last_klik) - 1 != 0) {
            listKategori.get(0).setDi_klik("false");
        }
        else {
            listKategori.get(0).setDi_klik("true");
        }

        adapterKategori     = new AdapterKategori(HomeActivity.this, listKategori);
        adapterKategori.setOnItemClickListener(new AdapterKategori.recyclerViewClickListener() {
            @Override
            public void onClick(View v, int position, TextView textView) {
                pos = position;
                loading1.setVisibility(View.VISIBLE);
                txt_kategori.setText(listKategori.get(position).getNama_kategori());

                sessionManager.updatePoin(listKategori.get(position).getId_kategori());
                HashMap<String, String> map = sessionManager.getDetailLogin();
                last_klik                   = map.get(sessionManager.KLIK_KAEGORI);

                for (int i = 0; i < listKategori.size(); i++){
                    listKategori.get(i).setDi_klik("false");
                }
                listKategori.get(position).setDi_klik("true");
                adapterKategori.notifyItemChanged(position);
                adapterKategori.notifyDataSetChanged();

                if (position + 1 == Integer.parseInt(listKategori.get(position).getId_kategori())){
                    textView.setBackgroundResource(R.drawable.bg_btn_login);
                    textView.setTextColor(Color.WHITE);
                }
                else
                {
                    textView.setBackgroundResource(R.drawable.bg_layout);
                    textView.setTextColor(Color.DKGRAY);
                }

                setProdukSqlite(listKategori.get(position).getId_kategori(), "default");
            }
        });

        adapterKategori.notifyDataSetChanged();
        recyclerView.setAdapter(adapterKategori);
        recyclerView.scrollToPosition(pos);

        setProdukSqlite(last_klik, "default");
    }

    private void cekVersi(){
        AndroidNetworking.post(Server.URL2 + "cek_versi")
                .addBodyParameter("id_app", "1")
                .addBodyParameter("versi_code", String.valueOf(BuildConfig.VERSION_CODE))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("201")){
                                Dialog dialog       = new Dialog(HomeActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.alertupdate);
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(dialog.getWindow().getAttributes());
                                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                dialog.getWindow().setAttributes(lp);
                                dialog.show();

                                Button exit     = dialog.findViewById(R.id.exit);
                                TextView title  = dialog.findViewById(R.id.title);

                                title.setText(response.getString("message"));

                                exit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finishAffinity();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public void cek_sinkron(){
        Dialog dialog       = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_sinkron);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        Button btn_tutup            = dialog.findViewById(R.id.btn_tutup_dialog);
        Button btn_lanjut           = dialog.findViewById(R.id.btn_sinkron_dialog);

        btn_tutup.setVisibility(View.GONE);

        btn_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getKategori1();
            }
        });

        btn_tutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void cek_sinkron_pertama(){
        Dialog dialog       = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_sinkron);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        Button btn_tutup            = dialog.findViewById(R.id.btn_tutup_dialog);
        Button btn_lanjut           = dialog.findViewById(R.id.btn_sinkron_dialog);

        btn_tutup.setVisibility(View.GONE);

        btn_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(HomeActivity.this, SinkronDataActivity.class));
                finish();
            }
        });

        btn_tutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    //input SQLITE
    private void getKategori1() {
        sqliteHelper.delete_kategori();
        sqliteHelper.delete_produk();
        sqliteHelper.delete_bahan_produk();
        sqliteHelper.delete_bahan_outlet();
        sqliteHelper.delete_info();
        Dialog dialog               = new Dialog(HomeActivity.this);
        dialog.show();
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView txt_title          = dialog.findViewById(R.id.txt_title);
        txt_title.setText("Sinkronasi Data");

        AndroidNetworking.post(Server.URL + "kategori")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addHeaders("Accept", "application/json")
                .addBodyParameter("id_outlet", HomeActivity.id_outlet)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            String responseCode = response.getString("response");

                            if (responseCode.equals("200")) {
                                JSONArray jsonArray         = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject   = jsonArray.getJSONObject(i);
                                    sqliteHelper.add_kategori(jsonObject.getString("id"), jsonObject.getString("nama_kategori"), jsonObject.getString("urutan") );
                                }
                                getProduk1(dialog);
                            } else {
                                FancyToast.makeText(HomeActivity.this,  message,
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(HomeActivity.this, "Gagal mengambil data Kategori Produk, Kesalahan Request",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        Log.d("Kategori", String.valueOf(anError.getErrorCode()));
//                        Toast.makeText(HomeActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        if (anError.getErrorCode() == 401){
                            HomeActivity.sm.logout();
                            HomeActivity.sessionManager.storeLogin(HomeActivity.id_outlet, HomeActivity.nama, HomeActivity.notelp, HomeActivity.alamat, HomeActivity.logo, HomeActivity.nama_rek, HomeActivity.no_rek, "0");
                            finish();
                        }
                        else{
                            FancyToast.makeText(HomeActivity.this, "Gagal mengambil data Kategori Produk, Jaringan Bermasalah",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }
                });
    }

    private void getProduk1(Dialog dialog) {
        AndroidNetworking.get(Server.URL + "all-produk")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            String responseCode = response.getString("response");

                            if (responseCode.equals("200")) {
                                JSONArray jsonArray         = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject   = jsonArray.getJSONObject(i);
                                    sqliteHelper.add_produk(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("id_kategori"),
                                            jsonObject.getString("nama_produk"),
                                            jsonObject.getString("harga"),
                                            jsonObject.getString("img"));
                                }
                                getBahanProduk1(dialog);

                            } else {
                                FancyToast.makeText(HomeActivity.this,  message,
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(HomeActivity.this, "Gagal mengambil data Produk, Kesalahan Request",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        Log.d("Produk", String.valueOf(anError.getErrorCode()));
                        if (anError.getErrorCode() == 401){
                            HomeActivity.sm.logout();
                            HomeActivity.sessionManager.storeLogin(HomeActivity.id_outlet, HomeActivity.nama, HomeActivity.notelp, HomeActivity.alamat, HomeActivity.logo, HomeActivity.nama_rek, HomeActivity.no_rek, "0");
                            finish();
                        }
                        else{
                            FancyToast.makeText(HomeActivity.this, "Gagal mengambil data Produk, Jaringan Bermasalah",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }
                });
    }

    private void getBahanProduk1(Dialog dialog) {
        AndroidNetworking.get(Server.URL + "all-komposisi_produk")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            String responseCode = response.getString("response");

                            if (responseCode.equals("200")) {
                                JSONArray jsonArray         = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject   = jsonArray.getJSONObject(i);
                                    sqliteHelper.add_bahan_produk(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("id_produk"),
                                            jsonObject.getString("id_bahan"),
                                            jsonObject.getString("terpakai"),
                                            jsonObject.getString("nama_bahan"));
                                }
                                getBahanOutlet1(dialog);

                            } else {
                                FancyToast.makeText(HomeActivity.this,  message,
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(HomeActivity.this, e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        Log.d("Bahan Produk", String.valueOf(anError.getErrorCode()));
                        if (anError.getErrorCode() == 401){
                            HomeActivity.sm.logout();
                            HomeActivity.sessionManager.storeLogin(HomeActivity.id_outlet, HomeActivity.nama, HomeActivity.notelp, HomeActivity.alamat, HomeActivity.logo, HomeActivity.nama_rek, HomeActivity.no_rek, "0");
                            finish();
                        }
                        else{
                            FancyToast.makeText(HomeActivity.this, "Gagal mengambil data Bahan Produk, Jaringan Bermasalah",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }
                });
    }

    private void getBahanOutlet1(Dialog dialog) {
        AndroidNetworking.post(Server.URL + "stok_manajemen")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addHeaders("Accept", "application/json")
                .addBodyParameter("id_outlet", HomeActivity.id_outlet)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            String responseCode = response.getString("response");

                            if (responseCode.equals("200")) {
                                JSONArray jsonArray         = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject   = jsonArray.getJSONObject(i);
                                    if (!jsonObject.getString("stok").equals("0")) {
                                        sqliteHelper.add_bahan_outlet(
                                                jsonObject.getString("id_stok"),
                                                jsonObject.getString("id_bahan"),
                                                jsonObject.getString("stok"),
                                                jsonObject.getString("nama_bahan"));
                                    }

                                    Log.d("Salah", jsonArray.toString());
                                }
                                sqliteHelper.add_update_info("1", tanggal.getTanggal() + " " + tanggal.getTime());
                                FancyToast.makeText(HomeActivity.this,  "Sinkronasi data berhasil, data berhasil diupdate!" ,
                                        FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                                setKategoriSqlite();
                                dialog.dismiss();
                            } else {
                                FancyToast.makeText(HomeActivity.this,  message,
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(HomeActivity.this, e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        Log.d("Bahan Outlet", String.valueOf(anError.getErrorCode()));
                        if (anError.getErrorCode() == 401){
                            HomeActivity.sm.logout();
                            HomeActivity.sessionManager.storeLogin(HomeActivity.id_outlet, HomeActivity.nama, HomeActivity.notelp, HomeActivity.alamat, HomeActivity.logo, HomeActivity.nama_rek, HomeActivity.no_rek, "0");
                            finish();
                        }
                        else{
                            FancyToast.makeText(HomeActivity.this, "Gagal mengambil data Bahan Outlet, Jaringan Bermasalah",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }
                });
    }
}
