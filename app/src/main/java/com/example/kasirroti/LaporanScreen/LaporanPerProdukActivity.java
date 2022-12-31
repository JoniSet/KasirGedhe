package com.example.kasirroti.LaporanScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.Adapter.AdapterLaporanPerproduk;
import com.example.kasirroti.Helper.SingOut;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.Model.ListLaporanPerproduk;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LaporanPerProdukActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<ListLaporanPerproduk> listLaporanPerproduk;
    private AdapterLaporanPerproduk adapterLaporanPerproduk;
    private ListLaporanPerproduk list;
    private ProgressBar loading_perproduk;

    private TextView txt_judul_filter, txt_tgl_filter, txt_filter;

    private Tanggal tanggal     = new Tanggal();

    com.example.kasirroti.Helper.Tanggal tgl   = new com.example.kasirroti.Helper.Tanggal();

    int status                  = 0;

    private TextView txt_hari_ini, txt_7_hari, txt_bulan_ini, txt_tahun_ini;

    String end_date, start_date, filter_tanggal, filter_tanggal2;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter, dateFormateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_per_produk);

        Toolbar toolbar         = findViewById(R.id.toolbar_goods);
        toolbar.setTitle("Laporan Perproduk");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_hari_ini            = findViewById(R.id.txt_hari_ini);
        txt_7_hari              = findViewById(R.id.txt_7_hari);
        txt_bulan_ini           = findViewById(R.id.txt_bulan_ini);
        txt_tahun_ini           = findViewById(R.id.txt_tahun_ini);
        loading_perproduk       = findViewById(R.id.loading_perproduk);
        txt_judul_filter        = findViewById(R.id.txt_judul_filter);
        txt_tgl_filter          = findViewById(R.id.txt_tgl_filter);
        txt_filter              = findViewById(R.id.txt_filter);

        txt_tgl_filter.setText(tgl.getTanggal());

        recyclerView            = findViewById(R.id.list_laporan_produk);
        listLaporanPerproduk    = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        txt_hari_ini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_ui();
                loading_perproduk.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                setLaporan(tanggal.getTanggal(), tanggal.getTanggal());
                txt_judul_filter.setText("HARI INI");
                status          = 0;
            }
        });

        txt_7_hari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_ui();
                loading_perproduk.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                txt_judul_filter.setText("7 HARI TERAKHIR");
                status          = 1;

                setLaporan(tanggal.getMingguuu(), tanggal.getTanggal());

//                if(tanggal.getMinggu().length() == 1)
//                {
//                    setLaporan(tanggal.getBulan() + "0" + tanggal.getMinggu(), tanggal.getTanggal());
//                }
//                else {
//                    setLaporan(tanggal.getBulan() + tanggal.getMinggu(), tanggal.getTanggal());
//                }
            }
        });

        txt_bulan_ini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_ui();
                loading_perproduk.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                txt_judul_filter.setText("BULAN INI");
                status          = 1;

                setLaporan(tanggal.getBulan() + "01", tanggal.getBulan() + "31");
            }
        });

        txt_tahun_ini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_ui();
                loading_perproduk.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                txt_judul_filter.setText("TAHUN INI");
                status          = 1;

                setLaporan(tanggal.getTahun() + "01-01", tanggal.getTahun() + "12-31");
            }
        });

        txt_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_ui();
                loading_perproduk.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                status          = 2;

                showDateDialog();
            }
        });

        setLaporan(tanggal.getTanggal(), tanggal.getTanggal());
    }

    private void setLaporan(String startdate, String enddate) {
        recyclerView.setAdapter(null);
        listLaporanPerproduk.clear();
        txt_tgl_filter.setText("");

        AndroidNetworking.post(Server.URL + "sales_good")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addHeaders("Accept", "application/json")
                .addBodyParameter("startdate", startdate)
                .addBodyParameter("enddate", enddate)
                .addBodyParameter("id_outlet", HomeActivity.id_outlet)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            loading_perproduk.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            String message              = response.getString("message");
                            String res_kode             = response.getString("response");
                            JSONArray jsonArray         = response.getJSONArray("data");

                            end_date                     = response.getString("end_date");
                            start_date                   = response.getString("start_date");

                            if (status == 1)
                            {
                                txt_tgl_filter.setText(start_date + " - " + end_date);
                            } else if (status == 2){
                                txt_tgl_filter.setText(filter_tanggal2);
                            }
                            else
                            {
                                txt_tgl_filter.setText(tgl.getTanggal());
                            }

                            if (res_kode.equals("200"))
                            {

                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject data     = jsonArray.getJSONObject(i);

                                    ListLaporanPerproduk objek  = new ListLaporanPerproduk(
                                            data.getString("nama_produk"),
                                            data.getString("harga_satuan"),
                                            data.getString("terjual"),
                                            data.getString("pendapatan"),
                                            data.getString("img")
                                    );

                                    listLaporanPerproduk.add(objek);

                                    adapterLaporanPerproduk     = new AdapterLaporanPerproduk(LaporanPerProdukActivity.this, listLaporanPerproduk);
                                    adapterLaporanPerproduk.notifyDataSetChanged();

                                    recyclerView.setAdapter(adapterLaporanPerproduk);

                                }
                            }
                            else
                            {
                                Toast.makeText(LaporanPerProdukActivity.this, "Tidak ada transaksi", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            loading_perproduk.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                            Toast.makeText(LaporanPerProdukActivity.this, "Terjadi kesalahan request!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(LaporanPerProdukActivity.this);
                        }
                        else {
                            loading_perproduk.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            Toast.makeText(LaporanPerProdukActivity.this, "Terjadi kesalahan jaringan!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_laporan_produk, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id      = item.getItemId();

        switch (id)
        {
            case R.id.menu_refresh:
                setLaporan(tanggal.getTanggal(), tanggal.getTanggal());
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private static class Tanggal{
        public String getTanggal(){
            DateFormat dateFormat       = new SimpleDateFormat("yyyy-MM-dd");
            Date date                   = new Date();

            return dateFormat.format(date);
        }

        public String getBulan(){
            DateFormat dateFormat       = new SimpleDateFormat("yyyy-MM-");
            Date date                   = new Date();

            return dateFormat.format(date);
        }

        public String getTahun(){
            DateFormat dateFormat       = new SimpleDateFormat("yyyy-");
            Date date                   = new Date();

            return dateFormat.format(date);
        }

        public String getMinggu(){
            DateFormat dateFormat       = new SimpleDateFormat("yyyy-");
            Date date                   = new Date();

            String minggu               = String.valueOf(date.getDate() - 7);

            return minggu;
        }

        public String getMingguuu(){
            final LocalDate date;
            String formattedDate = "";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                date = LocalDate.now();
                final LocalDate plusDays = date.minusDays(6);
                //if you want to show past 7 days, change to data.minusDays(7);
                formattedDate = plusDays.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // show is this format 09/30/2020

            }

            return formattedDate;
        }
    }


    public void set_ui(){
        if (txt_hari_ini.isPressed())
        {
            txt_hari_ini.setBackgroundResource(R.color.putih);
            txt_hari_ini.setTextColor(getResources().getColor(R.color.colorAccent));

            txt_7_hari.setBackgroundResource(R.color.colorAccent);
            txt_7_hari.setTextColor(getResources().getColor(R.color.putih));

            txt_bulan_ini.setBackgroundResource(R.color.colorAccent);
            txt_bulan_ini.setTextColor(getResources().getColor(R.color.putih));

            txt_tahun_ini.setBackgroundResource(R.color.colorAccent);
            txt_tahun_ini.setTextColor(getResources().getColor(R.color.putih));

            txt_filter.setBackgroundResource(R.color.colorAccent);
            txt_filter.setTextColor(getResources().getColor(R.color.putih));
        }
        else if (txt_7_hari.isPressed())
        {
            txt_7_hari.setBackgroundResource(R.color.putih);
            txt_7_hari.setTextColor(getResources().getColor(R.color.colorAccent));

            txt_hari_ini.setBackgroundResource(R.color.colorAccent);
            txt_hari_ini.setTextColor(getResources().getColor(R.color.putih));

            txt_bulan_ini.setBackgroundResource(R.color.colorAccent);
            txt_bulan_ini.setTextColor(getResources().getColor(R.color.putih));

            txt_tahun_ini.setBackgroundResource(R.color.colorAccent);
            txt_tahun_ini.setTextColor(getResources().getColor(R.color.putih));

            txt_filter.setBackgroundResource(R.color.colorAccent);
            txt_filter.setTextColor(getResources().getColor(R.color.putih));
        }
        else if (txt_7_hari.isPressed())
        {
            txt_7_hari.setBackgroundResource(R.color.putih);
            txt_7_hari.setTextColor(getResources().getColor(R.color.colorAccent));

            txt_hari_ini.setBackgroundResource(R.color.colorAccent);
            txt_hari_ini.setTextColor(getResources().getColor(R.color.putih));

            txt_bulan_ini.setBackgroundResource(R.color.colorAccent);
            txt_bulan_ini.setTextColor(getResources().getColor(R.color.putih));

            txt_tahun_ini.setBackgroundResource(R.color.colorAccent);
            txt_tahun_ini.setTextColor(getResources().getColor(R.color.putih));

            txt_filter.setBackgroundResource(R.color.colorAccent);
            txt_filter.setTextColor(getResources().getColor(R.color.putih));
        }
        else if (txt_bulan_ini.isPressed())
        {
            txt_filter.setBackgroundResource(R.color.putih);
            txt_filter.setTextColor(getResources().getColor(R.color.colorAccent));

            txt_bulan_ini.setBackgroundResource(R.color.colorAccent);
            txt_bulan_ini.setTextColor(getResources().getColor(R.color.putih));

            txt_hari_ini.setBackgroundResource(R.color.colorAccent);
            txt_hari_ini.setTextColor(getResources().getColor(R.color.putih));

            txt_7_hari.setBackgroundResource(R.color.colorAccent);
            txt_7_hari.setTextColor(getResources().getColor(R.color.putih));

            txt_tahun_ini.setBackgroundResource(R.color.colorAccent);
            txt_tahun_ini.setTextColor(getResources().getColor(R.color.putih));
        }
        else if (txt_filter.isPressed())
        {
            txt_filter.setBackgroundResource(R.color.putih);
            txt_filter.setTextColor(getResources().getColor(R.color.colorAccent));

            txt_bulan_ini.setBackgroundResource(R.color.colorAccent);
            txt_bulan_ini.setTextColor(getResources().getColor(R.color.putih));

            txt_hari_ini.setBackgroundResource(R.color.colorAccent);
            txt_hari_ini.setTextColor(getResources().getColor(R.color.putih));

            txt_7_hari.setBackgroundResource(R.color.colorAccent);
            txt_7_hari.setTextColor(getResources().getColor(R.color.putih));

            txt_tahun_ini.setBackgroundResource(R.color.colorAccent);
            txt_tahun_ini.setTextColor(getResources().getColor(R.color.putih));
        }
        else if (txt_tahun_ini.isPressed())
        {
            txt_tahun_ini.setBackgroundResource(R.color.putih);
            txt_tahun_ini.setTextColor(getResources().getColor(R.color.colorAccent));

            txt_hari_ini.setBackgroundResource(R.color.colorAccent);
            txt_hari_ini.setTextColor(getResources().getColor(R.color.putih));

            txt_7_hari.setBackgroundResource(R.color.colorAccent);
            txt_7_hari.setTextColor(getResources().getColor(R.color.putih));

            txt_bulan_ini.setBackgroundResource(R.color.colorAccent);
            txt_bulan_ini.setTextColor(getResources().getColor(R.color.putih));

            txt_filter.setBackgroundResource(R.color.colorAccent);
            txt_filter.setTextColor(getResources().getColor(R.color.putih));
        }
    }


    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormateText = new SimpleDateFormat("dd MMM yyyy", Locale.US);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                filter_tanggal  = dateFormatter.format(newDate.getTime());
                filter_tanggal2 = dateFormateText.format(newDate.getTime());

                txt_judul_filter.setText("FILTER TANGGAL");

                setLaporan(filter_tanggal, filter_tanggal);
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}

