package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.document

import android.R.attr.path
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns.DISPLAY_NAME
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.loader.content.CursorLoader
import com.agrosurvey.R
import com.agrosurvey.databinding.FragmentDocumentQuestionBinding
import java.io.File
import java.net.URISyntaxException


class DocumentFragment : Fragment() {


    val FILE_SELECT_CODE = 1234
    lateinit var binding : FragmentDocumentQuestionBinding
    var questionID : Int = 0

    lateinit var filePathInterface: FilePathInterface

    interface FilePathInterface{
        fun onNewPath(imagePath: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_document_question, container, false)

        binding.btnPick.setOnClickListener {
            showFileChooser()
        }

        return binding.root

    }

    private fun showFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        try {
            startActivityForResult(
                Intent.createChooser(intent, "Select a File to attach"),
                FILE_SELECT_CODE
            )
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                requireActivity(), "Please install a File Manager.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FILE_SELECT_CODE -> if (resultCode === RESULT_OK) {


                binding.fileName.text = data!!.data?.path

                val uri = data!!.data
                val uriString = data!!.data.toString()
                var file = File(uriString)

                var displayName: String? = null

                if (uriString.startsWith("content://")) {
                    var cursor: Cursor? = null
                    try {
                        cursor =
                            requireActivity().contentResolver.query(uri!!, null, null, null, null)
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName =
                                cursor.getString(cursor.getColumnIndex(DISPLAY_NAME))
                        }
                    } finally {
                        cursor!!.close()
                    }
                } else if (uriString.startsWith("file://")) {
                    displayName = file.absolutePath
                    Log.e("FILLE", "FFFFFFFFFFFFFF  "+displayName)

                }

                val fsb = getPathD(uri!!)
                Log.e("FILLE", "FSVVVVV  "+fsb)

                Log.e("FILLE", "DISPLA  "+displayName)


                binding.fileName.text = displayName

                /*val uri: Uri? = data!!.data
                var fileName = getPath(requireActivity(),
                    uri!!)!!
                val file = File(fileName)
                fileName = file.name

                val cw = ContextWrapper(requireActivity().applicationContext)
                val directory = cw.getDir("questions", Context.MODE_PRIVATE)

                val imagePath = FileUtil.savefile(
                    directory = directory,
                    sourceFilename = fileName,
                    fileName = "question_${questionID}.pdf"
                )
                imagePath?.let { filePathInterface.onNewPath("question_${questionID}.pdf") }*/

            }else{

            }
        }
    }


    fun getPathD(uri: Uri): String {
        val projection =
            arrayOf(MediaStore.MediaColumns.DATA)

        var path : String = ""
        val cr: ContentResolver = requireActivity().application
                .getContentResolver()
        val metaCursor = cr.query(uri, projection, null, null, null)
        if (metaCursor != null) {
            try {
                if (metaCursor.moveToFirst()) {
                    path = metaCursor.getString(0)
                }
            } finally {
                metaCursor.close()
            }
        }
        return path
    }


    private fun getRealPathFromURI(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(requireActivity(), contentUri, proj, null, null, null)
        val cursor: Cursor = loader.loadInBackground()!!
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result = cursor.getString(column_index)
        cursor.close()
        return result
    }

    fun getRealPathFromURI(
        context: Context,
        contentUri: Uri?
    ): String? {
        var cursor: Cursor? = null
        return try {
            val proj =
                arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            val column_index: Int = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)!!
            cursor?.moveToFirst()
            cursor?.getString(column_index)
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }

    @Throws(URISyntaxException::class)
    fun getPath(context: Context, uri: Uri): String? {
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf("_data")
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver.query(uri, projection, null, null, null)
                val column_index = cursor!!.getColumnIndexOrThrow("_data")
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index)
                }
            } catch (e: Exception) {
                // Eat it
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }


    companion object {
        @JvmStatic
        fun newInstance(questionID: Int,
                        filePathInterface: FilePathInterface
        ) = DocumentFragment()
            .apply {
            this.questionID = questionID
            this.filePathInterface = filePathInterface
        }
    }
}