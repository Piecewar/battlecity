package game;

import utils.DrawUtils;

import java.io.IOException;

//  11.墙水草铁绘制
//	模仿坦克类和子弹类即可
public class Wall extends Picture implements Blockable,Hitable,Destroyedable{
//    private int x;
//    private int y;
//    private int width;
//    private int height;

    public Wall(int x,int y){
//        this.x = x;
//        this.y = y;
        super(x,y);
        try {
            int[] size = DrawUtils.getSize("tankwar\\res\\img\\wall.gif");
            this.width = size[0];
            this.height = size[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void draw(){
        try {
            DrawUtils.draw("tankwar\\res\\img\\wall.gif", x, y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //  24.铁墙销毁
    //  铁或墙都会给反应产生爆炸物对象,在他们的给反应方法里面让血量减减,
    //  提供一个移除方法当血量小于等于0返回true,调用模仿子弹或爆炸物类
    private int blood = 1;
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
