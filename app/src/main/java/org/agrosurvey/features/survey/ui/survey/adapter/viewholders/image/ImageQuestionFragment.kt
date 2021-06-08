package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.image

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapRegionDecoder
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.agrosurvey.R
import com.agrosurvey.databinding.FragmentImageQuestionBinding
import com.agrosurvey.survey.data.ResponseValue
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.github.dhaval2404.imagepicker.ImagePicker
import com.squareup.picasso.Picasso
import org.agrosurvey.FileUtil
import java.io.File

class ImageQuestionFragment : Fragment() {


    lateinit var binding : FragmentImageQuestionBinding
    var questionAndResponse : QuestionAndResponse? = null

    lateinit var filePathInterface: FilePathInterface

    interface FilePathInterface{
        fun onNewPath(imagePath: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_question, container, false)
        binding.btnPick.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }

        questionAndResponse?.response?.value?.let {
            Picasso.get()
                .load("file://"+(it as ResponseValue.Image).path)
                .into(binding.image)
        }


        return binding.root

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {

                val imageUri = data?.data

                imageUri?.let {imgUri ->
                    if(Build.VERSION.SDK_INT < 28) {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            imgUri
                        )
                        binding.image.setImageBitmap(bitmap)
                        val cw = ContextWrapper(requireActivity().applicationContext)
                        val directory = cw.getDir("questions", Context.MODE_PRIVATE)

                        val imagePath = FileUtil.createDirectoryAndSaveFile(
                            imageToSave = bitmap,
                            directory = directory,
                            fileName = "question_${questionAndResponse?.question?.question?.id}.JPEG",
                            compressFormat = Bitmap.CompressFormat.JPEG

                        )
                        imagePath?.let { filePathInterface.onNewPath(it) }

                    } else {
                        val source = ImageDecoder.createSource(requireActivity().contentResolver, imageUri)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        binding.image.setImageBitmap(bitmap)
                        val cw = ContextWrapper(requireActivity().applicationContext)
                        val directory = cw.getDir("questions", Context.MODE_PRIVATE)

                        val imagePath = FileUtil.createDirectoryAndSaveFile(
                            imageToSave = bitmap,
                            directory = directory,
                            fileName = "question_${questionAndResponse?.question?.question?.id}.JPEG",
                            compressFormat = Bitmap.CompressFormat.JPEG
                        )
                        imagePath?.let { filePathInterface.onNewPath(it) }
                    }
                }



            }

            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }



    companion object {
        @JvmStatic
        fun newInstance(questionAndResponse: QuestionAndResponse,
                        filePathInterface: FilePathInterface
        ) = ImageQuestionFragment().apply {
            this.questionAndResponse = questionAndResponse
            this.filePathInterface = filePathInterface
        }
    }
}