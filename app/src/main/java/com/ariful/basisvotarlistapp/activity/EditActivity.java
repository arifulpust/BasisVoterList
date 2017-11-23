package com.ariful.basisvotarlistapp.activity;

import android.content.ContentValues;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ariful.basisvotarlistapp.R;
import com.ariful.basisvotarlistapp.database.AppData;
import com.ariful.basisvotarlistapp.database.DBTablesInfo;
import com.ariful.basisvotarlistapp.database.DatabaseHelper;
import com.ariful.basisvotarlistapp.model.Person;
import com.ariful.basisvotarlistapp.utils.ToastyMsg;
import com.google.gson.Gson;

import org.chalup.microorm.MicroOrm;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EditActivity extends AppCompatActivity {

    //btn_confirm

    @BindView(R.id.mid)
    EditText mid;
    @BindView(R.id.titleBar)
    TextView titleBar;
//titleBar
    @BindView(R.id.company)
EditText company;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.designation)
    EditText designation;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.comment)
    EditText comment;
    @BindView(R.id.isVisited)
    CheckBox isVisited;
    @BindView(R.id.possibilySpinner)
    Spinner possibilySpinner;
    @BindView(R.id.isCalled)
    CheckBox isCalled;
    @BindView(R.id.btn_confirm)
    Button btn_confirm;
    Unbinder unbinder;
    DatabaseHelper db;
    Person person;
    boolean flag;
    int id;
    private ArrayList<String> posibilitirs = new ArrayList<>(
            Arrays.asList( new String[]{"Negative","Medium","Positive","Confirm","None"} ) );
    ArrayAdapter<String> spinnerArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
setTheme(R.style.AppTheme2);
        setContentView(R.layout.activity_edit);
        unbinder=  ButterKnife.bind(this);
        btn_confirm=(Button)findViewById(R.id.btn_confirm);
        boolean isData=getIntent().getBooleanExtra(AppData.check,false);
        Log.e("isData",""+isData);
        if(isData)
        {
            SetData();
        }
       else
        {
            setVanSpinner(0);
            titleBar.setText("Add Voter");
            btn_confirm.setText("Save");
        }

    }

    String Posibility="";

    private void setVanSpinner(int pos)
    {
        //  restVanList.addAll(vanMessage.getRestVanList());

        try {
            spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, posibilitirs);
            possibilySpinner.setAdapter(spinnerArrayAdapter);
        }catch (Exception e){

            e.printStackTrace();
        }
        possibilySpinner.setSelection(pos);
        possibilySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Posibility = posibilitirs.get(i);
                try {
                    ((TextView) view).setTextColor(Color.parseColor("#4a4a4a"));
                    //((TextView) adapterView.getChildAt(i)).setTextColor(Color.parseColor("#4a4a4a"));
                }
                catch (Exception E)
                {

                }
                Log.e("Posibility",""+Posibility);
                //SetDataByCatChanged(selectedCat);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
    private int getItemFromPosibility(String item)
    {
Log.e("item",""+item);
        if(item.trim().equals("")||item.trim().length()<1){
            return 4;
        }
        else if(item.trim().toLowerCase().equals("negative"))
        {
            return 0;
        }
        else if(item.trim().toLowerCase().equals("medium"))
        {
            return 1;
        }
        else if(item.trim().toLowerCase().equals("positive"))
        {
            return 2;
        }
        else if(item.trim().toLowerCase().equals("confirm"))
        {
            return 3;
        }
        else
        {
            return 4;
        }

    }
private void SetData()
{

    Bundle b = this.getIntent().getExtras();
    if (b != null) {
       this. person = (Person) b.getSerializable(AppData.Person);
        Log.e("person",""+new Gson().toJson(person));
        id=Integer.parseInt(person.id);
        company.setText(person.company_name);
        mid.setText(person.MID);
        name.setText(person.name);
        designation.setText(person.designation);
        address.setText(person.address);
        mobile.setText(person.mobile);
        comment.setText(person.comments);
        if(person.isvisited==1)
        {isVisited.setChecked(true);

        }
        else
        {
            isVisited.setChecked(false);
        }
        if(person.isCalled==1)
        {isCalled.setChecked(true);

        }
        else
        {
            isCalled.setChecked(false);
        }
Log.e("posibility",""+getItemFromPosibility(person.possiblity));
        setVanSpinner( getItemFromPosibility(person.possiblity));
       // possibily.setText(person.possiblity);
        titleBar.setText("Update Voter");
        btn_confirm.setText("Update");
    }
    else
    {
        setVanSpinner(0);
        titleBar.setText("Add Voter");
        btn_confirm.setText("Save");
    }


}
@OnClick(R.id.back_button)
void BackPress()
{
    finish();
    overridePendingTransition(   R.anim.pull_in_left, R.anim.push_out_right);
}
    @OnClick(R.id.btn_confirm)
     void SaveVoter()
    {
       if(btn_confirm.getText().toString().trim().toLowerCase().equals("save"))
       {
           SaveVoterInfo();
       }
       else
       {
           UpdateVoterInfo();
       }
    }
private void  SaveVoterInfo()
{

    try {
        db = new DatabaseHelper(getApplicationContext());
        ArrayList<ContentValues> contentValues = new ArrayList<>();
                        Person person = new Person();
                        if( company.getText().toString().trim().length()<3) {
                            ToastyMsg.Warning(getApplicationContext(), "Company Name Empty");
return;
                        }
        if( name.getText().toString().trim().length()<3) {
            ToastyMsg.Warning(getApplicationContext(), "Name Empty");
            return;
        }
                        person.company_name = company.getText().toString().trim();
                        person.MID = mid.getText().toString().trim();
                        person.name =  name.getText().toString().trim();
                        person.designation =  designation.getText().toString().trim();
                        person.address =  address.getText().toString().trim();
                        person.mobile =  mobile.getText().toString().trim();
                        person.comments =  comment.getText().toString().trim();

                       person.possiblity = Posibility;

        if(isVisited.isChecked())
        { person.isvisited=1;

        }
        else
        {
            person.isvisited=0;
        }

        if(isCalled.isChecked())
        { person.isCalled=1;

        }
        else
        {
            person.isCalled=0;
        }
                        MicroOrm uOrm = new MicroOrm();
                        ContentValues values = uOrm.toContentValues(person);
                        contentValues.add(values);

                        flag = db.addItems(DBTablesInfo.TABALE_NAME, contentValues);
                        Log.e("saved", "" + flag);
                        // db.close();
                        Log.e("saved", "" + flag);

                        if(flag)
                        {
                            ToastyMsg.Success(getApplicationContext(),"Voter Save Successfully!");

                            finish();
                            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                            //overridePendingTransition(   R.anim.pull_in_left, R.anim.push_out_right);
                        }
    }
    catch (Exception e)
    {

    }

}
    private void  UpdateVoterInfo()
    {
Person persons=new Person();
        persons.id=person.id;
        persons.company_name = company.getText().toString().trim();
        persons.MID = mid.getText().toString().trim();
        persons.name =  name.getText().toString().trim();
        persons.designation =  designation.getText().toString().trim();
        persons.address =  address.getText().toString().trim();
        persons.mobile =  mobile.getText().toString().trim();
        persons.comments =  comment.getText().toString().trim();
        persons.possiblity = Posibility;

        if(isVisited.isChecked())
        { persons.isvisited=1;

        }
        else
        {
            persons.isvisited=0;
        }

        if(isCalled.isChecked())
        { persons.isCalled=1;

        }
        else
        {
            persons.isCalled=0;
        }
        try {
        db = new DatabaseHelper(getApplicationContext());
            Log.e("person update", person.getName()+person.name+"" + new Gson().toJson(persons));
            int update = db.UpdateValue(DBTablesInfo.ID_KEY, DBTablesInfo.TABALE_NAME, persons);
            if (update > 0) {
                finish();
                ToastyMsg.Success(getApplicationContext(), "Successfully Updated");
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            } else {
                ToastyMsg.Error(getApplicationContext(), "Fail to Update");
            }

       }
       catch (Exception e)
        {
           Log.e("person", "" + e.getMessage());
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(   R.anim.pull_in_left, R.anim.push_out_right);
    }
}
