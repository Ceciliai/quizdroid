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

        urlEditText.setText(sharedPreferences.getString("data_url", "http://tednewardsandbox.site44.com/questions.json"))
        checkFrequencyEditText.setText(sharedPreferences.getInt("check_frequency", 60).toString())

        saveButton.setOnClickListener {
            val url = urlEditText.text.toString()
            val frequency = checkFrequencyEditText.text.toString().toIntOrNull() ?: 60

            with(sharedPreferences.edit()) {
                putString("data_url", url)
                putInt("check_frequency", frequency)
                apply()
            }

            // 输出保存的设置值
            Log.d("PreferencesActivity", "Saved data_url: $url")
            Log.d("PreferencesActivity", "Saved check_frequency: $frequency")

            finish()
        }
    }
}
