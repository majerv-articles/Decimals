package ch.javasoft.decimal.arithmetic;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import ch.javasoft.decimal.OverflowMode;
import ch.javasoft.decimal.ScaleMetrics;

/**
 * Base class for arithmetic implementations which involve rounding strategies.
 * The result of an operation that leads to an overflow is silently truncated.
 */
public class RoundingArithmetics extends AbstractScaledArithmetics {

	private final DecimalRounding rounding;

	/**
	 * Constructor for decimal arithmetics with given scale, rounding mode and
	 * {@link OverflowMode#SILENT SILENT} overflow mode.
	 * 
	 * @param scaleMetrics
	 *            the scale metrics for this decimal arithmetics
	 * @param roundingMode
	 *            the rounding mode to use for all decimal arithmetics
	 */
	public RoundingArithmetics(ScaleMetrics scaleMetrics, RoundingMode roundingMode) {
		this(scaleMetrics, DecimalRounding.valueOf(roundingMode));
	}

	/**
	 * Constructor for decimal arithmetics with given scale, rounding mode and
	 * {@link OverflowMode#SILENT SILENT} overflow mode.
	 * 
	 * @param scaleMetrics
	 *            the scale metrics for this decimal arithmetics
	 * @param rounding
	 *            the rounding mode to use for all decimal arithmetics
	 */
	public RoundingArithmetics(ScaleMetrics scaleMetrics, DecimalRounding rounding) {
		super(scaleMetrics);
		this.rounding = rounding;
	}

	public DecimalRounding getDecimalRounding() {
		return rounding;
	}

	@Override
	public final RoundingMode getRoundingMode() {
		return getDecimalRounding().getRoundingMode();
	}

	@Override
	public long multiply(long uDecimal1, long uDecimal2) {
		final ScaleMetrics scaleMetrics = getScaleMetrics();
		final long i1 = scaleMetrics.divideByScaleFactor(uDecimal1);
		final long i2 = scaleMetrics.divideByScaleFactor(uDecimal2);
//		final long f1 = scaleMetrics.moduloByScaleFactor(uDecimal1);
//		final long f2 = scaleMetrics.moduloByScaleFactor(uDecimal2);
		final long f1 = uDecimal1 - scaleMetrics.multiplyByScaleFactor(i1);
		final long f2 = uDecimal2 - scaleMetrics.multiplyByScaleFactor(i2);
		final long f1xf2 = f1 * f2;
		final long inc = scaleMetrics.divideByScaleFactor(f1xf2);
//		final long rem = scaleMetrics.moduloByScaleFactor(f1xf2);
		final long rem = f1xf2 - scaleMetrics.multiplyByScaleFactor(inc);
		final long unrounded = scaleMetrics.multiplyByScaleFactor(i1 * i2) + i1 * f2 + i2 * f1 + inc;
		return unrounded + rounding.calculateRoundingIncrement(unrounded, rem, one());
	}
	
	@Override
	public long divideByLong(long uDecimalDividend, long lDivisor) {
		final long quotient = uDecimalDividend / lDivisor;
		final long remainder = uDecimalDividend - quotient * lDivisor;
		return quotient + rounding.calculateRoundingIncrementForDivision(quotient, remainder, lDivisor);
	}

	@Override
	public long divide(long uDecimalDividend, long uDecimalDivisor) {
		//special cases first
		final SpecialDivisionResult special = SpecialDivisionResult.getFor(this, uDecimalDividend, uDecimalDivisor);
		if (special != null) {
			return special.divide(this, uDecimalDividend, uDecimalDivisor);
		}
		//div by power of 10
		final ScaleMetrics pow10 = ScaleMetrics.findByScaleFactor(Math.abs(uDecimalDivisor));
		if (pow10 != null) {
			return divideByPowerOf10(uDecimalDividend, uDecimalDivisor, pow10);
		}
		return divide128(uDecimalDividend, uDecimalDivisor);
	}
	private long divideByPowerOf10(long uDecimalDividend, long uDecimalDivisor, ScaleMetrics pow10) {
		final int scaleDiff = getScale() - pow10.getScale();
		if (scaleDiff <= 0) {
			//divide
			final ScaleMetrics scaler = ScaleMetrics.valueOf(-scaleDiff);
			final long truncatedValue = scaler.divideByScaleFactor(uDecimalDividend);
			final long truncatedDigits = uDecimalDividend - scaler.multiplyByScaleFactor(truncatedValue);
			if (uDecimalDivisor > 0) {
				return truncatedValue + rounding.calculateRoundingIncrementForDivision(truncatedValue, truncatedDigits, uDecimalDivisor);
			}
			return -truncatedValue + rounding.calculateRoundingIncrementForDivision(-truncatedValue, truncatedDigits, uDecimalDivisor);
			
		} else {
			//multiply
			final ScaleMetrics scaler = ScaleMetrics.valueOf(scaleDiff);
			final long quot = scaler.multiplyByScaleFactor(uDecimalDividend);
			return uDecimalDivisor > 0 ? quot : -quot;
		}
	}
	private long divide128(long uDecimalDividend, long uDecimalDivisor) {
		final ScaleMetrics scaleMetrics = getScaleMetrics();
		final DecimalArithmetics truncArith = scaleMetrics.getTruncatingArithmetics();
		final long unrounded = truncArith.divide(uDecimalDividend, uDecimalDivisor);
		final long product = truncArith.multiply(unrounded, uDecimalDivisor);
		final long delta = uDecimalDividend - product;
		if (delta != 0) {
			final long deltaScaled = scaleMetrics.multiplyByScaleFactor(delta);
			return unrounded + rounding.calculateRoundingIncrementForDivision(unrounded, deltaScaled, uDecimalDivisor);//OVERFLOW possible
		}
		return unrounded;
	}

	@Override
	public long invert(long uDecimal) {
		//special cases first
		final long one = one();
		final SpecialDivisionResult special = SpecialDivisionResult.getFor(this, one, uDecimal);
		if (special != null) {
			return special.divide(this, one, uDecimal);
		}
		//div by power of 10
		final ScaleMetrics pow10 = ScaleMetrics.findByScaleFactor(Math.abs(uDecimal));
		if (pow10 != null) {
			return divideByPowerOf10(one(), pow10.getScaleFactor(), pow10);
		}
		//check if one * one fits in long
		final ScaleMetrics scaleMetrics = getScaleMetrics();
		if (scaleMetrics.getScale() <= 9) {
			final long oneSquare = scaleMetrics.multiplyByScaleFactor(one);
			final long truncatedValue = oneSquare / uDecimal;
			final long truncatedDigits = oneSquare % uDecimal;
			return truncatedValue + rounding.calculateRoundingIncrementForDivision(truncatedValue, truncatedDigits, uDecimal);
		}
		//too big, use divide128 now
		return divide128(one, uDecimal);
	}

	@Override
	public long pow(long uDecimal, int exponent) {
		//FIXME implement with rounding (not only on multiplications!)
		return super.pow(uDecimal, exponent);
	}
	
	@Override
	public long shiftLeft(long uDecimal, int positions) {
		if (positions < 0) {
			if (positions == Integer.MIN_VALUE) {
				final long tmp = shiftRight(uDecimal, -(positions/2)); 
				return shiftRight(tmp, -(positions/2)); 
			}
			return super.shiftRight(uDecimal, -positions);
		}
		return uDecimal << positions;
	}
	
	@Override
	public long shiftRight(long uDecimal, int positions) {
		if (positions > 0) {
			//rounding may be necessary
			if (positions < 63) {
				final long truncated = uDecimal >> positions;
				final long remainder = uDecimal - (truncated << positions);
				final TruncatedPart truncatedPart = TruncatedPart.valueOf(Math.abs(remainder), 1L << positions);
				return truncated + rounding.calculateRoundingIncrement(truncated, uDecimal < 0, truncatedPart);
			}
			return rounding.calculateRoundingIncrement(0, uDecimal < 0, uDecimal == 0 ? TruncatedPart.ZERO : TruncatedPart.LESS_THAN_HALF_BUT_NOT_ZERO);
		}
		//shift left or shift by 0, no rounding
		return uDecimal >> positions;
	}

	@Override
	public long fromBigDecimal(BigDecimal value) {
		return value.multiply(getScaleMetrics().getScaleFactorAsBigDecimal()).setScale(0, getRoundingMode()).longValue();
	}

	@Override
	public long fromUnscaled(long unscaledValue, int scale) {
		final int targetScale = getScale();
		if (scale == 0) {
			return fromLong(unscaledValue);
		}
		long result = unscaledValue;
		if (scale < targetScale) {
			for (int i = scale; i < targetScale; i++) {
				result *= 10;
			}
		} else if (scale > targetScale) {
			int lastDigit = 0;
			boolean zeroAfterLastDigit = true;
			for (int i = targetScale; i < scale; i++) {
				zeroAfterLastDigit &= (lastDigit == 0);
				lastDigit = (int) Math.abs(result % 10);
				result /= 10;
			}
			//rounding
			result += rounding.calculateRoundingIncrement(result, unscaledValue < 0, lastDigit, zeroAfterLastDigit);
		}
		return result;
	}

	@Override
	public long parse(String value) {
		final int indexOfDot = value.indexOf('.');
		if (indexOfDot < 0) {
			return fromLong(Long.parseLong(value));
		}
		final long iValue;
		if (indexOfDot > 0) {
			//NOTE: here we handle the special case "-.xxx" e.g. "-.25"
			iValue = indexOfDot == 1 && value.charAt(0) == '-' ? 0 : Long.parseLong(value.substring(0, indexOfDot));
		} else {
			iValue = 0;
		}
		final String fractionalPart = value.substring(indexOfDot + 1);
		final long fValue;
		final int fractionalLength = fractionalPart.length();
		if (fractionalLength > 0) {
			long fractionDigits = Long.parseLong(fractionalPart);
			final int scale = getScale();
			for (int i = fractionalLength; i < scale; i++) {
				fractionDigits *= 10;
			}
			int lastDigit = 0;
			boolean zeroAfterLastDigit = true;
			for (int i = scale; i < fractionalLength; i++) {
				zeroAfterLastDigit &= (lastDigit == 0);
				lastDigit = (int) Math.abs(fractionDigits % 10);
				fractionDigits /= 10;
			}
			//rounding
			fractionDigits += rounding.calculateRoundingIncrement(fractionDigits, false, lastDigit, zeroAfterLastDigit);
			fValue = fractionDigits;
		} else {
			fValue = 0;
		}
		final boolean negative = iValue < 0 || value.startsWith("-");
		return iValue * one() + (negative ? -fValue : fValue);
	}

	@Override
	public long toLong(long uDecimal) {
		final ScaleMetrics scaleMetrics = getScaleMetrics();
		final long truncated = scaleMetrics.divideByScaleFactor(uDecimal);
		final long reminder = scaleMetrics.moduloByScaleFactor(uDecimal);
		return truncated + rounding.calculateRoundingIncrement(truncated, reminder, one());
	}

	@Override
	public BigDecimal toBigDecimal(long uDecimal, int scale) {
		return toBigDecimal(uDecimal).round(new MathContext(scale, getRoundingMode()));
	}
}
