package com.martin.layoutmanager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * <p>
 * Package Name:com.martin.layoutmanager
 * </p>
 * <p>
 * Class Name:CommonAdapter
 * <p>
 * Description:{@link MainActivity}
 * </p>
 *
 * @Author Martin
 * @Version 1.0 2018/10/31 4:59 PM Release
 * @Reviser:
 * @Modification Time:2018/10/31 4:59 PM
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_layout_main, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTime.setText(String.format(dateFormat.format(new Date())));
        holder.mNineView.setLayoutManager(new NineGridLayoutManager(holder.mNineView.getContext()));
        holder.mNineView.setAdapter(new NineGridAdapter(position+1));
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView mNineView;
        TextView mTime;

        public ViewHolder(View itemView) {
            super(itemView);
            mNineView = itemView.findViewById(R.id.nine_grid);
            mTime = itemView.findViewById(R.id.time);

        }
    }
}

