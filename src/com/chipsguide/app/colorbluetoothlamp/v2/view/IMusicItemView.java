package com.chipsguide.app.colorbluetoothlamp.v2.view;

import android.content.Context;
import android.widget.FrameLayout;

import com.chipsguide.app.colorbluetoothlamp.v2.bean.Music;

public abstract class IMusicItemView extends FrameLayout {

	public IMusicItemView(Context context) {
		super(context);
	}

	public abstract void render(int index, Music music, boolean blzDeviceMusic);
	
	public abstract void setSelected(boolean playing);
	
	public abstract void disSelected() ;
	
}