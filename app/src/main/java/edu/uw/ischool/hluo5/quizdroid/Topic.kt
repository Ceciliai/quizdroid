package edu.uw.ischool.hluo5.quizdroid

import java.io.Serializable

data class Topic(
    val title: String,
    val shortDescription: String,
    val longDescription: String,
    val questions: List<Question>,
    val iconResId: Int
) : Serializable
