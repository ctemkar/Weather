package com.ctemkar.weather.ui.today

import ViewModels.LocationInfoViewModel
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ctemkar.weather.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_today.*
import network.ApiHelper
import network.RetrofitBuilder
import ui.base.LocationInfoViewModelFactory
import utils.Status

class TodayFragment : Fragment() {

    private val TAG : String = "TodayFragment"
    private lateinit var viewModel: LocationInfoViewModel
    private var woeId = -1
    // private lateinit var homeViewModel: TodayViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var location : Location = Location("")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // homeViewModel = ViewModelProviders.of(this).get(TodayViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_today, container, false)

        val ctx = getActivity()?.applicationContext
        if (ctx != null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { loc: Location? ->
                    if (loc != null) {
                        this.location = loc
                        setupObservers()

                    } else
                    // Got last known location. In some rare situations this can be null.
                        Log.d(TAG, "Location is null")
                            //                location.latitude()
                }

        }

        return root
    }

    override fun onResume() {
        super.onResume()
        setupViewModel()
        setupUI()
       // setupObservers()
    }
    private fun setupViewModel() {
        /* var viewModel: MainViewModel = ViewModelProvider(viewModelStore,
             ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))).get(
             MainViewModel::class.java)

         */

        viewModel = ViewModelProviders.of(
            this,
            LocationInfoViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(LocationInfoViewModel::class.java)


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
        val sLatLong = if(location.latitude == 0.0)
            "40.793896,-73.940711" else location.latitude.toString() + "," + location.longitude.toString()
        viewModel.getLocationInfo(sLatLong).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
//                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        if(resource.data != null) {
                            text_day.text = resource.data.title
                            val woeid = resource.data.woeid
                            setupCurrentWeatherObserver(woeid)
                        }
                        else
                            text_day.text = getString(R.string.cantGetWeather)
                    }

                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        // Toast.makeText(activity, "Loading", Toast.LENGTH_LONG).show()
                        progressBar.visibility = View.VISIBLE
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
                        progressBar.visibility = View.GONE
                        if(resource.data != null) {
                            text_current_temp.text = resource.data.the_tempF.toString()
                            text_minimum_temp.text = resource.data.min_tempF.toString()
                            text_maxumum_temp.text = resource.data.max_tempF.toString()
                            text_weather_state.text = resource.data.weather_state_name

                            val imageDrawable =  getWeatherStateImage(resource.data.weather_state_abbr)
                            val appContext = activity?.applicationContext
                            if(appContext != null && imageDrawable >= 0) {
                                image_weather_state.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        appContext, // Context
                                         imageDrawable// Drawable
                                    ))

                            }
                             //text_time_at_location.text =
                            //text_location.text = resource.data?.title
                            //woeId = resource.data?.woeid
                        }
                        else
                            text_day.text = getString(R.string.cantGetWeather)

                    }

                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        // Toast.makeText(activity, "Loading", Toast.LENGTH_LONG).show()
                        progressBar.visibility = View.VISIBLE
                    }

                }
            }
        })

    }

    private fun getWeatherStateImage(weatherStateAbbr: String) : Int {
        return when(weatherStateAbbr) {
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
