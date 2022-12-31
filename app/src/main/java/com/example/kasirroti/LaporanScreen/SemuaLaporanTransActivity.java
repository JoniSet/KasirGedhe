package com.example.kasirroti.LaporanScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.kasirroti.R;

public class SemuaLaporanTransActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semua_laporan_trans);

        Toolbar toolbar = findViewById(R.id.toolbar_treport);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        LinearLayout L_6 = findViewById(R.id.L_6);
        L_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SemuaLaporanTransActivity.this, LaporanSemuaTransaskiActivity.class));
            }
        });

        LinearLayout L_4 = findViewById(R.id.L_4);
        L_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent       = new Intent(SemuaLaporanTransActivity.this, LaporanTransaksiPertanggalActivity.class);
                intent.putExtra("tanggal", "");
                startActivity(intent);
            }
        });

        LinearLayout L_3 = findViewById(R.id.L_3);
        L_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent       = new Intent(SemuaLaporanTransActivity.this, LaporanTransaksiPerhariActivity.class);
                intent.putExtra("tanggal", "");
                startActivity(intent);
            }
        });

        LinearLayout L_5 = findViewById(R.id.L_5);
        L_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent       = new Intent(SemuaLaporanTransActivity.this, LaporanTransaksiPerbulanActivity.class);
                intent.putExtra("tanggal", "");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}