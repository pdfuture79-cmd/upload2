package egovframework.admin.basicinf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import egovframework.admin.basicinf.service.CommCode;
import egovframework.admin.basicinf.service.CommMenu;
import egovframework.admin.basicinf.service.CommMenuAcc;
import egovframework.admin.basicinf.service.CommMenuAccService;
import egovframework.admin.basicinf.service.CommMenuService;
import egovframework.admin.basicinf.service.CommUsr;
import egovframework.admin.openinf.service.OpenDs;
import egovframework.admin.openinf.service.OpenDscol;
import egovframework.admin.service.service.OpenInfSrv;
import egovframework.common.WiseOpenConfig;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;



@Service("CommMenuAccService")
public class CommMenuAccServiceImpl extends AbstractServiceImpl implements CommMenuAccService {

    @Resource(name = "CommMenuAccDAO")
    private CommMenuAccDAO commMenuAccDAO;

    private static final Logger logger = Logger.getLogger(CommMenuAccServiceImpl.class.getClass());
	
    
    /**
     * 리스트 조회
     */
	@Override
	public Map<String, Object> selectMenuListIbPaging(CommMenuAcc commMenuAcc)
			throws Exception {
		List<CommMenuAcc> result = commMenuAccDAO.selectMenuList(commMenuAcc);
		int cnt = commMenuAccDAO.selectMenuListCnt(commMenuAcc);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultList", result);
		map.put("resultCnt", Integer.toString(cnt));
		return map;
	}

	/**
	 * 데이터 수정
	 */
	@Override
	public int updateCommMenuAccCUD(ArrayList<CommMenuAcc> list) throws Exception {
		int result = 0;
		
		for (CommMenuAcc saveVO : list) {
			result = saveCommMenuAccCUD(saveVO);
		}
		
		return result;
	}
    
	// 데이터 수정
	public int saveCommMenuAccCUD(CommMenuAcc saveVO) throws Exception {
		int result = 0;
		
		if(saveVO.getStatus().equals("I")){
			//result += commMenuAccDAO.insertDsCol(saveVO);
		}else if(saveVO.getStatus().equals("U")){
			result += commMenuAccDAO.updateCommMenuAcc(saveVO);
		}else if(saveVO.getStatus().equals("D")){
			//result += commMenuAccDAO.deleteCommMenuAcc(saveVO);
		}else{
		}
		
		return result;
		
	}
	
    

    
    
    

}
