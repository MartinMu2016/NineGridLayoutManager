package com.martin.layoutmanager;

import android.support.v4.util.SparseArrayCompat;

/**
 * <p>
 * Package Name:com.martin.layoutmanager
 * </p>
 * <p>
 * Class Name:Pool
 * <p>
 * Description:{@link NineGridLayoutManager}
 * </p>
 *
 * @Author Martin
 * @Version 1.0 2018/10/31 5:05 PM Release
 * @Reviser:
 * @Modification Time:2018/10/31 5:05 PM
 */
public class Pool<T> {
    private SparseArrayCompat<T> mPool;
    private New<T> mNewInstance;

    public Pool(New<T> newInstance) {
        mPool = new SparseArrayCompat<>();
        mNewInstance = newInstance;
    }

    public T get(int key) {
        T res = mPool.get(key);
        if (res == null) {
            res = mNewInstance.get();
            mPool.put(key, res);
        }
        return res;
    }

    public interface New<T> {
        T get();
    }
}
