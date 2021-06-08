package com.agrosurvey.survey

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
import org.mockito.junit.MockitoJUnit
import toothpick.testing.ToothPickRule

@RunWith(AndroidJUnit4::class)
class MySurveyTests {


    @get:Rule val initRule = MockitoJUnit.rule()
    @get:Rule var toothpickRule = ToothPickRule(this, "test scope")


    lateinit var classUnderTest: MySurvey


    @Before
    fun initDb() {
        toothpickRule.inject(this)
    }

    @Test fun `Get count`() = runBlocking {
    }
}