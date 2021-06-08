package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.signature

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import com.squareup.picasso.Picasso
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.room.util.FileUtil
import com.agrosurvey.databinding.ItemShortTextBinding
import com.agrosurvey.databinding.ItemSignatureBinding
import com.agrosurvey.survey.data.ResponseValue
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.agrosurvey.survey.data.question.QuestionAndType
import com.github.gcacace.signaturepad.views.SignaturePad
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.BaseViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.QuestionInterface

class SignatureViewHolder(val context: Context, private val binding: ItemSignatureBinding):
    BaseViewHolder(binding.root) {
    private val viewModel =  SignatureViewModel()

    lateinit var questionInterface: QuestionInterface

    lateinit var filePath: String

    override fun bind(questionAndResponse: QuestionAndResponse){
        viewModel.bind(questionAndResponse)
        binding.viewModel = viewModel
        binding.lifecycleOwner = context as LifecycleOwner


        filePath = "signature_${questionAndResponse.response?.questionId}_${questionAndResponse.response?.feedBackId}.JPEG"

        viewModel.isMandatory.observe(context, Observer {
            if(it!!){
                binding.isMandatory.visibility = View.VISIBLE
            } else {
                binding.isMandatory.visibility =  View.GONE
                questionInterface.onChange(questionAndResponse.question!!, null)
            }

        })

        binding.btnReload.setOnClickListener {
            binding.signaturePad.clear()
        }

        binding.btnReset.setOnClickListener {
            binding.signaturePadBlock.visibility = View.VISIBLE
            binding.previewImageBlock.visibility = View.GONE
        }

        viewModel.userResponse.observe(context, Observer { path ->
            if(path.isNullOrBlank().not()){

                Picasso.get()
                    .load("file://"+(questionAndResponse?.response?.value as ResponseValue.Signature).path)
                    .into(binding.previewImage)

                binding.signaturePadBlock.visibility = View.GONE
                binding.previewImageBlock.visibility = View.VISIBLE
            }
        })
        binding.signaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener{
            override fun onStartSigning() {

            }

            override fun onClear() {
            }

            override fun onSigned() {

                val cw = ContextWrapper(context.applicationContext)
                val directory = cw.getDir("questions", Context.MODE_PRIVATE)
                val imagePath = org.agrosurvey.FileUtil.createDirectoryAndSaveFile(
                    imageToSave = binding.signaturePad.signatureBitmap,
                    directory = directory,
                    fileName = filePath,
                    compressFormat = Bitmap.CompressFormat.JPEG
                )

                questionInterface.onChange(questionAndResponse.question!!, imagePath)


            }

        })

        binding.executePendingBindings()
    }



}
