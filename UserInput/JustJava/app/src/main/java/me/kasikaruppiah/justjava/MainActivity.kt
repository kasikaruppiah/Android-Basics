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
        displayQuantity(quantity)
    }

    fun increment(view: View) {
        quantity++
        displayQuantity(quantity)
    }

    fun decrement(view: View) {
        quantity--
        displayQuantity(quantity)
    }

    fun submitOrder(view: View) = displayMessage(createOrderSummary(calculatePrice()))

    private fun displayQuantity(quantity: Int) {
        quantity_text_view.text = "$quantity"
    }

    private fun displayMessage(orderSummary: String) {
        order_summary_text_view.text = orderSummary
    }

    private fun calculatePrice() = quantity * 5

    private fun createOrderSummary(price: Int) = "Name: Shifu" +
            "\nQuantity: $quantity" +
            "\nTotal: $$price" +
            "\nThank you!"
}
