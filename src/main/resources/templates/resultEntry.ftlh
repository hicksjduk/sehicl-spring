<script language="javascript" src="/result.js" type="text/javascript"></script>
<script language="javascript">
var match = ${jsonResult?no_esc};
window.onload = () => initPage(); 
</script>

<h2>${result.homeTeam.name} v ${result.awayTeam.name}</h2>
<p>${result.leagueName}, ${result.date} at ${result.time}, court ${result.court}.</p>
<form onreset="batFirstChanged(0)" action='${result.uri}' method="post">
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
		Which team batted first? <select id="battingfirst" name="battingfirst" onchange="batFirstChanged()">
		    <#if result.innings??>
		       <#assign batFirst = result.innings[0].homeTeamBatting?then("h", "a")/>
		    </#if>
			<option value="hometeam" <#if batFirst!"" == "h">selected</#if>>${result.homeTeam.name}</option>
			<option value="awayteam" <#if batFirst!"" == "a">selected</#if>>${result.awayTeam.name}</option>
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
						id="batsman${inns}${bat}" 
						name="${playerDataField.BATSMAN}${inns}${bat}"></td>
				<td><select id="howout${inns}${bat}" 
				        name="${playerDataField.HOW_OUT}${inns}${bat}" 
				        onchange="howOutChanged(${inns}, ${bat})">
				    <#list howOut as k, ho>
				        <option value="${ho.attribute}" ${(ho?counter == 1)?then("selected", "")}>${ho.text}</option>
				    </#list>
				</select></td>
				<td><input type="text" 
					onchange="refreshBowlerWickets(${inns})"
                    id="wicketbowler${inns}${bat}" 
					name="${playerDataField.WICKET_BOWLER}${inns}${bat}" 
					list="${(inns == 1)?then('awayplayers', 'homeplayers')}" 
					disabled></td>
				<td><input type="number" name="${playerDataField.RUNS_SCORED}${inns}${bat}"
				    id="runsscored${inns}${bat}" 
				    min="0" disabled onchange="refreshTotal(${inns})"></td>
			</tr>
			</#list>
			<tr>
				<th colspan="4" style="text-align: right;">Extras</th>
				<td><input type="number" 
				id="extras${inns}" 
                name="${inningsDataField.EXTRAS}${inns}" 
				min="0" required onchange="refreshTotal(${inns})"></td>
			</tr>
			<tr>
				<th colspan="4" style="text-align: right;">Total</th>
				<td><input type="number" readonly 
				id="total${inns}" 
                name="${inningsDataField.TOTAL}${inns}" 
				min="0"></td>
			</tr>
			<tr>
				<th colspan="4" style="text-align: right;">Wickets</th>
				<td><input type="number" readonly 
				id="wickets${inns}" 
                name="${inningsDataField.WICKETS}${inns}" 
				min="0"></td>
			</tr>
			<tr>
				<th colspan="4" style="text-align: right;">Overs</th>
				<td><input type="number" readonly 
				id="overs${inns}" 
                name="${inningsDataField.OVERS}${inns}" 
				min="0"></td>
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
                        id="bowler${inns}${bowl}" 
						name="${playerDataField.BOWLER}${inns}${bowl}"></td>
				<td><select name="${playerDataField.OVERS}${inns}${bowl}"
				    id="overs${inns}${bowl}"
				    onchange="refreshOvers(${inns})">
					<#list 0..3 as overs>
						<option value="${overs * 6}">${overs}</option>
					</#list>
					<#list 0..2 as overs>
						<#list 1..5 as balls>
							<option value="${overs * 6 + balls}">${overs}.${balls}</option>
						</#list>
					</#list>
				</select></td>
				<td><input type="number" name="${playerDataField.RUNS_CONCEDED}${inns}${bowl}" 
				    id="runsConceded${inns}${bowl}" min="0"></td>
				<td><input type="number" 
				    name="${playerDataField.WICKETS}${inns}${bowl}"
					id="wickets${inns}${bowl}" readonly min="0"></td>
			</tr>
			</#list>
		</table>
	</fieldset>
	</#list>
	<br /> <br /> <br /> <br /> <input type="submit" value="Submit">
	<input type="reset" value="Reset">
</form>
