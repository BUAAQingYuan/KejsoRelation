package cn.buaaqingyuan.KejsoRelation.Tool;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;

import cn.buaaqingyuan.KejsoRelation.Ner.EntityRecognizer;
import cn.buaaqingyuan.KejsoRelation.Sql.SqlUtil;

public class SeparateCorpus {

	public static void Separate_all_types(String inputfile) throws IOException 
	{
		File  input_f = new File(inputfile);
		File  part_f = new File("Part.txt");
		int part_count = 0;
		File  position_f = new File("Position.txt");
		int position_count = 0;
		File  from_f = new File("From.txt");
		int from_count = 0;
		File  identity_f = new File("Identity.txt");
		int identity_count = 0;
		File  cause_f = new File("Cause.txt");
		int cause_count = 0;
		File  describe_f = new File("Describe.txt");
		int describe_count = 0;
		File  medicine_f = new File("Medicine.txt");
		int medicine_count = 0;
		File  other_f = new File("Other.txt");
		int other_count = 0;
		int total_count = 0;
		for(String line:FileUtils.readLines(input_f))
		{
			String[] items = line.split("\t");
			if(items.length == 2)
			{
				total_count += 1;
				if(items[0].equals("Part"))
				{
					part_count += 1;
					FileUtils.write(part_f, items[1]+"\n", true);
				}else if(items[0].equals("Position"))
				{
					position_count += 1;
					FileUtils.write(position_f, items[1]+"\n", true);
				}else if(items[0].equals("From"))
				{
					from_count += 1;
					FileUtils.write(from_f, items[1]+"\n", true);
				}else if(items[0].equals("Identity"))
				{
					identity_count += 1;
					FileUtils.write(identity_f, items[1]+"\n", true);
				}else if(items[0].equals("Cause"))
				{
					cause_count += 1;
					FileUtils.write(cause_f, items[1]+"\n", true);
				}else if(items[0].equals("Describe"))
				{
					describe_count += 1;
					FileUtils.write(describe_f, items[1]+"\n", true);
				}else if(items[0].equals("Medicine"))
				{
					medicine_count += 1;
					FileUtils.write(medicine_f, items[1]+"\n", true);
				}else{
					other_count += 1;
					FileUtils.write(other_f, items[1]+"\n", true);
				}
			}
		}
		
		System.out.println("Part:"+part_count+","+part_count*1.0/total_count);
		System.out.println("Position:"+position_count+","+position_count*1.0/total_count);
		System.out.println("From:"+from_count+","+from_count*1.0/total_count);
		System.out.println("Identity:"+identity_count+","+identity_count*1.0/total_count);
		System.out.println("Cause:"+cause_count+","+cause_count*1.0/total_count);
		System.out.println("Describe:"+describe_count+","+describe_count*1.0/total_count);
		System.out.println("Medicine:"+medicine_count+","+medicine_count*1.0/total_count);
		System.out.println("Other:"+other_count+","+other_count*1.0/total_count);
		
	}
	
	public static void main(String[] args) throws IOException 
	{
		SeparateCorpus.Separate_all_types("Medicine_new.txt");
	}
}
