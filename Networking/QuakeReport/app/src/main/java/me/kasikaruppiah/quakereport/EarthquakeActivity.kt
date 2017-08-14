package me.kasikaruppiah.quakereport

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_earthquake.*


class EarthquakeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earthquake)

        val earthquakes = QueryUtils.extractEarthquakes()

        val adapter = EarthquakeAdapter(this, earthquakes)
        list.adapter = adapter

        list.setOnItemClickListener { adapterView, view, i, l ->
            kotlin.run {
                val webpage = Uri.parse(earthquakes.get(i).url)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(intent)
            }
        }
    }
}
