package org.alvarowau.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.alvarowau.R;

public class ImageTextAdapter extends ArrayAdapter<String> {

    private String[] spinnerTitles;
    private int[] spinnerImages;
    private LayoutInflater mInflater;

    public ImageTextAdapter(@NonNull Context context, String[] titles, int[] images) {
        super(context, R.layout.spinner_row, titles);
        this.spinnerTitles = titles;
        this.spinnerImages = images;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return spinnerTitles.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spinner_row, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.vImage = convertView.findViewById(R.id.imagen);
            mViewHolder.vName = convertView.findViewById(R.id.nombre);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.vImage.setImageResource(spinnerImages[position]);
        mViewHolder.vName.setText(spinnerTitles[position]);

        return convertView;
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }


    private static class ViewHolder {
        ImageView vImage;
        TextView vName;
    }
}
