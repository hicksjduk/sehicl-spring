package uk.org.sehicl.website;

import java.io.IOException;
import java.io.StringReader;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import uk.org.sehicl.admin.UsersExporter;
import uk.org.sehicl.admin.UsersImporter;
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
    private static Logger LOG = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private UserManager userManager;
    @Autowired
    private UsersExporter usersExporter;
    @Autowired
    private UsersImporter usersImporter;

    private String getRequestUri(HttpServletRequest req)
    {
        return Stream
                .of(req.getRequestURI(), req.getPathInfo())
                .filter(Objects::nonNull)
                .collect(Collectors.joining());
    }

    private String getRequestUri()
    {
        return ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
    }

    @RequestMapping("/")
    public String home()
    {
        return new PageTemplate(new HomePage(getRequestUri())).process();
    }

    @RequestMapping("/contacts")
    public String contacts()
    {
        return new PageTemplate(new ContactsPage(getRequestUri())).process();
    }

    @RequestMapping("/fullContacts")
    public String fullContacts(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        final UserSession userSession = new UserSession(req);
        if (userManager.sessionHasRole(userSession.getToken(), null))
            return new PageTemplate(new FullContactsPage(getRequestUri())).process();
        userSession.setRedirectTarget(getRequestUri());
        resp.sendRedirect("/login");
        return "";
    }

    @RequestMapping("/resources")
    public String resources()
    {
        return new PageTemplate(new StaticPage("resources", "resources.ftlh", Section.RESOURCES,
                getRequestUri(), "SEHICL Resources")).process();
    }

    @RequestMapping("/rules")
    public String rules()
    {
        return new PageTemplate(new StaticPage("rules", "rules.ftlh", Section.RULES,
                getRequestUri(), "SEHICL Rules")).process();
    }

    @RequestMapping("/records")
    public String recordsIndex()
    {
        return new PageTemplate(new StaticPage("records", "records/index.ftlh", Section.RECORDS,
                getRequestUri(), "SEHICL Records")).process();
    }

    @RequestMapping("/records/performances")
    public String recordPerformances()
    {
        return new PageTemplate(new StaticPage("records", "records/performances.ftlh",
                Section.RECORDS, getRequestUri(), "SEHICL Record Performances")).process();
    }

    @RequestMapping("/records/winners")
    public String divisionalWinners()
    {
        return new PageTemplate(new StaticPage("divwinners", "records/divwinners.ftlh",
                Section.RECORDS, getRequestUri(), "SEHICL Divisional Winners")).process();
    }

    @RequestMapping("/records/awards")
    public String individualAwards()
    {
        return new PageTemplate(new StaticPage("awards", "records/individualawards.ftlh",
                Section.RECORDS, getRequestUri(), "SEHICL Individual Awards")).process();
    }

    @RequestMapping("/records/fairplay")
    public String fairplay()
    {
        return new PageTemplate(new StaticPage("fairplay", "records/fairplay.ftlh", Section.RECORDS,
                getRequestUri(), "SEHICL Sporting & Efficiency")).process();
    }

    @RequestMapping("/presentation")
    public String presentationEvening()
    {
        return new PageTemplate(new StaticPage("presentation", "presentation/schedule.ftlh",
                Section.HOME, getRequestUri(), "SEHICL Presentation Evening")).process();
    }

    @RequestMapping("/archive/presentation/{season}")
    public String presentationEvening(@PathVariable String season)
    {
        return new PageTemplate(new StaticPage("presentation",
                String.format("presentation/%s.ftlh", season), Section.ARCHIVE, getRequestUri(),
                String.format("SEHICL Presentation Evening %s", season))).process();
    }

    @RequestMapping("/tables")
    public String currentTables()
    {
        return new PageTemplate(new LeagueTablesPage(getRequestUri())).process();
    }

    @RequestMapping("/tables/league/{leagueId}")
    public String currentTable(@PathVariable String leagueId)
    {
        return new PageTemplate(new LeagueTablePage(leagueId, getRequestUri())).process();
    }

    @RequestMapping("/archive/table/{leagueId}/{season}")
    public String archiveTable(@PathVariable String leagueId, @PathVariable int season)
    {
        Page page = season <= 5
                ? new StaticPage("archive", String.format("archive%d/%s.html", season, leagueId),
                        Section.ARCHIVE, getRequestUri(), "SEHICL Archive")
                : new LeagueTablePage(leagueId, season, getRequestUri());
        return new PageTemplate(page).process();
    }

    @RequestMapping("/averages/batting/{selector}")
    public String currentBattingAverages(@PathVariable String selector)
    {
        return new PageTemplate(new LeagueBattingAveragesPage(
                LeagueSelector.valueOf(selector.toUpperCase()), getRequestUri())).process();
    }

    @RequestMapping("/archive/batting/{selector}/{season}")
    public String archiveBattingAverages(@PathVariable String selector, @PathVariable int season)
    {
        Page page = season <= 5
                ? new StaticPage("archive",
                        String.format("archive%d/%sBatting.html", season, selector),
                        Section.ARCHIVE, getRequestUri(), "SEHICL Archive")
                : new LeagueBattingAveragesPage(LeagueSelector.valueOf(selector.toUpperCase()),
                        season, getRequestUri());
        return new PageTemplate(page).process();
    }

    @RequestMapping("/averages/bowling/{selector}")
    public String currentBowlingAverages(@PathVariable String selector)
    {
        return new PageTemplate(new LeagueBowlingAveragesPage(
                LeagueSelector.valueOf(selector.toUpperCase()), getRequestUri())).process();
    }

    @RequestMapping("/archive/bowling/{selector}/{season}")
    public String archiveBowlingAverages(@PathVariable String selector, @PathVariable int season)
    {
        Page page = season <= 5
                ? new StaticPage("archive",
                        String.format("archive%d/%sBowling.html", season, selector),
                        Section.ARCHIVE, getRequestUri(), "SEHICL Archive")
                : new LeagueBowlingAveragesPage(LeagueSelector.valueOf(selector.toUpperCase()),
                        season, getRequestUri());
        return new PageTemplate(page).process();
    }

    @RequestMapping("/averages/team/{teamId}")
    public String currentTeamAverages(@PathVariable String teamId)
    {
        return new PageTemplate(new TeamAveragesPage(teamId, getRequestUri())).process();
    }

    @RequestMapping("/archive/teamAverages/{teamId}")
    public String archiveTeamAverages(@PathVariable String teamId)
    {
        return new PageTemplate(new TeamAveragesPage(teamId, null, getRequestUri())).process();
    }

    @RequestMapping("/archive/teamAverages/{teamId}/{season}")
    public String archiveTeamAverages(@PathVariable String teamId, @PathVariable int season)
    {
        return new PageTemplate(new TeamAveragesPage(teamId, season, getRequestUri())).process();
    }

    @RequestMapping("/averages/byTeam")
    public String teamAveragesIndex()
    {
        return new PageTemplate(new TeamAveragesIndexPage(getRequestUri())).process();
    }

    @RequestMapping("/averages")
    public String averagesIndex()
    {
        return new PageTemplate(new StaticPage("averages", "averagesindex.ftlh", Section.AVERAGES,
                getRequestUri(), "SEHICL Averages")).process();
    }

    @RequestMapping("/archive")
    public String archiveIndex()
    {
        return new PageTemplate(new ArchiveIndexPage(getRequestUri())).process();
    }

    @RequestMapping("/archive/season/{season}")
    public String seasonArchiveIndex(@PathVariable int season)
    {
        return new PageTemplate(new SeasonArchiveIndexPage(getRequestUri(), season)).process();
    }

    @RequestMapping("/fixtures/team/{teamId}")
    public String teamFixtures(@PathVariable String teamId)
    {
        return new PageTemplate(new TeamFixturesPage(teamId, getRequestUri())).process();
    }

    @RequestMapping("/fixtures/team/{teamId}/{season}")
    public String archiveTeamFixtures(@PathVariable String teamId, @PathVariable int season)
    {
        return new PageTemplate(new TeamFixturesPage(teamId, season, getRequestUri())).process();
    }

    @RequestMapping("/fixtures")
    public String leagueFixtures()
    {
        return new PageTemplate(new LeagueFixturesPage(getRequestUri())).process();
    }

    @RequestMapping("/fixtures/{season}")
    public String leagueFixtures(@PathVariable int season)
    {
        return new PageTemplate(new LeagueFixturesPage(season, getRequestUri())).process();
    }

    @RequestMapping("/fixtures/league/{leagueId}")
    public String leagueFixtures(@PathVariable String leagueId)
    {
        return new PageTemplate(new LeagueFixturesPage(leagueId, getRequestUri())).process();
    }

    @RequestMapping("/fixtures/league/{leagueId}/{season}")
    public String leagueFixtures(@PathVariable String leagueId, @PathVariable int season)
    {
        return new PageTemplate(new LeagueFixturesPage(season, leagueId, getRequestUri()))
                .process();
    }

    @RequestMapping("/results")
    public String dateResultsLatest()
    {
        return new PageTemplate(new DateResultsPage(getRequestUri())).process();
    }

    @RequestMapping("/results/date/{date}")
    public String dateResults(@PathVariable String date) throws ParseException
    {
        return new PageTemplate(
                new DateResultsPage(DateUtils.parseDate(date, "yyyyMMdd"), getRequestUri()))
                        .process();
    }

    @RequestMapping("/results/league/{leagueId}")
    public String leagueResults(@PathVariable String leagueId)
    {
        return new PageTemplate(new LeagueResultsPage(leagueId, getRequestUri())).process();
    }

    @RequestMapping("/dutyRota")
    public String dutyRota()
    {
        return new PageTemplate(new StaticPage("dutyRota", "dutyRota.ftlh", Section.FIXTURES,
                getRequestUri(), "SEHICL Duty Rota")).process();
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login()
    {
        return new PageTemplate(new LoginPage(getRequestUri(), userManager)).process();
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(@RequestParam String email, @RequestParam String password,
            @RequestParam(name = "Login", required = false) String loginOption,
            HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        Login login = new Login(userManager, email, password);
        if (loginOption != null)
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
            login
                    .validateAndReset(URI
                            .create(req.getRequestURL().toString())
                            .resolve("/pwdReset")
                            .toString());
        }
        return new PageTemplate(new LoginPage(getRequestUri(), login)).process();
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String register() throws IOException
    {
        return new PageTemplate(new RegisterPage(getRequestUri(), userManager)).process();
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(@RequestParam String email, @RequestParam String name,
            @RequestParam String club, @RequestParam String password,
            @RequestParam String passwordConf, @RequestParam(required = false) String agreement,
            @RequestParam(name = "g-recaptcha-response") String recaptcha, HttpServletResponse resp)
            throws IOException
    {
        if (!realPerson(recaptcha))
        {
            resp.sendRedirect("/register");
            return "";
        }
        Register register = new Register(userManager, email, name, club, password, passwordConf,
                agreement != null);
        User user = register
                .validateAndRegister(URI.create(getRequestUri()).resolve("/").toString());
        return new PageTemplate(user == null ? new RegisterPage(getRequestUri(), register)
                : new RegisterConfPage(getRequestUri(), user)).process();
    }

    @RequestMapping(path = "/activate/{userId}")
    public String activate(@PathVariable long userId) throws IOException
    {
        User user = null;
        try
        {
            user = userManager.activateUser(userId);
        }
        catch (UserException ex)
        {
        }
        return new PageTemplate(new ActivatePage(getRequestUri(), user)).process();
    }

    @RequestMapping(path = "/userDetails/{userId}")
    public String userDetails(HttpServletRequest req, HttpServletResponse resp,
            @PathVariable long userId) throws IOException
    {
        UserSession userSession = new UserSession(req);
        if (userManager.sessionHasRole(userSession.getToken(), "admin"))
        {
            User user = userManager.getUserById(userId);
            return new PageTemplate(new UserDetailsPage(getRequestUri(), user)).process();
        }
        userSession.setRedirectTarget(getRequestUri());
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
            return new PageTemplate(new DeleteUserPage(getRequestUri(), user, false)).process();
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
            return new PageTemplate(new DeleteUserPage(getRequestUri(), user, true)).process();
        }
        userSession.setRedirectTarget(req.getRequestURI());
        resp.sendRedirect("/login");
        return "";
    }

    @RequestMapping(path = "/pwdReset/{resetId}", method = RequestMethod.GET)
    public String passwordReset(@PathVariable long resetId) throws IOException
    {
        return new PageTemplate(new ResetPage(getRequestUri(), new Reset(resetId, userManager)))
                .process();
    }

    @RequestMapping(path = "/pwdReset/{resetId}", method = RequestMethod.POST)
    public String passwordReset(@RequestParam String password, @RequestParam String passwordConf,
            @RequestParam(name = "g-recaptcha-response") String recaptcha, HttpServletResponse resp,
            @PathVariable long resetId) throws IOException
    {
        if (!realPerson(recaptcha))
        {
            resp.sendRedirect(String.format("/pwdReset/%d", resetId));
            return "";
        }
        Reset reset = new Reset(resetId, userManager);
        if (reset.validateAndReset(password, passwordConf))
        {
            resp.sendRedirect("/login");
            return "";
        }
        return new PageTemplate(new ResetPage(getRequestUri(), reset)).process();
    }

    @RequestMapping(path = "/reconfirm/{userId}", method = RequestMethod.GET)
    public String reconfirmUser(@PathVariable long userId) throws IOException
    {
        return new PageTemplate(
                new ReconfirmPage(getRequestUri(), new Reconfirm(userId, userManager))).process();
    }

    @RequestMapping(path = "/reconfirm/{userId}", method = RequestMethod.POST)
    public String reconfirm(@RequestParam(required = false) String agreement,
            HttpServletResponse resp, @PathVariable long userId) throws IOException
    {
        Reconfirm reconfirm = new Reconfirm(userId, userManager);
        try
        {
            if (reconfirm.validateAndReconfirm(agreement != null))
            {
                resp.sendRedirect("/reconfConf");
                return "";
            }
        }
        catch (EmailException ex)
        {
            LOG.error("Unexpected exception", ex);
        }
        return new PageTemplate(new ReconfirmPage(getRequestUri(), reconfirm)).process();
    }

    @RequestMapping(path = "/reconfConf")
    public String confirmReconfirmation() throws IOException
    {
        return new PageTemplate(
                new StaticPage("reconfirm", "reconfConf.ftlh", null, getRequestUri(), "Thank you"))
                        .process();
    }

    @RequestMapping(path = "/dp")
    public String dataProtection() throws IOException
    {
        return new PageTemplate(new StaticPage("dp", "dataProtection.ftlh", Section.DP,
                getRequestUri(), "SEHICL Data Protection Policy")).process();
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

    @RequestMapping(path = "/admin/userExport")
    public String exportUsers(@RequestHeader(required = false) String adminSecret,
            HttpServletResponse resp) throws IOException
    {
        if (adminSecret == null || !Objects.equals(EnvVar.ADMIN_SECRET.get(), adminSecret))
        {
            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
            return "";
        }
        return usersExporter.export();
    }

    @PostMapping(path = "/admin/userImport")
    public String importUsers(@RequestHeader(required = false) String adminSecret,
            @RequestBody(required = false) String body, HttpServletResponse resp) throws IOException
    {
        if (adminSecret == null || !Objects.equals(EnvVar.ADMIN_SECRET.get(), adminSecret))
        {
            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
            return "";
        }
        if (!StringUtils.hasLength(body))
        {
            resp.setStatus(HttpStatus.BAD_REQUEST.value());
            return "";
        }
        return String
                .format("%d user(s) imported", usersImporter.importUsers(new StringReader(body)));
    }

    @Value("${recaptcha.url:https://www.google.com/recaptcha/api/siteverify}")
    private String recaptchaUrl;

    private boolean realPerson(String recaptcha) throws IOException
    {
        HttpPost post = new HttpPost(recaptchaUrl);
        post
                .setEntity(new UrlEncodedFormEntity(Arrays
                        .asList(new BasicNameValuePair("secret", EnvVar.RECAPTCHA_SECRET.get()),
                                new BasicNameValuePair("response", recaptcha))));
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
}