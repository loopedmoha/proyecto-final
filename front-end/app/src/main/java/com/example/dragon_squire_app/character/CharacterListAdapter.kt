package com.example.dragon_squire_app.character

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dragon_squire_app.R
import com.example.dragon_squire_app.chat.adapters.MessageAdapter
import com.example.dragon_squire_app.chat.models.Message
import com.example.dragon_squire_app.databinding.CharacterItemViewBinding
import com.example.dragon_squire_app.models.player.Player
import kotlinx.coroutines.NonDisposableHandle.parent

class CharacterListAdapter(private val characters: MutableList<Player>, private val listener: CharacterListListener) :
    RecyclerView.Adapter<CharacterListAdapter.CharacterViewHolder>() {
    val charactersViewModel = CharacterListViewModel()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.character_item_view, parent, false)
        return CharacterViewHolder(view)


    }

    override fun getItemCount(): Int {
        return characters.size
    }


    fun add(character: Player) {
        characters.add(character)
        notifyItemInserted(characters.size - 1)
    }

    fun clear(){
        characters.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characters[position])
        holder.setListener(characters[position])
    }

    inner class CharacterViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private var characterName : TextView = itemView.findViewById(R.id.text_character_item_name) as TextView
        private var characterClass : TextView = itemView.findViewById(R.id.text_character_item_class) as TextView
        private var characterLevel : TextView = itemView.findViewById(R.id.text_character_item_level) as TextView
        private var characterRace : TextView = itemView.findViewById(R.id.text_character_item_race) as TextView

        fun bind(player: Player) {
            characterName.text = player.name
            characterClass.text = player.characterClass.name
            characterLevel.text = player.level.toString()
            characterRace.text = player.race.name

            view.setOnClickListener {
                Log.d("CharacterListAdapter", "Character clicked: $player")

            }
        }

        fun setListener(player: Player){
            view.setOnClickListener {
                listener.onCharacterClicked(player)
            }
        }
    }
}

interface CharacterListListener {
    fun onCharacterClicked(player: Player)
}