package org.trinity_services.cleaningchecklist.dal;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.trinity_services.cleaningchecklist.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JLP on 8/22/2014.
 */
public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private List<String> categories;
    private Map<String, List<String>> children;
    private Context context;
    private static final String LOG_TAG = "[TSI]Cleaning Checklist";
    Sendable sendable;
    private Map<String,Integer> persistentScores;

    public CustomExpandableListAdapter(Context context) {
        this.context = context;
        categories = new ArrayList<String>();
        children = new LinkedHashMap<String, List<String>>();
        setCategories();
        persistentScores = new HashMap<String, Integer>();

    }

    @Override
    public int getGroupCount() {
        return this.categories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.children.get(this.categories.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.categories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.children.get(this.categories.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View retVal = convertView;
        if(retVal==null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            retVal = (View) inflater.inflate(R.layout.list_category,null);
        }

        String categoryName = this.categories.get(groupPosition);
        TextView categoryTextView = (TextView) retVal.findViewById(R.id.listCategory);
        categoryTextView.setText(categoryName);

        return retVal;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View retVal = convertView;
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        retVal = (View) inflater.inflate(R.layout.custom_child_view,null);

        String question = this.children.get(categories.get(groupPosition)).get(childPosition);


        TextView questionText = (TextView)retVal.findViewById(R.id.child_question);
        final Spinner spinner = (Spinner)retVal.findViewById(R.id.item_score);

        questionText.setText(question);

        spinner.setSelection(2);
        if(spinner.getTag()==null) {
            spinner.setTag("Spinner-" + groupPosition + "-" + childPosition);
            Log.d(LOG_TAG, "Spinner tag set: " + spinner.getTag());
        }

        if(persistentScores.get(spinner.getTag().toString())==null) {
            persistentScores.put(spinner.getTag().toString(), Integer.valueOf(spinner.getItemAtPosition(childPosition).toString()));
            Log.w(LOG_TAG,"Adding new score: " + spinner.getTag().toString());
        } else {
            int selectionPosition = (int) Integer.valueOf(persistentScores.get(spinner.getTag().toString()));
            selectionPosition+=2;
            Log.w(LOG_TAG,"RETRIEVING SCORE: " + spinner.getTag().toString());
            spinner.setSelection(selectionPosition);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int score = 0;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                score = Integer.parseInt(parent.getItemAtPosition(position).toString());
                sendable.sendScore(score,groupPosition,childPosition);
                persistentScores.put(spinner.getTag().toString(),score);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.w(LOG_TAG,"This shouldn't fire");
            }
        });
        return retVal;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private void setCategories() {
        //TODO decouple this
        String[] catsFromResources = context.getResources().getStringArray(R.array.categories);
        for(String s : catsFromResources) {
            categories.add(s);
        }
        //TODO Refactor for maintainability.

        setChildren(categories.get(0),R.array.Dusting);
        setChildren(categories.get(1),R.array.Kitchen);
        setChildren(categories.get(2),R.array.Bathroom);
        setChildren(categories.get(3),R.array.Bedroom);
        setChildren(categories.get(4),R.array.Flooring);
        setChildren(categories.get(5),R.array.Furniture);
        setChildren(categories.get(6),R.array.LaundryRoom);
        setChildren(categories.get(7),R.array.Walls);
        setChildren(categories.get(8),R.array.ExteriorOfHome);
    }
    private void setChildren(String key, int valuesId) {
        //TODO decouple this
        String[] childValues = context.getResources().getStringArray(valuesId);
        ArrayList<String> v = new ArrayList<String>();
        for(String s : childValues) {
            v.add(s);
        }
        Log.d(LOG_TAG,"Putting list for " + key + ":" + v.size());
        children.put(key,v);
    }

    public void reset() {
        categories.clear();
        children.clear();
        persistentScores.clear();


    }

    public interface Sendable {
        void sendScore(int score, int parentLocation, int childLocation);
    }

    public void setSendable(Sendable s)
    {
        Log.w(LOG_TAG, "Setting Sendable: " + s);
        this.sendable=s;
    }


}
