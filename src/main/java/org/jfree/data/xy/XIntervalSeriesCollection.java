/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2013, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * ------------------------------
 * XIntervalSeriesCollection.java
 * ------------------------------
 * (C) Copyright 2006-2013, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 20-Oct-2006 : Version 1 (DG);
 * 27-Nov-2006 : Added clone() override (DG);
 * 18-Jan-2008 : Added removeSeries() and removeAllSeries() methods (DG);
 * 22-Apr-2008 : Implemented PublicCloneable (DG);
 * 02-Jul-2013 : Use ParamChecks (DG);
 *
 */

package org.jfree.data.xy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jfree.chart.util.Args;
import org.jfree.chart.util.CloneUtils;
import org.jfree.chart.util.PublicCloneable;

import org.jfree.data.general.DatasetChangeEvent;

/**
 * A collection of {@link XIntervalSeries} objects.
 *
 * @since 1.0.3
 *
 * @see XIntervalSeries
 */
public class XIntervalSeriesCollection<S extends Comparable<S>> 
        extends AbstractIntervalXYDataset
        implements IntervalXYDataset, PublicCloneable, Serializable {

    /** Storage for the data series. */
    private List<XIntervalSeries<S>> data;

    /**
     * Creates a new {@code XIntervalSeriesCollection} instance.
     */
    public XIntervalSeriesCollection() {
        this.data = new ArrayList<>();
    }

    /**
     * Adds a series to the collection and sends a {@link DatasetChangeEvent}
     * to all registered listeners.
     *
     * @param series  the series ({@code null} not permitted).
     */
    public void addSeries(XIntervalSeries<S> series) {
        Args.nullNotPermitted(series, "series");
        this.data.add(series);
        series.addChangeListener(this);
        fireDatasetChanged();
    }

    /**
     * Returns the number of series in the collection.
     *
     * @return The series count.
     */
    @Override
    public int getSeriesCount() {
        return this.data.size();
    }

    /**
     * Returns a series from the collection.
     *
     * @param series  the series index (zero-based).
     *
     * @return The series.
     *
     * @throws IllegalArgumentException if {@code series} is not in the
     *     range {@code 0} to {@code getSeriesCount() - 1}.
     */
    public XIntervalSeries<S> getSeries(int series) {
        Args.requireInRange(series, "series", 0, this.data.size() - 1);
        return this.data.get(series);
    }

    /**
     * Returns the key for a series.
     *
     * @param series  the series index (in the range {@code 0} to
     *     {@code getSeriesCount() - 1}).
     *
     * @return The key for a series.
     *
     * @throws IllegalArgumentException if {@code series} is not in the
     *     specified range.
     */
    @Override
    public S getSeriesKey(int series) {
        // defer argument checking
        return getSeries(series).getKey();
    }

    /**
     * Returns the number of items in the specified series.
     *
     * @param series  the series (zero-based index).
     *
     * @return The item count.
     *
     * @throws IllegalArgumentException if {@code series} is not in the
     *     range {@code 0} to {@code getSeriesCount() - 1}.
     */
    @Override
    public int getItemCount(int series) {
        // defer argument checking
        return getSeries(series).getItemCount();
    }

    /**
     * Returns the x-value for an item within a series.
     *
     * @param series  the series index.
     * @param item  the item index.
     *
     * @return The x-value.
     */
    @Override
    public Number getX(int series, int item) {
        XIntervalSeries<S> s = this.data.get(series);
        XIntervalDataItem di = (XIntervalDataItem) s.getDataItem(item);
        return di.getX();
    }

    /**
     * Returns the start x-value (as a double primitive) for an item within a
     * series.
     *
     * @param series  the series index (zero-based).
     * @param item  the item index (zero-based).
     *
     * @return The value.
     */
    @Override
    public double getStartXValue(int series, int item) {
        XIntervalSeries<S> s = this.data.get(series);
        return s.getXLowValue(item);
    }

    /**
     * Returns the end x-value (as a double primitive) for an item within a
     * series.
     *
     * @param series  the series (zero-based index).
     * @param item  the item (zero-based index).
     *
     * @return The value.
     */
    @Override
    public double getEndXValue(int series, int item) {
        XIntervalSeries<S> s = this.data.get(series);
        return s.getXHighValue(item);
    }

    /**
     * Returns the y-value (as a double primitive) for an item within a
     * series.
     *
     * @param series  the series index (zero-based).
     * @param item  the item index (zero-based).
     *
     * @return The value.
     */
    @Override
    public double getYValue(int series, int item) {
        XIntervalSeries<S> s = this.data.get(series);
        return s.getYValue(item);
    }

    /**
     * Returns the y-value for an item within a series.
     *
     * @param series  the series index.
     * @param item  the item index.
     *
     * @return The y-value.
     */
    @Override
    public Number getY(int series, int item) {
        XIntervalSeries<S> s = this.data.get(series);
        XIntervalDataItem di = (XIntervalDataItem) s.getDataItem(item);
        return di.getYValue();
    }

    /**
     * Returns the start x-value for an item within a series.
     *
     * @param series  the series index.
     * @param item  the item index.
     *
     * @return The x-value.
     */
    @Override
    public Number getStartX(int series, int item) {
        XIntervalSeries<S> s = this.data.get(series);
        XIntervalDataItem di = (XIntervalDataItem) s.getDataItem(item);
        return di.getXLowValue();
    }

    /**
     * Returns the end x-value for an item within a series.
     *
     * @param series  the series index.
     * @param item  the item index.
     *
     * @return The x-value.
     */
    @Override
    public Number getEndX(int series, int item) {
        XIntervalSeries<S> s = this.data.get(series);
        XIntervalDataItem di = (XIntervalDataItem) s.getDataItem(item);
        return di.getXHighValue();
    }

    /**
     * Returns the start y-value for an item within a series.  This method
     * maps directly to {@link #getY(int, int)}.
     *
     * @param series  the series index.
     * @param item  the item index.
     *
     * @return The start y-value.
     */
    @Override
    public Number getStartY(int series, int item) {
        return getY(series, item);
    }

    /**
     * Returns the end y-value for an item within a series.  This method
     * maps directly to {@link #getY(int, int)}.
     *
     * @param series  the series index.
     * @param item  the item index.
     *
     * @return The end y-value.
     */
    @Override
    public Number getEndY(int series, int item) {
        return getY(series, item);
    }

    /**
     * Removes a series from the collection and sends a
     * {@link DatasetChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero-based).
     *
     * @since 1.0.10
     */
    public void removeSeries(int series) {
        Args.requireInRange(series, "series", 0, this.data.size() - 1);
        XIntervalSeries ts = this.data.get(series);
        ts.removeChangeListener(this);
        this.data.remove(series);
        fireDatasetChanged();
    }

    /**
     * Removes a series from the collection and sends a
     * {@link DatasetChangeEvent} to all registered listeners.
     *
     * @param series  the series ({@code null} not permitted).
     *
     * @since 1.0.10
     */
    public void removeSeries(XIntervalSeries<S> series) {
        Args.nullNotPermitted(series, "series");
        if (this.data.contains(series)) {
            series.removeChangeListener(this);
            this.data.remove(series);
            fireDatasetChanged();
        }
    }

    /**
     * Removes all the series from the collection and sends a
     * {@link DatasetChangeEvent} to all registered listeners.
     *
     * @since 1.0.10
     */
    public void removeAllSeries() {
        // Unregister the collection as a change listener to each series in
        // the collection.
        for (XIntervalSeries series : this.data) {
          series.removeChangeListener(this);
        }
        this.data.clear();
        fireDatasetChanged();
    }

    /**
     * Tests this instance for equality with an arbitrary object.
     *
     * @param obj  the object ({@code null} permitted).
     *
     * @return A boolean.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof XIntervalSeriesCollection)) {
            return false;
        }
        XIntervalSeriesCollection<S> that = (XIntervalSeriesCollection) obj;
        return Objects.equals(this.data, that.data);
    }

    /**
     * Returns a clone of this instance.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException if there is a problem.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        XIntervalSeriesCollection clone
                = (XIntervalSeriesCollection) super.clone();
        clone.data = CloneUtils.cloneList(this.data);
        return clone;
    }

}
