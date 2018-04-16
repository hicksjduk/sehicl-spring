<h1>Login</h1>
${message!""}
<p>
	If you do not have a login, <a href="/register">register here</a>.
</p>
<p>If you have already registered, please fill in the fields below
	and press "Login". If you cannot remember your password, fill in the
	e-mail address and press "Reset password"; if the e-mail address you specify is
	that of a registered user, a password reset link will be sent to that
	address.</p>
<form action="/login" method="post">
	<table>
		<tr>
			<td>E-mail address</td>
			<td><input type="text" name="email" value="${login.validation.email!""}"></td>
			<td>${login.validation.emailMessage!""}</td>
		</tr>
		<tr>
			<td>Password</td>
			<td><input type="password" name="password"
				value="${login.validation.password!""}"></td>
			<td>${login.validation.passwordMessage!""}</td>
		</tr>
	</table>
	<p>
		<input name="Login" type="Submit" value="Login"/> <input
			name="Remind" type="Submit" value="Reset password"/>
	</p>
</form>
