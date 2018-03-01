package cn.buaaqingyuan.KejsoRelation.Entity;

public class EntityRelation {
	private int id;
	private String entity;
	private String Cause_entitys;
	private String Describe_entitys;
	private String From_entitys;
	private String Identity_entitys;
	private String Medicine_entitys;
	private String Part_entitys;
	private String Position_entitys;
	private int edges;
	private int medicine_count;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getCause_entitys() {
		return Cause_entitys;
	}
	public void setCause_entitys(String cause_entitys) {
		Cause_entitys = cause_entitys;
	}
	public String getDescribe_entitys() {
		return Describe_entitys;
	}
	public void setDescribe_entitys(String describe_entitys) {
		Describe_entitys = describe_entitys;
	}
	public String getFrom_entitys() {
		return From_entitys;
	}
	public void setFrom_entitys(String from_entitys) {
		From_entitys = from_entitys;
	}
	public String getIdentity_entitys() {
		return Identity_entitys;
	}
	public void setIdentity_entitys(String identity_entitys) {
		Identity_entitys = identity_entitys;
	}
	public String getMedicine_entitys() {
		return Medicine_entitys;
	}
	public void setMedicine_entitys(String medicine_entitys) {
		Medicine_entitys = medicine_entitys;
	}
	public String getPart_entitys() {
		return Part_entitys;
	}
	public void setPart_entitys(String part_entitys) {
		Part_entitys = part_entitys;
	}
	public String getPosition_entitys() {
		return Position_entitys;
	}
	public void setPosition_entitys(String position_entitys) {
		Position_entitys = position_entitys;
	}
	public int getEdges() {
		return edges;
	}
	public void setEdges(int edges) {
		this.edges = edges;
	}
	public int getMedicine_count() {
		return medicine_count;
	}
	public void setMedicine_count(int medicine_count) {
		this.medicine_count = medicine_count;
	}
	
}
