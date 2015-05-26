package org.trinity_services.cleaningchecklist.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.trinity_services.cleaningchecklist.R;

/**
 * Created by JLP on 8/24/2014.
 */
public class CommentsActivity extends Activity {

    private Button btnSubmit;
    private EditText etComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);

        btnSubmit = (Button)findViewById(R.id.btnSubmitComments);
        etComments = (EditText)findViewById(R.id.etCommentary);

        if(getIntent().getExtras()!=null) {
            etComments.setText(getIntent().getExtras().getString("comments"));
        }

        btnSubmit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("comments",etComments.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }


}
