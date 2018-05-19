package com.Appisos.apps.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.Appisos.apps.R;

/**
 * Created by apps on 13/10/2015.
 */
public class RegisterFragment extends Fragment {
    private Button next;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.register_fragment2, container, false);

        return inflater.inflate(R.layout.register_fragment, container, false);

    }
}
