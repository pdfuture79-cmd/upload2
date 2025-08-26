package egovframework.admin.bbs.service;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import egovframework.common.grid.CommVo;


@SuppressWarnings("serial")
public class SurveyAnsExam extends CommVo implements Serializable {

	private int surveyId;  //설문조사id
	private int ansSeq;
	
	//private String arrItemSeq;
	
	//private String arrQuestSeq;
	private int ansVal; //응답점수 item_val - > ans_val 
	private String examVal; //지문값 item_grade -> exam_val 컬럼변경 
	private String regDttm;
	
	
	private String examAns; //text 200자 등   item_ans -> exam_ans
	private String examSeq;
	private String questSeq;
	
	//배열로 받아 split하기 위해.
	private String[] questSeqArr;
	private String[] examGradeArr;
	private String[] examSeqArr;
	private String[] examAnsArr;
	
	//분기문을 사용하기 위해 지문의 타입을 받는다.
//	private String questItemCd;
//	private String[] questItemCdArr;
	private String questExamCd;
	private String[] questExamCdArr;
	
	
	
	
	
	public String getExamVal() {
		return examVal;
	}
	public int getAnsVal() {
		return ansVal;
	}
	public void setAnsVal(int ansVal) {
		this.ansVal = ansVal;
	}
	public void setExamVal(String examVal) {
		this.examVal = examVal;
	}
	public String getQuestExamCd() {
		return questExamCd;
	}
	public void setQuestExamCd(String questExamCd) {
		this.questExamCd = questExamCd;
	}
	public String[] getQuestExamCdArr() {
		return questExamCdArr;
	}
	public void setQuestExamCdArr(String[] questExamCdArr) {
		this.questExamCdArr = questExamCdArr;
	}
	public String[] getExamAnsArr() {
		return examAnsArr;
	}
	public void setExamAnsArr(String[] examAnsArr) {
		this.examAnsArr = examAnsArr;
	}
	public String[] getExamSeqArr() {
		return examSeqArr;
	}
	public void setExamSeqArr(String[] examSeqArr) {
		this.examSeqArr = examSeqArr;
	}
	public String[] getExamGradeArr() {
		return examGradeArr;
	}
	public void setExamGradeArr(String[] examGradeArr) {
		this.examGradeArr = examGradeArr;
	}
	public String[] getQuestSeqArr() {
		return questSeqArr;
	}
	public void setQuestSeqArr(String[] questSeqArr) {
		this.questSeqArr = questSeqArr;
	}
	
	public int getSurveyId() {
		return surveyId;
	}
	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}
	public int getAnsSeq() {
		return ansSeq;
	}
	public void setAnsSeq(int ansSeq) {
		this.ansSeq = ansSeq;
	}
	public String getQuestSeq() {
		return questSeq;
	}
	public void setQuestSeq(String questSeq) {
		this.questSeq = questSeq;
	}
	
	
	
	public String getExamAns() {
		return examAns;
	}
	public void setExamAns(String examAns) {
		this.examAns = examAns;
	}
	public String getRegDttm() {
		return regDttm;
	}
	public void setRegDttm(String regDttm) {
		this.regDttm = regDttm;
	}
	

	public String getExamSeq() {
		return examSeq;
	}
	public void setExamSeq(String examSeq) {
		this.examSeq = examSeq;
	}
	
	

	/**
	 * toString 메소드를 대치한다.
	 */
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	
	
	
}
