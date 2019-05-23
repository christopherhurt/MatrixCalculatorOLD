package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = -693036000167528691L;

	String[] functions = {
		"REF",
		"RREF",
		"Determinant",
		"Transpose",
		"Upper Triangular",
		"Eigenvalues"
	};
	
	private Matrix matrix = new Matrix(0, 0);
	
	private JPanel buttons = new JPanel();
	private JButton newMatrix = new JButton("New");
	private JButton edit = new JButton("Edit");
	private JButton calculate = new JButton("Calculate");
	private JComboBox options = new JComboBox(functions);
	
	public GUI(){
		super("Matrix Calculator");
		setSize(800, 600);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		newMatrix.addActionListener(this);
		edit.addActionListener(this);
		calculate.addActionListener(this);
		
		buttons.add(newMatrix);
		buttons.add(edit);
		buttons.add(options);
		buttons.add(calculate);
		
		setLayout(new BorderLayout());
		add(matrix, BorderLayout.CENTER);
		add(buttons, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		Object src = e.getSource();
		if(src == newMatrix){
			int rows = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter number of rows for new matrix:"));
			int columns = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter number of columns for new matrix:"));
			
			remove(matrix);
			matrix = new Matrix(rows, columns);
			add(matrix, BorderLayout.CENTER);
			revalidate();
		}else if(src == edit){
			for(int r = 0; r < matrix.getRows(); r++){
				for(int c = 0; c < matrix.getColumns(); c++){
					float value = Float.parseFloat(JOptionPane.showInputDialog(null,
							"Enter value for row " + (r + 1) + " column " + (c + 1) + ":"));
					matrix.setValue(value, r, c);
				}
			}
		}else if(src == calculate){
			switch(options.getSelectedItem().toString()){
			case "REF":
				new Solution(matrix, Solution.REF);
				break;
			case "RREF":
				new Solution(matrix, Solution.RREF);
				break;
			case "Determinant":
				new Solution(matrix, Solution.DETERMINANT);
				break;
			case "Transpose":
				new Solution(matrix, Solution.TRANSPOSE);
				break;
			case "Upper Triangular":
				new Solution(matrix, Solution.UPPER_TRIANGULAR);
				break;
			case "Eigenvalues":
				new Solution(matrix, Solution.EIGENVALUES);
				break;
			}
		}
	}
	
	public static void main(String args[]){
		new GUI();
	}

}
