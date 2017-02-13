package uk.org.sehicl.website.data;

import uk.org.sehicl.website.rules.Rules;

public interface Outcome
{
    boolean isComplete(Rules rules);
}
