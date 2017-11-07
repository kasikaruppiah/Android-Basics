package edu.northeastern.ledlab.dotmemorytask

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_dot_memory_task.*
import java.util.*
import kotlin.collections.ArrayList


class DotMemoryTask : AppCompatActivity() {
    private val gridSize = 6

    private val buttonTags = ArrayList<String>()
    private var trialValidTags = ArrayList<String>()
    private var trialUserTags = ArrayList<String>()
    private val assessmentSizes = ArrayList<Int>()

    private val handler = Handler()
    private var pid = ""
    private var currentAssessment = 1
    private var totalAssessment = 20
    private var currentTrial = 0
    private var totalTrial = 2
    private var trialScore = 0
    private var trialSpeed = 1
    private var currentSetSize = 3
    private var totalSetSize = gridSize * gridSize
    private var currentPracticeTrial = 0
    private var totalPracticeTrials = 0
    private var nby2 = 0
    private var meanSetSize = 0.0

    private var countDownTimer: CountDownTimer? = null
    private var countDownTimerPaused = false
    private var gameInProgress = false
    private var validatingTrial = false
    private var breakSegment = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dot_memory_task)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        val dotIntent = intent
        pid = dotIntent.getStringExtra("pid")
        totalAssessment = dotIntent.getIntExtra("assessment", 20)
        nby2 = totalAssessment / 2
        currentSetSize = dotIntent.getIntExtra("setsize", 3)
        totalPracticeTrials = dotIntent.getIntExtra("practicetrials", 0)

        createGrid()
    }

    override fun onBackPressed() {
        pause()
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setCancelable(false)
        alertDialog.setMessage(getString(R.string.back_pressed))
        alertDialog.setPositiveButton(getString(R.string.ok)) { p0, p1 ->
            if (countDownTimer != null)
                countDownTimer!!.cancel()

            val intent = Intent(this, DotMemoryConfiguration::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }
        alertDialog.setNegativeButton(getString(R.string.cancel)) { p0, p1 -> resume() }
        alertDialog.show()
    }

    private fun pause() {
        if (gameInProgress) {
            handler.removeCallbacksAndMessages(null)
            if (countDownTimer != null) {
                countDownTimerPaused = true
                countDownTimer!!.cancel()
                countDownTimer = null
            }
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun onPause() {
        super.onPause()
        pause()
    }

    private fun resume() {
        if (gameInProgress) {
            if (!breakSegment) {
                relative_layout.findViewWithTag<ImageView>("gridmask").visibility = View.INVISIBLE
                val button = relative_layout.findViewWithTag<Button>("ddisplay")
                button.text = getString(R.string.resume)
                button.isClickable = true
                button.setOnClickListener { v ->
                    v.visibility = View.INVISIBLE
                    if (countDownTimerPaused) {
                        countDownTimerPaused = false
                        val dtimer = relative_layout.findViewWithTag<ProgressBar>("dtimer")
                        countDownTimer = Timer((dtimer.max - dtimer.progress).toLong(), 250)
                        countDownTimer!!.start()
                    } else {
                        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        if (validatingTrial) {
                            trialValidTags
                                    .asSequence()
                                    .map { relative_layout.findViewWithTag<Button>(it) }
                                    .forEach { it.setBackgroundColor(Color.WHITE) }
                            handler.postDelayed(this::validateTrial, 100)
                        } else {
                            trialValidTags
                                    .asSequence()
                                    .map { relative_layout.findViewWithTag<Button>(it) }
                                    .forEach { it.setBackgroundColor(Color.WHITE) }
                            if (currentAssessment == 1 && currentTrial == 0 && currentPracticeTrial <= totalPracticeTrials)
                                currentPracticeTrial -= 1
                            else if (currentTrial > 0)
                                currentTrial -= 1
                            handler.postDelayed(this::newTrial, 1000)
                        }
                    }
                }
                button.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        resume()
    }

    private fun getScreenSize(): Int {
        val configuration = resources.configuration
        val width = configuration.screenWidthDp
        val height = configuration.screenHeightDp

        return Math.min(Math.min(Math.min(height, width), 320) + 32, height)
    }

    private fun createGrid() {
        val scale = resources.displayMetrics.density
        val size = getScreenSize()
        val gameSize = (size * scale + 0.5f).toInt()
        val gridsSize = ((size - 32) * scale + 0.5f).toInt()
        val padding = (4 * scale + 0.5f).toInt()
        val progressPadding = padding * 2

        val outerLinearLayout = LinearLayout(this)
        val params = RelativeLayout.LayoutParams(gridsSize, gameSize)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        outerLinearLayout.orientation = LinearLayout.VERTICAL
        outerLinearLayout.layoutParams = params

        val gridLinearLayout = LinearLayout(this)
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> gridLinearLayout.background = resources.getDrawable(R.drawable.border, theme)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN -> gridLinearLayout.background = resources.getDrawable(R.drawable.border)
            else -> gridLinearLayout.setBackgroundDrawable(resources.getDrawable(R.drawable.border))
        }

        gridLinearLayout.orientation = LinearLayout.VERTICAL
        gridLinearLayout.weightSum = gridSize.toFloat()
        gridLinearLayout.setPadding(padding, padding, padding, padding)

        val gridParams = LinearLayout.LayoutParams(gridsSize, gridsSize)
        gridParams.gravity = Gravity.CENTER_HORIZONTAL
        gridLinearLayout.layoutParams = gridParams

        for (i in 0 until gridSize) {
            val innerLinearLayout = LinearLayout(this)
            innerLinearLayout.orientation = LinearLayout.HORIZONTAL
            innerLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1F)
            innerLinearLayout.weightSum = gridSize.toFloat()
            for (j in 0 until gridSize) {
                val button = Button(this, null, R.style.Widget_AppCompat_Button_Borderless)
                val tag = "grid_button_$i$j"
                buttonTags.add(tag)
                button.tag = tag
                button.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1F)
                button.setBackgroundColor(Color.WHITE)
                button.setOnClickListener { v ->
                    trialUserTags.add(v.tag.toString())
                    v.setBackgroundColor(Color.BLACK)
                    window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    if (trialUserTags.size == currentSetSize) {
                        countDownTimer!!.cancel()
                        countDownTimer = null
                        validatingTrial = true
                        handler.postDelayed({
                            v.setBackgroundColor(Color.WHITE)
                            validateTrial()
                        }, 100)
                    } else
                        handler.postDelayed({
                            v.setBackgroundColor(Color.WHITE)
                            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        }, 100)
                }
                innerLinearLayout.addView(button)
            }
            gridLinearLayout.addView(innerLinearLayout)
        }
        outerLinearLayout.addView(gridLinearLayout)

        val progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal)
        progressBar.tag = "dtimer"
        progressBar.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        progressBar.setPadding(progressPadding, progressPadding, progressPadding, progressPadding)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            progressBar.progressTintList = ColorStateList.valueOf(Color.BLACK)
        else
            progressBar.progressDrawable.setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.SRC_IN)
        outerLinearLayout.addView(progressBar)

        relative_layout.addView(outerLinearLayout)

        val button = Button(this)
        button.tag = "ddisplay"
        button.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        button.setBackgroundColor(Color.WHITE)
        button.text = getString(R.string.ready)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            button.setTextAppearance(android.R.style.TextAppearance_Large)
        else
            button.setTextAppearance(this, android.R.style.TextAppearance_Large)
        button.layoutParams = params
        button.setOnClickListener { v ->
            gameInProgress = true
            v.visibility = View.INVISIBLE
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            handler.postDelayed(this::newTrial, 1000)
        }
        relative_layout.addView(button)

        val maskImage = ImageView(this)
        maskImage.tag = "gridmask"
        maskImage.layoutParams = params
        maskImage.scaleType = ImageView.ScaleType.FIT_XY
        maskImage.setImageResource(R.drawable.black_and_white_grainy_dot)
        maskImage.visibility = View.INVISIBLE
        relative_layout.addView(maskImage)
    }

    private fun checkAdjacent(previous: String, current: String): Boolean {
        val previousGrid = previous.takeLast(2).toInt()
        val currentGrid = current.takeLast(2).toInt()

        return Math.abs(currentGrid - previousGrid) in listOf(1, 9, 10, 11)
    }

    private fun getNButtons(n: Int): List<String> {
        Collections.shuffle(buttonTags)
        val randomTags = ArrayList<String>()
        val random = Random()
        val size = buttonTags.size
        var index = 0
        for (i in 0 until n) {
            index = random.nextInt(size)
            if (i == 1)
                while (buttonTags[index] == randomTags[i - 1] || checkAdjacent(randomTags[i - 1], buttonTags[index]))
                    index = random.nextInt(size)
            else if (i > 1)
                while (buttonTags[index] == randomTags[i - 1] || buttonTags[index] == randomTags[i - 2] || checkAdjacent(randomTags[i - 1], buttonTags[index]))
                    index = random.nextInt(size)
            randomTags.add(buttonTags[index])
        }
        return randomTags
    }

    private fun newTrial() {
        relative_layout.findViewWithTag<ProgressBar>("dtimer").progress = 0
        if (currentTrial == totalTrial) {
            assessmentSizes.add(currentSetSize)

            if (trialScore > currentSetSize)
                currentSetSize += 1
            else if (trialScore < currentSetSize)
                currentSetSize -= 1
            if (currentSetSize > totalSetSize)
                currentSetSize = totalSetSize
            else if (currentSetSize < 1)
                currentSetSize = 1

            trialScore = 0
            currentTrial = 0
            currentAssessment += 1
        }
        val button = relative_layout.findViewWithTag<Button>("ddisplay")
        if (currentPracticeTrial < totalPracticeTrials || currentAssessment <= totalAssessment) {
            trialUserTags = ArrayList()
            if (currentPracticeTrial < totalPracticeTrials) {
                trialSpeed = totalPracticeTrials - currentPracticeTrial
                currentPracticeTrial += 1
                button.text = String.format(getString(R.string.practice_trial), currentPracticeTrial)
            } else {
                currentTrial += 1
                button.text = if (currentTrial == 1 && currentAssessment == 1) getString(R.string.start_game) else getString(R.string.new_trial)
            }
            trialValidTags = ArrayList(getNButtons(currentSetSize))
            button.isClickable = false
            button.visibility = View.VISIBLE
            handler.postDelayed({
                button.visibility = View.INVISIBLE
                button.isClickable = true
                displayTrialGrid()
            }, 1000)
        } else {
            gameInProgress = false
            meanSetSize = assessmentSizes.subList(nby2, currentAssessment - 1).sum() / (currentAssessment - 1 - nby2).toDouble()
            button.text = String.format(getString(R.string.mean_set_size), pid, meanSetSize)
            button.setOnClickListener(null)
            button.setOnLongClickListener {
                val intent = Intent(this, DotMemoryConfiguration::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
                true
            }
            button.visibility = View.VISIBLE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    private fun displayTrialGrid() {
        var timer = 500L

        for (tag in trialValidTags) {
            val btn = relative_layout.findViewWithTag<Button>(tag)
            handler.postDelayed({
                btn.setBackgroundColor(Color.BLACK)
            }, timer)
            timer += 1500 * trialSpeed
            handler.postDelayed({
                btn.setBackgroundColor(Color.WHITE)
            }, timer)
            timer += 150 * trialSpeed
        }
        handler.postDelayed({
            relative_layout.findViewWithTag<ImageView>("gridmask").visibility = View.VISIBLE
        }, timer)
        timer += 1000
        handler.postDelayed({
            relative_layout.findViewWithTag<ImageView>("gridmask").visibility = View.INVISIBLE
            val dtimer = relative_layout.findViewWithTag<ProgressBar>("dtimer")
            dtimer.max = currentSetSize * 1750 * 2 * trialSpeed
            countDownTimer = Timer(dtimer.max.toLong(), 250)
            countDownTimer!!.start()
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }, timer)
    }

    private fun validateTrial() {
        var timer = 0L
        val interval = (2000 / currentSetSize).toLong()
        for (i in 0 until currentSetSize) {
            val btn = relative_layout.findViewWithTag<Button>(trialValidTags[i])
            var drawable = R.drawable.ic_close_black_24dp
            if (i < trialUserTags.size)
                if (trialUserTags[i] == trialValidTags[i]) {
                    trialScore += 1
                    drawable = R.drawable.ic_check_black_24dp
                }
            handler.postDelayed({
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> btn.background = resources.getDrawable(drawable, theme)
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN -> btn.background = resources.getDrawable(drawable)
                    else -> btn.setBackgroundDrawable(resources.getDrawable(drawable))
                }
            }, timer)
            timer += interval
            handler.postDelayed({
                btn.setBackgroundColor(Color.WHITE)
            }, timer)
            timer += 100
        }
        handler.postDelayed({
            validatingTrial = false
            if (currentTrial == 2 && currentAssessment == totalAssessment / 2) {
                breakSegment = true
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                val button = relative_layout.findViewWithTag<Button>("ddisplay")
                button.text = getString(R.string.break_segment)
                button.isClickable = true
                button.setOnClickListener { v ->
                    breakSegment = false
                    v.visibility = View.INVISIBLE
                    window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    handler.postDelayed(this::newTrial, 1000)
                }
                button.visibility = View.VISIBLE
            } else
                newTrial()
        }, timer)
    }

    inner class Timer(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
            val dtimer = relative_layout.findViewWithTag<ProgressBar>("dtimer")
            dtimer.progress = (dtimer.max - millisUntilFinished).toInt()
        }

        override fun onFinish() {
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            val dtimer = relative_layout.findViewWithTag<ProgressBar>("dtimer")
            dtimer.progress = dtimer.max
            handler.postDelayed({
                validatingTrial = true
                validateTrial()
            }, 100)
            countDownTimer = null
        }
    }
}