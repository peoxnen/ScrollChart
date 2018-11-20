package iview.wsienski.scrollchart

import android.content.res.Resources
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var startTouchY: Float = 0F
    private var startContentY: Float = 0F
    private var maxChartHeight: Int = 300.px
    private val minChartHeight: Int = 100.px

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        constraintLayout.setOnTouchListener { _, event ->
            val actualTouchY = event.y
            when {
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    startTouchY = actualTouchY
                    startContentY = content.y
                }
                event.actionMasked == MotionEvent.ACTION_MOVE -> {
                    val touchDifference = startTouchY - actualTouchY
                    val newContentY = startContentY - touchDifference

                    if (isChartMinimumHeightMode(newContentY) && hasReachedEndOfScreen(newContentY)) {
                        content.y = newContentY
                    } else if (isChartScalingMode(newContentY)) {
                        guideline.setGuidelineBegin(newContentY.toInt())
                        content.y = newContentY
                    }
                }
            }
            return@setOnTouchListener true
        }
    }

    private fun isChartMinimumHeightMode(newContentY: Float) = newContentY < minChartHeight

    private fun hasReachedEndOfScreen(newContentY: Float) =
            newContentY + content.height > constraintLayout.height

    private fun isChartScalingMode(newContentY: Float) =
            newContentY in minChartHeight..maxChartHeight

    val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}