package com.example.pohon_keluarga

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
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
import com.example.pohon_keluarga.databinding.FragmentSilsilahInsertBinding
import org.json.JSONObject

class SilsilahInsertFragment : Fragment() {
    lateinit var thisParent: DashboardActivity
    lateinit var v: View
    private lateinit var b: FragmentSilsilahInsertBinding

    lateinit var urlClass: UrlClass
    lateinit var preferences: SharedPreferences

    val PREF_NAME = "akun"
    val NAMA_MC = "nama_mc"
    val DEF_NAMA_MC = ""

    var sts = "Umum"
    private var isChecked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as DashboardActivity
        b = FragmentSilsilahInsertBinding.inflate(layoutInflater)
        v = b.root

        val slide_end = AnimationUtils.loadAnimation(v.context, R.anim.translate_end)
        thisParent.b.frameLayout.startAnimation(slide_end)

        urlClass = UrlClass()
        preferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val fragment = fragmentManager?.findFragmentById(R.id.frameLayout)

        b.btnBatalkan.setOnClickListener {
            fragment?.let { it1 -> fragmentManager?.beginTransaction()?.remove(it1)?.commit()
            }
            thisParent.b.frameLayout.visibility = View.GONE
        }

        b.insStatus.setOnCheckedChangeListener { _, isChecked ->
            this.isChecked = isChecked
            if (isChecked) {
                sts = "Privat"
            }
        }

        b.btnSimpan.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setTitle("Informasi!")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah anda ingin menambahkan Silsilah Keluarga Anda?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    insertKeluarga("insert")
                    fragment?.let { it1 -> fragmentManager?.beginTransaction()?.remove(it1)?.commit()
                    }
                    thisParent.recreate()
                    thisParent.b.frameLayout.visibility = View.GONE
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
        }

        return v
    }

    private fun insertKeluarga(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlKeluarga,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    Toast.makeText(v.context, "Berhasil menambahkan Silsilah Keluarga!", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(v.context, "Gagal Menambahkan silsilah keluarga!", Toast.LENGTH_LONG)
                        .show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                when(mode){
                    "insert"->{
                        hm.put("mode","insert")
                        hm.put("nama_mc", preferences.getString(NAMA_MC, DEF_NAMA_MC).toString())
                        hm.put("nama_keluarga", b.insNamaKeluarga.text.toString())
                        hm.put("status_keluarga", sts)
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}