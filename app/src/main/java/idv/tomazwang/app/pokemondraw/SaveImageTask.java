package idv.tomazwang.app.pokemondraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TomazWang on 2016/9/7.
 */

public class SaveImageTask extends AsyncTask<Bitmap, Void, File>{

    private static final String APP_NAME = "Pokemon Draw";
    private static final String TAG = SaveImageTask.class.getSimpleName();
    private final Context mContext;
    private final Callback mCallback;

    public SaveImageTask(Context context, Callback callback){
        this.mContext = context;
        this.mCallback = callback;
    }


    @Override
    protected File doInBackground(Bitmap... bitmaps) {

        Bitmap image = bitmaps[0];
        return saveBitmapToFile(mContext, image);

    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        mCallback.onSaveComplete(file);
    }

    public interface Callback{
        void onSaveComplete (File filePath);
    }


    public static File saveBitmapToFile(Context ctx, Bitmap bitmap){

        File pictureFile = getOutputMediaFile(ctx);
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");
            return null;
        }

        if(pictureFile.exists()){
            Log.d(TAG, "saveBitmapToFile: file already exist");
            return pictureFile;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }

        return pictureFile;
    }

    public static File getOutputMediaFile(Context context){

        String state = Environment.getExternalStorageState();
        if (! Environment.MEDIA_MOUNTED.equals(state)) {
            Log.w(TAG, "getOutputMediaFile: Environment storage not writable");
            return null;
        }

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
              ,APP_NAME);
//                + "/Android/data/"
//                + context.getApplicationContext().getPackageName()
//                + "/Files");
        Log.d(TAG, "getOutputMediaFile: media storage dir = "+ mediaStorageDir);


        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        String mImageName="pd_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

}
