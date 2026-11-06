package com.standard.pacebook.ui.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.standard.pacebook.data.model.RecruitingPost
import com.standard.pacebook.data.repository.CommunityRepository
import kotlinx.coroutines.launch

class CommunityViewModel : ViewModel() {

    private val repository = CommunityRepository()

    private val _recruitingPosts = MutableLiveData<List<RecruitingPost>>()
    val recruitingPosts: LiveData<List<RecruitingPost>> get() = _recruitingPosts

    fun fetchRecruitingPosts() {
        viewModelScope.launch {
            val posts = repository.getRecruitingPosts()
            _recruitingPosts.postValue(posts)
        }
    }

    // 서버 통신 없이, 데모용 더미 게시글을 현재 리스트에 추가하는 함수
    fun addDummyPost(post: RecruitingPost) {
        val currentList = _recruitingPosts.value?.toMutableList() ?: mutableListOf()
        currentList.add(0, post) // 리스트의 맨 앞에 추가
        _recruitingPosts.value = currentList
    }

    fun createRecruitingPost(post: RecruitingPost) {
        viewModelScope.launch {
            repository.createRecruitingPost(post)
            // 새 글 작성 후 목록을 새로고침합니다.
            fetchRecruitingPosts()
        }
    }
}