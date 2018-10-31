package com.martin.layoutmanager;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * <p>
 * Package Name:com.martin.layoutmanager
 * </p>
 * <p>
 * Class Name:NineGridAdapter
 * <p>
 * Description:{@link Adapter}
 * </p>
 *
 * @Author Martin
 * @Version 1.0 2018/10/31 5:05 PM Release
 * @Reviser:
 * @Modification Time:2018/10/31 5:05 PM
 */
public class NineGridAdapter extends RecyclerView.Adapter<NineGridAdapter.Holder> {

    private static final int[] COLORS = {0xff00FFFF, 0xffDEB887, 0xff5F9EA0,
            0xff7FFF00, 0xff6495ED, 0xffDC143C,
            0xff008B8B, 0xff006400, 0xff2F4F4F,
            0xffFF69B4, 0xffFF00FF, 0xffCD5C5C,
            0xff90EE90, 0xff87CEFA, 0xff800000};


//    private final int[] buckets = {1, 4, 9};
    private int mCount = 0;

    public NineGridAdapter(int i) {
        this.mCount = i;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_nine_grid, parent, false);
        return new Holder(item);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        holder.text.setText(String.format("菜单%d",position));
        holder.itemView.setBackground(new ColorDrawable(randomColor()));
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(),String.format("菜单%d",position),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    private int randomColor() {
        return COLORS[new Random().nextInt(COLORS.length)];
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView text;
        public Holder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }





}
