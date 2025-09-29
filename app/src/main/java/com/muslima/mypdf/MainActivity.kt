package com.muslima.mypdf

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import com.muslima.mypdf.PDFViewActivity.Companion.isOnline
import com.muslima.mypdf.databinding.ActivityMainBinding
import org.json.JSONException



@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), MyData {
    val ipAddress = "http://192.168.0.12:80"


    lateinit var map: HashMap<String, String>
    var mainArrayLst = ArrayList<HashMap<String, String>>()
    private var offlineArrayLst = ArrayList<HashMap<String, String>>()
    private var onlineArrayLst = ArrayList<HashMap<String, String>>()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = resources.getColor(R.color.main_color)
        window.navigationBarColor = resources.getColor(R.color.main_color)
        val drawerLayout = binding.main
        val toolbar = binding.toolbar
        val navigation = binding.navigation
        val myBookAdapter = MyBookAdapter(this, this)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.open, R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        navigation.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.appInfo -> {
                    Toast.makeText(this, "app info", Toast.LENGTH_LONG).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                }

                R.id.developerInfo -> {
                    Toast.makeText(this, "developer info", Toast.LENGTH_LONG).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                }

                R.id.privacyPolicy -> {
                    Toast.makeText(this, "privacy policy", Toast.LENGTH_LONG).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            true
        }
        offlineBooklist()
        onlineBookList()
        mainArrayLst = offlineArrayLst
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = myBookAdapter
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // When a tab is selected
                if (tab.position == 0){
                    mainArrayLst=offlineArrayLst
                    binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    binding.recyclerView.adapter = myBookAdapter
                }else if (tab.position == 1){
                    mainArrayLst=onlineArrayLst
                    binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    binding.recyclerView.adapter = myBookAdapter
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // When a tab is unselected
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // When a tab is reselected
            }
        })

    }

    private fun offlineBooklist() {
        offlineArrayLst.clear()

        val books = listOf(
            Triple("offline book 1", "1.pdf", "offline"),
            Triple("offline book 2", "2.pdf", "offline"),
            Triple("offline book 3", "3.pdf", "offline"),
            Triple("offline book 4", "4.pdf", "offline"),
            Triple("offline book 5", "5.pdf", "offline")
        )

        books.forEach { (title, url, category) ->
            map = hashMapOf(
                BOOK_IMG to "${R.drawable.pdf}",
                BOOK_TITLE to title,
                BOOK_URL to url,
                BOOK_CATEGORY to category
            )
            offlineArrayLst.add(map)
        }
    }

    override fun myOonClick(m: String,bookCategory: String) {
        val intercept = Intent(this, PDFViewActivity::class.java)
        intercept.putExtra(BOOK, m)
        startActivity(intercept)

        when (bookCategory) {
            "offline" -> {
                isOnline=false
            }
            "online" -> {
                // handle online book
                isOnline=true
            }
        }
    }

    private fun onlineBookList() {
        val url = "$ipAddress/book/book_list.json"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { res ->
                try {
                    for (x in 0 until res.length()) {
                        val obj = res.getJSONObject(x)
                        val category = obj.getString("category")
                        val bookName = obj.getString("name")
                        val bookImage = obj.getString("image")
                        val bookUrl = obj.getString("url")
                        map = hashMapOf()
                        map.put(BOOK_IMG, ipAddress + bookImage)
                        map.put(BOOK_TITLE, bookName)
                        map.put(BOOK_URL, ipAddress + bookUrl)
                        map.put(BOOK_CATEGORY, category)
                        onlineArrayLst.add(map)
                    }
                } catch (e: JSONException) {
                    throw RuntimeException(e)
                }
            }, { error ->
                Log.d("TAG", "onlineBookList: $error")
            })
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonArrayRequest)
    }

    companion object {
        const val BOOK_IMG = "book_img"
        const val BOOK_CATEGORY = "Category"
        const val BOOK_TITLE = "book_title"
        const val BOOK_URL = "book_url"
        const val BOOK = "book"
    }
}