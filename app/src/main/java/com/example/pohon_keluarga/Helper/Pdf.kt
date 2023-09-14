package com.example.pohon_keluarga.Helper

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pohon_keluarga.R
import com.example.pohon_keluarga.UrlClass
import com.example.pohon_keluarga.databinding.PdfBinding
import org.json.JSONArray

class Pdf : AppCompatActivity() {
    private lateinit var binding: PdfBinding
    private var urlClass: UrlClass = UrlClass()
    private val familyMembersList = mutableListOf<FamilyMember>()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PdfBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setTitle("Download PDF Silsilah")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val webView: WebView = findViewById(R.id.webView)

        val url = intent?.getStringExtra("url").toString()

        webView.loadUrl(url)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true

        webView.setOnLongClickListener { v ->
            val hitTestResult = webView.hitTestResult
            if (hitTestResult.type == WebView.HitTestResult.SRC_ANCHOR_TYPE) {
                AlertDialog.Builder(this)
                    .setTitle("Unduh PDF")
                    .setMessage("Anda ingin mengunduh PDF ini?")
                    .setPositiveButton("Unduh") { _, _ ->
                        val pdfUrl = hitTestResult.extra
                        pdfUrl?.let { requestDownloadPermission(it) }
                    }
                    .setNegativeButton("Batal", null)
                    .show()
                true
            } else false
        }
    }

    private fun requestDownloadPermission(url: String) {
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_EXTERNAL_STORAGE
            )
        } else {
            downloadPDF(url)
        }
    }

    private fun downloadPDF(pdfUrl: String) {
        val request = DownloadManager.Request(Uri.parse(pdfUrl))
            .setTitle("PDF Download")
            .setDescription("Downloading PDF")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "${System.currentTimeMillis()}.pdf"
            )
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)

        Toast.makeText(this, "Mengunduh PDF...", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUEST_WRITE_EXTERNAL_STORAGE = 1
    }
}