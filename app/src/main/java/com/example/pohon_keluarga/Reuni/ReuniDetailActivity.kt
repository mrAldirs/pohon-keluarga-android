package com.example.pohon_keluarga.Reuni

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pohon_keluarga.UrlClass
import com.example.pohon_keluarga.databinding.ActivityReuniDetailBinding
import org.json.JSONObject

class ReuniDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReuniDetailBinding
    private val urlClass : UrlClass = UrlClass()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReuniDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setTitle("Detail Undangan Reuni")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        detail()
    }

    fun detail() {
        val request = object : StringRequest(
            Method.POST, urlClass.urlReuni,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("nama_mc")
                val st2 = jsonObject.getString("tempat")
                val st3 = jsonObject.getString("jam_reuni")
                val st4 = jsonObject.getString("tgl_reuni")
                val st5 = jsonObject.getString("pesan")

                binding.dtNamaPengirim.setText(st1)
                binding.dtTempat.setText(st2)
                binding.dtJam.setText(st3)
                binding.dtTanggal.setText(st4)
                binding.dtPesan.setText(st5)
            },
            Response.ErrorListener { error ->
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                val kode = intent.getStringExtra("kode").toString()
                hm.put("mode", "detail")
                hm.put("kd_reuni", kode)

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}