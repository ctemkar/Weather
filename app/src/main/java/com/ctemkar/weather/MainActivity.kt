package com.ctemkar.weather

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import utils.ForegroundOnlyLocationService
import utils.SharedPreferenceUtil
import utils.SharedViewModel
import utils.toText

private const val TAG = "MainActivity"
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    // Location is got in activity rather than fragment so it can be used in any fragment
    // without repeating code
    // plus helps in consolidating checking for permissions, etc.
    // Location vars
    private var foregroundOnlyLocationServiceBound = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient    // Provides location updates for while-in-use feature.
    private var foregroundOnlyLocationService: ForegroundOnlyLocationService? = null

    // Listens for location broadcasts from ForegroundOnlyLocationService.
    private lateinit var foregroundOnlyBroadcastReceiver: ForegroundOnlyBroadcastReceiver
    private lateinit var sharedPreferences: SharedPreferences
    private var location: Location? = null

    // Monitors connection to the while-in-use service.
    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundOnlyLocationService.LocalBinder
            foregroundOnlyLocationService = binder.service
            foregroundOnlyLocationServiceBound = true
            if (foregroundPermissionApproved()) {
                foregroundOnlyLocationService?.subscribeToLocationUpdates()
                    ?: Log.d(TAG, "Service Not Bound")
            } else {
                requestForegroundPermissions()
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundOnlyLocationService = null
            foregroundOnlyLocationServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (location == null)
            nav_host_fragment.visibility = View.GONE
        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver()

        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        //    val navView: BottomNavigationView = findViewById(R.id.nav_view)

//        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
         */
//        setupActionBarWithNavController(navController, appBarConfiguration)
        // navView.setupWithNavController(navController)
        //checkGpsStatus()
        //isGPSEnabled(this)
    }

    override fun onStart() {
        super.onStart()

/*
        updateButtonState(
            sharedPreferences.getBoolean(SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false)
        )

 */
//        sharedPreferences.registerOnSharedPreferenceChangeListener(this)



    }

    override fun onResume() {
        super.onResume()
        val serviceIntent = Intent(this, ForegroundOnlyLocationService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)
        LocalBroadcastManager.getInstance(this).registerReceiver(
            foregroundOnlyBroadcastReceiver,
            IntentFilter(
                ForegroundOnlyLocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST
            )
        )
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        createLocationRequest()
    }

    override fun onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(
//            foregroundOnlyBroadcastReceiver
//        )
        super.onPause()
    }

    override fun onStop() {
        if (foregroundOnlyLocationServiceBound) {
            unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)

        super.onStop()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        // Updates button states if new while in use location is added to SharedPreferences.
        if (key == SharedPreferenceUtil.KEY_FOREGROUND_ENABLED) {
            updateButtonState(
                sharedPreferences.getBoolean(
                    SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false
                )
            )
        }
    }

    // Step 1.0, Review Permissions: Method checks if permissions approved.
    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    // Step 1.0, Review Permissions: Method requests permissions.
    private fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            Snackbar.make(
                findViewById(R.id.activity_main),
                R.string.permission_rationale,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.ok) {
                    // Request permission
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                    )
                }
                .show()
        } else {
            Log.d(TAG, "Request foreground only permission")
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    // Step 1.0, Review Permissions: Handles permission result.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "onRequestPermissionResult")

        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    // If user interaction was interrupted, the permission request
                    // is cancelled and you receive empty arrays.
                    Log.d(TAG, "User interaction was cancelled.")

                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    // Permission was granted.
                    foregroundOnlyLocationService?.subscribeToLocationUpdates()

                else -> {
                    // Permission denied.
                    updateButtonState(false)

                    Snackbar.make(
                        findViewById(R.id.activity_main),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.settings) {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                BuildConfig.APPLICATION_ID,
                                null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        .show()
                }
            }
        }
    }

    private fun updateButtonState(trackingLocation: Boolean) {
        Log.d(TAG, "updateButtonState")

        if (trackingLocation) {
            Log.d(TAG, "Tracking location")
            //foregroundOnlyLocationButton.text = getString(R.string.stop_location_updates_button_text)
        } else {
            Log.d(TAG, "not tracking location")
            //foregroundOnlyLocationButton.text = getString(R.string.start_location_updates_button_text)
        }


    }

    private fun logResultsToScreen(output: String) {
        Log.d(TAG, output)
        //   val outputWithPreviousLogs = "$output\n${outputTextView.text}"
        //  outputTextView.text = outputWithPreviousLogs
    }

    private fun gotLocation(loc: Location) {
        waiting_for_location.visibility = View.GONE
        nav_host_fragment.visibility = View.VISIBLE
        location = loc
        val model: SharedViewModel by viewModels()
        model.setLocation(location!!)
    }

    /**
     * Receiver for location broadcasts from [ForegroundOnlyLocationService].
     */
    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val loc = intent.getParcelableExtra<Location>(
                ForegroundOnlyLocationService.EXTRA_LOCATION
            )

            if (loc != null) {
                gotLocation(loc)
                logResultsToScreen("Foreground location: ${loc.toText()}")
            }
        }
    }

    fun checkGpsStatus() {
        val locationManager =
            this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // assert(locationManager != null)
        val GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (GpsStatus) {
            Log.d(TAG, "GPS Is Enabled")
        } else {
            Log.d(TAG, "GPS Is Disabled")
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

        }
    }
/*
    fun isGPSEnabled(context: Context): Boolean {
        val service =
            context.getSystemService(Activity.LOCATION_SERVICE) as LocationManager
        val enabled = service
            .isProviderEnabled(LocationManager.GPS_PROVIDER)
        return if (!enabled) {
            val alertDialog =
                AlertDialog.Builder(context)
            alertDialog.setTitle("Switch GPS location ON")
            alertDialog.setMessage(getString((R.string.noLocationMessage)))
            alertDialog.setPositiveButton(
                "Settings"
            ) { dialog, which ->
                val intent =
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
/*                Toast.makeText(
                    context,
                    lib.app.util.Connectivity.sGPSConnectionBatterySaving,
                    Toast.LENGTH_SHORT
                ).show()

 */
            }
            alertDialog.setNegativeButton(
                "Cancel"
            ) { dialog, which -> dialog.cancel() }
            alertDialog.show()
            false
        } else {
            getLastLocation()
            true
        }
    }

 */

    private fun getLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { loc: Location? ->
                if (loc != null) {
                    this.location = loc
                    gotLocation(loc)
                } else
                // Got last known location. In some rare situations this can be null.
                    Log.d(TAG, "Location is null")
                //                location.latitude()
            }



    }

    fun createLocationRequest() {
        val REQUEST_CHECK_SETTINGS = 1
        val locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }
        if(locationRequest != null) {
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
            val client: SettingsClient = LocationServices.getSettingsClient(this)
            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
            task.addOnSuccessListener { locationSettingsResponse ->
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                getLastLocation()

            }

            task.addOnFailureListener { exception ->
                if (exception is ResolvableApiException){
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().

                        exception.startResolutionForResult(this@MainActivity,
                            REQUEST_CHECK_SETTINGS)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }
                }
            }
        }

    }
}
