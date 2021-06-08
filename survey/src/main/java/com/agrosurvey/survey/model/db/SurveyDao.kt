package com.agrosurvey.survey.model.db

import androidx.paging.DataSource
import androidx.room.*
import com.agrosurvey.survey.data.question.feedback.Feedback
import com.agrosurvey.survey.data.question.Question
import com.agrosurvey.survey.data.question.QuestionAndType
import com.agrosurvey.survey.data.question.feedback.FeedbackAndSurvey
import com.agrosurvey.survey.data.survey.Survey

@Dao
interface SurveyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(surveys: List<Survey>): List<Long>

    @Query("SELECT * FROM survey WHERE title LIKE :text OR gist LIKE :text ")
    fun getSurveys(text : String): DataSource.Factory<Int, Survey>

    @Query("SELECT * FROM question WHERE survey_id = :surveyId")
    fun getQuestionsForSurvey(surveyId: String): DataSource.Factory<Int, Question>

    @Transaction
    @Query("SELECT * FROM question WHERE survey_id = :surveyId")
    suspend fun getAllQuestionsForSurvey(surveyId: String): List<QuestionAndType>

    @Query("SELECT * FROM question ")
    fun getQuestionsForSurvey(): DataSource.Factory<Int, Question>


    @Transaction
    @Query("SELECT * FROM feedback WHERE surveyId = :surveyId")
    fun getFeedBacksForSurvey(surveyId: String): DataSource.Factory<Int, FeedbackAndSurvey>

    @Query("DELETE FROM survey")
    suspend fun deleteAllSurveys(): Int

    @Query("DELETE FROM question")
    suspend fun deleteAllQuestions(): Int

    @Query("DELETE FROM feedback")
    suspend fun deleteAllFeedback(): Int

    @Query("DELETE FROM response")
    suspend fun deleteAllResponses(): Int


    @Query("SELECT COUNT(*) FROM survey ")
    suspend fun getSurveysCount() : Int

    @Query("SELECT * FROM survey WHERE id = :surveyId")
    suspend fun getSurvey(surveyId: String) : Survey

    @Query("SELECT * FROM feedback WHERE surveyId = :id")
    suspend fun getAllFeedBacksForSurvey(id: String): List<Feedback>

    @Transaction
    suspend fun flushSurveys() {
        deleteAllQuestions()
        deleteAllResponses()
        deleteAllFeedback()
        deleteAllSurveys()
    }


}