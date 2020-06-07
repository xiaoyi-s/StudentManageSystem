package studentsystem.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//�����ݿ�ͨ�ŵ���
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

import studentsystem.bean.AnalyzeResult;
import studentsystem.bean.Student;
import studentsystem.bean.User;

public class JdbcHelper implements JdbcConfig{
	//�����������ݿ�����Ҫ�Ķ���
	//Ԥ�������ps
	private PreparedStatement ps = null;
	//���������
	private ResultSet rs = null;
	//���Ӷ���
	private Connection ct = null;
	

	//������ݿ������
	private  void init(){
		try {
			//���ؼ�ע��JDBC��������
			Class.forName(DRIVER);
			ct = DriverManager.getConnection(URL, USERNAME, PASSWORD);// ������ݿ�����
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			//û�ҵ�DRIVER
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//û�������ݿⱨ��
			e.printStackTrace();
		}
		
	}
	
	//�޲ι��캯��
	//��ʼ������
	public JdbcHelper(){
		this.init();
	}
	

	/**
	 * ��ȡ�û�����
	 * ���ݴ�����û�������ȡ��Ӧ���û����������û�����
	 * @return �û�����
	 */
	public User getUser(User user){
		//ʵ����һ���û�����
		User newUser = new User();
		try {
			//���Ԥ�����û�
			ps = ct.prepareStatement("select * from tb_User where User_name=?");
			//	select * from tb_User where User_name="?"
			//	���ϵ�һ���ʺŵĲ�������Ϊ��user.getUsername();
			ps.setString(1, user.getUsername());
			//	����������ѯ�ĵ��������
			rs = ps.executeQuery();
			if(rs.next()){
				newUser.setUsername(rs.getString(1));	//�����û���
				newUser.setPassword(rs.getString(2));	//��������
				newUser.setIsLogin(rs.getInt(3));	//�����Ƿ��½
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//	��ȡ���û��󷵻��û�������
		return newUser;
	}
	
	/**
	 * ע�ᴦ��
	 * @param user	�û�����
	 * @return	�����Ƿ�ע��ɹ�
	 */
	public boolean register(User user){
		boolean b = true;
		try {
			//	Ԥ����ע�������û�������ע����������
			ps = ct.prepareStatement("insert into tb_User(User_name,Password_) values(?,?)");
			//	��һ���ʺŵĲ�������Ϊ�û���
			ps.setString(1, user.getUsername());
			//	�ڶ����ʺõĲ�������Ϊ����
			ps.setString(2, user.getPassword());
			//	���ps�����ı�������Ϊ1����ʾע���û�ʧ��
			if(ps.executeUpdate()!=1){	//ִ��sql���
				b = false;
			}
		} catch (SQLException e) {
			b = false;
			e.printStackTrace();
		}
		//	����ע��ɹ�����״̬
		return b;
	}
	
	/**
	 * �޸��û�"�Ƿ�"��½״̬
	 * @param user
	 * @return
	 */
	public boolean update_IsLogin(User user){
		boolean b = true;
		try {
			//	Ԥ�����û����ĵ�½״̬
			ps = ct.prepareStatement("update tb_User set IsLogin=? where User_name=?");
			//	��һ���ʺŲ���Ϊ��½״̬
			ps.setInt(1, user.getIsLogin());
			//	�ڶ����ʺŲ���Ϊ�û���
			ps.setString(2, user.getUsername());
			//	�ж��Ƿ��½
			if(ps.executeUpdate()!=1){
				b = false;
			}
		} catch (SQLException e) {
			b = false;
			e.printStackTrace();
		}
		return b;
	}
	
	
	
	
	
		/**
		 * �޸�����
		 * @param user  �û�����
		 * @param new_Password ������
		 * @return	�����Ƿ��޸ĳɹ�
		 */
		public boolean update_Password(User user,String new_Password){
			boolean b = true;
			try {
				//	Ԥ�����û���������
				ps = ct.prepareStatement("update tb_User set Password_=? where User_name=?");
				//	��һ���ʺŲ���Ϊ�û�������
				ps.setString(1, new_Password);
				//	�ڶ�������Ϊ�û���
				ps.setString(2, user.getUsername());
				//	�ж��Ƿ��޸ĳɹ�
				if(ps.executeUpdate()!=1){	//ִ��sql���
					b = false;
				}
			} catch (SQLException e) {
				b = false;
				e.printStackTrace();
			}
			return b;
		}
		
		
		/**
		 * �������Ժϵ
		 * @return ��������Ժϵ��HashMap����
		 */
		public HashMap<String, String> getAllDepartment(){
			//	����һ��Map����
			HashMap<String, String> map = new LinkedHashMap<String, String>();
			map.put("", "");//���һ���յ�Ԫ��
			try {
				//	Ԥ���밴��tb_Department��Department_ID�����򣬻�ý����
				ps = ct.prepareStatement("select * from tb_Department order by Department_ID");
				//	rs���ز�ѯ�Ľ����
				rs = ps.executeQuery();
				//	����������浽hash map��
				while(rs.next()){
					map.put(rs.getString(2),rs.getString(1)); 
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return map;
		}
	
		
		/**
		 * ��ö�ӦԺϵ��רҵ
		 * @return ����Vector<String>����
		 */
		public Vector<String> getMajor(String department_ID){
			Vector<String> vector = new Vector<String>();
			vector.add("");//���һ���յ�Ԫ��
			try {
				//	Ԥ���밴��רҵ��Ķ�Ӧ��Department_ID��Major_ID�����򣬻�ý����
				ps = ct.prepareStatement("select * from tb_Major where Department_ID=? order by Major_ID");
				//	��һ���ʺŲ�������Ϊdepartment_ID
				ps.setString(1, department_ID);
				//	���ؽ����
				rs = ps.executeQuery();
				while(rs.next()){
					//	��÷��ؽ���ĵڶ���λ�÷ŵ�vector��
					vector.add(rs.getString(2));	//���רҵ����
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//	���ض�ӦԺϵ��רҵ
			return vector;
		}
		
		/**
		 * �������רҵ 
		 * @return ��������רҵ
		 */
		public HashMap<String, String>getAllMajor(){
			HashMap<String, String> map = new LinkedHashMap<String, String>();
			map.put("", "");//���һ���յ�Ԫ��
			try {
				//	Ԥ���밴��רҵ���Ӧ��Major_ID�������ý����
				ps = ct.prepareStatement("select * from tb_Major order by Major_ID");
				//	���ؽ����
				rs = ps.executeQuery();
				while(rs.next()){
					//	��������ĵڶ���λ�ú͵�һ��λ�÷ŵ�map��
					map.put(rs.getString(2),rs.getString(1)); 
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//	��������רҵ
			return map;
		}
		
		/**
		 * ���ѧ��
		 * @param student	ѧ������
		 * @return	�����Ƿ���ӳɹ�
		 */
		public boolean addStudent(Student student){
			boolean b = true;
			try {
				//Ԥ�������ѧ��
				ps = ct.prepareStatement("insert into tb_Student(Student_Id,Student_Name,Student_Sex,Classe,Grade,Major_ID,Department_ID,Major_Name,Department_Name) values(?,?,?,?,?,?,?,?,?)");
				//	��ѧ������Ϣ������У�ͨ����õ�ѧ����Ϣ
				ps.setString(1, student.getStudent_ID());
				ps.setString(2, student.getStudent_Name());
				ps.setString(3, student.getSex());
				ps.setString(4, student.getClasse());
				ps.setString(5, student.getGrade());
				ps.setString(6, student.getMajor_ID());
				ps.setString(7, student.getDepartment_ID());
				ps.setString(8, student.getMajor_Name());
				ps.setString(9, student.getDepartment_Name());
				if(ps.executeUpdate()!=1){
					b = false;
				}
			} catch (SQLException e) {
				b = false;
				e.printStackTrace();
			}
			return b;
		}
		
		
		/**
		 * �޸�ѧ����Ϣ
		 * @param newStudent	��ѧ������
		 * @param oldStudentID	��ѧ����Ϣ
		 * @return	�Ƿ��޸ĳɹ�
		 */
		public boolean updateStudent(Student newStudent,String oldStudentID){
			boolean b = true;
			try {
				//update
				//	Ԥ�������ѧ������Ϣ
				ps = ct.prepareStatement("update tb_Student set Student_Id=?, Student_Name=?, Student_Sex=? ,Classe=? ,Grade=?  ,Major_ID=? ,Department_ID=? ,Major_Name=? ,Department_Name=? where Student_Id=?");
				ps.setString(1, newStudent.getStudent_ID());
				ps.setString(2, newStudent.getStudent_Name());
				ps.setString(3, newStudent.getSex());
				ps.setString(4, newStudent.getClasse());
				ps.setString(5, newStudent.getGrade());
				ps.setString(6, newStudent.getMajor_ID());
				ps.setString(7, newStudent.getDepartment_ID());
				ps.setString(8, newStudent.getMajor_Name());
				ps.setString(9, newStudent.getDepartment_Name());
				//	�Ծ�ѧ����id���и���
				ps.setString(10, oldStudentID);
				//	�ж��Ƿ�ִ�гɹ�
				if(ps.executeUpdate()!=1){
					b = false;
				}
			} catch (SQLException e) {
				b = false;
				e.printStackTrace();
			}
			return b;
		}
		
		/**
		 * ����ѧ��ѧ�Ŵ����ݿ��Ƴ���ѧ��
		 * @param studentID	ѧ��ѧ��
		 * @return	�����Ƿ�ɾ���ɹ�
		 */
		public boolean deleteStudent(String studentID){
			boolean b = true;
			try {
				//	Ԥ����ɾ��ѧ��������Student_Id
				ps = ct.prepareStatement("delete from tb_Student where Student_Id=?");
				ps.setString(1, studentID);
				if(ps.executeUpdate()!=1){
					b = false;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				b = false;
				e.printStackTrace();
			}
			return b;
		}
		

		/**
		 * ����sql��䷵���ض���ѧ������
		 * @param sql	sql���
		 * @return	����Vector<Student>����
		 */
		public Vector<Student> getStudent(String sql){
			Vector<Student> students = new Vector<Student>();
			try {
				//	�����ض�ѧ���ļ��ϸ���sql���
				//	Ԥ����
				ps = ct.prepareStatement(sql);
				//	���룬��ý����
				rs = ps.executeQuery();
				while(rs.next()){
					// ���ѧ������
					Student student = new Student();
					student.setStudent_ID(rs.getString(1));
					student.setStudent_Name(rs.getString(2));
					student.setSex(rs.getString(3));
					student.setGrade(rs.getString(4));
					student.setClasse(rs.getString(5));
					student.setMajor_ID(rs.getString(6));
					student.setMajor_Name(rs.getString(7));
					student.setDepartment_ID(rs.getString(8));
					student.setDepartment_Name(rs.getString(9));
					//	��ѧ��������ӵ��������
					students.add(student);
				}
			}catch(SQLException e){
				e.printStackTrace();
			}
			return students;
		}
		
		/**
		 * ����ѧ��(�꼶�ͱ��)���ض�Ӧ�İ༶
		 * @param student
		 * @return
		 */
		public Vector<String> getAllClasse(String grade,String major_ID){
			Vector<String> vector = new Vector<String>();
			vector.add("");	//���һ����ѡ��
			try {
				//	�����꼶�ͱ�ţ����ض�Ӧ�İ༶
				ps = ct.prepareStatement("select Classe from tb_Classe where Grade=? and Major_ID=?");
				//	Ԥ���룬��һ���ʺ�����Ϊ�꼶grade
				ps.setString(1, grade);
				//	Ԥ���룬�ڶ����ʺ�����ΪרҵID
				ps.setString(2, major_ID);
				rs = ps.executeQuery();
				while(rs.next()){
					//	������༶
					vector.add(rs.getString(1));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return vector;
		}
		
		
		
		
		
		
		/**
		 *  ����רҵ��ŷ��ض�Ӧ�����пγ�
		 * @param major_Id
		 * @return ���ؿγ̼���
		 */
		public Vector<String> getCourse(String major_Id,String grade){
			Vector<String> vector = new Vector<String>();
			try {
				//	Ԥ���룬����רҵ��ź��꼶����ý����
				ps = ct.prepareStatement("select Course_Name from tb_Course where Major_ID=? and Grade=?");
				//	Ԥ���룬��һ���ʺ�Ϊרҵ���
				ps.setString(1, major_Id);
				//	Ԥ���룬�ڶ����ʺ�Ϊ�꼶
				ps.setString(2, grade);
				//	���ؽ����
				rs = ps.executeQuery();
				while(rs.next()){
				//	���γ���ӵ�vector������
					vector.add(rs.getString(1));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return vector;
		}
		
		
		/**
		 * ���ѧ���ɼ�
		 * @param student ѧ������
		 * @return �����Ƿ���ӳɹ�
		 */
		public boolean addStudentScore(Student student,Vector<String> courses){
			boolean b = true;
			HashMap<String, String> map = student.getScores();
			try {
				int i;
				for(i=0;i<map.size();i++){
					ps = ct.prepareStatement("insert into tb_Score(Course_Name,Student_Id,Student_Name,Score) values (?,?,?,?)");
					//	Ԥ���룬��һ���ʺ�Ϊ�γ̵�����
					ps.setString(1, courses.get(i));
					//	Ԥ���룬�ڶ����ʺ�Ϊѧ����ID
					ps.setString(2, student.getStudent_ID());
					//	�������ʺ�Ϊѧ��������
					ps.setString(3, student.getStudent_Name());
					//	���ĸ��ʺ�Ϊѧ���ɼ�
					ps.setString(4, map.get(courses.get(i)));
					//	�������ʧ�ܣ���ֹ
					if(ps.executeUpdate()!=1){
						break;
					}
				}
				//	���i��С�������Ĵ�С�����ʾ����ʧ��
				if(i<map.size()){
					b = false;
				}
			} catch (SQLException e) {
				b = false;
				e.printStackTrace();
			}
			return b;
		}
		
		/**
		 * �޸�ѧ���ɼ�
		 * @param student_Id	ѧ��ѧ��
		 * @param course_Name	�γ�����
		 * @param score �ɼ�
		 * @return	�����Ƿ��޸ĳɹ�
		 */
		public boolean updateStudentScore(String student_Id,String course_Name,String score){
			boolean b = true;
			try {
				//	���ݿγ̵�����Course_Name����ѧ����id��Student_Id��������ѧ���ĳɼ�
				ps = ct.prepareStatement("update tb_Score set Score=? where Course_Name=? and Student_Id=? ");
				ps.setString(1, score);
				ps.setString(2, course_Name);
				ps.setString(3, student_Id);
				if(ps.executeUpdate()!=1){
					b = false;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				b = false;
				e.printStackTrace();
			}
			return b;
		}
		
		
		/**
		 * ��ѯѧ�����гɼ�
		 * @param student_Id	ѧ��ѧ��
		 * @return	���سɼ�����
		 */
		public HashMap<String, String> getStudentScore(String student_Id){
			HashMap<String, String> scores = new HashMap<String, String>();
			try {
				//	����ascii���������и���ѧ����ID��Student_Id��
				ps = ct.prepareStatement("select Course_Name,Score from tb_Score where Student_Id=? order by Student_Id asc");
				ps.setString(1, student_Id);
				//	���ؽ����
				rs = ps.executeQuery();
				while(rs.next()){
					scores.put(rs.getString(1), rs.getString(2));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//	���سɼ����
			return scores;
		}
		
//		/**
//		 * ����ѧ�źͿγ�����ѯָ���ĳɼ�
//		 * @param student_Id ѧ��
//		 * @param course_Name	�γ���
//		 * @return	���سɼ�
//		 */
//		public String getStudentScore(String student_Id,String course_Name){
//			String score = null;
//			try {
//				ps = ct.prepareStatement("select Score from tb_Score where Student_Id=? and Course_Name=?");
//				ps.setString(1, student_Id);
//				ps.setString(2, course_Name);
//				rs = ps.executeQuery();
//				rs.next();
//				score = rs.getString(1);
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return score;
//		}
//	
//		
		
		/**
		 * �����ض���sql��䷵��ѧ������
		 * @param sql
		 * @return
		 */
		public Vector<Student> getScores(String sql){
			Vector<Student> students = new Vector<Student>();
			try {
				ps = ct.prepareStatement(sql);
				//	��ý����
				rs = ps.executeQuery();
				while(rs.next()){
					Student student = new Student();
					HashMap<String, String>scores = new HashMap<String, String>();
					//	�ɼ����γ��������е÷�
					scores.put(rs.getString(3), rs.getString(4));
					//	���óɼ�
					student.setScores(scores);
					//	���Student_ID
					student.setStudent_ID(rs.getString(1));
					//	���Student_Name
					student.setStudent_Name(rs.getString(2));
					//	��ѧ����ӵ�vector<Student>��
					students.add(student);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
			return students;
		}
		
		
		/**
		 * ��ð༶�ܳɼ�
		 * @param classe	�༶
		 * @param grade		�꼶
		 * @param major		רҵ
		 * @return �ܳɼ�
		 */
		public String getClasse_SumScore(String classe,String grade,String major){
			String sum = null;
			try {
				//	����ȫ��ĳɼ��������꼶��Major_Nameרҵ����
				ps = ct.prepareStatement("select SUM(Score) from tb_Score where Student_Id in(Select Student_Id from tb_Student where Classe=? and Grade=? and Major_Name=?)");
				ps.setString(1, classe);
				ps.setString(2,grade);
				ps.setString(3, major);
				rs = ps.executeQuery();
				if(rs.next()){
					sum = rs.getString(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return sum;
		}
		
		
		
		
		
		
		
		/**
		 * ����ѧ��id���ظ�ѧ����ƽ���ɼ�
		 * @param student_Id
		 * @return
		 */
		public String getStudentAvgScore(String student_Id){
			String avg = null;
			try {
				ps = ct.prepareStatement("select  AVG(Score) from tb_Score where Student_Id=? ");
				ps.setString(1, student_Id);
				rs = ps.executeQuery();
				if(rs.next()){
					avg = rs.getString(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return avg;
		}
		
		/**
		 * ����ѧ��רҵ���꼶���༶����Ŀ�����Գɼ�����(�ţ������У����񣬲�����)���ظ����͵�ѧ������
		 * @param grade
		 * @param classe
		 * @param major
		 * @param type
		 * @param course_Name
		 * @return
		 */
		public String getCount_ScoreType(String grade,String classe,String major,String type,String course_Name){
			String count = null;
			String sql = null;
			//	����ѧ�����꼶���༶��רҵ����Ŀ
			if(type.equals("��")){
				sql = "select  Count(*) from tb_Score where Student_Id in(select Student_Id from tb_Student where Grade=? and Classe=? and Major_Name=?) and Course_Name=? and Score>=90 and Score<=100";
			}else if(type.equals("��")){
				sql = "select  Count(*) from tb_Score where Student_Id in(select Student_Id from tb_Student where Grade=? and Classe=? and Major_Name=?) and Course_Name=? and Score>=80 and Score<90";
			}else if(type.equals("��")){
				sql = "select  Count(*) from tb_Score where Student_Id in(select Student_Id from tb_Student where Grade=? and Classe=? and Major_Name=?) and Course_Name=? and Score>=70 and Score<80";
			}else if(type.equals("����")){
				sql = "select  Count(*) from tb_Score where Student_Id in(select Student_Id from tb_Student where Grade=? and Classe=? and Major_Name=?) and Course_Name=? and Score>=60 and Score<70";
			}else if(type.equals("������")){
				sql = "select  Count(*) from tb_Score where Student_Id in(select Student_Id from tb_Student where Grade=? and Classe=? and Major_Name=?) and Course_Name=? and Score>=0 and Score<60";
			}
			try {
				ps = ct.prepareStatement(sql);
				ps.setString(1, grade);
				ps.setString(2, classe);
				ps.setString(3, major);
				ps.setString(4, course_Name);
				rs = ps.executeQuery();
				if(rs.next()){
					count = rs.getString(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return count+"��";
		}
		
		/**
		 * ��ÿγ̵�ƽ����
		 * @param course �γ���
		 * @param major	רҵ
		 * @return	ƽ����
		 */
		public String getCourseAvg(String course,String major,String classe,String grade){
			String avg = null;
			try {
				//	���ݿγ����֣����ƽ���֣����꼶���༶����רҵ��ѡ�е������£�
				ps = ct.prepareStatement("select  AVG(Score) from tb_Score  where Course_Name=? and  Student_Id in (select Student_Id from tb_Student where Grade=? and Classe=? and Major_Name=?) ");
				ps.setString(1, course);
				ps.setString(2, grade);
				ps.setString(3,classe);
				ps.setString(4, major);
				rs = ps.executeQuery();
				if(rs.next()){
					avg = rs.getString(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return avg;
		}
		
		
		/**
		 * ��õ��Ƴɼ�����߷�
		 * @param course
		 * @param major
		 * @param classe
		 * @param grade
		 * @return
		 */
		public String getCourseHighestScore(String course,String major,String classe,String grade){
			String highest = null;
			try {
				//	����ѧ����ID�����ƽ���֣����꼶���༶����רҵ��ѡ�е������£�
				ps = ct.prepareStatement("select MAX(Score) from tb_Score where Student_Id in(select Student_Id from tb_Student where Grade=? and Classe=? and Major_Name=?) and Course_Name=? " );
				ps.setString(1, grade);
				ps.setString(2, classe);
				ps.setString(3, major);
				ps.setString(4, course);
				rs = ps.executeQuery();
				if(rs.next()){
					highest = rs.getString(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return highest;
		}
		
		
		/**
		 * ��õ��Ƴɼ�����ͷ�
		 * @param course
		 * @param major
		 * @param classe
		 * @param grade
		 * @return
		 */
		public String getCourseLowestScore(String course,String major,String classe,String grade){
			String lowest = null;
			try {
				ps = ct.prepareStatement("select MIN(Score) from tb_Score where Student_Id in(select Student_Id from tb_Student where Grade=? and Classe=? and Major_Name=?) and Course_Name=? " );
				ps.setString(1, grade);
				ps.setString(2, classe);
				ps.setString(3, major);
				ps.setString(4, course);
				rs = ps.executeQuery();
				if(rs.next()){
					lowest = rs.getString(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return lowest;
		}
		
		
		/**
		 * ��øð༶�гɼ�������
		 * @param major
		 * @param classe
		 * @param grade
		 * @return
		 */
		public String getHaveScoreCount(String major,String classe,String grade){
			String count = null;
			try {
				//	��ñ��༶�������������꼶���༶���γ�����
				ps = ct.prepareStatement("select Count(*) from tb_Score where Student_Id in(select Student_Id from tb_Student where Grade=? and Classe=? and Major_Name=?) GROUP BY  Course_Name");
				ps.setString(1, grade);
				ps.setString(2, classe);
				ps.setString(3, major);
				rs = ps.executeQuery();
				if(rs.next()){
					count = rs.getString(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return count;
		}
		
		/**
		 * ��óɼ������Ľ��
		 * @param major	רҵ
		 * @param classe	�༶
		 * @param grade	�꼶
		 * @return	���ط�������
		 */
		public Vector<AnalyzeResult> analyzeScore(String major,String classe,String grade ){
			Vector<AnalyzeResult> vector = new Vector<AnalyzeResult>();
			try {
				//	�����꼶�Ľ��򣬸���ѧ��ID������
				ps = ct.prepareStatement("select  Student_Id,Student_Name,SUM(Score),Avg(Score) as score from tb_Score where Student_Id in (Select Student_Id from tb_Student where Grade=? and Classe=? and Major_Name=? )GROUP BY Student_Id order by score desc");
				ps.setString(1, grade);
				ps.setString(2, classe);
				ps.setString(3, major);
				rs = ps.executeQuery();
				while(rs.next()){
					AnalyzeResult result = new AnalyzeResult();
					result.setStudentId(rs.getString(1));
					result.setStudentName(rs.getString(2));
					result.setSumScore(rs.getString(3));
					result.setAvgScore(rs.getString(4));
					vector.add(result);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return vector;
		}
		
		
		//�ر����ݿ���Դ
		public void close()	{
			try {
				if(rs!=null) rs.close();
				if(ps!=null) ps.close();
				if(ct!=null) ct.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	
}
