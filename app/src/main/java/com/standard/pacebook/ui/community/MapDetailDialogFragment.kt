package com.standard.pacebook.ui.community

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.standard.pacebook.databinding.DialogMapDetailBinding

class MapDetailDialogFragment : DialogFragment(), OnMapReadyCallback {

    private var _binding: DialogMapDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private var naverMap: NaverMap? = null

    // 다이얼로그 내부에서만 사용할 참가 상태 변수
    private var isJoined = false

    private var lat: Double = 0.0
    private var lng: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            lat = it.getDouble(ARG_LAT)
            lng = it.getDouble(ARG_LNG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogMapDetailBinding.inflate(inflater, container, false)
        mapView = binding.mapViewDialog
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonClose.setOnClickListener {
            dismiss() // 다이얼로그 닫기
        }

        binding.buttonJoin.setOnClickListener {
            // 참가 상태를 반전시킴
            isJoined = !isJoined

            if (isJoined) {
                // 상태가 '참가중'일 때
                binding.buttonJoin.text = "참가 취소"
                Toast.makeText(requireContext(), "참가 신청이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                // 상태가 '미참가'일 때
                binding.buttonJoin.text = "참가"
                Toast.makeText(requireContext(), "참가 취소되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        val position = LatLng(lat, lng)
        Marker().apply {
            this.position = position
            this.map = naverMap
        }
        val cameraUpdate = CameraUpdate.scrollTo(position)
        naverMap.moveCamera(cameraUpdate)
    }

    // MapView 생명주기 관리
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
        _binding = null
    }

    companion object {
        private const val ARG_LAT = "latitude"
        private const val ARG_LNG = "longitude"

        fun newInstance(lat: Double, lng: Double): MapDetailDialogFragment {
            val fragment = MapDetailDialogFragment()
            val args = Bundle().apply {
                putDouble(ARG_LAT, lat)
                putDouble(ARG_LNG, lng)
            }
            fragment.arguments = args
            return fragment
        }
    }
}