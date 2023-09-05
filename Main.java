//package a;

import javax.swing.*;

class Slide extends JSlider {
    public Slide() {
        super(0,100);
    }

    synchronized public void Increase(int increment){
        setValue((int)getValue() + increment);
    }
}

class MyThread extends Thread {
    private int increment;
    private Slide mySlider;
    private int count;
    private static int BOUND = 1000000;
    private static int THREAD_COUNTER = 0;
    private int curNum;

    public MyThread(Slide mySlider, int increment, int priority) {
        this.mySlider = mySlider;
        this.increment = increment;
        curNum = ++THREAD_COUNTER;
        setPriority(priority);
    }

    @Override
    public void run() {
        while(!interrupted()){
            int val = (int)(mySlider.getValue());
            ++count;
            if(count > BOUND){
                mySlider.Increase(increment);
                count = 0;
            }
        }
    }
    public JPanel GetJPanel() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Потік #" + curNum + ", Приорітет = " + increment);
        SpinnerModel sModel = new SpinnerNumberModel(getPriority(), Thread.MIN_PRIORITY, Thread.MAX_PRIORITY, 1);
        JSpinner Spinner = new JSpinner(sModel);
        Spinner.addChangeListener(e->{setPriority((int)(Spinner.getValue()));});
        panel.add(Spinner);
        panel.add(label);
        return panel;
    }
}

public class Main {
    public static int SEMAPHORE = 0;
    static Thread T1, T2;
    public static void main(String[] args) {
        JFrame MyFrame = new JFrame();
        MyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MyFrame.setSize(600, 300);
        Slide MySlider = new Slide();

        MyThread Thread1 = new MyThread(MySlider, +1, Thread.NORM_PRIORITY);

        MyThread Thread2 = new MyThread(MySlider, -1, Thread.NORM_PRIORITY);

        JButton startBTN = new JButton("Старт");
        startBTN.addActionListener(e -> {
            Thread1.start();
            Thread2.start();
            startBTN.setEnabled(false);

        });
        JPanel MyPanel = new JPanel();
        MyPanel.setLayout(new BoxLayout(MyPanel, BoxLayout.Y_AXIS));

        MyPanel.add(MySlider);
        MyPanel.add(Thread1.GetJPanel());
        MyPanel.add(Thread2.GetJPanel());

        JPanel jPanel = new JPanel();
        jPanel.add(startBTN);
        MyPanel.add(jPanel);

        MyFrame.setContentPane(MyPanel);
        MyFrame.setVisible(true);

    }
}