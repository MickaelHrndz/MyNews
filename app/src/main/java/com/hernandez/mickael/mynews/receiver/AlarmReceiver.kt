package com.hernandez.mickael.mynews.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log

import com.hernandez.mickael.mynews.R
import com.hernandez.mickael.mynews.api.ApiSingleton
import com.hernandez.mickael.mynews.enums.SectionSingleton
import com.hernandez.mickael.mynews.models.search.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val apiService = ApiSingleton.getInstance()
        var sum = 0
        for(section in SectionSingleton.sections){
            apiService.section(section).enqueue(object : Callback<SearchResponse> {
                override fun onResponse(call: Call<SearchResponse>?, response: Response<SearchResponse>?) {
                    //Log.d(LOG_TAG, response?.errorBody().toString())
                    if(response?.body()?.searchSubResponse?.docs != null){
                        //mArray.addAll(response.body()?.searchSubResponse!!.docs.asIterable())
                        sum += response.body()?.searchSubResponse!!.docs.size
                    }

                    //Log.d("TABSIZE", mArray.size.toString())
                }

                override fun onFailure(call: Call<SearchResponse>?, t: Throwable?) {
                    //Log.d(LOG_TAG, "MOSTPOPULAR API CALL FAILED : ")
                    t?.printStackTrace()
                }

            })
        }

        val mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.notification_text, sum, SectionSingleton.sections.size))

        // Gets instance of NotificationManager service
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        mNotificationManager?.notify(0, mBuilder.build())
    }

    companion object {

        private val DEBUG_TAG = "AlarmReceiver"
    }

}