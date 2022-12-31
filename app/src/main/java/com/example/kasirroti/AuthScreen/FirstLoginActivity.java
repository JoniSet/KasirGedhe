package com.example.kasirroti.AuthScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.Adapter.CustomSpinnerAdapter;
import com.example.kasirroti.BuildConfig;
import com.example.kasirroti.Model.ListOutlet;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.example.kasirroti.SessionManager.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FirstLoginActivity extends AppCompatActivity {

    private Spinner spinner;
    private TextInputEditText edt_pass;
    private Button btn_login_outlet;
    private ProgressBar loading_spinner, loading;

    private ArrayList<ListOutlet> listOutlett;
    private CustomSpinnerAdapter mAdapter;

    private String outlet, no, alamat;
    ListOutlet clickedItem;

    SessionManager sessionManager;

    public static Activity login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        AndroidNetworking.initialize(this);

        login           = this;

        spinner         = findViewById(R.id.spinner_outlet);
        edt_pass        = findViewById(R.id.edt_pass_outlet);
        btn_login_outlet= findViewById(R.id.btn_login_outlet);
        loading         = findViewById(R.id.loading_outlet);
        loading_spinner = findViewById(R.id.loading_spinner);

        listOutlett     = new ArrayList<>();

        sessionManager  = new SessionManager(this);
        id_outlet();

        cekVersi();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                clickedItem                     = (ListOutlet) parent.getItemAtPosition(position);
                outlet                          = clickedItem.getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_login_outlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading.setVisibility(View.VISIBLE);
                btn_login_outlet.setVisibility(View.GONE);

                String pass     = edt_pass.getText().toString().trim();

                if (pass.isEmpty())
                {
                    edt_pass.setError("Mohon isikan password anda!");
                    loading.setVisibility(View.GONE);
                    btn_login_outlet.setVisibility(View.VISIBLE);
                }
                else
                {
                    login();
                }

            }
        });

    }

    private void login() {
        AndroidNetworking.post(Server.URL + "login_outlet")
                .addBodyParameter("id_outlet", outlet)
                .addBodyParameter("password", edt_pass.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("200"))
                            {

                                String id               = response.getString("outlet");
                                JSONObject jsonObject   = new JSONObject(id);

                                sessionManager.storeLogin(outlet,
                                        clickedItem.getNama_outlet(),
                                        jsonObject.getString("notelp"),
                                        jsonObject.getString("alamat"),
                                        response.getString("logo"),
                                        jsonObject.getString("nama_rek"),
                                        jsonObject.getString("no_rek"),
                                        "0"
                                );

                                Intent intent       = new Intent(FirstLoginActivity.this, SecondLoginActivity.class);
                                startActivity(intent);
                                finish();
                                loading.setVisibility(View.GONE);
                                btn_login_outlet.setVisibility(View.VISIBLE);

                            }
                            else {
                                loading.setVisibility(View.GONE);
                                btn_login_outlet.setVisibility(View.VISIBLE);
                                FancyToast.makeText(FirstLoginActivity.this, response.getString("message"),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Login Outlet", e.getMessage());
                            loading.setVisibility(View.GONE);
                            btn_login_outlet.setVisibility(View.VISIBLE);
                            FancyToast.makeText(FirstLoginActivity.this, e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Login Outlet", anError.getMessage());
                        loading.setVisibility(View.GONE);
                        btn_login_outlet.setVisibility(View.VISIBLE);
                        FancyToast.makeText(FirstLoginActivity.this, anError.getMessage(),
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                });
    }

    private void id_outlet(){
        AndroidNetworking.get(Server.URL + "outlet")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            loading_spinner.setVisibility(View.GONE);
                            spinner.setVisibility(View.VISIBLE);

                            Log.d("Outlet", response.toString());
                            String message      = response.getString("message");
                            String res_code     = response.getString("response");

                            if (res_code.equals("200"))
                            {
                                JSONArray array = response.getJSONArray("data");

                                for (int i = 0; i < array.length(); i++)
                                {
                                    JSONObject data = array.getJSONObject(i);

                                    ListOutlet list = new ListOutlet(
                                            data.getString("id"),
                                            data.getString("nama_outlet")
                                    );

                                    listOutlett.add(list);

                                    mAdapter        = new CustomSpinnerAdapter(FirstLoginActivity.this, listOutlett);
                                    spinner.setAdapter(mAdapter);

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Outlet", response.toString());
                            loading_spinner.setVisibility(View.GONE);
                            spinner.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        loading_spinner.setVisibility(View.GONE);
                        spinner.setVisibility(View.VISIBLE);Log.d("Outlet", anError.getMessage());
                    }
                });
    }

    public boolean doubleTapParams = false;

    //fungsi tombol back untuk keluar
    @Override
    public void onBackPressed() {
        if (doubleTapParams){
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            return;
        }
        this.doubleTapParams = true;
        FancyToast.makeText(this, "Tap Sekali Lagi Untuk Keluar!",
                FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleTapParams = false;
            }

        }, 2000);
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
                                Dialog dialog       = new Dialog(FirstLoginActivity.this);
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

}
