package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class TestResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_results)

        val resultTextView: TextView = findViewById(R.id.resultTextView)
        val percentage = intent.getDoubleExtra("percentage", 0.0)

        resultTextView.text = "Your test result: ${"%.2f".format(percentage)}%"

        // Найдем кнопки навигационной панели
        val homeButton: Button = findViewById(R.id.homeButton)
        val testingButton: Button = findViewById(R.id.testingButton)
        val statisticsButton: Button = findViewById(R.id.statisticsButton)
        val profileButton: Button = findViewById(R.id.profileButton)

        // Установим обработчики событий для каждой кнопки
        homeButton.setOnClickListener {
            val intent = Intent(this, HomepageActivity::class.java)
            startActivity(intent)
        }

        testingButton.setOnClickListener {
            val intent = Intent(this, TestingPageActivity::class.java)
            startActivity(intent)
        }

        statisticsButton.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }

        profileButton.setOnClickListener {
            val intent = Intent(this, ProfilePageActivity::class.java)
            startActivity(intent)
        }
    }
}
