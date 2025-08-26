package egovframework.admin.basicinf.service;

import java.util.ArrayList;
import java.util.List;

public interface CommOrgMngUsrService {

	List<CommUsrAdmin> selectOrgMngUsrList(CommUsrAdmin srchVo) throws Exception;

	int saveOpenDtfileOrgRelCUD(ArrayList<CommUsrAdmin> list);  
 
}
