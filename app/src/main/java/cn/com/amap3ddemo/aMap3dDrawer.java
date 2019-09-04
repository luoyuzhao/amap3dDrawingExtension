package cn.com.amap3ddemo;

import android.graphics.PointF;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

public class aMap3dDrawer {
    public aMap3dDrawer(){
        initShader();
    }

    private int pointCount=0;

    private float[] getOpenGLColor(int uColor){
        float fAlpha = (float)(uColor >> 24) / 0xFF;
        float fRed = (float)((uColor >> 16) & 0xFF) / 0xFF;
        float fGreen = (float)((uColor >> 8) & 0xFF) / 0xFF;
        float fBlue = (float)(uColor & 0xFF) / 0xFF;
        return new float[]{fRed,fGreen,fBlue,fAlpha};
    }
    protected int mGLProgId;
    protected int mGLAttribPosition;
    private int GLESDRAWMODE;

    String vertexShader =
            "attribute vec3 position;\n" + // 顶点着色器的顶点坐标,由外部程序传入
                    "uniform mat4 model;"+
                    "uniform mat4 view;"+
                    "uniform mat4 projection;"+
                    " \n" +
                    "void main()\n" +
                    "{\n" +
                    "    vec3 FragPos = vec3(model * vec4(position, 1.0));\n"+
                    "    gl_Position = projection * view * vec4(FragPos, 1.0);\n" +
                    "}";

    String fragmentShader =
            "precision mediump float;"+
                    "uniform vec4 color;"+
                    "void main()\n" +
                    "{" +
                    "     gl_FragColor =  color;\n" +//默认红色
                    "}";

    public FloatBuffer GenPolygonFrame(List<Point3D> polygon){
        float vertexPoints[]=new float[polygon.size()*3];
        for (int i=0;i<polygon.size();i++) {
            Point3D point=polygon.get(i);
            vertexPoints[i*3+0]=point.x;
            vertexPoints[i*3+1]=point.y;
            vertexPoints[i*3+2]=point.z;
        }
        pointCount=polygon.size();//一个点有3个坐标
        // 顶点数组缓冲器
        FloatBuffer vertextBuffer = ByteBuffer.allocateDirect(vertexPoints.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertextBuffer.put(vertexPoints).position(0);
        GLESDRAWMODE= GLES20.GL_LINE_LOOP;
        return vertextBuffer;
    }
    public FloatBuffer GenConeFrame(){
        Point3D center=new Point3D(0,0,0);
        float vertexPoints[]=new float[]{
                center.x-25,center.y,center.z-25,
                center.x+25,center.y,center.z-25,
                center.x,center.y,center.z+25,

                center.x,center.y-100,center.z,
                center.x-25,center.y,center.z-25,
                center.x+25,center.y,center.z-25,

                center.x,center.y-100,center.z,
                center.x+25,center.y,center.z-25,
                center.x,center.y,center.z+25,

                center.x,center.y-100,center.z,
                center.x-25,center.y,center.z-25,
                center.x,center.y,center.z+25,
        };
        pointCount=vertexPoints.length/3;
        // 顶点数组缓冲器
        FloatBuffer vertextBuffer = ByteBuffer.allocateDirect(vertexPoints.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertextBuffer.put(vertexPoints).position(0);
        GLESDRAWMODE=GLES20.GL_TRIANGLES;
        return vertextBuffer;
    }
    public FloatBuffer GenCircleFrame(Point3D center,float radius){
        int _vertCount = 20;//把圆切分10份
        float[] vertexPoints = new float[_vertCount * 3];
        int index=0;
        for (int i = 0; i < _vertCount; i++) {
            float delta = (float)(2.0*Math.PI/_vertCount)*i;
            vertexPoints[index++] = center.x + radius * (float)Math.cos(delta);
            vertexPoints[index++] = center.y + radius * (float)Math.sin(delta);
            vertexPoints[index++] = center.z;
        }
        pointCount=vertexPoints.length/3;//一个点有3个坐标
        // 顶点数组缓冲器
        FloatBuffer vertextBuffer = ByteBuffer.allocateDirect(vertexPoints.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertextBuffer.put(vertexPoints).position(0);
        GLESDRAWMODE=GLES20.GL_LINE_LOOP;
        return vertextBuffer;
    }
    public FloatBuffer GenLineFrame(List<PointF> points,float z){
        float vertexPoints[]=new float[points.size()*3];
        pointCount=vertexPoints.length/3;//一个点有3个坐标
        FloatBuffer vertextBuffer = ByteBuffer.allocateDirect(vertexPoints.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertextBuffer.put(vertexPoints).position(0);
        mGLProgId = OpenGLUtils.loadProgram(vertexShader, fragmentShader); // 编译链接着色器，创建着色器程序
        GLESDRAWMODE=GLES20.GL_LINE_STRIP;
        return  vertextBuffer;
    }
    private void initShader(){
        mGLProgId = OpenGLUtils.loadProgram(vertexShader, fragmentShader); // 编译链接着色器，创建着色器程序
        mGLAttribPosition = GLES20.glGetAttribLocation(mGLProgId, "position"); // 顶点着色器的顶点坐标
    }



    //绘制指定帧
    public void draw(float[] model, float[] view, float[] projection,
                     FloatBuffer frameData,int color,int width
    ) {
        GLES20.glUseProgram(mGLProgId);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        //设置模型矩阵
        int mModelHandler = GLES20.glGetUniformLocation(mGLProgId,"model");
        GLES20.glUniformMatrix4fv(mModelHandler,1,false, model, 0);
        //设置相机矩阵
        int mViewHandler = GLES20.glGetUniformLocation(mGLProgId,"view");
        GLES20.glUniformMatrix4fv(mViewHandler,1,false, view, 0);
        //设置投影矩阵
        int mProjectionHandler = GLES20.glGetUniformLocation(mGLProgId,"projection");
        GLES20.glUniformMatrix4fv(mProjectionHandler,1,false, projection, 0);

        //设置颜色
        int mColorHandle = GLES20.glGetUniformLocation(mGLProgId, "color");
        float[] colorarr=getOpenGLColor(color);
        GLES20.glUniform4f(mColorHandle,colorarr[0],colorarr[1],colorarr[2],colorarr[3]);

        GLES20.glVertexAttribPointer(mGLAttribPosition, 3, GLES20.GL_FLOAT, false, 0, frameData);
        GLES20.glEnableVertexAttribArray(mGLAttribPosition);

        GLES20.glLineWidth(width);



        //绘制线
        GLES20.glDrawArrays(GLESDRAWMODE, 0, pointCount);
        GLES20.glDisableVertexAttribArray(mGLAttribPosition);


    }
}
