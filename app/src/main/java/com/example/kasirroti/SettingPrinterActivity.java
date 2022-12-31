package com.example.kasirroti;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.bluetooth.BluetoothAdapter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.SettingsSlicesContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.example.kasirroti.Helper.SqliteHelper;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.zip.Inflater;

public class SettingPrinterActivity extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter;
    private BluetoothConnection selectedDevice;
    String bt       = "";

    public static TextView txt_printer_terhubung, txt_pilih_printer, txt_a;
    private LinearLayout L_15;

    SqliteHelper sqliteHelper       = new SqliteHelper(SettingPrinterActivity.this);
    protected Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_printer);

        Toolbar toolbar         = findViewById(R.id.toolbar_setting);
        toolbar.setTitle("Setting Bluetooth Printer");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bluetoothAdapter        = BluetoothAdapter.getDefaultAdapter();

        txt_printer_terhubung   = findViewById(R.id.txt_printer_terhubung);
        L_15                    = findViewById(R.id.L_15);
        txt_a                   = findViewById(R.id.txt_a);

        L_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if (bluetoothAdapter == null)
                    {
                        Toast.makeText(SettingPrinterActivity.this, "Bluetooth tidak suport!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (!bluetoothAdapter.isEnabled())
                        {
                            txt_printer_terhubung.setText("Bluetooth tidak aktif");
                            FancyToast.makeText(SettingPrinterActivity.this,
                                "Bluetooth belum diaktifkan!",
                                FancyToast.LENGTH_SHORT,
                                FancyToast.WARNING,
                                false)
                                .show();
                        }
                        else {
                            show_dialog2();
                        }
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        cek_bluetooth();

    }

    public void cek_bluetooth(){
        try
        {
            if (bluetoothAdapter == null)
            {
                Toast.makeText(SettingPrinterActivity.this, "Bluetooth tidak suport!", Toast.LENGTH_SHORT).show();
            }
            else{
                if (!bluetoothAdapter.isEnabled())
                {
                    txt_printer_terhubung.setText("Bluetooth tidak aktif");
                    FancyToast.makeText(SettingPrinterActivity.this,
                            "Bluetooth belum diaktifkan!",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.WARNING,
                            false)
                            .show();
                }
                else {
                    set_bt_tersimpan();
                    show_dialog();
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
            }
            txt_printer_terhubung.setText(items[i]);
        }

    }

    private void show_dialog2() {
        AlertDialog.Builder dialog  = new AlertDialog.Builder(SettingPrinterActivity.this);
        LayoutInflater inflater     = getLayoutInflater();
        View dialogView             = inflater.inflate(R.layout.dialog_print, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        final AlertDialog a = dialog.create();

        Button print    = dialogView.findViewById(R.id.btn_printt);
        Spinner spinner = dialogView.findViewById(R.id.spinner_pilihbluetooth);

        print.setText("Simpan");

        final BluetoothConnection[] bluetoothDevicesList = (new BluetoothPrintersConnections()).getList();

        if (bluetoothDevicesList != null) {
            final String[] items = new String[bluetoothDevicesList.length + 1];
            items[0] = "Default Printer";

            int i = 0;
            for (BluetoothConnection device : bluetoothDevicesList) {
                items[++i]      = device.getDevice().getName();
            }

            int index = i - 1;
            if(index == -1) {
                selectedDevice = null;
            } else {
                selectedDevice = bluetoothDevicesList[index];
            }
            txt_printer_terhubung.setText(items[i]);
            bt                  = items[i];

            ArrayAdapter arrayAdapter   = new ArrayAdapter(this, R.layout.list_spinner_bluetooth, items);
            arrayAdapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
        }

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_printer_terhubung.setText(bt);
                if (cursor.getCount() == 0)
                {
                    save_bt_paired(selectedDevice.getDevice().getName(), selectedDevice.getDevice().getAddress());
                }
                else
                {
                    update_bt_paired(selectedDevice.getDevice().getName(), selectedDevice.getDevice().getAddress());
                }

                a.dismiss();
            }
        });

        a.show();
    }

    private void update_bt_paired(String nama, String id) {
        SQLiteDatabase db   = sqliteHelper.getReadableDatabase();
        db.execSQL("update printer set nama_bt ='"+
                nama +"', id_bt='" +
                id + "' where id_printer ='" +
                 "1" +"'");

        Toast.makeText(SettingPrinterActivity.this, "Berhasil update", Toast.LENGTH_SHORT).show();
    }

    private void save_bt_paired(String nama, String id) {
        SQLiteDatabase db   = sqliteHelper.getWritableDatabase();
        db.execSQL("insert into printer(nama_bt, id_bt) values('" +
                nama + "','" +
                id + "')");
    }

    public void set_bt_tersimpan()
    {
        SQLiteDatabase db   = sqliteHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM printer WHERE id_printer = '1'",null);
        cursor.moveToFirst();

        if (cursor.getCount() == 0)
        {
            Toast.makeText(SettingPrinterActivity.this, "Belum di setting!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            txt_printer_terhubung.setText(cursor.getString(1).toString());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
