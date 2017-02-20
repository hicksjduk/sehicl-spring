package uk.org.sehicl.website.data;

import uk.org.sehicl.website.rules.Rules;

public interface Outcome
{
    Completeness getCompleteness(Rules rules);
}
