package com.Appisos.apps.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.Appisos.apps.R;

/**
 * Created by David on 13/10/2015.
 */

public class RegisterFragment2 extends Fragment {
    private EditText rNameUser;
    private EditText rPassUser;
    private String nameUser;
    private String passUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View view = inflater.inflate(R.layout.register_fragment2, container, false);


        return inflater.inflate(R.layout.register_fragment2, container, false);

    }
}
