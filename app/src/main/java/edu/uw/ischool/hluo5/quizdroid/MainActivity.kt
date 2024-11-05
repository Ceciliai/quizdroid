package edu.uw.ischool.hluo5.quizdroid

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.ListView
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 获取 QuizApp 实例并从中访问 TopicRepository
        val quizApp = application as QuizApp
        val topics = quizApp.topicRepository.getTopics()  // 从 TopicRepository 获取主题列表

        // 将 Topic 列表的标题提取出来
        val topicTitles = topics.map { it.title }

        // 使用 ArrayAdapter 显示从 TopicRepository 获取的主题标题
        val listView = findViewById<ListView>(R.id.topic_list_view)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, topicTitles)
        listView.adapter = adapter

        // 设置点击事件监听器
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedTopic = topics[position]
            Log.d("MainActivity", "Selected topic: ${selectedTopic.title}")

            // 创建 Intent 并传递选定主题的详细信息
            val intent = Intent(this, TopicOverviewActivity::class.java)
            intent.putExtra("topic", selectedTopic.title)
            intent.putExtra("description", selectedTopic.longDescription)
            startActivity(intent)
        }
    }
}