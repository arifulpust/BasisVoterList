package com.ariful.basisvotarlistapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import com.ariful.basisvotarlistapp.R;
import com.ariful.basisvotarlistapp.adapter.MenuAdapter;
import com.ariful.basisvotarlistapp.adapter.VoterAdapter;
import com.ariful.basisvotarlistapp.database.AppData;
import com.ariful.basisvotarlistapp.database.DBTablesInfo;
import com.ariful.basisvotarlistapp.database.DatabaseHelper;
import com.ariful.basisvotarlistapp.interfaces.CheckInListener;
import com.ariful.basisvotarlistapp.model.Person;
import com.ariful.basisvotarlistapp.utils.ToastyMsg;
import com.jakewharton.rxbinding.widget.RxTextView;

import org.chalup.microorm.MicroOrm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity implements CheckInListener {

    @BindView(R.id.right_drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view_right)
    NavigationView navigationView;
    @BindView(R.id.lst_menu_items)
    ListView menuList;
    @BindView(R.id.totalvoter)
    TextView totalvoter;
    @BindView(R.id.search_list)
    EditText searchList;
    @BindView(R.id.catSpinner)
    Spinner catSpinner;
    @BindView(R.id.possibilySpinner)
    Spinner possibilySpinner;
    //possibilySpinner
    private ListView listView;
    Cursor c=null;
    DatabaseHelper db;
    @BindView(R.id.addvoter)
    FloatingActionButton addvoter;
    @BindView(R.id.listView)
    RecyclerView list;
    Unbinder unbinder;
    VoterAdapter voterAdapter;
    MenuAdapter menuAdapter;
    ArrayAdapter<String> spinnerArrayAdapter;
    ArrayAdapter<String> spinnerArrayAdapterPosivility;
    //totalvoter

    int isVisitedIndex=9,isCalleIndex=10,isPosibilityIndex=8;
    private ArrayList<String> posibilitirs = new ArrayList<>(
            Arrays.asList( new String[]{"Negative","Medium","Positive","Confirm","None"} ) );
    private ArrayList<String> visit = new ArrayList<>(
            Arrays.asList( new String[]{"Visited","Not Visited"} ) );
    private ArrayList<String> called = new ArrayList<>(
            Arrays.asList( new String[]{"Called","Not Called"} ) );
    private ArrayList<String> Collumns = new ArrayList<>(
            Arrays.asList( new String[]{"All","Company Name","Name","Address","MID","Designation","Mobile","Comments","Possiblity","Visited","Called"} ) );
    public static List<Person>personArrayList=new ArrayList<>();
    public static int listPosition=0;
    public static ArrayList<String> menuNames = new ArrayList<>(
            Arrays.asList( new String[]{"All","Uttara","Bashundhara R/A"," Baridhara DOHS","Progoti Sharani + Baridhara ","Mirpur + Mirpur DOHS"," Mohakhali + Mohakhali DOSH","Banani","Gulshan + Gulshan-1+ Niketon","Gulshan-2","BDBL + Janata Tower + Kawran Bazar ","Panthapath + Green Road + Farmgate","Dhanmondi","Mohammadpur + Shyamoli","Others"} ) );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder=  ButterKnife.bind(this);

        addvoter=(FloatingActionButton)findViewById(R.id.addvoter);

        addvoter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("addvoter","call");
                AddVoter();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        list.setHasFixedSize(true);
        list.setLayoutManager(linearLayoutManager);
        voterAdapter = new VoterAdapter(getApplicationContext(), new ArrayList<>(),this);
        list.setAdapter(voterAdapter);
        listPosition=0;
        db = new DatabaseHelper(MainActivity.this);
        menuAdapter = new MenuAdapter(getApplicationContext(),menuNames);
        menuList.setAdapter(menuAdapter);
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e("pressed", position+" dq"+listPosition);
                listPosition=position;
                for (int ctr=0;ctr<menuNames.size();ctr++){

try {
    if (position == ctr) {
        menuList.getChildAt(ctr).setBackgroundColor(Color.parseColor("#557db8"));
    } else {
        menuList.getChildAt(ctr).setBackgroundColor(Color.parseColor("#4B729E"));
    }
}catch ( Exception E)
{
  Log.e("Exception ","Exception color"+E.getMessage());
}
                }
                nenuIndex=position;
                drawer.closeDrawers();
                filterProductsByCategory(position);
            }
        });

        setVanSpinner();
    }

    int selectedCat=0,nenuIndex=0;
    private void setpinner(int type)
    {
        //  restVanList.addAll(vanMessage.getRestVanList());
        Log.e("setpinner",""+type);
        try {
            if(isPosibilityIndex==type) {
                spinnerArrayAdapterPosivility = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, posibilitirs);
            }
            else if(isVisitedIndex==type)
            {
                spinnerArrayAdapterPosivility = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, visit);
            }
            else if(isCalleIndex==type)
            {
                spinnerArrayAdapterPosivility = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, called);
            }
            possibilySpinner.setAdapter(spinnerArrayAdapterPosivility);
        }catch (Exception e){

            e.printStackTrace();
        }
        //   possibilySpinner.setSelection();
        possibilySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                SetDataByCatChanged(selectedCat,true,type,i);
                try {
                    ((TextView) view).setTextColor(Color.parseColor("#4a4a4a"));
                   // ((TextView) adapterView.getChildAt(i)).setTextColor(Color.parseColor("#4a4a4a"));
                }
                catch (Exception E)
                {

                }
                // Posibility = posibilitirs.get(i);
                //SetDataByCatChanged(selectedCat);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
    private void setVanSpinner()
    {
        //  restVanList.addAll(vanMessage.getRestVanList());

        try {
            spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, Collumns);
            catSpinner.setAdapter(spinnerArrayAdapter);
        }catch (Exception e){

            e.printStackTrace();
        }

        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCat = i;
                try {
                   // ((TextView) view).setTextColor(Color.RED);
                    ((TextView) view).setTextColor(Color.parseColor("#4a4a4a"));
                }
                catch (Exception E)
                {

                }
                if(isPosibilityIndex==selectedCat)
                {
                    searchList.setVisibility(View.GONE);
                    possibilySpinner.setVisibility(View.VISIBLE);
                    setpinner(selectedCat);
                }
                else if(isVisitedIndex==selectedCat)
                {
                    searchList.setVisibility(View.GONE);
                    possibilySpinner.setVisibility(View.VISIBLE);
                    //   GetDataVisitedORCalled(DBTablesInfo.ISVISITED);
                    setpinner(selectedCat);
                    //GetDataVisitedORCalled(DBTablesInfo.ISVISITED);
                }
                else if(isCalleIndex==selectedCat)
                {
                    searchList.setVisibility(View.GONE);
                    possibilySpinner.setVisibility(View.VISIBLE);
                    setpinner(selectedCat);
                    //GetDataVisitedORCalled(DBTablesInfo.ISCALLED);
                }
                else {
                    searchList.setVisibility(View.VISIBLE);
                    possibilySpinner.setVisibility(View.GONE);
                    SetDataByCatChanged(selectedCat,false,0,0);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
    void filterProductsByCategory(int position){

        GetDataByAddress(position);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.product_menu, menu);//Menu Resource, Menu
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }else{
                    drawer.openDrawer(GravityCompat.END);
                }
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    public void StartObservingSearchBar(EditText searchList) {


        Disposable _disposabl2 = RxJavaInterop.toV2Observable(RxTextView.textChanges(searchList))
                .debounce(300, TimeUnit.MILLISECONDS)
                .map(charSequence -> {
                    return filterList(charSequence.toString().trim());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Person>>() {
                    @Override
                    public void onNext(List<Person> data) {
                        totalvoter.setText("Toatal Voters: "+ data.size());
                        voterAdapter.setPerson(data);
                        voterAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public List<Person> filterList(String text) {
        List<Person> tempProduct = new ArrayList<>();

        if(text.length()>0) {
            tempProduct=  GetDataBySearchValue(text);
            return tempProduct;
        }
        else
        {
            //GetData();
            return GetDataData();
        }

    }
    private void GetDataVisitedORCalled(String cuoullumn,int value )
    {
        try {
            // db.openDataBase();
            Cursor cursor2 = db.getAllItemsByVisitedORCalled(DBTablesInfo.TABALE_NAME,cuoullumn,value);
            MicroOrm uOrm = new MicroOrm();
            personArrayList = uOrm.listFromCursor(cursor2, Person.class);
            if(personArrayList!=null&&personArrayList.size()>0) {
                // noticeAdapter.setNotice(someObjects2);
                for (int i = 0; i < personArrayList.size(); i++) {
                    // Log.e("notice title", personArrayList.get(i).id + " " + personArrayList.get(i).name + "  " + personArrayList.get(i).mobile);
                }
                Log.e("total size 2", "" + personArrayList.size());
            }
            totalvoter.setText("Toatal Voters: "+ personArrayList.size());
            voterAdapter.setPerson(personArrayList);


        } catch (Exception sqle) {

            throw sqle;

        }
    }
    private  List<Person> GetDataData()
    {
        try {
            // db.openDataBase();
            Cursor cursor2 = db.getAllResItems(DBTablesInfo.TABALE_NAME);
            MicroOrm uOrm = new MicroOrm();
            personArrayList = uOrm.listFromCursor(cursor2, Person.class);
            if(personArrayList!=null&&personArrayList.size()>0) {
                // noticeAdapter.setNotice(someObjects2);
                for (int i = 0; i < personArrayList.size(); i++) {
                    Log.e("notice title", personArrayList.get(i).id + " " + personArrayList.get(i).name + "  " + personArrayList.get(i).mobile);
                }
                Log.e("total size 2", "" + personArrayList.size());
            }
           // totalvoter.setText("Toatal Voters: "+ personArrayList.size());
          //  voterAdapter.setPerson(personArrayList);


        } catch (Exception sqle) {

            throw sqle;

        }
        return personArrayList;
    }
    private void GetData()
    {
        try {
            // db.openDataBase();
            Cursor cursor2 = db.getAllResItems(DBTablesInfo.TABALE_NAME);
            MicroOrm uOrm = new MicroOrm();
            personArrayList = uOrm.listFromCursor(cursor2, Person.class);
            if(personArrayList!=null&&personArrayList.size()>0) {
                // noticeAdapter.setNotice(someObjects2);
                for (int i = 0; i < personArrayList.size(); i++) {
                    Log.e("notice title", personArrayList.get(i).id + " " + personArrayList.get(i).name + "  " + personArrayList.get(i).mobile);
                }
                Log.e("total size 2", "" + personArrayList.size());
            }
            totalvoter.setText("Toatal Voters: "+ personArrayList.size());
            voterAdapter.setPerson(personArrayList);


        } catch (Exception sqle) {

            throw sqle;

        }
    }

    public String getCollumnName(int pos)
    {
        if(pos==1)
        {
            return DBTablesInfo.COMPANY_NAME;
        }
        else if(pos==2)
        {
            return DBTablesInfo.NAME;
        }
        else if(pos==3)
        {
            return DBTablesInfo.ADDRESS;
        }
        else if(pos==4)
        {
            return DBTablesInfo.MID;
        }
        else if(pos==5)
        {
            return DBTablesInfo.DESIGNATION;
        }
        else if(pos==6)
        {
            return DBTablesInfo.MOBILE;
        }
        else if(pos==7)
        {
            return DBTablesInfo.COMMENTS;
        }
        else if(pos==8)
        {
            return DBTablesInfo.POSIBILITY;
        }
        else
            return DBTablesInfo.COMPANY_NAME;
    }

    private void SetDataByCatChanged(int pos,boolean isPosibility,int type,int value)
    {
        List<Person> tempProduct = new ArrayList<>();
        try {

            Cursor cursor2=null;


            // db.openDataBase();
            Log.e("isPosibility ",""+selectedCat+  "  "+isPosibility+"  "+value );
//        if(pos==0||searchList.getText().toString().trim().length()<1) {
//            GetData();
//            return;
//        }
            String rearchText="";
            if(isPosibility)
            {



                if(type==isPosibilityIndex) {

                    rearchText= posibilitirs.get(value) ;
                    if (value == 4) {
                        cursor2 = db.getAllDateItemsByCat(DBTablesInfo.TABALE_NAME, getCollumnName(pos), "None");
                    }
                    else
                    {
                        cursor2 = db.getAllDateItemsByCat(DBTablesInfo.TABALE_NAME,getCollumnName(pos),rearchText );
                    }
                }
                else if(type==isVisitedIndex)
                {
                    GetDataVisitedORCalled(DBTablesInfo.ISVISITED,value);
                    return;
                }
                else if(type==isCalleIndex)
                {
                    GetDataVisitedORCalled(DBTablesInfo.ISCALLED,value);
                    return;
                }
                else

                    cursor2 = db.getAllDateItemsByCat(DBTablesInfo.TABALE_NAME,getCollumnName(pos),rearchText );


            }
            else  if(searchList.getText().toString().trim().length()>0)
            {


                rearchText=searchList.getText().toString().trim();

                Log.e("rearchText",""+rearchText);
                cursor2 = db.getAllDateItemsByCat(DBTablesInfo.TABALE_NAME,getCollumnName(pos),rearchText );
            }
            else
            {
                GetData();
                return;
            }

            // db.openDataBase();
//searchList
            MicroOrm uOrm = new MicroOrm();
            tempProduct = uOrm.listFromCursor(cursor2, Person.class);
            if(tempProduct!=null&&tempProduct.size()>0) {
                // noticeAdapter.setNotice(someObjects2);
//                for (int i = 0; i < tempProduct.size(); i++) {
//                    Log.e("notice title", tempProduct.get(i).id + " " + tempProduct.get(i).name + "  " + personArrayList.get(i).mobile);
//                }
                Log.e("total size 2", "" + personArrayList.size());

                totalvoter.setText("Toatal Voters: "+ tempProduct.size());
                voterAdapter.setPerson(tempProduct);

            }
            else {
                totalvoter.setText("Toatal Voters: "+ tempProduct.size());
                voterAdapter.setPerson(tempProduct);
            }


        } catch (Exception sqle) {

            throw sqle;

        }
    }
    private List<Person> GetDataBySearchValue(String search)
    {
        List<Person> tempProduct = new ArrayList<>();
        try {

            Cursor cursor2=null;


            // db.openDataBase();
            Log.e("selectedCat",""+selectedCat);
            if(selectedCat==0) {
                if(listPosition==(menuNames.size()-1))
                {

                }else if(listPosition==0)
                {
                    cursor2 = db.getAllDateItemsByMultiValue(DBTablesInfo.TABALE_NAME, search.trim());
                }
                else
                {
                    cursor2 = db.getAllDateItemsByMultiValue(DBTablesInfo.TABALE_NAME, search.trim());
                }

            }
            else
            {
                //if()

                cursor2 = db.getAllDateItemsByCat(DBTablesInfo.TABALE_NAME,getCollumnName(selectedCat), search.trim());
            }


            MicroOrm uOrm = new MicroOrm();
            tempProduct = uOrm.listFromCursor(cursor2, Person.class);
            if(tempProduct!=null&&tempProduct.size()>0) {
                // noticeAdapter.setNotice(someObjects2);
                // for (int i = 0; i < personArrayList.size(); i++)
                {
                    //Log.e("notice title", personArrayList.get(i).id + " " + personArrayList.get(i).name + "  " + personArrayList.get(i).mobile);
                }
                Log.e("total size 2", "" + tempProduct.size());
            }

            // voterAdapter.setPerson(personArrayList);


        } catch (Exception sqle) {

            Log.e("Exception",""+sqle.getMessage());

            throw sqle;

        }
        return tempProduct;
    }
    private void GetDataByAddress(int position)
    {
        List<Person> tempProduct = new ArrayList<>();
        try {
            Cursor cursor2=null;
            String categoryName = menuNames.get(position);
            if((menuNames.size()-1)==position)
            {

                cursor2 =    db.getAllDateItemsByAddressNot(DBTablesInfo.TABALE_NAME, DBTablesInfo.ADDRESS,menuNames);
            }
            else if (position==0){
                cursor2 =     db.getAllResItems(DBTablesInfo.TABALE_NAME);
            }
            else
            {
                cursor2 =     db.getAllDateItemsByAddress(DBTablesInfo.TABALE_NAME, DBTablesInfo.ADDRESS,categoryName);
            }
            // db.openDataBase();

            MicroOrm uOrm = new MicroOrm();
            tempProduct = uOrm.listFromCursor(cursor2, Person.class);
            if(tempProduct!=null&&tempProduct.size()>0) {
                // noticeAdapter.setNotice(someObjects2);
//                for (int i = 0; i < tempProduct.size(); i++) {
//                    Log.e("notice title", tempProduct.get(i).id + " " + tempProduct.get(i).name + "  " + personArrayList.get(i).mobile);
//                }
                Log.e("total size 2", "" + personArrayList.size());

                totalvoter.setText("Toatal Voters: "+ tempProduct.size());
                voterAdapter.setPerson(tempProduct);

            }
            else {
                totalvoter.setText("Toatal Voters: "+ tempProduct.size());
                voterAdapter.setPerson(tempProduct);
            }


        } catch (Exception sqle) {

            throw sqle;

        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (AppData.getData(AppData.copy, getApplicationContext()).equals("1")) {
            GetData();
            StartObservingSearchBar(searchList);
        }
        else
        {
            CopyDB();
            StartObservingSearchBar(searchList);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void CopyDB() {
        boolean flag=false;

        try {

            db.createDataBase();
            flag=true;


        } catch (Exception ioe) {
            flag=false;
            throw new Error("Unable to create database");
        }
        finally {
            if(flag)
            {
                Log.e("flaf",""+flag);
            }
        }
        try {
            db.openDataBase();
            flag=true;
        }
        catch (Exception ee)
        {
            flag=false;
            Log.e("Exception",""+ee.getMessage());
        }
        finally {
            Log.e("flag",""+flag);
            if(flag)
            {
                AppData.saveData(AppData.copy, "1",getApplicationContext());
                Log.e("flaf",""+flag);
                GetData();
            }
        }


    }


    public   void AddVoter()
    {
        Log.e("flaf","call");
        Intent intent=new Intent(this, EditActivity.class);
        intent.putExtra(AppData.check,false);
        startActivity(intent);
        //overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    public void userInfo(Person list, int value) {
        Log.e("flaf","call");
        Intent intent=new Intent(this, EditActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(AppData.Person, list);

        intent.putExtras(b);
        intent.putExtra(AppData.check,true);
        startActivity(intent);
    }

    @Override
    public void openPopUp(Person list) {
        //
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to Delete the Voter");
        alertDialogBuilder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
//                        if(NetworkConnection.getConnectivitychecking(getActivity())) {
                        removeRoute(list);
//                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void removeRoute(Person person) {

        try {
            int delete = db.deleteItems(DBTablesInfo.TABALE_NAME, DBTablesInfo.ID_KEY, person.id);
            if(delete>0)
            {
                GetData();
                ToastyMsg.Success(getApplicationContext(),"Successfully Deleted");
            }
            else
            {
                ToastyMsg.Error(getApplicationContext(),"Fail");
            }
        }
        catch (Exception e)
        {

        }

    }
}
