package terrariumUI;

import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import controller.ProductsSales;

/**
 * StatisticUI for show statistic income in days.
 * 
 * @author Wanchanapon Thanwaranurak
 * @version 25/5/2017
 */
public class StatisticUI implements Observer {
	/** Define the null Object */
	private static final StatisticUI NOOP = null;
	/** Define the StatisticUI */
	private static StatisticUI instance = NOOP;
	private JFrame frame, jframe;
	private JTextField txtDate;
	private int number;
	private String date;
	private DefaultTableModel dmodel;
	private Map<String, List<String>> mapProductsDay;
	private JLabel labelShowTotal, lblStatistic, lblRevenueAtDate, lblTotal;
	private JButton btnSelectDate, btnClose;
	private JTable table;
	private JScrollPane scrollPane;

	/**
	 * Create constructor
	 */
	public StatisticUI() {
		mapProductsDay = new HashMap<>();
		initialize();
	}

	/**
	 * Get all the information of the TerrariumGUI.
	 * 
	 * @return the TerrariumGUI
	 */
	public static StatisticUI getInstance() {
		if (instance == null)
			instance = new StatisticUI();
		return instance;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Statistic");
		frame.setBounds(100, 100, 500, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		dmodel = new DefaultTableModel();
		initTable(dmodel);

		table = new JTable();
		table.setModel(dmodel);
		table.getColumnModel().getColumn(3).setPreferredWidth(200);

		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(27, 105, 438, 164);
		frame.getContentPane().add(scrollPane);

		lblStatistic = new JLabel("Statistic");
		lblStatistic.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		lblStatistic.setBounds(207, 6, 88, 42);
		frame.getContentPane().add(lblStatistic);

		lblRevenueAtDate = new JLabel("Revenue at date");
		lblRevenueAtDate.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblRevenueAtDate.setBounds(27, 77, 147, 16);
		frame.getContentPane().add(lblRevenueAtDate);

		txtDate = new JTextField();
		txtDate.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		txtDate.setBounds(186, 73, 129, 20);
		frame.getContentPane().add(txtDate);
		txtDate.setColumns(20);

		btnSelectDate = new JButton("...");
		btnSelectDate.setBounds(327, 70, 27, 23);
		btnSelectDate.addActionListener((e) -> {
			jframe = new JFrame();
			txtDate.setText(new DatePickerUI(jframe).setPickedDate());
			displayStatistic(ProductsSales.getInstance());
		});
		frame.getContentPane().add(btnSelectDate);

		lblTotal = new JLabel("Total");
		lblTotal.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
		lblTotal.setBounds(51, 281, 71, 34);
		frame.getContentPane().add(lblTotal);

		labelShowTotal = new JLabel("0.00");
		labelShowTotal.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
		labelShowTotal.setBounds(148, 281, 206, 30);
		labelShowTotal.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(labelShowTotal);

		btnClose = new JButton("Close");
		btnClose.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
		btnClose.setBounds(366, 281, 99, 34);
		btnClose.addActionListener((e) -> {
			close();
		});
		frame.getContentPane().add(btnClose);
	}

	/**
	 * Set the visibility to true.
	 */
	public void run() {
		frame.setVisible(true);
	}

	/**
	 * Set the visibility to false.
	 */
	public void close() {
		frame.setVisible(false);
	}

	/**
	 * Initialize the header of the table.
	 * 
	 * @param dmodel
	 *            is a TableModel to add the header.
	 */
	private void initTable(DefaultTableModel dmodel) {
		String[] COLUMN_NAMES = new String[] { "#", "Date", "Product ID", "Name", "Quantity", "Total" };
		for (String string : COLUMN_NAMES)
			dmodel.addColumn(string);
	}

	/**
	 * Clear all table model data.
	 */
	public void clearAllTable() {
		int rowCount = dmodel.getRowCount();
		for (int i = rowCount - 1; i >= 0; i--) {
			dmodel.removeRow(i);
		}
	}

	/**
	 * Set the number to zero.
	 */
	protected void setNumbertoZero() {
		number = 0;
	}

	/**
	 * Show a data of statistic in UI
	 * 
	 * @param productsSales
	 *            is a ProductsSales object that contain information of
	 *            statistic
	 */
	public void displayStatistic(ProductsSales productsSales) {
		setNumbertoZero();
		clearAllTable();
		productsSales.resetTotal();
		clearAllTable();
		date = txtDate.getText();
		mapProductsDay = productsSales.getProductsDay(date);
		for (Map.Entry<String, List<String>> entry : mapProductsDay.entrySet()) {
			dmodel.addRow(new String[] { String.format("%d", ++number), date, entry.getKey(), entry.getValue().get(0),
					entry.getValue().get(1), String.format("%.2f", Double.parseDouble(entry.getValue().get(2))) });
		}
		labelShowTotal.setText(String.format("%.2f", ProductsSales.getInstance().getTotal()));
	}

	/**
	 * Update of view of statistic, when statistic change
	 */
	@Override
	public void update(Observable subject, Object info) {
		if (info != null)
			System.out.println(info);
		if (subject instanceof ProductsSales) {
			ProductsSales productsSales = (ProductsSales) subject;
			displayStatistic(productsSales);
		}
	}

}
