package com.example.pohon_keluarga

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.pohon_keluarga.databinding.ActivityQrBinding
import com.google.zxing.integration.android.IntentIntegrator

class QrActivity : AppCompatActivity() {
    private lateinit var b: ActivityQrBinding

    lateinit var intentIntegrator: IntentIntegrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityQrBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        intentIntegrator = IntentIntegrator(this)

        b.btnScanQR.setOnClickListener {
            intentIntegrator.setBeepEnabled(true).initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)

        if(intentResult!=null){
            if(intentResult.contents !=null) {
                val hasil = intentResult.contents

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(hasil)
                startActivity(intent)
            }
        }
    }
}