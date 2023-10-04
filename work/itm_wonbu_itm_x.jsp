<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="uc" uri="/tags/userControl"%>
	<head>
		<script type="text/javascript">
			function fn_WindowLoad() {
				IBSHeet_Init();
			}

			//IBSheet1 초기화
			function IBSHeet_Init() {
				var initdata = {};
				initdata.Cfg = {SearchMode:smServerPaging, Page:100, MergeSheet:msNone,Sort:0};
				initdata.Cols = [
					{ Edit: 1, Hidden: 0, Type: "Text", Header: "번호", Width: 40, SaveName: "num", Align: "Center" , Sort:0 },
					{ Edit: 1, Hidden: 0, Type: "Text", Header: "회사", Width: 80, SaveName: "compNm", Align: "Center" , Sort:0 },
// 					{ Edit: 1, Hidden: 0, Type: "Text", Header: "Branch", Width: 100, SaveName: "branchPlantNm", Align: "Center"  , Sort:0 },
					{ Edit: 1, Hidden: 0, Type: "Text", Header: "대분류", Width: 80, SaveName: "wonbuCategoryNm1", Align: "Center" , Sort:0 },
					{ Edit: 1, Hidden: 0, Type: "Text", Header: "중분류", Width: 80, SaveName: "wonbuCategoryNm2", Align: "Center"  , Sort:0 },
					{ Edit: 1, Hidden: 0, Type: "Text", Header: "소분류", Width: 80, SaveName: "wonbuCategoryNm3", Align: "Center"  , Sort:0 },
					{ Edit: 1, Hidden: 0, Type: "Text", Header: "통합자재코드", Width: 100, SaveName: "itmNo", Align: "Center" , Sort:0 },
// 					{ Edit: 1, Hidden: 0, Type: "Text", Header: "Short No.", Width: 70, SaveName: "shortItemNo", Align: "Center"  , Sort:0 },
// 					{ Edit: 1, Hidden: 0, Type: "Text", Header: "Second No.", Width: 70, SaveName: "scndItemNo", Align: "Center"  , Sort:0 },
// 					{ Edit: 1, Hidden: 0, Type: "Text", Header: "Third No.", Width: 100, SaveName: "thirdItemNo", Align: "Center"  , Sort:0 },
					{ Edit: 1, Hidden: 0, Type: "Text", Header: "품명", Width: 180, SaveName: "itmNm", Align: "Left"  , Sort:0 },
// 					{ Edit: 1, Hidden: 0, Type: "Text", Header: "스팩", Width: 120, SaveName: "specMapping", Align: "Left"  , Sort:0 },
					{ Edit: 1, Hidden: 0, Type: "Text", Header: "uom", Width: 70, SaveName: "uom", Align: "Center"  , Sort:0 },
// 					{ Edit: 1, Hidden: 0, Type: "Text", Header: "G/L Class", Width: 80, SaveName: "glClass", Align: "Center"  , Sort:0 },
					{ Edit: 1, Hidden: 0, Type: "Text", Header: "", Width: 80, SaveName: "", Align: "" , Sort:0  },

					//Hidden Cols
					{ Edit: 0, Hidden: 1, Type: "Status", Header: "상태", Width: 100, SaveName: "status" },
					{ Edit: 0, Hidden: 1, Type: "Text",  SaveName: "itmId" },
					{ Edit: 0, Hidden: 1, Type: "Text",  SaveName: "compCd" },
					{ Edit: 0, Hidden: 1, Type: "Text",  SaveName: "branchPlant" },
				];

				IBS_InitSheet(mySheet,initdata);
				mySheet.SetEditableColorDiff(2);
				mySheet.SetEditable(0);
				mySheet.SetCountPosition(4);
			}

			/**그리드 더블 클릭시 **/
			function mySheet_OnDblClick(Row, Col, Value, CellX, CellY, CellW, CellH) {
				//params["buCd"] = IBSheet_GreqItm.CellValue2(selectedRow, "useBu");
				var selectRowJson = mySheet.GetRowData(Row);


				console.log(selectRowJson)

				
 				var params = {};
				params["itmId"] = selectRowJson["itmId"] ;
				params["itmNo"] = selectRowJson["itmNo"] ;
				params["compCd"] =selectRowJson["compCd"]  ;
// 				params["shortItemNo"] = selectRowJson["shortItemNo"]  ;
				params["reqYn"] = "N";

				fng_ItemDetailPop(params);

			}

			function fn_IBSheet1_Search(){
				var formData = [] ;
				formData.push("compCd="+$("#compCd").val()) ;
				formData.push("branchPlant="+$("#branchPlant").val()) ;
				formData.push("lineType="+$("#lineType").val()) ;
				formData.push("itmNm=" + $("#itmNm").val()) ;
				formData.push("itmNo="+$("#itmNo").val()) ;
				formData.push("itmTypeNo="+$("#itmTypeNo").val()) ;
				formData.push("wonbuCategoryId1="+$("#categoryId1").val()) ;
				formData.push("wonbuCategoryId2="+$("#categoryId2").val()) ;
				formData.push("wonbuCategoryId3="+$("#categoryId3").val()) ;

				var param = {"Param":"onepagerow=100&"+formData.join("&")};
				console.log(param);								
				
				//mySheet.Search("/buy/itm", "itm_wonbu_itm_josn_x.do", param, true);
				
				mySheet.DoSearchPaging("/buy/itm/itm_wonbu_itm_josn_x.do",param);
			}

			   function fn_DoChangeWc(idx, value) {
				   var url = "";
					var wc = "";
					if ("1" == idx) {
						url = "selectWonbuCategory2.do";
						wc = "categoryId2";
					} else if ("2" == idx) {
						url = "selectWonbuCategory3.do";
						wc = "categoryId3";
					}else if ("4" == idx) {
						url = "itm_wonbu_itm_bp_josn_x.do";
						wc = "branchPlant";
					}

					var params = {};
					if("1" == idx || "2" == idx){
						params["categoryId"] = value;
					}else if ("3" == idx){
						params["compCd"] = $("#compCd").val();
					}else if ("4" == idx){
						params["comeCd"] = $("#compCd").val();
					}
					console.log("teastaset", params);
					$.fng_Ajax({
						SvcName: "/buy/itm/" + url,
						Params: { param: params },
						Callback: function (result) {
							$("#" + wc + " option").remove();
							$("#" + wc).append("<option value=''>==전체==</option>");

							if ("1" == idx) {
								for (var wcRst in result) {
									$("#" + wc).append("<option value='" + result[wcRst].categoryId2 + "'>" + result[wcRst].categoryNm2 + "</option>");
									$("#" + wc + " option:eq(0)").attr("selected", "selected");

									if ((Number(wcRst) + 1) == result.length && Number(idx) <= 1)
										fn_DoChangeWc(Number(idx) + 1, $("#" + wc + " option:eq(1)").attr("value"));
								}
							} else if ("2" == idx) {
								for (var wcRst in result) {
									$("#" + wc).append("<option value='" + result[wcRst].categoryId3 + "'>" + result[wcRst].categoryNm3 + "</option>");
									$("#" + wc + " option:eq(0)").attr("selected", "selected");
									if ((Number(wcRst) + 1) == result.length && Number(idx) <= 1)
										fn_DoChangeWc(Number(idx) + 1, "");

								}
							} else if ("3" == idx) {
								for (var wcRst in result) {
									$("#" + wc).append("<option value='" + result[wcRst].VALUE + "'>" + result[wcRst].TEXT + "</option>");
									$("#" + wc + " option:eq(0)").attr("selected", "selected");
								}
							}
						}
					});
				}

			   //팝업에서 리로드(아이템정보 수정)
			   function IBSheet_GreqItm_Search(){
				   fn_IBSheet1_Search() ;
			   }
		</script>
	</head>
	<body>
		<div id="toproundbox" class="roundbox" >
    <fieldset class="search">
       <div class="searchDiv" >
        	<table>
				<tr>
        			<th>회사</th>
        			<td>
        				<select id="compCd" name="compCd" field="@compCd" onchange="fn_DoChangeWc(4, this.value);" style="width: 100%;">
							<c:forEach var="useComp" items="${useCompList}">
								<option value="${useComp.VALUE}">${useComp.TEXT}</option>
							</c:forEach>
						</select>
					</td>
					<th>BP</th>
        			<td>
						<select id="branchPlant" name="branchPlant" field="@branchPlant" >
		                	<option value="">==전체==</option>
		                	<c:forEach var="useBp" items="${branchPlantList}">
								<option value="${useBp.VALUE}">${useBp.TEXT}</option>
							</c:forEach>
		                </select>
        			</td>
					<th>품목명</th>
			        <td>
						<input type="text" name="itmNm" id="itmNm" field="@itmNm" class="input_text w97p"/>
			        </td>
        			<th>품목코드</th>
					<td>
						<input type="text" name="itmNo" id="itmNo" field="@itmNo" class="input_text w97p"/>
					</td>
        		</tr>
        		<tr>
        			<th>대분류</th>
        			<td>
        				<select id="categoryId1" name="categoryId1" field="@categoryId1" onchange="fn_DoChangeWc(1, this.value);">
		                	<option value="">==전체==</option>
		                	<c:forEach var="wonbuCategory1" items="${wonbuCategory1List}">
								<option value="${wonbuCategory1.VALUE}">${wonbuCategory1.TEXT}</option>
							</c:forEach>
		                </select>
        			</td>
					<th>중분류</th>
        			<td>
        				<select id="categoryId2" name="categoryId2" field="@categoryId2" onchange="fn_DoChangeWc(2, this.value);">
		                	<option value="">==전체==</option>
		                	<c:forEach var="wonbuCategory2" items="${wonbuCategory2List}">
								<option value="${wonbuCategory2.VALUE}">${wonbuCategory2.TEXT}</option>
							</c:forEach>
		                </select>
        			</td>
					<th>소분류</th>
			        <td>
			        	<select id="categoryId3" name="categoryId3" field="@categoryId3">
		                	<option value="">==전체==</option>
		                	<c:forEach var="wonbuCategory3" items="${wonbuCategory3List}">
								<option value="${wonbuCategory3.VALUE}">${wonbuCategory3.TEXT}</option>
							</c:forEach>
		                </select>
			        </td>
        			<th>Line Type</th>
					<td>
						<select id="lineType" name="lineType" field="@lineType">
		              		<option value="S">stock</option>
		              		<option value="N">non-stock</option>
						</select>
		                </td>
        		</tr>
        	</table>
        </div>
        <div class="searchbtn" style="padding-top: 10px;">
        	<!--  <input type="button" id="btnSearch" class="btn UseSearch" value="테스트" onclick="eventTest()"/> -->
            <input type="button" id="btnSearch" class="btn UseSearch" value="조회" onclick="fn_IBSheet1_Search();"/>
        </div>
    </fieldset>
</div>
		<div class="gridline auto_height">
		   	<script type="text/javascript"> createIBSheet("mySheet", "100%", "100%"); </script>
		</div>
	</body>