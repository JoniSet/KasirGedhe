package com.example.kasirroti.TransaksiScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.example.kasirroti.Adapter.AdapterProdukDiStruk;
import com.example.kasirroti.Helper.SingOut;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.Model.ListInputPesan;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.example.kasirroti.Helper.Tanggal;
import com.example.kasirroti.async.AsyncBluetoothEscPosPrint;
import com.example.kasirroti.async.AsyncEscPosPrinter;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailTransaksiActivity extends AppCompatActivity {

    private String tampil_nama, logo_nota, nama, kode_transaksi, nama_induk_toko, outlet;
    private TextView txt_alamat_outlet, txt_notelp_outlet, txt_nama_struk, txt_nama_outlet, txt_tanggal, txt_waktu
            , txt_no_antri, txt_metode_struk, txt_total_b, txt_total_c, txt_kembalian_struk, txt_order_id, txt_judul;

    private Tanggal tanggal;
    private ImageView img_logo;

    private LinearLayout L_17;
    private ProgressBar loading_struk;

    RecyclerView listStruk;
    BluetoothAdapter bluetoothAdapter;
    private BluetoothConnection selectedDevice;

    ArrayList<String> list_struk    = new ArrayList<>();
    Bitmap bitmap1;
    Bitmap b;

    int plus, minus;

    StringBuffer sb                 = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi);

        Toolbar toolbar = findViewById(R.id.toolbar_struk);
        toolbar.setTitle("Detail Transaksi");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent           = getIntent();
        kode_transaksi          = intent.getStringExtra("id");

        bluetoothAdapter        = BluetoothAdapter.getDefaultAdapter();

        final BluetoothConnection[] bluetoothDevicesList = (new BluetoothPrintersConnections()).getList();

        txt_alamat_outlet       = findViewById(R.id.txt_alamat_outlet);
        txt_notelp_outlet       = findViewById(R.id.txt_notelp_outlet);
        txt_nama_struk          = findViewById(R.id.txt_nama_struk);
        txt_nama_outlet         = findViewById(R.id.txt_nama_outlet);
        txt_tanggal             = findViewById(R.id.txt_tanggal);
        txt_waktu               = findViewById(R.id.txt_waktu);
        txt_no_antri            = findViewById(R.id.txt_no_antri);
        txt_metode_struk        = findViewById(R.id.txt_metode_struk);
        txt_total_b             = findViewById(R.id.txt_total_b);
        txt_total_c             = findViewById(R.id.txt_total_c);
        txt_kembalian_struk     = findViewById(R.id.txt_kembalian_struk);
        loading_struk           = findViewById(R.id.loading_struk);
        L_17                    = findViewById(R.id.L_17);
        txt_order_id            = findViewById(R.id.txt_order_id);
        txt_judul               = findViewById(R.id.txt_judul);
        img_logo                = findViewById(R.id.img_logo);

        listStruk               = findViewById(R.id.list_produk_struk);
        listStruk.setHasFixedSize(true);
        listStruk.setLayoutManager(new LinearLayoutManager(this));
        listStruk.setItemAnimator(new DefaultItemAnimator());

        set_transaksi();
    }

    public void set_transaksi(){
        AndroidNetworking.post(Server.URL + "laporan_nota")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addHeaders("Accept", "application/json")
                .addBodyParameter("id_transaksi", kode_transaksi)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res_kode     = response.getString("response");
                            String message      = response.getString("response");
                            String object       = response.getString("data");

                            if (res_kode.equals("200"))
                            {
                                JSONObject data = new JSONObject(object);

                                txt_order_id.setText(data.getString("order_id"));
                                txt_total_c.setText("Rp. " + data.getString("uang_bayar"));
                                txt_total_b.setText("Rp. " + data.getString("total_harga"));
                                txt_metode_struk.setText(data.getString("metode_bayar"));
                                txt_no_antri.setText(data.getString("order_id").substring(5, 9));
                                txt_tanggal.setText(data.getString("tgl").substring(0, 10));
                                txt_waktu.setText(data.getString("tgl").substring(11, 19));
                                txt_kembalian_struk.setText("Rp. " + String.valueOf(Integer.parseInt(data.getString("uang_bayar")) - Integer.parseInt(data.getString("total_harga"))));
                                txt_alamat_outlet.setText(data.getString("alamat"));
                                txt_nama_struk.setText(data.getString("name"));
                                txt_nama_outlet.setText(data.getString("nama_outlet"));
                                txt_notelp_outlet.setText(data.getString("notelp"));
//                                txt_judul.setText(data.getString("nama_outlet"));

                                nama            = data.getString("customer");
                                if (nama.equals("null"))
                                {
                                    nama        = "-";
                                }

                                String[] dua_kata   = data.getString("name").trim().split(" ");
                                if (dua_kata.length > 2){
                                    tampil_nama  = dua_kata[0] + " " + dua_kata[1];
                                }
                                else {
                                    tampil_nama = data.getString("name");
                                }

                                txt_nama_struk.setText(tampil_nama);


                                String[] kata_toko              = data.getString("nama_outlet").trim().split(" ");
                                if (kata_toko.length < 4){
                                    nama_induk_toko = "Roti Gembong Gedhe";
                                }
                                else {
                                    nama_induk_toko = kata_toko[0] + " " + kata_toko[1] + " " + kata_toko[2];
                                }

                                for (int i = 3; i < kata_toko.length; i++)
                                {
                                    sb.append(kata_toko[i] + " ");
                                }
                                outlet                      = String.valueOf(sb);
                                txt_judul.setText(nama_induk_toko + "\n" + outlet);

                                plus            = Integer.parseInt(data.getString("total_harga"));
                                minus           = Integer.parseInt(data.getString("uang_bayar"));

                                keep_logo();
                            }
                            else
                            {
                                Toast.makeText(DetailTransaksiActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailTransaksiActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(DetailTransaksiActivity.this);
                        }
                        else {
                            L_17.setVisibility(View.VISIBLE);
                            loading_struk.setVisibility(View.GONE);
                            Toast.makeText(DetailTransaksiActivity.this, "Jaringan error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void keep_logo() {
        AndroidNetworking.get(Server.URL + "logo_nota")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("200"))
                            {
                                logo_nota           = response.getString("data");

                                ImageView profile = new ImageView(DetailTransaksiActivity.this);
                                Picasso.get().load(logo_nota).into(profile, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Bitmap innerBitmap = ((BitmapDrawable) profile.getDrawable()).getBitmap();
                                        img_logo.setImageBitmap(innerBitmap);

                                        b           = Bitmap.createScaledBitmap(innerBitmap, 120, 100, false);
                                        Canvas c = new Canvas(b);
                                        Paint paint = new Paint();
                                        ColorMatrix cm = new ColorMatrix();
                                        cm.setSaturation(0);
                                        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
                                        paint.setColorFilter(f);
                                        c.drawBitmap(b, 0, 0, paint);
                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });

                                set_nota_produk();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(DetailTransaksiActivity.this);
                        }
                        else {
                            Toast.makeText(DetailTransaksiActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void set_nota_produk(){
        AndroidNetworking.post(Server.URL + "nota_produk")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addHeaders("Accept", "application/json")
                .addBodyParameter("id_transaksi", kode_transaksi)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        L_17.setVisibility(View.VISIBLE);
                        loading_struk.setVisibility(View.GONE);

                        try {
                            if (response.getString("response").equals("200"))
                            {
                                JSONArray array             = response.getJSONArray("data");

                                ArrayList<ListInputPesan> listItem  = new ArrayList<>();

                                for (int i = 0; i < array.length(); i++)
                                {
                                    JSONObject object       = array.getJSONObject(i);

                                    list_struk.add(
                                            "[L]" + object.getString("nama_produk") + "\n" +
                                                    "[L]" + object.getString("jumlah") + " X " +
                                                    "Rp. " + object.getString("harga_satuan") +
                                                    "[R]Rp. " + String.valueOf(Integer.parseInt(object.getString("jumlah")) * Integer.parseInt(object.getString("harga_satuan"))) + "\n"
                                    );

                                    ListInputPesan list     = new ListInputPesan(
                                            object.getString("nama_produk"),
                                            object.getString("jumlah"),
                                            object.getString("harga_satuan")
                                    );

                                    listItem.add(list);

                                    AdapterProdukDiStruk adapter    = new AdapterProdukDiStruk(DetailTransaksiActivity.this, listItem);
                                    adapter.notifyDataSetChanged();

                                    listStruk.setAdapter(adapter);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(DetailTransaksiActivity.this);
                        }
                        else {
                            L_17.setVisibility(View.VISIBLE);
                            loading_struk.setVisibility(View.GONE);
                            Toast.makeText(DetailTransaksiActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_struk, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id              = item.getItemId();

        switch (id)
        {
            case R.id.nav_print:
                cariBluetooth();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    void cariBluetooth(){
        try
        {
            if (bluetoothAdapter == null)
            {
                FancyToast.makeText(DetailTransaksiActivity.this,  "Bluetooth Tidak Support!",
                        FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            }
            else{
                if (!bluetoothAdapter.isEnabled())
                {
                    showDialogStatus();
                }
                else {
                    show_dialog();
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void showDialogStatus(){
        final Dialog dialog                           = new Dialog(DetailTransaksiActivity.this);
        dialog.setContentView(R.layout.dialog_print_status);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView btn_ok   = dialog.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void show_dialog() {
        final BluetoothConnection[] bluetoothDevicesList = (new BluetoothPrintersConnections()).getList();

        if (bluetoothDevicesList != null) {
            final String[] items = new String[bluetoothDevicesList.length + 1];
            items[0] = "Default Printer";

            int i = 0;
            for (BluetoothConnection device : bluetoothDevicesList) {
                items[++i] = device.getDevice().getName();
            }

            int index = i - 1;
            if(index == -1) {
                selectedDevice = null;
            } else {
                selectedDevice = bluetoothDevicesList[index];
                Toast.makeText(DetailTransaksiActivity.this, String.valueOf(selectedDevice.getDevice().getName()), Toast.LENGTH_SHORT).show();
                doPrint();
            }
        }
    }

    public void doPrint() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, PembayaranSuksesActivity.PERMISSION_BLUETOOTH);
            } else {
                new AsyncBluetoothEscPosPrint(this).execute(this.getAsyncEscPosPrinter(selectedDevice));
            }
        } catch (Exception e) {
            Log.e("APP", "Can't print", e);
        }
    }

    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection) {

        StringBuffer sb = new StringBuffer();
        String[] data = new String[list_struk.size()];
        data = list_struk.toArray(data);

        for (int i = 0; i < data.length; i++)
        {
            sb.append(data[i]);
        }

        AsyncEscPosPrinter printer  = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);

        return printer.setTextToPrint(
                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, b) + "</img>\n" +
                        "[C]" + nama_induk_toko + "\n" +
                        "[C]" + outlet + "\n" +
                        "[C]" + txt_alamat_outlet.getText().toString()+ "\n" +
                        "[C]" + txt_notelp_outlet.getText().toString() + "\n" +
                        "[C]================================\n" +
                        "[C]" + txt_order_id.getText().toString() + "\n" +
                        "[L]" + txt_tanggal.getText().toString() + "[R]" + txt_waktu.getText().toString() + "\n" +
                        "[L]" + txt_nama_struk.getText().toString()  + "\n" +
                        "[L]No. " + txt_order_id.getText().toString().substring(5) + "\n" +
                        "[L]Nama Pelanggan : " + nama + "\n" +
                        "[C]--------------------------------\n" +
                        sb.toString() +
                        "[C]--------------------------------\n" +
                        "[L]Total[R]Rp. " + txt_total_b.getText().toString() + "\n" +
                        "[L]Pembayaran(" + txt_metode_struk.getText().toString() + ")[R]Rp. " + txt_total_c.getText().toString() + "\n" +
                        "[L]Kembalian[R]Rp. " + String.valueOf(minus - plus) + "\n" +
                        "[C]--------------------------------\n" +
                        "[C]Terima Kasih Atas Kunjungan Anda\n" +
                        "[C] ******************"
        );
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}