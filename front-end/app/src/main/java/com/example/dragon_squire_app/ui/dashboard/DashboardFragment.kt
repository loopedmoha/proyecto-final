package com.example.dragon_squire_app.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dragon_squire_app.databinding.FragmentDashboardBinding
import com.example.dragon_squire_app.models.stats.SingleAttribute
import com.example.dragon_squire_app.models.stats.toSingleAttributes
import com.example.dragon_squire_app.utils.SessionData

class DashboardFragment : Fragment(), AttributeListListener {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val player = SessionData.player
        Log.d("DashboardFragment", "onCreateView: ${SessionData.player}")
        /*val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/


        val adapter = AttributesAdapter(player.characterStats.attributes.toSingleAttributes().toMutableList(), this)
        binding.textCharacterViewHp.text = player.characterStats.hitPoints.toString()
        binding.textCharacterViewCa.text = player.characterStats.armorClass.toString()
        binding.textCharacterViewSpeed.text = player.characterStats.initiative.toString()
        binding.textCharacterViewName.text = player.name
        binding.recyclerView3.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerView3.adapter = adapter

        Log.d("DashboardFragment", "onCreateView: ${SessionData.player.characterStats.attributes.toSingleAttributes().toMutableList()}")
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCharacterClicked(attribute: SingleAttribute) {

        SessionData.wsDice.send(quickRoll())
        Log.d("DashboardFragment", "onCharacterClicked: ${attribute.name}")
    }

    private fun quickRoll() : String{
        return "0;0;0;0;0;1;0;1"
    }
}