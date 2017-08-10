package me.kasikaruppiah.justjava

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var quantity = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        display(quantity)
        displayMessage("Total: $0")
    }

    fun increment(view: View) {
        quantity++
        display(quantity)
    }

    fun decrement(view: View) {
        quantity--
        display(quantity)
    }

    fun submitOrder(view: View) {
        val price = quantity * 5
        val message = "Total: $$price\nThank you!"
        displayMessage(message)
    }

    private fun display(number: Int) {
        quantity_text_view.text = "$number"
    }

    private fun displayMessage(string: String) {
        price_text_view.text = string
    }
}
