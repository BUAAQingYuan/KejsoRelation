package cn.buaaqingyuan.KejsoRelation.Sql;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.buaaqingyuan.KejsoRelation.Entity.EntityRelation;
import cn.buaaqingyuan.KejsoRelation.Entity.MedicineInfo;
import cn.buaaqingyuan.KejsoRelation.Entity.RelationRecord;


public class SqlUtil {
	
	private static SqlSessionFactory sessionFactory = null;
	
	// 读取mybatis配置文件
	static {
		Reader reader = null;
		Properties prop = null;
		try {
			reader = Resources.getResourceAsReader("mybatis_config.xml");
			prop= new Properties();
			FileInputStream in = new FileInputStream("jdbc.properties");
			prop.load(in);
		} catch (IOException e) {
			System.out.println("读取数据库配置文件出错!");
			e.printStackTrace();
		}
		//sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		sessionFactory = new SqlSessionFactoryBuilder().build(reader, prop);
	}
	
	public static SqlSession getSession() {
		return sessionFactory.openSession();
	}
	
	
	public static List<String> getContents(SqlSession session,int offset) {
		
		List<String> contents = new ArrayList<String>();
		
		Map<String, Object> create = new HashMap<String, Object>();
		create.put("offset",String.valueOf(offset));
		
		String state="SqlMapper.TemplateMapper.getData";
		
		List<Object> data = session.selectList(state, create);
		for(Object one : data)
		{
			String abstract_cn = (String) ((HashMap<String,Object>)one).get("abstract_cn");
			contents.add(abstract_cn);
		}
		
		return contents;
	}
	
	public static List<String> getKeywords(SqlSession session,int offset) {
		List<String> keywords = new ArrayList<String>();
		Map<String, Object> create = new HashMap<String, Object>();
		create.put("offset",String.valueOf(offset));
		
		String state="SqlMapper.TemplateMapper.getKeyword";
		
		List<Object> data = session.selectList(state, create);
		for(Object one : data)
		{
			String keyword_cn = (String) ((HashMap<String,Object>)one).get("keyword_cn");
			String[] ks = keyword_cn.split("\\|\\|");
			for(int i=0;i<ks.length;i++)
			{
				keywords.add(ks[i]);
			}
		}
		return keywords;
	}
	
	public static void insertRelation(SqlSession session,RelationRecord record) {
		
		String state="SqlMapper.TemplateMapper.insertRelation";
		session.insert(state, record);
	}
	
	public static void insertEntityRelation(SqlSession session,EntityRelation record) {
		String state="SqlMapper.TemplateMapper.insertEntityRelation";
		session.insert(state, record);
	}
	
	public static List<RelationRecord> getRecordByEntity(SqlSession session,String entity) {
		
		String state="SqlMapper.TemplateMapper.getRecordByEntity";
		List<RelationRecord> records = new ArrayList<RelationRecord>();
		records = session.selectList(state, entity);
		return records;
	}
	
	public static List<EntityRelation> getAllMedicine(SqlSession session) {
		
		String state="SqlMapper.TemplateMapper.getAllMedicineEntity";
		List<EntityRelation> medicines = new ArrayList<EntityRelation>();
		medicines = session.selectList(state);
		return medicines;
	}
	
	
	public static EntityRelation getMedicineByEntity(SqlSession session,String entity) {	
		String state="SqlMapper.TemplateMapper.getMedicineByEntity";
		List<EntityRelation> medicines = new ArrayList<EntityRelation>();
		medicines = session.selectList(state, entity);
		if(medicines.size()>0)
			return medicines.get(0);
		return null;
	}
	
	public static void insertMedicineInfo(SqlSession session,MedicineInfo record) {
		String state="SqlMapper.TemplateMapper.insertMedicineInfo";
		session.insert(state, record);
	}
}
