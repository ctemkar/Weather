package com.ctemkar.weather.ui.future

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ctemkar.weather.R
import kotlinx.android.synthetic.main.weather_item_layout.view.*
import model.WeatherInfo
import kotlin.math.roundToInt

class FutureAdapter(private val users: ArrayList<WeatherInfo>) : RecyclerView.Adapter<FutureAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: WeatherInfo) {
            itemView.apply {
                tvDay.text = user.applicable_date.toString()
                tvWeatherStateName.text = user.weather_state_name.toString()
                tvPredicatablility.text =  resources.getString(R.string.predictability, user.predictability.toString(), "%")
                tvTempMax.text = user.max_tempF.toString()
                tvTempMin.text = user.min_tempF.toString()
//                    user.predictability.toString() + "%"

                /*Glide.with(imageViewAvatar.context)
                    .load(user.avatar)
                    .into(imageViewAvatar)

                 */
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_item_layout, parent, false))

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(users[position])
    }

    fun addWeatherByDates(weatherByDate: List<WeatherInfo>) {
        this.users.apply {
            clear()
            // We just need one value per date, can be averaged or can just take the top value
            // for brevity, choosing top value
            var currentDate = ""
            var summarizedWeather : ArrayList<WeatherInfo> = ArrayList<WeatherInfo>()

            for (weather in weatherByDate) {
                if(currentDate != weather.applicable_date) {
                    currentDate = weather.applicable_date
                    weather.max_tempF = ((weather.max_temp * 9/5.0) + 32).roundToInt()
                    weather.min_tempF = ((weather.min_temp * 9/5.0) + 32).roundToInt()
                    summarizedWeather.add(weather)
                }
            }


            addAll(summarizedWeather)
            // addAll(weatherByDate)
        }

    }
}