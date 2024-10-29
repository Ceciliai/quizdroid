package edu.uw.ischool.hluo5.quizdroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AnswerActivity : AppCompatActivity() {

    private var correctCount = 0
    private var totalQuestions = 0
    private var currentQuestionIndex = 0
    private lateinit var topic: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        val selectedAnswer = intent.getStringExtra("selectedAnswer") ?: "No Answer"
        val correctAnswer = intent.getStringExtra("correctAnswer") ?: "No Correct Answer"
        currentQuestionIndex = intent.getIntExtra("currentQuestionIndex", 0)
        totalQuestions = intent.getIntExtra("totalQuestions", 0)
        topic = intent.getStringExtra("topic") ?: "Math"
        correctCount = intent.getIntExtra("correctCount", 0)

        val isCorrect = selectedAnswer == correctAnswer
        if (isCorrect) correctCount++

        findViewById<TextView>(R.id.selected_answer).text = "Your Answer: $selectedAnswer"
        findViewById<TextView>(R.id.correct_answer).text = "Correct Answer: $correctAnswer"
        findViewById<TextView>(R.id.score).text = "You have $correctCount out of $totalQuestions correct"

        val nextButton = findViewById<Button>(R.id.next_button)
        if (currentQuestionIndex >= totalQuestions - 1) {
            nextButton.text = "Finish"
            nextButton.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        } else {
            nextButton.text = "Next"
            nextButton.setOnClickListener {
                val intent = Intent(this, QuestionActivity::class.java)
                intent.putExtra("topic", topic)
                intent.putExtra("currentQuestionIndex", currentQuestionIndex + 1)
                intent.putExtra("totalQuestions", totalQuestions)
                intent.putExtra("correctCount", correctCount)
                startActivity(intent)
                finish()
            }
        }
    }
}
