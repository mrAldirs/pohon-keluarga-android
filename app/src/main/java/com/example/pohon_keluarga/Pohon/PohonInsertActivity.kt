package com.example.pohon_keluarga.Pohon

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.apk_pn.Helper.MediaHelper
import com.example.pohon_keluarga.R
import com.example.pohon_keluarga.UrlClass
import com.example.pohon_keluarga.databinding.ActivityPohonInsertBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.Locale

class PohonInsertActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPohonInsertBinding
    lateinit var urlClass: UrlClass

    lateinit var mediaHealper: MediaHelper
    var imStr = ""
    var jenkel = ""
    var statusAnggota = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPohonInsertBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setTitle("Tambah Anggota Keluarga")

        urlClass = UrlClass()
        mediaHealper = MediaHelper(this)

        val kode = intent?.getStringExtra("kode").toString()
        val status = intent?.getStringExtra("status").toString()
        if (status.equals("null")) {
            statusAnggota = "Kakek"
        } else if (status.equals("Kakek")) {
            statusAnggota = "Anak"
        } else if (status.equals("Anak")) {
            statusAnggota = "Cucu"
        } else if (status.equals("Cucu")) {
            statusAnggota = "Cicit"
        } else if (status.equals("Cicit")) {
            statusAnggota = "Canggah"
        } else if (status.equals("Canggah")) {
            statusAnggota = "Wareng"
        }
        binding.insStatusAnggota.setText(statusAnggota)

        binding.btnChoose.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent,mediaHealper.RcGallery())
        }

        binding.rgJenkel.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.rbLaki -> jenkel = "Laki-laki"
                R.id.rbPerempuan -> jenkel = "Perempuan"
            }
        }

        binding.btnSimpan.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Tambah Anggota")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda ingin menambah Anggota keluarga baru?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    insertAnggota()
                    val intent = Intent(this@PohonInsertActivity, PohonActivity::class.java)
                    intent.putExtra("kode", kode)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    recreate()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
            true
        }

        binding.btnBatalkan.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == mediaHealper.RcGallery()){
                imStr = mediaHealper.getBitmapToString(data!!.data,binding.insImage)
            }
        }
    }

    private fun insertAnggota() {
        val request = object : StringRequest(
            Method.POST, urlClass.urlAnggota,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    Toast.makeText(this, "Berhasil menambahkan anggota keluarga baru.", Toast.LENGTH_LONG)
                        .show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                val nodeId = intent.getStringExtra("nodeId")
                val nmFile ="IMG"+ SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(
                    Date()
                )+".jpg"

                hm.put("mode","insert")
                hm.put("kd_keluarga", intent.getStringExtra("kode").toString())
                hm.put("nama_anggota", binding.insNamaAnggota.text.toString())
                hm.put("jenkel", jenkel)
                hm.put("status_anggota", statusAnggota)
                hm.put("parent_id", nodeId.toString())
                hm.put("image",imStr)
                hm.put("file",nmFile)

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}
