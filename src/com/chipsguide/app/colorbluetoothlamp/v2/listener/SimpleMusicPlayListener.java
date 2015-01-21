package com.chipsguide.app.colorbluetoothlamp.v2.listener;



public interface SimpleMusicPlayListener {
	void onMusicProgress(long duration,
			long currentDuration, int percent);
	
	void onMusicPlayStateChange(boolean playing);
	
	void onMusicChange();
	
}
