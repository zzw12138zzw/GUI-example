/***************************************************************/    
/*   Program Name:     Lab6                                    */    
/*                                                             */    
/*   Student Name:     Zhengwei Zhou                           */    
/*   Semester:         Fall 2018                               */    
/*   Class-Section:    CoSc 10403-055                          */    
/*   Instructor:       Mei Bo                                  */    
/*                                                             */    
/*   Program Overview:                                         */    
/*     This program creates a Java GUI order system            */
/*     to order sandwiches, drinks and sides.                  */    
/*                                                             */    
/*   Input:                                                    */    
/*    The selection for lists, comboBox and textArea           */    
/*                                                             */    
/*   Output:                                                   */    
/*     A program displaying a GUI with 1 icon, 3 textarea,     */
/*     3 lists, 1 checkbox, 2 button, 1 buttongroup, 1combobox.*/    
/*                                                             */    
/*   Program Limitations:                                      */    
/*     The input is always considered proper                   */    
/*                                                             */ 
/*   Significant Program Variables:                            */
/*      soundFile, audioIn, clip, done                         */    
/*      Sandwiches, Drinks, Sides, cbb                         */    
/*      order, cancel, music                                   */
/*      iconlabel, orderInfo, p, a,                            */
/*      prices, specialrequirements, fry, grill                */                        
/***************************************************************/  

//the font used in this program is in the folder and might need to be installed.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*; 
import javax.sound.sampled.*;

//import the packages

public class Lab6 extends JFrame implements ListSelectionListener ,ActionListener ,ItemListener ,FocusListener {
	//naming variables
	Font                     myfont;
	ImageIcon                icon;
	JLabel                   iconlabel, label1, label2, label3, label4, label5, label6, label7, label8, label9;
	JTextArea                prices, serve, specialrequirements;
	DefaultListModel<String> model, model2, model3; 
	JList<String>            Sandwiches, Drinks, Sides;
	JButton                  order, cancel, done;	
	JCheckBox                music;
	JRadioButton             fry, grill;
	ButtonGroup              cooktype;
	JComboBox<String>        cbb;
    Color                    mycolor1, mycolor2, mycolor3;
    JPanel                   westPanel, middlePanel, southPanel;
    JFrame                   orderInfo;
    double                   p;
    File                     soundFile, soundFile2; 
    AudioInputStream         audioIn, audioIn2; 
    Clip                     clip, clip2;

	public Lab6() {
		 //set up the window
		 setSize(550,310);
         setTitle("Schnellwich");
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         setLayout(new BorderLayout());
         
         fontAndColor();
         Top();
         Left();
         Middle();
         Bottom();
         popupwindow();

         setVisible(true);
	}
    
	//set font and color
	public void fontAndColor() {
		 myfont   = new Font("Serif", Font.ITALIC, 15);
    	 mycolor1 = new Color(255, 182, 193);
         mycolor2 = new Color(135, 206, 250);
         mycolor3 = new Color(255, 255, 30);
    }
    
	//set up the top part of the window
    public void Top() {
    	 icon      = new ImageIcon("images/Picture1.png");
         iconlabel = new JLabel(icon);
         add(iconlabel,BorderLayout.NORTH);
    }
    
    //set up the left part of the window
    public void Left() {
    	 westPanel = new JPanel();
    	 westPanel.setLayout(new GridLayout(2,1,0,3));
    	 
    	 //set price text
    	 prices = new JTextArea("$5.50~Small \n $7.00~Medium \n $8.50~Large \n $9.50~Extra-Large", 4, 10);
         prices.setEditable(false);
         prices.setForeground(Color.GREEN);
         
         //set serve text
         serve = new JTextArea("Each order contains \n 1 sandwich, 1 drink \nand 1 side.");
         serve.setEditable(false);
         serve.setForeground(Color.GREEN);
         
         westPanel.add(prices);
         westPanel.add(serve);
         
         
         add(westPanel, BorderLayout.WEST);
    	
    }
    
    //set up the middle part of the window
    public void Middle() {
    	 middlePanel = new JPanel();
    	 middlePanel.setLayout(new GridLayout(1,4));
    	 
    	 //set 3 menu lists and 1 size JComboBox
    	 model      = new DefaultListModel<String>(); 
         Sandwiches = new JList<String>(model);
         addItems();
         
         model2     = new DefaultListModel<String>();
         Drinks     = new JList<String>(model2);
         addItems2();
         
         model3     = new DefaultListModel<String>();
         Sides      = new JList<String>(model3);
         addItems3();
         
         cbb = new JComboBox<String>();
         addSizeOptions();
         
         Sandwiches.setForeground(mycolor3);
         Drinks.setForeground(mycolor1);
         Sides.setForeground(mycolor2);
         cbb.setForeground(Color.ORANGE);
         
         //set selection mode for 3 menu lists
         Sandwiches.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         Drinks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         Sides.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         
         //add Listeners to JLists and JComboBox
         Sandwiches.addListSelectionListener(this);
         Drinks.addListSelectionListener(this);
         Sides.addListSelectionListener(this);
         cbb.addItemListener(this);
    	 
         middlePanel.add(Sandwiches);
         middlePanel.add(Drinks);
         middlePanel.add(Sides);
         middlePanel.add(cbb);
         
         add(middlePanel, BorderLayout.CENTER);
    }
    
    //set up the bottom part of the window
    public void Bottom() {
    	 southPanel = new JPanel();
    	 southPanel.setLayout(new FlowLayout());
    	 
    	 //set cooktype choice button
    	 fry      = new JRadioButton("Fried");
         grill    = new JRadioButton("Grilled");
         cooktype = new ButtonGroup();
         cooktype.add(fry);
         cooktype.add(grill);
         fry.setFont(myfont);
         grill.setFont(myfont);
         
         //message for requirements
         specialrequirements = new JTextArea("special requirements?",1,10);
        
         //set buttons
         order  = new JButton("Order");
         cancel = new JButton("Cancel");
         order.setBackground(Color.PINK);
         cancel.setBackground(Color.PINK);
         
         //set sound files
         try {
             soundFile  = new File("sounds/thankyou.wav");
             audioIn = AudioSystem.getAudioInputStream(soundFile);
             clip = AudioSystem.getClip(); 
             clip.open(audioIn); 
             soundFile2 = new File("sounds/yingyingying.wav");
             audioIn2 = AudioSystem.getAudioInputStream(soundFile2);
             clip2 = AudioSystem.getClip(); 
             clip2.open(audioIn2); 
         } catch (Exception e) {
        	 System.out.println("sound error");
         }
         
         //disable the cancel button
         cancel.setEnabled(false);
        	
         //add Listeners to JButtons and JRadioButtons
         order.addActionListener(this);
         cancel.addActionListener(this);
         grill.addItemListener(this);
         fry.addItemListener(this);
         specialrequirements.addFocusListener(this);
         
         //set music on/off choice box
         music = new JCheckBox("music", true);
         music.setForeground(Color.GRAY);
         
         southPanel.add(fry);
         southPanel.add(grill);
         southPanel.add(specialrequirements);
         southPanel.add(order);
         southPanel.add(cancel);
         southPanel.add(music);
         
         add(southPanel, BorderLayout.SOUTH);
    }
  //set up the pop-up window of the program
    public void popupwindow() {
    	 //define label variables
         label1 = new JLabel();
    	 label2 = new JLabel();
    	 label3 = new JLabel();
    	 label4 = new JLabel();
    	 label5 = new JLabel();
    	 label6 = new JLabel();
    	 label7 = new JLabel();
    	 label8 = new JLabel();
    	 label9 = new JLabel();
    	 
    	 //set button in the pop-up window
     	 done = new JButton("Done");
     	 done.addActionListener(this);
    	
    }
    
    //set up selection event for JLists
    public void valueChanged(ListSelectionEvent le) {
    	Object o = le.getSource();
		if(o == Sandwiches) {
			int index = Sandwiches.getSelectedIndex();
			if(index == 0) {
				label1.setText("Entree: " + "Black Forest Ham");
			} else if(index == 1) {
				label1.setText("Entree: " + "Cold cut Combo");
			} else if(index == 2) {
				label1.setText("Entree: " + "Meatball Marinana");
			} else if(index == 3) {
				label1.setText("Entree: " + "Classic Tuna");
			} else if(index == 4) {
			 	label1.setText("Entree: " + "Oven Roasted Chicken");
			} 
		} else if(o == Drinks) {
			int index = Drinks.getSelectedIndex();
			if(index == 0) {
				label2.setText("Drink: " + "Low Fat Milk");
			} else if(index == 1) {
				label2.setText("Drink: " + "Coca Cola");
			} else if(index == 2) {
				label2.setText("Drink: " + "Dr pepper");
			} else if(index == 3) {
				label2.setText("Drink: " + "Dasani");
			} else if(index == 4) {
				label2.setText("Drink: " + "Gatorade");
			} 
		} else if(o == Sides) {
			int index = Sides.getSelectedIndex();
			if(index == 0) {
				label3.setText("Side: " + "Hand-cut Fries");
			} else if(index == 1) {
				label3.setText("Side: " + "Sweet Cookies");
			} else if(index == 2) {
				label3.setText("Side: " + "Sweet Potato");
			} else if(index == 3) {
				label3.setText("Side: " + "Onion Rings");
			} else if(index == 4) {
				label3.setText("Side: " + "Cheese Curds");
			}
		} 
    }
    
    
    //set up selection event for JRadioButtons and JComboBox
    public void itemStateChanged(ItemEvent ie) {
    	Object o = ie.getSource();
		if(o == grill) {
			label6.setText("CookType: " + "Grilled");
		} else if(o == fry) {
			label6.setText("CookType: " + "Fried");
		} else if(o == cbb) {
			int index = cbb.getSelectedIndex();
			if(index == 1) {
				label4.setText("Size: " + "Small");
				label5.setText("Price: " + "$5.50");
				p = 5.5; //set p value to compute tax
			} else if(index == 2) {
				label4.setText("Size: " + "medium");
				label5.setText("Price: " + "$7.00");
				p = 7.0;
			} else if(index == 3) {
				label4.setText("Size: " + "Large");
				label5.setText("Price: " + "$8.50");
				p = 8.5;
			} else if(index == 4) {
				label4.setText("Size: " + "Extra-Large");
				label5.setText("Price: " + "$9.50");
				p = 9.5;
			}
		} 	
    }
    
    
    
    //set up action event for JButtons
    public void actionPerformed(ActionEvent ae) {
      Object obj = ae.getSource();
	 
		if(obj == order) {
			//Handle pop-up window
			orderInfo = new JFrame("OrderInfo");
			orderInfo.setSize(190, 230);
			orderInfo.setLayout(new FlowLayout());
			orderInfo.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			
			//Handle content of the special requirement JTextArea
			String a = new String(specialrequirements.getText());
			if(a.equals("special requirements?")) {
				label7.setText("Requirements: " + "None");
			} else if(a.equals("")) {
				label7.setText("Requirements: " + "None");
			} else {
				label7.setText("Requirements: " + a);
			}
			
			//Handle Tax and Total price
			double taxx = Math.round(p*8.25);
			double tax = taxx/100;
			//double test = Math.round(p*8.25)/100;
			double total = tax + p;
			label8.setText("Sales Tax: " + tax);
			label9.setText("Total: " + total);
			
			//disable the order button and enable the cancel button
			order.setEnabled(false);
			cancel.setEnabled(true);
			
			orderInfo.setResizable(false);
			
			orderInfo.add(label1);
			orderInfo.add(label2);
			orderInfo.add(label3);
			orderInfo.add(label6);
			orderInfo.add(label4);
			orderInfo.add(label5);
			orderInfo.add(label7);
			orderInfo.add(label8);
			orderInfo.add(label9);
			orderInfo.add(done);
			
			
			
			orderInfo.setVisible(true);
			
			//set sound for order
			if(music.isSelected() == true) {
			clip.setFramePosition(0);
			clip.start();
			}
			
			
		} else if(obj == cancel) {
			 //Clear all contents
			 Sandwiches.clearSelection();
			 Drinks.clearSelection();
			 Sides.clearSelection();
			 cbb.setSelectedIndex(0);
			 cooktype.clearSelection();
			 music.setSelected(false);
			 specialrequirements.setText("");
			 
			 //disable the cancel button and enable the order button
			 cancel.setEnabled(false);
		  	 order.setEnabled(true);
		  	 
		     // Dispose/close the pop-up window.
		     orderInfo.dispose();
		     
		     //set sound for cancel
		     if(music.isSelected() == true) {
		     clip2.setFramePosition(0);
			 clip2.start();
		     }
		
		} else if(obj == done) {
			// Dispose/close the pop-up window. 
			orderInfo.dispose();
			cancel.setEnabled(false);
		  	order.setEnabled(true);
		  	 
		}
    } 
    	
    
	//set the placeholder
    public void focusGained(FocusEvent e) {
		if(specialrequirements.getText().equals("special requirements?")){
			specialrequirements.setText("");
		} 
	}
    public void focusLost(FocusEvent e) {
		if(specialrequirements.getText().equals("")) {
			  specialrequirements.setText("special requirements?");
		}
	}
    
    
	//add choices for sandwich
  	public void addItems() { 
  		model.addElement("Black Forest Ham"); 
  		model.addElement("Cold cut Combo"); 
  		model.addElement("Meatball Marinana");
  		model.addElement("Classic Tuna"); 
  		model.addElement("Oven Roasted Chicken"); 
  	}
  	//add choices for drinks
  	public void addItems2() {
  		model2.addElement("Low Fat Milk"); 
  		model2.addElement("Coca Cola"); 
  		model2.addElement("Dr pepper");
  		model2.addElement("Dasani"); 
  		model2.addElement("Gatorade");
  	}
      //add choices for sides
  	public void addItems3() { 
  		model3.addElement("Hand-cut Fries"); 
  		model3.addElement("Sweet Cookies"); 
  		model3.addElement("Sweet Potato");
  		model3.addElement("Onion Rings"); 
  		model3.addElement("Cheese Curds");		
  	}
  	//add choices for size
  	public void addSizeOptions() {
  		cbb.addItem("Select a Size");
  		cbb.addItem("Small");
  		cbb.addItem("Medium");
  		cbb.addItem("Large");
  		cbb.addItem("Extra-Large");
  	}
    public static void main (String args[]) {
    	//construct the frame
		new Lab6();	
	}
    

}

