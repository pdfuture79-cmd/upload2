package egovframework.admin.basicinf.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.admin.basicinf.service.CommCode;
import egovframework.admin.basicinf.service.CommMenu;
import egovframework.admin.basicinf.service.CommMenuAcc;
import egovframework.admin.basicinf.service.CommUsr;
import egovframework.admin.openinf.service.OpenDs;
import egovframework.admin.openinf.service.OpenDscol;
import egovframework.admin.openinf.service.OpenDtbl;
import egovframework.admin.service.service.OpenInfSrv;
import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

/**
 * 메뉴 관리를 위한 데이터 접근 클래스
 * @author wiseopen
 * @since 2014.04.17
 * @version 1.0
 * @see
 *
 */
@Repository("CommMenuAccDAO")
public class CommMenuAccDAO extends EgovComAbstractDAO {

    /**
     * 전체 리스트 조회
     * @param commMenu
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<CommMenuAcc> selectMenuList(CommMenuAcc commMenuAcc) throws Exception {
    	return list("CommMenuAccDAO.selectMenuList", commMenuAcc);
    }
    
    /**
     * 전체 리스트 조회 건수
     * @param commMenu
     * @return
     * @throws Exception
     */
    public int selectMenuListCnt(CommMenuAcc commMenuAcc) throws Exception {
    	return (Integer)getSqlMapClientTemplate().queryForObject("CommMenuAccDAO.selectMenuListCnt", commMenuAcc);
    }
    
    /**
     * 메뉴권한을 저장한다
     * @param saveVO
     * @return
     * @throws Exception
     */
    public int updateCommMenuAcc(CommMenuAcc saveVO) throws Exception {
    	return (Integer)update("CommMenuAccDAO.updateCommMenuAcc", saveVO);
    }
   
}

