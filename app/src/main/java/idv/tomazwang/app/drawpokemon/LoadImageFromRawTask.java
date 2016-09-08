package idv.tomazwang.app.drawpokemon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

/**
 * Created by TomazWang on 2016/9/9.
 */

public class LoadImageFromRawTask extends AsyncTask<String, Float, Bitmap[]>{

    private final Callback mCallback;
    private final Context mContext;
    private int length;

    public LoadImageFromRawTask(Context ctx, Point size , Callback callback){
        this.mCallback = callback;
        this.mContext = ctx;
        int width = size.x;
        int height = size.y;

        if(width < height){
            length = width;
        }else{
            length = height;
        }

    }

    @Override
    protected Bitmap[] doInBackground(String... params) {

        Bitmap[] bitmaps = new Bitmap[params.length];

        for(int i=0; i < params.length; i++){
            Bitmap bitmap = null;
            String rawFileName = params[i];


            try {
                bitmap = Glide
                        .with(mContext)
                        .load("android.resource://idv.tomazwang.app.drawpokemon/raw/" + rawFileName)
                        .asBitmap()
                        .error(R.drawable.pokeball)
                        .listener(new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                Log.d(TAG, "onException: " + e.getMessage());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .fitCenter()
                        .centerCrop()
                        .into(length,length)
                        .get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            bitmaps[i] = bitmap;

            publishProgress((float)i/(float)params.length);

        }
        return bitmaps;
    }


    @Override
    protected void onPostExecute(Bitmap[] bitmaps) {
        super.onPostExecute(bitmaps);
        mCallback.callBack(bitmaps);
    }

    public interface Callback {
        void callBack(Bitmap[] bitmaps);
    }
}
