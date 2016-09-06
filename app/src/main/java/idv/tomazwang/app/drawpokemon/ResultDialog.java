package idv.tomazwang.app.drawpokemon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by TomazWang on 2016/9/6.
 */

public class ResultDialog extends DialogFragment {


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
        mResultBitmap = ((MainActivity)getActivity()).getResultBitmap();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view  = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_result, null);
        mResultImg = (ImageView)view.findViewById(R.id.iv_result);
        mSaveBtn = (Button)view.findViewById(R.id.btn_save);
        mShareBtn = (Button)view.findViewById(R.id.btn_share);
        mAgainBtn = (Button)view.findViewById(R.id.btn_again);

        mResultImg.setImageBitmap(mResultBitmap);

        mSaveBtn.setOnClickListener(v -> saveImage());
        mShareBtn.setOnClickListener(v -> share());
        mAgainBtn.setOnClickListener(v-> palyAgain());


        mDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCanceledOnTouchOutside(false);




        return mDialog;
    }

    private void palyAgain() {

    }

    private void share() {

    }

    private void saveImage() {
    }
}
