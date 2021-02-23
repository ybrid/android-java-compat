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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.time.temporal.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Duration extends PrivateUtils.SecondsNanosecondsBaseClass<Duration> implements TemporalAmount {
    /**
     * Duration of zero time.
     */
    public static final Duration ZERO = new Duration(0, 0);

    private static final @NotNull @Unmodifiable List<@NotNull TemporalUnit> units;

    static {
        final @NotNull List<@NotNull TemporalUnit> list = new ArrayList<>(2);
        list.add(ChronoUnit.SECONDS);
        list.add(ChronoUnit.NANOS);
        units = Collections.unmodifiableList(list);
    }

    @Contract(pure = true)
    private Duration(long seconds, long nanoseconds) {
        super(seconds, nanoseconds);
    }

    @Override
    @NotNull Duration newInstance(long s, long ns) {
        return new Duration(s, ns);
    }

    /**
     * Creates a duration of the given time.
     * @param seconds The time in [s].
     * @param nanoAdjustment The adjustment.
     * @return The new duration.
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Duration ofSeconds(long seconds, long nanoAdjustment) {
        return new Duration(seconds, nanoAdjustment);
    }

    /**
     * Creates a duration of the given time.
     * @param seconds The time in [s].
     * @return The new duration.
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Duration ofSeconds(long seconds) {
        return new Duration(seconds, 0);
    }

    /**
     * Creates a duration of the given time.
     * @param millis The time in [ms].
     * @return The new duration.
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Duration ofMillis(long millis) {
        final long seconds = millis / PrivateUtils.MS_PER_S;
        final long nanoseconds = (millis - seconds * PrivateUtils.MS_PER_S) * PrivateUtils.MS_PER_NS;
        return new Duration(seconds, nanoseconds);
    }

    /**
     * Creates a duration of the given time.
     * @param nanos The time in [ns].
     * @return The new duration.
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Duration ofNanos(long nanos) {
        return new Duration(0, nanos);
    }

    /**
     * Creates a duration of the given time.
     * @param days The time in [86400s].
     * @return The new duration.
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Duration ofDays(long days) {
        return ofSeconds(PrivateUtils.mul(days, PrivateUtils.S_PER_DAY));
    }

    /**
     * Creates a duration of the given time.
     * @param hours The time in [3600s].
     * @return The new duration.
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Duration ofHours(long hours) {
        return ofSeconds(PrivateUtils.mul(hours, PrivateUtils.S_PER_HOUR));
    }

    /**
     * Creates a duration of the given time.
     * @param minutes The time in [60s].
     * @return The new duration.
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Duration ofMinutes(long minutes) {
        return ofSeconds(minutes * PrivateUtils.S_PER_MINUTE);
    }

    @Override
    @Contract(pure = true)
    public int compareTo(Duration o) {
        final long otherSeconds = o.get(ChronoUnit.SECONDS);
        final long otherNanoseconds = o.get(ChronoUnit.NANOS);
        int res;

        if (!o.getUnits().equals(units))
            throw new IllegalArgumentException();

        res = Long.compare(seconds, otherSeconds);
        if (res == 0)
            res = Long.compare(nanoseconds, otherNanoseconds);

        return res;
    }

    @Contract(pure = true)
    @Override
    public long get(TemporalUnit unit) {
        if (unit instanceof ChronoUnit) {
            switch ((ChronoUnit) unit) {
                case NANOS:
                    return nanoseconds;
                case SECONDS:
                    return seconds;
            }
        }

        throw PrivateUtils.unsupportedTemporalUnit(unit);
    }

    @Contract(pure = true)
    @Override
    @Unmodifiable
    @NotNull
    public List<@NotNull TemporalUnit> getUnits() {
        return units;
    }

    /**
     * Returns the length of this duration in seconds.
     * @return The length.
     */
    @Contract(pure = true)
    public long getSeconds() {
        return seconds;
    }

    /**
     * Returns whether this duration is negative.
     * @return Whether this duration is negative.
     */
    @Contract(pure = true)
    public boolean isNegative() {
        return seconds < 0;
    }

    /**
     * Returns whether this duration is zero.
     * @return Whether this duration is zero.
     */
    @Contract(pure = true)
    public boolean isZero() {
        return seconds == 0 && nanoseconds == 0;
    }

    /**
     * Returns the absolute value of this duration.
     * @return The absolute value of this duration.
     */
    @Contract(pure = true)
    public @NotNull Duration abs() {
        if (isNegative())
            return negated();
        //noinspection ReturnOfThis
        return this;
    }

    /**
     * Negates this duration.
     * @return The negated duration.
     */
    @Contract(pure = true)
    public @NotNull Duration negated() {
        return new Duration(-seconds, -nanoseconds);
    }

    /**
     * Adds a duration.
     * @param duration The duration to add.
     * @return The sum.
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Duration plus(@NotNull Duration duration) {
        return add(duration.getSeconds(), duration.getNano());
    }

    /**
     * Subtracts a duration.
     * @param duration The duration to subtract.
     * @return The result.
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Duration minus(@NotNull Duration duration) {
        return add(PrivateUtils.negate(duration.getSeconds()), PrivateUtils.negate(duration.getNano()));
    }

    /**
     * Multiplies this duration.
     * @param multiplicand The factor to multiply with.
     * @return The result.
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Duration multipliedBy(long multiplicand) {
        return new Duration(multiplicand * seconds, multiplicand * nanoseconds);
    }

    /**
     * Divides this duration.
     * @param divisor The divisor.
     * @return The result.
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Duration dividedBy(long divisor) {
        final long s;
        final long sRest;
        final long ns;
        final boolean neg = divisor < 0;
        final @NotNull Duration ret;

        if (divisor == 0 || divisor == Long.MIN_VALUE)
            throw new ArithmeticException("Invalid divisor");

        if (neg)
            divisor *= -1;

        s = seconds / divisor;
        sRest = seconds % divisor;

        if (sRest >= (Long.MAX_VALUE / PrivateUtils.NS_PER_S))
            throw new ArithmeticException("Overflow");

        ns = (nanoseconds + sRest * PrivateUtils.NS_PER_S) / divisor;

        ret = new Duration(s, ns);

        if (neg)
            return ret.negated();

        return ret;
    }


    /**
     * Calculates the difference between two {@link Temporal Temporals}.
     * @param startInclusive The start.
     * @param endExclusive The end.
     * @return The difference between the two {@link Temporal Temporals}.
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Duration between(@NotNull Temporal startInclusive, @NotNull Temporal endExclusive) {
        final long startSeconds = startInclusive.getLong(ChronoField.INSTANT_SECONDS);
        final long startNanoseconds = startInclusive.getLong(ChronoField.NANO_OF_SECOND);
        final long endSeconds = endExclusive.getLong(ChronoField.INSTANT_SECONDS);
        final long endNanoseconds = endExclusive.getLong(ChronoField.NANO_OF_SECOND);

        return new Duration(PrivateUtils.subtract(endSeconds, startSeconds), PrivateUtils.subtract(endNanoseconds, startNanoseconds));
    }

    /**
     * Sets the nanosecond part of this duration.
     * @param nanoOfSecond The new nanosecond part.
     * @return The resulting new duration.
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Duration withNanos(int nanoOfSecond) {
        return new Duration(seconds, nanoOfSecond);
    }

    /**
     * Sets the second part of this duration.
     * @param seconds The new second part.
     * @return The resulting new duration.
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Duration withSeconds(@SuppressWarnings("ParameterHidesMemberVariable") long seconds) {
        return new Duration(seconds, nanoseconds);
    }

    /**
     * Returns the duration in [ms].
     * @return The duration in [ms].
     */
    public long toMillis() {
        return to(ChronoUnit.MILLIS);
    }

    /**
     * Returns the duration in [ns].
     * @return The duration in [ns].
     */
    public long toNanos() {
        return to(ChronoUnit.NANOS);
    }
}
