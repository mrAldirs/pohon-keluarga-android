package com.example.pohon_keluarga

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pohon_keluarga.DetailAnggota.DetailAnggotaActivity
import com.example.pohon_keluarga.databinding.FragmentAnggotaKeluargaBinding
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONArray

class AnggotaKeluargaFragment : Fragment() {
    private lateinit var b: FragmentAnggotaKeluargaBinding
    lateinit var v: View

    lateinit var urlClass: UrlClass
    val dataKeluarga = mutableListOf<HashMap<String,String>>()
    lateinit var keluargaAdapter : AdapterKeluarga

    lateinit var namaAdapter: ArrayAdapter<String>
    val daftarNama = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentAnggotaKeluargaBinding.inflate(layoutInflater)
        v = b.root

        val bundle = arguments
        val kode = bundle?.get("kode").toString()

        urlClass = UrlClass()
        keluargaAdapter = AdapterKeluarga(dataKeluarga)
        b.rvAnggota.layoutManager = LinearLayoutManager(v.context)
        b.rvAnggota.adapter = keluargaAdapter

        b.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                showDataKeluarga("read_anggota", b.etSearch.text.toString().trim())
                true
            } else {
                false
            }
        }

        namaAdapter = ArrayAdapter(v.context, android.R.layout.simple_list_item_1, daftarNama)
        b.etSearch.setAdapter(namaAdapter)
        b.etSearch.threshold = 0

        return v
    }

    override fun onStart() {
        super.onStart()
        showDataKeluarga("read_anggota", "")
        getNama()
    }

    private fun showDataKeluarga(mode: String, nama_anggota: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlAnggota,
            Response.Listener { response ->
                dataKeluarga.clear()
                if (response.equals(0)) {
                    Toast.makeText(v.context,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var  frm = HashMap<String,String>()
                        frm.put("nama_anggota",jsonObject.getString("nama_anggota"))
                        frm.put("status_anggota",jsonObject.getString("status_anggota"))
                        frm.put("kd_anggota",jsonObject.getString("kd_anggota"))
                        frm.put("img",jsonObject.getString("img"))

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
                val bundle = arguments
                when(mode){
                    "read_anggota" -> {
                        hm.put("mode","read_anggota")
                        hm.put("kd_keluarga", bundle?.get("kode").toString())
                        hm.put("nama_anggota", nama_anggota)
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(v.context)
        queue.add(request)
    }

    private fun getNama() {
        val request = object : StringRequest(
            Request.Method.POST,urlClass.urlAnggota,
            Response.Listener { response ->
                daftarNama.clear()
                val jsonArray = JSONArray(response)
                for (x in 0..(jsonArray.length()-1)){
                    val jsonObject = jsonArray.getJSONObject(x)
                    daftarNama.add(jsonObject.getString("nama_anggota"))
                }
                namaAdapter.notifyDataSetChanged()
            },
            Response.ErrorListener { error ->

            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("mode", "get_anggota")

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    class AdapterKeluarga(val dataKeluarga: List<HashMap<String,String>>) :
        RecyclerView.Adapter<AdapterKeluarga.HolderDataKeluarga>(){
        class HolderDataKeluarga(v : View) : RecyclerView.ViewHolder(v) {
            val ft = v.findViewById<CircleImageView>(R.id.adpFoto)
            val nm = v.findViewById<TextView>(R.id.adpNamaAnggota)
            val sts = v.findViewById<TextView>(R.id.adpStatusAnggota)
            val dt = v.findViewById<TextView>(R.id.adpDetail)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataKeluarga {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.row_keluarga, parent, false)
            return HolderDataKeluarga(v)
        }

        override fun getItemCount(): Int {
            return dataKeluarga.size
        }

        override fun onBindViewHolder(holder: HolderDataKeluarga, position: Int) {
            val data = dataKeluarga.get(position)
            holder.nm.setText(data.get("nama_anggota"))
            holder.sts.setText(data.get("status_anggota"))
            Picasso.get().load(data.get("img")).into(holder.ft)

            holder.dt.setOnClickListener {
                val intentDetail = Intent(it.context, DetailAnggotaActivity::class.java)
                intentDetail.putExtra("kdA",data.get("kd_anggota"))
                intentDetail.putExtra("nm",data.get("nama_anggota"))
                intentDetail.putExtra("img",data.get("img"))
                it.context.startActivity(intentDetail)
            }
        }
    }
}