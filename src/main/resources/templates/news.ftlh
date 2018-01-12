<#assign today = .now?date?string.iso/>
<#macro news start=today end=today>
	<!-- News start: ${start}, end: ${end}, today: ${today} -->
	<#if start?date.iso <= today?date.iso && today?date.iso <= end?date.iso>
		<#if !started??>
			<div id="news">
			<#assign started = true/>
		</#if>
		<p>
			<#nested/>
		</p>
	</#if>
</#macro>

<@news>
We are sad to have heard of the death of <b>Brian Ravenhall</b>. Brian was a stalwart member
of Waterlooville Cricket Club, and scored for their senior teams until relatively
recently; he also umpired in the League for several years.
We send our condolences and best wishes to Brian's family, friends and clubmates.
<br><b>A minute's silence</b> will be observed in Brian's memory before the first senior games
on Sunday 14th January; those wishing to take part are asked to be present no later than 6pm.
</@news>

<@news end="2018-01-31">
Some <b>equipment has gone missing</b> from the kit of Portsmouth senior players:
a blue Masuri helmet and a set of wicketkeeping kit - helmet, gloves and pads.
All players are asked to check their kit bags and contact 
<script language="javascript">
document.write(mailTo("neil.palmer", "live.co.uk", "Neil Palmer", "Neil Palmer"));
</script> 
if they find the missing equipment.
</@news>

<@news end="2018-03-25">
Over the past few months, the League has been <b>trying out a new ball</b> in some matches. 
These balls are more expensive than the balls we have regularly used, but are more durable 
and therefore can be reused more often. They were used in a small number of league matches 
last season, but to give them a proper trial they have been used in every Division 5 
match so far this season.
<br>
The feedback from those involved in these games has been mixed. The new ball is easier to 
see than the standard ball, due to its prominent, light-coloured seam, but it is also 
considerably bouncier, making it harder for batsmen and fielders to judge. On balance, 
the verdict has been somewhat unfavourable; therefore, the committee has concluded that 
it is time to bring the trial to an end, and will not be pursuing the option of adopting 
the new ball.
<br>
For the time being, the new-style balls remain available for use, and if the captains agree 
they may be used in any league match, in any division. However, this decision must be explicitly 
made before the match starts, and if no such agreement is reached the default assumption 
will be that a standard ball is used.
<br>
We would like to thank all those who have participated in this trial and given us their feedback.
</@news>

<#if started??>
	</div>
</#if>
