package egovframework.admin.bbs.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.admin.bbs.service.Survey;
import egovframework.admin.bbs.service.SurveyAns;
import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

@Repository("SurveyAnsDao")
public class SurveyAnsDao extends EgovComAbstractDAO{
	/**
	 * 설문관리 전체조회 
	 * @param survey
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Survey> selectSurveyAnsAll(Survey survey) throws Exception {
		return list("SurveyAnsDao.selectSurveyAnsAll",survey);
	}
	
	/**
	 * 설문결과 리스트 조회
	 * @param survey
	 * @return
	 * @throws Exception
	 */
	public SurveyAns selectSurveyAnsList(Survey survey) throws Exception {
		return (SurveyAns) selectByPk("SurveyAnsDao.selectSurveyAnsList",survey);
	}
	
	/**
	 * 설문결과 인원 조회
	 * @param survey
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public int selectSurveyAnsCnt(Survey survey) throws Exception {
		return  (Integer)getSqlMapClientTemplate().queryForObject("SurveyAnsDao.selectSurveyAnsCnt",survey);
	}

	/**
	 * ip중복체크
	 * @param surveyAns
	 * @return
	 */
	public int selectSurveyAnsIpdup(SurveyAns surveyAns) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("SurveyAnsDao.selectSurveyAnsIpdup",surveyAns);
	}

	/**
	 * 설문응답자 정보 등록
	 * @param surveyAns
	 * @return
	 * @throws Exception
	 */
	public int insertSurveyAns(SurveyAns surveyAns) throws Exception{
		return (Integer)update("SurveyAnsDao.insertSurveyAns",surveyAns);
	}
	
	/**
	 * 설문응답자 정보 삭제
	 * @param surveyAns
	 * @return
	 * @throws Exception
	 */
	public int deleteSurveyAns(SurveyAns surveyAns) throws Exception{
		return (Integer)update("SurveyAnsDao.deleteSurveyAns",surveyAns);
	}

	/**
	 * 설문결과 인원 조회
	 * @param surveyAns
	 * @return
	 * @throws Exception
	 */
	public int getSurveyAnsSeq(SurveyAns surveyAns) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("SurveyAnsDao.getSurveyAnsSeq",surveyAns);
	}
	
	/**
	 * 설문 결과 문항 갯수 조회(리스트)
	 * @param survey
	 * @return
	 * @throws Exception
	 */
	public List<SurveyAns> selectSurveyQuestCnt(Survey survey) throws Exception {
		return list("SurveyAnsDao.selectSurveyQuestCnt", survey);
	}
	
	/**
	 * 설문결과 항목의 X축 조회
	 * @param survey
	 * @return
	 * @throws Exception
	 */
	public List<SurveyAns> selectSurveyAnsPersonGubun(Survey survey) throws Exception {
		return list("SurveyAnsDao.selectSurveyAnsPersonGubun", survey);
	}

	/**
	 * 설문결과 팝업 항목 리스트 조회
	 * @param survey
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<LinkedHashMap<String,?>> selectSurveyAnsPopupListAll(Survey survey) throws Exception {
    	return list("SurveyAnsDao.selectSurveyAnsPopupListAll", survey);
    }

	/**
	 * 설문에 응답자가 한명이라도 있다면 수정불가하도록 체크한다.
	 * @param surveyAns
	 * @return
	 * @throws Exception
	 */
	public int selectSurveyAnsRegCheck(SurveyAns surveyAns) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("SurveyAnsDao.selectSurveyAnsRegCheck",surveyAns);
	}
	
	@SuppressWarnings("unchecked")
	public List<SurveyAns> surveyAnsListPopupExport(SurveyAns surveyAns) throws Exception {
    	return list("SurveyAnsDao.surveyAnsListPopupExport", surveyAns);
    }

	public List<Survey> selectSurveyAnsJobCount(Survey survey) throws Exception {
		return list("SurveyAnsDao.selectSurveyAnsJobCount", survey);
	}

	public List<Survey> selectSurveyAnsAgeCount(Survey survey) throws Exception {
		return list("SurveyAnsDao.selectSurveyAnsAgeCount", survey);
	}

	public List<Survey> selectSurveyAnsExamCount(Survey survey) throws Exception {
		return list("SurveyAnsDao.selectSurveyAnsExamCount", survey);
	}

	public List<Survey> selectSurveyAnsExamCountCheck(Survey survey) throws Exception {
		return list("SurveyAnsDao.selectSurveyAnsExamCountCheck", survey);
	}

	public List<Survey> selectSurveyAnsExamCountText(Survey survey) throws Exception {
		return list("SurveyAnsDao.selectSurveyAnsExamCountText", survey);
	}

	public List<SurveyAns> selectSurveyAnsExportInfo(SurveyAns surveyAns) throws Exception {
		return list("SurveyAnsDao.selectSurveyAnsExportInfo", surveyAns);
	}

	public List<HashMap<String, Object>> selectSurveyAnsResult(Survey survey) throws Exception{
		return list("SurveyAnsDao.selectSurveyAnsResult", survey);
	}

	public int getColCnt(Survey survey) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("SurveyAnsDao.getColCnt",survey);
	}
	
	public int inserSurveyFile(SurveyAns surveyAns) throws Exception{
		return (Integer)update("SurveyAnsDao.inserSurveyFile",surveyAns);
	}

	public int selectSurveyFileCnt(Survey survey) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("SurveyAnsDao.selectSurveyFileCnt",survey);
	}

	public List<Survey> surveyFileList(Survey survey) throws Exception{
		return  list("SurveyAnsDao.surveyFileList", survey);
	}

	public List<SurveyAns> surveyTotalAnsSearchList(Survey survey) {
		return list("SurveyAnsDao.surveyTotalAnsSearchList", survey);
	}

	public int beforeDeleteCheck(Survey survey) {
		return  (Integer)getSqlMapClientTemplate().queryForObject("SurveyAnsDao.beforeDeleteCheck",survey);
	}

	public int deletePersonerInfo(Survey survey) {
		 return (Integer)update("SurveyAnsDao.deletePersonerInfo",survey);
	}
	

}
