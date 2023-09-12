package com.example.pohon_keluarga.Adapter

import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.pohon_keluarga.AlbumKeluargaFragment
import com.example.pohon_keluarga.ImageDetailActivity
import com.example.pohon_keluarga.R
import com.squareup.picasso.Picasso

class AdapterAlbum(val dataAlbum: List<HashMap<String,String>>, val parent: AlbumKeluargaFragment) :
    RecyclerView.Adapter<AdapterAlbum.HolderDataAlbum>(){
    class HolderDataAlbum(v : View) : RecyclerView.ViewHolder(v) {
        val nm = v.findViewById<TextView>(R.id.adpNamaAlbum)
        val img = v.findViewById<ImageView>(R.id.adpAlbum)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataAlbum {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_album, parent, false)
        return HolderDataAlbum(v)
    }

    override fun getItemCount(): Int {
        return dataAlbum.size
    }

    override fun onBindViewHolder(holder: HolderDataAlbum, position: Int) {
        val data = dataAlbum.get(position)
        holder.nm.setText(data.get("foto_album"))
        Picasso.get().load(data.get("img_album")).into(holder.img)

        holder.img.setOnClickListener {
            val intent = Intent(it.context, ImageDetailActivity::class.java)
            intent.putExtra("img", data.get("img_album").toString())
            it.context.startActivity(intent)
        }

        holder.img.setOnLongClickListener {
            parent.kdAl = data.get("kd_album").toString()

            val contextMenu = PopupMenu(it.context, it)
            contextMenu.menuInflater.inflate(R.menu.context_hapus, contextMenu.menu)
            contextMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.context_hapus -> {
                        AlertDialog.Builder(parent.requireContext())
                            .setTitle("Hapus Album!")
                            .setIcon(R.drawable.warning)
                            .setMessage("Apakah Anda ingin menghapus Album ini?")
                            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                                parent.delete("delete")
                                parent.b.refresh.isRefreshing = true
                                parent.refreshData()
                            })
                            .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                            })
                            .show()
                        true
                    }
                }
                false
            }
            contextMenu.show()
            true
        }
    }
}