<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="urn:jsptagdir:/WEB-INF/tags"%>

<acme:form readonly="${readonly}">
	<acme:input-select code="inventor.patronage.form.label.status" path="status">
		<acme:input-option code="PROPOSED" value="PROPOSED" selected="${status == 'PROPOSED'}"/>
		<acme:input-option code="ACCEPTED" value="ACCEPTED" selected="${status == 'ACCEPTED'}"/>
		<acme:input-option code="DENIED" value="DENIED" selected="${status == 'DENIED'}"/>
	</acme:input-select>
	<acme:input-textbox code="inventor.patronage.form.label.code" path="code"/>
	<acme:input-textarea code="inventor.patronage.form.label.legal-stuff" path="legalStuff"/>
	<acme:input-money code="inventor.patronage.form.label.budget" path="budget"/>
	<acme:input-moment code="inventor.patronage.form.label.creation-moment" path="creationMoment"/>
	<acme:input-moment code="inventor.patronage.form.label.start-date" path="startDate"/>
	<acme:input-moment code="inventor.patronage.form.label.end-date" path="endDate"/>
	<acme:input-url code="inventor.patronage.form.label.more-info" path="moreInfo"/>
	<acme:input-textbox code="inventor.patronage.form.label.patron" path="company"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(command, 'show, update') && status =='PROPOSED'}">
			<acme:submit code="inventor.patronage.form.button.updateStatus" action="/inventor/patronage/update-status"/>
			
		</jstl:when>
		<jstl:when test="${command == 'show' && status != 'PROPOSED'}">
			<h2><acme:message code="inventor.patronage.message.patron"/></h2>
			<acme:input-textbox code="inventor.patronage.form.label.patron.username" path="username"/>
			<acme:input-textbox code="inventor.patronage.form.label.patron.company" path="company"/>
			<acme:input-textbox code="inventor.patronage.form.label.patron.statement" path="statement"/>
			<acme:input-url code="inventor.patronage.form.label.patron.info" path="patronLink"/>
		
			<acme:button code="inventor.patronage.form.button.patronageReport" action="/inventor/patronage-report/list?patronageId=${patronageId}"/>
		</jstl:when>
	</jstl:choose>
			
		
			
		
		
		
	
</acme:form>