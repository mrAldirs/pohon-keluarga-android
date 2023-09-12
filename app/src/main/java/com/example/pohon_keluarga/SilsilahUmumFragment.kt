package com.example.pohon_keluarga

import android.content.Intent
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

class SilsilahUmumFragment : Fragment() {
    lateinit var v : View
    private lateinit var b: FragmentSilsilahBinding

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
        keluargaAdapter = AdapterKeluarga(dataKeluarga)
        b.rvSilsilah.layoutManager = LinearLayoutManager(v.context)
        b.rvSilsilah.adapter = keluargaAdapter

        return v
    }

    override fun onStart() {
        super.onStart()
        showDataKeluarga("show_data_keluarga_umum")
    }

    private fun showDataKeluarga(mode: String) {
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
                    "show_data_keluarga_umum" -> {
                        hm.put("mode","show_data_keluarga_umum")
                    }
                }

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
            holder.md.setText("Moderator : "+data.get("nama_mc"))

            val st = data.get("status_keluarga")
            if (!st.toString().equals("Privat")) {
                holder.sts.visibility = View.GONE
            }

            holder.cd.setOnClickListener {
                val intentDetail = Intent(it.context, PohonActivity::class.java)
                intentDetail.putExtra("kode",data.get("kd_keluarga"))
                intentDetail.putExtra("nm",data.get("nama_keluarga"))
                it.context.startActivity(intentDetail)
            }
        }
    }
}