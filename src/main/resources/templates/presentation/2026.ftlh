<#include "presentation.ftlh"/>

<h1>Presentation Evening</h1>
<p>The Presentation Evening for the 2025/26 season was held at
		Fareham Leisure Centre on Sunday 19th April.</p>

<@section "Colts">
	<@games>
 		<@exhibitionMatch "Under-13"
            innings("Havant", 113, 4, 12, "O Concannon*", "J Newman 2/24", "T Moore 2/38")
			innings("Under-13 Select VI", 114, 5, 12, "T Moore 29", "J Cooper 26*")
		/>
		<tr>
			<td colspan="2"><img border="0" src="/graphics/presentation2026/U13Select.JPG"
				alt="Colts Under-13 Select VI"><br>The Colts Under-13 Select side
		</tr>
		<@exhibitionMatch "Under-16"
            innings("Waterlooville", 104, 6, 9.1, "H Miller 34", "R Reynolds 28", 
                "H Ainsworth 3/20", "J Pay 2/28")
            innings("Under-16 Select VI", 105, 4, 10.4, "J Pay 40*")
		/>
		<tr>
			<td colspan="2"><img border="0" src="/graphics/presentation2026/U16Select.JPG"
				alt="Colts Under-16 Select VI"><br>The Colts Under-16 Select side

		</tr>
	</@games>

 	<@awards "The Colts awards were presented by League Chairman Richard Matthews.">
		<@award "u16" "Waterlooville" "/graphics/presentation2026/U16.JPG"/>
		<@award "u13" "Havant"  "/graphics/presentation2026/U13.JPG"/>
		<@award "u16bat" "Alex Varney (Locks Heath)"  "/graphics/presentation2026/U16Batting.JPG"/>	
		<@award "u16bowl" "Louie De Bohan (Waterlooville)" "/graphics/presentation2026/U16Bowling.JPG"/>
		<@award "u13bat" "Teddy Moore (Gosport Borough)" "/graphics/presentation2026/U13Batting.JPG"/>	
		<@award "u13bowl" "Harry Freemantle (Sarisbury Athletice)" "/graphics/presentation2026/U13Bowling.JPG"/>	
		<@award "cfair" "Waterlooville Under-16" "/graphics/presentation2026/ColtsFairplay.JPG"/>	
	</@awards>
</@section>

<@section "Senior">
	<@games>
		<@exhibitionMatch "The Mayor of Fareham's VIII v Division 1 Champions' VIII"
            innings("Waterlooville A", 114, 7, 12, "A Reynolds 29", "K Muthuraman 3/31")
            innings("The Mayor of Fareham's VIII", 116, 4, 10.4, "M Bradley 26*", "B Rajan 25*",
                "S Worsley 23") 
            8/>
		<tr>
			<td colspan="2"><img border="0" src="/graphics/presentation2026/MayorsTeam.JPG"
				alt="Mayor of Fareham's VIII"><br>The Mayor of Fareham's VIII

		</tr>
		<#--
		<tr>
			<td colspan="2"><img border="0" src="/graphics/presentation2026/MayorsTrophy.JPG"
				alt="Mayor's game winners"><br>The trophy for the winners of the Mayor's game is presented
				to the captain of the Mayor of Fareham's VIII 

		</tr>
		-->
		<@exhibitionMatch "Division 2 Champions v Division 3 Champions"
		<#--
			innings("Havant", 93, 6, 10, "P Hopson 35", "S Jones 2/19")
			innings("Portsmouth", 94, 0, 5.3, "H Gadd 29*", "Y Fernando 27*", "J Kooner-Evans 26*")
		-->
		/>
		<@exhibitionMatch "Division 4 Champions v Committee/Umpires"
			innings("Superkings", 177, 0, 10, "B Varghese 38*", "N Joy 29*",
			     "S Shajahan 28*", "Q Naveed 26*", "K Patel 25*")
			innings("Committee/Umpires", 177, 1, 10, "J Lewis 47*", "A Tucker 28*",
			     "A Smith 27*", "D Frostick 26*")/>
	</@games>
	<@awards "The Senior awards were presented by Cllr Louise Clubley, Mayor
		of Fareham, assisted by League Chairman Richard Matthews.">
		<@award	"d1" "Waterlooville A"  "/graphics/presentation2026/Div1.JPG"/>
		<@award "d2" "Portsmouth A",  "/graphics/presentation2026/Div2.JPG"/>
		<@award "d3" "Hambledon C"  "/graphics/presentation2026/Div3.JPG"/>
		<@award "d4" "Superkings"  "/graphics/presentation2026/Div4.JPG"/>
		<@award "sbat" "Matt Bradley (Clanfield A)" "/graphics/presentation2026/SeniorBatting.JPG"/>
		<@award "sbowl" "Jamie Lewis (Hambledon B)"  "/graphics/presentation2026/SeniorBowling.JPG"/>
		<@award "sfair" "Emsworth"  "/graphics/presentation2026/SeniorFairplay.JPG"/>
	</@awards>
</@section>
