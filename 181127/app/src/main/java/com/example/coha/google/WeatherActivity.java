package com.example.coha.google;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class WeatherActivity extends AppCompatActivity {

    public void onBackClicked(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    NetworkImageView symbolView;
    TextView temperatureView;
    TextView upView;
    TextView downView;
    RecyclerView recyclerView;

    MyAdapter adapter;
    ArrayList<ItemData> list;

    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        temperatureView=(TextView)findViewById(R.id.weather_temperature);
        upView=(TextView)findViewById(R.id.weather_up_text);
        downView=(TextView)findViewById(R.id.weather_down_text);
        symbolView=(NetworkImageView)findViewById(R.id.weather_symbol);
        recyclerView=(RecyclerView)findViewById(R.id.weather_recycler);

        list=new ArrayList<>();
        adapter=new MyAdapter(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new MyItemDecoration());
        recyclerView.setAdapter(adapter);

        queue=Volley.newRequestQueue(this);

        StringRequest currentRequest = new StringRequest(Request.Method.POST, "http://api.openweathermap.org/data/2.5/weather?q=seoul&mode=xml&units=metric&appid=060476b15c1e70f440730e44068a5856", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseXMLCurrent(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
    });

        StringRequest forecastRequest = new StringRequest(Request.Method.POST, "http://api.openweathermap.org/data/2.5/forecast/daily?q=seoul&mode=xml&units=metric&appid=db441b5cd16577f716641f657592c172", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseXMLForecast(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

    });

        queue.add(currentRequest);
        queue.add(forecastRequest);
    }

    private class ItemData {
        public String max;
        public String min;
        public String day;
        public Bitmap image;
    }

    private  class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView dayView;
        public TextView maxView;
        public TextView minView;
        public ImageView imageView;

        public MyViewHolder(View itemView){
            super(itemView);
            dayView=(TextView)itemView.findViewById(R.id.weather_item_day);
            maxView=(TextView)itemView.findViewById(R.id.weather_item_max);
            minView=(TextView)itemView.findViewById(R.id.weather_item_min);
            imageView=(ImageView)itemView.findViewById(R.id.weather_item_image);
        }
    }
    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private final List<ItemData>list;
        public MyAdapter(List<ItemData>list){
            this.list=list;
        }

        public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item,parent,false);
            return new MyViewHolder(view);
        }

        public void onBindViewHolder(MyViewHolder holder, int position) {
            ItemData vo = list.get(position);
            holder.dayView.setText(vo.day);
            holder.maxView.setText(vo.max);
            holder.minView.setText(vo.min);
            holder.imageView.setImageBitmap(vo.image);
        }

        public int getItemCount() {
            return list.size();
        }
    }

    class MyItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(10,10,10,10);
            view.setBackgroundColor(0x88929090);
        }
    }

    private  void parseXMLCurrent(String response) {
        try{
            DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(response)));
            doc.getDocumentElement().normalize();

            Element tempElement=(Element)(doc.getElementsByTagName("temperature").item(0));
            String temperature=tempElement.getAttribute("value");
            String min = tempElement.getAttribute("min");
            String max = tempElement.getAttribute("max");

            temperatureView.setText(temperature);
            upView.setText(max);
            downView.setText(min);

            Element weatherElement=(Element)(doc.getElementsByTagName("weather").item(0));
            String symbol=weatherElement.getAttribute("icon");
            ImageLoader imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
                @Override
                public Bitmap getBitmap(String url) {
                    return null;
                }

                @Override
                public void putBitmap(String url, Bitmap bitmap) {

                }
            });
            symbolView.setImageUrl("http://openweathermpa.org/img?/"+symbol+".png",imageLoader);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parseXMLForecast(String response){
        try{
            DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(response)));
            doc.getDocumentElement().normalize();

            NodeList nodeList=doc.getElementsByTagName("time");
            for(int i=0; i<nodeList.getLength(); i++){
                final ItemData vo = new ItemData();

                Element timeNode=(Element)nodeList.item(i);
                vo.day=timeNode.getAttribute("day").substring(5);

                Element temperatureNode = (Element)timeNode.getElementsByTagName("temperature").item(0);
                vo.max=temperatureNode.getAttribute("max");
                vo.min=temperatureNode.getAttribute("min");

                Element symbolNode=(Element)timeNode.getElementsByTagName("symbol").item(0);
                String symbol=symbolNode.getAttribute("var");

                String url="http://openweathermap.org/img/w/"+symbol+".png";
                ImageRequest imageRequest=new ImageRequest(url, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        vo.image=response;
                        adapter.notifyDataSetChanged();
                    }
                },0,0,ImageView.ScaleType.CENTER_CROP,null,new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                queue.add(imageRequest);
                list.add(vo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup contanier, Bundle savedlnstanceState) {
        View v = inflater.inflate(R.layout.activity_home, contanier, false);
        return v;
    }
}
