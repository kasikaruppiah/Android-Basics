package me.kasikaruppiah.practiceset2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        display1("This is Box 1.")
        display2("And this is Box 2.")
        display3("And look! Box 3!")
    }

    fun display(string: String) {
        display_text_view.text = string
    }

    fun display(int: Int) {
        display_text_view.text = int.toString()
    }

    fun display1(string: String) {
        display(string)
    }

    fun display2(string: String) {
        display_text_view_2.text = string
    }

    fun display3(string: String) {
        display_text_view_3.text = string
    }
}
