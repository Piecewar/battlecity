package game;

import utils.DrawUtils;

import java.io.IOException;

//  11.墙水草铁绘制
//	模仿坦克类和子弹类即可
public class Grass extends Picture{
//    private int x;
//    private int y;
//    private int width;
//    private int height;

    public Grass(int x, int y){
//        this.x = x;
//        this.y = y;
        super(x,y);
        try {
            int[] size = DrawUtils.getSize("tankwar\\res\\img\\grass.gif");
            this.width = size[0];
            this.height = size[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void draw(){
        try {
            DrawUtils.draw("tankwar\\res\\img\\grass.gif", x, y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
