package com.example.pohon_keluarga

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.apk_pn.Helper.MediaHelper
import com.example.pohon_keluarga.databinding.FragmentAlbumInsertBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.Locale

class AlbumInsertFragment : Fragment() {
    lateinit var thisParent : AlbumKeluargaFragment
    lateinit var b: FragmentAlbumInsertBinding
    lateinit var v : View

    lateinit var urlClass: UrlClass

    lateinit var mediaHealper: MediaHelper
    var imStr = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = parentFragment as AlbumKeluargaFragment
        b = FragmentAlbumInsertBinding.inflate(layoutInflater)
        v = b.root

        val slide_end = AnimationUtils.loadAnimation(v.context, R.anim.translate_end)
        thisParent.b.frameLayout.startAnimation(slide_end)

        val fragment = fragmentManager?.findFragmentById(R.id.frameLayout)

        mediaHealper = MediaHelper(v.context)
        urlClass = UrlClass()

        val bundle = arguments
        val kd = bundle?.getString("kode").toString()

        b.btnBatalkan.setOnClickListener {
            fragment?.let { it1 -> fragmentManager?.beginTransaction()?.remove(it1)?.commit()
            }
            thisParent.b.frameLayout.visibility = View.GONE
        }

        b.btnChoose.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent,mediaHealper.RcGallery())
        }

        b.btnSimpan.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setTitle("Tambah Album")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda ingin menambah Album keluarga baru?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    insertAlbum("insert")
                    fragment?.let { it1 -> fragmentManager?.beginTransaction()?.remove(it1)?.commit()
                    }
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
            true
        }
        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == mediaHealper.RcGallery()){
                imStr = mediaHealper.getBitmapToString(data!!.data,b.insImg)
            }
        }
    }

    fun insertAlbum(mode : String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlAlbum,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                if (respon.equals("1")) {
                    Toast.makeText(v.context, "Berhasil menambahkan album keluarga baru.", Toast.LENGTH_LONG)
                        .show()
                    thisParent.showData("show_data_album")
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                val nmFile ="IMG"+ SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(
                    Date()
                )+".jpg"
                val bundle = arguments
                when(mode){
                    "insert"->{
                        hm.put("mode","insert")
                        hm.put("kd_keluarga", bundle?.getString("kode").toString())
                        hm.put("image",imStr)
                        hm.put("file",nmFile)
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}