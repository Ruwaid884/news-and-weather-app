package com.example.wnsplatform

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ContentProviderClient
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import com.example.wnsplatform.DataClassNews.NewsResponse
import com.example.wnsplatform.Network.NewsService
import com.example.wnsplatform.Network.WeatherService
import com.example.wnsplatform.dataClass.WeatherResponse
import com.example.wnsplatform.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

   private var binding:ActivityMainBinding? = null
    private var mProgressDialog: Dialog? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.news?.setOnClickListener{
            val intent = Intent(this,News::class.java)
            startActivity(intent)
        }



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            if(!IsLocationEnabled()){
                Toast.makeText(this,"Turn On the Location",Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            } else{
                Dexter.withActivity(this)
                    .withPermissions(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ).withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                            if (report!!.areAllPermissionsGranted()) {

                                requestLocationData()
                            }

                            if (report.isAnyPermissionPermanentlyDenied) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "You have denied the location.Please enable it",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permissions: List<PermissionRequest?>?,
                            token: PermissionToken?
                        ) {

                            showRationalDialogForPermission()
                        }
                    }).onSameThread()
                    .check()

                    }
            }

    private fun showRationalDialogForPermission(){
        AlertDialog.Builder(this)
            .setMessage("It looks Like you have turned off permissions")
            .setPositiveButton(
                "Go to Settings"
            ){_,_->
                try{
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package",packageName,null)
                    intent.data = uri
                    startActivity(intent)
                }catch (e:ActivityNotFoundException){
                    e.printStackTrace()
                }

            }
            .setNegativeButton("Cancel"){dialog,_->
                dialog.dismiss()
            }.show()
    }



    @SuppressLint("MissingPermission")
    private fun requestLocationData(){
        val mLocationRequest = com.google.android.gms.location.LocationRequest()
        mLocationRequest.priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY

        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location? = locationResult.lastLocation
            val latitude = mLastLocation?.latitude
            val longitude = mLastLocation?.longitude

            getLocationWeatherDetails(latitude!!,longitude!!)

        }
    }



    private fun getLocationWeatherDetails(latitude:Double,longitude:Double){
        if(Constants.isNetworkAvailable(this)){

            val retrofit : Retrofit = Retrofit.Builder().
                    baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service: WeatherService = retrofit.create(WeatherService::class.java)

            val listCall : Call<WeatherResponse> = service.getWeather(
                latitude,longitude, Constants.METRIC_UNIT,Constants.APP_ID
            )

            showCustomDialog()
            listCall.enqueue(object :Callback<WeatherResponse>{
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if(response.isSuccessful){


                        hideProgressDialog()
                        val weatherList: WeatherResponse? = response.body()
                        setupUI(weatherList!!)
                    }
                    else{
                        val rc = response.code()
                        when(rc){
                            400-> {
                                Log.e("Error 400","Bad Connection")
                            }
                            404 -> {
                                Log.e("Error 404","Not found")

                            }

                            else ->{
                                Log.e("Error","Generic Error")
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.e("Errorrrr",t!!.message.toString())
                    hideProgressDialog()
                }

            })
        }
        else{

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return when(item.itemId){
           R.id.action_refresh->{
               requestLocationData()
               true
           }else -> super.onOptionsItemSelected(item)
       }
    }

    private fun IsLocationEnabled(): Boolean{
        val locationManager:LocationManager =
            getSystemService(Context.LOCATION_SERVICE)as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showCustomDialog(){
        mProgressDialog = Dialog(this)
        mProgressDialog!!.setContentView(R.layout.dialog_custom_file)
        mProgressDialog!!.show()
    }

    private fun hideProgressDialog(){
        if(mProgressDialog!= null){
            mProgressDialog!!.dismiss()
        }
    }

    private fun setupUI(weatherList: WeatherResponse){
        for(i in weatherList.weather.indices){

            binding?.tvMain?.text = weatherList.weather[i].main
            binding?.tvMainDescription?.text = weatherList.weather[i].description

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding?.tvTemp?.text = weatherList.main.temp.toString() +
                        getUnit(application.resources.configuration.locales.toString())
            }

            binding?.tvSunriseTime?.text = unixTime(weatherList.sys.sunrise)
            binding?.tvSunsetTime?.text = unixTime(weatherList.sys.sunset)
            binding?.tvHumidity?.text = weatherList.main.humidity.toString() + " per cent"
            binding?.tvMax?.text = weatherList.main.temp_max.toString() + " max"
            binding?.tvMin?.text = weatherList.main.temp_min.toString() + " min"
            binding?.tvSpeed?.text = weatherList.wind.speed.toString()
            binding?.tvName?.text = weatherList.name
            binding?.tvCountry?.text = weatherList.sys.country

            when(weatherList.weather[i].icon){
                "01d"-> binding?.ivMain?.setImageResource(R.drawable.sunny)
                "02d"-> binding?.ivMain?.setImageResource(R.drawable.cloud)
                "03d"-> binding?.ivMain?.setImageResource(R.drawable.cloud)
                "04d"-> binding?.ivMain?.setImageResource(R.drawable.cloud)

                "04n"-> binding?.ivMain?.setImageResource(R.drawable.cloud)
                "10d"-> binding?.ivMain?.setImageResource(R.drawable.rain)
                "11d"-> binding?.ivMain?.setImageResource(R.drawable.storm)
                "13d"-> binding?.ivMain?.setImageResource(R.drawable.snowflake)
                "01n"-> binding?.ivMain?.setImageResource(R.drawable.cloud)
                "02n"-> binding?.ivMain?.setImageResource(R.drawable.cloud)
                "03n"-> binding?.ivMain?.setImageResource(R.drawable.cloud)
                "10n"-> binding?.ivMain?.setImageResource(R.drawable.cloud)
                "11n"-> binding?.ivMain?.setImageResource(R.drawable.rain)
                "13n"-> binding?.ivMain?.setImageResource(R.drawable.snowflake)
            }


        }
    }

    private fun getUnit(value: String): String? {
        var value = "°C"
        if("US"== value || "LR"== value || "MM" == value){
            value = "°F"
        }

        return value
    }

    private fun unixTime(timex:Long): String? {
        val date = Date(timex*1000L)
        val sdf = SimpleDateFormat("HH:mm",Locale.UK)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }


}