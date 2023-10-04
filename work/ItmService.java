package com.syds.tops.itm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.oracle.xmlns.SYC_DV_BSSV_jws.E90_2900_ItemMaster.BPEL_E90_2900_ItemMaster.BPEL_E90_2900_ItemMasterProxy;
import com.oracle.xmlns.SYC_DV_BSSV_jws.E90_2900_ItemMaster.BPEL_E90_2900_ItemMaster.D4101060A_Pub_Input;
import com.oracle.xmlns.SYC_DV_BSSV_jws.E90_2900_ItemMaster.BPEL_E90_2900_ItemMaster.D4101060A_Pub_Output;
import com.oracle.xmlns.SYC_DV_BSSV_jws.E90_2900_ItemMaster.BPEL_E90_2900_ItemMaster.D57MT81A_Pub_Location;
import com.syds.basic.common.util.EwsMailUtils;
import com.syds.basic.security.SignedUser;
import com.syds.basic.util.security.SecurityUtil;
import com.syds.tops.common.service.DocumentApprovalService;

/*
import BPEL_D2900_ItemMasterProxy;
import D4101060A_Pub_Input;
import D4101060A_Pub_Output;
import D4101060A_Pub_Uom;
import D57MT81A_Pub_Location;
*/

import com.syds.tops.common.util.PageUtil;
import com.syds.tops.common.vo.DeptVO;
import com.syds.tops.itm.dao.ItmDAO;
import com.syds.tops.itm.vo.ItmBasicsVO;
import com.syds.tops.itm.vo.ItmBranchVO;
import com.syds.tops.itm.vo.ItmCategoryMappingVO;
import com.syds.tops.itm.vo.ItmCategoryVO;
import com.syds.tops.itm.vo.ItmFormulaVO;
import com.syds.tops.itm.vo.ItmHistVO;
import com.syds.tops.itm.vo.ItmLotVO;
import com.syds.tops.itm.vo.ItmMappingCategoryVO;
import com.syds.tops.itm.vo.ItmMappingVO;
import com.syds.tops.itm.vo.ItmMasterFileContentVO;
import com.syds.tops.itm.vo.ItmMasterFileDetailVO;
import com.syds.tops.itm.vo.ItmMasterFileVO;
import com.syds.tops.itm.vo.ItmMasterVO;
import com.syds.tops.itm.vo.ItmProfileVO;
import com.syds.tops.itm.vo.ItmSourcingPdeptVO;
import com.syds.tops.itm.vo.ItmUomVO;
import com.syds.tops.itm.vo.ItmVO;
import com.syds.tops.itm.vo.ItmWonbuVO;
import com.syds.tops.sys.dao.OrgDAO;
import com.syds.tops.sys.vo.BranchCdMasterVO;
import com.syds.tops.sys.vo.FiorgDeptVO;
import com.syds.tops.sys.vo.JsTreeVO;
import com.syds.tops.sys.vo.UserMasterVO;

/**
 * @author SYC719233
 * @date : 2017. 4. 12. 오전 11:46:54
 * @desc : 자재 관리(서비스)
 */
@Service("itmService")
public class ItmService {
	Logger LOG = Logger.getLogger(ItmService.class);

	@Resource(name="itmDAO")
	private ItmDAO itmDAO;

	@Resource(name="orgDAO")
	private OrgDAO orgDAO ;

    @Resource
    EwsMailUtils ewsMailUtils;

    @Resource
	DocumentApprovalService documentApprovalService;

	@SuppressWarnings("rawtypes")
	public List<Map> getItemList(HashMap<String, Object> params) {
		return itmDAO.itemList(params);
	}

	public List<ItmMasterVO> selectItmMasterListNew(ItmMasterVO params) {
		return itmDAO.itmMasterSelect(params) ;
	}

	public List<ItmMasterVO> selectItmBranchListNew(ItmMasterVO params) {
		return itmDAO.itmBranchSelect(params) ;
	}

	public List<ItmMasterVO> selectItemListNew(ItmMasterVO params) {
		return itmDAO.itmSelect(params) ;
	}


	public ItmBasicsVO saveItem(HashMap<String, Object> params) {
		FiorgDeptVO myOrgSsInfo = PageUtil.getFiorgDeptInfo();

		params.put("EMP_NO", myOrgSsInfo.empNo);
		params.put("COMP_CD", myOrgSsInfo.compErpCd);
		params.put("BU_ID", myOrgSsInfo.buTopsOrgCd);
		params.put("FACTORY_ID", myOrgSsInfo.cpTopsOrgCd);
		params.put("DEPT_ID", myOrgSsInfo.dpTopsOrgCd);

		params.put("MODE", "I");

		HashMap<String, Object> map = itmDAO.itemSave(params);

		ItmBasicsVO vo = new ItmBasicsVO();

		vo.setItmSeq((Integer)map.get("OUT_ITM_SEQ"));
		vo.setItmMasterSeq((Integer)map.get("OUT_ITM_MASTER_SEQ"));

		return vo;
	}

	public void saveItemBasics(ItmBasicsVO vo) {
		UserMasterVO user = PageUtil.getUserInfo();

		vo.setEmpNo(user.EMPNO);

		itmDAO.itemBasicsSave(vo);
	}

	public List<ItmHistVO> getItemHistList(ItmHistVO vo) {
		return itmDAO.itemHistList(vo);
	}

	public void saveItemHist(List<ItmHistVO> params) {
		UserMasterVO user = PageUtil.getUserInfo();

		for (ItmHistVO param : params) {
			param.setEmpNo(user.EMPNO);

			itmDAO.itemHistSave(param);
		}
	}

	public void deleteItemHist(List<ItmHistVO> params) {
		UserMasterVO user = PageUtil.getUserInfo();

		for (ItmHistVO param : params) {
			param.setEmpNo(user.EMPNO);

			itmDAO.itemHistDel(param);
		}
	}

	public List<ItmBranchVO> getItemBranchList(ItmBranchVO vo) {
		return itmDAO.itemBranchList(vo);
	}

	public void saveItemBranch(List<ItmBranchVO> params) {
		UserMasterVO user = PageUtil.getUserInfo();

		for (ItmBranchVO param : params) {
			param.setEmpNo(user.EMPNO);

			itmDAO.itemBranchSave(param);
		}
	}

	public void deleteItemBranch(List<ItmBranchVO> params) {
		UserMasterVO user = PageUtil.getUserInfo();

		for (ItmBranchVO param : params) {
			param.setEmpNo(user.EMPNO);

			itmDAO.itemBranchDel(param);
		}
	}

	public List<ItmUomVO> getItemUomList(ItmUomVO vo) {
		return itmDAO.itemUomList(vo);
	}

	public void saveItemUom(List<ItmUomVO> params) {
		UserMasterVO user = PageUtil.getUserInfo();

		for (ItmUomVO param : params) {
			param.setEmpNo(user.EMPNO);

			itmDAO.itemUomSave(param);;
		}
	}

	public void deleteItemUom(List<ItmUomVO> params) {
		UserMasterVO user = PageUtil.getUserInfo();

		for (ItmUomVO param : params) {
			param.setEmpNo(user.EMPNO);

			itmDAO.itemUomDel(param);
		}
	}

	public void saveItemLot(ItmLotVO param) {
		UserMasterVO user = PageUtil.getUserInfo();

		param.setEmpNo(user.EMPNO);

		itmDAO.itemLotSave(param);
	}

	public void saveItemCategory(ItmCategoryVO param) {
		UserMasterVO user = PageUtil.getUserInfo();
		param.setEmpNo(user.EMPNO);

		itmDAO.itemCategorySave(param);
	}

	/**
	 * BranchCodeMaster 조회
	 * @param brchCode
	 * @return
	 */
	public List<BranchCdMasterVO> getBranchCdMaster(BranchCdMasterVO brchCode) {
		return itmDAO.getBranchCdMaster(brchCode);
	}

	public ItmVO getSelectTotalItm(ItmVO vo) {
		return itmDAO.selectTotalItm(vo);
	}

	public List<ItmSourcingPdeptVO> getItemSourcingPdeptList() {
		return itmDAO.getItemSourcingPdeptList();
	}

	public Boolean getItemSourcingPdeptCheck(ItmSourcingPdeptVO vo) {
		return itmDAO.getItemSourcingPdeptCheck(vo);
	}

	public Boolean saveItemSourcingPdept(List<ItmSourcingPdeptVO> params) {
		UserMasterVO user = PageUtil.getUserInfo();

		Boolean totalCheck = false;

		for (ItmSourcingPdeptVO param : params) {
			Boolean checkFlag = itmDAO.getItemSourcingPdeptCheck(param);

			// 프로시저 실행 후 적어도 하나 이상 존재 시 전체 롤백 여부 진실 처리
			if(checkFlag.equals(true)) {
				totalCheck = true;

				break;
			}

			if(totalCheck.equals(false)) {
				param.setWriteEmpNo(user.EMPNO);

				itmDAO.saveItemSourcingPdept(param);
			}
		}

		return totalCheck;
	}

	public void deleteItemSourcingPdept(List<ItmSourcingPdeptVO> params) {
		for (ItmSourcingPdeptVO param : params) {
			itmDAO.deleteItemSourcingPdept(param);
		}
	}

	public List<JsTreeVO> getItemMappingCategoryTree(ItmSourcingPdeptVO vo) {
		List<JsTreeVO> treeList = new ArrayList<JsTreeVO>();

		List<ItmMappingCategoryVO> imcList = itmDAO.getItemMappingCategoryTree(vo);

		if (imcList != null) {
			for (ItmMappingCategoryVO imcVO : imcList) {
				JsTreeVO node = new JsTreeVO();

				if(imcVO.getParentCategoryCd().equals(0)) {
					node.parent = "#";
				} else {
					node.parent = imcVO.getParentCategoryCd().toString();
				}

				node.text = imcVO.getCategoryNm();
				node.id = imcVO.getItmCategoryId().toString();
				node.lv = imcVO.getLvl().toString();
				node.icon = null;
				node.state.opened = true;
				node.type = null;
				// 일단은 선택할 수 없으므로 널 값으로 넘김(트리가 만들어지기 전이므로)
				node.selectedParents = null;

				treeList.add(node);
			}
		} else {
			JsTreeVO node = new JsTreeVO();
			node.id = null;
			treeList.add(node);
		}

		return treeList;
	}

	public void saveItemMappingCategoryTree(List<ItmMappingCategoryVO> params) {
		for (ItmMappingCategoryVO param : params) {
			FiorgDeptVO myOrgSsInfo = PageUtil.getFiorgDeptInfo();

			param.setWriteEmpNo(myOrgSsInfo.empNo);

			itmDAO.saveItemMappingCategoryTree(param);
		}
	}

	public void deleteItemMappingCategoryTree(List<ItmMappingCategoryVO> params) {
		for (ItmMappingCategoryVO param : params) {
			itmDAO.deleteItemMappingCategoryTree(param);
		}
	}

	public List<ItmMappingCategoryVO> getItemMappingCategoryList(ItmMappingCategoryVO vo) {
		return itmDAO.getItemMappingCategoryList(vo);
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getItemMappingList(ItmMappingVO vo) {
		return itmDAO.getItemMappingList(vo);
	}

	public void saveItemMapping(List<ItmMappingVO> params) {
		FiorgDeptVO myOrgSsInfo = PageUtil.getFiorgDeptInfo();

		for (ItmMappingVO param : params) {
			param.setWriteEmpNo(myOrgSsInfo.empNo);

			itmDAO.saveItemMapping(param);
		}
	}

	public void deleteItemMapping(List<ItmMappingVO> params) {
		for (ItmMappingVO param : params) {
			itmDAO.deleteItemMapping(param);
		}
	}

	// 카테고리 아이디로 품목 매핑 테이블 주요 키 목록을 가져온 후 그걸로 품목 매핑 테이블과 품목 테이블 목록 조회
	@SuppressWarnings("rawtypes")
	public List<Map> getItemListByCategoryId(ItmMappingCategoryVO vo) {
		ItmCategoryMappingVO icmVO = itmDAO.getItemCategoryMappingTreeIdSelect(vo);

		ItmMappingVO imVO = new ItmMappingVO();
		imVO.setItmCategoryId(vo.getItmCategoryId());
		imVO.setItmSourcingPdeptIdx(icmVO.getItmSourcingPdeptIdx());
		imVO.setItmSourcingGroup(icmVO.getItmSourcingGroup());

		return itmDAO.getItemMappingList(imVO);
	}

	public List<JsTreeVO> getItemSessionCategoryTree() {
		FiorgDeptVO myOrgSsInfo = PageUtil.getFiorgDeptInfo();

		List<JsTreeVO> treeList = new ArrayList<JsTreeVO>();

		ItmSourcingPdeptVO vo = new ItmSourcingPdeptVO();

		if((myOrgSsInfo.getCompTopsOrgCd() != null) && (!"".equals(myOrgSsInfo.getCompTopsOrgCd()))) {
			vo.setCompCd(myOrgSsInfo.getCompTopsOrgCd());
		}

		List<ItmMappingCategoryVO> imcList = itmDAO.getItemSessionCategoryTree(vo);

		if (imcList != null) {
			for (ItmMappingCategoryVO imcVO : imcList) {
				JsTreeVO node = new JsTreeVO();

				if(imcVO.getParentCategoryCd().equals(0)) {
					node.parent = "#";
				} else {
					node.parent = imcVO.getParentCategoryCd().toString();
				}

				node.text = imcVO.getCategoryNm();
				node.id = imcVO.getItmCategoryId().toString();
				node.lv = imcVO.getLvl().toString();
				node.icon = null;
				node.state.opened = true;
				node.type = null;
				// 일단은 선택할 수 없으므로 널 값으로 넘김(트리가 만들어지기 전이므로)
				node.selectedParents = null;

				treeList.add(node);
			}
		} else {
			JsTreeVO node = new JsTreeVO();
			node.id = null;
			treeList.add(node);
		}

		return treeList;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getItemMappingVitemSearch(HashMap<String, Object> keyword) {
		FiorgDeptVO myOrgSsInfo = PageUtil.getFiorgDeptInfo();

		if((myOrgSsInfo.getCompTopsOrgCd() != null) && (!"".equals(myOrgSsInfo.getCompTopsOrgCd()))) {
			keyword.put("compCd", myOrgSsInfo.getCompTopsOrgCd());
		}

		return itmDAO.getItemMappingVitemSearch(keyword);
	}

	public List<Map<String, Object>> getItemProfileList() {
		return itmDAO.getItemProfileList();
	}

	public ItmProfileVO getItemProfileView(Integer id) {
		return itmDAO.getItemProfileView(id);
	}

	public void saveItemProfile(ItmProfileVO vo) {
		itmDAO.saveItemProfile(vo);
	}

	public List<Map<String, Object>> getItemProfileInfoList(ItmProfileVO vo) {
		return itmDAO.getItemProfileInfoList(vo);
	}

	public List<ItmMasterFileVO> getItemMasterFileList(Map<String, Object> param) {
		return itmDAO.getItemMasterFileList(param);
	}

	public List<ItmMasterFileContentVO> getItemMasterFileContentList(ItmMasterFileVO vo) {
		return itmDAO.getItemMasterFileContentList(vo);
	}

	public void saveItemMasterMarketBuyPlan(List<ItmMasterFileContentVO> params) {
		UserMasterVO user = PageUtil.getUserInfo();

		for (ItmMasterFileContentVO param : params) {
			param.setWriteEmpNo(user.getEMPNO());

			itmDAO.saveItemMasterMarketBuyPlan(param);
		}
	}

	public void deleteItemMasterMarketBuyPlan(List<ItmMasterFileContentVO> params) {
		for (ItmMasterFileContentVO param : params) {
			itmDAO.deleteItemMasterMarketBuyPlan(param);
		}
	}

	public List<ItmMasterFileDetailVO> getItemMasterFileDetailList(ItmMasterFileDetailVO vo) {
		return itmDAO.getItemMasterFileDetailList(vo);
	}

	public List<ItmMasterFileDetailVO> getItemMasterFileDetailList2(ItmMasterFileDetailVO vo) {
		return itmDAO.getItemMasterFileDetailList2(vo);
	}

	public void saveItemMasterFileDetail(ItmMasterFileDetailVO param) {
		UserMasterVO user = PageUtil.getUserInfo();
		param.setWriteEmpNo(user.getEMPNO());

		itmDAO.saveItemMasterFileDetail(param);
	}

	public void deleteItemMasterFileDetail(ItmMasterFileDetailVO vo) {
		itmDAO.deleteItemMasterFileDetail(vo);
	}

	public ItmMasterFileDetailVO getItemMasterFileDetailView(ItmMasterFileDetailVO vo) {
		return itmDAO.getItemMasterFileDetailView(vo);
	}

	/*## 포뮬라(formula) 자재 관리 */
	/**
	 * 포뮬라 자제 정보 를 가져옴
	 * @param VO
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> selectFormula(ItmFormulaVO vo){
		return  itmDAO.selectFormula(vo);
	}

	/**
	 * 포뮬라 자제 상세 보기
	 * @param vo
	 * @return
	 */
	public ItmFormulaVO viewFormula(ItmFormulaVO vo){
		return itmDAO.viewFormula(vo) ;
	}

	/**
	 * 포뮬라 자제 저장
	 * @param vo
	 * @return
	 */
	public boolean saveFormula(ItmFormulaVO vo){
		boolean isRst = false;
		if("save".equals(vo.getPageMode()) || "modify".equals(vo.getPageMode())){
			itmDAO.saveFormula(vo) ;
			isRst = true ;
		}else if("del".equals(vo.getPageMode())){
			isRst = itmDAO.delFormula(vo) ;
		}
		return isRst ;
	}
	/*## 포뮬라(formula) 자재 관리 */


	/**
	 * 사용자 부서 정보를 가져온다 .
	 * @param vDeptVO
	 * @return
	 */
	public List<DeptVO> selectDept(DeptVO deptVO){
		LOG.debug("## 뭐징?" + deptVO.toString());
		return orgDAO.listTopsDept(deptVO) ;
	}

	/**
	 * 아이템 을 가져 온다 .
	 * @param vo
	 * @return
	 */
	public List<ItmMasterVO> selectItm(ItmMasterVO vo) {
		return itmDAO.itmSelect(vo) ;
	}

	/**
	 * 아이템 마스터 파일 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> selectItmMasterFile(Map<String, Object> param) {
		return itmDAO.selectItmMasterFile(param);
	}

	/**
	 * 아이템 상위 분류 조회
	 * @param param
	 * @return
	 */
	public Map<String, Object> selectItmTopGubn(Map<String, Object> param) {
		return itmDAO.selectItmTopGubn(param);
	}

	/**
	 * 품목팝업 저장
	 * @param param
	 * @return
	 */
	public void saveWonbuItem(Map<String, Object> param) {
		param.put("empNo", PageUtil.getFiorgDeptInfo().getEmpNo());

		itmDAO.saveWonbuItem(param);
	}

	/**
	 * 아이템 팝업 용도, 비고 조회
	 * @param param
	 * @return
	 */
	public Map<String, Object> selectItmWonbuUser(Map<String, Object> param) {
		return itmDAO.selectItmWonbuUser(param);
	}

	/**
	 * 아이템 을 저장한다
	 * @param vo
	 * @return
	 */
	public boolean saveItm(ItmMasterVO vo) throws Exception {
		LOG.debug("## Item 저장 ");
		//ERP 아이템 등록
		boolean isRst = false;
		itmDAO.saveItm(vo) ;

		//코딩중
		//UOM 변환 리스트 저장
		/*
		JSONParser parser = new JSONParser() ;
		JSONArray jsonArray = (JSONArray)parser.parse(vo.getJsonUOMObj()) ;
		for(int i=0; i>jsonArray.length();i++ ) {
			JSONObject jObj = (JSONObject)jsonArray.get(i) ;
		}
		D4101060A_Pub_Input input = new D4101060A_Pub_Input()  ;
		*/
		return true ;
	}


public  Map<String, Object> toMap(JSONObject object) throws JSONException {
    Map<String, Object> map = new HashMap<String, Object>();

    Iterator<String> keysItr = object.keys();
    while(keysItr.hasNext()) {
        String key = keysItr.next();
        Object value = object.get(key);

        if(value instanceof JSONArray) {
            value = toList((JSONArray) value);
        }

        else if(value instanceof JSONObject) {
            value = toMap((JSONObject) value);
        }
        map.put(key, value);
    }
    return map;
}

	public  List<Object> toList(JSONArray array) throws JSONException {
	    List<Object> list = new ArrayList<Object>();
	    for(int i = 0; i < array.length(); i++) {
	        Object value = array.get(i);
	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        list.add(value);
	    }
	    return list;
	  }

	public boolean sendErpItm() {
		//http://E90SOA2.sy.com:8001/soa-infra/services/syc/E90_2900_ItemMaster/bpel_e90_2900_itemmaster_client_ep?wsdl 삼양사
		//http://E90SOA2.sy.com:8001/soa-infra/services/D2900/D2900_ERP_ItemMaster/bpel_d2900_itemmaster_client_ep?WSDL 계열사
		String endpoint = "" ; //propertiesService.getString("soa.sy.sup");
		BPEL_E90_2900_ItemMasterProxy	proxy = new BPEL_E90_2900_ItemMasterProxy() ;
		proxy.setEndpoint("http://E90SOA2.sy.com:8001/soa-infra/services/syc/E90_2900_ItemMaster/bpel_e90_2900_itemmaster_client_ep?wsdl");

		D4101060A_Pub_Output d4101060A_Pub_Output = null ;
		LOG.debug(">> step 01 ");
		try {
			LOG.debug(">> step 02 ");
			D4101060A_Pub_Input itmVO = settingItmVO() ;
			LOG.debug(">> step 03 ");

			d4101060A_Pub_Output = proxy.getBPEL_E90_2900_ItemMaster().process(itmVO) ;
			LOG.debug(">> step 04 ");
		} catch (Exception e) {
			LOG.error("## SOA Supplier 에러 발생 :"+d4101060A_Pub_Output.getErrMsg() );
			e.printStackTrace();
		}

		LOG.debug(">> output : " + d4101060A_Pub_Output.getErrCode() ); // "S"
		LOG.debug(">> output : " + d4101060A_Pub_Output.getErrMsg() );

		LOG.debug(">> output : " + d4101060A_Pub_Output.getItem_Short() );
		LOG.debug(">> output : " + d4101060A_Pub_Output.getItem_second() );
		LOG.debug(">> output : " + d4101060A_Pub_Output.getItem_third() );
		return true ;
	}

	public String sendErpItm(ItmVO reqItmVO) {
		//http://E90SOA2.sy.com:8001/soa-infra/services/syc/E90_2900_ItemMaster/bpel_e90_2900_itemmaster_client_ep?wsdl 삼양사
		//http://E90SOA2.sy.com:8001/soa-infra/services/D2900/D2900_ERP_ItemMaster/bpel_d2900_itemmaster_client_ep?WSDL 계열사
		String endpoint = "" ; //propertiesService.getString("soa.sy.sup");
		BPEL_E90_2900_ItemMasterProxy	proxy = new BPEL_E90_2900_ItemMasterProxy() ;
		proxy.setEndpoint("http://E90SOA2.sy.com:8001/soa-infra/services/syc/E90_2900_ItemMaster/bpel_e90_2900_itemmaster_client_ep?wsdl");

		D4101060A_Pub_Output d4101060A_Pub_Output = null ;
		LOG.debug(">> step 01 ");
		try {
			LOG.debug(">> step 02 ");
			D4101060A_Pub_Input itmVO = settingItmVO() ;
			LOG.debug(">> step 03 ");
			d4101060A_Pub_Output = proxy.getBPEL_E90_2900_ItemMaster().process(itmVO) ;
			LOG.debug(">> step 04 ");
		} catch (Exception e) {
			LOG.error("## SOA Supplier 에러 발생 :"+d4101060A_Pub_Output.getErrMsg() );
			e.printStackTrace();
		}

		LOG.debug(">> output : " + d4101060A_Pub_Output.getErrCode() );
		LOG.debug(">> output : " + d4101060A_Pub_Output.getErrMsg() );
		LOG.debug(">> output : " + d4101060A_Pub_Output.getItem_Short() );
		LOG.debug(">> output : " + d4101060A_Pub_Output.getItem_second() );
		LOG.debug(">> output : " + d4101060A_Pub_Output.getItem_third() );
		return d4101060A_Pub_Output.getErrCode() ;
	}

	public D4101060A_Pub_Output sendErpItemNoneStock(ItmVO reqItmVO) {
		String endpoint = "" ;
		BPEL_E90_2900_ItemMasterProxy	proxy = new BPEL_E90_2900_ItemMasterProxy() ;
		proxy.setEndpoint("http://E90SOA2.sy.com:8001/soa-infra/services/syc/E90_2900_ItemMaster/bpel_e90_2900_itemmaster_client_ep?wsdl");

		D4101060A_Pub_Output d4101060A_Pub_Output = null ;
		LOG.debug(">> step 01 ");
		try {
			LOG.debug(">> step 02 ");

			String masterUom = reqItmVO.getUom();
			String itmNm = reqItmVO.getItmNm() ;
			String spac = reqItmVO.getSpec() ;
			String searchText = reqItmVO.getSearchText() ;
			String glClass = reqItmVO.getGlClass() ;
			String itmSrc = reqItmVO.getItmNo() ;

			D4101060A_Pub_Input input = new D4101060A_Pub_Input() ;
			itmNoneStockSet(masterUom  , itmNm , spac , searchText , glClass , itmSrc  , input) ; //입력값
			itmNoneStockDefaultSet(input); //기본값 세팅

			LOG.debug(">> step 03 ");
			d4101060A_Pub_Output = proxy.getBPEL_E90_2900_ItemMaster().process(input) ;
			LOG.debug(">> step 04 ");
		} catch (Exception e) {
			LOG.error("## SOA Supplier 에러 발생 :"+d4101060A_Pub_Output.getErrMsg() );
			e.printStackTrace();
		}
		boolean isDebug = false;
		if(isDebug) {
			LOG.debug(">> output : " + d4101060A_Pub_Output.getErrCode() );
			LOG.debug(">> output : " + d4101060A_Pub_Output.getErrMsg() );
			LOG.debug(">> output : " + d4101060A_Pub_Output.getItem_Short() );
			LOG.debug(">> output : " + d4101060A_Pub_Output.getItem_second() );
			LOG.debug(">> output : " + d4101060A_Pub_Output.getItem_third() );
		}

		return d4101060A_Pub_Output ;
	}

	/**
	 * 입력된 수정값
	 * @return
	 */
	public D4101060A_Pub_Input settingItmVO() {
		LOG.debug(">> step 03-1 ");
		D4101060A_Pub_Input input = new D4101060A_Pub_Input() ;
		input.setMasterData_szUnitOfMeasurePurchas("BX"); 	 //구매단위
		input.setMasterData_szUnitOfMeasurePrimary("EA"); 	  //Primary 단위
		input.setMasterData_szBranchPlant("        134A");		  	  //사용 BP
		input.setMasterData_szDescriptionLine1("연동테스트아이템1"); //품명
		input.setMasterData_szDescriptionLine2("테스트 규격");	  //규격
		input.setMasterData_szSearchText("테스트");			  //검색조건

		input.setMasterData_cAllocateByLot("1");              //LOT
		input.setMasterData_szGlCategory("S210");			  //GL - class
		input.setMasterData_szLineType("S");                  //[Stock]

		input.setMasterData_szIdentifier2NdItem("SOATEST108"); //2nd 아이템번호, 숫자+영문대문자
		//input.setMasterData_szIdentifier3RdItem("SOATEST104"); //3nd
		//input.setMasterData_szIdentifier3NdItem("SOATEST102") ;//3rd 아이템번호, 숫자+영문대문자

		input.setMasterData_szProgramId("TOPS");	//레가시 시스템명
		input.setMasterData_cStockingType("P");
		input.setCategory_szPurchasingReportCode1("0");		//commodity class
		input.setCategory_szPurchasingReportCode2("001") ;	// commondity sub class
		input.setCategory_szPurchReportingCode9("EI03") ;   // 수입물품 , 국내물품
		input.setCategory_szSalesReportingCode1("B30") ;
		input.setManufacture_cIssueTypeCode("U") ;    //원부원료,포장재 일 경우 'U' 로 고정
		input.setManufacture_cPlanningCode("2");	  //MRP 사용 시 '2', 사용치 않으면 '0' , 삼양사는 default 로 '2' 설정
		input.setItemCost_szUserID("SYC216457");
		input.setItemCost_priceUnit("1000");
		LOG.debug(">> step 03-2 ");

		//Location array
		D57MT81A_Pub_Location[] d57MT81A_Pub_Locations = new D57MT81A_Pub_Location[1] ;
		d57MT81A_Pub_Locations[0]  = new D57MT81A_Pub_Location() ;
		d57MT81A_Pub_Locations[0].setLocation_cPrimaryBinPS("P");
		d57MT81A_Pub_Locations[0].setLocation_szLocation("11");
		d57MT81A_Pub_Locations[0].setLocation_szUserID("SYC216457");
		d57MT81A_Pub_Locations[0].setLocation_szBranchPlant("        134A");

		input.setEditArray_Location(d57MT81A_Pub_Locations);


		//uomconvert
		/*
		D4101060A_Pub_Uom[] d4101060A_Pub_Uom  = new D4101060A_Pub_Uom[1] ;
		d4101060A_Pub_Uom[0] = new D4101060A_Pub_Uom() ;
		d4101060A_Pub_Uom[0].setUom_szFromUOM("BX");
		d4101060A_Pub_Uom[0].setUom_szToUOM("EA");
		d4101060A_Pub_Uom[0].setUom_mnConversionFactorFromtoTo("20");
		d4101060A_Pub_Uom[0].setUom_cActionCode("1");
		d4101060A_Pub_Uom[0].setUom_szBranchPlant("        134A");

		input.setEditArray(d4101060A_Pub_Uom);
		*/

		LOG.debug(">> step 03-4 ");
		itmDefaultSet(input) ;

		LOG.debug(">> step 03-5 " + input.toString() );
		return input ;
	}

	/**
	 * 고정값 세팅
	 * @param input
	 */
	public void itmDefaultSet(D4101060A_Pub_Input input) {
		input.setMasterData_cSerialNumberRequired("N");
		input.setMasterData_cLevelPurchasingPrice("3");
		input.setMasterData_szTransactionAction("A");
		input.setMasterData_cBulkPackedFlag("P");

		input.setMasterData_cUpdateItemBranch("1");
		input.setMasterData_szVersion("SYC0001");
		input.setMasterData_cTaxableYN1("Y");
		input.setMasterData_cCostLevel("2") ;
		input.setMasterData_cCheckAvailabilityYN("Y") ;
		input.setMasterData_cCommitmentDateMethod("1");

		input.setMasterData_cBackordersAllowedYN("Y");
		input.setMasterData_cTaxableYN("Y");

		input.setMasterData_jdEffectiveThruDate("2040-12-31");
		input.setMasterData_cLotExpirationDateCalculat("1");
		input.setMasterData_cPriceLevel("1");
		input.setCategory_szProgramId("EP4101");
		input.setCategory_jdEffectiveThruDate("2040-12-31") ;
		input.setCategory_szTransactionAction("A");
		input.setCategory_cUpdateMasterFile("1");
		//input.setManufacture_cIssueTypeCode("I");
		input.setManufacture_cOrderPolicyCode("1") ;
		input.setManufacture_szProgramId("EP4101");
		input.setManufacture_mnAcctingCostQty("1");
		input.setManufacture_jdEffectiveThruDate("2040-12-31");
		input.setManufacture_szVersion("SYC0001");
		input.setManufacture_cUpdateMasterFile("1");
		input.setManufacture_szTransactionAction("A");
		input.setUpdateItemProcess_cCategoryCodes("1");
		input.setUpdateItemProcess_szProgramId("EP4101");
		input.setUpdateItemProcess_szTransactionAction("A");
		input.setUpdateItemProcess_cBasicItemData("1");
		input.setUpdateItemProcess_jdEffectiveThruDate("2040-12-31");
		input.setUpdateItemProcess_cAdditionalSystemInfo("1") ;
		input.setUpdateItemProcess_cCategoryCodes("1");
		input.setCustService_szTransactionAction("A");
		input.setPacking_szTransactionAction("A");
		input.setBulk_szTransactionAction("A") ;
	}

	public void itmNoneStockSet(String masterUom
								, String itmNm
								, String spac
								, String searchText
								, String glClass
								, String itmSrcNumber
								, D4101060A_Pub_Input input) {

		input.setMasterData_szUnitOfMeasurePurchas(masterUom) ;     //구매단위
		input.setMasterData_szUnitOfMeasurePrimary(masterUom) ;     //Primary 단위
		input.setMasterData_szUnitOfMeasureSecondary(masterUom) ;   //그외 구매단위
		input.setMasterData_szUnitOfMeasureStocking(masterUom) ;    //그외 구매단위
		input.setMasterData_szUnitOfMeasurePricing(masterUom) ;     //그외 구매단위
		input.setMasterData_szUnitOfMeasureProduction(masterUom) ;  //그외 구매단위
		input.setMasterData_szUnitOfMeasureComponent(masterUom) ;   //그외 구매단위
		input.setMasterData_szUnitOfMeasureShipping(masterUom) ;    //그외 구매단위
		input.setMasterData_szUnitOfMeasureWeight(masterUom) ;      //그외 구매단위
		input.setMasterData_szDescriptionLine1(itmNm);     //품명
		input.setMasterData_szDescriptionLine2(spac) ;      //규격
		input.setMasterData_szSearchText(searchText) ;              //검색조건
		input.setMasterData_szGlCategory(glClass);				//GL Class
		//input.setMasterData_szIdentifier2ndItem(itmSrc);       //2nd 아이템번호, 숫자+영문대문자
		//input.setMasterData_szIdentifier3rdItem(itmSrc);       //3rd 아이템번호, 숫자+영문대문자
	}

	public void itmNoneStockDefaultSet(D4101060A_Pub_Input input) {
		input.setMasterData_cSerialNumberRequired("N") ;
		input.setMasterData_cLevelPurchasingPrice("1") ;
		input.setMasterData_szTransactionAction("A") ;
		input.setMasterData_cBulkPackedFlag("P") ;
		input.setMasterData_cUpdateItemBranch("1") ;
		input.setMasterData_szVersion("SYC0001") ;
		input.setMasterData_cCostLevel("1") ;
		input.setMasterData_cCheckAvailabilityYN("Y") ;
		input.setMasterData_cCommitmentDateMethod("1") ;
		input.setMasterData_cAllocateByLot("1") ;
		input.setMasterData_szLineType("N") ;
		input.setMasterData_cBackordersAllowedYN("Y") ;
		input.setMasterData_jdEffectiveThruDate("2040-12-31") ;
		input.setMasterData_szProgramId("EP4101") ;
		input.setMasterData_cStockingType("N") ;
		input.setMasterData_cLotExpirationDateCalculat("1") ;
		input.setMasterData_cPriceLevel("1") ;
		input.setCategory_szProgramId("EP4101") ;
		input.setCategory_jdEffectiveThruDate("2040-12-31") ;
		input.setCategory_szTransactionAction("A") ;
		input.setCategory_szSalesReportingCode1("R80") ;
		input.setCategory_cUpdateMasterFile("1") ;
		input.setManufacture_szTransactionAction("A") ;
		input.setUpdateItemProcess_szProgramId("EP4101") ;
		input.setUpdateItemProcess_szTransactionAction("A") ;
		input.setUpdateItemProcess_cBasicItemData("1") ;
		input.setUpdateItemProcess_jdEffectiveThruDate("2040-12-31") ;
		input.setUpdateItemProcess_cAdditionalSystemInfo("1") ;
		input.setCustService_szTransactionAction("A") ;
		input.setPacking_szTransactionAction("A") ;
		input.setBulk_szTransactionAction("A") ;
	}

	/**
	 * 아이템을 저장한다.
	 * @param vo
	 * @return
	 */
	public boolean saveNoneItm(ItmMasterVO vo) {
		LOG.debug("## Item None 저장 ");
		return itmDAO.saveNoneItm(vo) ;
	}


	/**
	 * 원부 카테고리1 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> selectWonbuCategory1(Map<String, Object> param) {
		return itmDAO.selectWonbuCategory1(param);
	}

	/**
	 * 원부 카테고리2 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> selectWonbuCategory2(Map<String, Object> param) {
		return itmDAO.selectWonbuCategory2(param);
	}

	/**
	 * 원부 카테고리3 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> selectWonbuCategory3(Map<String, Object> param) {
		return itmDAO.selectWonbuCategory3(param);
	}

	/**
	 * 원부 카테고리 관리
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectWonbuCategory(Map<String,Object> param){
		return itmDAO.SP_WONBU_CATEGORY(param) ;
	}


	/**
	 * 원부 카테고리 저장
	 * @param param
	 * @return
	 */
	public boolean selectWonbuCategorySave(List<ItmWonbuVO> param){

		 for (ItmWonbuVO itmWonbuVO : param) {
				itmWonbuVO.setIS_IMP_ITM("N");
				itmDAO.SP_WONBU_CATEGORY3_SAVE(itmWonbuVO) ;
		 }

		 return true;
	}

	public boolean selectWonbuCategory2Save(List<ItmWonbuVO> param){

		 for (ItmWonbuVO itmWonbuVO : param) {
				itmWonbuVO.setIS_IMP_ITM("N");
				itmDAO.SP_WONBU_CATEGORY2_SAVE(itmWonbuVO) ;
		 }

		 return true;
	}


	/**
	 * 원부 카테고리 아이템
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectWonbuCategoryItm(Map<String,Object> param){
		return itmDAO.SP_WONBU_CATEGORY_ITM(param) ;
	}


	public List<Map<String,Object>> selectWonbuCategoryItm2(Map<String,Object> param){
		return itmDAO.SP_WONBU_CATEGORY_ITM2(param) ;
	}

	/**
	 * 아이템 추가 삭제
	 * @param param
	 * @return
	 */
	public boolean saveWonbuCategoryItmUpdate(List<Map<String,Object>> param){

		 for (Map<String,Object> rowMap : param) {
			 	for (String key : rowMap.keySet()) {
			 		LOG.debug(">> rowMap :" + key + ":" + rowMap.get(key));
				}
				itmDAO.SP_WONBU_CATEGORY_ITM_UPDATE(rowMap) ;
		 }

		 return true;
	}

	public boolean saveWonbuCategoryItmUpdate(Map<String,Object> param){
		itmDAO.SP_WONBU_CATEGORY_ITM_UPDATE(param) ;
		return true;
	}



	/**
	 * 원부 아이템
	 * @param param
	 * @return
	 */
	public  List<Map<String,Object>>  selectWonbuItem(Map<String, Object> param) {
		 return itmDAO.SP_ITM_NEW_VIEW(param);
	}

	public  int selectWonbuItemCount(Map<String, Object> param) {
		Map<String, Object> rst =  itmDAO.SP_ITM_NEW_VIEW_COUNT(param);
		int cnt = 0 ;
		if(rst != null) {
			 cnt = (Integer)rst.get("CNT") ;
		}
		return cnt ;
	}

	public List<Map<String,Object>>  selectItmNewBuView(Map<String, Object> param) {
		 return itmDAO.SP_ITM_NEW_BU_VIEW(param);
	}

//	public List<ItmVO> itmErpAddSync(ItmVO vo) {
	public List<Map<String,Object>> itmErpAddSync(Map<String, Object> param) {
		return itmDAO.itmErpAddSync(param);
	}

	public List<Map<String,Object>> PROC_SELECT_MAP(Map<String, Object> param , String query) {
		return itmDAO.PROC_SELECT_MAP(param , query);
	}

	public boolean itmBasicMmMgtSave( Map<String,Object> reqForm , List<Map<String,Object>> reqMgtList) {
		boolean isRst = false;
		for (String key : reqForm.keySet()) {
			LOG.debug(" reqFrm [" + key + "] = " + reqForm.get(key));
		}

		for(int i=0;i<reqMgtList.size();i++) {
			Map<String,Object> row = reqMgtList.get(i) ;
			for(String key : row.keySet()) {
				LOG.debug(" row [" + key + "] = " + row.get(key));
			}
		}

		for(int i=0;i<reqMgtList.size();i++) {
			Map<String,Object> row = reqMgtList.get(i) ;
			for(String key : row.keySet()) {
				LOG.debug(" row [" + key + "] = " + row.get(key));
			}
		}

		HashMap<String,Object> headerParam = new HashMap<String,Object>() ;
		List<Map<String,Object>> headerRs = itmDAO.PROC_SELECT_MAP(headerParam , "SP_ITM_MM_MASTER_SELECT") ;
		for(int i=0;i<reqMgtList.size();i++) {
			Map<String,Object> row = reqMgtList.get(i) ;
			String sStatus =  String.valueOf(row.get("sStatus")) ;
			Map<String,Object> saveMap = new HashMap<String,Object>() ;

			String mgtDt = String.valueOf(row.get("MGT_DT")) ; //구분
			String[] mgtDts = mgtDt.split("-") ;
			String crcd = String.valueOf(row.get("CRCD")) ;    //환율
			String bigo = String.valueOf(row.get("BIGO")) ;    //환율
			String year = mgtDts[0] ;
			String month = mgtDts[1] ;
			int quarter = (int) Math.ceil( Integer.parseInt(month) / 3.0 ); //분기 구하기
			List<HashMap<String,Object>>  mailTarget = privateMMMgtSave(headerRs, row, mgtDt, year, quarter , sStatus);
			HashMap saveRow = new HashMap<String,Object>() ;

			saveRow.put("MGT_DT", mgtDt) ;
			saveRow.put("MGT_YEAR",year) ;
			saveRow.put("MGT_WEEK",quarter) ;
			saveRow.put("CRCD", String.valueOf(row.get("CRCD"))) ;
			saveRow.put("BIGO", String.valueOf(row.get("BIGO"))) ;
			saveRow.put("MODE",sStatus) ;
			itmDAO.PROC_INSERT(saveRow, "SP_TOPS_ITM_MM_MGT_DETAIL_SAVE") ;

			//메일 전송 :
			if("I".equals(sStatus) || "U".equals(sStatus)) {
				boolean isMailSend = false;
				StringBuffer mailContent = new StringBuffer() ;
				if(mailTarget != null && mailTarget.size() >= 1) {
					mailContent.append("<head><title>삼양그룹 통합구매시스템</title><link href='http://tops.samyang.com/resources/default/stylesheets/style.css' rel='stylesheet' type='text/css'>") ;
					mailContent.append("<table class='tType2' style='border:1px #333333 solid ; padding:0px ; margin: 0px; '>") ;
					mailContent.append("<tr><td>원료</td><td>증감(%)</td><td>대분류</td><td>중분류</td><td>소분류</td><td>품목명</td><td>규격</td><td>UOM</td><td>UOM</td><td>SHT.</td><td>SCND.</td><td>THIRD.</td></tr>") ;
					for (HashMap<String, Object> mailData : mailTarget) {
						HashMap mailParam = new HashMap() ;
						mailParam.put("mmIdx", String.valueOf(mailData.get("IDX"))) ;
						String itemNm = String.valueOf(mailData.get("ITM_NM")) ;
						String spotAddInt = String.valueOf(mailData.get("spotAddInt")) ;
						List<Map<String,Object>> mailTargetItm = itmDAO.PROC_SELECT_MAP(mailParam, "SP_ITM_MM_ITEM_VIEW") ;
						for(int k = 0;k<mailTargetItm.size();k++) {
							HashMap<String, Object> mailRow = (HashMap<String, Object>)mailTargetItm.get(k) ;
							mailContent.append("<tr >") ;
							mailContent.append("<td >" + itemNm + "</td>");
							mailContent.append("<td  align='center'>" + spotAddInt + "%</td>");
							mailContent.append("<td >" + String.valueOf(mailRow.get("CATEGORY_NM1")) + "</td>");
							mailContent.append("<td >" + String.valueOf(mailRow.get("CATEGORY_NM2")) + "</td>");
							mailContent.append("<td >" + String.valueOf(mailRow.get("CATEGORY_NM3")) + "</td>");
							mailContent.append("<td >" + String.valueOf(mailRow.get("compNm")) + "</td>");
							mailContent.append("<td >" + String.valueOf(mailRow.get("itmNm")) + "</td>");
							mailContent.append("<td >" + String.valueOf(mailRow.get("specStandard")) + "</td>");
							mailContent.append("<td >" + String.valueOf(mailRow.get("uomPurchasing")) + "</td>");
							mailContent.append("<td >" + String.valueOf(mailRow.get("shortItemNo")) + "</td>");
							mailContent.append("<td >" + String.valueOf(mailRow.get("scndItemNo")) + "</td>");
							mailContent.append("<td >" + String.valueOf(mailRow.get("thirdItemNo")) + "</td>");
							mailContent.append("<tr>") ;
						}

					}
					mailContent.append("</table>") ;

	                ArrayList recips = new ArrayList() ;
	                recips.add("mingoo.lee@samyang.com") ;
	                recips.add("jaehyun.song@samyang.com") ; //송재현

	                recips.add("chulmin.han@samyang.com") ; 			//한철민
	                recips.add("dohyung.kim@samyang.com") ; 		//김도형(삼양홀딩스 원부원료팀) <
	                recips.add("chankyoung.park@samyang.com") ; 	//박찬경 <>;
	                recips.add("hyewon.shim@samyang.com") ; 		//심혜원 <>;
	                recips.add("sanghun.lee@samyang.com") ; 			//이상훈(삼양홀딩스 원부원료팀) <>;
	                recips.add("kyungseok.chung@samyang.com") ; 	//정경석 <>;
	                recips.add("seungkwon.chung@samyang.com") ;	 //정승권 <>;
	                recips.add("jaewook.joo@samyang.com") ; 			 //주재욱(삼양홀딩스 원부원료팀) <>;
	                recips.add("seungkwon.chung@samyang.com") ;	 //정승권 <>;
	                recips.add("jaewook.joo@samyang.com") ; 			 //주재욱(삼양홀딩스 원부원료팀) <>;
	                recips.add("seonmi.lee@samyang.com");
	                recips.add("juhee.lee@samyang.com");

	                //메일전송
	                ewsMailUtils.ewsMailSend("원부원료 원료 정보" , mailContent.toString()  , recips);
				}

			}
		}

		return isRst ;
	}


	public boolean itmBasicMmMgt2Save( Map<String,Object> reqForm , List<Map<String,Object>> reqMgtList) {
		boolean isRst = false;

		//파라미터 정보를 확인 한다 . !
		for (String key : reqForm.keySet()) {
			LOG.debug(" reqFrm [" + key + "] = " + reqForm.get(key));
		}

		for(int i=0;i<reqMgtList.size();i++) {
			Map<String,Object> row = reqMgtList.get(i) ;
			for(String key : row.keySet()) {
				LOG.debug(" row [" + key + "] = " + row.get(key));
			}
		}

		//기본 마스터 정보
		String mode = String.valueOf(reqForm.get("MODE")) ;
		String mgtDt = String.valueOf(reqForm.get("MGT_DT")) ; //구분
		String year = mgtDt.substring(0, 3) ;
		String month = mgtDt.substring(3,5)
				;
		LOG.debug(">> year = " + year);
		LOG.debug(">> month = " + year);
		LOG.debug(">> mode = " + mode);

		String crcd = reqForm.get("CRCD") != null ? "0" : String.valueOf(reqForm.get("CRCD")) ;
		String bigo = String.valueOf(reqForm.get("BIGO")) ;
		int quarter = (int) Math.ceil( Integer.parseInt(month) / 3.0 ); //분기 구하기

		for(int i=0;i<reqMgtList.size();i++) {
			Map<String,Object> row = reqMgtList.get(i) ;
			String sStatus =  String.valueOf(row.get("sStatus")) ;
			Map<String,Object> saveMap = new HashMap<String,Object>() ;
			String idx = String.valueOf(row.get("IDX")) ;
			String spot = row.get("SPOT") == null || "".equals(String.valueOf(row.get("SPOT")))  ? "0" :  String.valueOf(row.get("SPOT")) ;

			HashMap saveRow = new HashMap<String,Object>() ;
			saveRow.put("MODE" , mode) ;
			saveRow.put("MGT_DT", mgtDt) ;
			saveRow.put("MGT_YEAR", year) ;
			saveRow.put("MGT_WEEK", quarter) ;
			saveRow.put("MM_IDX", idx) ;
			saveRow.put("SPOT", spot) ;
			saveRow.put("MGT_IDX_OUT", 0) ;
			//saveRow.put("SPOT_ADD", spotAddVal) ;
			itmDAO.PROC_INSERT(saveRow, "SP_TOPS_ITM_MM_MGT2_SAVE") ;
			String.valueOf(saveRow.get("MGT_IDX_OUT")) ;
		}

		//#비고랑 환율을 업데이트 한다 .

		HashMap saveRow = new HashMap<String,Object>() ;
		saveRow.put("MGT_DT", mgtDt) ;
		saveRow.put("MGT_YEAR",year) ;
		saveRow.put("MGT_WEEK",quarter) ;
		saveRow.put("CRCD", "0" ) ;
		saveRow.put("BIGO", "" ) ;
		saveRow.put("MODE", mode) ;
		itmDAO.PROC_INSERT(saveRow, "SP_TOPS_ITM_MM_MGT_DETAIL_SAVE") ;

		return isRst ;
	}

	private List<HashMap<String,Object>> privateMMMgtSave( List<Map<String, Object>> headerRs, Map<String, Object> row, String mgtDt,String year, int quarter  , String mode) {
		List<HashMap<String,Object>> rstMailTarget = new ArrayList() ;
		for (Map<String, Object> cols : headerRs) {
			String idx =  String.valueOf(cols.get("IDX")) ;
			String spotVal =  String.valueOf(row.get("SPOT_"+idx));
			String spotAddVal = String.valueOf(row.get("SPOT_ADD_"+idx)) ;
			HashMap saveRow = new HashMap<String,Object>() ;
			LOG.debug(">> IDX : " + idx);
			saveRow.put("MODE" , mode) ;
			saveRow.put("MGT_DT", mgtDt) ;
			saveRow.put("MGT_YEAR", year) ;
			saveRow.put("MGT_WEEK", quarter) ;
			saveRow.put("MM_IDX", idx) ;
			saveRow.put("SPOT", spotVal) ;
			saveRow.put("SPOT_ADD", spotAddVal) ;
			saveRow.put("MGT_IDX_OUT", 0) ;
			itmDAO.PROC_INSERT(saveRow, "SP_TOPS_ITM_MM_MGT_SAVE") ;
			String.valueOf(saveRow.get("MGT_IDX_OUT")) ;
			//Mail 대상을 선정 한다.
			int spotAddInt = Integer.parseInt(spotAddVal) ;
			int spotLimit = Integer.parseInt(String.valueOf(cols.get("LIMIT"))) ;
			HashMap<String,Object> mailTargetMap = new HashMap<String,Object>() ;
			if(spotAddInt >= spotLimit  ) {
				mailTargetMap.put("IDX", idx) ;
				mailTargetMap.put("ITM_NM", String.valueOf(cols.get("ITM_NM")) ) ;
				mailTargetMap.put("spotAddInt", spotAddInt) ;
				rstMailTarget.add(mailTargetMap) ;
			}

		}
		return rstMailTarget ;
	}

	public boolean itmBasicMmSave( Map<String,Object> reqForm , List<Map<String,Object>> reqCategory, List<Map<String,Object>> reqItm) {
		boolean isRst = false ;
		String mode = String.valueOf(reqForm.get("mode")) ;
		LOG.debug("## 실행 모드 : " + mode);
		String idx = "" ;
		FiorgDeptVO myOrgSsInfo = PageUtil.getFiorgDeptInfo();
		reqForm.put("inputEmpNo", myOrgSsInfo.empNo) ;
		reqForm.put("delYn", "N") ;
		reqForm.put("OUT_IDX" , "") ;
		reqForm.put("IDX" , "") ;
		/*
        for( String key : reqForm.keySet() ){
            System.out.println( String.format("키 : %s, 값 : %s", key, reqForm.get(key)) );
        }
		*/
        if("modify".equals(mode)) {
        	idx = String.valueOf(reqForm.get("mmIdx")) ;
            reqForm.put("IDX" ,  idx) ;

            //기본 저장된 내용 삭제 처리 한다 !
            Map<String,Object> delParam = new HashMap<String,Object>() ;
            delParam.put("MODE", "ALL_DEL") ;
            delParam.put("MM_IDX", idx) ;
            itmDAO.PROC_INSERT(delParam, "SP_ITM_MM_CATEGORY_SAVE") ; //카테고리 삭제
            itmDAO.PROC_INSERT(delParam, "SP_ITM_MM_ITEM_SAVE") ; //카테고리 삭제
        }

		itmDAO.PROC_INSERT(reqForm, "SP_ITM_MM_MASTER_SAVE") ;
		String mmIdx = String.valueOf(reqForm.get("OUT_IDX")) ;

		if(mmIdx != null && !"".equals(mmIdx) ) {
			LOG.debug("카테고리 Category 저장 : " + reqCategory.size());
			for (Map<String, Object> rowCategory : reqCategory) {
				if( "1".equals(String.valueOf(rowCategory.get("checkBox2"))) ) {
					rowCategory.put("MM_IDX", mmIdx);
					itmDAO.PROC_INSERT(rowCategory, "SP_ITM_MM_CATEGORY_SAVE") ;
				}
			}

			//아이템 저장
			LOG.debug("카테고리 itm 저장 : " + reqItm.size());
			for (Map<String, Object> rowItem : reqItm) {
				rowItem.put("MM_IDX", mmIdx);
				itmDAO.PROC_INSERT(rowItem, "SP_ITM_MM_ITEM_SAVE") ;
			}
			isRst = true  ;
		}else {
			isRst = false;
		}

		return isRst ;
    }


	public boolean itmBasicMmDelete( List<Map<String,Object>> reqItm) {
		boolean isRst = false ;
		for (Map<String, Object> row : reqItm) {
			if( "1".equals(String.valueOf(row.get("checkBox"))) ) {
				itmDAO.PROC_INSERT(row, "SP_ITM_MM_CATEGORY_DELETE") ;
			}
		}
		isRst = true ;
		return isRst ;
    }

	/**
	 * SRM 저장 및 버전 확정
	 * @param reqMap
	 * @param reqCurrtLst
	 * @param reqSupList
	 * @return
	 */
	public boolean itmSrmSave(Map<String,Object> reqMap  , List<Map<String,Object>> reqSupList) {
		SignedUser signedUser = SecurityUtil.getSignedUser();

		boolean isRst = false;
		String event = reqMap.get("event" ) == null ? "save" : String.valueOf(reqMap.get("event"))  ;

		if("save".equals(event) ) {
			//저장
			if(!reqMap.containsKey("empNo"))
				reqMap.put("empNo",signedUser.getUsername()) ;

			//마스터 정보 저장
			itmDAO.PROC_INSERT(reqMap, "SP_TOPS_SRM_SAVE") ;
			int idx = Integer.parseInt( String.valueOf(reqMap.get("outIdx")) ) ;

			LOG.debug("## idx 값은 : " + idx);
			/*
			for (Map<String, Object> row : reqSupList) {
				LOG.debug(row.get("sStatus")) ;
				if(!row.containsKey("vesionIdx"))
					row.put("vesionIdx" , idx) ;
				if(row.get("vesionIdx") == null || "".equals(row.get("vesionIdx")))
					row.put("vesionIdx" , idx) ;
				if("D".equals(row.get("sStatus")))
					row.put("delyn" , "Y") ;

				itmDAO.PROC_INSERT(row, "SP_TOPS_SRM_SUP_SAVE");
			}
			*/

			/*  업체 저장 삭제
			for (Map<String, Object> row : reqCurrtLst) {
				LOG.debug(row.get("sStatus")) ;
				if(!row.containsKey("vesionIdx"))
					row.put("vesionIdx" , idx) ;
				if(row.get("vesionIdx") == null || "".equals(row.get("vesionIdx")))
					row.put("vesionIdx" , idx) ;
				if("D".equals(row.get("sStatus")))
					row.put("delyn" , "Y") ;

				itmDAO.PROC_INSERT(row,"SP_TOPS_SRM_CURRT_SAVE") ;
			}
			*/
		}else if("versionSave".equals(event) ) {
			Map versionParam = new HashMap<String,Object>() ;
			versionParam.put("idx", reqMap.get("idx")) ;
			versionParam.put("empNo",signedUser.getUsername()) ;
			itmDAO.PROC_INSERT(versionParam,"SP_TOPS_SRM_VERSION_SAVE") ;
		}
		isRst = true ;

		return isRst ;
	}

	/**
	 * SRM view
	 * @param reqMap
	 * @param rstType
	 * @return
	 */
	public List<Map<String,Object>> itmSrmView(Map<String,Object> reqMap,String rstType){
		String query = "" ;
		if("viewMain".equals(rstType)) {
			query  = "SP_TOPS_SRM_VIEW" ;
		}
		else if("viewSupList".equals(rstType)) {
			query  = "SP_TOPS_SRM_SUP_VIEW" ;
		}
		else if("viewCurrtList".equals(rstType)) {
			query  = "SP_TOPS_SRM_CURRT_VIEW" ;
		}
		else if("viewVersionList".equals(rstType)) {
			query  = "SP_TOPS_SRM_VERSION_LIST" ;
		}
		return itmDAO.PROC_SELECT_MAP(reqMap,query) ;
	}

	public List<Map<String,Object>> itmSrmVersionView(Map<String,Object> reqMap,String rstType){
		String query = "" ;
		if("viewMain".equals(rstType)) {
			query  = "SP_TOPS_SRM_VERSION_VIEW" ;
		}
		else if("viewSupList".equals(rstType)) {
			query  = "SP_TOPS_SRM_SUP_VIEW" ;
		}
		else if("viewCurrtList".equals(rstType)) {
			query  = "SP_TOPS_SRM_CURRT_VIEW" ;
		}

		return itmDAO.PROC_SELECT_MAP(reqMap,query) ;
	}


	@SuppressWarnings("unchecked")
	public String getScmPrintHtml(Map<String, Object> param) throws Exception{

		String categoryId1 = String.valueOf(param.get("categoryId1")) ;
		String categoryId2 = String.valueOf(param.get("categoryId2")) ;
		String categoryId3 = String.valueOf(param.get("categoryId3")) ;
		LOG.debug(">> categoryId1:" +categoryId1 );
		LOG.debug(">> categoryId2:" +categoryId2 );
		LOG.debug(">> categoryId3:" +categoryId3 );

		//마스터 정보
		int idx  = 0 ;
		Map<String,Object> mRow = new HashMap<String,Object>() ;
		List<Map<String,Object>>  rsView  = itmSrmView(param, "viewMain") ;
		for (Map<String, Object> row : rsView) {
			idx = Integer.parseInt(String.valueOf(row.get("idx"))) ;
			mRow = row ;
		}
		if(!param.containsKey("idx")) {
			param.put("idx", idx) ;
		}
		if(mRow != null) {
			if(mRow.get("marketTrend") == null)
				mRow.put("marketTrend" , "") ;
			if(mRow.get("assayer") == null)
				mRow.put("assayer" , "") ;
			else
				mRow.put("assayer" , String.valueOf(mRow.get("assayer")).trim() ) ;


			System.out.println("#==================================");
			System.out.println(mRow.get("assayer"));
			String shortItmNo = String.valueOf(param.get("shortItmNo")) ;
			String thirdItmNo = String.valueOf(param.get("thirdItmNo")) ;
			String stDt = String.valueOf(param.get("stDt")) ;
			String edDt =String.valueOf(param.get("edDt")) ;
			if(stDt.indexOf("-") != -1)
				stDt = stDt.replaceAll("-", "") ;
			if(edDt.indexOf("-") != -1)
				edDt = edDt.replaceAll("-", "") ;

			mRow.put("categoryId1",categoryId1) ;
			mRow.put("categoryId2",categoryId2) ;
			mRow.put("categoryId3",categoryId3) ;
			mRow.put("year",String.valueOf(param.get("year")) ) ;
			mRow.put("stDt" , stDt ) ;
			mRow.put("edDt" , edDt ) ;
			mRow.put("shortItmNo" , shortItmNo  ) ;
			mRow.put("thirdItmNo" , thirdItmNo ) ;
			String mode = "total" ;
			if( !"".equals(shortItmNo) && !"".equals (thirdItmNo))
					mode = "itm" ;
			mRow.put("mode" , mode ) ;
		}

		//양식지 세팅
		mRow.put("formPrefix", param.get("formPrefix") );
		mRow.put("printFormPrefix", param.get("printFormPrefix") );

		//구매현황
		Map<String, Object> headerIBSheet1 = new LinkedHashMap<String, Object>();
		headerIBSheet1.put("useText","용도^^center") ;
		headerIBSheet1.put("content","내용^^center") ;
		List<Object> list1 = new ArrayList<Object>();
		list1.add(headerIBSheet1) ;
		list1.add(itmSrmView(param, "viewCurrtList")) ;

		//연간구매총액
		Map<String, Object> headerIBSheet2 = new LinkedHashMap<String, Object>();
		headerIBSheet2.put("inputEmpDt","년^^center") ;
		headerIBSheet2.put("awardAmt","금액^^center") ;
		headerIBSheet2.put("cnt","수량^^center") ;
		List<Object> list2 = new ArrayList<Object>();
		list2.add(headerIBSheet2) ;
		list2.add(PROC_SELECT_MAP(param , "SP_TOPS_SRM_TOTAL_AMT_SELECT") ) ;

		//공급업체
		Map<String, Object> headerIBSheet3 = new LinkedHashMap<String, Object>();

		headerIBSheet3.put("ipgoDate","년^^center") ;
		headerIBSheet3.put("supNm","업체^^center") ;
		headerIBSheet3.put("dan","단가^^center") ;
		headerIBSheet3.put("qty","수량^^center") ;
		headerIBSheet3.put("amt","금액^^center") ;

		List<Object> list3 = new ArrayList<Object>();
		list3.add(headerIBSheet3) ;
		list3.add(itmSrmView(param, "viewSupList")) ;

		//Html Append
		List<Object> lists = new ArrayList<Object>();
	    Map<String, Object> map = null ;
	    lists.add(list1) ;
	    lists.add(list2) ;
	    lists.add(list3) ;
		return documentApprovalService.getContent(mRow, lists);
	}

	/**
	 * 품목 목록 조회
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getItems(Map<String, Object> params) {
		return itmDAO.getItems(params);
	}

	/**
	 * 계약 품목 등록
	 * @param param1
	 * @param param2
	 * @throws Exception
	 */
	public void saveContItm(List<Map<String, Object>> contItms) throws Exception{
		FiorgDeptVO fd = PageUtil.getFiorgDeptInfo();
		for(Map<String, Object> contItm: contItms) {
			contItm.put("inputEmpNo", fd.getEmpNo());
			itmDAO.saveContItm(contItm);
		}
	}

	public List<Map<String, Object>> selectJdeErpItm(Map<String, Object> param) {
		return itmDAO.selectJdeErpItm(param);
	}
	
	/**
	 * 투자유형 카테고리 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> selectInvType(Map<String, Object> param) {
		return itmDAO.selectInvType(param);
	}
}