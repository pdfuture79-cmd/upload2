package egovframework.admin.bbs.service.impl;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.admin.bbs.service.Survey;
import egovframework.admin.bbs.service.SurveyQuest;
import egovframework.admin.bbs.service.SurveyService;
import egovframework.common.WiseOpenConfig;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;


@Service("SurveyService")
public class SurveyServiceImpl extends AbstractServiceImpl implements SurveyService {

	@Resource(name="SurveyDao")
	private SurveyDao surveyDao;
	
	@Override
	public List<Survey> selectSurveyAll(Survey survey) throws Exception {
		return surveyDao.selectSurveyAll(survey);
	}

	@Override
	public Survey surveyRetr(Survey survey) throws Exception {
		return surveyDao.surveyRetr(survey);
	}

	
	/**
	 * 설문조사 데이터 저장/ 수정/ 삭제
	 */
	@Override
	public int saveSurveyCUD(Survey survey, String status) throws Exception {
		int result = 0;
		
		if(WiseOpenConfig.STATUS_I.equals(status)){
			int surveyId = surveyDao.getSurveyId();
			survey.setSurveyId(surveyId);
			result = surveyDao.insertSurvey(survey);
		}else if((WiseOpenConfig.STATUS_U.equals(status))){
        	result = surveyDao.updateSurvey(survey);
    	}else if((WiseOpenConfig.STATUS_D.equals(status))){
    		
    		
        	result = surveyDao.deleteSurvey(survey); //상위인 survey가 삭제되면 하위인 문항,지문도 다 삭제가 되도록한다.
        	
        	if(result > 0){
	        	if( surveyDao.selectSurveyQuestCnt(survey) > 0 ){ //설문만 존재하고 문항 또는 지문이 없으면 result가 0이 되기에 분기한다.
	        		result = surveyDao.deleteSurveyQuest(survey);// 문항삭제
	        		if( surveyDao.selectSurveyExamCnt(survey) > 0 ){
	        			result = surveyDao.deleteSurveyExam(survey); //지문삭제
	        		}
	        	}else{
	        		result = 1;
	        	}
        	}else{
        		result = 40;
        	}
        	
        	
    	}
		else{
			result = WiseOpenConfig.STATUS_ERR;
		}
		
		return result;
	}

	/**
	 * 미리보기 팝업에 해당하는 설문의 pk값 전달받아 팝업에 뿌린다.
	 */
	@Override
	public Survey selectSurveyPop(Survey survey) throws Exception {
		return surveyDao.selectSurveyPop(survey);
	}
	
	/**
	 * 팝업대상인 설문 조회
	 * @param Survey
	 * @return List<LinkedHashMap<String, Object>>
	 * @throws Exception
	 */
	public List<LinkedHashMap<String, Object>> selectSurveyPopup(Survey survey) throws Exception {
		return surveyDao.selectSurveyPopup(survey);
	}

	@Override
	public int selectSurveyUseYnUpdate(ArrayList<Survey> list) throws Exception {
		// TODO Auto-generated method stub
		int result = 0;
		for(Survey survey : list){
			result += surveyDao.selectSurveyUseYnUpdate(survey);
		}
		
		return result;
	}
}
