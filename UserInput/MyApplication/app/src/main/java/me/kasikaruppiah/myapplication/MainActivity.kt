package me.kasikaruppiah.myapplication

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this)
        textView.text = "Wow!"
        textView.setTextColor(Color.GREEN)
        textView.textSize = 56F
        setContentView(textView)
    }
}
