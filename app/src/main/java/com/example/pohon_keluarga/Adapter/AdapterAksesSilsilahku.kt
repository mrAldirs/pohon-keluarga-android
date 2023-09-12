package com.example.pohon_keluarga.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pohon_keluarga.HakAkses.AksesBagikanFragment
import com.example.pohon_keluarga.R

class AdapterAksesSilsilahku (val dataKeluarga: List<HashMap<String,String>>) :
    RecyclerView.Adapter<AdapterAksesSilsilahku.HolderDataKeluarga>(){
    class HolderDataKeluarga(v : View) : RecyclerView.ViewHolder(v) {
        val klg = v.findViewById<TextView>(R.id.bagikan_keluarga)
        val jml = v.findViewById<TextView>(R.id.bagikan_jumlah)
        val btn = v.findViewById<ImageView>(R.id.bagikan_btn)
        val cd = v.findViewById<CardView>(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataKeluarga {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_akses_silsilahku, parent, false)
        return HolderDataKeluarga(v)
    }

    override fun getItemCount(): Int {
        return dataKeluarga.size
    }

    override fun onBindViewHolder(holder: HolderDataKeluarga, position: Int) {
        val data = dataKeluarga.get(position)
        holder.klg.setText(data.get("nama_keluarga"))
        holder.jml.setText("Jumlah Dibagikan : "+data.get("value"))

        holder.btn.setOnClickListener {
            val frag = AksesBagikanFragment()

            val bundle = Bundle()
            bundle.putString("kode", data.get("kd_keluarga").toString())
            bundle.putString("keluarga", data.get("nama_keluarga").toString())
            frag.arguments = bundle

            val fragmentManager = (it.context as AppCompatActivity).supportFragmentManager
            frag.show(fragmentManager, "bagikan")
        }
    }
}