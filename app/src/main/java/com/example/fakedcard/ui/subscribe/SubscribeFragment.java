package com.example.fakedcard.ui.subscribe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.fakedcard.R;

public class SubscribeFragment extends Fragment {

    private SubscribeViewModel subscribeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        subscribeViewModel =
                ViewModelProviders.of(this).get(SubscribeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_subscribe, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        subscribeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
