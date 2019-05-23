package main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Solution extends JFrame {
	private static final long serialVersionUID = 1894651726009228943L;
	
	public static final int REF	= 				0;
	public static final int RREF = 				1;
	public static final int DETERMINANT = 		2;
	public static final int TRANSPOSE = 		3;
	public static final int UPPER_TRIANGULAR =	4;
	public static final int EIGENVALUES = 		5;
	
	private JPanel matrixPanel = new JPanel();
	private JLabel[][] values;
	private JLabel solution;
	
	private Matrix tempMatrix;
	private boolean[][] isPivot;
	
	private boolean error;
	
	public Solution(Matrix matrix, int type){
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setResizable(false);
		matrixPanel.setBorder(new EmptyBorder(0, 25, 0, 0));
		tempMatrix = copyMatrix(matrix);
		isPivot = new boolean[tempMatrix.getRows()][tempMatrix.getColumns()];
		error = false;
		
		switch(type){
		case REF:
			setSize(800, 600);
			REF();
			printMatrix(tempMatrix);
			break;
		case RREF:
			setSize(800, 600);
			RREF();
			printMatrix(tempMatrix);
			revalidate();
			break;
		case DETERMINANT:
			if(tempMatrix.getColumns() != tempMatrix.getRows()) sizeError();
			else {setSize(200, 200); determinant();}
			break;
		case TRANSPOSE:
			setSize(800, 600);
			transpose(matrix);
			break;
		case UPPER_TRIANGULAR:
			setSize(800, 600);
			upperTriangular();
			printMatrix(tempMatrix);
			break;
		case EIGENVALUES:
			if(tempMatrix.getColumns() != tempMatrix.getRows()) sizeError();
			else {setSize(200, 200); eigenValues();}
			break;
		}
		
		add(matrixPanel);
		setLocationRelativeTo(null);
		if(!error) setVisible(true);
	}
	
	public void sizeError(){
		JOptionPane.showMessageDialog(null, "This function requires the matrix to be square.");
		error = true;
	}
	
	public Matrix copyMatrix(Matrix matrix){
		Matrix tempMatrix = new Matrix(matrix.getRows(), matrix.getColumns());
		for(int r = 0; r < matrix.getRows(); r++)
			for(int c = 0; c < matrix.getColumns(); c++)
				tempMatrix.setValue(matrix.getValue(r, c), r, c);
		return tempMatrix;
	}
	
	public void printMatrix(Matrix matrix){
		matrixPanel.setLayout(new GridLayout(matrix.getRows(), matrix.getColumns()));
		values = new JLabel[matrix.getRows()][matrix.getColumns()];
		for(int r = 0; r < values.length; r++){
			for(int c = 0; c < values[0].length; c++){
				float tempValue = matrix.getValue(r, c);
				if(tempValue % 1 == 0) values[r][c] = new JLabel((int) tempValue + "");
				else values[r][c] = new JLabel(tempValue + "");
				matrixPanel.add(values[r][c]);
			}
		}
	}
	
	public int REF(){
		int pivots = 0;
		for(int c = 0; c < tempMatrix.getColumns(); c++){
			boolean rowReduced = false;
			boolean pivotStored = false;
			while(!rowReduced){
				rowReduced = true;
				for(int r = pivots; r < tempMatrix.getRows(); r++){
					float tempValue = tempMatrix.getValue(r, c);
					if(tempValue != 0){
						if(!pivotStored) {isPivot[r][c] = true; pivotStored = true;}
						if(!(tempValue == 1 && r == pivots)){
							if(r == pivots) scaleRow(r, 1 / tempValue);
							if(r != pivots && tempMatrix.getValue(pivots, c) == 0) switchRows(pivots, r);
							if(r != pivots && tempMatrix.getValue(pivots, c) == 1) addRow(pivots, r, -tempValue);
							rowReduced = false;
						}
					}
				}
			}
			if(pivotStored) pivots++;
		}
		return pivots;
	}
	
	public void switchRows(int row1, int row2){
		for(int c = 0; c < tempMatrix.getColumns(); c++){
			float tempValue = tempMatrix.getValue(row1, c);
			tempMatrix.setValue(tempMatrix.getValue(row2, c), row1, c);
			tempMatrix.setValue(tempValue, row2, c);
		}
	}
	
	public void scaleRow(int row, float scale){
		for(int c = 0; c < tempMatrix.getColumns(); c++){
			float ogValue = tempMatrix.getValue(row, c);
			tempMatrix.setValue(ogValue * scale, row, c);
		}
	}
	
	public void addRow(int ogRow, int destRow, float scale){
		for(int c = 0; c < tempMatrix.getColumns(); c++){
			float tempValue = tempMatrix.getValue(ogRow, c);
			float ogValue = tempMatrix.getValue(destRow, c);
			tempMatrix.setValue(tempValue * scale + ogValue, destRow, c);
		}
	}
	
	public void RREF(){
		int pivots = REF();
		for(int c = tempMatrix.getColumns() - 1; c >= 0; c--){
			for(int r = pivots - 1; r >= 0; r--){
				float tempValue = tempMatrix.getValue(r, c);
				if(tempValue != 0 && r != pivots - 1 && isPivot[pivots - 1][c]) addRow(pivots - 1, r, -tempValue);
			}
			for(int r = 0; r < tempMatrix.getRows(); r++)
				if(isPivot[r][c]) pivots--;
		}
	}
	
	public void determinant(){
		upperTriangular();
		float det = 1;
		for(int i = 0; i < tempMatrix.getRows(); i++)
			det *= tempMatrix.getValue(i, i);
		printDet(det);
	}
	
	public void printDet(float det){
		matrixPanel.setLayout(new BorderLayout());
		if(det % 1 == 0) solution = new JLabel("det(A) = " + (int) det);
		else solution = new JLabel("det(A) = " + det);
		matrixPanel.add(solution, BorderLayout.CENTER);
	}

	public void transpose(Matrix matrix){
		matrixPanel.setLayout(new GridLayout(matrix.getColumns(), matrix.getRows()));
		values = new JLabel[matrix.getColumns()][matrix.getRows()];
		
		for(int c = 0; c < matrix.getColumns(); c++){
			for(int r = 0; r < matrix.getRows(); r++){
				values[c][r] = new JLabel();
				if(matrix.getValue(r, c) % 1 == 0) values[c][r].setText((int) matrix.getValue(r, c) + "");
				else values[c][r].setText(matrix.getValue(r, c) + "");
				matrixPanel.add(values[c][r]);
			}
		}
	}

	public void upperTriangular(){
		int pivots = 0;
		float[] scales = new float[tempMatrix.getRows()];
		for(int i = 0; i < scales.length; i++) scales[i] = 1;
		for(int c = 0; c < tempMatrix.getColumns(); c++){
			boolean rowReduced = false;
			boolean pivotStored = false;
			while(!rowReduced){
				rowReduced = true;
				for(int r = pivots; r < tempMatrix.getRows(); r++){
					float tempValue = tempMatrix.getValue(r, c);
					if(tempValue != 0){
						if(!pivotStored) {scales[r] = tempValue; isPivot[r][c] = true; pivotStored = true;}
						if(!(tempValue == 1 && r == pivots)){
							if(r == pivots) scaleRow(r, 1 / tempValue);
							if(r != pivots && tempMatrix.getValue(pivots, c) == 0) switchRows(pivots, r);
							if(r != pivots && tempMatrix.getValue(pivots, c) == 1) addRow(pivots, r, -tempValue);
							rowReduced = false;
						}
					}
				}
			}
			if(pivotStored) pivots++;
		}
		for(int r = 0; r < tempMatrix.getRows(); r++){
			for(int c = 0; c < tempMatrix.getColumns(); c++){
				float tempValue = tempMatrix.getValue(r, c);
				tempMatrix.setValue(tempValue * scales[r], r, c);
				float newValue = tempMatrix.getValue(r, c);
				if(Math.abs(Math.round(newValue) - newValue) < 0.000001)
					tempMatrix.setValue((float) Math.round(newValue), r, c);
			}
		}
	}

	public void eigenValues(){
		upperTriangular();
		ArrayList<Float> eigens = new ArrayList<Float>();
		for(int i = 0; i < tempMatrix.getRows(); i++)
			eigens.add(tempMatrix.getValue(i, i));
		printEigens(eigens);
	}
	
	public void printEigens(ArrayList<Float> eigens){
		matrixPanel.setLayout(new BorderLayout());
		solution = new JLabel("λ = ");
		for(int i = 0; i < eigens.size(); i++){
			float value = eigens.get(i);
			if(value % 1 == 0)
				solution.setText(solution.getText() + (int) value);
			else
				solution.setText(solution.getText() + value);
			if(i < eigens.size() - 1) solution.setText(solution.getText() + ", ");
		}
		matrixPanel.add(solution, BorderLayout.CENTER);
	}
	
}