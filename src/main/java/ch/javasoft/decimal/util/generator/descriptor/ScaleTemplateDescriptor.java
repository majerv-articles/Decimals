package ch.javasoft.decimal.util.generator.descriptor;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Map;

public class ScaleTemplateDescriptor implements TemplateDescriptor {

	@Override
	public String getTemplateName() {
		return "scaleNf.ftl";
	}

	@Override
	public String getClassName(int precision) {
		return MessageFormat.format("Scale{0}f", precision);	}

	@Override
	public Map<String, Object> getDataModel() {
		// FIXME constants for the following methods in ScaleNf.java
		// should be defined/calculated and added here to the model
		/**
		public long mulloByScaleFactor(int factor) {
			return (factor & LONG_MASK) * 1215752192;//(scaleFactor & LONG_MASK)
		}

		@Override
		public long mulhiByScaleFactor(int factor) {
			return (factor & LONG_MASK) * 23;//(scaleFactor >>> 32)
		}
		*/
		return Collections.emptyMap();
	}

}


