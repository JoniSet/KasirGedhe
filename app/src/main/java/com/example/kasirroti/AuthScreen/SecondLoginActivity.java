package com.example.kasirroti.AuthScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.Helper.SqliteHelper;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.example.kasirroti.SessionManager.SessionManager;
import com.example.kasirroti.SessionManager.SessionManagerUser;
import com.google.android.material.textfield.TextInputEditText;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SecondLoginActivity extends AppCompatActivity {

    private TextInputEditText edt_username, edt_password;
    private Button btn_login_kasir, btn_logout_outlet;
    private ProgressBar loading;
    private TextView T_nama_outlet;

    SessionManager sm;
    SessionManagerUser sessionManagerUser;
    String id_outlet;

    SqliteHelper sqliteHelper;

    private String uname, pass;
    public static Activity kasir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_login);

        AndroidNetworking.initialize(this);

        sqliteHelper            = new SqliteHelper(this);

        edt_username            = findViewById(R.id.edt_uname_kasir);
        edt_password            = findViewById(R.id.edt_pass_kasir);

        T_nama_outlet           = findViewById(R.id.T_nama_outlet);

        loading                 = findViewById(R.id.loading_kasir);

        kasir                   = this;

        sessionManagerUser      = new SessionManagerUser(this);
//        sessionManagerUser.cekLogin();

        sm                      = new SessionManager(this);
        sm.cekLogin();
        HashMap<String, String> map = sm.getDetailLogin();
        id_outlet               = map.get(sm.KEY_ID);
        T_nama_outlet.setText(map.get(sm.NAMA));

        btn_login_kasir         = findViewById(R.id.btn_login_kasir);
        btn_login_kasir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname    = edt_username.getText().toString().trim();
                pass     = edt_password.getText().toString().trim();

                if (uname.isEmpty())
                {
                    edt_username.setError("Username masih kosong!");
                }
                else if (pass.isEmpty())
                {
                    edt_password.setError("Password masih kosong!");
                }
                else {
                    loading.setVisibility(View.VISIBLE);
                    btn_login_kasir.setVisibility(View.GONE);
                    btn_logout_outlet.setVisibility(View.GONE);
                    login();
                }
            }
        });

        btn_logout_outlet       = findViewById(R.id.btn_logout_kasir);
        btn_logout_outlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqliteHelper.delete_kategori();
                sqliteHelper.delete_produk();
                sqliteHelper.delete_bahan_produk();
                sqliteHelper.delete_bahan_outlet();
                sqliteHelper.delete_info();

                sm.logout();
            }
        });
    }

    private void login() {

        AndroidNetworking.post(Server.URL + "login")
                .addBodyParameter("username", uname)
                .addBodyParameter("password", pass)
                .addBodyParameter("id_outlet", id_outlet)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            loading.setVisibility(View.GONE);
                            btn_login_kasir.setVisibility(View.VISIBLE);
                            btn_logout_outlet.setVisibility(View.VISIBLE);

                            if (response.getString("response").equals("200"))
                            {
                                String data                 = response.getString("user");

                                JSONObject object       = new JSONObject(data);

                                Intent intent           = new Intent(SecondLoginActivity.this, HomeActivity.class);
                                sessionManagerUser.storeLogin(response.getString("token"), object.getString("name"), object.getString("jabatan"));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else if (response.getString("response").equals("201")){
                                FancyToast.makeText(SecondLoginActivity.this, response.getString("message"),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                            else if (response.getString("response").equals("202")){
                                FancyToast.makeText(SecondLoginActivity.this, response.getString("message"),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                            else if (response.getString("response").equals("203")){
                                FancyToast.makeText(SecondLoginActivity.this, response.getString("message"),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Login User", e.getMessage());
                            loading.setVisibility(View.GONE);
                            btn_login_kasir.setVisibility(View.VISIBLE);
                            btn_logout_outlet.setVisibility(View.VISIBLE);
                            FancyToast.makeText(SecondLoginActivity.this, e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Login User", anError.getMessage());
                        loading.setVisibility(View.GONE);
                        btn_login_kasir.setVisibility(View.VISIBLE);
                        btn_logout_outlet.setVisibility(View.VISIBLE);
                        FancyToast.makeText(SecondLoginActivity.this, anError.getMessage(),
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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
        FancyToast.makeText(SecondLoginActivity.this, "Tap Sekali Lagi Untuk Keluar!",
                FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleTapParams = false;
            }

        }, 2000);
    }
}
