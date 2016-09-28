package net.dunyun.framework.android.mainapp.widget.viewpagerindicator;


/**
 */
public interface PageIndicator {
    /**
     *
     * @param view
     */
    void setViewPager(IndexViewPager view);

    /**
     *
     * @param view
     * @param initialPosition
     */
    void setViewPager(IndexViewPager view, int initialPosition);

    /**
     *
     * @param item
     */
    void setCurrentItem(int item);

    /**
     */
    void notifyDataSetChanged();
}
