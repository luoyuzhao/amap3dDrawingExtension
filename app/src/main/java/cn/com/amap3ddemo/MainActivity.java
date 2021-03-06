package cn.com.amap3ddemo;

import android.animation.FloatArrayEvaluator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    MapView mMapView = null;
    AMap map=null;
    MapRender render;
    Button btn;
    Button btn1;
    Button btn2;
    final LatLng center = new LatLng(39.90403, 116.407525);// 北京市政府经纬度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = findViewById(R.id.map);
        btn=findViewById(R.id.btn);
        btn1=findViewById(R.id.btn1);
        btn2=findViewById(R.id.btn2);
        mMapView.onCreate(savedInstanceState);
        if (map == null) {
            map = mMapView.getMap();
        }
        map.setMapType(AMap.MAP_TYPE_NORMAL);//普通地图 卫星地图MAP_TYPE_SATELLITE
        map.moveCamera(CameraUpdateFactory.newLatLng(center));
        map.moveCamera(CameraUpdateFactory.zoomTo(18));
        map.moveCamera(CameraUpdateFactory.changeTilt(45));//倾斜45度角
        //map.getUiSettings().setZoomControlsEnabled(false);
        render=new MapRender(map);
        map.setCustomRenderer(render);
        map.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        map.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                render.setCenter(center);//设置坐标轴圆心位置

                List<Point3D> points=new ArrayList<>();
                points.add(new Point3D(-374.0f,-899.0f,250f));
                points.add(new Point3D(-326.0f,811.0f,250f));
                points.add(new Point3D(869.0f,818.0f,250f));
                points.add(new Point3D(901.0f,-960.0f,250f));
                render.AddPolygon(points, Color.GREEN,3);

                List<Point3D> points1=new ArrayList<>();
                points1.add(new Point3D(-374.0f,-899.0f,350f));
                points1.add(new Point3D(-326.0f,811.0f,350f));
                points1.add(new Point3D(869.0f,818.0f,350f));
                points1.add(new Point3D(901.0f,-960.0f,350f));
                render.AddPolygon(points1, Color.RED,3);

                List<Point3D> points2=new ArrayList<>();
                points2.add(new Point3D(-374.0f,-899.0f,450f));
                points2.add(new Point3D(-326.0f,811.0f,450f));
                points2.add(new Point3D(869.0f,818.0f,450f));
                points2.add(new Point3D(901.0f,-960.0f,450f));
                render.AddPolygon(points2, Color.BLUE,3);

                List<Point3D> points3=new ArrayList<>();
                points3.add(new Point3D(-374.0f,-899.0f,550f));
                points3.add(new Point3D(-326.0f,811.0f,550f));
                points3.add(new Point3D(869.0f,818.0f,550f));
                points3.add(new Point3D(901.0f,-960.0f,550f));
                render.AddPolygon(points3, Color.YELLOW,3);

                List<Point3D> points4=new ArrayList<>();
                points4.add(new Point3D(-374.0f,-899.0f,600f));
                points4.add(new Point3D(-326.0f,811.0f,600f));
                points4.add(new Point3D(869.0f,818.0f,800f));
                points4.add(new Point3D(901.0f,-960.0f,800f));
                render.AddPolygon(points4, Color.GRAY,3);

                render.AddCircle(new Point3D(-374.0f,-899.0f,500),200,Color.RED,5);

                render.AddCircle(new Point3D(869.0f,818.0f,450),200,Color.YELLOW,5);

                render.AddCircle(new Point3D(901.0f,-960.0f,350),200,Color.BLUE,5);

                render.AddCircle(new Point3D(-326.0f,811.0f,250),200,Color.GREEN,5);

                List<Point3D> points5=new ArrayList<>();
                points5.add(new Point3D(-374.0f,-899.0f,1000f));
                points5.add(new Point3D(-326.0f,811.0f,1000f));
                points5.add(new Point3D(869.0f,-1200.0f,800f));
                render.AddLine(points5,Color.GREEN,10);

                List<Point3D> points6=new ArrayList<>();
                points6.add(new Point3D(-374.0f,-899.0f,1100f));
                points6.add(new Point3D(-326.0f,811.0f,1100f));
                points6.add(new Point3D(869.0f,-1200.0f,900f));
                render.AddLine(points6,Color.RED,10);

                interval();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heght=heght+100;
                List<Point3D> points6=new ArrayList<>();
                points6.add(new Point3D(-374.0f,-899.0f,heght));
                points6.add(new Point3D(-326.0f,811.0f,heght));
                points6.add(new Point3D(869.0f,818.0f,heght));
                points6.add(new Point3D(1300.0f,-960.0f,heght));
                points6.add(new Point3D(980.0f,-1260.0f,heght));
                render.AddPolygon(points6,Color.RED,5);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heght=heght+100;
                render.AddCircle(new Point3D(-374.0f,-899.0f,heght),200,Color.RED,5);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heght=heght+100;
                List<Point3D> points6=new ArrayList<>();
                points6.add(new Point3D(-374.0f,-899.0f,heght));
                points6.add(new Point3D(-326.0f,811.0f,heght));
                points6.add(new Point3D(869.0f,-1200.0f,heght));
                render.AddLine(points6,Color.RED,3);
            }
        });

    }
    float angle=0;
    float heght=100;
    double latitude=center.latitude;
    public void interval() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                if(latitude<center.latitude+0.001){
                    if(angle>360){
                        latitude=latitude+0.00001;
                    }
                    else{
                        angle=angle+5;
                    }
                }
                LatLng point=new LatLng(latitude,center.longitude);
                PointF p=render.getRealOpenGLLocation(point);
                render.SetMarker( p.x,p.y,500,angle,Color.RED);
            }
        }, 0,100);// 设定指定的时间time,此处为2000毫秒
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
}
