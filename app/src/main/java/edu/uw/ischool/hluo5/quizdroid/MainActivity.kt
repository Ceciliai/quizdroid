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
        // 初始化话题列表数据
        val topics = listOf("Math", "Physics", "Marvel Super Heroes")
        val listView = findViewById<ListView>(R.id.topic_list_view)

        // 使用 ArrayAdapter 显示话题列表
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, topics)
        listView.adapter = adapter

        // 为每个话题项添加点击事件，跳转到 TopicOverviewActivity
        listView.setOnItemClickListener { _, _, position, _ ->
            val topic = topics[position]
            Log.d("MainActivity", "Selected topic: $topic")
            val intent = Intent(this, TopicOverviewActivity::class.java)
            intent.putExtra("topic", topic)
            startActivity(intent)
        }

    }
}