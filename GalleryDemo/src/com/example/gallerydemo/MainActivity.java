package com.example.gallerydemo;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.swipeGesture.ActivitySwipeDetector;
import com.example.swipeGesture.SwipeInterface;

public class MainActivity extends Activity implements SwipeInterface { 
	private int count;
	Cursor mImageCursor;
	ImageView iv;
	Uri sourceUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	 //Uri sourceUri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iv=(ImageView)findViewById(R.id.iv_test);
		
		ActivitySwipeDetector swipe = new ActivitySwipeDetector(this);
		RelativeLayout swipe_layout = (RelativeLayout) findViewById(R.id.rl_swipe);
		iv.setOnTouchListener(swipe);
		
		mImageCursor = getContentResolver().query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null );
		
		   

}
	public void prev(View v)
	{
		unbindDrawables(v);
		Log.e("test","prev");
		v.setEnabled(false);
		if(mImageCursor.moveToPrevious())
		{
			
			setImage();

		}
		v.setEnabled(true);
	}
	
	public void next(View v)
	{
		unbindDrawables(v);
		Log.e("test","next");
		v.setEnabled(false);
		if(mImageCursor.moveToNext())
		{
			
			setImage();

		}
		v.setEnabled(true);
	}
	
	public void setImage()
	{
		String _id = mImageCursor.getString(mImageCursor.getColumnIndex(MediaStore.Images.Media._ID));
		Uri selUri = Uri.withAppendedPath(sourceUri, _id);
		Bitmap bitmap=decodeSampledBitmapFromResource(selUri,iv.getWidth(),iv.getHeight());
		//iv.setImageURI(selUri);
		iv.setImageBitmap(bitmap);
		Log.e("test","img path is :"+selUri.getPath())  ; 
	}
	
	public  String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	
	public  Bitmap decodeSampledBitmapFromResource(Uri uri, int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	   
	    BitmapFactory.decodeFile(getRealPathFromURI(uri), options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(getRealPathFromURI(uri), options);
	}
	
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        // Calculate ratios of height and width to requested height and width
        final int heightRatio = Math.round((float) height / (float) reqHeight);
        final int widthRatio = Math.round((float) width / (float) reqWidth);

        // Choose the smallest ratio as inSampleSize value, this will guarantee
        // a final image with both dimensions larger than or equal to the
        // requested height and width.
        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
    }

    return inSampleSize;
}
	
	private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
        view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
            unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
        ((ViewGroup) view).removeAllViews();
        }
    }
	
	// To animate view slide out from left to right
	public void slideToRight(View view){
	TranslateAnimation animate = new TranslateAnimation(0,view.getWidth(),0,0);
	animate.setDuration(500);
	animate.setFillAfter(true);
	view.startAnimation(animate);
	view.setVisibility(View.GONE);
	}
	// To animate view slide out from right to left
	public void slideToLeft(View view){
	TranslateAnimation animate = new TranslateAnimation(0,-view.getWidth(),0,0);
	animate.setDuration(500);
	animate.setFillAfter(true);
	view.startAnimation(animate);
	view.setVisibility(View.GONE);
	}
	@Override
	public void bottom2top(View v) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void left2right(View v) {
		Log.e("test","left2right");
		// TODO Auto-generated method stub
		switch(v.getId()){
        case R.id.iv_test:
            // do your stuff here
        	prev(v);
        break;
		}
	}
	@Override
	public void right2left(View v) {
		// TODO Auto-generated method stub
		Log.e("test","right2left");
		switch(v.getId()){
        case R.id.iv_test:
        	next(v);
            // do your stuff here
        break;
        
		}
	}
	@Override
	public void top2bottom(View v) {
		// TODO Auto-generated method stub
		
	}


}