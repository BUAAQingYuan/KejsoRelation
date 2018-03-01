package cn.buaaqingyuan.KejsoRelation;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class ExampleExtractor {
	
	private static Map<String,List<String>> patterns = new HashMap<String,List<String>>();
	
	private static Map<String,String> types = new HashMap<String,String>();
	
	//加载patterns
	static {
		String path = "relation_patterns";
		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
            for (File file2 : files) {
            	try {
            		String[] name = file2.getName().split("\\.");
					List<String> cur_pattern = FileUtils.readLines(file2);
					patterns.put(name[0], cur_pattern);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
		}
		//types
		types.put("部分_成分", "Part");
		types.put("地理_位置", "Position");
		types.put("来自_产出", "From");
		types.put("身份_职位", "Identity");
		types.put("因果", "Cause");
		types.put("指示_描述", "Describe");
		types.put("治疗_药物", "Medicine");
	}
	
	public static Map<String,List<String>> getPatterns()
	{
		return patterns;
	}
	
	public static String getType(String example)
	{
		for (Map.Entry<String, List<String>> entry : patterns.entrySet()) { 
			  String type = types.get(entry.getKey());
			  for(String pat:entry.getValue())
			  {
				if(Pattern.matches(pat, example))
				{
					return type;
				}
			  }
		}
		return "Other";
	}
	
	public static void main(String[] args) 
	{
		String example = "<<Entity><Position>江西赣州市副市长</Entity><Entity><NR>刘建平</Entity>与温氏集团副总裁罗旭芳共同签署了《赣州市政府与温氏集团200万头生猪产业化项目合作框架协议》";
		System.out.println(ExampleExtractor.getType(example));
	}

}
