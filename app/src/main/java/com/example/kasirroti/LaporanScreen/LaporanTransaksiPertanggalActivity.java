package com.example.kasirroti.LaporanScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.Adapter.AdapterLaporanTransPertanggal;
import com.example.kasirroti.Adapter.MyMarkerView;
import com.example.kasirroti.Helper.SingOut;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.Model.ListLaporanPertanggal;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LaporanTransaksiPertanggalActivity extends AppCompatActivity {

    private LineChart mChart;
    private TextView txt_laporan_jml_trans_perbulan, txt_laporan_pendapatan_perbulan, txt_kosong_bulanan;
    private CardView C_8, C_9;
    private ProgressBar loading_perbulan;

    private RecyclerView recycler_all_transaksi_perbulan;
    private ArrayList<ListLaporanPertanggal> listLaporanPertanggal;
    private AdapterLaporanTransPertanggal adapter;

    private ProgressDialog dialog;

    private String tanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_transaksi_pertanggal);

        Toolbar toolbar = findViewById(R.id.toolbar_trans_perbulan);
        toolbar.setTitle("Laporan Transaksi");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_laporan_jml_trans_perbulan          = findViewById(R.id.txt_laporan_jml_trans_perbulan);
        txt_laporan_pendapatan_perbulan         = findViewById(R.id.txt_laporan_pendapatan_perbulan);
        loading_perbulan                        = findViewById(R.id.loading_perbulan);
        txt_kosong_bulanan                      = findViewById(R.id.txt_kosong_bulanan);

        C_8         = findViewById(R.id.C_8);
        C_9         = findViewById(R.id.C_9);

        txt_laporan_jml_trans_perbulan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_laporan_jml_trans_perbulan.setBackgroundResource(R.drawable.bg_laporan_kiri_selected);
                txt_laporan_pendapatan_perbulan.setBackgroundResource(R.drawable.bg_laporan_kanan);
            }
        });

        txt_laporan_pendapatan_perbulan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_laporan_jml_trans_perbulan.setBackgroundResource(R.drawable.bg_laporan_kiri);
                txt_laporan_pendapatan_perbulan.setBackgroundResource(R.drawable.bg_laporan_kanan_selected);
            }
        });

        listLaporanPertanggal               = new ArrayList<>();
        recycler_all_transaksi_perbulan     = findViewById(R.id.recycler_all_transaksi_perbulan);
        recycler_all_transaksi_perbulan.setLayoutManager(new LinearLayoutManager(this));
        recycler_all_transaksi_perbulan.setItemAnimator(new DefaultItemAnimator());
        recycler_all_transaksi_perbulan.setHasFixedSize(true);

        mChart = findViewById(R.id.lineChart_perbulan);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);

        MyMarkerView mv = new MyMarkerView(getApplicationContext(), R.layout.custom_marker_view);
        mv.setChartView(mChart);
        mChart.setMarker(mv);

        Intent intent                       = getIntent();
        tanggal                             = intent.getStringExtra("tanggal");
        String thn                          = intent.getStringExtra("tahun");

        Calendar calendar   = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date monthFirstDay  = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthLastDay   = calendar.getTime();


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String startDateStr = df.format(monthFirstDay);
        String endDateStr   = df.format(monthLastDay);

        loading_perbulan.setVisibility(View.GONE);

        if (tanggal.equals(""))
        {
            setData(startDateStr, endDateStr);
        }
        else{
            setData(thn + "-"+ tanggal + "-01", thn + "-"+ tanggal + "-31");
        }

    }

    private void setData(String startdate, String enddate) {
        recycler_all_transaksi_perbulan.setAdapter(null);
        listLaporanPertanggal.clear();

        dialog              = new ProgressDialog(this);
        dialog.show();
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        C_8.setVisibility(View.GONE);
        C_9.setVisibility(View.GONE);

        AndroidNetworking.post(Server.URL + "transaksi_report")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addHeaders("Accept", "application/json")
                .addBodyParameter("id_outlet", HomeActivity.id_outlet)
                .addBodyParameter("startdate", startdate)
                .addBodyParameter("enddate", enddate)
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

                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject object       = jsonArray.getJSONObject(i);

                                    ListLaporanPertanggal data  = new ListLaporanPertanggal(
                                            object.getString("tanggal"),
                                            object.getString("tanggal"),
                                            object.getString("tanggal"),
                                            object.getString("jml"),
                                            object.getString("total")
                                    );

                                    listLaporanPertanggal.add(data);

                                }

                                adapter         = new AdapterLaporanTransPertanggal(LaporanTransaksiPertanggalActivity.this, listLaporanPertanggal);
                                adapter.notifyDataSetChanged();

                                adapter.setOnItemClickListener(new AdapterLaporanTransPertanggal.recyclerViewClickListener() {
                                    @Override
                                    public void onClick(View v, int position) {

                                        DateTimeFormatter formatter = null;

                                        String date         = listLaporanPertanggal.get(position).getTanggal();
                                        try {
                                            SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
                                            Date date1      = format.parse(date);
                                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
//                                            Toast.makeText(LaporanTransaksiPertanggalActivity.this, format1.format(date1).toString()
//                                                    , Toast.LENGTH_SHORT).show();

                                            Intent intent       = new Intent(LaporanTransaksiPertanggalActivity.this, LaporanTransaksiPerhariActivity.class);
                                            intent.putExtra("tanggal", format1.format(date1).toString());
                                            startActivity(intent);

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                recycler_all_transaksi_perbulan.setAdapter(adapter);
                                renderData(startdate, enddate);
                            }
                            else
                            {
                                loading_perbulan.setVisibility(View.GONE);
                                dialog.dismiss();
                                C_8.setVisibility(View.VISIBLE);
                                C_9.setVisibility(View.VISIBLE);
                                txt_kosong_bulanan.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading_perbulan.setVisibility(View.GONE);
                            dialog.dismiss();
                            C_8.setVisibility(View.VISIBLE);
                            C_9.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(LaporanTransaksiPertanggalActivity.this);
                        }
                        else {
                            loading_perbulan.setVisibility(View.GONE);
                            dialog.dismiss();
                            C_8.setVisibility(View.VISIBLE);
                            C_9.setVisibility(View.VISIBLE);
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

    private void setData(String startdate, String enddate, YAxis axis,XAxis xAxis) {

        ArrayList<Entry> values = new ArrayList<>();

        AndroidNetworking.post(Server.URL + "chart_day")
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
                            String res_kode     = response.getString("response");
                            if(res_kode.equals("200"))
                            {
                                JSONArray array     = response.getJSONArray("data");
                                String teritinggi   = response.getString("tertinggi");
                                int max             = Integer.parseInt(teritinggi);
                                float a             = Float.parseFloat(String.valueOf(max));
                                axis.setAxisMaximum(a);

                                values.add(new Entry(0, 0));

                                for(int i = 0; i < array.length(); i++)
                                {
                                    JSONObject object = array.getJSONObject(i);
                                    float x         = Float.parseFloat(object.getString("x"));
                                    float y         = Float.parseFloat(object.getString("y"));

                                    JSONObject x_max= array.getJSONObject(array.length() - 1); //cari nilai x tertinggi
                                    JSONObject x_min= array.getJSONObject(0); //cari nilai x terendah
                                    JSONObject y_min= array.getJSONObject(0); //cari nilai y terendah

                                    if (array.length() <= 1){
                                        xAxis.setAxisMinimum(0f);
                                        xAxis.setAxisMaximum(5f);
                                        axis.setAxisMaximum(a);
                                        axis.setAxisMinimum(0f);
                                        xAxis.setLabelCount(4);
                                    }
                                    else if (array.length() <= 5){
                                        xAxis.setAxisMinimum(Float.parseFloat(x_min.getString("x")));
                                        xAxis.setAxisMaximum(Float.parseFloat(x_max.getString("x")));
                                        axis.setAxisMaximum(a);
                                        axis.setAxisMinimum(Float.parseFloat(y_min.getString("y")));
                                        xAxis.setLabelCount(array.length());
                                        axis.setLabelCount(4);
                                    }
                                    else {
                                        xAxis.setAxisMinimum(Float.parseFloat(x_min.getString("x")));
                                        xAxis.setAxisMaximum(Float.parseFloat(x_max.getString("x")));
                                        axis.setAxisMaximum(a);
                                        axis.setAxisMinimum(Float.parseFloat(y_min.getString("y")));
                                        xAxis.setLabelCount(6);
                                        axis.setLabelCount(4);
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
                                    set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                                    set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                                    set1.setFormSize(15.f);
                                    set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

                                    if (Utils.getSDKInt() >= 18) {
                                        Drawable drawable = ContextCompat.getDrawable(LaporanTransaksiPertanggalActivity.this, R.drawable.fade_orange);
                                        set1.setFillDrawable(drawable);
                                    } else {
                                        set1.setFillColor(Color.DKGRAY);
                                    }
                                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                                    dataSets.add(set1);
                                    LineData data = new LineData(dataSets);
                                    mChart.setData(data);
                                }

                                loading_perbulan.setVisibility(View.GONE);
                                dialog.dismiss();
                                C_8.setVisibility(View.VISIBLE);
                                C_9.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                loading_perbulan.setVisibility(View.GONE);
                                dialog.dismiss();
                                C_8.setVisibility(View.VISIBLE);
                                C_9.setVisibility(View.VISIBLE);
                                Toast.makeText(LaporanTransaksiPertanggalActivity.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading_perbulan.setVisibility(View.GONE);
                            dialog.dismiss();
                            C_8.setVisibility(View.VISIBLE);
                            C_9.setVisibility(View.VISIBLE);
                            Toast.makeText(LaporanTransaksiPertanggalActivity.this, "error 1", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(LaporanTransaksiPertanggalActivity.this);
                        }
                        else {
                            loading_perbulan.setVisibility(View.GONE);
                            dialog.dismiss();
                            C_8.setVisibility(View.VISIBLE);
                            C_9.setVisibility(View.VISIBLE);
                            Toast.makeText(LaporanTransaksiPertanggalActivity.this, "error 2", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}