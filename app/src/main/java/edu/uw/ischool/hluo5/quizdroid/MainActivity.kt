package edu.uw.ischool.hluo5.quizdroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        // 初始化 ListView 和适配器
        val listView = findViewById<ListView>(R.id.topic_list_view)
        listViewAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, topics.map { it.title })
        listView.adapter = listViewAdapter

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

        // 加载数据
        loadTopics()
    }

    override fun onResume() {
        super.onResume()
        // 确保返回到 MainActivity 后更新数据
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
            repository.downloadJsonFile(it)
        }
    }

    private fun updateUI(newTopics: List<Topic>) {
        topics.clear()
        topics.addAll(newTopics)

        // 更新适配器的数据
        listViewAdapter.clear()
        listViewAdapter.addAll(topics.map { it.title })
        listViewAdapter.notifyDataSetChanged()
        Log.d("MainActivity", "UI updated with new topics, number of topics: ${newTopics.size}")
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
