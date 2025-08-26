package egovframework.admin.bbs.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.admin.bbs.service.Survey;
import egovframework.admin.bbs.service.SurveyAns;
import egovframework.admin.bbs.service.SurveyAnsService;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.common.util.UtilEncryption;
import egovframework.common.util.UtilString;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
@Service("SurveyAnsService")
public class SurveyAnsServiceImpl extends AbstractServiceImpl implements SurveyAnsService {

	protected static final Log logger = LogFactory.getLog(SurveyAnsServiceImpl.class);
	
	@Resource(name="SurveyAnsDao")
	private SurveyAnsDao surveyAnsDao;

	/**
	 * 설문결과 조회
	 */
	@Override
	public List<Survey> surveyAnsService(Survey survey) throws Exception {
		
		List<Survey> suv=surveyAnsDao.selectSurveyAnsAll(survey);

		return suv;
	}
	
	/**
	 * 설문결과 리스트 조회
	 */
	@Override
	public SurveyAns surveyAnsList(Survey survey) throws Exception {
		return surveyAnsDao.selectSurveyAnsList(survey);
	}
	
	/**
	 * 설문결과 인원 조회
	 */
	public int surveyAnsCnt(Survey survey) throws Exception {
		return surveyAnsDao.selectSurveyAnsCnt(survey);
	}
	
	
	/**
	 * 설문응답시 ip중복체크
	 */
	@Override
	public int selectSurveyAnsIpdup(SurveyAns surveyAns) throws Exception {
		return surveyAnsDao.selectSurveyAnsIpdup(surveyAns);
	}
	
	/**
	 *  설문응답자 정보 등록
	 */
	@Override
	public int saveSurveyAnsCUD(SurveyAns surveyAns) throws Exception {
		int result = 0;
		result = surveyAnsDao.insertSurveyAns(surveyAns); 
		return result;
	}
	
	/**
	 * AnsSeq추출
	 */
	@Override
	public int getSurveyAnsSeq(SurveyAns surveyAns) throws Exception {
		return surveyAnsDao.getSurveyAnsSeq(surveyAns);
	}
	
	/**
	 * 설문 결과 문항 갯수 조회(리스트)
	 */
	@Override
	public List<SurveyAns> selectSurveyQuestCnt(Survey survey) throws Exception {
		return surveyAnsDao.selectSurveyQuestCnt(survey);
	}
	
	/**
	 * 설문결과 항목의 X축 조회
	 */
	@Override
	public List<SurveyAns> selectSurveyAnsPopInfo(Survey survey) throws Exception {
		return surveyAnsDao.selectSurveyAnsPersonGubun(survey);
	}
	
	/**
	 * 설문결과 팝업 항목 리스트 조회
	 */
	@Override
	public Map<String, Object> surveyAnsListPopupListAllIbPaging(Survey survey) throws Exception {
		List<SurveyAns> info = surveyAnsDao.selectSurveyAnsPersonGubun(survey);
		survey.setDynamicCol(setSelectSurveyAnsPopInfo(info));
		
		List<LinkedHashMap<String,?>> result = surveyAnsDao.selectSurveyAnsPopupListAll(survey);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultList", result);
		map.put("resultCnt", result.size());
		return map;
	}
	
	/**
	 * 설문결과 항목 리스트 조회시 select 절 생성
	 * @param obj
	 * @return
	 */
	private String setSelectSurveyAnsPopInfo(List<SurveyAns> obj) {
		if(obj.size() == 0){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		int cnt = 0;
		sb.append("A.EXAM_NM ");
		//for ( SurveyAns surveyAns : obj ) {
		for ( int i=0; i < obj.size(); i++ ) {
			if ( i != 0 ) {
				String cntString = ++cnt+"";
				cntString = UtilString.SQLInjectionFilter(cntString);
				sb.append("\n, SUM(CASE WHEN A.RNUM = " + cntString + " THEN ROUND(B.ANS_CNT / B.TOT_ANS_CNT *100, 1) ELSE 0 END) RATE" + cntString);
			}
		}
		return sb.toString();
	}

	/**
	 * 설문에 응답자가 한명이라도 있다면 수정불가하도록 체크한다.
	 */
	@Override
	public int selectSurveyAnsRegCheck(SurveyAns surveyAns) throws Exception {
		return surveyAnsDao.selectSurveyAnsRegCheck(surveyAns);
	}

	@Override
	public Map<String, Object> surveyAnsListPopupExport(SurveyAns surveyAns) throws Exception {
		List<SurveyAns> result = surveyAnsDao.surveyAnsListPopupExport(surveyAns);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultList", result);
		map.put("resultCnt", String.valueOf(result.size()));
		return map;
	}

	/**
	 * 설문참여한 사용자들의 직업과 명수 체크
	 */
	@Override
	public List<Survey> selectSurveyAnsJobCount(Survey survey) throws Exception {
		return surveyAnsDao.selectSurveyAnsJobCount(survey);
	}

	/**
	 * 설문참여한 사용자들의 연령 / 성별 카운트 체크
	 */
	@Override
	public List<Survey> selectSurveyAnsAgeCount(Survey survey) throws Exception {
		return surveyAnsDao.selectSurveyAnsAgeCount(survey);
	}

	@Override
	public List<Survey> selectSurveyAnsExamCount(Survey survey) throws Exception {
		return surveyAnsDao.selectSurveyAnsExamCount(survey);
	}

	@Override
	public List<Survey> selectSurveyAnsExamCountCheck(Survey survey) throws Exception {
		return surveyAnsDao.selectSurveyAnsExamCountCheck(survey);
	}

	@Override
	public List<Survey> selectSurveyAnsExamCountText(Survey survey) throws Exception {
		return surveyAnsDao.selectSurveyAnsExamCountText(survey);
	}

	@Override
	public List<SurveyAns> selectSurveyAnsExportInfo(SurveyAns surveyAns) throws Exception {
		return surveyAnsDao.selectSurveyAnsExportInfo(surveyAns);
	}

	@Override
	public Map<String, Object> selectSurveyAnsResult(Survey survey) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		int cnt = surveyAnsDao.getColCnt(survey);
	
		subqueryCreate(cnt, survey);
		List<HashMap<String, Object>> result  = surveyAnsDao.selectSurveyAnsResult(survey);
		map.put("result", result);
		map.put("cnt", cnt);
		return map;
	}
	
	private void subqueryCreate(int cnt, Survey survey) {
		String query1 = "";
		String query2 = "";
		
		for(int i=1; i <=cnt; i++){
			query1 += ", MIN(DECODE(QUEST_SEQ , '"+i+"', QUEST_NM)) AS COL"+i;
			query2 += ", MIN(DECODE(AA.QUEST_SEQ , '"+i+"', AA.ANS_VAL)) AS COL"+i;
		}
		
		survey.setSelectQuery1(query1);
		survey.setSelectQuery2(query2);
		/*StringBuffer query1 = new StringBuffer();
		StringBuffer query2 = new StringBuffer();
		
		for(int i=1; i <=cnt; i++){
			query1.append(", MIN(DECODE(QUEST_SEQ , '"+i+"', QUEST_NM)) AS COL"+i);
			query2.append(", MIN(DECODE(AA.QUEST_SEQ , '"+i+"', AA.ANS_VAL)) AS COL"+i);
		}
		
		survey.setSelectQuery1(query1.toString());
		survey.setSelectQuery2(query2.toString());*/
	}

	
	//설문조사 파일업로드 & 데이터 인서트 서비스
	@Override
	public SurveyAns surveyFileUpload(MultipartFile mpf, SurveyAns surveyAns) throws Exception{
		
		String path =  EgovProperties.getProperty("Globals.SurveyFilePath");
		
		int pos = mpf.getOriginalFilename().lastIndexOf(".");
		String fileExt = mpf.getOriginalFilename().substring(pos+1);
		
		//파일정보
		surveyAns.setFileExt(fileExt); //확장자
		surveyAns.setSrcFileNm(mpf.getOriginalFilename()); //원본파일이름
		surveyAns.setViewFileNm(surveyAns.getAnsSeq() + "." + fileExt); //화면에 보여줄 파일이름
		surveyAns.setSaveFileNm(surveyAns.getAnsSeq() + "." + fileExt); //실제 저장된 파일이름
		surveyAns.setFileSize(""+mpf.getSize());
		
		surveyAns.setViewFileNm(surveyAns.getViewFileNm().replaceAll("\\\\/","").replaceAll("\\\\" , "" ));  //시큐어코딩 조치
		File file = new File(path + surveyAns.getSurveyId() + "/" + surveyAns.getViewFileNm());
		
		if(!file.exists()){
			file.mkdirs();
			
		}
		
		try {
			
			mpf.transferTo(file);
		} catch (IllegalStateException e) { //시큐어코딩 조치 - Exception to String
			logger.debug(ExceptionUtils.getStackTrace(e)); 
			return null;
		} catch (IOException e) { //시큐어코딩 조치 - Exception to String
			logger.debug(ExceptionUtils.getStackTrace(e)); 
			return null;
		}
		
		
		
		return surveyAns;
	}

	@Override
	public int surveyAnsFileCUD(SurveyAns result) throws Exception{
		
		return surveyAnsDao.inserSurveyFile(result);
	}

	@Override
	public int selectSurveyFileCnt(Survey survey) throws Exception {
		return surveyAnsDao.selectSurveyFileCnt(survey);
	}

	@Override
	public List<Survey> surveyFileList(Survey survey) throws Exception {
		return surveyAnsDao.surveyFileList(survey);
	}

	@Override
	public List<SurveyAns> surveyTotalAnsSearchList(Survey survey)	throws Exception {
		
		List<SurveyAns> list = surveyAnsDao.surveyTotalAnsSearchList(survey);
		
		UtilEncryption encryption = new UtilEncryption(); 
		//복호화
		for(int i=0; i<list.size() ;i++){
			
			String ansEmail=  encryption.decrypt(list.get(i).getAnsEmail()); 
			
			list.get(i).setAnsEmail(ansEmail);
			
			String ansTel = encryption.decrypt(list.get(i).getAnsTel());
			
			list.get(i).setAnsTel(ansTel);
			
			String ansNm = encryption.decrypt(list.get(i).getAnsNm());
			
			list.get(i).setAnsNm(ansNm);
			
		}
		
		return list;
	}

	@Override
	public Map<String, String> deletePersonerInfo(Survey survey)
			throws Exception {
		Map<String, String> map = new HashMap<String,String>();
		
		
		if(surveyAnsDao.beforeDeleteCheck(survey)>0){
			map.put("msg","현재 진행중인 설문조사는 개인정보를 삭제할 수 없습니다.");
			map.put("code","0");
			
			return map;
		}else{
			surveyAnsDao.deletePersonerInfo(survey);
			
		};
		
		
		
		map.put("msg","삭제를 성공했습니다.");
		map.put("code","1");
		return map;
	}

}
