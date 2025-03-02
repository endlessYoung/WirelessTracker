package com.example.wirelesstracker.ui.home

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.wirelesstracker.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    companion object {
        private const val TAG = "HomeFragment"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        Log.i(TAG, "binding: ${binding.root}")

        checkAndRequestPermissions()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.wifiStatus.observe(viewLifecycleOwner) {
            binding.wifiStatus.text = it
        }
        homeViewModel.ipStatus.observe(viewLifecycleOwner) {
            binding.ipStatus.text = it
        }
    }

    private fun checkAndRequestPermissions() {
//        val context = requireContext()
//        if (ContextCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                LOCATION_PERMISSION_REQUEST_CODE
//            )
//        } else {
        homeViewModel.initWifiManager(requireActivity()) // 有权限才初始化
//        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            homeViewModel.initWifiManager(requireActivity()) // 获取到权限后初始化
        } else {
            Log.e(TAG, "Location permission denied, cannot access Wi-Fi info")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
