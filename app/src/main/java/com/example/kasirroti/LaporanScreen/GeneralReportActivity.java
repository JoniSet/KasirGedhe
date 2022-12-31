package com.example.kasirroti.LaporanScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.Adapter.AdapterDetailMetode;
import com.example.kasirroti.Adapter.AdapterLaporanPerproduk;
import com.example.kasirroti.Adapter.MyMarkerView;
import com.example.kasirroti.Helper.SingOut;
import com.example.kasirroti.Helper.SqliteHelper;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.Model.ListDetailMetode;
import com.example.kasirroti.Model.ListLaporanPerproduk;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.example.kasirroti.SessionManager.SessionManager;
import com.example.kasirroti.SessionManager.SessionManagerUser;
import com.example.kasirroti.Helper.Tanggal;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GeneralReportActivity extends AppCompatActivity {

    private RelativeLayout R_pilih_hari;
    private TextView T_pilih_hari, T_judul_laporan, txt_tgl_laporan, txt_jml_trans, txt_pendapatan;
    private LinearLayout L_general;
    private CardView C_tanggal;
    private ProgressBar loading_general;

    private int jml_trans;
    int sesi = 0;

    private SqliteHelper sqliteHelper;

    String t, m, y;
    int max;

    private LineChart mChart;

    private RecyclerView list_metode, list_pergood;
    ArrayList<ListDetailMetode> listMetode;
    ArrayList<ListLaporanPerproduk> listLaporanPerproduk;

    Tanggal tanggal         = new Tanggal();

    SessionManagerUser sessionManagerUser;
    SessionManager sessionManager;
    public static String token, id_outlet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_report);

        Toolbar toolbar         = findViewById(R.id.toolbar_general);
        toolbar.setTitle("Laporan Penjualan");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManagerUser      = new SessionManagerUser(this);
        HashMap<String, String> map = sessionManagerUser.getDetailLogin();

        sessionManager          = new SessionManager(this);
        HashMap<String, String> map1 = sessionManager.getDetailLogin();

        token                   = map.get(sessionManagerUser.KEY_TOKEN);
        id_outlet               = map1.get(sessionManager.KEY_ID);

        R_pilih_hari            = findViewById(R.id.R_pilih_hari);
        T_pilih_hari            = findViewById(R.id.T_pilih_hari);
        T_judul_laporan         = findViewById(R.id.T_judul_laporan);
        txt_tgl_laporan         = findViewById(R.id.txt_tgl_laporan);
        txt_jml_trans           = findViewById(R.id.txt_jml_trans);
        L_general               = findViewById(R.id.L_general);
        loading_general         = findViewById(R.id.loading_general);
        C_tanggal               = findViewById(R.id.C_tanggal);

        listMetode              = new ArrayList<>();
        list_metode             = findViewById(R.id.list_metode);
        list_metode.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        list_metode.setItemAnimator(new DefaultItemAnimator());
        list_metode.setHasFixedSize(true);

        listLaporanPerproduk    = new ArrayList<>();
        list_pergood            = findViewById(R.id.list_pergood);
        list_pergood.setLayoutManager(new LinearLayoutManager(this));
        list_pergood.setItemAnimator(new DefaultItemAnimator());
        list_pergood.setHasFixedSize(true);

        txt_pendapatan          = findViewById(R.id.txt_pendapatan);

        T_pilih_hari.setTag("1");
        txt_tgl_laporan.setText(tanggal.getTanggal());

        R_pilih_hari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GeneralReportActivity.this);
                LayoutInflater inflater     = getLayoutInflater();
                View view                   = inflater.inflate(R.layout.dialog_hari, null);
                builder.setView(view);
                builder.setCancelable(true);

                Dialog dialog               = builder.create();
                TextView a, b, c;

                a                           = view.findViewById(R.id.txt_ini);
                b                           = view.findViewById(R.id.txt_kemarin);
                c                           = view.findViewById(R.id.txt_custom);

                a.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        T_pilih_hari.setText(a.getText().toString());
                        T_pilih_hari.setTag("1");
                        T_judul_laporan.setText("Laporan Hari Ini");
                        txt_tgl_laporan.setText(tanggal.getTanggal());
                        setLaporan(tanggal.getTanggal2(), tanggal.getTanggal2());
                        setting_metode_bayar(tanggal.getTanggal2(), tanggal.getTanggal2());
                        L_general.setVisibility(View.GONE);
                        loading_general.setVisibility(View.VISIBLE);
                    }
                });

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        T_pilih_hari.setText(b.getText().toString());
                        T_pilih_hari.setTag("2");
                        T_judul_laporan.setText("Laporan Kemarin");
                        setLaporan(tanggal.getBulan() + tanggal.getMinggu(), tanggal.getBulan() + tanggal.getMinggu());
                        setting_metode_bayar(tanggal.getBulan() + tanggal.getMinggu(), tanggal.getBulan() + tanggal.getMinggu());
                        L_general.setVisibility(View.GONE);
                        loading_general.setVisibility(View.VISIBLE);
                    }
                });

                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        C_tanggal.setVisibility(View.VISIBLE);

                        CalendarView calendarView       = (CalendarView) findViewById(R.id.calendar);
                        Button button_oke               = (Button)findViewById(R.id.btn_oke);
                        Button button_batal             = (Button)findViewById(R.id.btn_batal);
                        EditText edt_tglMulai           = (EditText)findViewById(R.id.edt_tglMulai);
                        EditText edt_tglAkhir           = (EditText)findViewById(R.id.edt_tglAkhir);

                        edt_tglMulai.setVisibility(View.GONE);
                        edt_tglAkhir.setVisibility(View.GONE);
                        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

                            @Override
                            public void onSelectedDayChange(CalendarView view, int year, int month,
                                                            int dayOfMonth) {

                                y = String.valueOf(year);
                                m = String.valueOf(month + 1);
                                t = String.valueOf(dayOfMonth);

                                if (m.length() == 1)
                                {
                                    m   = "0" + m;
                                }

                                if (t.length() == 1)
                                {
                                    t   = "0" + t;
                                }

                            }
                        });

                        button_batal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (sesi == 0)
                                {
                                    dialog.dismiss();
                                    C_tanggal.setVisibility(View.GONE);
                                }
                                else if (sesi == 1)
                                {
                                    button_batal.setText("Tutup");
                                    edt_tglMulai.setText(null);
                                    sesi            = 0;
                                }
                                else {
                                    edt_tglAkhir.setText(null);
                                    sesi            = 1;
                                }
                            }
                        });

                        button_oke.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                edt_tglMulai.setText(y + "-" + m + "-" + t);
                                edt_tglAkhir.setText(y + "-" + m + "-" + t);
                                T_pilih_hari.setText(c.getText().toString());
                                T_pilih_hari.setTag("3");
                                T_judul_laporan.setText("Laporan Tangal");

                                setLaporan(edt_tglMulai.getText().toString(), edt_tglAkhir.getText().toString());
                                setting_metode_bayar(edt_tglMulai.getText().toString(), edt_tglAkhir.getText().toString());
                                L_general.setVisibility(View.GONE);
                                loading_general.setVisibility(View.VISIBLE);

                                dialog.dismiss();
                                C_tanggal.setVisibility(View.GONE);
                                L_general.setVisibility(View.GONE);
                                loading_general.setVisibility(View.VISIBLE);

                                edt_tglMulai.setText(null);
                                edt_tglAkhir.setText(null);
                            }
                        });

                    }
                });

                dialog.show();

            }
        });

        setLaporan(tanggal.getTanggal2(), tanggal.getTanggal2());
        setting_metode_bayar(tanggal.getTanggal2(), tanggal.getTanggal2());

        mChart              = findViewById(R.id.lineChart2);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);

        MyMarkerView mv = new MyMarkerView(getApplicationContext(), R.layout.custom_marker_view);
        mv.setChartView(mChart);
        mChart.setMarker(mv);

    }

    private void setting_metode_bayar(String startdate, String enddate) {
        list_metode.setAdapter(null);
        listMetode.clear();

        AndroidNetworking.post(Server.URL + "general_report")
                .addHeaders("Authorization", "Bearer " + token)
                .addHeaders("Accept", "application/json")
                .addBodyParameter("startdate", startdate)
                .addBodyParameter("enddate", enddate)
                .addBodyParameter("id_outlet", id_outlet)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("200"))
                            {
                                renderData(startdate, enddate);
                                JSONArray array         = response.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++)
                                {
                                    JSONObject object   = array.getJSONObject(i);
                                    JSONArray metode    = object.getJSONArray("metode");
                                    JSONObject data = new JSONObject();
                                    ListDetailMetode listDetailMetode1 = new ListDetailMetode();


                                    //setting metode
                                    if (metode.toString().equals("[]"))
                                    {
                                        listDetailMetode1 = new ListDetailMetode(
                                                object.getString("tipe"),
                                                "0",
                                                "Rp. 0"
                                        );
                                    }
                                    else {
                                        for (int a = 0; a < metode.length(); a++) {
                                            data = metode.getJSONObject(a);

                                            listDetailMetode1 = new ListDetailMetode(
                                                    object.getString("tipe"),
                                                    data.getString("jml_transaksi"),
                                                    tanggal.formatRupiah(Double.parseDouble(data.getString("total_pendapatan")))
                                            );

                                        }
                                    }

                                    listMetode.add(listDetailMetode1);
                                    AdapterDetailMetode adapter     = new AdapterDetailMetode(GeneralReportActivity.this, listMetode);
                                    adapter.notifyDataSetChanged();

                                    list_metode.setAdapter(adapter);

                                    //setting laporan perproduk


                                }

                                if (T_pilih_hari.getTag().equals("3"))
                                {
                                    txt_tgl_laporan.setText(response.getString("startdate") + " - " + response.getString("enddate"));
                                }
                                else
                                {
                                    txt_tgl_laporan.setText(response.getString("enddate"));
                                }

                                txt_jml_trans.setText(response.getString("total_transaksi"));

                                if (!response.getString("total_pendapatan").equals("null")) {
                                    txt_pendapatan.setText(tanggal.formatRupiah(Double.parseDouble(response.getString("total_pendapatan"))));
                                }
                                else{
                                    txt_pendapatan.setText("Rp. 0");
                                }
                            }
                            else
                            {
                                txt_pendapatan.setText("Rp. 0");
                                FancyToast.makeText(GeneralReportActivity.this, response.getString("message"),
                                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(GeneralReportActivity.this, e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(GeneralReportActivity.this);
                        }
                        else {
                            FancyToast.makeText(GeneralReportActivity.this, anError.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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
                .addHeaders("Authorization", "Bearer " + token)
                .addBodyParameter("startdate", startdate)
                .addBodyParameter("enddate", enddate)
                .addBodyParameter("id_outlet", id_outlet)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res_kode     = response.getString("response");
                            if(res_kode.equals("200"))
                            {
                                L_general.setVisibility(View.VISIBLE);
                                loading_general.setVisibility(View.GONE);
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

                                    if (Utils.getSDKInt() >= 18) {
                                        Drawable drawable = ContextCompat.getDrawable(GeneralReportActivity.this, R.drawable.fade_orange);
                                        set1.setFillDrawable(drawable);
                                    } else {
                                        set1.setFillColor(Color.DKGRAY);
                                    }
                                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                                    dataSets.add(set1);
                                    LineData data = new LineData(dataSets);
                                    mChart.setData(data);
                                }
                            }
                            else
                            {
                                L_general.setVisibility(View.VISIBLE);
                                loading_general.setVisibility(View.GONE);

                                FancyToast.makeText(GeneralReportActivity.this, "Tidak Ada Transaksi",
                                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

//                                Toast.makeText(GeneralReportActivity.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            L_general.setVisibility(View.VISIBLE);
                            loading_general.setVisibility(View.GONE);
                            FancyToast.makeText(GeneralReportActivity.this, e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        L_general.setVisibility(View.VISIBLE);
                        loading_general.setVisibility(View.GONE);
                        FancyToast.makeText(GeneralReportActivity.this, anError.getMessage(),
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                });
    }

    private void setLaporan(String startdate, String enddate) {
        list_pergood.setAdapter(null);
        listLaporanPerproduk.clear();

        AndroidNetworking.post(Server.URL + "sales_good")
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
                            String message              = response.getString("message");
                            String res_kode             = response.getString("response");
                            JSONArray jsonArray         = response.getJSONArray("data");

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

                                    AdapterLaporanPerproduk adapterLaporanPerproduk     = new AdapterLaporanPerproduk(GeneralReportActivity.this, listLaporanPerproduk);
                                    adapterLaporanPerproduk.notifyDataSetChanged();

                                    list_pergood.setAdapter(adapterLaporanPerproduk);

                                }
                            }
                            else
                            {
                                FancyToast.makeText(GeneralReportActivity.this, "Tidak Ada Transaksi",
                                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
//                                Toast.makeText(GeneralReportActivity.this, "Tidak ada transaksi", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(GeneralReportActivity.this, "Terjadi Kesalahn Request!",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        FancyToast.makeText(GeneralReportActivity.this, "Terjadi Kesalahan Jaringan!",
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
