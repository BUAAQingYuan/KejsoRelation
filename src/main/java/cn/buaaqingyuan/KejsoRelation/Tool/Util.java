package cn.buaaqingyuan.KejsoRelation.Tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.buaaqingyuan.KejsoRelation.Entity.RelationRecord;

public class Util {
	/*
	 *  keywords 条件选择
	 *  允许两种情况
	 *  1.中文字符、英文字符、-
	 *  2.中文字符
	 * */
	public static boolean  filter(String content)
	{
		// 过滤数字
		if(isNumber(content))
		{
			return false;
		}
		
		// 过滤第一个字符
		if(content.charAt(0) == '型')
		{
			return false;
		}
		
		// -不能在首尾
		if(content.indexOf("-") == 0 || content.indexOf("-") == (content.length()-1))
		{
			return false;
		}
		
		//包含特殊字符
		List<String> chs = new ArrayList<String>(Arrays.asList("(",")","的","和","与","及","研发","性能"));
		for(String one:chs)
		{
			if(content.contains(one))
			{
				return false;
			}
		}
		
		String result = content.replaceAll("[^(\\u4E00-\\u9FA5)]", "");
		
		// 长度为1 或 中文长度大于6
		if(content.length() < 2 || result.length() > 6 )
		{
			return false;
		}
		
		//纯中文字符
		if(result.length() == content.length())
		{
			return true;
		}
		
		if(isContainChinese(content)&&isLetterDigitOrChinese(content))
		{
			return true;
		}
		
		return false;
	}
	
	// 判断是否仅包含字母和汉字
	public static boolean isLetterDigitOrChinese(String str) {
		  String regex = "^[-a-zA-Z\u4e00-\u9fa5]+$";
		  return str.matches(regex);
	}
	
	
	// 判断是否为数字
	public static boolean isNumber(String str) {
		  String regex = "^[0-9]+$";
		  return str.matches(regex);
	}
	
	//判断是否包含中文
	public static boolean isContainChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
	    Matcher m = p.matcher(str);
	    if (m.find()) {
	    	return true;
	    }
	    return false;
	}
	
	public static String set_to_string(Set entitys)
	{
		Iterator it = entitys.iterator();
		String result = "";
		while(it.hasNext()) {
			result += it.next().toString() + "||";
		}
		return result;
	}
	
	//mapping entitys
	public static Map mapping_entitys(List<RelationRecord> records, String entity)
	{
		Map relations = new HashMap();
		
		Set cause_ = new HashSet();
		Set describe_ = new HashSet();
		Set from_ = new HashSet();
		Set identity_ = new HashSet();
		Set medicine_ = new HashSet();
		Set part_ = new HashSet();
		Set position_ = new HashSet();
		
		for(RelationRecord record:records)
		{
			String target="";
			if(record.getE1()==entity)
				target = record.getE2();
			else
				target = record.getE1();
			
			if(target=="")
			{
				continue;
			}
			
			if(record.getRelation().equals("Cause"))
			{
				cause_.add(target);
			}else if(record.getRelation().equals("Describe"))
			{
				describe_.add(target);
			}else if(record.getRelation().equals("From"))
			{
				from_.add(target);
			}else if(record.getRelation().equals("Identity"))
			{
				identity_.add(target);
			}else if(record.getRelation().equals("Medicine"))
			{
				medicine_.add(target);
			}else if(record.getRelation().equals("Part"))
			{
				part_.add(target);
			}else if(record.getRelation().equals("Position"))
			{
				position_.add(target);
			}
		}
		
		//计数
		int total = 0;
		int medicine_count = medicine_.size();
		total += cause_.size();
		total += describe_.size();
		total += from_.size();
		total += identity_.size();
		total += medicine_.size();
		total += part_.size();
		total += position_.size();
		
		relations.put("Cause", set_to_string(cause_));
		relations.put("Describe", set_to_string(describe_));
		relations.put("From", set_to_string(from_));
		relations.put("Identity", set_to_string(identity_));
		relations.put("Medicine", set_to_string(medicine_));
		relations.put("Part", set_to_string(part_));
		relations.put("Position", set_to_string(position_));
		relations.put("total", total);
		relations.put("medicine_count", medicine_count);
		return relations;
	}
	
	public static List<String> getEntity(String relation_string)
	{
		List<String> results = new ArrayList<String>();
		if(relation_string == null || relation_string == "")
		{
			return results;
		}
		String[] entitys = relation_string.split("\\|\\|");
		for(int i=0;i<entitys.length;i++)
		{
			results.add(entitys[i]);
		}
		return results;
	}
	
	public static void main(String[] args)
	{
		Set cause_ = new HashSet();
		cause_.add("A");
		cause_.add("B");
		
		System.out.println(cause_.toString());
	}
	
}
