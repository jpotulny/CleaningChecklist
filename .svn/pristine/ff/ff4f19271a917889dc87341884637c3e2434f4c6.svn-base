package org.trinity_services.cleaningchecklist.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.trinity_services.cleaningchecklist.Communicable;
import org.trinity_services.cleaningchecklist.R;
import org.trinity_services.cleaningchecklist.dal.ReportTransferObject;
import org.trinity_services.cleaningchecklist.models.Checklist;
import org.trinity_services.cleaningchecklist.models.ChecklistCategory;

import java.sql.Date;
import java.util.zip.Inflater;

/**
 * Created by JLP on 8/22/2014.
 *
 * Currently has some separation of concerns issues. Not acting as a true controller class because
 * it is handling events and views for a few dialog windows that should be handled by their
 * individual classes.
 */
public class MainActivity extends FragmentActivity implements Communicable {

    //----------CONSTANTS--------------------------------------------------------------------------
    private static final String REVIEWER_TITLE = "Reviewer Title";
    private static final String REVIEWER = "Reviewer Name";
    private static final String COMMENTS = "Comments";
    private static final String DEPT = "Department";
    private static final String HOUSE = "House";
    private static final String LOG_TAG = "[TSI]Cleaning Checklist";
    private static final int COMMENT_REQUEST_CODE = 24601; //TODO find a more elegant solution than magic numbers

    //--------VIEW OBJECTS-------------------------------------------------------------------------
    private Spinner deptSpinner;
    private Spinner titleSpinner;
    private EditText etHouseName;
    private EditText etReviewerName;
    private MainFragment listFrag;
    private View preferencesMenu;  //TODO move to seperate class. SOC issue

    //-------DATA OBJECTS--------------------------------------------------------------------------
    private ReportTransferObject reportTransfer;
    private Checklist checklist;

    //TODO find a more elegant solution for the anonymous inner class scoping issue here
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listFrag = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.mainFragment);
        deptSpinner = (Spinner)findViewById(R.id.spinDepartments);
        titleSpinner = (Spinner)findViewById(R.id.spinTitles);
        etHouseName = (EditText)findViewById(R.id.etHouseName);
        etReviewerName = (EditText)findViewById(R.id.etReviewerName);

        checklist = new Checklist();

        listFrag.setContext(this);
        listFrag.setCommunicable(this);
        ImageButton imgbtnSubmit = (ImageButton) findViewById(R.id.imgSubmitList);
        ImageButton imgbtnComments = (ImageButton) findViewById(R.id.imgbtnComment);

        reportTransfer = new ReportTransferObject();
        reportTransfer.setInternalStorageDirectory(this.getFilesDir().toString());
        SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
        if(prefs.getString(this.getResources().getString(R.string.sharedPrefKey),"DEFAULT").equals("DEFAULT")
                || prefs.getString(this.getResources().getString(R.string.sharedPrefKey),"").equals("")) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(this.getResources().getString(R.string.sharedPrefKey),"http://192.168.2.9/TestProject/webresources/generic");
            editor.commit();
        } else {
            reportTransfer.setDestination(prefs.getString(this.getResources().getString(R.string.sharedPrefKey),"http://192.168.2.9/TestProject/webresources/generic"));
        }


        imgbtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Report Submission");
                dialog.setMessage("You are about to submit a cleaning checklist.  If you need to " +
                        "make any changes, please click on the Not Ready button and make your corrections.");

                dialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checklist.setDateOfList(new Date(System.currentTimeMillis()));
                        checklist.setDepartment(deptSpinner.getSelectedItem().toString());
                        checklist.setHouseName(etHouseName.getText().toString());
                        checklist.setReviewerName(etReviewerName.getText().toString());
                        checklist.setReviewerTitle(titleSpinner.getSelectedItem().toString());

                        listFrag.reset();

                        reportTransfer.sendReport(checklist);
                        checklist = new Checklist();
                        Toast.makeText(context, "Report Sent",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("Not Ready", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do Nothing
                    }
                });
                dialog.show();
            }
        });

        imgbtnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CommentsActivity.class);

                Log.w(LOG_TAG, "Intent Status: " + intent.getExtras());
                Log.w(LOG_TAG, "Comment Status: " + checklist.getComments());
                if(checklist.getComments()!=null) {
                    intent.putExtra("comments",checklist.getComments());
                }
                startActivityForResult(intent, COMMENT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case COMMENT_REQUEST_CODE:
                if(resultCode==RESULT_OK) {
                    String myResult = data.getExtras().getString("comments");
                    checklist.setComments(myResult);
                    Toast.makeText(this,"Comments Saved",Toast.LENGTH_SHORT).show();
                } else {
                    //Do nothing
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_bar,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_options:
                createPreferencesMenu();
                break;
            default:
                break;
        }
        return false;
    }

    private void createPreferencesMenu() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        if(preferencesMenu==null) {
            LayoutInflater inflate = getLayoutInflater();
            preferencesMenu = inflate.inflate(R.layout.preference_dialog, null,false);
        } else {
            ((ViewGroup) preferencesMenu.getParent()).removeAllViews();
        }
        dialog.setView(preferencesMenu);

        CheckBox acknowledgeRisk = (CheckBox)preferencesMenu.findViewById(R.id.checkEnableIPChange);
        final EditText etNewIp = (EditText)preferencesMenu.findViewById(R.id.etIpAddress);
        etNewIp.setText(reportTransfer.getDestination());
        acknowledgeRisk.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    etNewIp.setEnabled(true);
                } else {
                    etNewIp.setEnabled(false);
                }
            }
        });

        dialog.setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reportTransfer.setDestination(etNewIp.getText().toString());
                Toast.makeText(context,"IP updated",Toast.LENGTH_SHORT).show();

            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do Nothing
            }
        });
        dialog.show();
    }

    @Override
    public void putScore(int score, int childIndex, ChecklistCategory category) {

        int arrayId = getResources().getIdentifier(category.toString(),"array",getPackageName());
        String categoryText = getResources().getStringArray(arrayId)[childIndex];

        checklist.addItem(categoryText, category,score);
    }

    @Override
    public void putReportInfo(Bundle reportInfo) {
        //TODO remove method from interface
    }

}
