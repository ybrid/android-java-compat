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

/**
 * This interface re-implements {@link java.time.temporal.Temporal} as it is not provided by all versions of Android.
 */
public interface Temporal {
    /**
     * Returns whether this temporal supports a given unit.
     * @param unit The unit to check for.
     * @return Whether this temporal supports the given unit.
     */
    boolean isSupported(@NotNull TemporalUnit unit);

    /**
     * Returns an adjusted object.
     * @param adjuster The adjuster to use.
     * @return The adjusted object.
     */
    @NotNull Temporal with(@NotNull TemporalAdjuster adjuster);

    /**
     * Returns a object with the given field updated.
     * @param field The field to update.
     * @param newValue The new value.
     * @return The updated object.
     */
    @NotNull Temporal with(@NotNull TemporalField field, long newValue);

    /**
     * Adds a given temporal amount.
     * @param amount The amount to add.
     * @return The addition result.
     */
    @NotNull Temporal plus(@NotNull TemporalAmount amount);

    /**
     * Adds a given temporal amount.
     * @param amountToAdd The amount to add.
     * @param unit The unit of the amount.
     * @return The addition result.
     */
    @NotNull Temporal plus(long amountToAdd, @NotNull TemporalUnit unit);

    /**
     * Subtracts a given temporal amount.
     * @param amount The amount to subtract.
     * @return The subtraction result.
     */
    @NotNull Temporal minus(@NotNull TemporalAmount amount);

    /**
     * Subtracts a given temporal amount.
     * @param amountToSubtract The amount to substract.
     * @param unit The unit of the amount.
     * @return The subtraction result.
     */
    @NotNull Temporal minus(long amountToSubtract, @NotNull TemporalUnit unit);

    /**
     * Returns the amount of time between this and the end time.
     * @param endExclusive The end time.
     * @param unit The unit to use.
     * @return The amount of time in the given unit.
     */
    long until(@NotNull Temporal endExclusive, @NotNull TemporalUnit unit);
}
