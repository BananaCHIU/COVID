package com.example.covid.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.covid.R;
import com.google.firebase.perf.metrics.AddTrace;

public class MapFragment extends Fragment {

    @AddTrace(name = "MapOnCreateViewTrace", enabled = true)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        final WebView web_map = root.findViewById(R.id.web_map);
        WebSettings webSettings = web_map.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web_map.loadUrl("https://www.google.com/maps/d/embed?mid=1yQgFpsLoh8sdQ-2WXj7VftuDXE6c8dzq");
        return root;
    }
}
