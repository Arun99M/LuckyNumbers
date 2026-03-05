package com.example.kotlinapps

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivityrandomluckynumber : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_activityrandomluckynumber)

        val luckyText: TextView = findViewById(R.id.txtlucynumber)
        val shareBtn: Button = findViewById(R.id.btn1)
        val countView: TextView = findViewById(R.id.txtCount)
        val dynamicTable: TableLayout = findViewById(R.id.dynamicTable)

        val userName = receiveUserName()
        val randomNumber = Random.nextInt(1000)
        luckyText.text = randomNumber.toString()

        // 1. Check for duplicates and save only if it's a new name
        if (!isDuplicateName(userName)) {
            saveHistory(userName, randomNumber)
        } else {
            Toast.makeText(this, "Name already exists in history", Toast.LENGTH_SHORT).show()
        }

        // 2. Refresh UI data
        val entries = getHistoryList()
        countView.text = "Total Numbers Generated: ${entries.size}"
        displayHistoryInTable(dynamicTable, entries)

        shareBtn.setOnClickListener {
            shareResult(userName, randomNumber)
        }
    }

    private fun receiveUserName(): String = intent.getStringExtra("name") ?: "Guest"

    private fun isDuplicateName(name: String): Boolean {
        val sharedPref = getSharedPreferences("LuckyAppPrefs", Context.MODE_PRIVATE)
        val history = sharedPref.getString("history", "") ?: ""
        // Checks if the exact name entry exists
        return history.contains("$name:")
    }

    private fun saveHistory(name: String, number: Int) {
        val sharedPref = getSharedPreferences("LuckyAppPrefs", Context.MODE_PRIVATE)
        val existingHistory = sharedPref.getString("history", "")
        val newEntry = "$name: $number\n$existingHistory"
        sharedPref.edit().putString("history", newEntry).apply()
    }

    private fun getHistoryList(): List<String> {
        val sharedPref = getSharedPreferences("LuckyAppPrefs", Context.MODE_PRIVATE)
        val history = sharedPref.getString("history", "") ?: ""
        return history.split("\n").filter { it.isNotBlank() }
    }

    private fun displayHistoryInTable(table: TableLayout, entries: List<String>) {
        val totalEntries = entries.size
        table.removeAllViews() // Clear to prevent double-rendering

        entries.forEachIndexed { index, entry ->
            val tableRow = TableRow(this)
            tableRow.setPadding(0, 15, 0, 15)

            val parts = entry.split(": ")
            if (parts.size == 2) {
                val snoTv = createTableCell((totalEntries - index).toString(), 0.5f)
                val nameTv = createTableCell(parts[0], 1f)
                val numberTv = createTableCell(parts[1], 1f)

                tableRow.addView(snoTv)
                tableRow.addView(nameTv)
                tableRow.addView(numberTv)
                table.addView(tableRow)

                val divider = View(this).apply {
                    layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1)
                    setBackgroundColor(Color.parseColor("#44FFFFFF"))
                }
                table.addView(divider)
            }
        }
    }

    private fun createTableCell(content: String, weight: Float): TextView {
        return TextView(this).apply {
            text = content
            textSize = 14f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight)
        }
    }

    private fun shareResult(name: String, number: Int) {
        val message = "$name's lucky number is $number! Try your luck!"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        startActivity(Intent.createChooser(intent, "Share via:"))
    }
}