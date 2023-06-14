package com.example.dragon_squire_app.ui.dashboard

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dragon_squire_app.R
import com.example.dragon_squire_app.models.stats.SingleAttribute

class AttributesAdapter(private val attributes: MutableList<SingleAttribute>, private val listener: AttributeListListener)
    : RecyclerView.Adapter<AttributesAdapter.AttributeViewHolder>() {
        val dashboardViewModel = DashboardViewModel()


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttributeViewHolder {

            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.attribute_item_view, parent, false)
            return AttributeViewHolder(view)


        }

        override fun getItemCount(): Int {
            return attributes.size
        }


        fun add(attribute: SingleAttribute) {
            attributes.add(attribute)
            notifyItemInserted(attributes.size - 1)
        }

        fun clear(){
            attributes.clear()
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: AttributeViewHolder, position: Int) {
            holder.bind(attributes[position])
            holder.setListener(attributes[position])
        }

        inner class AttributeViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
            private var attributeName : TextView = itemView.findViewById(R.id.text_attribute_view_name)
            private var attributeValue : TextView = itemView.findViewById(R.id.text_attribute_view_value)
            private var attributeModifier : TextView = itemView.findViewById(R.id.text_attribute_view_mod)
            fun bind(attribute: SingleAttribute) {

                attributeName.text = attribute.name
                attributeValue.text = attribute.value.toString()
                attributeModifier.text = attribute.modifier.toString()

                view.setOnClickListener {
                    Log.d("CharacterListAdapter", "Character clicked: $attribute")

                }
            }

            fun setListener(attribute: SingleAttribute){
                view.setOnClickListener {
                    listener.onCharacterClicked(attribute)
                }
            }
        }
    }

    interface AttributeListListener {
        fun onCharacterClicked(attribute: SingleAttribute)}