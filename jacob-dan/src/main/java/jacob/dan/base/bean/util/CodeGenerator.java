package jacob.dan.base.bean.util;

import java.io.File;

import jacob.dan.user.entity.User;

/**
 * 代码生成器
 * @author ChangJian
 * @date 2019-01-30
 */
public class CodeGenerator {

	private String basePackage;
	private String superPackage;
	private String className;
	
	enum Category {
		Service("service"), 
		ServiceImpl("service/impl"), 
		Repository("repository");
		private String fileName;

		private Category(String fileName) {
			this.fileName = fileName;
		}
	}
	
	private CodeGenerator() {
		super();
	}

	private CodeGenerator(String basePackage, String superPackage, String className) {
		super();
		this.basePackage = basePackage;
		this.superPackage = superPackage;
		this.className = className;
	}

	private static CodeGenerator getInstance(Class<?> cls, String basePackage) {
		String[] folders = cls.getPackage().getName().split("\\.");
		String superPackage = "";
		for (int i = 0; i < folders.length - 1; i++) {
			superPackage += folders[i] + ".";
		}
		superPackage = superPackage.substring(0, superPackage.length() - 1);
		return new CodeGenerator(basePackage, superPackage, cls.getSimpleName());
	}
	
	public boolean generate(Category category) {
		File templateFile = new File("src/main/resources/generator/Template" + category + ".java");
		String content = FileUtils.getString(templateFile);
		content = content.replace("{superPackage}", superPackage);
		content = content.replace("{basePackage}", basePackage);
		content = content.replace("{ClassName}", className);
		FileUtils.newFile(content, "src/main/java/" + superPackage.replace('.', '/') + "/" + category.fileName + "/" + className + category + ".java");
		return true;
	}
	
	public static void generate(Package entityPackage) throws ClassNotFoundException {
		File packageFile = new File("src/main/java/" + entityPackage.getName().replace('.', '/'));
		for (String fileName : packageFile.list()) {
			Class<?> cls = Class.forName(entityPackage.getName() + "." + fileName.substring(0, (fileName.length() - ".java".length())));
			generate(cls);
		}
	}
	
	public static void generate(Class<?> cls) {
		CodeGenerator generator = CodeGenerator.getInstance(cls, "jacob.dan.base");
		for (Category category : Category.values()) {
			generator.generate(category);
			System.out.println(cls.getSimpleName() + " - " + category);
		}
	}
	
	public static void main(String[] args) {
		try {
			generate(User.class.getPackage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("finish");
	}
}
