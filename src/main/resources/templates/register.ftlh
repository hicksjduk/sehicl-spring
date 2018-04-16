<h1>New user registration</h1>
<p>Please fill in the fields below and press "Submit". All fields
	marked with "*" must be completed.</p>
<form action="/register" method="post">
	<table>
		<tr>
			<td>Name</td>
			<td>*</td>
			<td><input type="text" name="name" value="${(register.validation.name)!""}"></td>
			<td>${(register.validation.nameMessage)!""}</td>
		</tr>
		<tr>
			<td>Club</td>
			<td></td>
			<td><input type="text" name="club" value="${(register.validation.club)!""}"></td>
			<td>${(register.validation.clubMessage)!""}</td>
		</tr>
		<tr>
			<td>E-mail address</td>
			<td>*</td>
			<td><input type="text" name="email" value="${(register.validation.email)!""}"></td>
			<td>${(register.validation.emailMessage)!""}</td>
		</tr>
		<tr>
			<td>Password</td>
			<td>*</td>
			<td><input type="password" name="password""></td>
			<td>${(register.validation.passwordMessage)!""}</td>
		</tr>
		<tr>
			<td>Confirm password</td>
			<td>*</td>
			<td><input type="password" name="passwordConf""></td>
			<td>${(register.validation.passwordConfMessage)!""}</td>
		</tr>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td colspan="3">
				* 
				<input type="checkbox" name="agreement" ${register.validation.agreement?then("checked", "")}>
				I confirm that:</input>
			</td>
			<td>${(register.validation.agreementMessage)!""}</td>
		</tr>
	</table>
				<ul>
					<li>I have read and understood <a href="/dp">the League's data protection policy</a>.
					<li>I consent to the information I have supplied being stored on a computer
					system, and used exclusively for the purpose of administering my
					rights as a registered user of this site.</li>
					<li>I will treat all information to which my login gives me
					access with appropriate care and respect. In particular, where that
					information comprises other people's personal details, I will use it
					only for legitimate purposes connected with the League, unless I
					first gain the explicit consent of the person or persons concerned.</li>
				</ul>
	<p>
		<input type="Submit" value="Submit">
	</p>
</form>
