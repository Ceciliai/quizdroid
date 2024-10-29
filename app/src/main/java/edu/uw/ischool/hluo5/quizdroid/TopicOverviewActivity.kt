package edu.uw.ischool.hluo5.quizdroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TopicOverviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_topic_overview)


        val topic = intent.getStringExtra("topic") ?: "Unknown Topic"
        val description = "$topic is an interesting topic to explore."
        val questionCount = 3


        findViewById<TextView>(R.id.topic_description).text = description
        findViewById<TextView>(R.id.question_count).text = "Total Questions: $questionCount"


        val beginButton = findViewById<Button>(R.id.begin_button)
        beginButton.setOnClickListener {
            val intent = Intent(this, QuestionActivity::class.java)
            intent.putExtra("topic", topic)
            startActivity(intent)
        }
    }
}
