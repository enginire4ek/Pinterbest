package com.example.pinterbest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pinterbest.adapters.PinFeedHomeAdapter
import com.example.pinterbest.data.PinObject
import com.example.pinterbest.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.creators.setOnClickListener {
            val navHostFragment = requireActivity().supportFragmentManager
                .findFragmentById(R.id.NavHostFragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController
            navController.navigate(R.id.action_homeFragment_to_creatorsFragment)
        }

        val feedAdapter = PinFeedHomeAdapter(
            listOf(
                PinObject(R.drawable.help_image_pin, "lavander luberon"),
                PinObject(R.drawable.help_image_pin, "lavander luberon"),
                PinObject(R.drawable.help_image_pin, "lavander luberon"),
                PinObject(R.drawable.help_image_pin, "lavander luberon"),
                PinObject(R.drawable.help_image_pin, "lavander luberon"),
                PinObject(R.drawable.help_image_pin, "lavander luberon"),
                PinObject(R.drawable.help_image_pin, "lavander luberon"),
                PinObject(R.drawable.help_image_pin, "lavander luberon"),
                PinObject(R.drawable.help_image_pin, "lavander luberon"),
                PinObject(R.drawable.help_image_pin, "lavander luberon"),
                PinObject(R.drawable.help_image_pin, "lavander luberon"),
                PinObject(R.drawable.help_image_pin, "lavander luberon")
            )
        )
        binding.rvPins.apply {
            adapter = feedAdapter
            layoutManager = GridLayoutManager(context, GRID_COLUMNS)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val GRID_COLUMNS = 2
    }
}
