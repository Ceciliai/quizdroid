package edu.uw.ischool.hluo5.quizdroid

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class PreferencesActivity : AppCompatActivity() {

    private lateinit var urlEditText: EditText
    private lateinit var checkFrequencyEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        Log.d("PreferencesActivity", "Preferences activity started")

        sharedPreferences = getSharedPreferences("QuizDroidPrefs", Context.MODE_PRIVATE)

        urlEditText = findViewById(R.id.urlEditText)
        checkFrequencyEditText = findViewById(R.id.checkFrequencyEditText)
        saveButton = findViewById(R.id.saveButton)

        // Load current preferences and set default values if not set already
        val savedUrl = sharedPreferences.getString("data_url", "http://tednewardsandbox.site44.com/questions.json")
        val savedFrequency = sharedPreferences.getInt("check_frequency", 60)
        urlEditText.setText(savedUrl)
        checkFrequencyEditText.setText(savedFrequency.toString())

        Log.d("PreferencesActivity", "Loaded preferences - data_url: $savedUrl, check_frequency: $savedFrequency")

        saveButton.setOnClickListener {
            val url = urlEditText.text.toString()
            val frequency = checkFrequencyEditText.text.toString().toIntOrNull() ?: 60

            // Save new URL and frequency to SharedPreferences
            with(sharedPreferences.edit()) {
                putString("data_url", url)
                putInt("check_frequency", frequency)
                apply()
            }

            Log.d("PreferencesActivity", "Saved new preferences - data_url: $url, check_frequency: $frequency")

            // Start downloading the JSON file with the new URL
            val repository = JsonTopicRepository(this) { newTopics ->
                runOnUiThread {
                    // 在这里处理新的数据，例如更新UI
                    updateUI(newTopics)
                }
            }
            Log.d("PreferencesActivity", "Starting download of JSON file from URL: $url")

            repository.downloadJsonFile(url)

            Log.d("PreferencesActivity", "Download initiated for JSON file from URL: $url")

            // Finish the activity and return to the previous screen
            finish()
            Log.d("PreferencesActivity", "Preferences activity finished and closed")
        }
    }

    private fun updateUI(newTopics: List<Topic>) {
        // 这里是你的 UI 更新逻辑，比如刷新 ListView 或者 RecyclerView 等
        Log.d("PreferencesActivity", "UI updated with new topics, number of topics: ${newTopics.size}")
    }
}