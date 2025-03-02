package com.example.wirelesstracker.ui.home

import android.content.Context
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates

class HomeViewModel : ViewModel() {
    companion object {
        private const val TAG = "HomeViewModel"
    }

    private var wifiManager: WifiManager? = null
    private var wifiInfo: WifiInfo? = null

    private var ssid: String = "Unknown"
    private var bssid: String = "Unknown"
    private var frequency by Delegates.notNull<Int>()
    private var rssi by Delegates.notNull<Int>()

    private val _wifiStatus = MutableLiveData<String>()
    val wifiStatus: LiveData<String> = _wifiStatus

    private val _ipStatus = MutableLiveData<String>()
    val ipStatus: LiveData<String> = _ipStatus

    fun initWifiManager(context: Context?) {
        context?.let {
            wifiManager = it.getSystemService(Context.WIFI_SERVICE) as WifiManager

            try {
                wifiInfo = wifiManager?.connectionInfo
                wifiInfo?.let { info ->
                    ssid = info.ssid
                    bssid = info.bssid
                    frequency = info.frequency
                    rssi = info.rssi

                    val ipAddress = intToIp(info.ipAddress)

                    _wifiStatus.postValue("Wi-Fi connection: $ssid ($bssid)")
                    _ipStatus.postValue("IP: $ipAddress")
                }
            } catch (e: Exception) {
                Log.e(TAG, "initWifiManager error: ${e.message}")
            }
        }
    }
}

private fun intToIp(ipInt: Int): String {
    return buildString {
        append(ipInt and 0xFF).append(".")
        append(ipInt shr 8 and 0xFF).append(".")
        append(ipInt shr 16 and 0xFF).append(".")
        append(ipInt shr 24 and 0xFF)
    }
}
