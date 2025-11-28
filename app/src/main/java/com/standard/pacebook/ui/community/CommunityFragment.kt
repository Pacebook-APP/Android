package com.standard.pacebook.ui.community

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.standard.pacebook.data.model.RecruitingPost
import com.standard.pacebook.databinding.FragmentCommunityBinding
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CommunityViewModel by viewModels()
    private lateinit var adapter: RecruitingPostAdapter

    private val addPostLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val newPost = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableExtra("newPost", RecruitingPost::class.java)
            } else {
                result.data?.getParcelableExtra("newPost")
            }
            
            if (newPost != null) {
                viewModel.addDummyPost(newPost)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        binding.fabAddPost.setOnClickListener {
            val intent = Intent(requireContext(), AddPostActivity::class.java)
            addPostLauncher.launch(intent)
        }

        viewModel.fetchRecruitingPosts()
    }

    private fun setupRecyclerView() {
        adapter = RecruitingPostAdapter(emptyList()) { post ->
            val dialog = MapDetailDialogFragment.newInstance(
                post.location_lat.toDoubleOrNull() ?: 0.0,
                post.location_lng.toDoubleOrNull() ?: 0.0
            )
            dialog.show(childFragmentManager, "MapDetailDialog")
        }
        binding.recyclerViewCommunity.adapter = adapter
        binding.recyclerViewCommunity.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        viewModel.recruitingPosts.observe(viewLifecycleOwner) { posts ->
            adapter.updatePosts(posts ?: emptyList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}