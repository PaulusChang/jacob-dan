package jacob.dan.base.bean.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.springframework.util.StringUtils {
	
	/**
	 * @author ChangJian
	 * @date 2017年10月14日
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		if (null == obj) {
			return null;
		}
		return obj.toString();
	}
	
    public static String getMatcher(String regex, String source) {  
        String result = "";  
        Pattern pattern = Pattern.compile(regex);  
        Matcher matcher = pattern.matcher(source);  
        while (matcher.find()) {  
            result = matcher.group(0);
        }  
        return result;  
    }  	
    
    public static String beanString(Object obj) {
    	if (null == obj) {
			return null;
		}
		List<Field> fields = ReflectUtils.listFields(obj.getClass());
		String[] fieldValueArray = new String[fields.size()];
		for (int i = 0; i < fieldValueArray.length; i++) {
			fieldValueArray[i] = fields.get(i).getName() + "=" + ReflectUtils.getFieldVal(obj, fields.get(i));
		}
		return obj.getClass().getName() + Arrays.toString(fieldValueArray);
    	
    }

}
