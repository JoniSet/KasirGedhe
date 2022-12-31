package com.example.kasirroti.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
public class MetodeFragment extends Fragment {

    private TextView txt_pas, txt_dua, txt_lima, txt_seratus;
    private ProgressBar loading_uang_pas;
    SqliteHelper sqliteHelper;

    public MetodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view               = inflater.inflate(R.layout.fragment_metode, container, false);

        sqliteHelper            = new SqliteHelper(getContext());

        txt_pas                 = view.findViewById(R.id.txt_pas);
        txt_dua                 = view.findViewById(R.id.txt_dua);
        txt_lima                = view.findViewById(R.id.txt_lima);
        txt_seratus             = view.findViewById(R.id.txt_seratus);
        loading_uang_pas        = view.findViewById(R.id.loading_uang_pas);

        txt_pas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPesanan(String.valueOf(PembayaranActivity.bayar));
                txt_pas.setClickable(false);
            }
        });

        txt_dua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (20000 < PembayaranActivity.bayar)
                {
                    FancyToast.makeText(getActivity(),  "Uang Yang Anda Masukkan Kurang!",
                            FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                else {
                    txt_dua.setClickable(false);
                    inputPesanan("20000");
                }
            }
        });

        txt_lima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (50000 < PembayaranActivity.bayar)
                {
                    FancyToast.makeText(getActivity(),  "Uang Yang Anda Masukkan Kurang!",
                            FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                else {
                    txt_lima.setClickable(false);
                    inputPesanan("50000");
                }
            }
        });

        txt_seratus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (100000 < PembayaranActivity.bayar)
                {
                    FancyToast.makeText(getActivity(),  "Uang Yang Anda Masukkan Kurang!",
                            FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                else {
                    txt_seratus.setClickable(false);
                    inputPesanan("100000");
                }
            }
        });

        return view;
    }

    private void inputPesanan(String uang) {
        loading_uang_pas.setVisibility(View.VISIBLE);
        SessionManager sessionManager           = new SessionManager(getActivity());
        SessionManagerUser sessionManagerUser   = new SessionManagerUser(getActivity());

        HashMap<String, String> map = new HashMap<>();
        HashMap<String, String> map_user = new HashMap<>();

        map = sessionManager.getDetailLogin();
        String id_outlet    = map.get(sessionManager.KEY_ID);

        map_user            = sessionManagerUser.getDetailLogin();
        String token        = map_user.get(sessionManagerUser.KEY_TOKEN);

        Gson gson = new Gson();

        String listString = gson.toJson(
                PembayaranActivity.item,
                new TypeToken<ArrayList<ListPesan>>() {}.getType());

        try {
            JSONArray jsonArray     = new JSONArray(listString);

            ProgressDialog loading  = new ProgressDialog(getActivity());
            loading.show();
            loading.setContentView(R.layout.loading);
            loading.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            AndroidNetworking.post(Server.URL + "save_transaksi")
                    .addBodyParameter("id_outlet", id_outlet)
                    .addBodyParameter("total_harga", String.valueOf(PembayaranActivity.bayar))
                    .addBodyParameter("customer", PembayaranActivity.T_nama_pel.getText().toString())
                    .addBodyParameter("id_customer", PembayaranActivity.id_customer)
                    .addBodyParameter("uang_bayar", uang)
                    .addBodyParameter("metode_bayar", PembayaranActivity.B_metode_bayar.getTag().toString())
                    .addBodyParameter("produk", jsonArray.toString())
                    .addHeaders("Authorization", "Bearer " + token)
                    .addHeaders("Accept", "application/json")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            loading_uang_pas.setVisibility(View.GONE);
                            txt_pas.setClickable(true);
                            txt_dua.setClickable(true);
                            txt_lima.setClickable(true);
                            txt_seratus.setClickable(true);

                            try {
                                String res_kode     = response.getString("response");
                                String id_transaksi = response.getString("id_transaksi");
                                String jml, id_bahan;
                                Log.d("Hasil", response.toString());

                                if (res_kode.equals("200"))
                                {
                                    if (!sqliteHelper.readAllBahanOutletSqlite().isEmpty()) {
                                        for (Map.Entry<String, String> entry : PembayaranActivity.map.entrySet()) {
                                            id_bahan = entry.getKey();
                                            jml = entry.getValue();
                                            int jml_fnl = Integer.parseInt(sqliteHelper.readStokSqlite(id_bahan)) - Integer.parseInt(jml);

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
                                    intent.putExtra("uang", uang);
                                    intent.putExtra("nama", PembayaranActivity.T_nama_pel.getText().toString());
                                    intent.putExtra("metode", "Cash");
                                    intent.putExtra("id_transaksi", id_transaksi);

                                    startActivity(intent);
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
                                e.printStackTrace();
                                loading_uang_pas.setVisibility(View.GONE);
                                txt_pas.setClickable(true);
                                txt_dua.setClickable(true);
                                txt_lima.setClickable(true);
                                txt_seratus.setClickable(true);
                                loading.dismiss();
                                FancyToast.makeText(getActivity(),  e.getMessage(),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                Log.d("Hasil", e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            if (anError.getErrorCode() == 401){
                                SingOut signOut = new SingOut();
                                signOut.Logout(getActivity());
                            }
                            else {
                                loading_uang_pas.setVisibility(View.GONE);
                                txt_pas.setClickable(true);
                                txt_dua.setClickable(true);
                                txt_lima.setClickable(true);
                                txt_seratus.setClickable(true);
                                loading.dismiss();
                                FancyToast.makeText(getActivity(),  anError.getErrorDetail(),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                Log.d("Hasil", anError.toString());
                            }
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
            loading_uang_pas.setVisibility(View.GONE);
            txt_pas.setClickable(true);
            txt_dua.setClickable(true);
            txt_lima.setClickable(true);
            txt_seratus.setClickable(true);
        }

    }

}
