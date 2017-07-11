import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;

public class Line extends JFrame{
    private boolean stopped;
    private int level;
    private int power;
    private int score;

    private double hor;
    private double ver;

    private double newX;
    private double newY;
    private double rotate;
    private double speed;

    private BufferedImage img;
    private Graphics imG;
    private byte[] pixels;

    public Line(){
        super();
        setSize(476, 564);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ML ml = new ML();
        addMouseListener(ml);
        addMouseMotionListener(ml);
        stopped = true;
        hor = 200;
        ver = 200;

        newX = 200;
        newY = 200;
        rotate = Math.random() * Math.PI * 2;
        speed = Math.random() / 16;
        img = new BufferedImage(400, 400, 5);
        imG = img.getGraphics();
        pixels = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
        for(int i = 0; i < 1024; i++) {
            addPoint();
        }
        level = 0;
        for(int i = 0; i < 480000; i += 3) {
            if(pixels[i] == -1){
                level++;
            }
        }
        power = 0;
        score = 0;
        setVisible(true);
        createBufferStrategy(3);
    }

    public void addPoint() {
        imG.setColor(Color.BLUE);
        imG.fillOval((int)newX - 1, (int)newY - 1, 3, 3);
        newX = (newX + Math.cos(rotate) + 400) % 400;
        newY = (newY + Math.sin(rotate) + 400) % 400;
        rotate = (rotate + speed) % (Math.PI * 2);
        speed -= speed / 32 + (Math.random() - 0.5) / 64;
    }

    @Override
    public void paint(Graphics g){
        g.drawImage(img, 8, 31, null);

        g.setColor(Color.WHITE);
        g.fillOval(169, 441, 80, 80);
        g.fillRect(413, 31, 25, 500);
        g.fillRect(443, 31, 25, 500);
        g.fillRect(413, 536, 55, 20);
        if(power < 1024){
            g.fillOval((int)hor + 3, (int)ver + 26, 11, 11);
            g.setColor(Color.RED);
        }
        else{
            g.setColor(Color.RED);
            g.fillOval((int)hor + 3, (int)ver + 26, 11, 11);
        }
        int val2 = (int)(power * 125 / 256.0);
        g.fillRect(413, 531 - val2, 25, val2);

        g.setColor(Color.BLUE);
        int val = (int)(level * 125 / 1024.0);
        g.fillRect(443, 531 - val, 25, val);

        g.setColor(Color.BLACK);
        g.drawRect(413, 31, 25, 500);
        g.drawRect(443, 31, 25, 500);
        g.drawOval(169, 441, 80, 80);
        g.drawString("" + score, 413, 551);
        g.drawRect(413, 536, 55, 20);

        Point mouse = getMousePosition();
        if(stopped){
            //text here
        }
        else if(mouse != null && (mouse.x - 209) * (mouse.x - 209) + (mouse.y - 481) * (mouse.y - 481) < 1024){
            g.fillOval(mouse.x - 8, mouse.y - 8, 17, 17);
        }
    }

    public static void main(String[] args){
        Line line = new Line();
    }

    private class ML extends Thread implements MouseListener, MouseMotionListener{
        @Override
        public void run(){
            int prevX = (int)hor;
            int prevY = (int)ver;
            int pause = 500;
            long start = System.currentTimeMillis();
            while(true) {
                Point mouse = getMousePosition();
                if(mouse != null) {
                    double direction = Math.atan2(mouse.y - 481, mouse.x - 209);
                    hor = (hor + Math.cos(direction) + 400) % 400;
                    ver = (ver + Math.sin(direction) + 400) % 400;

                    imG.setColor(Color.BLACK);
                    imG.fillOval((int) hor - 5, (int) ver - 5, 11, 11);


                    int count = 0;
                    for (int i = 0; i < 480000; i += 3) {
                        if (pixels[i] == -1) {
                            count++;
                        }
                    }

                    for(int x = (int)hor - 101; x < (int)hor + 101; x++){
                        for(int y = (int)ver - 101; y < (int)ver + 101; y++){
                            if(pixels[y * 1200 + x * 3] == -1){
                                count++;
                            }
                        }
                    }

                    power += level - count;
                    if (power > 1024) {
                        power = 1024;
                    }

                    addPoint();
                    if(pause == 0){
                        imG.fillOval((int)(Math.random() * 380), (int)(Math.random() * 380),20, 20);
                        pause = 500;
                    }
                    else{
                        pause--;
                    }

                    level = 0;
                    for(int i = 0; i < 480000; i += 3) {
                        if(pixels[i] == -1){
                            level++;
                        }
                    }
                    if(level > 4096){
                        level = 4096;
                        repaint();
                        break;
                    }
                    repaint();
                    try{
                        Thread.sleep(25 + start - System.currentTimeMillis());
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                    start = System.currentTimeMillis();
                    score++;
                }
            }
        }
        @Override
        public void mouseClicked(MouseEvent e){
        }

        @Override
        public void mousePressed(MouseEvent e){
            if(stopped) {
                start();
                stopped = false;
            }
            else if(power == 1024){
                power = 0;
                imG.setColor(Color.BLACK);
                imG.fillOval((int) hor - 50, (int) ver - 50, 101, 101);

            }
        }

        @Override
        public void mouseReleased(MouseEvent e){
        }

        @Override
        public void mouseEntered(MouseEvent e){
        }

        @Override
        public void mouseExited(MouseEvent e){
        }

        @Override
        public void mouseMoved(MouseEvent e){
        }

        @Override
        public void mouseDragged(MouseEvent e){
        }
    }
}
