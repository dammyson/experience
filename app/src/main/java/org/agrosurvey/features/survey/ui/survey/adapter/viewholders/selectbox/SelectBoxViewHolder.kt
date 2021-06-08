package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.selectbox

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.view.get
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.agrosurvey.R
import com.agrosurvey.databinding.ItemSelectBoxBinding
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.agrosurvey.survey.data.question.QuestionAndType
import com.agrosurvey.survey.data.question.option.Option
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.BaseViewHolder
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.QuestionInterface

class SelectBoxViewHolder(val context: Context, private val binding: ItemSelectBoxBinding):
    BaseViewHolder(binding.root) {
    private val viewModel =
        SelectBoxViewModel()

    lateinit var questionInterface: QuestionInterface


    override fun bind(questionAndResponse: QuestionAndResponse){
        viewModel.bind(questionAndResponse)
        binding.viewModel = viewModel
        binding.lifecycleOwner = context as LifecycleOwner


        viewModel.isMandatory.observe(context, Observer {
            if(it!!){
                binding.isMandatory.visibility = View.VISIBLE
            } else {
                binding.isMandatory.visibility =  View.GONE
                questionInterface.onChange(questionAndResponse.question!!, null)
            }

        })




        val customDropDownAdapter =
            CustomDropDownAdapter(
                context,
                questionAndResponse.question?.question?.options as List<Option>
            )

        binding.spinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
               questionInterface.onChange(questionAndResponse.question!!, customDropDownAdapter.getItem(p2)!!)
            }
        }
        binding.spinner.adapter = customDropDownAdapter

        viewModel.userResponse.observe(context, Observer { option ->
            option?.let {
                binding.spinner.setSelection(
                    customDropDownAdapter.getPositionForItem(option.id!!)
                )

            }
        })

        binding.executePendingBindings()
    }


    class CustomDropDownAdapter(val context: Context, var dataSource: List<Option>) : BaseAdapter() {

        private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val view: View
            val vh: ItemHolder
            if (convertView == null) {
                view = inflater.inflate(R.layout.custom_spinner_item, parent, false)
                vh =
                    ItemHolder(
                        view
                    )
                view?.tag = vh
            } else {
                view = convertView
                vh = view.tag as ItemHolder
            }
            vh.label.text = dataSource.get(position).text



            return view
        }

        override fun getItem(position: Int): Any? {
            return dataSource[position];
        }

        override fun getCount(): Int {
            return dataSource.size;
        }

        override fun getItemId(position: Int): Long {
            return position.toLong();
        }

        fun getPositionForItem(optionId: Int): Int {
            return dataSource.indexOfFirst { it.id == optionId }
        }

        private class ItemHolder(row: View?) {
            val label: TextView


            init {
                label = row?.findViewById(R.id.text) as TextView

            }
        }

    }
}

