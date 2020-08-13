package com.bagasbest.mypreloaddata.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bagasbest.mypreloaddata.R;
import com.bagasbest.mypreloaddata.model.MahasiswaModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder> {

    private ArrayList<MahasiswaModel> listMahasiswa = new ArrayList<>();

    public MahasiswaAdapter(){
    }

    public void setData (ArrayList<MahasiswaModel> listMahasiswa) {
        if(listMahasiswa.size() > 0 ){
            this.listMahasiswa.clear();
        }

        this.listMahasiswa.addAll(listMahasiswa);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MahasiswaAdapter.MahasiswaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mahasiswa_row, parent, false);
        return new MahasiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MahasiswaAdapter.MahasiswaViewHolder holder, int position) {
        holder.tvName.setText(listMahasiswa.get(position).getName());
        holder.tvNim.setText(listMahasiswa.get(position).getNim());
    }

    @Override
    public int getItemCount() {
        return listMahasiswa.size();
    }

    static class MahasiswaViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvNim;

        MahasiswaViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.txtName);
            tvNim = itemView.findViewById(R.id.txtNim);

        }
    }
}
