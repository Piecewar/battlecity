package game;

import utils.DrawUtils;

import java.io.IOException;

//  19.铁给反应产生爆炸物对象
//	继承图片父类写好爆炸物类,在上面子弹打上铁要消失的下面,
//  通过坦克对象调用方法产生爆炸物对象,添加到图片集合,统一绘制爆炸物图片出来
public class Blast extends Picture implements Destroyedable{
//    public Blast(Steel steel){
    public Blast(Hitable h,boolean flag){
        Picture p = (Picture) h;
        try {
            int[] size = DrawUtils.getSize("tankwar\\res\\img\\blast_1.gif");
            this.width = size[0];
            this.height = size[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
//        this.x = steel.x - (this.width/2 - steel.width/2);
//        this.y = steel.y - (this.height/2 - steel.height/2);
        this.x = p.x - (this.width/2 - p.width/2);
        this.y = p.y - (this.height/2 - p.height/2);

        if (flag){
            arr = new String[]{"tankwar\\res\\img\\blast_l.gif","tankwar\\res\\img\\blast_2.gif",
                               "tankwar\\res\\img\\blast_3.gif","tankwar\\res\\img\\blast_4.gif"};
        }
    }
    //  21.爆炸物绘制修改
    //	定义数组存储爆炸物8张图片路径,在爆炸物的绘制方法里面不断获取路径,
    //  绘制从1到8的图片,用索引加加的方式,要注意防止索引越界
    //	如果索引大于等于数组的长度,就return不走访问数组元素的逻辑,
    //  也同时结束了绘制方法,这个时候暴露一个布尔类型的标记出去,销毁爆炸物
    String[] arr = {"tankwar\\res\\img\\blast_l.gif","tankwar\\res\\img\\blast_2.gif",
                    "tankwar\\res\\img\\blast_3.gif","tankwar\\res\\img\\blast_4.gif",
                    "tankwar\\res\\img\\blast_5.gif","tankwar\\res\\img\\blast_6.gif",
                    "tankwar\\res\\img\\blast_7.gif","tankwar\\res\\img\\blast_8.gif"};
    private int index;
    private boolean isDestroyed;
    @Override
    public void draw() {
        if (index >= arr.length){
            isDestroyed = true;
            return;
        }
        String path = arr[index];
        index++;
        try {
            DrawUtils.draw(path, x, y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //  22.爆炸物销毁
    //	写一个销毁方法值来自上面暴露的标记,然后在不断刷新方法里面调用,
    //  把爆炸物对象从集合里面移除,不会,可以模仿子弹的销毁
    public boolean isDestroyed(){
        return isDestroyed;
    }

    @Override
    public Blast showBigBlast() {
        return null;
    }
}
