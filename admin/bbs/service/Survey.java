package egovframework.admin.bbs.service;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import egovframework.common.grid.CommVo;
import egovframework.common.util.UtilString;

/**
 * 설문관리 데이터 처리 모델
 * @since 2014.08.06
 * @author jyson
 *
 */
@SuppressWarnings("serial")
public class Survey extends CommVo implements Serializable {


	private int    surveyId;        //설문조사id
	private String surveyNm;        //설문조사 제목
	private String surveyPpose;     //설문조사 목적
	private String langTag;         //설문조사언어
	private String ipDupYn;         //중복응답 여부
	private String startDttm;       //시작일자
	private String timeStaHh;       //시작일자
	private String endDttm;         //종료일자
	private String timeEndHh;       //종료일자
	private String orgCd;	        //담당부서
	private String usrCd;	        //담당자
	private String useYn;           //사용여부
	private String searchWord;      //검색어
	private String searchWd;        //검색선택
	private String surveyExp;       //설문조사 설명
	private String surveyDesc;      //설문조사 요약/요지 ..타입이 CLOB
	private String user1Yn;         //응답자 기본정보 수집여부
	private String user2Yn;	        //응답자 상세정보 수집여부
	private String loginYn;	        //회원가입 필수
	private String orgNm;	        //부서조직명
	private String usrNm;	        //담당자명
	private String endYn;	        //설문상태

	private String surveyJob1;
	private String surveyJob2;
	private String surveyJob3;
	private String surveyJob4;
	private String surveyJob5;
	private String surveyJob6;
	private String surveyJob7;
	private String surveyJob8;
	private String gubun;

	private String age19;
	private String age20;
	private String age30;
	private String age40;
	private String age50;
	private String age60;
	private String age61;
	private String m;
	private String f;

	private String exam1;
	private String exam2;
	private String exam3;
	private String exam4;
	private String exam5;
	private String exam6;
	private String exam7;
	private String exam8;
	private String exam9;
	private String exam10;

	private String examNm;
	private String examCount;

	private String examAns;

	private String surveyTitle;

	private String fileYn;
	private String fileSeq;
	private String srcFileNm;
	private String saveFileNm;
	private String viewFileNm;
	private String fileSize;
	private String fileExt;
	private String fileNm;

	private String selectQuery1;
	private String selectQuery2;

	private String axisX;		//가로축
	private String axisY;		//세로축
	private String prsnInfoYn;

	private int ansSeq;

	private String grpExp;

	private String delYn;
	private String regDttm;
	
	private String fileExp;
	
	
	private String userNameYn;
	private String userTelYn;
	private String userHpYn;
	private String userEmailYn;
	
	
	
	
	
	
	/**
	 * @return the age19
	 */
	public String getAge19() {
		return age19;
	}

	/**
	 * @param age19 the age19 to set
	 */
	public void setAge19(String age19) {
		this.age19 = age19;
	}

	/**
	 * @return the age61
	 */
	public String getAge61() {
		return age61;
	}

	/**
	 * @param age61 the age61 to set
	 */
	public void setAge61(String age61) {
		this.age61 = age61;
	}

	/**
	 * @return the surveyJob7
	 */
	public String getSurveyJob7() {
		return surveyJob7;
	}

	/**
	 * @param surveyJob7 the surveyJob7 to set
	 */
	public void setSurveyJob7(String surveyJob7) {
		this.surveyJob7 = surveyJob7;
	}

	/**
	 * @return the surveyJob8
	 */
	public String getSurveyJob8() {
		return surveyJob8;
	}

	/**
	 * @param surveyJob8 the surveyJob8 to set
	 */
	public void setSurveyJob8(String surveyJob8) {
		this.surveyJob8 = surveyJob8;
	}

	/**
	 * @return the userNameYn
	 */
	public String getUserNameYn() {
		return userNameYn;
	}

	/**
	 * @param userNameYn the userNameYn to set
	 */
	public void setUserNameYn(String userNameYn) {
		this.userNameYn = userNameYn;
	}

	/**
	 * @return the userTelYn
	 */
	public String getUserTelYn() {
		return userTelYn;
	}

	/**
	 * @param userTelYn the userTelYn to set
	 */
	public void setUserTelYn(String userTelYn) {
		this.userTelYn = userTelYn;
	}

	/**
	 * @return the userHpYn
	 */
	public String getUserHpYn() {
		return userHpYn;
	}

	/**
	 * @param userHpYn the userHpYn to set
	 */
	public void setUserHpYn(String userHpYn) {
		this.userHpYn = userHpYn;
	}

	/**
	 * @return the userEmailYn
	 */
	public String getUserEmailYn() {
		return userEmailYn;
	}

	/**
	 * @param userEmailYn the userEmailYn to set
	 */
	public void setUserEmailYn(String userEmailYn) {
		this.userEmailYn = userEmailYn;
	}

	/**
	 * @return the regDttm
	 */
	public String getRegDttm() {
		return regDttm;
	}

	/**
	 * @param regDttm the regDttm to set
	 */
	public void setRegDttm(String regDttm) {
		this.regDttm = regDttm;
	}

	/**
	 * @return the delYn
	 */
	public String getDelYn() {
		return delYn;
	}

	/**
	 * @param delYn the delYn to set
	 */
	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}

	public int getAnsSeq() {
		return ansSeq;
	}

	public void setAnsSeq(int ansSeq) {
		this.ansSeq = ansSeq;
	}

	public String getPrsnInfoYn() {
		return prsnInfoYn;
	}

	public void setPrsnInfoYn(String prsnInfoYn) {
		this.prsnInfoYn = prsnInfoYn;
	}

	public String getAxisX() {
		return axisX;
	}

	public void setAxisX(String axisX) {
		this.axisX = axisX;
	}

	public String getAxisY() {
		return axisY;
	}

	public void setAxisY(String axisY) {
		this.axisY = axisY;
	}

	public String getFileNm() {
		return fileNm;
	}

	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}

	public String getSelectQuery1() {
		return selectQuery1;
	}

	public void setSelectQuery1(String selectQuery1) {
		this.selectQuery1 = selectQuery1;
	}

	public String getSelectQuery2() {
		return selectQuery2;
	}

	public void setSelectQuery2(String selectQuery2) {
		this.selectQuery2 = selectQuery2;
	}

	public String getFileYn() {
		return fileYn;
	}

	public void setFileYn(String fileYn) {
		this.fileYn = fileYn;
	}

	public String getFileSeq() {
		return fileSeq;
	}

	public void setFileSeq(String fileSeq) {
		this.fileSeq = fileSeq;
	}

	public String getSrcFileNm() {
		return srcFileNm;
	}

	public void setSrcFileNm(String srcFileNm) {
		this.srcFileNm = srcFileNm;
	}

	public String getSaveFileNm() {
		return saveFileNm;
	}

	public void setSaveFileNm(String saveFileNm) {
		this.saveFileNm = saveFileNm;
	}

	public String getViewFileNm() {
		return viewFileNm;
	}

	public void setViewFileNm(String viewFileNm) {
		this.viewFileNm = viewFileNm;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public String getSurveyTitle() {
		return surveyTitle;
	}

	public void setSurveyTitle(String surveyTitle) {
		this.surveyTitle = surveyTitle;
	}

	public String getExamAns() {
		return examAns;
	}

	public void setExamAns(String examAns) {
		this.examAns = examAns;
	}

	public String getExamNm() {
		return examNm;
	}

	public void setExamNm(String examNm) {
		this.examNm = examNm;
	}

	public String getExamCount() {
		return examCount;
	}

	public void setExamCount(String examCount) {
		this.examCount = examCount;
	}

	public String getExam1() {
		return exam1;
	}

	public void setExam1(String exam1) {
		this.exam1 = exam1;
	}

	public String getExam2() {
		return exam2;
	}

	public void setExam2(String exam2) {
		this.exam2 = exam2;
	}

	public String getExam3() {
		return exam3;
	}

	public void setExam3(String exam3) {
		this.exam3 = exam3;
	}

	public String getExam4() {
		return exam4;
	}

	public void setExam4(String exam4) {
		this.exam4 = exam4;
	}

	public String getExam5() {
		return exam5;
	}

	public void setExam5(String exam5) {
		this.exam5 = exam5;
	}

	public String getExam6() {
		return exam6;
	}

	public void setExam6(String exam6) {
		this.exam6 = exam6;
	}

	public String getExam7() {
		return exam7;
	}

	public void setExam7(String exam7) {
		this.exam7 = exam7;
	}

	public String getExam8() {
		return exam8;
	}

	public void setExam8(String exam8) {
		this.exam8 = exam8;
	}

	public String getExam9() {
		return exam9;
	}

	public void setExam9(String exam9) {
		this.exam9 = exam9;
	}

	public String getExam10() {
		return exam10;
	}

	public void setExam10(String exam10) {
		this.exam10 = exam10;
	}

	public String getAge20() {
		return age20;
	}

	public void setAge20(String age20) {
		this.age20 = age20;
	}

	public String getAge30() {
		return age30;
	}

	public void setAge30(String age30) {
		this.age30 = age30;
	}

	public String getAge40() {
		return age40;
	}

	public void setAge40(String age40) {
		this.age40 = age40;
	}

	public String getAge50() {
		return age50;
	}

	public void setAge50(String age50) {
		this.age50 = age50;
	}

	public String getAge60() {
		return age60;
	}

	public void setAge60(String age60) {
		this.age60 = age60;
	}

	public String getM() {
		return m;
	}

	public void setM(String m) {
		this.m = m;
	}

	public String getF() {
		return f;
	}

	public void setF(String f) {
		this.f = f;
	}

	public String getSurveyJob1() {
		return surveyJob1;
	}

	public void setSurveyJob1(String surveyJob1) {
		this.surveyJob1 = surveyJob1;
	}

	public String getSurveyJob2() {
		return surveyJob2;
	}

	public void setSurveyJob2(String surveyJob2) {
		this.surveyJob2 = surveyJob2;
	}

	public String getSurveyJob3() {
		return surveyJob3;
	}

	public void setSurveyJob3(String surveyJob3) {
		this.surveyJob3 = surveyJob3;
	}

	public String getSurveyJob4() {
		return surveyJob4;
	}

	public void setSurveyJob4(String surveyJob4) {
		this.surveyJob4 = surveyJob4;
	}

	public String getSurveyJob5() {
		return surveyJob5;
	}

	public void setSurveyJob5(String surveyJob5) {
		this.surveyJob5 = surveyJob5;
	}

	public String getSurveyJob6() {
		return surveyJob6;
	}

	public void setSurveyJob6(String surveyJob6) {
		this.surveyJob6 = surveyJob6;
	}

	public String getGubun() {
		return gubun;
	}

	public void setGubun(String gubun) {
		this.gubun = gubun;
	}

	public String getSurveyDesc() {
		return surveyDesc;
	}

	public void setSurveyDesc(String surveyDesc) {
		this.surveyDesc = surveyDesc;
	}
	private int questSeq;
	private String dynamicCol;

	public String getOrgNm() {
		return orgNm;
	}

	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}

	public String getUsrNm() {
		return usrNm;
	}

	public void setUsrNm(String usrNm) {
		this.usrNm = usrNm;
	}

	public String getLoginYn() {
		return loginYn;
	}

	public void setLoginYn(String loginYn) {
		this.loginYn = loginYn;
	}

	public String getSurveyPpose() {
		return surveyPpose;
	}

	public void setSurveyPpose(String surveyPpose) {
		this.surveyPpose = surveyPpose;
	}

	public String getSurveyExp() {
		return surveyExp;
	}

	public void setSurveyExp(String surveyExp) {
		this.surveyExp = surveyExp;
	}

	public String getUser1Yn() {
		return user1Yn;
	}

	public void setUser1Yn(String user1Yn) {
		this.user1Yn = user1Yn;
	}

	public String getUser2Yn() {
		return user2Yn;
	}

	public void setUser2Yn(String user2Yn) {
		this.user2Yn = user2Yn;
	}

	public String getSearchWord() {
		return searchWord;
	}

	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}

	public String getSearchWd() {
		return searchWd;
	}

	public void setSearchWd(String searchWd) {
		this.searchWd = searchWd;
	}

	public int getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}

	public String getSurveyNm() {
		return surveyNm;
	}

	public void setSurveyNm(String surveyNm) {
		this.surveyNm = surveyNm;
	}

	public String getLangTag() {
		return langTag;
	}

	public void setLangTag(String langTag) {
		this.langTag = langTag;
	}

	public String getIpDupYn() {
		return ipDupYn;
	}

	public void setIpDupYn(String ipDupYn) {
		this.ipDupYn = ipDupYn;
	}

	public String getStartDttm() {
		return startDttm;
	}

	public void setStartDttm(String startDttm) {
		this.startDttm = startDttm;
	}

	public String getTimeStaHh() {
		return timeStaHh;
	}

	public void setTimeStaHh(String timeStaHh) {
		this.timeStaHh = timeStaHh;
	}

	public String getEndDttm() {
		return endDttm;
	}

	public void setEndDttm(String endDttm) {
		this.endDttm = endDttm;
	}

	public String getTimeEndHh() {
		return timeEndHh;
	}

	public void setTimeEndHh(String timeEndHh) {
		this.timeEndHh = timeEndHh;
	}

	public String getOrgCd() {
		return orgCd;
	}

	public void setOrgCd(String orgCd) {
		this.orgCd = orgCd;
	}

	public String getUsrCd() {
		return usrCd;
	}

	public void setUsrCd(String usrCd) {
		this.usrCd = usrCd;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	/** 설문목록 */
	public String getEndYn() {
		return endYn;
	}

	public void setEndYn(String endYn) {
		this.endYn = endYn;
	}

	public int getQuestSeq() {
		return questSeq;
	}

	public void setQuestSeq(int questSeq) {
		this.questSeq = questSeq;
	}

	public String getDynamicCol() {
		return dynamicCol;
	}

	public void setDynamicCol(String dynamicCol) {
		// 20161212 보안 점검 - 크로스사이트 스크립트
		this.dynamicCol = UtilString.SQLInjectionFilter(dynamicCol);
	}
	/**
	 * toString 메소드를 대치한다.
	 */
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public String getGrpExp() {
		return grpExp;
	}

	public void setGrpExp(String grpExp) {
		this.grpExp = grpExp;
	}

	public String getFileExp() {
		return fileExp;
	}

	public void setFileExp(String fileExp) {
		this.fileExp = fileExp;
	}


}
