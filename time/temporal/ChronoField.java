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

import android.os.Build;
import androidx.annotation.RequiresApi;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * This interface re-implements {@link java.time.temporal.ChronoField} as it is not provided by all versions of Android.
 * <P>
 * Only a subset of the full Java specifications are implemented here as the rest it outside of the scope of this project at this point.
 */
public enum ChronoField implements TemporalField {
    /**
     * [ns] part relative to a full second. Always positive.
     */
    NANO_OF_SECOND(ChronoUnit.NANOS),
    /**
     * Seconds since epoch.
     */
    INSTANT_SECONDS(ChronoUnit.SECONDS);

    private final @NotNull TemporalUnit baseUnit;

    ChronoField(@NotNull TemporalUnit baseUnit) {
        this.baseUnit = baseUnit;
    }

    @Contract(pure = true)
    @Override
    public @NotNull TemporalUnit getBaseUnit() {
        return baseUnit;
    }
}
