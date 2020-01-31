package util;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class Utils {
        
    public static String Tokenization(String str, String fact)
    {
        String res = "";
        int t=0;
        StringTokenizer token_result = new StringTokenizer(str, fact);

        while (token_result.hasMoreTokens()) 
        {
            res += token_result.nextToken() + "\n";
            t++;
        }
        
        return res;
    }
    
    public static boolean IsMatch(String re, String str)
    {
        return Pattern.matches(re.replace("+", "|"), str);
    }
    
}
