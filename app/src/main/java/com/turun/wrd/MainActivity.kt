package com.turun.wrd

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        scramble()
        setDimensions()
    }

    private fun init() {
        mGridView = findViewById<View>(R.id.grid) as GestureDetectGridView
        mGridView!!.numColumns = COLUMNS
        tileList = arrayOfNulls(DIMENSIONS)
        for (i in 0 until DIMENSIONS) {
            tileList[i] = i.toString()
        }
    }

    private fun scramble() {
        var index: Int
        var temp: String?
        val random = Random()
        for (i in tileList.size - 1 downTo 1) {
            index = random.nextInt(i + 1)
            temp = tileList[index]
            tileList[index] = tileList[i]
            tileList[i] = temp
        }
    }

    private fun setDimensions() {
        val vto = mGridView!!.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mGridView!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val displayWidth = mGridView!!.measuredWidth
                val displayHeight = mGridView!!.measuredHeight
                val statusbarHeight = getStatusBarHeight(applicationContext)
                val requiredHeight = displayHeight - statusbarHeight
                mColumnWidth = displayWidth / COLUMNS
                mColumnHeight = requiredHeight / COLUMNS
                display(applicationContext)
            }
        })
    }

    private fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier(
            "status_bar_height", "dimen",
            "android"
        )
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    companion object {
        private var mGridView: GestureDetectGridView? = null
        private const val COLUMNS = 3
        private const val DIMENSIONS = COLUMNS * COLUMNS
        private var mColumnWidth = 0
        private var mColumnHeight = 0
        const val up = "up"
        const val down = "down"
        const val left = "left"
        const val right = "right"
        private lateinit var tileList: Array<String?>
        private fun display(context: Context) {
            val buttons = ArrayList<Button>()
            var button: Button
            for (i in tileList.indices) {
                button = Button(context)
                if (tileList[i] == "0") button.setBackgroundResource(R.drawable.pigeon_piece8) else if (tileList[i] == "1") button.setBackgroundResource(
                    R.drawable.pigeon_piece2
                ) else if (tileList[i] == "2") button.setBackgroundResource(R.drawable.pigeon_piece3) else if (tileList[i] == "3") button.setBackgroundResource(
                    R.drawable.pigeon_piece4
                ) else if (tileList[i] == "4") button.setBackgroundResource(R.drawable.pigeon_piece5) else if (tileList[i] == "5") button.setBackgroundResource(
                    R.drawable.pigeon_piece6
                ) else if (tileList[i] == "6") button.setBackgroundResource(R.drawable.pigeon_piece7) else if (tileList[i] == "7") button.setBackgroundResource(
                    R.drawable.pigeon_piece8
                ) else if (tileList[i] == "8") button.setBackgroundResource(R.drawable.pigeon_piece9)
                buttons.add(button)
            }
            mGridView!!.adapter =
                CustomAdapter(buttons, mColumnWidth, mColumnHeight)
        }

        private fun swap(context: Context, currentPosition: Int, swap: Int) {
            val newPosition = tileList[currentPosition + swap]
            tileList[currentPosition + swap] = tileList[currentPosition]
            tileList[currentPosition] = newPosition
            display(context)
            if (isSolved) Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show()
        }

        fun moveTiles(context: Context, direction: String, position: Int) {

            // Upper-left-corner tile
            if (position == 0) {
                if (direction == right) swap(context, position, 1) else if (direction == down) swap(
                    context,
                    position,
                    COLUMNS
                ) else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show()

                // Upper-center tiles
            } else if (position > 0 && position < COLUMNS - 1) {
                if (direction == left) swap(context, position, -1) else if (direction == down) swap(
                    context,
                    position,
                    COLUMNS
                ) else if (direction == right) swap(context, position, 1) else Toast.makeText(
                    context,
                    "Invalid move",
                    Toast.LENGTH_SHORT
                ).show()

                // Upper-right-corner tile
            } else if (position == COLUMNS - 1) {
                if (direction == left) swap(context, position, -1) else if (direction == down) swap(
                    context,
                    position,
                    COLUMNS
                ) else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show()

                // Left-side tiles
            } else if (position > COLUMNS - 1 && position < DIMENSIONS - COLUMNS && position % COLUMNS == 0) {
                if (direction == up) swap(
                    context,
                    position,
                    -COLUMNS
                ) else if (direction == right) swap(
                    context,
                    position,
                    1
                ) else if (direction == down) swap(context, position, COLUMNS) else Toast.makeText(
                    context,
                    "Invalid move",
                    Toast.LENGTH_SHORT
                ).show()

                // Right-side AND bottom-right-corner tiles
            } else if (position == COLUMNS * 2 - 1 || position == COLUMNS * 3 - 1) {
                if (direction == up) swap(
                    context,
                    position,
                    -COLUMNS
                ) else if (direction == left) swap(
                    context,
                    position,
                    -1
                ) else if (direction == down) {

                    // Tolerates only the right-side tiles to swap downwards as opposed to the bottom-
                    // right-corner tile.
                    if (position <= DIMENSIONS - COLUMNS - 1) swap(
                        context, position,
                        COLUMNS
                    ) else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show()
                } else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show()

                // Bottom-left corner tile
            } else if (position == DIMENSIONS - COLUMNS) {
                if (direction == up) swap(
                    context,
                    position,
                    -COLUMNS
                ) else if (direction == right) swap(context, position, 1) else Toast.makeText(
                    context,
                    "Invalid move",
                    Toast.LENGTH_SHORT
                ).show()

                // Bottom-center tiles
            } else if (position < DIMENSIONS - 1 && position > DIMENSIONS - COLUMNS) {
                if (direction == up) swap(
                    context,
                    position,
                    -COLUMNS
                ) else if (direction == left) swap(
                    context,
                    position,
                    -1
                ) else if (direction == right) swap(context, position, 1) else Toast.makeText(
                    context,
                    "Invalid move",
                    Toast.LENGTH_SHORT
                ).show()

                // Center tiles
            } else {
                if (direction == up) swap(
                    context,
                    position,
                    -COLUMNS
                ) else if (direction == left) swap(
                    context,
                    position,
                    -1
                ) else if (direction == right) swap(context, position, 1) else swap(
                    context,
                    position,
                    COLUMNS
                )
            }
        }

        private val isSolved: Boolean
            private get() {
                var solved = false
                for (i in tileList.indices) {
                    if (tileList[i] == i.toString()) {
                        solved = true
                    } else {
                        solved = false
                        break
                    }
                }
                return solved
            }
    }
}