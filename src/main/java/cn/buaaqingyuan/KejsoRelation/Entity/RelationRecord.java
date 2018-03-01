package cn.buaaqingyuan.KejsoRelation.Entity;

public class RelationRecord {
	private int id;
	private String sentence;
	private String relation;
	private String e1;
	private String e2;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSentence() {
		return sentence;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	public String getE1() {
		return e1;
	}
	public void setE1(String e1) {
		this.e1 = e1;
	}
	public String getE2() {
		return e2;
	}
	public void setE2(String e2) {
		this.e2 = e2;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
}
