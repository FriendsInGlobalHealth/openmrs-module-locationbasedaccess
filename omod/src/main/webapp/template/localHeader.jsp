<spring:htmlEscape defaultHtmlEscape="true" />
<ul id="menu">
	<li class="first"><a
		href="${pageContext.request.contextPath}/admin"><spring:message
				code="admin.title.short" /></a></li>

	<li
		<c:if test='<%= request.getRequestURI().contains("/locationbasedaccessList") %>'>class="active"</c:if>>
		<a href="${pageContext.request.contextPath}/module/locationbasedaccess/locationbasedaccessList.form"><spring:message
				code="locationbasedaccess.manage" /></a>
	</li>
</ul>

