function initPage()
{
	if (match.inningsData == null)
		return null;
	battingFirstField().selectedIndex = match.inningsData[0].homeTeamBatting ? 0 : 1;
	alert("Hello");
	batsmanNameField(1, 1).value = "Jeremy";
	batFirstChanged();
}

function batFirstChanged(selection)
{
	if (selection == null)
	{
		selection = battingFirstField().selectedIndex;
	}
	setTeamName(1, [match.homeTeam, match.awayTeam][selection].name)
	setTeamName(2, [match.awayTeam, match.homeTeam][selection].name)
	for (inns = 0; inns < 2; inns++)
	{
		var batsmen = inns == selection ? 'homeplayers' : 'awayplayers' 
		var bowlers = inns == selection ? 'awayplayers' : 'homeplayers'
		for (p = 1; p <= 6; p++)
		{
			batsmanNameField(inns + 1, p).setAttribute('list', batsmen)
			bowlerTakingWicketField(inns + 1, p).setAttribute('list', bowlers)
			bowlerNameField(inns + 1, p).setAttribute('list', bowlers)
		}
	}
}

function battingFirstField()
{
	return document.getElementById("battingfirst")
}

function setTeamName(batSequence, name)
{
	document.getElementById("bat" + batSequence + "TeamName").innerHTML = "Innings of " + name;
}

function batsmanNameField(inns, sequence)
{
	return document.getElementById("batsman" + inns + sequence)
}

function bowlerTakingWicketField(inns, sequence)
{
	return document.getElementById("wicketbowler" + inns + sequence)
}

function howOutField(inns, sequence)
{
	return document.getElementById("howout" + inns + sequence)
}

function runsScoredField(inns, sequence)
{
	return document.getElementById("runsscored" + inns + sequence)
}

function bowlerNameField(inns, sequence)
{
	return document.getElementById("bowler" + inns + sequence)
}

function bowlerWicketsField(inns, sequence)
{
	return document.getElementById("wickets" + inns + sequence)
}

function howOutChanged(innings, batsman)
{
	var value = howOutField(innings, batsman).value;
	var score = value.indexOf("score") != -1;
	runsScoredField(innings, batsman).disabled = !score;
	var bowler = value.indexOf("bowler") != -1;
	bowlerTakingWicketField(innings, batsman).disabled = !bowler;
	refreshWicketCount(innings);
	refreshBowlerWickets(innings);
}

function refreshWicketCount(innings)
{
	var wickets = 0;
	for (bat = 1; bat < 7; bat++)
	{
		if (howOutField(innings, bat).value.indexOf(";out") != -1)
		{
			wickets++;
		}
	}
	document.getElementById("wickets" + innings).value = wickets;
}

function refreshTotal(innings)
{
	var total = Number(document.getElementById("extras" + innings).value) || 0;
	for (bat = 1; bat <= 6; bat++)
	{
		total = total + Number(runsScoredField(innings, bat).value) || 0;
	}
	document.getElementById("total" + innings).value = total;
}

function refreshOvers(innings)
{
	var balls = 0;
	for (bowler = 1; bowler < 7; bowler++)
	{
		balls += Number(document.getElementById("overs" + innings + bowler).value);
	}
	document.getElementById("overs" + innings).value = toOvers(balls);
}

function refreshBowlerWickets(innings)
{
	var wkByBowler = new Object();
	for (batsman = 1; batsman <= 6; batsman++)
	{
		var howout = howOutField(innings, batsman).value;
		if (howout.indexOf("bowler") != -1)
		{
			var bowler = bowlerTakingWicketField(innings, batsman).value;
			if (bowler != "")
			{
				wkByBowler[bowler] = ((bowler in wkByBowler) ? wkByBowler[bowler] : 0) + 1;
			}
		}
	}
	for (bowler = 1; bowler <= 6; bowler++)
	{
		var name = bowlerNameField(innings, bowler).value;
		var wickets = "";
		if (name != "")
		{
			wickets = (name in wkByBowler) ? wkByBowler[name] : 0;
		}
		bowlerWicketsField(innings, bowler).value = wickets;
	}
}

function toBalls(overs)
{
	var oversBowled = Number(overs);
	return Math.floor(oversBowled) * 6 + Math.floor(oversBowled * 10 % 10);
}

function toOvers(balls)
{
	return Math.floor(balls / 6) + (balls % 6) / 10;
}