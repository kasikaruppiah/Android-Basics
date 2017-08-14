package me.kasikaruppiah.miwok

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        numbers.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(p0!!.context, NumbersActivity::class.java)
                startActivity(intent)
            }
        })
        family.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(p0!!.context, FamilyActivity::class.java)
                startActivity(intent)
            }
        })
        colors.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(p0!!.context, ColorsActivity::class.java)
                startActivity(intent)
            }
        })
        phrases.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(p0!!.context, PhrasesActivity::class.java)
                startActivity(intent)
            }
        })
    }
}
