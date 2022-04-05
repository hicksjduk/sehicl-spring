<#include "presentation.ftlh"/>

<h1>Presentation Evening</h1>
<p>The Presentation Evening for the 2021/22 season was held at
		Fareham Leisure Centre on Sunday 3rd April.</p>

<@section "Colts">
	<@games>
 		<@exhibitionMatch "Under-13"
            innings("Grayshott", 111, 6, 10.3, "R Reynolds 2/19")
			innings("Under-13 Select VI", 92, 4, 12, "R Reynolds 21*", "L Mills 20*")
		/>
		<tr>
			<td colspan="2"><img border="0" src="/graphics/presentation2022/U13Select.jpg"
				alt="Colts Under-13 Select VI"><br>Two members of the Colts Under-13 Select side
		</tr>
		<@exhibitionMatch "Under-16"
			innings("Hambledon Jets", 192, 4, 12, "O Bembridge 61*", "T Restell 29*", "S Mote 28*")
            innings("Under-16 Select VI", 64, 6, 8.5, "T Restell 2/13")
		/>
		<tr>
			<td colspan="2"><img border="0" src="/graphics/presentation2022/U16Select.jpg"
				alt="Colts Under-16 Select VI"><br>The Colts Under-16 Select side

		</tr>
	</@games>

 	<@awards "The Colts awards were presented by Cllr Pam Bryant, Mayor of Fareham, 
		assisted by League Chairman Richard Matthews.">
		<@award "u16" "Hambledon Jets" "/graphics/presentation2022/U16.jpg"/>
		<@award "u13" "Grayshott"  "/graphics/presentation2022/U13.jpg"/>
		<@award "u16bat" "Ephraim Royle (Gosport Borough)"  "/graphics/presentation2022/U16Batting.jpg"/>	
		<@award "u16bowl" "Alfie Cole (Portsmouth A)"  "/graphics/presentation2022/U16Bowling.jpg"/>	
		<@award "u13bat" "Henry O'Hara (Portsmouth & Southsea)" "/graphics/presentation2022/U13Batting.jpg"/>	
		<@award "u13bowl" "Henry Ferrett (Portsmouth & Southsea)" "/graphics/presentation2022/U13Bowling.jpg"/>	
		<@award "cfair" "Hambledon Under-13" "/graphics/presentation2022/ColtsFairplay.jpg"/>	
	</@awards>
</@section>

<@section "Senior">
	<@games>
		<@exhibitionMatch "The Mayor of Fareham's VIII v Division 1 Champions' VIII"
			innings("The Mayor of Fareham's VIII", 119, 4, 12, "M Bradley 25*", 
				"J Oliver 25*")
			innings("Havant A", 120, 4, 10.3, "S Ransley 28*", "O Jones 27*") 8/>
		<tr>
			<td colspan="2"><img border="0" src="/graphics/presentation2022/MayorsTeam.jpg"
				alt="Mayor of Fareham's VIII"><br>The Mayor of Fareham's VIII

		</tr>
		<tr>
			<td colspan="2"><img border="0" src="/graphics/presentation2022/MayorsTrophy.jpg"
				alt="Mayor's game winners"><br>The trophy for the winners of the Mayor's game is presented
				to the captain of Havant A 

		</tr>
		<#--
		<@exhibitionMatch "Division 2 Champions v Division 3 Champions"
			innings("Hambledon A", 68, 6, 10.4, "B White 2/6", "M Frost 2/19")
			innings("Fareham & Crofton A", 71, 2, 5.5, "D Wimble 27*", "M Frost 25*")/>
		<@exhibitionMatch "Division 4 Champions v Division 5 Champions"
			innings("Afghan Society Portsmouth", 129, 3, 12, "H Peruzi 29*", "S Mahboob 26*", 
				"Jahanzeb Habib 20")
			innings("Waterlooville C", 58, 6, 6.2, "Hassan 3/3")/>
		-->
	</@games>
	<@awards "The Senior awards were presented by Cllr Pam Bryant, Mayor of
		Fareham, assisted by League Chairman Richard Matthews.">
		<@award	"d1" "Havant A"  "/graphics/presentation2022/Div1.jpg"/>
		<@award "d2" "Havant B",  "/graphics/presentation2022/Div2.jpg"/>
		<@award "d3" "Fareham & Crofton"  "/graphics/presentation2022/Div3.jpg"/>
		<@award "d4" "Droxford"  "/graphics/presentation2022/Div4.jpg"/>
		<@award "d5" "Havant C"  "/graphics/presentation2022/Div5.jpg"/>
		<@award "sbat" "Matt Bradley (Clanfield A)"  "/graphics/presentation2022/SeniorBatting.jpg"/>
		<@award "sbowl" "Connor Clark (Fareham & Crofton)"  "/graphics/presentation2022/SeniorBowling.jpg"/>
		<@award "sfair" "Clanfield B"  "/graphics/presentation2022/SeniorFairplay.jpg"/>
	</@awards>
</@section>
