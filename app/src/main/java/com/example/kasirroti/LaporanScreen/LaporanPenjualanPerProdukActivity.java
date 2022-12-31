package com.example.kasirroti.LaporanScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.kasirroti.R;

public class LaporanPenjualanPerProdukActivity extends AppCompatActivity {

    private LinearLayout L_7, L_9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_penjualan_per_produk);

        Toolbar toolbar     = findViewById(R.id.toolbar_report);
        toolbar.setTitle("Laporan Penjualan");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        L_7     = findViewById(R.id.L_7);
        L_9     = findViewById(R.id.L_9);

        L_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LaporanPenjualanPerProdukActivity.this, LaporanPerProdukActivity.class));
            }
        });

        L_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LaporanPenjualanPerProdukActivity.this, LaporanPerKategoriActivity.class));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
