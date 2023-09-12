package com.example.pohon_keluarga.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pohon_keluarga.R

class AdapterReuniTerkirim (val dataList: List<HashMap<String,String>>) :
    RecyclerView.Adapter<AdapterReuniTerkirim.HolderDataKeluarga>(){
    class HolderDataKeluarga(v : View) : RecyclerView.ViewHolder(v) {
        val jam = v.findViewById<TextView>(R.id.reu_jam)
        val tgl = v.findViewById<TextView>(R.id.reu_tgl)
        val psn = v.findViewById<TextView>(R.id.reu_pesan)
        val tmp = v.findViewById<TextView>(R.id.reu_tempat)
        val jml = v.findViewById<TextView>(R.id.reu_jumlah)
        val cd = v.findViewById<CardView>(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataKeluarga {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_reuni_terkirim, parent, false)
        return HolderDataKeluarga(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HolderDataKeluarga, position: Int) {
        val data = dataList.get(position)
        holder.jam.setText(data.get("jam_reuni"))
        holder.tgl.setText(data.get("tgl_reuni"))
        holder.psn.setText(data.get("pesan"))
        holder.tmp.setText(data.get("tempat"))
        val value = data.get("value").toString()
        holder.jml.setText("Dikirim ke $value pengguna")
    }
}