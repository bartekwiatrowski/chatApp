package client;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Utilities;
import java.awt.*;
import java.awt.event.ActionListener;
import static server.CaesarCipherLegit.*;



public class ClientWindow {

    private JFrame frame;
    private JTextField messageField;
    private static JTextArea textArea = new JTextArea();

    private Client client;

    public static void main(String[] args){


        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    ClientWindow window = new ClientWindow();
                    window.frame.setVisible(true);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public ClientWindow(){
        initialize();

        String name = JOptionPane.showInputDialog("Enter name");
        client = new Client(name, "localhost", 52864);
    }
    private void initialize(){
        frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle("ChatApp");
        frame.setBounds(100, 100, 850, 475);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));


        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.getContentPane().add(scrollPane);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        messageField = new JTextField();
        panel.add(messageField);
        messageField.setColumns(50);

        JButton buttonSend = new JButton("Send");
        buttonSend.addActionListener(e -> {

            client.send(messageField.getText());
            messageField.setText("");
        });
        panel.add(buttonSend);


        JButton buttonEncrypt = new JButton("Encrypt");
        buttonEncrypt.addActionListener(e -> {

            client.send(encode(messageField.getText(), 12));
            //client.send(messageField.getText());
            messageField.setText("");
        });
        panel.add(buttonEncrypt);



        JButton buttonDecrypt = new JButton("Decrypt");
        buttonDecrypt.addActionListener(e ->{

            int end = textArea.getDocument().getLength();
            int start = 0;
            try {
                start = Utilities.getRowStart(textArea, end);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }

            while (start == end)
            {
                end--;
                try {
                    start = Utilities.getRowStart(textArea, end);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
            try {
                textArea.append(decode(textArea.getText(start, end - start), 12) + "\n");
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
            //textArea.append(decode(textArea.getText(), 12) + "\n");
        });
        panel.add(buttonDecrypt);


        frame.setLocationRelativeTo(null);
    }

    public static void printToConsole(String message){

        textArea.setText(textArea.getText() + message + "\n");
    }


}
