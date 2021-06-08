package com.agrosurvey.survey.data.question.type

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.agrosurvey.survey.data.question.FieldType

@Entity
class QuestionType(
    @PrimaryKey
    var type_id: Int? = null,
    var name: String? = null,
    var gist: String? = null,
    var is_allowed_in_table_question: Int? = null,
    var slug: FieldType? = null,
    var questionTypeGroupId: Int? = null
)