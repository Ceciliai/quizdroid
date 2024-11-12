package edu.uw.ischool.hluo5.quizdroid

import android.util.Log
import java.io.Serializable

data class Question(
    val questionText: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
): Serializable
