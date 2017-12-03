package textFileTest;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedList;

/**
 *    文件输入流类中读如信息，并逐一将数据合体成一个学生，最后返回一个学生对象
 * Created by Administrator on 2017/12/2 0002.
 */

public class FileEncode {

    private Reader in1,in2;
    private Student student;
    private LinkedList<Student> students;   //增加数据的链表
    private LinkedList<String> IDNumbers;
    private LinkedList<Student> studentsNew;   //修改数据的链表
    private String interval = ",";
    private int totalRow;
    private DatabaseConnection connection;
    private String tableName = "Student";
    private OutputStream outputStream;


    //默认实例化一个学生链表,删除学生数据链表，和数据库查询连接
    public FileEncode() throws Exception
    {
        students = new LinkedList<>();
        IDNumbers = new LinkedList<>();
        studentsNew = new LinkedList<>();
        connection = new DatabaseConnection();
    }

    public int getTotalRow()
    {
        return totalRow;
    }

    /**
     * 返回一个学生（记录）链表
     */
    public LinkedList getStudents()
    {
        return students;
    }

    public LinkedList getIDNumber()
    {
        return IDNumbers;
    }

    public LinkedList getStudentNew()
    {
        return studentsNew;
    }

    public void clearStudentsList()
    {
        students.clear();
        totalRow = 0;
    }

    public void clearIDNumberList()
    {
        IDNumbers.clear();
        totalRow = 0;
    }

    public void clearStudentsNewList()
    {
        studentsNew.clear();
        totalRow = 0;
    }


    /**
     *   批量读取文件内容
     */
     public void readDataInsertedFile(String fileName)
     {
         try {
             in1 = new FileReader(fileName);

             //BufferedReader可以读一行，readLine，而FileReader没有
             BufferedReader buffer = new BufferedReader(in1);

             String line = buffer.readLine();

             if (null != line) {
                 totalRow = 1;
             }

             while (null != line) {
                 //用正则表达式将字符串分割
                 String token[] = line.split(interval);

                 String ID = token[0];
                 String name = token[1];
                 String sex = token[2];
                 double height = Double.parseDouble(token[3]);

                 /** birthday需要再次解析，因为birthday是3个int类型合成的*/
                 String birthday = token[4];

                 String birth_place = token[5];

                 /** 解析birthday 4+2+2*/

                 int year = 0, month = 0, day = 0;

                 year = Integer.parseInt(birthday.substring(0, 4));
                 month = Integer.parseInt(birthday.substring(5, 7));
                 day = Integer.parseInt(birthday.substring(8, 10));

                 //实例化一个学生对象,依次装在学生链表中
                 student = new Student(ID, name, sex, height, year, month, day, birth_place);
                 students.add(student);

                 line = buffer.readLine();

                 if (null != line) {
                     totalRow++;
                 }
             } //流需要关闭吗？
         }catch(IOException e)
         {
             System.out.println("文件读入有误");
         }
     }


    /**
     * 读取删除文件
     * @param fileName
     */

     public void readDataDeletedFile(String fileName)
     {
         try {
             in1 = new FileReader(fileName);
             BufferedReader buffer = new BufferedReader(in1);

             String line = buffer.readLine();

             if(line!=null)
             {
                  totalRow = 1;
             }

             while(line!=null) {
                 String ID = line;

                 IDNumbers.add(ID);

                 line = buffer.readLine();
                 if (line != null) {
                     totalRow++;
                 }
             }

         }catch(IOException e)
         {
             System.out.println("文件读入有误");
         }
     }

    /**
     *     读取更新文件（具体思路：把更新文件中的ID先读出来，到数据库已有文件把更改文件中存在的ID的元组
     * 找出来，该元组代表一个学生，并通过学生的更改器方法，对学生进行更改）
     * @param fileName
     */

     public void readDataUpdatedFile(String fileName) throws IOException
     {

         //将修改后的数据写入文件中
         OutputStream os = null;

         os = new FileOutputStream("UltimateUpdated.txt");

         //用来添加字符串
         StringBuffer stringBuffer = new StringBuffer();
         StringBuffer stringBuffer1 = new StringBuffer();

         try {
             in1 = new FileReader(fileName);
             in2 = new FileReader(fileName);

             //BufferedReader可以读一行，readLine，而FileReader没有
             BufferedReader buffer1 = new BufferedReader(in1);
             BufferedReader buffer2 = new BufferedReader(in2);

             String line = buffer1.readLine();

             if (null != line) {
                 totalRow = 1;
             }

             //把修改的ID号写入文件中
             while (null != line) {
                 //用正则表达式将字符串分割
                 String token[] = line.split(interval);

                 String ID = token[0];

                 IDNumbers.add(ID);

                 String str = IDNumbers.get(totalRow-1);

                 line = buffer1.readLine();

                 stringBuffer.append(line+"\n");

                 if (null != line) {
                     totalRow++;
                 }
             }

             // 先读出，再读入
             os.write(stringBuffer.toString().getBytes());
             os.close();
             

             buffer1.close();
             in1.close();
             totalRow = 0;

             String line1 = buffer2.readLine();

             while (null != line1) {
                 //用正则表达式将字符串分割
                 String token[] = line1.split(interval);

                 String ID = token[0];

                 try {
                     readDatabase(tableName, IDNumbers);
                 }catch(Exception e)
                 {
                     System.out.println("数据库读入异常");
                 }

                 //匹配修改表中的每一行的ID号，对一些属性进行修改
                 for(int i = 0;i<IDNumbers.size();i++)
                 {
                     if(ID.equals(studentsNew.get(i).getID()))
                     {
                         String name = token[1];
                         String sex = token[2];
                         double height = Double.parseDouble(token[3]);

                         /** birthday需要再次解析，因为birthday是3个int类型合成的*/
                         String birthday = token[4];

                         String birth_place = token[5];

                         /** 解析birthday 4+2+2*/

                         int year = Integer.parseInt(birthday.substring(0, 4));
                         int month = Integer.parseInt(birthday.substring(5, 7));
                         int day = Integer.parseInt(birthday.substring(8, 10));

                         //如果更改的数据为空，就用之前的数据取代
                         if(name.isEmpty())
                         {
                             name = studentsNew.get(i).getName();
                         }
                         if(sex.isEmpty())
                         {
                             sex = studentsNew.get(i).getSex();
                         }
                         if(height==0)
                         {
                             height = studentsNew.get(i).getHeight();
                         }
                         if(birthday.isEmpty())
                         {
                             year = studentsNew.get(i).getYear();
                             month = studentsNew.get(i).getYear();
                             day = studentsNew.get(i).getYear();
                         }
                         if(birth_place.isEmpty())
                         {
                            birth_place = studentsNew.get(i).getBirth_place();
                         }

                         //实例化一个已经修改好信息的学生对象,依次装在学生链表中
                         student = new Student(ID, name, sex, height, year, month, day, birth_place);
                         studentsNew.add(student);

                         String str = studentsNew.get(i).getID()+','+studentsNew.get(i).getName()
                                 +','+studentsNew.get(i).getSex()+','+studentsNew.get(i).getHeight()
                                 +','+studentsNew.get(i).getBirthday()+','+studentsNew.get(i).getBirth_place();

                         stringBuffer.append(str+"\n");
                     }
                 }

                 line1 = buffer2.readLine();

                 if (null != line) {
                     totalRow++;
                 }

             } //流需要关闭吗？
         }catch(IOException e)
         {
             System.out.println("文件读入有误");
         }
         os.write(stringBuffer.toString().getBytes());
         os.close();
     }

     //读一个元组，返回一个学生
     public void readDatabase(String tablename, LinkedList<String> ID) throws Exception
     {
         //写出到相对位置的文件中
         OutputStream os = new FileOutputStream("Original_students.txt");

         //用来添加字符串
         StringBuffer stringBuffer = new StringBuffer();

         int length = ID.size();
         for (int i = 0; i < length; i++) {

             //批量产生sql语句
             String sql = "select * from " + tablename + " where ID='" + ID.get(i) + "'";

             PreparedStatement stat = connection.getConn().prepareStatement(sql);

             ResultSet rs = stat.executeQuery();

             while(rs.next())
             {
                 //得到每个元组属性的数据，并强置转换成student对象中的相应属性的类型
                 String id = rs.getString(1);
                 String name = rs.getString(2);
                 String sex = rs.getString(3);
                 double height = Double.parseDouble(rs.getString(4));
                 String birthday = rs.getString(5);
                 String birth_place = rs.getString(6);

                 int year = Integer.parseInt(birthday.substring(0, 4));
                 int month = Integer.parseInt(birthday.substring(5, 7));
                 int day = Integer.parseInt(birthday.substring(8, 10));

                 Student student = new Student(id, name, sex, height, year, month, day, birth_place);
                 studentsNew.add(student);

                 String str = studentsNew.get(i-1).getID()+','+studentsNew.get(i-1).getName()
                         +','+studentsNew.get(i-1).getSex()+','+studentsNew.get(i-1).getHeight()
                         +','+studentsNew.get(i-1).getYear() + '-'
                         + studentsNew.get(i-1).getMonth() + '-'
                         + studentsNew.get(i-1).getDay() +','
                         +studentsNew.get(i-1).getBirth_place();

                 stringBuffer.append(str+"\n");
             }

         }

         outputStream = new FileOutputStream("Original_students.txt");
         outputStream.write(stringBuffer.toString().getBytes());
         outputStream.close();
     }
}
