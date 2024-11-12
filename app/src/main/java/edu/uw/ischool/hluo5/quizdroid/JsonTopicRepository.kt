package edu.uw.ischool.hluo5.quizdroid

import android.content.Context
import android.util.Log
import org.json.JSONArray
import java.io.InputStream

class JsonTopicRepository(private val context: Context) : TopicRepository {

    override fun getTopics(): List<Topic> {
        return try {
            // 从 assets 文件夹中读取 JSON 文件
            val inputStream: InputStream = context.assets.open("questions.json")
            parseQuestionsFromJson(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // 定义 parseQuestionsFromJson 方法来解析 JSON 数据
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

                // 将 answer 字符串转换为整数，表示正确答案的索引
                val correctAnswerIndex = questionObject.getString("answer").toInt()

                // 获取答案列表
                val answersArray = questionObject.getJSONArray("answers")
                val answers = mutableListOf<String>()
                for (k in 0 until answersArray.length()) {
                    answers.add(answersArray.getString(k))
                }

                // 添加解析后的问题对象
                questions.add(Question(questionText, answers, correctAnswerIndex))
            }

            // 添加解析后的主题对象
            topics.add(
                Topic(
                    title = title,
                    shortDescription = desc,
                    longDescription = desc,
                    questions = questions,
                    iconResId = 0 // 暂时设置 iconResId 为 0
                )
            )

            // 打印日志，确认主题和问题数量
            Log.d("JsonTopicRepository", "Parsed topic: $title with questions count: ${questions.size}")
        }
        return topics
    }
}
