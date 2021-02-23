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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Objects;

/**
 * This class is a helper class used only within this implementation.
 */
@ApiStatus.Internal
final class PrivateUtils {
    static final long NS_PER_S = 1_000_000_000L;
    static final long MS_PER_NS = 1_000_000L;
    static final long MS_PER_S = 1_000L;

    static abstract class SecondsNanosecondsBaseClass<T extends SecondsNanosecondsBaseClass<T>> implements Comparable<T>, Serializable {
        protected final long seconds;
        protected final long nanoseconds;

        @Contract(value = "_, _ -> new", pure = true)
        abstract @NotNull T newInstance(long s, long ns);

        @Contract(pure = true)
        protected SecondsNanosecondsBaseClass(long seconds, long nanoseconds) {
            while (nanoseconds < 0) {
                seconds--;
                nanoseconds += PrivateUtils.NS_PER_S;
            }

            while (nanoseconds >= PrivateUtils.NS_PER_S) {
                seconds++;
                nanoseconds -= PrivateUtils.NS_PER_S;
            }

            this.seconds = seconds;
            this.nanoseconds = nanoseconds;
        }

        @Contract(value = "null -> false", pure = true)
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return compareTo((T) o) == 0;
        }

        @Override
        @Contract(pure = true)
        public int hashCode() {
            return Objects.hash(getClass(), seconds, nanoseconds);
        }

        @Contract(pure = true, value = "_, _ -> new")
        @NotNull T add(long s, long ns) {
            return newInstance(seconds + s, nanoseconds + ns);
        }

        @Contract(value = "_ -> new", pure = true)
        public @NotNull T plus(long amountToAdd, @NotNull TemporalUnit unit) {
            if (!(unit instanceof ChronoUnit))
                throw new IllegalArgumentException();

            switch ((ChronoUnit)unit) {
                case NANOS:
                    return add(0, amountToAdd);
                case MILLIS:
                    return add(0, amountToAdd * MS_PER_NS);
                case SECONDS:
                    return add(amountToAdd, 0);
            }

            throw new IllegalArgumentException();
        }

        @Contract(value = "_ -> new", pure = true)
        public @NotNull T minus(long amountToSubtract, @NotNull TemporalUnit unit) {
            return plus(-amountToSubtract, unit);
        }

        /**
         * Adds the given amount of time.
         * @param millisToAdd The amount to add in [ms].
         * @return The new object.
         */
        @Contract(value = "_ -> new", pure = true)
        public @NotNull T plusMillis(long millisToAdd) {
            return add(0, millisToAdd * MS_PER_NS);
        }

        /**
         * Adds the given amount of time.
         * @param nanosToAdd The amount to add in [ns].
         * @return The new object.
         */
        @Contract(value = "_ -> new", pure = true)
        public @NotNull T plusNanos(long nanosToAdd) {
            return add(0, nanosToAdd);
        }

        /**
         * Adds the given amount of time.
         * @param secondsToAdd The amount to add in [s].
         * @return The new object.
         */
        @Contract(value = "_ -> new", pure = true)
        public @NotNull T plusSeconds(long secondsToAdd) {
            return add(secondsToAdd, 0);
        }

        /**
         * Subtracts the given amount of time.
         * @param millisToSubtract The amount to subtract in [ms].
         * @return The new object.
         */
        @Contract(value = "_ -> new", pure = true)
        public @NotNull T minusMillis(long millisToSubtract) {
            return add(0, -millisToSubtract* MS_PER_NS);
        }

        /**
         * Subtracts the given amount of time.
         * @param nanosToSubtract The amount to subtract in [ns].
         * @return The new object.
         */
        @Contract(value = "_ -> new", pure = true)
        public @NotNull T minusNanos(long nanosToSubtract) {
            return add(0, -nanosToSubtract);
        }

        /**
         * Subtracts the given amount of time.
         * @param secondsToSubtract The amount to subtract in [s].
         * @return The new object.
         */
        @Contract(value = "_ -> new", pure = true)
        public @NotNull T minusSeconds(long secondsToSubtract) {
            return add(-secondsToSubtract, 0);
        }

        /**
         * Gets the value of nanoseconds since full second as defined by {@link ChronoField#NANO_OF_SECOND}.
         * @return The value.
         */
        @Contract(pure = true)
        public int getNano() {
            return (int) nanoseconds;
        }

    }
}
