package game;

import org.lwjgl.input.Keyboard;
import utils.DrawUtils;
import utils.SoundUtils;
import utils.Window;

import javax.security.auth.Destroyable;
import java.io.IOException;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameWindow extends Window {

    public GameWindow(String title, int width, int height, int fps) {
        super(title, width, height, fps);
    }

    private Tank tank;

    @Override
    protected void onCreate() { // 只调用一次
        try {
            SoundUtils.play("tankwar\\res\\snd\\start.wav");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //  15.坦克隐藏在草坪里
        //	简单做法:改变先后添加顺序,先画出来的东西会给后画出来的东西覆盖
        //  创建坦克对象
        tank = new Tank(Config.WIDTH/2 - 64/2,Config.HEIGHT - 64);
//        list.add(tank);
        addPicture(tank);
//        tank.draw(); // 不能在这里绘制
        for (int i = 0; i < 18; i+=2) {
            Wall wall = new Wall(i * 64,64);
//            wlist.add(wall);
//            list.add(wall);
            addPicture(wall);
        }
        for (int i = 3; i < 12; i++) {
            Water water = new Water(i * 64,64 * 3);
//            walist.add(water);
//            list.add(water);
            addPicture(water);
        }
//        for (int i = 3; i < 15; i++) {
//            Steel steel = new Steel(i * 64,64 * 7);
////            slist.add(steel);
//            list.add(steel);
//        }
//        tank = new Tank(0,0);
//        list.add(tank);
        for (int i = 2; i < 16; i++) {
            Grass grass = new Grass(i * 64,64 * 5);
//            glist.add(grass);
//            list.add(grass);
            addPicture(grass);
        }
        for (int i = 3; i < 8; i++) {
            Steel steel = new Steel(i * 64,64 * 7);
//            slist.add(steel);
//            list.add(steel);
            addPicture(steel);
        }
        // 添加5辆敌方坦克
        for (int i = 0; i < 10; i++) {
            Tank2 tank2 = new Tank2(64 * i * 2,0);
            addPicture(tank2);
            list2.add(tank2);
        }

    }

    @Override
    protected void onMouseEvent(int key, int x, int y) {

    }
    // 子弹集合
//    private CopyOnWriteArrayList<Bullet> blist = new CopyOnWriteArrayList<>(); // 解决并发修改异常
//    private CopyOnWriteArrayList<Wall> wlist = new CopyOnWriteArrayList<>(); // 解决并发修改异常
//    private CopyOnWriteArrayList<Water> walist = new CopyOnWriteArrayList<>(); // 解决并发修改异常
//    private CopyOnWriteArrayList<Grass> glist = new CopyOnWriteArrayList<>(); // 解决并发修改异常
//    private CopyOnWriteArrayList<Steel> slist = new CopyOnWriteArrayList<>(); // 解决并发修改异常
    private CopyOnWriteArrayList<Picture> list = new CopyOnWriteArrayList<>(); // 解决并发修改异常
    //  专门用来统计敌方坦克数量的集合
    private CopyOnWriteArrayList<Picture> list2 = new CopyOnWriteArrayList<>(); // 解决并发修改异常

    @Override
    protected void onKeyEvent(int key) {
        // 2.坦克移动
        //	根据不同方向,做不同坐标的改变,在按键监听处调用,传入按键对应的方向
        switch(key) {
            case Keyboard.KEY_UP:
                tank.move(Direction.UP);
                break;
            case Keyboard.KEY_DOWN:
                tank.move(Direction.DOWN);
                break;
            case Keyboard.KEY_LEFT:
                tank.move(Direction.LEFT);
                break;
            case Keyboard.KEY_RIGHT:
                tank.move(Direction.RIGHT);
                break;
            case Keyboard.KEY_SPACE:
                //  5.坦克发射产生子弹对象
                //	创建子类类对象返回即可,这里可以控制子弹发射的频率
                if (!tank.isDestroyed()){
                    Bullet bullet = tank.shot();
                    if (bullet != null) {
//                    blist.add(bullet);
                        list.add(bullet);
                    }
                }
                break;
            default:
                break;
        }
    }

    //求反方向
    private Direction reverDirection(Direction direction) {
        if (direction==Direction.UP) {
            return Direction.DOWN;
        }else if (direction==Direction.DOWN) {
            return Direction.UP;
        }else if (direction==Direction.LEFT) {
            return Direction.RIGHT;
        }else {
            return Direction.LEFT;
        }
    }

    //产生一个随机方向,即产生一定范围的随机数然后根据不同的值给坦克的成员方向设置值
    private Random r = new Random();
    private long last2;//0
    private Direction direction = Direction.UP;
    private void getRandomDirection() {
        long now = System.currentTimeMillis();
		if (now-last2<300) {
			return;
		}else {
			last2 = now;
		}
        int i = r.nextInt(4);
        if (i==1) {
            this.direction = Direction.UP;
        }else if (i==2) {
            this.direction = Direction.DOWN;
        }else if (i==3) {
            this.direction = Direction.LEFT;
        }else {
            this.direction = Direction.RIGHT;
        }
    }

    @Override
    protected void onDisplayUpdate() { // 不断调用
        //显示胜利或者失败图片
        if (tank.isDestroyed() || list2.size()==0) {
            if (tank.isDestroyed()) {
                list.clear();

                try {
                    DrawUtils.draw("tankwar\\res\\img\\over.png", 0, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else {
                list.clear();

                try {
                    DrawUtils.draw("tankwar\\res\\img\\win.png", 0, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //  开启子线程3秒后退出虚拟机结束游戏或者你也可以做其他事情,
            //  不能在主线程即画图线程退出否则卡
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }).start();

        }
        // 敌方坦克随机移动并且发射子弹
        for (Picture p : list) {
            if (p instanceof Tank2){
                ((Tank2) p).move();
                Bullet2 bullet2 = ((Tank2) p).shot();
                if (bullet2 != null){
                    list.add(bullet2);
                }
            }
        }
        // 要在不断刷新方法里面调用,人的肉眼才能不断看到图片
//        tank.draw();

//        for (Bullet bullet : blist) {
//            //  9.子弹销毁
//            //	子弹超出窗体的范围,暴露一个方法出去,在不断刷新方法里面调用,把子弹对象从集合里面移除
//            if (bullet.isDestroyed()){
//                blist.remove(bullet);
//            }
//            bullet.draw();
//        }
//        for (Wall wall : wlist) {
//            wall.draw();
//        }
//        for (Water water : walist) {
//            water.draw();
//        }
//        for (Grass grass : glist) {
//            grass.draw();
//        }
//        for (Steel steel : slist) {
//            steel.draw();
//        }
        for (Picture p : list) {
//            if (p instanceof Bullet){
//                if (((Bullet) p).isDestroyed()){
//                    list.remove(p);
//                }
//            }
//            if (p instanceof Blast){
//                if (((Blast) p).isDestroyed()){
//                    list.remove(p);
//                }
//            }
//            if (p instanceof Steel){
//                if (((Steel) p).isDestroyed()){
//                    list.remove(p);
//                }
//            }
//            if (p instanceof Wall){
//                if (((Wall) p).isDestroyed()){
//                    list.remove(p);
//                }
//            }
            // 25.销毁接口抽取
            //	根据上面子弹或者其他要销毁的东西的是否销毁方法返回true从集合里面移除对象,
            //  替换为销毁接口,注释其他,点选鼠标创建,让类实现接口即可
            if (p instanceof Destroyedable){
                if (((Destroyedable) p).isDestroyed()){
                    //  26.销毁瞬间大爆炸物
                    //	在销毁接口的是否销毁方法返回true的判断处,用销毁接口的东西产生爆炸物对象添加到集合绘制,
                    //  让子弹爆炸物重写抽象方法返回null空实现,让铁或墙重写抽象方法创建爆炸物对象,增加一个标记,
                    //  在爆炸物的构造方法,里面根据不同的标记的值,重写赋值爆炸物图片数组为4张即可,
                    //	因为爆炸物的绘制方法不断调用数组里面图片路径,你改变了数组图片路径,
                    //  就绘制了新的几张图片,造成大小爆炸物的不同效果
                    Blast blast = ((Destroyedable) p).showBigBlast();
                    if (blast != null) {
                        list.add(blast);
                    }
                    list.remove(p);

                    //  具有销毁能力的东西是敌方坦克,要从专门统计敌方坦克数量的集合中移除,
                    //  这样我才知道敌方坦克是否死了
                    if (p instanceof Tank2){
                        list2.remove(p);
                    }
                }
            }
            p.draw();
        }

    //  16.坦克撞铁不能移动
    //	从图片集合循环嵌套拿到坦克对象和铁对象,如果坦克的碰撞检测方法返回true,用break跳出不再比较
//        for (Picture p : list) {
//            for (Picture p2 : list) {
//                if (p instanceof Tank && p2 instanceof Steel){
//                    if (((Tank) p).checkHit((Steel) p2)){
//                        break;
//                    }
//                }
//            }
//        }
        // 17.移动阻挡接口抽取
        //	根据上面的代码,把坦克类替换为移动接口,把铁类替换为阻挡接口,
        //  先使用后创建,点选鼠标创建即可,然后让坦克实现移动接口,重写抽象方法,
        //  要之前的碰撞检测方法代码拷贝到重写方法里面,把阻挡接口强转为图片父类接收,得到阻挡物坐标宽高
        for (Picture p : list) {
            for (Picture p2 : list) {
                if (p instanceof Moveable && p2 instanceof Blockable){
                    if (((Moveable) p).checkHit((Blockable) p2)){

                        //敌方坦克互相碰撞立马调转方向,即设置坦克方向为撞上方向的反方向
                        if (p instanceof Tank2 && p2 instanceof Tank2) {
                            ((Tank2)p).setDirection(reverDirection(((Tank2)p).getBadDirection()));
                        }
                        //敌方坦克碰到墙/铁/水,立马转弯,即设置坦克的方向为不是撞上方向的随机方向
                        if (p instanceof Tank2 && (p2 instanceof Wall || p2 instanceof Steel || p2 instanceof Water)) {
                            getRandomDirection();
                            if (((Tank2)p).getBadDirection()!=this.direction) {
                                ((Tank2)p).setDirection(this.direction);
                            }
                        }
                        break;
                    }
                }
            }
        }
        // 18.子弹打铁消失
        //	模仿坦克与铁,碰撞检测方法也可以拷贝坦克里面的,
        //  把撞上之前修改坦克的坐标的那一段删掉即可,因为子弹图片非常小,忽略认为相交等于接触
        //	碰撞检测方法返回true,从集合里面移除子弹对象
//        for (Picture p : list) {
//            for (Picture p2 : list) {
//                if (p instanceof Bullet && p2 instanceof Steel){
//                    if (((Bullet) p).checkHit((Steel) p2)){
//                        list.remove(p);
//                        //  19.铁给反应产生爆炸物对象
//                        //	继承图片父类写好爆炸物类,在上面子弹打上铁要消失的下面,
//                        //  通过坦克对象调用方法产生爆炸物对象,添加到图片集合,统一绘制爆炸物图片出来
//                        Blast blast = ((Steel) p2).showBlast();
//                        list.add(blast);
//                        break;
//                    }
//                }
//            }
//        }
        //  23.攻击挨打接口抽取
        //	根据上面的代码,把子弹类替换为攻击接口,把铁类替换为挨打接口,先使用后创建,点选鼠标创建即可
        for (Picture p : list) {
            for (Picture p2 : list) {
                if (p instanceof Attackable && p2 instanceof Hitable){
                    if (((Attackable) p).checkHit((Hitable) p2)){
                        list.remove(p);
                        //  敌方坦克不能打铁
                        if (p instanceof Bullet2 && p2 instanceof Steel){
                            break;
                        }
                        //  19.铁给反应产生爆炸物对象
                        //	继承图片父类写好爆炸物类,在上面子弹打上铁要消失的下面,
                        //  通过坦克对象调用方法产生爆炸物对象,添加到图片集合,统一绘制爆炸物图片出来
                        Blast blast = ((Hitable) p2).showBlast();
                        list.add(blast);

                        //  敌方坦克子弹和我方坦克子弹可以相互抵消,让他们都实现挨打能力,
                        //  但是不能自己打自己的同类,如果发生碰撞,双双从集合里面把子弹对象移除即可
                        //  攻击能力的东西打上任何东西都会移除上面已经做了,如果挨打的东西子弹,也要从集合里面移除,
                        //  即可互相抵消了,不再绘制了
                        if (p2 instanceof Bullet || p2 instanceof Bullet2) {
                            list.remove(p2);
                        }
                        break;
                    }
                }
            }
        }
    }
    //  15.坦克隐藏在草坪里
    //	排序做法:写一个方法1,把图片添加到集合,然后对集合里面的图片进行排序,
    //  定义排序方法2在图片父类,让其他类继承,坦克草重写,越大越后排
    public void addPicture(Picture p){
        list.add(p);
//        list.sort((o1,o2) -> o1.getOrder() - o2.getOrder());
        list.sort(Comparator.comparing(Picture::getOrder));
    }
}
