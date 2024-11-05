package edu.uw.ischool.hluo5.quizdroid

class InMemoryTopicRepository : TopicRepository {
    private val topics = listOf(
        Topic(
            title = "Math",
            shortDescription = "Math quizzes",
            longDescription = "A variety of math-related questions",
            questions = listOf(
                Question("What is 2 + 2?", listOf("3", "4", "5", "6"), 1),
                Question("What is 3 * 3?", listOf("6", "7", "8", "9"), 3)
            )
        ),
        Topic(
            title = "Physics",
            shortDescription = "Physics quizzes",
            longDescription = "Physics-related questions to challenge your understanding",
            questions = listOf(
                Question("What is the unit of force?", listOf("Newton", "Pascal", "Joule", "Watt"), 0),
                Question("What is the speed of light?", listOf("3x10^8 m/s", "1x10^6 m/s", "3x10^6 m/s", "1x10^8 m/s"), 0)
            )
        ),
        Topic(
            title = "Marvel Super Heroes",
            shortDescription = "Marvel Universe Trivia",
            longDescription = "Test your knowledge about Marvel superheroes",
            questions = listOf(
                Question("Who is Iron Man?", listOf("Bruce Wayne", "Tony Stark", "Clark Kent", "Peter Parker"), 1),
                Question("What is Captain America's shield made of?", listOf("Adamantium", "Vibranium", "Titanium", "Steel"), 1)
            )
        )
    )

    override fun getTopics(): List<Topic> = topics
}