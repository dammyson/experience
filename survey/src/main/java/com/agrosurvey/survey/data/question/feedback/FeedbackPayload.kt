package com.agrosurvey.survey.data.question.feedback

import com.agrosurvey.survey.data.question.option.OptionIn
import java.time.OffsetDateTime

class FeedbackPayload (var feedbacks: List<FeedbackIn>)

class FeedbackIn(
    var id: Int? = null,
    var feedback_sync_id: Int? = null,
    var created_at: OffsetDateTime? = null,
    var is_flagged: Int? = null,
    var short_question_feedbacks: List<ShortQuestionResponseIn?>?,
    var long_question_feedbacks: List<LongQuestionResponseIn?>?

)

class ShortQuestionResponseIn(
    var feedback_id: Int? = null,
    var short_feedback: String? = null,
    var option: OptionIn? = null,
    var question_id: Int? = null,
    var question: FeedbackQuestionIn? = null
)

class LongQuestionResponseIn(
    var feedback_id: Int? = null,
    var long_feedback: String? = null,
    var option: OptionIn? = null,
    var question_id: Int? = null,
    var question: FeedbackQuestionIn? = null
)

class FeedbackQuestionIn(
    var id: Int? = null,
    var survey_id: String? = null,
    var question_type_slug: String? = null
)