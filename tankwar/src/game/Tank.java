package game;

import utils.CollsionUtils;
import utils.DrawUtils;
import java.io.IOException;
import java.util.Random;

/*
    图片,坐标,宽高
    绘制自己
*/
//  13.让坦克,子弹也去继承图片类,成为图片的一种,统一存到图片集合绘制出来,便于管理
public class Tank extends Picture implements Moveable,Hitable,Destroyedable{
//    private int x;
//    private int y;
//    private int width;
//    private int height;

    public Tank(int x,int y){
//        this.x = x;
//        this.y = y;
        super(x,y);
        try {
            int[] size = DrawUtils.getSize("tankwar\\res\\img\\tank_u.gif");
            this.width = size[0];
            this.height = size[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Direction getDirection() {
        return direction;
    }

    // 绘制自己
    // 1.坦克绘制
    // 工具类调用draw方法,告诉图片路径和坐标即可,
    // 要在不断刷新方法里面调用,人的肉眼才能不断看到图片
    public void draw(){
        // 3.坦克绘制修改
        //	按键方向赋值成员方向,根据不同的成员方向绘制不同的图片
        switch(this.direction) {
            case UP:
                try {
                    DrawUtils.draw("tankwar\\res\\img\\tank_u.gif", x, y);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case DOWN:
                try {
                    DrawUtils.draw("tankwar\\res\\img\\tank_d.gif", x, y);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case LEFT:
                try {
                    DrawUtils.draw("tankwar\\res\\img\\tank_l.gif", x, y);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case RIGHT:
                try {
                    DrawUtils.draw("tankwar\\res\\img\\tank_r.gif", x, y);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
    //  2.坦克移动
    //	根据不同方向,做不同坐标的改变,在按键监听处调用,传入按键对应的方向
    //  private int speed = 32;
    //  速度修改为不是64的倍数多出余数那么一点点的距离,在工具类方法返回true时,
    //  重新获取坦克的坐标,根据不同的撞上方向和我发的图片,算出那么一点点的距离用成员变量接收,
    //  在坦克的移动方法,撞上方向有值return之前,根据不同的撞上方向,走完,即给坦克坐标赋值距离.
    private int speed = 23;
    private Direction direction = Direction.UP;
    private Direction badDirection;
    public void move(Direction direction){
        //	撞上方向有值return不走坐标处增加一个条件,
        //  当撞上方向和按键方向一致时才这样,避免其按键其他方向时,坦克也不能移动
        if (this.badDirection != null && this.badDirection == direction){
            //  速度修改为不是64的倍数多出余数那么一点点的距离,在工具类方法返回true时,
            //  重新获取坦克的坐标,根据不同的撞上方向和我发的图片,算出那么一点点的距离用成员变量接收,
            //  在坦克的移动方法,撞上方向有值return之前,根据不同的撞上方向,走完,即给坦克坐标赋值距离.
            switch(this.badDirection) {
                case UP:
                    y -= seed;
                    break;
                case DOWN:
                    y += seed;
                    break;
                case LEFT:
                    x -= seed;
                    break;
                case RIGHT:
                    x += seed;
                    break;
                default:
                    break;
            }
            return;
        }
//    4.坦克移动方法修改
//    修改二:坦克调转方向只改变方向,不改变坐标,
//    坦克调转方向的时候是成员方向this.direction跟按键方向direction不相等的时候
        if (this.direction != direction) {
            this.direction = direction;
            return; // 方法结束
        }
        switch(direction) {
            case UP:
                y -= speed;
                break;
            case DOWN:
                y += speed;
                break;
            case LEFT:
                x -= speed;
                break;
            case RIGHT:
                x += speed;
                break;
            default:
                break;
        }
        // 4.坦克移动方法修改
        //	修改一:超出屏幕的某个范围,就让它等于这个范围,定在那里,坦克就不能移动了
        if (x <= 0){
            x = 0;
        }
        if (x >= Config.WIDTH - 64){
            x = Config.WIDTH - 64;
        }
        if (y <= 0){
            y = 0;
        }
        if (y >= Config.HEIGHT - 64){
            y = Config.HEIGHT - 64;
        }
    }

    //  5.坦克发射产生子弹对象
    //	创建子类类对象返回即可,这里可以控制子弹发射的频率
    private long last; // 子弹上一次发射时间
    public Bullet shot(){
        //  10.子弹发射的频率的控制
        //  在坦克类的发射方法得到子弹对象里面,进行时间间隔的限制,
        //  小于一定的时间不产生子弹对象而是返回null
        long now = System.currentTimeMillis();
        if (now - last < 400){
            return null;
        } else{
            last = now;
        }
        //  6.子弹坐标计算
        //  传入this代表的坦克对象到子弹类的构造方法,
        //  在里面根据不同的子弹方向和我提供的图片,计算出子弹坐标赋值
        Bullet bullet = new Bullet(this);
        return bullet;
    }

    @Override
    public int getOrder() {
        return 1;
    }
    // 坦克的碰撞检测方法
    private int seed;
    public boolean checkHit(/*Steel steel*/ Blockable b){
        Picture p = (Picture) b;
        //	坦克碰撞检测方法调用工具类,需要两个矩形坐标和宽高,
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

        //  在调用之前,给坦克不同方向修改速度,让刚好接触发生碰撞,而不是工具类的相交碰撞
        switch(direction) {
            case UP:
                y2 -= speed;
                break;
            case DOWN:
                y2 += speed;
                break;
            case LEFT:
                x2 -= speed;
                break;
            case RIGHT:
                x2 += speed;
                break;
            default:
                break;
        }

        boolean flag = CollsionUtils.isCollsionWithRect(x1, y1, w1, h1, x2, y2, w2, h2);
        //	工具类方法返回true,就赋值坦克成员方向为撞上成员方向暴露出去,
        //  在坦克移动方法里面,根据撞上方向不为null有值return,不走坐标改变
        if (flag){
            this.badDirection = this.direction;
            //  速度修改为不是64的倍数多出余数那么一点点的距离,在工具类方法返回true时,
            //  重新获取坦克的坐标,根据不同的撞上方向和我发的图片,算出那么一点点的距离用成员变量接收,
            //  在坦克的移动方法,撞上方向有值return之前,根据不同的撞上方向,走完,即给坦克坐标赋值距离.
            int x22 = this.x;
            int y22 = this.y;
            int w22 = this.width;
            int h22 = this.height;
            switch(this.badDirection) {
                case UP:
                    seed = y22 - y1 - h1;
                    break;
                case DOWN:
                    seed = y1 - y22 - h22;
                    break;
                case LEFT:
                    seed = x22 - x1 - w1;
                    break;
                case RIGHT:
                    seed = x1 -  x22 - h22;
                    break;
                default:
                    break;
            }
        } else{
            //	最后记住坦克没有撞上时,也就是之前的工具类方法返回false时,
            //  要把撞上方向还原为null,避免不撞上时,坦克也不能移动
            this.badDirection = null;
        }
        return flag;
    }
    private int blood = 10;
    public Blast showBlast() {
        blood--;
        //  20.爆炸物坐标计算
        //  在铁给反应方法里面传入this代表的铁对象到爆炸物的构造方法里面,
        //  根据我发的图片算出爆炸物的坐标赋值,跟子弹坐标计算几乎一样
        Blast blast = new Blast(this,true);
        return blast;
    }
    public boolean isDestroyed(){
        return blood <= 0;
    }

    @Override
    public Blast showBigBlast() {
        Blast blast = new Blast(this,false);
        return blast;
    }
}
