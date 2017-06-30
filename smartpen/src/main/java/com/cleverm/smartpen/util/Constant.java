package com.cleverm.smartpen.util;

import android.os.Environment;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 95 on 2015/12/22.
 */
public class Constant {

    //alan add 20161114 for gameapp begin
//    public static final String GAME_ENTERTAINMENT = "com.gameley.runningman3.baidu1";          //手游试玩
    public static final String GAME_ENTERTAINMENT = "cn.jj";          //手游试玩
    //alan add 20161114 for gameapp end

    /**
     * URL地址
     */
    public static final String DDP_URL = Constant.BASE_URL;
    private static final String BASE_URL = "http://www.myee7.com";//正式机
    private static final String BASE_TEST_URL = "http://www.myee7.com/api_test";//测试机
    private static final String BASE_COMMON_IP_URL = "http://120.25.159.173:8280";
    private static final String BASE_CLEVER_IP_URL = "http://120.25.159.173:8080";
    private static final String BASE_PUSH_IP_URL = "http://120.25.159.173";
    public static final String QINIU_URL = "http://7xs3f5.com2.z0.glb.qiniucdn.com/";

    /**
     * Code 1
     */
    public static final int ORDER_DISHES1 = 1035;
    public static final int ADD_WATER1 = 1036;
    public static final int TISSUE1 = 1037;
    public static final int PAY1 = 1038;
    public static final int OTHER1 = 1039;
    public static final int TWO_DIMENSION_CODE1 = 1040;
    public static final int WEB1 = 1041;
    public static final int EVALUATE1 = 1042;
    public static final int DEMO1 = 1043;
    public static final int AMUSEMENTFRAGMENT1 = 1044;
    public static final int MO_JI1 = 1045;
    public static final int TOU_TIAO1 = 1046;
    public static final int BAI_DU1 = 1047;
    public static final int ONE_SHOP1 = 1048;
    public static final int DA_ZONG1 = 1049;
    public static final int E_JIA1 = 1050;
    public static final int ZHI_ZHU1 = 1051;
    public static final int YOU_HUI1 = 1052;
    public static final int RECOMMEND1 = 1053;
    public static final int SET1 = 1054;
    public static final int CLEAN_DESK = 1056;


    /**
     * Code 2
     */
    public static final int ORDER_DISHES2 = 1069;
    public static final int ADD_WATER2 = 1070;
    public static final int TISSUE2 = 1071;
    public static final int PAY2 = 1072;
    public static final int OTHER2 = 1073;
    public static final int TWO_DIMENSION_CODE2 = 1074;
    public static final int WEB2 = 1075;
    public static final int EVALUATE2 = 1076;
    public static final int DEMO2 = 1077;
    public static final int AMUSEMENTFRAGMENT2 = 1078;
    public static final int MO_JI2 = 1079;
    public static final int TOU_TIAO2 = 1080;
    public static final int BAI_DU2 = 1081;
    public static final int ONE_SHOP2 = 1082;
    public static final int DA_ZONG2 = 1083;
    public static final int E_JIA2 = 1084;
    public static final int ZHI_ZHU2 = 1085;
    public static final int YOU_HUI2 = 1086;
    public static final int RECOMMEND2 = 1087;
    public static final int SET2 = 1088;


    /**
     * Code 3
     */
    public static final int ORDER_DISHES3 = 1103;
    public static final int ADD_WATER3 = 1104;
    public static final int TISSUE3 = 1105;
    public static final int PAY3 = 1106;
    public static final int OTHER3 = 1107;
    public static final int TWO_DIMENSION_CODE3 = 1108;
    public static final int WEB3 = 1109;
    public static final int EVALUATE3 = 1110;
    public static final int DEMO3 = 1111;
    public static final int AMUSEMENTFRAGMENT3 = 1112;
    public static final int MO_JI3 = 1113;
    public static final int TOU_TIAO3 = 1114;
    public static final int BAI_DU3 = 1115;
    public static final int ONE_SHOP3 = 1116;
    public static final int DA_ZONG3 = 1117;
    public static final int E_JIA3 = 1118;
    public static final int ZHI_ZHU3 = 1119;
    public static final int YOU_HUI3 = 1120;
    public static final int RECOMMEND3 = 1121;
    public static final int SET3 = 1122;


    /**
     * Code 4
     */
    public static final int ORDER_DISHES4 = 1137;
    public static final int ADD_WATER4 = 1138;
    public static final int TISSUE4 = 1139;
    public static final int PAY4 = 1140;
    public static final int OTHER4 = 1141;
    public static final int TWO_DIMENSION_CODE4 = 1142;
    public static final int WEB4 = 1143;
    public static final int EVALUATE4 = 1144;
    public static final int DEMO4 = 1145;
    public static final int AMUSEMENTFRAGMENT4 = 1146;
    public static final int MO_JI4 = 1147;
    public static final int TOU_TIAO4 = 1148;
    public static final int BAI_DU4 = 1149;
    public static final int ONE_SHOP4 = 1150;
    public static final int DA_ZONG4 = 1151;
    public static final int E_JIA4 = 1152;
    public static final int ZHI_ZHU4 = 1153;
    public static final int YOU_HUI4 = 1154;
    public static final int RECOMMEND4 = 1155;
    public static final int SET4 = 1156;


    /**
     * Code 5
     */
    public static final int ORDER_DISHES5 = 1171;
    public static final int ADD_WATER5 = 1172;
    public static final int TISSUE5 = 1173;
    public static final int PAY5 = 1174;
    public static final int OTHER5 = 1175;
    public static final int TWO_DIMENSION_CODE5 = 1176;
    public static final int WEB5 = 1177;
    public static final int EVALUATE5 = 1178;
    public static final int DEMO5 = 1179;
    public static final int AMUSEMENTFRAGMENT5 = 1180;
    public static final int MO_JI5 = 1181;
    public static final int TOU_TIAO5 = 1182;
    public static final int BAI_DU5 = 1183;
    public static final int ONE_SHOP5 = 1184;
    public static final int DA_ZONG5 = 1185;
    public static final int E_JIA5 = 1186;
    public static final int ZHI_ZHU5 = 1187;
    public static final int YOU_HUI5 = 1188;
    public static final int RECOMMEND5 = 1189;
    public static final int SET5 = 1190;

    /**
     * 后面加进来的新码
     */
    public static final int FONDUE_SOUP = 1058;
    public static final int CHANGE_TABLEWARE = 1059;
    public static final int AWARD = 1055;
    public static final int FINANCIAL = 1057;
    public static final int ROBOT_SHOW = 1060;/**robot show*/
    public static final int UN_KNOW2 = 1061;
    public static final int UN_KNOW3 = 1062;
    public static final int UN_KNOW4 = 1063;

    /**
     * 呼叫表演码
     */
    public static final int CASH_PAY=0x123;
    public static final int UNION_CARD_PAY=0x124;

    public static final int WEIXIN_PAY=0X125;
    public static final int ALI_PAY=0X126;



    public static final String URL = "http://cn.vonvon.net/quiz/r/546/6116/v1GiNqZBv4fzmVf";
    public static final String NEARBY_DISCOUNT_URL = "http://sh.meituan.com/?utm_campaign=sogou&utm_medium=organic&utm_source=sogou&utm_content=homepage&utm_term=%25E7%25BE%258E%25E5%259B%25A2%25E7%25BD%2591";
    //public static final String NEARBY_DISCOUNT_URL = "http://i.meituan.com/";

    public static final String URL_H5_WANGWANG="http://www.hotkidclub.com/aoyun/index.html?partner=robot";


    public static final String MAIN_VIDEO_PATH = Environment.getExternalStorageDirectory().getPath() +
            "/muye/mainVideo";
    public static final String AD_VIDEO_PATH = Environment.getExternalStorageDirectory().getPath() + "/muye/ad";

    public static final String MOVINF_PATH = Environment.getExternalStorageDirectory().getPath() + "/muye/Video/Moving";
    public static final String MOVINF_PIC_PATH = Environment.getExternalStorageDirectory().getPath() +
            "/muye/Video/MovingPic";
    public static final String EVEING_PATH = Environment.getExternalStorageDirectory().getPath() +
            "/muye/Video/Evening";
    public static final String EVEING_PIC_PATH = Environment.getExternalStorageDirectory().getPath() +
            "/muye/Video/EveningPic";
    public static final String ZONGYI_PATH = Environment.getExternalStorageDirectory().getPath() + "/muye/Video/ZongYi";
    public static final String ZONGYI_PIC_PATH = Environment.getExternalStorageDirectory().getPath() +
            "/muye/Video/ZongYiPic";

    public static final String VIDEO_OVER = "back_to_commonFragment";
    public static final String BACK_TO_COMMONFRAGMENT = "back_to_commonFragment";
    public static final String ENTERAMUSEMENT = "enteramusement";
    public static final String PATH_KEY = "PATH";
    public static final String ISENTERAMUSEMENT_KEY = "isenteramusement_key";
    public static final String DIAN_CAI_PACKAGE_NAME = "com.cleverm.smartpen";


    /**
     * 内嵌APP
     */
    public static final String MO_JI_PACKAGE_NAME = "sina.mobile.tianqitonghd";
    public static final String TOU_TIAO_PACKAGE_NAME = "com.myzaker.ZAKER_HD";
    public static final String BAI_DU_PACKAGE_NAME = "com.tuniu.HD.ui";
    public static final String ONE_SHOP_PACKAGE_NAME = "com.taobao.apad";
    public static final String DA_ZONG_PACKAGE_NAME = "com.dianping.v1";
    public static final String ZHIZ_ZHU_PACKAGE_NAME = "com.dooland.padfordooland.reader";
    public static final String VIDEO_ENTERTAINMENT = "com.qiyi.video.pad";
    public static final String WAN_DOU_JIA = "http://www.wandoujia.com/apps";

    public static final String OTHER_STATE = "other_state";


    public static final String WEB_URL = "web_url";
    public static final int DELAY_BACK = 60000;

    public static final String NET_PATH = Constant.DDP_URL + "/cleverm/sockjs/execCommand";

//    public static final String NET_PATH = "http://192.168.1.72:8080/sockjs/execCommand";

    public static final String APP_NAME = "美味点点笔";
    public static final String PSW = "160323";

    public static final String HIDDEN_DOOR_OPEN_CHARGING="1107";
    public static final String HIDDEN_DOOR_CLOSE_CHARGING="2207";
    public static final String HIDDEN_DOOR_CHARGING_KEY="OutOfChargingKey";

    public static final String HIDDEN_DOOR_OPEN_ENGINEER="9900";
    public static final String HIDDEN_DOOR_CLOSE_ENGINEER="9910";
    public static final String HIDDEN_DOOR_ENGINEER_KEY="ENGINEERMODEL";


    public static final String SHARE = "CleverM";

    public static final String KEY_ID = "key_id";

    public static final long DESK_ID_DEF_DEFAULT = 8888L;
    public static final int USB_DISCONNECT = 100;

    public static final String DB_NAME = "clevermodel1.db";
    public static final String TAB_NAME = "RestaurantData";
    public static final String TAB_ID = "id";
    public static final String TAB_DESKID = "DeskId";

    /**
     * 短信码
     */
    public static final int FOOD_ADD_SMS = 1;//点餐加菜
    public static final int WATER_ADD_SMS = 2;//添加茶水
    public static final int TISSUE_ADD_SMS = 3;//湿巾纸巾
    public static final int PAY_MONRY_SMS = 4;//呼叫结账
    public static final int OTHER_SERVICE_SMS = 5;//其他服务
    public static final int CLEAN_SMS = 20;//收拾桌面
    public static final int PEN_PULL_OUT_SMS = 15;//笔拔出
    public static final int CASH_PAY_SMS = 16;//现金支付
    public static final int UNION_CARD_PAY_SMS = 17;//银行卡支付
    public static final int FONDUE_SOUP_SMS=21;//火锅加汤
    public static final int CHANGE_TABLEWARE_SMS=22;//更换餐具
    public static final int WEIXIN_PAY_SMS=18;//微信支付
    public static final int ALI_PAY_SMS=19;//支付宝支付
    public static final int OUT_OF_CHARGING=23;//充电宝断开
    //手环对应的服务码
    public static final int CASH_PAY_TO433 = 0x06; //现金结账
    public static final int UNION_CARD_PAY_TO433 = 0x07; //银行卡结账
    public static final int ALI_PAY__TO433 = 0x08; //支付宝结账
    public static final int WEIXIN_PAY_TO433 = 0x09; //微信结账

    public static final int TEMPLATEID_DELAY = 30000;



    public static final String WEATHER = "http://www.weather.com.cn/weather/101020100.shtml";
    public static final String NEWS = "http://news.baidu.com/";
    public static final String HAPPY = "http://www.dianping.com/";
    public static final String SHOP = "https://www.tmall.com/?ali_trackid=2:mm_26632325_6860397_24008452:1453968939_252_1805956268";
    public static final String CONCESSIONS = "https://ju.taobao.com/?ali_trackid=2:mm_26866744_2384196_21258512:1453969006_2k1_86931745";


    /**
     * URL Field
     */
    public static final String DEFAULT_WEB_SOCKET_HOST = "ws://192.168.1.128:8080";
    public static final String WEB_SOCKET_ADDRESS = "/cleverm/sockjs/simplepush";
    public static final String WEB_SOCKET_SERVER = DEFAULT_WEB_SOCKET_HOST
            + WEB_SOCKET_ADDRESS;
    public static final String SERVER_IP = "";
    public static final String SERVER_PORT = "8080";
    public static final String HTTP_SERVER_ROOT = "http://1232321";
    /**
     * Service Key
     */
    public static final String KEY_TOKEN = "token";
    public static final String KEY_DOWNLOAD_URL = "url";
    public static final String KEY_SEQUENCE_ID = "sequence_id";
    public static final String KEY_HEART_BEAT = "key-heart-beat";
    /**
     * Client Key
     */
    public static final String KEY_SOCKET_SERVER_HOST = "key-socket-server-host";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String KEY_MESSAGE = "key-message";
    public static final String KEY_UPDATE_PACKAGE_PATH = "key-package-path";
    public static final String KEY_CUSTOMER_NUMBER = "key-customer-number";
    public static final String KEY_TABLE_TYPE = "key-table-type";
    /**
     * Service Action
     */
    public static final String ACTION_TABLE_INFO_UPDATED = "com.clevermodel.communication"
            + ".INFO_UPDATED";
    public static final String ACTION_SEND_MESSAGE = "com.clevermodel.communication.SEND_MESSAGE";
    public static final String ACTION_UPDATE_PACKAGE = "com.clevermodel.communication.UPDATE_PACKAGE";
    public static final String ACTION_CANCEL_OPERATION = "com.clevermodel.communication.CANCEL_OPERATION";
    public static final String ACTION_UPDATE_PACKAGE_READY = "com.clevermodel.communication.PACKAGE_READY";
    public static final String ACTION_CONNECT_SOCKET = "com.clevermodel.communication.CONNECT_SOCKET";
    public static final String ACTION_DISCONNECT_SOCKET = "com.clevermodel.communication.DISCONNECT_SOCKET";
    public static final String ACTION_UPLOAD_LOGGING = "com.clevermodel.communication.UPLOAD_LOGGING";
    public static final String ACTION_INIT_APP = "com.clevermodel.communication.INIT_APP";
    public static final String ACTION_RESET_DESK = "com.clevermodel.RESET_DESK";
    public static final String ACTION_RESET_TABLE = "com.clevermodel.RESET_TABLE";
    public static final String ACTION_RESET_QUEUE = "com.clevermodel.RESET_QUEUE";
    public static final String ACTION_NOTIFY_REMOTE_INIT = "com.clevermodel.service.NOTIFY_INIT";
    public static final String ACTION_NOTIFY_REMOTE_UPDATE_NUMBER = "com.clevermodel.service.NOTIFY_UPDATE_NUMBER";
    public static final String ACTION_NOTIFY_REMOTE_UPDATE_TABLE = "com.clevermodel.service.NOTIFY_UPDATE_TABLE";
    public static final String ACTION_SUPERSONIC_SWITCH = "com.clevermodel.settings.supersonic.UPDATE_SUPERSONIC_SWITCH";
    public static final String ACTION_UPDATE_CONFIG = "com.clevermodel.settings.rf433.UPDATE_CONFIG";

    public static final int ONE_SECOND = 1000;
    public static final int ONE_MINUTE = 60 * ONE_SECOND;
    public static final int HALF_HOUR = ONE_MINUTE * 30;

    /**
     * Preference Key
     */
    public static final String KEY_RESTAURANT_NAME = "restaurant_name";
    public static final String KEY_RESTAURANT_ID = "key-restaurant-id";
    public static final String KEY_CLIENT_ID = "key-client-id";
    public static final String KEY_TABLE_TYPE_MD5 = "key-table_type_md5";
    public static final String KEY_BT_ADDRESS = "bt_address";
    public static final String KEY_QRCODE = "key-qrcode";

    public static final String KEY_SERVER_IP = "server_ip";
    public static final String KEY_SERVER_PORT = "server_port";

    public static final String KEY_NUM_SWITCH = "num_switch";
    public static final String KEY_BT_PRINTER_ENABLE = "bt_printer_enable";

    public static final String KEY_CALLER_OUTPUT = "key-caller-output";
    public static final String KEY_CALLER_REPEAT = "key-caller-repeat";

    public static final String KEY_BONUS_ENABLE_DURATION = "key-bonus-enable-duration";
    public static final String KEY_SECOND_BONUS_ENABLE_DURATION = "key-second-bonus-enable-duration";
    public static final String KEY_BONUS_PRIZE = "key-bonus-prize";

    public static final String KEY_FIRST_BONUS_THRESHOLD = "key-first-bonus-threshold";
    public static final String KEY_SECOND_BONUS_THRESHOLD = "key-second-bonus-threshold";
    public static final String KEY_THIRD_BONUS_THRESHOLD = "key-third-bonus-threshold";

    public static final String KEY_SWITCH = "key-switch";
    public static final String KEY_PROX_1 = "key-prox-timechecker1";
    public static final String KEY_PROX_2 = "key-prox-timechecker2";
    public static final String KEY_PROX_TIME_01_START_HOUR = "key-prox-time-01-start-hour";
    public static final String KEY_PROX_TIME_01_START_MINUTE = "key-prox-time-01-start-minute";
    public static final String KEY_PROX_TIME_01_END_HOUR = "key-prox-time-01-end-hour";
    public static final String KEY_PROX_TIME_01_END_MINUTE = "key-prox-time-01-end-minute";
    public static final String KEY_PROX_TIME_02_START_HOUR = "key-prox-time-02-start-hour";
    public static final String KEY_PROX_TIME_02_START_MINUTE = "key-prox-time-02-start-minute";
    public static final String KEY_PROX_TIME_02_END_HOUR = "key-prox-time-02-end-hour";
    public static final String KEY_PROX_TIME_02_END_MINUTE = "key-prox-time-02-end-minute";
    public static final String[] KEY_PROX_SWITCH = new String[]{KEY_PROX_1,
            KEY_PROX_2};
    public static final String[] KEY_PROX_TIME_START_HOUR = new String[]{
            KEY_PROX_TIME_01_START_HOUR, KEY_PROX_TIME_02_START_HOUR};
    public static final String[] KEY_PROX_TIME_START_MINUTE = new String[]{
            KEY_PROX_TIME_01_START_MINUTE, KEY_PROX_TIME_02_START_MINUTE};
    public static final String[] KEY_PROX_TIME_END_HOUR = new String[]{
            KEY_PROX_TIME_01_END_HOUR, KEY_PROX_TIME_02_END_HOUR};
    public static final String[] KEY_PROX_TIME_END_MINUTE = new String[]{
            KEY_PROX_TIME_01_END_MINUTE, KEY_PROX_TIME_02_END_MINUTE};
    public static final String KEY_WELCOME_PERIOD_SWITCH_1 = "key-welcome-switch1";
    public static final String KEY_WELCOME_PERIOD_SWITCH_2 = "key-welcome-switch2";
    public static final String KEY_WELCOME_PERIOD_SWITCH_3 = "key-welcome-switch3";
    public static final String[] KEY_WELCOME_SWITCH = new String[]{
            KEY_WELCOME_PERIOD_SWITCH_1, KEY_WELCOME_PERIOD_SWITCH_2,
            KEY_WELCOME_PERIOD_SWITCH_3};
    public static final String KEY_WELCOME_PERIOD_01_START_HOUR = "key-welcome-01-start-hour";
    public static final String KEY_WELCOME_PERIOD_01_START_MINUTE = "key-welcome-01-start-minute";
    public static final String KEY_WELCOME_PERIOD_01_END_HOUR = "key-welcome-01-end-hour";
    public static final String KEY_WELCOME_PERIOD_01_END_MINUTE = "key-welcome-01-end-minute";
    public static final String KEY_WELCOME_PERIOD_02_START_HOUR = "key-welcome-02-start-hour";
    public static final String KEY_WELCOME_PERIOD_02_START_MINUTE = "key-welcome-02-start-minute";
    public static final String KEY_WELCOME_PERIOD_02_END_HOUR = "key-welcome-02-end-hour";
    public static final String KEY_WELCOME_PERIOD_02_END_MINUTE = "key-welcome-02-end-minute";
    public static final String KEY_WELCOME_PERIOD_03_START_HOUR = "key-welcome-03-start-hour";
    public static final String KEY_WELCOME_PERIOD_03_START_MINUTE = "key-welcome-03-start-minute";
    public static final String KEY_WELCOME_PERIOD_03_END_HOUR = "key-welcome-03-end-hour";
    public static final String KEY_WELCOME_PERIOD_03_END_MINUTE = "key-welcome-03-end-minute";
    public static final String[] KEY_WELCOME_PERIOD_START_HOUR = new String[]{
            KEY_WELCOME_PERIOD_01_START_HOUR, KEY_WELCOME_PERIOD_02_START_HOUR,
            KEY_WELCOME_PERIOD_03_START_HOUR};
    public static final String[] KEY_WELCOME_PERIOD_START_MINUTE = new String[]{
            KEY_WELCOME_PERIOD_01_START_MINUTE,
            KEY_WELCOME_PERIOD_02_START_MINUTE,
            KEY_WELCOME_PERIOD_03_START_MINUTE};
    public static final String[] KEY_WELCOME_PERIOD_END_HOUR = new String[]{
            KEY_WELCOME_PERIOD_01_END_HOUR, KEY_WELCOME_PERIOD_02_END_HOUR,
            KEY_WELCOME_PERIOD_03_END_HOUR};
    public static final String[] KEY_WELCOME_PERIOD_END_MINUTE = new String[]{
            KEY_WELCOME_PERIOD_01_END_MINUTE, KEY_WELCOME_PERIOD_02_END_MINUTE,
            KEY_WELCOME_PERIOD_03_END_MINUTE};
    public static final String KEY_WELCOME_PATH_01 = "key-welcome-path-01";
    public static final String KEY_WELCOME_PATH_02 = "key-welcome-path-02";
    public static final String KEY_WELCOME_PATH_03 = "key-welcome-path-03";
    public static final String[] KEY_WELCOME_PATHS = new String[]{
            KEY_WELCOME_PATH_01, KEY_WELCOME_PATH_02, KEY_WELCOME_PATH_03};

    public static final String KEY_BONUS_SWITCH = "key-bonus-switch";

    /**
     * path
     */
    public static final String RESOURCEPATH = new File(
            Environment.getExternalStorageDirectory().getAbsolutePath(), "cleverm").getAbsolutePath();

    public static final String LOCAL_PROFILE_PATH = RESOURCEPATH + "/local_profile.cfg";

    public static final String PROP_PRODUCT_MODEL = "ro.product.model";
    public static final String PRODUCT_MODEL_CLEVER_MODEL = "CleverModel";
    public static final String KEY_IS_TEST_VERSION = "is_test_version";
    public static final String KEY_RESTAURANT_COMMENTS = "restaurant_comments";

    /**
     * TV
     */
    public static final String TAG_TV = "tv";
    public static final String NOTICE_TYPE_QUEUE_INFO = "QUEUE_INFO";
    public static final String NOTICE_TYPE_REPAST_INFO = "REPAST_INFO";
    public static final String NOTICE_TYPE_LOTTERY_INFO = "LOTTERY_INFO";


    /**
     * Discount
     */
    public static final String DEFALUT_ORGID = "100";
    public static final String MEMORY_PLAY_KEY = "MEMORY_PLAY_KEY";
    public static final String MEMORY_PLAY_VIDEO_URI_KEY = "MEMORY_PLAY_VIDEO_URI_KEY";
    public static final String BROADCAST_RESATRT_EVENT = "BROADCAST_RESATRT_EVENT";


    /**
     * Demo video
     */
    public static final String DEMO_VIDEO_URL=Constant.DDP_URL+"/api/api/v10/video/get.do";
    public static final String DEMO_VIDEO_ID="159";
    public static final String DEMO_VIDEO_TYPE="1";


    /**
     * ActivityFlag
     */
    public static  String NEW_FLAG="default";

    public static int VIDEO_WANGWANG_ID=235;
    public static int VIDEO_GM_CRUZE_ID=320; //302;311
    public static int VIDEO_JJ_GAME_ID=319;

    public static final String NEED_FOOD = "点餐加菜 ";
    public static final String NEED_TEA ="添加茶水 ";
    public static final String NEED_PAPER = "湿巾纸巾";
    public static final String NEED_PAY = "呼叫结账";
    public static final String NEED_OTHER = "其他服务";
    public static final String NEED_PAY_CASH = "现金结账";
    public static final String NEED_PAY_CARD = "银行卡结账";
    public static final String NEED_PAY_ZHIFUBAO= "支付宝结账";
    public static final String NEED_PAY_WEIXIN = "微信结账";
    public static final String NEED_SOUP = "火锅加汤";
    public static final String NEED_DISHWARE = "更换餐具";
    public static final String NEED_PAN = "更换烤盘";
    public static final String NEED_TABLEWARE = "收拾桌面";
    public static final String NEED_HURRY = "催菜";
    public static final String NEED_CHARCOAL= "加炭加火";
    public static final String NEED_TOOTHPICK = "纸巾牙签";

    //add by Randy for check if String is number
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }


}


