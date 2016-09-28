package net.dunyun.framework.android.mainapp.util;

import android.content.Context;
import android.media.MediaPlayer;

public class MediaPlayerUtil {

	private static MediaPlayerUtil instance;
	private static MediaPlayer playMedia = null;

	private MediaPlayerUtil() {
	}

	public static synchronized MediaPlayerUtil getInstance() {
		if (instance == null) {
			instance = new MediaPlayerUtil();
		}
		return instance;
	}

	public void start(Context context, int res) {
		if (null == playMedia) {
			playMedia = MediaPlayer.create(context, res);
		}else{
			if (playMedia.isPlaying()) {
				playMedia.stop();
			}
			playMedia.release();
			playMedia = MediaPlayer.create(context, res);
		}
		playMedia.start();
	}
}
