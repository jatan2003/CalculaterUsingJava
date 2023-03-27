import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

// class that calculate some details
class HelperFunc {
    // Gives output of current date
    public String calDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    // Check for unique attendance by date
    public boolean checkUniqueDate(String roll) {
        String reWrite = "";
        try {
            FileInputStream fin = new FileInputStream(roll);
            int i = 0;
            while ((i = fin.read()) != -1) {
                reWrite += (char) i;
            }
            fin.close();
            if(reWrite.length()==0){
                return true;
            }
            int size = reWrite.length();
            String preDate = reWrite.substring(size - 15, size - 7);
            int cur = Integer.parseInt(calDate());
            int pre = Integer.parseInt(preDate);
            return (pre != cur);
        } catch (Exception err) {
            return true;
        }

    }

    // Count Present
    int countPresent(String roll) {
        String s = "";
        try {
            FileInputStream fin = new FileInputStream(roll);
            int i = 0;
            while ((i = fin.read()) != -1) {
                s += (char) i;
            }
            fin.close();
        } catch (Exception err) {
            System.out.println(err);
        }
        if (s.length() == 0) {
            return 0;
        }
        int i = 0, p = 0;
        while (i < s.length() - 1) {
            if (s.charAt(i) == ',' && s.charAt(i + 1) == 'p') {
                p++;
            }
            i++;
        }
        return p;
    }

    // counts absent
    int countAbsent(String roll) {
        String s = "";
        try {
            FileInputStream fin = new FileInputStream(roll);
            int i = 0;
            while ((i = fin.read()) != -1) {
                s += (char) i;
            }
            fin.close();
        } catch (Exception err) {
            System.out.println(err);
        }
        if (s.length() == 0) {
            return 0;
        }
        int i = 0, p = 0;
        while (i < s.length() - 1) {
            if (s.charAt(i) == ',' && s.charAt(i + 1) == 'a') {
                p++;
            }
            i++;
        }
        return p;
    }

}

class Front extends Frame implements ActionListener {
    // declaring component
    Button submit;
    Button Count;
    Label alertLabel;
    Label date, rollLab, date2, isPresent;
    TextField roll;
    HelperFunc hf = new HelperFunc();
    Choice c;
    Panel p1, p2, p3, p4, p5, p6;

    Front() {
        // initializing componet
        // alert label for all type of errors
        alertLabel = new Label("Attendace Form");

        // attendance input
        rollLab = new Label("Roll Number");
        roll = new TextField(5);
        // Current date
        date2 = new Label("Date");
        date = new Label(
                hf.calDate().substring(0, 4) + "/" + hf.calDate().substring(4, 6) + "/" + hf.calDate().substring(6, 8));
        // attendance
        isPresent = new Label("Attendance");
        c = new Choice();
        c.add("prsnt");
        c.add("absnt");
        // button to submit attendance
        submit = new Button("submit");
        // Button to count attendance
        Count = new Button("Count Attendance");
        // giving layout from panel
        p1 = new Panel(new GridLayout(1, 1));
        p2 = new Panel(new GridLayout(1, 2));
        p3 = new Panel(new GridLayout(1, 2));
        p4 = new Panel(new GridLayout(1, 2));
        p5 = new Panel(new GridLayout(1, 1));
        p6 = new Panel(new GridLayout(1, 1));
        // add components to panel
        p1.add(alertLabel);
        p2.add(rollLab);
        p2.add(roll);
        p3.add(date2);
        p3.add(date);
        p4.add(isPresent);
        p4.add(c);
        p5.add(submit);
        p6.add(Count);
        // adding panel
        add(p1);
        add(p2);
        add(p3);
        add(p4);
        add(p5);
        add(p6);
        // setting font to whole page
        setFont(new Font("Arial", Font.BOLD, 18));
        setBackground(Color.CYAN);
        roll.setBackground(Color.LIGHT_GRAY);
        submit.setBackground(Color.RED);
        Count.setBackground(Color.GREEN);
        rollLab.setAlignment(Label.CENTER);
        date2.setAlignment(Label.CENTER);
        alertLabel.setAlignment(Label.CENTER);
        isPresent.setAlignment(Label.CENTER);
        // adding actions to submit and Count
        submit.addActionListener(this);
        Count.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            String s = roll.getText();
            String status = c.getSelectedItem();
            s += ".csv";

            String reWrite = "";
            if ((hf.checkUniqueDate(s)) && (s.length() == 12 || s.length() == 13)) {
                try {
                    FileInputStream fin = new FileInputStream(s);
                    int i = 0;
                    while ((i = fin.read()) != -1) {
                        reWrite += (char) i;
                    }
                    fin.close();
                } catch (Exception err) {
                }

                try {
                    FileOutputStream fout = new FileOutputStream(s);
                    String s2 = reWrite + hf.calDate() + "," + status + "\n";
                    byte b[] = s2.getBytes();
                    fout.write(b);
                    fout.close();
                } catch (Exception err) {
                    System.out.println(err);
                }
            } else if (!(s.length() >= 12 && s.length() <= 13)) {
                alertLabel.setText("Invalid Detail");
            } else {
                alertLabel.setText("Attendance Already taken");
            }
        }
        if (e.getSource() == Count) {
            String s = roll.getText();
            s += ".csv";
            if (s.length() < 12 || s.length() > 13) {
                alertLabel.setText("Invalid Roll Number");
            } else {
                int present = hf.countPresent(s);
                int absent = hf.countAbsent(s);
                String detail = "Present : " + present + "\n" + "absent :" + absent;
                alertLabel.setText(detail);
            }
        }
    }
}

public class Attendance {
    public static void main(String[] args) {
        Front f = new Front();
        f.setVisible(true);
        f.setSize(500, 500);
        f.setLayout(new GridLayout(6, 1));
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

}
