package gui;

import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

public class find
{
    //function to find min equal length----------------------------------------//
    public int find_length(String a, String b)
    {
        int length=Math.min(a.length(),b.length());
        int i=0;
        for(i=0;i<length;i++)
        {
            if(a.charAt(i)!=b.charAt(i))
                return (i);
        }
        return length;
    }
    //---------------------------------------------------------------------------//




    //fuction to find first for given Non_terminal in list-------------------------------//
    public ArrayList<String> find_first(int l , ArrayList<ArrayList<String>> left_factoring_removed_grammer,Map<String ,Integer> m1)
    {
        String start = left_factoring_removed_grammer.get(l).get(0);
        ArrayList<String> answer = new ArrayList<String>(10);
        int length = left_factoring_removed_grammer.get(l).size();

        for(int i=1;i<length;i++)
        {
            String ax = left_factoring_removed_grammer.get(l).get(i);
            if(!(ax.charAt(0)>='A' && ax.charAt(0)<='Z'))
                answer.add(String.valueOf(ax.charAt(0)));
            else
            {
                int l1 = m1.get(String.valueOf(ax.charAt(0)));
                ArrayList<String> ans = new ArrayList<String>(this.find_first(l1,left_factoring_removed_grammer,m1));
                int l2 = ans.size();
                for(int j=0;j<l2;j++)
                    answer.add(ans.get(j));
            }
        }
        return answer;
    }
    //---------------------------------------------------------------------------------------------//



    //function to find follow of given non_terminal----------------------------------------------------//
    public ArrayList<String> find_follow(ArrayList<ArrayList<String>> follow_table,int k1,String non_terminal , ArrayList<ArrayList<String>> left_factoring_removed_grammer,ArrayList<ArrayList<String>> first_table,Map<String ,Integer> m1)
    {
        ArrayList<String>ans = new ArrayList<String>(20);
        follow_calculate fc = new follow_calculate();


        for(int i=0;i<k1;i++)
        {

            int l = left_factoring_removed_grammer.get(i).size();
            for(int j=1;j<l;j++)
            {
                String ax = left_factoring_removed_grammer.get(i).get(j);
                Vector<Integer> vec = new Vector<Integer>(4);
                int l1 = ax.length();

                for(int k=0;k<l1;k++)
                    if(non_terminal.equals(String.valueOf(ax.charAt(k))))
                        vec.add(k);

                if(vec.size()==0)continue;

                for(int k=0;k<vec.size();k++)
                {
                    if(vec.get(k)==l1-1)
                    {
                        if(String.valueOf(ax.charAt(l1-1)).equals(left_factoring_removed_grammer.get(i).get(0)))continue;

                        ArrayList<String> answer;

                        if(fc.check(follow_table,left_factoring_removed_grammer.get(i).get(0)))
                            answer = new ArrayList<String>(fc.similar(follow_table,left_factoring_removed_grammer.get(i).get(0)));

                        else
                            answer =new ArrayList<String> (this.find_follow(follow_table, k1,left_factoring_removed_grammer.get(i).get(0),left_factoring_removed_grammer,first_table,m1));

                        int ll = answer.size();

                        for(int p=0;p<ll;p++)
                        {
                            if(fc.look(ans,answer.get(p)))
                                ans.add(answer.get(p));
                        }
                    }
                    else
                    {
                        int cu=1;
                        while(true)
                        {
                            if((vec.get(k)+cu)>=ax.length())break;

                            int jj = (vec.get(k)+(cu++));
                            if(vec.contains(jj))break;

                            char ax1 = ax.charAt(jj);
                            if(!(ax1>='A' && ax1<='Z'))
                            {
                                if(fc.look(ans,String.valueOf(ax1)))
                                    ans.add(String.valueOf(ax1));
                                break;
                            }

                            else
                            {
                                int jj1 = m1.get(String.valueOf(ax1));
                                int length = first_table.get(jj1).size();
                                int flag=0;
                                for(int p=1;p<length;p++)
                                {
                                    if(first_table.get(jj1).get(p).equals("#"))
                                    {
                                        flag=1;
                                        continue;
                                    }
                                    if(fc.look(ans,first_table.get(jj1).get(p)))
                                        ans.add(first_table.get(jj1).get(p));
                                }
                                if(flag==0)break;

                                if(jj==l1-1  && flag==1)
                                {
                                    if(String.valueOf(ax.charAt(l1-1)).equals(left_factoring_removed_grammer.get(i).get(0)))continue;
                                    ArrayList<String> answer;

                                    if(fc.check(follow_table,left_factoring_removed_grammer.get(i).get(0)))
                                        answer = new ArrayList<String>(fc.similar(follow_table,left_factoring_removed_grammer.get(i).get(0)));

                                    else
                                        answer =new ArrayList<String> (this.find_follow(follow_table, k1,left_factoring_removed_grammer.get(i).get(0),left_factoring_removed_grammer,first_table,m1));

                                    int ll = answer.size();

                                    for(int p=0;p<ll;p++)
                                    {
                                        if(fc.look(ans,answer.get(p)))
                                            ans.add(answer.get(p));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ans;
    }
    //----------------------------------------------------------------------------------------------------//



    //function to display --------------------------------------------------------------------------------//
    public void display(int length , ArrayList<ArrayList<String>> list )
    {
        System.out.print("\n");
        System.out.print("\n");
        System.out.print("\n");

        for(int i=0;i<length;i++)
        {
            for(int j=0;j<list.get(i).size();j++)
            {
                System.out.print(list.get(i).get(j)+" ");
            }
            System.out.print("\n");
        }
    }
    //--------------------------------------------------------------------------------------------------//

}