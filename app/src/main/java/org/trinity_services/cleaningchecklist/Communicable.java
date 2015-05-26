package org.trinity_services.cleaningchecklist;

import android.os.Bundle;

import org.trinity_services.cleaningchecklist.models.ChecklistCategory;

/**
 * Created by JLP on 8/22/2014.
 */
public interface Communicable {
    void putScore(int score, int childIndex, ChecklistCategory category);
    void putReportInfo(Bundle reportInfo);
}
