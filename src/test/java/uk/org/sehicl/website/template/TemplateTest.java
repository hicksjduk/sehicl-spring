package uk.org.sehicl.website.template;

import java.io.OutputStreamWriter;

import org.junit.Test;

import uk.org.sehicl.website.navigator.Section;
import uk.org.sehicl.website.page.ContactsPage;
import uk.org.sehicl.website.page.HomePage;
import uk.org.sehicl.website.page.StaticPage;

public class TemplateTest
{
    public void testHomePage()
    {
        new Template(new HomePage("/")).process(new OutputStreamWriter(System.out));
    }

    public void testContactsPage()
    {
        new Template(new ContactsPage("/contacts")).process(new OutputStreamWriter(System.out));
    }

    public void testResourcesPage()
    {
        new Template(new StaticPage("resources", "resources.ftlh", Section.RESOURCES, "/resources",
                "SEHICL Resources")).process(new OutputStreamWriter(System.out));
    }

    public void testRulesPage()
    {
        new Template(new StaticPage("rules", "rules.ftlh", Section.RULES, "/rules", "SEHICL Rules"))
                .process(new OutputStreamWriter(System.out));
    }

    public void testRecordsIndexPage()
    {
        new Template(new StaticPage("records", "records/index.ftlh", Section.RECORDS, "/records",
                "SEHICL Records")).process(new OutputStreamWriter(System.out));
    }

    public void testRecordPerformancesPage()
    {
        new Template(new StaticPage("records", "records/performances.ftlh", Section.RECORDS,
                "/records/performances", "SEHICL Record Performances"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testDivisionalWinnersPage()
    {
        new Template(new StaticPage("records", "records/divwinners.ftlh", Section.RECORDS,
                "/records/winners", "SEHICL Divisional Winners"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testIndividualAwardsPage()
    {
        new Template(new StaticPage("records", "records/individualawards.ftlh", Section.RECORDS,
                "/records/awards", "SEHICL Individual Awards"))
                        .process(new OutputStreamWriter(System.out));
    }

    @Test
    public void testFairplayPage()
    {
        new Template(new StaticPage("records", "records/fairplay.ftlh", Section.RECORDS,
                "/records/fairplay", "SEHICL Sporting & Efficiency"))
                        .process(new OutputStreamWriter(System.out));
    }
}
