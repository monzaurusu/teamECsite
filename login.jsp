<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="./css/form.css">
<link rel="stylesheet" type="text/css" href="./css/header.css">
<link rel="stylesheet" type="text/css" href="./css/spring.css">
<link rel="stylesheet" type="text/css" href="./css/submit-btn.css">
<link rel="stylesheet" type="text/css" href="./css/page-title.css">
<link rel="stylesheet" type="text/css" href="./css/message.css">
<title>LOGIN</title>
</head>
<body>
<jsp:include page="header.jsp"/>

	<div id="main">
		<div id="top">
		<h1>ログイン画面</h1>
		</div>


		<s:if test="errorMessage !='' && errorMessage != null">
			<div class="error">
				<div class="error-message">
					<s:property value="errorMessage" escape="false"/><br>
				</div>
			</div>
		</s:if>

		<s:if test="errorMessageList1 != null && errorMessageList1.size()>0">
			<div class="error">
				<div class="error-message">
			<s:iterator value="errorMessageList1"><s:property /><br>
			</s:iterator>
				</div>
			</div>
		</s:if>
		<s:if test="errorMessageList2 != null && errorMessageList2.size()>0">
			<div class="error">
				<div class="error-message">
		<s:iterator value="errorMessageList2"><s:property /><br>
		</s:iterator>
				</div>
			</div>
		</s:if>

		<s:form action="LoginAction">
			<table class="vertical-list-table2">
				<tr>
					<th scope="row"><s:label value="ユーザーID"/></th>
					<s:if test="session.savedUserIdFlg == true">
					<td><s:textfield name="userId" class="txt"  value='%{#session.userId}' autocomplete="off"/></td>
					</s:if>
					<s:else>
					<td><s:textfield name="userId" class="txt" placeholder="ユーザーID" value='%{userId}' autocomplete="off"/></td>
					</s:else>
				</tr>
				<tr>
					<th scope="row"><s:label value="パスワード"/></th>
					<td><s:password name="password" class="txt" placeholder="パスワード" autocomplete="off"/></td>
				</tr>
			</table>

			<div class="box">
				<s:if test="(#session.savedUserIdFlg == true && #session.userId != null && !#session.userId.isEmpty()) || savedUserIdFlg == true">
					<s:checkbox name="savedUserIdFlg" checked="checked"/>
				</s:if>
				<s:else>
					<s:checkbox name="savedUserIdFlg"/>
				</s:else>

			<s:label value="ユーザーID保存"/><br>
			</div>
			<div class="submit-btn-box">

					<s:submit value="ログイン" class="submit-btn"/><br>
			</div>
		</s:form>

	</div>
	<div>
			<div class="submit-btn-box">
				<s:form action="CreateUserAction">
					<s:submit value="新規ユーザー登録" class="submit-btn"/><br>
				</s:form>
			</div>

			<div class="submit-btn-box">
				<s:form action="ResetPasswordAction">
					<s:submit value="パスワード再設定" class="submit-btn"/>
				</s:form>
			</div>

		</div>

</body>
</html>