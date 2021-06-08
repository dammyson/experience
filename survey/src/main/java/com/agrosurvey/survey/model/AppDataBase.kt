package com.agrosurvey.survey.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.agrosurvey.survey.data.*
import com.agrosurvey.survey.data.question.Question
import com.agrosurvey.survey.data.question.type.QuestionType
import com.agrosurvey.survey.data.Section
import com.agrosurvey.survey.data.question.feedback.Feedback
import com.agrosurvey.survey.data.question.group.QuestionGroup
import com.agrosurvey.survey.data.survey.Survey
import com.agrosurvey.survey.model.db.FeedBackDao
import com.agrosurvey.survey.model.db.QuestionDao
import com.agrosurvey.survey.model.db.SurveyDao
import com.agrosurvey.survey.model.db.converters.*

@Database(
        entities = [Survey::class,
            Section::class,
            Question::class,
            QuestionType::class,
            QuestionGroup::class,
            Feedback::class,
            Response::class
        ],
        version = 1,
        exportSchema = false
)
@TypeConverters(FieldTypeConverter::class,
        ResponseValueConverter::class,
        OffsetDateTimeConverter::class,
        PendingActionConverter::class,
        ResponseValueConverter::class,
        SkipLogicListConverter::class,
        OptionConverter::class,
        OptionListConverter::class,
        LatLngConverter::class
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun surveyDao(): SurveyDao
    abstract fun questionDao(): QuestionDao
    abstract fun feedBackDao(): FeedBackDao

    companion object {

        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AppDataBase::class.java, "SURVEY.db")
                        .build()
    }
}