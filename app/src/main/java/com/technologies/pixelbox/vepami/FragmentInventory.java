package com.technologies.pixelbox.vepami;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FragmentInventory extends android.support.v4.app.Fragment {

    LoadInventoryDataListener event;

    ArrayList<Product> inventory;
    AdapterInventoryList adapterItemList;

    public FragmentInventory() {
        super();
    }

    public void setEventListener(LoadInventoryDataListener event) {
        this.event = event;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setInitialData();
    }


    public void setInitialData() {
        inventory = ((MainActivity)getActivity()).getInventory();
        CreateData("SelectInventarioWithImages");
    }

    protected void CreateData(String f)
    {
        StringBuilder mainURL = new StringBuilder(getString(R.string.main_url));//"http://www.pixelbox-tech.com/vepami/main.php?");
        List<String> columns = new ArrayList<String>();
        ExecuteTaskParameters exParams = new ExecuteTaskParameters();
        switch (f)
        {
            case "Test":
                mainURL.append("f=Test");
                exParams.setUrl(mainURL.toString());
                exParams.setColumns(columns);
                //new DownloadsImageTask(viewTest).execute("http://www.pixelbox-tech.com/vepami/images/Test_01.png");
                break;
            case "SelectInventarioWithImages":
                mainURL.append("f=SelectInventarioWithImages");
                columns.add("ID: ");
                columns.add("Producto: ");
                columns.add("Precio: ");
                columns.add("Cantidad: ");
                columns.add("ID_Imagen: ");
                columns.add("Thumbnail: ");
                columns.add("Localizacion: ");
                exParams.setUrl(mainURL.toString());
                exParams.setColumns(columns);
                new GetInventaryTask().execute(exParams);
                break;
            default:
                break;
        }
    }

    public Product getInventoryItems(int index) {
        return inventory.get(index);
    }

    class GetInventaryTask extends AsyncTask<ExecuteTaskParameters/*Params*/, Void/*Progress*/, String/*Result*/> {
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
                    String item;
                    int row = 0;

                    while ((item = r.readLine()) != null){
                        if (!item.isEmpty()) {
                            if (!this.columns.isEmpty()) {
                                for (String s : this.columns) {
                                    if (item.contains(s)) {
                                        dataRow.add(item.replaceFirst(s, ""));
                                        break;
                                    }
                                }
                                if (dataRow.size() ==  this.columns.size() * (row + 1)) {
                                    int pos = row * this.columns.size();

                                    int id = ParserNumbers.getInt(dataRow.get(4 + pos));
                                    int thumbnail = ParserNumbers.getInt(dataRow.get(5 + pos));
                                    ProductImage pi = new ProductImage(id, thumbnail, dataRow.get(6 + pos));

                                    inventory.add(new Product(
                                            dataRow.get(0 + pos),
                                            dataRow.get(1 + pos),
                                            Integer.valueOf(dataRow.get(2 + pos)),
                                            Float.valueOf(dataRow.get(3 + pos)),
                                            pi));
                                    row++;
                                }
                            }
                            else
                            {
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

        @Override
        protected void onPostExecute(String result) {
            adapterItemList = new AdapterInventoryList(getActivity(), inventory);
            setAdapter(adapterItemList);
        }
    }


    static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap image) {
            imageView.setImageBitmap(image);
        }
    }

    private void setAdapter(final AdapterInventoryList adapter ){
        event.callback(adapter);
    }

}
