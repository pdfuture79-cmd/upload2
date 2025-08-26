package egovframework.admin.basicinf.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import egovframework.admin.basicinf.service.CommUsr;
import egovframework.admin.basicinf.service.CommUsrAdmin;
import egovframework.admin.basicinf.service.CommUsrAdminService;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.common.WiseOpenConfig;
import egovframework.common.util.UtilEncryption;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

/**
 * 담당자관리 ServiceImpl
 * @author KJH
 * @since 2014.07.23
 */
 
@Service("CommUsrAdminService")
public class CommUsrAdminServiceImpl extends AbstractServiceImpl implements CommUsrAdminService {

    @Resource(name = "CommUsrAdminDAO") 
    private CommUsrAdminDAO commUsrAdminDAO;
    

    private static final Logger logger = Logger.getLogger(CommUsrAdminServiceImpl.class.getClass());
	
    /**
     * 사용자 정보를 체크한다.
     */
    public CommUsrAdmin selectCommUsrAdminCheck(CommUsrAdmin commUsrAdmin) throws Exception {
		return  commUsrAdminDAO.selectCommUsrAdminCheck(commUsrAdmin);
    }
    
    /**
     * 사용자 전체조회
     */
    public Map<String, Object> selectCommUsrAdminAllIbPaging(CommUsrAdmin commUsrAdmin) throws Exception {
    	
    	CommUsr loginVO = null; 
    	
    	//사용자 인증 및 사용자 권한정보 및 사용자코드 Set
        if(EgovUserDetailsHelper.isAuthenticated()) { 
    		EgovUserDetailsHelper.getAuthenticatedUser();
    		loginVO = (CommUsr)EgovUserDetailsHelper.getAuthenticatedUser();
    		
    		commUsrAdmin.setAuthAccCd(loginVO.getAccCd()); //관리자권한코드
    		commUsrAdmin.setAuthOrgCd(loginVO.getOrgCd()); //조직코드      		    		  
        }
    	
    	List<CommUsrAdmin> result = commUsrAdminDAO.selectCommUsrAdminListAll(commUsrAdmin);
    	
    	
    	for(CommUsrAdmin usr : result) {
    		UtilEncryption ue = new UtilEncryption();
    		usr.setUsrTel(ue.decrypt(usr.getUsrTel()));
    	}
		int cnt = commUsrAdminDAO.selectCommUsrAdminListAllCnt(commUsrAdmin); 
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultList", result);
		map.put("resultCnt", Integer.toString(cnt));
//		map.put("resultCnt", String.valueOf(result.size()));
		
		return map;
    }
    
    /**
     * 사용자 팝업 조회
     */
    @Override
    public Map<String, Object> selectCommUsrAdminPopIbPaging(CommUsrAdmin commUsrAdmin) throws Exception {
    	System.out.println("********************************************************");
    	System.out.println(commUsrAdmin.getOrgCd());
    	System.out.println(commUsrAdmin.getUsrGb());
    	System.out.println(commUsrAdmin.getAccCd());
    	System.out.println("********************************************************");
    	List<CommUsrAdmin> result = commUsrAdminDAO.selectCommUsrAdminPopList(commUsrAdmin);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultList", result);
		return map;
    }
    
    /**
     * 사용자 팝업 조회(직책추가)
     */
    @Override
    public Map<String, Object> selectCommUsrAdminPosPopIbPaging(CommUsrAdmin commUsrAdmin) throws Exception {
    	List<CommUsrAdmin> result = commUsrAdminDAO.selectCommUsrAdminPosPopList(commUsrAdmin);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultList", result);
		return map;
    }
    
    /**
     * 직원 단건조회
     */
    public CommUsrAdmin selectCommUsrAdminDtlInfo(CommUsrAdmin commUsrAdmin) throws Exception {
    	System.out.println("commUsrAdminDtTablInfo ServiceImpl");
    	CommUsrAdmin result = commUsrAdminDAO.selectCommUsrAdminDtlInfo(commUsrAdmin);
   		UtilEncryption ue = new UtilEncryption();
   		result.setUsrTel(ue.decrypt(result.getUsrTel()));
   		result.setUsrHp(ue.decrypt(result.getUsrHp()));
    	return result;  
    } 
    
    /**
     * 담당자정보 CUD
     */
    @Override
    public int saveCommUsrAdminCUD(CommUsrAdmin commUsrAdmin, String status) throws Exception {
    	int result = 0;
    	UtilEncryption ue = new UtilEncryption();
    	commUsrAdmin.setUsrPw(ue.encryptSha256(commUsrAdmin.getUsrPw(), commUsrAdmin.getUsrPw().getBytes()));
    	    	
    	if( WiseOpenConfig.STATUS_I.equals(status) ) {
    		result = commUsrAdminDAO.save(commUsrAdmin);
    	}else if( WiseOpenConfig.STATUS_U.equals(status) ) {
    	    result = commUsrAdminDAO.update(commUsrAdmin);
    	}else if( WiseOpenConfig.STATUS_D.equals(status) ){
    		result = commUsrAdminDAO.delete(commUsrAdmin);
    	}
    	return result;
    }
    
    /**
     * 담당자 승인
     */
    @Override
    public int commUsrAdminApprCUD(CommUsrAdmin commUsrAdmin, String status) throws Exception {
    	int result = 0;
    	if ( status.equals("A") ) {
    		
    		//해당기관의 변경대상 아닌 사람은 데이터등록자로 업데이트
			result = commUsrAdminDAO.updateNotOrgCmUsr(commUsrAdmin);     		
    		
    		result = commUsrAdminDAO.approval(commUsrAdmin);
    		
    	} else if ( status.equals("C") ) {
    		result = commUsrAdminDAO.approvalCancel(commUsrAdmin);
    	}
    	return result;
    }

    /**
     * 담당자ID 중복체크
     */
	@Override
	public int commUsrAdminUsrIdDup(CommUsrAdmin commUsrAdmin) throws Exception {
		int result=0;
		if (commUsrAdminDAO.usrIdDup(commUsrAdmin) > 0) {
			return -1;
		}
		return result;
	}

	/**
	 * 비밀번호 초기화
	 */
	@Override
	public CommUsrAdmin commUsrAdminInitialPw(CommUsrAdmin commUsrAdmin) throws Exception {
		String randomPw = commUsrAdminDAO.randomPw();
		commUsrAdmin.setInitialPwResult(0);

		if ( !randomPw.equals("") ) {
			UtilEncryption ue = new UtilEncryption();
			commUsrAdmin.setInitialPw(ue.encryptSha256(randomPw, randomPw.getBytes()));
			if ( commUsrAdminDAO.initialPw(commUsrAdmin) > 0 ) {
				commUsrAdmin.setInitialPwResult(-1);
			}
			commUsrAdmin.setInitialPw(randomPw);
		}
		
		return commUsrAdmin;
	}


    
}
