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
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        return new PageTemplate(new HomePage(uri)).process();
    }

    @RequestMapping("/contacts")
    public String contacts(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new ContactsPage(uri)).process();
    }

    @RequestMapping("/fullContacts")
    public String fullContacts(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        String answer = "";
        final UserSession userSession = new UserSession(req);
        if (userManager.sessionHasRole(userSession.getToken(), null))
        {
            String uri = getRequestUri(req);
            answer = new PageTemplate(new FullContactsPage(uri)).process();
        }
        else
        {
            userSession.setRedirectTarget(req.getRequestURI());
            resp.sendRedirect("/login");
        }
        return answer;
    }

    @RequestMapping("/resources")
    public String resources(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new StaticPage("resources", "resources.ftlh", Section.RESOURCES,
                uri, "SEHICL Resources")).process();
    }

    @RequestMapping("/rules")
    public String rules(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(
                new StaticPage("rules", "rules.ftlh", Section.RULES, uri, "SEHICL Rules"))
                        .process();
    }

    @RequestMapping("/records")
    public String recordsIndex(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new StaticPage("records", "records/index.ftlh", Section.RECORDS,
                uri, "SEHICL Records")).process();
    }

    @RequestMapping("/records/performances")
    public String recordPerformances(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new StaticPage("records", "records/performances.ftlh",
                Section.RECORDS, uri, "SEHICL Record Performances")).process();
    }

    @RequestMapping("/records/winners")
    public String divisionalWinners(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new StaticPage("divwinners", "records/divwinners.ftlh",
                Section.RECORDS, uri, "SEHICL Divisional Winners")).process();
    }

    @RequestMapping("/records/awards")
    public String individualAwards(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new StaticPage("awards", "records/individualawards.ftlh",
                Section.RECORDS, uri, "SEHICL Individual Awards")).process();
    }

    @RequestMapping("/records/fairplay")
    public String fairplay(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new StaticPage("fairplay", "records/fairplay.ftlh", Section.RECORDS,
                uri, "SEHICL Sporting & Efficiency")).process();
    }

    @RequestMapping("/presentation")
    public String presentationEvening(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new StaticPage("presentation", "presentation/schedule.ftlh",
                Section.HOME, uri, "SEHICL Presentation Evening")).process();
    }

    @RequestMapping("/archive/presentation/{season}")
    public String presentationEvening(HttpServletRequest req, @PathVariable String season)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new StaticPage("presentation",
                String.format("presentation/%s.ftlh", season), Section.ARCHIVE, uri,
                String.format("SEHICL Presentation Evening %s", season))).process();
    }

    @RequestMapping("/tables")
    public String currentTables(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new LeagueTablesPage(uri)).process();
    }

    @RequestMapping("/tables/league/{leagueId}")
    public String currentTable(HttpServletRequest req, @PathVariable String leagueId)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new LeagueTablePage(leagueId, uri)).process();
    }

    @RequestMapping("/archive/table/{leagueId}/{season}")
    public String archiveTable(HttpServletRequest req, @PathVariable String leagueId,
            @PathVariable int season)
    {
        String uri = getRequestUri(req);
        final Page page = season <= 5
                ? new StaticPage("archive", String.format("archive%d/%s.html", season, leagueId),
                        Section.ARCHIVE, uri, "SEHICL Archive")
                : new LeagueTablePage(leagueId, season, uri);
        return new PageTemplate(page).process();
    }

    @RequestMapping("/averages/batting/{selector}")
    public String currentBattingAverages(HttpServletRequest req, @PathVariable String selector)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(
                new LeagueBattingAveragesPage(LeagueSelector.valueOf(selector.toUpperCase()), uri))
                        .process();
    }

    @RequestMapping("/archive/batting/{selector}/{season}")
    public String archiveBattingAverages(HttpServletRequest req, @PathVariable String selector,
            @PathVariable int season)
    {
        String uri = getRequestUri(req);
        final Page page = season <= 5
                ? new StaticPage("archive",
                        String.format("archive%d/%sBatting.html", season, selector),
                        Section.ARCHIVE, uri, "SEHICL Archive")
                : new LeagueBattingAveragesPage(LeagueSelector.valueOf(selector.toUpperCase()),
                        season, uri);
        return new PageTemplate(page).process();
    }

    @RequestMapping("/averages/bowling/{selector}")
    public String currentBowlingAverages(HttpServletRequest req, @PathVariable String selector)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(
                new LeagueBowlingAveragesPage(LeagueSelector.valueOf(selector.toUpperCase()), uri))
                        .process();
    }

    @RequestMapping("/archive/bowling/{selector}/{season}")
    public String archiveBowlingAverages(HttpServletRequest req, @PathVariable String selector,
            @PathVariable int season)
    {
        String uri = getRequestUri(req);
        final Page page = season <= 5
                ? new StaticPage("archive",
                        String.format("archive%d/%sBowling.html", season, selector),
                        Section.ARCHIVE, uri, "SEHICL Archive")
                : new LeagueBowlingAveragesPage(LeagueSelector.valueOf(selector.toUpperCase()),
                        season, uri);
        return new PageTemplate(page).process();
    }

    @RequestMapping("/averages/team/{teamId}")
    public String currentTeamAverages(HttpServletRequest req, @PathVariable String teamId)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new TeamAveragesPage(teamId, uri)).process();
    }

    @RequestMapping("/archive/teamAverages/{teamId}")
    public String archiveTeamAverages(HttpServletRequest req, @PathVariable String teamId)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new TeamAveragesPage(teamId, null, uri)).process();
    }

    @RequestMapping("/archive/teamAverages/{teamId}/{season}")
    public String archiveTeamAverages(HttpServletRequest req, @PathVariable String teamId,
            @PathVariable int season)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new TeamAveragesPage(teamId, season, uri)).process();
    }

    @RequestMapping("/averages/byTeam")
    public String teamAveragesIndex(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new TeamAveragesIndexPage(uri)).process();
    }

    @RequestMapping("/averages")
    public String averagesIndex(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new StaticPage("averages", "averagesindex.ftlh", Section.AVERAGES,
                uri, "SEHICL Averages")).process();
    }

    @RequestMapping("/archive")
    public String archiveIndex(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new ArchiveIndexPage(uri)).process();
    }

    @RequestMapping("/archive/season/{season}")
    public String seasonArchiveIndex(HttpServletRequest req, @PathVariable int season)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new SeasonArchiveIndexPage(uri, season)).process();
    }

    @RequestMapping("/fixtures/team/{teamId}")
    public String teamFixtures(HttpServletRequest req, @PathVariable String teamId)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new TeamFixturesPage(teamId, uri)).process();
    }

    @RequestMapping("/fixtures/team/{teamId}/{season}")
    public String archiveTeamFixtures(HttpServletRequest req, @PathVariable String teamId,
            @PathVariable int season)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new TeamFixturesPage(teamId, season, uri)).process();
    }

    @RequestMapping("/fixtures")
    public String leagueFixtures(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new LeagueFixturesPage(uri)).process();
    }

    @RequestMapping("/fixtures/league/{leagueId}")
    public String leagueFixtures(HttpServletRequest req, @PathVariable String leagueId)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new LeagueFixturesPage(leagueId, uri)).process();
    }

    @RequestMapping("/results")
    public String dateResultsLatest(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new DateResultsPage(uri)).process();
    }

    @RequestMapping("/results/date/{date}")
    public String dateResults(HttpServletRequest req, @PathVariable String date)
            throws ParseException
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new DateResultsPage(DateUtils.parseDate(date, "yyyyMMdd"), uri))
                .process();
    }

    @RequestMapping("/results/league/{leagueId}")
    public String leagueResults(HttpServletRequest req, @PathVariable String leagueId)
            throws ParseException
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new LeagueResultsPage(leagueId, uri)).process();
    }

    @RequestMapping("/dutyRota")
    public String dutyRota(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new StaticPage("dutyRota", "dutyRota.ftlh", Section.FIXTURES, uri,
                "SEHICL Duty Rota")).process();
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest req)
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new LoginPage(uri, userManager)).process();
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        String answer = "";
        String uri = getRequestUri(req);
        final String email = req.getParameter("email");
        final String password = req.getParameter("password");
        final Login login = new Login(userManager, email, password);
        boolean redisplay = true;
        if (req.getParameter("Login") != null)
        {
            final Long token = login.validateAndLogin();
            if (token != null)
            {
                redisplay = false;
                final UserSession userSession = new UserSession(req);
                userSession.setToken(token);
                resp.sendRedirect(userSession.getRedirectTarget());
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
                redisplay = false;
                resp.sendRedirect(String.format("/emailError?message=%s", e.getMessage()));
            }
        }
        if (redisplay)
        {
            answer = new PageTemplate(new LoginPage(uri, login)).process();
        }
        return answer;
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String register(HttpServletRequest req) throws IOException
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new RegisterPage(uri, userManager)).process();
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        String answer = "";
        String uri = getRequestUri(req);
        final String email = req.getParameter("email");
        final String name = req.getParameter("name");
        final String club = req.getParameter("club");
        final String password = req.getParameter("password");
        final String passwordConf = req.getParameter("passwordConf");
        final String agreement = req.getParameter("agreement");
        final String recaptchaResponse = req.getParameter("g-recaptcha-response");
        if (!notARobot(recaptchaResponse))
        {
            resp.sendRedirect("/register");
            return "";
        }
        final Register register = new Register(userManager, email, name, club, password,
                passwordConf, agreement != null);
        try
        {
            User user = register
                    .validateAndRegister(
                            URI
                                    .create(req.getRequestURL().toString())
                                    .resolve("/activate")
                                    .toString(),
                            URI
                                    .create(req.getRequestURL().toString())
                                    .resolve("/userDetails")
                                    .toString());
            final Page page = user == null ? new RegisterPage(uri, register)
                    : new RegisterConfPage(uri, user);
            answer = new PageTemplate(page).process();
        }
        catch (EmailException e)
        {
            resp.sendRedirect(String.format("/emailError?message=%s", e.getMessage()));
        }
        return answer;
    }

    @RequestMapping(path = "/activate/{userId}")
    public String activate(HttpServletRequest req, @PathVariable long userId) throws IOException
    {
        String uri = getRequestUri(req);
        User user = null;
        try
        {
            user = userManager.activateUser(userId);
        }
        catch (UserException ex)
        {
        }
        return new PageTemplate(new ActivatePage(uri, user)).process();
    }

    @RequestMapping(path = "/userDetails/{userId}")
    public String userDetails(HttpServletRequest req, HttpServletResponse resp,
            @PathVariable long userId) throws IOException
    {
        final UserSession userSession = new UserSession(req);
        if (userManager.sessionHasRole(userSession.getToken(), "admin"))
        {
            String uri = getRequestUri(req);
            User user = userManager.getUserById(userId);
            return new PageTemplate(new UserDetailsPage(uri, user)).process();
        }
        userSession.setRedirectTarget(req.getRequestURI());
        resp.sendRedirect("/login");
        return "";
    }

    @RequestMapping(path = "/deleteUser/{userId}", method = RequestMethod.GET)
    public String deleteUser(HttpServletRequest req, HttpServletResponse resp,
            @PathVariable long userId) throws IOException
    {
        final UserSession userSession = new UserSession(req);
        if (userManager.sessionHasRole(userSession.getToken(), "admin"))
        {
            String uri = getRequestUri(req);
            User user = userManager.getUserById(userId);
            return new PageTemplate(new DeleteUserPage(uri, user, false)).process();
        }
        userSession.setRedirectTarget(req.getRequestURI());
        resp.sendRedirect("/login");
        return "";
    }

    @RequestMapping(path = "/deleteUser/{userId}", method = RequestMethod.POST)
    public String deleteUserConfirmed(HttpServletRequest req, HttpServletResponse resp,
            @PathVariable long userId) throws IOException
    {
        final UserSession userSession = new UserSession(req);
        if (userManager.sessionHasRole(userSession.getToken(), "admin"))
        {
            String uri = getRequestUri(req);
            User user = userManager.getUserById(userId);
            userManager.deleteUser(userId);
            return new PageTemplate(new DeleteUserPage(uri, user, true)).process();
        }
        userSession.setRedirectTarget(req.getRequestURI());
        resp.sendRedirect("/login");
        return "";
    }

    @RequestMapping(path = "/pwdReset/{resetId}", method = RequestMethod.GET)
    public String passwordReset(HttpServletRequest req, @PathVariable long resetId)
            throws IOException
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new ResetPage(uri, new Reset(resetId, userManager))).process();
    }

    @RequestMapping(path = "/pwdReset/{resetId}", method = RequestMethod.POST)
    public String passwordReset(HttpServletRequest req, HttpServletResponse resp,
            @PathVariable long resetId) throws IOException
    {
        String uri = getRequestUri(req);
        final String password = req.getParameter("password");
        final String passwordConf = req.getParameter("passwordConf");
        final String recaptchaResponse = req.getParameter("g-recaptcha-response");
        if (!notARobot(recaptchaResponse))
        {
            resp.sendRedirect(String.format("/pwdReset/%d", resetId));
            return "";
        }
        final Reset reset = new Reset(resetId, userManager);
        if (reset.validateAndReset(password, passwordConf))
        {
            resp.sendRedirect("/login");
            return "";
        }
        else
            return new PageTemplate(new ResetPage(uri, reset)).process();
    }

    @RequestMapping(path = "/reconfirm/{userId}", method = RequestMethod.GET)
    public String reconfirmUser(HttpServletRequest req, @PathVariable long userId)
            throws IOException
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new ReconfirmPage(uri, new Reconfirm(userId, userManager)))
                .process();
    }

    @RequestMapping(path = "/reconfirm/{userId}", method = RequestMethod.POST)
    public String reconfirm(HttpServletRequest req, HttpServletResponse resp,
            @PathVariable long userId) throws IOException
    {
        String uri = getRequestUri(req);
        final String agreement = req.getParameter("agreement");
        final boolean agreed = agreement != null;
        final Reconfirm reconfirm = new Reconfirm(userId, userManager);
        if (reconfirm.validateAndReconfirm(agreed))
        {
            resp.sendRedirect("/reconfConf");
            return "";
        }
        else
            return new PageTemplate(new ReconfirmPage(uri, reconfirm)).process();
    }

    @RequestMapping(path = "/reconfConf")
    public String confirmReconfirmation(HttpServletRequest req) throws IOException
    {
        String uri = getRequestUri(req);
        return new PageTemplate(
                new StaticPage("reconfirm", "reconfConf.ftlh", null, uri, "Thank you")).process();
    }

    @RequestMapping(path = "/dp")
    public String dataProtection(HttpServletRequest req) throws IOException
    {
        String uri = getRequestUri(req);
        return new PageTemplate(new StaticPage("dp", "dataProtection.ftlh", Section.DP, uri,
                "SEHICL Data Protection Policy")).process();
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

    private boolean notARobot(String recaptchaResponse) throws IOException
    {
        HttpPost post = new HttpPost(System.getenv("RECAPTCHA_URL"));
        post
                .setEntity(new UrlEncodedFormEntity(Arrays
                        .asList(new BasicNameValuePair("secret",
                                System.getenv("RECAPTCHA_SECRET")),
                                new BasicNameValuePair("response", recaptchaResponse))));
        String responseBody;
        try (CloseableHttpResponse response = HttpClients.createDefault().execute(post))
        {
            if (response.getStatusLine().getStatusCode() != HttpStatus.OK.value())
                return false;
            try (Scanner scanner = new Scanner(response.getEntity().getContent()))
            {
                responseBody = scanner.useDelimiter("\\A").next();
            }
        }
        return JsonParserFactory
                .getJsonParser()
                .parseMap(responseBody)
                .getOrDefault("success", Boolean.FALSE)
                .equals(Boolean.TRUE);
    }
}