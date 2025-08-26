package egovframework.admin.basicinf.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.admin.basicinf.service.CommCode;
import egovframework.admin.basicinf.service.CommMenu;
import egovframework.admin.basicinf.service.CommOrg;
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
@Repository("CommMenuDAO")
public class CommMenuDAO extends EgovComAbstractDAO {

    /**
     * 관리자 상단 메뉴를 조회 한다.
     * 
     * @param CommMenu
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<CommMenu> selectCommMenuList(CommUsr commUsr) throws Exception {
    	return list("CommMenuDAO.selectCommMenuList", commUsr);
    }

    /**
     * 전체 리스트 조회
     * @param commMenu
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<CommMenu> selectMenuList(CommMenu commMenu) throws Exception {
    	return list("CommMenuDAO.selectMenuList", commMenu);
    }
    
    /**
     * 전체 리스트 조회 건수
     * @param commMenu
     * @return
     * @throws Exception
     */
    public int selectMenuListCnt(CommMenu commMenu) throws Exception {
	return (Integer)getSqlMapClientTemplate().queryForObject("CommMenuDAO.selectMenuListCnt", commMenu);
    }
    
    /**
     * 키워드 조회
     * @param commMenu
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public List<CommMenu> selectMenuListKeywd(CommMenu commMenu) throws Exception {
    	return list("CommMenuDAO.selectMenuListKeywd", commMenu);
    }
    
    /**
     * 키워드 조회 건수
     * @param commMenu
     * @return
     * @throws Exception
     */
    public int selectMenuListKeywdCnt(CommMenu commMenu) throws Exception {
	return (Integer)getSqlMapClientTemplate().queryForObject("CommMenuDAO.selectMenuListKeywdCnt", commMenu);
    }
    
    /**
     * 단건 상세 조회
     * @param commMenu
     * @return
     * @throws Exception
     */
	public CommMenu selectMenuListInfo(CommMenu commMenu) throws Exception {
		return (CommMenu)selectByPk("CommMenuDAO.selectMenuListInfo", commMenu);
	}
	
	/**
	 * 메뉴 등록
	 * @param saveVO
	 * @return
	 * @throws Exception
	 */
	public int insertMenu(CommMenu saveVO) throws Exception {
		return (Integer)update("CommMenuDAO.insertMenu", saveVO);
	}
	
	/**
	 * 메뉴 수정
	 * @param saveVO
	 * @return
	 * @throws Exception
	 */
	public int updateMenu(CommMenu saveVO) throws Exception {
		return (Integer)update("CommMenuDAO.updateMenu", saveVO);
	}
	
	/**
	 * 메뉴 삭제
	 * @param saveVO
	 * @return
	 * @throws Exception
	 */
	public int deleteMenu(CommMenu saveVO) throws Exception {
		return (Integer)update("CommMenuDAO.deleteMenu", saveVO);
	}
	   
	/**
	 * 메뉴아이디 조회(최대값+1)
	 * @param saveVO
	 * @return
	 * @throws Exception
	 */
	public int getMenuId(CommMenu saveVO) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("CommMenuDAO.getMenuId", saveVO);
	}
	
	/**
	 * 하위 메뉴 조회
	 * @param commMenu
	 * @return
	 * @throws Exception
	 */
	public List selectCommLowMenuList(CommMenu commMenu) throws Exception {
		return list("CommMenuDAO.selectCommLowMenuList", commMenu);
	}
	
	/**
	 * 하위 메뉴 조회 건수
	 * @param commMenu
	 * @return
	 * @throws Exception
	 */
	public int selectCommLowMenuListCnt(CommMenu commMenu) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("CommMenuDAO.selectCommLowMenuListCnt", commMenu);
	}
	
	/**
	 * 메뉴정보 메뉴순서 수정
	 * @param commMenu
	 * @return
	 * @throws Exception
	 */
	public int commMenuListUpdateTreeOrder(CommMenu commMenu) throws Exception {
		return (Integer)update("CommMenuDAO.commMenuListUpdateTreeOrder", commMenu);
	}
   
}

