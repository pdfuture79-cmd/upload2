package egovframework.admin.basicinf.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import egovframework.admin.basicinf.service.CommUsr;
import egovframework.admin.basicinf.service.CommUsrService;
import egovframework.common.WiseOpenConfig;
import egovframework.common.util.UtilEncryption;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

/**
 * 메뉴를 관리를 위한 서비스 구현 클래스
 * @author wiseopen
 * @since 2014.04.17
 * @version 1.0
 * @see
 *
 */

@Service("CommUsrService")
public class CommUsrServiceImpl extends AbstractServiceImpl implements CommUsrService {

    @Resource(name = "CommUsrDAO")
    private CommUsrDAO CommUsrDAO;
    

    private static final Logger logger = Logger.getLogger(CommUsrServiceImpl.class.getClass());
	

    public CommUsr selectCommUsrCheck(CommUsr commUsr) throws Exception {
    	UtilEncryption ue = new UtilEncryption();
    	commUsr.setUsrPw(ue.encryptSha256(commUsr.getUsrPw(), commUsr.getUsrPw().getBytes()));
    	return  CommUsrDAO.selectCommUsrCheck(commUsr);
    }
    
    /**
     * 사용자 정보 전체조회
     */
    public Map<String, Object> selectCommUsrAllIbPaging(CommUsr commUsr) throws Exception {
    	List<CommUsr> result = CommUsrDAO.selectCommUsrListAll(commUsr);
    	for(CommUsr usr : result) {
    		UtilEncryption ue = new UtilEncryption();
    		usr.setUsrTel(ue.decrypt(usr.getUsrTel()));
    	}
    	
		int cnt = CommUsrDAO.selectCommUsrListAllCnt(commUsr);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultList", result);
		map.put("resultCnt", Integer.toString(cnt));
		
		return map;
    }
    
    /**
     * 사용자 정보 단건 조회
     */
    /*public CommUsr selectCommUsr(CommUsr commUsr) throws Exception {
		return  CommUsrDAO.selectCommUsrList(commUsr);
    }*/
    
    /**
     * 사용자 정보 단건 조회
     */
    public List<CommUsr> selectCommUsr(CommUsr commUsr) throws Exception {
    	List<CommUsr> result = CommUsrDAO.selectCommUsrList(commUsr);
    	UtilEncryption ue = new UtilEncryption();
    	String usrTel = ue.decrypt(result.get(0).getUsrTel());
    	String usrHp = ue.decrypt(result.get(0).getFirUsrHp());

    	String[] usrTel_sp = usrTel.split("-");
    	String[] usrHp_sp = usrHp.split("-");
    	
    	if(usrTel_sp != null) {
    		if(usrTel_sp.length == 3) {
    			result.get(0).setFirUsrTel(usrTel_sp[0]);
    			result.get(0).setMidUsrTel(usrTel_sp[1]);
    			result.get(0).setLastUsrTel(usrTel_sp[2]);
    		} else if(usrTel_sp.length == 2) {
    			result.get(0).setFirUsrTel(usrTel_sp[0]);
    			result.get(0).setMidUsrTel(usrTel_sp[1]);
    		} else if(usrTel_sp.length == 1) {
    			result.get(0).setFirUsrTel(usrTel_sp[0]);
    		}
    	}
    	
    	if(usrHp_sp != null) {
    		if(usrHp_sp.length == 3) {
    			result.get(0).setFirUsrHp(usrHp_sp[0]);
    			result.get(0).setMidUsrHp(usrHp_sp[1]);
    			result.get(0).setLastUsrHp(usrHp_sp[2]);
    		} else if(usrHp_sp.length == 2) {
    			result.get(0).setFirUsrHp(usrHp_sp[0]);
    			result.get(0).setMidUsrHp(usrHp_sp[1]);
    		} else if(usrHp_sp.length == 1) {
    			result.get(0).setFirUsrHp(usrHp_sp[0]);
    		}
    	}
    	
		return result;
    }
    
    /**
     * 사용자 정보 입력/수정/삭제
     */
    public int saveCommUsrCUD(CommUsr commUsr, String status) throws Exception {
    	int result = 0;
    	
    	UtilEncryption ue = new UtilEncryption();
    	commUsr.setUsrTel(ue.encrypt(commUsr.getFirUsrTel() + "-" + commUsr.getMidUsrTel() + "-" + commUsr.getLastUsrTel()));
    	commUsr.setFirUsrHp(ue.encrypt(commUsr.getFirUsrHp() + "-" + commUsr.getMidUsrHp() + "-" + commUsr.getLastUsrHp()));
    	
    	if(WiseOpenConfig.STATUS_I.equals(status)){
    		//commUsr.setUsrCd(1);
        	result = CommUsrDAO.insert(commUsr);
    	}else if((WiseOpenConfig.STATUS_U.equals(status))){
        	result = CommUsrDAO.update(commUsr);
    	}else if((WiseOpenConfig.STATUS_D.equals(status))){
        	result = CommUsrDAO.delete(commUsr);
    	}else{
    		
    		result = WiseOpenConfig.STATUS_ERR;
    	}
		return result;
    }

    /**
     * 사용자 로그인 실패 횟수 업데이트(성공이면 0, 실패면 +1)
     */
	@Override
	public int saveCommUsrFailCnt(CommUsr commUsr) throws Exception {
		return CommUsrDAO.saveCommUsrFailCnt(commUsr);
	}

	/**
	 * 아이디 체크(회원 존재여부)
	 */
	@Override
	public int selectCommUsrIdChk(CommUsr commUsr) throws Exception {
		return CommUsrDAO.selectCommUsrIdChk(commUsr);
	}

	/**
	 * 로그인 실패 횟수 조회 
	 */
	@Override
	public int selectCommUsrFailCnt(CommUsr commUsr) throws Exception {
		return CommUsrDAO.selectCommUsrFailCnt(commUsr);
	}

	/**
	 * 패스워드 변경기간 조회(60일 이후이면 true 반환), 패스워드 변경요청도 확인
	 */
	@Override
	public boolean selectCommUsrChangePwDttm(CommUsr commUsr) throws Exception {
		List<HashMap<String, Object>> map = CommUsrDAO.selectCommUsrChangePwDttm(commUsr);
		int pwDttm= Integer.parseInt(String.valueOf(map.get(0).get("pwDttm")));		//패스워드 변경 경과시간(기준정보 파일에 저장)
		String reqPwYn= (String) map.get(0).get("reqPwYn");							//패스워드 변경 요청
		if ( pwDttm > WiseOpenConfig.PWD_CHANGE_PERIOD | reqPwYn.equals("Y") )
			return true;
		else
			return false;
	}

	/**
	 * 패스워드 변경(패스워드 변경 기간초과에 따른)
	 */
	@Override
	public int saveCommUsrChangePw(CommUsr commUsr) throws Exception {
		return CommUsrDAO.saveCommUsrChangePw(commUsr);
	}

	/**
	 * PKI 인증서 등록
	 */
	@Override
	public int savePkiReg(CommUsr commusr) throws Exception {
		return CommUsrDAO.savePkiReg(commusr);
	}
	
	/**
	 * PKI 인증서 등록체크
	 */
	public CommUsr selectCommUsrPkiCheck(CommUsr commUsr) throws Exception {
		return  CommUsrDAO.selectCommUsrPkiCheck(commUsr);
    }

	
	/**
	 * Q&A 답변 요청 건수
	 */
	@Override
	public int selectQNACnt() throws Exception {
		return CommUsrDAO.selectQNACnt();
	}

	/**
	 * 활용사례 등록 요청 건수
	 */
	@Override
	public int selectGalleryCnt() throws Exception {
		return CommUsrDAO.selectGalleryCnt();
	}

	/**
	 * 관리자의 로그인 접속이력을 기록한다.
	 */
	@Override
	public int insertLogCommUsr(CommUsr commUsr) throws Exception {
		return CommUsrDAO.insertLogCommUsr(commUsr);
	}

	/**
	 * 사용자 정보를 조회한다.
	 */
	@Override
	public int selectCommUsrInfo(CommUsr commUsr) throws Exception {
		return CommUsrDAO.selectCommUsrInfo(commUsr);
	}

	/**
	 * AccCd 권한 체크하여 sys,admin 인 경우만 업무처리정보 볼수있다.
	 */
	@Override
	public int selectAccCdCheck(CommUsr commUsr) throws Exception {
		return CommUsrDAO.selectAccCdCheck(commUsr);
	}

	/**
	 * 이미 등록된 pki라면 등록 못하도록 한다.
	 */
	@Override
	public int selectDupPki(CommUsr commUsr) throws Exception {
		return CommUsrDAO.selectDupPki(commUsr);
	}

	/**
	 * 비밀번호 변경시 기존의 비밀번호로 변경못하도록 체크한다.
	 */
	@Override
	public int selectUserPwCheck(CommUsr commUsr) throws Exception {
		return CommUsrDAO.selectUserPwCheck(commUsr);
	}

	/**
	 * GPIN 관리자 중복체크
	 */
	@Override
	public int gpinAdminDupCheck(CommUsr commUsr) throws Exception {
		return CommUsrDAO.gpinAdminDupCheck(commUsr);
	}

	@Override
	public int commUsrIdDup(CommUsr commUsr, String status) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
    
    public CommUsr selectSysCommUsrCheck(CommUsr commUsr) throws Exception {
    	return  CommUsrDAO.selectSysCommUsrCheck(commUsr);
    }
    
}
