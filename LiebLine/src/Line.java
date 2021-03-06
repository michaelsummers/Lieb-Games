import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;

public class Line extends JFrame{
    private double hor;
    private double ver;

    private double newX;
    private double newY;
    private double rotate;
    private double speed;

    private BufferedImage img;
    private Graphics imG;

    public Line(){
        super();
        setSize(416, 539);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ML ml = new ML();
        addMouseListener(ml);
        addMouseMotionListener(ml);
        hor = 200;
        ver = 200;

        newX = 200;
        newY = 200;
        rotate = Math.random() * Math.PI * 2;
        speed = Math.random() / 16;
        img = new BufferedImage(400, 400, 5);
        imG = img.getGraphics();
        for(int i = 0; i < 1024; i++) {
            addPoint();
        }
        setVisible(true);
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
        g.fillOval((int)hor + 4, (int)ver + 27, 9, 9);
        g.fillOval(169, 441, 80, 80);
        g.setColor(Color.BLACK);
        g.drawOval(169, 441, 80, 80);
        Point mouse = getMousePosition();
        if(mouse != null && (mouse.x - 209) * (mouse.x - 209) + (mouse.y - 481) * (mouse.y - 481) < 1024){
            g.fillOval(mouse.x - 8, mouse.y - 8, 17, 17);
        }
    }

    public static void main(String[] args){
        new Line();
    }

    private class ML extends Thread implements MouseListener, MouseMotionListener{
        private boolean begun;

        private ML(){
            super();
            begun = false;
        }

        @Override
        public void run(){
            while(true) {
                Point mouse = getMousePosition();
                if(mouse != null) {
                    double direction = Math.atan2(mouse.y - 481, mouse.x - 209);
                    hor = (hor + Math.cos(direction) + 400) % 400;
                    ver = (ver + Math.sin(direction) + 400) % 400;
                }
                imG.setColor(Color.BLACK);
                imG.fillOval((int)hor - 4, (int)ver - 4, 9, 9);
                addPoint();
                try{
                    Thread.sleep(20);
                }
                catch(Exception ex) {
                }
                repaint();
            }
        }
        @Override
        public void mouseClicked(MouseEvent e){
        }

        @Override
        public void mousePressed(MouseEvent e){
            if(!begun) {
                start();
                begun = true;
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
