package com.example.kiemtragk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kiemtragk.Model.ItemModel;
import com.example.kiemtragk.Model.UserModel;
import com.example.kiemtragk.SQLite.SQLite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SubjectActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SQLite sqLite;
    List<UserModel> userModelList;
    List<ItemModel> itemModelList;
    static ItemModel model;
    static boolean check = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.layout_subject );
        onInit();
        itemModelList = new ArrayList<>();
        sqLite = new SQLite( SubjectActivity.this );
        userModelList = sqLite.selectLogin();
        recyclerView.setHasFixedSize( true );
        recyclerView.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );
        check = true;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(check == true){

                    Map<String, String> mMap = new HashMap<>();
                    mMap.put( "id", String.valueOf( userModelList.get( 0 ).getUser_id() ) );
                    new ItemAsyncTask( new IViewItem() {
                        @Override
                        public void onRequestSuccess(Bitmap bitmap) {

                        }

                        @Override
                        public void onGetDataSuccess(JSONArray jsonArray) {
                            itemModelList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject( i );
                                    ItemModel model = new ItemModel();
                                    model.setID( jsonObject.getInt( "id" ) );
                                    model.setProduct_Name( jsonObject.getString( "product_name" ) );
                                    model.setPrice( jsonObject.getInt( "price" ) );
                                    model.setDescription( jsonObject.getString( "description" ) );
                                    model.setProducer( jsonObject.getString( "producer" ) );
                                    itemModelList.add( model );
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                MyViewAdapter adapter = new MyViewAdapter( SubjectActivity.this, R.layout.item_list, itemModelList );
                                recyclerView.setAdapter( adapter );
                            }
                        }

                        @Override
                        public void onSuccess(String message) {

                        }

                        @Override
                        public void onFail(String message) {

                        }
                    }, mMap ).execute( "http://www.vidophp.tk/api/account/getdata" );
                    check = false;
                }

            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }

    private void onInit() {
        recyclerView = findViewById( R.id.recyclerView );
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                check = true;
                Toast.makeText( SubjectActivity.this, "Insert", Toast.LENGTH_SHORT ).show();
                AddNewInsert();
                return true;
            case 1:
                Toast.makeText( SubjectActivity.this, "Update", Toast.LENGTH_SHORT ).show();
                AddNewUpdate();
                return true;
            case 2:
                check = true;
                Toast.makeText( SubjectActivity.this, "Delete", Toast.LENGTH_SHORT ).show();
                Delete();
                return true;
            default:
                return super.onContextItemSelected( item );
        }

    }

    public void AddNewInsert() {

        userModelList = new ArrayList<>();
        userModelList = sqLite.selectLogin();
        AlertDialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder( SubjectActivity.this );
        View view = getLayoutInflater().inflate( R.layout.dialog_insert, null );

        final EditText edtUserID = view.findViewById( R.id.edtUserID );
        final EditText edtName = view.findViewById( R.id.edtName );
        final EditText edtNumber = view.findViewById( R.id.edtNumber );
        final EditText edtCode = view.findViewById( R.id.edtCode );
        final EditText edtDescription = view.findViewById( R.id.edtDescription );


        edtUserID.setText( String.valueOf( userModelList.get( 0 ).getUser_id() ) );

        builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                String mName = edtName.getText().toString();
                String mNumber = edtNumber.getText().toString();
                String mCode = edtCode.getText().toString();
                String mDescription = edtDescription.getText().toString();
                if(mName.equals( "" ) || mNumber.equals( "" ) || mCode.equals( "" ) || mDescription.equals( "" )){
                    Toast.makeText( SubjectActivity.this, "Please enter information" ,Toast.LENGTH_SHORT).show();
                }else{
                    Map<String, String> mMap = new HashMap<>();
                    mMap.put( "user_id", String.valueOf( userModelList.get( 0 ).getUser_id() ) );
                    mMap.put( "name", mName );
                    mMap.put( "number", mNumber );
                    mMap.put( "code", mCode );
                    mMap.put( "description", mDescription );
                    new InsertAsysnTask( new IViewItem() {
                        @Override
                        public void onRequestSuccess(Bitmap bitmap) {

                        }

                        @Override
                        public void onGetDataSuccess(JSONArray jsonArray) {
                            if (jsonArray != null) {
                                Toast.makeText( SubjectActivity.this, "Insert success", Toast.LENGTH_SHORT ).show();
                                dialogInterface.dismiss();
                            }
                        }

                        @Override
                        public void onSuccess(String message) {
                            Toast.makeText(SubjectActivity.this,message,Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFail(String message) {
                            Toast.makeText(SubjectActivity.this,message,Toast.LENGTH_SHORT).show();
                        }
                    }, mMap ).execute( "http://www.vidophp.tk/api/account/dataaction?context=insert" );
                    check = true;
                }
            }
        } );

        builder.setNeutralButton( "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        } );

        builder.setView( view );
        dialog = builder.create();
        dialog.show();
    }

    public void AddNewUpdate() {
        userModelList = new ArrayList<>();
        userModelList = sqLite.selectLogin();
        AlertDialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder( SubjectActivity.this );
        View view = getLayoutInflater().inflate( R.layout.dialog_update, null );

        final EditText edtUserID = view.findViewById( R.id.edtUserID );
        final EditText edtName = view.findViewById( R.id.edtName );
        final EditText edtNumber = view.findViewById( R.id.edtNumber );
        final EditText edtCode = view.findViewById( R.id.edtCode );
        final EditText edtDescription = view.findViewById( R.id.edtDescription );
        final EditText edtID = view.findViewById( R.id.edtID );


        edtUserID.setText( String.valueOf( userModelList.get( 0 ).getUser_id() ) );
        edtName.setText( model.getProduct_Name() );
        edtNumber.setText( String.valueOf( model.getPrice() ));
        edtDescription.setText( model.getDescription() );
        edtCode.setText( model.getProducer() );
        edtID.setText( String.valueOf(model.getID() ));



        builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                String mName = edtName.getText().toString();
                String mNumber = edtNumber.getText().toString();
                String mCode = edtCode.getText().toString();
                String mDescription = edtDescription.getText().toString();
                String mID = edtID.getText().toString();
                if(mName.equals( "" ) || mNumber.equals( "" ) || mCode.equals( "" ) || mDescription.equals( "" ) || mID.equals( "" )){
                    Toast.makeText( SubjectActivity.this, "Please enter information" ,Toast.LENGTH_SHORT).show();
                }else{
                    Map<String, String> mMap = new HashMap<>();
                    mMap.put( "user_id", String.valueOf( userModelList.get( 0 ).getUser_id() ) );
                    mMap.put( "name", mName );
                    mMap.put( "number", mNumber );
                    mMap.put( "code", mCode );
                    mMap.put( "description", mDescription );
                    mMap.put( "id", mID );
                    new InsertAsysnTask( new IViewItem() {
                        @Override
                        public void onRequestSuccess(Bitmap bitmap) {

                        }

                        @Override
                        public void onGetDataSuccess(JSONArray jsonArray) {

                        }

                        @Override
                        public void onSuccess(String message) {
                            Toast.makeText(SubjectActivity.this,message,Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFail(String message) {
                            Toast.makeText(SubjectActivity.this,message,Toast.LENGTH_SHORT).show();
                        }
                    }, mMap ).execute( "http://www.vidophp.tk/api/account/dataaction?context=update" );

                    check = true;
                }
            }
        } );

        builder.setNeutralButton( "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        } );

        builder.setView( view );
        dialog = builder.create();
        dialog.show();
    }

    public void Delete(){
        userModelList = new ArrayList<>();
        userModelList = sqLite.selectLogin();
        Map<String, String> mMap = new HashMap<>();
        mMap.put( "user_id", String.valueOf( userModelList.get( 0 ).getUser_id() ) );
        mMap.put( "id", String.valueOf( model.getID() ) );
        new InsertAsysnTask( new IViewItem() {
            @Override
            public void onRequestSuccess(Bitmap bitmap) {

            }

            @Override
            public void onGetDataSuccess(JSONArray jsonArray) {

            }

            @Override
            public void onSuccess(String message) {
                Toast.makeText(SubjectActivity.this,message,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(String message) {
                Toast.makeText(SubjectActivity.this,message,Toast.LENGTH_SHORT).show();
            }
        }, mMap ).execute( "http://www.vidophp.tk/api/account/dataaction?context=delete" );

        check = true;
    }
}
