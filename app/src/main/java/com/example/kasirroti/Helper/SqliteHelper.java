package com.example.kasirroti.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.kasirroti.DataSqlite.BahanOutletSqlite;
import com.example.kasirroti.DataSqlite.BahanProdukSqlite;
import com.example.kasirroti.DataSqlite.KategoriSqlite;
import com.example.kasirroti.DataSqlite.ProdukSqlite;
import com.example.kasirroti.DataSqlite.UpdateInfoSqlite;

import java.util.ArrayList;
import java.util.HashMap;

public class SqliteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    static final String DB_NAME = "pesanan.db";

    public static final String TABLE_SQLITE = "pesan";
    public static final String TABLE_PRINTER = "printer";
    public static final String TABLE_KATEGORI = "kategori";
    public static final String TABLE_PRODUK = "produk";
    public static final String TABLE_BAHAN_PRODUK = "bahan_produk";
    public static final String TABLE_BAHAN_OUTLET = "bahan_outlet";
    public static final String TABLE_UPDATE_INFO = "update_info";

    public static final String KODE = "kode_pesanan";

    public static final String ID_PRINTER = "id_printer";
    public static final String NAMA_BLUETOOTH = "nama_bt";
    public static final String ID_BLUETOOTH = "id_bt";


    public SqliteHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE_PRINTER = "CREATE TABLE " + TABLE_PRINTER + " (" +
                ID_PRINTER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAMA_BLUETOOTH + " TEXT NOT NULL, " +
                ID_BLUETOOTH + " TEXT NOT NULL " +
                " )";

        final String CREATE_TABLE_KATEGORI = "CREATE TABLE " + TABLE_KATEGORI + " (" +
                "id INTEGER NOT NULL, " +
                "nama_kategori TEXT NOT NULL, " +
                "urutan INTEGER PRIMARY KEY AUTOINCREMENT " +
                " )";

        final String CREATE_TABLE_PRODUK = "CREATE TABLE " + TABLE_PRODUK + " (" +
                "id INTEGER PRIMARY KEY, " +
                "id_kategori TEXT NOT NULL, " +
                "nama_produk TEXT NOT NULL, " +
                "harga TEXT NOT NULL, " +
                "img TEXT NOT NULL " +
                " )";

        final String CREATE_TABLE_BAHAN_PRODUK = "CREATE TABLE " + TABLE_BAHAN_PRODUK + " (" +
                "id INTEGER PRIMARY KEY, " +
                "id_produk TEXT NOT NULL, " +
                "id_bahan TEXT NOT NULL, " +
                "terpakai TEXT NOT NULL, " +
                "nama_bahan TEXT NOT NULL " +
                " )";

        final String CREATE_TABLE_BAHAN_OUTLET = "CREATE TABLE " + TABLE_BAHAN_OUTLET + " (" +
                "id INTEGER PRIMARY KEY, " +
                "id_bahan TEXT NOT NULL, " +
                "stok TEXT NOT NULL, " +
                "nama_bahan TEXT NOT NULL " +
                " )";

        final String CREATE_TABLE_INFO = "CREATE TABLE " + TABLE_UPDATE_INFO + " (" +
                "id INTEGER PRIMARY KEY, " +
                "status TEXT NOT NULL " +
                " )";

        db.execSQL(CREATE_TABLE_PRINTER);
        db.execSQL(CREATE_TABLE_KATEGORI);
        db.execSQL(CREATE_TABLE_PRODUK);
        db.execSQL(CREATE_TABLE_BAHAN_PRODUK);
        db.execSQL(CREATE_TABLE_BAHAN_OUTLET);
        db.execSQL(CREATE_TABLE_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRINTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KATEGORI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BAHAN_PRODUK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BAHAN_OUTLET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UPDATE_INFO);

        onCreate(db);
    }


    //=================================Keranjang=================================================================

    public void insert(String id_produk, String nama, String jumlah, String harga, String total) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO " + TABLE_SQLITE + " (kode_pesanan, id_produk, nama, jumlah, harga, total) " +
                "VALUES ('" + "1" + "', '" + id_produk + "', '" + nama + "', '" + jumlah + "','" + harga + "', '" + total + "')";

        Log.e("insert sqlite ", "" + queryValues);
        database.execSQL(queryValues);
        database.close();
    }

    public void delete() {
        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "DELETE FROM " + TABLE_SQLITE;
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public Cursor viewData() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor;

        String query = "Select * from " + TABLE_SQLITE;
        cursor = sqLiteDatabase.rawQuery(query, null);


        return cursor;
    }

    public void update(String kode) {
        SQLiteDatabase database = this.getWritableDatabase();

        String query = "UPDATE " + TABLE_SQLITE + " SET "
                + KODE + "='" + kode + "'"
                + " WHERE " + KODE + "=" + "'" + "1" + "'";
        database.execSQL(query);
        Log.d("Update", query);
        database.close();
    }

    public Cursor get_data_struk(String kode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM pesan, transaksi WHERE pesan.kode_pesanan = transaksi.kode_pesanan AND transaksi.kode_pesanan = '" + kode + "'", null);

        return cursor;
    }

    //=======================================PRINTER===========================================
    public void insert_bt_printer(String nama_bt, String id_bt) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO " + TABLE_PRINTER + " (nama_bt, id_bt) " +
                "VALUES ('" + nama_bt + "', '" + id_bt + "')";

        Log.e("insert bt printer ", "" + queryValues);
        database.execSQL(queryValues);
        database.close();
    }

    public void update_bt(String nama, String id) {
        SQLiteDatabase database = this.getWritableDatabase();

        String query = "UPDATE " + TABLE_PRINTER + " SET "
                + NAMA_BLUETOOTH + "='" + nama + "' AND"
                + ID_BLUETOOTH + "='" + id + "'"
                + " WHERE " + ID_PRINTER + "=" + "'" + "1" + "'";
        database.execSQL(query);
        Log.d("Update", query);
        database.close();
    }

    public Cursor viewDataBluetooth() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor;

        String query = "Select * from " + TABLE_PRINTER + " WHERE id_printer =  '1'";
        cursor = sqLiteDatabase.rawQuery(query, null);


        return cursor;
    }


    //================================Kategori=================================================
    public void add_kategori(String id, String nama_kategori, String urutan) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO " + TABLE_KATEGORI + " (id, nama_kategori) " +
                "VALUES ('" + id + "', '" + nama_kategori + "')";

        Log.e("insert kategori ", "" + queryValues);
        database.execSQL(queryValues);
        database.close();
    }

    public void delete_kategori() {
        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "DELETE FROM " + TABLE_KATEGORI;
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public ArrayList<KategoriSqlite> readKategoriSqlite() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_KATEGORI + " ORDER BY urutan ASC", null);

        ArrayList<KategoriSqlite> courseModalArrayList = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList.add(new KategoriSqlite(
                        cursorCourses.getString(0),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2)));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return courseModalArrayList;
    }


    //================================Produk=================================================
    public void add_produk(String id, String id_kategori, String nama_produk, String harga, String img) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO " + TABLE_PRODUK + " (id, id_kategori, nama_produk, harga, img) " +
                "VALUES ('" + id + "', '" + id_kategori + "', '" + nama_produk + "', '" + harga + "', '" + img + "')";

        Log.e("insert produk ", "" + queryValues);
        database.execSQL(queryValues);
        database.close();
    }

    public void delete_produk() {
        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "DELETE FROM " + TABLE_PRODUK;
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public ArrayList<ProdukSqlite> readAllProdukSqlite() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_PRODUK, null);

        ArrayList<ProdukSqlite> courseModalArrayList = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList.add(new ProdukSqlite(
                        cursorCourses.getString(0),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3),
                        cursorCourses.getString(4)
                ));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return courseModalArrayList;
    }

    public ArrayList<ProdukSqlite> readProdukSqlite(String id_kat) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_PRODUK + " WHERE id_kategori = '" + id_kat + "'", null);

        ArrayList<ProdukSqlite> courseModalArrayList = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList.add(new ProdukSqlite(
                        cursorCourses.getString(0),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3),
                        cursorCourses.getString(4)
                ));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return courseModalArrayList;
    }


    //================================Bahan Produk=================================================
    public void add_bahan_produk(String id, String id_produk, String id_bahan, String terpakai, String nama_bahan) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO " + TABLE_BAHAN_PRODUK + " (id, id_produk, id_bahan, terpakai, nama_bahan) " +
                "VALUES ('" + id + "', '" + id_produk + "', '" + id_bahan + "', '" + terpakai + "', '" + nama_bahan + "')";

        Log.e("insert bahan produk ", "" + queryValues);
        database.execSQL(queryValues);
        database.close();
    }

    public void delete_bahan_produk() {
        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "DELETE FROM " + TABLE_BAHAN_PRODUK;
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public ArrayList<BahanProdukSqlite> readAllBahanProdukSqlite() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_BAHAN_PRODUK, null);

        ArrayList<BahanProdukSqlite> courseModalArrayList = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList.add(new BahanProdukSqlite(
                        cursorCourses.getString(0),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3),
                        cursorCourses.getString(4)
                ));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return courseModalArrayList;
    }

    public ArrayList<BahanProdukSqlite> readBahanProdukSqlite(String id_prod) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_BAHAN_PRODUK + " WHERE id_produk = '" + id_prod + "'", null);

        ArrayList<BahanProdukSqlite> courseModalArrayList = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList.add(new BahanProdukSqlite(
                        cursorCourses.getString(0),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3),
                        cursorCourses.getString(4)
                ));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return courseModalArrayList;
    }


    //================================Bahan Outlet=================================================
    public void add_bahan_outlet(String id, String id_bahan, String stok, String nama_bahan) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO " + TABLE_BAHAN_OUTLET + " (id, id_bahan, stok, nama_bahan) " +
                "VALUES ('" + id + "', '" + id_bahan + "', '" + stok + "', '" + nama_bahan + "')";

        Log.e("insert bahan outlet ", "" + queryValues);
        database.execSQL(queryValues);
        database.close();
    }

    public void delete_bahan_outlet() {
        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "DELETE FROM " + TABLE_BAHAN_OUTLET;
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public ArrayList<BahanOutletSqlite> readAllBahanOutletSqlite() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_BAHAN_OUTLET, null);

        ArrayList<BahanOutletSqlite> courseModalArrayList = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList.add(new BahanOutletSqlite(
                        cursorCourses.getString(0),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3)
                ));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return courseModalArrayList;
    }

    public ArrayList<BahanOutletSqlite> readBahanOutletSqlite(String id_bah) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_BAHAN_OUTLET + " WHERE id_bahan = '" + id_bah + "'", null);

        ArrayList<BahanOutletSqlite> courseModalArrayList = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList.add(new BahanOutletSqlite(
                        cursorCourses.getString(0),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3)
                ));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return courseModalArrayList;
    }

    public String readStokSqlite(String id_bah) {
        String stok = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT stok FROM " + TABLE_BAHAN_OUTLET + " WHERE id_bahan = '" + id_bah + "'", null);

        if (cursorCourses.moveToFirst()) {
            stok = cursorCourses.getString(0);
            cursorCourses.moveToNext();
        }
        cursorCourses.close();
        return stok;
    }

    public void updateStokBahan(String stok_akhir, String id_bahan) {
        SQLiteDatabase database = this.getWritableDatabase();

        String query = "UPDATE " + TABLE_BAHAN_OUTLET + " SET stok = '" + stok_akhir + "' WHERE id_bahan = '" + id_bahan + "'";
        database.execSQL(query);
        Log.d("Update", query);
        database.close();
    }


    //================================Update Info=================================================
    public void add_update_info(String id, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO " + TABLE_UPDATE_INFO + " (id, status) " +
                "VALUES ('" + id + "', '" + status + "')";

        Log.e("insert bahan outlet ", "" + queryValues);
        database.execSQL(queryValues);
        database.close();
    }

    public void delete_info() {
        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "DELETE FROM " + TABLE_UPDATE_INFO;
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public String readAllInfoSqlite(String id) {
        String info = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_UPDATE_INFO + " WHERE id = '" + id + "'", null);

        ArrayList<UpdateInfoSqlite> courseModalArrayList = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            info = cursorCourses.getString(1);
            cursorCourses.moveToNext();
        }
        cursorCourses.close();
        return info;
    }

    public void updateInfo(String status) {
        SQLiteDatabase database = this.getWritableDatabase();

        String query = "UPDATE " + TABLE_UPDATE_INFO + " SET status = '" + status + "' WHERE id = '" + "1" + "'";
        database.execSQL(query);
        Log.d("Update", query);
        database.close();
    }
}