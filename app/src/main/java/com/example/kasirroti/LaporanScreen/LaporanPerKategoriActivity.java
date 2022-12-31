package com.example.kasirroti.LaporanScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.Adapter.AdapterLaporanPerkategori;
import com.example.kasirroti.Helper.SingOut;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.Model.ListLaporanPerkategori;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LaporanPerKategoriActivity extends AppCompatActivity {

    private PieChart pieChart;
    private ProgressBar loading_kategori;
    private TextView txt_hari_ini_a, txt_7_hari_a, txt_bulan_ini_a, txt_tahun_ini_a, txt_judul_filter_a, txt_tgl_filter_a, txt_filter_tanggal;
    private LinearLayout L_18;

    RecyclerView recyclerView;
    ArrayList<ListLaporanPerkategori> listLaporanPerkategori;

    private Tanggal tanggal     = new Tanggal();
    com.example.kasirroti.Helper.Tanggal tgl   = new com.example.kasirroti.Helper.Tanggal();

    ArrayList<PieEntry> yvalues = new ArrayList<>();
    PieDataSet dataSet;
    PieData data;

    int[] colorArray            = new int[]{
            R.color.colorAccent,
            Color.LTGRAY,
            Color.CYAN,
            R.color.kuning,
            Color.YELLOW,
            Color.BLUE
    };

    String end_date, start_date, filter_tanggal, filter_tanggal2;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter, dateFormateText;

    int status                  = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_per_kategori);

        Toolbar toolbar = findViewById(R.id.toolbar_kategori);
        toolbar.setTitle("Laporan Perkategori");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_hari_ini_a          = findViewById(R.id.txt_hari_ini_a);
        txt_7_hari_a            = findViewById(R.id.txt_7_hari_a);
        txt_bulan_ini_a         = findViewById(R.id.txt_bulan_ini_a);
        txt_tahun_ini_a         = findViewById(R.id.txt_tahun_ini_a);
        L_18                    = findViewById(R.id.L_18);
        txt_tgl_filter_a        = findViewById(R.id.txt_tgl_filter_a);
        txt_judul_filter_a      = findViewById(R.id.txt_judul_filter_a);
        txt_filter_tanggal      = findViewById(R.id.txt_filter_tanggal);

        txt_tgl_filter_a.setText(tgl.getTanggal());

        txt_hari_ini_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_ui();
                set_chartkategori(tanggal.getTanggal(), tanggal.getTanggal());
                setChart(tanggal.getTanggal(), tanggal.getTanggal());
                status          = 0;
            }
        });

        txt_7_hari_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_ui();
                status          = 1;
                txt_judul_filter_a.setText("7 HARI TERAKHIR");

                set_chartkategori(tanggal.getMinggu(), tanggal.getTanggal());
                setChart(tanggal.getMinggu(), tanggal.getTanggal());

//                if(tanggal.getMinggu().length() == 1)
//                {
//                    set_chartkategori(tanggal.getBulan() + "0" + tanggal.getMinggu(), tanggal.getTanggal());
//                    setChart(tanggal.getBulan() + "0" + tanggal.getMinggu(), tanggal.getTanggal());
//                }
//                else {
//                    set_chartkategori(tanggal.getBulan() + tanggal.getMinggu(), tanggal.getTanggal());
//                    setChart(tanggal.getBulan() + tanggal.getMinggu(), tanggal.getTanggal());
//                }
            }
        });

        txt_bulan_ini_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_ui();
                txt_judul_filter_a.setText("BULAN INI");
                status          = 1;
                set_chartkategori(tanggal.getBulan() + "01", tanggal.getBulan() + "31");
                setChart(tanggal.getBulan() + "01", tanggal.getBulan() + "31");
            }
        });

        txt_tahun_ini_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_ui();
                txt_judul_filter_a.setText("TAHUN INI");
                status          = 1;
                set_chartkategori(tanggal.getTahun() + "01-01", tanggal.getTahun() + "12-31");
                setChart(tanggal.getTahun() + "01-01", tanggal.getTahun() + "12-31");
            }
        });

        txt_filter_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_ui();
                status          = 2;

                showDateDialog();
            }
        });


        pieChart        = findViewById(R.id.pie_chart1);
        pieChart.setUsePercentValues(true);
        pieChart.invalidate();


        listLaporanPerkategori      = new ArrayList<>();
        recyclerView                = findViewById(R.id.list_laporan_kategori);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        setChart(tanggal.getTanggal(), tanggal.getTanggal());
        set_chartkategori(tanggal.getTanggal(), tanggal.getTanggal());
    }

    private void set_chartkategori(String startdate, String enddate){
        Dialog dialog               = new Dialog(LaporanPerKategoriActivity.this);
        dialog.show();
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        recyclerView.setAdapter(null);
        listLaporanPerkategori.clear();

        L_18.setVisibility(View.GONE);

        yvalues.clear();
        pieChart.setData(null);

        AndroidNetworking.post(Server.URL + "sales_kategori")
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
                            dialog.dismiss();
                            L_18.setVisibility(View.VISIBLE);
                            String res_kode             = response.getString("response");
                            JSONArray jsonArray         = response.getJSONArray("data");

                            end_date                     = response.getString("end_date");
                            start_date                   = response.getString("start_date");

                            if (status == 1)
                            {
                                txt_tgl_filter_a.setText(start_date + " - " + end_date);
                            } else if (status == 2){
                                txt_tgl_filter_a.setText(filter_tanggal2);
                            }
                            else
                            {

                                txt_tgl_filter_a.setText(tgl.getTanggal());
                            }

                            if (res_kode.equals("200"))
                            {
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject object   = jsonArray.getJSONObject(i);

                                    ListLaporanPerkategori data     = new ListLaporanPerkategori(
                                            object.getString("nama_kategori"),
                                            object.getString("terjual"),
                                            object.getString("pendapatan")
                                    );

                                    float x             = Float.parseFloat(object.getString("terjual"));

                                    yvalues.add(new PieEntry(x, object.getString("nama_kategori"), i));

                                    listLaporanPerkategori.add(data);

                                    AdapterLaporanPerkategori adapter   = new AdapterLaporanPerkategori(LaporanPerKategoriActivity.this, listLaporanPerkategori);
                                    adapter.notifyDataSetChanged();

                                    recyclerView.setAdapter(adapter);
                                }

                            }
                            else
                            {
                                Toast.makeText(LaporanPerKategoriActivity.this, "Tidak ada transaksi", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LaporanPerKategoriActivity.this, "Terjadi kesalahan pada request", Toast.LENGTH_SHORT).show();
                            L_18.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(LaporanPerKategoriActivity.this);
                        }
                        else {
                            Toast.makeText(LaporanPerKategoriActivity.this, "Terjadi kesalahan pada jaringan", Toast.LENGTH_SHORT).show();
                            L_18.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }

    private void setChart(String startdate, String enddate) {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        Map<String, Integer> typeAmountMap = new HashMap<>();

        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.parseColor("#FF8800"));
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        colors.add(Color.GRAY);

        AndroidNetworking.post(Server.URL + "sales_kategori")
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
                            if (response.getString("response").equals("200")){
                                JSONArray jsonArray         = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject object       = jsonArray.getJSONObject(i);
                                    typeAmountMap.put(object.getString("nama_kategori"), Integer.valueOf(object.getString("terjual")));
                                }

                                //input data and fit data into pie chart entry
                                for(String type: typeAmountMap.keySet()){
                                    pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
                                }

                                //collecting the entries with label name
                                PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
                                //setting text size of the value
                                pieDataSet.setValueTextSize(13f);
                                //providing color list for coloring different entries
                                pieDataSet.setColors(colors);
                                //grouping the data set from entry to chart
                                PieData pieData = new PieData(pieDataSet);
                                //showing the value of the entries, default true if not set
                                pieData.setDrawValues(true);
                                pieData.setValueFormatter(new PercentFormatter());

                                pieChart.getDescription().setEnabled(false);
                                pieChart.setDrawHoleEnabled(true);
                                pieChart.setDrawEntryLabels(false);
                                pieChart.setTransparentCircleRadius(58f);
                                pieChart.setEntryLabelColor(Color.BLACK);
                                pieChart.setUsePercentValues(true);
                                pieData.setValueTextColor(Color.BLACK);
                                pieDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
                                pieChart.setData(pieData);
                                pieChart.invalidate();


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(LaporanPerKategoriActivity.this);
                        }
                        else {
                            Toast.makeText(LaporanPerKategoriActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public void set_ui(){
        if (txt_hari_ini_a.isPressed())
        {
            txt_hari_ini_a.setBackgroundResource(R.color.putih);
            txt_hari_ini_a.setTextColor(getResources().getColor(R.color.colorAccent));

            txt_7_hari_a.setBackgroundResource(R.color.colorAccent);
            txt_7_hari_a.setTextColor(getResources().getColor(R.color.putih));

            txt_bulan_ini_a.setBackgroundResource(R.color.colorAccent);
            txt_bulan_ini_a.setTextColor(getResources().getColor(R.color.putih));

            txt_tahun_ini_a.setBackgroundResource(R.color.colorAccent);
            txt_tahun_ini_a.setTextColor(getResources().getColor(R.color.putih));

            txt_filter_tanggal.setBackgroundResource(R.color.colorAccent);
            txt_filter_tanggal.setTextColor(getResources().getColor(R.color.putih));
        }
        else if (txt_7_hari_a.isPressed())
        {
            txt_7_hari_a.setBackgroundResource(R.color.putih);
            txt_7_hari_a.setTextColor(getResources().getColor(R.color.colorAccent));

            txt_hari_ini_a.setBackgroundResource(R.color.colorAccent);
            txt_hari_ini_a.setTextColor(getResources().getColor(R.color.putih));

            txt_bulan_ini_a.setBackgroundResource(R.color.colorAccent);
            txt_bulan_ini_a.setTextColor(getResources().getColor(R.color.putih));

            txt_tahun_ini_a.setBackgroundResource(R.color.colorAccent);
            txt_tahun_ini_a.setTextColor(getResources().getColor(R.color.putih));

            txt_filter_tanggal.setBackgroundResource(R.color.colorAccent);
            txt_filter_tanggal.setTextColor(getResources().getColor(R.color.putih));
        }
        else if (txt_7_hari_a.isPressed())
        {
            txt_7_hari_a.setBackgroundResource(R.color.putih);
            txt_7_hari_a.setTextColor(getResources().getColor(R.color.colorAccent));

            txt_hari_ini_a.setBackgroundResource(R.color.colorAccent);
            txt_hari_ini_a.setTextColor(getResources().getColor(R.color.putih));

            txt_bulan_ini_a.setBackgroundResource(R.color.colorAccent);
            txt_bulan_ini_a.setTextColor(getResources().getColor(R.color.putih));

            txt_tahun_ini_a.setBackgroundResource(R.color.colorAccent);
            txt_tahun_ini_a.setTextColor(getResources().getColor(R.color.putih));

            txt_filter_tanggal.setBackgroundResource(R.color.colorAccent);
            txt_filter_tanggal.setTextColor(getResources().getColor(R.color.putih));
        }
        else if (txt_bulan_ini_a.isPressed())
        {
            txt_bulan_ini_a.setBackgroundResource(R.color.putih);
            txt_bulan_ini_a.setTextColor(getResources().getColor(R.color.colorAccent));

            txt_hari_ini_a.setBackgroundResource(R.color.colorAccent);
            txt_hari_ini_a.setTextColor(getResources().getColor(R.color.putih));

            txt_7_hari_a.setBackgroundResource(R.color.colorAccent);
            txt_7_hari_a.setTextColor(getResources().getColor(R.color.putih));

            txt_tahun_ini_a.setBackgroundResource(R.color.colorAccent);
            txt_tahun_ini_a.setTextColor(getResources().getColor(R.color.putih));

            txt_filter_tanggal.setBackgroundResource(R.color.colorAccent);
            txt_filter_tanggal.setTextColor(getResources().getColor(R.color.putih));
        }
        else if (txt_tahun_ini_a.isPressed())
        {
            txt_tahun_ini_a.setBackgroundResource(R.color.putih);
            txt_tahun_ini_a.setTextColor(getResources().getColor(R.color.colorAccent));

            txt_hari_ini_a.setBackgroundResource(R.color.colorAccent);
            txt_hari_ini_a.setTextColor(getResources().getColor(R.color.putih));

            txt_7_hari_a.setBackgroundResource(R.color.colorAccent);
            txt_7_hari_a.setTextColor(getResources().getColor(R.color.putih));

            txt_bulan_ini_a.setBackgroundResource(R.color.colorAccent);
            txt_bulan_ini_a.setTextColor(getResources().getColor(R.color.putih));

            txt_filter_tanggal.setBackgroundResource(R.color.colorAccent);
            txt_filter_tanggal.setTextColor(getResources().getColor(R.color.putih));
        }
        else if (txt_filter_tanggal.isPressed())
        {
            txt_filter_tanggal.setBackgroundResource(R.color.putih);
            txt_filter_tanggal.setTextColor(getResources().getColor(R.color.colorAccent));

            txt_tahun_ini_a.setBackgroundResource(R.color.colorAccent);
            txt_tahun_ini_a.setTextColor(getResources().getColor(R.color.putih));

            txt_hari_ini_a.setBackgroundResource(R.color.colorAccent);
            txt_hari_ini_a.setTextColor(getResources().getColor(R.color.putih));

            txt_7_hari_a.setBackgroundResource(R.color.colorAccent);
            txt_7_hari_a.setTextColor(getResources().getColor(R.color.putih));

            txt_bulan_ini_a.setBackgroundResource(R.color.colorAccent);
            txt_bulan_ini_a.setTextColor(getResources().getColor(R.color.putih));
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

                txt_judul_filter_a.setText("FILTER TANGGAL");

                set_chartkategori(filter_tanggal, filter_tanggal);
                setChart(filter_tanggal, filter_tanggal);
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
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

}
