package com.example.kasirroti.LaporanScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.Adapter.AdapterFilterMetodeBayar;
import com.example.kasirroti.Adapter.AdapterLaporanTransPerhari;
import com.example.kasirroti.Adapter.MyMarkerView;
import com.example.kasirroti.TransaksiScreen.DetailTransaksiActivity;
import com.example.kasirroti.Helper.SingOut;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.Model.ListLaporanPerhari;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.example.kasirroti.Helper.Tanggal;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LaporanTransaksiPerhariActivity extends AppCompatActivity {

    private LineChart mChart;
    private TextView txt_kosong;
    private CardView C_10, C_11;
    private ProgressBar loading_perhari;
    private ImageView img_filter;
    private LinearLayout linearMetode;
    private Button btn_filter;

    private RecyclerView recycler_all_transaksi_perhari, recycler_metode;
    private ArrayList<ListLaporanPerhari> listLaporanPerhari;
    private ArrayList<String> listMetode;
    public static ArrayList<String> listFilter;
    private AdapterFilterMetodeBayar adapterFilterMetodeBayar;

    public static AdapterLaporanTransPerhari adapter;

    private ProgressDialog dialog;

    String tanggal      = "";
    Tanggal tgl         = new Tanggal();

    int max;
    String status, nama_metode;
    boolean filter      = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_transaksi_perhari);

        Toolbar toolbar = findViewById(R.id.toolbar_trans_perhari);
        toolbar.setTitle("Laporan Transaksi");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_kosong                              = findViewById(R.id.txt_kosong);
        loading_perhari                         = findViewById(R.id.loading_perhari);
        img_filter                              = findViewById(R.id.img_filter);
        linearMetode                            = findViewById(R.id.linear_metode);
        btn_filter                              = findViewById(R.id.btn_filter_metode);

        listFilter = new ArrayList<>();

        C_10         = findViewById(R.id.C_10);
        C_11         = findViewById(R.id.C_11);

        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filter){
                    filter                      = false;
                    linearMetode.setVisibility(View.VISIBLE);
                }else{
                    filter                      = true;
                    linearMetode.setVisibility(View.GONE);
                }
            }
        });

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson               = new GsonBuilder().create();
                JsonArray myCustomArray = gson.toJsonTree(listFilter).getAsJsonArray();

                if (tanggal.equals(""))
                {
                    set_data(tgl.getTanggal2(), tgl.getTanggal2(), myCustomArray.toString());
//            set_data("2021-09-14", "2021-09-14");
                }
                else{
                    set_data(tanggal, tanggal,  myCustomArray.toString());
                }
            }
        });

        listLaporanPerhari                  = new ArrayList<>();
        recycler_all_transaksi_perhari      = findViewById(R.id.recycler_all_transaksi_perhari);
        recycler_all_transaksi_perhari.setLayoutManager(new LinearLayoutManager(this));
        recycler_all_transaksi_perhari.setItemAnimator(new DefaultItemAnimator());
        recycler_all_transaksi_perhari.setHasFixedSize(true);

        listMetode                              = new ArrayList<>();
        recycler_metode                         = findViewById(R.id.recycler_metode);
        recycler_metode.setLayoutManager(new GridLayoutManager(this, 3));
        recycler_metode.setItemAnimator(new DefaultItemAnimator());
        recycler_metode.setHasFixedSize(true);

        Intent intent                       = getIntent();
        tanggal                             = intent.getStringExtra("tanggal");

        loading_perhari.setVisibility(View.GONE);

        if (tanggal.equals(""))
        {
            set_data(tgl.getTanggal2(), tgl.getTanggal2(), "");
//            set_data("2021-09-14", "2021-09-14");
        }
        else{
            set_data(tanggal, tanggal, "");
        }


        mChart = findViewById(R.id.lineChart_perhari);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);

        MyMarkerView mv = new MyMarkerView(getApplicationContext(), R.layout.custom_marker_view);
        mv.setChartView(mChart);
        mChart.setMarker(mv);
        setFilterMetode();

    }

    private void set_data(String startdate, String enddate, String filter) {

        recycler_all_transaksi_perhari.setAdapter(null);
        listLaporanPerhari.clear();

//        loading_perhari.setVisibility(View.VISIBLE);

        dialog              = new ProgressDialog(this);
        dialog.show();
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        C_10.setVisibility(View.GONE);
        C_11.setVisibility(View.GONE);

        AndroidNetworking.post(Server.URL + "transaksi_report_harian")
                .addBodyParameter("id_outlet", HomeActivity.id_outlet)
                .addBodyParameter("startdate", startdate)
                .addBodyParameter("enddate", enddate)
                .addBodyParameter("filter", filter)
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res_kode         = response.getString("response");
                            if (res_kode.equals("200"))
                            {
                                JSONArray jsonArray         = response.getJSONArray("data");
                                JSONObject object;

                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    object                  = jsonArray.getJSONObject(i);

                                    ListLaporanPerhari data  = new ListLaporanPerhari(
                                            object.getString("jam"),
                                            object.getString("tgl"),
                                            object.getString("tgl"),
                                            object.getString("tgl"),
                                            object.getString("order_id"),
                                            object.getString("pendapatan"),
                                            object.getString("id"),
                                            object.getString("status")
                                    );

                                    listLaporanPerhari.add(data);

                                }

                                adapter         = new AdapterLaporanTransPerhari(LaporanTransaksiPerhariActivity.this, listLaporanPerhari);
                                adapter.notifyDataSetChanged();
                                adapter.setOnItemClickListener(new AdapterLaporanTransPerhari.recyclerViewClickListener() {
                                    @Override
                                    public void onClick(View v, int position) {
                                        Intent intent       = new Intent(LaporanTransaksiPerhariActivity.this, DetailTransaksiActivity.class);
                                        intent.putExtra("id", listLaporanPerhari.get(position).getId());
                                        startActivity(intent);

                                    }
                                });

                                recycler_all_transaksi_perhari.setAdapter(adapter);
                                txt_kosong.setVisibility(View.GONE);

                                renderData(startdate, enddate);
                            }
                            else
                            {
                                loading_perhari.setVisibility(View.GONE);
                                dialog.dismiss();
                                C_10.setVisibility(View.VISIBLE);
                                C_11.setVisibility(View.VISIBLE);
                                txt_kosong.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            loading_perhari.setVisibility(View.GONE);
                            dialog.dismiss();
                            C_10.setVisibility(View.VISIBLE);
                            C_11.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                            Toast.makeText(LaporanTransaksiPerhariActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(LaporanTransaksiPerhariActivity.this);
                        }
                        else {
                            loading_perhari.setVisibility(View.GONE);
                            dialog.dismiss();
                            C_10.setVisibility(View.VISIBLE);
                            C_11.setVisibility(View.VISIBLE);
                            Toast.makeText(LaporanTransaksiPerhariActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void renderData(String startdate, String enddate) {

        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawLimitLinesBehindData(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.isDrawZeroLineEnabled();
        leftAxis.setStartAtZero(true);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getXAxis().setDrawLabels(true);
        mChart.getAxisRight().setEnabled(false);
        mChart.getAxisLeft().setDrawGridLines(true);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getAxisLeft().setDrawAxisLine(false);

        mChart.getLegend().setEnabled(false);
        mChart.getDescription().setEnabled(false);

        setData(startdate, enddate, leftAxis, xAxis);
    }

    private void setData(String startdate, String enddate, YAxis axis, XAxis xAxis) {

        ArrayList<Entry> values = new ArrayList<>();

        AndroidNetworking.post(Server.URL + "chart_general")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addBodyParameter("startdate", startdate)
                .addBodyParameter("enddate", enddate)
                .addBodyParameter("id_outlet", HomeActivity.id_outlet)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res_kode     = response.getString("response");
                            if(res_kode.equals("200"))
                            {
                                JSONArray array     = response.getJSONArray("data");
                                String teritinggi   = response.getString("tertinggi");
                                max                 = Integer.parseInt(teritinggi) + 2;
                                float a             = Float.parseFloat(String.valueOf(max));
                                axis.setAxisMaximum(a);

                                for(int i = 0; i < array.length(); i++)
                                {
                                    JSONObject object = array.getJSONObject(i);
                                    float x         = Float.parseFloat(object.getString("x"));
                                    float y         = Float.parseFloat(object.getString("y"));

                                    JSONObject x_max= array.getJSONObject(array.length() - 1); //cari nilai x tertinggi
                                    JSONObject x_min= array.getJSONObject(0); //cari nilai x terendah
                                    JSONObject y_min= array.getJSONObject(0); //cari nilai y terendah

                                    if(array.length() > 1){
                                        xAxis.setAxisMinimum(Float.parseFloat(x_min.getString("x")));
                                        xAxis.setAxisMaximum(Float.parseFloat(x_max.getString("x")));
                                        xAxis.setLabelCount(4);
                                        axis.setLabelCount(4);
                                    }else{
                                        xAxis.setAxisMinimum(0f);
                                        xAxis.setAxisMaximum(24f);
                                        xAxis.setLabelCount(3);
                                        axis.setLabelCount(3);
                                        axis.setAxisMinimum(0f);
                                        axis.setAxisMaximum(a);
                                    }
                                    values.add(new Entry(x, y));
                                }

                                LineDataSet set1;
                                if (mChart.getData() != null &&
                                        mChart.getData().getDataSetCount() > 0) {
                                    set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                                    set1.setValues(values);
                                    mChart.getData().notifyDataChanged();
                                    mChart.notifyDataSetChanged();
                                    mChart.setAutoScaleMinMaxEnabled(true);
                                } else {
                                    set1 = new LineDataSet(values, "");
                                    set1.setDrawIcons(false);
                                    set1.setColor(Color.BLUE);
                                    set1.setCircleColor(Color.BLUE);
                                    set1.setLineWidth(1f);
                                    set1.setCircleRadius(3f);
                                    set1.setDrawCircleHole(false);
                                    set1.setValueTextSize(9f);
                                    set1.setDrawFilled(true);
                                    set1.setFormLineWidth(1f);
                                    set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                                    set1.setFormSize(15.f);
                                    set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

                                    if (Utils.getSDKInt() >= 18) {
                                        Drawable drawable = ContextCompat.getDrawable(LaporanTransaksiPerhariActivity.this, R.drawable.fade_orange);
                                        set1.setFillDrawable(drawable);
                                    } else {
                                        set1.setFillColor(Color.DKGRAY);
                                    }
                                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                                    dataSets.add(set1);
                                    LineData data = new LineData(dataSets);
                                    mChart.setData(data);
                                }

                                loading_perhari.setVisibility(View.GONE);
                                dialog.dismiss();
                                C_10.setVisibility(View.VISIBLE);
                                C_11.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                loading_perhari.setVisibility(View.GONE);
                                dialog.dismiss();
                                C_10.setVisibility(View.VISIBLE);
                                C_11.setVisibility(View.VISIBLE);
                                Toast.makeText(LaporanTransaksiPerhariActivity.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading_perhari.setVisibility(View.GONE);
                            dialog.dismiss();
                            C_10.setVisibility(View.VISIBLE);
                            C_11.setVisibility(View.VISIBLE);
                            Toast.makeText(LaporanTransaksiPerhariActivity.this, "error 1", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        loading_perhari.setVisibility(View.GONE);
                        dialog.dismiss();
                        C_10.setVisibility(View.VISIBLE);
                        C_11.setVisibility(View.VISIBLE);
                        Toast.makeText(LaporanTransaksiPerhariActivity.this, "error 2", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setFilterMetode(){
        AndroidNetworking.get(Server.URL + "list_pembayaran")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("200")){
                                JSONArray array         = response.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++)
                                {
                                    JSONObject object   = array.getJSONObject(i);
                                    status              = object.getString("status");
                                    nama_metode         = object.getString("nama_metode");

                                    if (status.equals("1")){
                                        listMetode.add(nama_metode);
                                    }
                                }
                                adapterFilterMetodeBayar= new AdapterFilterMetodeBayar(LaporanTransaksiPerhariActivity.this, listMetode);
                                adapterFilterMetodeBayar.notifyDataSetChanged();
                                recycler_metode.setAdapter(adapterFilterMetodeBayar);
                            }else {
                                FancyToast.makeText(
                                        LaporanTransaksiPerhariActivity.this,
                                        "Metode Pembayaran Kosong",
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR,
                                        false
                                ).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();FancyToast.makeText(
                                    LaporanTransaksiPerhariActivity.this,
                                    "Gagal Memuat Data!",
                                    FancyToast.LENGTH_SHORT,
                                    FancyToast.ERROR,
                                    false
                            ).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        FancyToast.makeText(
                                LaporanTransaksiPerhariActivity.this,
                                "Jaringan Bermasalah!",
                                FancyToast.LENGTH_SHORT,
                                FancyToast.ERROR,
                                false
                        ).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ekspor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id      = item.getItemId();

        switch (id)
        {
            case R.id.menu_ekspor:
                if (hasCameraPermissions()) {
                    Intent eIntent          = new Intent(LaporanTransaksiPerhariActivity.this, ExportActivity.class);
                    startActivity(eIntent);
                }
                else{
                    requestPermission();
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }



    private boolean hasCameraPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.CAMERA},
                10);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}