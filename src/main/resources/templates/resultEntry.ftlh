<script language="javascript" src="/result.js" type="text/javascript"></script>
<script language="javascript">
var match = ${jsonResult?no_esc};
</script>

<h2>${result.homeTeam.name} v ${result.awayTeam.name}</h2>
<p>${result.leagueName}, ${result.date} at ${result.time}, court ${result.court}.</p>
<form onreset="batFirstChanged(0)">
	<datalist id="homeplayers">
		<#list result.homeTeam.players as p> 
		<option value="${p.name}">
		</#list>
	</datalist>
	<datalist id="awayplayers">
		<#list result.awayTeam.players as p> 
		<option value="${p.name}">
		</#list>
	</datalist>
	<fieldset style="width: 20%;">
		Which team batted first? <select id="battingfirst" onchange="batFirstChanged()">
			<option value="hometeam">${result.homeTeam.name}</option>
			<option value="awayteam">${result.awayTeam.name}</option>
		</select>
	</fieldset>
	<#list 1..2 as inns>
	<br /> <br />
	<fieldset>
		<legend id="bat${inns}TeamName">Innings of ${[result.homeTeam, result.awayTeam][inns - 1].name}</legend>
		<table style="width: 50%; border: 1px solid black; float: left;">
			<tr>
				<th></th>
				<th>Batsman</th>
				<th>How Out</th>
				<th>Bowler</th>
				<th>Runs</th>
			</tr>
			<#list 1..6 as bat>
			<tr>
				<td>${bat}</td>
				<td><input type="text" 
						list="${(inns == 1)?then('homeplayers', 'awayplayers')}" 
						name="batsman${inns}${bat}" ${(bat <= 4)?then('required', '')}></td>
				<td><select id="howout${inns}${bat}" onchange="howOutChanged(${inns}, ${bat})">
						<option value="dnb" selected>Did not bat</option>
						<option value="notout;score">Not out</option>
						<option value="retired;score">Retired</option>
						<option value="runout;score;out">Run out</option>
						<option value="bowled;score;bowler;out">Bowled</option>
						<option value="caught;score;bowler;out">Caught</option>
						<option value="stumped;score;bowler;out">Stumped</option>
						<option value="lbw;score;bowler;out">LBW</option>
						<option value="absentno">Absent</option>
						<option value="absentout;out">Absent out</option>
						<option value="retiredhurtno;score">Retired hurt - not out</option>
						<option value="retiredhurt;score;out">Retired hurt - out </option>
						<option value="hitwicket;score;bowler;out">Hit wicket</option>
						<option value="handledball;score;out">Handled ball</option>
						<option value="obstructing;score;out">Obstructing the field</option>
						<option value="timedout;score;out">Timed out</option>
				</select></td>
				<td><input type="text" 
					onchange="refreshBowlerWickets(${inns})"
					name="bowlerwicket${inns}${bat}" 
					list="${(inns == 1)?then('awayplayers', 'homeplayers')}" 
					disabled></td>
				<td><input type="number" name="runsScored${inns}${bat}" min="0" disabled onchange="refreshTotal(${inns})"></td>
			</tr>
			</#list>
			<tr>
				<th colspan="4" style="text-align: right;">Extras</th>
				<td><input type="number" name="extras${inns}" min="0" required onchange="refreshTotal(${inns})"></td>
			</tr>
			<tr>
				<th colspan="4" style="text-align: right;">Total</th>
				<td><input type="number" readonly id="total${inns}" min="0"></td>
			</tr>
			<tr>
				<th colspan="4" style="text-align: right;">Wickets</th>
				<td><input type="number" readonly id="wickets${inns}" min="0"></td>
			</tr>
			<tr>
				<th colspan="4" style="text-align: right;">Overs</th>
				<td><input type="number" readonly id="overs${inns}" min="0"></td>
			</tr>
		</table>
		<table style="width: 40%; border: 1px solid black; float: right;">
			<tr>
				<th></th>
				<th>Bowler</th>
				<th>Overs</th>
				<th>Runs</th>
				<th>Wickets</th>
			</tr>
			<#list 1..6 as bowl>
			<tr>
				<td>${bowl}</td>
				<td><input type="text"
						onchange="refreshBowlerWickets(${inns})" 
						list="${(inns == 2)?then('homeplayers', 'awayplayers')}" 
						name="bowler${inns}${bowl}" ${(bowl <= 4)?then('required', '')}></td>
				<td><select name="overs${inns}${bowl}"onchange="refreshOvers(${inns})">
					<#list 0..3 as overs>
						<option value="${overs * 6}">${overs}</option>
					</#list>
					<#list 0..2 as overs>
						<#list 1..5 as balls>
							<option value="${overs * 6 + balls}">${overs}.${balls}</option>
						</#list>
					</#list>
				</select></td>
				<td><input type="number" name="runsConceded${inns}${bowl}" min="0"></td>
				<td><input type="number" name="wickets${inns}${bowl}" readonly min="0"></td>
			</tr>
			</#list>
		</table>
	</fieldset>
	</#list>
	<br /> <br /> <br /> <br /> <input type="submit" value="Submit">
	<input type="reset" value="Reset">
</form>