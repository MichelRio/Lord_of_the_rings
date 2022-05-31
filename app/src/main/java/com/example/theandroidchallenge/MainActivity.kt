package com.example.theandroidchallenge

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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
private lateinit var tvShowInfo: TextView
private lateinit var clList: ConstraintLayout
private lateinit var btPrev: Button
private lateinit var btNext: Button
private lateinit var btn1 :Button
private lateinit var btn2 :Button
private lateinit var btn3 :Button
private lateinit var btn4 :Button
private lateinit var btn5 :Button
private lateinit var btn6 :Button
private lateinit var btn7 :Button
private lateinit var btn8 :Button
private lateinit var btn9 :Button
private lateinit var btn10 :Button
var dataCharacters = Array(0){ Array(5) {""}}
var quotesArray = mutableListOf<String>()

//var paginationControl = 0
var limitSearch = 10
var offsetSearch = 0

val baseService     = NetworkUtils.baseService
val retrofitClient  = NetworkUtils.getRetrofitInstance(baseService)
val endpoint        = retrofitClient.create(Endpoint::class.java)
var accessToken     = "Bearer 4xJFes_zhiWb6yysfD-g"
var characterId     = "5cd99d4bde30eff6ebccfc57"

class MainActivity : AppCompatActivity(), RecyclerAdapter.OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvMainRecyclerView = findViewById(R.id.rvMainRecyclerView)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvMainRecyclerView.layoutManager = layoutManager
        adapter = RecyclerAdapter(this)
        rvMainRecyclerView.adapter = adapter

        clList = findViewById(R.id.cl_List)
        tvShowInfo = findViewById(R.id.tvName)
        btPrev = findViewById(R.id.btPrev)
        btNext = findViewById(R.id.btNext)
        clList.visibility = View.GONE
        btPrev.visibility = View.GONE
        btNext.visibility = View.GONE

        btn1 = findViewById(R.id.button1)
        btn2 = findViewById(R.id.button2)
        btn3 = findViewById(R.id.button3)
        btn4 = findViewById(R.id.button4)
        btn5 = findViewById(R.id.button5)
        btn6 = findViewById(R.id.button6)
        btn7 = findViewById(R.id.button7)
        btn8 = findViewById(R.id.button8)
        btn9 = findViewById(R.id.button9)
        btn10 = findViewById(R.id.button10)

        btn1.setOnClickListener(){ showDetailsDialog(dataCharacters[0]) }
        btn2.setOnClickListener(){ showDetailsDialog(dataCharacters[1]) }
        btn3.setOnClickListener(){ showDetailsDialog(dataCharacters[2]) }
        btn4.setOnClickListener(){ showDetailsDialog(dataCharacters[3]) }
        btn5.setOnClickListener(){ showDetailsDialog(dataCharacters[4]) }
        btn6.setOnClickListener(){ showDetailsDialog(dataCharacters[5]) }
        btn7.setOnClickListener(){ showDetailsDialog(dataCharacters[6]) }
        btn8.setOnClickListener(){ showDetailsDialog(dataCharacters[7]) }
        btn9.setOnClickListener(){ showDetailsDialog(dataCharacters[8]) }
        btn10.setOnClickListener(){ showDetailsDialog(dataCharacters[9]) }


//      getBookInfo()
        getMoviesInfo()
//        getCharactersInfo()
//        getDetailsInfo()
//        getCharacterQuotes()

        btNext.setOnClickListener(){
            offsetSearch +=10
            getCharactersInfo()
        }
        btPrev.setOnClickListener(){
            offsetSearch -=10
            getCharactersInfo()
        }
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
                    val nameMovie = rootArray.getJSONObject(i).getString("name")
                    val nominations = rootArray.getJSONObject(i).getInt("academyAwardNominations").toString()
                    data[i][0] = id
                    data[i][1] = nameMovie
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
        endpoint.getCharacters(accessToken,limitSearch,offsetSearch).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val textResult = response.body()?.string()

                val jo = JSONObject(textResult)
                val rootArray: JSONArray = jo.getJSONArray("docs")
                val rootArrayLength = rootArray.length()
                dataCharacters = Array(rootArrayLength){ Array(5) {""}}

                for (i in 0 until rootArrayLength) {
                    val idCharacter = rootArray.getJSONObject(i).getString("_id")
                    val nameCharacter = rootArray.getJSONObject(i).getString("name")
                    val gender = rootArray.getJSONObject(i).getString("gender")
                    val race = rootArray.getJSONObject(i).getString("race")
                    val birth = rootArray.getJSONObject(i).getString("birth")

                    setButtonText(i, nameCharacter)

                    dataCharacters[i][0] = idCharacter
                    dataCharacters[i][1] = nameCharacter
                    dataCharacters[i][2] = gender
                    dataCharacters[i][3] = race
                    dataCharacters[i][4] = birth
                }
                Log.i("MainAct:getCharacters","offset: $offsetSearch")
                when {
                    offsetSearch >  0 ->   btPrev.visibility = View.VISIBLE
                    offsetSearch == 0 -> { btPrev.visibility = View.GONE
                                           btNext.visibility = View.VISIBLE }
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

    fun getCharacterQuotes(accessToken: String, characterId: String) {
        endpoint.getCharactersQuotes(accessToken, characterId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val textResult = response.body()?.string()

                val jo = JSONObject(textResult)
                val rootArray: JSONArray = jo.getJSONArray("docs")
                val rootArrayLength = rootArray.length()

                for (i in 0 until rootArrayLength) {
                   var quotes = rootArray.getJSONObject(i).getString("dialog")
                    quotesArray.add(quotes.toString())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("MainAct:getQuotes", "failure")
            }
        })
    }

    private fun setButtonText(i: Int, name: String) {
        when (i){
            0 -> btn1.text = name
            1 -> btn2.text = name
            2 -> btn3.text = name
            3 -> btn4.text = name
            4 -> btn5.text = name
            5 -> btn6.text = name
            6 -> btn7.text = name
            7 -> btn8.text = name
            8 -> btn9.text = name
            9 -> btn10.text = name
        }
    }

    fun showDetailsDialog(personDetails: Array<String>) {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.details_dialog, null)
        val name = dialogLayout.findViewById(R.id.tvName) as TextView
        val gender = dialogLayout.findViewById(R.id.tvGender) as TextView
        val race = dialogLayout.findViewById(R.id.tvRace) as TextView
        val birth = dialogLayout.findViewById(R.id.tvBirth) as TextView
        val quotesLayout = dialogLayout.findViewById(R.id.clQuotes) as ConstraintLayout
        quotesLayout.visibility = View.GONE

        getCharacterQuotes(accessToken, personDetails[0])

        name.text   = personDetails[1]
        gender.text = personDetails[2]
        race.text   = personDetails[3]
        birth.text  = personDetails[4]

        dialog.setContentView(dialogLayout)
        dialog.show()

    }

    override fun onItemClick(position: Int, title: String) {
        if(title == "5cd95395de30eff6ebccde56"){
            //TODO by movie
        }
        clList.visibility = View.VISIBLE
        getCharactersInfo()
    }

}