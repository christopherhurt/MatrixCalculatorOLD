package main;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Matrix extends JPanel {
	private static final long serialVersionUID = 5545836333855100879L;

	private float[][] values;
	private JLabel[][] labels;
	
	public Matrix(int rows, int columns){
		if(rows <= 0 || columns <= 0 || rows > 20 || columns > 20) return;
		
		values = new float[rows][columns];
		labels = new JLabel[rows][columns];
		
		setBorder(new EmptyBorder(0, 25, 0, 0));
		setLayout(new GridLayout(rows, columns));
		
		for(int r = 0; r < labels.length; r++){
			for(int c = 0; c < labels[0].length; c++){
				labels[r][c] = new JLabel("0");
				add(labels[r][c]);
			}
		}
	}
	
	public float getValue(int row, int column){
		return values[row][column];
	}
	
	public void setValue(float value, int row, int column){
		values[row][column] = value;
		if(value % 1 == 0){
			labels[row][column].setText((int) value + "");
		}else{
			labels[row][column].setText(value + "");
		}
	}
	
	public int getRows(){
		return values.length;
	}
	
	public int getColumns(){
		return values[0].length;
	}
	
}
