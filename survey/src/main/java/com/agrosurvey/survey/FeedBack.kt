package com.agrosurvey.survey


import android.util.Log
import com.agrosurvey.survey.data.ResponseValue
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.agrosurvey.survey.data.question.QuestionAndType
import com.agrosurvey.survey.data.question.feedback.FeedbackAndSurvey
import com.agrosurvey.survey.data.question.skips.Logic
import com.agrosurvey.survey.data.question.skips.SkipLogic
import com.agrosurvey.survey.di.Scopes
import com.agrosurvey.survey.domain.FeedbackInteractor
import toothpick.Toothpick
import javax.inject.Inject

class FeedBack(
    private val feedback: FeedbackAndSurvey,
    var questionsListener: QuestionListener? = null
) {


    interface QuestionListener {
        fun onNextQuestion(question: QuestionAndResponse?)
    }
    
    @Inject
    lateinit var feedbackInteractor: FeedbackInteractor


    lateinit var surveyQuestions: List<QuestionAndResponse>

    val memo: HashMap<Int, Boolean> = HashMap<Int, Boolean>()

    init {
        Toothpick.inject(this, Toothpick.openScopes(Scopes.APP_SCOPE, Scopes.NETWORK_SCOPE))

    }



    fun questionsCount() = surveyQuestions.size
    fun title() = feedback.survey.title
    fun surveyId() = feedback.survey.id

    suspend fun getQuestions(): List<QuestionAndResponse> {
        surveyQuestions =
            feedbackInteractor.getFeedbackQuestionsFromDB(feedback.feedback.ID.toLong())
        return surveyQuestions
    }


    private var currentQuestionIndex = 0

    fun getFirstQuestion(): QuestionAndResponse? {

        return try {
            surveyQuestions[0]
        } catch (e: Exception) {
            null
        }
    }


    /**
     * Saves a response for a feedback question
     *
     *
     * @param question  the question instance
     * @param value the provided value for que question
     */
    suspend fun saveResponseForQuestion(question: QuestionAndType, providedResponse: Any?) {

        Log.e("DFGT", "Current Index " + currentQuestionIndex + " SIZE " + surveyQuestions.size)


        if (providedResponse != null) {
            surveyQuestions[currentQuestionIndex].response =
                feedbackInteractor.saveResponse(question, feedback.feedback.ID, providedResponse)
        }


        for (assumedQuestionAndResponse in surveyQuestions.subList(
            currentQuestionIndex + 1,
            surveyQuestions.size
        )) {

            var toBeSkipped = false

            for (skipLogic in assumedQuestionAndResponse.question?.question?.questionSkips!!) {

                Log.e("SKIP LOGIC", "SKIPPING QUESTION $skipLogic")

                when (skipLogic) {
                    is SkipLogic.RadioButton -> {

                        val questionToBeEvaluated =
                            surveyQuestions.find { it.question?.question?.id!! == skipLogic.questionId?.toInt() }
                        when(skipLogic.logic){
                            Logic.Equal -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.RadioButton?)?.value?.id == skipLogic.skippedOnOptionId) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                            Logic.GreaterThan -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.RadioButton?)?.value?.id?:0 > skipLogic.skippedOnOptionId?:0) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                            else -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.RadioButton?)?.value?.id == skipLogic.skippedOnOptionId) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                        }


                    }
                    is SkipLogic.ShortText -> {

                        val questionToBeEvaluated =
                            surveyQuestions.find { it.question?.question?.id!! == skipLogic.questionId?.toInt() }
                        when(skipLogic.logic){
                            Logic.Equal -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.ShortText?)?.text == skipLogic.skippedOnValue) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                            else -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.ShortText?)?.text == skipLogic.skippedOnValue) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                        }

                    }
                    is SkipLogic.LongText -> {

                        val questionToBeEvaluated =
                            surveyQuestions.find { it.question?.question?.id!! == skipLogic.questionId?.toInt() }
                        when(skipLogic.logic){
                            Logic.Equal -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.LongText?)?.text == skipLogic.skippedOnValue) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                            else -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.LongText?)?.text == skipLogic.skippedOnValue) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                        }



                    }
                    is SkipLogic.SelectBox -> {

                        val questionToBeEvaluated =
                            surveyQuestions.find { it.question?.question?.id!! == skipLogic.questionId?.toInt() }
                        when(skipLogic.logic){
                            Logic.Equal -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.SelectBox?)?.value?.id == skipLogic.skippedOnOptionId) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                            else -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.SelectBox?)?.value?.id == skipLogic.skippedOnOptionId) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                        }

                    }
                    is SkipLogic.ReferenceDataResponses -> {

                        val questionToBeEvaluated =
                            surveyQuestions.find { it.question?.question?.id!! == skipLogic.questionId?.toInt() }
                        when(skipLogic.logic){
                            Logic.Equal -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.ReferenceDataResponses?)?.id == skipLogic.skippedOnOptionId) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                            else -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.ReferenceDataResponses?)?.id == skipLogic.skippedOnOptionId) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                        }



                    }
                    is SkipLogic.CheckedBox -> {

                        val questionToBeEvaluated =
                            surveyQuestions.find { it.question?.question?.id!! == skipLogic.questionId?.toInt() }
                        when(skipLogic.logic){
                            Logic.Equal -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.SelectBox?)?.value?.id == skipLogic.skippedOnOptionId) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                            else -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.SelectBox?)?.value?.id == skipLogic.skippedOnOptionId) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                        }



                    }
                    is SkipLogic.AutoGeneratedId -> {

                        val questionToBeEvaluated =
                            surveyQuestions.find { it.question?.question?.id!! == skipLogic.questionId?.toInt() }
                        when(skipLogic.logic){
                            Logic.Equal -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.AutoGeneratedId?)?.id == skipLogic.skippedOnId) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                            else -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.AutoGeneratedId?)?.id == skipLogic.skippedOnId) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                        }



                    }
                    is SkipLogic.Integer -> {

                        val questionToBeEvaluated =
                            surveyQuestions.find { it.question?.question?.id!! == skipLogic.questionId?.toInt() }
                        when(skipLogic.logic){
                            Logic.Equal -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.Integer?)?.value == skipLogic.skippedOnValue) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                            Logic.GreaterThan -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.Integer?)?.value == skipLogic.skippedOnValue) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                            else -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.Integer?)?.value == skipLogic.skippedOnValue) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                        }



                    }
                    is SkipLogic.Decimal -> {

                        val questionToBeEvaluated =
                            surveyQuestions.find { it.question?.question?.id!! == skipLogic.questionId?.toInt() }
                        when(skipLogic.logic){
                            Logic.Equal -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.Decimal?)?.value == skipLogic.skippedOnValue) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                            Logic.GreaterThan -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.Decimal?)?.value == skipLogic.skippedOnValue) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                            else -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.Decimal?)?.value == skipLogic.skippedOnValue) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                        }



                    }
                    is SkipLogic.PhoneNumber -> {

                        val questionToBeEvaluated =
                            surveyQuestions.find { it.question?.question?.id!! == skipLogic.questionId?.toInt() }
                        when(skipLogic.logic){
                            Logic.Equal -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.PhoneNumber?)?.number == skipLogic.skippedOnValue) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                            Logic.GreaterThan -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.PhoneNumber?)?.number == skipLogic.skippedOnValue) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                            else -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.PhoneNumber?)?.number == skipLogic.skippedOnValue) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                        }



                    }
                    is SkipLogic.DateTimePicker -> {

                        val questionToBeEvaluated =
                            surveyQuestions.find { it.question?.question?.id!! == skipLogic.questionId?.toInt() }

                        when(skipLogic.logic){
                            Logic.Equal -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.DateTimePicker?)?.dateTime == skipLogic.skippedOnDateTime) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                            Logic.GreaterThan -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.DateTimePicker?)?.dateTime == skipLogic.skippedOnDateTime) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                            else -> {
                                if ((questionToBeEvaluated?.response?.value as ResponseValue.DateTimePicker?)?.dateTime == skipLogic.skippedOnDateTime) {
                                    toBeSkipped = true
                                    break
                                }
                            }
                        }



                    }
                    /*is SkipLogic.DatePicker -> TODO()
                    is SkipLogic.TimePicker -> TODO()
                    is SkipLogic.MonthPicker -> TODO()
                    is SkipLogic.DayOfWeekPicker -> TODO()
                    is SkipLogic.Signature -> TODO()
                    is SkipLogic.Image -> TODO()
                    is SkipLogic.Audio -> TODO()
                    is SkipLogic.PolyGonPlot -> TODO()
                    is SkipLogic.PathLine -> TODO()
                    is SkipLogic.SinglePoint -> TODO()
                    is SkipLogic.OneDimensionalTable -> TODO()
                    is SkipLogic.TwoDimensionalTable -> TODO()
                    is SkipLogic.BarCode -> TODO()
                    is SkipLogic.QRCode -> TODO()
                    is SkipLogic.PaymentButton -> TODO()
                    is SkipLogic.Fingerprint -> TODO()
                    is SkipLogic.Statement -> TODO()
                    is SkipLogic.PdfFile -> TODO()
                    null -> TODO()*/
                    else -> Log.e("SKIP LOGIC", "NULL FOUND")
                }


            }

            if (toBeSkipped.not()) {

                if (currentQuestionIndex != surveyQuestions.size - 1) {

                    if (memo[question.question.id] == null) {
                        currentQuestionIndex++
                        questionsListener?.onNextQuestion(assumedQuestionAndResponse)
                        break
                    }
                } else {
                    questionsListener?.onNextQuestion(null)
                }


            } else {

                if (memo[assumedQuestionAndResponse.question.question.id] == null) {
                    currentQuestionIndex++
                }
            }

        }

        memo[question.question.id!!] = true
        if (currentQuestionIndex == surveyQuestions.size - 1)
            questionsListener?.onNextQuestion(null)


    }


}