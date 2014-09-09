package ch.javasoft.decimal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import ch.javasoft.decimal.ScaleMetrics.Scale${precision}f;

@SuppressWarnings("serial")
public class MutableDecimal${precision}f extends
		AbstractMutableDecimal<Scale${precision}f, MutableDecimal${precision}f, Decimal${precision}f> implements
		Cloneable {

	/**
	 * Creates a new {@code MutableDecimal${precision}f} with value zero.
	 */
	public MutableDecimal${precision}f() {
		super(0, Scale${precision}f.INSTANCE);
	}

	private MutableDecimal${precision}f(long unscaledValue, Scale${precision}f scale) {
		super(unscaledValue, scale);
	}

	public MutableDecimal${precision}f(String value) {
		this(Decimal${precision}f.ARITHMETICS.parse(value));
	}

	public MutableDecimal${precision}f(String value, RoundingMode roundingMode) {
		this(Decimal${precision}f.ARITHMETICS.derive(roundingMode).parse(value));
	}

	public MutableDecimal${precision}f(long value) {
		this();
		set(value);
	}

	public MutableDecimal${precision}f(double value) {
		this();
		add(value);
	}

	public MutableDecimal${precision}f(double value, RoundingMode roundingMode) {
		this();
		add(value, roundingMode);
	}

	public MutableDecimal${precision}f(BigInteger value) {
		this();
		add(value);
	}

	public MutableDecimal${precision}f(BigDecimal value, RoundingMode roundingMode) {
		this();
		add(value, roundingMode);
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
	 */
	public MutableDecimal${precision}f(long unscaledValue, int scale) {
		this();
		addUnscaled(unscaledValue, scale);
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
	 *            the rounding mode to apply if the value argument needs to be
	 *            truncated when converted into a decimal number
	 */
	public MutableDecimal${precision}f(long unscaledValue, int scale, RoundingMode roundingMode) {
		this();
		addUnscaled(unscaledValue, scale, roundingMode);
	}

	public MutableDecimal${precision}f(Decimal<Scale${precision}f> value) {
		this(value.unscaledValue(), value.getScaleMetrics());
	}
	
	@Override
	protected MutableDecimal${precision}f self() {
		return this;
	}

	@Override
	public MutableDecimal${precision}f clone() {
		return new MutableDecimal${precision}f(this);
	}

	public static MutableDecimal${precision}f unscaled(long unscaledValue) {
		return new MutableDecimal${precision}f(unscaledValue, Scale${precision}f.INSTANCE);
	}

	public static MutableDecimal${precision}f zero() {
		return new MutableDecimal${precision}f();
	}

	public static MutableDecimal${precision}f ulp() {
		return new MutableDecimal${precision}f(Decimal${precision}f.ULP);
	}

	public static MutableDecimal${precision}f one() {
		return new MutableDecimal${precision}f(Decimal${precision}f.ONE);
	}

	public static MutableDecimal${precision}f two() {
		return new MutableDecimal${precision}f(Decimal${precision}f.TWO);
	}

	public static MutableDecimal${precision}f three() {
		return new MutableDecimal${precision}f(Decimal${precision}f.THREE);
	}

	public static MutableDecimal${precision}f four() {
		return new MutableDecimal${precision}f(Decimal${precision}f.FOUR);
	}

	public static MutableDecimal${precision}f five() {
		return new MutableDecimal${precision}f(Decimal${precision}f.FIVE);
	}

	public static MutableDecimal${precision}f six() {
		return new MutableDecimal${precision}f(Decimal${precision}f.SIX);
	}

	public static MutableDecimal${precision}f seven() {
		return new MutableDecimal${precision}f(Decimal${precision}f.SEVEN);
	}

	public static MutableDecimal${precision}f eight() {
		return new MutableDecimal${precision}f(Decimal${precision}f.EIGHT);
	}

	public static MutableDecimal${precision}f nine() {
		return new MutableDecimal${precision}f(Decimal${precision}f.NINE);
	}

	public static MutableDecimal${precision}f ten() {
		return new MutableDecimal${precision}f(Decimal${precision}f.TEN);
	}

	public static MutableDecimal${precision}f hundred() {
		return new MutableDecimal${precision}f(Decimal${precision}f.HUNDRED);
	}

	public static MutableDecimal${precision}f thousand() {
		return new MutableDecimal${precision}f(Decimal${precision}f.THOUSAND);
	}

	public static MutableDecimal${precision}f million() {
		return new MutableDecimal${precision}f(Decimal${precision}f.MILLION);
	}

	public static MutableDecimal${precision}f billion() {
		return new MutableDecimal${precision}f(Decimal${precision}f.BILLION);
	}

	public static MutableDecimal${precision}f minusOne() {
		return new MutableDecimal${precision}f(Decimal${precision}f.MINUS_ONE);
	}

	public static MutableDecimal${precision}f half() {
		return new MutableDecimal${precision}f(Decimal${precision}f.HALF);
	}

	public static MutableDecimal${precision}f tenth() {
		return new MutableDecimal${precision}f(Decimal${precision}f.TENTH);
	}

	public static MutableDecimal${precision}f hundredth() {
		return new MutableDecimal${precision}f(Decimal${precision}f.HUNDREDTH);
	}

	public static MutableDecimal${precision}f thousandth() {
		return new MutableDecimal${precision}f(Decimal${precision}f.THOUSANDTH);
	}

	public static MutableDecimal${precision}f millionth() {
		return new MutableDecimal${precision}f(Decimal${precision}f.MILLIONTH);
	}

	@Override
	public Decimal${precision}f toImmutableDecimal() {
		return Decimal${precision}f.valueOf(this);
	}

}
