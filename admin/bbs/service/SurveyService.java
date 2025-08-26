package egovframework.admin.bbs.service;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;

public interface SurveyService {

	
	/**
	 * 설문관리 전체조회.
	 * @param survey
	 * @return
	 * @throws Exception
	 */
	public List<Survey> selectSurveyAll(Survey survey) throws Exception;

	public Survey surveyRetr(Survey survey) throws Exception;


	public int selectSurveyUseYnUpdate(ArrayList<Survey> list) throws Exception;
	
	/**
	 * 데이터 저장/ 수정/ 삭제
	 * @param saveVO
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public int saveSurveyCUD(Survey survey, String status) throws Exception;

	public Survey selectSurveyPop(Survey survey) throws Exception;
	
	
	/**
	 * 팝업대상인 설문 조회
	 * @param Survey
	 * @return List<LinkedHashMap<String, Object>>
	 * @throws Exception
	 */
	public List<LinkedHashMap<String, Object>> selectSurveyPopup(Survey survey) throws Exception;
}
