package cn.com.amap3ddemo;

import android.graphics.PointF;
import android.opengl.Matrix;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CustomRenderer;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class MapRender implements CustomRenderer {
    public enum DataType{
        Polygon,
        Line,
        Circle
    }
    public class RenderData{
        public DataType Type;
        public FloatBuffer Buffer;
        public int color;
        public int lineWidth;
    }

    public class Marker3D{
        public float x;
        public float y;
        public float z;
        public float angle;
        public FloatBuffer Buffer;
        public int color;
    }

    private AMap aMap;
    float width, height;
    aMap3dDrawer drawerPolygon;
    aMap3dDrawer drawerCircle;
    aMap3dDrawer drawerLine;
    aMap3dDrawer drawerCone;
    List<RenderData> frames=new ArrayList<>();
    Marker3D marker3D;
    LatLng _center;
    public MapRender(AMap map,LatLng center) {
        this.aMap = map;
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center,18));
        _center=center;
    }
    float[] model = new float[16];
    float[] projection = new float[16];
    float[] view = new float[16];

    @Override
    public void onDrawFrame(GL10 gl) {
        // 摄影机坐标下物体的偏移
        PointF pointF = aMap.getProjection().toOpenGLLocation(_center);
        // 设置投影矩阵
        projection = aMap.getProjectionMatrix();
        // 初始化摄影机矩阵
        view = aMap.getViewMatrix();
        //重置矩阵
        Matrix.setIdentityM(model, 0);
        // 设置摄影机坐标下物体的偏移矩阵
        Matrix.translateM(view, 0 , pointF.x , pointF.y  , 0);
        for(int i=0;i<frames.size();i++){//逐帧绘制
            RenderData frame= frames.get(i);
            if(frame.Type==DataType.Polygon){
                drawerPolygon.draw(model,view,projection,frame.Buffer,frame.color,frame.lineWidth);
            }
            if(frame.Type==DataType.Circle){
                drawerCircle.draw(model,view,projection,frame.Buffer,frame.color,frame.lineWidth);
            }
            if(frame.Type==DataType.Line){
                drawerLine.draw(model,view,projection,frame.Buffer,frame.color,frame.lineWidth);
            }
            if(marker3D!=null){
                Matrix.translateM(model, 0 ,marker3D.x,marker3D.y,marker3D.z);//平移至正确位置
                Matrix.rotateM(model,0,marker3D.angle, 0,0,1f);//旋转指定角度
                drawerCone.draw(model,view,projection,marker3D.Buffer,marker3D.color,3);
                Matrix.setIdentityM(model, 0);//重置矩阵否则会影响后续绘制
            }
        }
    }
    public void AddPolygon(List<Point3D> points,int color, int width){
        RenderData res=new RenderData();
        res.color=color;
        res.lineWidth=width;
        res.Buffer=drawerPolygon.GenPolygonFrame(points);
        res.Type=DataType.Polygon;
        frames.add(res);
    }
    public void SetMarker(float x, float y,float z,float angle, int color){
        if(marker3D==null){
            marker3D=new Marker3D();
        }
        marker3D.Buffer=drawerCone.GenConeFrame();
        marker3D.x=x;
        marker3D.y=y;
        marker3D.z=z;
        marker3D.color=color;
        marker3D.angle=angle;
    }
    public void AddCircle(Point3D center, float r, int color, int width){
        RenderData res=new RenderData();
        res.color=color;
        res.lineWidth=width;
        res.Buffer=drawerCircle.GenCircleFrame(center,r);
        res.Type=DataType.Circle;
        frames.add(res);
    }
    public void AddLine(List<PointF> points,float z,int color,int width){
        RenderData res=new RenderData();
        res.color=color;
        res.lineWidth=width;
        res.Buffer=drawerLine.GenLineFrame(points,z);
        res.Type=DataType.Line;
        frames.add(res);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        drawerPolygon=new aMap3dDrawer();//不同类型不能使用同一个drawer
        drawerCircle=new aMap3dDrawer();
        drawerLine=new aMap3dDrawer();
        drawerCone=new aMap3dDrawer();
    }

    @Override
    public void OnMapReferencechanged() {

    }
}

