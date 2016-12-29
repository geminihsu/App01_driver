package tw.com.geminihsu.app01.utils;

import android.content.Context;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by geminihsu on 26/12/2016.
 */

public class FormatUtils {

    public final Context mContext;

    public FormatUtils(Context context) {
        mContext = context;
    }

    public static boolean isBlankField(EditText etPersonData) {
        return etPersonData.getText().toString().trim().equals("");
    }


    /**
     * 判斷是否為身份證字號格式 (
     */
    public static boolean isIdNoFormat(String word) {
        boolean check = true;

        if(word.length()>10||word.length()<10)
            check = false;
        for(int i = 0 ; i < word.length();i++) {
            String s = word.substring(i,i+1);
            if(i==0) {
                if(!isEnglish(s)) {
                    check = false;
                    break;
                }
            } else {
                if(!isDigit(s)) {
                    check = false;
                    break;
                }
            }
        }

        return check;
    }
    /**
     * 英文判斷
     */
    public static boolean isEnglish(String word){
        boolean check = true;
        for(int i=0;i<word.length();i++) {
            int c = (int)word.charAt(i);
            if(c>=65 && c<=90 || c>=97 && c<=122) {
                //是英文
            } else {
                check = false;
            }
        }
        return check;
    }
    /**
     * 數字判斷
     */
    public static boolean isDigit(String word) {
        boolean check = true;
        for(int i=0;i<word.length();i++) {
            int c = (int)word.charAt(i);
            if(c>=48 && c<=57 ) {
                //是數字
            } else {
                check = false;
            }
        }
        return check;
    }

    /*检查字符串是否为电话号码的方法,并返回true or false的判断值*/
    public static boolean isPhoneNumberValid(String phoneNumber)
    {
        boolean isValid = false;
    /* 可接受的电话格式有:
     * ^\\(? : 可以使用 "(" 作为开头
     * (\\d{3}): 紧接着三个数字
     * \\)? : 可以使用")"接续
     * [- ]? : 在上述格式后可以使用具选择性的 "-".
     * (\\d{3}) : 再紧接着三个数字
     * [- ]? : 可以使用具选择性的 "-" 接续.
     * (\\d{5})$: 以五个数字结束.
     * 可以比较下列数字格式:
     * (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890
    */
        String expression =
                "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";

    /* 可接受的电话格式有:
     * ^\\(? : 可以使用 "(" 作为开头
     * (\\d{3}): 紧接着三个数字
     * \\)? : 可以使用")"接续
     * [- ]? : 在上述格式后可以使用具选择性的 "-".
     * (\\d{4}) : 再紧接着四个数字
     * [- ]? : 可以使用具选择性的 "-" 接续.
     * (\\d{4})$: 以四个数字结束.
     * 可以比较下列数字格式:
     * (02)3456-7890, 02-3456-7890, 0234567890, (02)-3456-7890
    */
        String expression2=
                "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";

        CharSequence inputStr = phoneNumber;
    /*创建Pattern*/
        Pattern pattern = Pattern.compile(expression);
    /*将Pattern 以参数传入Matcher作Regular expression*/
        Matcher matcher = pattern.matcher(inputStr);
    /*创建Pattern2*/
        Pattern pattern2 =Pattern.compile(expression2);
    /*将Pattern2 以参数传入Matcher2作Regular expression*/
        Matcher matcher2= pattern2.matcher(inputStr);
        if(matcher.matches()||matcher2.matches())
        {
            isValid = true;
        }
        return isValid;
    }

    /*检查密碼確認*/
    public static boolean isConfirmPassword(String password,String confirm)
    {
        boolean isValid = false;
        if(password.equals(confirm))
            isValid = true;
        return  isValid;
    }
}
