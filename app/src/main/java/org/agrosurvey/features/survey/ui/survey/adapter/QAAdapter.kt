package org.agrosurvey.features.survey.ui.survey.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.agrosurvey.R
import com.agrosurvey.databinding.*
import com.agrosurvey.survey.data.question.FieldType
import com.agrosurvey.survey.data.mappers.toInt
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.agrosurvey.survey.data.question.QuestionAndType
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.*
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.audio.AudioViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.date.DateViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.datetime.DateTimeViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.decimal.DecimalNumberViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.document.DocumentViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.image.ImageViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.integer.IntegerNumberViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.longtext.LongTextViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.pathline.PathLineMapViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.phone.PhoneNumberViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.polygon.PolygonMapViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.radiobutton.RadioButtonViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.selectbox.SelectBoxViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.shorttext.ShortTextViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.signature.SignatureViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.singlepoint.SinglePointViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.statement.StatementViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.time.TimeViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.unknown.UnknownTypeViewHolder


class QAAdapter(val context: Context) : RecyclerView.Adapter<BaseViewHolder>(){


    lateinit var dataCollection: List<QuestionAndResponse>
    lateinit var questionListener: QuestionInterface


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        when(viewType){
            1 -> {
                val databinding: ItemShortTextBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.item_short_text, parent, false
                    )
                return ShortTextViewHolder(
                    context,
                    databinding
                ).apply {
                    this.questionInterface = object : QuestionInterface{
                        override fun onChange(question: QuestionAndType, response: Any?) {
                            questionListener.onChange(question, response)
                        }

                    }
                }
            }
            2 -> {
                val databinding: ItemLongTextBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.item_long_text, parent, false
                    )
                return LongTextViewHolder(
                    context,
                    databinding
                ).apply {
                    this.questionInterface = object : QuestionInterface{
                        override fun onChange(question: QuestionAndType, response : Any?) {
                            questionListener.onChange(question, response)
                        }

                    }
                }
            }
            FieldType.SelectBox.toInt() -> {
                val databinding: ItemSelectBoxBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.item_select_box, parent, false
                    )
                return SelectBoxViewHolder(
                    context,
                    databinding
                ).apply {
                    this.questionInterface = object : QuestionInterface{
                        override fun onChange(question: QuestionAndType, response : Any?) {
                            questionListener.onChange(question, response)
                        }

                    }
                }
            }

            4 -> {
                val databinding: ItemRadioButtonBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.item_radio_button, parent, false
                    )
                return RadioButtonViewHolder(context, databinding).apply {
                    this.questionInterface = object : QuestionInterface{
                        override fun onChange(question: QuestionAndType, response : Any?) {
                            questionListener.onChange(question, response)
                        }

                    }
                }
            }
            8 -> {
                val databinding: ItemIntegerTextBinding =

                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.item_integer_text, parent, false
                    )
                return IntegerNumberViewHolder(
                    context,
                    databinding
                ).apply {
                    this.questionInterface = object : IntegerNumberViewHolder.QuestionInterface{
                        override fun onChange(question: QuestionAndType, response: String?) {
                            questionListener.onChange(question, response)
                        }

                    }
                }
            }

            9 -> {
                val databinding: ItemDecimalTextBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.item_decimal_text, parent, false
                    )
                return DecimalNumberViewHolder(
                    context,
                    databinding
                ).apply {
                    this.questionInterface = object : DecimalNumberViewHolder.QuestionInterface{
                        override fun onChange(question: QuestionAndType, response: String?) {
                            questionListener.onChange(question, response)
                        }

                    }
                }
            }

            19 -> {
                val databinding: ItemPolygonMapBinding =
                        DataBindingUtil.inflate(
                                LayoutInflater.from(parent.context), R.layout.item_polygon_map, parent, false
                        )
                return PolygonMapViewHolder(context, databinding).apply {
                    this.questionInterface = object : QuestionInterface{
                        override fun onChange(question: QuestionAndType, response : Any?) {
                            questionListener.onChange(question, response)
                        }

                    }
                }
            }
            /*20 -> {
                val databinding: ItemMaplineBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.item_mapline, parent, false
                    )
                return MaplineAnswerViewHolder(
                    context,
                    databinding
                ).apply {
                    this.questionInterface = object : QuestionInterface{
                        override fun onChange(question: QuestionAndType, response : Any?) {
                            questionListener.onChange(question, response)
                        }
                    }
                }
            }
            21 -> {
                val databinding: ItemMapBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.item_map, parent, false
                    )
                return MapAnswerViewHolder(
                    context,
                    databinding
                ).apply {
                    this.questionInterface = object : QuestionInterface{
                        override fun onChange(question: QuestionAndType, response : Any?) {
                            questionListener.onChange(question, response)
                        }

                    }
                }
            }*/
            FieldType.Signature.toInt() -> {
                val databinding: ItemSignatureBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.item_signature, parent, false
                    )
                return SignatureViewHolder(context, databinding).apply {
                    this.questionInterface = object : QuestionInterface{
                        override fun onChange(question: QuestionAndType, response : Any?) {
                            questionListener.onChange(question, response)
                        }

                    }
                }
            }

            FieldType.Image.toInt() -> {
                val databinding: ItemImageBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.item_image, parent, false
                    )
                return ImageViewHolder(context, databinding).apply {
                    this.questionInterface = object : QuestionInterface{
                        override fun onChange(question: QuestionAndType, response : Any?) {
                            questionListener.onChange(question, response)
                        }

                    }
                }
            }

            10 -> {
                val databinding: ItemPhoneBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.item_phone, parent, false
                    )
                return PhoneNumberViewHolder(
                    context,
                    databinding
                ).apply {
                    this.questionInterface = object : PhoneNumberViewHolder.QuestionInterface{
                        override fun onChange(question: QuestionAndType, response: String?) {
                            questionListener.onChange(question, response)
                        }

                    }
                }
            }

            FieldType.Audio.toInt() -> {
                val databinding: ItemAudioBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.item_audio, parent, false
                    )
                return AudioViewHolder(context, databinding).apply {
                    this.questionInterface = object : QuestionInterface{
                        override fun onChange(question: QuestionAndType, response : Any?) {
                            questionListener.onChange(question, response)
                        }

                    }
                }
            }


            FieldType.DateTimePicker.toInt() -> {
                val databinding: ItemDatetimeBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.item_datetime, parent, false
                    )
                return DateTimeViewHolder(context, databinding).apply {
                    this.questionInterface = object : QuestionInterface{
                        override fun onChange(question: QuestionAndType, response : Any?) {
                            questionListener.onChange(question, response)
                        }

                    }
                }
            }


            FieldType.TimePicker.toInt() -> {
                val databinding: ItemTimeBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.item_time, parent, false
                    )
                return TimeViewHolder(context, databinding).apply {
                    this.questionInterface = object : QuestionInterface{
                        override fun onChange(question: QuestionAndType, response : Any?) {
                            questionListener.onChange(question, response)
                        }

                    }
                }
            }

            FieldType.PdfFile.toInt() -> {
                val databinding: ItemDocumentBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.item_document, parent, false
                    )
                return DocumentViewHolder(context, databinding).apply {
                    this.questionInterface = object : QuestionInterface{
                        override fun onChange(question: QuestionAndType, response : Any?) {
                            questionListener.onChange(question, response)
                        }

                    }
                }
            }

            FieldType.Statement.toInt() -> {
                val databinding: ItemStatementBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.item_statement, parent, false
                    )
                return StatementViewHolder(context, databinding).apply {
                    this.questionInterface = object : QuestionInterface{
                        override fun onChange(question: QuestionAndType, response : Any?) {
                            questionListener.onChange(question, response)
                        }

                    }
                }
            }

            FieldType.DatePicker.toInt() -> {
                val databinding: ItemDateBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.item_date, parent, false
                    )
                return DateViewHolder(context, databinding).apply {
                    this.questionInterface = object : QuestionInterface{
                        override fun onChange(question: QuestionAndType, response : Any?) {
                            questionListener.onChange(question, response)
                        }

                    }
                }
            }

            FieldType.SinglePoint.toInt() -> {
                val databinding: ItemSinglePointBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.item_single_point, parent, false
                    )
                return SinglePointViewHolder(context, databinding).apply {
                    this.questionInterface = object : QuestionInterface{
                        override fun onChange(question: QuestionAndType, response : Any?) {
                            questionListener.onChange(question, response)
                        }

                    }
                }
            }

            FieldType.PathLine.toInt() -> {
                val databinding: ItemPathLineBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.item_path_line, parent, false
                    )
                return PathLineMapViewHolder(context, databinding).apply {
                    this.questionInterface = object : QuestionInterface{
                        override fun onChange(question: QuestionAndType, response : Any?) {
                            questionListener.onChange(question, response)
                        }

                    }
                }
            }



            else -> {
                val databinding: ItemUnknownBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.item_unknown, parent, false
                    )

                return UnknownTypeViewHolder(
                    context,
                    databinding
                ).apply {
                    this.questionInterface = object : QuestionInterface{
                        override fun onChange(question: QuestionAndType, response : Any?) {
                            Log.e("SSS", "ADAPTER SEND SAVING REQUEST")
                            questionListener.onChange(question, response)
                        }

                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if(::dataCollection.isInitialized) dataCollection.size else 0
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(dataCollection[position])
    }

    override fun getItemViewType(position: Int): Int {
        return dataCollection[position].question?.type?.slug.toInt()
    }

    fun updateData(it: List<QuestionAndResponse>?) {
        dataCollection = it!!
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        (dataCollection as ArrayList).removeAt(position)
        notifyItemChanged(position)
    }

    fun addItem(position: Int, item : QuestionAndResponse) {
        (dataCollection as ArrayList).add(position, item)
        notifyDataSetChanged()
    }

    fun addItem(item : QuestionAndResponse) {
        (dataCollection as ArrayList).add(item)
        notifyItemChanged(dataCollection.size-1)
    }

}
