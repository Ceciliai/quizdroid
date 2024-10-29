package edu.uw.ischool.hluo5.quizdroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuestionActivity : AppCompatActivity() {

    private val questions = mapOf(
        "Math" to listOf(
            Triple("What is 2 + 2?", "4", listOf("3", "6", "5")),
            Triple("What is the square root of 9?", "3", listOf("1", "4", "6")),
            Triple("What is 5 * 5?", "25", listOf("20", "30", "15"))
        ),
        "Physics" to listOf(
            Triple("What is the unit of force?", "Newton", listOf("Joule", "Watt", "Pascal")),
            Triple("What is the speed of light?", "299,792,458 m/s", listOf("150,000,000 m/s", "300,000 m/s", "1,000,000 m/s")),
            Triple("What is acceleration due to gravity?", "9.8 m/s^2", listOf("9 m/s^2", "10 m/s^2", "11 m/s^2"))
        ),
        "Marvel Super Heroes" to listOf(
            Triple("Who is Iron Man?", "Tony Stark", listOf("Bruce Wayne", "Clark Kent", "Peter Parker")),
            Triple("Who is the Hulk?", "Bruce Banner", listOf("Steve Rogers", "Thor", "Wade Wilson")),
            Triple("Who is Thor's brother?", "Loki", listOf("Odin", "Balder", "Freyr"))
        )
    )

    private var currentQuestionIndex = 0
    private lateinit var currentTopicQuestions: List<Triple<String, String, List<String>>>
    private lateinit var correctAnswer: String
    private lateinit var topic: String
    private var correctCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        topic = intent.getStringExtra("topic") ?: "Math"
        currentTopicQuestions = questions[topic] ?: error("No questions for topic $topic")
        correctCount = intent.getIntExtra("correctCount", 0)
        currentQuestionIndex = intent.getIntExtra("currentQuestionIndex", 0)

        loadQuestion()

        val radioGroup = findViewById<RadioGroup>(R.id.radio_group)
        val submitButton = findViewById<Button>(R.id.submit_button)
        submitButton.isEnabled = false

        radioGroup.setOnCheckedChangeListener { _, _ ->
            submitButton.isEnabled = true
        }

        submitButton.setOnClickListener {
            val selectedAnswerId = radioGroup.checkedRadioButtonId
            val selectedAnswer = findViewById<RadioButton>(selectedAnswerId).text.toString()

            // 将答案和其他数据传递到AnswerActivity
            val intent = Intent(this, AnswerActivity::class.java)
            intent.putExtra("selectedAnswer", selectedAnswer)
            intent.putExtra("correctAnswer", correctAnswer)
            intent.putExtra("currentQuestionIndex", currentQuestionIndex)
            intent.putExtra("totalQuestions", currentTopicQuestions.size)
            intent.putExtra("correctCount", correctCount)
            intent.putExtra("topic", topic)
            startActivity(intent)

            finish()  // 结束当前活动
        }
    }

    private fun loadQuestion() {
        val questionText = findViewById<TextView>(R.id.question_text)
        val options = listOf(
            findViewById<RadioButton>(R.id.option1),
            findViewById<RadioButton>(R.id.option2),
            findViewById<RadioButton>(R.id.option3),
            findViewById<RadioButton>(R.id.option4)
        )

        val (question, answer, incorrectOptions) = currentTopicQuestions[currentQuestionIndex]
        correctAnswer = answer

        questionText.text = question

        // 将正确答案和错误答案混合后分配到选项按钮
        val allOptions = (incorrectOptions + answer).shuffled()
        options.forEachIndexed { index, radioButton ->
            radioButton.text = allOptions[index]
        }
    }
}