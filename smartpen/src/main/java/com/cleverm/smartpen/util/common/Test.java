package com.cleverm.smartpen.util.common;


/**
 * Created by xiong,An android project Engineer,on 31/8/2016.
 * Data:31/8/2016  上午 11:56
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public enum Test {

    INSTANCE;

    public   interface  CallBack {
        void  execute();
    }


    public  String consuming(CallBack callback){
        long begin = System.currentTimeMillis();
        callback.execute();
        long end = System.currentTimeMillis();
        return String.valueOf(end - begin);
    }

    public String doMethod(){
        return consuming(new CallBack() {
            @Override
            public void execute() {
                doYangHui();
            }
        });
    }

    /**
     * 19秒
     */
    private void doYangHui(){
        int triangle[][]=new int[1000][];// 创建二维数组
        // 遍历二维数组的第一层
        for (int i = 0; i < triangle.length; i++) {
            triangle[i]=new int[i+1];// 初始化第二层数组的大小
            // 遍历第二层数组
            for(int j=0;j<=i;j++){
                // 将两侧的数组元素赋值为1
                if(i==0||j==0||j==i){
                    triangle[i][j]=1;
                }else{// 其他数值通过公式计算
                    triangle[i][j]=triangle[i-1][j]+triangle[i-1][j-1];
                }
                System.out.print(triangle[i][j]+"\t");         // 输出数组元素
            }
            System.out.println();               //换行
        }
    }


}
