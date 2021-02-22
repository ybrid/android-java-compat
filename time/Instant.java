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

import android.annotation.SuppressLint;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.temporal.*;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * This interface re-implements {@link java.time.Instant} as it is not provided by all versions of Android.
 * <P>
 * Only a subset of the full Java specifications are implemented here as the rest it outside of the scope of this project at this point.
 */
public final class Instant implements Temporal, TemporalAdjuster, TemporalAccessor, Comparable<Instant>, Serializable {
    public static final Instant EPOCH = new Instant(0, 0);
    public static final Instant MAX = new Instant(31556889864403199L, 999999999);
    public static final Instant MIN = new Instant(-31557014167219200L, 0);

    private static final long NS_PER_S = 1_000_000_000L;
    private static final long MS_PER_S = 1_000L;
    private static final int MS_PER_NS = 1_000_000;

    private final long seconds;
    private final long nanoseconds;

    @Contract("_ -> new")
    private @NotNull RuntimeException unsupportedTemporalField(@NotNull TemporalField field) {
        return new RuntimeException("Unsupported TemporalField: " + field);
    }

    @Contract(pure = true, value = "_, _ -> new")
    private @NotNull Instant add(long s, long ns) {
        s += seconds;
        ns += nanoseconds;

        while (ns < 0) {
            s--;
            ns += NS_PER_S;
        }

        while (ns > NS_PER_S) {
            s++;
            ns -= NS_PER_S;
        }

        return new Instant(s, ns);
    }

    private Instant(long seconds, long nanoseconds) {
        this.seconds = seconds;
        this.nanoseconds = nanoseconds;
    }

    /**
     * Returns the instant of the current system time.
     * @return The current instant.
     */
    @Contract(value = " -> new", pure = true)
    public static @NotNull Instant now() {
        return ofEpochMilli(System.currentTimeMillis());
    }

    /**
     * Returns the current instant of the time of the given clock.
     * @param clock The clock to use.
     * @return The current instant.
     * @see Clock#instant()
     */
    public static @NotNull Instant now(Clock clock) {
        return clock.instant();
    }

    /**
     * Returns a new instant for the given time.
     * @param epochSecond The given time as defined by {@link ChronoField#INSTANT_SECONDS}.
     * @return The new instant.
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Instant ofEpochSecond(long epochSecond) {
        return new Instant(epochSecond, 0);
    }

    /**
     * Returns a new instant for the given time.
     * @param epochSecond The given time as defined by {@link ChronoField#INSTANT_SECONDS}.
     * @param nanoAdjustment An amount of nanoseconds to add to the given time.
     * @return The new instant.
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull static Instant ofEpochSecond(long epochSecond, long nanoAdjustment) {
        return ofEpochSecond(epochSecond).plusNanos(nanoAdjustment);
    }

    /**
     * Returns a new instant for the given time.
     * @param epochMilli The given time as [ms] since the epoch as defined by {@link ChronoField#INSTANT_SECONDS}.
     * @return The new instant.
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Instant ofEpochMilli(long epochMilli) {
        long s = epochMilli / MS_PER_S;
        long ns = (epochMilli - s * MS_PER_S) * MS_PER_NS;
        return new Instant(s, ns);
    }

    @Override
    @Contract(pure = true)
    public int compareTo(Instant o) {
        final long otherSeconds = o.getLong(ChronoField.INSTANT_SECONDS);
        final long otherNanoseconds = o.getLong(ChronoField.NANO_OF_SECOND);
        int res;

        res = Long.compare(seconds, otherSeconds);
        if (res == 0)
            res = Long.compare(nanoseconds, otherNanoseconds);

        return res;
    }

    @Override
    public Temporal adjustInto(Temporal temporal) {
        if (temporal instanceof Instant) {
            //noinspection ReturnOfThis
            return this;
        }

        return temporal.with(ChronoField.INSTANT_SECONDS, seconds).with(ChronoField.NANO_OF_SECOND, nanoseconds);
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean isSupported(TemporalUnit unit) {
        return unit instanceof ChronoUnit;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean isSupported(@Nullable TemporalField field) {
        return field instanceof ChronoField;
    }

    @Override
    public Temporal with(TemporalField field, long newValue) {
        if (field instanceof ChronoField) {
            switch ((ChronoField) field) {
                case NANO_OF_SECOND:
                    return new Instant(seconds, newValue);
                case INSTANT_SECONDS:
                    return new Instant(newValue, nanoseconds);
            }
        }

        throw unsupportedTemporalField(field);
    }

    @Contract(value = "_ -> new", pure = true)
    @Override
    public @NotNull Temporal plus(long amountToAdd, @NotNull TemporalUnit unit) {
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
    @Override
    public @NotNull Temporal minus(long amountToSubtract, @NotNull TemporalUnit unit) {
        return plus(-amountToSubtract, unit);
    }

    /**
     * Adds the given amount of time to the instant.
     * @param millisToAdd The amount to add in [ms].
     * @return The new instant.
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Instant plusMillis(long millisToAdd) {
        return add(0, millisToAdd* MS_PER_NS);
    }

    /**
     * Adds the given amount of time to the instant.
     * @param nanosToAdd The amount to add in [ns].
     * @return The new instant.
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Instant plusNanos(long nanosToAdd) {
        return add(0, nanosToAdd);
    }

    /**
     * Adds the given amount of time to the instant.
     * @param secondsToAdd The amount to add in [s].
     * @return The new instant.
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Instant plusSeconds(long secondsToAdd) {
        return add(secondsToAdd, 0);
    }

    /**
     * Subtracts the given amount of time from the instant.
     * @param millisToSubtract The amount to subtract in [ms].
     * @return The new instant.
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Instant minusMillis(long millisToSubtract) {
        return add(0, -millisToSubtract* MS_PER_NS);
    }

    /**
     * Subtracts the given amount of time from the instant.
     * @param nanosToSubtract The amount to subtract in [ns].
     * @return The new instant.
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Instant minusNanos(long nanosToSubtract) {
        return add(0, -nanosToSubtract);
    }

    /**
     * Subtracts the given amount of time from the instant.
     * @param secondsToSubtract The amount to subtract in [s].
     * @return The new instant.
     */
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Instant minusSeconds(long secondsToSubtract) {
        return add(-secondsToSubtract, 0);
    }

    @Override
    public long until(@NotNull Temporal endExclusive, @NotNull TemporalUnit unit) {
        return 0; // TODO.
    }

    @Override
    public int get(@NotNull TemporalField field) {
        final long val = getLong(field);
        if (val < Integer.MIN_VALUE || val > Integer.MAX_VALUE)
            throw new ArithmeticException();
        return (int) val;
    }

    @Override
    public long getLong(@NotNull TemporalField field) {
        if (field instanceof ChronoField) {
            switch ((ChronoField) field) {
                case NANO_OF_SECOND:
                    return nanoseconds;
                case INSTANT_SECONDS:
                    return seconds;
            }
        }

        throw unsupportedTemporalField(field);
    }

    /**
     * Gets the value of seconds since epoch as defined by {@link ChronoField#INSTANT_SECONDS}.
     * @return The value.
     */
    @Contract(pure = true)
    public long getEpochSecond() {
        return seconds;
    }

    /**
     * Gets the value of nanoseconds since full second as defined by {@link ChronoField#NANO_OF_SECOND}.
     * @return The value.
     */
    @Contract(pure = true)
    public int getNano() {
        return (int) nanoseconds;
    }

    /**
     * Returns the milliseconds since the epoch as defined by {@link ChronoField#INSTANT_SECONDS}.
     * @return The passed milliseconds.
     */
    @Contract(pure = true)
    public long toEpochMilli() {
        return (seconds * MS_PER_S) + (nanoseconds / MS_PER_NS);
    }

    /**
     * Returns whether this is after a given instant.
     * @param otherInstant The given instant to check with.
     * @return Whether this is after the given instant.
     */
    @Contract(pure = true)
    public boolean isAfter(Instant otherInstant) {
        return compareTo(otherInstant) > 0;
    }

    /**
     * Returns whether this is before a given instant.
     * @param otherInstant The given instant to check with.
     * @return Whether this is before the given instant.
     */
    @Contract(pure = true)
    public boolean isBefore(Instant otherInstant) {
        return compareTo(otherInstant) < 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return compareTo((Instant) o) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seconds, nanoseconds);
    }

    @SuppressWarnings({"UseOfObsoleteDateTimeApi", "HardCodedStringLiteral"})
    @SuppressLint("SimpleDateFormat")
    @Override
    public String toString() {
        @NotNull Date date = new Date(toEpochMilli());
        //noinspection SpellCheckingInspection
        @NotNull SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        return sdf.format(date);
    }
}
