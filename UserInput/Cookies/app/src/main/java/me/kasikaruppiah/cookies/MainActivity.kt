package me.kasikaruppiah.cookies

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun eatCookie(view: View) {
        android_cookie_image_view.setImageResource(R.drawable.after_cookie)
        status_text_view.text = "I'm so full"
    }
}
