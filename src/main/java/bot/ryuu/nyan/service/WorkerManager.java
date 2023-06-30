package bot.ryuu.nyan.service;

import gui.ava.html.image.generator.HtmlImageGenerator;

import java.awt.image.BufferedImage;

public class WorkerManager {
    public BufferedImage loadImage() {
        String html = "<body><img src=''></body>";

        String html2 = """
                <body>
                        <style>
                            .block {
                                width: 420px;
                                height: 120px;
                                /* overflow: hidden; */
                                border-radius: 60px;
                                position: relative;
                                padding: 10px;
                                box-sizing: border-box;
                                background: rgba(17, 17, 17, 1);
                            }
                
                            #background {
                                width: 100%;
                                height: 100%;
                                object-fit: cover;
                                position: absolute;
                            }
                
                            #filter {
                                position: absolute;
                                width: 100%;
                                height: 100%;
                                background: rgba(10, 10, 10, 0.4);
                                backdrop-filter: blur(25px);
                                border-radius: 60px;
                            }
                
                            #icon {
                                height: 100%;
                                border-radius: 100%;
                                aspect-ratio: 1;
                            }
                        </style>
                        <div class="block">
                            <!-- <img id="background" src="https://media.tenor.com/4bSiJ9SBjyMAAAAC/komi-komi-cant-communicate-season2.gif"> -->
                            <!-- <div id="filter"></div> -->
                            <img id="icon" src="https://media.tenor.com/4bSiJ9SBjyMAAAAC/komi-komi-cant-communicate-season2.gif"/>
                        </div>
                    </body>
                                
                """;

        HtmlImageGenerator hig = new HtmlImageGenerator();
        hig.loadHtml(html2);

        return hig.getBufferedImage();
    }
}
