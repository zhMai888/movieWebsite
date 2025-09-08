## 运行准备
1. 安装 redis
2. 创建新的数据库 `moviw_web` (名字必须相同)
3. 找到路径为：`sql\movie_web.sql` 的文件，将它拖到新创建的数据库中运行即可导入网站运行所需的数据
4. 打开代码所在的文件，打开命令行（最好以管理员的权限打开），运行 `npm install`

## 正式运行
1. 启动 redis
2. 找到路径为：`movie-admin\src\main\java\com\movie\MovieApplication.java` 的文件并运行以启动后端
3. 看到控制台输出：  
   ![image](https://github.com/user-attachments/assets/3994b10d-05a1-435d-a25a-a165413d1d9a)  
   表示后端启动成功！
4. 在命令行进入 `movie-ui` 文件夹下：`cd movie-ui`
5. 启动前端：`npm run dev`  
   ![image](https://github.com/user-attachments/assets/dbcb20f7-f35a-4b76-bb4a-3a974df93590)  
   前端启动后一般会自动打开网站。
