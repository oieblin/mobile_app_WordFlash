package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import java.io.File

class StatisticsActivity : AppCompatActivity() {

    private lateinit var percentageTextView: TextView
    private lateinit var homeButton: Button
    private lateinit var testingButton: Button
    private lateinit var statisticsButton: Button
    private lateinit var profileButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        percentageTextView = findViewById(R.id.percentageTextView)
        homeButton = findViewById(R.id.homeButton)
        testingButton = findViewById(R.id.testingButton)
        statisticsButton = findViewById(R.id.statisticsButton)
        profileButton = findViewById(R.id.profileButton)

        // Чтение процента из файла current_user.txt
        val percentage = loadPercentage()
        if (percentage != null) {
            percentageTextView.text = "${"%.2f".format(percentage)}%"  // Отображаем процент с двумя знаками после запятой
        } else {
            percentageTextView.text = "No Data Available"
        }

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

    private fun loadPercentage(): Double? {
        val file = File(filesDir, "current_user.txt")
        if (file.exists()) {
            val lines = file.readLines()
            for (line in lines) {
                if (line.contains("Test result:")) {
                    val resultIndex = line.indexOf("Test result:") + "Test result:".length
                    val percentageString = line.substring(resultIndex).trim().removeSuffix("%")
                    return percentageString.toDoubleOrNull()
                }
            }
        }
        return null  // Если данные не найдены
    }

    private fun updateTestResult(newPercentage: Double) {
        val file = File(filesDir, "current_user.txt")
        if (file.exists()) {
            // Читаем текущие строки
            val lines = file.readLines().toMutableList()
            var updated = false

            // Обновляем строку с результатом
            for (i in lines.indices) {
                if (lines[i].contains("Test result:")) {
                    lines[i] = "Test result: ${"%.2f".format(newPercentage)}%"
                    updated = true
                    break
                }
            }

            // Если строки с результатом не было, добавляем её
            if (!updated) {
                lines.add("Test result: ${"%.2f".format(newPercentage)}%")
            }

            // Записываем изменения обратно в файл
            file.writeText(lines.joinToString("\n"))
        } else {
            // Если файла нет, создаём его и записываем результат
            file.writeText("Test result: ${"%.2f".format(newPercentage)}%")
        }
    }
}
