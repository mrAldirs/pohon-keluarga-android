package com.example.pohon_keluarga

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

class SilsilahDibagikanFragment : Fragment() {
    lateinit var v : View
    private lateinit var b: FragmentSilsilahBinding
    lateinit var preferences: SharedPreferences

    val PREF_NAME = "akun"
    val PENGGUNA = "kd_pengguna"
    val DEF_PENGGUNA = ""

    lateinit var urlClass: UrlClass
    val dataKeluarga = mutableListOf<HashMap<String,String>>()
    lateinit var keluargaAdapter : AdapterKeluarga

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentSilsilahBinding.inflate(layoutInflater)
        v = b.root

        urlClass = UrlClass()
        preferences = v.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        keluargaAdapter = AdapterKeluarga(dataKeluarga)
        b.rvSilsilah.layoutManager = LinearLayoutManager(v.context)
        b.rvSilsilah.adapter = keluargaAdapter

        return v
    }

    override fun onStart() {
        super.onStart()
        showDataKeluarga()
    }

    private fun showDataKeluarga() {
        val request = object : StringRequest(
            Method.POST,urlClass.urlAkses,
            Response.Listener { response ->
                dataKeluarga.clear()
                val jsonArray = JSONArray(response)
                if (jsonArray.length() == 0) {
                    b.tvNotFound.visibility = View.VISIBLE
                    b.rvSilsilah.visibility = View.GONE
                } else {
                    b.tvNotFound.visibility = View.GONE
                    b.rvSilsilah.visibility = View.VISIBLE
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var  frm = HashMap<String,String>()
                        frm.put("kd_akses",jsonObject.getString("kd_akses"))
                        frm.put("kd_pengguna",jsonObject.getString("kd_pengguna"))
                        frm.put("username",jsonObject.getString("username"))
                        frm.put("kd_keluarga",jsonObject.getString("kd_keluarga"))
                        frm.put("nama_keluarga",jsonObject.getString("nama_keluarga"))
                        frm.put("tgl_akses",jsonObject.getString("tgl_akses"))
                        frm.put("status_akses",jsonObject.getString("status_akses"))

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

                hm.put("mode","akses_silsilah_acc")
                hm.put("kd_pengguna", preferences.getString(PENGGUNA, DEF_PENGGUNA).toString())

                return hm
            }
        }
        val queue = Volley.newRequestQueue(v.context)
        queue.add(request)
    }

    class AdapterKeluarga(val dataKeluarga: List<HashMap<String,String>>) :
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
            holder.md.setText("Dibagikan oleh : "+data.get("username"))
            holder.sts.setBackgroundResource(R.drawable.bagikan)

            holder.cd.setOnClickListener {
                val intentDetail = Intent(it.context, PohonActivity::class.java)
                intentDetail.putExtra("kode",data.get("kd_keluarga"))
                intentDetail.putExtra("nm",data.get("nama_keluarga"))
                intentDetail.putExtra("akses", "akses")
                it.context.startActivity(intentDetail)
            }
        }
    }
}