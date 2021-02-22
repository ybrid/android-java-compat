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
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * This interface re-implements {@link java.time.temporal.TemporalAmount} as it is not provided by all versions of Android.
 */
public interface TemporalAmount {
    /**
     * Gets the value defining this amount for the given unit.
     * @param unit The unit to return the value for.
     * @return The value for the given unit.
     */
    long get(TemporalUnit unit);

    /**
     * Returns the list of units used to define the exact state of this object.
     * @return The list of units.
     */
    @Unmodifiable @NotNull List<@NotNull TemporalUnit> getUnits();

    /**
     * Adds this to a temporal.
     *
     * @param temporal The temporal to add to.
     * @return The result of the addition.
     */
    @NotNull
    default Temporal addTo(@NotNull Temporal temporal) {
        return temporal.plus(this);
    }

    /**
     * Subtracts this from a temporal.
     *
     * @param temporal The temporal to subtract from.
     * @return The result of the subtraction.
     */
    @NotNull
    default Temporal subtractFrom(@NotNull Temporal temporal) {
        return temporal.minus(this);
    }
}
