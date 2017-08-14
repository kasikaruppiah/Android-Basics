package me.kasikaruppiah.quakereport

import android.app.Activity
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.text.DecimalFormat
import java.text.SimpleDateFormat

/**
 * Created by ledlab on 8/13/17.
 */
class EarthquakeAdapter(val context: Activity, val earthquakes: ArrayList<Earthquake>) : ArrayAdapter<Earthquake>(context, 0, earthquakes) {

    val decimalFormat = DecimalFormat("0.0")
    val locationRegex = " of "
    val dateFormat = SimpleDateFormat("LLL dd, yyyy")
    val timeFormat = SimpleDateFormat("h:mm a")

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var earthquakeListItemView = convertView
        if (convertView == null)
            earthquakeListItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false)

        val currentEarthquake = earthquakes.get(position)

        val magView = earthquakeListItemView!!.findViewById<TextView>(R.id.mag)
        magView.text = decimalFormat.format(currentEarthquake.mag)
        val backgroundCircle = magView.background as GradientDrawable
        backgroundCircle.setColor(ContextCompat.getColor(getContext(), getMagnitudeColor(currentEarthquake.mag)))

        val fullLocation = currentEarthquake.place
        val (location_offset, location) = fullLocation.split(locationRegex)
        earthquakeListItemView!!.findViewById<TextView>(R.id.location_offset).text = "$location_offset of"
        earthquakeListItemView!!.findViewById<TextView>(R.id.location).text = location

        val timeInMilliSeconds = currentEarthquake.time
        earthquakeListItemView!!.findViewById<TextView>(R.id.date).text = dateFormat.format(timeInMilliSeconds)
        earthquakeListItemView!!.findViewById<TextView>(R.id.time).text = timeFormat.format(timeInMilliSeconds)

        return earthquakeListItemView
    }

    fun getMagnitudeColor(mag: Double): Int {
        when (Math.floor(mag)) {
            0.0 -> return R.color.magnitude1
            1.0 -> return R.color.magnitude1
            2.0 -> return R.color.magnitude2
            3.0 -> return R.color.magnitude3
            4.0 -> return R.color.magnitude4
            5.0 -> return R.color.magnitude5
            6.0 -> return R.color.magnitude6
            7.0 -> return R.color.magnitude7
            8.0 -> return R.color.magnitude8
            9.0 -> return R.color.magnitude9
            else -> return R.color.magnitude10plus
        }
    }
}