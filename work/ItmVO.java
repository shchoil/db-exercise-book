package com.syds.tops.itm.vo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author SYC719233
 * @date : 2017. 4. 10. 오후 3:25:08
 * @desc : 자재 VO, TOPS_ITM_NEW 테이블 VO도 추가
 */
@Data
public class ItmVO {
	/*
	 * Field : 자재 시퀀스
	 */
	Integer itmSeq;

	/*
	 * Field : 자재 마스터 시퀀스
	 */
	Integer itmMasterSeq;

	/*
	 * Field : 자재 아이디
	 */
	String itmId;

	/*
	 * Field : 계열사 품목코드
	 */
	String itmNo;

	/*
	 * Field : 계약 여부
	 */
	String constYn;

	/*
	 * Field : Short Item Number
	 */
	String shortItemNo;

	/*
	 * Field : Second Item Number
	 */
	String scndItemNo;

	/*
	 * Field : Third Item Number
	 */
	String thirdItemNo;

	/*
	 * Field : 품목명
	 */
	String itmNm;

	/*
	 * Field : 영문 품목명
	 */
	String itmNmEng;

	/*
	 * Field : Catalog Number
	 */
	String catalogNm;

	/*
	 * Field : Search Text
	 */
	String searchText;

	/*
	 * Field : Stocking Type
	 */
	String stockingType;


	String specStandard;

	String specMapping;

	/*
	 * Field : G/L Class
	 */
	String glClass;

	/*
	 * Field : Unit of Measure
	 */
	String uom;

	String uomPurchasing;

	String uomSecondary;

	String uomPricing;

	String uomShipping;

	String uomProduction;

	String uomComponent;

	String uomWeight;

	String uomVolume;

	/*
	 * Field : Line Type
	 */
	String lineType;

	/*
	 * Field : Bulk/Packed Flag
	 */
	String bulkpackedFg;

	/*
	 * Field : 자재사용여부
	 */
	String useYn;

	String categoryCd1;

	String categoryNm1;

	String categoryCd2;

	String categoryNm2;

	String trcategoryCd2;

	String wonbuCategoryId1;

	String wonbuCategoryId2;

	String wonbuCategoryId3;

	String usages;

	String masterfileSeq;

	String inventoryCostLv;

	String salesPriceLv;

	String purchasePriceLv;

	String itmGubn;

	String orgArea;

	String ioFlag;

	String mroRegYn;

	String mroItmCd;

	String msdsMngYn;

	String msdsDocId;

	String msdsReceiptYn;

	String msdsNotReceiptRsn;

	String crcd;

	String orderPolicyCd;

	BigDecimal valueOrderPolicy;

	String issueTypeCd;

	String planningCd;

	String planningFenceRule;

	BigDecimal planningFence;

	BigDecimal freezeFence;

	BigDecimal leadtimeLevel;

	String fixedVariable;

	BigDecimal multipleOrderQuantity;

	BigDecimal unitsPerContainer;

	BigDecimal safetyStock;

	/*
	 *  입력 사번
	 */
	String inputEmpNo;

	/*
	 *  입력 시간
	 */
	String inputEmpDt;

	/*
	 *  수정 사번
	 */
	String modEmpNo;

	/*
	 *  수정 시간
	 */
	String modDt;

	/*
	 * Field : 프로시저용 사번
	 */
	String empNo;

	/*
	 * Field : 입력, 수정, 삭제 상태값
	 */
	String mode;

	/*
	 * Field : 테스트에서 조회만 할 건지 실제로 자재를 입력, 수정, 삭제가 가능한지 확인 하려는 필드 값
	 */
	Boolean onlyViewFlag;

	/*
	 * Field : ERP 품목번호
	 */
	String erpItemNo;

	/*
	 * Field : 스펙
	 */
	String spec;

	/*
	 * Field : MAKER
	 */
	String maker;

	/*
	 * Field : 화폐
	 */
	String currencyId;

	/*
	 * Field : 비고
	 */
	String bigo;

	/*
	 * Field : 용도
	 */
	String usg;

	/*
	 * Field : 귀속BU
	 */
	String branchPlant;

	String pageMode ;
	
	String compCd; 
	
	String itmTypeNo;
}