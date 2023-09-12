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
import com.example.pohon_keluarga.databinding.FragmentDetailKontakBinding
import org.json.JSONObject
import java.util.HashMap

class KontakDetailFragment : Fragment() {
    lateinit var b: FragmentDetailKontakBinding
    lateinit var v : View
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
        b = FragmentDetailKontakBinding.inflate(layoutInflater)
        v = b.root

        urlClass = UrlClass()
        b.btnEdit.setOnClickListener {
            val intentDetail = Intent(it.context, KontakEditActivity::class.java)
            intentDetail.putExtra("kode", kdA)
            it.context.startActivity(intentDetail)
        }

        return v
    }

    override fun onStart() {
        super.onStart()
        detailProfil("detail_kontak")
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

                kdA = st6
                nm_mc = st9
                if (nm_mc == thisParent.nm_pengguna) {
                    b.btnEdit.visibility = View.VISIBLE
                } else {
                    b.btnEdit.visibility = View.GONE
                }

                if (!st1.equals("null")) {
                    b.kontakEmail.setText(st1)
                } else {
                    b.kontakEmail.setText("<default>")
                }

                if (!st2.equals("null")) {
                    b.kotakNoHp.setText(st2)
                } else {
                    b.kotakNoHp.setText("<default>")
                }

                if (!st3.equals("null")) {
                    b.kontakAlamat.setText(st3)
                } else {
                    b.kontakAlamat.setText("<default>")
                }
            },
            Response.ErrorListener { error ->
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "detail_kontak" -> {
                        hm.put("mode","detail_kontak")
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