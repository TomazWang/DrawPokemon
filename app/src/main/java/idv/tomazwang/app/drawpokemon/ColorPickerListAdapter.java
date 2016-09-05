package idv.tomazwang.app.drawpokemon;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class ColorPickerListAdapter extends RecyclerView.Adapter<ColorPickerListAdapter.ViewHolder>{

    ArrayList<Integer> mColorList = new ArrayList<>();

    public ColorPickerListAdapter(ArrayList<Integer> colorList) {
        mColorList = colorList;
    }

    @Override
    public ColorPickerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ColorPickerListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mColorList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}