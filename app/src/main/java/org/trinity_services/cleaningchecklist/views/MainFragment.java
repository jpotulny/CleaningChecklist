package org.trinity_services.cleaningchecklist.views;

import android.app.ExpandableListActivity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.trinity_services.cleaningchecklist.Communicable;
import org.trinity_services.cleaningchecklist.R;
import org.trinity_services.cleaningchecklist.dal.CustomExpandableListAdapter;
import org.trinity_services.cleaningchecklist.models.ChecklistCategory;

/**
 * Created by JLP on 8/22/2014.
 */
public class MainFragment extends Fragment implements CustomExpandableListAdapter.Sendable {

    private Context context;
    private static final String LOG_TAG = "[TSI]Cleaning Checklist";
    private Communicable c;
    private ExpandableListView checklistScreen;
    private CustomExpandableListAdapter adapter;
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = (View)inflater.inflate(R.layout.list_view,null);
        checklistScreen = (ExpandableListView) view.findViewById(R.id.list);
        adapter = new CustomExpandableListAdapter(getActivity());
        checklistScreen.setAdapter(adapter);
        adapter.setSendable(this);
        return view;
    }

    @Override
    public void sendScore(int score, int parentLocation, int childLocation) {
        c.putScore(score, childLocation, ChecklistCategory.values()[parentLocation]);

    }
    public void setCommunicable(Communicable c) {
        this.c=c;
    }

    /**
     * Resets the <code>CustomExpandableListAdapter</code> to an empty state.  Also, resets the
     * interface between the fragment and adapter, which was obliterated by the reassignment. Finally,
     * reassigns the class field <code>adapter</code> to the new reference.
     */
    public void reset() {
        CustomExpandableListAdapter blankAdapter = new CustomExpandableListAdapter(context);
        adapter = blankAdapter;
        checklistScreen.setAdapter(blankAdapter);
        adapter.setSendable(this);

    }
}
