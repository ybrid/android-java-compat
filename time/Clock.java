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

package java.time;

import org.jetbrains.annotations.NotNull;

/**
 * This interface re-implements {@link java.time.Clock} as it is not provided by all versions of Android.
 * <P>
 * Only a subset of the full Java specifications are implemented here as the rest it outside of the scope of this project at this point.
 */
public abstract class Clock {
    /**
     * Returns the time zone used by this clock.
     *
     * @return The used time zone.
     */
    public abstract @NotNull ZoneId getZone();

    /**
     * Returns a copy of this clock with a different time zone selected.
     *
     * @param zone The new time zone to use.
     * @return The new clock.
     */
    public abstract @NotNull Clock withZone(@NotNull ZoneId zone);

    /**
     * Returns an {@link Instant} of the current time.
     * @return The current instant.
     */
    public abstract @NotNull Instant instant();

    /**
     * Returns the current time in milliseconds from epoch.
     * @return the current time.
     */
    public long millis() {
        return System.currentTimeMillis();
    }

    /**
     * Gets a clock that always returns the same {@link Instant}.
     *
     * @param fixedInstant The instant to return.
     * @param zone The time zone to use.
     * @return the new clock.
     */
    public static @NotNull Clock fixed(@NotNull Instant fixedInstant, @NotNull ZoneId zone) {
        return new Clock() {
            @Override
            public @NotNull ZoneId getZone() {
                return zone;
            }

            @Override
            public @NotNull Clock withZone(@NotNull ZoneId zone) {
                return Clock.fixed(fixedInstant, zone);
            }

            @Override
            public @NotNull Instant instant() {
                return fixedInstant;
            }

            @Override
            public long millis() {
                return fixedInstant.toEpochMilli();
            }
        };
    }
}
