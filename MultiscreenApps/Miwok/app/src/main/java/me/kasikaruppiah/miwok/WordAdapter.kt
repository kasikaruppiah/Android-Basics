package me.kasikaruppiah.miwok

import android.app.Activity
import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by ledlab on 8/11/17.
 */
class WordAdapter(val context: Activity, val words: ArrayList<Word>, val color: Int) : ArrayAdapter<Word>(context, 0, words) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var listItemView = convertView
        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false)
        listItemView!!.setBackgroundColor(ContextCompat.getColor(getContext(), color))
        val word = getItem(position)
        val imageView = listItemView!!.findViewById<ImageView>(R.id.image)
        if (word.imageResourceId != -1) {
            imageView.visibility = View.VISIBLE
            imageView.setImageResource(word.imageResourceId)
        } else {
            imageView.visibility = View.GONE
        }
        listItemView!!.findViewById<TextView>(R.id.miwok_text_view).text = word.miwokTranslation
        listItemView!!.findViewById<TextView>(R.id.default_text_view).text = word.defaultTranslation
        return listItemView!!
    }
}