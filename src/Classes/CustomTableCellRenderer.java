package Classes;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {
	
	private ArrayList<Integer> liste;
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component cell = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);

		if (liste.contains((Integer)row))
			cell.setBackground(Color.lightGray);
		else
			cell.setBackground(Color.white);
		return cell;
	}
	
	public void setArrayList(ArrayList<Integer> a){
		liste = a;
	}
	public void setList (Integer a){
		liste.add(a);
	}
	
	public ArrayList<Integer> getListe(){
		return liste;
	}
	
	public void removeListe(){
		liste.clear();
	}
}