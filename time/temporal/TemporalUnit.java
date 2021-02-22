/*
 * Copyright (c) 2021 nacamar GmbH - Ybrid®, a Hybrid Dynamic Live Audio Technology
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package java.time.temporal;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;

/**
 * This interface re-implements {@link java.time.temporal.TemporalUnit} as it is not provided by all versions of Android.
 */
public interface TemporalUnit {
    /**
     * Gets the duration of this unit. This may be estimated.
     * @return The duration of this unit.
     */
    @NotNull Duration getDuration();

    /**
     * Returns whether the result of {@link #getDuration()} is an estimation.
     * @return Whether the unit's duration is estimated.
     */
    boolean isDurationEstimated();

    /**
     * Returns whether this unit is date based.
     * @return Whether this unit is date based.
     */
    boolean isDateBased();

    /**
     * Return whether this unit is time based.
     * @return Whether this unit is time based.
     */
    boolean isTimeBased();

    /**
     * Whether this unit is supported by the given {@link Temporal}.
     *
     * @param temporal The {@link Temporal} to check.
     * @return Whether this unit is supported or not.
     */
    default boolean isSupportedBy(@NotNull Temporal temporal) {
        return temporal.isSupported(this);
    }

    /**
     * Returns a copy of the specified temporal object with the specified period added.
     *
     * @param temporal The object to add to.
     * @param amount   The amount to add.
     * @return The new object.
     */
    @NotNull
    default <R extends Temporal> R addTo(@NotNull R temporal, long amount) {
        //noinspection unchecked
        return (R)temporal.plus(amount, this);
    }

    /**
     * Calculates the time between it's parameters in this unit.
     *
     * @param temporal1Inclusive The start time.
     * @param temporal2Exclusive The end time.
     * @return The time difference.
     */
    default long between(@NotNull Temporal temporal1Inclusive, @NotNull Temporal temporal2Exclusive) {
        return temporal1Inclusive.until(temporal2Exclusive, this);
    }
}
