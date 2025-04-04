package com.RIDdev.antiphone

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.RIDdev.antiphone.Database.DBOperation
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
        val intent = Intent("android.settings.ACCESSIBILITY_SETTINGS")
        startActivity(intent)

    }
    fun Opt(V: View)
    {
        intent = Intent(this,Options::class.java)
        startActivity(intent)
    }
}