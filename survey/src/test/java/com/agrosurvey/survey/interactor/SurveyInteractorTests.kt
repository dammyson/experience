package com.agrosurvey.survey.interactor

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.agrosurvey.survey.data.survey.Survey
import com.agrosurvey.survey.domain.SurveyInteractor
import com.agrosurvey.survey.model.AppDataBase
import com.agrosurvey.survey.model.db.FeedBackDao
import com.agrosurvey.survey.model.db.QuestionDao
import com.agrosurvey.survey.model.db.SurveyDao
import com.agrosurvey.survey.model.repository.SurveyRepository
import com.agrosurvey.survey.model.system.NetworkHelper
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import toothpick.testing.ToothPickRule

@RunWith(AndroidJUnit4::class)
class SurveyInteractorTests {


    @get:Rule val initRule = MockitoJUnit.rule()
    @get:Rule var toothpickRule = ToothPickRule(this, "test scope")

    @Mock
    lateinit var surveyRepository: SurveyRepository

    @Mock
    lateinit var surveyDao: SurveyDao

    @Mock
    lateinit var questionDao: QuestionDao

    @Mock
    lateinit var feddBackDao: FeedBackDao

    @Mock
    lateinit var networkHelper: NetworkHelper

    @InjectMocks
    lateinit var classUnderTest: SurveyInteractor


    @Before
    fun setup() {
        toothpickRule.inject(this)
    }

    @Test fun `Given there survey dao return a survey, I can retrive it successfuly from my interactor`() = runBlocking {

        val SURVEY_ID = "SURVEY_ID"

        val surveys = Survey(SURVEY_ID)
        Mockito.`when`(surveyDao.getSurvey(SURVEY_ID)).thenReturn(surveys)

        val survey = classUnderTest.getSurvey(SURVEY_ID)

        assertEquals(SURVEY_ID, survey.id)
    }

}