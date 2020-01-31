package gui;

import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Shubham on 4/25/2016.
 */
public class follow_calculate {
    //to check if adding_follow is already present in answer array
    public boolean look(ArrayList<String>ans,String key)
    {
        int l = ans.size();
        for(int i=0;i<l;i++)
        {
            if(ans.get(i).equals(key))
                return false;
        }
        return true;
    }

    public ArrayList<String> similar(ArrayList<ArrayList<String>> follow_table , String to_check)
    {
        ArrayList<String> ans = new ArrayList<String>(20);

        int l = follow_table.size();
        for(int i=0;i<l;i++)
        {
            if(follow_table.get(i).get(0).equals(to_check))
            {
                int ll = follow_table.get(i).size();
                for(int j=1;j<ll;j++)
                    ans.add(follow_table.get(i).get(j));
                break;
            }
        }

        return ans;
    }

    static public boolean check(ArrayList<ArrayList<String>> follow_table , String to_check)
    {
        int l = follow_table.size();
        for(int i=0;i<l;i++)
        {
            if(follow_table.get(i).get(0).equals(to_check))
                return true;
        }
        return false;
    }

}

