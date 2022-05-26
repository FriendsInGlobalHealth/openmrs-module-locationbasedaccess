<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>



<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />
<openmrs:htmlInclude file="/scripts/dojoConfig.js" />
<openmrs:htmlInclude file="/scripts/dojo/dojo.js" />


<h2>
	<openmrs:message code="locationbasedaccess.linkUserToLocation" />
</h2>

<br />
<br />

<form method="post">

	<table>
		<tr>
			<td><openmrs:message code="User.find" /></td>
			<td><input type="text" name="name"
				value="<c:out value="${param.name}"/>" /></td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" name="action"
				value="<openmrs:message code="general.search"/>" /></td>
		</tr>
	</table>

</form>

<br />

<c:if test="${fn:length(users) == 0 && param.name != None }">
	<openmrs:message code="User.noUsersFound" />
</c:if>

<c:if test="${fn:length(users) > 0}">
	<h3>
		<openmrs:message code="locationbasedaccess.selectUser" />
	</h3>
	<b class="boxHeader"><openmrs:message code="User.list.title" /></b>
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
					<th><openmrs:message code="User.roles" javaScriptEscape="true" />
					</th>
					<openmrs:forEachDisplayAttributeType personType="user"
						displayType="listing" var="attrType">
						<th><openmrs:message
								code="PersonAttributeType.${fn:replace(attrType.name, ' ', '')}"
								javaScriptEscape="true" text="${attrType.name}" /></th>
					</openmrs:forEachDisplayAttributeType>
				</tr>
			</thead>
			<c:forEach var="user" items="${users}" varStatus="rowStatus">
				<tr
					class='${rowStatus.index % 2 == 0 ? "evenRow" : "oddRow" } ${user.retired ? "retired" : "" }'>
					<td style="white-space: nowrap"><a
						href="associateUserToLocationAccess.form?userId=<c:out value="${user.userId}"/>">
							<c:out value="${user.systemId}" />
					</a></td>
					<td><c:out value="${user.username}" /></td>
					<td><c:out value="${user.givenName}" /></td>
					<td><c:out value="${user.familyName}" /></td>
					<td><c:if test="${fn:length(userRolesMap[user]) > 3}">
							<span title="<c:out value='${userRolesMap[user]}'/>">
						</c:if> <c:forEach var="r" items="${userRolesMap[user]}"
							varStatus="varStatus" end="2">
							<c:choose>
								<c:when test="${varStatus.index == 0}">
									<c:choose>
										<c:when test="${r == role}">
											<span class='bold_text'>${r} </span>
										</c:when>
										<c:when test="${r != role && role != null}">
											<span class='bold_text'> <c:forEach
													var="inheritedRole" items="${userInheritanceLineMap[user]}"
													varStatus="inheritanceStatus">
													<c:out value='${inheritedRole}' />
													<c:if
														test="${inheritanceStatus.index ne fn:length(userInheritanceLineMap[user]) - 1}"> -> </c:if>
												</c:forEach>
											</span>
										</c:when>
										<c:otherwise>
											<c:out value='${r}' />
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>, <c:out value='${r}' />
								</c:otherwise>
							</c:choose>
						</c:forEach> <c:if test="${fn:length(userRolesMap[user]) > 3}">
				, ....</span>
						</c:if></td>
					<openmrs:forEachDisplayAttributeType personType="user"
						displayType="listing" var="attrType">
						<td><c:if test="${user.person != null}">${user.person.attributeMap[attrType.name]}</c:if></td>
					</openmrs:forEachDisplayAttributeType>
				</tr>
			</c:forEach>

		</table>
	</div>
</c:if>

<br />


<%@ include file="/WEB-INF/template/footer.jsp"%>