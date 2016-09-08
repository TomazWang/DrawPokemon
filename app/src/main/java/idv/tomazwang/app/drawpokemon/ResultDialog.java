package idv.tomazwang.app.drawpokemon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by TomazWang on 2016/9/6.
 */

public class ResultDialog extends DialogFragment {


    private static final String TAG = ResultDialog.class.getSimpleName();
    private Bitmap mResultBitmap;
    private ImageView mResultImg;
    private ImageView mSaveBtn;
    private ImageView mShareBtn;
    private ImageView mAgainBtn;
    private AlertDialog mDialog;

    public ResultDialog() {
        super();
    }


    public static ResultDialog newInstance() {
        ResultDialog rd = new ResultDialog();
        return rd;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_result, null);
        mResultImg = (ImageView) view.findViewById(R.id.iv_result);
        mSaveBtn = (ImageView) view.findViewById(R.id.btn_save);
        mShareBtn = (ImageView) view.findViewById(R.id.btn_share);
        mAgainBtn = (ImageView) view.findViewById(R.id.btn_again);

        mResultBitmap = ((MainActivity) getActivity()).getResultBitmap();

        mResultImg.setImageBitmap(mResultBitmap);

        mSaveBtn.setOnClickListener(v -> saveImage(filePath -> onSaveComplete(filePath)));
        mShareBtn.setOnClickListener(v -> share());
        mAgainBtn.setOnClickListener(v -> playAgain());


        mDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCanceledOnTouchOutside(false);


        return mDialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        playAgain();
    }

    private void playAgain() {
        ((MainActivity) getActivity()).playAgain();
        mDialog.dismiss();
    }

    private void share() {
        // TODO: share image


        saveImage(filePath -> {

                File pictureFile = ResultDialog.this.onSaveComplete(filePath);
                if(pictureFile != null){

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/jpeg");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pictureFile));
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_using)));

                }else{
                    Toast.makeText(getActivity(), R.string.cannot_share_image, Toast.LENGTH_SHORT).show();
                }

                }
        );


    }

    private void saveImage(SaveImageTask.Callback callback) {
        Log.d(TAG, "saveImage: saving image");
        SaveImageTask saveImageTask = new SaveImageTask(getActivity(), callback);
        saveImageTask.execute(mResultBitmap);
    }

    private File onSaveComplete(File filePath) {

        if (filePath == null) {
            Log.w(TAG, "onSaveComplete: file dir error");
            return null;
        }

        Toast saveCompleteMsg = Toast.makeText(getActivity(), getString(R.string.saveComplete), Toast.LENGTH_SHORT);
        saveCompleteMsg.show();
        Log.d(TAG, "onSaveComplete: save image in " + filePath);


        MediaScannerConnection.scanFile(
                getActivity(),
                new String[]{filePath.getAbsolutePath()},
                null,
                new MediaScannerConnection.MediaScannerConnectionClient() {
                    @Override
                    public void onMediaScannerConnected() {
                        Log.d(TAG, "onMediaScannerConnected");
                    }

                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.d(TAG, "onScanCompleted");
                    }
                }
        );

        return filePath;
    }


}
