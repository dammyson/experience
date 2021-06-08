package org.agrosurvey.features.survey.ui.survey

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.agrosurvey.R
import com.agrosurvey.databinding.ActivityFeedbackDetailBinding
import com.agrosurvey.databinding.ActivitySurveyBindingImpl
import com.agrosurvey.survey.FeedBack
import com.agrosurvey.survey.data.question.QuestionAndType
import com.agrosurvey.survey.data.question.feedback.FeedbackAndSurvey
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.android.synthetic.main.activity_survey.*
import kotlinx.android.synthetic.main.activity_survey.close
import kotlinx.android.synthetic.main.custom_dialog_fragment.view.*
import org.agrosurvey.features.survey.ui.survey.adapter.FeedbackDetailVMFactory
import org.agrosurvey.features.survey.ui.survey.adapter.QAAdapter
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.QuestionInterface
import org.agrosurvey.features.survey.ui.survey.viewitems.Sections
import java.io.File


class SurveyActivity : AppCompatActivity() {
    private lateinit var viewModel: FeedbackDetailViewModel
    lateinit var binding: ActivitySurveyBindingImpl
    lateinit var qaAdapter: QAAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        var feedBackData   = intent.getSerializableExtra("feedback") as FeedbackAndSurvey

        val feedBackDetail = FeedBack(feedBackData)
        val sections = arrayListOf<Sections>()

        for (i in 0..10){
            sections.add(Sections("S"+i))
        }

        viewModel = ViewModelProvider(this,
            FeedbackDetailVMFactory(feedBackDetail)
        )
            .get(FeedbackDetailViewModel::class.java)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_survey)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.close.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_fragment, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
            val  mAlertDialog = mBuilder.show()
            mDialogView.btn_discard.setOnClickListener {
                finish();
                mAlertDialog.dismiss()

            }
            mDialogView.btn_draft.setOnClickListener {
                finish();
                mAlertDialog.dismiss()
            }
        }

        binding.next.setOnClickListener{
            viewModel.saveResponse()
            Log.e("ACT----------", "NEW QUESTION ")
            //binding.viewPager.currentItem++

        }

        initPager()
        initObservables()
    }

    private fun initObservables() {
        /*viewModel.qaDatabaseList.observe(this, Observer{
            qaAdapter.updateData(it)
        })*/
        qaAdapter.updateData(ArrayList())
        viewModel.newQuestion.observe(this, Observer{ question ->


            question?.let {
                    it -> qaAdapter.addItem(it)
            }


        })

        viewModel.lastQuestionAnswered.observe(this, Observer{ question ->

            //(binding.next as Button).isEnabled = true

        })

        viewModel.isEndOfList.observe(this, Observer {
            if(it){
                binding.next.apply{
                    text = "DONE"
                    setOnClickListener{
                        finish()
                    }
                }
            }
        })

        viewModel.message.observe(this, Observer {
            Log.e("ACT----------", "CHANGING ITEM")
            binding.viewPager.currentItem++
            //(binding.next as Button).isEnabled = false

        })
    }

    private fun initPager() {
        val viewPager = binding.viewPager
        viewPager.setUserInputEnabled(false)
        qaAdapter= QAAdapter(this).apply {
            questionListener = object : QuestionInterface {
                override fun onChange(question: QuestionAndType, response: Any?) {
                    Log.e("SSS", "ACTIVITY SEND SAVING REQUEST")
                    viewModel.keepNewResponse(question, response)
                }
            }
        }



        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.adapter = qaAdapter
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }




}