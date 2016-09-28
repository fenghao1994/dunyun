package net.dunyun.framework.android.mainapp.widget.viewpagerindicator;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.dunyun.framework.android.mainapp.adapter.FragmentAdapter;
import net.dunyun.framework.lock.R;

/**
 */
public class IconTabPageIndicator extends LinearLayout implements PageIndicator {
    /**
     */
    private static final CharSequence EMPTY_TITLE = "";

    /**
     */
    public interface OnTabReselectedListener {
        /**
         *
         * @param position
         */
        void onTabReselected(int position);
    }

    private Runnable mTabSelector;

    private final OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            TabView tabView = (TabView) view;
            final int oldSelected = mViewPager.getCurrentItem();
            final int newSelected = tabView.getIndex();
            mViewPager.setCurrentItem(newSelected, false);
            if (mTabReselectedListener != null) {
                mTabReselectedListener.onTabReselected(newSelected);
            }
        }
    };

    private final LinearLayout mTabLayout;

    private IndexViewPager mViewPager;
    private IndexViewPager.OnPageChangeListener mListener;

    private int mSelectedTabIndex;

    private OnTabReselectedListener mTabReselectedListener;

    private int mTabWidth;

    public IconTabPageIndicator(Context context) {

        this(context, null);
    }

    public IconTabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);

        mTabLayout = new LinearLayout(context, null,  R.attr.tabPageIndicator);
//        mTabLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mTabLayout.setGravity(Gravity.CENTER);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        addView(mTabLayout, lp);
    }

    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
        mTabReselectedListener = listener;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;

        final int childCount = mTabLayout.getChildCount();

        if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
            mTabWidth = MeasureSpec.getSize(widthMeasureSpec) / childCount;
        } else {
            mTabWidth = -1;
        }

        final int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newWidth = getMeasuredWidth();

        if (lockedExpanded && oldWidth != newWidth) {
            // Recenter the tab display if we're at a new (scrollable) size.
            setCurrentItem(mSelectedTabIndex);
        }
    }

    private void animateToTab(final int position) {
        final View tabView = mTabLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            // Re-post the selector we saved
            post(mTabSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }

    private void addTab(int index, CharSequence text, int iconResId) {
        boolean display = false;
        if(index == 2){
            display = true;
        }else{
            display = false;
        }
        final TabView tabView = new TabView(getContext(), display);
        tabView.mIndex = index;
        tabView.setOnClickListener(mTabClickListener);
        tabView.setText(text);

        if (iconResId > 0) {
            tabView.setIcon(iconResId);
        }

        mTabLayout.addView(tabView);
    }

    @Override
    public void setViewPager(IndexViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        notifyDataSetChanged();
        mViewPager.setScanScroll(true);
    }

    public void notifyDataSetChanged() {
        mTabLayout.removeAllViews();
        PagerAdapter adapter = mViewPager.getAdapter();
        FragmentAdapter iconAdapter = null;
        if (adapter instanceof FragmentAdapter) {
            iconAdapter = (FragmentAdapter) adapter;
        }
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            CharSequence title = adapter.getPageTitle(i);
            if (title == null) {
                title = EMPTY_TITLE;
            }
            int iconResId = 0;
            if (iconAdapter != null) {
                iconResId = iconAdapter.getIconResId(i);
            }
            addTab(i, title, iconResId);
        }
        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }

    @Override
    public void setViewPager(IndexViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mSelectedTabIndex = item;
        mViewPager.setCurrentItem(item, false);

        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
            if (isSelected) {
                animateToTab(item);
            }
        }
    }

    private class TabView extends LinearLayout {
        private int mIndex;
        private ImageView mImageView;
        private TextView mTextView;
        private boolean displayTxt = false;

        public TabView(Context context, boolean displayTxt) {
            super(context, null, R.attr.tabView);
            this.displayTxt = displayTxt;
            View view = null;
            if(displayTxt){
                view = View.inflate(context, R.layout.tab_view2, null);
            }else {
                view = View.inflate(context, R.layout.tab_view, null);
            }
            mImageView = (ImageView) view.findViewById(R.id.tab_image);
            mTextView = (TextView) view.findViewById(R.id.tab_text);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            lp.gravity = Gravity.CENTER;
            this.addView(view, lp);
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            // Re-measure if we went beyond our maximum size.
            if (mTabWidth > 0) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(mTabWidth, MeasureSpec.EXACTLY),
                        heightMeasureSpec);
            }
        }

        public void setText(CharSequence text) {
            mTextView.setText(text);
        }

        public void setIcon(int resId) {
            if (resId > 0) {
                mImageView.setImageResource(resId);
            }
        }

        public int getIndex() {
            return mIndex;
        }
    }
}

