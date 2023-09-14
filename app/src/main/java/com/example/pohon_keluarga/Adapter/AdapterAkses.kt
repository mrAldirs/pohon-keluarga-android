package com.example.pohon_keluarga.Adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pohon_keluarga.HakAkses.AksesSilsilahActivity
import com.example.pohon_keluarga.Pohon.PohonActivity
import com.example.pohon_keluarga.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class AdapterAkses (val dataKeluarga: List<HashMap<String,String>>, val parent: AksesSilsilahActivity) :
    RecyclerView.Adapter<AdapterAkses.HolderDataKeluarga>(){
    class HolderDataKeluarga(v : View) : RecyclerView.ViewHolder(v) {
        val img = v.findViewById<CircleImageView>(R.id.aks_imgPengguna)
        val klg = v.findViewById<TextView>(R.id.aks_keluarga)
        val tgl = v.findViewById<TextView>(R.id.aks_tanggal)
        val nm = v.findViewById<TextView>(R.id.aks_namaPengguna)
        val sts = v.findViewById<TextView>(R.id.aks_status)
        val cd = v.findViewById<CardView>(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataKeluarga {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_akses, parent, false)
        return HolderDataKeluarga(v)
    }

    override fun getItemCount(): Int {
        return dataKeluarga.size
    }

    override fun onBindViewHolder(holder: HolderDataKeluarga, position: Int) {
        val data = dataKeluarga.get(position)
        holder.klg.setText(data.get("nama_keluarga"))
        holder.tgl.setText(data.get("tgl_akses"))
        holder.nm.setText("Dibagikan oleh "+data.get("username"))
        Picasso.get().load(data.get("img")).into(holder.img)

        val sts = data.get("status_akses").toString()
        if (sts.equals("acc")) {
            holder.sts.visibility = View.GONE
        } else if (sts.equals("baru")) {
            holder.sts.visibility = View.VISIBLE
        } else if (sts.equals("tolak")) {
            holder.sts.text = "Tolak"
            holder.sts.setTextColor(Color.RED)
        }

        holder.cd.setOnClickListener {
            if (sts.equals("baru")) {
                parent.dialog(data.get("kd_akses").toString(), data.get("username").toString())
            } else if (sts.equals("tolak")) {
                parent.delAkses(data.get("kd_akses").toString())
            }else {
                val intentDetail = Intent(it.context, PohonActivity::class.java)
                intentDetail.putExtra("kode",data.get("kd_keluarga"))
                intentDetail.putExtra("nm",data.get("nama_keluarga"))
                intentDetail.putExtra("akses", "akses")
                it.context.startActivity(intentDetail)
            }
        }
    }
}