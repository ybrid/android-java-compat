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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

/**
 * This interface re-implements {@link java.time.temporal.ChronoUnit} as it is not provided by all versions of Android.
 * <P>
 * Only a subset of the full Java specifications are implemented here as the rest it outside of the scope of this project at this point.
 */
public enum ChronoUnit implements TemporalUnit {
    /**
     * [ms]
     */
    MILLIS(Duration.ofMillis(1), false, false, true),
    /**
     * [ns]
     */
    NANOS(Duration.ofNanos(1), false, false, true),
    /**
     * [s]
     */
    SECONDS(Duration.ofSeconds(1), false, false, true);

    private final Duration duration;
    private final boolean durationEstimated;
    private final boolean dateBased;
    private final boolean timeBased;

    ChronoUnit(Duration duration, boolean durationEstimated, boolean dateBased, boolean timeBased) {
        this.duration = duration;
        this.durationEstimated = durationEstimated;
        this.dateBased = dateBased;
        this.timeBased = timeBased;
    }

    @Contract(pure = true)
    @Override
    @NotNull
    public Duration getDuration() {
        return duration;
    }

    @Contract(pure = true)
    @Override
    public boolean isDurationEstimated() {
        return durationEstimated;
    }

    @Contract(pure = true)
    @Override
    public boolean isDateBased() {
        return dateBased;
    }

    @Contract(pure = true)
    @Override
    public boolean isTimeBased() {
        return timeBased;
    }
}
