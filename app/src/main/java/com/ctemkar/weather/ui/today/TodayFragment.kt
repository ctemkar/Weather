package com.ctemkar.weather.ui.today

import com.ctemkar.weather.viewmodels.LocationInfoViewModelFactory
import com.ctemkar.weather.viewmodels.WeatherViewModel
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
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
import com.ctemkar.weather.database.getDatabase
import com.soywiz.klock.DateTime
import com.soywiz.klock.days
import kotlinx.android.synthetic.main.fragment_today.*
import kotlinx.android.synthetic.main.weather_card.view.*
import model.WeatherInfo
import network.ApiHelper
import network.RetrofitBuilder
import timber.log.Timber
import utils.Constant
import utils.DatePickerFragment
import utils.SharedViewModel
import utils.Status


class TodayFragment : Fragment() {

    private var dateToShow: DateTime = DateTime.now() + 1.days
    private val TAG: String = "TodayFragment"
    private lateinit var viewModel: WeatherViewModel

/*
    private val viewModel: WeatherViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, WeatherViewModel.Factory(activity.application))
            .get(WeatherViewModel::class.java)
    }
 */

    private var woeId = -1
    private val model: SharedViewModel by activityViewModels()
    // private lateinit var homeViewModel: TodayViewModel
   // private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.weather_main_frag_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        /*

        ViewModelProviders.of(activity!!)[SharedViewModel::class.java]
            .selectedDate
            .observe(activity!!, Observer { dateOfBirthField.editText.setText(it) })

         */
        return when (item.itemId) {
            R.id.weather_on_date -> {

                ViewModelProviders.of(requireActivity())[SharedViewModel::class.java]
                    .selectedDate
                    .observe(requireActivity(), Observer {
                        dateToShow = it
                        setupDateRangeObserver(
                            woeId,
                            it,
                            Constant.defaultWeatherDays
                        )
                    })

                val newFragment = DatePickerFragment()
                activity?.supportFragmentManager?.let { newFragment.show(it, "datePicker") }


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

        getDatabase(activity?.applicationContext)
        setupViewModel()
        setupUI()
        val root = inflater.inflate(R.layout.fragment_today, container, false)
        model.currentLocation.observe(viewLifecycleOwner, Observer<Location> { item ->
            location = model.currentLocation.value!!
            Timber.d("lat: ${location.longitude}, lon: ${location.longitude}")
            setupObservers()

            // Update the UI
        })
        // val ctx = getActivity()?.applicationContext
        setHasOptionsMenu(true)

        return root
    }

    override fun onResume() {
        super.onResume()
        // setupObservers()
    }

    private fun setupViewModel() {
        /* var viewModel: MainViewModel = ViewModelProvider(viewModelStore,
             ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))).get(
             MainViewModel::class.java)

         */

        viewModel = ViewModelProviders.of(this,
            LocationInfoViewModelFactory(
                ApiHelper(RetrofitBuilder.apiService),
                context?.applicationContext
            )
        ).get(WeatherViewModel::class.java)


    }

    private fun setupUI() {


        /*
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = FutureAdapter(arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter

         */
    }

    private fun setupObservers() {
        val sLatLong = if (location.latitude == 0.0)
            "40.793896,-73.940711" else location.latitude.toString() + "," + location.longitude.toString()
        viewModel.getLocationInfo(sLatLong).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        initWeatherCardContainer()
                        progressBarTodaysWeather.visibility = View.GONE
                        if (resource.data != null) {
                           text_day.text = resource.data.title
                           woeId = resource.data.woeid
                            setupCurrentWeatherObserver(woeId)

                            setupDateRangeObserver(
                                woeId,
                                dateToShow,
                                Constant.defaultWeatherDays
                            )


                        } else
                            text_day.text = getString(R.string.cantGetWeather)
                    }

                    Status.ERROR -> {
                        progressBarTodaysWeather.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        // Toast.makeText(activity, "Loading", Toast.LENGTH_LONG).show()
                        progressBarTodaysWeather.visibility = View.VISIBLE
                    }

                }
            }
        })
    }

    private fun setupCurrentWeatherObserver(woeid: Int) {
        viewModel.getCurrentWeatherFromDb(woeid).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
//                        recyclerView.visibility = View.VISIBLE
                        progressBarTodaysWeather.visibility = View.GONE
                        val weatherInfo = resource.data
                        if (weatherInfo != null) {
                            text_current_temp.text = weatherInfo.the_tempF.toString()
                            text_minimum_temp.text = weatherInfo.min_tempF.toString()
                            text_maxumum_temp.text = weatherInfo.max_tempF.toString()
                            text_weather_state.text = weatherInfo.weather_state_name
//                            text_day.text = weatherInfo.day

                            val imageDrawable =
                                getWeatherStateImage(resource.data.weather_state_abbr)
                            val appContext = activity?.applicationContext
                            if (appContext != null && imageDrawable >= 0) {
                                image_weather_state.setImageDrawable(
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
                            text_day.text = getString(R.string.cantGetWeather)

                    }

                    Status.ERROR -> {
                        progressBarTodaysWeather.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        // Toast.makeText(activity, "Loading", Toast.LENGTH_LONG).show()
                        progressBarTodaysWeather.visibility = View.VISIBLE
                    }

                }
            }
        })


        viewModel.getCurrentWeather(woeid).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
//                        recyclerView.visibility = View.VISIBLE
                        progressBarTodaysWeather.visibility = View.GONE
                        val weatherInfo = resource.data
                        if (weatherInfo != null) {
                            text_current_temp.text = weatherInfo.the_tempF.toString()
                            text_minimum_temp.text = weatherInfo.min_tempF.toString()
                            text_maxumum_temp.text = weatherInfo.max_tempF.toString()
                            text_weather_state.text = weatherInfo.weather_state_name
//                            text_day.text = weatherInfo.day

                            val imageDrawable =
                                getWeatherStateImage(resource.data.weather_state_abbr)
                            val appContext = activity?.applicationContext
                            if (appContext != null && imageDrawable >= 0) {
                                image_weather_state.setImageDrawable(
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
                            text_day.text = getString(R.string.cantGetWeather)

                    }

                    Status.ERROR -> {
                        progressBarTodaysWeather.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        // Toast.makeText(activity, "Loading", Toast.LENGTH_LONG).show()
                        progressBarTodaysWeather.visibility = View.VISIBLE
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
        progressBarTodaysWeather.visibility = View.VISIBLE
        viewModel.getWeatherDateRange(woeid, dateTime, noOfDays)
            .observe(viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            initWeatherCardContainer()
                            progressBarTodaysWeather.visibility = View.GONE
                            LinearLayout_CardContainerLayout.removeAllViews()
                            if (resource.data != null) {
                                Timber.d(resource.data[0].weather_state_name)
                                val weatherList = resource.data
                                for (weatherInfo in weatherList) {
                                    addWeatherCard(weatherInfo)
                                }

                            } else
                                text_day.text = getString(R.string.cantGetWeather)

                        }

                        Status.ERROR -> {
                            progressBarTodaysWeather.visibility = View.GONE
                            Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                        }
                        Status.LOADING -> {
                            // Toast.makeText(activity, "Loading", Toast.LENGTH_LONG).show()
                            progressBarTodaysWeather.visibility = View.VISIBLE
                        }

                    }
                }
            })

    }

    private fun initWeatherCardContainer() {
        LinearLayout_CardContainerLayout.removeAllViews()
        val progressBar = ProgressBar(activity)
        //setting height and width of progressBar
        progressBar.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        LinearLayout_CardContainerLayout.addView(progressBar)

    }
    private fun addWeatherCard(weatherInfo: WeatherInfo) {
        val inflater =
            activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v: View = inflater.inflate(R.layout.weather_card, null)

        v.text_weather_state.text = weatherInfo.weather_state_name
        v.text_day.text = weatherInfo.day

        v.text_minimum_temp.text = weatherInfo.min_tempF.toString()
        v.text_maxumum_temp.text = weatherInfo.max_tempF.toString()

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

        LinearLayout_CardContainerLayout.addView(
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


}
