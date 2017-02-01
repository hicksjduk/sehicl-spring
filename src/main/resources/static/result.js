/**
 * 
 */

Player = function() {}

Player.prototype.setName = function(name) {
	this.name = name;
	return this;
}

Player.prototype.setId = function(id) {
	this.id = id;
	return this;
}

Team = function() {}

Team.prototype.setName = function(name) {
	this.name = name;
	return this;
}

Team.prototype.setId = function(id) {
	this.id = id;
	return this;
}

Team.prototype.addPlayer = function(player) {
	(this.players = this.players || []).push(player);
	return this;
}

Team.prototype.getPlayerByName = function(name)
{
	var answer;
	for (let player of this.players)
	{
		if (name == player.name)
		{
			answer = player;
			break;
		}
	}
	if (!answer)
	{
		answer = new Player().setName(name);
		this.addPlayer(answer);
	}
	return answer;
}

Match = function() {}

Match.prototype.setDate = function(date) {
	this.date = date;
	return this;
}

Match.prototype.setTime = function(time) {
	this.time = time;
	return this;
}

Match.prototype.setCourt = function(court) {
	this.court = court;
	return this;
}

Match.prototype.setLeague = function(league) {
	this.league = league;
	return this;
}

Match.prototype.setHomeTeam = function(team) {
	this.homeTeam = team;
	return this;
}

Match.prototype.setAwayTeam = function(team) {
	this.awayTeam = team;
	return this;
}

Match.prototype.setFirstInnings = function(innings) {
	this.firstInnings = innings;
	return this;
}

Match.prototype.setSecondInnings = function(innings) {
	this.secondInnings = innings;
	return this;
}

Match.prototype.swapInnings = function() {
	var inns = this.firstInnings;
	this.firstInnings = this.secondInnings;
	this.secondInnings = inns;
	return this;
}

Innings = function() {}

Innings.prototype.setBattingTeam = function(team) 
{
	this.battingTeam = team;
	return this;
}

Innings.prototype.setBowlingTeam = function(team) 
{
	this.bowlingTeam = team;
	return this;
}

Innings.prototype.setExtras = function(extras) 
{
	this.extras = extras;
	return this;
}

Innings.prototype.getTotal = function()
{
	var answer = this.extras || 0;
	for (let batsman of (this.batsmen || []))
	{
		answer += (batsman.runsScored || 0);
	}
	return answer;
}

Innings.prototype.getWickets = function()
{
	var answer = 0;
	for (let batsman of (this.batsmen || []))
	{
		howOut = batsman.howOut || HowOuts.didNotBat;
		answer += howOut.isOut ? 1 : 0;
	}
	return answer;
}

Innings.prototype.setBattingDetails = function(batsmanName, runsScored, howOut, bowlerName)
{
	this.batsmen = this.batsmen || [];
	var player = this.battingTeam.getPlayerByName(batsmanName);
	var batsman;
	for (let bat of this.batsmen)
	{
		if (bat.player == player)
		{
			batsman = bat;
			break;
		}
	}
	if (!batsman)
	{
		this.batsmen.push(batsman = new Batsman(player));
	}
	batsman.setRunsScored(runsScored);
	var bowler = bowlerName ? this.bowlingTeam.getPlayerByName(bowlerName) : null;
	batsman.setHowOut(howOut, bowler);
	return this;
}

Innings.prototype.setBowlingDetails = function(bowlerName, oversBowled, runsConceded)
{
	this.bowlers = this.bowlers || [];
	var player = this.bowlingTeam.getPlayerByName(bowlerName);
	var bowler;
	for (let b of this.bowlers)
	{
		if (b.player == player)
		{
			bowler = b;
			break;
		}
	}
	if (!bowler)
	{
		this.bowlers.push(bowler = new Bowler(player));
	}
	bowler.setOversBowled(oversBowled)
	bowler.setRunsConceded(runsConceded);
	return this;
}

HowOut = function(title, isOut, creditedToBowler)
{
	this.title = title;
	this.isOut = isOut;
	this.creditedToBowler = creditedToBowler || false;
}

HowOuts = 
{
	"didNotBat": new HowOut("Did not bat", false),
	"notOut": new HowOut("Not out", false),
	"retired": new HowOut("Retired", false),
	"runOut": new HowOut("Run out", true, false),
	"bowled": new HowOut("Bowled", true, true),
	"caught": new HowOut("Caught", true, true),
	"stumped": new HowOut("Stumped", true, true),
	"lbw": new HowOut("LBW", true, true),
	"retiredHurt": new HowOut("Retired hurt", false),
	"hitWicket": new HowOut("Hit wicket", true, true),
	"handledBall": new HowOut("Handled ball", true, false),
	"obstructingField": new HowOut("Obstructing the field", true, false),
	"hitBallTwice": new HowOut("Hit ball twice", true, false),
	"timedOut": new HowOut("Timed out", true, false)
};

Batsman = function(player)
{
	this.player = player;
}

Batsman.prototype.setRunsScored = function(runsScored)
{
	this.runsScored = runsScored;
	return this;
}

Batsman.prototype.setHowOut = function(howOut, bowler)
{
	this.howOut = howOut;
	if (howOut.creditedToBowler)
	{
		this.bowler = bowler;
	}
}

Bowler = function(player)
{
	this.player = player;
}

Bowler.prototype.setOversBowled = function(oversBowled)
{
	this.ballsBowled = Math.floor(oversBowled) * 6 + (oversBowled * 10 % 10);
	return this;
}

Bowler.prototype.setRunsConceded = function(runsConceded)
{
	this.runsConceded = runsConceded;
	return this;
}

if (module) module.exports = [Match, Player, Team, Batsman, Bowler, Innings, HowOut];


function batFirstChanged()
{
	var selection = document.getElementById("battingfirst").selectedIndex;
	var bat1Name = [match.homeTeam, match.awayTeam][selection].name;
	document.getElementById("bat1TeamName").innerHTML = "Innings of " + bat1Name;
	var bat2Name = [match.awayTeam, match.homeTeam][selection].name;
	document.getElementById("bat2TeamName").innerHTML = "Innings of " + bat2Name;
}


function howOutChanged(innings, batsman)
{
	var value = document.getElementById("howout" + innings + batsman).value;
	var score = value.indexOf("score") != -1;
	document.getElementsByName("runsScored" + innings + batsman)[0].disabled = !score;
	var bowler = value.indexOf("bowler") != -1;
	document.getElementsByName("bowlerwicket" + innings + batsman)[0].disabled = !bowler;
	refreshWicketCount(innings); 
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
		balls += toBalls(document.getElementsByName("overs" + innings + bowler)[0].value);
	}
	document.getElementById("overs" + innings).value = toOvers(balls);
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