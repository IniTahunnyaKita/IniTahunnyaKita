package com.kitekite.initahunnyakita.util;

/**
 * Created by florian on 2/5/2015.
 */
/**
 * Dispatched when the current selected page of the application navigation changed. E.g. user swipes from the center
 * page to the left page.
 */
public class PageChangedEvent {

    // -----------------------------------------------------------------------
    //
    // Constructors
    //
    // -----------------------------------------------------------------------
    /**
     * @param hasVerticalNeighbors
     *            true if the current selected page has vertical (below and/or above) neighbor pages, false - if not.
     */
    public PageChangedEvent(boolean hasVerticalNeighbors, int position) {
        mHasVerticalNeighbors = hasVerticalNeighbors;
        mPosition = position;
    }

    // -----------------------------------------------------------------------
    //
    // Fields
    //
    // -----------------------------------------------------------------------
    private boolean mHasVerticalNeighbors = true;
    private int mPosition;

    // -----------------------------------------------------------------------
    //
    // Methods
    //
    // -----------------------------------------------------------------------
    /**
     * @return true if the page has vertical (below and/or above) neighbor pages, false - if not.
     */
    public boolean hasVerticalNeighbors() {
        return mHasVerticalNeighbors;
    }

    public int getPosition(){
        return mPosition;
    }

}