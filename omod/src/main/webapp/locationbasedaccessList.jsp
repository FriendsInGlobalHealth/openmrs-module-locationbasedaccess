<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>

<openmrs:htmlInclude
	file="/scripts/jquery/dataTables/css/dataTables_jui.css" />
<openmrs:htmlInclude
	file="/scripts/jquery/dataTables/js/jquery.dataTables.min.js" />

<script type="text/javascript">
	$j(document).ready(function() {
		$j('#myTable').dataTable({
			"iDisplayLength" : 50
		});
	})
</script>

<h2>
	<openmrs:message code="locationbasedaccess.list" />
</h2>

<c:if test="${successfullyExecuted}">
	<div id="openmrs_msg">
		<b> <spring:message code="locationbasedaccess.successfullExecuted" />
		</b>
	</div>
</c:if>

<br />
<br />
<a href="selectUser.form"><openmrs:message
		code="locationbasedaccess.addUserLocationBasedAccess" /></a>
<br />
<br />
<br />
<b class="boxHeader"><openmrs:message
		code="locationbasedaccess.list.title" /></b>
<form method="post" style="margin-bottom: 15px;">
	<table id="myTable" class="display" width="100%" cellpadding="2"
		cellspacing="0" style="font-size: 13px;">
		<thead>
			<tr>
				<th><openmrs:message code="User.systemId" /></th>
				<th><openmrs:message code="User.username" /></th>
				<th><openmrs:message code="PersonName.givenName" /></th>
				<th><openmrs:message code="PersonName.familyName" /></th>
				<th><openmrs:message code="Location.header" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="userLocationAccess" items="${userLocationAccessList}">
				<tr>
					<td style="white-space: nowrap"><a
						href="associateUserToLocationAccess.form?userId=<c:out value="${userLocationAccess.user.userId}"/>">
							<c:out value="${userLocationAccess.user.systemId}" />
					</a></td>
					<td>${userLocationAccess.user.username}</td>
					<td>${userLocationAccess.user.person.givenName}</td>
					<td>${userLocationAccess.user.person.familyName}</td>
					<td><c:forEach var="location"
							items="${userLocationAccess.locations}" varStatus="varStatus">
							<c:out value='${location.name}' /> (<c:out
								value='${location.parentLocation.name}' />)
							<br />
						</c:forEach></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</form>
<%@ include file="/WEB-INF/template/footer.jsp"%>