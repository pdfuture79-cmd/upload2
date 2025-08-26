package egovframework.admin.bbs.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface SurveyAnsService {

	/**
	 * 설문관리 전체조회.
	 * @param survey
	 * @return
	 * @throws Exception
	 */
	public List<Survey> surveyAnsService(Survey survey) throws Exception;
	public SurveyAns surveyAnsList(Survey survey) throws Exception;
	public int surveyAnsCnt(Survey survey) throws Exception;
	
	/**
	 * ip중복체크 
	 * @param surveyAns
	 * @return
	 */
	public int selectSurveyAnsIpdup(SurveyAns surveyAns) throws Exception;
	/**
	 * 설문 팝업에서 등록
	 * @param surveyAns
	 * @return
	 * @throws Exception
	 */
	public int saveSurveyAnsCUD(SurveyAns surveyAns) throws Exception;
	
	/**
	 * AnsSeq 추출
	 * @param surveyAns
	 * @return
	 */
	public int getSurveyAnsSeq(SurveyAns surveyAns) throws Exception;
	
	/**
	 * 설문 결과 문항 갯수 조회(리스트)
	 * @param survey
	 * @return
	 * @throws Exception
	 */
	public List<SurveyAns> selectSurveyQuestCnt(Survey survey) throws Exception;
	
	public List<SurveyAns> selectSurveyAnsPopInfo(Survey survey) throws Exception;
	
	public Map<String, Object> surveyAnsListPopupListAllIbPaging(Survey survey) throws Exception;
	
	/**
	 * 설문에 응답자가 한명이라도 있다면 수정불가하도록 체크한다.
	 * @param surveyAns
	 * @return
	 * @throws Exception
	 */
	public int selectSurveyAnsRegCheck(SurveyAns surveyAns) throws Exception;
	
	
	public Map<String, Object> surveyAnsListPopupExport(SurveyAns surveyAns) throws Exception;
	public List<Survey> selectSurveyAnsJobCount(Survey survey) throws Exception;
	public List<Survey> selectSurveyAnsAgeCount(Survey survey) throws Exception;
	public List<Survey> selectSurveyAnsExamCount(Survey survey) throws Exception;
	public List<Survey> selectSurveyAnsExamCountCheck(Survey survey) throws Exception;
	public List<Survey> selectSurveyAnsExamCountText(Survey survey) throws Exception;
	public List<SurveyAns> selectSurveyAnsExportInfo(SurveyAns surveyAns)  throws Exception;
	public Map<String, Object> selectSurveyAnsResult(Survey survey) throws Exception;
	public SurveyAns surveyFileUpload(MultipartFile mpf, SurveyAns surveyAns) throws Exception;
	public int surveyAnsFileCUD(SurveyAns result) throws Exception;
	public int selectSurveyFileCnt(Survey survey)  throws Exception;
	public List<Survey> surveyFileList(Survey survey) throws Exception;
	public List<SurveyAns> surveyTotalAnsSearchList(Survey survey)  throws Exception;
	public Map<String, String> deletePersonerInfo(Survey survey)   throws Exception;
}
