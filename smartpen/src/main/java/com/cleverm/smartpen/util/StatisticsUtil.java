package com.cleverm.smartpen.util;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cleverm.smartpen.app.SelectTableActivity;
import com.cleverm.smartpen.application.SmartPenApplication;
import com.cleverm.smartpen.pushtable.UpdateTableHandler;
import com.cleverm.smartpen.statistic.dao.DaoMaster;
import com.cleverm.smartpen.statistic.dao.DaoSession;
import com.cleverm.smartpen.statistic.dao.StatsDao;
import com.cleverm.smartpen.statistic.model.Stats;
import com.cleverm.smartpen.util.excle.CreateExcel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;

/**
 * Created by xiong,An android project Engineer,on 2016/3/3.
 * Data:2016-03-03  14:18
 * Base on clever-m.com(JAVA Service)
 * Describe: 统计帮助类
 * sqlite数据库位于：/data/data/项目包/databases中
 * Version:1.0
 * Open source
 */
public class StatisticsUtil {

    public static final String UPLOAD_FILE_URL=Constant.DDP_URL+"/api/api/v10/uploadAccessLog/save";

    //统计Event对应表
    public static final int   CALL_ADD_FOOD =1;//点餐加菜
    public static final String CALL_ADD_FOOD_DESC="点餐加菜";
    public static final int   CALL_ADD_WATER =2;//添加茶水
    public static final String CALL_ADD_WATER_DESC="添加茶水";
    public static final int   CALL_ADD_TISSUE =3;//湿巾纸巾
    public static final String CALL_ADD_TISSUE_DESC="湿巾纸巾";
    public static final int   CALL_PAY =4;//呼叫结账
    public static final String   CALL_PAY_DESC="呼叫结账";
    public static final int   CALL_OTHER_SERVIC =5;//其他服务
    public static final String CALL_OTHER_SERVIC_DESC="其他服务";

    //1现金结账/2银行卡结账/3支付宝结账/4微信结账
    public static final long   CALL_PAY_CASH=1L;
    public static final String CALL_PAY_CASH_DESC="现金结账";
    public static final long   CALL_PAY_CARD=2L;
    public static final String   CALL_PAY_CARD_DESC="银行卡结账";
    public static final long   CALL_PAY_ALIPAY=3L;
    public static final String   CALL_PAY_ALIPAY_DESC="支付宝结账";
    public static final long   CALL_PAY_WEIXIN=4L;
    public static final String CALL_PAY_WEIXIN_DESC="微信结账";


    public static final int   SERVICE_DEMO =6;//DEMO引导
    public static final String SERVICE_DEMO_DESC="DEMO引导";
    public static final int   SERVICE_EVALUATE=7;//服务评价
    public static final String SERVICE_EVALUATE_DESC="服务评价";
    public static final int   SERVICE_BUY_MYSELF =8;//自助买单
    public static final String SERVICE_BUY_MYSELF_DESC="自助买单";
    public static final int   SERVICE_DISCOUNT =9;//优惠专区
    public static final String SERVICE_DISCOUNT_DESC="优惠专区";
    public static final int   SERVICE_DAIJIA =10;//代驾服务
    public static final String SERVICE_DAIJIA_DESC="代驾服务";
    public static final int   SERVICE_LOCAL_DISCOUNT =11;//本店推介
    public static final String SERVICE_LOCAL_DISCOUNT_DESC="本店推介";

    public static final int   APP_WEATHER =12;//今日天气
    public static final String APP_WEATHER_DESC="今日天气";
    public static final int   APP_NEWS =13;//今日头条
    public static final String APP_NEWS_DESC="今日头条";
    public static final int   APP_AROUNDPLAY =14;//周边玩乐
    public static final String APP_AROUNDPLAY_DESC="周边玩乐";
    public static final int   APP_ONLINEBUY =15;//在线商场
    public static final String APP_ONLINEBUY_DESC="在线商场";
    public static final int   APP_AROUNDDISCOUNT =16;//周边优惠
    public static final String APP_AROUNDDISCOUNT_DESC="周边优惠";
    public static final int   APP_VIDEO =17;//视频娱乐
    public static final String APP_VIDEO_DESC="视频娱乐";
    public static final int   APP_MAGAZINE =18;//电子杂志
    public static final String APP_MAGAZINE_DESC="电子杂志";
    public static final int   H5_GAME =19;//手游试玩
    public static final String H5_GAME_DESC="手游试玩";
    public static final int   SETTING =20;//设置
    public static final String SETTING_DESC="设置";
    public static final int   CALL_GET_CARD =37;//办卡有礼
    public static final String CALL_GET_CARD_DESC="办卡有礼";
    public static final int   CALL_GET_CAR=38;//通用汽车
    public static final String CALL_GET_CAR_DESC="通用汽车";
    public static final int   UN_KNOW =100;//暂时没有加进来功能
    public static final String UN_KNOW_DESC="预留功能";

    public static final int SECOND_DISCOUNT_ACTIVITY=21;//优惠专区活动轮播
    public static final String SECOND_DISCOUNT_ACTIVITY_DESC="优惠专区活动轮播";
    public static final int SECOND_LOACL_DISCOUNT_ACTIVITY =22;//本店推介活动轮播
    public static final String SECOND_LOACL_DISCOUNT_ACTIVITY_DESC="本店推介活动轮播";

    public static final int OTHER_OPEN_TIME=23;//开机时间
    public static final String OTHER_OPEN_TIME_DESC="开机时间";
    public static final int OTHER_CLOSE_TIME=24;//关机时间
    public static final String OTHER_CLOSE_TIME_DESC="关机时间";
    public static final int OTHER_COMMENT_SUBMIT=25;//评价提交
    public static final String OTHER_COMMENT_SUBMIT_DESC="评价提交";
    public static final int OTHER_GIVE_MONEY=26;//打赏
    public static final String OTHER_GIVE_MONEY_DESC="打赏";

    public static final int BACK_VIDEO=27;
    public static final String BACK_VIDEO_DESC="视频界面";

    public static final int CLEAN_DESK=28;
    public static final String CLEAN_DESK_DESC="收拾桌面";

    public static final int FONDUE_SOUP_STAT=29;
    public static final String FONDUE_SOUP_STAT_DESC="火锅加汤";

    public static final int CHANGE_TABLEWARE_STAT=30;
    public static final String CHANGE_TABLEWARE_STAT_DESC="更换餐具";

    public static final int VIDEO_AD_DETAIL=31;
    public static final String VIDEO_AD_DETAIL_DESC="视频广告详情";

    public static final int APP_PRIZE_GOIN=32;
    public static final String APP_PRIZE_GOIN_DESC="抽奖活动";

    public static final int APP_PRIZE_CLICKDRAW=33;
    public static final String APP_PRIZE_CLICKDRAW_DESC="点击抽奖";

    public static final int APP_PRIZE_INPUTPHONE=34;
    public static final String APP_PRIZE_INPUTPHONE_DESC="输入手机号";

    public static final int APP_PRIZE_RECEIVEGOODS=35;
    public static final String APP_PRIZE_RECEIVEGOODS_DESC="领取奖品";

    //主事件为36，二级事件值为 该云端奖券的prizeID值
    public static final int APP_PRIZE_SERVER_LUCKYID=36;
    public static final String APP_PRIZE_SERVER_LUCKYID_DESC="云端奖券";




    //点点笔数据库名
    //private static final String DB_NAME="ddb_db";//第一个版本的数据库名称ddb_db，增加flag字段后为了不出现crash,取新名ddb_db2
    private static final String DB_NAME="ddb_db2";//第一个版本的数据库名称ddb_db，增加flag字段后为了不出现crash,取新名ddb_db2

    //用于FutureActivity-Intent参数
    public static final String FUTURE_INTNET_EVENTID="FUTURE_INTNET_EVENTID";
    public static final String FUTRUE_INTENT_EVENTDESC="FUTRUE_INTENT_EVENTDESC";
    public static final String FUTURE_INTENT_EVENTDESC_DEFAULT="自助买单";
    public static final String ERROR_EVENTID="错误事件,请不要用于统计分析";
    public static final int ERROR_AND_NOE_STAISTICS=-8989;
    public static final long ERROR_AND_NOE_STAISTICS_LONG=-8989L;


    //参数回调
    public interface  SqlInterface{
        String onComplete();
    }


    private static StatisticsUtil INSTANCE = new StatisticsUtil();

    public static StatisticsUtil getInstance() {
        return INSTANCE;
    }

    private  StatisticsUtil(){

    }

    public class DaoReturnValue{

        private SQLiteDatabase db;

        private StatsDao statsDao;

        public DaoReturnValue(){

        }

        public DaoReturnValue(SQLiteDatabase db,StatsDao statsDao){
            this.db=db;
            this.statsDao=statsDao;
        }

        public void setDb(SQLiteDatabase db) {
            this.db = db;
        }

        public StatsDao getStatsDao() {
            return statsDao;
        }

        public SQLiteDatabase getDb() {
            return db;
        }

        public void setStatsDao(StatsDao statsDao) {
            this.statsDao = statsDao;
        }
    }

    /**
     * 初始化获取dao对象
     * @param application
     */
    public DaoReturnValue onInit(Application application){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(application, DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();

        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        StatsDao statsDao = daoSession.getStatsDao();

        return new DaoReturnValue(db,statsDao);
    }

    public class TimeValue{

        private Long rawID;
        private long start;
        private long end;

        public TimeValue(){

        }

        public TimeValue(Long rawID,long start,long end){
            this.rawID=rawID;
            this.start=start;
            this.end=end;
        }

        public Long getRawID() {
            return rawID;
        }

        public void setRawID(Long rawID) {
            this.rawID = rawID;
        }

        public long getStart() {
            return start;
        }

        public void setStart(long start) {
            this.start = start;
        }

        public long getEnd() {
            return end;
        }

        public void setEnd(long end) {
            this.end = end;
        }
    }

    /**
     * 请在OnCreate中调用
     * @param EventId
     * @param desc
     * @return
     */
    public TimeValue onCreate(int EventId,String desc){
        TimeValue timeValue = new TimeValue();
        Long rawID = StatisticsUtil.getInstance().insert(EventId, desc);
        long start = System.currentTimeMillis();
        timeValue.setRawID(rawID);
        timeValue.setStart(start);
        return timeValue;
    }

    /**
     * 请在OnDestry中调用
     * @param timeValue
     */
    public void onDestory(TimeValue timeValue){
        long end = System.currentTimeMillis();
        timeValue.setEnd(end);
        if(timeValue.getRawID()!=ERROR_AND_NOE_STAISTICS_LONG){
            StatisticsUtil.getInstance().update_timestay(timeValue.getRawID(), timeValue.getEnd() - timeValue.getStart());
        }
    }


    /**
     * 获取bean对象
     * @param actionId
     * @param timePoit
     * @param timeStay
     * @param clientId
     * @param orgId
     * @param tableId
     * @param desc
     * @param secondid
     * @param querydata
     * @return
     */
    private Stats getStasObject( int actionId, Long timePoit, Long timeStay, Long clientId, Long orgId, Long tableId, String desc, Long secondid, String querydata,boolean flag){
        Stats stats = new Stats(null,  actionId,  timePoit,  timeStay,  clientId,  orgId,  tableId,  desc,  secondid,  querydata,flag);
        return stats;
    }

    /**
     * 执行Event的插入
     * insert语法
     * @param eventId
     * @return Long 该自增长的id值,他将会完成timeStay的赋值
     */
    public Long insert(int eventId,String desc){
        QuickUtils.log("insert - eventId = " + eventId + "   eventDesc = " + desc);
        if(checkValid(eventId)){
            //插入数据库
            return SmartPenApplication.getStatsDao().insert(StatisticsUtil.getInstance().getStasObject(
                    eventId,
                    getCurrentTimeMillis(),
                    0L,
                    getClientId(),
                    getOrgId(),
                    getDeskId(),
                    desc,null,getEventHappenTime(),false
            ));
        }else{
            return ERROR_AND_NOE_STAISTICS_LONG;
        }



    }

    /**
     * Event 4,21,22 三个事件是含有子事件的
     * @param eventId
     * @param desc
     * @param secondEvent
     * @return
     */
    public Long insertWithSecondEvent(int eventId,String desc,Long secondEvent){
        QuickUtils.log("insert - eventId = " + eventId + "   eventDesc = " + desc+" secondEvent = "+secondEvent);
        //插入数据库
        return SmartPenApplication.getStatsDao().insert(StatisticsUtil.getInstance().getStasObject(
                eventId,
                getCurrentTimeMillis(),
                0L,
                getClientId(),
                getOrgId(),
                getDeskId(),desc,secondEvent,
                getEventHappenTime(),false));
    }

    /**
     * 根据自增长Id值,统计该界面停留时长
     * 计算 oDestory()-onCreate()方法之间的时长
     * update语法,直接使用sql语法
     * update TableName set timeStay = xxxx where id = xxxx;
     * update t_stats set de.greenrobot.dao.Property@41d1a1a8 = 8744 where de.greenrobot.dao.Property@41d19fb0 = 11;
     * @param rawId
     * @param timeStay 毫秒值
     */
    public void  update_timestay(Long rawId,Long timeStay){

        String SQL="update " +StatsDao.TABLENAME+
                " set "+StatsDao.Properties.TimeStay.columnName+
                " =?"+" where " +StatsDao.Properties.Id.columnName+
                " =?";

        //Or Use this SQL
        String SQL2="update t_stats set "+" TIME_STAY ="+ timeStay +" where _id = "+rawId;

        SmartPenApplication.getDb().execSQL(SQL,new Object[] {timeStay, rawId});
    }


    /**
     * 记录点击次数
     * @param event
     */
    public void rememberCount(int event){


    }

    public boolean checkValid(int eventId){
        if(eventId==ERROR_AND_NOE_STAISTICS){
            return false;
        }
        return true;
    }



    /**
     * 拿取当前的时间戳
     * @return
     */
    private Long getCurrentTimeMillis(){
        return System.currentTimeMillis();
    }

    private String getClickTime(String clickTime){
        return QuickUtils.timeStamp2DateNoSec2(clickTime + "", null);
    }

    private String getStayTime(String stayTime){
        long l = Long.parseLong(stayTime);
        long second = l / 1000;
        return second+"";
    }
    
    public Long str2Long(String str){
        long l = Long.parseLong(str);
        return l;
    }


    public String getEventHappenTime(){
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String queryTime = dateFormat.format(date);
        return queryTime;
    }


    private static final String DEFALUT_CLIENTID="";
    private static final String DEFALUT_ORGID="";

    /**
     * 获取推送的商户号
     * @return
     */
    private Long getClientId(){
        String clientId = RememberUtil.getString(UpdateTableHandler.CLIENTID, DEFALUT_CLIENTID);
        if(clientId.equals(DEFALUT_CLIENTID)){
            return 8888L;
        }
        return Long.parseLong(clientId);
    }

    /**
     * 获取推送的餐厅号
     * @return
     */
    private Long getOrgId(){
        String orgId=RememberUtil.getString(UpdateTableHandler.ORGID, DEFALUT_ORGID);
        if(orgId.equals(DEFALUT_ORGID)){
            return 8888L;
        }
        return Long.parseLong(orgId);
    }

    /**
     * 获取推送的餐桌号
     * @return
     */
    private Long getDeskId(){
        long deskId = RememberUtil.getLong(SelectTableActivity.SELECTEDTABLEID, Constant.DESK_ID_DEF_DEFAULT);
        return deskId;
    }

    private String getOrgStrId(){
        String orgId=RememberUtil.getString(UpdateTableHandler.ORGID, DEFALUT_ORGID);
        return orgId;
    }



    private Cursor exeSql(String sql) {
        return SmartPenApplication.getDb().rawQuery(sql, null);
    }


    public ArrayList<ArrayList<String>> getDBForExcel(){
        String QUERY_TIME = getEventHappenTime();
        String SQL_DATA;
        ArrayList<ArrayList<String>> out_lists = new ArrayList<ArrayList<String>>();
        String SQL_COUNT="select count"+"("+StatsDao.Properties.Querydata.columnName+")"+" as QueryCount "+"from t_stats where "+StatsDao.Properties.Querydata.columnName+" = "+"'"+QUERY_TIME+"'";

        Cursor cursor1 = exeSql(SQL_COUNT);
        cursor1.moveToFirst();
        long count = cursor1.getLong(0);

        //2003--最大65536
        if(count>65530){
             SQL_DATA="select * from t_stats where "+StatsDao.Properties.Querydata.columnName+" = "+"'"+QUERY_TIME+"'"+"limit 0,65530";
        }else{
             SQL_DATA="select * from t_stats where "+StatsDao.Properties.Querydata.columnName+" = "+"'"+QUERY_TIME+"'";
        }

        QuickUtils.log("SQL_COUNT=" + SQL_COUNT + "/SQL_DATA" + SQL_DATA+"/COUNT="+count);
        Cursor cursor = exeSql(SQL_DATA);
        while (cursor.moveToNext()){

            ArrayList<String> inner_list = new ArrayList<String>();
            int columnIndex_0 = cursor.getColumnIndex(StatsDao.Properties.Id.columnName);
            int columnIndex_1 = cursor.getColumnIndex(StatsDao.Properties.ActionId.columnName);
            int columnIndex_2 = cursor.getColumnIndex(StatsDao.Properties.TimePoit.columnName);
            int columnIndex_3 = cursor.getColumnIndex(StatsDao.Properties.TimeStay.columnName);
            int columnIndex_4 = cursor.getColumnIndex(StatsDao.Properties.ClientId.columnName);
            int columnIndex_5 = cursor.getColumnIndex(StatsDao.Properties.OrgId.columnName);
            int columnIndex_6 = cursor.getColumnIndex(StatsDao.Properties.TableId.columnName);
            int columnIndex_7 = cursor.getColumnIndex(StatsDao.Properties.Desc.columnName);
            int columnIndex_8 = cursor.getColumnIndex(StatsDao.Properties.Secondid.columnName);
            int columnIndex_9 = cursor.getColumnIndex(StatsDao.Properties.Querydata.columnName);

            String string = cursor.getString(columnIndex_2);
            QuickUtils.log("cursor==" + string);

            //统计9列数据+一行自增长的id值=10条数据
            inner_list.add(cursor.getString(columnIndex_0));
            inner_list.add(cursor.getString(columnIndex_1));
            inner_list.add(getClickTime(cursor.getString(columnIndex_2)));
            inner_list.add(getStayTime(cursor.getString(columnIndex_3)));
            inner_list.add(cursor.getString(columnIndex_4));
            inner_list.add(cursor.getString(columnIndex_5));
            inner_list.add(cursor.getString(columnIndex_6));
            inner_list.add(cursor.getString(columnIndex_7));
            inner_list.add(cursor.getString(columnIndex_8));
            inner_list.add(cursor.getString(columnIndex_9));
            out_lists.add(inner_list);
        }
        cursor.close();
        return out_lists;
    }

    /**
     *
     */
    public void  updateExcleToService(String url,File file){

        if(!getDeskId().equals(Constant.DESK_ID_DEF_DEFAULT)){
            QuickUtils.log("updateExcleToService");
            OkHttpUtils.post()
                    .addParams("orgId", getOrgStrId())
                    .addParams("tableId", getDeskId()+"")
                    .addParams("path", "statistic")
                    .addFile("resFile", file.getName(), file)//
                    .url(url)
                    .build()//
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            QuickUtils.log("upload-file-onError=" + e.getMessage());
                        }

                        @Override
                        public void onResponse(String result) {
                            //{"desc":"","data":"","code":"1000","err":""}
                            QuickUtils.log("upload-file-onResponse="+result.toString());

                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String code = jsonObject.getString("code");
                                if(code.equals("1000")){
                                    RememberUtil.putBooleanSync(getEventHappenTime(),true);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    });
        }
    }

    public File getStatsFile(){
        File file = new File(CreateExcel.EXPORT_PATH,getEventHappenTime()+".xls");
        if(file == null){
            return null;
        }
        return file;
    }


}
