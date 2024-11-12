package edu.uw.ischool.hluo5.quizdroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Set up the toolbar as the app's action bar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Apply padding to handle system bars (for edge-to-edge display)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Use JsonTopicRepository to load topics from JSON
        val repository: TopicRepository = JsonTopicRepository(this)
        val topics = repository.getTopics()  // Get list of topics from JsonTopicRepository

        // Log each topic and its questions to verify loading
        topics.forEach { topic ->
            Log.d("MainActivity", "Title: ${topic.title}, Short Description: ${topic.shortDescription}, Long Description: ${topic.longDescription}")
            topic.questions.forEach { question ->
                Log.d("MainActivity", "Question: ${question.questionText}, Answers: ${question.answers}")
            }
        }

        // Extract titles from the topics list
        val topicTitles = topics.map { it.title }

        // Set up ListView with the topic titles
        val listView = findViewById<ListView>(R.id.topic_list_view)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, topicTitles)
        listView.adapter = adapter

        // Set click listener for ListView items
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedTopic = topics[position]
            Log.d("MainActivity", "Selected topic: ${selectedTopic.title}")

            // Create an Intent to start TopicOverviewActivity, passing selected topic details
            val intent = Intent(this, TopicOverviewActivity::class.java)
            intent.putExtra("topic", selectedTopic.title)
            intent.putExtra("questions", ArrayList(selectedTopic.questions)) // Pass the list of questions as ArrayList
            startActivity(intent)
        }
    }

    // Inflate the options menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Handle menu item selection
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_preferences -> {
                // Start PreferencesActivity when "Preferences" menu item is selected
                val intent = Intent(this, PreferencesActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
