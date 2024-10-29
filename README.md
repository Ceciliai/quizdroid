# quizdroid

**QuizDroid (Basics)**


Helpful reminder: try and design the architecture with your application that makes things reusable and easy to integrate.


**QuizDroid (Basics)**
An application that will allow users to take multiple-choice quizzes


**Stories:**


As a user, when I start the app, I should see a list of different topics on which to take a quiz. (For now, these should be hard-coded to read "Math", "Physics", and "Marvel Super Heroes", as well as any additional topics you feel like adding into the mix.)


As a user, when I select a topic from the list, it should take me to the "topic overview" page, which displays a brief description of this topic, the total number of questions in this topic, and a "Begin" button taking me to the first question.


As a user, when I press the "Begin" button from the topic overview page, it should take me to the first question page, which will consist of a question (TextView), four radio buttons each of which consist of one answer, and a "Submit" button.


As a user, when I am on a question page and I press the "Submit" button, if no radio button is selected, it should do nothing. If a radio button is checked, it should take me to the "answer" page. (Ideally, the Submit button should not be visible until a radio button is selected.)


As a user, when I am on the "answer" page, it should display the answer I gave, the correct answer, and tell me the total number of correct vs incorrect answers so far ("You have 5 out of 9 correct"), and display a "Next" button taking me to the next question page, or else display a "Finish" button if that is the last question in the topic.


As a user, when I am on the "answer" page, and it is the last question of the topic, the "Finish" button should take me back to the first topic-list page, so that I can take a new quiz.


**Implementation Note:**


you can use either separate Activities or one Activity + multiple Fragments to build this; this is entirely up to you, and there's pros/cons with each approach


it's helpful to spend some time thinking about how an MVC approach would work with this, and design accordingly


keep in mind almost all the text (questions, topics, etc) will be loaded dynamically in future parts of this homework, which may influence some of your decisions now
Grading (5 pts) 
repo should be called 'quizdroid'; do work on branch 'basicui'


We will clone and build it from the GH repo


1 pt for each story


**Extra Credit (2 pts):**


As a user on the question page, if I hit the "back" button it should NOT take me to the answer summary page of the previous question, but to the previous question page directly. (1 pt)


As a user on the first question page of a topic, hitting "back" should take me back to the topic list page, not the topic overview page. (1 pt)
