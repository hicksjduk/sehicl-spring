package uk.org.sehicl.website;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import uk.org.sehicl.admin.UsersExporter;
import uk.org.sehicl.website.navigator.Section;
import uk.org.sehicl.website.page.ActivatePage;
import uk.org.sehicl.website.page.ArchiveIndexPage;
import uk.org.sehicl.website.page.ContactsPage;
import uk.org.sehicl.website.page.DateResultsPage;
import uk.org.sehicl.website.page.DeleteUserPage;
import uk.org.sehicl.website.page.FullContactsPage;
import uk.org.sehicl.website.page.HomePage;
import uk.org.sehicl.website.page.LeagueBattingAveragesPage;
import uk.org.sehicl.website.page.LeagueBowlingAveragesPage;
import uk.org.sehicl.website.page.LeagueFixturesPage;
import uk.org.sehicl.website.page.LeagueResultsPage;
import uk.org.sehicl.website.page.LeagueTablePage;
import uk.org.sehicl.website.page.LeagueTablesPage;
import uk.org.sehicl.website.page.LoginPage;
import uk.org.sehicl.website.page.Page;
import uk.org.sehicl.website.page.ReconfirmPage;
import uk.org.sehicl.website.page.RegisterConfPage;
import uk.org.sehicl.website.page.RegisterPage;
import uk.org.sehicl.website.page.ResetPage;
import uk.org.sehicl.website.page.SeasonArchiveIndexPage;
import uk.org.sehicl.website.page.StaticPage;
import uk.org.sehicl.website.page.TeamAveragesIndexPage;
import uk.org.sehicl.website.page.TeamAveragesPage;
import uk.org.sehicl.website.page.TeamFixturesPage;
import uk.org.sehicl.website.page.UserDetailsPage;
import uk.org.sehicl.website.report.LeagueSelector;
import uk.org.sehicl.website.template.PageTemplate;
import uk.org.sehicl.website.users.EmailException;
import uk.org.sehicl.website.users.Login;
import uk.org.sehicl.website.users.Reconfirm;
import uk.org.sehicl.website.users.Register;
import uk.org.sehicl.website.users.Reset;
import uk.org.sehicl.website.users.User;
import uk.org.sehicl.website.users.UserException;
import uk.org.sehicl.website.users.UserManager;

@RestController
public class Controller
{
    @Autowired
    private UserManager userManager;
    @Autowired
    private UsersExporter usersExporter;

    private String getRequestUri(HttpServletRequest req)
    {
        return Stream
                .of(req.getRequestURI(), req.getPathInfo())
                .filter(Objects::nonNull)
                .collect(Collectors.joining());
    }

    @RequestMapping("/")
    public String home(HttpServletRequest req)
    {
        return new PageTemplate(new HomePage(getRequestUri(req))).process();
    }

    @RequestMapping("/contacts")
    public String contacts(HttpServletRequest req)
    {
        return new PageTemplate(new ContactsPage(getRequestUri(req))).process();
    }

    @RequestMapping("/fullContacts")
    public String fullContacts(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        final UserSession userSession = new UserSession(req);
        if (userManager.sessionHasRole(userSession.getToken(), null))
            return new PageTemplate(new FullContactsPage(getRequestUri(req))).process();
        userSession.setRedirectTarget(req.getRequestURI());
        resp.sendRedirect("/login");
        return "";
    }

    @RequestMapping("/resources")
    public String resources(HttpServletRequest req)
    {
        return new PageTemplate(new StaticPage("resources", "resources.ftlh", Section.RESOURCES,
                getRequestUri(req), "SEHICL Resources")).process();
    }

    @RequestMapping("/rules")
    public String rules(HttpServletRequest req)
    {
        return new PageTemplate(new StaticPage("rules", "rules.ftlh", Section.RULES,
                getRequestUri(req), "SEHICL Rules")).process();
    }

    @RequestMapping("/records")
    public String recordsIndex(HttpServletRequest req)
    {
        return new PageTemplate(new StaticPage("records", "records/index.ftlh", Section.RECORDS,
                getRequestUri(req), "SEHICL Records")).process();
    }

    @RequestMapping("/records/performances")
    public String recordPerformances(HttpServletRequest req)
    {
        return new PageTemplate(new StaticPage("records", "records/performances.ftlh",
                Section.RECORDS, getRequestUri(req), "SEHICL Record Performances")).process();
    }

    @RequestMapping("/records/winners")
    public String divisionalWinners(HttpServletRequest req)
    {
        return new PageTemplate(new StaticPage("divwinners", "records/divwinners.ftlh",
                Section.RECORDS, getRequestUri(req), "SEHICL Divisional Winners")).process();
    }

    @RequestMapping("/records/awards")
    public String individualAwards(HttpServletRequest req)
    {
        return new PageTemplate(new StaticPage("awards", "records/individualawards.ftlh",
                Section.RECORDS, getRequestUri(req), "SEHICL Individual Awards")).process();
    }

    @RequestMapping("/records/fairplay")
    public String fairplay(HttpServletRequest req)
    {
        return new PageTemplate(new StaticPage("fairplay", "records/fairplay.ftlh", Section.RECORDS,
                getRequestUri(req), "SEHICL Sporting & Efficiency")).process();
    }

    @RequestMapping("/presentation")
    public String presentationEvening(HttpServletRequest req)
    {
        return new PageTemplate(new StaticPage("presentation", "presentation/schedule.ftlh",
                Section.HOME, getRequestUri(req), "SEHICL Presentation Evening")).process();
    }

    @RequestMapping("/archive/presentation/{season}")
    public String presentationEvening(HttpServletRequest req, @PathVariable String season)
    {
        return new PageTemplate(new StaticPage("presentation",
                String.format("presentation/%s.ftlh", season), Section.ARCHIVE, getRequestUri(req),
                String.format("SEHICL Presentation Evening %s", season))).process();
    }

    @RequestMapping("/tables")
    public String currentTables(HttpServletRequest req)
    {
        return new PageTemplate(new LeagueTablesPage(getRequestUri(req))).process();
    }

    @RequestMapping("/tables/league/{leagueId}")
    public String currentTable(HttpServletRequest req, @PathVariable String leagueId)
    {
        return new PageTemplate(new LeagueTablePage(leagueId, getRequestUri(req))).process();
    }

    @RequestMapping("/archive/table/{leagueId}/{season}")
    public String archiveTable(HttpServletRequest req, @PathVariable String leagueId,
            @PathVariable int season)
    {
        Page page = season <= 5
                ? new StaticPage("archive", String.format("archive%d/%s.html", season, leagueId),
                        Section.ARCHIVE, getRequestUri(req), "SEHICL Archive")
                : new LeagueTablePage(leagueId, season, getRequestUri(req));
        return new PageTemplate(page).process();
    }

    @RequestMapping("/averages/batting/{selector}")
    public String currentBattingAverages(HttpServletRequest req, @PathVariable String selector)
    {
        return new PageTemplate(new LeagueBattingAveragesPage(
                LeagueSelector.valueOf(selector.toUpperCase()), getRequestUri(req))).process();
    }

    @RequestMapping("/archive/batting/{selector}/{season}")
    public String archiveBattingAverages(HttpServletRequest req, @PathVariable String selector,
            @PathVariable int season)
    {
        Page page = season <= 5
                ? new StaticPage("archive",
                        String.format("archive%d/%sBatting.html", season, selector),
                        Section.ARCHIVE, getRequestUri(req), "SEHICL Archive")
                : new LeagueBattingAveragesPage(LeagueSelector.valueOf(selector.toUpperCase()),
                        season, getRequestUri(req));
        return new PageTemplate(page).process();
    }

    @RequestMapping("/averages/bowling/{selector}")
    public String currentBowlingAverages(HttpServletRequest req, @PathVariable String selector)
    {
        return new PageTemplate(new LeagueBowlingAveragesPage(
                LeagueSelector.valueOf(selector.toUpperCase()), getRequestUri(req))).process();
    }

    @RequestMapping("/archive/bowling/{selector}/{season}")
    public String archiveBowlingAverages(HttpServletRequest req, @PathVariable String selector,
            @PathVariable int season)
    {
        Page page = season <= 5
                ? new StaticPage("archive",
                        String.format("archive%d/%sBowling.html", season, selector),
                        Section.ARCHIVE, getRequestUri(req), "SEHICL Archive")
                : new LeagueBowlingAveragesPage(LeagueSelector.valueOf(selector.toUpperCase()),
                        season, getRequestUri(req));
        return new PageTemplate(page).process();
    }

    @RequestMapping("/averages/team/{teamId}")
    public String currentTeamAverages(HttpServletRequest req, @PathVariable String teamId)
    {
        return new PageTemplate(new TeamAveragesPage(teamId, getRequestUri(req))).process();
    }

    @RequestMapping("/archive/teamAverages/{teamId}")
    public String archiveTeamAverages(HttpServletRequest req, @PathVariable String teamId)
    {
        return new PageTemplate(new TeamAveragesPage(teamId, null, getRequestUri(req))).process();
    }

    @RequestMapping("/archive/teamAverages/{teamId}/{season}")
    public String archiveTeamAverages(HttpServletRequest req, @PathVariable String teamId,
            @PathVariable int season)
    {
        return new PageTemplate(new TeamAveragesPage(teamId, season, getRequestUri(req))).process();
    }

    @RequestMapping("/averages/byTeam")
    public String teamAveragesIndex(HttpServletRequest req)
    {
        return new PageTemplate(new TeamAveragesIndexPage(getRequestUri(req))).process();
    }

    @RequestMapping("/averages")
    public String averagesIndex(HttpServletRequest req)
    {
        return new PageTemplate(new StaticPage("averages", "averagesindex.ftlh", Section.AVERAGES,
                getRequestUri(req), "SEHICL Averages")).process();
    }

    @RequestMapping("/archive")
    public String archiveIndex(HttpServletRequest req)
    {
        return new PageTemplate(new ArchiveIndexPage(getRequestUri(req))).process();
    }

    @RequestMapping("/archive/season/{season}")
    public String seasonArchiveIndex(HttpServletRequest req, @PathVariable int season)
    {
        return new PageTemplate(new SeasonArchiveIndexPage(getRequestUri(req), season)).process();
    }

    @RequestMapping("/fixtures/team/{teamId}")
    public String teamFixtures(HttpServletRequest req, @PathVariable String teamId)
    {
        return new PageTemplate(new TeamFixturesPage(teamId, getRequestUri(req))).process();
    }

    @RequestMapping("/fixtures/team/{teamId}/{season}")
    public String archiveTeamFixtures(HttpServletRequest req, @PathVariable String teamId,
            @PathVariable int season)
    {
        return new PageTemplate(new TeamFixturesPage(teamId, season, getRequestUri(req))).process();
    }

    @RequestMapping("/fixtures")
    public String leagueFixtures(HttpServletRequest req)
    {
        return new PageTemplate(new LeagueFixturesPage(getRequestUri(req))).process();
    }

    @RequestMapping("/fixtures/{season}")
    public String leagueFixtures(HttpServletRequest req, @PathVariable int season)
    {
        return new PageTemplate(new LeagueFixturesPage(season, getRequestUri(req))).process();
    }

    @RequestMapping("/fixtures/league/{leagueId}")
    public String leagueFixtures(HttpServletRequest req, @PathVariable String leagueId)
    {
        return new PageTemplate(new LeagueFixturesPage(leagueId, getRequestUri(req))).process();
    }

    @RequestMapping("/fixtures/league/{leagueId}/{season}")
    public String leagueFixtures(HttpServletRequest req, @PathVariable String leagueId, @PathVariable int season)
    {
        return new PageTemplate(new LeagueFixturesPage(season, leagueId, getRequestUri(req))).process();
    }

    @RequestMapping("/results")
    public String dateResultsLatest(HttpServletRequest req)
    {
        return new PageTemplate(new DateResultsPage(getRequestUri(req))).process();
    }

    @RequestMapping("/results/date/{date}")
    public String dateResults(HttpServletRequest req, @PathVariable String date)
            throws ParseException
    {
        return new PageTemplate(
                new DateResultsPage(DateUtils.parseDate(date, "yyyyMMdd"), getRequestUri(req)))
                        .process();
    }

    @RequestMapping("/results/league/{leagueId}")
    public String leagueResults(HttpServletRequest req, @PathVariable String leagueId)
    {
        return new PageTemplate(new LeagueResultsPage(leagueId, getRequestUri(req))).process();
    }

    @RequestMapping("/dutyRota")
    public String dutyRota(HttpServletRequest req)
    {
        return new PageTemplate(new StaticPage("dutyRota", "dutyRota.ftlh", Section.FIXTURES,
                getRequestUri(req), "SEHICL Duty Rota")).process();
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest req)
    {
        return new PageTemplate(new LoginPage(getRequestUri(req), userManager)).process();
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        Login login = new Login(userManager, req.getParameter("email"),
                req.getParameter("password"));
        if (req.getParameter("Login") != null)
        {
            Long token = login.validateAndLogin();
            if (token != null)
            {
                UserSession userSession = new UserSession(req);
                userSession.setToken(token);
                resp.sendRedirect(userSession.getRedirectTarget());
                return "";
            }
        }
        else
        {
            try
            {
                login
                        .validateAndRemind(URI
                                .create(req.getRequestURL().toString())
                                .resolve("/pwdReset")
                                .toString());
            }
            catch (EmailException e)
            {
                resp.sendRedirect(String.format("/emailError?message=%s", e.getMessage()));
                return "";
            }
        }
        return new PageTemplate(new LoginPage(getRequestUri(req), login)).process();
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String register(HttpServletRequest req) throws IOException
    {
        return new PageTemplate(new RegisterPage(getRequestUri(req), userManager)).process();
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        if (!realPerson(req))
        {
            resp.sendRedirect("/register");
            return "";
        }
        Register register = new Register(userManager, req.getParameter("email"),
                req.getParameter("name"), req.getParameter("club"), req.getParameter("password"),
                req.getParameter("passwordConf"), req.getParameter("agreement") != null);
        try
        {
            User user = register
                    .validateAndRegister(
                            URI.create(req.getRequestURL().toString()).resolve("/").toString());
            return new PageTemplate(user == null ? new RegisterPage(getRequestUri(req), register)
                    : new RegisterConfPage(getRequestUri(req), user)).process();
        }
        catch (EmailException e)
        {
            resp.sendRedirect(String.format("/emailError?message=%s", e.getMessage()));
            return "";
        }
    }

    @RequestMapping(path = "/activate/{userId}")
    public String activate(HttpServletRequest req, @PathVariable long userId) throws IOException
    {
        User user = null;
        try
        {
            user = userManager.activateUser(userId);
        }
        catch (UserException ex)
        {
        }
        return new PageTemplate(new ActivatePage(getRequestUri(req), user)).process();
    }

    @RequestMapping(path = "/userDetails/{userId}")
    public String userDetails(HttpServletRequest req, HttpServletResponse resp,
            @PathVariable long userId) throws IOException
    {
        UserSession userSession = new UserSession(req);
        if (userManager.sessionHasRole(userSession.getToken(), "admin"))
        {
            User user = userManager.getUserById(userId);
            return new PageTemplate(new UserDetailsPage(getRequestUri(req), user)).process();
        }
        userSession.setRedirectTarget(req.getRequestURI());
        resp.sendRedirect("/login");
        return "";
    }

    @RequestMapping(path = "/deleteUser/{userId}", method = RequestMethod.GET)
    public String deleteUser(HttpServletRequest req, HttpServletResponse resp,
            @PathVariable long userId) throws IOException
    {
        UserSession userSession = new UserSession(req);
        if (userManager.sessionHasRole(userSession.getToken(), "admin"))
        {
            User user = userManager.getUserById(userId);
            return new PageTemplate(new DeleteUserPage(getRequestUri(req), user, false)).process();
        }
        userSession.setRedirectTarget(req.getRequestURI());
        resp.sendRedirect("/login");
        return "";
    }

    @RequestMapping(path = "/deleteUser/{userId}", method = RequestMethod.POST)
    public String deleteUserConfirmed(HttpServletRequest req, HttpServletResponse resp,
            @PathVariable long userId) throws IOException
    {
        UserSession userSession = new UserSession(req);
        if (userManager.sessionHasRole(userSession.getToken(), "admin"))
        {
            User user = userManager.getUserById(userId);
            userManager.deleteUser(userId);
            return new PageTemplate(new DeleteUserPage(getRequestUri(req), user, true)).process();
        }
        userSession.setRedirectTarget(req.getRequestURI());
        resp.sendRedirect("/login");
        return "";
    }

    @RequestMapping(path = "/pwdReset/{resetId}", method = RequestMethod.GET)
    public String passwordReset(HttpServletRequest req, @PathVariable long resetId)
            throws IOException
    {
        return new PageTemplate(new ResetPage(getRequestUri(req), new Reset(resetId, userManager)))
                .process();
    }

    @RequestMapping(path = "/pwdReset/{resetId}", method = RequestMethod.POST)
    public String passwordReset(HttpServletRequest req, HttpServletResponse resp,
            @PathVariable long resetId) throws IOException
    {
        if (!realPerson(req))
        {
            resp.sendRedirect(String.format("/pwdReset/%d", resetId));
            return "";
        }
        Reset reset = new Reset(resetId, userManager);
        if (reset.validateAndReset(req.getParameter("password"), req.getParameter("passwordConf")))
        {
            resp.sendRedirect("/login");
            return "";
        }
        return new PageTemplate(new ResetPage(getRequestUri(req), reset)).process();
    }

    @RequestMapping(path = "/reconfirm/{userId}", method = RequestMethod.GET)
    public String reconfirmUser(HttpServletRequest req, @PathVariable long userId)
            throws IOException
    {
        return new PageTemplate(
                new ReconfirmPage(getRequestUri(req), new Reconfirm(userId, userManager)))
                        .process();
    }

    @RequestMapping(path = "/reconfirm/{userId}", method = RequestMethod.POST)
    public String reconfirm(HttpServletRequest req, HttpServletResponse resp,
            @PathVariable long userId) throws IOException
    {
        Reconfirm reconfirm = new Reconfirm(userId, userManager);
        if (reconfirm.validateAndReconfirm(req.getParameter("agreement") != null))
        {
            resp.sendRedirect("/reconfConf");
            return "";
        }
        return new PageTemplate(new ReconfirmPage(getRequestUri(req), reconfirm)).process();
    }

    @RequestMapping(path = "/reconfConf")
    public String confirmReconfirmation(HttpServletRequest req) throws IOException
    {
        return new PageTemplate(new StaticPage("reconfirm", "reconfConf.ftlh", null,
                getRequestUri(req), "Thank you")).process();
    }

    @RequestMapping(path = "/dp")
    public String dataProtection(HttpServletRequest req) throws IOException
    {
        return new PageTemplate(new StaticPage("dp", "dataProtection.ftlh", Section.DP,
                getRequestUri(req), "SEHICL Data Protection Policy")).process();
    }

    @RequestMapping(path = "/admin/reconf")
    public String sendUserReconfirmRequests(HttpServletRequest req) throws IOException
    {
        final UserSession userSession = new UserSession(req);
        if (!userManager.sessionHasRole(userSession.getToken(), "admin"))
            return "Not authorised";
        String reconfirmationPageAddress = URI
                .create(req.getRequestURL().toString())
                .resolve("/reconfirm")
                .toString();
        userManager.sendReconfirmationEmails(reconfirmationPageAddress);
        return "Emails sent";
    }
    
    @RequestMapping(path="/admin/userExport")
    public String exportUsers(HttpServletRequest req) throws IOException
    {
        return usersExporter.export(req.getHeader("adminSecret"));
    }

    @Value("${recaptcha.url:https://www.google.com/recaptcha/api/siteverify}")
    private String recaptchaUrl;

    private boolean realPerson(HttpServletRequest req) throws IOException
    {
        HttpPost post = new HttpPost(recaptchaUrl);
        post
                .setEntity(new UrlEncodedFormEntity(Arrays
                        .asList(new BasicNameValuePair("secret", System.getenv("RECAPTCHA_SECRET")),
                                new BasicNameValuePair("response",
                                        req.getParameter("g-recaptcha-response")))));
        try (CloseableHttpResponse response = HttpClients.createDefault().execute(post))
        {
            if (response.getStatusLine().getStatusCode() != HttpStatus.OK.value())
                return false;
            try (Scanner scanner = new Scanner(response.getEntity().getContent()))
            {
                return JsonParserFactory
                        .getJsonParser()
                        .parseMap(scanner.useDelimiter("\\A").next())
                        .getOrDefault("success", Boolean.FALSE)
                        .equals(Boolean.TRUE);
            }
        }
    }

    String getParameter(HttpServletRequest req, String name)
    {
        String value = req.getParameter(name);
        return value == null ? null : value.trim();
    }
}