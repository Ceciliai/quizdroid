package edu.uw.ischool.hluo5.quizdroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TopicOverviewActivity : AppCompatActivity() {

    private lateinit var topic: String
    private lateinit var questionsList: List<Question>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic_overview)

        // 从 Intent 获取 topic 和 questions 列表
        topic = intent.getStringExtra("topic") ?: "Unknown Topic"
        questionsList = intent.getSerializableExtra("questions") as? List<Question> ?: emptyList()

        // 如果 questionsList 为空，记录错误日志
        if (questionsList.isEmpty()) {
            Log.e("TopicOverviewActivity", "Error: questionsList is empty for topic: $topic")
        } else {
            Log.d("TopicOverviewActivity", "Received topic: $topic with ${questionsList.size} questions")
        }

        // 更新界面中的主题描述和问题数量
        findViewById<TextView>(R.id.topic_description).text = "$topic is an interesting topic to explore."
        findViewById<TextView>(R.id.question_count).text = "Total Questions: ${questionsList.size}"

        // 设置 Begin 按钮的点击事件监听器
        val beginButton = findViewById<Button>(R.id.begin_button)
        beginButton.setOnClickListener {
            // 再次检查 questionsList 是否为空，确保在点击时不引发错误
            if (questionsList.isEmpty()) {
                Log.e("TopicOverviewActivity", "Error: questionsList is empty, cannot start quiz.")
                return@setOnClickListener
            }

            // 准备启动 QuestionActivity，并传递 topic 和 questions 列表
            val intent = Intent(this, QuestionActivity::class.java).apply {
                putExtra("topic", topic)
                putExtra("questions", ArrayList(questionsList))  // 确保传递的问题列表为 ArrayList 类型
                putExtra("currentQuestionIndex", 0)  // 从第一个问题开始
            }

            // 日志记录，表明 quiz 即将开始
            Log.d("TopicOverviewActivity", "Starting quiz for topic: $topic with ${questionsList.size} questions")

            // 启动 QuestionActivity
            startActivity(intent)
        }
    }
}
