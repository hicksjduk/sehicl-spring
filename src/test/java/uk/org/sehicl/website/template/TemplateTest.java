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

    public void testFairplayPage()
    {
        new Template(new StaticPage("records", "records/fairplay.ftlh", Section.RECORDS,
                "/records/fairplay", "SEHICL Sporting & Efficiency"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testPresentationPage2008()
    {
        new Template(new StaticPage("presentation", "presentation/2008.ftlh", Section.ARCHIVE,
                "/archive/presentation/2008", "SEHICL Presentation Evening 2008"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testPresentationPage2009()
    {
        new Template(new StaticPage("presentation", "presentation/2009.ftlh", Section.ARCHIVE,
                "/archive/presentation/2009", "SEHICL Presentation Evening 2009"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testPresentationPage2010()
    {
        new Template(new StaticPage("presentation", "presentation/2010.ftlh", Section.ARCHIVE,
                "/archive/presentation/2010", "SEHICL Presentation Evening 2010"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testPresentationPage2011()
    {
        new Template(new StaticPage("presentation", "presentation/2011.ftlh", Section.ARCHIVE,
                "/archive/presentation/2011", "SEHICL Presentation Evening 2011"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testPresentationPage2012()
    {
        new Template(new StaticPage("presentation", "presentation/2012.ftlh", Section.ARCHIVE,
                "/archive/presentation/2012", "SEHICL Presentation Evening 2012"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testPresentationPage2013()
    {
        new Template(new StaticPage("presentation", "presentation/2013.ftlh", Section.ARCHIVE,
                "/archive/presentation/2013", "SEHICL Presentation Evening 2013"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testPresentationPage2014()
    {
        new Template(new StaticPage("presentation", "presentation/2014.ftlh", Section.ARCHIVE,
                "/archive/presentation/2014", "SEHICL Presentation Evening 2014"))
                        .process(new OutputStreamWriter(System.out));
    }

    public void testPresentationPage2015()
    {
        new Template(new StaticPage("presentation", "presentation/2015.ftlh", Section.ARCHIVE,
                "/archive/presentation/2015", "SEHICL Presentation Evening 2015"))
                        .process(new OutputStreamWriter(System.out));
    }

    @Test
    public void testPresentationPage2016()
    {
        new Template(new StaticPage("presentation", "presentation/2016.ftlh", Section.ARCHIVE,
                "/archive/presentation/2016", "SEHICL Presentation Evening 2016"))
                        .process(new OutputStreamWriter(System.out));
    }
}
