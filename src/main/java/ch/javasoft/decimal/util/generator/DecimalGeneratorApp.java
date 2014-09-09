package ch.javasoft.decimal.util.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import ch.javasoft.decimal.util.generator.descriptor.DecimalTemplateDescriptor;
import ch.javasoft.decimal.util.generator.descriptor.MutableDecimalTemplateDescriptor;
import ch.javasoft.decimal.util.generator.descriptor.ScaleTemplateDescriptor;
import ch.javasoft.decimal.util.generator.descriptor.TemplateDescriptor;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class DecimalGeneratorApp {

	private static final String TEMPLATE_BASE_DIR = "src/main/resources/templates";
	private static final String GENERATED_SRC_DIR = "target/generated-sources/decimals/";
	
	private static final String PROPERTY_PRECISION = "precision";

	// XXX could be parameterized, or parsed via annotations etc.
	private static final Collection<? extends TemplateDescriptor> DESCRIPTORS = Arrays
			.asList(new DecimalTemplateDescriptor(), new MutableDecimalTemplateDescriptor(), new ScaleTemplateDescriptor());

	private static final Map<String, Object> DATA_MODEL = new HashMap<String, Object>();
	
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("Not enough arguments: count needed.");
		}
		
		final int maxPrecision = Integer.parseInt(args[0]);
		final Configuration cfg = createConfiguration();

		prepareTargetDir();
		
		for (int i = 0; i < maxPrecision; i++) {
			generateCodeFromTemplates(cfg, i+1);
		}
	}

	private static void prepareTargetDir() throws IOException {
		final File outputDir = new File(GENERATED_SRC_DIR);
		FileUtils.deleteDirectory(outputDir);
		outputDir.mkdirs();
	}

	private static void generateCodeFromTemplates(final Configuration cfg, final int precision) throws Exception {
		for (TemplateDescriptor descriptor : DESCRIPTORS) {
			generateCodeFromTemplate(cfg, descriptor, precision);
		}
	}

	private static Configuration createConfiguration() throws IOException {
		final Configuration cfg = new Configuration();

		cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_BASE_DIR));

		cfg.setDefaultEncoding("UTF-8");
		cfg.setLocale(Locale.US);
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		
		return cfg;
	}

	private static void generateCodeFromTemplate(final Configuration cfg,
			final TemplateDescriptor descriptor, final int precision) throws Exception {
		final Template template = cfg.getTemplate(descriptor.getTemplateName());
		
		DATA_MODEL.clear();
		DATA_MODEL.put(PROPERTY_PRECISION, precision);
		DATA_MODEL.putAll(descriptor.getDataModel());

		final String fileName = MessageFormat.format("{0}{1}.java", GENERATED_SRC_DIR, descriptor.getClassName(precision));
		generateCode(template, DATA_MODEL, fileName);
	}

	private static void generateCode(final Template template, final Map<String, Object> input, final String fileName) throws Exception {
		// just for fun
		final Writer consoleWriter = new OutputStreamWriter(System.out);
		template.process(input, consoleWriter);
		///
		
		final File outputFile = new File(fileName);
		final Writer fileWriter = new FileWriter(outputFile);
		try {
			template.process(input, fileWriter);
		} finally {
			fileWriter.close();
		}
	}

}
