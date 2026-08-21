import javax.swing.*;

public class JavaDemo {
    public static void main(String[] args) {
        JFrame obj = new JFrame();
        JLabel label = new JLabel("Password :");
        JPasswordField JPS = new JPasswordField();
        JRadioButton jRadio = new JRadioButton();

        label.setBounds(40,100, 100,30);
        obj.add(label);
    
        JPS.setBounds(100,100,150,30);
        obj.add(JPS);
 
         obj.setSize(400,400);
         obj.setLayout(null);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
    }
    
}
