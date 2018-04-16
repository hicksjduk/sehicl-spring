<h1>Password reset</h1>
<#if !(reset.user??)>
<p>The specified password reset token does not exist, or has expired.</p>
<p>To generate a new token, <a href="/login">go to the Login page.</a></p>
<#else>

<p>
	Please enter the new password.
</p>
<form action="/pwdReset/${reset.resetId?string.computer}" method="post">
	<table>
		<tr>
			<td>E-mail address</td>
			<td></td>
			<td>${reset.user.email}</td>
		</tr>
		<tr>
			<td>Password</td>
			<td>*</td>
			<td><input type="password" name="password""></td>
			<td>${(reset.validation.passwordMessage)!""}</td>
		</tr>
		<tr>
			<td>Confirm password</td>
			<td>*</td>
			<td><input type="password" name="passwordConf""></td>
			<td>${(reset.validation.passwordConfMessage)!""}</td>
		</tr>
	</table>
	<p>
		<input name="Reset" type="Submit" value="Reset"/>
	</p>
</form>
</#if>