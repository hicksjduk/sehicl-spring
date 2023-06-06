<#include "presentation.ftlh"/>

<h1>Presentation Evening</h1>
<p>The Presentation Evening for the 2022/23 season was held at
		Fareham Leisure Centre on Sunday 2nd April.</p>

<@section "Colts">
	<@games>
 		<@exhibitionMatch "Under-13"
            innings("Waterlooville", 135, 1, 12, "C Carson 22*", "R Reynolds 22*", "L Harradine 20*")
			innings("Under-13 Select VI", 73, 6, 5.4, "J Wheeler 28", "S Wheeler 27", "J Bishop 2/15")
		/>
		<tr>
			<td colspan="2"><img border="0" src="/graphics/presentation2023/U13Select.jpg"
				alt="Colts Under-13 Select VI"><br>The Colts Under-13 Select side
		</tr>
		<@exhibitionMatch "Under-16"
			innings("Portsmouth A", 75, 6, 9, "S Harradine 2/13", "D Bradley-Turner 2/24", "K Prior 2/30")
            innings("Under-16 Select VI", 77, 4, 9.4, "S Harradine 25*", "C Collins 3/16")
		/>
		<tr>
			<td colspan="2"><img border="0" src="/graphics/presentation2023/U16Select.jpg"
				alt="Colts Under-16 Select VI"><br>The Colts Under-16 Select side

		</tr>
	</@games>

 	<@awards "The Colts awards were presented by League Chairman Richard Matthews.">
		<@award "u16" "Portsmouth A" "/graphics/presentation2023/U16.jpg"/>
		<@award "u13" "Waterlooville"  "/graphics/presentation2023/U13.jpg"/>
		<@award "u16bat" "Samuel Harradine (Hambledon Jets)"  "/graphics/presentation2023/U16Batting.jpg"/>	
		<@award "u16bowl" "Freddie Hayes (Hambledon Sharks)" "/graphics/presentation2023/U16Bowling.jpg" 
		"Presented by Mark Le-Clercq at Hambledon's home ground, as Freddie was not available to attend the presentation evening"/>	
		<@award "u13bat" "Luke Harradine (Waterlooville)" "/graphics/presentation2023/U13Batting.jpg"/>	
		<@award "u13bowl" "Jayden Pay (Havant)" "/graphics/presentation2023/U13Bowling.jpg"/>	
		<@award "cfair" "Waterlooville Under-16" "/graphics/presentation2023/ColtsFairplay.jpg"/>	
	</@awards>
</@section>

<@section "Senior">
	<@games>
		<@exhibitionMatch "The Mayor of Fareham's VIII v Division 1 Champions' VIII"
			innings("Hambledon A", 105, 7, 12, "M Butcher 27", "S Le-Clercq 21", "B White 3/23")
            innings("The Mayor of Fareham's VIII", 106, 4, 10.1, "S Hillman 26*", "A Reynolds 25*",
                "J Oliver 2/37") 8/>
		<tr>
			<td colspan="2"><img border="0" src="/graphics/presentation2023/MayorsTeam.jpg"
				alt="Mayor of Fareham's VIII"><br>The Mayor of Fareham's VIII

		</tr>
		<#--tr>
			<td colspan="2"><img border="0" src="/graphics/presentation2023/MayorsTrophy.jpg"
				alt="Mayor's game winners"><br>The trophy for the winners of the Mayor's game is presented
				to the captain of Havant A 

		</tr-->
		<@exhibitionMatch "Division 2 Champions v Division 3 Champions"
			innings("Hambledon B", 114, 5, 12, "C Boyd 29*", "O Bembridge 25", "C Dixon 23", "B Eales 3/37")
			innings("Portsmouth A", 115, 4, 10.3, "T Jones 28*", "J Stretton 27*")/>
		<@exhibitionMatch "Division 4 Champions v Committee/Umpires"
			innings("Barracuda", 155, 3, 12, "H Hovey 49*", "J Baker 27*", 
				"M Wake 23*")
			innings("Committee/Umpires", 84, 5, 12, "T Newman 26*", "M Farrington 3/18")/>
	</@games>
	<@awards "The Senior awards were presented by Cllr Mike Ford and Mrs Anne Ford, Mayor
		and Mayoress of Fareham, assisted by League Chairman Richard Matthews.">
		<@award	"d1" "Hambledon A"  "/graphics/presentation2023/Div1.jpg"/>
		<@award "d2" "Portsmouth A",  "/graphics/presentation2023/Div2.jpg"/>
		<@award "d3" "Hambledon B"  "/graphics/presentation2023/Div3.jpg"/>
		<@award "d4" "Barracuda"  "/graphics/presentation2023/Div4.jpg"/>
		<@award "sbat" "Harry Hovey (Barracuda)"  "/graphics/presentation2023/SeniorBatting.jpg"/>
		<@award "sbowl" "James Headen (Fareham & Crofton)"  "/graphics/presentation2023/SeniorBowling.jpg"/>
		<@award "sfair" "Gosport Borough A"  "/graphics/presentation2023/SeniorFairplay.jpg"/>
	</@awards>
</@section>
