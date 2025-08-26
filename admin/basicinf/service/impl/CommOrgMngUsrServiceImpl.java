package egovframework.admin.basicinf.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.admin.basicinf.service.CommOrgMngUsrService;
import egovframework.admin.basicinf.service.CommUsrAdmin;
import egovframework.common.util.UtilEncryption;

@Service("CommOrgMngUsrService")
public class CommOrgMngUsrServiceImpl implements CommOrgMngUsrService{
    
	@Resource(name = "CommUsrAdminDAO") 
    private CommUsrAdminDAO commUsrAdminDAO;	 
	
	@Override 
	public List<CommUsrAdmin> selectOrgMngUsrList(CommUsrAdmin srchVo) throws Exception {
		
		List<CommUsrAdmin> result = commUsrAdminDAO.selectOrgMngUsrList(srchVo);
		
		//========전화번호 복호화==========
		for(CommUsrAdmin tmpVo : result){
			
			UtilEncryption ue = new UtilEncryption();  
    		tmpVo.setUsrTel(ue.decrypt(tmpVo.getUsrTel()));  
		}	
		//============================
				
		return result;
	}

	@Override
	public int saveOpenDtfileOrgRelCUD(ArrayList<CommUsrAdmin> list) {
		
		int result = 0;
		
		for(CommUsrAdmin saveVo : list) {
			
			//해당기관의 변경대상 아닌 사람은 데이터등록자로 업데이트
			result += commUsrAdminDAO.updateNotOrgCmUsr(saveVo);
			
			//관리자로 업데이트
			result += commUsrAdminDAO.updateMngOrgUser(saveVo); 
		}
		
		return result;
	}

	
}
