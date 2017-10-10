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
	</table>
	<p>By clicking the "Submit" button below, you agree that:
	<ul>
		<li>We may store the information you have supplied on a computer
			system, and we may use it only for the purpose of administering your
			rights as a registered user of this site. We will never give your
			details to any other party.</li>
		<li>You will treat all information to which your login gives you
			access with appropriate care and respect. In particular, where that
			information comprises other people's personal details, you may use it
			only for legitimate purposes connected with the League, unless you
			first gain the explicit consent of the person or persons concerned.</li>
	</ul>
	</p>
	<p>
		<input type="Submit" value="Submit">
	</p>
</form>
