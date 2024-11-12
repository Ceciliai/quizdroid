package edu.uw.ischool.hluo5.quizdroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuestionActivity : AppCompatActivity() {

    private lateinit var questionsList: List<Question>
    private var currentQuestionIndex = 0
    private lateinit var correctAnswer: String
    private lateinit var topic: String
    private var correctCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        topic = intent.getStringExtra("topic") ?: "Unknown Topic"
        questionsList = intent.getSerializableExtra("questions") as? List<Question> ?: emptyList()
        if (questionsList.isEmpty()) {
            Log.e("QuestionActivity", "Error: questionsList is empty!")
            finish()
            return
        }
        currentQuestionIndex = intent.getIntExtra("currentQuestionIndex", 0)
        correctCount = intent.getIntExtra("correctCount", 0)

        Log.d("QuestionActivity", "onCreate: Topic - $topic, Question Index - $currentQuestionIndex, Correct Count - $correctCount")

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

            // 跳转到 AnswerActivity 以显示答题进度
            Log.d("QuestionActivity", "Answer submitted: Selected - $selectedAnswer, Correct Answer - $correctAnswer")

            val intent = Intent(this, AnswerActivity::class.java)
            intent.putExtra("selectedAnswer", selectedAnswer)
            intent.putExtra("correctAnswer", correctAnswer)
            intent.putExtra("currentQuestionIndex", currentQuestionIndex)
            intent.putExtra("totalQuestions", questionsList.size)
            intent.putExtra("correctCount", correctCount)
            intent.putExtra("topic", topic)
            intent.putExtra("questions", ArrayList(questionsList)) // 传递问题列表
            startActivity(intent)
            finish()
        }

        val backButton = findViewById<Button>(R.id.back_button)
        backButton.setOnClickListener {
            handleBackButtonPress()
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

        val currentQuestion = questionsList[currentQuestionIndex]
        correctAnswer = currentQuestion.answers[currentQuestion.correctAnswerIndex]
        questionText.text = currentQuestion.questionText

        val allOptions = currentQuestion.answers.shuffled()
        options.forEachIndexed { index, radioButton ->
            radioButton.text = allOptions[index]
        }
        Log.d("QuestionActivity", "Loaded question: ${currentQuestion.questionText}, Correct Answer: $correctAnswer")
    }

    private fun handleBackButtonPress() {
        if (currentQuestionIndex == 0) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        } else {
            currentQuestionIndex--
            loadQuestion()
            val radioGroup = findViewById<RadioGroup>(R.id.radio_group)
            radioGroup.clearCheck()
            findViewById<Button>(R.id.submit_button).isEnabled = false
        }
    }
}
