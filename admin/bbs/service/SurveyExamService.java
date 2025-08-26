package egovframework.admin.bbs.service;

import java.util.ArrayList;
import java.util.List;

public interface SurveyExamService {

	/**
	 * 지문 추가, 수정, 삭제
	 * @param list
	 * @param statusI
	 * @param surveyExam
	 * @return
	 * @throws Exception
	 */
	int surveyExamSheetCUD(ArrayList<SurveyExam> list, String status, SurveyExam surveyExam) throws Exception;

	List<SurveyExam> selectSurveyExamAll(SurveyExam surveyExam) throws Exception;

	List<SurveyExam> selectSurveyExamPop(SurveyExam surveyExam) throws Exception;

	List<SurveyExam> selectSurveyExamCdPop() throws Exception;

}
