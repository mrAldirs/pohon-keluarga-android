package com.example.pohon_keluarga.HakAkses

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pohon_keluarga.Adapter.AdapterAkses
import com.example.pohon_keluarga.R
import com.example.pohon_keluarga.UrlClass
import com.example.pohon_keluarga.databinding.ActivityAksesSilsilahBinding
import org.json.JSONArray
import org.json.JSONObject

class AksesSilsilahActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAksesSilsilahBinding

    lateinit var preferences: SharedPreferences

    val PREF_NAME = "akun"
    val PENGGUNA = "kd_pengguna"
    val DEF_PENGGUNA = ""

    lateinit var urlClass: UrlClass
    val dataKeluarga = mutableListOf<HashMap<String,String>>()
    lateinit var keluargaAdapter : AdapterAkses

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAksesSilsilahBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setTitle("Akses Silsilah Keluarga")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()
        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        keluargaAdapter = AdapterAkses(dataKeluarga, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = keluargaAdapter

        binding.btnSilsilah.setOnClickListener {
            startActivity(Intent(this@AksesSilsilahActivity, AksesSilsilahkuActivity::class.java))
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        showAkses()
    }

    fun dialog(kode: String, pengguna: String) {
        AlertDialog.Builder(this)
            .setTitle("Peringatan!")
            .setIcon(R.drawable.warning)
            .setMessage("Apakah Anda ingin menerima hak akses silsilah keluarga dari $pengguna?")
            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                accAkses(kode, "acc")
            })
            .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                accAkses(kode, "tolak")
            })
            .show()
    }

    private fun accAkses(kode: String, status: String) {
        val request = object : StringRequest(
            Method.POST, urlClass.urlAkses,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    Toast.makeText(this, "Anda menerima hak akses silsilah dari pengguna lain!", Toast.LENGTH_SHORT).show()
                    showAkses()
                } else {
                    Toast.makeText(this, "Anda menolak hak akses silsilah dari penguna lain!", Toast.LENGTH_SHORT).show()
                    showAkses()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()

                hm.put("mode","acc_akses")
                hm.put("kd_akses", kode)
                hm.put("status_akses", status)

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun delAkses(kode: String) {
        val request = object : StringRequest(
            Method.POST, urlClass.urlAkses,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    Toast.makeText(this, "Anda menghapus pemberian hak akses silsilah yang Anda tolak!", Toast.LENGTH_SHORT).show()
                    showAkses()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()

                hm.put("mode","hapus_akses")
                hm.put("kd_akses", kode)

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun showAkses() {
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
                        frm.put("kd_akses",jsonObject.getString("kd_akses"))
                        frm.put("kd_pengguna",jsonObject.getString("kd_pengguna"))
                        frm.put("username",jsonObject.getString("username"))
                        frm.put("kd_keluarga",jsonObject.getString("kd_keluarga"))
                        frm.put("nama_keluarga",jsonObject.getString("nama_keluarga"))
                        frm.put("tgl_akses",jsonObject.getString("tgl_akses"))
                        frm.put("status_akses",jsonObject.getString("status_akses"))
                        frm.put("img",jsonObject.getString("img"))

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
                hm.put("mode","show_akses_silsilah")
                hm.put("kd_pengguna", preferences.getString(PENGGUNA, DEF_PENGGUNA).toString())

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}