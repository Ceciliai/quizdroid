# quizdroid

**QuizDroid: Repository**


**QuizDroid (Domain Models/Repository)**
An application that will allow users to take multiple-choice quizzes

now we will refactor to use a domain model and an Application object

note that a future version of this codebase will require permissions to be set; this can be done now or later, as you wish.

--------------------------------------------------------
**Developer Tasks:**

Create a class called QuizApp extending android.app.Application and make sure it is referenced from the app manifest; override the onCreate() method to emit a message to the diagnostic log to ensure it is being loaded and run

Use the "Repository" pattern to create a TopicRepository interface; create one implementation that simply stores elements in memory from a hard-coded list initialized on startup.

Create domain objects for Topic and Question

a Question is question text, four answers, and an integer saying which of the four answers is correct

a Topic is a title, short description, long description, and a collection of Question objects

--------------------------------------------------------------------------------------
**Developer Tasks:**

Provide a method or property on QuizApp for accessing the TopicRepository.

Refactor the activities in the application to use the TopicRepository.

On the topic list page, the title and the short description should come from the similar fields in the Topic object.

On the topic overview page, the title and long description should come from the similar fields in the Topic object. The Question object should be similarly easy to match up to the UI.

----------------------------------------------------------------------------------------
**Grading (5 pts):**

repo should be called 'quizdroid' on branch 'repository'

2pt: QuizApp extends Application, holds TopicRepository instance, is referenced from manifest, and writes to log

3pts: TopicRepository provides all access to the Topic/Quiz objects, and all data is coming from those objects

-------------------------------------------------------------------------------------------
**Extra Credit (5 pts)**

In the next part, we will need this application to need to access the Internet, among other things. Look through the list of permissions in the Android documentation, and add uses-permission elements as necessary to enable that now. (1 pt)

Create some unit tests that test the TopicRepository (2 pts)

Refactor the domain model so that Topics can have an icon to go along with the title and descriptions. (Use the stock Android icon for now if you don't want to test your drawing skills.) Refactor the topic list view to use the icon as part of the layout for each item in the list view. Display the icon on the topic overview page. (2pts)





