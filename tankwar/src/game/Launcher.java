package game;

/**
     0.准备工作
     新建项目,拷贝jar包,图片,工具类,
     新建测试类,游戏窗体类,调用start方法开启,配置libraries
 */
public class Launcher {
    public static void main(String[] args) {
        GameWindow gw = new GameWindow(Config.TITLE,Config.WIDTH,Config.HEIGHT,Config.FPS);
        gw.start(); // file --> project structure --> libraries
    }
}
