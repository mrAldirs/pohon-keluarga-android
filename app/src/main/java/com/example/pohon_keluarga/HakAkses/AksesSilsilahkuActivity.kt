package com.example.pohon_keluarga.HakAkses

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pohon_keluarga.Adapter.AdapterAksesSilsilahku
import com.example.pohon_keluarga.UrlClass
import com.example.pohon_keluarga.databinding.ActivityAksesSilsilahkuBinding
import org.json.JSONArray

class AksesSilsilahkuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAksesSilsilahkuBinding

    lateinit var preferences: SharedPreferences

    val PREF_NAME = "akun"
    val PENGGUNA = "kd_pengguna"
    val DEF_PENGGUNA = ""

    lateinit var urlClass: UrlClass
    val dataKeluarga = mutableListOf<HashMap<String,String>>()
    lateinit var keluargaAdapter : AdapterAksesSilsilahku

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAksesSilsilahkuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setTitle("Silsilah Keluarga Dibagikan")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()
        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        keluargaAdapter = AdapterAksesSilsilahku(dataKeluarga)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = keluargaAdapter

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        showDibagikan()
    }

    private fun showDibagikan() {
        val request = object : StringRequest(
            Method.POST,urlClass.urlAkses,
            Response.Listener { response ->
                dataKeluarga.clear()
                if (response.equals(0)) {
                    Toast.makeText(this,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var  frm = HashMap<String,String>()
                        frm.put("nama_keluarga",jsonObject.getString("nama_keluarga"))
                        frm.put("kd_keluarga",jsonObject.getString("kd_keluarga"))
                        frm.put("value",jsonObject.getString("value"))

                        dataKeluarga.add(frm)
                    }
                    keluargaAdapter.notifyDataSetChanged()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("mode","show_data_bagikan")
                hm.put("kd_pengguna", preferences.getString(PENGGUNA, DEF_PENGGUNA).toString())

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}