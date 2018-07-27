package game;

import utils.CollsionUtils;
import utils.DrawUtils;
import utils.SoundUtils;

import java.io.IOException;

public class Bullet extends Picture implements Attackable,Destroyedable,Hitable{
//    private int x;
//    private int y;
//    private int width;
//    private int height;

    //  6.子弹坐标计算
    //  传入this代表的坦克对象到子弹类的构造方法,
    //  在里面根据不同的子弹方向和我提供的图片,计算出子弹坐标赋值
    public Bullet(Tank tank){
        try {
            int[] size = DrawUtils.getSize("tankwar\\res\\img\\bullet_u.gif");
            this.width = size[0];
            this.height = size[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        direction = tank.getDirection();
        switch(direction) {
            case UP:
                this.x = tank.getX() + tank.getWidth()/2 - this.width/2;
                this.y = tank.getY() - this.height/2;
                break;
            case DOWN:
                this.x = tank.getX() + tank.getWidth()/2 - this.width/2;
                this.y = tank.getY() + tank.getHeight() - this.height/2;
                break;
            case LEFT:
                this.x = tank.getX() - this.height/2;
                this.y = tank.getY() + tank.getWidth()/2 - this.width/2;
                break;
            case RIGHT:
                this.x = tank.getX() + tank.getHeight() - this.height/2;
                this.y = tank.getY() + tank.getWidth()/2 - this.width/2;
                break;
            default:
                break;
        }
        //  14.子弹发射声音
        //  在子弹的构造方法里面,通过声音工具类的播放方法传入音乐文件的路径即可
        try {
            SoundUtils.play("tankwar\\res\\snd\\fire.wav");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //  7.子弹绘制修改
    //	坦克方向赋值子弹成员方向,根据不同的成员方向绘制不同的图片
    //  8.子弹飞
    //	子弹不断绘制,在绘制方法里面,根据不同的成员方向做不同坐标的改变
    private Direction direction;
    private int speed = 8;
    public void draw(){
        switch(direction) {
            case UP:
                y -= speed;
                try {
                    DrawUtils.draw("tankwar\\res\\img\\bullet_u.gif", x, y);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case DOWN:
                y += speed;
                try {
                    DrawUtils.draw("tankwar\\res\\img\\bullet_d.gif", x, y);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case LEFT:
                x -= speed;
                try {
                    DrawUtils.draw("tankwar\\res\\img\\bullet_l.gif", x, y);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case RIGHT:
                x += speed;
                try {
                    DrawUtils.draw("tankwar\\res\\img\\bullet_r.gif", x, y);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
    //  9.子弹销毁
    //	子弹超出窗体的范围,暴露一个方法出去,在不断刷新方法里面调用,把子弹对象从集合里面移除
    public boolean isDestroyed(){
        if (x <= 0 || y <= 0 || x >= Config.WIDTH || y >= Config.HEIGHT){
            return true;
        }
        return false;
    }

    @Override
    public Blast showBigBlast() {
        return null;
    }

    // 18.子弹打铁消失
    //	模仿坦克与铁,碰撞检测方法也可以拷贝坦克里面的,
    //  把撞上之前修改坦克的坐标的那一段删掉即可,因为子弹图片非常小,忽略认为相交等于接触
    //	碰撞检测方法返回true,从集合里面移除子弹对象
    public boolean checkHit(/*Steel steel*/ Hitable h) {
        if (h instanceof Tank || h instanceof Bullet){
            return false;
        }
        Picture p = (Picture) h;
        int x1 = p.x;
        int y1 = p.y;
        int w1 = p.width;
        int h1 = p.height;

//        int x1 = steel.x;
//        int y1 = steel.y;
//        int w1 = steel.width;
//        int h1 = steel.height;

        int x2 = this.x;
        int y2 = this.y;
        int w2 = this.width;
        int h2 = this.height;

        boolean flag = CollsionUtils.isCollsionWithRect(x1, y1, w1, h1, x2, y2, w2, h2);

        return flag;
    }

    @Override
    public Blast showBlast() {
        Blast blast = new Blast(this,true);
        return blast;
    }
}
