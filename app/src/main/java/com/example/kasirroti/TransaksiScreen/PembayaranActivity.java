package com.example.kasirroti.TransaksiScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.Adapter.AdapterListCustomer;
import com.example.kasirroti.Adapter.AdapterPembayaran;
import com.example.kasirroti.Adapter.PagerAdapter;
import com.example.kasirroti.Fragment.KalkulatorFragment;
import com.example.kasirroti.Helper.SingOut;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.Model.DataCustomer;
import com.example.kasirroti.Model.ListGridPesanan;
import com.example.kasirroti.Model.ListPesan;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

public class PembayaranActivity extends AppCompatActivity {

    public static TextView T_total_bayar_1, T_nama_pel;
    public static TextView T_uang;
    private Button B_nama_pel;
    public static Button B_metode_bayar;
    public static int bayar;
    String a    = "Total bayar : Rp. ";
    String status, nama_metode;
    public static String id_customer = "";

    ImageView tutorial;

    public static ViewPager viewPager;

    public static ArrayList<ListGridPesanan> item;
    PagerAdapter pagerAdapter;

    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    public static HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        viewPager                       = findViewById(R.id.pager);
        pagerAdapter                    = new PagerAdapter(getSupportFragmentManager(), 2);

        viewPager.setAdapter(pagerAdapter);

        T_total_bayar_1                 = findViewById(R.id.T_total_bayar_1);
        T_nama_pel                      = findViewById(R.id.T_nama_pel);
        T_uang                          = findViewById(R.id.T_uang);
        B_nama_pel                      = findViewById(R.id.B_nama_pel);
        B_metode_bayar                  = findViewById(R.id.B_metode_bayar);
        tutorial                        = findViewById(R.id.tutorial);

        B_metode_bayar.setTag("Cash");

        Intent intent                   = getIntent();
        String total_bayar              = intent.getStringExtra("total");
        T_total_bayar_1.setText(a + total_bayar);
        bayar                           = Integer.parseInt(total_bayar);

        //ListPesan diganti ListGridPesan
        Bundle bundle = getIntent().getExtras();
        item                            = (ArrayList<ListGridPesanan>) bundle.getSerializable("pesan");
        map                             = new HashMap<>();
        map                             = (HashMap<String, String>) intent.getSerializableExtra("map");

        Gson gson = new Gson();
        String listString = gson.toJson(item,
                new TypeToken<ArrayList<ListPesan>>() {}.getType());

        Log.d("DataPesanan", listString);

        if (T_nama_pel.getVisibility() == View.VISIBLE)
        {
            B_nama_pel.setVisibility(View.GONE);
        }

        B_nama_pel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_edit_nama_dialog();
            }
        });

        B_metode_bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting_metode();
            }
        });

        T_nama_pel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_edit_nama_dialog();
            }
        });

        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_tutorial();
            }
        });

    }

    private void show_edit_nama_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater     = getLayoutInflater();
        View v                      = inflater.inflate(R.layout.dialog_set_nama, null);
        v.setBackgroundResource(android.R.color.transparent);
        builder.setCancelable(true);
        builder.setView(v);

        Dialog dialog               = builder.create();

        EditText edt_cari_nomor_wa  = v.findViewById(R.id.edt_cari_wa);
        RecyclerView recycler_data_pelanggan = v.findViewById(R.id.recycler_data_pelanggan);
        Button btn_cari             = v.findViewById(R.id.btn_cari_customer);
        Button btn_simpan           = v.findViewById(R.id.btn_simpan);
        Button btn_tambah_customer  = v.findViewById(R.id.btn_tambah_customer);
        LinearLayout linear_cari    = v.findViewById(R.id.linear_cari);
        LinearLayout linear_tambah  = v.findViewById(R.id.linear_tambah);
        EditText edt_nama           = v.findViewById(R.id.edt_nama);
        EditText edt_nomor          = v.findViewById(R.id.edt_nomor);
        EditText edt_tanggal_lahir  = v.findViewById(R.id.edt_tanggal_lahir);
        ProgressBar progress  = v.findViewById(R.id.progress);
        ProgressBar p_loading  = v.findViewById(R.id.p_loading);
        LinearLayout linear_tanggal = v.findViewById(R.id.linear_tanggal);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        ArrayList<DataCustomer> dataCustomers   = new ArrayList<>();

        edt_tanggal_lahir.setEnabled(false);
        linear_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(edt_tanggal_lahir);
            }
        });

        btn_cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataCustomers.clear();
                recycler_data_pelanggan.setAdapter(null);
                btn_tambah_customer.setVisibility(View.GONE);
                getCustomer(edt_cari_nomor_wa.getText().toString(), dataCustomers, recycler_data_pelanggan, dialog, progress, btn_tambah_customer);
            }
        });

        btn_tambah_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_nomor.setText(edt_cari_nomor_wa.getText().toString());
                linear_cari.setVisibility(View.GONE);
                linear_tambah.setVisibility(View.VISIBLE);
            }
        });

        //id_customer

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_cari_nomor_wa.getText().toString().isEmpty())
                {
                    Toast.makeText(PembayaranActivity.this, "Anda belum memasukkan nama!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addCustomer(edt_nomor.getText().toString(), edt_nama.getText().toString(), edt_tanggal_lahir.getText().toString(), dialog, p_loading, btn_simpan);
                }
            }
        });

        dialog.show();
    }

    private void setting_metode()
    {
        //Dialog loading
        Dialog dialog               = new Dialog(PembayaranActivity.this);
        dialog.show();
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        //Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater     = getLayoutInflater();
        View v                      = inflater.inflate(R.layout.list_metode, null);
        v.setBackgroundResource(android.R.color.transparent);
        builder.setCancelable(true);
        builder.setView(v);

        Dialog dial                 = builder.create();

        //setting recyclerView
        RecyclerView recyclerView   = v.findViewById(R.id.list_detail_metode);
        recyclerView.setLayoutManager(new GridLayoutManager(PembayaranActivity.this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        ArrayList<String> metode_pembayaran = new ArrayList<>();

        AndroidNetworking.get(Server.URL + "list_pembayaran")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            dialog.dismiss();
                            dial.show();
                            JSONArray array         = response.getJSONArray("data");

                            for (int i = 0; i < array.length(); i++)
                            {
                                JSONObject object   = array.getJSONObject(i);
                                status              = object.getString("status");
                                nama_metode         = object.getString("nama_metode");

                                if (status.equals("1"))
                                {
                                    metode_pembayaran.add(nama_metode);
                                }
                            }

                            AdapterPembayaran adapter   = new AdapterPembayaran(PembayaranActivity.this, metode_pembayaran);
                            adapter.notifyDataSetChanged();

                            adapter.setOnItemClickListener(new AdapterPembayaran.recyclerViewClickListener() {
                                @Override
                                public void onClick(View v, int position) {

                                    if (metode_pembayaran.get(position).equals("Cash"))
                                    {
                                        B_metode_bayar.setTag(metode_pembayaran.get(position));
                                        B_metode_bayar.setText("Metode : " + metode_pembayaran.get(position));

                                        int a       = KalkulatorFragment.uang;
                                        int b       = bayar;
                                        KalkulatorFragment.uang = 0;
                                        T_uang.setText("");
                                        KalkulatorFragment.buttonenter.setVisibility(View.INVISIBLE);

                                        pagerAdapter                    = new PagerAdapter(getSupportFragmentManager(), 2);
                                        viewPager.setAdapter(pagerAdapter);

                                        if (a < b)
                                        {
                                            KalkulatorFragment.buttonenter.setVisibility(View.INVISIBLE);
                                        }
                                        else {
                                            KalkulatorFragment.buttonenter.setVisibility(View.VISIBLE);
                                        }
                                        dial.dismiss();
                                    }
                                    else {
                                        B_metode_bayar.setTag(metode_pembayaran.get(position));
                                        B_metode_bayar.setText("Metode : " + metode_pembayaran.get(position));
                                        KalkulatorFragment.buttonenter.setVisibility(View.VISIBLE);
                                        KalkulatorFragment.uang = bayar;
                                        T_uang.setText(String.valueOf(bayar));

                                        pagerAdapter                    = new PagerAdapter(getSupportFragmentManager(), 1);
                                        viewPager.setAdapter(pagerAdapter);

                                        dial.dismiss();
                                    }
                                }
                            });

                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(PembayaranActivity.this);
                        }
                        else {
                            dialog.dismiss();
                        }
                    }
                });

    }

    private void show_tutorial(){
        new GuideView.Builder(this)
                .setTitle("Total Pembayaran")
                .setContentText("")
                .setTargetView(T_total_bayar_1)
                .setContentTextSize(16)//optional
                .setTitleTextSize(20)//optional
                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                .setGuideListener(new GuideListener() {
                    @Override
                    public void onDismiss(View view) {

                        new GuideView.Builder(PembayaranActivity.this)
                                .setTitle("Form input nama Pelanggan")
                                .setContentText("")
                                .setTargetView(B_nama_pel)
                                .setContentTextSize(16)//optional
                                .setTitleTextSize(20)//optional
                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                .setGuideListener(new GuideListener() {
                                    @Override
                                    public void onDismiss(View view) {

                                        new GuideView.Builder(PembayaranActivity.this)
                                                .setTitle("Pilihan metode Pembayaran")
                                                .setContentText("")
                                                .setTargetView(B_metode_bayar)
                                                .setContentTextSize(12)//optional
                                                .setTitleTextSize(20)//optional
                                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                                .setGuideListener(new GuideListener() {
                                                    @Override
                                                    public void onDismiss(View view) {

                                                        new GuideView.Builder(PembayaranActivity.this)
                                                                .setTitle("Pilihan Metode bayar Cash")
                                                                .setContentText("")
                                                                .setTargetView(KalkulatorFragment.buttonok)
                                                                .setContentTextSize(12)//optional
                                                                .setTitleTextSize(20)//optional
                                                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                                                .setGuideListener(new GuideListener() {
                                                                    @Override
                                                                    public void onDismiss(View view) {

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

    private void getCustomer(
            String nohp,
            ArrayList<DataCustomer> customers,
            RecyclerView recyclerView,
            Dialog popup,
            ProgressBar progressBar,
            Button tambah){
        progressBar.setVisibility(View.VISIBLE);
        AndroidNetworking.post(Server.URL + "search_customer")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addBodyParameter("nohp", nohp)
                .setPriority(Priority.MEDIUM)
                .setTag("search_customer")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            String res_kode     = response.getString("response");
                            String message      = response.getString("message");

                            if (res_kode.equals("200")){
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
                                AdapterListCustomer adapter     = new AdapterListCustomer(PembayaranActivity.this, customers);
                                recyclerView.setLayoutManager(new LinearLayoutManager(PembayaranActivity.this));
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(adapter);
                                adapter.setOnItemClickListener(new AdapterListCustomer.recyclerViewClickListener() {
                                    @Override
                                    public void onClick(View v, int position) {
                                        popup.dismiss();
                                        T_nama_pel.setText(customers.get(position).getNama());
                                        id_customer = customers.get(position).getId();
                                        B_nama_pel.setVisibility(View.GONE);
                                        T_nama_pel.setVisibility(View.VISIBLE);
                                    }
                                });

                                tambah.setVisibility(View.GONE);
                            }
                            else if (res_kode.equals("201")){
                                FancyToast.makeText(PembayaranActivity.this, message, FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                                tambah.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(PembayaranActivity.this, e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Hasil", anError.getMessage());
                        FancyToast.makeText(PembayaranActivity.this, anError.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        progressBar.setVisibility(View.GONE);

                    }
                });
    }

    private void addCustomer(String nohp, String nama, String tgl, Dialog popup, ProgressBar loading, Button tambah){
        loading.setVisibility(View.VISIBLE);
        tambah.setVisibility(View.GONE);
        AndroidNetworking.post(Server.URL + "add_customer")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addBodyParameter("nohp", nohp)
                .addBodyParameter("nama", nama)
                .addBodyParameter("tgl_lahir", tgl)
                .setPriority(Priority.MEDIUM)
                .setTag("add_customer")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res_kode     = response.getString("response");
                            String message      = response.getString("message");
                            if (res_kode.equals("200")){
                                FancyToast.makeText(PembayaranActivity.this, message, FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();

                                popup.dismiss();
                                T_nama_pel.setText(nama);
                                B_nama_pel.setVisibility(View.GONE);
                                T_nama_pel.setVisibility(View.VISIBLE);
                            }
                            else {
                                FancyToast.makeText(PembayaranActivity.this, message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                loading.setVisibility(View.GONE);
                                tambah.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(PembayaranActivity.this, e.toString(), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                            loading.setVisibility(View.GONE);
                            tambah.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        FancyToast.makeText(PembayaranActivity.this, anError.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        loading.setVisibility(View.GONE);
                        tambah.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void showDateDialog(EditText tanggal){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                tanggal.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        KalkulatorFragment.uang = 0;
        finish();
    }
}
