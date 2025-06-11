package uk.org.sehicl.website;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
    private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

    private static final boolean forceHttps = EnvVar.FORCE_HTTPS
            .get()
            .filter(Boolean::parseBoolean)
            .isPresent();

    @Autowired
    private UserManager userManager;
    @Autowired
    private UsersExporter usersExporter;
    @Autowired
    private UsersImporter usersImporter;

    private String getRequestUri()
    {
        var urib = ServletUriComponentsBuilder.fromCurrentRequestUri();
        if (forceHttps)
            urib.scheme("https");
        return urib.toUriString();
    }

    private String getRequestUri(String pathToResolve)
    {
        return URI.create(getRequestUri()).resolve(pathToResolve).toString();
    }

    @RequestMapping("/")
    public String home()
    {
        return new PageTemplate(new HomePage()).process();
    }

    @RequestMapping("/contacts")
    public String contacts()
    {
        return new PageTemplate(new ContactsPage()).process();
    }

    public String fullContactsPlaceholder() throws IOException
    {
        return new PageTemplate(new StaticPage("contacts", "fullContactsPlaceholder.ftlh",
                Section.CONTACTS, "SEHICL Full Contacts")).process();
    }

    @RequestMapping("/fullContacts")
    public String fullContacts(HttpSession session, HttpServletResponse resp) throws IOException
    {
        final UserSession userSession = new UserSession(session);
        if (userManager.sessionHasRole(userSession.getToken(), null))
            return new PageTemplate(new FullContactsPage()).process();
        userSession.setRedirectTarget(getRequestUri());
        resp.sendRedirect(getRequestUri("/login"));
        return "";
    }

    @RequestMapping("/resources")
    public String resources()
    {
        return new PageTemplate(new StaticPage("resources", "resources.ftlh", Section.RESOURCES,
                "SEHICL Resources")).process();
    }

    @RequestMapping("/rules")
    public String rules()
    {
        return new PageTemplate(
                new StaticPage("rules", "rules.ftlh", Section.RULES, "SEHICL Rules")).process();
    }

    @RequestMapping("/rules/constitution")
    public String constitution()
    {
        return new PageTemplate(new StaticPage("rules", "rules/constitution/index.ftlh",
                Section.RULES, "SEHICL Constitution")).process();
    }

    @RequestMapping("/rules/administration")
    public String adminRules()
    {
        return new PageTemplate(new StaticPage("rules", "rules/administration/index.ftlh",
                Section.RULES, "SEHICL Administrative Rules")).process();
    }

    @RequestMapping("/rules/playing")
    public String playingRules()
    {
        return new PageTemplate(new StaticPage("rules", "rules/playing/index.ftlh", Section.RULES,
                "SEHICL Rules Of Play")).process();
    }

    @RequestMapping("/records")
    public String recordsIndex()
    {
        return new PageTemplate(
                new StaticPage("records", "records/index.ftlh", Section.RECORDS, "SEHICL Records"))
                        .process();
    }

    @RequestMapping("/records/performances")
    public String recordPerformances()
    {
        return new PageTemplate(new StaticPage("records", "records/performances.ftlh",
                Section.RECORDS, "SEHICL Record Performances")).process();
    }

    @RequestMapping("/records/winners")
    public String divisionalWinners()
    {
        return new PageTemplate(new StaticPage("divwinners", "records/divwinners.ftlh",
                Section.RECORDS, "SEHICL Divisional Winners")).process();
    }

    @RequestMapping("/records/awards")
    public String individualAwards()
    {
        return new PageTemplate(new StaticPage("awards", "records/individualawards.ftlh",
                Section.RECORDS, "SEHICL Individual Awards")).process();
    }

    @RequestMapping("/records/fairplay")
    public String fairplay()
    {
        return new PageTemplate(new StaticPage("fairplay", "records/fairplay.ftlh", Section.RECORDS,
                "SEHICL Sporting & Efficiency")).process();
    }

    @RequestMapping("/presentation")
    public String presentationEvening()
    {
        return new PageTemplate(new StaticPage("presentation", "presentation/schedule.ftlh",
                Section.HOME, "SEHICL Presentation Evening")).process();
    }

    @RequestMapping("/archive/presentation/{season}")
    public String presentationEvening(@PathVariable String season)
    {
        return new PageTemplate(
                new StaticPage("presentation", String.format("presentation/%s.ftlh", season),
                        Section.ARCHIVE, String.format("SEHICL Presentation Evening %s", season)))
                                .process();
    }

    @RequestMapping("/tables")
    public String currentTables()
    {
        return new PageTemplate(new LeagueTablesPage()).process();
    }

    @RequestMapping("/tables/league/{leagueId}")
    public String currentTable(@PathVariable String leagueId)
    {
        return new PageTemplate(new LeagueTablePage(leagueId)).process();
    }

    @RequestMapping("/archive/table/{leagueId}/{season}")
    public String archiveTable(@PathVariable String leagueId, @PathVariable int season)
    {
        Page page = season <= 5
                ? new StaticPage("archive", String.format("archive%d/%s.html", season, leagueId),
                        Section.ARCHIVE, "SEHICL Archive")
                : new LeagueTablePage(leagueId, season);
        return new PageTemplate(page).process();
    }

    @RequestMapping("/averages/batting/{selector}")
    public String currentBattingAverages(@PathVariable String selector)
    {
        return new PageTemplate(
                new LeagueBattingAveragesPage(LeagueSelector.valueOf(selector.toUpperCase())))
                        .process();
    }

    @RequestMapping("/archive/batting/{selector}/{season}")
    public String archiveBattingAverages(@PathVariable String selector, @PathVariable int season)
    {
        Page page = season <= 5
                ? new StaticPage("archive",
                        String.format("archive%d/%sBatting.html", season, selector),
                        Section.ARCHIVE, "SEHICL Archive")
                : new LeagueBattingAveragesPage(LeagueSelector.valueOf(selector.toUpperCase()),
                        season);
        return new PageTemplate(page).process();
    }

    @RequestMapping("/averages/bowling/{selector}")
    public String currentBowlingAverages(@PathVariable String selector)
    {
        return new PageTemplate(
                new LeagueBowlingAveragesPage(LeagueSelector.valueOf(selector.toUpperCase())))
                        .process();
    }

    @RequestMapping("/archive/bowling/{selector}/{season}")
    public String archiveBowlingAverages(@PathVariable String selector, @PathVariable int season)
    {
        Page page = season <= 5
                ? new StaticPage("archive",
                        String.format("archive%d/%sBowling.html", season, selector),
                        Section.ARCHIVE, "SEHICL Archive")
                : new LeagueBowlingAveragesPage(LeagueSelector.valueOf(selector.toUpperCase()),
                        season);
        return new PageTemplate(page).process();
    }

    @RequestMapping("/averages/team/{teamId}")
    public String currentTeamAverages(@PathVariable String teamId)
    {
        return new PageTemplate(new TeamAveragesPage(teamId)).process();
    }

    @RequestMapping("/archive/teamAverages/{teamId}")
    public String archiveTeamAverages(@PathVariable String teamId)
    {
        return new PageTemplate(new TeamAveragesPage(teamId, null)).process();
    }

    @RequestMapping("/archive/teamAverages/{teamId}/{season}")
    public String archiveTeamAverages(@PathVariable String teamId, @PathVariable int season)
    {
        return new PageTemplate(new TeamAveragesPage(teamId, season)).process();
    }

    @RequestMapping("/averages/byTeam")
    public String teamAveragesIndex()
    {
        return new PageTemplate(new TeamAveragesIndexPage()).process();
    }

    @RequestMapping("/averages")
    public String averagesIndex()
    {
        return new PageTemplate(new StaticPage("averages", "averagesindex.ftlh", Section.AVERAGES,
                "SEHICL Averages")).process();
    }

    @RequestMapping("/archive")
    public String archiveIndex()
    {
        return new PageTemplate(new ArchiveIndexPage()).process();
    }

    @RequestMapping("/archive/season/{season}")
    public String seasonArchiveIndex(@PathVariable int season)
    {
        return new PageTemplate(new SeasonArchiveIndexPage(season)).process();
    }

    @RequestMapping("/fixtures/team/{teamId}")
    public String teamFixtures(@PathVariable String teamId)
    {
        return new PageTemplate(new TeamFixturesPage(teamId)).process();
    }

    @RequestMapping("/fixtures/team/{teamId}/{season}")
    public String archiveTeamFixtures(@PathVariable String teamId, @PathVariable int season)
    {
        return new PageTemplate(new TeamFixturesPage(teamId, season)).process();
    }

    @RequestMapping("/fixtures")
    public String leagueFixtures()
    {
        return new PageTemplate(new LeagueFixturesPage()).process();
    }

    @RequestMapping("/fixtures/{season}")
    public String leagueFixtures(@PathVariable int season)
    {
        return new PageTemplate(new LeagueFixturesPage(season)).process();
    }

    @RequestMapping("/fixtures/league/{leagueId}")
    public String leagueFixtures(@PathVariable String leagueId)
    {
        return new PageTemplate(new LeagueFixturesPage(leagueId)).process();
    }

    @RequestMapping("/fixtures/league/{leagueId}/{season}")
    public String leagueFixtures(@PathVariable String leagueId, @PathVariable int season)
    {
        return new PageTemplate(new LeagueFixturesPage(season, leagueId)).process();
    }

    @RequestMapping("/results")
    public String dateResultsLatest()
    {
        return new PageTemplate(new DateResultsPage()).process();
    }

    @RequestMapping("/results/date/{date}")
    public String dateResults(@PathVariable String date) throws ParseException
    {
        return new PageTemplate(new DateResultsPage(DateUtils.parseDate(date, "yyyyMMdd")))
                .process();
    }

    @RequestMapping("/results/league/{leagueId}")
    public String leagueResults(@PathVariable String leagueId)
    {
        return new PageTemplate(new LeagueResultsPage(leagueId)).process();
    }

    @RequestMapping("/dutyRota")
    public String dutyRota()
    {
        return new PageTemplate(
                new StaticPage("dutyRota", "dutyRota.ftlh", Section.FIXTURES, "SEHICL Duty Rota"))
                        .process();
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login()
    {
        return new PageTemplate(new LoginPage(userManager)).process();
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(HttpSession session, HttpServletResponse resp, @RequestParam String email,
            @RequestParam String password, @RequestParam("Login") Optional<String> loginCmd)
            throws IOException
    {
        Login login = new Login(userManager, email, password);
        if (loginCmd.isPresent())
        {
            Long token = login.validateAndLogin();
            if (token != null)
            {
                UserSession userSession = new UserSession(session);
                userSession.setToken(token);
                resp.sendRedirect(userSession.getRedirectTarget());
                return "";
            }
        }
        else
        {
            login.validateAndReset(getRequestUri("/pwdReset"));
        }
        return new PageTemplate(new LoginPage(login)).process();
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String register() throws IOException
    {
        return new PageTemplate(new RegisterPage(userManager)).process();
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(HttpServletResponse resp, @RequestParam String email,
            @RequestParam String name, @RequestParam String club, @RequestParam String password,
            @RequestParam String passwordConf, @RequestParam String agreement,
            @RequestParam("g-recaptcha-response") String recaptchaResponse) throws IOException
    {
        if (!realPerson(recaptchaResponse))
        {
            resp.sendRedirect(getRequestUri("/register"));
            return "";
        }
        Register register = new Register(userManager, email, name, club, password, passwordConf,
                agreement != null);
        User user = register.validateAndRegister(getRequestUri("/"));
        return new PageTemplate(
                user == null ? new RegisterPage(register) : new RegisterConfPage(user)).process();
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
        return new PageTemplate(new ActivatePage(user)).process();
    }

    @RequestMapping(path = "/userDetails/{userId}")
    public String userDetails(HttpSession session, HttpServletResponse resp,
            @PathVariable long userId) throws IOException
    {
        UserSession userSession = new UserSession(session);
        if (userManager.sessionHasRole(userSession.getToken(), "admin"))
        {
            User user = userManager.getUserById(userId);
            return new PageTemplate(new UserDetailsPage(user)).process();
        }
        userSession.setRedirectTarget(getRequestUri());
        resp.sendRedirect(getRequestUri("/login"));
        return "";
    }

    @RequestMapping(path = "/deleteUser/{userId}", method = RequestMethod.GET)
    public String deleteUser(HttpSession session, HttpServletResponse resp,
            @PathVariable long userId) throws IOException
    {
        UserSession userSession = new UserSession(session);
        if (userManager.sessionHasRole(userSession.getToken(), "admin"))
        {
            User user = userManager.getUserById(userId);
            return new PageTemplate(new DeleteUserPage(user, false)).process();
        }
        userSession.setRedirectTarget(getRequestUri());
        resp.sendRedirect(getRequestUri("/login"));
        return "";
    }

    @RequestMapping(path = "/deleteUser/{userId}", method = RequestMethod.POST)
    public String deleteUserConfirmed(HttpSession session, HttpServletResponse resp,
            @PathVariable long userId) throws IOException
    {
        UserSession userSession = new UserSession(session);
        if (userManager.sessionHasRole(userSession.getToken(), "admin"))
        {
            User user = userManager.getUserById(userId);
            userManager.deleteUser(userId);
            return new PageTemplate(new DeleteUserPage(user, true)).process();
        }
        userSession.setRedirectTarget(getRequestUri());
        resp.sendRedirect(getRequestUri("/login"));
        return "";
    }

    @RequestMapping(path = "/pwdReset/{resetId}", method = RequestMethod.GET)
    public String passwordReset(@PathVariable long resetId) throws IOException
    {
        return new PageTemplate(new ResetPage(new Reset(resetId, userManager))).process();
    }

    @RequestMapping(path = "/pwdReset/{resetId}", method = RequestMethod.POST)
    public String passwordReset(HttpServletResponse resp, @PathVariable long resetId,
            @RequestParam String password, @RequestParam String passwordConf,
            @RequestParam("g-recaptcha-response") String recaptchaResponse) throws IOException
    {
        if (!realPerson(recaptchaResponse))
        {
            resp.sendRedirect(getRequestUri("/pwdReset/%d".formatted(resetId)));
            return "";
        }
        Reset reset = new Reset(resetId, userManager);
        if (reset.validateAndReset(password, passwordConf))
        {
            resp.sendRedirect(getRequestUri("/login"));
            return "";
        }
        return new PageTemplate(new ResetPage(reset)).process();
    }

    @RequestMapping(path = "/reconfirm/{userId}", method = RequestMethod.GET)
    public String reconfirmUser(@PathVariable long userId) throws IOException
    {
        return new PageTemplate(new ReconfirmPage(new Reconfirm(userId, userManager))).process();
    }

    @RequestMapping(path = "/reconfirm/{userId}", method = RequestMethod.POST)
    public String reconfirm(HttpServletResponse resp, @PathVariable long userId,
            @RequestParam String agreement) throws IOException
    {
        Reconfirm reconfirm = new Reconfirm(userId, userManager);
        try
        {
            if (reconfirm.validateAndReconfirm(agreement != null))
            {
                resp.sendRedirect(getRequestUri("/reconfConf"));
                return "";
            }
        }
        catch (EmailException ex)
        {
            LOG.error("Unexpected exception", ex);
        }
        return new PageTemplate(new ReconfirmPage(reconfirm)).process();
    }

    @RequestMapping(path = "/reconfConf")
    public String confirmReconfirmation() throws IOException
    {
        return new PageTemplate(new StaticPage("reconfirm", "reconfConf.ftlh", null, "Thank you"))
                .process();
    }

    @RequestMapping(path = "/dp")
    public String dataProtection() throws IOException
    {
        return new PageTemplate(new StaticPage("dp", "dataProtection.ftlh", Section.DP,
                "SEHICL Data Protection Policy")).process();
    }

    @RequestMapping(path = "/admin/reconf")
    public String sendUserReconfirmRequests(HttpSession session) throws IOException
    {
        final UserSession userSession = new UserSession(session);
        if (!userManager.sessionHasRole(userSession.getToken(), "admin"))
            return "Not authorised";
        String reconfirmationPageAddress = URI
                .create(getRequestUri())
                .resolve("/reconfirm")
                .toString();
        userManager.sendReconfirmationEmails(reconfirmationPageAddress);
        return "Emails sent";
    }

    @RequestMapping(path = "/admin/userExport")
    public String exportUsers(@RequestHeader String adminSecret, HttpServletResponse resp)
            throws IOException
    {
        if (adminSecret == null || EnvVar.ADMIN_SECRET.get().filter(adminSecret::equals).isEmpty())
        {
            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
            return "";
        }
        return usersExporter.export();
    }

    @PostMapping(path = "/admin/userImport")
    public String importUsers(@RequestHeader String adminSecret, @RequestBody String content,
            HttpServletResponse resp) throws IOException
    {
        try
        {
            if (adminSecret == null
                    || EnvVar.ADMIN_SECRET.get().filter(adminSecret::equals).isEmpty())
            {
                resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                return "";
            }
            if (content.length() == 0)
            {
                resp.setStatus(HttpStatus.BAD_REQUEST.value());
                return "";
            }
            return String
                    .format("%d user(s) imported",
                            usersImporter.importUsers(new StringReader(content)));
        }
        catch (Throwable ex)
        {
            StringWriter sw = new StringWriter();
            try (PrintWriter pw = new PrintWriter(sw))
            {
                ex.printStackTrace(pw);
            }
            return sw.toString();
        }
    }

    @Value("${recaptcha.url:https://www.google.com/recaptcha/api/siteverify}")
    private String recaptchaUrl;

    private boolean realPerson(String recaptchaResponse)
    {
        return EnvVar.RECAPTCHA_SECRET.get().map(recaptchaSecret ->
        {
            try
            {
                var restTemplate = new RestTemplate();
                var requestMap = new LinkedMultiValueMap<String, String>();
                requestMap.add("secret", recaptchaSecret);
                requestMap.add("response", recaptchaResponse);
                var apiResponse = restTemplate
                        .postForObject(recaptchaUrl, requestMap, String.class);
                LOG.info(apiResponse.toString());
                var answer = apiResponse != null
                        && apiResponse.indexOf("\"success\": true") > -1;
                if (answer)
                    LOG.info("Recaptcha validation succeeded: {}", apiResponse);
                else
                    LOG.error("Recaptcha validation failed: {}", apiResponse);
                return answer;
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }).orElse(true);
    }
}