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

        // 使用 JsonTopicRepository 从 JSON 文件加载题目数据
        val repository: TopicRepository = JsonTopicRepository(this)
        val topics = repository.getTopics()  // 从 JsonTopicRepository 获取主题列表

        // 日志输出每个主题及其问题，检查是否正确加载
        topics.forEach { topic ->
            Log.d("MainActivity", "Title: ${topic.title}, Short Description: ${topic.shortDescription}, Long Description: ${topic.longDescription}")
            topic.questions.forEach { question ->
                Log.d("MainActivity", "Question: ${question.questionText}, Answers: ${question.answers}")
            }
        }

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

            // 创建一个 Intent，并传递所选主题的详细信息，包括问题列表
            val intent = Intent(this, TopicOverviewActivity::class.java)
            intent.putExtra("topic", selectedTopic.title)
            intent.putExtra("questions", ArrayList(selectedTopic.questions)) // 将问题列表作为 ArrayList 传递
            startActivity(intent)
        }
    }
}
