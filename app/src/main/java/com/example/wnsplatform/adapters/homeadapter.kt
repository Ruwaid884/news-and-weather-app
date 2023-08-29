package com.example.wnsplatform.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wnsplatform.DataClassNews.NewsResponse
import com.example.wnsplatform.R
import com.example.wnsplatform.news.EntertainmentFragment
import com.example.wnsplatform.newsView

class homeadapter(val newslist:NewsResponse, private val context: Context): RecyclerView.Adapter<homeadapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val headline: TextView = itemView.findViewById(R.id.textView0)
        val description: TextView = itemView.findViewById(R.id.textView1)
        val image: ImageView = itemView.findViewById(R.id.image)
        val author: TextView = itemView.findViewById(R.id.textView2)
        val source: TextView = itemView.findViewById(R.id.textView3)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_card,
            parent, false
        )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.headline.text = newslist.articles[position].title
        holder.description.text = newslist.articles[position].description
        holder.author.text = newslist.articles[position].author
        holder.source.text = newslist.articles[position].source.name
        Glide.with(holder.itemView)
            .load(
                newslist.articles[position]
                    .urlToImage
            ).into(holder.image)
        holder.itemView.setOnClickListener{
           val intent = Intent(context,newsView::class.java)
            intent.putExtra("url",newslist.articles[position].url)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return newslist.articles.size
    }
}