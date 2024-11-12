package edu.uw.ischool.hluo5.quizdroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AnswerActivity : AppCompatActivity() {

    private var correctCount = 0
    private var totalQuestions = 0
    private var currentQuestionIndex = 0
    private lateinit var topic: String
    private lateinit var questionsList: List<Question>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        val selectedAnswer = intent.getStringExtra("selectedAnswer") ?: "No Answer"
        val correctAnswer = intent.getStringExtra("correctAnswer") ?: "No Correct Answer"
        currentQuestionIndex = intent.getIntExtra("currentQuestionIndex", 0)
        totalQuestions = intent.getIntExtra("totalQuestions", 0)
        topic = intent.getStringExtra("topic") ?: "Math"
        correctCount = intent.getIntExtra("correctCount", 0)
        questionsList = intent.getSerializableExtra("questions") as? List<Question> ?: emptyList()

        // 判断用户答案是否正确
        val isCorrect = selectedAnswer == correctAnswer
        if (isCorrect) correctCount++

        Log.d("AnswerActivity", "User selected: $selectedAnswer, Correct Answer: $correctAnswer, Is Correct: $isCorrect, Correct Count: $correctCount")

        // 显示用户的答案，正确答案，以及当前得分情况
        findViewById<TextView>(R.id.selected_answer).text = "Your Answer: $selectedAnswer"
        findViewById<TextView>(R.id.correct_answer).text = "Correct Answer: $correctAnswer"
        findViewById<TextView>(R.id.score).text = "You have $correctCount out of $totalQuestions correct"

        val nextButton = findViewById<Button>(R.id.next_button)
        if (currentQuestionIndex >= totalQuestions - 1) {
            // 如果是最后一题，将按钮文本设置为“Finish”
            nextButton.text = "Finish"
            nextButton.setOnClickListener {
                Log.d("AnswerActivity", "Quiz completed. Returning to MainActivity.")
                // 返回主界面
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        } else {
            // 如果不是最后一题，将按钮文本设置为“Next”
            nextButton.text = "Next"
            nextButton.setOnClickListener {
                Log.d("AnswerActivity", "Proceeding to next question. Current Index: $currentQuestionIndex, Next Index: ${currentQuestionIndex + 1}")
                // 跳转到下一个问题页面
                val intent = Intent(this, QuestionActivity::class.java)
                intent.putExtra("topic", topic)
                intent.putExtra("questions", ArrayList(questionsList))
                intent.putExtra("currentQuestionIndex", currentQuestionIndex + 1)
                intent.putExtra("totalQuestions", totalQuestions)
                intent.putExtra("correctCount", correctCount)
                startActivity(intent)
                finish()
            }
        }
    }
}
