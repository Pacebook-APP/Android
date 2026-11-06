package com.standard.pacebook.data.repository

import android.util.Log
import com.standard.pacebook.data.model.RecruitingPost
import com.standard.pacebook.data.model.RecruitingPostResponse
import com.standard.pacebook.data.network.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class CommunityRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun getRecruitingPosts(): List<RecruitingPost>? = coroutineScope {
        try {
            // ID 1과 3에 대한 API 호출을 동시에 시작
            val deferredPost1 = async { apiService.getRecruitmentPostById(4) }
            val deferredPost3 = async { apiService.getRecruitmentPostById(5) }

            val posts = mutableListOf<RecruitingPost>()

            // 첫 번째 API 호출 결과 처리
            val response1 = deferredPost1.await()
            if (response1.isSuccessful) {
                response1.body()?.let { posts.add(it.toRecroutingPost()) }
            }

            // 두 번째 API 호출 결과 처리
            val response3 = deferredPost3.await()
            if (response3.isSuccessful) {
                response3.body()?.let { posts.add(it.toRecroutingPost()) }
            }

            posts
        } catch (e: Exception) {
            // 네트워크 오류 등 예외 발생 시 null 반환
            Log.e("CommunityRepository", "Failed to fetch posts", e)
            null
        }
    }

    suspend fun createRecruitingPost(post: RecruitingPost): RecruitingPost? {
        val response = apiService.createRecruitingPost(post)
        return if (response.isSuccessful) {
            response.body()
        } else {
            Log.e("CommunityRepository", "Failed to create recruitment post: ${response.code()}")
            null
        }
    }
}

// RecruitingPostResponse 객체를 RecruitingPost 객체로 변환하는 확장 함수
fun RecruitingPostResponse.toRecroutingPost(): RecruitingPost {
    return RecruitingPost(
        id = this.id,
        title = this.title,
        scheduled_time = this.scheduled_time,
        location_lat = this.location_lat,
        location_lng = this.location_lng,
        pace = this.pace,
        distance_km = this.distance_km,
        description = this.description
    )
}