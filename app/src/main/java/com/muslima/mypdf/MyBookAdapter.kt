package com.muslima.mypdf

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.muslima.mypdf.MainActivity.Companion.BOOK_CATEGORY
import com.muslima.mypdf.MainActivity.Companion.BOOK_IMG
import com.muslima.mypdf.MainActivity.Companion.BOOK_TITLE
import com.muslima.mypdf.MainActivity.Companion.BOOK_URL
import com.muslima.mypdf.databinding.BookItemBinding
import com.squareup.picasso.Picasso

class MyBookAdapter(private val main: MainActivity, private val allData: MyData) :
    RecyclerView.Adapter<MyBookAdapter.MyBookViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyBookViewHolder {
        return MyBookViewHolder(
            BookItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: MyBookViewHolder,
        position: Int
    ) {
        val h: HashMap<String, String> = main.mainArrayLst[position]
        val bookImg = h[BOOK_IMG]
        val bookTitle = h[BOOK_TITLE]
        val bookUrl = h[BOOK_URL]
        val bookCategory = h[BOOK_CATEGORY]
        when (bookCategory) {
            "offline" -> {
                // handle offline book
                holder.binding.bookImg.setImageResource(bookImg?.toInt()!!)
                holder.binding.bookTitle.setTextColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.main_color)
                )

            }
            "online" -> {
                // handle online book
                Picasso.get()
                    .load(bookImg)
                    .placeholder(R.drawable.pdf)
                    .into(holder.binding.bookImg)
                holder.binding.bookTitle.setTextColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.blue_color)
                )
            }
        }
        holder.binding.bookTitle.text=bookTitle
        holder.binding.bookList.setOnClickListener {
            allData.myOonClick(bookUrl!!,bookCategory!!)
        }
    }

    override fun getItemCount(): Int {
        return main.mainArrayLst.size
    }

    class MyBookViewHolder(val binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root)
}