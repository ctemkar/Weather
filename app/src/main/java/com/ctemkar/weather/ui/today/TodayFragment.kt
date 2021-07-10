package com.ctemkar.weather.ui.today

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.*
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ctemkar.weather.R
import com.ctemkar.weather.ViewModels.LocationInfoViewModelFactory
import com.ctemkar.weather.ViewModels.SharedViewModel
import com.ctemkar.weather.ViewModels.WeatherViewModel
import com.ctemkar.weather.databinding.FragmentTodayBinding
import com.ctemkar.weather.model.WeatherInfo
import com.ctemkar.weather.network.ApiHelper
import com.ctemkar.weather.network.RetrofitBuilder
import com.ctemkar.weather.utils.Constant
import com.ctemkar.weather.utils.DatePickerFragment
import com.ctemkar.weather.utils.Status
import com.soywiz.klock.DateTime
import com.soywiz.klock.days
import kotlinx.android.synthetic.main.weather_card.view.*
import timber.log.Timber


class TodayFragment : Fragment() {
    private var _binding: FragmentTodayBinding? = null
    private var dateToShow: DateTime = DateTime.now() + 1.days
    // private val TAG: String = "TodayFragment"
    private lateinit var viewModel: WeatherViewModel
    private var woeId = -1
    private val model: SharedViewModel by activityViewModels()
    private val binding get() = _binding!!
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.weather_main_frag_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.weather_on_date -> {

                ViewModelProviders.of(requireActivity())[SharedViewModel::class.java]
                    .selectedDate
                    .observe(requireActivity(), {
                        dateToShow = it
                        setupDateRangeObserver(
                            woeId,
                            it,
                            Constant.defaultWeatherDays
                        )
                    })

                val newFragment = DatePickerFragment()
                activity?.supportFragmentManager?.let { newFragment.show(it, "datePicker") }
                // activity?.supportFragmentManager?.let { TimePickerFragment().show(it, "timePicker") }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    var location: Location = Location("")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // homeViewModel = ViewModelProviders.of(this).get(TodayViewModel::class.java)
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        val view = binding.root
        setupViewModel()
       // val root = inflater.inflate(R.layout.fragment_today, container, false)
        model.currentLocation.observe(viewLifecycleOwner, { item ->
            location = model.currentLocation.value!!
            Timber.d("lat: $(location.latitude), lon: $(location.longitude)")
            setupObservers()
        })
        setHasOptionsMenu(true)
        //return root
        return view
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            LocationInfoViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(WeatherViewModel::class.java)
    }


    private fun setupObservers() {
        val sLatLong = if (location.latitude == 0.0)
            "40.793896,-73.940711" else location.latitude.toString() + "," + location.longitude.toString()
        binding.progressBarTodaysWeather.visibility = View.VISIBLE
        viewModel.getLocationInfo(sLatLong).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        initWeatherCardContainer()
                        binding.progressBarTodaysWeather.visibility = View.GONE
                        if (resource.data != null) {
                            binding.textDay.text = resource.data.title
                            woeId = resource.data.woeid
                            setupCurrentWeatherObserver(woeId)
                            setupDateRangeObserver(
                                woeId,
                                dateToShow,
                                Constant.defaultWeatherDays
                            )
                        } else
                            binding.textDay.text = getString(R.string.cantGetWeather)
                    }

                    Status.ERROR -> {
                        binding.progressBarTodaysWeather.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        // Toast.makeText(activity, "Loading", Toast.LENGTH_LONG).show()
                        binding.progressBarTodaysWeather.visibility = View.VISIBLE
                    }

                }
            }
        })

    }

    private fun setupCurrentWeatherObserver(woeid: Int) {
        viewModel.getCurrentWeather(woeid).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
//                        recyclerView.visibility = View.VISIBLE
                        binding.progressBarTodaysWeather.visibility = View.GONE
                        val weatherInfo = resource.data
                        if (weatherInfo != null) {
                            binding.textCurrentTemp.text = weatherInfo.the_tempF.toString()
                            binding.textMinimumTemp.text = weatherInfo.min_tempF.toString()
                            binding.textMaximumTemp.text = weatherInfo.max_tempF.toString()
                            binding.textWeatherState.text = weatherInfo.weather_state_name
//                            text_day.text = weatherInfo.day

                            val imageDrawable =
                                getWeatherStateImage(resource.data.weather_state_abbr)
                            val appContext = activity?.applicationContext
                            if (appContext != null && imageDrawable >= 0) {
                                binding.imageWeatherState.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        appContext, // Context
                                        imageDrawable// Drawable
                                    )
                                )
                            }
                            //text_time_at_location.text =
                            //text_location.text = resource.data?.title
                            //woeId = resource.data?.woeid
                        } else
                            binding.textDay.text = getString(R.string.cantGetWeather)

                    }

                    Status.ERROR -> {
                        binding.progressBarTodaysWeather.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        // Toast.makeText(activity, "Loading", Toast.LENGTH_LONG).show()
                        binding.progressBarTodaysWeather.visibility = View.VISIBLE
                    }

                }
            }
        })

    }

    private fun setupDateRangeObserver(
        woeid: Int,
        dateTime: DateTime,
        noOfDays: Int
    ) {
        binding.progressBarTodaysWeather.visibility = View.VISIBLE
        viewModel.getWeatherDateRange(woeid, dateTime, noOfDays)
            .observe(viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
//                        recyclerView.visibility = View.VISIBLE
                            initWeatherCardContainer()
                            binding.progressBarTodaysWeather.visibility = View.GONE
                            binding.LinearLayoutCardContainerLayout.removeAllViews()
                            if (resource.data != null) {
                                Timber.d(resource.data[0].weather_state_name)
                                val weatherList = resource.data
                                for (weatherInfo in weatherList) {
                                    addWeatherCard(weatherInfo)
                                }

                            } else
                                binding.textDay.text = getString(R.string.cantGetWeather)

                        }

                        Status.ERROR -> {
                            binding.progressBarTodaysWeather.visibility = View.GONE
                            Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                        }
                        Status.LOADING -> {
                            // Toast.makeText(activity, "Loading", Toast.LENGTH_LONG).show()
                            binding.progressBarTodaysWeather.visibility = View.VISIBLE
                        }

                    }
                }
            })

    }

    private fun initWeatherCardContainer() {
        binding.LinearLayoutCardContainerLayout.removeAllViews()
        val progressBar = ProgressBar(activity)
        //setting height and width of progressBar
        progressBar.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        binding.LinearLayoutCardContainerLayout.addView(progressBar)

    }

    private fun addWeatherCard(weatherInfo: WeatherInfo) {
        val inflater =
            activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v: View = inflater.inflate(R.layout.weather_card, null)

        v.text_weather_state.text = weatherInfo.weather_state_name
        v.text_day.text = weatherInfo.day

        v.text_minimum_temp.text = weatherInfo.min_tempF.toString()
        v.text_maximum_temp.text = weatherInfo.max_tempF.toString()

        val imageDrawable =
            getWeatherStateImage(weatherInfo.weather_state_abbr)
        val appContext = activity?.applicationContext
        if (appContext != null && imageDrawable >= 0) {
            v.image_weather_state.setImageDrawable(
                ContextCompat.getDrawable(
                    appContext, // Context
                    imageDrawable// Drawable
                )
            )
        }

        binding.LinearLayoutCardContainerLayout.addView(
            v,
            0,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    private fun getWeatherStateImage(weatherStateAbbr: String): Int {
        return when (weatherStateAbbr) {
            "sn" -> R.drawable.mw_sn
            "sl" -> R.drawable.mw_sl
            "h" -> R.drawable.mw_h
            "t" -> R.drawable.mw_t
            "hr" -> R.drawable.mw_sn
            "lr" -> R.drawable.mw_lr
            "s" -> R.drawable.mw_s
            "hc" -> R.drawable.mw_hc
            "ln" -> R.drawable.mw_lc
            "c" -> R.drawable.mw_c
            else -> -1
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
