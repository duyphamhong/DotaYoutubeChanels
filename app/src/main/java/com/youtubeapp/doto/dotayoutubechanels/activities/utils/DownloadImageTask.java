package com.youtubeapp.doto.dotayoutubechanels.activities.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

	ImageView bmImage;
	private IDownLoadImageCompletedListener listener;

	public DownloadImageTask(ImageView bmImage, IDownLoadImageCompletedListener listener) {
		this.bmImage = bmImage;
		this.listener = listener;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		// TODO Auto-generated method stub
		String urldisplay = params[0];
		Bitmap mIcon11 = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);

			new CachingBitmaps().addBitmapToMemoryCache(params[0], mIcon11);

		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon11;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		bmImage.setImageBitmap(result);
		if(listener!=null)
			listener.onDownloadCompleted(result);
	}

	public interface IDownLoadImageCompletedListener{
		void onDownloadCompleted(Bitmap bitmap);
	}
}
