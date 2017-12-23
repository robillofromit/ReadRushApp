package com.robillo.readrush.ui.rushread.custom_dialog;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.robillo.readrush.R;
import com.robillo.readrush.di.component.ActivityComponent;
import com.robillo.readrush.ui.base.BaseFragment;
import com.robillo.readrush.ui.main.library.LibraryFragment;
import com.robillo.readrush.ui.rushread.ReadRushActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomFragment extends BaseFragment implements CustomFragmentMvpView {

    @BindView(R.id.size1)
    ImageButton mSizeOne;

    @BindView(R.id.size2)
    ImageButton mSizeTwo;

    @BindView(R.id.size3)
    ImageButton mSizeThree;

    @BindView(R.id.size4)
    ImageButton mSizeFour;

    @BindView(R.id.font1raleway)
    TextView mFont1Raleway;

    @BindView(R.id.font2tiempo)
    TextView mFont2Tiempo;

    @BindView(R.id.font3comfortaa)
    TextView mFont3Comfortaa;

    @BindView(R.id.font4georgia)
    TextView mFont4Georgia;

    @BindView(R.id.font5rounded)
    TextView mFont5Rounded;

    public CustomFragment() {
        // Required empty public constructor
    }

    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_custom, container, false);

        ActivityComponent component = getActivityComponent();
        if(component!=null) setUnBinder(ButterKnife.bind(this, v));

        return v;
    }

    @Override
    protected void setUp(View view) {

    }

    @OnClick(R.id.size1)
    public void setmSizeOne() {
        if(getActivity()!=null) ((ReadRushActivity) getActivity()).setFontSize(1);
    }

    @OnClick(R.id.size2)
    public void setmSizeTwo() {
        if(getActivity()!=null) ((ReadRushActivity) getActivity()).setFontSize(2);
    }

    @OnClick(R.id.size3)
    public void setmSizeThree() {
        if(getActivity()!=null) ((ReadRushActivity) getActivity()).setFontSize(3);
    }

    @OnClick(R.id.size4)
    public void setmSizeFour() {
        if(getActivity()!=null) ((ReadRushActivity) getActivity()).setFontSize(4);
    }

    @OnClick(R.id.font1raleway)
    public void setmFont1Raleway() {
        if(getActivity()!=null) ((ReadRushActivity) getActivity()).setFontPath("font1");
    }

    @OnClick(R.id.font2tiempo)
    public void setmFont2Tiempo() {
        if(getActivity()!=null) ((ReadRushActivity) getActivity()).setFontPath("font2");
    }

    @OnClick(R.id.font3comfortaa)
    public void setmFont3Comfortaa() {
        if(getActivity()!=null) ((ReadRushActivity) getActivity()).setFontPath("font3");
    }

    @OnClick(R.id.font4georgia)
    public void setmFont4Georgia() {
        if(getActivity()!=null) ((ReadRushActivity) getActivity()).setFontPath("font4");
    }

    @OnClick(R.id.font5rounded)
    public void setmFont5Rounded() {
        if(getActivity()!=null) ((ReadRushActivity) getActivity()).setFontPath("font5");
    }

    @OnClick(R.id.max_brightness)
    public void setmMaxBrightness() {
        if(getActivity()!=null) ((ReadRushActivity) getActivity()).setLightTheme();
    }

    @OnClick(R.id.min_brightness)
    public void setmMinBrightness() {
        if(getActivity()!=null) ((ReadRushActivity) getActivity()).setDarkTheme();
    }
}
