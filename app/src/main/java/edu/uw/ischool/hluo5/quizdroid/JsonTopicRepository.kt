package edu.uw.ischool.hluo5.quizdroid

import android.content.Context
import org.json.JSONArray
import java.io.InputStream

class JsonTopicRepository(private val context: Context) : TopicRepository {

    override fun getTopics(): List<Topic> {
        val sharedPreferences = context.getSharedPreferences("QuizDroidPrefs", Context.MODE_PRIVATE)
        val filePath = sharedPreferences.getString("data_url", "default")

        val inputStream: InputStream = if (filePath == "default" || filePath == "http://tednewardsandbox.site44.com/questions.json") {
            context.assets.open("questions.json")
        } else {
            // 这里可以加入从外部存储读取文件的逻辑
            context.assets.open("questions.json") // 占位符，直到下载逻辑实现
        }

        return parseQuestionsFromJson(inputStream)
    }

    private fun parseQuestionsFromJson(inputStream: InputStream): List<Topic> {
        val topics = mutableListOf<Topic>()
        val json = inputStream.bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(json)

        for (i in 0 until jsonArray.length()) {
            val topicObject = jsonArray.getJSONObject(i)
            val title = topicObject.getString("title")
            val desc = topicObject.getString("desc")
            val questionsArray = topicObject.getJSONArray("questions")

            val questions = mutableListOf<Question>()
            for (j in 0 until questionsArray.length()) {
                val questionObject = questionsArray.getJSONObject(j)
                val questionText = questionObject.getString("text")
                val correctAnswerIndex = questionObject.getString("answer").toInt()

                val answersArray = questionObject.getJSONArray("answers")
                val answers = mutableListOf<String>()
                for (k in 0 until answersArray.length()) {
                    answers.add(answersArray.getString(k))
                }

                questions.add(Question(questionText, answers, correctAnswerIndex))
            }

            topics.add(
                Topic(
                    title = title,
                    shortDescription = desc,
                    longDescription = desc,
                    questions = questions,
                    iconResId = 0
                )
            )
        }
        return topics
    }
}
