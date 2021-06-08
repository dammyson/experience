package org.agrosurvey.features.survey.ui.survey.adapter.viewholders.phone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.agrosurvey.R
import com.agrosurvey.databinding.ItemPhoneBinding
import com.agrosurvey.survey.data.question.QuestionAndResponse
import com.agrosurvey.survey.data.question.QuestionAndType
import org.agrosurvey.data.Country
import org.agrosurvey.features.survey.ui.survey.adapter.viewholders.BaseViewHolder

class PhoneNumberViewHolder(val context: Context, private val binding: ItemPhoneBinding):
    BaseViewHolder(binding.root) {
    private val viewModel =
        PhoneNumberViewModel()
    private  var phone_number: String=""
    private var code: String=""

    lateinit var questionInterface: QuestionInterface

    interface QuestionInterface {
        fun onChange(question: QuestionAndType, response : String?)
    }

    override fun bind(questionAndResponse: QuestionAndResponse){
        viewModel.bind(questionAndResponse)
        binding.viewModel = viewModel
        binding.lifecycleOwner = context as LifecycleOwner

        val country = arrayListOf<Country>()
        country.add(Country( "bn", "+225"))
        country.add(Country( "nj", "+227"))
        country.add(Country( "iv", "+235"))

        val customDropDownAdapter =
            CustomDropDownAdapter(
                context,
                country
            )

        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                code = country.get(p2).value

            }
        }
        binding.spinner.adapter = customDropDownAdapter



        binding.subtitle.doOnTextChanged { enteredText, start, before, count ->
            questionInterface.onChange(questionAndResponse.question!!, code+enteredText.toString())
        }

        viewModel.isMandatory.observe(context, Observer {
            if(it!!){
                binding.isMandatory.visibility = View.VISIBLE
            } else {
                binding.isMandatory.visibility =  View.GONE
                questionInterface.onChange(questionAndResponse.question!!, null)
            }

        })

        viewModel.userResponse.observe(context, Observer { phoneNumber ->
            if(phoneNumber.isNullOrBlank().not()){
                code = phoneNumber?.substring(0,4)!!
                binding.subtitle.setText(phoneNumber?.let { it.substring(4, it.length)} )
                binding.spinner.setSelection(customDropDownAdapter.getPositionForItem(
                    code
                ))
            }
        })

        binding.executePendingBindings()
    }



    class CustomDropDownAdapter(val context: Context, var dataSource: List<Country>) : BaseAdapter() {

        private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val view: View
            val vh: ItemHolder
            if (convertView == null) {
                view = inflater.inflate(R.layout.country_item, parent, false)
                vh =
                    ItemHolder(
                        view
                    )
                view?.tag = vh
            } else {
                view = convertView
                vh = view.tag as ItemHolder
            }
            vh.label.text = dataSource.get(position).value
            val id = context.resources.getIdentifier(dataSource.get(position).url, "drawable", context.packageName)
            vh.img.setBackgroundResource(id)


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

        fun getPositionForItem(code: String): Int {
            return dataSource.indexOfFirst { it.value == code }
        }

        private class ItemHolder(row: View?) {
            val label: TextView
            val img: ImageView
            init {
                label = row?.findViewById(R.id.text) as TextView
                img = row?.findViewById(R.id.img) as ImageView
            }
        }

    }


}
