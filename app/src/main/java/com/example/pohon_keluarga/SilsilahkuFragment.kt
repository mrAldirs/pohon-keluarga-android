package com.example.pohon_keluarga

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pohon_keluarga.Pohon.PohonActivity
import com.example.pohon_keluarga.databinding.FragmentSilsilahBinding
import org.json.JSONArray
import org.json.JSONObject

class SilsilahkuFragment : Fragment() {
    lateinit var v : View
    private lateinit var b: FragmentSilsilahBinding
    lateinit var parent: DashboardActivity

    lateinit var preferences: SharedPreferences

    val PREF_NAME = "akun"
    val PENGGUNA = "kd_pengguna"
    val DEF_PENGGUNA = ""

    lateinit var urlClass: UrlClass
    val dataKeluarga = mutableListOf<HashMap<String,String>>()
    lateinit var keluargaAdapter : AdapterKeluarga

    var kdK = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentSilsilahBinding.inflate(layoutInflater)
        v = b.root
        parent = activity as DashboardActivity

        urlClass = UrlClass()
        preferences = v.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        keluargaAdapter = AdapterKeluarga(dataKeluarga, this)
        b.rvSilsilah.layoutManager = LinearLayoutManager(v.context)
        b.rvSilsilah.adapter = keluargaAdapter

        return v
    }

    override fun onStart() {
        super.onStart()
        showDataKeluarga("show_data_keluarga")
    }

    private fun delete(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlKeluarga,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    Toast.makeText(v.context, "Berhasil menghapus Silsilah Keluarga!", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = java.util.HashMap<String, String>()
                when(mode){
                    "delete"->{
                        hm.put("mode","delete")
                        hm.put("kd_keluarga", kdK)
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    var statusUbah = ""

    fun dialog(status: String, kode:String) {

        if (status.equals("Umum")) {
            statusUbah = "Privat"
        } else if (status.equals("Privat")) {
            statusUbah = "Umum"
        }

        AlertDialog.Builder(v.context)
            .setTitle("Ubah Silsilah!")
            .setIcon(R.drawable.warning)
            .setMessage("Apakah Anda ingin mengubah status Silsilah Keluarga ini menjadi mode $statusUbah?")
            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                ubah(statusUbah, kode)
                parent.recreate()
            })
            .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

            })
            .show()
    }

    private fun ubah(status: String, kode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlKeluarga,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    Toast.makeText(v.context, "Berhasil mengubah status Silsilah Keluarga menjadi $status!", Toast.LENGTH_SHORT).show()
                    showDataKeluarga("show_data_keluarga")
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = java.util.HashMap<String, String>()

                hm.put("mode","ubah_status")
                hm.put("kd_keluarga", kode)
                hm.put("status_keluarga", status)

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    fun showDataKeluarga(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlKeluarga,
            Response.Listener { response ->
                dataKeluarga.clear()
                if (response.equals(0)) {
                    Toast.makeText(v.context,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var  frm = HashMap<String,String>()
                        frm.put("nama_keluarga",jsonObject.getString("nama_keluarga"))
                        frm.put("kd_keluarga",jsonObject.getString("kd_keluarga"))
                        frm.put("status_keluarga",jsonObject.getString("status_keluarga"))
                        frm.put("nama_mc",jsonObject.getString("nama_mc"))

                        dataKeluarga.add(frm)
                    }
                    keluargaAdapter.notifyDataSetChanged()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(v.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "show_data_keluarga" -> {
                        hm.put("mode","show_data_keluarga")
                        hm.put("kd_pengguna", preferences.getString(PENGGUNA, DEF_PENGGUNA).toString())
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(v.context)
        queue.add(request)
    }

    class AdapterKeluarga(val dataKeluarga: List<HashMap<String,String>>, val parent: SilsilahkuFragment) :
        RecyclerView.Adapter<AdapterKeluarga.HolderDataKeluarga>(){
        class HolderDataKeluarga(v : View) : RecyclerView.ViewHolder(v) {
            val nm = v.findViewById<TextView>(R.id.adpNamaKeluarga)
            val md = v.findViewById<TextView>(R.id.adpModeratorKeluarga)
            val sts = v.findViewById<ImageView>(R.id.adpStatusKeluarga)
            val cd = v.findViewById<CardView>(R.id.card)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataKeluarga {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.row_silsilah, parent, false)
            return HolderDataKeluarga(v)
        }

        override fun getItemCount(): Int {
            return dataKeluarga.size
        }

        override fun onBindViewHolder(holder: HolderDataKeluarga, position: Int) {
            val data = dataKeluarga.get(position)
            holder.nm.setText(data.get("nama_keluarga"))
            holder.md.setText("Moderator : "+data.get("nama_mc"))

            val st = data.get("status_keluarga")
            if (st.toString().equals("Privat")) {
                holder.sts.setBackgroundResource(R.drawable.lock)
            } else {
                holder.sts.visibility = View.GONE
            }

            holder.cd.setOnClickListener {
                val intentDetail = Intent(it.context, PohonActivity::class.java)
                intentDetail.putExtra("kode",data.get("kd_keluarga"))
                intentDetail.putExtra("nm",data.get("nama_keluarga"))
                it.context.startActivity(intentDetail)
            }

            holder.cd.setOnLongClickListener {
                parent.kdK = data.get("kd_keluarga").toString()

                val contextMenu = PopupMenu(it.context, it)
                contextMenu.menuInflater.inflate(R.menu.context_silsilah, contextMenu.menu)
                contextMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.context_hapus -> {
                            AlertDialog.Builder(parent.requireContext())
                                .setTitle("Hapus Silsilah!")
                                .setIcon(R.drawable.warning)
                                .setMessage("Apakah Anda ingin menghapus Silsilah Keluarga Anda ini?")
                                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                                    parent.delete("delete")
                                    parent.requireActivity().recreate()
                                })
                                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                                })
                                .show()
                            true
                        }
                        R.id.context_ubah -> {
                            parent.dialog(data.get("status_keluarga").toString(), data.get("kd_keluarga").toString())
                        }
                    }
                    false
                }
                contextMenu.show()
                true
            }
        }
    }
}