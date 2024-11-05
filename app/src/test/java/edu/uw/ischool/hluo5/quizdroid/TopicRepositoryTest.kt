package edu.uw.ischool.hluo5.quizdroid

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TopicRepositoryTest {

    private lateinit var repository: TopicRepository

    @Before
    fun setUp() {
        repository = InMemoryTopicRepository() // 使用您的实际实现
    }

    @Test
    fun `test if topics are not empty`() {
        val topics = repository.getTopics()
        assertTrue("Topics should not be empty", topics.isNotEmpty())
    }

    @Test
    fun `test if repository returns correct topics`() {
        val topics = repository.getTopics()
        assertEquals("There should be 3 topics", 3, topics.size)

        val mathTopic = topics[0]
        assertEquals("Math", mathTopic.title)
        assertEquals("Math quizzes", mathTopic.shortDescription)
        assertEquals("A variety of math-related questions", mathTopic.longDescription)
        assertEquals("Math topic should have 2 questions", 2, mathTopic.questions.size)

        val physicsTopic = topics[1]
        assertEquals("Physics", physicsTopic.title)
        assertEquals("Physics quizzes", physicsTopic.shortDescription)
        assertEquals("Physics-related questions to challenge your understanding", physicsTopic.longDescription)
        assertEquals("Physics topic should have 2 questions", 2, physicsTopic.questions.size)

        val marvelTopic = topics[2]
        assertEquals("Marvel Super Heroes", marvelTopic.title)
        assertEquals("Marvel Universe Trivia", marvelTopic.shortDescription)
        assertEquals("Test your knowledge about Marvel superheroes", marvelTopic.longDescription)
        assertEquals("Marvel Super Heroes topic should have 2 questions", 2, marvelTopic.questions.size)
    }

    @Test
    fun `test if each topic has questions`() {
        val topics = repository.getTopics()
        for (topic in topics) {
            assertTrue("Each topic should have at least one question", topic.questions.isNotEmpty())
        }
    }

    @Test
    fun `test if questions in topics have correct answers`() {
        val topics = repository.getTopics()
        for (topic in topics) {
            for (question in topic.questions) {
                assertTrue("Each question should have at least one answer option", question.options.isNotEmpty())
                assertTrue("Correct answer index should be within options range",
                    question.correctAnswerIndex in question.options.indices)
            }
        }
    }
}
