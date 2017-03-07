package uk.org.sehicl.website.report;

import java.util.Collection;
import java.util.Date;

import uk.org.sehicl.website.report.ReportStatus.Status;

public interface Averages<T>
{
    Collection<T> getRows();
    Date getLastIncludedDate();
    Status getStatus();
    int getToCome();
}
