package com.example.pohon_keluarga.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pohon_keluarga.R
import com.example.pohon_keluarga.Reuni.ReuniActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class AdapterReuniMasuk (val dataKeluarga: List<HashMap<String,String>>, val parent: ReuniActivity) :
    RecyclerView.Adapter<AdapterReuniMasuk.HolderDataKeluarga>(){
    class HolderDataKeluarga(v : View) : RecyclerView.ViewHolder(v) {
        val img = v.findViewById<CircleImageView>(R.id.reu_imgPengguna)
        val nm = v.findViewById<TextView>(R.id.reu_NamaPengguna)
        val tgl = v.findViewById<TextView>(R.id.reu_tgl)
        val psn = v.findViewById<TextView>(R.id.reu_pesan)
        val sts = v.findViewById<TextView>(R.id.reu_status)
        val cd = v.findViewById<CardView>(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataKeluarga {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_reuni, parent, false)
        return HolderDataKeluarga(v)
    }

    override fun getItemCount(): Int {
        return dataKeluarga.size
    }

    override fun onBindViewHolder(holder: HolderDataKeluarga, position: Int) {
        val data = dataKeluarga.get(position)
        holder.nm.setText(data.get("nama_mc"))
        holder.tgl.setText(data.get("tgl_reuni"))
        holder.psn.setText(data.get("pesan"))
        Picasso.get().load(data.get("img")).into(holder.img)

        val sts = data.get("status_reuni").toString()
        if (sts.equals("done")) {
            holder.sts.visibility = View.GONE
        } else if (sts.equals("baru")) {
            holder.sts.visibility = View.VISIBLE
        }

        holder.cd.setOnClickListener {
            parent.lihatUndanganBaru(data.get("kd_reuni").toString())
        }
    }
}