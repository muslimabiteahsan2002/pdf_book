package com.muslima.mypdf

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.muslima.mypdf.MainActivity.Companion.BOOK
import com.muslima.mypdf.databinding.ActivityPdfviewerBinding
import java.io.InputStream

@Suppress("DEPRECATION")
class PDFViewActivity : AppCompatActivity(), MyOnlinePdf.Online {
    private lateinit var binding: ActivityPdfviewerBinding
    var pdfUrl: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfviewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pdfUrl = intent.getStringExtra(BOOK)
        if (isOnline){
            MyOnlinePdf(pdfUrl!!,this).execute(pdfUrl)
        }else{
        offlinePdf()}
    }

    private fun offlinePdf() {
        binding.pdfView.fromAsset(pdfUrl).enableSwipe(true).swipeHorizontal(false)
            .enableDoubletap(true).defaultPage(0).spacing(10).nightMode(false).pageSnap(true)
            .onLoad {
                binding.animationContainer.visibility = View.GONE
                binding.pdfViewContainer.visibility = View.VISIBLE
                binding.pdfView.visibility = View.VISIBLE
            }.load()
    }

    override fun online(result: InputStream?) {
        if (result != null) {
            binding.pdfView.fromStream(result)
                .onLoad { nbPages ->
                    binding.animationContainer.visibility = View.GONE
                    binding.pdfViewContainer.visibility = View.VISIBLE
                    binding.pdfView.visibility = View.VISIBLE
                }
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .spacing(10)
                .nightMode(false) // toggle night mode
                .load()
        } else {
            Toast.makeText(this, "Pdf Load Failed", Toast.LENGTH_SHORT).show()
        }

    }
    companion object {
        var isOnline = false
    }

}