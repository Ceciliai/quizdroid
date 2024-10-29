package edu.uw.ischool.hluo5.quizdroid

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.ListView
import android.util.Log


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val topics = listOf("Math", "Physics", "Marvel Super Heroes")
        val listView = findViewById<ListView>(R.id.topic_list_view)


        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, topics)
        listView.adapter = adapter


        listView.setOnItemClickListener { _, _, position, _ ->
            val topic = topics[position]
            Log.d("MainActivity", "Selected topic: $topic")
            val intent = Intent(this, TopicOverviewActivity::class.java)
            intent.putExtra("topic", topic)
            startActivity(intent)
        }

    }
}