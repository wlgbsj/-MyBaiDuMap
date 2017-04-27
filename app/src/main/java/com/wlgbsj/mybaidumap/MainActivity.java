package com.wlgbsj.mybaidumap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.radar.RadarNearbyResult;
import com.baidu.mapapi.radar.RadarNearbySearchOption;
import com.baidu.mapapi.radar.RadarSearchError;
import com.baidu.mapapi.radar.RadarSearchListener;
import com.baidu.mapapi.radar.RadarSearchManager;
import com.baidu.mapapi.radar.RadarUploadInfo;

import java.util.ArrayList;
import java.util.List;

//设置周边雷达的监听器
public class MainActivity extends Activity implements View.OnClickListener, RadarSearchListener {

    private MapView mMapView;

    private BaiduMap mBaiduMap;

    private Button bt_putong;
    private Button bt_weixing;
    private Button bt_kongbai;
    private Button bt_jiaotong;
    private Button bt_relitu;
    private Button bt_addview;


    private Button bt_zhoubianleida;
    private Button bt_getmy;
    private Button bt_jiheview;
    private Button bt_jwejnzi;


    private CheckBox checkbox;


    private Marker marker;
    private BitmapDescriptor bitmap;
    private LocationClient mLocClient;
    private boolean isFirstLoc = true;


    private final String TAG = "MainActivity";
    private LocationClient mLocationClient;
    private BDLocationListener mBDLocationListener;

    private LatLng mYpoint;

    private RadarSearchManager mRadarSearchManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        findView();
        setListener();

    }

    private void findView() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        bt_putong = (Button) findViewById(R.id.bt_putong);
        bt_weixing = (Button) findViewById(R.id.bt_weixing);
        bt_kongbai = (Button) findViewById(R.id.bt_kongbai);
        bt_jiaotong = (Button) findViewById(R.id.bt_jiaotong);
        bt_relitu = (Button) findViewById(R.id.bt_relitu);
        bt_addview = (Button) findViewById(R.id.bt_addview);
        bt_zhoubianleida = (Button) findViewById(R.id.bt_zhoubianleida);
        bt_getmy = (Button) findViewById(R.id.bt_getmy);
        bt_jiheview = (Button) findViewById(R.id.bt_jiheview);
        bt_jwejnzi = (Button) findViewById(R.id.bt_jwejnzi);
        checkbox = (CheckBox) findViewById(R.id.checkbox);


        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.ic_launcher);
    }

    private void setListener() {
        bt_putong.setOnClickListener(this);
        bt_weixing.setOnClickListener(this);
        bt_kongbai.setOnClickListener(this);
        bt_jiaotong.setOnClickListener(this);
        bt_relitu.setOnClickListener(this);
        bt_addview.setOnClickListener(this);
        bt_zhoubianleida.setOnClickListener(this);
        bt_getmy.setOnClickListener(this);
        bt_jiheview.setOnClickListener(this);
        bt_jwejnzi.setOnClickListener(this);
        checkbox.setOnClickListener(this);
    }


    /**
     * 添加地图标注
     */
    private void addView(double latitude, double longitude) {
        //定义Maker坐标点
        LatLng point = new LatLng(latitude, longitude);
        //构建Marker图标

        //构建MarkerOption，用于在地图上添加Marker

        OverlayOptions options = new MarkerOptions()
                .position(point)  //设置marker的位置
                .icon(bitmap)  //设置marker图标
                .zIndex(9)  //设置marker所在层级
                .draggable(true)  //设置手势拖拽
                .alpha((float) 0.5);   //设置透明度

        //将marker添加到地图上
        marker = (Marker) (mBaiduMap.addOverlay(options));



        /*通过marker的icons设置一组图片，再通过period设置多少帧刷新一次图片资源
        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
        giflist.add(bdA);
        giflist.add(bdB);
        giflist.add(bdC);
        OverlayOptions ooD = new MarkerOptions()
                .position(pt)
                .icons(giflist)
                .zIndex(0)
                .period(10);
        mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));*/


        //调用BaiduMap对象的setOnMarkerDragListener方法设置marker拖拽的监听
        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
                //拖拽中
                //  Toast.makeText(MainActivity.this,"拖拽中",Toast.LENGTH_SHORT).show();
            }

            public void onMarkerDragEnd(Marker marker) {
                //拖拽结束
                Toast.makeText(MainActivity.this, "拖拽结束", Toast.LENGTH_SHORT).show();
                marker.remove();   //调用Marker对象的remove方法实现指定marker的删除
            }

            public void onMarkerDragStart(Marker marker) {
                //开始拖拽
                Toast.makeText(MainActivity.this, "开始拖拽", Toast.LENGTH_SHORT).show();

            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.remove();
                return true;
            }
        });
    }

    /**
     * 添加动画  从天上掉下来  或者 从地上长出来的 效果
     */
    private void addDonghu() {
        LatLng point = new LatLng(39.963175, 116.400244);
        //从地上生长和从天上落下。
        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
        BitmapDescriptor bdA = BitmapDescriptorFactory
                .fromResource(R.drawable.lifebuoy);
        BitmapDescriptor bdB = BitmapDescriptorFactory
                .fromResource(R.drawable.light);
        BitmapDescriptor bdC = BitmapDescriptorFactory
                .fromResource(R.drawable.toy);
        giflist.add(bdA);
        giflist.add(bdB);
        giflist.add(bdC);
        MarkerOptions ooD = new MarkerOptions()
                .position(point)
                .icons(giflist)
                .zIndex(0)
                .period(500);
        if (checkbox.isChecked()) {
            // 生长动画
            ooD.animateType(MarkerOptions.MarkerAnimateType.drop);
        } else {
            ooD.animateType(MarkerOptions.MarkerAnimateType.grow);
        }
        Marker mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));
    }


    /** 几何覆盖物 */
    private void testGeometryOverlay() {
        //定义多边形的五个顶点
        LatLng pt1 = new LatLng(39.93923, 116.357428);
        LatLng pt2 = new LatLng(39.91923, 116.327428);
        LatLng pt3 = new LatLng(39.89923, 116.347428);
        LatLng pt4 = new LatLng(39.89923, 116.367428);
        LatLng pt5 = new LatLng(39.91923, 116.387428);
        List<LatLng> pts = new ArrayList<LatLng>();
        pts.add(pt1);
        pts.add(pt2);
        pts.add(pt3);
        pts.add(pt4);
        pts.add(pt5);
        //构建用户绘制多边形的Option对象
        OverlayOptions polygonOption = new PolygonOptions()
                .points(pts)
                .stroke(new Stroke(5, 0xAA00FF00))
                .fillColor(0xAAFFFF00);

        //在地图上添加多边形Option，用于显示
        mBaiduMap.addOverlay(polygonOption);
    }

    /** 文字覆盖物 */
    private void testTextOverlay() {
        LatLng text = new LatLng(39.86923, 116.397428);
        OverlayOptions options = new TextOptions()
                .bgColor(0xAAFFFF00)
                .fontSize(24)
                .zIndex(15)
                .fontColor(0xFFFF00FF)
                .text("文字覆盖物测试")
                //.rotate(-45) // 旋转角度
                .rotate(0) // 旋转角度
                .position(mYpoint);

        mBaiduMap.addOverlay(options);
    }



    /**
     *  获得所在位置经纬度及详细地址 
     */
    private void getLocation() {


        // 定位初始化
        mLocClient = new LocationClient(MainActivity.this);
        mLocClient.registerLocationListener(new MyLocationListenner());
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        LatLng ll = new LatLng(100,
                100);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

    }

    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mBaiduMap.clear();

            mYpoint = new LatLng(location.getLatitude(), location.getLongitude());

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(5)//设置圈的直径大小
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(locData);
            LatLng llA = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions ooA = new MarkerOptions()
                    .position(llA)
                    .icon(bitmap)
                    .zIndex(9)
                    //.title(location.getAddrStr().toString())
                    .draggable(true);
            mBaiduMap.addOverlay(ooA);

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }


    }

    private void findNearUser() {

        //初始化周边雷达功能
         mRadarSearchManager = RadarSearchManager.getInstance();
        //周边雷达设置监听
        mRadarSearchManager.addNearbyInfoListener(this);


        //周边雷达设置用户身份标识，id为空默认是设备标识
        mRadarSearchManager.setUserID(null);
        //上传位置
        RadarUploadInfo info = new RadarUploadInfo();
        info.comments = "用户备注信息";
        info.pt = mYpoint;
        mRadarSearchManager.uploadInfoRequest(info);


        //构造请求参数，其中centerPt是自己的位置坐标
        RadarNearbySearchOption option = new RadarNearbySearchOption().centerPt(mYpoint).pageNum(0).pageCapacity(50).radius(2000);
        //发起查询请求
        mRadarSearchManager.nearbyInfoRequest(option);

        //用户信息清除后，将不会再被其他人检索到。
        //mRadarSearchManager.clearUserInfo();


       /*
       //连续信息上传
        //设置自动上传的callback和时间间隔
        mManager.startUploadAuto(this, 5000);
           //实现上传callback，自动上传
        @Override
                publicRadarUploadInfoOnUploadInfoCallback() {
            // TODO Auto-generated method stub
            RadarUploadInfo info = new RadarUploadInfo();
            info.comments = “用户备注信息”;
            info.pt = pt;
            return info;
      */
    }

    @Override
    public void onGetNearbyInfoList(RadarNearbyResult radarNearbyResult, RadarSearchError radarSearchError) {
// TODO Auto-generated method stub
        if (radarSearchError == RadarSearchError.RADAR_NO_ERROR) {
            Toast.makeText(MainActivity.this, "查询周边成功", Toast.LENGTH_LONG)
                    .show();
            //获取成功，处理数据
        } else {
            //获取失败
            Toast.makeText(MainActivity.this, "查询周边失败", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onGetUploadState(RadarSearchError error) {
        // TODO Auto-generated method stub
        if (error == RadarSearchError.RADAR_NO_ERROR) {
            //上传成功
            Toast.makeText(MainActivity.this, "单次上传位置成功", Toast.LENGTH_LONG)
                    .show();
        } else {
            //上传失败
            Toast.makeText(MainActivity.this, "单次上传位置失败", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onGetClearInfoState(RadarSearchError radarSearchError) {
// TODO Auto-generated method stub
        if (radarSearchError == RadarSearchError.RADAR_NO_ERROR) {
            //清除成功
            Toast.makeText(MainActivity.this, "清除位置成功", Toast.LENGTH_LONG)
                    .show();
        } else {
            //清除失败
            Toast.makeText(MainActivity.this, "清除位置失败", Toast.LENGTH_LONG)
                    .show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();

        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mBDLocationListener);
        }

        //移除监听
        mRadarSearchManager.removeNearbyInfoListener(this);
         //清除用户信息
        mRadarSearchManager.clearUserInfo();
        //释放资源
        mRadarSearchManager.destroy();
        mRadarSearchManager = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    private boolean bt_jiaotong_flag = false;
    private boolean bt_relitu_flag = false;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_putong) {
            //普通地图
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        }

        if (v.getId() == R.id.bt_weixing) {
            //卫星地图
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        }

        if (v.getId() == R.id.bt_kongbai) {
            //空白地图, 基础地图瓦片将不会被渲染。在地图类型中设置为NONE，将不会使用流量下载基础地图瓦片图层。使用场景：与瓦片图层一起使用，节省流量，提升自定义瓦片图下载速度。
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
        }
        if (v.getId() == R.id.bt_jiaotong) {
            bt_jiaotong_flag = !bt_jiaotong_flag;
            if (bt_jiaotong_flag) {
                //开启交通图
                mBaiduMap.setTrafficEnabled(true);
            } else {
                mBaiduMap.setTrafficEnabled(false);
            }

        }
        if (v.getId() == R.id.bt_relitu) {
            bt_relitu_flag = !bt_relitu_flag;
            if (bt_relitu_flag) {
                //开启城市热力图
                mBaiduMap.setBaiduHeatMapEnabled(true);
            } else {
                //开启城市热力图
                mBaiduMap.setBaiduHeatMapEnabled(false);
            }

        }

        if (v.getId() == R.id.bt_addview) {
            addView(39.963175, 116.400244);
        }
        if (v.getId() == R.id.checkbox) {
            addDonghu();
        }
        if (v.getId() == R.id.bt_zhoubianleida) {
            findNearUser();
        }
        if (v.getId() == R.id.bt_getmy) {
            getLocation();
        }
        if (v.getId() == R.id.bt_jiheview) {
            testGeometryOverlay();
        }

        if (v.getId() == R.id.bt_jwejnzi) {
            testTextOverlay();
        }



    }

}
