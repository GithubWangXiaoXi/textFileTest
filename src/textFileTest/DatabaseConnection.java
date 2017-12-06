package textFileTest;

import sun.security.jca.GetInstance;

import java.sql.*;
import java.util.LinkedList;

/**
 * Created by Administrator on 2017/12/1 0001.
 */
public class DatabaseConnection {

    private String url = "jdbc:mysql://localhost/jdbc";
    private String username = "root";
    private String password = "19971010a";

    private String tableName = "student";

    private Connection conn =null;

    private LinkedList<Student> students;   //转化成String类型的中间媒介
    private LinkedList<String> studentsStr;  //为了返回数据库中的学生元组，方便排序并写入csv文件中

    public DatabaseConnection() throws Exception
    {
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        conn = DriverManager.getConnection(url,username,password);
        Statement stat = conn.createStatement();
        System.out.println("数据库连接成功");
//        ResultSet rs = stat.executeQuery("select * from account");
//        while(rs.next())
//        {
//            System.out.println(rs.getString(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3));
//        }

//        //创建一个Student的表
//        System.out.println("创建一个Student的表");
//        TableCreate();

        //插入数据
//        System.out.println("插入数据");
//        DataAdd();

        //删除数据


        //修改数据
    }

    //方便文件读取数据库的信息，并在Update方法中修改数据库里的内容
    public Connection getConn()
    {
        return conn;
    }

    /**
     *   创建表
     * @throws Exception
     */
    public void TableCreate(String tableName)// throws SQLException
    {
        try{
            String createTable = "create table " + tableName + " (ID VARCHAR(15), NAME VARCHAR(15), SEX VARCHAR(15), " +
                    "HEIGHT DOUBLE, BIRTH DATE, BIRTH_PLACE VARCHAR(15), primary key(ID))";
            PreparedStatement statement1 = conn.prepareStatement(createTable);
            statement1.executeUpdate();
            System.out.println(tableName+"建立成功");
        }catch(SQLException e){
            System.out.println("TableCreate方法语句异常");
        }

    }

    /**
     *   数据添加
     * @throws Exception
     */

    public void DataAdd(String tablename,Student stu)throws Exception
    {
        Student stu1 = stu;
        String data_add = "insert into " + tablename + " (ID,NAME,SEX,HEIGHT,BIRTH,BIRTH_PLACE) values (?,?,?,?,?,?)";
        PreparedStatement DataAdd = conn.prepareStatement(data_add);
        String str1 = stu1.getID().toString();
        String str2 = stu1.getName().toString();
        String str3 = stu1.getSex().toString();
        //String str4 = String.valueOf(stu1.getHeight());
        String str4 = stu1.getHeight().toString();
        String str5 = String.valueOf(stu1.getYear()) + "-" + String.valueOf(stu1.getMonth()) + "-" + String.valueOf(stu1.getDay());
        String str6 = stu1.getBirth_place().toString();


        DataAdd.setString(1,str1);
        //System.out.println(stu1.getID().toString());
        DataAdd.setString(2,str2);
        DataAdd.setString(3,str3);
        DataAdd.setString(4,str4);
        DataAdd.setString(5,str5);
        DataAdd.setString(6,str6);
        int row = DataAdd.executeUpdate();
        if(1 == row)
        {
            System.out.println("插入成功");
        }
    }


    /**
     * 数据删除
     */
    public void DataDelete(String tableName,String sql) throws Exception
    {
         PreparedStatement statement1 = conn.prepareStatement(sql);
         statement1.executeUpdate();
         System.out.println("删除成功");
    }

    /**
     * 数据更改
     */
    public void DataUpdate(String tableName,String sql) throws Exception
    {
        PreparedStatement statement2 = conn.prepareStatement(sql);
        statement2.executeUpdate();
    }


    /**
     * 数据查找
     */
    public void DataSearch(String tablename,String sql) throws Exception
    {
        String allSelect = sql;
        PreparedStatement preparedStatement = conn.prepareStatement(allSelect);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next())
        {
            System.out.println(rs.getString(1)+"\t"+rs.getString(2)+"\t"
                    +rs.getString(3)+"\t"+rs.getString(4)+"\t"
                    +rs.getString(5)+"\t"+rs.getString(6));
        }
    }

    //用链表储存student，再最终转化成String类型每个学生元组到String链表中
    public LinkedList<String> getStudentsStr(String sqlID) throws SQLException
    {
        String sql;
        if(sqlID.equals("No")) {
             sql = "select * from " + tableName;
        }

        else{
            sql = "select * from "+tableName+" where ID = '"+sqlID+"'";
        }

        PreparedStatement stat = conn.prepareStatement(sql);
        ResultSet rs = stat.executeQuery(sql);

        int studentTotal = 0;

        students = new LinkedList<>();
        studentsStr = new LinkedList<>();

        while(rs.next())
        {
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
            students.add(student);

            String str = students.get(studentTotal).getID()+','+students.get(studentTotal).getName()
                    +','+students.get(studentTotal).getSex()+','+students.get(studentTotal).getHeight()
                    +','+students.get(studentTotal).getYear() + '-'
                    + students.get(studentTotal).getMonth() + '-'
                    + students.get(studentTotal).getDay() +','
                    +students.get(studentTotal).getBirth_place();

            studentsStr.add(str);
            studentTotal++;
        }
        return studentsStr;
    }

    public static void main(String[] args) throws Exception{
        DatabaseConnection database1 = new DatabaseConnection();
    }
}
