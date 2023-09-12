package com.example.pohon_keluarga.Reuni

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pohon_keluarga.Adapter.AdapterReuniMasuk
import com.example.pohon_keluarga.R
import com.example.pohon_keluarga.UrlClass
import com.example.pohon_keluarga.databinding.ActivityReuniBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ReuniActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityReuniBinding

    lateinit var preferences: SharedPreferences

    val PREF_NAME = "akun"
    val PENGGUNA = "kd_pengguna"
    val DEF_PENGGUNA = ""

    lateinit var urlClass: UrlClass
    val dataList = mutableListOf<HashMap<String,String>>()
    lateinit var adapter : AdapterReuniMasuk

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReuniBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setTitle("Undangan Reuni Keluarga")
        supportActionBar?.setSubtitle("Undangan Masuk")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()
        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        adapter = AdapterReuniMasuk(dataList, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        showUndangan()
    }

    fun lihatUndanganBaru(kode:String) {
        val request = object : StringRequest(
            Method.POST, urlClass.urlReuni,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    intentDetail(kode)
                } else {
                    intentDetail(kode)
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()

                hm.put("mode","lihat_undangan_baru")
                hm.put("kd_reuni", kode)

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun intentDetail(kode: String) {
        val intent = Intent(this@ReuniActivity, ReuniDetailActivity::class.java)
        intent.putExtra("kode", kode)
        startActivity(intent)
    }

    private fun showUndangan() {
        val request = object : StringRequest(
            Method.POST,urlClass.urlReuni,
            Response.Listener { response ->
                dataList.clear()
                try {
                    val jsonArray = JSONArray(response)
                    if (jsonArray.length() == 0) {
                        binding.textView.visibility = View.VISIBLE
                    } else {
                        binding.textView.visibility = View.GONE
                        for (x in 0..(jsonArray.length()-1)){
                            val jsonObject = jsonArray.getJSONObject(x)
                            var  frm = HashMap<String,String>()
                            frm.put("kd_reuni",jsonObject.getString("kd_reuni"))
                            frm.put("nama_mc",jsonObject.getString("nama_mc"))
                            frm.put("tgl_reuni",jsonObject.getString("tgl_reuni"))
                            frm.put("pesan",jsonObject.getString("pesan"))
                            frm.put("status_reuni",jsonObject.getString("status_reuni"))
                            frm.put("img",jsonObject.getString("img"))

                            dataList.add(frm)
                        }
                        adapter.notifyDataSetChanged()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("mode","show_data_reuni")
                hm.put("kd_pengguna", preferences.getString(PENGGUNA, DEF_PENGGUNA).toString())

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.menu_masuk -> {
                supportActionBar?.setSubtitle("Undangan Masuk")
                binding.frameLayout.visibility = View.GONE
            }
            R.id.menu_terkirim -> {
                supportActionBar?.setSubtitle("Undangan Terkirim")
                var frag = ReuniTerkirimFragment()

                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, frag).commit()
                binding.frameLayout.setBackgroundColor(Color.argb(255,255,255,255))
                binding.frameLayout.visibility = View.VISIBLE
            }
        }
        return true
    }


}