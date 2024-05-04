package com.example.seabattle

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

data class Ship(val name: String, val size: Int)

class MainActivity : AppCompatActivity() {
    private lateinit var gridLayout: GridLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridLayout = findViewById(R.id.gridLayout)

        createGameBoard()

        placeShips(gridLayout)

        val rearrangeButton = findViewById<Button>(R.id.rearrangeButton)
        rearrangeButton.setOnClickListener {
            clearGameBoard()
            placeShips(gridLayout)
        }
    }

    private fun createGameBoard() {
        for (i in 0 until gridLayout.rowCount) {
            for (j in 0 until gridLayout.columnCount) {
                val cell = TextView(this)
                cell.width = 100
                cell.height = 100
                cell.text = "0"
                cell.setTextColor(Color.WHITE)
                cell.setBackgroundResource(R.drawable.cell_background)
                cell.setOnClickListener { onCellClick(it) }
                gridLayout.addView(cell)
            }
        }
    }

    private fun placeShips(gridLayout: GridLayout) {
        val fleet = mutableListOf(
            Ship("Линкор", 4),
            Ship("Эсминец", 3),
            Ship("Эсминец", 3),
            Ship("Крейсер", 2),
            Ship("Крейсер", 2),
            Ship("Крейсер", 2),
            Ship("Подводная лодка", 1),
            Ship("Подводная лодка", 1),
            Ship("Подводная лодка", 1),
            Ship("Подводная лодка", 1)
        )

        for (ship in fleet) {
            var isValidPlacement = false
            var attempts = 0

            while (!isValidPlacement && attempts < 100) {
                val randomRow = (0..9).random()
                val randomColumn = (0..9).random()
                val orientation = (0..1).random() == 0

                if (checkValidPlacement(gridLayout, randomRow, randomColumn, ship.size, orientation)) {
                    isValidPlacement = true
                    placeShipOnGrid(gridLayout, randomRow, randomColumn, ship.size, orientation)
                }
                attempts++
            }
        }
    }

    private fun checkValidPlacement(
        gridLayout: GridLayout,
        row: Int,
        column: Int,
        shipSize: Int,
        isHorizontal: Boolean
    ): Boolean {
        if (isHorizontal && column + shipSize > 10) return false
        if (!isHorizontal && row + shipSize > 10) return false

        for (i in row - 1..row + 1) {
            for (j in column - 1..column + shipSize) {
                if (i in 0..9 && j in 0..9) {
                    val cell = gridLayout.getChildAt(i * 10 + j) as TextView
                    if (cell.text == "1") return false
                }
            }
        }

        for (i in 0 until shipSize) {
            val checkRow = if (isHorizontal) row else row + i
            val checkColumn = if (isHorizontal) column + i else column

            val cell = gridLayout.getChildAt(checkRow * 10 + checkColumn) as TextView
            if (cell.text == "1") return false
        }

        return true
    }

    private fun placeShipOnGrid(
        gridLayout: GridLayout,
        row: Int,
        column: Int,
        shipSize: Int,
        isHorizontal: Boolean
    ) {
        val textColor = Color.WHITE
        val backgroundColor = Color.parseColor("#000000")

        for (i in 0 until shipSize) {
            val cellIndex = if (isHorizontal) row * 10 + column + i else (row + i) * 10 + column
            val cell = gridLayout.getChildAt(cellIndex) as TextView
            cell.text = "1"
            cell.setTextColor(textColor)
            cell.setBackgroundColor(backgroundColor)
        }
    }

    private fun clearGameBoard() {
        for (i in 0 until gridLayout.childCount) {
            val cell = gridLayout.getChildAt(i) as TextView
            cell.setBackgroundResource(R.drawable.cell_background)
            cell.text = "0"
        }
    }

    private fun onCellClick(view: View) {
        val cell = view as TextView
        if (cell.text == "0") {
            cell.text = "1"
        } else {
            cell.text = "0"
        }
    }
}
