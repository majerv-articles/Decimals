package ch.javasoft.decimal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import ch.javasoft.decimal.ScaleMetrics.Scale${precision}f;
import ch.javasoft.decimal.arithmetic.DecimalArithmetics;

/**
 * <tt>Decimal${precision}f</tt> represents a immutable decimal number with ${precision} fractional
 * digits.
 */
@SuppressWarnings("serial")
public class Decimal${precision}f extends AbstractImmutableDecimal<Scale${precision}f, Decimal${precision}f, MutableDecimal${precision}f> {

	public static final DecimalArithmetics ARITHMETICS = Scale${precision}f.INSTANCE.getTruncatingArithmetics().derive(RoundingMode.HALF_UP);
//	public static final DecimalArithmetics ARITHMETICS = Scale${precision}f.INSTANCE.getTruncatingArithmetics().derive(RoundingMode.HALF_EVEN);
//	public static final DecimalArithmetics ARITHMETICS = Scale${precision}f.INSTANCE.getTruncatingArithmetics();

	public static final long ONE_UNSCALED = ARITHMETICS.one();

	public static final Decimal${precision}f ZERO = new Decimal${precision}f(0);
	public static final Decimal${precision}f ULP = new Decimal${precision}f(1);

	public static final Decimal${precision}f ONE = new Decimal${precision}f(1 * ONE_UNSCALED);
	public static final Decimal${precision}f TWO = new Decimal${precision}f(2 * ONE_UNSCALED);
	public static final Decimal${precision}f THREE = new Decimal${precision}f(3 * ONE_UNSCALED);
	public static final Decimal${precision}f FOUR = new Decimal${precision}f(4 * ONE_UNSCALED);
	public static final Decimal${precision}f FIVE = new Decimal${precision}f(5 * ONE_UNSCALED);
	public static final Decimal${precision}f SIX = new Decimal${precision}f(6 * ONE_UNSCALED);
	public static final Decimal${precision}f SEVEN = new Decimal${precision}f(7 * ONE_UNSCALED);
	public static final Decimal${precision}f EIGHT = new Decimal${precision}f(8 * ONE_UNSCALED);
	public static final Decimal${precision}f NINE = new Decimal${precision}f(9 * ONE_UNSCALED);

	public static final Decimal${precision}f TEN = new Decimal${precision}f(10 * ONE_UNSCALED);
	public static final Decimal${precision}f HUNDRED = new Decimal${precision}f(100 * ONE_UNSCALED);
	public static final Decimal${precision}f THOUSAND = new Decimal${precision}f(1000 * ONE_UNSCALED);
	public static final Decimal${precision}f MILLION = new Decimal${precision}f(1000000 * ONE_UNSCALED);
	public static final Decimal${precision}f BILLION = new Decimal${precision}f(1000000000 * ONE_UNSCALED);

	public static final Decimal${precision}f HALF = new Decimal${precision}f(ONE_UNSCALED / 2);
	public static final Decimal${precision}f TENTH = new Decimal${precision}f(ONE_UNSCALED / 10);
	public static final Decimal${precision}f HUNDREDTH = new Decimal${precision}f(ONE_UNSCALED / 100);
	public static final Decimal${precision}f THOUSANDTH = new Decimal${precision}f(ONE_UNSCALED / 1000);
	public static final Decimal${precision}f MILLIONTH = new Decimal${precision}f(ONE_UNSCALED / 1000000);

	public static final Decimal${precision}f MINUS_ONE = new Decimal${precision}f(-ONE_UNSCALED);

	public static final Decimal${precision}f MAX_VALUE = new Decimal${precision}f(Long.MAX_VALUE);
	public static final Decimal${precision}f MAX_INTEGER_VALUE = new Decimal${precision}f((Long.MAX_VALUE / ONE_UNSCALED) * ONE_UNSCALED);
	public static final Decimal${precision}f MIN_VALUE = new Decimal${precision}f(Long.MIN_VALUE);
	public static final Decimal${precision}f MIN_INTEGER_VALUE = new Decimal${precision}f((Long.MIN_VALUE / ONE_UNSCALED) * ONE_UNSCALED);

	Decimal${precision}f(long unscaled, DecimalArithmetics arithmetics) {
		super(unscaled, Scale${precision}f.INSTANCE, arithmetics);
	}
	private Decimal${precision}f(long unscaled) {
		super(unscaled, Scale${precision}f.INSTANCE, ARITHMETICS);
	}

	public Decimal${precision}f(String value) {
		super(ARITHMETICS.parse(value), Scale${precision}f.INSTANCE, ARITHMETICS);
	}

	@Override
	protected Decimal${precision}f self() {
		return this;
	}

	@Override
	public Decimal${precision}f convert(RoundingMode roundingMode) {
		final DecimalArithmetics arith = getArithmetics();
		if (roundingMode == arith.getRoundingMode()) {
			return this;
		}
		return new Decimal${precision}f(unscaledValue(), arith.derive(roundingMode));
	}
	
	@Override
	public Decimal${precision}f convert(OverflowMode overflowMode) {
		final DecimalArithmetics arith = getArithmetics();
		if (overflowMode == arith.getOverflowMode()) {
			return this;
		}
		return new Decimal${precision}f(unscaledValue(), arith.derive(overflowMode));
	}

	public static Decimal${precision}f valueOf(long value) {
		return valueOfUnscaled(ARITHMETICS.fromLong(value));
	}

	public static Decimal${precision}f valueOf(double value) {
		return valueOfUnscaled(ARITHMETICS.fromDouble(value));
	}

	//FIXME apply rounding mode to decimal or not?
	public static Decimal${precision}f valueOf(double value, RoundingMode roundingMode) {
		return valueOfUnscaled(ARITHMETICS.derive(roundingMode).fromDouble(value));
	}

	public static Decimal${precision}f valueOf(BigInteger value) {
		return valueOfUnscaled(ARITHMETICS.fromBigInteger(value));
	}

	public static Decimal${precision}f valueOf(BigDecimal value) {
		return valueOfUnscaled(ARITHMETICS.fromBigDecimal(value));
	}

	//FIXME apply rounding mode to decimal or not?
	public static Decimal${precision}f valueOf(BigDecimal value, RoundingMode roundingMode) {
		return valueOfUnscaled(ARITHMETICS.derive(roundingMode).fromBigDecimal(value));
	}

	/**
	 * Converts the specified unscaled decimal with the given scale to a
	 * {@code Decimal${precision}} value. If the given scale is more precise than the scale
	 * for {@code Decimal${precision}} and decimals need to be truncated,
	 * {@link RoundingMode#HALF_EVEN HALF_EVEN} rounding mode is applied.
	 * 
	 * @param unscaledValue
	 *            the unscaled decimal value to convert
	 * @param scale
	 *            the scale used for {@code unscaledValue}
	 * @return the {@code Decimal${precision}} for the specified unscaled decimal value
	 *         with the given scale
	 */
	public static Decimal${precision}f valueOf(long unscaledValue, int scale) {
		return valueOfUnscaled(ARITHMETICS.fromUnscaled(unscaledValue, scale));
	}

	/**
	 * Converts the specified unscaled decimal with the given scale to a
	 * {@code Decimal${precision}} value. If the given scale is more precise than the scale
	 * for {@code Decimal${precision}} and decimals need to be truncated, the specified
	 * rounding mode is applied.
	 * 
	 * @param unscaledValue
	 *            the unscaled decimal value to convert
	 * @param scale
	 *            the scale used for {@code unscaledValue}
	 * @param roundingMode
	 *            the rounding mode to apply if the value needs to be truncated
	 * @return the {@code Decimal${precision}} for the specified unscaled decimal value
	 *         with the given scale
	 */
	//FIXME apply rounding mode to decimal or not?
	public static Decimal${precision}f valueOf(long unscaledValue, int scale, RoundingMode roundingMode) {
		return valueOfUnscaled(ARITHMETICS.derive(roundingMode).fromUnscaled(unscaledValue, scale));
	}

	public static Decimal${precision}f valueOf(Decimal<?> value) {
		return valueOf(value.unscaledValue(), value.getArithmetics().getScale());
	}

	//FIXME apply rounding mode to decimal or not?
	public static Decimal${precision}f valueOf(Decimal<?> value, RoundingMode roundingMode) {
		return valueOf(value.unscaledValue(), value.getArithmetics().getScale(), roundingMode);
	}

	public static Decimal${precision}f valueOf(String value) {
		return valueOfUnscaled(ARITHMETICS.parse(value));
	}

	//FIXME apply rounding mode to decimal or not?
	public static Decimal${precision}f valueOf(String value, RoundingMode roundingMode) {
		return valueOfUnscaled(ARITHMETICS.derive(roundingMode).parse(value));
	}

	public static Decimal${precision}f valueOfUnscaled(long unscaledValue) {
		if (unscaledValue == 0) {
			return ZERO;
		}
		if (unscaledValue == 1) {
			return ULP;
		}
		if (unscaledValue == ONE_UNSCALED) {
			return ONE;
		}
		if (unscaledValue == -ONE_UNSCALED) {
			return MINUS_ONE;
		}
		return new Decimal${precision}f(unscaledValue);
	}

	@Override
	protected Decimal${precision}f create(long unscaled) {
		return valueOfUnscaled(unscaled);
	}

	@Override
	public MutableDecimal${precision}f toMutableDecimal() {
		return new MutableDecimal${precision}f(this);
	}

}
