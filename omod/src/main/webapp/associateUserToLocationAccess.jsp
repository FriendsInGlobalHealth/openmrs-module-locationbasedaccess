<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>

<openmrs:require privilege="Manage User and Location Associations"
	otherwise="/login.htm"
	redirect="/module/locationbasedaccess/associateUserToLocationAccess.form" />

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


<script type="text/javascript">
	
	$j(document).ready(function(){
		
		var selectedLocationIDs = ${selectedLocationIDs};
		selectedLocationIDs.forEach(locationID => {
			var row = document.getElementById(locationID);
			if(row){
				row.checked = true;
				row.value = locationID;
			}
		  });
	});
</script>


<h2>
	<openmrs:message code="locationbasedaccess.linkUserToLocation" />
</h2>

<br />
<br />


<b class="boxHeader"><openmrs:message code="Relationship.user" /></b>
<div class="box">
	<table class="openmrsSearchTable" style="width: 100%;" cellpadding="2"
		cellspacing="0">
		<thead>
			<tr style="" dojoattachpoint="headerRow">
				<th><openmrs:message code="User.systemId"
						javaScriptEscape="true" /></th>
				<th><openmrs:message code="User.username"
						javaScriptEscape="true" /></th>
				<th><openmrs:message code="PersonName.givenName"
						javaScriptEscape="true" /></th>
				<th><openmrs:message code="PersonName.familyName"
						javaScriptEscape="true" /></th>
			</tr>
		</thead>
		<tr>
			<td><c:out value="${userLocationAccess.user.systemId}" /></td>
			<td><c:out value="${userLocationAccess.user.username}" /></td>
			<td><c:out value="${userLocationAccess.user.givenName}" /></td>
			<td><c:out value="${userLocationAccess.user.familyName}" /></td>
		</tr>
	</table>
</div>
<br />


<c:if test="${UserWithAssociations eq true}">
	<a style="float: right; color: red;"
		href="removeAssociation.form?userId=<c:out value="${userLocationAccess.user.userId}"/>">
		<openmrs:message code="locationbasedaccess.unLinkAssociation" />
	</a>
</c:if>

<h3>
	<openmrs:message code="locationbasedaccess.selectLocations" />
</h3>
<b class="boxHeader"><openmrs:message code="Location.header" /></b>
<form method="post" style="margin-bottom: 15px;"
	action="linkUserToLocation.form">
	<table id="myTable" width="100%" cellpadding="2" cellspacing="0"
		style="font-size: 13px;">
		<thead>
			<tr>
				<th></th>
				<th><openmrs:message code="general.name" /></th>
				<th><openmrs:message code="general.description" /></th>
				<th><openmrs:message code="Location.province" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${userLocationAccess.locations}" var="location"
				varStatus="itemsRow">
				<tr>
					<td><input type="checkbox" name="selectedLocationIDs"
						value="${location.locationId}" id="${location.locationId}"></td>
					<td><c:out value="${location.name}" /></td>
					<td><c:out value="${location.description}" /></td>
					<td><c:out value="${location.parentLocation.name}" /></td>
				</tr>
			</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td><input type="submit"
					value="<openmrs:message code="general.save"/>"
					name="linkUserToLocation"></td>
				<td colspan="2"></td>
			</tr>
		</tfoot>
	</table>
</form>
<br />

<%@ include file="/WEB-INF/template/footer.jsp"%>