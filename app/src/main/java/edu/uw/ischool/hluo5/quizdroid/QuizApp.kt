package edu.uw.ischool.hluo5.quizdroid

import android.app.Application
import android.util.Log

class QuizApp : Application() {

    lateinit var topicRepository: TopicRepository

    override fun onCreate() {
        super.onCreate()

        Log.d("QuizApp", "QuizApp has been initialized")


        topicRepository = InMemoryTopicRepository()
    }
}