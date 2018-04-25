function batFirstChanged(selection)
{
	if (selection == null)
	{
		selection = document.getElementById("battingfirst").selectedIndex;
	}
	var bat1Name = [match.homeTeam, match.awayTeam][selection].name;
	document.getElementById("bat1TeamName").innerHTML = "Innings of " + bat1Name;
	var bat2Name = [match.awayTeam, match.homeTeam][selection].name;
	document.getElementById("bat2TeamName").innerHTML = "Innings of " + bat2Name;
	for (let inns = 0; inns < 2; inns++)
	{
		var batsmen = inns == selection ? 'homeplayers' : 'awayplayers' 
		var bowlers = inns == selection ? 'awayplayers' : 'homeplayers'
		for (let p = 1; p <= 6; p++)
		{
			document.getElementsByName("batsman" + (inns + 1) + p)[0].setAttribute('list', batsmen)
			document.getElementsByName("bowlerwicket" + (inns + 1) + p)[0].setAttribute('list', bowlers)
			document.getElementsByName("bowler" + (inns + 1) + p)[0].setAttribute('list', bowlers)
		}
	}
}


function howOutChanged(innings, batsman)
{
	var value = document.getElementById("howout" + innings + batsman).value;
	var score = value.indexOf("score") != -1;
	document.getElementsByName("runsScored" + innings + batsman)[0].disabled = !score;
	var bowler = value.indexOf("bowler") != -1;
	document.getElementsByName("bowlerwicket" + innings + batsman)[0].disabled = !bowler;
	refreshWicketCount(innings);
	refreshBowlerWickets(innings);
}

function refreshWicketCount(innings)
{
	var wickets = 0;
	for (let bat = 1; bat < 7; bat++)
	{
		if (document.getElementById("howout" + innings + bat).value.indexOf(";out") != -1)
		{
			wickets++;
		}
	}
	document.getElementById("wickets" + innings).value = wickets;
}

function refreshTotal(innings)
{
	var total = Number(document.getElementsByName("extras" + innings)[0].value) || 0;
	for (let bat = 1; bat < 7; bat++)
	{
		total = total + Number(document.getElementsByName("runsScored" + innings + bat)[0].value) || 0;
	}
	document.getElementById("total" + innings).value = total;
}

function refreshOvers(innings)
{
	var balls = 0;
	for (let bowler = 1; bowler < 7; bowler++)
	{
		balls += parseInt(document.getElementsByName("overs" + innings + bowler)[0].value);
	}
	document.getElementById("overs" + innings).value = toOvers(balls);
}

function refreshBowlerWickets(innings)
{
	var wkByBowler = new Object();
	for (let batsman = 1; batsman <= 6; batsman++)
	{
		var howout = document.getElementById("howout" + innings + batsman).value;
		if (howout.indexOf("bowler") != -1)
		{
			var bowler = document.getElementsByName("bowlerwicket" + innings + batsman)[0].value;
			if (bowler != "")
			{
				wkByBowler[bowler] = ((bowler in wkByBowler) ? wkByBowler[bowler] : 0) + 1;
			}
		}
	}
	for (let bowler = 1; bowler <= 6; bowler++)
	{
		var name = document.getElementsByName("bowler" + innings + bowler)[0].value;
		var wickets = "";
		if (name != "")
		{
			wickets = (name in wkByBowler) ? wkByBowler[name] : 0;
		}
		document.getElementsByName("wickets" + innings + bowler)[0].value = wickets;
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