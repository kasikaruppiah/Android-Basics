package me.kasikaruppiah.menu

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun printToLogs(view: View) {
        Log.i("Menu Item 1", "${menu_item_1.text.toString()}")
        Log.i("Menu Item 2", "${menu_item_2.text.toString()}")
        Log.i("Menu Item 3", "${menu_item_3.text.toString()}")
    }
}
