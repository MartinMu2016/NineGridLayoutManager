package com.martin.layoutmanager;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p>
 * Package Name:org.loader.layoutmanager
 * </p>
 * <p>
 * Class Name:NineGridLayoutManager
 * <p>
 * Description:自定义九宫格LayoutManager类
 * </p>
 *
 * @Author Martin
 * @Version 1.0 2018/10/29 9:42 AM Release
 * @Reviser:
 * @Modification Time:2018/10/29 9:42 AM
 */
public class NineGridLayoutManager extends RecyclerView.LayoutManager {
    private int gridSpacing;//gap
    private int mSpanCount = 3; //默认为3
    private int maxImageSize = 9;
    public static final int STATE_ONE = 1;//1
    public static final int STATE_FOUR = 4;//2*2
    private int singleImageWidth;               // 单张图片时的最大宽度,单位dp
    private int singleImageHeight;              // 单张图片时的最大高度,单位dp
    private Pool<Rect> mCacheBorders;   //用于规定Item显示的区域
    private int itemWidth;
    private int itemHeight;

    public NineGridLayoutManager(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        gridSpacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, dm);
        singleImageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, dm);
        singleImageHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140, dm);
        mCacheBorders = new Pool<>(new Pool.New<Rect>() {
            @Override
            public Rect get() {
                return new Rect();
            }
        });
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return false;
    }

    /**
     * 测量RecyclerView控件的宽高
     * @param recycler
     * @param state
     * @param widthSpec
     * @param heightSpec
     */
    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        super.onMeasure(recycler, state, widthSpec, heightSpec);
        if (getItemCount() <= 0 || state.isPreLayout()) {
            return;
        }
        int width = View.MeasureSpec.getSize(widthSpec);//不能直接用getWidth 可能获取 0
        itemWidth = (width - getPaddingLeft() - getPaddingRight() - gridSpacing * (mSpanCount - 1)) / mSpanCount;
        itemHeight = itemWidth;//高度没问题
        int childCount = getItemCount();
        if (childCount < 0) {
            return;
        }
        int height = 0;
        if (childCount == 1) {
            height = singleImageHeight;
            widthSpec = View.MeasureSpec.makeMeasureSpec(singleImageWidth, View.MeasureSpec.EXACTLY);
        } else if (childCount > 0 && childCount <= 3) {
            height = 1 * itemHeight;
        } else if (childCount > 3 && childCount <= 6) {
            height = 2 * itemHeight;
        } else if (childCount >6 && childCount <= 9){
            height = 3 * itemHeight;
        }
        height += getPaddingTop()+getPaddingBottom();//TODO:解决 Padding问题
        heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        super.onMeasure(recycler, state, widthSpec, heightSpec);
    }

    /**
     * @param recycler
     * @param state
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        if (getItemCount() <= 0 || state.isPreLayout()) {//预布局状态不考虑
            return;
        }

        if (state.getItemCount() == 0) {
            /*
             * 没有Item可布局,就回收全部临时缓存(参考自带的LinearLayoutManager)
             * 这里没有item，是指adapter里面的数据集
             * 可能临时被清空了，但不确定何时还会继续添加回来。
             */
            removeAndRecycleAllViews(recycler);
            return;
        }
        //界面上的所有item都detach掉, 并缓存在scrap中,以便下次直接拿出来显示
        detachAndScrapAttachedViews(recycler);
        layoutChunk();
        fill(recycler);
    }

    /**
     * 测量
     */
    private void layoutChunk() {
        int childCount = getItemCount();
        childCount = Math.min(childCount, maxImageSize); // 计算一下最大的条目数量
        if (childCount <= 0) {
            return;
        }
        int cl = getPaddingLeft();
        int ct = getPaddingTop();
        switch (getItemCount()) {
            case STATE_ONE:
                for (int i = 0; i < getItemCount(); i++) {
                    Rect item = mCacheBorders.get(i);
                    item.set(cl, ct, cl + singleImageWidth, ct + singleImageHeight);
                }
                break;
            case STATE_FOUR:
                for (int i = 0; i < getItemCount(); i++) {
                    Rect item = mCacheBorders.get(i);
                    item.set(cl, ct, cl + itemWidth, ct + itemHeight);
                    // 累加宽度
                    cl += itemWidth + gridSpacing;
                    // 如果是换行
                    if ((i + 1) % 2 == 0) {//2*2
                        // 重置左边的位置
                        cl = getPaddingLeft();
                        // 叠加高度
                        ct += itemHeight + gridSpacing;
                    }
                }
                break;
            default:
                for (int i = 0; i < getItemCount(); i++) {
                    Rect item = mCacheBorders.get(i);
                    item.set(cl, ct, cl + itemWidth, ct + itemHeight);
                    // 累加宽度
                    cl += itemWidth + gridSpacing;
                    // 如果是换行
                    if ((i + 1) % 3 == 0) {//3列
                        // 重置左边的位置
                        cl = getPaddingLeft();
                        // 叠加高度
                        ct += itemHeight + gridSpacing;
                    }
//                    child.layout(cl, ct, cl + width, ct + height);
//                    // 累加宽度
//                    cl += width + gridSpacing;
//                    // 如果是换行
//                    if ((i + 1) % 3 == 0) {//3列
//                        // 重置左边的位置
//                        cl = getPaddingLeft();
//                        // 叠加高度
//                        ct += height + gridSpacing;
//                    }
                }
                break;
        }
    }



    /**
     * 填充
     *
     * @param recycler
     */
    private void fill(RecyclerView.Recycler recycler) {
        int itemSpecW;
        int itemSpecH;
        for (int i = 0; i < getItemCount(); i++) {
            Rect frame = mCacheBorders.get(i);
            View scrap = recycler.getViewForPosition(i);
            addView(scrap);
            itemSpecW = View.MeasureSpec.makeMeasureSpec(frame.width(), View.MeasureSpec.EXACTLY);
            itemSpecH = View.MeasureSpec.makeMeasureSpec(frame.height(), View.MeasureSpec.EXACTLY);
            scrap.measure(itemSpecW, itemSpecH);
            layoutDecorated(scrap, frame.left, frame.top, frame.right, frame.bottom);
        }
    }





}
