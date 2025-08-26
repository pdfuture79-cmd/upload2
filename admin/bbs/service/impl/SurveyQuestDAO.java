package egovframework.admin.bbs.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.admin.bbs.service.Survey;
import egovframework.admin.bbs.service.SurveyQuest;
import egovframework.com.cmm.service.impl.EgovComAbstractDAO;


@Repository("SurveyQuestDAO")
public class SurveyQuestDAO extends EgovComAbstractDAO {


	/**
	 * survey_quest insert등록
	 * @param surveyQuest
	 * @return
	 * @throws Exception
	 */
	public int insertSurveyQuest(SurveyQuest surveyQuest) throws Exception {
		return (Integer)update("SurveyQuestDAO.insertSurveyQuest",surveyQuest);
	}

	/**
	 * 문항 조회
	 * @param surveyQuest
	 * @return
	 * @throws Exception
	 */
	public List<SurveyQuest> selectSurveyQuestAll(SurveyQuest surveyQuest) throws Exception {
		return list("SurveyQuestDAO.selectSurveyQuestAll",surveyQuest);
	}

	/**
	 * 문항 삭제 
	 * @param surveyQuest
	 * @return
	 * @throws Exception
	 */
	public int deleteSurveyQuest(SurveyQuest surveyQuest) throws Exception {
		return (Integer)update("SurveyQuestDAO.deleteSurveyQuest",surveyQuest);
	}

	/**
	 * 지문삭제
	 * @param surveyQuest
	 * @return
	 * @throws Exception
	 */
	public int deleteSurveyExam(SurveyQuest surveyQuest) throws Exception {
		return (Integer)update("SurveyQuestDAO.deleteSurveyExam",surveyQuest);
	}

	/**
	 * 미리보기 팝업 문항 출력
	 * @param surveyQuest
	 * @return
	 */
	public List<SurveyQuest> selectSurveyQuestPop(SurveyQuest surveyQuest) throws Exception {
		return list("SurveyQuestDAO.selectSurveyQuestPop",surveyQuest);
	}



	/**
	 * quest_seq값 생성
	 * @param surveyQuest2
	 * @return
	 * @throws Exception
	 */
	public int selectSurveyQuestSeq(SurveyQuest surveyQuest) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("SurveyQuestDAO.selectSurveyQuestSeq",surveyQuest);
	}

	/**
	 * 설문문항을 변경할때 기존 문항이 어떤 유형이였는지 select 
	 * @param surveyQuest
	 * @return
	 */
	public String selectSurveyQuestExamCd(SurveyQuest surveyQuest) {
		return (String)selectByPk("SurveyQuestDAO.selectSurveyQuestExamCd", surveyQuest);
	}

}
