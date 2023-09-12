package com.example.pohon_keluarga.DetailAnggota

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pohon_keluarga.UrlClass
import com.example.pohon_keluarga.databinding.FragmentDetailProfilBinding
import org.json.JSONObject
import java.util.HashMap

class ProfilDetailFragment : Fragment() {
    lateinit var b: FragmentDetailProfilBinding
    lateinit var v: View
    lateinit var thisParent: DetailAnggotaActivity
    lateinit var urlClass: UrlClass

    var nm_mc = ""
    var kdA = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as DetailAnggotaActivity
        b = FragmentDetailProfilBinding.inflate(layoutInflater)
        v = b.root

        urlClass = UrlClass()

        b.btnEdit.setOnClickListener {
            val intentDetail = Intent(it.context, ProfilEditActivity::class.java)
            intentDetail.putExtra("kode", kdA)
            it.context.startActivity(intentDetail)
        }

        return v
    }

    override fun onStart() {
        super.onStart()
        detailProfil("detail_profil")
    }

    private fun detailProfil(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlProfil,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("nama_anggota")
                val st2 = jsonObject.getString("jenkel")
                val st3 = jsonObject.getString("status_anggota")
                val st4 = jsonObject.getString("tgl_lahir")
                val st5 = jsonObject.getString("status_hidup")
                val st6 = jsonObject.getString("kd_anggota")
                val st7 = jsonObject.getString("kd_profil")
                val st8 = jsonObject.getString("kd_keluarga")
                val st9 = jsonObject.getString("nama_mc")

                kdA = st6
                b.profNama.setText(st1)
                b.profJenkel.setText(st2)
                b.profStatusAnggota.setText(st3)

                nm_mc = st9
                if (nm_mc == thisParent.nm_pengguna) {
                    b.btnEdit.visibility = View.VISIBLE
                } else {
                    b.btnEdit.visibility = View.GONE
                }

                if (!st4.equals("null")) {
                    b.profTglLahir.setText(st4)
                } else {
                    b.profTglLahir.setText("<default>")
                }

                if (!st5.equals("null")) {
                    b.profStatusHigup.setText(st5)
                } else {
                    b.profStatusHigup.setText("<default>")
                }
            },
            Response.ErrorListener { error ->
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "detail_profil" -> {
                        hm.put("mode","detail_profil")
                        hm.put("kd_anggota", thisParent.kdA)
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(thisParent)
        queue.add(request)
    }
}