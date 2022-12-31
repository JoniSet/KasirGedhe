package com.example.kasirroti.Fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.Helper.SingOut;
import com.example.kasirroti.Helper.SqliteHelper;
import com.example.kasirroti.Model.ListPesan;
import com.example.kasirroti.TransaksiScreen.PembayaranActivity;
import com.example.kasirroti.TransaksiScreen.PembayaranSuksesActivity;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.example.kasirroti.SessionManager.SessionManager;
import com.example.kasirroti.SessionManager.SessionManagerUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class KalkulatorFragment extends Fragment {

    private Button button0, button1, button2, button3, button4, button5, button6,
            button7, button8, button9,  buttonSub, buttonDivision,
            buttonn, button000, buttonC;

    String id_produk, jumlah, harga_satuan;
    HashMap<String, String> map1    = new HashMap<>();

    public static ImageButton buttonDel, buttonok;

    public static ImageButton buttonenter;

    public static Activity pembayaran;

    public static int uang = 0;

    SqliteHelper sqliteHelper;


    public KalkulatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view                       = inflater.inflate(R.layout.fragment_kalkulator, container, false);

        sqliteHelper                    = new SqliteHelper(getContext());

        button0     = (Button) view.findViewById(R.id.button0);
        button1     = (Button) view.findViewById(R.id.button1);
        button2     = (Button) view.findViewById(R.id.button2);
        button3     = (Button) view.findViewById(R.id.button3);
        button4     = (Button) view.findViewById(R.id.button4);
        button5     = (Button) view.findViewById(R.id.button5);
        button6     = (Button) view.findViewById(R.id.button6);
        button7     = (Button) view.findViewById(R.id.button7);
        button8     = (Button) view.findViewById(R.id.button8);
        button9     = (Button) view.findViewById(R.id.button9);
        buttonC     = (Button) view.findViewById(R.id.buttonc);
        buttonok    = view.findViewById(R.id.buttonok);
        buttonok    = view.findViewById(R.id.buttonok);
        buttonenter = view.findViewById(R.id.buttonenter);
        button000   = (Button) view.findViewById(R.id.button000);
        buttonn     = (Button) view.findViewById(R.id.buttonn);
        buttonDel   = view.findViewById(R.id.buttonx);

        pembayaran  = getActivity();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                int a       = Integer.parseInt(uang);
                int b       = PembayaranActivity.bayar;

                if (PembayaranActivity.T_uang.getText().length() > 9)
                {
                    FancyToast.makeText(getActivity(),  "Max 10 Karakter!",
                            FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                else {
                    PembayaranActivity.T_uang.setText(PembayaranActivity.T_uang.getText() + "1");
                    uang   = Integer.parseInt(String.valueOf(uang) + "1");

                    if (uang < b) {
                        buttonenter.setVisibility(View.INVISIBLE);
                    } else {
                        buttonenter.setVisibility(View.VISIBLE);
                        button1.setEnabled(true);
                    }
                }

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PembayaranActivity.T_uang.getText().length() > 9)
                {
                    FancyToast.makeText(getActivity(),  "Max 10 Karakter!",
                            FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                else {
                    PembayaranActivity.T_uang.setText(PembayaranActivity.T_uang.getText() + "2");
                    uang = Integer.parseInt(String.valueOf(uang) + "2");

                    int b = PembayaranActivity.bayar;

                    if (uang < b) {
                        buttonenter.setVisibility(View.INVISIBLE);
                    } else {
                        buttonenter.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PembayaranActivity.T_uang.getText().length() > 9)
                {
                    FancyToast.makeText(getActivity(),  "Max 10 Karakter!",
                            FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                else {
                    PembayaranActivity.T_uang.setText(PembayaranActivity.T_uang.getText() + "3");
                    uang = Integer.parseInt(String.valueOf(uang) + "3");

                    int b = PembayaranActivity.bayar;

                    if (uang < b) {
                        buttonenter.setVisibility(View.INVISIBLE);
                    } else {
                        buttonenter.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PembayaranActivity.T_uang.getText().length() > 9)
                {
                    FancyToast.makeText(getActivity(),  "Max 10 Karakter!",
                            FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                else {
                    PembayaranActivity.T_uang.setText(PembayaranActivity.T_uang.getText() + "4");
                    uang = Integer.parseInt(String.valueOf(uang) + "4");


                    int b = PembayaranActivity.bayar;

                    if (uang < b) {
                        buttonenter.setVisibility(View.INVISIBLE);
                    } else {
                        buttonenter.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PembayaranActivity.T_uang.getText().length() > 9)
                {
                    FancyToast.makeText(getActivity(),  "Max 10 Karakter!",
                            FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                else {
                    PembayaranActivity.T_uang.setText(PembayaranActivity.T_uang.getText() + "5");
                    uang = Integer.parseInt(String.valueOf(uang) + "5");

                    int b = PembayaranActivity.bayar;

                    if (uang < b) {
                        buttonenter.setVisibility(View.INVISIBLE);
                    } else {
                        buttonenter.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PembayaranActivity.T_uang.getText().length() > 9)
                {
                    FancyToast.makeText(getActivity(),  "Max 10 Karakter!",
                            FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                else {
                    PembayaranActivity.T_uang.setText(PembayaranActivity.T_uang.getText() + "6");
                    uang = Integer.parseInt(String.valueOf(uang) + "6");

                    int b = PembayaranActivity.bayar;

                    if (uang < b) {
                        buttonenter.setVisibility(View.INVISIBLE);
                    } else {
                        buttonenter.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PembayaranActivity.T_uang.getText().length() > 9)
                {
                    FancyToast.makeText(getActivity(),  "Max 10 Karakter!",
                            FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                else {
                    PembayaranActivity.T_uang.setText(PembayaranActivity.T_uang.getText() + "7");
                    uang = Integer.parseInt(String.valueOf(uang) + "7");

                    int b = PembayaranActivity.bayar;

                    if (uang < b) {
                        buttonenter.setVisibility(View.INVISIBLE);
                    } else {
                        buttonenter.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PembayaranActivity.T_uang.getText().length() > 9)
                {
                    FancyToast.makeText(getActivity(),  "Max 10 Karakter!",
                            FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                else {
                    PembayaranActivity.T_uang.setText(PembayaranActivity.T_uang.getText() + "8");
                    uang = Integer.parseInt(String.valueOf(uang) + "8");

                    int b = PembayaranActivity.bayar;

                    if (uang < b) {
                        buttonenter.setVisibility(View.INVISIBLE);
                    } else {
                        buttonenter.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PembayaranActivity.T_uang.getText().length() > 9)
                {
                    FancyToast.makeText(getActivity(),  "Max 10 Karakter!",
                            FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                else {
                    PembayaranActivity.T_uang.setText(PembayaranActivity.T_uang.getText() + "9");
                    uang = Integer.parseInt(String.valueOf(uang) + "9");

                    int b = PembayaranActivity.bayar;

                    if (uang < b) {
                        buttonenter.setVisibility(View.INVISIBLE);
                    } else {
                        buttonenter.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PembayaranActivity.T_uang.getText().length() > 9)
                {
                    FancyToast.makeText(getActivity(),  "Max 10 Karakter!",
                            FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                else {
                    PembayaranActivity.T_uang.setText(PembayaranActivity.T_uang.getText() + "0");
                    uang = Integer.parseInt(String.valueOf(uang) + "0");

                    int b = PembayaranActivity.bayar;

                    if (uang < b) {
                        buttonenter.setVisibility(View.INVISIBLE);
                    } else {
                        buttonenter.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        button000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PembayaranActivity.T_uang.getText().length() > 7)
                {
                    FancyToast.makeText(getActivity(),  "Max 10 Karakter!",
                            FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                else {
                    PembayaranActivity.T_uang.setText(PembayaranActivity.T_uang.getText() + "000");
                    uang = Integer.parseInt(String.valueOf(uang) + "000");

                    int b = PembayaranActivity.bayar;

                    if (uang < b) {
                        buttonenter.setVisibility(View.INVISIBLE);
                    } else {
                        buttonenter.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PembayaranActivity.T_uang.getText().toString().length() == 0){
                    FancyToast.makeText(getActivity(),  "Input Angka Terlebih Dahulu!",
                            FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, false).show();
                }
                else if (PembayaranActivity.T_uang.getText().toString().length() == 1)
                {
                    PembayaranActivity.T_uang.setText(PembayaranActivity.T_uang.getText().toString().substring(0
                            , PembayaranActivity.T_uang.getText().length() - 1));

                    uang    = 0;
                }
                else {
                    PembayaranActivity.T_uang.setText(PembayaranActivity.T_uang.getText().toString().substring(0
                            , PembayaranActivity.T_uang.getText().length() - 1));
//                    uang   = uang.substring(0, uang.length() -1);
                    String o       = String.valueOf(uang);
                    uang   = Integer.parseInt(o.substring(0, o.length() -1));

                    int b       = PembayaranActivity.bayar;

                    if (uang < b)
                    {
                        buttonenter.setVisibility(View.INVISIBLE);
                    }
                    else {
                        buttonenter.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PembayaranActivity.viewPager.setCurrentItem(1);
            }
        });


        if (!PembayaranActivity.B_metode_bayar.getTag().equals("Cash"))
        {
            buttonenter.setVisibility(View.VISIBLE);
        }
        else
        {
            buttonenter.setVisibility(View.INVISIBLE);
        }

        buttonenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uang < PembayaranActivity.bayar)
                {
                    buttonenter.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Uang yang anda masukkan kurang!", Toast.LENGTH_SHORT).show();
                }
                else {
                    buttonenter.setEnabled(false);
                    inputPesanan();
                }

            }
        });

        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PembayaranActivity.T_uang.setText("");
                uang    = 0;
            }
        });

        return view;
    }

    private void inputPesanan() {
        SessionManager sessionManager = new SessionManager(getActivity());
        SessionManagerUser sessionManagerUser = new SessionManagerUser(getActivity());

        HashMap<String, String> map = new HashMap<>();
        HashMap<String, String> map_user = new HashMap<>();

        map                 = sessionManager.getDetailLogin();
        String id_outlet    = map.get(sessionManager.KEY_ID);

        map_user            = sessionManagerUser.getDetailLogin();
        String token        = map_user.get(sessionManagerUser.KEY_TOKEN);

        Gson gson = new Gson();

        String listString = gson.toJson(
                PembayaranActivity.item,
                new TypeToken<ArrayList<ListPesan>>() {}.getType());

        Log.d("DataPesanan", listString);

        try {
            JSONArray jsonArray     = new JSONArray(listString);

            ProgressDialog loading  = new ProgressDialog(getActivity());
            loading.show();
            loading.setContentView(R.layout.loading);
            loading.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            loading.setCancelable(false);

            TextView txt_title          = loading.findViewById(R.id.txt_title);
            txt_title.setText("Membuat\nStuk Pesanan");

            AndroidNetworking.post(Server.URL + "save_transaksi")
                    .addBodyParameter("id_outlet", id_outlet)
                    .addBodyParameter("total_harga", String.valueOf(PembayaranActivity.bayar))
                    .addBodyParameter("customer", PembayaranActivity.T_nama_pel.getText().toString())
                    .addBodyParameter("id_customer", PembayaranActivity.id_customer)
                    .addBodyParameter("metode_bayar", PembayaranActivity.B_metode_bayar.getTag().toString())
                    .addBodyParameter("uang_bayar", PembayaranActivity.T_uang.getText().toString())
                    .addBodyParameter("produk", jsonArray.toString())
                    .addHeaders("Authorization", "Bearer " + token)
                    .addHeaders("Accept", "application/json")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                String res_kode     = response.getString("response");
                                String id_transaksi = response.getString("id_transaksi");
                                String jml, id_bahan;

                                if (res_kode.equals("200"))
                                {
                                    if (!sqliteHelper.readAllBahanOutletSqlite().isEmpty()) {
                                        for (Map.Entry<String, String> entry : PembayaranActivity.map.entrySet()) {
                                            id_bahan = entry.getKey();
                                            jml = entry.getValue();
                                            int jml_fnl = 0;
                                            Log.d("Salah", "id_bahan = " + sqliteHelper.readStokSqlite(id_bahan) +"\n" + jml);
                                            if (!sqliteHelper.readStokSqlite(id_bahan).isEmpty() || !jml.isEmpty()) {
                                                jml_fnl = Integer.parseInt(sqliteHelper.readStokSqlite(id_bahan)) - Integer.parseInt(jml);
                                            }
                                            else{
                                                FancyToast.makeText(getContext(), "Stok bahan anda kosong",
                                                        FancyToast.WARNING, FancyToast.LENGTH_SHORT, false).show();
                                            }

                                            sqliteHelper.updateStokBahan(String.valueOf(jml_fnl), id_bahan);
                                            Log.d("CekData", id_bahan + " = " + String.valueOf(jml_fnl));
                                        }
                                    }

                                    Intent intent   = new Intent(getActivity(), PembayaranSuksesActivity.class);
                                    if (!response.getString("poin").equals("[]")){
                                        JSONObject poin = new JSONObject(response.getString("poin"));
                                        intent.putExtra("poin_baru", poin.getString("poin_baru"));
                                        intent.putExtra("poin_total", poin.getString("poin_total"));
                                        Log.d("CekPoin", poin.toString());
                                    }
                                    else {
                                        intent.putExtra("poin_baru", "");
                                        intent.putExtra("poin_total", "");
                                        Log.d("CekPoin", "Tidak ada customer");
                                    }

                                    intent.putExtra("bayar", String.valueOf(PembayaranActivity.bayar));
                                    intent.putExtra("uang", PembayaranActivity.T_uang.getText().toString());
                                    intent.putExtra("nama", PembayaranActivity.T_nama_pel.getText().toString());
                                    intent.putExtra("metode", PembayaranActivity.B_metode_bayar.getTag().toString());
                                    intent.putExtra("id_transaksi", id_transaksi);
                                    startActivity(intent);


                                    PembayaranActivity.T_uang.setText("");
                                    uang            = 0;
                                    loading.dismiss();
                                    PembayaranActivity.id_customer      = "";
                                    PembayaranActivity.T_nama_pel.setText("");
                                }
                                else
                                {
                                    loading.dismiss();
                                    FancyToast.makeText(getActivity(),  response.getString("message"),
                                            FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                                }
                            } catch (JSONException e) {
                                loading.dismiss();
                                e.printStackTrace();
                                FancyToast.makeText(getActivity(),  e.getMessage(),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            if (anError.getErrorCode() == 401){
                                SingOut signOut = new SingOut();
                                signOut.Logout(getActivity());
                            }
                            else {
                                loading.dismiss();
                                FancyToast.makeText(getActivity(), anError.getMessage(),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
