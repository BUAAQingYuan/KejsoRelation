package cn.buaaqingyuan.KejsoRelation.Tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;

import cn.buaaqingyuan.KejsoRelation.Entity.EntityRelation;
import cn.buaaqingyuan.KejsoRelation.Entity.MedicineInfo;
import cn.buaaqingyuan.KejsoRelation.Entity.RelationRecord;
import cn.buaaqingyuan.KejsoRelation.Sql.SqlUtil;

public class Extract_from_Corpus {
	
	public static void extracte_relation_from_file(String inputfile) throws IOException
	{
		
		SqlSession session=SqlUtil.getSession();
		
		File  input_f = new File(inputfile);
		
		int count = 0;
		
		for(String line:FileUtils.readLines(input_f))
		{
			count += 1;
			if(count % 3 == 1)
			{
				String[] items = line.split("\t",2);
				if(items.length == 2)
				{
					String type = items[0];
					String sentence = items[1];
					//对句子预处理
					sentence = sentence.replaceAll("\n", "").replaceAll(" ", "").replaceAll("<br>", "").replaceAll("\t", "");
					int num = 0;
					if(sentence.indexOf("</Entity>") != -1)  
				    {  
				        String[] str1 = (sentence+"##").split("</Entity>");  
				        num = str1.length-1;  
				    }
					if(num == 2 && !type.equals("Other"))
					{
						String e1 = "";
						String e2 = "";
						Pattern pat = Pattern.compile("<Entity>(.*?)</Entity>");
						Matcher m = pat.matcher(sentence);
						while(m.find())
						{
							if(e1.equals(""))
							{
								e1 = m.group(1).replaceAll("<NR>", "").replaceAll("<Position>", "").replaceAll("<br>", "");
							}else{
								e2 = m.group(1).replaceAll("<NR>", "").replaceAll("<Position>", "").replaceAll("<br>", "");
							}
						}
						sentence = sentence.replaceAll("<Entity>", "").replaceAll("</Entity>", "").replaceAll("<NR>", "").replaceAll("<Position>", "");
						System.out.println(type +" "+e1+" "+e2+" "+sentence);
						RelationRecord record = new RelationRecord();
						record.setRelation(type);
						record.setSentence(sentence);
						record.setE1(e1);
						record.setE2(e2);
						SqlUtil.insertRelation(session, record);
					}
					
				}
				
			}
		}
		session.commit();
		
	}
	
	
	public static void extracte_entity_from_file(String inputfile,String outputfile) throws IOException
	{
		
		
		File  input_f = new File(inputfile);
		File  output_f = new File(outputfile);
		
		Set entitys = new HashSet();
		
		int count = 0;
		
		for(String line:FileUtils.readLines(input_f))
		{
			count += 1;
			if(count % 3 == 1)
			{
				String[] items = line.split("\t",2);
				if(items.length == 2)
				{
					String type = items[0];
					String sentence = items[1];
					//对句子预处理
					sentence = sentence.replaceAll("\n", "").replaceAll(" ", "").replaceAll("<br>", "").replaceAll("\t", "");
					int num = 0;
					if(sentence.indexOf("</Entity>") != -1)  
				    {  
				        String[] str1 = (sentence+"##").split("</Entity>");  
				        num = str1.length-1;  
				    }
					if(num == 2 && !type.equals("Other"))
					{
						String e1 = "";
						String e2 = "";
						Pattern pat = Pattern.compile("<Entity>(.*?)</Entity>");
						Matcher m = pat.matcher(sentence);
						while(m.find())
						{
							if(e1.equals(""))
							{
								e1 = m.group(1).replaceAll("<NR>", "").replaceAll("<Position>", "").replaceAll("<br>", "");
							}else{
								e2 = m.group(1).replaceAll("<NR>", "").replaceAll("<Position>", "").replaceAll("<br>", "");
							}
						}
						sentence = sentence.replaceAll("<Entity>", "").replaceAll("</Entity>", "").replaceAll("<NR>", "").replaceAll("<Position>", "");
						System.out.println(type +" "+e1+" "+e2+" "+sentence);
						entitys.add(e1);
						entitys.add(e2);
					}
					
				}
				
			}
		}
		
		Iterator it = entitys.iterator();
		while(it.hasNext()) {
			FileUtils.write(output_f, it.next().toString()+"\n", true);
		}
		
	}
	
	
	public static void mapping_entitys(String entitysfile) throws IOException
	{
		
		SqlSession session=SqlUtil.getSession();
		File  input_f = new File(entitysfile);
		
		int count = 0;
		
		for(String line:FileUtils.readLines(input_f))
		{
			String entity = line.replaceAll("\n", "");
			List<RelationRecord> records = SqlUtil.getRecordByEntity(session, entity);
			for(RelationRecord record:records)
			{
				System.out.println(record.getRelation()+" "+record.getE1() +" "+record.getE2()+" "+record.getSentence());
			}
			
			Map relations = Util.mapping_entitys(records, entity);
			EntityRelation entity_relations = new EntityRelation();
			entity_relations.setEntity(entity);
			entity_relations.setCause_entitys(relations.get("Cause").toString());
			entity_relations.setDescribe_entitys(relations.get("Describe").toString());
			entity_relations.setFrom_entitys(relations.get("From").toString());
			entity_relations.setIdentity_entitys(relations.get("Identity").toString());
			entity_relations.setMedicine_entitys(relations.get("Medicine").toString());
			entity_relations.setPart_entitys(relations.get("Part").toString());
			entity_relations.setPosition_entitys(relations.get("Position").toString());
			entity_relations.setEdges(Integer.valueOf(relations.get("total").toString()));
			entity_relations.setMedicine_count(Integer.valueOf(relations.get("medicine_count").toString()));
			SqlUtil.insertEntityRelation(session, entity_relations);
			
			count += 1;
			if(count % 1000 == 0)
				session.commit();
			
		}
		
		session.commit();
	}
	
	
	//medicine info
	public static void mapping_medicineinfo(int limit) throws IOException
	{
		
		SqlSession session=SqlUtil.getSession();
		
		//get all medicine entity
		List<EntityRelation> medicines = new ArrayList<EntityRelation>();
		medicines = SqlUtil.getAllMedicine(session);
		System.out.println("medicine size:"+medicines.size());
		
		int total = 0;
		for(EntityRelation medicine:medicines)
		{
			System.out.println(medicine.getEntity());
			List<String> relations = new ArrayList<String>();
			int relation_count = 0;
			Set entitys = new HashSet();
			entitys.add(medicine.getEntity());
			relations.add(medicine.getEntity()+"#"+medicine.getMedicine_entitys());
			List<String> un_find = Util.getEntity(medicine.getMedicine_entitys());
			relation_count += un_find.size();
			while(un_find.size()>0)
			{
				String item = un_find.get(0);
				//System.out.println("find item:"+item);
				if(!entitys.contains(item))
				{
					System.out.println("relation size:"+relation_count);
					//max num
					if(relation_count > limit)
					{
						break;
					}
					entitys.add(item);
					un_find.remove(0);
					//expand
					EntityRelation temp = SqlUtil.getMedicineByEntity(session, item);
					List<String> temp_items = Util.getEntity(temp.getMedicine_entitys());
					relations.add(item+"#"+temp.getMedicine_entitys());
					relation_count += temp_items.size();
					//max num
					if(relation_count > limit)
					{
						break;
					}
					for(String one:temp_items)
					{
						un_find.add(one);
					}
				}else{
					un_find.remove(0);
				}
			}
			//解析结果
			String result = "";
			int count = 0;
			for(String relation_string : relations)
			{
				String[] parts = relation_string.split("#");
				List<String> temp_entitys = new ArrayList<String>();
				temp_entitys = Util.getEntity(parts[1]);
				for(String one:temp_entitys)
				{
					if(!one.equals(parts[0]))
					{
						result += parts[0]+"#"+one+"||";
						count += 1;
					}
				}
			}
			System.out.println(result);
			if(count < 5)
				continue;
			MedicineInfo info = new MedicineInfo();
			info.setEntity(medicine.getEntity());
			info.setRelations(result);
			info.setCount(count);
			SqlUtil.insertMedicineInfo(session, info);
			total += 1;
			if(total % 100 == 0)
			{
				session.commit();
			}
		}
		
		session.commit();
	}
	
	
	public static void main(String[] args) throws IOException 
	{
		//extracte_relation_from_file("train.txt");
		//extracte_entity_from_file("train.txt","entitys.txt");
		//mapping_entitys("entitys.txt");
		mapping_medicineinfo(50);
	}

}
