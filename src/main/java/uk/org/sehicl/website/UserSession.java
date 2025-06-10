package uk.org.sehicl.website;

import jakarta.servlet.http.HttpSession;

public class UserSession
{
    private final HttpSession session;
    private Long token;
    private String redirectTarget;

    public UserSession(HttpSession session)
    {
        this.session = session;
        token = (Long) session.getAttribute("token");
        redirectTarget = (String) session.getAttribute("redirectTarget");
    }

    public Long getToken()
    {
        return token;
    }

    public void setToken(Long token)
    {
        this.token = token;
        session.setAttribute("token", token);
    }

    public String getRedirectTarget()
    {
        return redirectTarget == null ? "/" : redirectTarget;
    }

    public void setRedirectTarget(String redirectTarget)
    {
        this.redirectTarget = redirectTarget;
        session.setAttribute("redirectTarget", redirectTarget);
    }
}
