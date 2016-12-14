package com.cleverm.smartpen.util.excle;

import android.os.Environment;

import com.cleverm.smartpen.util.QuickUtils;
import com.cleverm.smartpen.util.StatisticsUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Created by xiong,An android project Engineer,on 2016/3/7.
 * Data:2016-03-07  15:46
 * Base on clever-m.com(JAVA Service)
 * Describe: 使用jxl-jar创建excel文件
 * Version:1.0
 * Open source
 */
public class CreateExcel {

    public final static  String EXPORT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/muyefile";
    public final static String UTF8_ENCODING = "UTF-8";


    //excle工作表的标题
    private WritableSheet sheet;
    //excle工作簿
    private WritableWorkbook wwb;
    //excle第一行-->9列
    private String[] colName = {"ID值","事件编号", "点击时间(秒)", "停留时间(秒)", "客户编号", "餐厅编号", "桌位编号", "描述备注", "二级事件编号", "统计时间"};


    public CreateExcel() {
    }

    public static WritableFont text14Font = null;
    public static WritableCellFormat text14Format = null;

    public static WritableFont text12Font = null;
    public static WritableCellFormat text12Format = null;

    public static WritableFont text10Font = null;
    public static WritableCellFormat text10Format = null;

    //制定表单的格式:字体大小，字体颜色等配置
    public static void format() {

        text14Font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
        text12Font = new WritableFont(WritableFont.ARIAL, 12);
        text10Font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

        try {
            //第一行的样式
            text14Font.setColour(Colour.LIGHT_BLUE);
            text14Format = new WritableCellFormat(text14Font);
            text14Format.setAlignment(Alignment.CENTRE);
            text14Format.setBorder(Border.ALL, BorderLineStyle.THIN);
            text14Format.setBackground(Colour.VERY_LIGHT_YELLOW);

            //第二行的样式
            text12Font.setColour(Colour.RED);
            text12Format = new WritableCellFormat(text12Font);
            text12Format.setBorder(Border.ALL, BorderLineStyle.THIN);

            //数据行的样式
            text10Font.setColour(Colour.GRAY_50);//字的颜色
            text10Format = new WritableCellFormat(text10Font);
            text10Format.setAlignment(Alignment.CENTRE);
            text10Format.setBorder(Border.ALL, BorderLineStyle.THIN);
            text10Format.setBackground(Colour.WHITE);//背景的颜色


        } catch (WriteException e) {
            e.printStackTrace();
        }

    }


    /**
     * 先写Excle第一行和第二行:
     * 第一行是:2016-03-27 统计报表
     * 第二行是:各列的文字说明
     * @return execlName
     */
    public String init() {

        String execlName = StatisticsUtil.getInstance().getEventHappenTime();

        //定义execl的格式
        format();
        try {
            File file = new File(EXPORT_PATH +File.separator+execlName+".xls");
            //先判断文件夹是否在
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            //再创建文件
            if (!file.exists()) {
                file.createNewFile();
            }
            wwb = Workbook.createWorkbook(file);
            //添加第一个工作表Sheet
            sheet = wwb.createSheet("" + StatisticsUtil.getInstance().getEventHappenTime(), 0);

            //定义一行单元格
            WritableCell writableCell_first_line = new Label(0, 0, execlName + "  统计报表", text14Format);
            sheet.addCell(writableCell_first_line);

            //定义第二行单元格
            for (int i = 0; i < colName.length; i++) {
                sheet.addCell(new Label(i, 0, colName[i], text12Format));
            }

            //写操作
            wwb.write();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //无论如何都关闭close
            if (wwb != null) {
                try {
                    wwb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
        }

        return execlName;
    }


    /**
     * 始for循环的一行一行的写Excle
     *
     * @param lists    ArrayList<ArrayList<String>>
     * @param <T>
     */
    public <T> void writeExcel(List<T> lists,String fileName) {

        //拼接FileName,Notice勿忘记--->“.xls”后缀
        String path = EXPORT_PATH +File.separator+fileName+".xls";

        WritableWorkbook writebook = null;
        InputStream in = null;

        try {

            if (lists != null && lists.size() > 0) {

                //设置为UTF-8
                WorkbookSettings workbookSettings = new WorkbookSettings();
                workbookSettings.setEncoding(UTF8_ENCODING);

                //写第三行就是去get而不是去create了
                in = new FileInputStream(new File(path));
                Workbook workbook = Workbook.getWorkbook(in);
                writebook = workbook.createWorkbook(new File(path), workbook);
                //获取第一个sheet
                WritableSheet sheet = writebook.getSheet(0);
                //遍历循环，开始将内存中的数据写入excle
                for (int i = 0; i < lists.size(); i++) {
                    ArrayList<String> inner_lists = (ArrayList<String>) lists.get(i);
                    for (int j = 0; j < inner_lists.size(); j++) {
                        String content = inner_lists.get(j);
                        //制定行与列
                        Label label = new Label(j, i + 1, content, text10Format);
                        sheet.addCell(label);
                    }
                }
                //写
                writebook.write();
                QuickUtils.log("写excle成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //必须关闭
            if (writebook != null) {
                try {
                    writebook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }

            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


}
