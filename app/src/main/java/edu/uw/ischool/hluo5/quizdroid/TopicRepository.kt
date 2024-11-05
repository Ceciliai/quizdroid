package edu.uw.ischool.hluo5.quizdroid

interface TopicRepository {
    fun getTopics(): List<Topic>
}