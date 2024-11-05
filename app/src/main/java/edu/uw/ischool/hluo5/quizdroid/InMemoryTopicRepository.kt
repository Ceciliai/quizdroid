package edu.uw.ischool.hluo5.quizdroid

import android.R // 导入 Android 默认图标资源

class InMemoryTopicRepository : TopicRepository {
    private val topics = listOf(
        Topic(
            title = "Math",
            shortDescription = "Math quizzes",
            longDescription = "A variety of math-related questions",
            questions = listOf(
                Question("What is 2 + 2?", listOf("3", "4", "5", "6"), 1),
                Question("What is the square root of 9?", listOf("1", "3", "4", "6"), 1),
                Question("What is 5 * 5?", listOf("15", "20", "25", "30"), 2)
            ),
            iconResId = R.drawable.ic_menu_info_details // 设置图标资源 ID
        ),
        Topic(
            title = "Physics",
            shortDescription = "Physics quizzes",
            longDescription = "Physics-related questions to challenge your understanding",
            questions = listOf(
                Question("What is the unit of force?", listOf("Joule", "Newton", "Watt", "Pascal"), 1),
                Question("What is the speed of light?", listOf("150,000,000 m/s", "299,792,458 m/s", "300,000 m/s", "1,000,000 m/s"), 1),
                Question("What is acceleration due to gravity?", listOf("9 m/s^2", "9.8 m/s^2", "10 m/s^2", "11 m/s^2"), 1)
            ),
            iconResId = R.drawable.ic_menu_info_details // 设置图标资源 ID
        ),
        Topic(
            title = "Marvel Super Heroes",
            shortDescription = "Marvel Universe Trivia",
            longDescription = "Test your knowledge about Marvel superheroes",
            questions = listOf(
                Question("Who is Iron Man?", listOf("Bruce Wayne", "Tony Stark", "Clark Kent", "Peter Parker"), 1),
                Question("Who is the Hulk?", listOf("Steve Rogers", "Bruce Banner", "Thor", "Wade Wilson"), 1),
                Question("Who is Thor's brother?", listOf("Odin", "Loki", "Balder", "Freyr"), 1)
            ),
            iconResId = R.drawable.ic_menu_info_details // 设置图标资源 ID
        )
    )

    override fun getTopics(): List<Topic> = topics
}

