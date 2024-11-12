package edu.uw.ischool.hluo5.quizdroid
import android.content.Context
import org.json.JSONArray
import org.json.JSONObject


interface TopicRepository {
    fun getTopics(): List<Topic>
}