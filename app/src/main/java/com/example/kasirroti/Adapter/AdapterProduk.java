package com.example.kasirroti.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.Model.ListGridPesanan;
import com.example.kasirroti.Model.ListProduk;
import com.example.kasirroti.Model.ListStokRotiP;
import com.example.kasirroti.R;
import com.example.kasirroti.Helper.Tanggal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterProduk extends RecyclerView.Adapter<AdapterProduk.ViewHolder> {

    Context context;
    private List<ListProduk> listProduk;

    Tanggal tanggal         = new Tanggal();
    String no;

    Dialog dialog;

    public AdapterProduk(Context context, List<ListProduk> listProduk) {
        this.context    = context;
        this.listProduk = listProduk;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view       = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_produk, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListProduk data = listProduk.get(position);
        holder.txt_harga.setText(tanggal.formatRupiah(Double.parseDouble(data.getHarga())));
        holder.txt_nama_roti_a.setText(data.getNama_produk());
        String gambar   = data.getImg();
        String a        = String.valueOf(position);

        Picasso.get()
                .load(gambar)
                .into(holder.img_a);

//        holder.setIsRecyclable(false);

        if (HomeActivity.listGridPesanan.size() > 0)
        {
            for (int i = 0; i < HomeActivity.mappp.size(); i++){
                if (HomeActivity.mappp.containsKey(data.getId()))
                {
                    for (int o = 0; o < HomeActivity.listGridPesanan.size(); o++)
                    {
                        if (HomeActivity.listGridPesanan.get(o).getId_produk().equals(data.getId())) {
                            holder.txt_jml.setText(HomeActivity.listGridPesanan.get(o).getJumlah());
                            holder.txt_jml.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.txt_jml.setVisibility(View.VISIBLE);
                HomeActivity.status             = "0";
                HomeActivity.txt_lanjut.setText("Lanjut");

                if (HomeActivity.mappp.size() == 0)
                {
                    HomeActivity.mappp.put(data.getId(), a);
                    HomeActivity.fab_hapus.setVisibility(View.VISIBLE);
                    data.setJml("1");

                    //setting textView
                    int jnl_total   = Integer.parseInt(HomeActivity.txt_total_pesanan.getText().toString()) + 1;
                    HomeActivity.txt_total_pesanan.setText(String.valueOf(jnl_total));

                    //setting data pesanan
                    buat_pesanan(data.getId(), data);
                    HomeActivity.R_lanjut.setBackgroundResource(R.color.colorAccent);
                }
                else if (HomeActivity.mappp.containsKey(data.getId()))
                {
                    data.setJml("1");
                    int jumlah      = Integer.parseInt(holder.txt_jml.getText().toString()) + 1;
                    holder.txt_jml.setText(String.valueOf(jumlah));
                    data.setJml(String.valueOf(jumlah));

                    //setting textView
                    int jnl_total   = Integer.parseInt(HomeActivity.txt_total_pesanan.getText().toString()) + 1;
                    HomeActivity.txt_total_pesanan.setText(String.valueOf(jnl_total));

                    //setting data pesanan
                    tambah_pesanan(data.getId(), jumlah);
                    HomeActivity.R_lanjut.setBackgroundResource(R.color.colorAccent);
                }
                else {
                    HomeActivity.mappp.put(data.getId(), a);
                    data.setJml("1");

                    //setting textView
                    int jnl_total   = Integer.parseInt(HomeActivity.txt_total_pesanan.getText().toString()) + 1;
                    HomeActivity.txt_total_pesanan.setText(String.valueOf(jnl_total));

                    //setting data pesanan
                    buat_pesanan(data.getId(), data);
                    HomeActivity.R_lanjut.setBackgroundResource(R.color.colorAccent);
                }
            }
        });

        holder.R_8.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dialog              = new Dialog(context);
                dialog.setContentView(R.layout.dialog_set_pesanan);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                //initialize =======================================================================
                ImageView img_c             = dialog.findViewById(R.id.img_c);
                ImageView img_produk_plus   = dialog.findViewById(R.id.img_produk_plus);
                ImageView img_produk_min    = dialog.findViewById(R.id.img_produk_min);
                TextView txt_nama_produk    = dialog.findViewById(R.id.txt_nama_produk);
                EditText txt_jumlah_produk  = dialog.findViewById(R.id.txt_jumlah_produk);
                Button btn_simpan1          = dialog.findViewById(R.id.btn_simpan1);
                Button btn_batal            = dialog.findViewById(R.id.btn_batal);

                //Setting data======================================================================
                txt_jumlah_produk.setText(holder.txt_jml.getText().toString());
                txt_nama_produk.setText(holder.txt_nama_roti_a.getText().toString());
                Picasso.get()
                        .load(gambar)
                        .fit()
                        .centerCrop()
                        .into(img_c);
                if (holder.txt_jml.getVisibility() == View.GONE){
                    img_produk_min.setVisibility(View.GONE);
                }else {
                    img_produk_min.setVisibility(View.VISIBLE);
                }

                //setting function==================================================================
                btn_batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btn_simpan1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int awal        = Integer.parseInt(data.getJml());
                        int akhir       = Integer.parseInt(txt_jumlah_produk.getText().toString());
                        int total;
                        int total_awal  = Integer.parseInt(HomeActivity.txt_total_pesanan.getText().toString());
                        int total_akhir;
                        if (awal < akhir){
                            total       = akhir - awal;
                            total_akhir = total_awal + total;
                            HomeActivity.txt_total_pesanan.setText(String.valueOf(total_akhir));
                        }else {
                            total       = awal - akhir;
                            total_akhir = total_awal - total;
                            HomeActivity.txt_total_pesanan.setText(String.valueOf(total_akhir));
                        }

                        HomeActivity.status             = "0";
                        HomeActivity.txt_lanjut.setText("Lanjut");

                        if (HomeActivity.mappp.size() == 0)
                        {
                            if (txt_jumlah_produk.getText().toString().equals("0"))
                            {
                                dialog.dismiss();
                            }
                            else {
                                HomeActivity.mappp.put(data.getId(), a);
                                HomeActivity.fab_hapus.setVisibility(View.VISIBLE);
                                data.setJml(txt_jumlah_produk.getText().toString());

                                //setting data pesanan
                                buat_pesanan(data.getId(), data);
                                HomeActivity.R_lanjut.setBackgroundResource(R.color.colorAccent);

                                holder.txt_jml.setVisibility(View.VISIBLE);
                                holder.txt_jml.setText(txt_jumlah_produk.getText().toString());
                                dialog.dismiss();
                            }
                        }
                        else if (HomeActivity.mappp.containsKey(data.getId()))
                        {
                            if (txt_jumlah_produk.getText().toString().equals("0")){
                                holder.txt_jml.setVisibility(View.GONE);
                                HomeActivity.mappp.remove(data.getId());
                                data.setJml(txt_jumlah_produk.getText().toString());
                                holder.txt_jml.setText(txt_jumlah_produk.getText().toString());
                                data.setJml(String.valueOf(total_akhir));

                                //setting data pesanan
                                tambah_pesanan(data.getId(), Integer.parseInt(txt_jumlah_produk.getText().toString()));
                                HomeActivity.R_lanjut.setBackgroundResource(R.color.colorAccent);
                                dialog.dismiss();
                            }
                            else {
                                data.setJml(txt_jumlah_produk.getText().toString());
                                holder.txt_jml.setText(txt_jumlah_produk.getText().toString());
                                data.setJml(String.valueOf(total_akhir));

                                //setting data pesanan
                                tambah_pesanan(data.getId(), Integer.parseInt(txt_jumlah_produk.getText().toString()));
                                HomeActivity.R_lanjut.setBackgroundResource(R.color.colorAccent);
                                dialog.dismiss();
                            }
                        }
                        else {
                            if (txt_jumlah_produk.getText().toString().equals("0"))                            {
                                dialog.dismiss();
                            }
                            else {
                                HomeActivity.mappp.put(data.getId(), a);
                                data.setJml(txt_jumlah_produk.getText().toString());

                                //setting data pesanan
                                buat_pesanan(data.getId(), data);
                                HomeActivity.R_lanjut.setBackgroundResource(R.color.colorAccent);

                                holder.txt_jml.setVisibility(View.VISIBLE);
                                holder.txt_jml.setText(txt_jumlah_produk.getText().toString());
                                dialog.dismiss();
                            }
                        }

                    }
                });

                img_produk_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String jumlah   = txt_jumlah_produk.getText().toString();
                        int jlh         = 0;
                        if (jumlah.isEmpty()){
                            jlh         = 0;
                        }else{
                            jlh         = Integer.parseInt(jumlah);
                        }
                        int jumlah_final= jlh + 1;

                        txt_jumlah_produk.setText(String.valueOf(jumlah_final));
                        img_produk_min.setVisibility(View.VISIBLE);
                    }
                });

                img_produk_min.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String jumlah   = txt_jumlah_produk.getText().toString();
                        int jlh         = 0;
                        if (jumlah.isEmpty()){
                            jlh         = 0;
                        }else{
                            jlh         = Integer.parseInt(jumlah);
                        }
                        int jumlah_final= jlh - 1;

                        txt_jumlah_produk.setText(String.valueOf(jumlah_final));
                        if (txt_jumlah_produk.getText().toString().equals("0")){
                            img_produk_min.setVisibility(View.GONE);
                        }
                    }
                });

                return true;
            }
        });

    }


    @Override
    public int getItemCount() {
        return listProduk.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView img_a;
        TextView txt_nama_roti_a, txt_harga, txt_jml;
        RelativeLayout R_8;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_a               = itemView.findViewById(R.id.img_a);
            txt_jml             = itemView.findViewById(R.id.txt_jml);
            txt_nama_roti_a     = itemView.findViewById(R.id.txt_nama_roti_a);
            txt_harga           = itemView.findViewById(R.id.txt_harga_a);
            R_8                 = itemView.findViewById(R.id.R_8);

        }

        @Override
        public void onClick(View view) {

        }
    }


    //=======================================Step pesanan===========================================
    public void tambah_pesanan(String id_produk, int jumlah){
        for (int i = 0; i < HomeActivity.listGridPesanan.size(); i++)
        {
            if (HomeActivity.listGridPesanan.get(i).getId_produk().equals(id_produk))
            {
                HomeActivity.listGridPesanan.get(i).setJumlah(String.valueOf(jumlah));
            }
        }
    }

    public void buat_pesanan(String id_produk, ListProduk listProduk){
        ArrayList<ListStokRotiP> listStokRotiParent     = new ArrayList<>();
        ListGridPesanan list        = new ListGridPesanan(
                id_produk,
                listProduk.getNama_produk(),
                listProduk.getJml(),
                listProduk.getHarga(),
                listProduk.getImg(),
                String.valueOf(
                        Integer.parseInt(listProduk.getJml()) *
                                Integer.parseInt(listProduk.getHarga())
                ),
                listStokRotiParent
        );

        HomeActivity.listGridPesanan.add(list);
    }

    //==============================================================================================

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

}
