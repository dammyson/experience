package com.agrosurvey.survey.model.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.agrosurvey.survey.data.question.Question
import com.agrosurvey.survey.data.question.group.QuestionGroup
import com.agrosurvey.survey.data.question.type.QuestionType
import com.agrosurvey.survey.data.survey.Survey

@Dao
interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(surveys: List<Question>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestionType(questionTypes: List<QuestionType>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestionGroupType(map: List<QuestionGroup>): List<Long>


    @Query("SELECT * FROM question")
    fun getQuestions(): DataSource.Factory<Int, Question>

    @Query("DELETE FROM survey")
    suspend fun deleteAll(): Int


}