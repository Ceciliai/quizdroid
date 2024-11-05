package edu.uw.ischool.hluo5.quizdroid

data class Topic(
    val title: String,
    val shortDescription: String,
    val longDescription: String,
    val questions: List<Question>
)