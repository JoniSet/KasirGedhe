package com.example.kasirroti.LaporanScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.GzipRequestInterceptor;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.Adapter.AdapterSisaBahan;
import com.example.kasirroti.Helper.SingOut;
import com.example.kasirroti.Helper.VolleyMultipartRequest;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.Model.ListInputExport;
import com.example.kasirroti.Model.ListSisaBahan;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

public class ExportActivity extends AppCompatActivity {

    LinearLayout L_list, L_20, L_21, linear_take_image;
    FloatingActionButton fab, fab_tambah_pengeluaran, fab_tambah_foto;
    EditText edt_uang_modal, edt_uang_makan, edt_esbatu, edt_gas, edt_plastik, edt_galon, edt_sedekah, edt_jml;
    Button btn_export;
    RecyclerView recycler_sisa_bahan;
    ImageView tutorial, img_nota;
    String modal, makan, es, gas, plastik, galon, sedekah, tpg;
    boolean result      = false;
    NestedScrollView scroll;

    int sum             = 0;

    private ProgressBar progress;
    private int Value   = 0;
    int a               = 0;
    private Handler handler = new Handler();

    ArrayList<ListInputExport> listInputExport      = new ArrayList<>();
    ArrayList<ListSisaBahan> listSisaBahan          = new ArrayList<>();
    ArrayList<ListSisaBahan> tepung                 = new ArrayList<>();

    AdapterSisaBahan adapterSisaBahan;

    Float translationY  = 100f;
    Boolean isMenuOpen  = false;

    String currentPhotoPath;
    Bitmap bitmap, scaled;
    File photoFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        Toolbar toolbar         = findViewById(R.id.toolbar_export);
        toolbar.setTitle("Form Export");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        L_list                  = findViewById(R.id.L_list);
        L_20                    = findViewById(R.id.L_20);
        L_21                    = findViewById(R.id.L_21);
        edt_uang_modal          = findViewById(R.id.edt_uang_modal);
        edt_uang_makan          = findViewById(R.id.edt_uang_makan);
        edt_esbatu              = findViewById(R.id.edt_esbatu);
        edt_gas                 = findViewById(R.id.edt_gas);
        edt_plastik             = findViewById(R.id.edt_plastik);
        edt_galon               = findViewById(R.id.edt_galon);
        edt_sedekah             = findViewById(R.id.edt_sedekah);
        edt_jml                 = findViewById(R.id.edt_jml);
        recycler_sisa_bahan     = findViewById(R.id.recycler_sisa_bahan);
        btn_export              = findViewById(R.id.btn_export);
        tutorial                = findViewById(R.id.tutorial);
        img_nota                = findViewById(R.id.img_nota);
        fab                     = findViewById(R.id.fab_tambah);
        fab_tambah_pengeluaran  = findViewById(R.id.fab_tambah_pengeluaran);
        fab_tambah_foto         = findViewById(R.id.fab_tambah_foto);
        linear_take_image       = findViewById(R.id.linear_take_image);
        scroll                  = findViewById(R.id.scroll);

        fab_tambah_foto.setAlpha(0f);
        fab_tambah_pengeluaran.setAlpha(0f);

        fab_tambah_foto.setTranslationY(translationY);
        fab_tambah_pengeluaran.setTranslationY(translationY);

        fab_tambah_foto.setEnabled(false);
        fab_tambah_pengeluaran.setEnabled(false);

        recycler_sisa_bahan.setLayoutManager(new LinearLayoutManager(ExportActivity.this));
        recycler_sisa_bahan.setItemAnimator(new DefaultItemAnimator());
        recycler_sisa_bahan.setHasFixedSize(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (isMenuOpen){
//                    closeMenu();
//                }
//                else {
//                    openMenu();
//                }
                addView();
            }
        });

        fab_tambah_pengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView();
            }
        });

        fab_tambah_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMenu();
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Toast.makeText(ExportActivity.this, "Terjadi Kesalahan!", Toast.LENGTH_SHORT).show();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(ExportActivity.this,
                                "com.example.kasirroti.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, 1);
                    }
                }
            }
        });

        linear_take_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMenu();
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Toast.makeText(ExportActivity.this, "Terjadi Kesalahan!", Toast.LENGTH_SHORT).show();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(ExportActivity.this,
                                "com.example.kasirroti.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, 1);
                    }
                }
            }
        });


        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_tutorial();
            }
        });

        btn_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modal           = edt_uang_modal.getText().toString();
                makan           = edt_uang_makan.getText().toString();
                es              = edt_esbatu.getText().toString();
                gas             = edt_gas.getText().toString();
                plastik         = edt_plastik.getText().toString();
                galon           = edt_galon.getText().toString();
                sedekah         = edt_sedekah.getText().toString();
                tpg             = edt_jml.getText().toString();

                ListSisaBahan listSisaBahan = new ListSisaBahan(
                        "0",
                        "Tepung",
                        tpg,
                        "ADONAN"
                );
                tepung.add(listSisaBahan);

                if (modal.isEmpty())
                {
                    FancyToast.makeText(ExportActivity.this, "Uang Modal Tidak Boleh Kosong!",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.WARNING,
                            false)
                            .show();
                }
                else if(scaled == null){
                    FancyToast.makeText(ExportActivity.this, "Mohon lampirkan foto nota/bukti pengeluaran!",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.WARNING,
                            false)
                            .show();
                }
                else
                {
                    btn_export.setEnabled(false);
                    if (makan.isEmpty())
                    {
                        makan       = "0";
                    }
                    else if (es.isEmpty())
                    {
                        es          = "0";
                    }
                    else if (gas.isEmpty())
                    {
                        gas         = "0";
                    }
                    else if (plastik.isEmpty())
                    {
                        plastik     = "0";
                    }
                    else if (galon.isEmpty())
                    {
                        galon       = "0";
                    }
                    else if (sedekah.isEmpty())
                    {
                        sedekah     = "0";
                    }

                    CekValid();

                    for (int i = 0; i < listInputExport.size(); i++)
                    {
                        sum                 += Integer.parseInt(listInputExport.get(i).getJumlah());
                    }

                    Gson gson = new Gson();

                    String listString = gson.toJson(
                            listInputExport,
                            new TypeToken<ArrayList<ListInputExport>>() {}.getType());

                    export_voley(listString);
//                    export(listString);
                }

            }
        });

        DaftarBahanSisa();

    }

    private void export(String listString) {
        final Dialog dialog                           = new Dialog(ExportActivity.this);
        dialog.setContentView(R.layout.progress_loading);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        progress            = dialog.findViewById(R.id.progress);
        progress.setProgress(0);

        Gson gson               = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(listSisaBahan).getAsJsonArray();
        JsonArray mustomArray   = gson.toJsonTree(tepung).getAsJsonArray();

        new Thread(new Runnable() {
            public void run() {
                while (Value < 100) {
                    Value += 1;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            progress.setProgress(Value);
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        try {
            JSONArray jsonArray     = new JSONArray(listString);

            OkHttpClient okHttpClient = new OkHttpClient()
                    .newBuilder()
                    .addInterceptor(new GzipRequestInterceptor())
                    .build();

            AndroidNetworking.post(Server.URL + "export")
                    .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                    .addHeaders("Accept", "application/json")
                    .addBodyParameter("id_outlet", HomeActivity.id_outlet)
                    .addBodyParameter("uang_modal", modal)
                    .addBodyParameter("uang_makan", makan)
                    .addBodyParameter("es_batu", es)
                    .addBodyParameter("gas_elpiji", gas)
                    .addBodyParameter("plastik", plastik)
                    .addBodyParameter("galon", galon)
                    .addBodyParameter("sedekah", sedekah)
                    .addBodyParameter("pengeluaran", jsonArray.toString())
                    .addBodyParameter("sisa_utuh", myCustomArray.toString())
                    .addBodyParameter("tepung", mustomArray.toString())
                    .addBodyParameter("total_pengeluaran", String.valueOf(sum))
                    .addByteBody(getFileDataFromDrawable(bitmap))
                    .addByteBody(getFileDataFromDrawable(scaled))
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            btn_export.setEnabled(true);
                            try {
                                a                           = response.length();
                                progress.setMax(a);
                                Log.d("Test", String.valueOf(a));

                                if(response.getString("response").equals("200")) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(response.getString("link")));
                                    startActivity(intent);

                                    dialog.dismiss();
                                    sum     = 0;
                                }
                                else
                                {
                                    FancyToast.makeText(ExportActivity.this,
                                            response.getString("message"),
                                            FancyToast.LENGTH_SHORT,
                                            FancyToast.ERROR,
                                            false)
                                            .show();

                                    listInputExport.clear();
                                    listSisaBahan.clear();
                                    tepung.clear();

                                    finish();

                                    btn_export.setEnabled(true);

                                    dialog.dismiss();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                FancyToast.makeText(
                                        ExportActivity.this,
                                        "Kesalahan Request, Mohon Ulangi Kembali!",
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR,
                                        false
                                ).show();

                                listInputExport.clear();
                                listSisaBahan.clear();
                                tepung.clear();

                                finish();

                                btn_export.setEnabled(true);
                                dialog.dismiss();
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
                            if (anError.getErrorCode() == 401){
                                SingOut signOut = new SingOut();
                                signOut.Logout(ExportActivity.this);
                            }
                            else {
                                FancyToast.makeText(
                                        ExportActivity.this,
                                        "Kesalahan Jaringan, Periksa Koneksi Internet Anda!",
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR,
                                        false
                                ).show();

                                Log.d("Exporttt", anError.getErrorBody() +
                                        "\n " + HomeActivity.token +
                                        "\n " + HomeActivity.id_outlet);

                                listInputExport.clear();
                                listSisaBahan.clear();
                                tepung.clear();

                                finish();

                                btn_export.setEnabled(true);
                                dialog.dismiss();
                            }
                        }

                    });

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    private void export_voley(String listString) {
        final Dialog dialog                           = new Dialog(ExportActivity.this);
        dialog.setContentView(R.layout.progress_loading);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        progress            = dialog.findViewById(R.id.progress);
        progress.setProgress(0);

        Gson gson               = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(listSisaBahan).getAsJsonArray();
        JsonArray mustomArray   = gson.toJsonTree(tepung).getAsJsonArray();

        new Thread(new Runnable() {
            public void run() {
                while (Value < 100) {
                    Value += 1;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            progress.setProgress(Value);
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        try {
            JSONArray jsonArray     = new JSONArray(listString);

            OkHttpClient okHttpClient = new OkHttpClient()
                    .newBuilder()
                    .addInterceptor(new GzipRequestInterceptor())
                    .build();

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Server.URL + "export",
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            String resultResponse = new String(response.data);
                            try {
                                JSONObject jsonObject = new JSONObject(resultResponse);
                                String respon = jsonObject.getString("response");
                                String message = jsonObject.getString("message");
                                if (respon.equals("200")){
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(jsonObject.getString("link")));
                                    startActivity(intent);
                                    finish();

                                    dialog.dismiss();
                                    sum     = 0;
                                }else {
                                    FancyToast.makeText(ExportActivity.this,
                                            jsonObject.getString("message"),
                                            FancyToast.LENGTH_SHORT,
                                            FancyToast.ERROR,
                                            false)
                                            .show();

                                    listInputExport.clear();
                                    listSisaBahan.clear();
                                    tepung.clear();

                                    finish();

                                    btn_export.setEnabled(true);

                                    dialog.dismiss();
                                }
                                dialog.dismiss();
                            } catch (JSONException e){
                                e.printStackTrace();
                                FancyToast.makeText(
                                        ExportActivity.this,
                                        "Kesalahan Request, Mohon Ulangi Kembali!",
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR,
                                        false
                                ).show();

                                listInputExport.clear();
                                listSisaBahan.clear();
                                tepung.clear();

                                finish();

                                btn_export.setEnabled(true);
                                dialog.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    FancyToast.makeText(
                            ExportActivity.this,
                            "Kesalahan Jaringan, Periksa Koneksi Internet Anda!",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR,
                            false
                    ).show();
                    dialog.dismiss();

                    Log.d("Exporttt", error.getMessage() +
                            "\n " + HomeActivity.token +
                            "\n " + HomeActivity.id_outlet);

                    listInputExport.clear();
                    listSisaBahan.clear();
                    tepung.clear();

                    finish();

                    btn_export.setEnabled(true);
                    dialog.dismiss();

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Accept", "application/json");
                    headers.put("Authorization", "Bearer " + HomeActivity.token);
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("id_outlet", HomeActivity.id_outlet);
                    params.put("uang_modal", modal);
                    params.put("uang_makan", makan);
                    params.put("es_batu", es);
                    params.put("gas_elpiji",gas);
                    params.put("plastik", plastik);
                    params.put("galon", galon);
                    params.put("sedekah",sedekah);
                    params.put("pengeluaran", jsonArray.toString());
                    params.put("sisa_utuh", myCustomArray.toString());
                    params.put("tepung",  mustomArray.toString());
                    params.put("total_pengeluaran", String.valueOf(sum));
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("foto_nota", new DataPart(HomeActivity.nama + ".jpg", getFileDataFromDrawable(bitmap)));
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(volleyMultipartRequest);
            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    private boolean CekValid(){
        listInputExport.clear();

        for(int i = 0; i < L_list.getChildCount(); i++)
        {
            View editTextView       = L_list.getChildAt(i);

            EditText edtJenis       = (EditText)editTextView.findViewById(R.id.edt_jenis);
            EditText edtTotal       = (EditText)editTextView.findViewById(R.id.edt_total);

            ListInputExport list    = new ListInputExport();

            if(!edtJenis.getText().toString().equals(""))
            {
                list.setJenis(edtJenis.getText().toString());
            }
            else
            {
                result              = false;
                break;
            }

            if (!edtTotal.getText().toString().equals(""))
            {
                list.setJumlah(edtTotal.getText().toString());
            }
            else{
                result              = false;
                break;
            }

            listInputExport.add(list);
        }

        if (listInputExport.size() == 0)
        {
            result                  = false;
        }
        else if (result)
        {
            Toast.makeText(ExportActivity.this, "Isi semua field!", Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    private void addView()
    {
        View editTextView       = getLayoutInflater().inflate(R.layout.dynamic_edittext, null, false);

        EditText edtJenis       = (EditText)editTextView.findViewById(R.id.edt_jenis);
        EditText edtTotal       = (EditText)editTextView.findViewById(R.id.edt_total);
        ImageView img_remove    = (ImageView)editTextView.findViewById(R.id.img_remove);

        img_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(editTextView);
            }
        });

        L_list.addView(editTextView);
    }

    private void DaftarBahanSisa(){
        listSisaBahan.clear();
        recycler_sisa_bahan.setAdapter(null);
        Dialog dialog           = new Dialog(ExportActivity.this);
        dialog.setContentView(R.layout.loading);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        AndroidNetworking.get(Server.URL + "bahan")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res_code         = response.getString("response");
                            if (res_code.equals("200")){
                                JSONArray array     = response.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++){
                                    JSONObject obj  = array.getJSONObject(i);

                                    ListSisaBahan list  = new ListSisaBahan(
                                            obj.getString("id"),
                                            obj.getString("nama_bahan"),
                                            "",
                                            ""
                                    );
                                    listSisaBahan.add(list);
                                }

                                adapterSisaBahan        = new AdapterSisaBahan(ExportActivity.this, listSisaBahan);
                                adapterSisaBahan.notifyDataSetChanged();

                                recycler_sisa_bahan.setLayoutManager(new LinearLayoutManager(ExportActivity.this));
                                recycler_sisa_bahan.setItemAnimator(new DefaultItemAnimator());
                                recycler_sisa_bahan.setHasFixedSize(true);
                                recycler_sisa_bahan.setAdapter(adapterSisaBahan);
                                dialog.dismiss();
                            }else{
                                Toast.makeText(ExportActivity.this, "Tidak Ada Data Ditampilkan!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ExportActivity.this, "Terjadi Kesalahan Request!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(ExportActivity.this);
                        }
                        else {
                            Toast.makeText(ExportActivity.this, "Jaringan Bermasalah!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
    }

    private void removeView(View view)
    {
        L_list.removeView(view);
    }

    private void show_tutorial(){
        new GuideView.Builder(this)
                .setTitle("Langkah - langkah Export Laporan")
                .setContentText("Isi jumlah uang modal")
                .setTargetView(edt_uang_modal)
                .setContentTextSize(16)//optional
                .setTitleTextSize(20)//optional
                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                .setGuideListener(new GuideListener() {
                    @Override
                    public void onDismiss(View view) {

                        new GuideView.Builder(ExportActivity.this)
                                .setTitle("Isi daftar pengeluaran uang modal")
                                .setContentText("Daftar Pengeluaran modal dapat di isi secara opsional atau di biarkan kosong sesuai keadaan di lapangan")
                                .setTargetView(L_20)
                                .setContentTextSize(16)//optional
                                .setTitleTextSize(20)//optional
                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                .setGuideListener(new GuideListener() {
                                    @Override
                                    public void onDismiss(View view) {

                                        new GuideView.Builder(ExportActivity.this)
                                                .setTitle("Tambah daftar pengeluaran modal")
                                                .setContentText("Pengeluaran uang modal yang tidak terdapat pada daftar bisa di tambahkan dengan menekan tombol berikut!")
                                                .setTargetView(fab)
                                                .setContentTextSize(16)//optional
                                                .setTitleTextSize(20)//optional
                                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                                .setGuideListener(new GuideListener() {
                                                    @Override
                                                    public void onDismiss(View view) {

                                                        new GuideView.Builder(ExportActivity.this)
                                                                .setTitle("Daftar tambahan pengeluaran uang modal")
                                                                .setContentText("Anda juga dapat menghapus daftar tambahan pengeluaran uang modal dengan cara menekan tombol (X) di sebelah kanan!" )
                                                                .setTargetView(L_list)
                                                                .setContentTextSize(16)//optional
                                                                .setTitleTextSize(20)//optional
                                                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                                                .setGuideListener(new GuideListener() {
                                                                    @Override
                                                                    public void onDismiss(View view) {

                                                                        new GuideView.Builder(ExportActivity.this)
                                                                                .setTitle("Daftar jumlah sisa utuh bahan")
                                                                                .setContentText("Isi setiap jumlah sisa bahan pada daftar berikut!" )
                                                                                .setTargetView(L_21)
                                                                                .setContentTextSize(16)//optional
                                                                                .setTitleTextSize(20)//optional
                                                                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                                                                .setGuideListener(new GuideListener() {
                                                                                    @Override
                                                                                    public void onDismiss(View view) {

                                                                                        new GuideView.Builder(ExportActivity.this)
                                                                                                .setTitle("Tombol Export")
                                                                                                .setContentText("Setelah form sudah terisi, anda bisa mengeksport laporan dalam file PDF dengan menekan tombol EXPORT. Setelah proses selesai, anda akan diarahkan ke Google Chrome untuk mengunduh file PDF anda." )
                                                                                                .setTargetView(btn_export)
                                                                                                .setContentTextSize(16)//optional
                                                                                                .setTitleTextSize(20)//optional
                                                                                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                                                                                .setGuideListener(new GuideListener() {
                                                                                                    @Override
                                                                                                    public void onDismiss(View view) {
                                                                                                        scroll.fullScroll(NestedScrollView.FOCUS_DOWN);
                                                                                                        new Handler().postDelayed(new Runnable() {
                                                                                                            @Override
                                                                                                            public void run() {
                                                                                                                new GuideView.Builder(ExportActivity.this)
                                                                                                                        .setTitle("Tombol Foto Nota")
                                                                                                                        .setContentText("Foto semua nota/bukti pengeluaran" )
                                                                                                                        .setTargetView(linear_take_image)
                                                                                                                        .setContentTextSize(16)//optional
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
                                                                                                        }, 300);

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
                                })
                                .build()
                                .show();
                    }

                })
                .build()
                .show();
    }

    private void openMenu(){
        isMenuOpen = !isMenuOpen;

        fab.animate().setInterpolator(new OvershootInterpolator()).rotation(45f).setDuration(300).start();

        fab_tambah_pengeluaran.animate().translationY(0f).alpha(1f).setInterpolator(new OvershootInterpolator()).setDuration(300).start();
        fab_tambah_foto.animate().translationY(0f).alpha(1f).setInterpolator(new OvershootInterpolator()).setDuration(300).start();

        fab_tambah_foto.setEnabled(true);
        fab_tambah_pengeluaran.setEnabled(true);
    }

    private void closeMenu(){
        isMenuOpen = !isMenuOpen;

        fab.animate().setInterpolator(new OvershootInterpolator()).rotation(0f).setDuration(300).start();

        fab_tambah_pengeluaran.animate().translationY(translationY).alpha(0f).setInterpolator(new OvershootInterpolator()).setDuration(300).start();
        fab_tambah_foto.animate().translationY(translationY).alpha(0f).setInterpolator(new OvershootInterpolator()).setDuration(300).start();

        fab_tambah_foto.setEnabled(false);
        fab_tambah_pengeluaran.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            linear_take_image.setVisibility(View.GONE);
            bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            if (bitmap != null) {
                int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
//                img_nota.setImageBitmap(scaled);
                img_nota.setImageBitmap(bitmap);
            }
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("MMdd_HHmmss").format(new Date());
        String imageFileName = HomeActivity.id_outlet + "_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();
        Log.d("Lokasi", currentPhotoPath);
        return image;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}