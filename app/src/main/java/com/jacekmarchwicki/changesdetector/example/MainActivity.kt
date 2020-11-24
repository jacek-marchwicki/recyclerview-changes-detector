package com.jacekmarchwicki.changesdetector.example

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.main_kotlin_example_btn).setOnClickListener {
            startActivity(Intent(this, KotlinExampleActivity::class.java))
        }

        findViewById<View>(R.id.main_detector_example_btn).setOnClickListener {
            startActivity(Intent(this, ChangesDetectorActivity::class.java))
        }
    }
}