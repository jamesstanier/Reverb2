import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Frame {
	
	private JFrame Reverb2;
	private StreamAudio audio;
	static String message;
	private JCheckBox checkBox;
	private String fileName = "";
	private JSlider sliderRD1, sliderRD2, sliderRD3, sliderRD4;
	private JSlider sliderVD1, sliderVD2, sliderVD3, sliderVD4;
	private JSlider sliderGR, sliderGV;
	private JLabel lblDelayRD1_value, lblDelayRD2_value, lblDelayRD3_value, lblDelayRD4_value;
	private JLabel lblDelayVD1_value, lblDelayVD2_value, lblDelayVD3_value, lblDelayVD4_value;
	private JLabel lblGainR_value, lblGainV_value;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame window = new Frame();
					window.Reverb2.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
	
	public Frame() {
		setup();
	}
	
	private void setup() {
		Reverb2 = new JFrame();
		Reverb2.setTitle("Reverberator");
		Reverb2.setBounds(100, 100, 800, 500);
		Reverb2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Reverb2.getContentPane().setLayout(null);

		JButton btnPlay = new JButton("Play Audio");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	
			try {
				if (fileName == "") {
					System.out.println("No file selected!");
				}
				else
				{
					audio = new StreamAudio(fileName);
					audio.start();
					audio.bLoop = checkBox.isSelected();
					
					audio.processAudio.delayRD1 = (sliderRD1.getValue()/1.0f);
					audio.processAudio.delayRD2 = (sliderRD2.getValue()/1.0f);
					audio.processAudio.delayRD3 = (sliderRD3.getValue()/1.0f);
					audio.processAudio.delayRD4 = (sliderRD4.getValue()/1.0f);
					
					audio.processAudio.delayVD1 = (sliderVD1.getValue()/1000.0f);
					audio.processAudio.delayVD2 = (sliderVD2.getValue()/1000.0f);
					audio.processAudio.delayVD3 = (sliderVD3.getValue()/1000.0f);
					audio.processAudio.delayVD4 = (sliderVD4.getValue()/1000.0f);
					
					audio.processAudio.gainR = (sliderGR.getValue()/100.0f);
					audio.processAudio.gainV = (sliderGV.getValue()/100.0f);
				}
				}
			
					catch(UnsupportedAudioFileException uafe)
					{
						message = "Only WAV files supported. More formats to be supported in future.";
						uafe.printStackTrace();
						JOptionPane.showMessageDialog(Reverb2, message);
					}
					catch(FileNotFoundException fnfe)
					{
						message = "Please ensure filename and filepath is correct.";
						fnfe.printStackTrace();
					}
					catch(IOException ioe)
					{
						message = "Error occured will audio input/ output. Please try again.";
						ioe.printStackTrace();
						JOptionPane.showMessageDialog(Reverb2, message);
					}
					catch(OutOfMemoryError oome)
					{
						message = "File size too large.";
						oome.printStackTrace();
						JOptionPane.showMessageDialog(Reverb2, message);
					}
					catch(IllegalArgumentException iae)
					{
						message = "Error loading audio on system.";
						JOptionPane.showMessageDialog(Reverb2, message);
					}
					catch(Exception ex)
					{
						message = "Application stopped working. Please restart application.";
						ex.printStackTrace();
						JOptionPane.showMessageDialog(Reverb2, message);
					}

			}
		});
		
		//This button is to stop Audio playback. On button press, the method 'stopTheMusic' is called from the class Reverberator.
		btnPlay.setBounds(250, 400, 97, 25);
		Reverb2.getContentPane().add(btnPlay);
		
	    checkBox = new JCheckBox("Loop");    
	    checkBox.addItemListener(new ItemListener() {    
            public void itemStateChanged(ItemEvent e) {                 
               if (audio != null) audio.bLoop = (e.getStateChange()==1?true:false);    
            }    
         });    
	    checkBox.setBounds(180, 400, 70, 25);
	    Reverb2.add(checkBox);

		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (audio != null) audio.bStop = true;
			}
		});
		btnStop.setBounds(350, 400, 97, 25);
		Reverb2.getContentPane().add(btnStop);

		//Create a file chooser
		JFileChooser fc = new JFileChooser();
		
		// This is for the 'Browse file' button. It will pop up the file chooser window and copy the path of selected file into the fileName variable
		JButton btnBrowse = new JButton("Browse");
		fc.addChoosableFileFilter(new FileNameExtensionFilter("Wave Files", "wav"));
		fc.setAcceptAllFileFilterUsed(false);
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (arg0.getSource() == btnBrowse) {
			        int returnVal = fc.showOpenDialog(null);

			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fc.getSelectedFile();
			            //This is where the file path gets copied.
			            	fileName = file.getPath();
			        } else {
			        }
				}
			}
		});
		btnBrowse.setBounds(50, 400, 97, 25);
		Reverb2.getContentPane().add(btnBrowse);
	
		//This slider is for the Delay RD1 parameter
		sliderRD1 = new JSlider(JSlider.HORIZONTAL, 0, 500, 100);
		sliderRD1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				
				if (audio != null) audio.processAudio.delayRD1 = (sliderRD1.getValue()/1.0f);
				lblDelayRD1_value.setText(Integer.toString(sliderRD1.getValue()) + " ms");
			}
		});
		sliderRD1.setBounds(100, 50, 250, 25);
		Reverb2.getContentPane().add(sliderRD1);
	
		//This slider is for the Delay RD2 parameter
		sliderRD2 = new JSlider(JSlider.HORIZONTAL, 0, 500, 160);
		sliderRD2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				
				if (audio != null) audio.processAudio.delayRD2 = (sliderRD2.getValue()/1.0f);
				lblDelayRD2_value.setText(Integer.toString(sliderRD2.getValue()) + " ms");
			}
		});
		sliderRD2.setBounds(100, 100, 250, 25);
		Reverb2.getContentPane().add(sliderRD2);
	
		//This slider is for the Delay RD3 parameter
		sliderRD3 = new JSlider(JSlider.HORIZONTAL, 0, 500, 220);
		sliderRD3.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				
				if (audio != null) audio.processAudio.delayRD3 = (sliderRD3.getValue()/1.0f);
				lblDelayRD3_value.setText(Integer.toString(sliderRD3.getValue()) + " ms");
			}
		});
		sliderRD3.setBounds(100, 150, 250, 25);
		Reverb2.getContentPane().add(sliderRD3);
	
		//This slider is for the Delay RD4 parameter
		sliderRD4 = new JSlider(JSlider.HORIZONTAL, 0, 500, 280);
		sliderRD4.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				
				if (audio != null) audio.processAudio.delayRD4 = (sliderRD4.getValue()/1.0f);
				lblDelayRD4_value.setText(Integer.toString(sliderRD4.getValue()) + " ms");
			}
		});
		sliderRD4.setBounds(100, 200, 250, 25);
		Reverb2.getContentPane().add(sliderRD4);
	
		//This slider is for the Delay VD1 parameter
		sliderVD1 = new JSlider(JSlider.HORIZONTAL, 0, 500, 100);
		sliderVD1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				
				if (audio != null) audio.processAudio.delayVD1 = (sliderVD1.getValue()/1000.0f);
				lblDelayVD1_value.setText(Integer.toString(sliderVD1.getValue()) + " us");
			}
		});
		sliderVD1.setBounds(450, 50, 250, 25);
		Reverb2.getContentPane().add(sliderVD1);
	
		//This slider is for the Delay VD2 parameter
		sliderVD2 = new JSlider(JSlider.HORIZONTAL, 0, 500, 160);
		sliderVD2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				
				if (audio != null) audio.processAudio.delayVD2 = (sliderVD2.getValue()/1000.0f);
				lblDelayVD2_value.setText(Integer.toString(sliderVD2.getValue()) + " us");
			}
		});
		sliderVD2.setBounds(450, 100, 250, 25);
		Reverb2.getContentPane().add(sliderVD2);
	
		//This slider is for the Delay VD3 parameter
		sliderVD3 = new JSlider(JSlider.HORIZONTAL, 0, 500, 220);
		sliderVD3.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				
				if (audio != null) audio.processAudio.delayVD3 = (sliderVD3.getValue()/1000.0f);
				lblDelayVD3_value.setText(Integer.toString(sliderVD3.getValue()) + " us");
			}
		});
		sliderVD3.setBounds(450, 150, 250, 25);
		Reverb2.getContentPane().add(sliderVD3);
	
		//This slider is for the Delay VD4 parameter
		sliderVD4 = new JSlider(JSlider.HORIZONTAL, 0, 500, 280);
		sliderVD4.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				
				if (audio != null) audio.processAudio.delayVD4 = (sliderVD4.getValue()/1000.0f);
				lblDelayVD4_value.setText(Integer.toString(sliderVD4.getValue()) + " us");
			}
		});
		sliderVD4.setBounds(450, 200, 250, 25);
		Reverb2.getContentPane().add(sliderVD4);
	
		//This slider is for the Gain R parameter
		sliderGR = new JSlider(JSlider.HORIZONTAL, 0, 99, 70);
		sliderGR.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				
				if (audio != null) audio.processAudio.gainR = (sliderGR.getValue()/100.0f);
				lblGainR_value.setText(Float.toString(sliderGR.getValue()/100.0f));
			}
		});
		sliderGR.setBounds(100, 300, 250, 25);
		Reverb2.getContentPane().add(sliderGR);
	
		//This slider is for the Gain V parameter
		sliderGV = new JSlider(JSlider.HORIZONTAL, 0, 99, 70);
		sliderGV.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				
				if (audio != null) audio.processAudio.gainV = (sliderGV.getValue()/100.0f);
				lblGainV_value.setText(Float.toString(sliderGV.getValue()/100.0f));
			}
		});
		sliderGV.setBounds(450, 300, 250, 25);
		Reverb2.getContentPane().add(sliderGV);

		JLabel lblTitle = new JLabel("First Configuration Reverb Engine");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(200, 20, 400, 16);
		Reverb2.getContentPane().add(lblTitle);

		JLabel lblCredits = new JLabel("<html>Created by James Stanier 2023<br/>(with help from Rishikesh Daoo)<html>");
		lblCredits.setHorizontalAlignment(SwingConstants.CENTER);
		lblCredits.setBounds(500, 400, 200, 32);
		Reverb2.getContentPane().add(lblCredits);

		JLabel lblDelayRD1 = new JLabel("RD1");
		lblDelayRD1.setHorizontalAlignment(SwingConstants.CENTER);
		lblDelayRD1.setBounds(50, 50, 50, 16);
		Reverb2.getContentPane().add(lblDelayRD1);

		JLabel lblDelayRD2 = new JLabel("RD2");
		lblDelayRD2.setHorizontalAlignment(SwingConstants.CENTER);
		lblDelayRD2.setBounds(50, 100, 50, 16);
		Reverb2.getContentPane().add(lblDelayRD2);

		JLabel lblDelayRD3 = new JLabel("RD3");
		lblDelayRD3.setHorizontalAlignment(SwingConstants.CENTER);
		lblDelayRD3.setBounds(50, 150, 50, 16);
		Reverb2.getContentPane().add(lblDelayRD3);

		JLabel lblDelayRD4 = new JLabel("RD4");
		lblDelayRD4.setHorizontalAlignment(SwingConstants.CENTER);
		lblDelayRD4.setBounds(50, 200, 50, 16);
		Reverb2.getContentPane().add(lblDelayRD4);

		JLabel lblDelayVD1 = new JLabel("VD1");
		lblDelayVD1.setHorizontalAlignment(SwingConstants.CENTER);
		lblDelayVD1.setBounds(400, 50, 50, 16);
		Reverb2.getContentPane().add(lblDelayVD1);

		JLabel lblDelayVD2 = new JLabel("VD2");
		lblDelayVD2.setHorizontalAlignment(SwingConstants.CENTER);
		lblDelayVD2.setBounds(400, 100, 50, 16);
		Reverb2.getContentPane().add(lblDelayVD2);

		JLabel lblDelayVD3 = new JLabel("VD3");
		lblDelayVD3.setHorizontalAlignment(SwingConstants.CENTER);
		lblDelayVD3.setBounds(400, 150, 50, 16);
		Reverb2.getContentPane().add(lblDelayVD3);

		JLabel lblDelayVD4 = new JLabel("VD4");
		lblDelayVD4.setHorizontalAlignment(SwingConstants.CENTER);
		lblDelayVD4.setBounds(400, 200, 50, 16);
		Reverb2.getContentPane().add(lblDelayVD4);

		JLabel lblGainR = new JLabel("Gain R");
		lblGainR.setHorizontalAlignment(SwingConstants.CENTER);
		lblGainR.setBounds(50, 300, 50, 16);
		Reverb2.getContentPane().add(lblGainR);

		JLabel lblGainV = new JLabel("Gain V");
		lblGainV.setHorizontalAlignment(SwingConstants.CENTER);
		lblGainV.setBounds(400, 300, 50, 16);
		Reverb2.getContentPane().add(lblGainV);

		lblDelayRD1_value = new JLabel(Integer.toString(sliderRD1.getValue()) + " ms");
		lblDelayRD1_value.setHorizontalAlignment(SwingConstants.CENTER);
		lblDelayRD1_value.setForeground(Color.GRAY);
		lblDelayRD1_value.setBounds(350, 50, 56, 16);
		Reverb2.getContentPane().add(lblDelayRD1_value);
		
		lblDelayRD2_value = new JLabel(Integer.toString(sliderRD2.getValue()) + " ms");
		lblDelayRD2_value.setHorizontalAlignment(SwingConstants.CENTER);
		lblDelayRD2_value.setForeground(Color.GRAY);
		lblDelayRD2_value.setBounds(350, 100, 56, 16);
		Reverb2.getContentPane().add(lblDelayRD2_value);
		
		lblDelayRD3_value = new JLabel(Integer.toString(sliderRD3.getValue()) + " ms");
		lblDelayRD3_value.setHorizontalAlignment(SwingConstants.CENTER);
		lblDelayRD3_value.setForeground(Color.GRAY);
		lblDelayRD3_value.setBounds(350, 150, 56, 16);
		Reverb2.getContentPane().add(lblDelayRD3_value);
		
		lblDelayRD4_value = new JLabel(Integer.toString(sliderRD4.getValue()) + " ms");
		lblDelayRD4_value.setHorizontalAlignment(SwingConstants.CENTER);
		lblDelayRD4_value.setForeground(Color.GRAY);
		lblDelayRD4_value.setBounds(350, 200, 56, 16);
		Reverb2.getContentPane().add(lblDelayRD4_value);
		
		lblDelayVD1_value = new JLabel(Integer.toString(sliderVD1.getValue()) + " us");
		lblDelayVD1_value.setHorizontalAlignment(SwingConstants.CENTER);
		lblDelayVD1_value.setForeground(Color.GRAY);
		lblDelayVD1_value.setBounds(700, 50, 56, 16);
		Reverb2.getContentPane().add(lblDelayVD1_value);
		
		lblDelayVD2_value = new JLabel(Integer.toString(sliderVD2.getValue()) + " us");
		lblDelayVD2_value.setHorizontalAlignment(SwingConstants.CENTER);
		lblDelayVD2_value.setForeground(Color.GRAY);
		lblDelayVD2_value.setBounds(700, 100, 56, 16);
		Reverb2.getContentPane().add(lblDelayVD2_value);
		
		lblDelayVD3_value = new JLabel(Integer.toString(sliderVD3.getValue()) + " us");
		lblDelayVD3_value.setHorizontalAlignment(SwingConstants.CENTER);
		lblDelayVD3_value.setForeground(Color.GRAY);
		lblDelayVD3_value.setBounds(700, 150, 56, 16);
		Reverb2.getContentPane().add(lblDelayVD3_value);
		
		lblDelayVD4_value = new JLabel(Integer.toString(sliderVD4.getValue()) + " us");
		lblDelayVD4_value.setHorizontalAlignment(SwingConstants.CENTER);
		lblDelayVD4_value.setForeground(Color.GRAY);
		lblDelayVD4_value.setBounds(700, 200, 56, 16);
		Reverb2.getContentPane().add(lblDelayVD4_value);
		
		lblGainR_value = new JLabel(Float.toString(sliderGR.getValue()/100.0f));
		lblGainR_value.setHorizontalAlignment(SwingConstants.CENTER);
		lblGainR_value.setForeground(Color.GRAY);
		lblGainR_value.setBounds(350, 300, 56, 16);
		Reverb2.getContentPane().add(lblGainR_value);
		
		lblGainV_value = new JLabel(Float.toString(sliderGV.getValue()/100.0f));
		lblGainV_value.setHorizontalAlignment(SwingConstants.CENTER);
		lblGainV_value.setForeground(Color.GRAY);
		lblGainV_value.setBounds(700, 300, 56, 16);
		Reverb2.getContentPane().add(lblGainV_value);
		
		
	}
}
