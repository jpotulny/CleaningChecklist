package org.trinity_services.cleaningchecklist.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Checklist {

    private List<ChecklistItem> items;
    private static final String CATEGORY = "Item Category";
    private static final String ITEM_AND_SCORE = "Item and Score";
    private static final String LOG_TAG = "[TSI]Cleaning Checklist";
    private static final String REP_TITLE = "Report Title";
    private static final String HEADER = "Header Information";
    private static final String COMMENTS = "Comments";
    private Set<String> categoryNames;
    private String comments;
    private String houseName;
    private String reviewerName;
    private String reviewerTitle;
    private String department;
    private Date dateOfList;

    public Checklist()
    {
        items = new ArrayList<ChecklistItem>();
        categoryNames = new HashSet<String>();
    }


    public void addItem(String categoryText, ChecklistCategory categoryType, int score)
    {
        try {
            ChecklistItem item = new ChecklistItem(categoryText, categoryType);
            item.setScore(score);
            if(items.contains(item)) {
                items.remove(item);
                items.add(item);
            } else {
                items.add(item);
            }
            categoryNames.add(item.getJsonItem().getString(CATEGORY));

        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON Error in Alternate Initializer for Checklist.AddItem: " + e.getMessage());
        }
    }

    /**
     * For better information hiding, this particular method should not be used externally.
     *
     * @deprecated
     * @param item
     */
    public void addItem(ChecklistItem item)
    {
        try {
            String catName = item.getJsonItem().getString(CATEGORY);
            items.add(item);
            categoryNames.add(catName);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON Error in Checklist.AddItem: " + e.getMessage());
        }
    }

    public JSONObject getChecklistData() {
        JSONObject aggregator = new JSONObject();
        try {
            aggregator.put(REP_TITLE, "Report" + System.currentTimeMillis());
            JSONArray header = new JSONArray();

            header.put(this.getHouseName());
            header.put(this.getReviewerName());
            header.put(this.getReviewerTitle());
            header.put(this.getDepartment());
            header.put(this.getDateOfList());

            aggregator.put(HEADER, header);
            aggregator.put(COMMENTS, this.getComments());
            for(String s : categoryNames) {
                //This is inefficient - there's probably a better way to do this that isn't so many iterations
                JSONArray array = new JSONArray();

                for(int x = 0; x < items.size(); x++) {
                    if(items.get(x).getJsonItem().getString(CATEGORY).equals(s)) {
                        array.put(items.get(x).getJsonItem().get(ITEM_AND_SCORE));
                    }
                }
                aggregator.put( s, array);
            }
        } catch(JSONException e) {
            Log.e(LOG_TAG,"JSON Exception: " + e.getMessage());
        }
        return aggregator;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public void setReviewerTitle(String reviewerTitle) {
        this.reviewerTitle = reviewerTitle;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setDateOfList(Date dateOfList) {
        this.dateOfList = dateOfList;
    }
    public void setComments(String comments) {
        this.comments=comments;
    }
    private String getHouseName() {
        return houseName;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public String getReviewerTitle() {
        return reviewerTitle;
    }

    public String getDepartment() {
        return department;
    }

    public Date getDateOfList() {
        return dateOfList;
    }
    public String getComments() {
        return this.comments;
    }

    public void reset() {
        items.clear();
        categoryNames.clear();
        comments = "";
        houseName = "";
        reviewerName = "";
        reviewerTitle = "";
        department = "";
    }

    public class ChecklistItem {

        private ChecklistCategory category;
        private int score;
        private String itemText;

        /**
         * Left in for compatibility reasons (some frameworks in Android require default constructors)
         */
        private ChecklistItem() {

        }


        /**
         * Preferred constructor.
         *
         * @param s The text that describes the item that this Class is scoring.
         * @param c The category that the item falls under.
         */
        public ChecklistItem(String s, ChecklistCategory c) {
            itemText=s;
            category=c;
        }

        public void setScore(int score) {
            this.score = score;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ChecklistItem that = (ChecklistItem) o;

            if (category != that.category) return false;
            if (!itemText.equals(that.itemText)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = category.hashCode();
            result = 31 * result + itemText.hashCode();
            return result;
        }

        public JSONObject getJsonItem() {
            JSONObject holder = new JSONObject();
            JSONArray array = new JSONArray();
            try {
                holder.put(CATEGORY, category.toString());
                array.put(itemText);
                array.put(score);
                holder.put(ITEM_AND_SCORE, array);
            } catch (JSONException e) {
                Log.e(LOG_TAG,"JSON Error in Item: " + e.getMessage());
            }
            //Log.i(LOG_TAG, "Returning JSON from Item: " + holder.toString());
            return holder;
        }
    }
}
