package com.ctemkar.weather.ui.today

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ctemkar.weather.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import data.repository.DataRepository

class TodayFragment : Fragment() {

    private val TAG : String = "TodayFragment"
    private lateinit var homeViewModel: TodayViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    public lateinit var location: Location
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(TodayViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_today, container, false)
        val textView: TextView = root.findViewById(R.id.text_today)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val ctx = getActivity()?.applicationContext
        if (ctx != null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { loc: Location? ->
                    if (loc != null) {
                        this.location = loc

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


        val data: DataRepository

        //       getCurrentData()
    }

}
