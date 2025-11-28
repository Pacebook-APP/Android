package com.standard.pacebook.data.network

import com.standard.pacebook.data.model.RecruitingPost
import com.standard.pacebook.data.model.RecruitingPostResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // {id} 부분에 숫자를 받아 동적으로 주소를 생성
    @GET("api/v1/runningmates/{id}")
    suspend fun getRecruitmentPostById(@Path("id") id: Int): Response<RecruitingPostResponse>

    // 새 모집글을 생성하는 API
    @POST("api/v1/runningmates/")
    suspend fun createRecruitingPost(@Body post: RecruitingPost): Response<RecruitingPost>
}