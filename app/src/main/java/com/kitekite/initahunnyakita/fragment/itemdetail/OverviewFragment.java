package com.kitekite.initahunnyakita.fragment.itemdetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kitekite.initahunnyakita.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by tinklabs on 2/5/2015.
 */
public class OverviewFragment extends Fragment{
    PhotoViewAttacher mAttacher;
    PhotoView image;
    float [] zoomScales = new float[]{1f,1.44f,2.07f,3f};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_item_detail_overview, container, false);
        image = (PhotoView) fragmentView.findViewById(R.id.overview_img);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Picasso.with(getActivity())
                .load(getArguments().getString("url"))
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        mAttacher = new PhotoViewAttacher(image);
                        mAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        mAttacher.setZoomable(false);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public void setZoomEnabled(boolean enabled){
        if(mAttacher != null){
            mAttacher.setZoomable(enabled);
        }
    }

    public boolean canZoom(){
        if(mAttacher!=null)
            return mAttacher.canZoom();
        return false;
    }

    public void zoomIn(){
        for(int i=0; i<zoomScales.length;i++){
            if(zoomScales[i]>mAttacher.getScale()){
                mAttacher.setScale(zoomScales[i], true);
                break;
            }
        }
    }

    public void zoomOut(){
        for(int i=zoomScales.length-1; i>=0;i--){
            if(zoomScales[i]<mAttacher.getScale()){
                mAttacher.setScale(zoomScales[i], true);
                break;
            }
        }
    }
}
