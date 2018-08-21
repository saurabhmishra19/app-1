package park.epam.com.parkit;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import park.epam.com.parkit.park.epam.com.dao.EmplyeeProvider;

public class RegistraionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registraion);
    }
    public void onClickAddName(View view) {
        // Add a new student record
        ContentValues values = new ContentValues();
        values.put(EmplyeeProvider.NAME,
                ((EditText)findViewById(R.id.editText2)).getText().toString());

        values.put(EmplyeeProvider.EMPID,
                ((EditText)findViewById(R.id.editText3)).getText().toString());

        Uri uri = getContentResolver().insert(
                EmplyeeProvider.CONTENT_URI, values);

        Toast.makeText(getBaseContext(),
                uri.toString(), Toast.LENGTH_LONG).show();
    }
    public void onClickRetrieveEmployee(View view) {
        // Retrieve student records
        String URL = "content://park.epam.com.parkit.park.epam.com.dao.EmplyeeProvider";

        Uri students = Uri.parse(URL);
        Cursor c = managedQuery(students, null, null, null, "name");

        if (c.moveToFirst()) {
            do{
                Toast.makeText(this,
                        c.getString(c.getColumnIndex(EmplyeeProvider._ID)) +
                                ", " +  c.getString(c.getColumnIndex( EmplyeeProvider.NAME)) +
                                ", " + c.getString(c.getColumnIndex( EmplyeeProvider.EMPID)),
                        Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
        }
    }





}
