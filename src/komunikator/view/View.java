package komunikator.view;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame{
    private JFrame frame;
    private JLabel lblPort;
    private JTextField textField_sprava;
    private JTextField textField_TargetIP;
    private JTextField textFragSize;
    private JButton btnSend;
    private JLabel lblIP;
    private JButton btnSetIP;
    private JCheckBox chckbxLoses;
    private List textArea_chat;
    private JSlider slider;
    private JButton btnFile;
    private JTextField textField_MyIP;
    private JPanel panel1;
    private JLabel lblMyIp;
    private JTextField textField_port;
    private JFileChooser fileChooser;

    public JTextField getTextField_port() {
		return textField_port;
	}

	public String getText(JTextField textfield){
        return textfield.getText();
    }

    public JButton getBtnSend() {
        return btnSend;
    }

    public JButton getBtnSetIP() {
        return btnSetIP;
    }

    public JButton getBtnFile() {
        return btnFile;
    }

    public JTextField getTextField_sprava() {
        return textField_sprava;
    }

    public JTextField getTextField_TargetIP() {
        return textField_TargetIP;
    }

    public JTextField getTextFragSize() {
        return textFragSize;
    }

    public JSlider getSlider() {
        return slider;
    }

    public List getTextArea_chat() {
        return textArea_chat;
    }

    public JCheckBox getChckbxLoses() {
		return chckbxLoses;
	}

	public JTextField getTextField_MyIP() {
        return textField_MyIP;
    }

    public View() {
        frame = new JFrame("program");
        frame.getContentPane().setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        textField_sprava = new JTextField();
        textField_sprava.setBounds(12, 253, 299, 22);
        frame.getContentPane().add(textField_sprava);
        textField_sprava.setColumns(10);

        btnSend = new JButton("Odoslat");
        btnSend.setBounds(323, 252, 97, 25);
        frame.getContentPane().add(btnSend);

        lblIP = new JLabel("Target IP:");
        lblIP.setBounds(12, 13, 64, 16);
        frame.getContentPane().add(lblIP);

        textField_TargetIP = new JTextField("147.175.176.37");
        textField_TargetIP.setBounds(88, 10, 116, 22);
        frame.getContentPane().add(textField_TargetIP);
        textField_TargetIP.setColumns(10);

        btnSetIP = new JButton("Set IP");
        btnSetIP.setBounds(210, 9, 97, 25);
        frame.getContentPane().add(btnSetIP);

        chckbxLoses = new JCheckBox("Simulovat stratu");
        chckbxLoses.setBounds(311, 43, 140, 25);
        frame.getContentPane().add(chckbxLoses);

        textArea_chat = new List();
        textArea_chat.setBounds(12, 149, 299, 91);
        frame.getContentPane().add(textArea_chat);

        slider = new JSlider(JSlider.HORIZONTAL,18, 1472, 1472);
        slider.setBounds(22, 77, 200, 26);
        slider.setMajorTickSpacing(100);
        slider.setMinorTickSpacing(1);
        frame.getContentPane().add(slider);

        textFragSize = new JTextField("1456");
        textFragSize.setBounds(64, 116, 116, 22);
        frame.getContentPane().add(textFragSize);
        textFragSize.setColumns(10);

        btnFile = new JButton("Subor");

        btnFile.setBounds(323, 180, 97, 25);
        frame.getContentPane().add(btnFile);

        lblMyIp = new JLabel("My IP:");
        lblMyIp.setBounds(12, 42, 56, 16);
        frame.getContentPane().add(lblMyIp);

        textField_MyIP = new JTextField("147.175.176.37");
        textField_MyIP.setBounds(88, 44, 116, 22);
        frame.getContentPane().add(textField_MyIP);
        textField_MyIP.setColumns(10);
        
        lblPort = new JLabel("Port:");
        lblPort.setBounds(323, 13, 56, 16);
        frame.getContentPane().add(lblPort);
        
        textField_port = new JTextField("9998");
        textField_port.setBounds(354, 10, 116, 22);
        frame.getContentPane().add(textField_port);
        textField_port.setColumns(10);
        
        fileChooser = new JFileChooser();

        frame.setVisible(true);
    }



    public JFileChooser getFileChooser() {
		return fileChooser;
	}

	public void settexttochat(String str){
        textArea_chat.add(str);
    }
}
