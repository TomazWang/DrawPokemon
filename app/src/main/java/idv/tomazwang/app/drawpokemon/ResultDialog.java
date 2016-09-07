package idv.tomazwang.app.drawpokemon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
    private Button mSaveBtn;
    private Button mShareBtn;
    private Button mAgainBtn;
    private AlertDialog mDialog;

    public ResultDialog(){super();}


    public static ResultDialog newInstance(){
        ResultDialog rd = new ResultDialog();
        return rd;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view  = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_result, null);
        mResultImg = (ImageView)view.findViewById(R.id.iv_result);
        mSaveBtn = (Button)view.findViewById(R.id.btn_save);
        mShareBtn = (Button)view.findViewById(R.id.btn_share);
        mAgainBtn = (Button)view.findViewById(R.id.btn_again);

        mResultBitmap = ((MainActivity)getActivity()).getResultBitmap();

        mResultImg.setImageBitmap(mResultBitmap);

        mSaveBtn.setOnClickListener(v -> saveImage(mResultBitmap));
        mShareBtn.setOnClickListener(v -> share());
        mAgainBtn.setOnClickListener(v-> playAgain());


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
        ((MainActivity)getActivity()).playAgain();
        mDialog.dismiss();
    }

    private void share() {
        // TODO: share image
    }

    private void saveImage(Bitmap bitmap) {
        SaveImageTask saveImageTask = new SaveImageTask(getActivity(), filePath -> onSaveComplete(filePath));
        saveImageTask.execute(bitmap);
    }

    private void onSaveComplete(File filePath) {

        if(filePath == null){
            Log.w(TAG, "onSaveComplete: file dir error");
            return;
        }

        Toast saveCompleteMsg = Toast.makeText(getActivity(), getString(R.string.saveComplete), Toast.LENGTH_SHORT);
        saveCompleteMsg.show();
        Log.d(TAG, "onSaveComplete: save image in "+filePath);


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

    }


}
