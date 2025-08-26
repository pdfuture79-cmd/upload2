package egovframework.admin.bbs.web;

/**
 *
 * 설문조사 클래스
 * @author jyson
 * @since 2014.08.07
 * @version 1.0
 * @see
 *
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.admin.basicinf.web.CommOrgController;
import egovframework.admin.bbs.service.Survey;
import egovframework.admin.bbs.service.SurveyAns;
import egovframework.admin.bbs.service.SurveyAnsExam;
import egovframework.admin.bbs.service.SurveyAnsExamService;
import egovframework.admin.bbs.service.SurveyAnsService;
import egovframework.admin.bbs.service.SurveyExam;
import egovframework.admin.bbs.service.SurveyExamService;
import egovframework.admin.bbs.service.SurveyQuest;
import egovframework.admin.bbs.service.SurveyQuestService;
import egovframework.admin.bbs.service.SurveyService;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.common.WiseOpenConfig;
import egovframework.common.base.constants.RequestAttribute;
import egovframework.common.base.constants.SessionAttribute;
import egovframework.common.code.service.CodeListService;
import egovframework.common.grid.IBSResultVO;
import egovframework.common.grid.IBSheetListVO;
import egovframework.common.helper.Messagehelper;
import egovframework.common.helper.TABListVo;
import egovframework.common.util.UtilEncryption;
import egovframework.common.util.UtilJson;


@Controller
public class SurveyController implements ApplicationContextAware ,InitializingBean{

	private ApplicationContext applicationContext;

	protected static final Log logger = LogFactory.getLog(SurveyController.class);

	private Map<String, Object> codeMap;

	@Resource
	private CodeListService commCodeListService;

	@Resource(name="SurveyService")
	private SurveyService surveyService;

	@Resource(name="SurveyQuestService")
	private SurveyQuestService surveyQuestService;

	@Resource(name="SurveyExamService")
	private SurveyExamService surveyExamService;

	@Resource(name="SurveyAnsService")
	private SurveyAnsService surveyAnsService;

	@Resource(name="SurveyAnsExamService")
	private SurveyAnsExamService surveyAnsExamService;


	static class Surveys extends HashMap<String, ArrayList<Survey>> {  }

	static class SurveyExams extends HashMap<String, ArrayList<SurveyExam>> {  }

	static class SurveyQuests extends HashMap<String, ArrayList<SurveyQuest>> {  }


	//공통 메시지 사용시 선언
	@Resource
	Messagehelper messagehelper;

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
		logger.info("EgovComIndexController setApplicationContext method has called!");
	}


	/**
	 * 공통코드를 조회 한다.
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute("codeMap")
	public Map<String, Object> getcodeMap() throws Exception{
		// 20161212 보안 점검 - 경쟁조건 : 검사시점과 사용시점(TOCTOU)
		Map<String, Object> codeMap = new HashMap<String, Object>();
		codeMap.put("examCdIbs", UtilJson.convertJsonString(commCodeListService.getCodeListIBS("C1011"))  ); //문항유형
		codeMap.put("ansAge", commCodeListService.getCodeList("C1013")); //연령
		codeMap.put("ansJob", commCodeListService.getCodeList("C1014")); //직업
		codeMap.put("ansTel", commCodeListService.getCodeList("C1015")); //연락처
		codeMap.put("ansHp", commCodeListService.getCodeList("C1007")); //휴대폰통신사번호
		codeMap.put("ansEmail", commCodeListService.getCodeList("C1009")); //이메일
		return codeMap;
	}

	/**설문관리목록 페이지 */
	@RequestMapping("/dream/bbs/surveyManagementPage.do")
	public String surveyPage(ModelMap model) throws Exception{
		//페이지 title을 가져오려면 반드시 *Page로 끝나야한다.
		//Interceptor(PageNavigationInterceptor)에서 조회함
		return "/admin/bbs/surveyManagement";
	}

	/** 설문관리목록을 전체 조회한다.*/
	@RequestMapping("/dream/bbs/surveyListAll.do")
	@ResponseBody
	public IBSheetListVO<Survey> surveyListAll(@ModelAttribute("searchVO") Survey survey, ModelMap model) throws Exception{
		List<Survey> list = surveyService.selectSurveyAll(survey);
		return new IBSheetListVO<Survey>(list, list.size());
	}

	/**
	 * 설문관리 에서 사용여부 수정사항 저장.
	 * @param data
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/surveyListUseYnUpdate.do")
	@ResponseBody
	public IBSResultVO<Survey> surveyListUseYnUpdate(@RequestBody Surveys data, Locale locale) throws Exception{
		ArrayList<Survey> list = data.get("data");
		int result = surveyService.selectSurveyUseYnUpdate(list);
		String resmsg;

		return new IBSResultVO<Survey>(result, messagehelper.getSavaMessage(result));
	}

	/**설문관리 sheet클릭 단건 조회  */
	@RequestMapping("/dream/bbs/surveyRetr.do")
	@ResponseBody
	public TABListVo<Survey> surveyRetr(@RequestBody Survey survey, ModelMap model) throws Exception{
	//	model.addAttribute("surveyAnsCheck", "11232" );
		//surveyAnsService.selectSurveyAnsIpdup(surveyAns)
		return new TABListVo<Survey>(surveyService.surveyRetr(survey));
	}

	/** 설문관리 등록 (단건 데이터 저장) insert */
	@RequestMapping("/dream/bbs/surveyReg.do")
	@ResponseBody
	public IBSResultVO<Survey> SurveyReg(@ModelAttribute Survey survey, ModelMap model) throws Exception {
		
		String tmpDttm= survey.getStartDttm();
		String timeStaHh = survey.getTimeStaHh();
		String startDttm = tmpDttm + " " + timeStaHh;
		survey.setStartDttm(startDttm);

		tmpDttm= survey.getEndDttm();
		String timeEndHh = survey.getTimeEndHh();
		String endDttm = tmpDttm + " " + timeEndHh;
		survey.setEndDttm(endDttm);
		
		int result = 0;
		result = surveyService.saveSurveyCUD(survey, WiseOpenConfig.STATUS_I);
		
		
		
		return new IBSResultVO<Survey>(result, messagehelper.getSavaMessage(result));
	}

	/** 설문관리 수정 (단건 데이터 수정) update */
	@RequestMapping("/dream/bbs/surveyUpd.do")
	@ResponseBody
	public IBSResultVO<Survey> surveyUpd(@ModelAttribute Survey survey, ModelMap model) throws Exception {
		//등록, 변경, 삭제 처리시  등록자, 수정자 Id를 세션에서 가져올 경우 *CUD 메소드명을 사용해야함
		int result = 0;

		String tmpDttm= survey.getStartDttm();
		String timeStaHh = survey.getTimeStaHh();
		String startDttm = tmpDttm + " " + timeStaHh;
		survey.setStartDttm(startDttm);

		tmpDttm= survey.getEndDttm();
		String timeEndHh = survey.getTimeEndHh();
		String endDttm = tmpDttm + " " + timeEndHh;
		survey.setEndDttm(endDttm);
		result = surveyService.saveSurveyCUD(survey, WiseOpenConfig.STATUS_U);
		return new IBSResultVO<Survey>(result, messagehelper.getUpdateMessage(result));
	}

	/** 설문관리 삭제  delete */
	@RequestMapping("/dream/bbs/surveyDel.do")
	@ResponseBody
	public IBSResultVO<Survey> surveyDel(@ModelAttribute Survey survey, ModelMap model) throws Exception {
		//등록, 변경, 삭제 처리시  등록자, 수정자 Id를 세션에서 가져올 경우 *CUD 메소드명을 사용해야함
		int result = 0;
		result = surveyService.saveSurveyCUD(survey, WiseOpenConfig.STATUS_D);
		
		String msg = "";
		if(result == 40){
			msg = "설문 결과가 존재할 경우 삭제할 수 없습니다.";
		}else{
			msg = messagehelper.getDeleteMessage(result);
		}
		return new IBSResultVO<Survey>(result, msg  );
	}

	/** 설문 미리보기 팝업창 */
	@RequestMapping("/dream/bbs/popup/surveyPop.do")
	public String surveyPop(String adminYn , HttpServletRequest request, ModelMap model, @ModelAttribute Survey survey, @ModelAttribute SurveyQuest surveyQuest, @ModelAttribute SurveyExam surveyExam , @ModelAttribute SurveyAns surveyAns) throws Exception{

		// ip중복체크 클라이언트의 ip 찾는다.
		//단, 윈도우7 이상에서는 IPv6를 우선지원하기에 -> IPv4로 변경하기 위해
		//Run- Configurations 에서 arguments 제일 하단에 -Djava.net.preferIPv4Stack=true 붙여넣기 해주면 변경된다.
//		String clientIp = request.getHeader("X_FORWARDED_ROR");
		String clientIp = (String) request.getAttribute(RequestAttribute.USER_IP);
		if(null == clientIp || clientIp.length() == 0 || clientIp.toLowerCase().equals("unknown")){
			clientIp = request.getRemoteAddr();
			surveyAns.setAnsIp(clientIp); //지금 설문을 하려는 사용자의 ip를 획득한다.
		}

		model.addAttribute("Survey", surveyService.selectSurveyPop(survey)); // pk 넘겨준다.
		model.addAttribute("SurveyQuest", surveyQuestService.selectSurveyQuestPop(surveyQuest)); // pk 넘겨준다.
		model.addAttribute("SurveyQuestCnt", surveyQuestService.selectSurveyQuestPop(surveyQuest).size()); // 설문시 문항의 개수를 카운트한다.
		model.addAttribute("SurveyExam", surveyExamService.selectSurveyExamPop(surveyExam));  // pk 넘겨준다.
		model.addAttribute("SurveyExamCd", surveyExamService.selectSurveyExamCdPop() ); //  Exam CD값 지문
		model.addAttribute("ClientIpDup", surveyAnsService.selectSurveyAnsIpdup(surveyAns)); //ip중복체크
		model.addAttribute("ClientIp", clientIp); //ip중복체크
		model.addAttribute("adminYn", adminYn); //ip중복체크
		return "/admin/bbs/popup/surveypop";
	}

	/** 설문 팝업창 */
	@RequestMapping("/portal/bbs/popup/surveyPop.do")
	public String portalSurveyPop(HttpServletRequest request, ModelMap model, @ModelAttribute Survey survey, @ModelAttribute SurveyQuest surveyQuest, @ModelAttribute SurveyExam surveyExam , @ModelAttribute SurveyAns surveyAns) throws Exception{

		// ip중복체크 클라이언트의 ip 찾는다.
		//단, 윈도우7 이상에서는 IPv6를 우선지원하기에 -> IPv4로 변경하기 위해
		//Run- Configurations 에서 arguments 제일 하단에 -Djava.net.preferIPv4Stack=true 붙여넣기 해주면 변경된다.
//		String clientIp = request.getHeader("X_FORWARDED_ROR");
		String clientIp = (String) request.getAttribute(RequestAttribute.USER_IP);
		
		//url로 호출하여 jsp 호출
		//파라미터 popupYn=Y
		String qs = (String) request.getQueryString();
		
		//System.out.println("surveyQs : "+qs);
		
		String popupYn = "N";
		
		String[] qs_arr = qs.split("&");
		
		for(String qs_s :qs_arr ){
			//System.out.println(qs_s);
			if(qs_s.equals("popupYn=Y")){popupYn="Y";}
		}
		
		
		if(null == clientIp || clientIp.length() == 0 || clientIp.toLowerCase().equals("unknown")){
			clientIp = request.getRemoteAddr();
			surveyAns.setAnsIp(clientIp); //지금 설문을 하려는 사용자의 ip를 획득한다.
		}
		Survey s = surveyService.selectSurveyPop(survey);
		model.addAttribute("Survey",s ); 
		model.addAttribute("SurveyQuest", surveyQuestService.selectSurveyQuestPop(surveyQuest)); 
		model.addAttribute("SurveyQuestCnt", surveyQuestService.selectSurveyQuestPop(surveyQuest).size()); // 설문시 문항의 개수를 카운트한다.
		model.addAttribute("SurveyExam", surveyExamService.selectSurveyExamPop(surveyExam));  // pk 넘겨준다.
		model.addAttribute("SurveyExamCd", surveyExamService.selectSurveyExamCdPop() ); //  Exam CD값 지문
		model.addAttribute("ClientIpDup", surveyAnsService.selectSurveyAnsIpdup(surveyAns)); //ip중복체크
		model.addAttribute("ClientIp", clientIp); //ip중복체크
		model.addAttribute("adminYn", popupYn); //ip중복체크
		return "/admin/bbs/popup/surveypop";
	}

	/**
	 * 설문에 응답자가 한명이라도 있다면 수정불가하도록 체크한다.
	 * @param survey
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/surveyAnsRegCheck.do")
	@ResponseBody
	public IBSResultVO<SurveyAns> surveyAnsRegCheck(@ModelAttribute SurveyAns surveyAns, ModelMap model) throws Exception {
		int result = 0;
		result = surveyAnsService.selectSurveyAnsRegCheck(surveyAns);
		return new IBSResultVO<SurveyAns>(result, messagehelper.getSavaMessage(result));
	}


	/** 설문 응답자 개인정보, 설문 정보 등록   */
	@RequestMapping("/portal/bbs/surveyAnsExamPopReg.do")
	@ResponseBody
	public void surveyAnsExamPopReg(HttpSession session, @ModelAttribute("searchVO") SurveyAnsExam surveyAnsExam, ModelMap model, @ModelAttribute SurveyAns surveyAns , HttpServletResponse response) throws Exception {
		int result = 0;
		String ansCd="";
		PrintWriter writer = response.getWriter();
		String input = surveyAns.getAnsCd();
		Pattern pattern = Pattern.compile("[A-Z0-9].{0,10}");
  	Matcher matcher;
  	StringBuffer sb = new StringBuffer();
  	

  	//대문자, 숫자 , 10자내외로만 허용
  	if(input != null) {
    	if(input.length() > 0 && input.length() <= 10  ){ //단어수는 1~10자이상일때 실행
    		for(int i=0; i < input.length(); i++){
	    		matcher = pattern.matcher(Character.toString(input.charAt(i)));
	    		if(matcher.matches()) { //true가 아니면 값을 넣어주지 않으면 ,, 값을 기본으로..
	    			sb.append(input.charAt(i));
	    		}else{
	    			sb.setLength(0); //버퍼 초기화
	    			sb.trimToSize(); //배열메모리 최소화
	    			break;
	    		}
	    	}
	    	ansCd = sb.toString();
    	}
  	}
  	surveyAns.setAnsCd(ansCd); //회원구분 설정

		int ansSeq= surveyAnsService.getSurveyAnsSeq(surveyAns); //AnsSeq 추출 - 답변 시퀀스

		surveyAns.setAnsSeq(ansSeq); //ansSeq추출값 set
		if( "Y".equals(surveyAns.getLoginYn()) ){
			surveyAns.setUserId((String)session.getAttribute(SessionAttribute.USER_ID)); //사용자 id (로그인시)
		}
		String[] examSeq = surveyAnsExam.getExamSeq().split(" "); //ExamSeq
		surveyAnsExam.setExamSeqArr(examSeq);

		String[] questSeq = surveyAnsExam.getQuestSeq().split(" "); //questSeq
		surveyAnsExam.setQuestSeqArr(questSeq);

		String[] examVal = surveyAnsExam.getExamVal().split(" "); //ExamGrade
		surveyAnsExam.setExamGradeArr(examVal);

		String[] examAns = surveyAnsExam.getExamAns().split("12.34,56"); //ExamAns
		surveyAnsExam.setExamAnsArr(examAns);

		String[] questExamCd = surveyAnsExam.getQuestExamCd().split(" "); //questExamCd
		surveyAnsExam.setQuestExamCdArr(questExamCd);

		surveyAnsExam.setAnsSeq(ansSeq); //AnsSeq 추출값 set
		
		
		UtilEncryption encryption = new UtilEncryption();
		
		//이메일 암호화
		if(surveyAns.getAnsEmail()!=null && !"".equals(surveyAns.getAnsEmail())){
			
			String ansEmail = surveyAns.getAnsEmail().split(",")[0] +"@"+ surveyAns.getAnsEmail().split(",")[1];
			ansEmail = encryption.encrypt(ansEmail);
			
			surveyAns.setAnsEmail(ansEmail);
			
		}
		
		//전화번호 암호화
		if(surveyAns.getAnsTel()!=null && !"".equals(surveyAns.getAnsTel())){
			
			String ansTel = surveyAns.getAnsTel().split(",")[0] + "-" + surveyAns.getAnsTel().split(",")[1] + "-" + surveyAns.getAnsTel().split(",")[2];
			ansTel = encryption.encrypt(ansTel);
			
			surveyAns.setAnsTel(ansTel);
			
		}
		
		//이름 암호화
		if(surveyAns.getAnsNm()!=null && !"".equals(surveyAns.getAnsNm())){
			
			String ansNm = surveyAns.getAnsNm();
			ansNm = encryption.encrypt(ansNm);
			
			surveyAns.setAnsNm(ansNm);
			
		}
		
		
		
		result = surveyAnsExamService.saveSurveyAnsExamCUD(surveyAnsExam, surveyAns); //설문 정보 등록


		response.setContentType("text/html;charset=UTF-8");

		writer.println(ansSeq);
		writer.close();

	}
	
	
	/** 파일업로드   */
	@RequestMapping("/portal/bbs/surveyFileUpload.do")
	public void surveyFileUpload(MultipartHttpServletRequest req ,SurveyAns surveyAns,Locale locale,HttpServletResponse response ) throws Exception {
		
		Iterator itr = req.getFileNames();
		PrintWriter writer = response.getWriter();
		SurveyAns result = null;
		
		if(itr.hasNext()){
			MultipartFile mpf = req.getFile((String) itr.next());
			result = surveyAnsService.surveyFileUpload(mpf,surveyAns);
		}
		
		int result_db = 0;
		if(result!=null){
			result_db = surveyAnsService.surveyAnsFileCUD(result);
		}
		
		response.setContentType("text/html;charset=UTF-8");

		writer.println(result_db);
		writer.close();
	}



	/**
	 * 설문결과목록 페이지로 이동한다.
	 * @param  model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/surveyAnsListPage.do")
	public String surveyAnsListPage(ModelMap model) throws Exception{
		//페이지 title을 가져오려면 반드시 *Page로 끝나야한다.
		//Interceptor(PageNavigationInterceptor)에서 조회함
		return "/admin/bbs/surveyAnsList";
	}

	/**
	 * 설문결과목록을 전체 조회한다.
	 * @param  survey
	 * @param  model
	 * @return IBSheetListVO<Survey>
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/surveyAnsListAll.do")
	@ResponseBody
	public IBSheetListVO<Survey> surveyAnsListAll(@ModelAttribute("searchVO") Survey survey, ModelMap model) throws Exception{
		List<Survey> list = surveyAnsService.surveyAnsService(survey);
		
		return new IBSheetListVO<Survey>(list, list.size());
	}

	/**
	 * 리스트에서 선택한 설문결과정보를 조회한다
	 * @param survey
	 * @param model
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/surveyAnsListPopup.do")
	public ModelAndView surveyAnsListPopup(Survey survey, ModelMap model) throws Exception{
		ModelAndView modelAndView=new ModelAndView();

		modelAndView.addObject("ansList", surveyAnsService.surveyAnsList(survey));
		modelAndView.addObject("totalCnt", surveyAnsService.surveyAnsCnt(survey));
		modelAndView.addObject("questCnt", surveyAnsService.selectSurveyQuestCnt(survey));
		modelAndView.addObject("totalQuestCnt", surveyAnsService.selectSurveyQuestCnt(survey).size());
		modelAndView.addObject("surveyFileCnt", surveyAnsService.selectSurveyFileCnt(survey));
		modelAndView.addObject("SurveyExamHead", surveyExamService.selectSurveyExamCdPop() ); //  Exam CD값 지문으로 SHEET의 HEAD셋팅
		modelAndView.addObject("head", surveyAnsService.selectSurveyAnsPopInfo(survey));//job 헤더
		modelAndView.setViewName("/admin/bbs/popup/surveyAnsListpopup");
		
		return modelAndView;
	}
	
	


	@RequestMapping("/dream/bbs/surveyAnsListPopupExport.do")
	@ResponseBody
	public IBSheetListVO<SurveyAns> surveyAnsListPopupExport(@ModelAttribute("searchVO") SurveyAns surveyAns, ModelMap model) throws Exception{
		//페이징 처리시 반드시 *IbPaging 메소드명을 사용해야함
		Map<String, Object> map = surveyAnsService.surveyAnsListPopupExport(surveyAns);
		@SuppressWarnings("unchecked")
		List<SurveyAns> result = (List<SurveyAns>) map.get("resultList");
		return new IBSheetListVO<SurveyAns>(result, Integer.parseInt((String)map.get("resultCnt")));
	}

	/**
	 * 문항별 설문 참가자들의 지문 체크 카운트 RADIO , CHECK 문항 건수 조회한다.
	 * @param survey
	 * @param mode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/surveyAnsExamCountCheck.do")
	@ResponseBody
	public IBSheetListVO<Survey> surveyAnsExamCountCheck(@ModelAttribute("searchVO")  Survey survey, ModelMap mode) throws Exception{
		List<Survey> list = surveyAnsService.selectSurveyAnsExamCountCheck(survey);
		return new IBSheetListVO<Survey>(list, list.size());
	}

	/**
	 * 문항별 설문 참가자들의 지문 체크 카운트 TEXT , CLOB 문항 건수 조회한다.
	 * @param survey
	 * @param mode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/surveyAnsExamCountText.do")
	@ResponseBody
	public IBSheetListVO<Survey> surveyAnsExamCountText(@ModelAttribute("searchVO")  Survey survey, ModelMap mode) throws Exception{
		List<Survey> list = surveyAnsService.selectSurveyAnsExamCountText(survey);
		return new IBSheetListVO<Survey>(list, list.size());
	}

	/**
	 * 문항별 설문 참가자들의 지문 체크 카운트 5지선답 또는 점수 지문 건수 조회한다.
	 * @param survey
	 * @param mode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/surveyAnsExamCount.do")
	@ResponseBody
	public IBSheetListVO<Survey> surveyAnsExamCount(@ModelAttribute("searchVO")  Survey survey, ModelMap mode) throws Exception{
		List<Survey> list = surveyAnsService.selectSurveyAnsExamCount(survey);
		return new IBSheetListVO<Survey>(list, list.size());
	}

	/**
	 * 설문에 응한 참여잘들의 직업정보 카운트
	 * @param survey
	 * @param mode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/surveyAnsJobCount.do")
	@ResponseBody
	public IBSheetListVO<Survey> surveyAnsJobCount(@ModelAttribute("searchVO")  Survey survey, ModelMap mode) throws Exception{
		List<Survey> list = surveyAnsService.selectSurveyAnsJobCount(survey);
		return new IBSheetListVO<Survey>(list, list.size());
	}

	/**
	 * 설문에 응한 참여잘들의 나이 / 성별 카운트
	 * @param survey
	 * @param mode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/surveyAnsAgeCount.do")
	@ResponseBody
	public IBSheetListVO<Survey> surveyAnsAgeCount(@ModelAttribute("searchVO")  Survey survey, ModelMap mode) throws Exception{
		List<Survey> list = surveyAnsService.selectSurveyAnsAgeCount(survey);
		return new IBSheetListVO<Survey>(list, list.size());
	}

	/**
	 * 설문에 응한 참여자들의 정보
	 * @param survey
	 * @param mode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/surveyAnsExportInfo.do")
	@ResponseBody
	public IBSheetListVO<SurveyAns> surveyAnsExportInfo(@ModelAttribute("searchVO")  SurveyAns surveyAns, ModelMap mode) throws Exception{
		List<SurveyAns> list = surveyAnsService.selectSurveyAnsExportInfo(surveyAns);
		
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
		
		
		return new IBSheetListVO<SurveyAns>(list, list.size());
	}

	/**
	 * 설문결과 팝업 항목을 조회한다.
	 * @param survey
	 * @param mode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/surveyAnsListPopupListAll.do")
	@ResponseBody
	public IBSheetListVO<LinkedHashMap<String,?>> openInfColViewListAll(Survey survey, ModelMap mode) throws Exception{
		//페이징 처리와 IbSheet 헤더 정렬은 반드시 *IbPaging 메소드명을 사용해야함(AOP 사용)
		Map<String, Object> map = surveyAnsService.surveyAnsListPopupListAllIbPaging(survey);
		@SuppressWarnings("unchecked")
		List<LinkedHashMap<String,?>> result = (List<LinkedHashMap<String,?>>) map.get("resultList");
		//int cnt = Integer.parseInt((String)map.get("resultCnt"));
		return new IBSheetListVO<LinkedHashMap<String,?>>(result, result.size());
	}

	/** 설문관리 항목 목록을 조회한다.*/
	@RequestMapping("/dream/bbs/surveyQuestListAll.do")
	@ResponseBody
	public IBSheetListVO<SurveyQuest> surveyQuestListAll(@ModelAttribute("searchVO") SurveyQuest surveyQuest, ModelMap model) throws Exception{
		List<SurveyQuest> list = surveyQuestService.selectSurveyQuestAll(surveyQuest);
		return new IBSheetListVO<SurveyQuest>(list, list.size());
	}

	/** 설문 항목추가, 수정   */
	@RequestMapping("/dream/bbs/surveyQuestAdd.do")
	@ResponseBody
	public IBSResultVO<SurveyQuest> surveyQuestAdd(@RequestBody SurveyQuests data, Locale locale , @ModelAttribute SurveyQuest surveyQuest ) throws Exception {
		ArrayList<SurveyQuest> list = data.get("data");
		int result = surveyQuestService.surveyQuestSheetCUD(list, WiseOpenConfig.STATUS_I, surveyQuest);
		return new IBSResultVO<SurveyQuest>(result, messagehelper.getSavaMessage(result));
	}


	/** 설문 항목삭제   */
	@RequestMapping("/dream/bbs/surveyQuestDelete.do")
	@ResponseBody
	public IBSResultVO<SurveyQuest> surveyQuestDelete(@RequestBody SurveyQuests data, Locale locale) throws Exception {
		ArrayList<SurveyQuest> list = data.get("data");
		int result = surveyQuestService.surveyQuestSheetCUD(list, WiseOpenConfig.STATUS_D, null);
		return new IBSResultVO<SurveyQuest>(result, messagehelper.getDeleteMessage(result));
	}

	/** 설문관리 지문목록을 조회한다.*/
	@RequestMapping("/dream/bbs/surveyExamListAll.do")
	@ResponseBody
	public IBSheetListVO<SurveyExam> surveyExamListAll(@ModelAttribute("searchVO") SurveyExam surveyExam, ModelMap model) throws Exception{
		List<SurveyExam> list = surveyExamService.selectSurveyExamAll(surveyExam);
		return new IBSheetListVO<SurveyExam>(list, list.size());
	}

	/** 지문추가   */
	@RequestMapping("/dream/bbs/surveyExamAdd.do")
	@ResponseBody
	public IBSResultVO<SurveyExam> surveyExamAdd(@RequestBody SurveyExams data, Locale locale , @ModelAttribute("searchVO") SurveyExam surveyExam ) throws Exception {
		ArrayList<SurveyExam> list = data.get("data");
		int result = surveyExamService.surveyExamSheetCUD(list, WiseOpenConfig.STATUS_I, surveyExam);
		return new IBSResultVO<SurveyExam>(result, messagehelper.getSavaMessage(result));
	}

	/** 지문삭제   */
	@RequestMapping("/dream/bbs/surveyExamDelete.do")
	@ResponseBody 
	public IBSResultVO<SurveyExam> surveyExamDelete(@RequestBody SurveyExams data, Locale locale ) throws Exception {
		ArrayList<SurveyExam> list = data.get("data");
		int result = surveyExamService.surveyExamSheetCUD(list, WiseOpenConfig.STATUS_D, null);
		return new IBSResultVO<SurveyExam>(result, messagehelper.getDeleteMessage(result));
	}

	@RequestMapping("/dream/bbs/surveyExcelDown.do")
	public ModelAndView surveyExcelDown(@ModelAttribute("command") Survey survey) throws Exception{
		ModelAndView mnv = new ModelAndView();
		Map<String, Object> map = surveyAnsService.selectSurveyAnsResult(survey);

		mnv.addObject("surveyResult", map.get("result"));
		mnv.addObject("surveyCnt", map.get("cnt"));
		mnv.setViewName("/admin/bbs/popup/surveyExcelDown");
		return mnv;
	}
	
	
/**
	 * 파일 리스트
	 */
	@RequestMapping("/dream/bbs/surveyFileList.do")
	@ResponseBody
	public IBSheetListVO<Survey> surveyFileList(@ModelAttribute("searchVO")  Survey survey) throws Exception{
		List<Survey> list = surveyAnsService.surveyFileList(survey);
		return new IBSheetListVO<Survey>(list, list.size());
	}
	
	/**
	 * 파일 단건 다운로드
	 */
	@RequestMapping("/dream/bbs/surveyFileDownLoad.do")
	public void surveyFileDownLoad(@ModelAttribute("searchVO")  Survey survey, HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		
		String fileName = survey.getSaveFileNm();
		
		fileName = URLDecoder.decode(fileName,"UTF-8");
		
		
		String filePath = EgovProperties.getProperty("Globals.SurveyFilePath") + survey.getSurveyId() + "/" ;
			
		String path = filePath + fileName;
		
		File file = new File(path);
		
		
		String userAgent = req.getHeader("User-Agent");
		boolean ie = userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("rv:11") > -1;
		
		String srcFileName = null;
		
		if(ie){
//			srcFileName = URLEncoder.encode(survey.getSrcFileNm(),"utf-8");
			//이미 한글은 인코딩되어 오므로 그냥 넣어주면 됨 ( 익스 )
			srcFileName = survey.getSrcFileNm();
//			srcFileName = URLDecoder.decode(survey.getSrcFileNm(),"utf-8");
		}else{
			srcFileName = new String(survey.getSrcFileNm().getBytes("utf-8"),"iso-8859-1");
		}
		
		srcFileName = srcFileName.replaceAll("%0d", "").replaceAll("%0a", "").replaceAll("\r", "").replaceAll("\n", ""); //시큐어코딩 조치    
		
		res.setContentType("application/octet-stream");
		res.setHeader("Content-Disposition","attachment;filename=\""+srcFileName+"\";");
		
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		ServletOutputStream so = null;
		BufferedOutputStream bos = null;
		
		try{
		
		fis = new FileInputStream(file);
		bis = new BufferedInputStream(fis);
		so = res.getOutputStream();
		bos = new BufferedOutputStream(so);
		
		byte[] data = new byte[2048];
		int input=0;
		while((input=bis.read(data))!=-1){
			bos.write(data,0,input);
			bos.flush();
		}
		
		}catch(FileNotFoundException e){ //시큐어코딩 조치 - Exception to String
			logger.debug(ExceptionUtils.getStackTrace(e)); 
			/*
			res.setContentType("text/html; charset=UTF-8");
			PrintWriter out = res.getWriter();
			out.println("<script>alert('파일을 찾을 수 없습니다.')</script>");
			out.flush();
			*/
			/*
			PrintWriter writer = res.getWriter();
			writer.println("<script>alert('파일을 찾을 수 없습니다.')</script>");
			*/
		}finally{
		
			if(bos!=null)bos.close();
			if(bis!=null)bis.close();
			if(so!=null)so.close();
			if(fis!=null)fis.close();
		
		}
	}
	
/**
	 * 전체 답변 목록
	 */
	@RequestMapping("/dream/bbs/surveyTotalAnsSearchList.do")
	@ResponseBody
	public IBSheetListVO<SurveyAns> surveyTotalAnsSearchList(@ModelAttribute("searchVO")  Survey survey) throws Exception{
		List<SurveyAns> list = surveyAnsService.surveyTotalAnsSearchList(survey);
		return new IBSheetListVO<SurveyAns>(list, list.size());
	}
	
	//개인정보 삭제
	@RequestMapping("/dream/bbs/deletePersonerInfo.do")
	@ResponseBody
	public Map<String,String> deletePersonerInfo(@ModelAttribute("searchVO")  Survey survey) throws Exception{
		Map<String,String> map = surveyAnsService.deletePersonerInfo(survey);
		return map;
	}
	
	
}
