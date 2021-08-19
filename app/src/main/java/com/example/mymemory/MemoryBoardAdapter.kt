package com.example.mymemory

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mymemory.models.BoardSize
import com.example.mymemory.models.MemoryCard
import com.squareup.picasso.Picasso
import kotlin.math.min

class MemoryBoardAdapter(
    private val context: Context,
    private val boardSize: BoardSize,
    private val cards: List<MemoryCard>,
    private val cardClickListener: CardClickListener

) :
    RecyclerView.Adapter<MemoryBoardAdapter.ViewHolder>() {
    private val TAG = "MemoryBoardAdapter"
    companion object{
        private const val MARGIN_SIZE = 10
        private const val TAG = "MemoryBoardAdapter"

    }
    interface CardClickListener {
        fun onCardClicked(position: Int)
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)

        fun bind(position: Int) {
            Log.d(TAG, "bind: bind")
            val memoryCard = cards[position]
           if(memoryCard.isFaceUp){
               if (memoryCard.imageUrl != null){
                   Picasso.get().load(memoryCard.imageUrl).placeholder(R.drawable.ic_image).into(imageButton)
               }else{
                   imageButton.setImageResource(memoryCard.identifier)
               }
           }else{
               imageButton.setImageResource(R.drawable.beige)
           }

           /* if (memoryCard.isFaceUp){
                imageButton.setImageResource(memoryCard.identifier)
            }else {
                imageButton.setImageResource(R.drawable.ic_launcher_background)
            }*/
            imageButton.alpha = if (memoryCard.isMatched) .4f else 1f
            val colorStateList = if (memoryCard.isMatched) ContextCompat.getColorStateList(context, R.color.color_gray) else null
            ViewCompat.setBackgroundTintList(imageButton, colorStateList)
            imageButton.setOnClickListener {

               /* if(memoryCard.isFaceUp){
                    //do nothing
                }else{
                    memoryCard.isFaceUp = true
                    imageButton.setImageResource(cards[position].identifier)
                }*/
                Log.i(TAG, "Clicked on position $position")
                cardClickListener.onCardClicked(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "bind: onCreateViewHolder")
        val cardWidth = parent.width / boardSize.getWidth()  -(2* MARGIN_SIZE)
        val cardHeight = parent.height / boardSize.getHeight() -(2* MARGIN_SIZE)
        val cardSideLength = min(cardWidth, cardHeight)
        val view = LayoutInflater.from(context).inflate(R.layout.memory_card, parent, false)
        val layoutParams = view.findViewById<CardView>(R.id.cardView).layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.width = cardSideLength
        layoutParams.setMargins(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE)
        layoutParams.height = cardSideLength
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "bind: onBindViewHolder")
        holder.bind(position)
    }

    override fun getItemCount(): Int = boardSize.numCards


}
