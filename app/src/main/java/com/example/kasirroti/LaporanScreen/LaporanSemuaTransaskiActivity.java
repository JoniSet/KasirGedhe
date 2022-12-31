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
import com.example.kasirroti.Adapter.AdapterLaporanTransPertahun;
import com.example.kasirroti.Adapter.MyMarkerView;
import com.example.kasirroti.Helper.SingOut;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.Model.ListLaporanPertahun;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.example.kasirroti.Helper.Tanggal;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LaporanSemuaTransaskiActivity extends AppCompatActivity {

    private LineChart mChart;
    private TextView txt_laporan_jml_trans, txt_laporan_pendapatan, txt_kosong_transaksi;

    private RecyclerView recycler_all_transaksi;
    private CardView C_8, C_9;
    private ProgressBar loading_perbulan;

    private ArrayList<ListLaporanPertahun> listLaporanPertahun;
    private AdapterLaporanTransPertahun adapter;

    private ProgressDialog dialog;

    Tanggal tanggal     = new Tanggal();

    int max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_semua_transaski);

        Toolbar toolbar = findViewById(R.id.toolbar_trans);
        toolbar.setTitle("Laporan Transaksi");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_laporan_jml_trans                   = findViewById(R.id.txt_laporan_jml_trans);
        txt_laporan_pendapatan                  = findViewById(R.id.txt_laporan_pendapatan);
        loading_perbulan                        = findViewById(R.id.loading_transaski);
        txt_kosong_transaksi                    = findViewById(R.id.txt_kosong_transaksi);

        C_8         = findViewById(R.id.C_14);
        C_9         = findViewById(R.id.C_15);

        txt_laporan_jml_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_laporan_jml_trans.setBackgroundResource(R.drawable.bg_laporan_kiri_selected);
                txt_laporan_pendapatan.setBackgroundResource(R.drawable.bg_laporan_kanan);
            }
        });

        txt_laporan_pendapatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_laporan_jml_trans.setBackgroundResource(R.drawable.bg_laporan_kiri);
                txt_laporan_pendapatan.setBackgroundResource(R.drawable.bg_laporan_kanan_selected);
            }
        });

        listLaporanPertahun                     = new ArrayList<>();
        recycler_all_transaksi                  = findViewById(R.id.recycler_all_transaksi);
        recycler_all_transaksi.setLayoutManager(new LinearLayoutManager(this));
        recycler_all_transaksi.setItemAnimator(new DefaultItemAnimator());
        recycler_all_transaksi.setHasFixedSize(true);

        mChart = findViewById(R.id.lineChart1);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);

        MyMarkerView mv = new MyMarkerView(getApplicationContext(), R.layout.custom_marker_view);
        mv.setChartView(mChart);
        mChart.setMarker(mv);

        loading_perbulan.setVisibility(View.GONE);
        setData("2020-01-01", "2030-12-31");
    }

    private void setData(String startdate, String enddate) {
        recycler_all_transaksi.setAdapter(null);
        listLaporanPertahun.clear();

//        loading_perbulan.setVisibility(View.VISIBLE);
        dialog              = new ProgressDialog(this);
        dialog.show();
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        C_8.setVisibility(View.GONE);
        C_9.setVisibility(View.GONE);

        AndroidNetworking.post(Server.URL + "transaksi_report_tahun")
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

                                    ListLaporanPertahun data  = new ListLaporanPertahun(
                                            object.getString("tanggal"),
                                            object.getString("jml"),
                                            object.getString("total")
                                    );

                                    listLaporanPertahun.add(data);

                                }

                                adapter         = new AdapterLaporanTransPertahun(LaporanSemuaTransaskiActivity.this, listLaporanPertahun);
                                adapter.notifyDataSetChanged();

                                adapter.setOnItemClickListener(new AdapterLaporanTransPertahun.recyclerViewClickListener() {
                                    @Override
                                    public void onClick(View v, int position) {
//                                        Toast.makeText(LaporanSemuaTransaskiActivity.this, listLaporanPertahun.get(position).getBulan()
//                                                , Toast.LENGTH_SHORT).show();
                                        Intent intent       = new Intent(LaporanSemuaTransaskiActivity.this, LaporanTransaksiPerbulanActivity.class);
                                        intent.putExtra("tanggal", listLaporanPertahun.get(position).getBulan());
                                        startActivity(intent);
                                    }
                                });

                                recycler_all_transaksi.setAdapter(adapter);
                                renderData(startdate, enddate);
                            }
                            else
                            {
                                dialog.dismiss();
                                loading_perbulan.setVisibility(View.GONE);
                                C_8.setVisibility(View.VISIBLE);
                                C_9.setVisibility(View.VISIBLE);
                                txt_kosong_transaksi.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            loading_perbulan.setVisibility(View.GONE);
                            C_8.setVisibility(View.VISIBLE);
                            C_9.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(LaporanSemuaTransaskiActivity.this);
                        }
                        else {
                            dialog.dismiss();
                            loading_perbulan.setVisibility(View.GONE);
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
        xAxis.setSpaceMax(1f);
        xAxis.setSpaceMin(1f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.isDrawZeroLineEnabled();
        leftAxis.setStartAtZero(true);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) Math.floor(value));
            }
        });
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getXAxis().setDrawLabels(true);
        mChart.getAxisRight().setEnabled(false);
        mChart.getAxisLeft().setDrawGridLines(true);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getAxisLeft().setDrawAxisLine(false);

        mChart.getLegend().setEnabled(false);
        mChart.getDescription().setEnabled(false);

        setChart(startdate, enddate, leftAxis, xAxis);
    }

    private void setChart(String startdate, String enddate, YAxis axis, XAxis xAxis) {
        ArrayList<Entry> values = new ArrayList<>();

        AndroidNetworking.post(Server.URL + "chart_year")
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
                                max                 = Integer.parseInt(teritinggi) + 3;
                                int maxx            = Integer.parseInt(tanggal.getTahun()) + 3;
                                int ymin            = Integer.parseInt(teritinggi) - 10;
                                float a             = Float.parseFloat(String.valueOf(max));
                                float b             = Float.parseFloat(String.valueOf(maxx));
                                float c             = Float.parseFloat(String.valueOf(ymin));

                                JSONObject x_max    = array.getJSONObject(array.length() - 1); //cari nilai x tertinggi
                                JSONObject x_min    = array.getJSONObject(0); //cari nilai x terendah
                                JSONObject y_min    = array.getJSONObject(0); //cari nilai y terendah

                                if (array.length() <= 1){
                                    xAxis.setAxisMinimum(2020f);
                                    xAxis.setAxisMaximum(2023f);
                                    axis.setAxisMaximum(a);
                                    axis.setAxisMinimum(0f);
                                    xAxis.setLabelCount(3);
                                }
                                else {
                                    xAxis.setAxisMinimum(Float.parseFloat(x_min.getString("x")));
                                    xAxis.setAxisMaximum(Float.parseFloat(x_max.getString("x")));
                                    axis.setAxisMaximum(a);
                                    axis.setAxisMinimum(0f);
                                    xAxis.setLabelCount(array.length());
                                }

                                for(int i = 0; i < array.length(); i++)
                                {
                                    JSONObject object = array.getJSONObject(i);
                                    float x         = Float.parseFloat(object.getString("x"));
                                    float y         = Float.parseFloat(object.getString("y"));

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
                                    set1.setCircleRadius(4f);
                                    set1.setDrawCircleHole(false);
                                    set1.setValueTextSize(9f);
                                    set1.setDrawFilled(true);
                                    set1.setFormLineWidth(1f);
                                    set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                                    set1.setFormSize(15.f);
                                    set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

                                    if (Utils.getSDKInt() >= 18) {
                                        Drawable drawable = ContextCompat.getDrawable(LaporanSemuaTransaskiActivity.this, R.drawable.fade_orange);
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
                                Toast.makeText(LaporanSemuaTransaskiActivity.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading_perbulan.setVisibility(View.GONE);
                            dialog.dismiss();
                            C_8.setVisibility(View.VISIBLE);
                            C_9.setVisibility(View.VISIBLE);
                            Toast.makeText(LaporanSemuaTransaskiActivity.this, "error 1", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(LaporanSemuaTransaskiActivity.this);
                        }
                        else {
                            loading_perbulan.setVisibility(View.GONE);
                            dialog.dismiss();
                            C_8.setVisibility(View.VISIBLE);
                            C_9.setVisibility(View.VISIBLE);
                            Toast.makeText(LaporanSemuaTransaskiActivity.this, "error 2", Toast.LENGTH_SHORT).show();
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
