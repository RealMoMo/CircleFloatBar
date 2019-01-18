## CirCleFloatBar
    圆形悬浮窗菜单。(并没有做6.0动态权限申请，下载Demo的apk，请各大神自己手动开启权限)
### Gif
在大屏上，实际项目效果(撸多了，手抖请笑纳)

![image](https://github.com/RealMoMo/CircleFloatBar/blob/master/gif/real_demo.gif)

精简实际项目效果(核心效果逻辑保留)
触摸icon的缩放动画 & 无触摸后定时的透明动画

![image](https://github.com/RealMoMo/CircleFloatBar/blob/master/gif/alpha.gif)

展开与隐藏菜单的位移+缩放动画 & 
贝塞尔曲线位移动画 （通过广播带坐标信息，让CircleFloatBar移动该处）

![image](https://github.com/RealMoMo/CircleFloatBar/blob/master/gif/move_show.gif)

### 项目结构

![image](https://github.com/RealMoMo/CircleFloatBar/blob/master/gif/project.png)


### 版本

* Version:1.1.0 
</br>初次发布
* Version:1.2.0
</br>1.解决快速多次点击展开&隐藏菜单概率性崩溃bug。
</br>2.优化PathAnimation，动态计算控制点。
</br>3.优化逻辑。
* Version:1.3.0
</br>增加无触控，菜单自动吸附到屏幕边缘。
* Version:1.3.1
</br>悬浮窗适配。

### Thanks
	Everyone who has contributed code and reported issues and pull requests!


### License

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.

