package Project;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSplitPane;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.JTextField;

//import gui.ProgramGUI;
//import gui.SettingsGUI;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.TickerBehaviour;
import java.util.*;

public class SettingsGUI {

	private JFrame frame;
	private JTextField txtApu;
	private int tradeUpdateTemp;
	private JTextField txtAur;
	private JTextField txtAur_1;
	private JTextField txtAur_2;
	private JTextField txtAur_3;
	private JTextField txtApu_1;
	private JTextField txtAur_4;
	private JTextField txtAur_5;
	private JTextField txtApu_2;
	private JTextField txtHp;
	private JTextField txtHp_1;
	private JTextField txtRi_2;
	private JTextField txtRi_3;
	private JTextField txtRi;
	private JTextField txtRi_1;
	private JTextField txtRi_4;
	private JTextField txtRi_5;
	private JTextField txtHomeTradeUpdate;
	
	//First two letters = agent (R1 = Retailer 1), middle letters = The data (GR = Generation Rate, P = Price) 
	//The Final letter = before and after (L = Low(Min), H = High(Max), F = Final (After randomizing)
	public String A1GRL = "2";
	public String A1GRH = "20";
	public Integer A1GRF;
	public String A1URL = "2";
	public String A1URH = "20";
	public Integer A1URF;
	public String A1PU = "10000";
	public String A2GRL = "2";
	public String A2GRH = "20";
	public Integer A2GRF;
	public String A2URL = "2";
	public String A2URH = "20";
	public Integer A2URF;
	public String A2PU = "10000";
	public String A3GRL = "2";
	public String A3GRH = "20";
	public Integer A3GRF;
	public String A3URL = "2";
	public String A3URH = "20";
	public Integer A3URF;
	public String A3PU = "10000";
	public String A1N = "Appliance 1";
	public String A2N = "Appliance 2";
	public String A3N = "Appliance 3";
	
	public String R1PL = "2";
	public String R1PH = "10";
	public Integer R1PF = 1;
	public String R2PL = "2";
	public String R2PH = "10";
	public Integer R2PF = 1;
	public String R3PL = "2";
	public String R3PH = "10";
	public Integer R3PF = 1;
	public String R1N = "Retailer 1";
	public String R2N = "Retailer 2";
	public String R3N = "Retailer 3";
	
	public String HIL = "900";
	public String HIH = "1100";
	public Integer HIF = 1;
	public String HTU = "15000";
	private JTextField txtAppliance;
	private JTextField txtAppliance_1;
	private JTextField txtAppliance_2;
	private JTextField txtRetailer;
	private JTextField txtRetailer_1;
	private JTextField txtRetailer_2;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SettingsGUI window = new SettingsGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SettingsGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 714, 483);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblHomeEnergySystem = new JLabel("Home Energy System Settings");
		lblHomeEnergySystem.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblHomeEnergySystem.setBounds(207, 0, 312, 29);
		frame.getContentPane().add(lblHomeEnergySystem);
		
		JLabel lblAppliance = new JLabel("Appliance 1");
		lblAppliance.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblAppliance.setBounds(60, 42, 129, 16);
		frame.getContentPane().add(lblAppliance);
		
		JLabel lblUsageRate = new JLabel("Usage Rate");
		lblUsageRate.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblUsageRate.setBounds(17, 105, 169, 16);
		frame.getContentPane().add(lblUsageRate);
		
		JLabel lblPowerUpdate = new JLabel("Power Update");
		lblPowerUpdate.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblPowerUpdate.setBounds(17, 156, 169, 16);
		frame.getContentPane().add(lblPowerUpdate);
		
		txtApu = new JTextField();
		txtApu.setText(A1PU);
		txtApu.setBounds(17, 173, 192, 22);
		frame.getContentPane().add(txtApu);
		txtApu.setColumns(10);
		
		JButton btnStartHomeEnergy = new JButton("Start Home Energy Trading System");
		btnStartHomeEnergy.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnStartHomeEnergy.setBounds(257, 326, 425, 104);
		frame.getContentPane().add(btnStartHomeEnergy);
		btnStartHomeEnergy.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  A1URH = txtAur_1.getText();
	    	  A1URL = txtAur.getText();
	    	  Random A1URR = new Random();
	    	  A1URF = A1URR.nextInt((Integer.parseInt(A1URH) - Integer.parseInt(A1URL) + 1)) + Integer.parseInt(A1URL);
	    	  
	    	  A1PU = txtApu.getText();
	    	  
	    	  A2URH = txtAur_3.getText();
	    	  A2URL = txtAur_2.getText();
	    	  Random A2URR = new Random();
	    	  A2URF = A2URR.nextInt((Integer.parseInt(A2URH) - Integer.parseInt(A2URL) + 1)) + Integer.parseInt(A2URL);
	    	  
	    	  A2PU = txtApu_1.getText();
	    	  
	    	  A3URH = txtAur_5.getText();
	    	  A3URL = txtAur_4.getText();
	    	  Random A3URR = new Random();
	    	  A3URF = A3URR.nextInt((Integer.parseInt(A3URH) - Integer.parseInt(A3URL) + 1)) + Integer.parseInt(A3URL);
	    	  
	    	  A3PU = txtApu_2.getText();
	    	  
	    	  HIH = txtHp_1.getText();
	    	  HIL = txtHp.getText();
	    	  Random HIR = new Random();
	    	  HIF = HIR.nextInt((Integer.parseInt(HIH) - Integer.parseInt(HIL) + 1)) + Integer.parseInt(HIL);
	    	  
	    	  HTU = txtHomeTradeUpdate.getText();
	    	  
	    	  R1PH = txtRi_1.getText();
	    	  R1PL = txtRi.getText();
	    	  Random R1PR = new Random();
	    	  R1PF = R1PR.nextInt((Integer.parseInt(R1PH) - Integer.parseInt(R1PL) + 1)) + Integer.parseInt(R1PL);
	    	  
	    	  R2PH = txtRi_3.getText();
	    	  R2PL = txtRi_2.getText();
	    	  Random R2PR = new Random();
	    	  R2PF = R2PR.nextInt((Integer.parseInt(R2PH) - Integer.parseInt(R2PL) + 1)) + Integer.parseInt(R2PL);
	    	  
	    	  R3PH = txtRi_5.getText();
	    	  R3PL = txtRi_4.getText();
	    	  Random R3PR = new Random();
	    	  R3PF = R3PR.nextInt((Integer.parseInt(R3PH) - Integer.parseInt(R3PL) + 1)) + Integer.parseInt(R3PL);
	    	  
	    	  A1N = txtAppliance.getText();
	    	  A2N = txtAppliance_1.getText();
	    	  A3N = txtAppliance_2.getText();
	    	  R1N = txtRetailer.getText();
	    	  R2N = txtRetailer_1.getText();
	    	  R3N = txtRetailer_2.getText();
	    	  
	    	  String[] argsMain = new String[22];
	    	  argsMain[0] = A1N.toString();
	    	  argsMain[1] = A1URL.toString();
	    	  argsMain[2] = A1PU;
	    	  
	    	  argsMain[3] = A2N.toString();
	    	  argsMain[4] = A2URL.toString();
	    	  argsMain[5] = A2PU;
	    	  
	    	  argsMain[6] = A3N.toString();
	    	  argsMain[7] = A3URL.toString();
	    	  argsMain[8] = A3PU;
	    	  
	    	  argsMain[9] = HIF.toString();
	    	  argsMain[10] = HTU;
	    	  
	    	  argsMain[11] = R1PL.toString();
	    	  argsMain[12] = R2PF.toString();
	    	  argsMain[13] = R3PL.toString();
	    	  argsMain[14] = R1N.toString();
	    	  argsMain[15] = R2N.toString();
	    	  argsMain[16] = R3N.toString();
	    	  argsMain[17] = A1URH.toString();
	    	  argsMain[18] = A2URH.toString();
	    	  argsMain[19] = A3URH.toString();
	    	  argsMain[20] = R1PH.toString();
	    	  argsMain[21] = R3PH.toString();
	    	  
	    	  String[] argsHome = new String[2];
	    	  argsHome[0] = HIF.toString();
	    	  argsHome[1] = HTU;
	    	  
	    	  String[] argsApplication1 = new String[4];
	    	  argsApplication1[0] = "Home";
	    	  argsApplication1[1] = A1URL.toString();
	    	  argsApplication1[2] = A1URH.toString();
	    	  argsApplication1[3] = A1PU;
	    	  
	    	  String[] argsApplication2 = new String[4];
	    	  argsApplication2[0] = "Home";
	    	  argsApplication2[1] = A2URL.toString();
	    	  argsApplication2[2] = A2URH.toString();
	    	  argsApplication2[3] = A2PU;
	    	  
	    	  String[] argsApplication3 = new String[4];
	    	  argsApplication3[0] = "Home";
	    	  argsApplication3[1] = A3URL.toString();
	    	  argsApplication3[2] = A3URH.toString();
	    	  argsApplication3[3] = A3PU;
	    	  
	    	  
	    	  String[] argsRetailer1 = new String[3];
	    	  argsRetailer1[0] = R1N;
	    	  argsRetailer1[1] = R1PL.toString();
	    	  argsRetailer1[2] = R1PH.toString();
	    	  
	    	  String[] argsRetailer2 = new String[2];
	    	  argsRetailer2[0] = R2N;
	    	  argsRetailer2[1] = R2PF.toString();
	    	  
	    	  String[] argsRetailer3 = new String[3];
	    	  argsRetailer3[0] = R3N;
	    	  argsRetailer3[1] = R3PL.toString();
	    	  argsRetailer3[2] = R3PH.toString();
	    	  
	    	  ProjectGUI pgui = new ProjectGUI();
	    	  SettingsGUI sgui = new SettingsGUI();
	    	  
	    	  pgui.SetValue(argsMain);
	    	  
	    	  pgui.frame.revalidate();
	    	  pgui.initialize();
	    	  pgui.frame.setVisible(true);
	    	  frame.setVisible(false);
	    	  
		  	Runtime runtime = Runtime.instance();
		       Profile profile = new ProfileImpl();
		       profile.setParameter(Profile.GUI, "true");
		       ContainerController containerController = runtime.createMainContainer(profile);

		           AgentController homeController;
		           try {
		        	   homeController = containerController.createNewAgent("Home", "Project.HomeAgent", argsHome);
		        	   homeController.start();    
		           } catch (StaleProxyException e5) {
		               e5.printStackTrace();
		           }
		           AgentController appAgentController1;
		           AgentController appAgentController2;
		           AgentController appAgentController3;
		           try {
		        	   appAgentController1 = containerController.createNewAgent(A1N, "Project.ApplicationAgent", argsApplication1);
		        	   appAgentController2 = containerController.createNewAgent(A2N, "Project.ApplicationAgent", argsApplication2);
		        	   appAgentController3 = containerController.createNewAgent(A3N, "Project.ApplicationAgent", argsApplication3);
		        	   appAgentController1.start();    
		        	   appAgentController2.start();  
		        	   appAgentController3.start();   
		           } catch (StaleProxyException e6) {
		               e6.printStackTrace();
		           }
		           AgentController retailerAgentController1;
		           AgentController retailerAgentFixedController2;
		           AgentController retailerAgentFlexibleController3;
		           try {
		               retailerAgentController1 = containerController.createNewAgent(R1N, "Project.RetailerAgent", argsRetailer1);
		               retailerAgentFixedController2 = containerController.createNewAgent(R2N, "Project.RetailerAgentFixed", argsRetailer2);
		               retailerAgentFlexibleController3 = containerController.createNewAgent(R3N, "Project.RetailerAgentFlexible", argsRetailer3);
		               retailerAgentController1.start();    
		               retailerAgentFixedController2.start();    
		               retailerAgentFlexibleController3.start();    
		           } catch (StaleProxyException e7) {
		               e7.printStackTrace();
		           }
	      }
	    });
		
		JLabel label = new JLabel("MIN:");
		label.setBounds(17, 120, 87, 16);
		frame.getContentPane().add(label);
		
		txtAur = new JTextField();
		txtAur.setToolTipText("");
		txtAur.setText(A1URL);
		txtAur.setColumns(10);
		txtAur.setBounds(17, 134, 91, 22);
		frame.getContentPane().add(txtAur);
		
		txtAur_1 = new JTextField();
		txtAur_1.setText(A1URH);
		txtAur_1.setColumns(10);
		txtAur_1.setBounds(118, 134, 91, 22);
		frame.getContentPane().add(txtAur_1);
		
		JLabel label_2 = new JLabel("MAX:");
		label_2.setBounds(118, 120, 87, 16);
		frame.getContentPane().add(label_2);
		
		JLabel lblAppliance_1 = new JLabel("Appliance 2");
		lblAppliance_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblAppliance_1.setBounds(300, 42, 129, 16);
		frame.getContentPane().add(lblAppliance_1);
		
		JLabel label_10 = new JLabel("Usage Rate");
		label_10.setFont(new Font("Tahoma", Font.BOLD, 13));
		label_10.setBounds(257, 105, 169, 16);
		frame.getContentPane().add(label_10);
		
		JLabel label_13 = new JLabel("MIN:");
		label_13.setBounds(257, 120, 87, 16);
		frame.getContentPane().add(label_13);
		
		txtAur_2 = new JTextField();
		txtAur_2.setText(A2URL);
		txtAur_2.setColumns(10);
		txtAur_2.setBounds(257, 134, 91, 22);
		frame.getContentPane().add(txtAur_2);
		
		txtAur_3 = new JTextField();
		txtAur_3.setText(A2URH);
		txtAur_3.setColumns(10);
		txtAur_3.setBounds(358, 134, 91, 22);
		frame.getContentPane().add(txtAur_3);
		
		JLabel label_14 = new JLabel("MAX:");
		label_14.setBounds(358, 120, 87, 16);
		frame.getContentPane().add(label_14);
		
		JLabel label_15 = new JLabel("Power Update");
		label_15.setFont(new Font("Tahoma", Font.BOLD, 13));
		label_15.setBounds(257, 156, 169, 16);
		frame.getContentPane().add(label_15);
		
		txtApu_1 = new JTextField();
		txtApu_1.setText(A2PU);
		txtApu_1.setColumns(10);
		txtApu_1.setBounds(257, 173, 192, 22);
		frame.getContentPane().add(txtApu_1);
		
		JLabel lblAppliance_2 = new JLabel("Appliance 3");
		lblAppliance_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblAppliance_2.setBounds(533, 42, 129, 16);
		frame.getContentPane().add(lblAppliance_2);
		
		JLabel label_20 = new JLabel("Usage Rate");
		label_20.setFont(new Font("Tahoma", Font.BOLD, 13));
		label_20.setBounds(490, 105, 169, 16);
		frame.getContentPane().add(label_20);
		
		JLabel label_21 = new JLabel("MIN:");
		label_21.setBounds(490, 120, 87, 16);
		frame.getContentPane().add(label_21);
		
		txtAur_4 = new JTextField();
		txtAur_4.setText(A3URL);
		txtAur_4.setColumns(10);
		txtAur_4.setBounds(490, 134, 91, 22);
		frame.getContentPane().add(txtAur_4);
		
		txtAur_5 = new JTextField();
		txtAur_5.setText(A3URH);
		txtAur_5.setColumns(10);
		txtAur_5.setBounds(591, 134, 91, 22);
		frame.getContentPane().add(txtAur_5);
		
		JLabel label_22 = new JLabel("MAX:");
		label_22.setBounds(591, 120, 87, 16);
		frame.getContentPane().add(label_22);
		
		JLabel label_23 = new JLabel("Power Update");
		label_23.setFont(new Font("Tahoma", Font.BOLD, 13));
		label_23.setBounds(490, 156, 169, 16);
		frame.getContentPane().add(label_23);
		
		txtApu_2 = new JTextField();
		txtApu_2.setText(A3PU);
		txtApu_2.setColumns(10);
		txtApu_2.setBounds(490, 173, 192, 22);
		frame.getContentPane().add(txtApu_2);
		
		JLabel lblHome = new JLabel("Home");
		lblHome.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblHome.setBounds(82, 326, 87, 16);
		frame.getContentPane().add(lblHome);
		
		JLabel lblPrice_1 = new JLabel("Funding Pool");
		lblPrice_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblPrice_1.setBounds(17, 341, 169, 16);
		frame.getContentPane().add(lblPrice_1);
		
		JLabel label_16 = new JLabel("MIN:");
		label_16.setBounds(17, 356, 87, 16);
		frame.getContentPane().add(label_16);
		
		txtHp = new JTextField();
		txtHp.setText(HIL);
		txtHp.setColumns(10);
		txtHp.setBounds(17, 370, 91, 22);
		frame.getContentPane().add(txtHp);
		
		txtHp_1 = new JTextField();
		txtHp_1.setText(HIH);
		txtHp_1.setColumns(10);
		txtHp_1.setBounds(118, 370, 91, 22);
		frame.getContentPane().add(txtHp_1);
		
		JLabel label_24 = new JLabel("MAX:");
		label_24.setBounds(118, 356, 87, 16);
		frame.getContentPane().add(label_24);
		
		JLabel lblRetailer = new JLabel("Retailer 2");
		lblRetailer.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblRetailer.setBounds(300, 208, 129, 16);
		frame.getContentPane().add(lblRetailer);
		
		JLabel lblIncome_1 = new JLabel("Price");
		lblIncome_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblIncome_1.setBounds(257, 262, 169, 16);
		frame.getContentPane().add(lblIncome_1);
		
		JLabel label_12 = new JLabel("MIN:");
		label_12.setBounds(257, 277, 87, 16);
		frame.getContentPane().add(label_12);
		
		txtRi_2 = new JTextField();
		txtRi_2.setText(R2PL);
		txtRi_2.setColumns(10);
		txtRi_2.setBounds(257, 291, 91, 22);
		frame.getContentPane().add(txtRi_2);
		
		txtRi_3 = new JTextField();
		txtRi_3.setText(R2PH);
		txtRi_3.setColumns(10);
		txtRi_3.setBounds(358, 291, 91, 22);
		frame.getContentPane().add(txtRi_3);
		
		JLabel label_28 = new JLabel("MAX:");
		label_28.setBounds(358, 277, 87, 16);
		frame.getContentPane().add(label_28);
		
		JLabel lblRetailer_1 = new JLabel("Retailer 1");
		lblRetailer_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblRetailer_1.setBounds(60, 208, 129, 16);
		frame.getContentPane().add(lblRetailer_1);
		
		JLabel lblPrice = new JLabel("Price");
		lblPrice.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblPrice.setBounds(17, 262, 169, 16);
		frame.getContentPane().add(lblPrice);
		
		JLabel label_8 = new JLabel("MIN:");
		label_8.setBounds(17, 277, 87, 16);
		frame.getContentPane().add(label_8);
		
		txtRi = new JTextField();
		txtRi.setText(R1PL);
		txtRi.setColumns(10);
		txtRi.setBounds(17, 291, 91, 22);
		frame.getContentPane().add(txtRi);
		
		JLabel label_25 = new JLabel("MAX:");
		label_25.setBounds(118, 277, 87, 16);
		frame.getContentPane().add(label_25);
		
		txtRi_1 = new JTextField();
		txtRi_1.setText(R1PH);
		txtRi_1.setColumns(10);
		txtRi_1.setBounds(118, 291, 91, 22);
		frame.getContentPane().add(txtRi_1);
		
		JLabel lblRetailer_2 = new JLabel("Retailer 3");
		lblRetailer_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblRetailer_2.setBounds(533, 208, 129, 16);
		frame.getContentPane().add(lblRetailer_2);
		
		JLabel lblPrice_2 = new JLabel("Price");
		lblPrice_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblPrice_2.setBounds(490, 262, 169, 16);
		frame.getContentPane().add(lblPrice_2);
		
		JLabel label_29 = new JLabel("MIN:");
		label_29.setBounds(490, 277, 87, 16);
		frame.getContentPane().add(label_29);
		
		txtRi_4 = new JTextField();
		txtRi_4.setText(R3PL);
		txtRi_4.setColumns(10);
		txtRi_4.setBounds(490, 291, 91, 22);
		frame.getContentPane().add(txtRi_4);
		
		JLabel label_31 = new JLabel("MAX:");
		label_31.setBounds(591, 277, 87, 16);
		frame.getContentPane().add(label_31);
		
		txtRi_5 = new JTextField();
		txtRi_5.setText(R3PH);
		txtRi_5.setColumns(10);
		txtRi_5.setBounds(591, 291, 91, 22);
		frame.getContentPane().add(txtRi_5);
		
		JLabel label_1 = new JLabel("Trade Update");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		label_1.setBounds(17, 393, 169, 16);
		frame.getContentPane().add(label_1);
		
		txtHomeTradeUpdate = new JTextField();
		txtHomeTradeUpdate.setText(HTU);
		txtHomeTradeUpdate.setColumns(10);
		txtHomeTradeUpdate.setBounds(17, 408, 192, 22);
		frame.getContentPane().add(txtHomeTradeUpdate);
		
		JLabel label_11 = new JLabel("------------------------------------------------------------------------------------------------------------------------------------");
		label_11.setBounds(17, 193, 665, 16);
		frame.getContentPane().add(label_11);
		
		JLabel label_26 = new JLabel("------------------------------------------------------------------------------------------------------------------------------------");
		label_26.setBounds(17, 306, 665, 16);
		frame.getContentPane().add(label_26);
		
		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblName.setBounds(17, 68, 169, 16);
		frame.getContentPane().add(lblName);
		
		txtAppliance = new JTextField();
		txtAppliance.setText(A1N);
		txtAppliance.setColumns(10);
		txtAppliance.setBounds(17, 85, 192, 22);
		frame.getContentPane().add(txtAppliance);
		
		JLabel lblName_1 = new JLabel("Name");
		lblName_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblName_1.setBounds(257, 68, 169, 16);
		frame.getContentPane().add(lblName_1);
		
		txtAppliance_1 = new JTextField();
		txtAppliance_1.setText(A2N);
		txtAppliance_1.setColumns(10);
		txtAppliance_1.setBounds(257, 85, 192, 22);
		frame.getContentPane().add(txtAppliance_1);
		
		JLabel lblName_2 = new JLabel("Name");
		lblName_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblName_2.setBounds(490, 68, 169, 16);
		frame.getContentPane().add(lblName_2);
		
		txtAppliance_2 = new JTextField();
		txtAppliance_2.setText(A3N);
		txtAppliance_2.setColumns(10);
		txtAppliance_2.setBounds(490, 85, 192, 22);
		frame.getContentPane().add(txtAppliance_2);
		
		JLabel label_4 = new JLabel("Name");
		label_4.setFont(new Font("Tahoma", Font.BOLD, 13));
		label_4.setBounds(17, 222, 169, 16);
		frame.getContentPane().add(label_4);
		
		txtRetailer = new JTextField();
		txtRetailer.setText(R1N);
		txtRetailer.setColumns(10);
		txtRetailer.setBounds(17, 239, 192, 22);
		frame.getContentPane().add(txtRetailer);
		
		JLabel label_5 = new JLabel("Name");
		label_5.setFont(new Font("Tahoma", Font.BOLD, 13));
		label_5.setBounds(257, 222, 169, 16);
		frame.getContentPane().add(label_5);
		
		txtRetailer_1 = new JTextField();
		txtRetailer_1.setText(R2N);
		txtRetailer_1.setColumns(10);
		txtRetailer_1.setBounds(257, 239, 192, 22);
		frame.getContentPane().add(txtRetailer_1);
		
		JLabel label_6 = new JLabel("Name");
		label_6.setFont(new Font("Tahoma", Font.BOLD, 13));
		label_6.setBounds(490, 222, 169, 16);
		frame.getContentPane().add(label_6);
		
		txtRetailer_2 = new JTextField();
		txtRetailer_2.setText(R3N);
		txtRetailer_2.setColumns(10);
		txtRetailer_2.setBounds(490, 239, 192, 22);
		frame.getContentPane().add(txtRetailer_2);
	}
}
