package com.standard.pacebook.ui.community

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.standard.pacebook.data.model.RecruitingPost
import com.standard.pacebook.databinding.ActivityAddPostBinding
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class AddPostActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityAddPostBinding
    private lateinit var mapView: MapView
    private var naverMap: NaverMap? = null
    
    // 선택된 위치 좌표를 저장할 변수
    private var selectedLatLng: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView = binding.mapViewAddPost
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        binding.buttonSave.setOnClickListener {
            savePost()
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        // 초기 위치를 서울시청으로 설정
        val initialPosition = LatLng(37.5665, 126.9780)
        naverMap.moveCamera(CameraUpdate.scrollTo(initialPosition))
        
        // 초기 좌표 저장
        selectedLatLng = initialPosition

        // 카메라 이동이 멈출 때마다 중앙 좌표를 업데이트
        naverMap.addOnCameraIdleListener {
            selectedLatLng = naverMap.cameraPosition.target
            binding.textViewLocationInfo.text = "선택된 위치: ${selectedLatLng!!.latitude}, ${selectedLatLng!!.longitude}"
        }
    }

    private fun savePost() {
        val title = binding.editTextTitle.text.toString().trim()
        val description = binding.editTextDescription.text.toString().trim()
        val distance = binding.editTextDistance.text.toString()
        val pace = binding.editTextPace.text.toString()

        if (title.isEmpty() || description.isEmpty() || distance.isEmpty() || pace.isEmpty() || selectedLatLng == null) {
            Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val newPost = RecruitingPost(
            id = System.currentTimeMillis().toInt(),
            title = title,
            description = description,
            location_lat = String.format("%.6f", selectedLatLng!!.latitude),
            location_lng = String.format("%.6f", selectedLatLng!!.longitude),
            distance_km = distance.toIntOrNull() ?: 0,
            pace = pace.toIntOrNull() ?: 0,
            scheduled_time = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        )

        val resultIntent = Intent()
        resultIntent.putExtra("newPost", newPost)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    // MapView 생명주기 관리
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
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

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}