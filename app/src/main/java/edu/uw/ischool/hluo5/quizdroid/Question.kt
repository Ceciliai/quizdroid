package edu.uw.ischool.hluo5.quizdroid

import android.util.Log

data class Question(
    val questionText: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
) {
    init {
        Log.d("Question", "Created question: $questionText")
        Log.d("Question", "Answers: $answers")
        Log.d("Question", "Correct answer index: $correctAnswerIndex")
    }
}
