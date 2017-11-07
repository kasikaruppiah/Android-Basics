package edu.northeastern.ledlab.dotmemorytask

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_dot_memory_configuration.*

class DotMemoryConfiguration : AppCompatActivity() {
    var pidVal = ""
    var assessmentVal = 20
    var setsizeVal = 3
    var practicetrialsVal = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dot_memory_configuration)
        updateBegin()

        pid.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                //TODO("not implemented") //To change body of created functions use File |
                // Settings | File Templates.
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("not implemented") //To change body of created functions use File |
                // Settings | File Templates.
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                pidVal = p0.toString().trim()
                updateBegin()
            }
        })

        assessment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                //TODO("not implemented") //To change body of created functions use File |
                // Settings | File Templates.
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("not implemented") //To change body of created functions use File |
                // Settings | File Templates.
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                assessmentVal = if (p0.toString().isNotEmpty())
                    p0.toString().toInt()
                else
                    0
                updateBegin()
            }
        })

        setsize.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                //TODO("not implemented") //To change body of created functions use File |
                // Settings | File Templates.
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("not implemented") //To change body of created functions use File |
                // Settings | File Templates.
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setsizeVal = if (p0.toString().isNotEmpty())
                    p0.toString().toInt()
                else
                    0
                updateBegin()
            }
        })

        practicetrials.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                //TODO("not implemented") //To change body of created functions use File |
                // Settings | File Templates.
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("not implemented") //To change body of created functions use File |
                // Settings | File Templates.
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                practicetrialsVal = if (p0.toString().isNotEmpty())
                    p0.toString().toInt()
                else
                    0
                updateBegin()
            }
        })

        begin.setOnClickListener {
            begin.isEnabled = false
            val intent = Intent(this, DotMemoryTask::class.java)
            intent.putExtra("pid", pidVal)
            intent.putExtra("assessment", assessmentVal)
            intent.putExtra("setsize", setsizeVal)
            intent.putExtra("practicetrials", practicetrialsVal)
            startActivity(intent)
            finish()
        }
    }

    private fun updateBegin() {
        begin.isEnabled = !pidVal.isEmpty() and (assessmentVal > 0) and (setsizeVal > 0)
    }
}