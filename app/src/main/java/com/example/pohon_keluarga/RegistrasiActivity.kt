package com.example.pohon_keluarga

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
import com.example.pohon_keluarga.databinding.ActivityRegistrasiBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.Locale

class RegistrasiActivity : AppCompatActivity() {
    private lateinit var b: ActivityRegistrasiBinding

    lateinit var urlClass: UrlClass
    lateinit var mediaHealper: MediaHelper
    var imStr = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityRegistrasiBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Registrasi Akun")

        mediaHealper = MediaHelper(this)
        urlClass = UrlClass()

        b.btnChoose.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent,mediaHealper.RcGallery())
        }

        b.btnDaftarkan.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Registrasi!")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda yakin ingin membuat Akun baru?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    if (b.regisPassword.text.toString() == b.regisKonfirm.text.toString()) {
                        if (imStr ==  "" || b.regisNama.text.toString().equals("") || b.regisNoHp.text.toString().equals("") ||
                            b.regisUsername.text.toString().equals("") || b.regisPassword.text.toString().equals("") || b.regisKonfirm.text.toString().equals("")) {
                            Toast.makeText(this, "Tolong isi data dengan benar!", Toast.LENGTH_SHORT).show()
                        } else {
                            registrasi("regis")
                        }
                    } else {
                        Toast.makeText(this, "Tolong konfirmasi Password dengan benar!", Toast.LENGTH_SHORT).show()
                    }
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == mediaHealper.RcGallery()){
                imStr = mediaHealper.getBitmapToString(data!!.data,b.insImage)
            }
        }
    }

    private fun registrasi(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.validasi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("0")) {
                    Toast.makeText(this, "Username telah digunakan!", Toast.LENGTH_SHORT).show()
                } else if (respon.equals("1")) {
                    Toast.makeText(this, "Nomor Handphone telah terdaftar!", Toast.LENGTH_SHORT).show()
                } else if (respon.equals("2")) {
                    Toast.makeText(this, "Nama Lengkap Anda telah terdaftar!", Toast.LENGTH_SHORT).show()
                } else if (respon.equals("3")) {
                    Toast.makeText(this, "Berhasil mendaftarkan Akun!", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                val nmFile ="IMG"+ SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(
                    Date()
                )+".jpg"
                when(mode){
                    "regis"->{
                        hm.put("mode","regis")
                        hm.put("username", b.regisUsername.text.toString())
                        hm.put("password", b.regisPassword.text.toString())
                        hm.put("nohp_mc", b.regisNoHp.text.toString())
                        hm.put("nama_mc", b.regisNama.text.toString())
                        hm.put("email_mc", b.regisEmail.text.toString())
                        hm.put("image",imStr)
                        hm.put("file",nmFile)
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }


}