package Project;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.Font;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.CyclicBehaviour;
import java.util.*;

public class ProjectGUI {//extends Agent{

	public JFrame frame;
	public String appliance1Name = "Appliance 1";
	public String appliance1MinUsageRate = "0";
	public String appliance1MaxUsageRate = "0";
	public String appliance1PowerUpdate = "0";
	
	public String appliance2Name = "Appliance 2";
	public String appliance2MinUsageRate = "0";
	public String appliance2MaxUsageRate = "0";
	public String appliance2PowerUpdate = "0";
	
	public String appliance3Name = "Appliance 3";
	public String appliance3MinUsageRate = "0";
	public String appliance3MaxUsageRate = "0";
	public String appliance3PowerUpdate = "0";
	
	public String homeIncome = "0";
	public String homeTradeUpdate = "0";
	
	public String retailer1MinPrice = "0";
	public String retailer2Price = "0";
	public String retailer3MinPrice = "0";
	public String retailer1MaxPrice = "0";
	public String retailer3MaxPrice = "0";
	public String retailer1Name = "Retailer 1";
	public String retailer2Name = "Retailer 2";
	public String retailer3Name = "Retailer 3";
	public String homeMinMonthlyIncome;
	public String homeMaxMonthlyIncome;
	public String homeMonthlyIncomeUpdate;
	
	protected void setup () {
//		Object[] args = getArguments();
//		appliance1GenerationRate = args[0].toString();
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProjectGUI window = new ProjectGUI();
					window.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void SetValue(String[] argument) {
		appliance1Name = argument[0];
		appliance1MinUsageRate = argument[1];
		appliance1PowerUpdate = argument[2];
		appliance2Name = argument[3];
		appliance2MinUsageRate = argument[4];
		appliance2PowerUpdate = argument[5];
		appliance3Name = argument[6];
		appliance3MinUsageRate = argument[7];
		appliance3PowerUpdate = argument[8];
		
		homeIncome = argument[9];
		homeTradeUpdate = argument[10];
		
		retailer1MinPrice = argument[11];
		retailer2Price = argument[12];
		retailer3MinPrice = argument[13];
		retailer1Name = argument [14];
		retailer2Name = argument [15];
		retailer3Name = argument [16];
		appliance1MaxUsageRate = argument[17];
		appliance2MaxUsageRate = argument[18];
		appliance3MaxUsageRate = argument[19];
		retailer1MaxPrice = argument[20];
		retailer3MaxPrice = argument[21];
		homeMinMonthlyIncome = argument[22];
		homeMaxMonthlyIncome = argument[23];
		homeMonthlyIncomeUpdate = argument[24];
	}
	/**
	 * Create the application.
	 */
	public ProjectGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 643, 516);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblHomeEnergySystem = new JLabel("Home Energy System");
		lblHomeEnergySystem.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblHomeEnergySystem.setBounds(201, 13, 228, 25);
		frame.getContentPane().add(lblHomeEnergySystem);
		
		JLabel lblOurNames = new JLabel("Trieu Hoang Nguyen, Qiyuan Zhu, Darcy Flood & Patrick Carty");
		lblOurNames.setBounds(116, 42, 364, 16);
		frame.getContentPane().add(lblOurNames);
		
		JLabel lblNewLabel = new JLabel(appliance1Name);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel.setBounds(12, 84, 187, 16);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblUsageRate = new JLabel("Min Usage Rate:");
		lblUsageRate.setBounds(12, 113, 113, 16);
		frame.getContentPane().add(lblUsageRate);
		
		JLabel label_2 = new JLabel(appliance1MinUsageRate);
		label_2.setBounds(122, 113, 56, 16);
		frame.getContentPane().add(label_2);
		
		JLabel lblPowerUpdate = new JLabel("Power Update:");
		lblPowerUpdate.setBounds(12, 157, 113, 16);
		frame.getContentPane().add(lblPowerUpdate);
		
		JLabel label_4 = new JLabel(appliance1PowerUpdate);
		label_4.setBounds(122, 157, 56, 16);
		frame.getContentPane().add(label_4);
		
		JLabel lblApplication = new JLabel(appliance2Name);
		lblApplication.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblApplication.setBounds(12, 210, 187, 16);
		frame.getContentPane().add(lblApplication);
		
		JLabel lblMinUsageRate = new JLabel("Min Usage Rate:");
		lblMinUsageRate.setBounds(12, 239, 113, 16);
		frame.getContentPane().add(lblMinUsageRate);
		
		JLabel lblPowerUpdate_1 = new JLabel("Power Update:");
		lblPowerUpdate_1.setBounds(12, 284, 113, 16);
		frame.getContentPane().add(lblPowerUpdate_1);
		
		JLabel label_7 = new JLabel(appliance2PowerUpdate);
		label_7.setBounds(122, 284, 56, 16);
		frame.getContentPane().add(label_7);
		
		JLabel label_8 = new JLabel(appliance2MinUsageRate);
		label_8.setBounds(122, 239, 56, 16);
		frame.getContentPane().add(label_8);
		
		JLabel lblApplication_1 = new JLabel(appliance3Name);
		lblApplication_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblApplication_1.setBounds(12, 344, 187, 16);
		frame.getContentPane().add(lblApplication_1);
		
		JLabel lblMinUsageRate_1 = new JLabel("Min Usage Rate:");
		lblMinUsageRate_1.setBounds(12, 373, 113, 16);
		frame.getContentPane().add(lblMinUsageRate_1);
		
		JLabel label_13 = new JLabel("Power Update");
		label_13.setBounds(12, 414, 113, 16);
		frame.getContentPane().add(label_13);
		
		JLabel label_14 = new JLabel(appliance3PowerUpdate);
		label_14.setBounds(122, 414, 56, 16);
		frame.getContentPane().add(label_14);
		
		JLabel label_15 = new JLabel(appliance3MinUsageRate);
		label_15.setBounds(122, 373, 56, 16);
		frame.getContentPane().add(label_15);
		
		JLabel lblHome = new JLabel("Home");
		lblHome.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblHome.setBounds(272, 210, 56, 16);
		frame.getContentPane().add(lblHome);
		
		JLabel lblIncome = new JLabel("Initial Income:");
		lblIncome.setBounds(235, 239, 82, 16);
		frame.getContentPane().add(lblIncome);
		
		JLabel label_17 = new JLabel(homeIncome);
		label_17.setBounds(325, 239, 82, 16);
		frame.getContentPane().add(label_17);
		
		JLabel lblTradeUpdate = new JLabel("Trade Update:");
		lblTradeUpdate.setBounds(235, 261, 93, 16);
		frame.getContentPane().add(lblTradeUpdate);
		
		JLabel label_10 = new JLabel(homeTradeUpdate);
		label_10.setBounds(325, 261, 82, 16);
		frame.getContentPane().add(label_10);
		
		JLabel lblRetailer = new JLabel(retailer1Name);
		lblRetailer.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblRetailer.setBounds(444, 84, 147, 16);
		frame.getContentPane().add(lblRetailer);
		
		JLabel label_20 = new JLabel(retailer1MinPrice);
		label_20.setBounds(557, 113, 56, 16);
		frame.getContentPane().add(label_20);
		
		JLabel lblPrice = new JLabel("Min Price:");
		lblPrice.setBounds(447, 113, 113, 16);
		frame.getContentPane().add(lblPrice);
		
		JLabel lblRetailer_1 = new JLabel(retailer2Name);
		lblRetailer_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblRetailer_1.setBounds(447, 210, 144, 16);
		frame.getContentPane().add(lblRetailer_1);
		
		JLabel lblFixedPrice = new JLabel("Fixed Price:");
		lblFixedPrice.setBounds(447, 239, 113, 16);
		frame.getContentPane().add(lblFixedPrice);
		
		JLabel label_23 = new JLabel(retailer2Price);
		label_23.setBounds(557, 239, 56, 16);
		frame.getContentPane().add(label_23);
		
		JLabel lblRetailer_2 = new JLabel(retailer3Name);
		lblRetailer_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblRetailer_2.setBounds(447, 344, 166, 16);
		frame.getContentPane().add(lblRetailer_2);
		
		JLabel lblMinprice = new JLabel("Min Price:");
		lblMinprice.setBounds(447, 375, 113, 16);
		frame.getContentPane().add(lblMinprice);
		
		JLabel label_28 = new JLabel(retailer3MinPrice);
		label_28.setBounds(557, 375, 56, 16);
		frame.getContentPane().add(label_28);
		
		JLabel lblMaxPrice = new JLabel("Max Price:");
		lblMaxPrice.setBounds(447, 135, 113, 16);
		frame.getContentPane().add(lblMaxPrice);
		
		JLabel lblRm = new JLabel(retailer1MaxPrice);
		lblRm.setBounds(557, 135, 56, 16);
		frame.getContentPane().add(lblRm);
		
		JLabel lblRm_1 = new JLabel(retailer3MaxPrice);
		lblRm_1.setBounds(557, 395, 56, 16);
		frame.getContentPane().add(lblRm_1);
		
		JLabel label_3 = new JLabel("Max Price:");
		label_3.setBounds(447, 395, 113, 16);
		frame.getContentPane().add(label_3);
		
		JLabel lblMaxUsageRate = new JLabel("Max Usage Rate:");
		lblMaxUsageRate.setBounds(12, 135, 113, 16);
		frame.getContentPane().add(lblMaxUsageRate);
		
		JLabel lblAm = new JLabel(appliance1MaxUsageRate);
		lblAm.setBounds(122, 135, 56, 16);
		frame.getContentPane().add(lblAm);
		
		JLabel lblAm_1 = new JLabel(appliance2MaxUsageRate);
		lblAm_1.setBounds(122, 261, 56, 16);
		frame.getContentPane().add(lblAm_1);
		
		JLabel lblMaxUsageRate_1 = new JLabel("Max Usage Rate:");
		lblMaxUsageRate_1.setBounds(12, 261, 113, 16);
		frame.getContentPane().add(lblMaxUsageRate_1);
		
		JLabel lblAm_2 = new JLabel(appliance3MaxUsageRate);
		lblAm_2.setBounds(122, 396, 56, 16);
		frame.getContentPane().add(lblAm_2);
		
		JLabel lblMaxUsageRate_2 = new JLabel("Max Usage Rate:");
		lblMaxUsageRate_2.setBounds(12, 396, 113, 16);
		frame.getContentPane().add(lblMaxUsageRate_2);
		
		JLabel lblMimi = new JLabel(homeMinMonthlyIncome);
		lblMimi.setBounds(325, 284, 82, 16);
		frame.getContentPane().add(lblMimi);
		
		JLabel lblMinMonthlyIncome = new JLabel("Min Monthly Income:");
		lblMinMonthlyIncome.setBounds(198, 284, 119, 16);
		frame.getContentPane().add(lblMinMonthlyIncome);
		
		JLabel lblMiu = new JLabel(homeMonthlyIncomeUpdate);
		lblMiu.setBounds(325, 328, 82, 16);
		frame.getContentPane().add(lblMiu);
		
		JLabel lblMonthlyIncomeUpdate = new JLabel("Monthly Income Update:");
		lblMonthlyIncomeUpdate.setBounds(178, 328, 139, 16);
		frame.getContentPane().add(lblMonthlyIncomeUpdate);
		
		JLabel lblMami = new JLabel(homeMaxMonthlyIncome);
		lblMami.setBounds(325, 305, 82, 16);
		frame.getContentPane().add(lblMami);
		
		JLabel lblMaxMonthlyIncome = new JLabel("Max Monthly Income:");
		lblMaxMonthlyIncome.setBounds(198, 305, 130, 16);
		frame.getContentPane().add(lblMaxMonthlyIncome);
	}
}
