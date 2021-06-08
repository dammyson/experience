package com.agrosurvey.survey.dao

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.agrosurvey.survey.data.question.FieldType
import com.agrosurvey.survey.data.Response
import com.agrosurvey.survey.data.ResponseValue
import com.agrosurvey.survey.data.question.Question
import com.agrosurvey.survey.data.question.feedback.Feedback
import com.agrosurvey.survey.data.question.type.QuestionType
import com.agrosurvey.survey.data.survey.Survey
import com.agrosurvey.survey.model.AppDataBase
import com.agrosurvey.survey.model.db.FeedBackDao
import com.agrosurvey.survey.model.db.QuestionDao
import com.agrosurvey.survey.model.db.SurveyDao
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FeedbackDaoTests {



    lateinit var appDataBase: AppDataBase
    lateinit var classUnderTest: FeedBackDao
    lateinit var surveyDao: SurveyDao
    lateinit var questionDao: QuestionDao


    @Before
    fun initDb() {

        appDataBase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDataBase::class.java
        ).allowMainThreadQueries()
            .build()

        surveyDao = appDataBase.surveyDao()
        questionDao = appDataBase.questionDao()
        classUnderTest = appDataBase.feedBackDao()

    }

    @Test fun `Given there is surveys and feedbacks in database, I can retrives questions and their answers if any for a feedback`() = runBlocking {

        // Given
        val SURVEY_ID = "SURVEY_ID"
        val QUESTIONTYPE_ID = 1
        val FEEDBACK_ID = 1
        val FEEDBACK_2_ID = 2

        val QUESTION1_NAME = "Q1"
        val QUESTION2_NAME = "Q2"
        val QUESTION3_NAME = "Q3"

        val QUESTION1_RESPONSE = "RESP Q1"
        val QUESTION2_RESPONSE = "RESP Q2"

        surveyDao.insert(listOf(Survey(SURVEY_ID)))
        surveyDao.insert(listOf(Survey("#2")))
        questionDao.insertQuestionType(listOf(
                QuestionType(type_id = QUESTIONTYPE_ID,
                        name = "Question 1", slug = FieldType.ShortText

                )))
        questionDao.insert(listOf(
                Question(id = 1, survey_id = SURVEY_ID,
                        question_type_id = QUESTIONTYPE_ID,
                        title = QUESTION1_NAME
                )))
        questionDao.insert(listOf(
                Question(id = 2, survey_id = SURVEY_ID,
                        question_type_id = QUESTIONTYPE_ID,
                        title = QUESTION2_NAME
                )))
        questionDao.insert(listOf(
                Question(id = 3, survey_id = SURVEY_ID,
                        question_type_id = QUESTIONTYPE_ID,
                        title = QUESTION3_NAME
                )))

        classUnderTest.insert(listOf(
                Feedback(surveyId = SURVEY_ID).apply{ ID = FEEDBACK_ID }
        ))

        classUnderTest.insert(listOf(
            Feedback(surveyId = SURVEY_ID).apply{ ID = FEEDBACK_2_ID }
        ))

        classUnderTest.insertResponse(listOf(Response(
                questionId = 1, feedBackId = FEEDBACK_ID, value = ResponseValue.ShortText(QUESTION1_RESPONSE)
        )))
        classUnderTest.insertResponse(listOf(Response(
                questionId = 2, feedBackId = FEEDBACK_ID, value = ResponseValue.ShortText(QUESTION2_RESPONSE)
        )))

        // When
        val qaList = classUnderTest.getQuestionsAndResponseFor(FEEDBACK_ID.toLong())
        val qa2List = classUnderTest.getQuestionsAndResponseFor(FEEDBACK_2_ID.toLong())

        val counts = qaList.size

        // Then
        assertEquals(3, counts)
        assertNotNull(qaList[0].response)
        assertNull(qaList[2].response)

        assertEquals(QUESTION1_NAME, qaList[0].question?.question?.title)
        assertEquals(QUESTION2_NAME, qaList[1].question?.question?.title)
        assertEquals(QUESTION3_NAME, qaList[2].question?.question?.title)

        assertEquals(QUESTION1_RESPONSE, (qaList[0].response?.value as ResponseValue.ShortText).text)
        assertEquals(QUESTION2_RESPONSE, (qaList[1].response?.value  as ResponseValue.ShortText).text)


        assertNull(qaList[2].response)

        assertNull( qa2List[0].response?.value)






    }
}