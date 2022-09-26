package com.joyrun.banner

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.annotation.Px

/**
 * author: wenjie
 * date: 2022/9/21 10:57
 * description:
 */
interface Indicator {


    fun initIndicator(pageSize: Int)

    /**
     * Set focused color for indicator.
     * @param focusColor
     * @return
     */
    fun setFocusColor(focusColor: Int): Indicator

    /**
     * Set normal color for indicator.
     * @param normalColor
     * @return
     */
    fun setNormalColor(normalColor: Int): Indicator

    /**
     * Set stroke color for indicator.
     * @param strokeColor
     * @return
     */
    fun setStrokeColor(strokeColor: Int): Indicator

    /**
     * Set stroke width for indicator.
     * @param strokeWidth
     * @return
     */
    fun setStrokeWidth(strokeWidth: Int): Indicator

    /**
     * Set spacing between indicator item ，the default value is item's height.
     * @param indicatorPadding
     * @return
     */
    fun setIndicatorPadding(indicatorPadding: Int): Indicator

    /**
     * Set the corner radius of the indicator item.
     * @param radius
     * @return
     */
    fun setRadius(radius: Int): Indicator

    /**
     * Sets the orientation of the layout.
     * @param orientation
     * @return
     */
    fun setOrientation(orientation: Int): Indicator

    /**
     * Set the location at which the indicator should appear on the screen.
     *
     * @param gravity android.view.Gravity
     * @return
     */
    fun setGravity(gravity: Int): Indicator

    /**
     * Set focused resource ID for indicator.
     * @param focusResId
     * @return
     */
    fun setFocusResId(focusResId: Int): Indicator

    /**
     * Set normal resource ID for indicator.
     * @param normalResId
     * @return
     */
    fun setNormalResId(normalResId: Int): Indicator


    fun setFocusDrawableId(@DrawableRes focusDrawableId: Int): Indicator

    fun setNormalDrawableId(@DrawableRes normalDrawableId: Int): Indicator


    /**
     * Set focused icon for indicator.
     * @param bitmap
     * @return
     */
    fun setFocusIcon(bitmap: Bitmap): Indicator

    /**
     * Set normal icon for indicator.
     * @param bitmap
     * @return
     */
    fun setNormalIcon(bitmap: Bitmap): Indicator

    /**
     * Set margins for indicator.
     * @param left   the left margin in pixels
     * @param top    the top margin in pixels
     * @param right  the right margin in pixels
     * @param bottom the bottom margin in pixels
     * @return
     */
    fun setMargin(left: Int, top: Int, right: Int, bottom: Int): Indicator

    /**
     * Combine all of the options that have been set and return a new IUltraIndicatorBuilder object.
     */
    fun build()

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     * Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    fun onPageScrolled(position: Int, positionOffset: Float, @Px positionOffsetPixels: Int)

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    fun onPageSelected(position: Int)

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
     *
     * @see androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING
     *
     * @see androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_SETTLING
     */
    fun onPageScrollStateChanged(state: Int)
}