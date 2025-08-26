package egovframework.admin.basicinf.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.admin.basicinf.service.CommCode;
import egovframework.admin.openinf.service.OpenInf;
import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import egovframework.admin.basicinf.service.CommUsr;

/**
 * 사용자 관리를 위한 데이터 접근 클래스
 * @author wiseopen
 * @since 2014.04.17
 * @version 1.0
 * @see
 *
 */
@Repository("CommUsrDAO")
public class CommUsrDAO extends EgovComAbstractDAO {

    /**
     * 사용자 정보를 단건 조회 한다.
     * 
     * @param CommUsr
     * @return
     * @throws Exception
     */
    public CommUsr selectCommUsrCheck(CommUsr commUsr) throws Exception {
    	return (CommUsr)selectByPk("CommUsrDAO.selectCommUsrCheck", commUsr);
    }
    
    /**
     * 사용자 정보를 전체 조회 한다.
     * 
     * @param CommUsr
     * @return
     * @throws Exception
     */
    
    @SuppressWarnings("unchecked")
	public List<CommUsr> selectCommUsrListAll(CommUsr commUsr) throws Exception {
    	return list("CommUsrDAO.selectCommUsrListAll", commUsr);
    }
    
    /**
     * 사용자 정보를 전체 건수를 조회  한다.
     * 
     * @param CommUsr
     * @return
     * @throws Exception
     */
    
	public int selectCommUsrListAllCnt(CommUsr commUsr) throws Exception {
    	return (Integer)getSqlMapClientTemplate().queryForObject("CommUsrDAO.selectCommUsrListAllCnt", commUsr);
    }
	
	/**
     * 사용자 정보를 단건 조회 한다.
     * 
     * @param CommUsr
     * @return
     * @throws Exception
     */
    /*public List<CommUsr> selectCommUsrList(CommUsr commUsr) throws Exception {
    	return (CommUsr)selectByPk("CommUsrDAO.selectCommUsrList", commUsr);
    }*/
    
    @SuppressWarnings("unchecked")
	public List<CommUsr> selectCommUsrList(CommUsr commUsr) throws Exception {
    	return  list("CommUsrDAO.selectCommUsrList", commUsr);
   }
    
    /**
     *사용자 정보를 저장한다.
     * @param commUsr
     * @return
     * @throws Exception
     */
    public int insert(CommUsr commUsr) throws Exception {
	return (Integer)update("CommUsrDAO.insert", commUsr);
    }
    
    /**
     *등록된 사용자 속성정보를 수정한다.
     * @param commUs
     * @return
     * @throws Exception
     */
    public int update(CommUsr commUs) throws Exception {
    	return (Integer)update("CommUsrDAO.update", commUs);
    }
    
    /**
     *등록된 사용자 속성정보를 삭제한다.
     * @param commUs
     * @return
     * @throws Exception
     */
    public int delete(CommUsr commUs) throws Exception {
    	return (Integer)update("CommUsrDAO.delete", commUs);
    }
	
    /**
     * 사용자 로그인 실패 횟수 업데이트(성공이면 0, 실패면 +1)
     * @param commUsr
     * @return
     * @throws Exception
     */
    public int saveCommUsrFailCnt(CommUsr commUsr) throws Exception {
		return (Integer)update("CommUsrDAO.saveCommUsrFailCnt", commUsr);
	}
    
    /**
     * 아이디 체크(회원 존재여부)
     * @param commUsr
     * @return
     * @throws Exception
     */
    public int selectCommUsrIdChk(CommUsr commUsr) throws Exception {
    	return (Integer)getSqlMapClientTemplate().queryForObject("CommUsrDAO.selectCommUsrIdChk", commUsr);
    }
    
    /**
     * 로그인 실패 횟수 조회 
     * @param commUsr
     * @return
     * @throws Exception
     */
    public int selectCommUsrFailCnt(CommUsr commUsr) throws Exception {
    	return (Integer)getSqlMapClientTemplate().queryForObject("CommUsrDAO.selectCommUsrFailCnt", commUsr);
    }
    
    /**
     * 패스워드 변경기간 조회(60일 이후이면 true 반환)
     * @param commUsr
     * @return
     * @throws Exception
     */
    public List<HashMap<String, Object>> selectCommUsrChangePwDttm(CommUsr commUsr) throws Exception {
		 return getSqlMapClientTemplate().queryForList("CommUsrDAO.selectCommUsrChangePwDttm", commUsr);
	}
    
    /**
     * 패스워드 변경(패스워드 변경 기간초과에 따른)
     * @param commUsr
     * @return
     * @throws Exception
     */
    public int saveCommUsrChangePw(CommUsr commUsr) throws Exception {
    	return (Integer)update("CommUsrDAO.saveCommUsrChangePw", commUsr);
    }
    
    /**
     * PKI 인증서 등록
     * @param commUsr
     * @return
     * @throws Exception
     */
    public int savePkiReg(CommUsr commUsr) throws Exception {
    	return (Integer)update("CommUsrDAO.savePkiReg", commUsr);
    }
    
    /**
     * PKI 인증서 등록체크
     * @param commUsr
     * @return
     * @throws Exception
     */
    public CommUsr selectCommUsrPkiCheck(CommUsr commUsr) throws Exception {
    	return (CommUsr)selectByPk("CommUsrDAO.selectCommUsrPkiCheck", commUsr);
    }

    /**
     * Q&A답변 요청 건수
     * @return
     * @throws Exception
     */
	public int selectQNACnt() throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("CommUsrDAO.selectQNACnt");
	}

	/**
	 * 활용사례 등록 요청 건수
	 * @return
	 * @throws Exception
	 */
	public int selectGalleryCnt() throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("CommUsrDAO.selectGalleryCnt");
	}

	/**
	 * 관리자의 로그인 접속이력을 기록한다.
	 * @param commUsr
	 * @return
	 * @throws Exception
	 */
	public int insertLogCommUsr(CommUsr commUsr) throws Exception{
		return (Integer)insert("CommUsrDAO.insertLogCommUsr", commUsr); 
	}

	/**
	 * 사용자 정보를 조회한다.
	 * @param commUsr
	 * @return
	 * @throws Exception
	 */
	public int selectCommUsrInfo(CommUsr commUsr) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("CommUsrDAO.selectCommUsrInfo", commUsr);
	}

	/**
	 * AccCd 권한 체크하여 sys,admin 인 경우만 업무처리정보 볼수있다.
	 * @param commUsr
	 * @return
	 * @throws Exception
	 */
	public int selectAccCdCheck(CommUsr commUsr) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("CommUsrDAO.selectAccCdCheck",commUsr);
	}

	/**
	 * 이미 등록된 pki라면 등록 못하도록 한다.
	 * @param commUsr
	 * @return
	 * @throws Exception
	 */
	public int selectDupPki(CommUsr commUsr) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("CommUsrDAO.selectDupPki",commUsr);
	}

	/**
	 * 비밀번호 변경시 기존의 비밀번호로 변경못하도록 체크한다.
	 * @param commUsr
	 * @return
	 * @throws Exception
	 */
	public int selectUserPwCheck(CommUsr commUsr) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("CommUsrDAO.selectUserPwCheck",commUsr);
	}
	
	/**
	 * GPIN 관리자 중복체크
	 * @param commUsr
	 * @return
	 * @throws Exception
	 */
	public int gpinAdminDupCheck(CommUsr commUsr) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("CommUsrDAO.gpinAdminDupCheck", commUsr);
	}
	
    /**
     * 사용자 정보를 단건 조회 한다. (Sys 관리자 용)
     * 
     * @param CommUsr
     * @return
     * @throws Exception
     */
    public CommUsr selectSysCommUsrCheck(CommUsr commUsr) throws Exception {
    	return (CommUsr)selectByPk("CommUsrDAO.selectSysCommUsrCheck", commUsr);
    }
    
}
