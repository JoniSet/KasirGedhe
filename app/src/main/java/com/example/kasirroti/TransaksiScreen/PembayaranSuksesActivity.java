package com.example.kasirroti.TransaksiScreen;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.example.kasirroti.Helper.SingOut;
import com.example.kasirroti.Helper.Tanggal;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.example.kasirroti.SessionManager.SessionManager;
import com.example.kasirroti.SessionManager.SessionManagerUser;
import com.example.kasirroti.async.AsyncBluetoothEscPosPrint;
import com.example.kasirroti.async.AsyncEscPosPrinter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PembayaranSuksesActivity extends AppCompatActivity{

    private RelativeLayout R_print, R_print_order, R_more, R_kirim;
    private Button btn_finish;
    private LinearLayout L_more, R_lihat, R_pengaturan, layout_qrcode;
    private TextView txt_kembalian, T_printer_terpasang, textView3, txt_printer, txt_printer2;
    private String logo, nama, uang, bayar, metode, kasir, outlet, struk, notelp_outlet, alamat_outlet, token, id_transaksi, nama_induk_toko;

    private String id, id_kasir, id_outlet, total_harga, tgl, customer, order_id, metode_bayar;
    ArrayList<String> list_nota     = new ArrayList<>();
    ArrayList<String> list_struk    = new ArrayList<>();

    private ProgressBar loading_checkout;
    private LinearLayout card_checkout;

    private Tanggal tanggal1  = new Tanggal();

    public static final int PERMISSION_BLUETOOTH = 1;

    private static final int RequieredPermission = 120;

    private BluetoothConnection selectedDevice;

    String kembalian, logo_nota, tampil_nama, poin_baru, poin_total;

    boolean more        = true;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    BluetoothAdapter bluetoothAdapter;

    SessionManagerUser sessionManagerUser;
    SessionManager sessionManager;

    Bitmap bitmap1;
    Bitmap b;

    StringBuffer stringBuff     = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran_sukses);

        R_print         = findViewById(R.id.R_print);
        R_print_order   = findViewById(R.id.R_print_order);
        R_more          = findViewById(R.id.R_more);
        R_pengaturan    = findViewById(R.id.R_pengaturan);
        R_print         = findViewById(R.id.R_print);
        R_lihat         = findViewById(R.id.R_lihat);
        L_more          = findViewById(R.id.L_more);
        txt_kembalian   = findViewById(R.id.txt_kembalian);
        T_printer_terpasang   = findViewById(R.id.T_printer_terpasang);
        textView3       = findViewById(R.id.textView3);
        txt_printer2    = findViewById(R.id.txt_printer2);
        txt_printer     = findViewById(R.id.txt_printer);
        loading_checkout   = findViewById(R.id.loading_checkout);
        card_checkout   = findViewById(R.id.card_checkout);
        layout_qrcode   = findViewById(R.id.layout_qrcode);

        bluetoothAdapter    = BluetoothAdapter.getDefaultAdapter();

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
            }
            T_printer_terpasang.setText(items[i]);
            txt_printer.setText(items[i]);
            txt_printer2.setText(items[i]);

        }

        Intent intent   = getIntent();
        nama            = intent.getStringExtra("nama");
        bayar           = intent.getStringExtra("bayar");
        uang            = intent.getStringExtra("uang");
        metode          = intent.getStringExtra("metode");
        id_transaksi    = intent.getStringExtra("id_transaksi");

        if (nama.equals("")){
            poin_baru       = "[L]" + intent.getStringExtra("poin_baru") + " X\n";
            poin_total      = "[L]Poin Total[R]Rp. " + intent.getStringExtra("poin_total") + "\n";
        }
        else {
            poin_baru       = "";
            poin_total      = "";
        }

//        poin_baru       = intent.getStringExtra("poin_baru");
//        poin_total      = intent.getStringExtra("poin_total");

        textView3.setText(metode);

        sessionManagerUser  = new SessionManagerUser(PembayaranSuksesActivity.this);
        sessionManager      = new SessionManager(PembayaranSuksesActivity.this);

        HashMap<String, String> map1    = sessionManagerUser.getDetailLogin();
        HashMap<String, String> map2    = sessionManager.getDetailLogin();

        outlet                          = map2.get(sessionManager.NAMA);
        notelp_outlet                   = map2.get(sessionManager.TELP);
        logo                            = map2.get(sessionManager.LOGO);
        alamat_outlet                   = map2.get(sessionManager.ALAMAT);
        kasir                           = map1.get(sessionManagerUser.KEY_NAME);
        token                           = map1.get(sessionManagerUser.KEY_TOKEN);

        String[] dua_kata               = kasir.trim().split(" ");
        if (dua_kata.length > 2){
            tampil_nama = dua_kata[0] + " " + dua_kata[1];
        }
        else {
            tampil_nama = kasir;
        }

        String[] kata_toko              = outlet.trim().split(" ");
        if (kata_toko.length < 4){
            nama_induk_toko = "Roti Gembong Gedhe";
        }
        else {
            nama_induk_toko = kata_toko[0] + " " + kata_toko[1] + " " + kata_toko[2];
        }

        for (int i = 3; i < kata_toko.length; i++)
        {
            stringBuff.append(kata_toko[i] + " ");
        }
        outlet                      = String.valueOf(stringBuff);

        Picasso.get().load(logo).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // loaded bitmap is here (bitmap)
                bitmap1     = bitmap;

                b           = Bitmap.createScaledBitmap(bitmap, 120, 100, false);
                Canvas c = new Canvas(b);
                Paint paint = new Paint();
                ColorMatrix cm = new ColorMatrix();
                cm.setSaturation(0);
                ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
                paint.setColorFilter(f);
                c.drawBitmap(b, 0, 0, paint);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        });

        //============================setting nama====================================
        if (nama.isEmpty())
        {
            nama        = "-";
        }

        if (uang.isEmpty())
        {
            uang        = bayar;
        }

        //==========================================================================================

        kembalian       = String.valueOf(Integer.parseInt(uang) - Integer.parseInt(bayar));
        txt_kembalian.setText("Rp. " + kembalian);

        btn_finish      = findViewById(R.id.btn_finish);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent       = new Intent(PembayaranSuksesActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        R_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (more)
                {
                    L_more.setVisibility(View.VISIBLE);
                    more= false;
                }
                else
                {
                    L_more.setVisibility(View.GONE);
                    more= true;
                }


            }
        });

        R_pengaturan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseBluetoothDevice();
            }
        });

        R_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                struk   = "2";
                cariBluetooth();
            }
        });

        R_print_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                struk   = "1";
                cariBluetooth();
            }
        });

        R_lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ia   = new Intent(PembayaranSuksesActivity.this, DetailTransaksiActivity.class);
                ia.putExtra("id", id_transaksi);
                startActivity(ia);
            }
        });

        layout_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order_id.isEmpty()){
                    FancyToast.makeText(PembayaranSuksesActivity.this, "ID Pesanan Kosong",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR, false).show();
                }
                else{
                    show_dialog_qrcode();
                }
            }
        });

        keep_logo();
    }


    /*==============================================================================================
    ======================================BLUETOOTH PART============================================
    ==============================================================================================085696311313*/


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

    public void printPesanan() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, PembayaranSuksesActivity.PERMISSION_BLUETOOTH);
            } else {
                new AsyncBluetoothEscPosPrint(this).execute(this.getAsyncEscPosPEsan(selectedDevice));
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
        //setting ukuran gambar
        //gambar di set dari url lalu di convert ke bitmap "bitmap1"
        AsyncEscPosPrinter printer1 = new AsyncEscPosPrinter(printerConnection, 150, 20f, 17);
        return printer.setTextToPrint(
                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, b) + "</img>\n" +
                        "[C]" + nama_induk_toko + "\n" +
                        "[C]" + outlet + "\n" +
                        "[C]" + alamat_outlet + "\n" +
                        "[C]" + notelp_outlet + "\n" +
                        "[C]================================\n" +
                        "[C]" + order_id + "\n" +
                        "[L]" + tgl.substring(0, 10) + "[R]" + tgl.substring(11) + "\n" +
                        "[L]" + tampil_nama + "\n" +
                        "[L]No. " + order_id.substring(5) + "\n" +
                        "[L]Nama Pelanggan : " + nama + "\n" +
                        "[C]--------------------------------\n" +
                        sb.toString() +
                        "\n[L]Poin Baru\n" +
                        poin_baru +
                        "[C]--------------------------------\n" +
                        poin_total +
                        "[L]Total[R]Rp. " + total_harga + "\n" +
                        "[L]Pembayaran(" + metode + ")[R]Rp. " + uang + "\n" +
                        "[L]Kembalian[R]Rp. " + String.valueOf(Integer.parseInt(uang) - Integer.parseInt(bayar)) + "\n\n" +
                        "[C]--------------------------------\n" +
                        "[C]Terima Kasih Atas Kunjungan Anda\n" +
                        "[C] ******************"
        );
    }

    public AsyncEscPosPrinter getAsyncEscPosPEsan(DeviceConnection printerConnection) {

        String[] stringArray        = new String[list_nota.size()];
        stringArray                 = list_nota.toArray(stringArray);

        StringBuffer stringBuffer   = new StringBuffer();

        for (int i = 0; i < stringArray.length; i++)
        {
            stringBuffer.append(stringArray[i]);
        }

        String substr           = order_id.substring(5);

        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);
        return printer.setTextToPrint(
                "[L]\n" +
                        "[C]<font size='tall'>" + "PESANAN" + "</font>\n" +
                        "[C]<font size='tall'>No. " + substr + "</font>\n" +
                        "[C]<font size='tall'>Nama Pemesan : " + nama + "</font>\n" +
                        "[C]================================\n" +
                        "[L]Tanggal : " + tgl + "\n" +
                        "[L]Kasir   : " + kasir + "\n" +
                        "[L]Outlet  : " + outlet + "\n" +
                        "[C]--------------------------------\n" +
                        stringBuffer.toString()
                        +"[C]--------------------------------\n"
        );
    }

    public void browseBluetoothDevice() {
        try
        {
            if (bluetoothAdapter == null)
            {
                FancyToast.makeText(PembayaranSuksesActivity.this,  "Bluetooth Tidak Support!",
                        FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            }
            else{
                if (!bluetoothAdapter.isEnabled())
                {
                    showDialogStatus();
                }
                else {
                    final BluetoothConnection[] bluetoothDevicesList = (new BluetoothPrintersConnections()).getList();

                    if (bluetoothDevicesList != null) {
                        final String[] items = new String[bluetoothDevicesList.length + 1];
                        items[0] = "Printer belum tersambung";
                        int i = 0;
                        for (BluetoothConnection device : bluetoothDevicesList) {
                            items[++i] = device.getDevice().getName();
                        }

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PembayaranSuksesActivity.this);
                        alertDialog.setTitle("Pilihan bluetooth printer");
                        alertDialog.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int index = i - 1;
                                if(index == -1) {
                                    selectedDevice = null;
                                } else {
                                    selectedDevice = bluetoothDevicesList[index];
                                }
                                T_printer_terpasang.setText(items[i]);
                                txt_printer.setText(items[i]);
                                txt_printer2.setText(items[i]);
                            }
                        });

                        AlertDialog alert = alertDialog.create();
                        alert.setCanceledOnTouchOutside(false);
                        alert.show();

                    }
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    /*==============================================================================================
    ======================================Database============================================
    ==============================================================================================*/

    public void set_transaksi(){
        AndroidNetworking.post(Server.URL + "nota_transaksi")
                .addHeaders("Authorization", "Bearer " + token)
                .addHeaders("Accept", "application/json")
                .addBodyParameter("id_transaksi", id_transaksi)
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

                                id              = data.getString("id");
                                id_outlet       = data.getString("id_outlet");
                                id_kasir        = data.getString("id_kasir");
                                total_harga     = data.getString("total_harga");
                                tgl             = data.getString("tgl");
                                customer        = data.getString("customer");
                                order_id        = data.getString("order_id");
                                metode_bayar    = data.getString("metode_bayar");

                                set_nota_produk();
                            }
                            else
                            {
                                card_checkout.setVisibility(View.VISIBLE);
                                loading_checkout.setVisibility(View.GONE);
                                Toast.makeText(PembayaranSuksesActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            card_checkout.setVisibility(View.VISIBLE);
                            loading_checkout.setVisibility(View.GONE);
                            Toast.makeText(PembayaranSuksesActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(PembayaranSuksesActivity.this);
                        }
                        else {
                            card_checkout.setVisibility(View.VISIBLE);
                            loading_checkout.setVisibility(View.GONE);
                            Toast.makeText(PembayaranSuksesActivity.this, anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void keep_logo() {
        AndroidNetworking.get(Server.URL + "logo_nota")
                .addHeaders("Authorization", "Bearer " + token)
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

                                ImageView profile   = new ImageView(PembayaranSuksesActivity.this);
                                Picasso.get().load(logo_nota).into(profile, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Bitmap innerBitmap = ((BitmapDrawable) profile.getDrawable()).getBitmap();

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
                                        Toast.makeText(PembayaranSuksesActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                                set_transaksi();
                            }
                            else {
                                Toast.makeText(PembayaranSuksesActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PembayaranSuksesActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(PembayaranSuksesActivity.this);
                        }
                        else {
                            Toast.makeText(PembayaranSuksesActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void set_nota_produk(){
        AndroidNetworking.post(Server.URL + "nota_produk")
                .addHeaders("Authorization", "Bearer " + token)
                .addHeaders("Accept", "application/json")
                .addBodyParameter("id_transaksi", id_transaksi)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("200"))
                            {
                                JSONArray array             = response.getJSONArray("data");


                                for (int i = 0; i < array.length(); i++)
                                {
                                    JSONObject object       = array.getJSONObject(i);
                                    list_nota.add(
                                            "[L]" + object.getString("jumlah") +
                                                    " X " +
                                                    object.getString("nama_produk") + "\n"
                                    );


                                    list_struk.add(
                                            "[L]" + object.getString("nama_produk") + "\n" +
                                                    "[L]" + object.getString("jumlah") + " X " +
                                                    "Rp. " + object.getString("harga_satuan") +
                                                    "[R]Rp. " + String.valueOf(Integer.parseInt(object.getString("jumlah")) * Integer.parseInt(object.getString("harga_satuan"))) + "\n"
                                    );
                                }
                                card_checkout.setVisibility(View.VISIBLE);
                                loading_checkout.setVisibility(View.GONE);
                                doPrint();
                            }
                            else{
                                Toast.makeText(PembayaranSuksesActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            card_checkout.setVisibility(View.VISIBLE);
                            loading_checkout.setVisibility(View.GONE);
                            Toast.makeText(PembayaranSuksesActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(PembayaranSuksesActivity.this);
                        }
                        else {
                            card_checkout.setVisibility(View.VISIBLE);
                            loading_checkout.setVisibility(View.GONE);
                            Toast.makeText(PembayaranSuksesActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


//    ======================Dialog=======================================================================

    private void show_dialog() {
        dialog          = new AlertDialog.Builder(PembayaranSuksesActivity.this);
        inflater        = getLayoutInflater();
        dialogView      = inflater.inflate(R.layout.dialog_print, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        final AlertDialog a = dialog.create();

        Button print    = dialogView.findViewById(R.id.btn_printt);
        Spinner spinner = dialogView.findViewById(R.id.spinner_pilihbluetooth);

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
            }
            T_printer_terpasang.setText(items[i]);
            txt_printer.setText(items[i]);
            txt_printer2.setText(items[i]);

            ArrayAdapter arrayAdapter   = new ArrayAdapter(this, R.layout.list_spinner_bluetooth, items);
            arrayAdapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
        }

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.dismiss();
                if (struk.equals("1"))
                {
                    printPesanan();
                }
                else {
                    doPrint();
                }
            }
        });

        a.show();

    }

    void cariBluetooth(){
        try
        {
            if (bluetoothAdapter == null)
            {
                FancyToast.makeText(PembayaranSuksesActivity.this,  "Bluetooth Tidak Support!",
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
        final Dialog dialog                           = new Dialog(PembayaranSuksesActivity.this);
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

    //==================================qrGenerator==============================================

    private void show_dialog_qrcode(){
        Dialog dialog       = new Dialog(PembayaranSuksesActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_qrcode);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        ImageView imageView     = dialog.findViewById(R.id.img_qrcode);
        Button btn_tutup_qr     = dialog.findViewById(R.id.btn_tutup_qr);

        btn_tutup_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Bitmap qrCode           = setQrcode(order_id);
        imageView.setImageBitmap(qrCode);
    }

    private Bitmap setQrcode(String order_id){
        BitMatrix hasil     = null;
        try {
            hasil   = new MultiFormatWriter().encode(
                    order_id,
                    BarcodeFormat.QR_CODE,
                    250, 250, null
            );
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        int w       = hasil.getWidth();
        int h       = hasil.getHeight();
        int[] pix   = new int[w * h];
        for (int i = 0; i < h; i++){
            int offset  = i * w;
            for (int a = 0; a < w; a++){
                pix[offset + a] = hasil.get(a, i) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap   = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return bitmap;
    }

    //===========================================================================================

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /* Terminate bluetooth connection and close all sockets opened */
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent       = new Intent(PembayaranSuksesActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
