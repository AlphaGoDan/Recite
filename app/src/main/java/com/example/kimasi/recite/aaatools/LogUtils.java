package com.example.kimasi.recite.aaatools;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

//  import com.config.AppConfig; //应该是存放自定义应用配制的包

/**
 * 日志打印工具类
 * Created by kimasi on 2016/3/21.
 */
public class LogUtils {
    /**
     * isWrite:用于开关是否吧日志写入txt文件中</p>
     */
    private static final boolean isWrite = false;//要改为false
    /**
     * isDebug :是用来控制，是否打印日志
     */
    private static final boolean isDeBug = true;
    /**
     * 存放日志文件的所在路径
     */
    private static final String DIRPATH = "/a日志/"; //AppConfig.LOG_DIRPATH;
    // private static final String DIRPATH = "/log";
    /**
     * 存放日志的文本名
     */
    private static final String LOGNAME ="合金弹头日志.txt";// AppConfig.LOG_FILENAME;
    // private static final String LOGNAME = "log.txt";
    /**
     * 设置时间的格式
     */
    private static final String INFORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * VERBOSE日志形式的标识符
     */
    public static final int VERBOSE = 5;
    /**
     * DEBUG日志形式的标识符
     */
    public static final int DEBUG = 4;
    /**
     * INFO日志形式的标识符
     */
    public static final int INFO = 3;
    /**
     * WARN日志形式的标识符
     */
    public static final int WARN = 2;
    /**
     * ERROR日志形式的标识符
     */
    public static final int ERROR = 1;

    private static Integer count=0;

    /**
     * 把异常用来输出日志的综合方法
     *
     *  @param tag 日志标识
     *  @param throwable 抛出的异常
     *  @param type 日志类型
     * @return void 返回类型
     * @throws
     */
    public static void log(String tag, Throwable throwable, int type) {
        log(tag, exToString(throwable), type);
    }

    /**
     * 用来输出日志的综合方法（文本内容）
     *
     * @param tag 日志标识
     * @param msg 要输出的内容
     * @param type 日志类型
     * @return void 返回类型
     * @throws
     */
    public static void log(String tag, String msg, int type) {
        switch (type) {
            case VERBOSE:
                v(tag, msg);// verbose等级
                break;
            case DEBUG:
                d(tag, msg);// debug等级
                break;
            case INFO:
                i(tag, msg);// info等级
                break;
            case WARN:
                w(tag, msg);// warn等级
                break;
            case ERROR:
                e(tag, msg);// error等级
                break;
            default:
                break;
        }
    }

    /**
     * verbose等级的日志输出
     *
     * @param tag
     *            日志标识
     * @param msg
     *            要输出的内容
     * @return void 返回类型
     * @throws
     */
    public static void v(String tag, String msg) {
        // 是否开启日志输出
        if (isDeBug) {
            Log.v(tag, msg);
        }
        // 是否将日志写入文件
        if (isWrite) {
            write(tag, msg);
        }
    }

    /**
     * debug等级的日志输出
     *
     * @param tag
     *            标识
     * @param msg
     *            内容
     * @return void 返回类型
     * @throws
     */
    public static void d(String tag, String msg) {
        if (isDeBug) {
            Log.d(tag, msg);
        }
        if (isWrite) {
            write(tag, msg);
        }
    }

    /**
     * info等级的日志输出
     *
     * @param  tag 标识
     * @param  msg 内容
     * @return void 返回类型
     * @throws
     */
    public static void i(String tag, String msg) {
        if (isDeBug) {
            Log.i(tag, msg);
        }
        if (isWrite) {
            write(tag, msg);
        }
    }

    /**
     * warn等级的日志输出
     *
     * @param tag 标识
     * @param msg 内容
     * @return void 返回类型
     * @throws
     */
    public static void w(String tag, String msg) {
        if (isDeBug) {
            Log.w(tag, msg);
        }
        if (isWrite) {
            write(tag, msg);
        }
    }

    /**
     * error等级的日志输出
     *
     * @param  tag 标识
     * @param  msg 内容
     * @return void 返回类型
     */
    public static void e(String tag, String msg) {
        if (isDeBug) {
            Log.w(tag, msg);
        }
        if (isWrite) {
            write(tag, msg);
        }
    }

    /**
     * 自定义的日志输出
     * @param tag
     * @param msg
     */
    public static void v2(String tag, String msg) {
        // 是否开启日志输出
        if (isDeBug) {
            Log.v("BBB",tag+"  |  "+msg);
        }
        // 是否将日志写入文件
        if (isWrite) {
            write("",countLog()+tag+"  |  "+msg);
        }
    }
    public static void v2(String tag) {
        // 是否开启日志输出
        if (isDeBug) {
            Log.v("BBB",tag);
        }
        // 是否将日志写入文件
        if (isWrite) {
            write("",tag);
        }
    }

    /**
     * 计数器
     * @return
     */
    public static String countLog(){
        return " "+(++count);
    }
    /**
     * 用于把日志内容写入制定的文件
     *
     * @param @param tag 标识
     * @param @param msg 要输出的内容
     * @return void 返回类型
     * @throws
     */
    public static void write(String tag, String msg) {
        String path = FileUtils.createMkdirsAndFiles(DIRPATH, LOGNAME);
    //    Log.v("=================","文件成功");
        if (TextUtils.isEmpty(path)) {
            return;
        }
        String log = DateFormat.format(INFORMAT, System.currentTimeMillis())// INFORMAT = "yyyy-MM-dd HH:mm:ss"
              //   +tag
                + "\n"
                + msg
                + "\n";//=================================分割线=================================";
        FileUtils.write2File(path, log, true);
    }

    /**
     * 用于把日志内容写入制定的文件
     *
     * @param ex
     *            异常
     */
    public static void write(Throwable ex) {
        write("", exToString(ex));
    }

    /**
     * 把异常信息转化为字符串
     *
     * @param ex 异常信息
     * @return 异常信息字符串
     */
    private static String exToString(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        printWriter.close();
        String result = writer.toString();
        return result;
    }
}