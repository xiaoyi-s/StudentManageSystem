package studentsystem.model;

import studentsystem.bean.AnalyzeResult;
import studentsystem.bean.Student;
import studentsystem.dao.ManageHelper;

import java.util.HashMap;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

public class ScoreAnalyzeModel extends AbstractTableModel{
	private ManageHelper helper;
	private Vector<Student> students;
	private  Vector<String> columnNames = null;	//����
	private Vector<Vector<String>> rowData = null;	//������
	
		
	 public ScoreAnalyzeModel(String major_Name,String grade,String classe) {
		helper = new ManageHelper();
		Vector<String> courses = helper.getCourse(helper.getAllMajor().get(major_Name),grade);//��ÿγ�
		Vector<AnalyzeResult> results = helper.analyzeScore(major_Name, classe, grade);	//�õ������Ľ������
		
		columnNames = new Vector<String>();
		rowData = new Vector<Vector<String>>();
		columnNames.add("ѧ��");
		columnNames.add("����");
		for(int i=0;i<courses.size();i++){
			columnNames.add(courses.get(i));
		}
		columnNames.add("�ܳɼ�");
		columnNames.add("ƽ���ɼ�");
		columnNames.add("����");
		for(int i=0;i<results.size();i++){
			Vector<String> hang = new Vector<String>();
			AnalyzeResult result = results.get(i);
			hang.add(result.getStudentId());
			hang.add(result.getStudentName());
			HashMap<String, String> scores = helper.getStudentScore(result.getStudentId());	//�õ����пγ�
			for(int j=0;j<scores.size();j++){
				String score = scores.get(courses.get(j));	//�õ��ɼ�
				hang.add(score);
			}
			hang.add(result.getSumScore());
			String avg = result.getAvgScore();
			hang.add(avg.charAt(0)+""+avg.charAt(1)+""+avg.charAt(2)+""+avg.charAt(3));
			hang.add(getRowCount()+1+"");
			rowData.add(hang);
		}
	
		
	}
	
	//�õ����ж�����
		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return this.rowData.size();
		}
		//�õ����ж�����
		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return this.columnNames.size();
		}
		//�õ�ĳ��ĳ�е�����
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return ((Vector)this.rowData.get(rowIndex)).get(columnIndex);
		}
		
		//��д���� getColumnName
		@Override  
		public String getColumnName(int column) {
			// TODO Auto-generated method stub
			return (String)this.columnNames.get(column);
		}
}
