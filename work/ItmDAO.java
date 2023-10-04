package com.syds.tops.itm.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.syds.basic.common.dao.mybatis.AbstractDAO;
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
import com.syds.tops.sys.vo.BranchCdMasterVO;
import com.syds.tops.sys.vo.CodeMasterVO;
import com.syds.tops.sys.vo.CodeVO;

/**
 * @author SYC719233
 * @date : 2017. 4. 12. 오전 11:34:46
 * @desc : 자재관리 DAO
 */
@Repository("itmDAO")
@SuppressWarnings("unchecked")
public class ItmDAO extends AbstractDAO {
	final String NAMESPACE = "item.";

	Logger LOG = Logger.getLogger(ItmDAO.class);

	/**
	 * 자재 리스트 조회
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> itemList(HashMap<String, Object> params) {
		return sqlSelectForListOfMap(NAMESPACE + "itemList", params);
	}

	/**
	 * 자재 리스트 조회
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> selectItemViewList(HashMap<String, Object> params) {
		return sqlSelectForListOfMap(NAMESPACE + "selectItemViewList", params);
	}

	/**
	 * 자재 리스트 조회
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> selectItemViewOneList(HashMap<String, Object> params) {
		return sqlSelectForListOfMap(NAMESPACE + "selectItemViewOneList", params);
	}
	
	/**
	 * 자재 리스트 조회
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> selectItemViewOneList2(HashMap<String, Object> params) {
		return sqlSelectForListOfMap(NAMESPACE + "selectItemViewOneList2", params);
	}


	/**
	 * 원부 원료 카테고리 아이템 추가 팝업
	 * @param param
	 * @return
	 */
	public List<Map> SP_ITM_POPUP_SELECT(Map<String, Object> params) {
		return sqlSelectForList(NAMESPACE + "SP_ITM_POPUP_SELECT", params);
	}


	/**
	 * 자재 리스트 추가 및 수정
	 */
	public HashMap<String, Object> itemSave(HashMap<String, Object> params) {
		sqlInsert(NAMESPACE + "itemSave", params);

		return params;
	}

	/**
	 * 자재 리스트 상세 정보
	 */
	public ItmVO itemView(HashMap<String, Object> params) {
		return (ItmVO)sqlSelectForObject(NAMESPACE + "itemView", params);
	}

	/**
	 * 자재 베이직 추가 및 수정
	 */
	public void itemBasicsSave(ItmBasicsVO vo) {
		sqlInsert(NAMESPACE + "itemBasicsSave", vo);
	}

	/**
	 * 자재 기본 상세 정보
	 */
	public ItmBasicsVO itemBasicsView(ItmBasicsVO vo) {
		return (ItmBasicsVO)sqlSelectForObject(NAMESPACE + "itemBasicsView", vo);
	}

	/**
	 * 자재 목록 정보 조회
	 */
	public List<ItmHistVO> itemHistList(ItmHistVO vo) {
		return sqlSelectForList(NAMESPACE + "itemHistList", vo);
	}

	/**
	 * 아이템 정보(히스토리) 저장 및 수정
	 */
	public void itemHistSave(ItmHistVO vo) {
		sqlInsert(NAMESPACE + "itemHistSave", vo);
	}

	/**
	 * 아이템 정보(히스토리) 삭제
	 */
	public void itemHistDel(ItmHistVO vo) {
		sqlDelete(NAMESPACE + "itemHistDel", vo);
	}

	/**
	 * 자재 브랜치 목록 조회
	 */
	public List<ItmBranchVO> itemBranchList(ItmBranchVO vo) {
		return sqlSelectForList(NAMESPACE + "itemBranchList", vo);
	}

	/**
	 * 자재 브랜치 저장 및 수정
	 */
	public void itemBranchSave(ItmBranchVO vo) {
		sqlInsert(NAMESPACE + "itemBranchSave", vo);
	}

	/**
	 * 자재 브랜치 삭제
	 */
	public void itemBranchDel(ItmBranchVO vo) {
		sqlDelete(NAMESPACE + "itemBranchDel", vo);
	}

	/**
	 * 자재 UOM 목록 조회
	 */
	public List<ItmUomVO> itemUomList(ItmUomVO vo) {
		return sqlSelectForList(NAMESPACE + "itemUomList", vo);
	}

	/**
	 * 자재 UOM 저장 및 수정
	 */
	public void itemUomSave(ItmUomVO vo) {
		sqlInsert(NAMESPACE + "itemUomSave", vo);
	}

	/**
	 * 자재 UOM 삭제
	 */
	public void itemUomDel(ItmUomVO vo) {
		sqlDelete(NAMESPACE + "itemUomDel", vo);
	}

	/**
	 * 아이템 LOT 저장 및 수정
	 */
	public void itemLotSave(ItmLotVO vo) {
		sqlInsert(NAMESPACE + "itemLotSave", vo);
	}

	/**
	 * 아이템 CATEGORY 상세 보기
	 */
	public ItmCategoryVO itemCategoryView(ItmCategoryVO vo) {
		return (ItmCategoryVO)sqlSelectForObject(NAMESPACE + "itemCategoryView", vo);
	}

	/**
	 * 아이템 CATEGORY 저장 및 수정
	 */
	public void itemCategorySave(ItmCategoryVO vo) {
		sqlInsert(NAMESPACE + "itemCategorySave", vo);
	}

	/**
	 * BranchCodeMaster 조회
	 * @param code
	 * @return
	 */
	public List<BranchCdMasterVO> getBranchCdMaster(BranchCdMasterVO brchCode) {
		return sqlSelectForList(NAMESPACE + "getBranchCdMaster", brchCode);
	}

	/**
	 * 품목 전체 리스트
	 */
	public ItmVO selectTotalItm(ItmVO vo) {
		return (ItmVO) sqlSelectForObject(NAMESPACE + "selectTotalItm", vo);
	}

	/**
	 * 아이템 부서 리스트
	 */
	public List<ItmSourcingPdeptVO> getItemSourcingPdeptList() {
		return sqlSelectForList(NAMESPACE + "getItemSourcingPdeptList");
	}

	/**
	 * 회사, BU, 사업장, 부서 코드로 품목 소싱 부 테이블에 존재여부 확인
	 * @param vo
	 * @return
	 */
	public Boolean getItemSourcingPdeptCheck(ItmSourcingPdeptVO vo) {
		sqlSelectForObject(NAMESPACE + "getItemSourcingPdeptCheck", vo);

		return vo.getExistFlag();
	}

	/**
	 * 아이템 부서 저장
	 */
	public void saveItemSourcingPdept(ItmSourcingPdeptVO vo) {
		sqlInsert(NAMESPACE + "saveItemSourcingPdept", vo);
	}

	/**
	 * 아이템 부서 삭제
	 */
	public void deleteItemSourcingPdept(ItmSourcingPdeptVO vo) {
		sqlDelete(NAMESPACE + "deleteItemSourcingPdept", vo);
	}

	/**
	 * 아이템 분류 트리 조회
	 */
	public List<ItmMappingCategoryVO> getItemMappingCategoryTree(ItmSourcingPdeptVO vo) {
		return sqlSelectForList(NAMESPACE + "getItemMappingCategoryTree", vo);
	}

	/**
	 * 아이템 분류 트리 저장
	 */
	public void saveItemMappingCategoryTree(ItmMappingCategoryVO vo) {
		sqlInsert(NAMESPACE + "saveItemMappingCategoryTree", vo);
	}

	/**
	 * 아이템 분류 트리 삭제
	 */
	public void deleteItemMappingCategoryTree(ItmMappingCategoryVO vo) {
		sqlDelete(NAMESPACE + "deleteItemMappingCategoryTree", vo);
	}

	/**
	 * 아이템 분류 조회
	 */
	public List<ItmMappingCategoryVO> getItemMappingCategoryList(ItmMappingCategoryVO vo) {
		return sqlSelectForList(NAMESPACE + "getItemMappingCategoryList", vo);
	}

	/**
	 * 아이템 매핑 조회
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getItemMappingList(ItmMappingVO vo) {
		return sqlSelectForListOfMap(NAMESPACE + "getItemMappingList", vo);
	}

	/**
	 * 아이템 매핑 저장
	 */
	public void saveItemMapping(ItmMappingVO vo) {
		sqlInsert(NAMESPACE + "saveItemMapping", vo);
	}

	/**
	 * 아이템 매핑 삭제
	 */
	public void deleteItemMapping(ItmMappingVO vo) {
		sqlDelete(NAMESPACE + "deleteItemMapping", vo);
	}

	/**
	 * 아이템 분류 매핑 테이블에서 품목 매핑 관련 키 조회
	 */
	public ItmCategoryMappingVO getItemCategoryMappingTreeIdSelect(ItmMappingCategoryVO vo) {
		return (ItmCategoryMappingVO) sqlSelectForObject(NAMESPACE + "getItemCategoryMappingTreeIdSelect", vo);
	}

	/**
	 * 아이템 분류 리스트 트리 조회
	 */
	public List<ItmMappingCategoryVO> getItemSessionCategoryTree(ItmSourcingPdeptVO vo) {
		return sqlSelectForList(NAMESPACE + "getItemSessionCategoryTree", vo);
	}

	/**
	 * 아이템 분류 리스트 검색 조회
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getItemMappingVitemSearch(HashMap<String, Object> keyword) {
		return sqlSelectForListOfMap(NAMESPACE + "getItemMappingVitemSearch", keyword);
	}

	/**
	 * 아이템 프로필 자동 조회
	 */
	public List<Map<String, Object>> getItemProfileList() {
		return sqlSelectForList(NAMESPACE + "getItemProfileList");
	}

	/**
	 * 아이템 프로필 상세 조회
	 */
	public ItmProfileVO getItemProfileView(Integer id) {
		return (ItmProfileVO) sqlSelectForObject(NAMESPACE + "getItemProfileView", id);
	}

	/**
	 * 아이템 프로필 저장
	 */
	public void saveItemProfile(ItmProfileVO vo) {
		sqlInsert(NAMESPACE + "saveItemProfile", vo);
	}

	/**
	 * 아이템 Profile에서 품목 정보 조회
	 */
	public List<Map<String, Object>> getItemProfileInfoList(ItmProfileVO vo) {
		return sqlSelectForList(NAMESPACE + "getItemProfileInfoList", vo);
	}

	/**
	 * 품목 Master File에서 품목 조회
	 */
	public List<ItmMasterFileVO> getItemMasterFileList(Map<String, Object> param) {
		return sqlSelectForList(NAMESPACE + "getItemMasterFileList", param);
	}

	/**
	 * 품목 Master File에서 시장구매전략 목록 조회
	 */
	public List<ItmMasterFileContentVO> getItemMasterFileContentList(ItmMasterFileVO vo) {
		return sqlSelectForList(NAMESPACE + "getItemMasterFileContentList", vo);
	}

	/**
	 * 품목 Master File에서 시장구매전략 저장 및 수정
	 */
	public void saveItemMasterMarketBuyPlan(ItmMasterFileContentVO vo) {
		sqlInsert(NAMESPACE + "saveItemMasterMarketBuyPlan", vo);
	}

	/**
	 * 품목 Master File에서 시장구매전략 삭제
	 */
	public void deleteItemMasterMarketBuyPlan(ItmMasterFileContentVO vo) {
		sqlDelete(NAMESPACE + "deleteItemMasterMarketBuyPlan", vo);
	}

	/**
	 * 품목 Master File에서 상세 목록 조회
	 */
	public List<ItmMasterFileDetailVO> getItemMasterFileDetailList(ItmMasterFileDetailVO vo) {
		return sqlSelectForList(NAMESPACE + "getItemMasterFileDetailList", vo);
	}

	public List<ItmMasterFileDetailVO> getItemMasterFileDetailList2(ItmMasterFileDetailVO vo) {
		return sqlSelectForList(NAMESPACE + "getItemMasterFileDetailList2", vo);
	}

	/**
	 * 품목 Master File에서 상세 저장 및 수정
	 */
	public void saveItemMasterFileDetail(ItmMasterFileDetailVO vo) {
		sqlInsert(NAMESPACE + "saveItemMasterFileDetail", vo);
	}

	/**
	 * 품목 Master File에서 상세 삭제
	 */
	public void deleteItemMasterFileDetail(ItmMasterFileDetailVO vo) {
		sqlDelete(NAMESPACE + "deleteItemMasterFileDetail", vo);
	}

	/**
	 * 품목 Master File에서 상세 조회
	 */
	public ItmMasterFileDetailVO getItemMasterFileDetailView(ItmMasterFileDetailVO vo) {
		return (ItmMasterFileDetailVO) sqlSelectForObject(NAMESPACE + "getItemMasterFileDetailView", vo);
	}

	/*## 포뮬라(formula) 자재 관리 */
	/**
	 * 포뮬라 자제 정보 를 가져옴
	 * @param VO
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> selectFormula(ItmFormulaVO vo){
		return sqlSelectForListOfMap(NAMESPACE + "SP_FORMULA_SELECT" , vo) ;
	}

	/**
	 * 단일 로우 포뮬라 자제 정보 를 가져옴
	 * @param VO
	 * @return
	 */
	public ItmFormulaVO viewFormula(ItmFormulaVO vo){
		return (ItmFormulaVO)sqlSelectForObject(NAMESPACE + "SP_FORMULA_VIEW" , vo) ;
	}

	/**
	 * 포뮬라 자제 정보를 저장함
	 * @param vo
	 * @return
	 */
	public Object saveFormula(ItmFormulaVO vo){
		return sqlInsert(NAMESPACE + "SP_FORMULA_SAVE", vo);
	}

	/**
	 * 포뮬라 자제 정보를 삭제함
	 * @param vo
	 * @return
	 */
	public boolean delFormula(ItmFormulaVO vo){
		return sqlDelete(NAMESPACE + "SP_FORMULA_DELETE", vo) >= 1 ;
	}
	/*## 포뮬라(formula) 자재 관리 */

	/**==========================**/
	/**==========================**/
	/**==========================**/
	/**
	 * 아이템 마스터 조회
	 * @param vo
	 * @return
	 */
	public List<ItmMasterVO> itmMasterSelect(ItmMasterVO vo) {
		return sqlSelectForList(NAMESPACE + "V_ITM_GROUP_SELECT" , vo) ;
	}

	/**
	 *아이쳄 블런치 조회
	 * @param vo
	 * @return
	 */
	public List<ItmMasterVO> itmBranchSelect(ItmMasterVO vo) {
		return sqlSelectForList(NAMESPACE + "V_ITM_BRANCH_SELECT" , vo) ;
	}

	/**
	 * 아이템 조회
	 * @param vo
	 * @return
	 */
	public List<ItmMasterVO> itmSelect(ItmMasterVO vo) {
		return sqlSelectForList(NAMESPACE + "V_ITM_SELECT" , vo) ;
	}

	/**
	 * 아이템 저장
	 * @param vo
	 * @return
	 */
	public boolean saveItm(ItmMasterVO vo) {
		LOG.debug("DAO 실행 ");
		sqlInsert(NAMESPACE + "SP_ITM_SAVE_NEW_INSERT", vo);
		return true;
	}


	public boolean saveNoneItm(ItmMasterVO vo) {
		LOG.debug("DAO 실행 ");
		sqlInsert(NAMESPACE + "SP_ITM_NONE_SAVE_NEW_INSERT", vo);
		return true;
	}

	/**
	 * 아이템 마스터 파일 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> selectItmMasterFile(Map<String, Object> param) {
		return sqlSelectForList(NAMESPACE + "SP_ITM_MASTER_FILE_SELECT", param);
	}

	/**
	 * 아이템 상위 분류 조회
	 * @param param
	 * @return
	 */
	public Map<String, Object> selectItmTopGubn(Map<String, Object> param) {
		return (Map<String, Object>) sqlSelectForObject(NAMESPACE + "SP_ITM_TOP_GUBN_SELECT", param);
	}

	/**
	 * 품목팝업 저장
	 * @param param
	 * @return
	 */
	public void saveWonbuItem(Map<String, Object> param) {
		sqlInsert(NAMESPACE + "SP_ITM_WONBU_SAVE", param);
	}

	/**
	 * 아이템 팝업 용도, 비고 조회
	 * @param vo
	 * @return
	 */
	public Map<String, Object> selectItmWonbuUser(Map<String, Object> param) {
		return (Map<String, Object>) sqlSelectForObject(NAMESPACE + "SP_ITM_WONBU_USER_SELECT" , param);
	}

	/**
	 * 원부 카테고리1 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> selectWonbuCategory1(Map<String, Object> param) {
		return sqlSelectForList(NAMESPACE + "SP_WONBU_CATEGORY1_SELECT", param);
	}

	/**
	 * 원부 카테고리2 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> selectWonbuCategory2(Map<String, Object> param) {
		return sqlSelectForList(NAMESPACE + "SP_WONBU_CATEGORY2_SELECT", param);
	}

	/**
	 * 원부 카테고리3 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> selectWonbuCategory3(Map<String, Object> param) {
		return sqlSelectForList(NAMESPACE + "SP_WONBU_CATEGORY3_SELECT", param);
	}

	/**
	 * 원부 카테고리 보기
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> SP_WONBU_CATEGORY(Map<String, Object> param) {
		return sqlSelectForList(NAMESPACE + "SP_WONBU_CATEGORY" , param) ;
	}

	/**
	 * 원부 카테고리 아이템
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> SP_WONBU_CATEGORY_ITM(Map<String,Object> param){
		return sqlSelectForList(NAMESPACE + "SP_WONBU_CATEGORY_ITM" , param) ;
	}

	/**
	 * 원부 카테고리 아이템 2
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> SP_WONBU_CATEGORY_ITM2(Map<String,Object> param){
		return sqlSelectForList(NAMESPACE + "SP_WONBU_CATEGORY_ITM2" , param) ;
	}

	/**
	 * 원부 카테고리 저장
	 * @param param
	 */
	public void SP_WONBU_CATEGORY3_SAVE(ItmWonbuVO param) {
		sqlInsert(NAMESPACE + "SP_WONBU_CATEGORY3_SAVE", param);
	}

	public void SP_WONBU_CATEGORY2_SAVE(ItmWonbuVO param) {
		sqlInsert(NAMESPACE + "SP_WONBU_CATEGORY2_SAVE", param);
	}

	public void SP_WONBU_CATEGORY_ITM_UPDATE(Map<String,Object> param) {
		sqlInsert(NAMESPACE + "SP_WONBU_CATEGORY_ITM_UPDATE", param);
	}

	/**
	 * 원부 아이템
	 * @param param
	 * @return
	 */
	public Map<String,Object> SP_ITM_NEW_VIEW_COUNT(Map<String,Object> param){
		return (Map<String, Object>) sqlSelectForObject(NAMESPACE + "SP_ITM_NEW_VIEW_COUNT" , param) ;
	}

	/**
	 * 원부 아이템
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> SP_ITM_NEW_VIEW(Map<String,Object> param){
		return sqlSelectForList(NAMESPACE + "SP_ITM_NEW_VIEW" , param) ;
	}

	/**
	 * 원부 아이템 View
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> SP_ITM_NEW_BU_VIEW (Map<String,Object> param){
		return     sqlSelectForList(NAMESPACE + "SP_ITM_NEW_BU_VIEW" , param) ;
	}

//	public List<ItmVO> itmErpAddSync(ItmVO vo){
	public List<Map<String,Object>> itmErpAddSync (Map<String,Object> param){
		return sqlSelectForList(NAMESPACE + "SP_ITM_ERP_ADD_SYNC" , param);
	}

	public List<Map<String,Object>> PROC_SELECT_MAP (Map<String,Object> param , String query){
		return sqlSelectForList(NAMESPACE + query ,  param);
	}

	public Object PROC_INSERT (Map<String,Object> param , String query){
		return sqlInsert(NAMESPACE + query ,  param);
	}

	public boolean PROC_UPDATE (Map<String,Object> param , String query){
		return sqlUpdate(NAMESPACE + query ,  param) >= 1 ;
	}

	public boolean PROC_DELETE (Map<String,Object> param , String query){
		return sqlDelete(NAMESPACE + query ,  param) >= 1;
	}

	public List<CodeMasterVO> getItmTypeList(Map<String, String> itmTypeMap) {
		return sqlSelectForList(NAMESPACE + "SP_ITM_TYPE_SELECT" ,  itmTypeMap);
	}

	/**
	 * 품목 목록 조회
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getItems(Map<String, Object> params) {
		return sqlSelectForList(NAMESPACE + "SP_ITEM_LIST", params);
	}

	/**
	 * 계약품 등록
	 * @param param
	 * @return
	 */
	public Object saveContItm(Map<String, Object> param) {
		return sqlInsert(NAMESPACE + "SP_CONT_ITEM_SAVE", param);
	}

	public List<Map<String, Object>> selectJdeErpItm(Map<String, Object> param) {
		return (List<Map<String, Object>>)sqlSelectForList(NAMESPACE + "selectJdeErpItm", param);
	}
	
	/**
	 * 투자유형 카테고리 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> selectInvType(Map<String, Object> param) {
		return sqlSelectForList(NAMESPACE + "SP_INVTYPE_SELECT", param);
	}
}