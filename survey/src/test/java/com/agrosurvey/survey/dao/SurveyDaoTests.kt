package com.agrosurvey.survey.dao

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.agrosurvey.survey.data.survey.Survey
import com.agrosurvey.survey.model.AppDataBase
import com.agrosurvey.survey.model.db.SurveyDao
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SurveyDaoTests {



    lateinit var appDataBase: AppDataBase
    lateinit var classUnderTest: SurveyDao


    @Before
    fun initDb() {

        appDataBase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDataBase::class.java
        ).allowMainThreadQueries()
            .build()

        classUnderTest = appDataBase.surveyDao()

    }

    @Test fun `Given there is surveys in db, I can actually get correct count of them`() = runBlocking {

        val SURVEY_ID = "SURVEY_ID"
        // Given there is a saved survey
        classUnderTest.insert(listOf(Survey(SURVEY_ID)))
        classUnderTest.insert(listOf(Survey("#2")))
        classUnderTest.insert(listOf(Survey("#3")))

        // When getting the Users via the DAO
        val counts = classUnderTest.getSurveysCount()

        // Then the retrieved Users matches the original user object
        assertEquals(3, counts)
    }

    @Test fun `Given there is a corresponding survey in db, I can actually get it when requested through its Id`() = runBlocking {

        val SURVEY_ID = "SURVEY_ID"
        // Given there is are saved surveys
        classUnderTest.insert(listOf(Survey(SURVEY_ID)))
        classUnderTest.insert(listOf(Survey("#2")))
        classUnderTest.insert(listOf(Survey("#3")))

        // When getting the Users via the DAO
        val survey = classUnderTest.getSurvey(SURVEY_ID)

        // Then the retrieved Users matches the original user object
        assertEquals(SURVEY_ID, survey.id)
    }
}