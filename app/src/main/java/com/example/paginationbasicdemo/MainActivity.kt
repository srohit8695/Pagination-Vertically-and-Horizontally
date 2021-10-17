package com.example.paginationbasicdemo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    val numberList :  MutableList<String> = ArrayList()

    var page = 1
    var isLoading = false
    val limit = 10
    val areas = arrayOf("Adambakkam", "Adyar", "Alandur", "Alapakkam", "Alwarpet", "Alwarthirunagar", "Ambattur", "Aminjikarai", "Anna Nagar", "Annanur", "Arumbakkam", "Ashok Nagar", "Avadi", "Ayanavaram", "Besant Nagar", "Basin Bridge", "Chepauk", "Chetput", "Chintadripet", "Chitlapakkam", "Choolai", "Choolaimedu", "Chrompet", "Egmore", "Ekkaduthangal", "Eranavur", "Ennore", "Foreshore Estate", "Fort St. George", "George Town", "Gopalapuram", "Government Estate", "Guindy", "Gerugambakkam", "IIT Madras", "Injambakkam", "ICF", "Iyyapanthangal", "Jafferkhanpet", "Karapakkam", "Kattivakkam", "Kattupakkam", "Kazhipattur", "K.K. Nagar", "Keelkattalai", "Kattivakkam", "Kilpauk", "Kodambakkam", "Kodungaiyur", "Kolathur", "Korattur", "Korukkupet", "Kottivakkam", "Kotturpuram", "Kottur", "Kovilambakkam", "Koyambedu", "Kundrathur", "Madhavaram", "Madhavaram Milk Colony", "Madipakkam", "Madambakkam", "Maduravoyal", "Manali", "Manali New Town", "Manapakkam", "Mandaveli", "Mangadu", "Mannady", "Mathur", "Medavakkam", "Meenambakkam", "MGR Nagar", "Minjur", "Mogappair", "MKB Nagar", "Mount Road", "Moolakadai", "Moulivakkam", "Mugalivakkam", "Mudichur", "Mylapore", "Nandanam", "Nanganallur", "Nanmangalam", "Neelankarai", "Nemilichery", "Nesapakkam", "Nolambur", "Noombal", "Nungambakkam", "Otteri", "Padi", "Pakkam", "Palavakkam", "Pallavaram", "Pallikaranai", "Pammal", "Park Town", "Parry's Corner", "Pattabiram", "Pattaravakkam", "Pazhavanthangal", "Peerkankaranai", "Perambur", "Peravallur", "Perumbakkam", "Perungalathur", "Perungudi", "Pozhichalur", "Poonamallee", "Porur", "Pudupet", "Pulianthope", "Purasaiwalkam", "Puthagaram", "Puzhal", "Puzhuthivakkam/ Ullagaram", "Raj Bhavan", "Ramavaram", "Red Hills", "Royapettah", "Royapuram", "Saidapet", "Saligramam", "Santhome", "Sembakkam", "Selaiyur", "Shenoy Nagar", "Sholavaram", "Sholinganallur", "Sithalapakkam", "Sowcarpet", "St.Thomas Mount", "Surapet", "Tambaram", "Teynampet", "Tharamani", "T. Nagar", "Thirumangalam", "Thirumullaivoyal", "Thiruneermalai", "Thiruninravur", "Thiruvanmiyur", "Tiruverkadu", "Thiruvotriyur", "Thuraipakkam", "Tirusulam", "Tiruvallikeni", "Tondiarpet", "United India Colony", "Vandalur", "Vadapalani", "Valasaravakkam", "Vallalar Nagar", "Vanagaram", "Velachery", "Velappanchavadi", "Villivakkam", "Virugambakkam", "Vyasarpadi", "Washermanpet", "West Mambalam")

    lateinit var adapter: NumberAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.mainRecyclerView)
        progressBar = findViewById(R.id.progressBar)

        title = "Chennai Hotels"

        layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

        getPage()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (dy>0) {
                    val visibleCount = layoutManager!!.childCount
                    val pastVisibleItem = layoutManager!!.findFirstCompletelyVisibleItemPosition()
                    val total = adapter.itemCount

                    if(!isLoading){
                        if((visibleCount + pastVisibleItem) >= total){
                            page++
                            getPage()
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

    }

    fun getPage(){

        isLoading = true
        progressBar.visibility = View.VISIBLE

        val start = (page - 1) * limit
        var end = page * limit - 1

        if(areas.size<end){
            end = areas.size-1
        }

        for(i in start..end){
            numberList.add(areas.get(i))
        }

        Handler().postDelayed({
            if(::adapter.isInitialized){
                adapter.notifyDataSetChanged()
            }
            else{
                adapter = NumberAdapter(this)
                recyclerView.adapter = adapter
            }
            progressBar.visibility = View.GONE
            isLoading = false
        },1000)

    }

    class NumberAdapter(val activity: MainActivity) : RecyclerView.Adapter<NumberAdapter.NumberViewHolder>(){



        class NumberViewHolder (v : View) : RecyclerView.ViewHolder(v){
                val areaName = v.findViewById<TextView>(R.id.headLine)
                val subRecyclerView = v.findViewById<RecyclerView>(R.id.subRecyclerView)
                val progressBar = v.findViewById<ProgressBar>(R.id.subProgressBar)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
            return NumberViewHolder(LayoutInflater.from(activity).inflate(R.layout.recycler_view_row,parent,false))
        }

        override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
            lateinit var childAdapter : ChildAdapter
            val subNumberList :  MutableList<String> = ArrayList()
            var page = 1
            var isLoading = false
            val limit = 10
            var layoutManager: LinearLayoutManager
            holder.areaName.text = activity.numberList[position]
            childAdapter = ChildAdapter(activity, subNumberList)
            holder.subRecyclerView.adapter = childAdapter
            getChildPage(holder.subRecyclerView,subNumberList,page,isLoading,limit,childAdapter,holder.progressBar)

            layoutManager = LinearLayoutManager(activity)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            holder.subRecyclerView.layoutManager = layoutManager

            holder.subRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    val visibleCount = layoutManager!!.childCount
                    val pastVisibleItem = layoutManager!!.findFirstCompletelyVisibleItemPosition()
                    val total = childAdapter.itemCount

                    if(!isLoading){
                        if((visibleCount + pastVisibleItem) >= total){
                            page++
                            getChildPage(holder.subRecyclerView,subNumberList,page,isLoading,limit,childAdapter,holder.progressBar)
                        }
                    }

                    super.onScrolled(recyclerView, dx, dy)
                }
            })

        }

        override fun getItemCount(): Int {
           return activity.numberList.size
        }

        fun getChildPage(recyclerView : RecyclerView, subNumberList : MutableList<String>, page : Int, isLoading : Boolean, limit : Int, childAdapter : ChildAdapter, progressBar : ProgressBar){
//            isLoading = true
            progressBar.visibility = View.VISIBLE

            val start = (page - 1) * limit
            val end = page * limit - 1

            for(i in start..end){
                subNumberList.add("Horizontal $i")
            }



            Handler().postDelayed({
            childAdapter.notifyDataSetChanged()
                /*if(::childAdapter.isInitialized){
                    childAdapter.notifyDataSetChanged()
                }
                else{
                    childAdapter = ChildAdapter(activity, subNumberList)
                    recyclerView.adapter = childAdapter
                }*/
                progressBar.visibility = View.GONE
//                isLoading = false
            },1000)

        }


    }

    class ChildAdapter(val activity: MainActivity,var subNumberList : MutableList<String>) : RecyclerView.Adapter<ChildAdapter.ChildViewHolder>(){

        class ChildViewHolder (v : View) : RecyclerView.ViewHolder(v){
            val shopName = v.findViewById<TextView>(R.id.shopName)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
            return ChildViewHolder(LayoutInflater.from(activity).inflate(R.layout.recyclerview_sub_rows, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
            holder.shopName.text = subNumberList[position]
        }

        override fun getItemCount(): Int {
            return subNumberList.size
        }


    }

}