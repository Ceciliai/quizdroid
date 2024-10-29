package edu.uw.ischool.hluo5.quizdroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TopicOverviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 引用 activity_topic_overview.xml 布局文件
        setContentView(R.layout.activity_topic_overview)

        // 获取传递的 topic 数据
        val topic = intent.getStringExtra("topic") ?: "Unknown Topic"
        val description = "$topic is an interesting topic to explore."
        val questionCount = 3

        // 设置话题描述和问题数量
        findViewById<TextView>(R.id.topic_description).text = description
        findViewById<TextView>(R.id.question_count).text = "Total Questions: $questionCount"

        // 点击“Begin”按钮跳转到问题页面
        val beginButton = findViewById<Button>(R.id.begin_button)
        beginButton.setOnClickListener {
            val intent = Intent(this, QuestionActivity::class.java)
            intent.putExtra("topic", topic)
            startActivity(intent)
        }
    }
}
