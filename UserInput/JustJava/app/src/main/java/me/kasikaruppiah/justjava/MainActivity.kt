package me.kasikaruppiah.justjava

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.text.NumberFormat


class MainActivity : AppCompatActivity() {

    var quantity = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        displayQuantity(quantity)
    }

    fun increment(view: View) {
        quantity++
        if (quantity == 100) {
            increment_button.isEnabled = false
            Toast.makeText(this, resources.getString(R.string.max_order), Toast.LENGTH_SHORT).show()
        }
        if (!decrement_button.isEnabled) decrement_button.isEnabled = true
        displayQuantity(quantity)
    }

    fun decrement(view: View) {
        quantity--
        if (quantity == 1) {
            decrement_button.isEnabled = false
            Toast.makeText(this, resources.getString(R.string.min_order), Toast.LENGTH_SHORT).show()
        }
        if (!increment_button.isEnabled) increment_button.isEnabled = true
        displayQuantity(quantity)
    }

    fun submitOrder(view: View) {
        val name = name_edit_text.text.toString()
        if (name != "") {
            val hasWhippedCream = whipped_cream_check_box.isChecked
            val hasChocolate = chocolate_check_box.isChecked
            val price = calculatePrice(hasWhippedCream, hasChocolate)
            val message = createOrderSummary(name, hasWhippedCream, hasChocolate, price)
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.type = "text/plain"
            intent.setData(Uri.parse("mailto:"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "${this.resources.getString(R.string.app_name)} order for $name")
            intent.putExtra(Intent.EXTRA_TEXT, message)
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        } else {
            Toast.makeText(this, resources.getString(R.string.name_required), Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayQuantity(quantity: Int) {
        quantity_text_view.text = "$quantity"
    }

    private fun calculatePrice(hasWhippedCream: Boolean, hasChocolate: Boolean): Int {
        var basePrice = 5
        if (hasWhippedCream) basePrice += 1
        if (hasChocolate) basePrice += 2
        return quantity * basePrice
    }

    private fun createOrderSummary(name: String, hasWhippedCream: Boolean, hasChocolate: Boolean, price: Int) =
            resources.getString(R.string.name_mail, name) +
            resources.getString(R.string.whipped_cream_mail, hasWhippedCream) +
            resources.getString(R.string.chocolate_mail, hasChocolate) +
            resources.getString(R.string.price_mail, NumberFormat.getCurrencyInstance().format(price`)) +
            resources.getString(R.string.thankyou)
}
