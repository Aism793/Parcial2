package com.example.practicaparcial2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolder> implements View.OnClickListener {

    private View.OnClickListener listener;
    public ArrayList<Cancion> listCanciones;

    public AdapterDatos(ArrayList<Cancion> listCanciones) {
        this.listCanciones = listCanciones;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item;

        public ViewHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.text_view_item);
        }
    }

    @Override
    public AdapterDatos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        view.setOnClickListener(this);

        AdapterDatos.ViewHolder viewHolder = new AdapterDatos.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterDatos.ViewHolder holder, int position) {
        holder.item.setText(listCanciones.get(position).nombre + " - " + listCanciones.get(position).artista + " - " + listCanciones.get(position).duracion);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }


    @Override
    public int getItemCount() {
        return listCanciones.size();
    }
}
