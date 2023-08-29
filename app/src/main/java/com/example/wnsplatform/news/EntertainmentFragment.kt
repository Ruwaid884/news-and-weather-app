package com.example.wnsplatform.news

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wnsplatform.Constants
import com.example.wnsplatform.DataClassNews.NewsResponse
import com.example.wnsplatform.Network.NewsService
import com.example.wnsplatform.R
import com.example.wnsplatform.adapters.homeadapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EntertainmentFragment : Fragment() {



    private var mProgressDialog: Dialog? = null
    private lateinit var RecyclerView: RecyclerView
    private lateinit var adapter: homeadapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootview = inflater.inflate(R.layout.fragment_entertainment, container, false)

        getNewsDetails()
        RecyclerView= rootview.findViewById(R.id.entertainment_recycler)


        return rootview
    }



    private fun getNewsDetails(){
        showCustomDialog()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constants.NEWS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()



        val service: NewsService = retrofit.create(NewsService::class.java)


        val listCall: Call<NewsResponse> = service.getNews("in","entertainment",100, Constants.NEWS_APP_ID)


        listCall.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(
                call: Call<NewsResponse>,
                response: Response<NewsResponse>
            ) {
                if(response.isSuccessful){

                    hideProgressDialog()
                    val newslist: NewsResponse? = response.body()
                    RecyclerView.layoutManager = LinearLayoutManager(context,
                        LinearLayoutManager.VERTICAL,false)
                    adapter = newslist?.let { homeadapter(it,requireContext()) }!!
                    RecyclerView.adapter = adapter


                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e("Errorrrr",t!!.message.toString())
                hideProgressDialog()
            }

        })

    }

    private fun showCustomDialog(){
        mProgressDialog = Dialog(requireContext())
        mProgressDialog!!.setContentView(R.layout.dialog_custom_file)
        mProgressDialog!!.show()
    }

    private fun hideProgressDialog(){
        if(mProgressDialog!= null){
            mProgressDialog!!.dismiss()
        }
    }


}