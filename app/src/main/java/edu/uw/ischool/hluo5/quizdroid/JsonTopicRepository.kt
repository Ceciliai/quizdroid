package edu.uw.ischool.hluo5.quizdroid

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class JsonTopicRepository(
    private val context: Context,
    private val callback: (List<Topic>) -> Unit
) : TopicRepository {

    override fun getTopics(): List<Topic> {
        val downloadedFile = File(context.filesDir, "custom_questions.json")
        return if (downloadedFile.exists()) {
            // 如果下载的 JSON 文件存在，从该文件中读取
            Log.d("JsonTopicRepository", "Loading JSON from downloaded file")
            parseJson(downloadedFile.inputStream())
        } else {
            // 否则从 assets 文件夹中读取默认 JSON 文件
            Log.d("JsonTopicRepository", "Loading JSON from assets")
            context.assets.open("default_questions.json").use {
                return parseJson(it)
            }
        }
    }

    fun downloadJsonFile(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("JsonTopicRepository", "Starting download of JSON file from URL: $url")
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()
                Log.d("JsonTopicRepository", "Connection established, response code: ${connection.responseCode}")

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val downloadedFile = File(context.filesDir, "custom_questions.json")
                    inputStream.use { input ->
                        downloadedFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    Log.d("JsonTopicRepository", "JSON file downloaded successfully")

                    // 下载成功后解析 JSON 并通过回调传递数据
                    val topics = parseJson(downloadedFile.inputStream())
                    withContext(Dispatchers.Main) {
                        callback(topics)
                    }
                } else {
                    Log.e("JsonTopicRepository", "Failed to download JSON file, response code: ${connection.responseCode}")
                }
            } catch (e: Exception) {
                Log.e("JsonTopicRepository", "Error downloading JSON file", e)
            }
        }
    }

    private fun parseJson(inputStream: InputStream): List<Topic> {
        Log.d("JsonTopicRepository", "Parsing JSON data")
        val topics = mutableListOf<Topic>()
        try {
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
            Log.d("JsonTopicRepository", "Successfully parsed ${topics.size} topics")
        } catch (e: Exception) {
            Log.e("JsonTopicRepository", "Error parsing JSON", e)
        }
        return topics
    }
}