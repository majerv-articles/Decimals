package ch.javasoft.decimal.util.generator.descriptor;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Map;

public class MutableDecimalTemplateDescriptor implements TemplateDescriptor {

	@Override
	public String getTemplateName() {
		return "mutable_decimalNf.ftl";
	}

	@Override
	public String getClassName(int precision) {
		return MessageFormat.format("MutableDecimal{0}f", precision);	}

	@Override
	public Map<String, Object> getDataModel() {
		return Collections.emptyMap();
	}

}