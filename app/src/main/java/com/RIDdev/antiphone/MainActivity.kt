package com.RIDdev.antiphone

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.RIDdev.antiphone.background.Constant

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        startService(Intent(this, Constant::class.java))
        val progressTotal: ProgressBar = findViewById(R.id.progressTotal)
        val tvTotalUsage: TextView = findViewById(R.id.tvTotalUsage)

        val Seconds = Constant.total
        val AllMinutes = Seconds / 60
        progressTotal.max = 1440
        progressTotal.progress = AllMinutes

        val hours = AllMinutes / 60
        val minutes = AllMinutes % 60
        tvTotalUsage.text = "Total Screen Time: $hours h $minutes m"
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)

    }
    fun Appblock(V: View)
    {
        intent = Intent(this,AppBlock::class.java)
        startActivity(intent)
    }
}