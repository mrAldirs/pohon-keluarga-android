package com.example.pohon_keluarga.DetailAnggota

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pohon_keluarga.R
import com.example.pohon_keluarga.UrlClass
import com.example.pohon_keluarga.databinding.ActivityEditLainnyaBinding
import org.json.JSONObject
import java.util.HashMap

class KontakEditActivity : AppCompatActivity() {
    private lateinit var b: ActivityEditLainnyaBinding
    lateinit var urlClass: UrlClass

    var kdA = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityEditLainnyaBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Edit Kontak")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        urlClass = UrlClass()

        var paket : Bundle? = intent.extras
        kdA = paket?.getString("kode").toString()

        b.btnSimpan.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Edit Kontak!")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda yakin ingin mengedit kontak ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    editKontak("edit_kontak")
                    Toast.makeText(this, "Berhasil mengedit kontak!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, DetailAnggotaActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        detailProfil("detail_kontak")
        b.linearLainnya.visibility = View.GONE
    }

    private fun detailProfil(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlProfil,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("email")
                val st2 = jsonObject.getString("nohp_kontak")
                val st3 = jsonObject.getString("alamat")
                val st6 = jsonObject.getString("kd_anggota")
                val st9 = jsonObject.getString("nama_mc")

                if (st1.equals("null")) {
                    b.edtEmail.setText("")
                } else {
                    b.edtEmail.setText(st1)
                }

                if (st2.equals("null")) {
                    b.edtNoHp.setText("")
                } else {
                    b.edtNoHp.setText(st2)
                }

                if (st3.equals("null")) {
                    b.edtAlamat.setText("")
                } else {
                    b.edtAlamat.setText(st3)
                }
            },
            Response.ErrorListener { error ->
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "detail_kontak" -> {
                        hm.put("mode","detail_kontak")
                        hm.put("kd_anggota", kdA)
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun editKontak(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlProfil,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                when(mode){
                    "edit_kontak"->{
                        hm.put("mode","edit_kontak")
                        hm.put("kd_anggota", kdA)
                        hm.put("email", b.edtEmail.text.toString())
                        hm.put("nohp_kontak", b.edtNoHp.text.toString())
                        hm.put("alamat", b.edtAlamat.text.toString())
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}