package ch.javasoft.decimal.util.generator.descriptor;

import java.util.Map;

public interface TemplateDescriptor {

	String getTemplateName();
	String getClassName(final int precision);

	Map<String, Object> getDataModel();
	
}
