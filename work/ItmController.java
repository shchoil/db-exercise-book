package com.syds.tops.itm.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.syds.basic.common.util.CommonUtils;
import com.syds.basic.common.util.StringUtil;
import com.syds.basic.util.security.SecurityUtil;
import com.syds.tops.common.service.CodeHelperService;
import com.syds.tops.common.util.PageUtil;
import com.syds.tops.common.util.ScriptUtil;
import com.syds.tops.common.vo.DeptVO;
import com.syds.tops.common.web.PmsBaseController;
import com.syds.tops.itm.service.ItmService;
import com.syds.tops.itm.vo.ItmBasicsVO;
import com.syds.tops.itm.vo.ItmBranchVO;
import com.syds.tops.itm.vo.ItmCategoryVO;
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
import com.syds.tops.sys.service.GlClassService;
import com.syds.tops.sys.vo.BranchCdMasterVO;
import com.syds.tops.sys.vo.CodeMasterVO;
import com.syds.tops.sys.vo.FiorgDeptVO;
import com.syds.tops.sys.vo.GlClassVO;
import com.syds.tops.sys.vo.ParameterWrapper;
import com.syds.tops.sys.vo.ParameterWrapper2;
import com.syds.tops.sys.vo.ParameterWrapper3;
import com.syds.tops.sys.vo.UserMasterVO;

/**
 * @author SYC719233
 * @date : 2017. 4. 11. 오후 6:20:43
 * @desc : 자재 컨트롤러
 */
@Controller
@RequestMapping(value="/buy/itm")
public class ItmController extends PmsBaseController {
	Logger LOG = Logger.getLogger(ItmController.class);

	@Resource
	private ItmService itmService;

	@Resource
	CodeHelperService codeHelperService;

	@Autowired
	ScriptUtil scriptUtil;

	@Resource(name="glClassService")
	private GlClassService glClassService;

	/*
	 *	품목 리스트 : 처음
	 */
	@RequestMapping(value="/itm_list_x.do")
	public String initList(Model model , ItmMasterVO itmMasterVO) {
		FiorgDeptVO fiorgDeptVO = PageUtil.getFiorgDeptInfo();

		// 회사와 BU셋팅
		model.addAttribute("useCompList", codeHelperService.getFiorgDeptData("1", fiorgDeptVO.getCompTopsOrgCd()));
		model.addAttribute("useBuList", codeHelperService.getFiorgDeptData("2", fiorgDeptVO.getBuUpOrgCd()));
		model.addAttribute("itmMasterVO", itmMasterVO) ;

		return "/buy/itm/itm_list_x";
	}

	/*
	 *	품목 리스트 : 기본정보 탭
	 */
	@RequestMapping(value="/basic.do")
	public String baseTab() {
		return "/main/itm/join/itm_basic_x";
	}

	/*
	 *	품목 리스트 : 아이템 저장 팝업
	 */
	@RequestMapping(value="/itm_save_p.do")
	public ModelAndView saveItmPopup(ModelAndView mv
									, @RequestParam(value="mode" , required=false) String mode
									, ItmMasterVO itmMasterVO) {

		//## 자신이 포함된 회사의 BP 정보  값
		FiorgDeptVO fiorgDeptVO  = PageUtil.getFiorgDeptInfo();
		DeptVO deptVO = new DeptVO() ;
		deptVO.setCompErpCd(fiorgDeptVO.getCompTopsOrgCd());

		JSONObject jsonBranchObj = new JSONObject() ;
		jsonBranchObj.put("Data", itmService.selectDept(deptVO)) ;

		//## 공통코드
		mv.addObject("STOCKING_TYPE"     		  , getComonCode("STOCKING_TYPE" 			 , fiorgDeptVO) ) ;
		mv.addObject("BULK_PACKED_FG"    		  , getComonCode("BULK_PACKED_FG" 			 , fiorgDeptVO) ) ;
		mv.addObject("INVENTORY_COST_LV" 		  , getComonCode("INVENTORY_COST_LV" 		 , fiorgDeptVO) ) ;
		mv.addObject("SALES_PRICE_LV" 	 		  , getComonCode("SALES_PRICE_LV" 			 , fiorgDeptVO) ) ;
		mv.addObject("PURCHASE_PRICE_LV" 		  , getComonCode("PURCHASE_PRICE_LV" 		 , fiorgDeptVO) ) ;
		mv.addObject("COMMITMENT_MD"     		  , getComonCode("COMMITMENT_MD" 			 , fiorgDeptVO) ) ;
		mv.addObject("LOT_STATUS_CD"     	      , getComonCode("LOT_STATUS_CD" 			 , fiorgDeptVO) ) ;
		mv.addObject("LOT_PROCESS_TYPE"  		  , getComonCode("LOT_PROCESS_TYPE" 		 , fiorgDeptVO) ) ;
		mv.addObject("SHELF_LIFE_DAYS"   		  , getComonCode("SHELF_LIFE_DAYS" 			 , fiorgDeptVO) ) ;

		mv.addObject("SALES_CATALOG_SECT"         , getComonCode("SALES_CATALOG_SECT" 		 , fiorgDeptVO) ) ;
		mv.addObject("COMMODITY_CLASS"  	      , getComonCode("COMMODITY_CLASS" 			 , fiorgDeptVO) ) ;
		mv.addObject("COMMODITY_SUB_CLASS"    	  , getComonCode("COMMODITY_SUB_CLASS" 		 , fiorgDeptVO) ) ;
		mv.addObject("MASTER_PLANNING_FAMILY"  	  , getComonCode("MASTER_PLANNING_FAMILY" 	 , fiorgDeptVO) ) ;
		mv.addObject("SHIPPING_COMMONDITY_CLASS"  , getComonCode("SHIPPING_COMMONDITY_CLASS" , fiorgDeptVO) ) ;
		mv.addObject("WAREHOUSE_PROCESS_GRP3"     , getComonCode("WAREHOUSE_PROCESS_GRP3" 	 , fiorgDeptVO) ) ;
		mv.addObject("ORDER_POLICY_CD"     		  , getComonCode("ORDER_POLICY_CD" 			 , fiorgDeptVO) ) ;
		mv.addObject("ISSUE_TYPE_CD"     		  , getComonCode("ISSUE_TYPE_CD" 			 , fiorgDeptVO) ) ;
		mv.addObject("PLANNING_CD"     		  	  , getComonCode("PLANNING_CD" 			     , fiorgDeptVO) ) ;
		mv.addObject("PLANNING_FENCE_RULE"     	  , getComonCode("PLANNING_FENCE_RULE" 		 , fiorgDeptVO) ) ;
		mv.addObject("FIXED_VARIABLE"     	  	  , getComonCode("FIXED_VARIABLE" 			 , fiorgDeptVO) ) ;
		mv.addObject("LINE_TYPE"     	  	  	  , getComonCode("LINE_TYPE") ) ;
		mv.addObject("UOM"     	  	  	  		  , getComonCode("UOM") ) ;
		GlClassVO glClassVO = new GlClassVO() ;
		glClassVO.setCompCd(fiorgDeptVO.getCompTopsOrgCd()) ;
		mv.addObject("GL_CLASS"     	  	  	  ,  glClassService.getGlClassList(glClassVO) ) ;

		//초기값 세팅
		itmMasterVO.setLineType("S");
		itmMasterVO.setBulkPackedFg("P");
		itmMasterVO.setStockingType("P");
		itmMasterVO.setInventoryCostLv("2");
		itmMasterVO.setSalesPriceLv("1");
		itmMasterVO.setPurchasePriceLv("3");
		itmMasterVO.setUom("KG");
		itmMasterVO.setUomPurchasing("KG");

		mv.addObject("itmMasterVO" , itmMasterVO) ;// itemRequest
		mv.addObject("jsonBranchObj", jsonBranchObj) ;	 //Bp 정보
		mv.setViewName("/buy/itm/itm_save_p") ;

		return mv;
	}


	@RequestMapping(value="/itm_branch_save_p.do")
	public ModelAndView saveItmBranchPopup(ModelAndView mv
									, @RequestParam(value="mode" , required=false) String mode
									, ItmMasterVO itmMasterVO) {

		//## 자신이 포함된 회사의 BP 정보  값
		FiorgDeptVO fiorgDeptVO  = PageUtil.getFiorgDeptInfo();
		DeptVO deptVO = new DeptVO() ;
		deptVO.setCompErpCd(fiorgDeptVO.getCompTopsOrgCd());

		JSONObject jsonBranchObj = new JSONObject() ;
		jsonBranchObj.put("Data", itmService.selectDept(deptVO)) ;

		//## 저장된 BP 정보
		JSONObject jsonSaveBranchObj = new JSONObject() ;
		itmMasterVO.setCompCd(fiorgDeptVO.getCompTopsOrgCd());
		jsonSaveBranchObj.put("Data", itmService.selectItmBranchListNew(itmMasterVO)) ;

		//## 저장된 Master 코드 정보

		//## 공통코드
		mv.addObject("STOCKING_TYPE"     		  , getComonCode("STOCKING_TYPE" 			 , fiorgDeptVO) ) ;
		mv.addObject("BULK_PACKED_FG"    		  , getComonCode("BULK_PACKED_FG" 			 , fiorgDeptVO) ) ;
		mv.addObject("INVENTORY_COST_LV" 		  , getComonCode("INVENTORY_COST_LV" 		 , fiorgDeptVO) ) ;
		mv.addObject("SALES_PRICE_LV" 	 		  , getComonCode("SALES_PRICE_LV" 			 , fiorgDeptVO) ) ;
		mv.addObject("PURCHASE_PRICE_LV" 		  , getComonCode("PURCHASE_PRICE_LV" 		 , fiorgDeptVO) ) ;
		mv.addObject("COMMITMENT_MD"     		  , getComonCode("COMMITMENT_MD" 			 , fiorgDeptVO) ) ;
		mv.addObject("LOT_STATUS_CD"     	      , getComonCode("LOT_STATUS_CD" 			 , fiorgDeptVO) ) ;
		mv.addObject("LOT_PROCESS_TYPE"  		  , getComonCode("LOT_PROCESS_TYPE" 		 , fiorgDeptVO) ) ;
		mv.addObject("SHELF_LIFE_DAYS"   		  , getComonCode("SHELF_LIFE_DAYS" 			 , fiorgDeptVO) ) ;

		mv.addObject("SALES_CATALOG_SECT"         , getComonCode("SALES_CATALOG_SECT" 		 , fiorgDeptVO) ) ;
		mv.addObject("COMMODITY_CLASS"  	      , getComonCode("COMMODITY_CLASS" 			 , fiorgDeptVO) ) ;
		mv.addObject("COMMODITY_SUB_CLASS"    	  , getComonCode("COMMODITY_SUB_CLASS" 		 , fiorgDeptVO) ) ;
		mv.addObject("MASTER_PLANNING_FAMILY"  	  , getComonCode("MASTER_PLANNING_FAMILY" 	 , fiorgDeptVO) ) ;
		mv.addObject("SHIPPING_COMMONDITY_CLASS"  , getComonCode("SHIPPING_COMMONDITY_CLASS" , fiorgDeptVO) ) ;
		mv.addObject("WAREHOUSE_PROCESS_GRP3"     , getComonCode("WAREHOUSE_PROCESS_GRP3" 	 , fiorgDeptVO) ) ;
		mv.addObject("ORDER_POLICY_CD"     		  , getComonCode("ORDER_POLICY_CD" 			 , fiorgDeptVO) ) ;
		mv.addObject("ISSUE_TYPE_CD"     		  , getComonCode("ISSUE_TYPE_CD" 			 , fiorgDeptVO) ) ;
		mv.addObject("PLANNING_CD"     		  	  , getComonCode("PLANNING_CD" 			     , fiorgDeptVO) ) ;
		mv.addObject("PLANNING_FENCE_RULE"     	  , getComonCode("PLANNING_FENCE_RULE" 		 , fiorgDeptVO) ) ;
		mv.addObject("FIXED_VARIABLE"     	  	  , getComonCode("FIXED_VARIABLE" 			 , fiorgDeptVO) ) ;
		mv.addObject("LINE_TYPE"     	  	  	  , getComonCode("LINE_TYPE") ) ;
		mv.addObject("itmMasterVO" 		 	      , itmMasterVO) ;// itemRequest
		mv.addObject("jsonBranchObj"	 		  , jsonBranchObj) ;	 //Bp 정보
		mv.addObject("jsonSaveBranchObj"          , jsonSaveBranchObj) ; //SaveBp 정보
		GlClassVO glClassVO = new GlClassVO() ;
		glClassVO.setCompCd(fiorgDeptVO.getCompTopsOrgCd()) ;
		mv.addObject("GL_CLASS"     	  	  	  ,  glClassService.getGlClassList(glClassVO) ) ;
		mv.addObject("UOM"     	  	  	  		  , getComonCode("UOM") ) ;
		mv.addObject("mode" 		              , "branch") ; //Branch 모드

		mv.setViewName("/buy/itm/itm_save_p") ;
		return mv;
	}
	/**
	 * 아이템을 저장한다.
	 * @param mv
	 * @param itmMasterVO
	 * @return
	 */
	@RequestMapping(value="/itm_save.do")
	public ModelAndView saveItm(ModelAndView mv , ItmMasterVO itmMasterVO)throws Exception {
	   LOG.debug(">> request 값 : " + itmMasterVO.toString()) ;

	   //, 제거 나중에. 자료형 변경으로 . 변경
	   itmMasterVO.setUnitPrice(StringUtil.removeNumPoint(itmMasterVO.getUnitPrice())) ;
	   itmMasterVO.setShelfLifeDays(StringUtil.removeNumPoint(itmMasterVO.getShelfLifeDays())) ;
	   itmMasterVO.setUnitPrice(StringUtil.removeNumPoint(itmMasterVO.getUnitPrice())) ;
	   itmMasterVO.setUnitPrice(StringUtil.removeNumPoint(itmMasterVO.getUnitPrice())) ;
	   itmMasterVO.setUnitPrice(StringUtil.removeNumPoint(itmMasterVO.getUnitPrice())) ;
	   itmMasterVO.setPlanningFence( StringUtil.removeNumPoint(itmMasterVO.getPlanningFence()) ) ;
	   itmMasterVO.setFreezeFence( StringUtil.removeNumPoint(itmMasterVO.getFreezeFence()) ) ;
	   itmMasterVO.setLeadtimeLevel( StringUtil.removeNumPoint(itmMasterVO.getLeadtimeLevel()) ) ;
	   itmMasterVO.setMultipleOrderQuantity( StringUtil.removeNumPoint(itmMasterVO.getMultipleOrderQuantity()) ) ;
	   itmMasterVO.setUnitsPerContainer( StringUtil.removeNumPoint(itmMasterVO.getUnitsPerContainer()) ) ;
	   itmMasterVO.setSafetyStock( StringUtil.removeNumPoint(itmMasterVO.getSafetyStock()) ) ;
	   itmMasterVO.setValueOrderPolicy( StringUtil.removeNumPoint(itmMasterVO.getValueOrderPolicy()) ) ;

	   //키만들기
	   java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyMMdd") ;
	   java.util.Calendar cal = Calendar.getInstance() ;
	   String dayKey = sdf.format(cal.getTime()) ;

	   //초기화
	   FiorgDeptVO fiorgDeptVO  = PageUtil.getFiorgDeptInfo();
	   itmMasterVO.setCompCd(fiorgDeptVO.getCompTopsOrgCd()) ;
	   itmMasterVO.setItmId(dayKey);
	   itmMasterVO.setItmNo(fiorgDeptVO.getCompTopsOrgCd() + "-" + dayKey) ;
	   itmMasterVO.setConstYn("N") ;
	   //itmMasterVO.setStockingType("S") ;

	   String src = "/buy/itm/itm_save.do" ;
	   if(itmService.saveItm(itmMasterVO)) {
			scriptUtil.doSelfCloseAndOpenerReload(mv,  "저장 되었습니다."  , true);
	   } else{
			scriptUtil.doAlertToHistoryBack(mv, "저장  실패 ");
	   }

	   return mv ;
	}

	/**
	 * 아이템 Stock 리스트 가져옴 json
	 * @param params
	 * @return
	 */
	@RequestMapping(value="itm_list_new.do")
	public @ResponseBody Object itemListNew(@RequestBody ParameterWrapper<ItmMasterVO> params) {
		ItmMasterVO reqVO = params.param  ;
		FiorgDeptVO fiorgDeptVO  = PageUtil.getFiorgDeptInfo();
		reqVO.setCompCd(fiorgDeptVO.getCompTopsOrgCd());
		reqVO.setLineType("S");

		return PageUtil.getJsonResult(itmService.selectItmMasterListNew(reqVO));
	}

	/**
	 * 아이템 Branch 조회 json
	 * @param params
	 * @return
	 */
	@RequestMapping(value="itm_list_branch_list.do")
	public @ResponseBody Object itemListBranchNew(@RequestBody ParameterWrapper<ItmMasterVO> params) {
		ItmMasterVO reqVO = params.param  ;
		FiorgDeptVO fiorgDeptVO  = PageUtil.getFiorgDeptInfo();
		reqVO.setComp(fiorgDeptVO.getCompTopsOrgCd());

		return PageUtil.getJsonResult(itmService.selectItmBranchListNew(reqVO));
	}

	@RequestMapping(value="itm_view.do")
	public ModelAndView itmView(ModelAndView mv , ItmMasterVO reqVO) {
		FiorgDeptVO fiorgDeptVO  = PageUtil.getFiorgDeptInfo();
		//reqVO.setComp(fiorgDeptVO.getCompTopsOrgCd());
		reqVO.setCompCd(fiorgDeptVO.getCompTopsOrgCd());

		ItmMasterVO rstItmMasterVO = null ;
		if(reqVO.getItmMasterSeq() != null && !"".equals(reqVO.getItmMasterSeq())) {
			List<ItmMasterVO> rowList = itmService.selectItemListNew(reqVO) ;
			if(rowList != null) {
				for (ItmMasterVO rowVO : rowList) {
					rstItmMasterVO = rowVO ;
				}
			}
		}

		reqVO.setUom("KG");
		reqVO.setUomPurchasing("KG");

		mv.addObject("viewVO" , rstItmMasterVO) ;
		mv.setViewName("/buy/itm/itm_view_x");
		return mv ;
	}

	/**==========================**/
	/* E:품목 관리(Stock)*/
	/**==========================**/



	/**==========================**/
	/* S:품목 관리(None-Stock*/
	/**==========================**/

	/**
	 * 품목 관리(None-Stock)
	 * @param mv
	 * @param reqVO
	 * @return
	 */
	@RequestMapping(value="itm_none_list_x.do")
	public ModelAndView itmNoneList(ModelAndView mv, ItmMasterVO reqVO) {
		mv.setViewName("/buy/itm/itm_none_list_x");
		return mv ;
	}

	/**
	 * None Stock List 값을 조회 한다.
	 * @param params
	 * @return
	 */
	@RequestMapping(value="itm_none_list_json.do")
	public @ResponseBody Object itmNoneListJson(@RequestBody ParameterWrapper<ItmMasterVO> params) {
		ItmMasterVO reqVO = params.param  ;
		FiorgDeptVO fiorgDeptVO  = PageUtil.getFiorgDeptInfo();
		reqVO.setCompCd(fiorgDeptVO.getCompTopsOrgCd());

		reqVO.setStockingType("N");

		return PageUtil.getJsonResult(itmService.selectItemListNew(reqVO)) ;
	}

	/**
	 * None Stock 아이템을 조회 한다.
	 * @param mv
	 * @param reqVO
	 * @return
	 */
	@RequestMapping(value="itm_none_save_p.do")
	public ModelAndView itmNoneSavePopup(ModelAndView mv , ItmMasterVO reqVO) {
		FiorgDeptVO fiorgDeptVO  = PageUtil.getFiorgDeptInfo();


		mv.addObject("LINE_TYPE"   , getComonCode("LINE_TYPE") ) ;
		GlClassVO glClassVO = new GlClassVO() ;
		glClassVO.setCompCd(fiorgDeptVO.getCompTopsOrgCd()) ;
		mv.addObject("GL_CLASS"     	  	  	  ,  glClassService.getGlClassList(glClassVO) ) ;
		mv.addObject("UOM"     	  	  	  		  , getComonCode("UOM") ) ;

		reqVO.setLineType("N");
		mv.addObject("itmMasterVO" , reqVO) ;// itemRequest

		mv.setViewName("/buy/itm/itm_none_save_p");
		return mv ;
	}

	/**
	 * None Stock 아이템을 저장 한다
	 * @param mv
	 * @param reqVO
	 * @return
	 */
	@RequestMapping(value="itm_none_save.do")
	public ModelAndView itmNoneSave(ModelAndView mv , ItmMasterVO reqVO) {
		  String src = "/buy/itm/itm_save.do" ;

		  //키만들기
		  java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyMMdd") ;
		  java.util.Calendar cal = Calendar.getInstance() ;
		  String dayKey = sdf.format(cal.getTime()) ;

		  //값 초기화
		  FiorgDeptVO fiorgDeptVO  = PageUtil.getFiorgDeptInfo();
		  UserMasterVO userMaster = SecurityUtil.getSignedUser().getUserMaster();
		  reqVO.setStockingType("N");
		  reqVO.setCompCd(fiorgDeptVO.compErpCd);
		  reqVO.setBranchPlant(fiorgDeptVO.dpOrgCd);
		  reqVO.setBranchPlantNm(fiorgDeptVO.dpNm);
		  reqVO.setInputEmpNo(userMaster.USER_ID);
		  reqVO.setItmId(dayKey);
		  reqVO.setItmNo(fiorgDeptVO.getCompTopsOrgCd() + "-" + dayKey) ;
		  reqVO.setConstYn("N") ;
		  reqVO.setUseYn("Y");

		  LOG.debug(">> none 아이템 저장 " + reqVO.toString() );
		  if(itmService.saveNoneItm(reqVO)) {
			  scriptUtil.doSelfCloaseAndOpenerCallback(mv,"저장 되었습니다.", "evt_reload()") ;
		  } else{
			  scriptUtil.doAlertToHistoryBack(mv, "저장  실패 ");
		  }

		  return mv ;
	}

	/**==========================**/
	/* E:품목 관리(None-Stock*/
	/**==========================**/


	/**==========================**/
	/**==========================**/
	/**==========================**/


	private List<CodeMasterVO> getComonCode(String codeNm , FiorgDeptVO fiorgDeptVO) {
		return codeHelperService.selectCodeMaster( (new CodeMasterVO()).setFind(codeNm , fiorgDeptVO.getCompTopsOrgCd()) )  ;
	}


	private List<CodeMasterVO> getComonCode(String codeNm ) {
		CodeMasterVO codeMasterVO = new CodeMasterVO() ;
		codeMasterVO.setCD_GUBN(codeNm) ;
		return codeHelperService.selectCodeMaster(codeMasterVO)   ;
	}




	/*
	 *	품목 리스트 : 자재목록 조회
	 */
	@RequestMapping(value="/list.do")
	public @ResponseBody Object itemSearchList(@RequestBody ParameterWrapper<HashMap<String, Object>> params) {
		return PageUtil.getJsonResult(itmService.getItemList(params.param));
	}

	/*
	 *	품목 리스트 : 자재 저장 및 수정
	 */
	@RequestMapping(value="/save.do")
	public @ResponseBody ItmBasicsVO itemSave(@RequestBody ParameterWrapper<HashMap<String, Object>> params) {
		return itmService.saveItem(params.param);
	}

	/*
	 *	품목 리스트 : 자재 Basics 저장 및 수정
	 */
	@RequestMapping(value="/basics/save.do")
	public @ResponseBody void itemBasicsSave(@RequestBody ParameterWrapper<ItmBasicsVO> params) {
		itmService.saveItemBasics(params.param);
	}

	/**
	 * 품목 리스트 : 자재 정보 조회
	 *
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/hist/list.do")
	public @ResponseBody Object itemHistList(@RequestBody ParameterWrapper<ItmHistVO> param) {
		return PageUtil.getJsonResult(itmService.getItemHistList(param.param));
	}

	/**
	 * 품목 리스트 : 자재 정보 등록 및 수정
	 */
	@RequestMapping(value="/hist/save.do")
	public @ResponseBody void itemHistSave(@RequestBody ParameterWrapper<List<ItmHistVO>> params) {
		itmService.saveItemHist(params.param);
	}

	/**
	 * 품목 리스트 : 자재 정보 삭제
	 */
	@RequestMapping(value="/hist/delete.do")
	public @ResponseBody void itemHistDel(@RequestBody ParameterWrapper<List<ItmHistVO>> params) {
		itmService.deleteItemHist(params.param);
	}

	/*
	 *	품목 리스트 : Branch 탭
	 */
	@RequestMapping(value="/branch.do")
	public String branchTab(Model model) {
		UserMasterVO userInfo = PageUtil.getUserInfo();

		model.addAttribute("tmpEmpNo", userInfo.USER_ID);

		return "/main/itm/join/itm_branch_x";
	}

	/*
	 *	품목 리스트 : Branch 팝업
	 */
	@RequestMapping(value="/itm_branch_p.do")
	public String branchPopup() {
		return "/buy/itm/itm_branch_p";
	}

	/*
	 *	품목 리스트 : Branch 목록
	 */
	@RequestMapping(value="/branch/list.do")
	public @ResponseBody Object itmBranchList(@RequestBody ParameterWrapper<ItmBranchVO> param) {
		return PageUtil.getJsonResult(itmService.getItemBranchList(param.param));
	}

	/*
	 *	품목 리스트 : Branch 저장 및 수정
	 */
	@RequestMapping(value="/branch/save.do")
	public @ResponseBody void itemBranchSave(@RequestBody ParameterWrapper<List<ItmBranchVO>> params) {
		itmService.saveItemBranch(params.param);
	}

	/*
	 *	품목 리스트 : Branch 삭제
	 */
	@RequestMapping(value="/branch/delete.do")
	public @ResponseBody void ItmBranchDel(@RequestBody ParameterWrapper<List<ItmBranchVO>> params) {
		itmService.deleteItemBranch(params.param);
	}

	/*
	 *	품목 리스트 : UOM 탭
	 */
	@RequestMapping(value="/uom.do")
	public String uomTab() {
		return "/main/itm/join/itm_uom_x";
	}

	/*
	 *	품목 리스트 : UOM 목록
	 */
	@RequestMapping(value="/uom/list.do")
	public @ResponseBody Object itemUomList(@RequestBody ParameterWrapper<ItmUomVO> param) {
		return PageUtil.getJsonResult(itmService.getItemUomList(param.param));
	}

	/*
	 *	품목 리스트 : UOM 저장 및 수정
	 */
	@RequestMapping(value="/uom/save.do")
	public @ResponseBody void itemUomSave(@RequestBody ParameterWrapper<List<ItmUomVO>> params) {
		itmService.saveItemUom(params.param);
	}

	/*
	 *	품목 리스트 : UOM 삭제
	 */
	@RequestMapping(value="/uom/delete.do")
	public @ResponseBody void itemUomDel(@RequestBody ParameterWrapper<List<ItmUomVO>> params) {
		itmService.deleteItemUom(params.param);
	}

	/*
	 *	품목 리스트 : LOT 탭
	 */
	@RequestMapping(value="/lot.do")
	public String lotTab() {
		return "/main/itm/join/itm_lot_x";
	}

	/*
	 *	품목 리스트 : LOT 저장 및 수정
	 */
	@RequestMapping(value="/lot/save.do")
	public @ResponseBody void itmLotSave(@RequestBody ParameterWrapper<ItmLotVO> param) {
		itmService.saveItemLot(param.param);
	}

	/*
	 *	품목 리스트 : Location 탭
	 */
	@RequestMapping(value="/location.do")
	public String locationTab() {
		return "/main/itm/join/itm_location_x";
	}

	/*
	 *	품목 리스트 : Category 탭
	 */
	@RequestMapping(value="/category.do")
	public String categoryTab() {
		return "/main/itm/join/itm_category_x";
	}

	/*
	 *	품목 리스트 : Category 저장 및 수정
	 */
	@RequestMapping(value="/category/save.do")
	public @ResponseBody void itemCategorySave(@RequestBody ParameterWrapper<ItmCategoryVO> param) {
		itmService.saveItemCategory(param.param);
	}

	/*
	 *	품목 리스트 : 대체품 탭
	 */
	@RequestMapping(value="/replace.do")
	public void replaceTab() {

	}

	/*
	 *	품목 리스트 : 업체 정보 탭
	 */
	@RequestMapping(value="/company.do")
	public void companyTab() {

	}

	/*
	 *	품목 리스트 : 입고 실적 탭
	 */
	@RequestMapping(value="/wr.do")
	public void wrTab() {

	}

	// 브랜치 코드 목록 조회
	@RequestMapping(value="/getBranchCdMaster.do")
	public @ResponseBody Object getBranchCdMaster(@RequestBody ParameterWrapper<BranchCdMasterVO> params) {
		return PageUtil.getJsonResult(itmService.getBranchCdMaster(params.param));
	}

	/*
	 *	품목 전체 리스트
	 */
	@RequestMapping(value="/selectTotalItm.do")
	public @ResponseBody Object selectTotalItm(@RequestBody ParameterWrapper<ItmVO> param) {
		return PageUtil.getJsonResult(itmService.getSelectTotalItm(param.param));
	}

	/*
	 *	자재 Category 관리 뷰
	 */
	@RequestMapping(value="/itm_mapping_x.do")
	public String itemCategoryManageView() {
		return "/buy/itm/itm_mapping_x";
	}

	/**
	 * 자재부서 뷰 팝업
	 * @return string
	 */
	@RequestMapping("/pdept_p.do")
	public String itmAddPopupView() {
		return "/buy/itm/itm_pdept_p";
	}

	/**
	 * 자재부서 목록 조회
	 */
	@RequestMapping("/pdept/list.do")
	public @ResponseBody Object getItemSourcingPdeptList() {
		return PageUtil.getJsonResult(itmService.getItemSourcingPdeptList());
	}

	/**
	 * 회사, BU, 사업장, 부서 코드로 이미 존재하는지 여부 확인
	 * @param param
	 * @return
	 */
	@RequestMapping("/pdept/check.do")
	public @ResponseBody Boolean getItemSourcingPdeptCheck(@RequestBody ParameterWrapper<ItmSourcingPdeptVO> param) {
		return itmService.getItemSourcingPdeptCheck(param.param);
	}

	/**
	 * 자재부서 저장
	 */
	@RequestMapping("/pdept/save.do")
	public @ResponseBody Boolean saveItemSourcingPdept(@RequestBody ParameterWrapper<List<ItmSourcingPdeptVO>> params) {
		return itmService.saveItemSourcingPdept(params.param);
	}

	/**
	 * 자재부서 삭제
	 */
	@RequestMapping("/pdept/delete.do")
	public @ResponseBody void deleteItemSourcingPdept(@RequestBody ParameterWrapper<List<ItmSourcingPdeptVO>> params) {
		itmService.deleteItemSourcingPdept(params.param);
	}

	/**
	 * 자재 분류 트리 조회
	 */
	@RequestMapping("/ctgr/tree.do")
	public @ResponseBody Object getItemMappingCategoryTree(@RequestBody ParameterWrapper<ItmSourcingPdeptVO> param) {
		return PageUtil.getJsonResult(itmService.getItemMappingCategoryTree(param.param));
	}

	/**
	 * 자재 분류 트리 저장 및 수정
	 */
	@RequestMapping("/ctgr/save.do")
	public @ResponseBody void saveItemMappingCategoryTree(@RequestBody ParameterWrapper<List<ItmMappingCategoryVO>> params) {
		LOG.debug(">> : 되나 ???");
		itmService.saveItemMappingCategoryTree(params.param);
	}

	/**
	 * 자재 분류 트리 삭제
	 */
	@RequestMapping("/ctgr/delete.do")
	public @ResponseBody void deleteItemMappingCategoryTree(@RequestBody ParameterWrapper<List<ItmMappingCategoryVO>> params) {
		itmService.deleteItemMappingCategoryTree(params.param);
	}

	/**
	 * 자재 분류 목록 조회
	 */
	@RequestMapping("/ctgr/list.do")
	public @ResponseBody Object getItemMappingCategoryList(@RequestBody ParameterWrapper<ItmMappingCategoryVO> param) {
		return PageUtil.getJsonResult(itmService.getItemMappingCategoryList(param.param));
	}

	/**
	 * 자재 매핑 조회
	 */
	@RequestMapping("/mapping/list.do")
	public @ResponseBody Object getItemMappingList(@RequestBody ParameterWrapper<ItmMappingVO> param) {
		return PageUtil.getJsonResult(itmService.getItemMappingList(param.param));
	}

	/**
	 * 자재 매핑 저장
	 */
	@RequestMapping("/mapping/save.do")
	public @ResponseBody void saveItemMapping(@RequestBody ParameterWrapper<List<ItmMappingVO>> params) {
		itmService.saveItemMapping(params.param);
	}

	/**
	 * 자재 매핑 삭제
	 */
	@RequestMapping("/mapping/delete.do")
	public @ResponseBody void deleteItemMapping(@RequestBody ParameterWrapper<List<ItmMappingVO>> params) {
		itmService.deleteItemMapping(params.param);
	}

	/**
	 * 품목 분류 리스트 뷰
	 */
	@RequestMapping("/itm_mapping_view_x.do")
	public String itemCategoryView() {
		return "/buy/itm//itm_mapping_view_x";
	}

	/**
	 * 품목 분류 리스트 트리 조회
	 */
	@RequestMapping("/ctgr/ss/tree.do")
	public @ResponseBody Object getItemSessionCategoryTree() {
		return PageUtil.getJsonResult(itmService.getItemSessionCategoryTree());
	}

	/**
	 * 품목 분류 아이디로 매핑 테이블 주요 키를 조회하여 자재 목록 불러오기
	 */
	@RequestMapping("/ctgr/mapping/list.do")
	public @ResponseBody Object getItemListByCategoryId(@RequestBody ParameterWrapper<ItmMappingCategoryVO> param) {
		return PageUtil.getJsonResult(itmService.getItemListByCategoryId(param.param));
	}

	/**
	 * 품목 분류 리스트 단어 검색 조회
	 */
	@RequestMapping("/ctgr/search.do")
	public @ResponseBody Object getItemMappingVitemSearch(@RequestBody ParameterWrapper<HashMap<String, Object>> param) {
		return PageUtil.getJsonResult(itmService.getItemMappingVitemSearch(param.param));
	}

	/**
	 * 품목 profile 뷰
	 */
	@RequestMapping("/itm_profile_x.do")
	public String itemProfileView() {
		return "/buy/itm/itm_profile_x";
	}

	/**
	 * 품목 당년도 누적 금액 iframe 탭
	 */
	@RequestMapping("/accrue.do")
	public String itemAccrueSumTab() {
		return "/main/itm/join/itm_accrue_x";
	}

	/**
	 * 품목 정보 iframe 탭
	 */
	@RequestMapping("/info.do")
	public String itemInfoTab() {
		return "/main/itm/join/itm_info_x";
	}

	/**
	 * 품목 profile 자동 조회
	 */
	@RequestMapping("/profile/list.do")
	public @ResponseBody Object getItemProfileList() {
		return PageUtil.getJsonResult(itmService.getItemProfileList());
	}

	/**
	 * 품목 profile 아코디언 클릭 조회
	 */
	@RequestMapping("/profile/view.do")
	public @ResponseBody Object getItemProfileView(@RequestBody ParameterWrapper<Integer> param) {
		return PageUtil.getJsonResult(itmService.getItemProfileView(param.param));
	}

	/**
	 * 품목 profile 아코디언 저장
	 */
	@RequestMapping("/profile/save.do")
	public @ResponseBody void saveItemProfile(@RequestBody ParameterWrapper<ItmProfileVO> param) {
		itmService.saveItemProfile(param.param);
	}

	/**
	 * 품목 profile 의 품목정보 목록 조회
	 */
	@RequestMapping("/info/list.do")
	public @ResponseBody Object getItemProfileInfoList(@RequestBody ParameterWrapper<ItmProfileVO> param) {
		return PageUtil.getJsonResult(itmService.getItemProfileInfoList(param.param));
	}

	/**
	 * 품목 Master File 뷰
	 */
	@RequestMapping("/itm_master_file_x.do")
	public String itemMasterFileView() {
		return "/buy/itm/itm_master_file_x";
	}

	@RequestMapping("/itm_master_content_x.do")
	public ModelAndView itemMasterContentView(ModelAndView mv, HttpServletRequest request) {
		mv.addObject("contentParamFg", request.getParameter("contentParamFg") ) ;
		mv.setViewName("/buy/itm/itm_master_content_x");
		return mv;
	}

	@RequestMapping("/itm_master_view_pop.do")
	public ModelAndView itemMasterViewPop(ModelAndView mv, int masterSeq , int seq) {

		ItmMasterFileDetailVO param = new ItmMasterFileDetailVO() ;
		param.setMasterSeq(masterSeq);
		param.setSeq(seq);
		mv.addObject("row", itmService.getItemMasterFileDetailView(param)) ;

		mv.setViewName("/buy/itm/itm_master_view_pop");
		return mv;
	}




	/**
	 * 품목 Master File 시장구매전략 뷰
	 */
	@RequestMapping("/market/plan.do")
	public ModelAndView marketBuyPlanView(ModelAndView mv, @RequestParam String masterSeq) {
		mv.addObject("masterSeq", masterSeq);
		mv.setViewName("/main/itm/join/itm_market_plan_x");

		return mv;
	}

	/**
	 * 품목 Master File명 조회
	 */
	@RequestMapping("/master/file/list.do")
	public @ResponseBody Object getItemMasterFileList(@RequestBody ParameterWrapper<Map<String, Object>> param) {
		return PageUtil.getJsonResult(itmService.getItemMasterFileList(param.param));
	}

	/**
	 * 품목 Master File에서 시장구매전략 목록 조회
	 */
	@RequestMapping("/market/list.do")
	public @ResponseBody Object getItemMasterFileContentList(@RequestBody ParameterWrapper<ItmMasterFileVO> param) {
		return PageUtil.getJsonResult(itmService.getItemMasterFileContentList(param.param));
	}

	/**
	 * 품목 Master File에서 시장구매전략 저장 및 수정
	 */
	@RequestMapping("/market/save.do")
	public @ResponseBody void saveItemMasterMarketBuyPlan(@RequestBody ParameterWrapper<List<ItmMasterFileContentVO>> params) {
		itmService.saveItemMasterMarketBuyPlan(params.param);
	}

	/**
	 * 품목 Master File에서 시장구매전략 삭제
	 */
	@RequestMapping("/market/delete.do")
	public @ResponseBody void deleteItemMasterMarketBuyPlan(@RequestBody ParameterWrapper<List<ItmMasterFileContentVO>> params) {
		itmService.deleteItemMasterMarketBuyPlan(params.param);
	}

	/**
	 * 품목 Master File에서 상세 목록 조회
	 */
	@RequestMapping("/master/detail/list.do")
	public @ResponseBody Object getItemMasterFileDetailList(@RequestBody ParameterWrapper<ItmMasterFileDetailVO> param) {
		return PageUtil.getJsonResult(itmService.getItemMasterFileDetailList(param.param));
	}

	/**
	 * 품목 Master File에서 상세 목록 조회
	 */
	@RequestMapping("/master/detail/list2.do")
	public @ResponseBody Object getItemMasterFileDetailList2(@RequestBody ParameterWrapper<ItmMasterFileDetailVO> param) {
		return PageUtil.getJsonResult(itmService.getItemMasterFileDetailList2(param.param));
	}


	/**
	 * 품목 Master File에서 상세 저장 및 수정
	 */
	@RequestMapping("/master/detail/save.do")
	public @ResponseBody void saveItemMasterFileDetail(@RequestBody ParameterWrapper<ItmMasterFileDetailVO> param) {
		itmService.saveItemMasterFileDetail(param.param);
	}

	/**
	 * 품목 Master File에서 상세 삭제
	 */
	@RequestMapping("/master/detail/delete.do")
	public @ResponseBody void deleteItemMasterFileDetail(@RequestBody ParameterWrapper<ItmMasterFileDetailVO> param) {
		itmService.deleteItemMasterFileDetail(param.param);
	}

	/**
	 * 품목 Master File에서 상세 내용 조회
	 */
	@RequestMapping("/master/detail/view.do")
	public @ResponseBody Object getItemMasterFileDetailView(@RequestBody ParameterWrapper<ItmMasterFileDetailVO> param) {
		return PageUtil.getJsonResult(itmService.getItemMasterFileDetailView(param.param));
	}

	/**
	 * bp 리스트 정보
	 * @return
	 */
	@RequestMapping(value="/bpList.do")
	public @ResponseBody Object getBPList() {
		//UserMasterVO userInfo = PageUtil.getUserInfo();
		FiorgDeptVO fiorgDeptVO  = PageUtil.getFiorgDeptInfo();

		DeptVO deptVO = new DeptVO() ;
		deptVO.setCompErpCd(fiorgDeptVO.getCompTopsOrgCd());

		JSONObject jsonObj = new JSONObject() ;
		jsonObj.put("Data", itmService.selectDept(deptVO)) ;
		return PageUtil.getJsonResult(itmService.selectDept(deptVO));
	}

	public JSONObject getJsonStringFromMap( Map<String, String> map ) {

		JSONObject json = new JSONObject();
		for( Map.Entry<String, String> entry : map.entrySet() ) {
			String key = entry.getKey();
			Object value = entry.getValue();
			json.put(key, value);
		}

		return json;
	}

	@RequestMapping(value="/erp_send_test.do")
	public @ResponseBody Object erpSendTest() {
		//UserMasterVO userInfo = PageUtil.getUserInfo();
		return PageUtil.getJsonResult(itmService.sendErpItm());
	}

	/**
	 * 아이템 마스터 파일 조회
	 */
	@RequestMapping("/selectItmMasterFile.do")
	public @ResponseBody Object selectItmMasterFile(@RequestBody ParameterWrapper<Map<String, Object>> param) {
		return PageUtil.getJsonResult(itmService.selectItmMasterFile(param.param));
	}

	/**
	 * 품목팝업 저장
	 * @param mv
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saveWonbuItem.do")
	public @ResponseBody void saveWonbuItem(@RequestBody ParameterWrapper<Map<String, Object>> param) {
		itmService.saveWonbuItem(param.param);
	}

	/**
	 * 원부 카테고리1 조회
	 */
	@RequestMapping("/selectWonbuCategory1.do")
	public @ResponseBody Object selectWonbuCategory1(@RequestBody ParameterWrapper<Map<String, Object>> param) {
		return PageUtil.getJsonResult(itmService.selectWonbuCategory1(param.param));
	}

	/**
	 * 원부 카테고리2 조회
	 */
	@RequestMapping("/selectWonbuCategory2.do")
	public @ResponseBody Object selectWonbuCategory2(@RequestBody ParameterWrapper<Map<String, Object>> param) {
		return PageUtil.getJsonResult(itmService.selectWonbuCategory2(param.param));
	}

	/**
	 * 원부 카테고리3 조회
	 */
	@RequestMapping("/selectWonbuCategory3.do")
	public @ResponseBody Object selectWonbuCategory3(@RequestBody ParameterWrapper<Map<String, Object>> param) {
		return PageUtil.getJsonResult(itmService.selectWonbuCategory3(param.param));
	}

	/**
	 * 원부 원료 카테고리 페이지
	 * @param mv
	 * @return
	 */
	@RequestMapping("/itm_wonbu_category_x.do")
	public ModelAndView itmWonbuCategory(ModelAndView mv) {
		mv.setViewName("/buy/itm/itm_wonbu_category_x");
		return mv ;
	}


	/**
	 * 원부 원료 카테고리 JSON
	 * @param param
	 * @return
	 */
	@RequestMapping("/itm_wonbu_category_json.do")
	public @ResponseBody Object itmWonbuCategoryJson(@RequestBody ParameterWrapper<Map<String, Object>> param) {
		return PageUtil.getJsonResult(itmService.selectWonbuCategory(param.param)) ;
	}

	@RequestMapping("/itm_wonbu_category_save.do")
	public @ResponseBody Object itmWonbuCategorySaveJson(@RequestBody ParameterWrapper<List<ItmWonbuVO>> params) {
		itmService.selectWonbuCategorySave(params.param)  ;
		return "true";
	}

	@RequestMapping("/itm_wonbu_category2_save.do")
	public @ResponseBody Object itmWonbuCategorySave2Json(@RequestBody ParameterWrapper<List<ItmWonbuVO>> params) {
		itmService.selectWonbuCategory2Save(params.param)  ;
		return "true";
	}

	/**
	 * 원부 카테고리 Item
	 * @param param
	 * @return
	 */
	@RequestMapping("/itm_wonbu_category_itm_json.do")
	public @ResponseBody Object itmWonbuCategoryItmJson(@RequestBody ParameterWrapper<Map<String, Object>> param) {
		return PageUtil.getJsonResult(itmService.selectWonbuCategoryItm2(param.param)) ;
	}


	/**
	 * 원부 카테고리 Item 추가
	 * @param param
	 * @return
	 */
	@RequestMapping("/itm_wonbu_category_itm_save.do")
	public @ResponseBody Object itmWonbuCategoryItmSaveJson(@RequestBody ParameterWrapper<List<Map<String,Object>>> params) {
		itmService.saveWonbuCategoryItmUpdate(params.param) ;
		return "true";
	}

	/**
	 * 원부 카테고리 Item 삭제
	 * @param param
	 * @return
	 */
	@RequestMapping("/itm_wonbu_category_itm_delete.do")
	public @ResponseBody Object itmWonbuCategoryItmDelete(@RequestBody ParameterWrapper<Map<String,Object>> param) {
		itmService.saveWonbuCategoryItmUpdate(param.param) ;
		return "true";
	}



	/**
	 * 원부 아이템 숮ㅇ
	 * @param mv
	 * @return
	 */
	@RequestMapping("/itm_wonbu_itm_x.do")
	public ModelAndView itmWonbuItm(ModelAndView mv) {

		FiorgDeptVO fiorgDeptVO  = PageUtil.getFiorgDeptInfo();

		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put("compCd", "SN") ;

		mv.addObject("useCompList", codeHelperService.getFiorgDeptData("1", fiorgDeptVO.getCompTopsOrgCd())); //회사
		mv.addObject("branchPlantList", itmService.selectItmNewBuView(param)); // bu
		mv.addObject("wonbuCategory1List", codeHelperService.mapListToCode(itmService.selectWonbuCategory1(null), "categoryNm1", "categoryId1")); //카테고리
		mv.addObject("wonbuCategory2List", codeHelperService.mapListToCode(itmService.selectWonbuCategory2(null), "categoryNm2", "categoryId2")); //카테고리
		mv.addObject("wonbuCategory3List", codeHelperService.mapListToCode(itmService.selectWonbuCategory3(null), "categoryNm3", "categoryId3")); //카테고리

		mv.setViewName("/buy/itm/itm_wonbu_itm_x");
		return mv ;
	}
	
	/**
	 * 구 erp 아이템 숮ㅇ
	 * @param mv
	 * @return
	 */
	@RequestMapping("/itm_jde_erp_itm_x.do")
	public ModelAndView itmJdeErpItm(ModelAndView mv) {
		mv.setViewName("/buy/itm/itm_jde_erp_itm_x");
		return mv ;
	}
	
	/**
	 * 구매청구 리스트
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/selectJdeErpItm.do")
	public @ResponseBody Object selectJdeErpItm(@RequestBody ParameterWrapper<Map<String, Object>> param) {
		return PageUtil.getJsonResult(itmService.selectJdeErpItm(param.param));
    }


	/**
	 * 원부 아이템 숮ㅇ
	 * @param mv
	 * @return
	 */
	@RequestMapping("/itm_wonbu_itm_josn_x.do")
	public @ResponseBody Object itmWonbuItm(ModelAndView mv , HttpServletRequest request) {

		String rowidx = nvlReq(request,"ibpage");  //현재가 몇번째 페이지
		String onepagerow = nvlReq(request,"onepagerow"); //한번에 몇건씩 가져올 것인지

		if(rowidx==null){
			 rowidx="1";
			 onepagerow = "0";
		}

		int intStIdx = (Integer.parseInt(rowidx)-1) * Integer.parseInt(onepagerow)+1;
		int intEdIdx = intStIdx + (Integer.parseInt(onepagerow)-1);

		Map<String,Object> param = new HashMap<String,Object>() ;
		mapRequest(param , request , "compCd") ;
		mapRequest(param , request , "branchPlant") ;
		mapRequest(param , request , "lineType") ;
		mapRequest(param , request , "itmNm") ;
		mapRequest(param , request , "itmNo") ;
		mapRequest(param , request , "itmTypeNo") ;
		mapRequest(param , request , "wonbuCategoryId1") ;
		mapRequest(param , request , "wonbuCategoryId2") ;
		mapRequest(param , request , "wonbuCategoryId3") ;
		param.put("stIdx", intStIdx) ;
		param.put("edIdx", intEdIdx) ;

		for (String key : param.keySet() ) {
			LOG.debug("key : " +key +":" + param.get(key) );
		}

		 return this.ibSheetWapper(itmService.selectWonbuItem(param)
				 								, itmService.selectWonbuItemCount(param)) ;

	}


	@RequestMapping("/itm_wonbu_itm_bp_josn_x.do")
	public @ResponseBody Object itmWonbuItmBu(@RequestBody Map<String,Object> param) {
		 return PageUtil.getJsonResult(itmService.selectItmNewBuView(param));
	}


	protected Map ibSheetWapper(Object obj , int count){
		Map map = new HashMap() ;
		map.put("Data", obj) ;
		map.put("Total", count) ;
		return map;
	}

	private void mapRequest(Map<String,Object> param , HttpServletRequest request  , String key ) {
		param.put(key, nvlReq(request ,key))  ;
	}

	private String nvlReq(HttpServletRequest request , String key) {
		if(request.getParameter(key) == null)
			return "" ;
		if("".equals(request.getParameter(key) ) || "undefined".equals(request.getParameter(key)))
			return "" ;
		return request.getParameter(key) ;
	}

	/**
	 * 품목 ERP 동기화 페이지
	 */
	@RequestMapping("/itm_erp_add_x.do")
	public String itmErpAddX() {
		return "/buy/itm/itm_erp_add_x";
	}

	/**
	 * 품목 ERP 동기화처리
	 * @return
	 */
	@RequestMapping(value = "/itm_erp_add_sync.do")
	public @ResponseBody Object itmErpAddSync(@RequestBody ParameterWrapper<Map<String,Object>> params) {
		return PageUtil.getJsonResult(itmService.itmErpAddSync(params.param));
    }

	/**
	 * 기초유화 마스터 정보를 등록
	 * @param mv
	 * @return
	 */
	@RequestMapping("/itm_basic_mm_mgt_x.do")
	public ModelAndView itmBasicMMMgt(ModelAndView mv) {

		java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calFmDt = Calendar.getInstance();
		calFmDt.add(Calendar.MONTH, -3);

		Calendar calToDt = Calendar.getInstance();
		//calToDt.add(Calendar.MONTH, +1);

		mv.addObject("calFmDt" , sdf.format(calFmDt.getTime()));
		mv.addObject("calToDt" , sdf.format(calToDt.getTime()));
		HashMap headerParam  = new HashMap<String,String>() ;
		List<Map<String,Object>> headerRs = itmService.PROC_SELECT_MAP(headerParam , "SP_ITM_MM_MASTER_SELECT") ;
		mv.addObject("headerRs", headerRs) ;
		mv.addObject("headerRsSize", headerRs.size()) ;
		//이전 등록된 정보를 가져옴
		Map<String,Object> reqParam = new HashMap<String,Object>() ;
		reqParam.put("MODE","T") ;
		List<Map<String,Object>>  preRs = itmService.PROC_SELECT_MAP(reqParam , "SP_ITM_MM_MASTER_DATA_SELECT") ;

		JSONArray jsonArray = new JSONArray();
		for(int i=0;i<preRs.size();i++) {
			jsonArray.put(getJsonObjectFromMap( preRs.get(i)  ));
		}
		mv.addObject("preRs" , jsonArray.toString() );
		mv.setViewName("/buy/itm/itm_basic_mm_mgt_x");
		return mv ;
	}

	@RequestMapping("/itm_basic_mm_mgt_p.do")
	public ModelAndView itmBasicMMMgtP(ModelAndView mv , HttpServletRequest request) {
		java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calFmDt = Calendar.getInstance();

		Calendar calToDt = Calendar.getInstance();
		mv.addObject("calToDt" , sdf.format(calToDt.getTime()));
		String mode = request.getAttribute("MODE") == null ? "I" : String.valueOf(request.getAttribute("MODE"));


		HashMap headerParam  = new HashMap<String,String>() ;
		List<Map<String,Object>> headerRs = itmService.PROC_SELECT_MAP(headerParam , "SP_ITM_MM_MASTER_SELECT") ;
		mv.addObject("headerRs", headerRs) ;
		mv.addObject("headerRsSize", headerRs.size()) ;
		mv.addObject("MODE" , mode) ;

		mv.setViewName("/buy/itm/itm_basic_mm_mgt_p") ;
		return mv ;
	}

	//SP_TOPS_ITM_MM_MGT_DETAIL_VIEW

	public  JSONObject getJsonObjectFromMap( Map<String, Object> map ) {

		JSONObject json = new JSONObject();
		for( Map.Entry<String, Object> entry : map.entrySet() ) {
			String key = entry.getKey();
			Object value = entry.getValue();
			json.put(key, value);
		}

		return json;
	}


	@RequestMapping(value="/itm_basic_mm_mgt_save.do")
	public @ResponseBody Object itmBasicMmMgtSave(ModelAndView mv ,	@RequestBody List< ParameterWrapper2< Map<String,Object>, List<Map<String,Object>> >> params){

			Map<String,Object> reqFrm = params.get(0).param1 ;
			List<Map<String,Object>> reqMgtList = params.get(0).param2 ; //저장리스트
			boolean isRst =  itmService.itmBasicMmMgtSave(reqFrm  , reqMgtList ) ;

			return PageUtil.getJsonResult(isRst) ;

	}

	@RequestMapping(value="/itm_basic_mm_mgt2_save.do")
	public @ResponseBody Object itmBasicMmMgt2Save(ModelAndView mv ,	@RequestBody List< ParameterWrapper2< Map<String,Object>, List<Map<String,Object>> >> params){
			String rstCode = "" ;
			Map<String,Object> reqFrm = params.get(0).param1 ; //폼 정보
			List<Map<String,Object>> reqMgtList = params.get(0).param2 ; //저장 리스트
			//중복 체크
			String query = "SP_TOPS_ITM_MM_MGT_CHECK" ;
			int checkCnt = 0 ;
			 List<Map<String,Object>> checkList = itmService.PROC_SELECT_MAP(reqFrm , query);
			 for (Map<String, Object> map : checkList) {
				 checkCnt = (map.get("CNT") == null  ) ? 0 : Integer.parseInt(String.valueOf(map.get("CNT"))) ;
			}
			 /*
			if(checkCnt>=1) {rstCode = "0" ;}else {}
			 */

			boolean isRst =  itmService.itmBasicMmMgt2Save(reqFrm  , reqMgtList ) ;
			rstCode = "1" ;
			return PageUtil.getJsonResult(rstCode) ;

	}

	/**
	 * 기초유화 마스터 정보를 등록
	 * @param mv
	 * @return
	 */
	@RequestMapping("/itm_basic_mm_x.do")
	public ModelAndView itmBasicMM(ModelAndView mv) {
		mv.setViewName("/buy/itm/itm_basic_mm_x");
		return mv ;
	}

	/**
	 *
	 * @return
	 */
	@RequestMapping(value = "/itm_basic_mm_select.do")
	public @ResponseBody Object itmBasicMMSelect(@RequestBody ParameterWrapper<Map<String,Object>> params) {
		List<Map<String,Object>> colList = itmService.PROC_SELECT_MAP(params.param , "SP_ITM_MM_MASTER_SELECT") ;
		List<Map<String,Object>> dataList = itmService.PROC_SELECT_MAP(params.param , "SP_TOPS_ITM_MM_MGT2_VIEW") ;
		for (Map<String, Object> colRow : colList) {
			String idx = String.valueOf(colRow.get("IDX")) ;
			for (Map<String, Object> dataRow : dataList) {
				String mmIdx = String.valueOf(dataRow.get("MM_IDX")) ;
				if(idx.equals(mmIdx)) {
					colRow.put("SPOT", String.valueOf(dataRow.get("SPOT")))  ;
					break ;
				}
			}
		}

		return PageUtil.getJsonResult(colList);
    }

	/**
	 *
	 * @return
	 */
	@RequestMapping(value = "/itm_basic_mm_data_select.do")
	public @ResponseBody Object itmBasicMMDataSelect(@RequestBody ParameterWrapper<Map<String,Object>> params) {
		String query = "SP_ITM_MM_MASTER_DATA_SELECT" ;
		Map<String,Object> reqParam = params.param ;
		reqParam.put("MODE","L") ;
		return PageUtil.getJsonResult(itmService.PROC_SELECT_MAP(params.param , query));
   }



	/**
	 * 품목 ERP 동기화처리
	 * @return
	 */
	@RequestMapping(value = "/itm_basic_mm_category_select.do")
	public @ResponseBody Object itmBasicMmCategorySelect(@RequestBody ParameterWrapper<Map<String,Object>> params) {
		String query = "SP_ITM_MM_CATE_SELECT" ;
		return PageUtil.getJsonResult(itmService.PROC_SELECT_MAP(params.param , query));
    }

	/**
	 * 품목 ERP 동기화처리
	 * @return
	 */
	@RequestMapping(value = "/itm_basic_mm_category_view.do")
	public @ResponseBody Object itmBasicMmCategoryView(@RequestBody ParameterWrapper<Map<String,Object>> params) {
		String query = "SP_ITM_MM_CATE_VIEW" ;
		return PageUtil.getJsonResult(itmService.PROC_SELECT_MAP(params.param , query));
    }

	@RequestMapping(value = "/itm_basic_mm_item_view.do")
	public @ResponseBody Object itmBasicMmItemView(@RequestBody ParameterWrapper<Map<String,Object>> params) {
		String query = "SP_ITM_MM_ITEM_VIEW" ;
		return PageUtil.getJsonResult(itmService.PROC_SELECT_MAP(params.param , query));
    }



	/**
	 * 기초유화 등록 idx" : idx , "mode"
	 * @param mv
	 * @return
	 */
	@RequestMapping("/itm_basic_mm_p.do")
	public ModelAndView itmBasicMMP(ModelAndView mv , String idx , String mode) {
		LOG.debug("idx : " + idx);
		LOG.debug("mode : " + mode);

		Map<String, Object> masterRs = null ;
		if("view".equals(mode)) {
			Map<String,Object>reqMap = new HashMap<String,Object>() ;
			reqMap.put("idx", idx);
			List<Map<String,Object>>  masterList = itmService.PROC_SELECT_MAP(reqMap , "SP_ITM_MM_MASTER_VIEW") ;
			for (Map<String, Object> map : masterList) {
				masterRs = map ;
			}
		}
		Map<String,Object>reqMap = new HashMap<String,Object>() ;
		List<Map<String,Object>>  maxSeqList = itmService.PROC_SELECT_MAP(reqMap , "SP_ITM_MM_MASTER_MAX") ;
		String ORDER_SEQ = "" ;
		for (Map<String, Object> map : maxSeqList) {
			ORDER_SEQ = String.valueOf(map.get("ORDER_SEQ"));
		}
		mv.addObject("maxOrderSeq" , ORDER_SEQ ) ;
		mv.addObject("idx" , idx) ;
		mv.addObject("mode" , mode) ;
		mv.addObject("masterRs" , masterRs ) ;
		mv.setViewName("/buy/itm/itm_basic_mm_p");
		return mv ;
	}

	/**
	 * 품목 ERP 동기화처리
	 * @return
	 */
	@RequestMapping(value = "/itm_basic_mm_itm_select.do")
	public @ResponseBody Object itmBasicMmItmSelect(@RequestBody ParameterWrapper<Map<String,Object>> params) {
		String query = "SP_ITM_MM_ITM_SELECT" ;
		return PageUtil.getJsonResult(itmService.PROC_SELECT_MAP(params.param , query));
    }

	/**
	 * 기초 유화 등록
	 * @param mv
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/itm_basic_mm_save.do")
	public @ResponseBody Object popupItmPriceSave(ModelAndView mv ,	@RequestBody List<ParameterWrapper3<
											 Map<String,Object>
											, List<Map<String,Object>>
											, List<Map<String,Object>>
										>> params
			){

			Map<String,Object> reqFrm = params.get(0).param1 ;
			List<Map<String,Object>> reqCategory = params.get(0).param2 ; //선정업체 선택
			List<Map<String,Object>> reqItm = params.get(0).param3 ; //선정업체 선택
			boolean isRst = itmService.itmBasicMmSave(reqFrm , reqCategory , reqItm) ;//아이템 저장
			return PageUtil.getJsonResult(isRst) ;
	}

	/**
	 * 기초 유화 삭제
	 * @param mv
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/itm_basic_mm_delete.do")
	public @ResponseBody Object popupItmPriceDelete(ModelAndView mv ,	@RequestBody ParameterWrapper<List<Map<String,Object>>> params){
			List<Map<String,Object>> reqCategory = params.param ; //선정업체 선택
			boolean isRst = itmService.itmBasicMmDelete( reqCategory ) ;//아이템 저장
			return PageUtil.getJsonResult(isRst) ;
	}

	@RequestMapping(value="/itm_srm_x.do")
	public ModelAndView itmSrmM(ModelAndView mv ) {
		 mv.setViewName("/buy/itm/itm_srm_x");
		 return mv ;
	}

	@RequestMapping(value="/itm_srm_1t.do")
	public ModelAndView itmSrm1T(ModelAndView mv, HttpServletRequest request) {
		 mv.addObject("jsonParam", request.getParameter("param") ) ;
//		 mv.addObject("jsonParam", new org.json.simple.JSONObject(CommonUtils.getMapFromRequest(request)).toJSONString());
		 mv.setViewName("/buy/itm/itm_srm_1t");
		 return mv ;
	}

	@RequestMapping(value="/itm_srm_2t.do")
	public ModelAndView itmSrm2T(ModelAndView mv ) {
		 mv.setViewName("/buy/itm/itm_srm_2t");
		 return mv ;
	}


	@RequestMapping(value="/itm_srm_3t.do")
	public ModelAndView itmSrm3T(ModelAndView mv, @RequestParam(required=true,name="categoryId1")  String categoryId1
			  , @RequestParam(required=true,name="categoryId2") String categoryId2
			  , @RequestParam(required=true,name="categoryId3") String categoryId3 
			  , @RequestParam(required=false,name="initFlag") String initFlag ) {
		 mv.addObject("categoryId1", categoryId1) ;
		 mv.addObject("categoryId2", categoryId2) ;
		 mv.addObject("categoryId3", categoryId3) ;
		 mv.addObject("initFlag", initFlag) ;
		 mv.setViewName("/buy/itm/itm_srm_3t");
		 return mv ;
	}

	@RequestMapping(value="/itm_srm_chart.do")
public ModelAndView itmSrmChart(ModelAndView mv , @RequestParam(required=true,name="categoryId1")  String categoryId1
																			  , @RequestParam(required=true,name="categoryId2") String categoryId2
																			  , @RequestParam(required=true,name="categoryId3") String categoryId3
																			  , @RequestParam(required=true,name="mode", defaultValue="total") String mode
																			  , @RequestParam(required=false,name="stDt") String stDt
																			  , @RequestParam(required=false,name="edDt") String edDt
//																			  , @RequestParam(required=false,name="shortItmNo") String shortItmNo
//																			  , @RequestParam(required=false,name="thirdItmNo") String thirdItmNo
																			  , @RequestParam(required=false,name="buItmNo") String buItmNo
																			  
		) {
		mv.addObject("categoryId1", categoryId1) ;
		mv.addObject("categoryId2", categoryId2) ;
		mv.addObject("categoryId3", categoryId3) ;
		if(stDt != null && !"".equals(stDt))
			stDt = stDt.replaceAll("-", "") ;
		if(edDt != null && !"".equals(edDt))
			edDt = edDt.replaceAll("-", "") ;

		mv.addObject("stDt", stDt) ;
		mv.addObject("edDt", edDt) ;
//		mv.addObject("shortItmNo", shortItmNo) ;
//		mv.addObject("thirdItmNo", thirdItmNo) ;
		mv.addObject("buItmNo", buItmNo) ;
		mv.addObject("mode", mode) ;
		mv.setViewName("/buy/itm/itm_srm_chart");
		 return mv ;
	}


	@RequestMapping(value="/itm_srm_p.do")
	public ModelAndView itmSrmP(ModelAndView mv , String selectForm) {
		mv.addObject("selectForm", selectForm) ;
		mv.setViewName("/buy/itm/itm_srm_p");
		return mv ;
	}

	@RequestMapping(value="/itm_srm_view.do")
	public @ResponseBody Object itmSrmView(ModelAndView mv
														,	@RequestBody ParameterWrapper<Map<String,Object>> reqParams){
			Map<String,Object> reqMap = reqParams.param ; //선정업체 선택
			Map rstList = new HashMap<String,Object>() ;
			List<Map<String,Object>>  rsView  = itmService.itmSrmView(reqMap, "viewMain") ;
			if(rsView != null) {
				int idx = 0 ;
				for (Map<String, Object> row : rsView) {
					idx = Integer.parseInt(String.valueOf(row.get("idx"))) ;
				}
				if(!reqMap.containsKey("idx")) {
					reqMap.put("idx", idx) ;
				}

				rstList.put("viewMain", rsView)   ;
				rstList.put("viewSupList", PageUtil.getJsonResult(itmService.itmSrmView(reqMap, "viewSupList")))   ;
				rstList.put("viewCurrtList", PageUtil.getJsonResult(itmService.itmSrmView(reqMap, "viewCurrtList")))   ;
			}

			//rstList.put("viewVersionList", itmService.itmSrmView(reqMap, "viewVersionList"))   ;
			return rstList ;
	}

	@RequestMapping(value="/itm_srm_version_view.do")
	public @ResponseBody Object itmSrmVersionView(ModelAndView mv
														,	@RequestBody ParameterWrapper<Map<String,Object>> reqParams){
			Map<String,Object> reqMap = reqParams.param ; //선정업체 선택
			Map rstList = new HashMap<String,Object>() ;
			List<Map<String,Object>>  rsView  = itmService.itmSrmVersionView(reqMap, "viewMain") ;
			if(rsView != null) {
				int idx = 0 ;
				for (Map<String, Object> row : rsView) {
					idx = Integer.parseInt(String.valueOf(row.get("idx"))) ;
				}
				if(!reqMap.containsKey("idx")) {
					reqMap.put("idx", idx) ;
				}

				rstList.put("viewMain", rsView)   ;
				rstList.put("viewSupList", PageUtil.getJsonResult(itmService.itmSrmVersionView(reqMap, "viewSupList")))   ;
				rstList.put("viewCurrtList", PageUtil.getJsonResult(itmService.itmSrmVersionView(reqMap, "viewCurrtList")))   ;
			}

			//rstList.put("viewVersionList", itmService.itmSrmView(reqMap, "viewVersionList"))   ;
			return rstList ;
	}

	//itm_srm_1t_save
	@RequestMapping(value="/itm_srm_1t_save.do")
	public @ResponseBody Object itmSrm1tSave(ModelAndView mv
																		,@RequestBody List<ParameterWrapper2<
																		 Map<String,Object>
																		, List<Map<String,Object>>
																	>> params){

		Map<String,Object> reqMap = params.get(0).param1 ;
		reqMap.put("event" , "save") ;

		boolean isRst = itmService.itmSrmSave( reqMap
										, params.get(0).param2) ;

		return isRst ;
	}

	//itm_srm_1t_save
	@RequestMapping(value="/itm_srm_1t_confirm.do")
	public @ResponseBody Object itmSrm1tConfirm(ModelAndView mv
																			,@RequestBody List<ParameterWrapper2<
																			 Map<String,Object>
																			, List<Map<String,Object>>
																		>> params){

			Map<String,Object> reqMap = params.get(0).param1 ;
			reqMap.put("event" , "versionSave") ;

			boolean isRst = itmService.itmSrmSave( reqMap
											, params.get(0).param2) ;

			return isRst ;
		}


	@RequestMapping(value="/itm_srm_version_p.do")
	public ModelAndView itmSrmVersionP(ModelAndView mv , String categoryId1 , String categoryId2 , String categoryId3) {
		Map<String,Object> reqMap = new HashMap<String,Object>() ;

		reqMap.put("categoryId1" , categoryId1);
		reqMap.put("categoryId2" , categoryId2) ;
		reqMap.put("categoryId3" , categoryId3) ;
		List<Map<String,Object>> rst = itmService.itmSrmView(reqMap, "viewVersionList") ;

		JSONArray jsonArray = new JSONArray();
		for(int i=0;i<rst.size();i++) {
			jsonArray.put(getJsonObjectFromMap( rst.get(i)  ));
		}
		mv.addObject("viewVersionList" , jsonArray.toString() );

		mv.addObject("categoryId1", categoryId1) ;
		mv.addObject("categoryId2", categoryId2) ;
		mv.addObject("categoryId3", categoryId3) ;


		mv.setViewName("/buy/itm/itm_srm_version_p");
		return mv ;
	}

	@RequestMapping(value="/itm_srm_version_view_p.do")
	public ModelAndView itmSrmVersionViewP(ModelAndView mv , String idx) {
		mv.addObject("idx",idx) ;
		mv.setViewName("/buy/itm/itm_srm_version_view_p");
		return mv ;
	}


	@RequestMapping(value="/itm_srm_category_view.do")
	public @ResponseBody Object itmSrmCategoryView(ModelAndView mv
														,	@RequestBody ParameterWrapper<Map<String,Object>> reqParams){
		/*
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put("categoryId1" , "2");
		param.put("categoryId2" , "37");
		param.put("categoryId3" , "69");
		param.put("year" , "2019");
		*/

		List<Map<String,Object>>  rstList = itmService.PROC_SELECT_MAP(reqParams.param , "SP_TOPS_SRM_CATEGORY_SELECT") ;
		return PageUtil.getJsonResult(rstList) ;
	}

	@RequestMapping(value="/itm_srm_category_item_view.do")
	public @ResponseBody Object itmSrmCategoryItmView(ModelAndView mv
														,	@RequestBody ParameterWrapper<Map<String,Object>> reqParams){
		List<Map<String,Object>> rstList = itmService.PROC_SELECT_MAP(reqParams.param , "SP_TOPS_SRM_ITM_SELECT") ;
		return PageUtil.getJsonResult(rstList) ;
	}

	@RequestMapping(value="/itm_srm_total_amt_view.do")
	public @ResponseBody Object itmSrmTotalAmtView(ModelAndView mv
														,	@RequestBody ParameterWrapper<Map<String,Object>> reqParams){
			List<Map<String,Object>>  rstList = itmService.PROC_SELECT_MAP(reqParams.param , "SP_TOPS_SRM_TOTAL_AMT_SELECT") ;
		return PageUtil.getJsonResult(rstList) ;
	}


	@RequestMapping(value="/itm_srm_chart_amt_view.do")
	public @ResponseBody Object itmSrmChartAmtView(ModelAndView mv
														,	@RequestBody ParameterWrapper<Map<String,Object>> reqParams){
		List<Map<String,Object>>  totalList = itmService.PROC_SELECT_MAP(reqParams.param , "SP_TOPS_SRM_TOTAL_CHART1_SELECT") ;
		List<Map<String,Object>>  etcList = itmService.PROC_SELECT_MAP(reqParams.param , "SP_TOPS_SRM_TOTAL_CHART2_SELECT") ;
		Map<String,Object> rst = new HashMap<String,Object>() ;
		rst.put("totalList" , totalList) ;
		rst.put("etcList" , etcList) ;
		return PageUtil.getJsonResult(rst) ;
	}

	@RequestMapping(value="/itm_srm_chart_amt2_view.do")
	public @ResponseBody Object itmSrmChartAmt2View(ModelAndView mv
														,	@RequestBody ParameterWrapper<Map<String,Object>> reqParams){
		List<Map<String,Object>>  totalList = itmService.PROC_SELECT_MAP(reqParams.param , "SP_TOPS_SRM_TOTAL_CHART3_SELECT") ;
		return PageUtil.getJsonResult(totalList) ;
	}


	@RequestMapping(value="/itm_srm_itm_search_p.do")
	public ModelAndView itmSrmItmSearchP(ModelAndView mv , String categoryId1 , String categoryId2 , String categoryId3) {
		Map<String,Object> reqMap = new HashMap<String,Object>() ;

		reqMap.put("categoryId1" , categoryId1);
		reqMap.put("categoryId2" , categoryId2) ;
		reqMap.put("categoryId3" , categoryId3) ;

		List<Map<String,Object>> rst = itmService.PROC_SELECT_MAP(reqMap , "SP_TOPS_SRM_ITM_SELECT") ;

		JSONArray jsonArray = new JSONArray();
		for(int i=0;i<rst.size();i++) {
			jsonArray.put(getJsonObjectFromMap( rst.get(i)  ));
		}
		mv.addObject("viewList" , jsonArray.toString() );

		mv.setViewName("/buy/itm/itm_srm_itm_search_p");
		return mv ;
	}


	@RequestMapping(value="/itm_srm_itm_view.do")
	public @ResponseBody Object itmSrmItmView(ModelAndView mv
														,	@RequestBody ParameterWrapper<Map<String,Object>> reqParams){
		List<Map<String,Object>>  totalList = itmService.PROC_SELECT_MAP(reqParams.param , "SP_TOPS_SRM_ITM_SELECT") ;
		return PageUtil.getJsonResult(totalList) ;
	}

	/**
	 * 
	 * @param params
	 */
	@RequestMapping(value="/v2/list.do")
	public @ResponseBody Object getItems(@RequestBody ParameterWrapper<Map<String, Object>> param) {
		return PageUtil.getJsonResult(itmService.getItems(param.param));
	}
	
	/**
	 * 
	 * @param params
	 */
	@RequestMapping(value="/cont/save.do")
	public @ResponseBody Object saveContItm(@RequestBody ParameterWrapper<List<Map<String, Object>>> param) {
		StringBuffer message = new StringBuffer();
		boolean isSuccess = true;
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			itmService.saveContItm(param.param);
			message.append("저장되었습니다.");
		} catch (Exception e) {
			isSuccess = false;
			message.append(e.getMessage());
		}
		
		return PageUtil.getJsonResult(isSuccess, message.toString(), data);
	}
	
	/**
	 * 투자유형 카테고리 조회
	 */
	@RequestMapping("/selectInvType.do")
	public @ResponseBody Object selectInvType(@RequestBody ParameterWrapper<Map<String, Object>> param) {
		return PageUtil.getJsonResult(itmService.selectInvType(param.param));
	}
}