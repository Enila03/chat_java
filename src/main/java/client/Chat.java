package client;

import common.Utils;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

class Chat extends JFrame {

    private JLabel jl_title;
    private JEditorPane messages;
    private JTextField jt_message;
    private JButton jb_message;
    private JPanel panel;
    private JScrollPane scroll;
    
    private Home home;
    private Socket connection;
    private String connection_info;
    private ArrayList<String> message_list;
    
    public Chat(Home home, Socket connection, String connection_info, String title){
        super("Chat " + connection_info.split(":")[0]);
        this.home = home;
        this.connection = connection;
        this.connection_info = connection_info;
        initComponents();
        configComponents();
        insertComponents();
        insertAction();
        start();
    }

    private void initComponents() {
        message_list = new ArrayList<String>();
        jl_title = new JLabel(connection_info.split(":")[0], SwingConstants.CENTER); 
        messages = new JEditorPane();
        scroll = new JScrollPane(messages);
        jt_message = new JTextField();
        jb_message = new JButton("Enviar");
        panel = new JPanel(new BorderLayout());
     
    }

    private void configComponents() {
        this.setMinimumSize(new Dimension(480,720));
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        messages.setContentType("text/html");
        messages.setEditable(false);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jb_message.setSize(100, 40);
    }

    private void insertComponents() {
        this.add(jl_title, BorderLayout.NORTH);
        this.add(scroll, BorderLayout.CENTER);
        this.add(panel, BorderLayout.SOUTH);
        panel.add(jt_message, BorderLayout.CENTER);
        panel.add(jb_message, BorderLayout.EAST);
    }

    private void insertAction() {
        jb_message.addActionListener(event -> send());
        jt_message.addKeyListener(new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent e){

            }

            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyChar() == KeyEvent.VK_ENTER) {
                    send();
                }
            }

            @Override
            public void keyReleased(KeyEvent e){
                
            }

        });
        
        this.addWindowListener(new WindowListener(){
            @Override
            public void windowOpened(WindowEvent e) {
                
            }

            @Override
            public void windowClosing(WindowEvent e) {
                Utils.sendMessage(connection, "CHAT_CLOSE");
                home.getOpenned_chats().remove(connection_info);
                home.getConnected_listeners().get(connection_info).setChatOpen(false);
                home.getConnected_listeners().get(connection_info).setRunning(false);
                home.getConnected_listeners().remove(connection_info);
                
            }

            @Override
            public void windowClosed(WindowEvent e) {
                
            }

            @Override
            public void windowIconified(WindowEvent e) {
                
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                
            }

            @Override
            public void windowActivated(WindowEvent e) {
                
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                
            }
            
        });
        
    }

    public void append_message(String received){
        message_list.add(received);
        String message = "";
        
        for (String str : message_list){
            message += str;
        }
        
        messages.setText(message);
    }

    private void send() {
        if (jt_message.getText().length() > 0 ) {
            DateFormat df = new SimpleDateFormat("hh:mm"); 
            Utils.sendMessage(connection, "MESSAGE;" + "<b><font face:'helvetica'>[" + df.format(new Date()) + "] " + connection_info.split(":")[0] + ": </b>" + jt_message.getText() + "</font><br>");                             
            append_message("<b><font face:'helvetica'>[" + df.format(new Date()) + "] Eu: </b>" + jt_message.getText() + "</font><br>");
            jt_message.setText("");
            
            
        }
    }
      
    private void start() {
        this.pack();
        this.setVisible(true);
    }
    
}
