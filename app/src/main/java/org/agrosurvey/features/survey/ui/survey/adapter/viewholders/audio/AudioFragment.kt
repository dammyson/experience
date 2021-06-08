package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.audio

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.agrosurvey.R
import com.agrosurvey.databinding.FragmentAudioQuestionBinding
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import java.io.IOException
import java.lang.Exception


class AudioFragment : Fragment() {


    lateinit var binding : FragmentAudioQuestionBinding
    var questionAndResponse : QuestionAndResponse? = null

    lateinit var filePathInterface: FilePathInterface


    private var fileName: String = ""

    private var recorder: MediaRecorder? = null

    private var player: MediaPlayer? = null

    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    var mIsPlaying = false
    var isRecording = true

    val handler = Handler(Looper.getMainLooper())

    interface FilePathInterface{
        fun onNewPath(imagePath: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_audio_question, container, false)
        resetState()

        binding.btnStart.setOnClickListener {
            startRecording()
            setViewRecording()
            isRecording = true
        }

        binding.progressBar.setUserSeekAble(false)
        binding.progressBar.onSeekChangeListener = object  : OnSeekChangeListener{
            //TODO implement seeking
            override fun onSeeking(seekParams: SeekParams?) {
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {
            }

        }

        binding.btnPlay.setOnClickListener{
            if(mIsPlaying){
                setViewPlayingReady()
                pausePlaying()
                mIsPlaying = false

            }else{
                setViewPlaying()
                startPlaying()
                mIsPlaying = true
            }

        }


        binding.btnDelete.setOnClickListener{
            if(isRecording){
                stopRecording()
                setViewPlayingReady()
                isRecording = false
            }else{
                resetState()
            }
        }

        ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        fileName =
            "${requireActivity().externalCacheDir?.absolutePath}/audiorecord_${questionAndResponse?.question?.question?.id}.3gp"

        if(questionAndResponse?.response != null){
            setViewPlayingReady()
        }

        return binding.root

    }

    fun resetState(){
        binding.timingBlock.visibility = View.INVISIBLE
        binding.recordingViewer.visibility = View.GONE
        binding.btnDelete.visibility = View.GONE
        binding.btnPlay.visibility = View.GONE
        binding.btnStart.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.btnPlay.setImageResource(R.drawable.ic_play)
        binding.progressBar.setProgress(0f)
        stopPlaying()

    }

    fun setViewRecording(){
        binding.timingBlock.visibility = View.GONE
        binding.recordingViewer.visibility = View.VISIBLE
        binding.btnDelete.visibility = View.VISIBLE
        binding.btnPlay.visibility = View.GONE
        binding.btnStart.visibility = View.GONE
        binding.btnDelete.setImageResource(R.drawable.ic_mic)

    }

    fun setViewPlaying(){
        binding.timingBlock.visibility = View.VISIBLE
        binding.recordingViewer.visibility = View.GONE
        binding.btnDelete.visibility = View.VISIBLE
        binding.btnPlay.visibility = View.VISIBLE
        binding.btnStart.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.btnDelete.setImageResource(R.drawable.ic_cancel)
        binding.btnPlay.setImageResource(R.drawable.ic_pause)

    }

    fun setViewPlayingReady(){
        binding.timingBlock.visibility = View.VISIBLE
        binding.recordingViewer.visibility = View.GONE
        binding.btnDelete.visibility = View.VISIBLE
        binding.btnPlay.visibility = View.VISIBLE
        binding.btnStart.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.btnDelete.setImageResource(R.drawable.ic_cancel)
        binding.btnPlay.setImageResource(R.drawable.ic_play)

        if(player == null){
            player = MediaPlayer().apply {
                try {
                    setDataSource(fileName)
                    prepare()
                    setOnCompletionListener {

                        it.seekTo(0)
                        updateTimer()
                        binding.progressBar.setProgress(0f)
                        binding.btnPlay.setImageResource(R.drawable.ic_play)
                        handler.removeCallbacksAndMessages(null)
                        mIsPlaying = !mIsPlaying

                    }
                    binding.progressBar.max = this.duration.toFloat()
                    binding.progressBar.setProgress(0f)

                } catch (e: IOException) {
                    Log.e(LOG_TAG, "prepare() failed " + e.stackTraceToString())
                }
            }


            updateTimer()

        }



    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == Companion.REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted)
            Toast.makeText(requireActivity(), "PERMISSION DENIED", Toast.LENGTH_SHORT).show()
    }


    private fun startPlaying() {
        if(player != null){
            player?.start()
            handler.postDelayed(updateSeekBar, 15)
        }


    }

    private val updateSeekBar: Runnable = object : Runnable {
        override fun run() {
            updateTimer()
            handler.postDelayed(this, 15)
        }
    }

    fun updateTimer(){
        val totalDuration: Int? = player?.duration
        val currentDuration: Int? = player?.currentPosition

        binding.totatTime.text = "${milliSecondsToTimer(totalDuration?.toLong()!!)}"
        binding.elapsedTime.text = "${milliSecondsToTimer(currentDuration!!.toLong())}"
        binding.progressBar.setProgress(currentDuration.toFloat())
    }

    fun milliSecondsToTimer(milliseconds: Long): String? {
        var finalTimerString = ""
        var secondsString = ""

        // Convert total duration into time
        val hours = (milliseconds / (1000 * 60 * 60)).toInt()
        val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
        val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
        // Add hours if there
        if (hours > 0) {
            finalTimerString = "$hours:"
        }

        // Prepending 0 to seconds if it is one digit
        secondsString = if (seconds < 10) {
            "0$seconds"
        } else {
            "" + seconds
        }
        finalTimerString = "$finalTimerString$minutes:$secondsString"

        // return  string
        return finalTimerString
    }

    private fun stopPlaying() {
        player?.release()
        player = null
        handler.removeCallbacks(updateSeekBar)
        mIsPlaying = false
    }

    private fun pausePlaying(){
        player?.pause()
    }

    private fun startRecording() {

        try {
            recorder = MediaRecorder().apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                    setOutputFile(fileName)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

                    try {
                        prepare()
                    } catch (e: IOException) {
                        Log.e(LOG_TAG, "prepare() failed")
                    }

                    start()
                }
        }catch (e: Exception){
            Toast.makeText(requireActivity(), "Error recording", Toast.LENGTH_LONG).show()
        }

    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        filePathInterface.onNewPath(fileName)
    }


    override fun onStop() {
        super.onStop()
        recorder?.release()
        recorder = null
        player?.release()
        player = null
    }





    companion object {
        @JvmStatic
        fun newInstance(questionAndResponse: QuestionAndResponse,
                        filePathInterface: FilePathInterface
        ) = AudioFragment()
            .apply {
            this.questionAndResponse = questionAndResponse
            this.filePathInterface = filePathInterface
        }

        private const val LOG_TAG = "AudioRecordTest"
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }
}