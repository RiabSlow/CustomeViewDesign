package com.example.sakkar.canvaspainttest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;


public class CustomLayout extends RelativeLayout implements CustomTextView.SelectStateListener {

    public enum SelectionState {
        SINGLE(1),
        MULTIPLE(2);
        private int value;

        SelectionState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum Gravity {
        START(0),
        CENTER(1);
        private int value;

        Gravity(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private int mGravity = 1;
    private int startX, startY, minHeight, minWidth;
    private int mHeight, mWidth;

    private ItemSelectionListener listener;

    private ArrayList<CustomTextView> listOfView;
    private ArrayList<Integer> heights;
    private ArrayList<Integer> widths;

    private int textColor, clickedTextColor, itemBackgroundColor, selectionState,
            itemClickedBackgroundColor, itemBorderColor, itemClickedBorderColor, itemBorderWidth;
    private int itemHorizontalSpacing,itemVerticalSpacing,heightPadding,widthPadding;
    private float textSize;

    private CustomTextView tempCustomTextView;

    public CustomLayout(@NonNull Context context) {
        super(context);
        init(null);
    }

    public CustomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    void init(AttributeSet attrs) {

        startX = getPaddingLeft();
        startY = getPaddingTop();

        itemVerticalSpacing=getValuePx(10);
        itemHorizontalSpacing=getValuePx(8);

        widthPadding=getValuePx(8);
        heightPadding=getValuePx(4);

        if (attrs != null) {
            TypedArray ta = getResources().obtainAttributes(attrs, R.styleable.CustomLayout);
            mGravity = ta.getInt(R.styleable.CustomLayout_item_gravity, Gravity.CENTER.getValue());
            itemBackgroundColor = ta.getColor(R.styleable.CustomLayout_item_background, Color.TRANSPARENT);
            itemClickedBackgroundColor = ta.getColor(R.styleable.CustomLayout_item_clicked_background, Color.BLACK);
            itemBorderColor = ta.getColor(R.styleable.CustomLayout_item_border_color, Color.BLACK);
            itemClickedBorderColor = ta.getColor(R.styleable.CustomLayout_item_clicked_border_color, Color.BLACK);
            textColor = ta.getColor(R.styleable.CustomLayout_item_text_color, Color.BLACK);
            clickedTextColor = ta.getColor(R.styleable.CustomLayout_item_clicked_text_color, Color.WHITE);
            itemBorderWidth = ta.getDimensionPixelSize(R.styleable.CustomLayout_item_border_width, itemBorderWidth);
            itemHorizontalSpacing = ta.getDimensionPixelSize(R.styleable.CustomLayout_item_horizontal_spacing, itemHorizontalSpacing);
            itemVerticalSpacing = ta.getDimensionPixelSize(R.styleable.CustomLayout_item_vertical_spacing, itemVerticalSpacing);
            heightPadding = ta.getDimensionPixelSize(R.styleable.CustomLayout_item_height_padding, heightPadding);
            widthPadding = ta.getDimensionPixelSize(R.styleable.CustomLayout_item_width_padding, widthPadding);
            textSize = ta.getDimension(R.styleable.CustomLayout_item_text_size, 0f);
            selectionState = ta.getInt(R.styleable.CustomLayout_item_selection_state, SelectionState.MULTIPLE.getValue());
            ta.recycle();
        }

        observeViewTree();
    }

    private void observeViewTree() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeight = getHeight();
                mWidth = getWidth();
                if (minWidth > mWidth)
                    mWidth = minWidth;
                rearrangeChildren();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    void addItemList(ArrayList<String> itemList) {
        listOfView = new ArrayList<>();
        heights = new ArrayList<>();
        widths = new ArrayList<>();
        int i = 0;
        for (String s : itemList) {
            final CustomTextView textView = new CustomTextView(getContext());
            textView.addListener(this);
            addView(textView);
            addItemDetails(s,i,textView);
            listOfView.add(textView);
            i++;
        }
        obtainHeightAndWidths();
    }

    private void obtainHeightAndWidths() {
        widths.clear();
        heights.clear();
        for(CustomTextView textView:listOfView) {
            widths.add(textView.getmWidth());
            heights.add(textView.getmHeight());
            minHeight = minHeight > textView.getmHeight() ? minHeight : textView.getmHeight();
            minWidth = minWidth > textView.getmWidth() ? minWidth : textView.getmWidth();
        }
        minWidth += getPaddingLeft() * 2;
        minHeight += getPaddingTop() * 2;
    }

    private void addItemDetails(String s,int i,CustomTextView textView) {
        textView.setText(s);
        textView.setPosition(i);
        textView.setStrokeWidth(itemBorderWidth);
        textView.setTextSize(textSize);
        textView.setHeightPadding(heightPadding);
        textView.setHeightPadding(widthPadding);
        setColorsToChild(textView);
    }

    private void setColorsToChild(CustomTextView textView) {
        textView.setBorderClickedColor(itemClickedBorderColor);
        textView.setBorderNotClickedColor(itemBorderColor);
        textView.setShaderNotClickedColor(itemBackgroundColor);
        textView.setShaderClickedColor(itemClickedBackgroundColor);
        textView.setClickedTextColor(clickedTextColor);
        textView.setNotClickedTextColor(textColor);
    }

    void rearrangeChildren() {
        if (mGravity == Gravity.START.getValue()) {
            for (int i = 0; i < widths.size(); ) {
                do {
                    getChild(i).setX(startX);
                    getChild(i).setY(startY);
                    startX += widths.get(i) + itemHorizontalSpacing;
                    i++;
                } while (i < widths.size() && startX + widths.get(i) < mWidth - getPaddingRight());
                startX = getPaddingLeft();
                startY += heights.get(i - 1) + itemVerticalSpacing;
            }
        }
        if (mGravity == Gravity.CENTER.getValue()) {
            for (int i = 0; i < widths.size(); ) {
                int width = 0, j, count = 0;
                for (j = i; j < widths.size(); j++) {
                    width += widths.get(j);
                    if (width > mWidth - getPaddingLeft() - getPaddingRight()) {
                        width -= (widths.get(j) + itemHorizontalSpacing);
                        break;
                    } else
                        width += itemHorizontalSpacing;
                    count++;
                }
                if (j == widths.size())
                    width -= itemHorizontalSpacing;
                startX = mWidth / 2 - width / 2;
                for (; count > 0; count--) {
                    getChild(i).setX(startX);
                    getChild(i).setY(startY);
                    startX += widths.get(i) + itemHorizontalSpacing;
                    i++;
                }
                startX = getPaddingLeft();
                startY += heights.get(i - 1) + itemVerticalSpacing;
            }
        }
        startY = getPaddingTop();
        setLayoutParams(new LinearLayout.LayoutParams(mWidth, mHeight > startY ? mHeight : startY));
    }

    @Override
    public void onSelect(CustomTextView textView) {
        if (selectionState == SelectionState.SINGLE.getValue())
            if (tempCustomTextView != null) {
                tempCustomTextView.setPressed(false);
            }
        if (tempCustomTextView != null && tempCustomTextView.getPosition() == textView.getPosition())
            tempCustomTextView = null;
        else
            tempCustomTextView = textView;

        if (listener != null)
            listener.itemSelected(textView);
    }

    int getValuePx(int x) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, x, getResources().getDisplayMetrics());
    }

    CustomTextView getChild(int position) {
        return listOfView.get(position);
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        for (CustomTextView textView : listOfView) {
            textView.setNotClickedTextColor(textColor);
        }
        postInvalidate();
    }

    public void setClickedTextColor(int clickedTextColor) {
        this.clickedTextColor = clickedTextColor;
        for (CustomTextView textView : listOfView) {
            textView.setClickedTextColor(textColor);
        }
        postInvalidate();
    }

    public void setItemBackgroundColor(int itemBackgroundColor) {
        this.itemBackgroundColor = itemBackgroundColor;
        for (CustomTextView textView : listOfView) {
            textView.setShaderNotClickedColor(textColor);
        }
        postInvalidate();
    }

    public void setItemClickedBackgroundColor(int itemClickedBackgroundColor) {
        this.itemClickedBackgroundColor = itemClickedBackgroundColor;
        for (CustomTextView textView : listOfView) {
            textView.setShaderClickedColor(textColor);
        }
        postInvalidate();
    }

    public void setItemBorderColor(int itemBorderColor) {
        this.itemBorderColor = itemBorderColor;
        for (CustomTextView textView : listOfView) {
            textView.setBorderNotClickedColor(textColor);
        }
        postInvalidate();
    }

    public void setItemClickedBorderColor(int itemClickedBorderColor) {
        this.itemClickedBorderColor = itemClickedBorderColor;
        for (CustomTextView textView : listOfView) {
            textView.setBorderClickedColor(textColor);
        }
        postInvalidate();
    }

    public void setItemBorderWidth(int itemBorderWidth) {
        this.itemBorderWidth = itemBorderWidth;
        for (CustomTextView textView : listOfView) {
            textView.setStrokeWidth(textColor);
        }
        itemAttributeChanged();
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        for(CustomTextView textView:listOfView)
            textView.setTextSize(textSize);
        itemAttributeChanged();
    }

    public void setSelectionState(SelectionState selectionState) {
        this.selectionState = selectionState.getValue();
    }

    public void setListener(ItemSelectionListener listener) {
        this.listener = listener;
    }

    public boolean isSingleSelectable() {
        return selectionState == SelectionState.SINGLE.getValue();
    }

    public void setGravity(Gravity gravity) {
        mGravity = gravity.getValue();
        rearrangeAndInvalidate();
    }

    public boolean isGravityCenter(){return mGravity==Gravity.CENTER.getValue();}

    public void setItemHorizontalSpacing(int itemHorizontalSpacing) {
        this.itemHorizontalSpacing = itemHorizontalSpacing;
        rearrangeAndInvalidate();
    }

    public void setItemVerticalSpacing(int itemVerticalSpacing) {
        this.itemVerticalSpacing = itemVerticalSpacing;
        rearrangeAndInvalidate();
    }

    public void rearrangeAndInvalidate(){
        rearrangeChildren();
        invalidate();
        requestLayout();
    }

    public int getItemHorizontalSpacing() {
        return itemHorizontalSpacing;
    }

    public int getItemVerticalSpacing() {
        return itemVerticalSpacing;
    }

    public void setHeightPadding(int heightPadding) {
        this.heightPadding = heightPadding;
        for(CustomTextView textView: listOfView)
            textView.setHeightPadding(heightPadding);
        itemAttributeChanged();
    }

    public void setWidthPadding(int widthPadding) {
        this.widthPadding = widthPadding;
        for(CustomTextView textView: listOfView)
            textView.setWidthPadding(widthPadding);
        itemAttributeChanged();
    }

    void itemAttributeChanged(){
        obtainHeightAndWidths();
        rearrangeAndInvalidate();
    }

    public interface ItemSelectionListener {
        void itemSelected(CustomTextView textView);
    }
}
