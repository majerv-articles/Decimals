package ch.javasoft.decimal.util.generator.descriptor;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Map;

public class DecimalTemplateDescriptor implements TemplateDescriptor {

	@Override
	public String getTemplateName() {
		return "decimalNf.ftl";
	}

	@Override
	public String getClassName(final int precision) {
		return MessageFormat.format("Decimal{0}f", precision);
	}

	@Override
	public Map<String, Object> getDataModel() {
		return Collections.emptyMap();
	}

}
