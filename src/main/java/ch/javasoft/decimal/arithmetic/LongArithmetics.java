package ch.javasoft.decimal.arithmetic;

import java.math.BigDecimal;
import java.math.BigInteger;

import ch.javasoft.decimal.ScaleMetrics.Scale0f;

/**
 * The special case for longs with {@link Scale0f} and no rounding.
 */
public class LongArithmetics extends TruncatingArithmetics {
	
	/**
	 * Default singleton instance
	 */
	public static final LongArithmetics INSTANCE = new LongArithmetics();
	
	private LongArithmetics() {
		super(Scale0f.INSTANCE);
	}
	
	@Override
	protected DecimalArithmetics createDecimalArithmeticsFor(DecimalRounding rounding) {
		return new RoundingLongArithmetics(rounding);
	}

	@Override
	public int getScale() {
		return 0;
	}

	@Override
	public long one() {
		return 1L;
	}

	@Override
	public long multiply(long uDecimal1, long uDecimal2) {
		return uDecimal1 * uDecimal2;
	}

	@Override
	public long divide(long uDecimalDividend, long uDecimalDivisor) {
		return uDecimalDividend / uDecimalDivisor;
	}

	@Override
	public long fromLong(long value) {
		return value;
	}

	@Override
	public long fromDouble(double value) {
		return (long)value;
	}

	@Override
	public long fromBigInteger(BigInteger value) {
		return value.longValue();
	}

	@Override
	public long fromBigDecimal(BigDecimal value) {
		return value.longValue();
	}

	@Override
	public long fromUnscaled(long unscaledValue, int scale) {
		while (scale > 0) {
			unscaledValue /= 10;
			scale--;
		}
		while (scale < 0) {
			unscaledValue *= 10;
			scale++;
		}
		return unscaledValue;
	}

	@Override
	public long parse(String value) {
		return Long.parseLong(value);
	}

	@Override
	public long toLong(long uDecimal) {
		return uDecimal;
	}

	@Override
	public double toDouble(long uDecimal) {
		return (double)uDecimal;
	}

	@Override
	public BigDecimal toBigDecimal(long uDecimal) {
		return BigDecimal.valueOf(uDecimal);
	}

	@Override
	public String toString(long uDecimal) {
		return Long.toString(uDecimal);
	}

}
