package com.cherryclaw.portioncat;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The MainActivity contains methods from the Navigation Drawer template
 * the database is accessed here, a cursor adapter is used to populate the
 * ListView that the user can click for details.
 *
 * @author Jocelynne Gonzales
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate Main Activity");



        /* CatOpenHelper connects to SQL lite */
        final CatOpenHelper handler = new CatOpenHelper(this);
        //Access writable database
        SQLiteDatabase db = handler.getWritableDatabase();
        // Query for items and get cursor
        Cursor todoCursor = db.rawQuery("SELECT  * FROM cat", null);

        // Find ListView to populate
        ListView lvItems = (ListView) findViewById(R.id.catListView);
        // Setup cursor adapter using cursor from last step
        ToDoCursorAdapter todoAdapter = new ToDoCursorAdapter(this, todoCursor);
        // Attach adapter to listview
        lvItems.setAdapter(todoAdapter);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Floating action button */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCat.class);
                startActivity(intent);

            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        /*When an item is clicked, a detailed view launches */
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView catName = (TextView) view.findViewById(R.id.cat_name);

                //When cat is found, create a new cat object then start DetailsActivity
                Cat foundCat = handler.findCat(catName.getText().toString());
                String myName = foundCat.getName();
                Log.d("MainActivity myName: ", catName.getText().toString());
                Intent myIntent = new Intent(MainActivity.this, DetailsActivity.class).putExtra("catName",myName);
               startActivity(myIntent);
            }
        });




    }



    /**
     * Makes sure the listview is updated on restart of the app
     */
    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        CatOpenHelper handler = new CatOpenHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor todoCursor = db.rawQuery("SELECT  * FROM cat", null);

        ListView lvItems = (ListView) findViewById(R.id.catListView);
        ToDoCursorAdapter todoAdapter = new ToDoCursorAdapter(this, todoCursor);
        lvItems.setAdapter(todoAdapter);




    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Navigation bar where users can add a cat and find vets in their local vicinity using
     * Google Maps API
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_cat) {
            Intent intent = new Intent(MainActivity.this, AddCat.class);
            startActivity(intent);
        } else if (id == R.id.nav_find_vet) {

            // Use Google Maps to search for Vets near the user, taken from Google Maps API
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=veterinarian");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);

        } else if (id == R.id.nav_manage) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
