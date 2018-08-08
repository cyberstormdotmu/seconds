<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<script type="text/javascript">
	var el = document.getElementById('foo');
	el.onclick = changeOrder('userId');

	function changeOrder(property) {
		var value = document.getElementById(property).value;
		window.location.href = '../bookingPeriod/orderBookingList.mpa?'
				+ property + '=' + value + '';
	}

	function deleteBookingPeriodBlock(referenceNo) {
		var answer = confirm("Are you sure want to delete this Period Block?");
		if (answer) {
			$('#referenceNo').val(referenceNo);
			var form = document.getElementById("mainform");
			form.action = '../bookingPeriod/deleteBookingPeriodBlock.mpa';
			form.submit();
			//window.location.href="/user/deleteUser.mpa?method=delete&id="+id;
		} else {
			return false;
		}
	}

	function changeRows() {
		var records = document.getElementById("recordsPerPage").value;
		// 	var curPage = document.getElementById("cPage").value;
		var curPage = 1;
		window.location.href = "../bookingPeriod/listBookingPeriodBlock.mpa?recordsPerPage="
				+ records + "&page=" + curPage + "";
	}

	function updateBookingPeriodBlock(referenceNo) {

		$('#userId').val(userId);
		var form = document.getElementById("mainform");
		form.action = 'updateUserForm.mpa';
		form.submit();
	}
</script>
<div id="wrapper">
	<!--main area start-->
	<div id="middle">
	
	<c:choose>
	
	<c:when test="${listOfBookingPeriodBlock.size() == 0 } }">
	
	<h2>No Records found.</h2>
	
	</c:when>
	<c:otherwise>
	</c:otherwise>
	</c:choose>
	
	
	
		<h1>Booking Period List</h1>

		<div class="form_grid top_pad">
			<div class="titlebg">
				<h2>Appointment Booking List</h2>
			</div>
			<div class="content_block">
				<div id="page-heading">
					<h1>
						<c:out value="${param['deletedSuccess']}" />
					</h1>
				</div>
				<!-- end page-heading -->

				<c:if test="${not empty messages}">
					<div id="message-green">
						<table width="100%" cellspacing="0" cellpadding="0" border="0">
							<tbody>
								<tr>
									<td class="green-left">${messages}</td>
									<td class="green-right"><a class="close-green"><img
											alt="" src="../images/table/icon_close_green.gif"></a></td>
								</tr>
								<tr style="height: 5px;"></tr>
							</tbody>
						</table>
					</div>
				</c:if>
				<form id="mainform" action="" method="post">
					<input type="hidden" id="userId" name="userId" />
					<table border="0" width="100%" cellpadding="0" cellspacing="1"
						class="grid">
						<tr>
							<th class="table-header-repeat line-left minwidth-1"><a
								href="javascript:;"
								onclick="changeOrder('periodStartDateString');">Date To
									Block</a><input type="hidden" id="periodStartDateString"
								name="periodStartDateString"
								value="${periodStartDateStringOrder}" /></th>
							<th class="table-header-repeat line-left minwidth-1"><a
								href="javascript:;" onclick="changeOrder('periodStartTime');">Period
									Start Time</a><input type="hidden" id="periodStartTime"
								name="periodStartTime" value="${periodStartTimeOrder}" /></th>
							<th class="table-header-repeat line-left minwidth-1"><a
								href="javascript:;" onclick="changeOrder('periodEndTime');">Period
									End Time</a><input type="hidden" id="periodEndTime"
								name="periodEndTime" value="${periodEndTimeOrder}" /></th>
							<th class="table-header-repeat line-left"><a
								href="javascript:;" onclick="changeOrder('reason');">Reason
									Of Blocking</a><input type="hidden" id="reason" name="reason"
								value="${reasonOrder}" /></th>
						</tr>
						<%
							int a = 0;
						%>
						<c:forEach var="bookingPeriodBlock"
							items="${listOfBookingPeriodBlock}">
							<%
								if (a % 2 == 0) {
							%>
							<tr class="row2">
								<%
									} else {
								%>
							
							<tr class="row1">
								<%
									}
										a++;
								%>
								<td style="text-align: center;">${bookingPeriodBlock.periodStartDateString}</td>
								<td style="text-align: center;">${bookingPeriodBlock.periodStartTime}</td>
								<td style="text-align: center;">${bookingPeriodBlock.periodEndTime}</td>
								<td style="text-align: center;">${bookingPeriodBlock.reason}</td>
							</tr>
						</c:forEach>

					</table>
				</form>
				<div class="paging">
					<input type="hidden" id="cPage" name="cPage" value="${currentPage}">
					<ul>
						<c:if test="${currentPage gt 1 }">
							<li><a
								href="${pageContext.request.contextPath}/bookingPeriod/listBookingPeriodBlock.mpa?page=${currentPage - 1}">&laquo;Prev</a></li>
						</c:if>
						<c:forEach begin="1" end="${noOfPages}" var="i">
							<c:choose>
								<c:when test="${currentPage eq i }">
									<li><a href="#" class="active">${i}</a></li>
								</c:when>
								<c:otherwise>
									<li><a
										href="${pageContext.request.contextPath}/bookingPeriod/listBookingPeriodBlock.mpa?page=${i}">${i}</a></li>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						<c:if test="${currentPage lt noOfPages }">
							<li><a
								href="${pageContext.request.contextPath}/bookingPeriod/listBookingPeriodBlock.mpa?page=${currentPage + 1}">Next
									&raquo;</a></li>
						</c:if>
					</ul>

					<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; No of rows: &nbsp; <select
						name="recordsPerPage" id="recordsPerPage" style="width: 60px;">
							<%
								int appRows = session.getAttribute("appRows") == null ? 10
										: (Integer) session.getAttribute("appRows");
								if (appRows == 5) {
							%>
							<option value="5" selected="selected">5</option>
							<%
								} else {
							%>
							<option value="5">5</option>
							<%
								}
								if (appRows == 10) {
							%>
							<option value="10" selected="selected">10</option>
							<%
								} else {
							%>
							<option value="10">10</option>
							<%
								}
								if (appRows == 15) {
							%>
							<option value="15" selected="selected">15</option>
							<%
								} else {
							%>
							<option value="15">15</option>
							<%
								}
								if (appRows == 20) {
							%>
							<option value="20" selected="selected">20</option>
							<%
								} else {
							%>
							<option value="20">20</option>
							<%
								}
							%>
					</select> <input type="button" value="Go" onclick="changeRows()">
					</span>
				</div>
			</div>
		</div>
	</div>
</div>
