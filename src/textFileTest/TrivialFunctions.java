package textFileTest;

import com.csvreader.CsvWriter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Created by Administrator on 2017/12/5 0005.
 */
public class TrivialFunctions{

    private DatabaseConnection connection;  //连接数据库
    private CsvWriter writer;
    private LinkedList<String> studentsStr;
    private LinkedList<String> studentsBirthday;
    private LinkedList<String> studentID;
    private int[] studentID1;
    private LinkedList<Integer> year;
    private LinkedList<Integer> month;
    private LinkedList<Integer> day;
    private int[] year2;
    private int[] month2;
    private int[] day2;
    private String symbol; //指定学生籍贯为黑龙江的元组



    public TrivialFunctions() throws Exception
    {
        connection = new DatabaseConnection();
    }

    //将处理好的学生元组写入置path路径下的csv文件中
    public void WriteFromDatabaseToCsv(String path,String birth) throws Exception {

        studentsStr = connection.getStudentsStr("No");
        studentID = new LinkedList<>();

        year = new LinkedList<>();
        month = new LinkedList<>();
        day = new LinkedList<>();

        year2 = new int[100];
        month2 = new int[100];
        day2 = new int[100];

        studentID1 = new int[100];

        int k = 0;
        try {
            writer = new CsvWriter(path, ',', Charset.forName("GBK"));

            for (int i = 0; i < studentsStr.size(); i++) {

                //CsvWriter一个构造器的参数是(字符串，标识符，编码类型)
                String token[] = studentsStr.get(i).split(",");
                String ID = token[0];
                String name = token[1];
                String sex = token[2];
                String height = token[3];
                String birthday = token[4];

                String[] token1 = birthday.split("-");
                int year1 = Integer.parseInt(token1[0]);
                int month1 = Integer.parseInt(token1[1]);
                int day1 = Integer.parseInt(token1[2]);

                String birth_place = token[5];

                symbol = birth_place.substring(0,3);

                /**如果是黑龙江省的就将其存在一个新的字符串队列中，再来按照生日排序
                 *排序思路：因为链表不能交换，初始链表又无序，所以打算比较生日后，
                 *记录ID的顺序，最后再从数据库里读出，如果生日重复了，但是ID是唯一的，所以这么读可以
                 */

                 if(symbol.equals(birth))
                {
                    studentID.add(ID);
                    year.add(year1);   //先用链表存，待会再写到数组里，再去比较
                    month.add(month1);
                    day.add(day1);

                    System.out.println(year.get(k)+"-"+month.get(k)+"-"+day.get(k));
//                  writer.writeRecord(token);
                    k++;
                }
                //writer.endRecord();
            }

            //按生日排序
            for(int i = 0;i<studentID.size();i++)
            {
                studentID1[i] = Integer.parseInt(studentID.get(i));
                year2[i] = year.get(i);
                month2[i] = month.get(i);
                day2[i] = day.get(i);
            }


            for(int i = 0;i<studentID.size();i++)
            {
                System.out.println(studentID1[i]);
            }


            for(int i = 0;i<studentID.size()-1;i++)
            {
                for(int j=0;j<studentID.size()-i-1;j++)
                {
                    //注意ID在交换时日期也得交换，使ID与日期绑定
                    if(!sort(j,j+1))
                    {
                        int temp = studentID1[j];
                        studentID1[j] = studentID1[j+1];
                        studentID1[j+1] = temp;

                        int temp1 = year2[j];
                        year2[j] = year2[j+1];
                        year2[j+1] = temp1;

                        int temp2 = month2[j];
                        month2[j] = month2[j+1];
                        month2[j+1] = temp2;

                        int temp3 = day2[j];
                        day2[j] = day2[j+1];
                        day2[j+1] = temp3;
                    }
                }

                //打印测试ID按日期排序是否正确
//                for(int l =0;l<studentID.size();l++)
//                {
//                    System.out.print(studentID1[l]+":"+year2[l]+"-"+month2[l]+"-"+day2[l]+"   ");
//                }
//
//                System.out.println();
            }

            int length = studentID.size();

            studentsStr.clear();
            studentID.clear();

            studentsStr = new LinkedList<>();
            studentID = new LinkedList<>();

            for(int i = 0;i<length;i++)
            {
                studentID.add(String.valueOf(studentID1[i]));

                //ID按照生日排序后，从数据库里读出来就是按照生日排序的，再打印到csv文件中
                studentsStr = connection.getStudentsStr(studentID.get(i));

                String token2[] = studentsStr.get(0).split(",");

                writer.writeRecord(token2);
            }

            System.out.println("成功输入到csv文件中");
            writer.close();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean sort(int i,int j)
    {
        if(year2[i] < year2[j])   //get函数默认的object类型不能比较,对象的比较用equals函数
        {
            return true;
        }
        else if(year2[i] == year2[j])
        {
            if(month2[i] < month2[j])
            {
               return true;
            }
            else if(month2[i] == month2[j])
            {
                if(day2[i] < day2[j])
                {
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public static void main(String[] args) throws Exception{

    }


}
