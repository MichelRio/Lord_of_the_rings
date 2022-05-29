package com.example.theandroidchallenge

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.theandroidchallenge.api.Endpoint
import com.example.theandroidchallenge.utils.NetworkUtils
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private var layoutManager: RecyclerView.LayoutManager? = null
private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null
private lateinit var rvMainRecyclerView: RecyclerView

val baseService     = NetworkUtils.baseService
val retrofitClient  = NetworkUtils.getRetrofitInstance(baseService)
val endpoint        = retrofitClient.create(Endpoint::class.java)
var accessToken     = "Bearer 4xJFes_zhiWb6yysfD-g"
var characterId     = "5cd99d4bde30eff6ebccfc57"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvMainRecyclerView = findViewById(R.id.rvMainRecyclerView)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvMainRecyclerView.layoutManager = layoutManager
        adapter = RecyclerAdapter()
        rvMainRecyclerView.adapter = adapter

//        text = findViewById<TextView>(R.id.tvTitle)
//      getBookInfo()
        getMoviesInfo()
//        getCharactersInfo()
//        getDetailsInfo()
//        getCharacterQuotes()
    }

    fun getBookInfo() {
        endpoint.getBooksResponse().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val textResult = response.body()?.string()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("MainAct:getBooks", "failure")
            }
        })
    }


    fun getMoviesInfo() {
        endpoint.getMoviesResponse(accessToken).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val textResult = response.body()?.string()

                val jo = JSONObject(textResult)
                val rootArray: JSONArray = jo.getJSONArray("docs")
                val rootArrayLength = rootArray.length()
                var data = Array(rootArrayLength){ Array(3) {""}}

                for (i in 0 until rootArrayLength) {
                    val id = rootArray.getJSONObject(i).getString("_id")
                    val name = rootArray.getJSONObject(i).getString("name")
                    val nominations = rootArray.getJSONObject(i).getInt("academyAwardNominations").toString()
                    data[i][0] = id
                    data[i][1] = name
                    data[i][2] = nominations
                    if(i == rootArrayLength-1){
                        RecyclerAdapter.titles = data
                        adapter?.notifyDataSetChanged()
                    }
                }
//                println(data.contentDeepToString())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("MainAct:getCharacters", "failure")
            }
        })
    }

    fun getCharactersInfo() {
        endpoint.getCharacters(accessToken).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val textResult = response.body()?.string()
                var data = mutableListOf<String>()
                val jo = JSONObject(textResult)
                val rootArray: JSONArray = jo.getJSONArray("docs")
                val rootArrayLength = rootArray.length()
                for (i in 0 until rootArrayLength) {
                    val name = rootArray.getJSONObject(i).getString("name")
                    data.add(name.toString())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("MainAct:getCharacters", "failure")
            }
        })
    }

    fun getDetailsInfo() {
        endpoint.getCharacterDetails(accessToken, characterId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val textResult = response.body()?.string()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("MainAct:getDetails", "failure")
            }
        })
    }

    fun getCharacterQuotes() {
        endpoint.getCharactersQuotes(accessToken, characterId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val textResult = response.body()?.string()
                var data = mutableListOf<String>()
                val jo = JSONObject(textResult)
                val rootArray: JSONArray = jo.getJSONArray("docs")
                val rootArrayLength = rootArray.length()
                for (i in 0 until rootArrayLength) {
                    val quotes = rootArray.getJSONObject(i).getString("dialog")
                    data.add(quotes.toString())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("MainAct:getQuotes", "failure")
            }
        })
    }


}