package com.hht.floatbar.module.setting.fragment.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hht.floatbar.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Realmo
 * @version 1.0.0
 * @name FloatBarMenu
 * @email momo.weiye@gmail.com
 * @time 2019/9/19 16:07
 * @describe
 */
public class FloatBarFilterAppFragment extends Fragment {





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_app,container,false);
    }
}
