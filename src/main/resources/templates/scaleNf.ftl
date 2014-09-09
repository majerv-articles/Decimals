/**
 * Scale class for decimals with ${precision} {@link #getScale() fraction digit} and
 * {@link #getScaleFactor() scale factor} 10^${precision}.
 */
public final class Scale${precision}f extends ScaleMetrics {
	
	public static final Scale${precision}f INSTANCE = new Scale${precision}f();

	private static final long SCALE_FACTOR = (long) Math.pow(10, ${precision});

	private Scale${precision}f() {
		// hide constructor
	}

	@Override
	public int getScale() {
		return ${precision};
	}

	@Override
	public long getScaleFactor() {
		return SCALE_FACTOR;
	}

	@Override
	public long multiplyByScaleFactor(long factor) {
		return factor * SCALE_FACTOR;
	}

	// FIXME constants for the following methods in ScaleNf.java
	// should be defined/calculated and added here to the model
	@Override
	public long mulloByScaleFactor(int factor) {
		return (factor & LONG_MASK) * 1215752192;//(scaleFactor & LONG_MASK)
	}

	@Override
	public long mulhiByScaleFactor(int factor) {
		return (factor & LONG_MASK) * 23;//(scaleFactor >>> 32)
	}
	///
	
	@Override
	public long divideByScaleFactor(long dividend) {
		return dividend / SCALE_FACTOR;
	}

	@Override
	public long moduloByScaleFactor(long dividend) {
		return dividend % SCALE_FACTOR;
	}
	
}