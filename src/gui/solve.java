package gui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class solve
{
    //----------------------------global variables------------------------------------------------------------//
    public int number_of_grammers;
    public char append = 'Z'; //for adding pseudo character if left_recursion is there
    public int k=0,k1=0; //k and k1 no of grammers after removing left recursion and factoring respectively
    public ArrayList<ArrayList<String>> list_of_grammers = new ArrayList<ArrayList<String>>(number_of_grammers);
    public ArrayList<ArrayList<String>> left_recursion_removed_grammer = new ArrayList<ArrayList<String>>(100);
    public ArrayList<ArrayList<String>> left_factoring_removed_grammer = new ArrayList<ArrayList<String>>(100);
    public ArrayList<ArrayList<String>> first_table = new ArrayList<ArrayList<String>>(100);
    public ArrayList<ArrayList<String>> follow_table = new ArrayList<ArrayList<String>>(100);
    String parseTable[][] = new String[20][20];
    int parse_flag=0;
    Map<String ,Integer> m1 = new HashMap<String,Integer>(); //using hasmap for mapping non_terminals with there order of apperance
    find f = new find();
    //------------------------------------------------------------------------------------------------------//


    //------initializing readers----------------------------//
    Scanner sc = new Scanner(System.in);
    InputStreamReader r=new InputStreamReader(System.in);
    BufferedReader br = new BufferedReader(r);
    //------------------------------------------------------//



    //------------fuction to read grammer--------------------//
    public  solve(String g[],int n)
    {
        try
        {
            number_of_grammers = n;

            for(int i=0; i<number_of_grammers; i++)
                list_of_grammers.add(new ArrayList<String>(20));


            for(int i=0;i<number_of_grammers;i++)
            {
                String production = g[i];
                int k=0;

                for (String retval: production.split("->"))
                {
                    if(k==1)
                    {
                        production = retval;
                    }
                    else
                    {
                        list_of_grammers.get(i).add(retval);
                        k++;
                    }

                }
                for (String retval: production.split("/"))
                    list_of_grammers.get(i).add(retval);
            }

        }catch(Exception e){}
    }

    //-------------------------------------------------------------------------------------//





    //-----------------------function to remove left recursion-----------------------------//
    public int remove_left_recursion()
    {
        for(int i=0;i<number_of_grammers;i++)
        {
            String ax = list_of_grammers.get(i).get(0);
            char bx = ax.charAt(0);
            int left_recursion=0;
            int length = list_of_grammers.get(i).size();
            Vector<Integer> vec = new Vector<Integer>(4);

            //looping to find which all grammmers A->aB has left recursion and storing index of aB in vec at which left_recursion is there
            for(int j=1;j<length;j++)
            {
                if(bx==list_of_grammers.get(i).get(j).charAt(0))
                {
                    left_recursion=1;
                    vec.add(j);
                }
            }

            //if that production doesnt has left_recursion simply add that production as it is in left_recursion_removed_grammer
            if(left_recursion==0)
            {
                left_recursion_removed_grammer.add(new ArrayList<String>(20));
                for(int j=0;j<length;j++)
                {
                    left_recursion_removed_grammer.get(k).add(list_of_grammers.get(i).get(j));
                }
                k++;
            }

            //else we need to make two rows for storing two new production and here we are assuming E0 as #
            else
            {
                left_recursion_removed_grammer.add(new ArrayList<String>(20));
                left_recursion_removed_grammer.add(new ArrayList<String>(20));

                left_recursion_removed_grammer.get(k).add(ax);
                String append1 = String.valueOf(append);
                left_recursion_removed_grammer.get(k+1).add(append1);

                for(int j=1;j<length;j++)
                {

                    if(vec.contains(j))
                    {
                        String to_add = list_of_grammers.get(i).get(j);
                        to_add+=append1;

                        String to_add1 ="";
                        for(int i1=1;i1<to_add.length();i1++)
                            to_add1+=to_add.charAt(i1);

                        left_recursion_removed_grammer.get(k+1).add(to_add1);
                    }
                    else
                    {
                        left_recursion_removed_grammer.get(k).add(list_of_grammers.get(i).get(j)+append1);
                    }
                }

                left_recursion_removed_grammer.get(k+1).add("#");
                append--;
                k+=2;
            }
        }


        f.display(k,left_recursion_removed_grammer);
        return k;
    }
    //-----------------------------------------------------------------------------------------//





    //---------------------function to remove left factoring----------------------------------//
    public int remove_left_factoring()
    {

        for(int i=0;i<k;i++)
        {

            int length = left_recursion_removed_grammer.get(i).size();
            int left_factoring=0;

            int[][] matrix = new int[length][length];

            for(int p=0;p<length;p++)
                for(int q=0;q<length;q++)
                    matrix[p][q]=0;

            //intializing matrix with matrix[i][j] having length of same prefix in i and j
            for(int p=1;p<length;p++)
            {
                for(int q = p+1;q<length;q++)
                {
                    int ll = f.find_length(left_recursion_removed_grammer.get(i).get(p),left_recursion_removed_grammer.get(i).get(q));
                    if(ll>0)
                    {
                        left_factoring=1;
                    }
                    matrix[p][q]=ll;
                }
            }

            //no left factoring simply push the ith grammer
            if(left_factoring==0)
            {
                left_factoring_removed_grammer.add(new ArrayList<String>(20));
                for(int j=0;j<length;j++)
                {
                    left_factoring_removed_grammer.get(k1).add(left_recursion_removed_grammer.get(i).get(j));
                }
                k1++;
            }
            else
            {
                left_factoring_removed_grammer.add(new ArrayList<String>(20));
                left_factoring_removed_grammer.add(new ArrayList<String>(20));

                left_factoring_removed_grammer.get(k1).add(left_recursion_removed_grammer.get(i).get(0));
                String append1 = String.valueOf(append);
                left_factoring_removed_grammer.get(k1+1).add(append1);


                for(int p=1;p<length;p++)
                {

                    int cu=0;
                    int min_length=99999;
                    int parity=0;

                    //finding min_length of prefix common in most of the production
                    for(int q=p+1;q<length;q++)
                    {
                        if(matrix[p][q]>0)
                        {
                            cu+=matrix[p][q];
                            min_length = Math.min(min_length,matrix[p][q]);
                        }
                        if(matrix[p][q]<0)p=1;
                    }

                    //no left factoring in particular production of ith grammer
                    if(cu==0 && p==0)
                    {
                        left_factoring_removed_grammer.get(k1).add(left_recursion_removed_grammer.get(i).get(p));
                    }
                    else if(cu>0)
                    {
                        //finding prefix value
                        String prefix="";
                        String ax = left_recursion_removed_grammer.get(i).get(p);

                        for(int j=0;j<min_length;j++)
                            prefix+=(ax.charAt(j));
                        String prefix1=prefix;
                        prefix+=append1;

                        //pushing the prefix as one of the production in ith grammer
                        left_factoring_removed_grammer.get(k1).add(prefix);

                        //finding the remaining part of production which has to be pushed in i+1 th production
                        for(int q =1;q<length;q++)
                        {
                            if(left_recursion_removed_grammer.get(i).get(q).length()>0)
                            {
                                String axx = left_recursion_removed_grammer.get(i).get(q);
                                if(axx.startsWith(prefix1))
                                {
                                    String bxx="";
                                    int l = axx.length();
                                    if(!(l==min_length))
                                    {
                                        for(int j=min_length;j<l;j++)
                                            bxx+=(axx.charAt(j));
                                        left_factoring_removed_grammer.get(k1+1).add(bxx);
                                    }
                                    else
                                    {
                                        left_factoring_removed_grammer.get(k1+1).add("#");
                                    }
                                }
                                else
                                {
                                    left_factoring_removed_grammer.get(k1).add(axx);
                                }
                            }
                        }
                    }
                }

                append--;
                k1+=2;
            }

        }

        f.display(k1,left_factoring_removed_grammer);
        return k1;
    }
    //-----------------------------------------------------------------------------------------//





    //-- ------------function to calculate first----------------------------------------------//
    public int first()
    {
        for(int i=0;i<k1;i++)
        {
            Integer ii = new Integer(i);
            m1.put(left_factoring_removed_grammer.get(i).get(0),ii);
        }


        for(int i=0;i<k1;i++)
        {
            first_table.add(new ArrayList<String>(20));
            int l = left_factoring_removed_grammer.get(i).size();
            first_table.get(i).add(left_factoring_removed_grammer.get(i).get(0));

            for(int j=1;j<l;j++)
            {
                String ax = left_factoring_removed_grammer.get(i).get(j);
                if(!(ax.charAt(0)>='A' && ax.charAt(0)<='Z'))
                {
                    first_table.get(i).add(String.valueOf(ax.charAt(0)));
                }
                else
                {
                    boolean flag=true;
                    int count1=0;
                    while(flag)
                    {
                        int jj=0;
                        if(m1.get(String.valueOf(ax.charAt(count1)))!=null)
                        {
                            jj = m1.get(String.valueOf(ax.charAt(count1)));
                            ArrayList<String>ans = new ArrayList<String>(f.find_first(jj,left_factoring_removed_grammer,m1));

                            int f1=0;
                            int ll = ans.size();
                            for(int k=0;k<ll;k++)
                            {
                                if(ans.get(k).equals("#"))
                                {
                                    f1=1;
                                    continue;
                                }
                                first_table.get(i).add(ans.get(k));
                            }

                            if(f1==0)break;
                            count1++;
                        }
                        else
                        {
                            first_table.get(i).add(String.valueOf(ax.charAt(count1)));
                            break;
                        }
                    }
                }
            }
        }

        f.display(k1,first_table);
        return  k1;
    }
    //----------------------------------------------------------------------------------------------------//




    //------------------------function to calculate follow------------------------------------------------//
    public int follow()
    {
        follow_calculate fc1 = new follow_calculate();
        for(int i=0;i<k1;i++)
        {
            follow_table.add(new ArrayList<String>(20));
            follow_table.get(i).add(left_factoring_removed_grammer.get(i).get(0));
            if(i==0)
            {
                follow_table.get(i).add("$");
            }

            ArrayList<String>ans = new ArrayList<String>(f.find_follow(follow_table, k1,follow_table.get(i).get(0),left_factoring_removed_grammer,first_table,m1));
            int l = ans.size();
            for(int j=0;j<l;j++)
                if(fc1.look(follow_table.get(i),ans.get(j)))
                    follow_table.get(i).add(ans.get(j));
        }

        f.display(k1,follow_table);
        return  k1;
    }
    //-----------------------------------------------------------------------------------------------------//





    //-----------------------------------------------------------------------------------------------------//
    public Map<String ,Integer> createTable()
    {
        ArrayList<ArrayList<String>> parser  = new ArrayList<ArrayList<String>>(100);
        ArrayList<String> terminals = new ArrayList<String>(10);
        Map<String ,Integer> m2 = new HashMap<String,Integer>();


        for(int i=0;i<10;i++)
            for(int j=0;j<10;j++)
                parseTable[i][j] = "-1";

        for(int i=0;i<k1;i++)
        {
            int len = left_factoring_removed_grammer.get(i).size();
            for(int j=1;j<len;j++)
            {
                String x = left_factoring_removed_grammer.get(i).get(j);
                if(x.equals("id"))
                    terminals.add("i");
                else{
                    for(int n = 0;n<x.length();n++)
                    {
                        if(!(x.charAt(n)>='A' && x.charAt(n)<='Z') && !terminals.contains(x))
                            terminals.add(String.valueOf(x.charAt(n)));
                    }
                }

            }
        }

        terminals.remove("#");
        terminals.add("$");

        for(int i=0;i<terminals.size();i++)
        {
            //System.out.println(terminals.get(i));
            Integer ii = new Integer(i);
            m2.put(terminals.get(i),ii);/// all terminals mapped in m2
        }

        for(int i=0; i<=m1.size(); i++)
            parser.add(new ArrayList<String>(20));

        for(int i = 0;i<=k1;i++){
            //System.out.println("size of m1 "+m1.size()+" m2 size"+ m2.size());
            //System.out.print(left_factoring_removed_grammer.get(i).get(0)+"  ");
            if(i==0)
                //parser.get(i).add("  ");
                parseTable[0][0] = "  ";
            else
                parseTable[i][0] = 	left_factoring_removed_grammer.get(i-1).get(0);
            //parser.get(i).add(String.valueOf(left_factoring_removed_grammer.get(i-1).get(0)));

        }
        System.out.print("\n");


        for(int j=1;j<=m2.size();j++)
            parseTable[0][j] = String.valueOf(terminals.get(j-1));
        String s1;
        String pdn;

        for(int i=0;i<left_factoring_removed_grammer.size();i++)
        {
            for (int j=1;j<left_factoring_removed_grammer.get(i).size();j++ )
            {
                s1 = new String(String.valueOf(left_factoring_removed_grammer.get(i).get(0)));
                pdn = new String(String.valueOf(left_factoring_removed_grammer.get(i).get(j)));
                //System.out.println(s1 + "->" + pdn);if(!(x.charAt(n)>='A' && x.charAt(n)<='Z')
                int l = m1.get(s1)+1;
                String in = new String(s1+"->"+pdn);

                if(!(pdn.charAt(0)>='A' && pdn.charAt(0)<='Z') && pdn.charAt(0)!='#')
                {    int k;
                    //String in = new String(s1+"->"+pdn);
                    //
                    //if(pdn.charAt(0)!='i')
                    k = m2.get(String.valueOf(pdn.charAt(0))) + 1;
                    //else
                    //	k = m2.get("i")+1;

                    if(parseTable[l][k]!="-1")parse_flag=1;
                    parseTable[l][k] = in;

                }// end of if 1,,where 1st i/p is terminal
                else if((pdn.charAt(0)>='A' && pdn.charAt(0)<='Z')  && pdn.charAt(0)!='#')
                {
                    int v=-1;
                    //System.out.println(first_table.get(m1.get(String.valueOf(pdn.charAt(0)))));
                    for(int l1=1;l1<first_table.get(m1.get(String.valueOf(pdn.charAt(0)))).size();l1++)
                    {

                        String axx = String.valueOf(first_table.get(m1.get(String.valueOf(pdn.charAt(0)))).get(l1));
                        //if(axx.equals("i"))
                        //		v = m2.get("i")+1;
                        //	else
                        //v = m2.get(first_table.get(m1.get(String.valueOf(pdn.charAt(0)))).get(l1))+1;
                        v= m2.get(axx)+1;
                        //System.out.println(m2.get(String.valueOf(first_table.get(m1.get(String.valueOf(pdn.charAt(0)))).get(l1))));
                        //v=0;
                        if(parseTable[l][v]!="-1")parse_flag=1;
                        parseTable[l][v] = in;
                        //System.out.println(axx);
                    }

                }//end of 2nd if where 1st char is Non terminal
                else if(pdn.charAt(0) == '#')
                {
                    int v1;
                    for(int l1 = 1;l1<follow_table.get(m1.get(String.valueOf(s1.charAt(0)))).size();l1++)
                    {
                        String ax2 = String.valueOf(follow_table.get(m1.get(String.valueOf(s1.charAt(0)))).get(l1));
                        //System.out.println(m2.get(ax2));
                        //if(ax2.equals("i"))
                        //	v1 = m2.get("i")  + 1;
                        //else
                        v1 = m2.get(ax2) + 1;
                        if(parseTable[l][v1]!="-1")parse_flag=1;
                        parseTable[l][v1] = in;

                    }

                }

            }

        }

        for(int i=0;i<=m1.size();i++)
        {
            for(int j=0;j<=m2.size();j++)
            {
                System.out.print(parseTable[i][j]+"     ");
            }
            System.out.print("\n");
        }

        System.out.println(parse_flag);
        return m2;
    }



}