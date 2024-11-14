package edu.uw.ischool.hluo5.quizdroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {

    private lateinit var listViewAdapter: ArrayAdapter<String>
    private var topics = mutableListOf<Topic>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity", "MainActivity started")

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        Log.d("MainActivity", "Toolbar set up successfully")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            Log.d("MainActivity", "Applied system bar insets for edge-to-edge display")
            insets
        }

        // Initialize ListView and adapter
        val listView = findViewById<ListView>(R.id.topic_list_view)
        listViewAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, topics.map { it.title })
        listView.adapter = listViewAdapter
        Log.d("MainActivity", "ListView and adapter initialized")

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedTopic = topics[position]
            Log.d("MainActivity", "User selected topic: ${selectedTopic.title}")

            val intent = Intent(this, TopicOverviewActivity::class.java).apply {
                putExtra("topic", selectedTopic.title)
                putExtra("questions", ArrayList(selectedTopic.questions))
            }
            Log.d("MainActivity", "Starting TopicOverviewActivity with topic: ${selectedTopic.title}")
            startActivity(intent)
        }

        // Use Coroutines to show Toast with a delay between each
        CoroutineScope(Dispatchers.Main).launch {
            // Get the URL from SharedPreferences
            val preferences = getSharedPreferences("QuizDroidPrefs", Context.MODE_PRIVATE)
            val url = preferences.getString("data_url", "http://tednewardsandbox.site44.com/questions.json") ?: ""

            // Check airplane mode status and network availability
            if (NetworkUtils.isAirplaneModeOn(this@MainActivity)) {
                Log.d("MainActivity", "Airplane mode is on, displaying dialog")
                // Show dialog to prompt the user to turn off Airplane mode
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Airplane Mode Enabled")
                    .setMessage("Airplane mode is currently on. Would you like to go to settings to turn it off?")
                    .setPositiveButton("Go to Settings") { _, _ ->
                        // Navigate to Airplane mode settings
                        startActivity(Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS))
                    }
                    .setNegativeButton("Cancel") { _, _ ->
                        Log.d("MainActivity", "User chose to stay in Airplane mode.")
                    }
                    .show()
                return@launch
            } else if (!NetworkUtils.isNetworkAvailable(this@MainActivity)) {
                Log.d("MainActivity", "No network available, showing no network dialog")
                // Use AlertDialog to inform the user about no network availability
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Network Unavailable")
                    .setMessage("Unable to connect to the internet. Please check your network connection.")
                    .setPositiveButton("OK") { _, _ ->
                        Log.d("MainActivity", "User acknowledged network unavailability.")
                    }
                    .show()
                return@launch
            } else {
                // Show the URL using a Toast message
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Download URL: $url", Toast.LENGTH_LONG).show()
                }
                Log.d("MainActivity", "Toast displayed with download URL: $url")

                // Wait for 2 seconds before proceeding with the download
                delay(2000)

                // Check network availability before calling the download task
                if (url.isNotEmpty()) {
                    Log.d("MainActivity", "Network available. Starting download from URL: $url")
                    val networkStatusTextView = findViewById<TextView>(R.id.network_status)
                    networkStatusTextView.visibility = TextView.GONE

                    CoroutineScope(Dispatchers.IO).launch {
                        val success = downloadJson(this@MainActivity, url)
                        withContext(Dispatchers.Main) {
                            if (success) {
                                Log.d("MainActivity", "Download succeeded")
                                Toast.makeText(this@MainActivity, "Download succeeded!", Toast.LENGTH_LONG).show()
                                Log.d("MainActivity", "Toast: Download succeeded displayed")
                            } else {
                                Log.d("MainActivity", "Download failed")
                                // Handle download failure gracefully by asking to retry or quit
                                showDownloadRetryDialog(url)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume called, reloading topics")
        // Reload topics when returning to MainActivity
        loadTopics()
    }

    private fun loadTopics() {
        val repository = JsonTopicRepository(this) { newTopics ->
            runOnUiThread {
                updateUI(newTopics)
            }
        }
        val sharedPreferences = getSharedPreferences("QuizDroidPrefs", MODE_PRIVATE)
        val url = sharedPreferences.getString("data_url", "http://tednewardsandbox.site44.com/questions.json")
        Log.d("MainActivity", "Loading topics from URL: $url")
        url?.let {
            // Add Toast to notify the user before downloading starts
            Toast.makeText(this, "Loading topics from URL: $url", Toast.LENGTH_LONG).show()
            repository.downloadJsonFile(it)
        }
    }

    private fun updateUI(newTopics: List<Topic>) {
        topics.clear()
        topics.addAll(newTopics)
        Log.d("MainActivity", "Topics updated, number of topics: ${newTopics.size}")

        // Update adapter data
        listViewAdapter.clear()
        listViewAdapter.addAll(topics.map { it.title })
        listViewAdapter.notifyDataSetChanged()
        Log.d("MainActivity", "ListView adapter updated with new topics")
    }

    private fun downloadJson(context: Context, url: String): Boolean {
        return try {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val jsonString = response.body?.string() ?: return false
                val file = File(context.filesDir, "questions.json")
                file.writeText(jsonString)
                Log.d("MainActivity", "File saved successfully at ${file.path}")
                true
            } else {
                Log.e("MainActivity", "Download failed with response code: ${response.code}")
                false
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Exception during download", e)
            false
        }
    }

    private fun showDownloadRetryDialog(url: String) {
        AlertDialog.Builder(this)
            .setTitle("Download Failed")
            .setMessage("Failed to download the data. Would you like to retry?")
            .setPositiveButton("Retry") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    val success = downloadJson(this@MainActivity, url)
                    withContext(Dispatchers.Main) {
                        if (success) {
                            Toast.makeText(this@MainActivity, "Download succeeded!", Toast.LENGTH_LONG).show()
                        } else {
                            showDownloadRetryDialog(url)
                        }
                    }
                }
            }
            .setNegativeButton("Quit") { _, _ ->
                finish()
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        Log.d("MainActivity", "Options menu created")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_preferences -> {
                Log.d("MainActivity", "Preferences menu item selected")
                val intent = Intent(this, PreferencesActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }.also {
            Log.d("MainActivity", "Options item selected: ${item.title}")
        }
    }
}
