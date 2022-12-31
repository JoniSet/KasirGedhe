package com.example.kasirroti.AuthScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.Helper.SingOut;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.example.kasirroti.SessionManager.SessionManagerUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GantiPasswordActivity extends AppCompatActivity {

    private EditText lama, baru, konfirmasi;
    private Button btn_simpan;
    private ProgressBar loading_password;

    private String pass_lama, pass_baru, pass_konfir, token;

    SessionManagerUser sessionManagerUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);

        Toolbar toolbar     = findViewById(R.id.toolbar_ganti_password);
        toolbar.setTitle("Ganti Password");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManagerUser  = new SessionManagerUser(this);
        HashMap<String, String> map = sessionManagerUser.getDetailLogin();

        token               = map.get(sessionManagerUser.KEY_TOKEN);

        lama                = findViewById(R.id.edt_password_lama);
        baru                = findViewById(R.id.edt_password_baru);
        konfirmasi          = findViewById(R.id.edt_konfirmasi_paswword);

        loading_password    = findViewById(R.id.loading_password);

        btn_simpan          = findViewById(R.id.btn_simpan_password);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass_baru   = baru.getText().toString().trim();
                pass_lama   = lama.getText().toString().trim();
                pass_konfir = konfirmasi.getText().toString().trim();

                if (pass_baru.length() == 0)
                {
                    baru.setError("Masukkan password baru anda!");
                }
                else if (pass_lama.length() == 0)
                {
                    lama.setError("Masukkan password lama anda!");
                }
                else if (!pass_baru.equals(pass_konfir))
                {
                    konfirmasi.setError("Konfirmasi password tidak cocok!");
                }
                else{
                    btn_simpan.setEnabled(false);
                    setting_password();
                }
            }
        });
    }

    private void setting_password() {
        btn_simpan.setVisibility(View.GONE);
        loading_password.setVisibility(View.VISIBLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater     = getLayoutInflater();
        View v                      = inflater.inflate(R.layout.dialog_ganti_password, null);
        v.setBackgroundResource(android.R.color.transparent);
        builder.setCancelable(false);
        builder.setView(v);

        Dialog dialog               = builder.create();

        Button btn_simpan           = v.findViewById(R.id.btn_simpan_password_2);

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidNetworking.post(Server.URL + "ganti_password")
                        .addHeaders("Authorization", "Bearer " + token)
                        .addHeaders("Accept", "application/json")
                        .addBodyParameter("password_lama", pass_lama)
                        .addBodyParameter("password_baru", pass_baru)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                btn_simpan.setEnabled(true);
                                try {
                                    if (response.getString("response").equals("200"))
                                    {
                                        dialog.dismiss();
                                        btn_simpan.setVisibility(View.VISIBLE);
                                        loading_password.setVisibility(View.GONE);
                                        startActivity(new Intent(GantiPasswordActivity.this, HomeActivity.class));
                                        finish();
                                    }
                                    else{
                                        dialog.dismiss();
                                        btn_simpan.setVisibility(View.VISIBLE);
                                        loading_password.setVisibility(View.GONE);
                                        Toast.makeText(GantiPasswordActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    dialog.dismiss();
                                    btn_simpan.setVisibility(View.VISIBLE);
                                    loading_password.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                if (anError.getErrorCode() == 401){
                                    SingOut signOut = new SingOut();
                                    signOut.Logout(GantiPasswordActivity.this);
                                }
                                else {
                                    Toast.makeText(GantiPasswordActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    btn_simpan.setVisibility(View.VISIBLE);
                                    loading_password.setVisibility(View.GONE);
                                    btn_simpan.setEnabled(true);
                                }
                            }
                        });
            }
        });

        dialog.show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}