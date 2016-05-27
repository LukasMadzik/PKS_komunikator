package komunikator.controller;

import komunikator.comunicate.Informer;
import komunikator.comunicate.Receiver;
import komunikator.comunicate.Sender;
import komunikator.keepalive.KeepAliveReceive;
import komunikator.keepalive.KeepAliveSend;
import komunikator.view.View;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Created by Lukas on 30.10.2015.
 */
public class Controller {
    private View guii;
    private ActionListener actionListenerSend;
    private ActionListener actionListenerFile;
    private ActionListener actionListenerSetIp;
    private ActionListener actionListenerSlider;
    private FocusAdapter focusAdapterFragText;
    private ChangeListener changeListenerSlider;

    public Controller(){
        guii = new View();
        fragGuiSet();
    }

    private void fragGuiSet() {
    	actionListenerSend = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(guii.getTextField_TargetIP().isEditable())
                    JOptionPane.showMessageDialog(null, "IP adresa nebola nastavena");
                else {
                    Sender sender = new Sender(guii.getTextField_sprava().getText(), guii.getTextField_MyIP().getText(), guii.getTextField_TargetIP().getText(),
                        guii.getSlider().getValue(), guii.getChckbxLoses().isSelected());
                    sender.performSend();
                    guii.getTextArea_chat().add("Ja: "+guii.getTextField_sprava().getText());
                }
            }
        };
        
        actionListenerFile = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = guii.getFileChooser().showOpenDialog(guii.getFileChooser());
                if(returnVal == JFileChooser.APPROVE_OPTION){
                	File f = guii.getFileChooser().getSelectedFile();
                	Path p = f.toPath();
                	try {
						byte[] data = Files.readAllBytes(p);
						String text = new String(data);
						Sender sender = new Sender(data, guii.getTextField_MyIP().getText(), guii.getTextField_TargetIP().getText(),
                        guii.getSlider().getValue(), guii.getChckbxLoses().isSelected(), f.getName());
						sender.performSendFile();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
            }
        };

        actionListenerSetIp = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!Objects.equals(guii.getTextField_TargetIP().getText(), "")) {
                    guii.getTextField_TargetIP().setEditable(false);
                    guii.getTextField_MyIP().setEditable(false);
                    guii.getTextField_port().setEditable(false);
                    Informer.setPort(Integer.parseInt(guii.getTextField_port().getText()));
                    Informer.setMyIp(guii.getTextField_MyIP().getText());
                    Informer.setTargetIp(guii.getTextField_TargetIP().getText());
                    new Thread(new Receiver(guii)).start();
                    new Thread(new KeepAliveReceive(guii)).start();
                    new Thread(new KeepAliveSend(guii)).start();
                    System.out.printf("Recieve");
                }
            }
        };

        actionListenerSlider = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guii.getSlider().setValue(Integer.parseInt(guii.getTextFragSize().getText())+16);
            }
        };
        changeListenerSlider = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                if (!source.getValueIsAdjusting()) {
                    guii.getTextFragSize().setText(String.valueOf(source.getValue()-16));
                }
            }
        };
        focusAdapterFragText = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if(Integer.parseInt(guii.getTextFragSize().getText())< guii.getSlider().getMinimum()-16 ||
                        Integer.parseInt(guii.getTextFragSize().getText())> guii.getSlider().getMaximum()-16){
                    JOptionPane.showMessageDialog(null, "Velkost fragmentu je mimo povoleny rozsah <2,1456>");
                }else{
                    guii.getSlider().setValue(Integer.parseInt(guii.getTextFragSize().getText()));
                }
            }
        };
        guii.getBtnFile().addActionListener(actionListenerFile);
        guii.getBtnSend().addActionListener(actionListenerSend);
        guii.getBtnSetIP().addActionListener(actionListenerSetIp);
        guii.getSlider().addChangeListener(changeListenerSlider);
        guii.getTextFragSize().addFocusListener(focusAdapterFragText);
        guii.getTextFragSize().addActionListener(actionListenerSlider);
    }
}
