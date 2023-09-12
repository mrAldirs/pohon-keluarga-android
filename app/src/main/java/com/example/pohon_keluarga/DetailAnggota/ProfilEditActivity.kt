package com.example.pohon_keluarga.DetailAnggota

import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.apk_pn.Helper.MediaHelper
import com.example.pohon_keluarga.R
import com.example.pohon_keluarga.UrlClass
import com.example.pohon_keluarga.databinding.ActivityEditProfilBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.HashMap
import java.util.Locale

class ProfilEditActivity : AppCompatActivity() {
    private lateinit var b: ActivityEditProfilBinding
    lateinit var urlClass: UrlClass

    var kdA = ""

    val hdpSpinner = arrayOf("-- Status Hidup --","Masih Hidup","Meninggal")
    lateinit var adapterHdp : ArrayAdapter<String>

    val jkSpinner = arrayOf("-- Jenis Kelamin --", "Laki-laki", "Perempuan")
    lateinit var adapterJk : ArrayAdapter<String>

    lateinit var mediaHealper: MediaHelper
    var imStr = ""

    var jk = ""
    var kwn = ""
    var sts = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Edit Profil")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()
        mediaHealper = MediaHelper(this)

        var paket : Bundle? = intent.extras
        kdA = paket?.getString("kode").toString()

        adapterHdp = ArrayAdapter(this, android.R.layout.simple_list_item_1, hdpSpinner)
        b.spStatusKawin.adapter = adapterHdp

        adapterJk = ArrayAdapter(this, android.R.layout.simple_list_item_1, jkSpinner)
        b.spJenkel.adapter = adapterJk

        b.edtTglLahir.setOnClickListener {
            showDatePickerDialog()
        }

        b.btnChoose.setOnClickListener {
            b.edtImage.visibility = View.GONE
            b.updateImage.visibility = View.VISIBLE
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent,mediaHealper.RcGallery())
        }

        b.btnSimpan.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Edit Profil!")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda yakin ingin mengedit profil ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    editProfil("edit_profil")
                    Toast.makeText(this, "Berhasil mengedit profil!", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
            true
        }
    }

    override fun onStart() {
        super.onStart()
        detailProfil("detail_profil")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == mediaHealper.RcGallery()){
                imStr = mediaHealper.getBitmapToString(data!!.data,b.updateImage)
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val date = "$year-${month + 1}-$dayOfMonth"
                b.edtTglLahir.setText(date)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun detailProfil(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlProfil,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("nama_anggota")
                val st2 = jsonObject.getString("jenkel")
                val st4 = jsonObject.getString("tgl_lahir")
                val st5 = jsonObject.getString("status_hidup")
                val st6 = jsonObject.getString("img")
                val st7 = jsonObject.getString("kd_profil")
                val st8 = jsonObject.getString("kd_keluarga")

                b.edtNama.setText(st1)
                if (st4.equals("null")) {
                    b.edtTglLahir.setText("0000-00-00")
                } else {
                    b.edtTglLahir.setText(st4)
                }

                val spPositionJk = adapterJk.getPosition(st2)
                b.spJenkel.setSelection(spPositionJk)

                val spPositionHdp = adapterHdp.getPosition(st5)
                b.spStatusKawin.setSelection(spPositionHdp)

                Picasso.get().load(st6).into(b.edtImage)
            },
            Response.ErrorListener { error ->
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "detail_profil" -> {
                        hm.put("mode","detail_profil")
                        hm.put("kd_anggota", kdA)
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun editProfil(mode: String) {
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
                val nmFile ="IMG"+ SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(
                    Date()
                )+".jpg"
                when(mode){
                    "edit_profil"->{
                        hm.put("mode","edit_profil")
                        hm.put("kd_anggota", kdA)
                        hm.put("nama_anggota", b.edtNama.text.toString())
                        hm.put("jenkel", b.spJenkel.selectedItem.toString())
                        hm.put("tgl_lahir", b.edtTglLahir.text.toString())
                        hm.put("status_hidup", b.spStatusKawin.selectedItem.toString())
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