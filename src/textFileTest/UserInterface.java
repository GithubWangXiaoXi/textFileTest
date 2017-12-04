package textFileTest;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 *   用户可以对数据库内容进行建表，对数据增删改查
 * Created by Administrator on 2017/12/1 0001.
 */
public class UserInterface {

    private DatabaseConnection connection;
    private FileEncode fileEncode;
    private Student student;
    private String tableName = "student";  //tablename会贯穿与增删改查中，又因为增删改查每个功能独立，所以将其定为一般属性
    public static boolean isSuccess = false;
    public static boolean addflag = false;
    public static boolean deleteflag = false;
    public static boolean updateflag = false;

    public UserInterface() throws Exception
    {
        connection = new DatabaseConnection();
        fileEncode = new FileEncode();
        student = new Student();
    }

    public DatabaseConnection getConnection()
    {
        return connection;
    }

    public void tableCreate()
    {
        Scanner input = new Scanner(System.in);

        //用户可以建表
        System.out.print("你想建的表的名字: ");
        tableName = input.nextLine();
        try {
            connection.TableCreate(tableName);
            isSuccess = true;
        } catch (Exception e) {
            System.out.println(tableName+"之前已建立，请重新输入");
            isSuccess = false;
        }
    }

    public void dataAdd() throws Exception
    {
        while(addflag) {
            System.out.println("批量添加文件，请输入文件所在位置");

            Scanner input = new Scanner(System.in);
            String url = input.nextLine();

            /**
             * 添加文件是否存在的代码
             */

            fileEncode.readDataInsertedFile(url);
            LinkedList<Student> students = fileEncode.getStudents();

            /** 将学生对象数据批量写入数据库中 */

            int length = students.size();
            int i = 0;
            while (students != null && i < length) {
                student = students.get(i++);
                connection.DataAdd(tableName, student);
            }

            //清空链表内容，方便以后继续循环操作
            fileEncode.clearStudentsList();

            System.out.println("是否选择继续添加数据: 是的话输入1，否则输入2");
            int answer2 = input.nextInt();
            input.nextLine();
            if (2 == answer2) {
                addflag = false;
            }
        }
    }

//        Scanner input = new Scanner(System.in);
//        while(addflag)
//        {
//            //这个输入操作应该放在写入文件里面，待会在这个页面呈现的应该是文件读取后的结果
//            System.out.println("学生数据具体为：学生编号，姓名，性别，身高，生日，籍贯");
//
//            System.out.println("请输入学生编号:");
//            String ID =input.next();
//            input.nextLine();  //用来读回车键
//
//            System.out.println("请输入姓名:");
//            String name = input.nextLine();
//
//            System.out.println("请输入性别:");
//            String sex = input.nextLine();
//
//            System.out.println("请输入身高:");
//            double height = input.nextDouble();
//            input.nextLine();
//
//            System.out.println("请输入生日:");
//            int years = input.nextInt();
//            int months = input.nextInt();
//            int days = input.nextInt();
//            input.nextLine();
//
//            System.out.println("请输入籍贯:");
//            String birth_place = input.nextLine();
//
////            connection.DataAdd(tableName,);
//
//            System.out.println("是否选择继续添加数据: 是的话输入1，否则输入2");
//            int answer2 = input.nextInt();
//            input.nextLine();
//            if(2 == answer2)
//            {
//                addflag = false;
//            }
//        }


    public void dataDelete()
    {
        while(deleteflag)
        {
          System.out.println("批量删除文件，请输入文件所在位置");

          Scanner input = new Scanner(System.in);
          String url = input.nextLine();

          //读入删除的文件
          fileEncode.readDataDeletedFile(url);

        /**
         * 删除文件是否存在的代码
         */

        LinkedList<String> IDNumbers = new LinkedList<>();
        IDNumbers = fileEncode.getIDNumber();
        int rows = IDNumbers.size();

        for(int i = 0;i<rows;i++)
        {
            String sql = "Delete from " + tableName+ " where ID='"+IDNumbers.get(i)+"'";

            try {
                connection.DataDelete(tableName, sql);
            }catch(Exception e)
            {
                //System.out.println("删除语句有误");
            }
         }

         //清空链表内容，方便以后继续循环操作
            fileEncode.clearIDNumberList();

            System.out.println("是否选择继续删除数据: 是的话输入1，否则输入2");
            int answer2 = input.nextInt();
            input.nextLine();
            if (2 == answer2) {
                deleteflag = false;
            }
        }
    }

    //用来帮助修改学生元组中属性的方法，增加修改后的元组
    public void dataAdd(String url) throws Exception
    {
        fileEncode.readDataInsertedFile(url);
        LinkedList<Student> students = fileEncode.getStudents();

        /** 将学生对象数据批量写入数据库中 */

        int length = students.size();
        int i = 0;
        while (students != null && i < length) {
            student = students.get(i++);
            connection.DataAdd(tableName, student);
        }

        //清空链表内容，方便以后继续循环操作
        fileEncode.clearStudentsList();
    }


    //用来帮助修改学生元组中属性的方法，删除原有的元组
    private void dataDelete(String url)
    {
        //读入删除的文件
        fileEncode.readDataDeletedFile(url);

        LinkedList<String> IDNumbers = new LinkedList<>();
        IDNumbers = fileEncode.getIDNumber();
        int rows = IDNumbers.size();

        for(int i = 0;i<rows;i++)
        {
            String sql = "Delete from " + tableName+ " where ID='"+IDNumbers.get(i)+"'";
            try {
                connection.DataDelete(tableName, sql);
            }catch(Exception e)
            {
                //System.out.println("删除语句有误");
            }
        }

        fileEncode.clearIDNumberList();
    }


    public void dataUpdate() throws Exception
    {
        while(updateflag)
        {
            System.out.println("批量修改文件，请输入文件所在位置");
            Scanner input = new Scanner(System.in);
            String url =input.nextLine();

            /**    读入更新文件，待会返回一个学生链表,先删除数据库里的内容，
             * 再添加进去，又因为增删的函数参数是文件地址，所以需要先把数据库的初次信息和修改后的信息写出
             */
            fileEncode.readDataUpdatedFile(url);

            //原始学生数据所在的文件
            String original_url = "Updated_IDNumber.txt";
            this.dataDelete(original_url);

            //更新后学生数据所在的文件
            String ultimate_url = "UltimateUpdated.txt";
            this.dataAdd(ultimate_url);

            //fileEncode.clearIDNumberList();
            fileEncode.clearStudentsNewList();
            fileEncode.clearStudentsList();

            System.out.println("是否选择继续更新数据: 是的话输入1，否则输入2");
            int answer2 = input.nextInt();
            input.nextLine();
            if (2 == answer2) {
                updateflag = false;
            }
        }
    }

    public void dataSearch() throws Exception
    {
        String sql = "select * from "+ tableName + " order by ID asc";
        connection.DataSearch(tableName,sql);
    }




    public static void main(String[] args) throws  Exception{

        UserInterface user = new UserInterface();

        //用户可以连接数据库，并且得到那把"钥匙"
        DatabaseConnection connection = user.getConnection();

//        //建表不成功，则继续
//        while(!isSuccess) {
//            user.tableCreate();
//        }

//        //用户可以批量添加学生数据
//        Scanner input = new Scanner(System.in);
//        System.out.println("是否添加学生数据: 是的话输入1，否则输入2");
//        int answer = input.nextInt();
//        input.nextLine();
//        addflag = false;
//        if(1 == answer)
//        {
//            addflag = true;
//        }
//        user.dataAdd();
//
//        //用户可以批量删除学生数据
//        //Scanner input = new Scanner(System.in);
//        System.out.println("是否删除学生数据: 是的话输入1，否则输入2");
//        int answer1 = input.nextInt();
//        deleteflag = false;
//        if(1 == answer1)
//        {
//            deleteflag = true;
//        }
//        user.dataDelete();


        //用户可以更新学生数据
        System.out.println("是否更改学生数据: 是的话输入1，否则输入2");
        Scanner input = new Scanner(System.in);
        int answer2 = input.nextInt();
        updateflag = false;
        if(1 == answer2)
        {
            updateflag = true;
        }
        user.dataUpdate();
        input.nextLine();

        System.out.println("是否查询学生数据: 是的话输入1，否则输入2");
        int answer3 = input.nextInt();
        input.nextLine();
        if(answer3 == 1)
        user.dataSearch();

//        FileEncode fileEncode = new FileEncode();
//
//        LinkedList<String> ID = new LinkedList<>();
//        ID.add("10001");
//        ID.add("10002");
//        ID.add("10003");
//
//        fileEncode.readDatabase("Student", ID);
    }
}
