package cn.buaaqingyuan.KejsoRelation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;

import cn.buaaqingyuan.KejsoRelation.Ner.EntityRecognizer;
import cn.buaaqingyuan.KejsoRelation.Sql.SqlUtil;


/*
 * 从科搜数据库中抽取标注实体的句子。
 * 
 */
public class GenerateExample {
	
	public static void generatefromsql(String outputfile,int total,int offset) throws IOException
	{
		SqlSession session=SqlUtil.getSession();
		
		List<String> contents = new ArrayList<String>();
		
		int block = 10000;
		
		File  phrase_f = new File(outputfile);
		
		for(int i=1;i<=total;i++)
		{
			contents = SqlUtil.getContents(session, offset);
			
			for(String content:contents)
			{
				List<String> results = EntityRecognizer.LableText(content);
				for(String result:results)
				{
					String type = ExampleExtractor.getType(result);
					FileUtils.write(phrase_f, type+"\t"+result+"\n", true);
				}
				
			}
			
			offset = offset + block ;
			System.out.format("already processed %f%%, total is %d,current offset is %d\n", (float)i*100/total,i*block,offset);
		}
		
		session.close();
		
	}
	
	public static void generatefromfile(String inputfile,String outputfile) throws IOException
	{	
		File  input_f = new File(inputfile);
		File  phrase_f = new File(outputfile);
		for(String line:FileUtils.readLines(input_f))
		{
			String type = ExampleExtractor.getType(line);
			FileUtils.write(phrase_f, type+"\t"+line+"\n", true);
		}
	}
	
	public static void extractKeywords(String outputfile,int total,int offset) throws IOException
	{
		SqlSession session=SqlUtil.getSession();
		
		List<String> contents = new ArrayList<String>();
		
		int block = 10000;
		
		File  phrase_f = new File(outputfile);
		
		for(int i=1;i<=total;i++)
		{
			contents = SqlUtil.getKeywords(session, offset);
			
			for(String content:contents)
			{
				FileUtils.write(phrase_f, content+"\n", true);
			}
			
			offset = offset + block ;
			System.out.format("already processed %f%%, total is %d,current offset is %d\n", (float)i*100/total,i*block,offset);
		}
		
		session.close();
		
	}
	
	public static void main(String[] args) throws IOException
	{
		//GenerateExample.generatefromsql("Extracted_examples_2000.txt", 29, 0);
		GenerateExample.generatefromfile("Medicine_old.txt","Medicine_new.txt");
		//GenerateExample.extractKeywords("journal_all_keywords.txt", 540, 0);
	}

}
