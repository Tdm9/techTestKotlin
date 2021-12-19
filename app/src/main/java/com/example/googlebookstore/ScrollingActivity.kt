package com.example.googlebookstore

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.googlebookstore.api.ApiClient
import com.example.googlebookstore.api.pojos.BooksResponse
import com.example.googlebookstore.api.BooksService
import com.example.googlebookstore.databinding.ActivityScrollingBinding
import kotlinx.android.synthetic.main.content_scrolling.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

import android.widget.PopupWindow

import android.widget.LinearLayout
import androidx.room.Room
import com.example.googlebookstore.api.pojos.VolumeInfo
import com.example.googlebookstore.room.BookDao
import com.example.googlebookstore.room.Database
import com.example.googlebookstore.viewmodel.BookDetailModel
import kotlinx.android.synthetic.main.popup_window.view.*
import androidx.recyclerview.widget.RecyclerView




class ScrollingActivity : AppCompatActivity() {

    //TODO: define API_KEY
    val API_KEY: String? = null;
    private var apiInterface: BooksService? = null
    private lateinit var binding: ActivityScrollingBinding
    private lateinit var adapter: ListAdapter
    private lateinit var dao: BookDao
    var data= ArrayList<VolumeInfo>();
    var page = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (API_KEY.isNullOrEmpty()){
            throw Exception("PLEASE PROVIDE A GOOGLE API_KEY")
        }


        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title

        // create instance of DAO to access the entities
        dao = Room.databaseBuilder(
            this, Database::class.java, "books"
        ).allowMainThreadQueries().build().userDao()

        createRecyclerView()

        var beforeTogglingFilter: ArrayList<VolumeInfo>?=null

        binding.fab.setOnClickListener {
            if (beforeTogglingFilter.isNullOrEmpty()){
                beforeTogglingFilter = ArrayList(data)
                data.removeAll(data)
                data.addAll(dao.getAll().map { it.toVolumeInfo()} as ArrayList<VolumeInfo>)
            }else{
                data.removeAll(data)
                data.addAll(beforeTogglingFilter as ArrayList<VolumeInfo>)
                beforeTogglingFilter=null
            }

            adapter.notifyDataSetChanged();
        }


        apiInterface= ApiClient().client?.create(BooksService::class.java)

        loadBooks(1)
    }

    fun loadBooks(page:Int){
        if (API_KEY != null) {
            apiInterface?.search40Books(page,"a",API_KEY)
                ?.enqueue(object : Callback<BooksResponse?> {
                    override fun onResponse(call: Call<BooksResponse?>?, response: Response<BooksResponse?>) {
                        if (response.isSuccessful) {
                            val  bks : List<VolumeInfo?>? = response.body()?.items?.map { it.volumeInfo }
                            if (!bks.isNullOrEmpty()) {
                                bks.forEach {
                                    if (it != null) {
                                        data.add(it)
                                    }
                                }
                                adapter.notifyDataSetChanged()
                            }
                            Log.i(TAG, "post submitted to API." + bks.toString())
                        }
                    }

                    override fun onFailure(call: Call<BooksResponse?>?, t: Throwable?) {
                        Log.e(TAG, "Unable to submit post to API.")
                    }
                })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


    fun onButtonShowPopupWindowClick(view: View?,popupData: VolumeInfo) {

        // inflate the layout of the popup window
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window, null)

        print(popupData)

        popupView.title.text=popupData.title
        popupView.subtitle.text=popupData.subtitle
        popupView.description.text=popupData.description
        if (!popupData.authors.isNullOrEmpty()){
            popupView.author.text= popupData.authors[0]

        }

        val d: List<BookDetailModel> = dao.loadAllByIds(popupData.title,popupData.imageLinks.thumbnail)
        if (d.isNullOrEmpty()){
            popupView.imageButton.setOnClickListener {
                dao.insertAll(popupData.toBookDetailModel())
                popupView.imageButton.setImageResource(android.R.drawable.btn_star_big_on)
            }
        }else {
            popupView.imageButton.setImageResource(android.R.drawable.btn_star_big_on)
            popupView.imageButton.setOnClickListener {
                dao.delete(d[0])
                popupView.imageButton.setImageResource(android.R.drawable.btn_star_big_off)
            }
        }


        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        // dismiss the popup window when touched
    }

    fun createRecyclerView(){
        categoryList.layoutManager= GridLayoutManager(this, 2)

        val cb={ i: VolumeInfo -> onButtonShowPopupWindowClick(binding.root,i)}
        adapter= ListAdapter(this,data,cb )

        categoryList.adapter=adapter

        val spanCount = 2// 2 columns
        val spacing = 100 // 50px
        val includeEdge = true

        categoryList.addItemDecoration(RecyclerViewDecoration(spanCount, spacing, includeEdge))


        categoryList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    ++page
                    loadBooks(page)
                }
            }
        })

    }
}