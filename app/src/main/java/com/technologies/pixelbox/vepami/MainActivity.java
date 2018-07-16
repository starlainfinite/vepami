package com.technologies.pixelbox.vepami;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.support.v7.widget.Toolbar;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Events events;

    TabLayout tabs;
    ListView listViewInventory;
    ViewPager viewPager;
    AdapterFragments adapterFragments;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ArrayList<Product> inventory;

    Toolbar toolbar;

    boolean rememberme;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set splash screen
        setContentView(R.layout.activity_main);

        // set time limit for splash screen before change to main view
        //new Timer().schedule(setMainViewTask, 50);

        user = new User();

        // CREATE TABS
        TabItem tab;
        tabs = findViewById(R.id.tabLayoutMain);
        tabs.addTab(tabs.newTab(),0, true);
        tabs.addTab(tabs.newTab(), false);
        tabs.addTab(tabs.newTab(), false);
        tabs.addOnTabSelectedListener(onTabSelectedListener);

        //  CREATE CUSTOM EVENTS TO UPDATE VIEWS
        events = new Events();
        events.setOnLoadInventoryData(onAdapterInventoryReady);
        events.setOnClickInventoryItem(onClickInventoryItem);

        // CREATE VIEWS FOR THE FRAGMENTS
        adapterFragments = new AdapterFragments (getSupportFragmentManager(), tabs.getTabCount(), events.eventListener);
        viewPager = findViewById(R.id.pagerView);
        viewPager.setAdapter(adapterFragments);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        // SET TAB NAMES
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(0).setText("Inventario");
        tabs.getTabAt(1).setText("Carrito");
        tabs.getTabAt(2).setText("Compras");


        inventory = new ArrayList<>();

        // SET TOOLBAR AND ADD A HAMBURGUER BUTTON
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        //ADD DRAWER LAYOUT TO DISPLAY LEFT MENU
        drawerLayout = findViewById(R.id.drawer_layout);
        //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.layout.menu_unlogged);
        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);




        // GET SAVED DATA TO LOAD AUTOMATIC USER DATA
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        // GET REMEMBER ME VAR
        rememberme = sharedPref.getBoolean("re", false);
        if (rememberme) {
            // GET ID
            user.id = sharedPref.getInt("id", 0);
            // GET USER NAME
            user.alias = sharedPref.getString("al", ""); // ALIAS
            // GET ID LOGIN TYPE
            user.id_tipoLogin = sharedPref.getInt("itl", 0); // 1 EMAIL, 2 FACEBOOK, 3 TWITTER, 4 GOOGLE
            // GET LOGIN TYPE
            user.tipoLogin = sharedPref.getString("tl", "");
            // GET ID USER TYPE
            user.id_tipoUsuario = sharedPref.getInt("itu", 0); // 1 ADMIN, 2 CLIENT
            // USER TYPE
            user.tipoUsuario = sharedPref.getString("tu", "");
            // GET ID IMAGE
            user.id_imagen = sharedPref.getInt("iim", 0);
            // GET IMAGE LOCATION
            user.localizacion = sharedPref.getString("lo", "");
            // GET ACCOUNT VERIFICATION
            user.verificada = sharedPref.getInt("ve", 0);
            // GET ID STATUS
            user.id_estado = sharedPref.getInt("ist", 0);
            // GET STATUS
            user.estado = sharedPref.getString("st", "");

            // LOAD ...
        }
    }



    // CREATE AN OVERRIDE ACTIVITY RESULT TO RETRIEVE DATA FROM THE ACTIVITY VIEW PREVIOUSLY CREATED
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Retrieve data in the intent
        if (resultCode == 1) {
            user.id = data.getIntExtra("id", 0);
            user.alias = data.getStringExtra("alias");
            user.id_tipoLogin = data.getIntExtra("id_tipoLogin", 0);
            user.tipoLogin = data.getStringExtra("tipo_login");
            user.id_tipoUsuario = data.getIntExtra("id_tipoUsuario", 0);
            user.tipoUsuario = data.getStringExtra("tipo_usuario");
            user.id_imagen = data.getIntExtra("id_imagen", 0);
            user.localizacion = data.getStringExtra("localizacion");
            user.verificada = data.getIntExtra("verificada", 0);
            user.id_estado = data.getIntExtra("id_estado", 0);
            user.estado = data.getStringExtra("estado");

            if (user.tipoUsuario.equals("Cliente")) {
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.layout.menu_client);
            }
        }
    }


    // CREATE AN OVERRIDE TO GET HAMBURGER BUTTON EVENTS
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.closeDrawers();
                }
                else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // GET MENU ITEM CLICK EVENTS
    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            // GO TO LOGIN ACTIVITY
            if (item.getTitle().equals("Login")) {
                // SEND DATA TO THAT ACTIVITY
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("id", user.id);
                intent.putExtra("alias", user.alias);
                intent.putExtra("id_tipoLogin", user.id_tipoLogin);
                intent.putExtra("tipo_login", user.tipoLogin);
                intent.putExtra("id_tipoUsuario", user.id_tipoUsuario);
                intent.putExtra("tipo_usuario", user.tipoUsuario);
                intent.putExtra("id_imagen", user.id_imagen);
                intent.putExtra("localizacion", user.localizacion);
                intent.putExtra("verificada", user.verificada);
                intent.putExtra("id_estado", user.id_estado);
                intent.putExtra("estado", user.estado);
                startActivityForResult(intent, 0);
            }
            else if (item.getTitle().equals("Direcciones")) {
                //Intent intent = new Intent(MainActivity.this, );
                //startActivity();
            }
            return false;
        }
    };


    // ON CLICK EVENTS FOR TABS
    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener (){
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private ListView.OnItemClickListener onItemClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final int pos = position;
            View childView = ((ViewGroup)view).getChildAt(0);
            CreateDetailItemActivity(pos, (ImageView)childView);
        }
    };

    public ArrayList<Product> getInventory () {
        return inventory;
    }

    public void CreateDetailItemActivity (final int position, final ImageView view) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Intent myIntent = new Intent(MainActivity.this, DetailItemActivity.class);
                myIntent.putExtra("id", inventory.get(position).getId()); //Optional parameters
                myIntent.putExtra("cantidad", inventory.get(position).getCantidad());
                myIntent.putExtra("precio", inventory.get(position).getPrecio());
                myIntent.putExtra("producto", inventory.get(position).getProducto());
                myIntent.putExtra("id_imagen", inventory.get(position).getProductImage(0).getId());
                myIntent.putExtra("thumbnail", inventory.get(position).getProductImage(0).getIsThumbnail());
                myIntent.putExtra("localizacion", inventory.get(position).getProductImage(0).getLocalizacion());

                BitmapDrawable bitmapDrawable = (BitmapDrawable)view.getDrawable();
                Bitmap bitmap;
                bitmap = bitmapDrawable.getBitmap();
                if (bitmap != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    myIntent.putExtra("bitmap", byteArray);
                }

                MainActivity.this.startActivity(myIntent);
            }
        });
    }


    // LOAD INVENTORY
    private LoadInventoryDataListener onAdapterInventoryReady = new LoadInventoryDataListener() {
        @Override
        public void callback(AdapterInventoryList inventoryList) {
            TableLayout tableLayout = findViewById(R.id.tableLayoutInventory);

            for (int i = 0; i <5; i++) {
                HorizontalScrollView horizontalScrollView = new HorizontalScrollView(MainActivity.this);
                TableRow row = new TableRow(MainActivity.this);
                for (int j = 0; j < 10 ; j++) {
                    horizontalScrollView.setLayoutParams(
                           new android.widget.HorizontalScrollView.LayoutParams(
                                    HorizontalScrollView.LayoutParams.WRAP_CONTENT, HorizontalScrollView.LayoutParams.WRAP_CONTENT));
                    horizontalScrollView.setHorizontalScrollBarEnabled(false);
                    InventoryItemView item = new InventoryItemView(MainActivity.this, row, events.itemEventListener);
                    row.addView(item);

                }
                horizontalScrollView.addView(row);
                tableLayout.addView(horizontalScrollView);
            }

            //listViewInventory = findViewById(R.id.listViewItems);
            if (listViewInventory != null) {
                listViewInventory.setOnItemClickListener(onItemClickListener);
                listViewInventory.setAdapter(inventoryList);
            }
        }
    };



    private  ItemEventListener onClickInventoryItem = new ItemEventListener() {
        @Override
        public void onClickInventoryItem(View view) {

        }
    };

    TimerTask setMainViewTask = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //setContentView(R.layout.activity_main);

                }
            });

        }
    };




    class GetUserTask extends AsyncTask<ExecuteTaskParameters/*Params*/, Void/*Progress*/, String/*Result*/> {
        InputStream iStream = null;
        String response;
        String items;
        List<String> dataRow = new ArrayList<String>();
        List<String> columns = new ArrayList<String>();

        protected String doInBackground(ExecuteTaskParameters... params) {
            try {
                HttpURLConnection urlConnection = null;
                int length = 500;
                URL url;
                url = new URL(params[0].getUrl());
                this.columns = params[0].getColumns();
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    int res = urlConnection.getResponseCode();
                    response = urlConnection.getResponseMessage();
                    if (res != 200) {
                        return response;
                    }
                    iStream = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader r = new BufferedReader(new InputStreamReader(iStream));
                    int row = 0;
                    String item;
                    while ((item = r.readLine()) != null) {
                        if (item.equals("0 results")) {
                            return item;
                        }
                        if (!item.isEmpty()) {
                            if (!this.columns.isEmpty()) {
                                for (String s : this.columns) {
                                    if (item.contains(s)) {
                                        dataRow.add(item.replaceFirst(s, ""));
                                        break;
                                    }
                                }
                                if (dataRow.size() == this.columns.size() * (row + 1)) {
                                    int pos = row * this.columns.size();
                                    user.id = ParserNumbers.getInt(dataRow.get(0 + pos));
                                    user.alias = dataRow.get(1 + pos);
                                    user.id_tipoLogin = ParserNumbers.getInt(dataRow.get(2 + pos));
                                    user.tipoLogin = dataRow.get(3 + pos);
                                    user.id_tipoUsuario = ParserNumbers.getInt(dataRow.get(4 + pos));
                                    user.tipoUsuario = dataRow.get(5 + pos);
                                    user.id_imagen = ParserNumbers.getInt(dataRow.get(6 + pos));
                                    user.localizacion = dataRow.get(7 + pos);
                                    user.verificada = ParserNumbers.getInt(dataRow.get(8 + pos));
                                    user.id_estado = ParserNumbers.getInt(dataRow.get(9 + pos));
                                    user.estado = dataRow.get(10 + pos);
                                    row++;
                                }
                            } else {
                                return item;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (iStream != null) {
                        try {
                            iStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                return e.getMessage();
            }
            return response;
        }
    }
}



