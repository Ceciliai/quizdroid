**QuizDroid (Network)**
An application that will allow users to take multiple-choice quizzes

now we will download the topics and questions from a server



**Tasks:**

When the application fires up, it should, as a background operation, attempt to download a JSON file containing the questions from the server. (This doesn't need to happen more than once, when the app starts up.) For now, pop a Toast message displaying the URL that will eventually be hit. Make sure this URL is what's defined in the Preferences.

The background operation should now attempt to download the JSON file specified in the Preferences URL. Save this data to a local file as questions.json. Make sure that this file always remains in a good state--if the download fails or is interrupted, the previous file should remain in place.

If I am currently offline (in Airplane mode or in a no-bars area) the application should display a message telling me I have no access to the Internet. If I am in airplane mode, it should ask if I want to turn airplane mode off and take me to the Settings activity to do so. If I simply have no signal, it should just punt gracefully with a nice error message.

If the download of the questions fails, the application should tell me and ask if I want to retry or quit the application and try again later.

**Grading (5 pts)**
repo should be called 'quizdroid' on branch 'network'

2 pts: file is downloaded

2 pts: detect network signal/online access and display message if necessary

1 pt: display retry dialog

**Extra Credit (2 pts)**
provide some kind of notification to the user that downloading the JSON file is being attempted (1 pt)
