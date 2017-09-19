<h1>Archive: Season ${(season + 1999)?string.computer}-${season?string("00")}</h1>
<table>
	<thead>
		<tr>
			<th>Tables</th>
			<th></th>
			<th>Averages</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>
				<ul>
					<li><a href="/archive/table/Division1/${season}">Division 1</a></li>
					<li><a href="/archive/table/Division2/${season}">Division 2</a></li>
					<li><a href="/archive/table/Division3/${season}">Division 3</a></li>
					<li><a href="/archive/table/Division4/${season}">Division 4</a></li>
					<li><a href="/archive/table/ColtsUnder16/${season}">Colts Under-16</a></li>
					<li><a href="/archive/table/ColtsUnder13/${season}">Colts Under-13</a></li>
				</ul>
			</td>
			<td width="30"></td>
			<td>
				<ul>
					<li><a href="/archive/batting/Senior/${season}">Senior Batting</a></li>
					<li><a href="/archive/bowling/Senior/${season}">Senior Bowling</a></li>
					<li><a href="/archive/batting/ColtsUnder16/${season}">Colts Under-16 Batting</a></li>
					<li><a href="/archive/bowling/ColtsUnder16/${season}">Colts Under-16 Bowling</a></li>
					<li><a href="/archive/batting/ColtsUnder13/${season}">Colts Under-13 Batting</a></li>
					<li><a href="/archive/bowling/ColtsUnder13/${season}">Colts Under-13 Bowling</a></li>
				</ul>
			</td>
		</tr>
		<#if (season >= 8)> 
		<tr>
			<td colspan="3" align="center"><a href="/archive/presentation/${(2000 + season)?string.computer}">
				Presentation Evening</a></td>
		</tr>
		</#if>
	</tbody>
</table>
