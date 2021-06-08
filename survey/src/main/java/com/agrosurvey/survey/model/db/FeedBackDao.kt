package com.agrosurvey.survey.model.db

import androidx.paging.DataSource
import androidx.room.*
import com.agrosurvey.survey.data.question.feedback.Feedback
import com.agrosurvey.survey.data.Response
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.agrosurvey.survey.data.question.feedback.FeedbackAndSurvey

@Dao
interface FeedBackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(feedbacks: List<Feedback>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponse(responses: List<Response>): List<Long>

    @Query("DELETE FROM feedback")
    suspend fun deleteAll(): Int

    @Transaction
    @Query("SELECT *  FROM feedback WHERE ID = :feedbackId")
    suspend fun getFeedBackById(feedbackId: Long): FeedbackAndSurvey


    @Transaction
    //@Query("SELECT * FROM question WHERE survey_id = (SELECT surveyId FROM feedback WHERE id = :feedbackId)")

    @Query("SELECT *\n" +
            "FROM Question\n" +
            "LEFT JOIN (SELECT * FROM Response WHERE feedBackId = :feedbackId) as Resp\n" +
            "ON Question.id = Resp.questionId\n" +
            "WHERE Question.survey_id = (SELECT surveyId FROM FeedBack WHERE id = :feedbackId)\n")

    suspend fun getQuestionsAndResponseFor(feedbackId: Long): List<QuestionAndResponse>

}