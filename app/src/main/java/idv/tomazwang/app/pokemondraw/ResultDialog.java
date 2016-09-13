package idv.tomazwang.app.pokemondraw;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by TomazWang on 2016/9/6.
 */

public class ResultDialog extends DialogFragment {


    private static final String TAG = ResultDialog.class.getSimpleName();
    private static final String KEY_FILE_PATH = "key_filePath";
    private static final String APP_NAME = "Pokemon Draw";

    private ImageView mResultImg;
    private ImageView mShareBtn;
    private ImageView mAgainBtn;
    private AlertDialog mDialog;
    private ImageView mDeleteBtn;
    private String mFileName;
    private ProgressBar mProgressBar;
    private MainFragment mainFragment;

    public ResultDialog() {
        super();
    }


    public static ResultDialog newInstance(String fileName, MainFragment mf) {
        ResultDialog rd = new ResultDialog();

        Bundle bundle = new Bundle();
        bundle.putString(KEY_FILE_PATH, fileName);

        rd.setArguments(bundle);

        rd.setMainFragment(mf);
        return rd;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mFileName = bundle.getString(KEY_FILE_PATH);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Log.d(TAG, "onCreateDialog: on create dialog");

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_result, null);
        mResultImg = (ImageView) view.findViewById(R.id.iv_result);
        mShareBtn = (ImageView) view.findViewById(R.id.btn_share);
        mAgainBtn = (ImageView) view.findViewById(R.id.btn_again);
        mDeleteBtn = (ImageView) view.findViewById(R.id.btn_delete);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);


        mDeleteBtn.setColorFilter(Color.parseColor("#B3696969"), PorterDuff.Mode.SRC_ATOP);
        mShareBtn.setColorFilter(Color.parseColor("#B3696969"), PorterDuff.Mode.SRC_ATOP);

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
        mainFragment.playAgain();
        mDialog.dismiss();
    }


    private void share() {
        // TODO: share image


        File pictureFile = mainFragment.getResultFile();

        if (pictureFile != null) {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/jpeg");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pictureFile));
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_using)));

        } else {
            Toast.makeText(getActivity(), R.string.cannot_share_image, Toast.LENGTH_SHORT).show();
        }

    }


    private void deleteFile(String fileName) {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                , APP_NAME);
        File imageFile = new File(mediaStorageDir.getPath() + File.separator + fileName);

        if (imageFile.exists()) {
            imageFile.delete();
            Toast.makeText(getActivity(), R.string.delete_file, Toast.LENGTH_SHORT).show();
            playAgain();
        }

    }


    public void notifyImageComplete() {


        File file = mainFragment.getResultFile();

        mProgressBar.setVisibility(View.GONE);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int heigth = size.y * 6 / 10;

        ViewGroup.LayoutParams layoutParams = mResultImg.getLayoutParams();
        layoutParams.height = heigth;

        mResultImg.setLayoutParams(layoutParams);

        Glide
                .with(getActivity())
                .load(file)
                .into(mResultImg);


        mDeleteBtn.clearColorFilter();
        mShareBtn.clearColorFilter();


        mDeleteBtn.setOnClickListener(v -> deleteFile(mFileName));
        mShareBtn.setOnClickListener(v -> share());

    }


    public void setMainFragment(MainFragment mainFragment) {
        this.mainFragment = mainFragment;
    }

}
