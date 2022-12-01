package uk.org.sehicl.website.template;

import java.io.OutputStreamWriter;
import java.text.ParseException;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import uk.org.sehicl.website.navigator.Section;
import uk.org.sehicl.website.page.ContactsPage;
import uk.org.sehicl.website.page.DateResultsPage;
import uk.org.sehicl.website.page.HomePage;
import uk.org.sehicl.website.page.LeagueBattingAveragesPage;
import uk.org.sehicl.website.page.LeagueFixturesPage;
import uk.org.sehicl.website.page.LeagueResultsPage;
import uk.org.sehicl.website.page.LoginPage;
import uk.org.sehicl.website.page.StaticPage;
import uk.org.sehicl.website.page.TeamFixturesPage;
import uk.org.sehicl.website.report.LeagueSelector;
import uk.org.sehicl.website.users.UserManager;

public class TemplateTest
{
    public void testHomePage()
    {
        new PageTemplate(new HomePage("/")).process(new OutputStreamWriter(System.out));
    }

    public void testContactsPage()
    {
        new PageTemplate(new ContactsPage("/contacts")).process(new OutputStreamWriter(System.out));
    }

    public void testResourcesPage()
    {
        new PageTemplate(new StaticPage("resources", "resources.ftlh", Section.RESOURCES,
                "/resources", "SEHICL Resources")).process(new OutputStreamWriter(System.out));
    }

    public void testRulesPage()
    {
        new PageTemplate(
                new StaticPage("rules", "rules.ftlh", Section.RULES, "/rules", "SEHICL Rules"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testRecordsIndexPage()
    {
        new PageTemplate(new StaticPage("records", "records/index.ftlh", Section.RECORDS,
                "/records", "SEHICL Records")).process(new OutputStreamWriter(System.out));
    }

    public void testRecordPerformancesPage()
    {
        new PageTemplate(new StaticPage("records", "records/performances.ftlh", Section.RECORDS,
                "/records/performances", "SEHICL Record Performances"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testDivisionalWinnersPage()
    {
        new PageTemplate(new StaticPage("records", "records/divwinners.ftlh", Section.RECORDS,
                "/records/winners", "SEHICL Divisional Winners"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testIndividualAwardsPage()
    {
        new PageTemplate(new StaticPage("records", "records/individualawards.ftlh", Section.RECORDS,
                "/records/awards", "SEHICL Individual Awards"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testFairplayPage()
    {
        new PageTemplate(new StaticPage("records", "records/fairplay.ftlh", Section.RECORDS,
                "/records/fairplay", "SEHICL Sporting & Efficiency"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testPresentationPage2008()
    {
        new PageTemplate(new StaticPage("presentation", "presentation/2008.ftlh", Section.ARCHIVE,
                "/archive/presentation/2008", "SEHICL Presentation Evening 2008"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testPresentationPage2009()
    {
        new PageTemplate(new StaticPage("presentation", "presentation/2009.ftlh", Section.ARCHIVE,
                "/archive/presentation/2009", "SEHICL Presentation Evening 2009"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testPresentationPage2010()
    {
        new PageTemplate(new StaticPage("presentation", "presentation/2010.ftlh", Section.ARCHIVE,
                "/archive/presentation/2010", "SEHICL Presentation Evening 2010"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testPresentationPage2011()
    {
        new PageTemplate(new StaticPage("presentation", "presentation/2011.ftlh", Section.ARCHIVE,
                "/archive/presentation/2011", "SEHICL Presentation Evening 2011"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testPresentationPage2012()
    {
        new PageTemplate(new StaticPage("presentation", "presentation/2012.ftlh", Section.ARCHIVE,
                "/archive/presentation/2012", "SEHICL Presentation Evening 2012"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testPresentationPage2013()
    {
        new PageTemplate(new StaticPage("presentation", "presentation/2013.ftlh", Section.ARCHIVE,
                "/archive/presentation/2013", "SEHICL Presentation Evening 2013"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testPresentationPage2014()
    {
        new PageTemplate(new StaticPage("presentation", "presentation/2014.ftlh", Section.ARCHIVE,
                "/archive/presentation/2014", "SEHICL Presentation Evening 2014"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testPresentationPage2015()
    {
        new PageTemplate(new StaticPage("presentation", "presentation/2015.ftlh", Section.ARCHIVE,
                "/archive/presentation/2015", "SEHICL Presentation Evening 2015"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testPresentationPage2016()
    {
        new PageTemplate(new StaticPage("presentation", "presentation/2016.ftlh", Section.ARCHIVE,
                "/archive/presentation/2016", "SEHICL Presentation Evening 2016"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testPresentationPage2017()
    {
        new PageTemplate(new StaticPage("presentation", "presentation/2017.ftlh", Section.ARCHIVE,
                "/archive/presentation/2017", "SEHICL Presentation Evening 2017"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testCurrentAveragesPage()
    {
        new PageTemplate(
                new LeagueBattingAveragesPage(LeagueSelector.SENIOR, "/averages/batting/Senior"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testTeamFixturesPage()
    {
        new PageTemplate(new TeamFixturesPage("IBMSouthHants", "/fixtures/team/IBMSouthHants"))
                .process(new OutputStreamWriter(System.out));
    }

    public void testArchiveTeamFixturesPage()
    {
        new PageTemplate(
                new TeamFixturesPage("IBMSouthHants", 15, "/archive/teamFixtures/IBMSouthHants/15"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testLeagueFixturesPageAllLeagues()
    {
        new PageTemplate(new LeagueFixturesPage("/fixtures"))
                .process(new OutputStreamWriter(System.out));
    }

    public void testLeagueFixturesPageOneLeague()
    {
        new PageTemplate(new LeagueFixturesPage("Division4", "/fixtures/league/Division4"))
                .process(new OutputStreamWriter(System.out));
    }

    public void testDateResultsNoDate()
    {
        new PageTemplate(new DateResultsPage("/results"))
                .process(new OutputStreamWriter(System.out));
    }

    public void testDateResultsWithDate() throws ParseException
    {
        new PageTemplate(new DateResultsPage(DateUtils.parseDate("2016/11/06", "yyyy/MM/dd"),
                "/results/date/20161106")).process(new OutputStreamWriter(System.out));
    }

    public void testLeagueResults() throws ParseException
    {
        new PageTemplate(new LeagueResultsPage("Division3", "/results/league/Division3"))
                .process(new OutputStreamWriter(System.out));
    }

    @Test
    public void testLoginPage()
    {
        new PageTemplate(new LoginPage("/login", (UserManager) null))
                .process(new OutputStreamWriter(System.out));
    }
}
