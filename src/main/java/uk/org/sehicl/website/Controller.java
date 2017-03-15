package uk.org.sehicl.website;

import java.text.ParseException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.org.sehicl.website.navigator.Section;
import uk.org.sehicl.website.page.ArchiveIndexPage;
import uk.org.sehicl.website.page.ContactsPage;
import uk.org.sehicl.website.page.DateResultsPage;
import uk.org.sehicl.website.page.HomePage;
import uk.org.sehicl.website.page.LeagueBattingAveragesPage;
import uk.org.sehicl.website.page.LeagueBowlingAveragesPage;
import uk.org.sehicl.website.page.LeagueFixturesPage;
import uk.org.sehicl.website.page.LeagueTablePage;
import uk.org.sehicl.website.page.LeagueTablesPage;
import uk.org.sehicl.website.page.SeasonArchiveIndexPage;
import uk.org.sehicl.website.page.StaticPage;
import uk.org.sehicl.website.page.TeamAveragesIndexPage;
import uk.org.sehicl.website.page.TeamAveragesPage;
import uk.org.sehicl.website.page.TeamFixturesPage;
import uk.org.sehicl.website.report.LeagueSelector;
import uk.org.sehicl.website.template.Template;

@RestController
public class Controller
{
    private String getRequestUri(HttpServletRequest req)
    {
        String answer = Stream
                .of(req.getRequestURI(), req.getPathInfo())
                .filter(Objects::nonNull)
                .collect(Collectors.joining());
        return answer;
    }

    @RequestMapping("/")
    public String home(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new Template(new HomePage(uri)).process();
    }

    @RequestMapping("/contacts")
    public String contacts(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new Template(new ContactsPage(uri)).process();
    }

    @RequestMapping("/resources")
    public String resources(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new Template(new StaticPage("resources", "resources.ftlh", Section.RESOURCES, uri,
                "SEHICL Resources")).process();
    }

    @RequestMapping("/rules")
    public String rules(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new Template(
                new StaticPage("rules", "rules.ftlh", Section.RULES, uri, "SEHICL Rules"))
                        .process();
    }

    @RequestMapping("/records")
    public String recordsIndex(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new Template(new StaticPage("records", "records/index.ftlh", Section.RECORDS, uri,
                "SEHICL Records")).process();
    }

    @RequestMapping("/records/performances")
    public String recordPerformances(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new Template(new StaticPage("records", "records/performances.ftlh", Section.RECORDS,
                uri, "SEHICL Record Performances")).process();
    }

    @RequestMapping("/records/winners")
    public String divisionalWinners(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new Template(new StaticPage("divwinners", "records/divwinners.ftlh", Section.RECORDS,
                uri, "SEHICL Divisional Winners")).process();
    }

    @RequestMapping("/records/awards")
    public String individualAwards(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new Template(new StaticPage("awards", "records/individualawards.ftlh",
                Section.RECORDS, uri, "SEHICL Individual Awards")).process();
    }

    @RequestMapping("/records/fairplay")
    public String fairplay(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new Template(new StaticPage("fairplay", "records/fairplay.ftlh", Section.RECORDS,
                uri, "SEHICL Sporting & Efficiency")).process();
    }

    @RequestMapping("/archive/presentation/{season}")
    public String presentationEvening(HttpServletRequest req, @PathVariable String season)
    {
        String uri = getRequestUri(req);
        return new Template(new StaticPage("presentation",
                String.format("presentation/%s.ftlh", season), Section.ARCHIVE, uri,
                String.format("SEHICL Presentation Evening %s", season))).process();
    }

    @RequestMapping("/tables")
    public String currentTables(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new Template(new LeagueTablesPage(uri)).process();
    }

    @RequestMapping("/tables/league/{leagueId}")
    public String currentTable(HttpServletRequest req, @PathVariable String leagueId)
    {
        String uri = getRequestUri(req);
        return new Template(new LeagueTablePage(leagueId, uri)).process();
    }

    @RequestMapping("/archive/table/{leagueId}/{season}")
    public String archiveTable(HttpServletRequest req, @PathVariable String leagueId,
            @PathVariable int season)
    {
        String uri = getRequestUri(req);
        return new Template(new LeagueTablePage(leagueId, season, uri)).process();
    }

    @RequestMapping("/averages/batting/{selector}")
    public String currentBattingAverages(HttpServletRequest req, @PathVariable String selector)
    {
        String uri = getRequestUri(req);
        return new Template(
                new LeagueBattingAveragesPage(LeagueSelector.valueOf(selector.toUpperCase()), uri))
                        .process();
    }

    @RequestMapping("/archive/batting/{selector}/{season}")
    public String archiveBattingAverages(HttpServletRequest req, @PathVariable String selector,
            @PathVariable int season)
    {
        String uri = getRequestUri(req);
        return new Template(new LeagueBattingAveragesPage(
                LeagueSelector.valueOf(selector.toUpperCase()), season, uri)).process();
    }

    @RequestMapping("/averages/bowling/{selector}")
    public String currentBowlingAverages(HttpServletRequest req, @PathVariable String selector)
    {
        String uri = getRequestUri(req);
        return new Template(
                new LeagueBowlingAveragesPage(LeagueSelector.valueOf(selector.toUpperCase()), uri))
                        .process();
    }

    @RequestMapping("/archive/bowling/{selector}/{season}")
    public String archiveBowlingAverages(HttpServletRequest req, @PathVariable String selector,
            @PathVariable int season)
    {
        String uri = getRequestUri(req);
        return new Template(new LeagueBowlingAveragesPage(
                LeagueSelector.valueOf(selector.toUpperCase()), season, uri)).process();
    }

    @RequestMapping("/averages/team/{teamId}")
    public String currentTeamAverages(HttpServletRequest req, @PathVariable String teamId)
    {
        String uri = getRequestUri(req);
        return new Template(new TeamAveragesPage(teamId, uri)).process();
    }

    @RequestMapping("/archive/teamAverages/{teamId}/{season}")
    public String archiveTeamAverages(HttpServletRequest req, @PathVariable String teamId,
            @PathVariable int season)
    {
        String uri = getRequestUri(req);
        return new Template(new TeamAveragesPage(teamId, season, uri)).process();
    }

    @RequestMapping("/averages/byTeam")
    public String teamAveragesIndex(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new Template(new TeamAveragesIndexPage(uri)).process();
    }

    @RequestMapping("/averages")
    public String averagesIndex(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new Template(new StaticPage("averages", "averagesindex.ftlh", Section.AVERAGES, uri,
                "SEHICL Averages")).process();
    }

    @RequestMapping("/archive")
    public String archiveIndex(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new Template(new ArchiveIndexPage(uri)).process();
    }

    @RequestMapping("/archive/season/{season}")
    public String seasonArchiveIndex(HttpServletRequest req, @PathVariable int season)
    {
        String uri = getRequestUri(req);
        return new Template(new SeasonArchiveIndexPage(uri, season)).process();
    }

    @RequestMapping("/fixtures/team/{teamId}")
    public String teamFixtures(HttpServletRequest req, @PathVariable String teamId)
    {
        String uri = getRequestUri(req);
        return new Template(new TeamFixturesPage(teamId, uri)).process();
    }

    @RequestMapping("/fixtures/team/{teamId}/{season}")
    public String archiveTeamFixtures(HttpServletRequest req, @PathVariable String teamId,
            @PathVariable int season)
    {
        String uri = getRequestUri(req);
        return new Template(new TeamFixturesPage(teamId, season, uri)).process();
    }

    @RequestMapping("/fixtures")
    public String leagueFixtures(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new Template(new LeagueFixturesPage(uri)).process();
    }

    @RequestMapping("/fixtures/league/{leagueId}")
    public String leagueFixtures(HttpServletRequest req, @PathVariable String leagueId)
    {
        String uri = getRequestUri(req);
        return new Template(new LeagueFixturesPage(leagueId, uri)).process();
    }

    @RequestMapping("/results")
    public String dateResultsLatest(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new Template(new DateResultsPage(uri)).process();
    }

    @RequestMapping("/results/date/{date}")
    public String dateResultsLatest(HttpServletRequest req, @PathVariable String date) throws ParseException
    {
        String uri = getRequestUri(req);
        return new Template(new DateResultsPage(DateUtils.parseDate(date, "yyyyMMdd"),uri)).process();
    }
}