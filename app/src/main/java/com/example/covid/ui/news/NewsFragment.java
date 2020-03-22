package com.example.covid.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.covid.R;
import com.google.android.material.snackbar.Snackbar;
import com.prof.rssparser.Channel;


public class NewsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private NewsRecyclerViewAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;
    private NewsViewModel viewModel;
    private RelativeLayout relativeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);

        viewModel = ViewModelProviders.of(this).get(NewsViewModel.class);

        progressBar = root.findViewById(R.id.progress_news);

        mRecyclerView = root.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(NewsFragment.this.getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        relativeLayout = root.findViewById(R.id.root_layout);

        viewModel.getChannel().observe(getViewLifecycleOwner(), new Observer<Channel>() {
            @Override
            public void onChanged(Channel channel) {
                if (channel != null) {
                    if (channel.getTitle() != null) {
                        //setTitle(channel.getTitle());
                    }
                    mAdapter = new NewsRecyclerViewAdapter(channel.getArticles(), NewsFragment.this.getContext());
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        viewModel.getSnackbar().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    Snackbar.make(relativeLayout, s, Snackbar.LENGTH_LONG).show();
                    viewModel.onSnackbarShowed();
                }
            }
        });

        mSwipeRefreshLayout = root.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primaryColor, R.color.primaryDarkColor);
        mSwipeRefreshLayout.canChildScrollUp();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mAdapter.getArticleList().clear();
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(true);
                viewModel.fetchFeed();
            }
        });

        viewModel.fetchFeed();

        return root;
    }
}
